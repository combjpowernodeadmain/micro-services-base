package com.bjzhianjia.scp.security.admin.rest;

import com.bjzhianjia.scp.security.admin.service.JudicialUserService;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("judicialUser")
@CheckUserToken
@CheckClientToken
@Api(tags = "司法用户模块")
public class JudicialUserController {

    @Autowired
    private JudicialUserService judicialUserService;

    /**
     * 获取案卷登记获取处理部门id
     *
     * @param departId 当前所属部门id
     * @param groupId 角色id
     * @param professional 专业id（数据字典code）
     * @param isOwn 是否包含当前部门。（1 包含|0不包含）
     * @return
     *         -1 没有可以处理的部门
     */
    @ApiOperation("获取案卷登记获取处理部门id")
    @GetMapping("/record/caseDeal")
    public String getCaseDeal(@RequestParam(value = "departId",defaultValue = "") @ApiParam("当前所属部门id") String departId,
                              @RequestParam(value = "groupId",defaultValue = "") @ApiParam("角色id") String groupId,
                              @RequestParam(value = "professional",defaultValue = "") @ApiParam("专业id") String professional,
                              @RequestParam(value = "isOwn",defaultValue = "") @ApiParam("是否包含当前部门") Integer isOwn) {

        return judicialUserService.getCaseDeal(departId,groupId,professional,isOwn);
    }
}
