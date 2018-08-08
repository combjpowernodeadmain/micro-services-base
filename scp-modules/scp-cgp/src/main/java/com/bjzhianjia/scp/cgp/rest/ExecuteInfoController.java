package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.ExecuteInfoBiz;
import com.bjzhianjia.scp.cgp.entity.ExecuteInfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("executeInfo")
@CheckClientToken
@CheckUserToken
public class ExecuteInfoController extends BaseController<ExecuteInfoBiz,ExecuteInfo,Integer> {

}