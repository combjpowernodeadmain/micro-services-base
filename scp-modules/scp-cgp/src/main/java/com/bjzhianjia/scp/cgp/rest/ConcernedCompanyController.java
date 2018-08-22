package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;

import com.bjzhianjia.scp.cgp.biz.ConcernedCompanyBiz;
import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

/**
 * 当事人（企业）前端控制器
 * @author chenshuai
 *
 */
@RestController
@RequestMapping("concernedCompany")
@CheckClientToken
@CheckUserToken
@Api(tags="当事人（企业）")
public class ConcernedCompanyController extends BaseController<ConcernedCompanyBiz,ConcernedCompany,Integer> {

}