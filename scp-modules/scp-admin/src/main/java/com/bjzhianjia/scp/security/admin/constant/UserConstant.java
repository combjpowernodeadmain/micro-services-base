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

package com.bjzhianjia.scp.security.admin.constant;

/**
 * @author scp
 * @version 1.0
 */
public class UserConstant {
    /**
     * 密码腌制
     */
    public static int PW_ENCORDER_SALT = 15;

    /**
     * 已被删除 - 标识
     *
     * 用来标识数据库中的数据是否被删除
     */
    public static final String IS_DELETED_FLAG = "1";

    /**
     * 未被删除 - 标识
     *
     * 用来标识数据库中的数据是否被删除
     */
    public static final String IS_UNDELETED_FLAG = "0";
}
