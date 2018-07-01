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

package com.bjzhianjia.scp.security.admin.rest;

import com.bjzhianjia.scp.security.admin.biz.TenantBiz;
import com.bjzhianjia.scp.security.admin.biz.UserBiz;
import com.bjzhianjia.scp.security.admin.entity.Tenant;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 租户管理
 * @author scp
 */
@RestController
@RequestMapping("tenant")
@CheckClientToken
@CheckUserToken
@Api(tags = "租户模块")
public class TenantController extends BaseController<TenantBiz,Tenant,String> {
    @Autowired
    private UserBiz userBiz;
    @ApiOperation("租户授予用户")
    @RequestMapping(value = "/{id}/user",method = RequestMethod.PUT)
    public ObjectRestResponse<Boolean> updateUser(@PathVariable("id") String id, String userId){
        baseBiz.updateUser(id,userId);
        return new ObjectRestResponse<>();
    }

    @ApiOperation("获取租户授予用户")
    @RequestMapping(value = "/{id}/user",method = RequestMethod.GET)
    public ObjectRestResponse<User> updateUser(@PathVariable("id") String id){
        Tenant tenant = baseBiz.selectById(id);
        return new ObjectRestResponse<User>().data(userBiz.selectById(tenant.getOwner()));
    }
}