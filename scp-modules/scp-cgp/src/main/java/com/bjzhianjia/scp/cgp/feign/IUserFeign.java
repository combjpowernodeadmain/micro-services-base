package com.bjzhianjia.scp.cgp.feign;

import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;

/**
 * @author scp
 * @create 2018/2/11.
 */
@FeignClient(value = "scp-admin",configuration = FeignApplyConfiguration.class)
public interface IUserFeign {
    /**
     * 获取当前用户授权的部门数据权限Id列表
     * @return
     */
    @RequestMapping(value="/user/dataDepart",method = RequestMethod.GET)
    List<String> getUserDataDepartIds(@RequestParam("userId") String userId);
    
    @RequestMapping(value = "user/getByName", method = RequestMethod.GET)
    public JSONArray getUsersByFakeName(@RequestParam(value = "name") String name);
    
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
     * 通过用户ids查询
     * @param userIds 用户ids
     * @return
     */
    @RequestMapping(value="/user/ids",method=RequestMethod.GET)
    public JSONArray getByUserIds(@RequestParam(value="userIds") String userIds) ;
}
