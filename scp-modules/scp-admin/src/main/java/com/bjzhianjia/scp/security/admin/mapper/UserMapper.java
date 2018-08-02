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

package com.bjzhianjia.scp.security.admin.mapper;

import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
@Tenant()
public interface UserMapper extends CommonMapper<User> {
    public List<User> selectMemberByGroupId(@Param("groupId") String groupId);
    public List<User> selectLeaderByGroupId(@Param("groupId") String groupId);
    List<String> selectUserDataDepartIds(String userId);
    /**
     * 根据用户名获取用户部门ID
     * 
     * added by bozch 2018.7.31
     * 
     * @param usernmae
     * @return
     */
    public String selectDepartIdByUsername(@Param("username") String username);
    
    /**
     * 根据用户名获取用户租户ID
     * 
     * added by bozch 2018.7.31
     * 
     * @param usernmae
     * @return
     */
    public String selectTenantIdByUsername(@Param("username") String username);
}
