package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("caseRegistration")
@CheckClientToken
@CheckUserToken
public class CaseRegistrationController extends BaseController<CaseRegistrationBiz,CaseRegistration,String> {

}