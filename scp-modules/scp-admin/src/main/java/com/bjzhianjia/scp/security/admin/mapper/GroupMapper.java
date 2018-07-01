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

import com.bjzhianjia.scp.security.admin.entity.Group;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

import org.apache.ibatis.annotations.Param;
@Tenant()
public interface GroupMapper extends CommonMapper<Group> {
    public void deleteGroupMembersById (@Param("groupId") String groupId);
    public void deleteGroupLeadersById (@Param("groupId") String groupId);
    public void insertGroupMembersById (@Param("id") String id,@Param("groupId") String groupId,@Param("userId") String userId,@Param("tenantId") String tenantId);
    public void insertGroupLeadersById (@Param("id") String id,@Param("groupId") String groupId,@Param("userId") String userId,@Param("tenantId") String tenantId);
}
