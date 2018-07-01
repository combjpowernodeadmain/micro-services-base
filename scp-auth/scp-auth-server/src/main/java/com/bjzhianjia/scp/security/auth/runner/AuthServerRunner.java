/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.bjzhianjia.scp.security.auth.runner;

import com.alibaba.fastjson.JSON;
import com.bjzhianjia.scp.core.util.RsaKeyHelper;
import com.bjzhianjia.scp.security.auth.configuration.KeyConfiguration;
import com.bjzhianjia.scp.security.auth.constant.RedisKeyConstant;
import com.bjzhianjia.scp.security.auth.jwt.AECUtil;
import com.bjzhianjia.scp.security.auth.module.client.biz.GatewayRouteBiz;
import com.bjzhianjia.scp.security.auth.module.client.entity.GatewayRoute;
import com.bjzhianjia.scp.security.common.constant.RedisKeyConstants;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author scp
 * @version 1.0
 */
@Configuration
@Slf4j
public class AuthServerRunner implements CommandLineRunner {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private KeyConfiguration keyConfiguration;

    @Autowired
    private AECUtil aecUtil;

    @Autowired
    private RsaKeyHelper rsaKeyHelper;

    @Autowired
    private GatewayRouteBiz gatewayRouteBiz;

    @Override
    public void run(String... args) throws Exception {
        boolean flag = false;
        if (redisTemplate.hasKey(RedisKeyConstant.REDIS_USER_PRI_KEY)&&redisTemplate.hasKey(RedisKeyConstant.REDIS_USER_PUB_KEY)){
            try {
                keyConfiguration.setUserPriKey(rsaKeyHelper.toBytes(aecUtil.decrypt(redisTemplate.opsForValue().get(RedisKeyConstant.REDIS_USER_PRI_KEY).toString())));
                keyConfiguration.setUserPubKey(rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(RedisKeyConstant.REDIS_USER_PUB_KEY).toString()));
            }catch (Exception e){
                log.error("初始化用户公钥/密钥异常...",e);
                flag = true;
            }
        }
        if(flag){
            Map<String, byte[]> keyMap = rsaKeyHelper.generateKey(keyConfiguration.getUserSecret());
            keyConfiguration.setUserPriKey(keyMap.get("pri"));
            keyConfiguration.setUserPubKey(keyMap.get("pub"));
            redisTemplate.opsForValue().set(RedisKeyConstant.REDIS_USER_PRI_KEY, aecUtil.encrypt(rsaKeyHelper.toHexString(keyMap.get("pri"))));
            redisTemplate.opsForValue().set(RedisKeyConstant.REDIS_USER_PUB_KEY, rsaKeyHelper.toHexString(keyMap.get("pub")));
        }
        log.info("完成用户公钥/密钥的初始化...");
        flag = false;
        if (redisTemplate.hasKey(RedisKeyConstant.REDIS_SERVICE_PRI_KEY)&&redisTemplate.hasKey(RedisKeyConstant.REDIS_SERVICE_PUB_KEY)) {
            try {
                keyConfiguration.setServicePriKey(rsaKeyHelper.toBytes(aecUtil.decrypt(redisTemplate.opsForValue().get(RedisKeyConstant.REDIS_SERVICE_PRI_KEY).toString())));
                keyConfiguration.setServicePubKey(rsaKeyHelper.toBytes(redisTemplate.opsForValue().get(RedisKeyConstant.REDIS_SERVICE_PUB_KEY).toString()));
            }catch (Exception e){
                log.error("初始化服务公钥/密钥异常...",e);
                flag = true;
            }
        } else {
            flag = true;
        }
        if(flag){
            Map<String, byte[]> keyMap = rsaKeyHelper.generateKey(keyConfiguration.getServiceSecret());
            keyConfiguration.setServicePriKey(keyMap.get("pri"));
            keyConfiguration.setServicePubKey(keyMap.get("pub"));
            redisTemplate.opsForValue().set(RedisKeyConstant.REDIS_SERVICE_PRI_KEY, aecUtil.encrypt(rsaKeyHelper.toHexString(keyMap.get("pri"))));
            redisTemplate.opsForValue().set(RedisKeyConstant.REDIS_SERVICE_PUB_KEY, rsaKeyHelper.toHexString(keyMap.get("pub")));
        }
        log.info("完成服务公钥/密钥的初始化...");
        List<GatewayRoute> gatewayRoutes = gatewayRouteBiz.selectListAll();
        redisTemplate.opsForValue().set(RedisKeyConstants.ZUUL_ROUTE_KEY, JSON.toJSONString(gatewayRoutes));
        log.info("完成网关路由的更新...");
    }
}
