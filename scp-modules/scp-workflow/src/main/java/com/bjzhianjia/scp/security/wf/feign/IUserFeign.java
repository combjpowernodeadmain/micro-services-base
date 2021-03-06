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

package com.bjzhianjia.scp.security.wf.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;

/**
 * @author scp
 * @create 2018/2/11.
 */
@FeignClient(value = "scp-admin",configuration = FeignApplyConfiguration.class)
public interface IUserFeign {
    /**
     * 获取当前用户工作流的岗位Id列表
     * @return
     */
    @RequestMapping(value="/user/flowPosition",method = RequestMethod.GET)
    List<String> getUserFlowPositions(@RequestParam("userId") String userId);
    

    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public ObjectRestResponse getUserInfo(String username);
    
    /**
     * 根据用户名获取其部门ID
     * 
     * @param username
     * @return
     */
    @RequestMapping(value = "/api/getDepartIdByUserId", method = RequestMethod.GET)
    public String getDepartIdByUserId(@RequestParam(value = "userid") String userid);
    
    /**
     * 根据用户名获取其租户tenantId
     * 
     * @param username
     * @return
     */
    @RequestMapping(value = "/api/getTenantIdByUserId", method = RequestMethod.GET)
    public String getTenantIdByUserId(@RequestParam(value = "userid") String userid);
    
    /**
     * 根据用户id 获取角色/分组codes
     * 
     * @param userid
     * @return
     */
    @RequestMapping(value = "/api/getGroupCodesdByUserId", method = RequestMethod.GET)
    public List<String> getGroupCodesByUserId(@RequestParam(value = "userid") String userid);
    
    /**
     * 获取用户可管辖部门id列表
     * 
     * @param userId
     * @return
     */
    @RequestMapping(value = "/dataDepart", method = RequestMethod.GET)
    public List<String> getUserDataDepartIds(String userId);
}
