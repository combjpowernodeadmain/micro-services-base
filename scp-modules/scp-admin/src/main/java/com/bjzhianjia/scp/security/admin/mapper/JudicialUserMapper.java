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


/**
 * JudicialUserController 司法用户.
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/9/29          admin      1.0            ADD
 * </pre>
 *
 * @author admin
 * @version 1.0
 */
@Tenant()
public interface JudicialUserMapper extends CommonMapper<User> {

    /**
     * 获取分案人总数
     *
     * @param departId 部门id
     * @param groupId  角色id
     * @param professional 专业id
     * @return
     */
    Integer selectUserByDepartAndGroup(@Param("departId") String departId, @Param("groupId") String groupId,
                                       @Param("professional") String professional);
}
