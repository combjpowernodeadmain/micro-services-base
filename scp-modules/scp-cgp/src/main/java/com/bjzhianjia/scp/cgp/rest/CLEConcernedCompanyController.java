package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.ConcernedCompanyBiz;
import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("cleconcernedCompany")
@CheckClientToken
@CheckUserToken
public class CLEConcernedCompanyController extends BaseController<ConcernedCompanyBiz,ConcernedCompany,Integer> {

}