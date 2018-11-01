/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package com.bjzhianjia.scp.security.auth.module.oauth.config;

import com.bjzhianjia.scp.core.constants.CommonConstants;
import com.bjzhianjia.scp.core.util.RsaKeyHelper;
import com.bjzhianjia.scp.security.auth.configuration.KeyConfiguration;
import com.bjzhianjia.scp.security.auth.constant.RedisKeyConstant;
import com.bjzhianjia.scp.security.auth.jwt.AECUtil;
import com.bjzhianjia.scp.security.auth.jwt.user.JwtTokenUtil;
import com.bjzhianjia.scp.security.auth.module.oauth.bean.OauthUser;
import com.bjzhianjia.scp.security.auth.module.oauth.service.OauthUserDetailsService;
import com.bjzhianjia.scp.security.common.util.Sha256PasswordEncoder;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.sql.DataSource;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class OAuthSecurityConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager auth;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private RsaKeyHelper rsaKeyHelper;
    @Autowired
    private AECUtil aecUtil;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private KeyConfiguration keyConfiguration;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    @Autowired
    private TokenStore tokenStore;


    @Bean
    public RedisTokenStore redisTokenStore(){
        RedisTokenStore redisTokenStore = new RedisTokenStore(redisConnectionFactory);
        redisTokenStore.setPrefix("AG:OAUTH:");
        return redisTokenStore;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security)
            throws Exception {
        security
                .tokenKeyAccess("permitAll()");
        security.checkTokenAccess("isAuthenticated()");
        //需要更换成加密模式
        security.passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints)
            throws Exception {
        endpoints
                .authenticationManager(auth)
                .tokenStore(redisTokenStore()).accessTokenConverter(accessTokenConverter())

                /*
                 * 在刷新token时，refresh_tokenbn也会重新生成
                 * 如果reuseRefreshTokens为true，则重生成的refresh_token新不会被保存到redis中
                 * 需要在该实现里将reuseRefreshTokens设置为false，以将原refresh_token从redis中移除，
                 * 将生成的新refresh_token保存到redis中
                 */
//        .reuseRefreshTokens(false)
        ;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients)
            throws Exception {
        //需要更换成加密模式
        clients.jdbc(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Configuration
    @Order(100)
    protected static class AuthenticationManagerConfiguration extends GlobalAuthenticationConfigurerAdapter {
        @Autowired
        private OauthUserDetailsService oauthUserDetailsService;

        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(oauthUserDetailsService).passwordEncoder(new Sha256PasswordEncoder());
        }
    }



    @Bean
    public JwtAccessTokenConverter accessTokenConverter() throws IOException, InvalidKeyException, NoSuchAlgorithmException {
        byte[] pri,pub = null;
        try {
            pri = rsaKeyHelper.toBytes(aecUtil.decrypt(redisTemplate.opsForValue().get(RedisKeyConstant.REDIS_USER_PRI_KEY).toString()));
            pub = rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(RedisKeyConstant.REDIS_USER_PUB_KEY).toString());
        }catch(Exception e){
            log.error("初始化用户认证公钥/密钥异常...",e);
            Map<String, byte[]> keyMap = rsaKeyHelper.generateKey(keyConfiguration.getUserSecret());
            redisTemplate.opsForValue().set(RedisKeyConstant.REDIS_USER_PRI_KEY, aecUtil.encrypt(rsaKeyHelper.toHexString(keyMap.get("pri"))));
            redisTemplate.opsForValue().set(RedisKeyConstant.REDIS_USER_PUB_KEY, rsaKeyHelper.toHexString(keyMap.get("pub")));
            pri = keyMap.get("pri");
            pub = keyMap.get("pub");
        }
        JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter() {
            /***
             * 重写增强token方法,用于自定义一些token返回的信息
             */
            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                OauthUser user = (OauthUser) authentication.getUserAuthentication().getPrincipal();// 与登录时候放进去的UserDetail实现类一直查看link{SecurityConfiguration}
                /** 自定义一些token属性 ***/
                final Map<String, Object> additionalInformation = new HashMap<>();
                Date expireTime = DateTime.now().plusSeconds(jwtTokenUtil.getExpire()).toDate();
                additionalInformation.put(CommonConstants.JWT_KEY_EXPIRE, expireTime);
                additionalInformation.put(CommonConstants.JWT_KEY_USER_ID, user.getId());
                additionalInformation.put(CommonConstants.JWT_KEY_TENANT_ID, user.getTenantId());
                additionalInformation.put(CommonConstants.JWT_KEY_DEPART_ID, user.getDepartId());
                additionalInformation.put(CommonConstants.JWT_KEY_NAME, user.getName());
                additionalInformation.put("sub", user.getUsername());
                ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInformation);
                OAuth2AccessToken enhancedToken = super.enhance(accessToken, authentication);

                 /*
                  * ==by尚==开始==
                  * 当进行刷新accessToken操作时，会生成一个与原refreshToken(O_refreshToken)过期时间相同，
                  * jti不同的新refreshToken(N_refreshToken)，该新refreshToken不会被保存到tokenStore中去，
                  * 因此无法使用该tokenStore再次进行刷新accessToken操作
                  *
                  * 以下代码作用为，当tokenStore中存在refreshToken时(该refreshToken即为O_refreshToken)，即用户进行了登陆，且
                  * accessToken未过期时，用该refreshToken替换enhancedToken对象中的新refreshToken(N_refreshToken),
                  * 如果满足了“用户进行了登陆，且accessToken未过期”的条件，只有刷新accessToken操作才会执行到这里，
                  * 因为可以认为以下逻辑代码针对于刷新accessToken操作
                  *
                  * 这样做可以使用达到以下目的：
                  * 当用户进行刷新accessToken操作时，请求到的refreshToken永远为同一个字符序列的refreshToken
                  */
                OAuth2RefreshToken refreshToken = tokenStore.readRefreshToken(accessToken.getRefreshToken().getValue());
                if (refreshToken != null) {
                    if(enhancedToken instanceof DefaultOAuth2AccessToken){
                        ((DefaultOAuth2AccessToken) enhancedToken).setRefreshToken(refreshToken);
                    }
                }
                return enhancedToken;
            }

        };
        accessTokenConverter.setKeyPair( new KeyPair(new RSAPublicKeyImpl(pub), RSAPrivateCrtKeyImpl.newKey(pri)));
        return accessTokenConverter;
    }
}