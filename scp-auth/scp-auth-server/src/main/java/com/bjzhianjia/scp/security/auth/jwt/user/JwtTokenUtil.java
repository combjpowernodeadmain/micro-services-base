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

package com.bjzhianjia.scp.security.auth.jwt.user;

import com.bjzhianjia.scp.core.util.jwt.IJWTInfo;
import com.bjzhianjia.scp.core.util.jwt.JWTHelper;
import com.bjzhianjia.scp.security.auth.configuration.KeyConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * @author scp
 * @version 1.0
 */
@Component
public class JwtTokenUtil {

    public int getExpire() {
        return expire;
    }

    @Value("${jwt.expire}")
    private int expire;

    @Autowired
    private KeyConfiguration keyConfiguration;

    @Autowired
    private JWTHelper jwtHelper;

    public String generateToken(IJWTInfo jwtInfo, Map<String, String> otherInfo,Date expireTime) throws Exception {
        return jwtHelper.generateToken(jwtInfo, keyConfiguration.getUserPriKey(), expireTime, otherInfo);
    }

    public IJWTInfo getInfoFromToken(String token) throws Exception {
        IJWTInfo infoFromToken = jwtHelper.getInfoFromToken(token, keyConfiguration.getUserPubKey());
        return infoFromToken;
    }



}
