package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("caseInfo")
@CheckClientToken
@CheckUserToken
public class CaseInfoController extends BaseController<CaseInfoBiz,CaseInfo,Integer> {

}