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

package com.bjzhianjia.scp.security.auth.jwt.client;

import com.bjzhianjia.scp.core.util.jwt.IJWTInfo;
import com.bjzhianjia.scp.core.util.jwt.JWTHelper;
import com.bjzhianjia.scp.security.auth.configuration.KeyConfiguration;
import com.bjzhianjia.scp.security.common.exception.auth.ClientTokenException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;

/**
 * Created by scp on 2017/9/10.
 */
@Configuration
public class ClientTokenUtil {
    private Logger logger = LoggerFactory.getLogger(ClientTokenUtil.class);
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${client.expire}")
    private int expire;
    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private KeyConfiguration keyConfiguration;

    public String generateToken(IJWTInfo jwtInfo) throws Exception {
        return jwtHelper.generateToken(jwtInfo, keyConfiguration.getServicePriKey(), expire);
    }

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        IJWTInfo infoFromToken = jwtHelper.getInfoFromToken(token, keyConfiguration.getServicePubKey());
        Date current = infoFromToken.getExpireTime();
        if(new Date().after(current)){
            throw new ClientTokenException("Client token expired!");
        }
        return infoFromToken;
    }

}
