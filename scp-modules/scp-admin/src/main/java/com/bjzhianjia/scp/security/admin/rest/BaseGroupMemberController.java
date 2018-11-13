package com.bjzhianjia.scp.security.admin.rest;

import com.bjzhianjia.scp.security.admin.biz.BaseGroupMemberBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * BaseGroupMember 用户角色(成员)关系.
 *
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018年10月14日          bo      chenshuai            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
@RestController
@RequestMapping("baseGroupMember")
@CheckClientToken
@CheckUserToken
@Api(tags = "用户角色(成员)关系")
public class BaseGroupMemberController {
    @Autowired
    private BaseGroupMemberBiz baseGroupMemberBiz;

    @GetMapping("/group/ids/{userId}")
    public List<String> getGroupIdUserId(@PathVariable("userId") String userId) {
        return baseGroupMemberBiz.getGroupIdUserId(userId);
    }

    /**
     * 获取指定用户，所有成员角色列表
     * @return
     */
    @ApiOperation("获取指定用户，所有成员角色")
    @GetMapping("/userId/{userId}")
    public ObjectRestResponse<List<Map<String, Object>>> getNameByUserId(@PathVariable("userId") String userId) {
        ObjectRestResponse<List<Map<String, Object>>> result = new ObjectRestResponse<>();
        result.setData(baseGroupMemberBiz.getNameByUserId(userId));
        return result;
    }


    /**
     * 更新指定用户的成员角色
     * @return
     */
    @ApiOperation("更新指定用户的成员角色")
    @PutMapping("/userId/{userId}")
    public ObjectRestResponse<Void> update(@PathVariable("userId") String userId,
                                           @RequestParam(value = "groupIds",defaultValue = "") String groupIds) {
        ObjectRestResponse<Void> result = new ObjectRestResponse<>();
        try {
            baseGroupMemberBiz.update(userId, groupIds);
        }catch (Exception e){
            result.setMessage(e.getMessage());
            result.setStatus(400);
        }
        return result;
    }
}