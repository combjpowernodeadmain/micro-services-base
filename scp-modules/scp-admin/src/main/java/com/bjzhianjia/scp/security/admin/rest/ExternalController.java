package com.bjzhianjia.scp.security.admin.rest;

import com.alibaba.fastjson.JSONArray;
import com.bjzhianjia.scp.security.admin.biz.DepartBiz;
import com.bjzhianjia.scp.security.admin.biz.UserBiz;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * ExternalController 对外提供的接口.
 * <p>
 * 只保留client token验证，取消user token验证
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/11/28          admin      1.0            ADD
 * </pre>
 *
 * @author admin
 * @version 1.0
 */
@CheckClientToken
@RestController
@RequestMapping("external")
@Api(tags = "对外提供的接口类")
public class ExternalController {

    @Autowired
    private UserBiz userBiz;

    @Autowired
    private DepartBiz departBiz;

    /**
     * 获取用户详情，包括部门及岗位
     *
     * @param userIds 用户Ids
     * @return
     */
    @RequestMapping(value = "/user/info", method = RequestMethod.GET)
    public JSONArray getInfoByUserIds(@RequestParam("userIds") @ApiParam("用户ids") String userIds) {
        return userBiz.getUserDetail(userIds);
    }

    /**
     * 批量获取人员信息
     *
     * @param userIds 用户Ids
     * @return
     */
    @RequestMapping(value = "/userIds", method = RequestMethod.GET)
    public Map<String, String> getUser(@RequestParam("userIds") String userIds) {
        return userBiz.getUsers(userIds);
    }

    /**
     * 通过部门ids获取部门信息
     *
     * @param deptIds 部门ids
     * @return
     */
    @RequestMapping(value = "/deptIds", method = RequestMethod.GET)
    public Map<String, String> getDepartByDeptIds(@RequestParam("deptIds") String deptIds) {
        return departBiz.getDeparts(deptIds);
    }
}
