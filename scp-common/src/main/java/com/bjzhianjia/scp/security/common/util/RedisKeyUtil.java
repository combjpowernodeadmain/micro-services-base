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

package com.bjzhianjia.scp.security.common.util;

import com.bjzhianjia.scp.core.constants.CommonConstants;
import com.bjzhianjia.scp.security.common.constant.RedisKeyConstants;

import java.util.Date;

/**
 * @author scp
 * @create 2018/3/11.
 */
public class RedisKeyUtil {
    /**
     *
     * @param userId
     * @param expire
     * @return
     */
    public static String buildUserAbleKey(String userId,Date expire){
        return CommonConstants.REDIS_USER_TOKEN + RedisKeyConstants.USER_ABLE + userId + ":" + expire.getTime();
    }

    /**
     * 
     * @param userId
     * @param expire
     * @return
     */
    public static String buildUserDisableKey(String userId,Date expire){
        return CommonConstants.REDIS_USER_TOKEN + RedisKeyConstants.USER_DISABLE + userId + ":" + expire.getTime();
    }
}
