package com.bjzhianjia.scp.security.admin.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

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
public class BaseGroupMemberController{

}