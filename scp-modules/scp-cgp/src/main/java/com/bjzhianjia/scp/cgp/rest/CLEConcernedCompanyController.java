package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;

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
@Api(tags="综合执法 - 案件登记当事人(单位)")
public class CLEConcernedCompanyController extends BaseController<ConcernedCompanyBiz,ConcernedCompany,Integer> {

}