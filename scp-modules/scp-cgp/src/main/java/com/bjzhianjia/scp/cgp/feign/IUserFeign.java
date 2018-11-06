package com.bjzhianjia.scp.cgp.feign;


import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import org.springframework.cloud.netflix.feign.FeignClient;

import com.alibaba.fastjson.JSONArray;
import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

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

    /**
     * 获取通讯录用户列表
     * @param userName
     * @param deptIds
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value="/user/phoneList")
    TableResultResponse<Map<String,Object>> phoneList(
            @RequestParam(value = "userName") String userName,@RequestParam(value ="deptIds") String deptIds,
            @RequestParam(value="page") Integer page,@RequestParam(value="limit") Integer limit);

    /**
     * 获取用户基本信息
     * @param userId 用户Id
     * @return
     */
    @GetMapping(value="/user/{userId}/info")
    Map<String,Object> getUserByUserId(@PathVariable(value="userId") String userId);
}
