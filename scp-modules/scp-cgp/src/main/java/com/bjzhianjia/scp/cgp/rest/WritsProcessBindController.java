package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;

import com.bjzhianjia.scp.cgp.biz.WritsProcessBindBiz;
import com.bjzhianjia.scp.cgp.entity.WritsProcessBind;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("writsProcessBind")
@CheckClientToken
@CheckUserToken
@Api(tags="综合执法 - 案件登记 文书与流程绑定")
public class WritsProcessBindController extends BaseController<WritsProcessBindBiz,WritsProcessBind,Integer> {

}