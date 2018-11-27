package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.cgp.service.PhoneListService;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


/**
 * PhoneListController 通讯录接口.
 * <p>
 * ${tags}
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/11/5          admin      1.0            ADD
 * </pre>
 *
 * @author admin
 * @version 1.0
 */
@RestController
@RequestMapping("phoneList")
@CheckClientToken
@CheckUserToken
@Api(tags = "通讯录")
public class PhoneListController {

    @Autowired
    private PhoneListService phoneListService;

    /**
     * 获取通讯录用户列表
     *
     * @param userName 用户名称
     * @param deptIds  部门ids
     * @param page     页码
     * @param limit    页容量
     * @return
     */
    @ApiOperation(value = "执法人员")
    @GetMapping
    public TableResultResponse<Map<String, Object>> phoneList(
            @RequestParam(value = "userName", defaultValue = "") @ApiParam("用户名称") String userName,
            @RequestParam(value = "deptIds", defaultValue = "") @ApiParam("部门ids") String deptIds,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit) {
        return phoneListService.phoneList(userName, deptIds, page, limit);
    }

    /**
     * 通讯录用户详情
     *
     * @param userId 用户id
     * @return
     */
    @ApiOperation(value = "通讯录用户详情")
    @GetMapping("/userId/{userId}")
    public ObjectRestResponse<Map<String, Object>> userInfo(@PathVariable("userId") @ApiParam("用户id") String userId) {
        ObjectRestResponse<Map<String, Object>> result = new ObjectRestResponse<>();
        result.setData(phoneListService.userInfo(userId));
        return result;
    }
}
