package com.bjzhianjia.scp.security.admin.rest;

import com.bjzhianjia.scp.security.admin.biz.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.util.*;

/**
 * BaseGroupMember 用户角色关系.
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
@Api(tags = "用户角色关系")
public class BaseGroupMemberController {
    @Autowired
    private BaseGroupMemberBiz baseGroupMemberBiz;

    @GetMapping("/group/ids/{userId}")
    public List<String> getGroupIdUserId(@PathVariable("userId") String userId) {
        return baseGroupMemberBiz.getGroupIdUserId(userId);
    }
}