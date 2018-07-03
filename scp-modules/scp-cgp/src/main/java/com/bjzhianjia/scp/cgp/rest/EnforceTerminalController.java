package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.EnforceTerminalBiz;
import com.bjzhianjia.scp.cgp.entity.EnforceTerminal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("enforceTerminal")
@CheckClientToken
@CheckUserToken
public class EnforceTerminalController extends BaseController<EnforceTerminalBiz,EnforceTerminal,Integer> {

}