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

package com.bjzhianjia.scp.security.admin.rpc;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ace.cache.annotation.Cache;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.admin.biz.UserBiz;
import com.bjzhianjia.scp.security.admin.rpc.service.PermissionService;
import com.bjzhianjia.scp.security.api.vo.authority.PermissionInfo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

/**
 * ${DESCRIPTION}
 *
 * @author scp
 * @version 1.0 
 */
@RestController
@RequestMapping("api")
@CheckUserToken
@CheckClientToken
public class UserRest {
    @Autowired
    private PermissionService permissionService;
    
    @Autowired
    private UserBiz userBiz;

    @Cache(key="permission")
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public @ResponseBody
    List<PermissionInfo> getAllPermission(){
        return permissionService.getAllPermission();
    }

    @RequestMapping(value = "/user/permissions", method = RequestMethod.GET)
    public @ResponseBody List<PermissionInfo> getPermissionByUsername(){
        String username = BaseContextHandler.getUsername();
        return permissionService.getPermissionByUsername(username);
    }


    /**
     * 根据username查询用户部门ID
     * 
     * @param name
     * @return
     */
    @RequestMapping(value = "/getDepartIdByUserId", method = RequestMethod.GET)
    public String getDepartIdByUserame(@RequestParam(value = "userid") String userid) {
        String departId = userBiz.getDepartIdByUserId(userid);
        if (StringUtils.isEmpty(departId)) {
            return StringUtils.EMPTY;
        } else {
            return departId;
        }
    }
    
    
    /**
     * 根据username查询tenantID
     * 
     * @param name
     * @return
     */
    @RequestMapping(value = "/getTenantIdByUserId", method = RequestMethod.GET)
    public String getTenantIdByUserId(@RequestParam(value = "userid") String userid) {
        String tenantId = userBiz.getTenantIdByUserId(userid);
        if (StringUtils.isEmpty(tenantId)) {
            return StringUtils.EMPTY;
        } else {
            return tenantId;
        }
    }
    
    
    /**
     * 根据用户id 获取角色/分组codes
     * 
     * @param userid
     * @return
     */
    @RequestMapping(value = "/getGroupCodesdByUserId", method = RequestMethod.GET)
    public List<String> getGroupCodesByUserId(@RequestParam(value = "userid") String userid) {
        List<String> groupCodes = userBiz.getGroupCodesByUserId(userid);
        if (groupCodes.isEmpty()) {
            return Collections.emptyList();
        } else {
            return groupCodes;
        }
    }

}
