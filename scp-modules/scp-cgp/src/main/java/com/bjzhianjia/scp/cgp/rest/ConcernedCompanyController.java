package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;

import com.bjzhianjia.scp.cgp.biz.ConcernedCompanyBiz;
import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/one/{id}")
    @ApiOperation("获取单个对象")
    public ObjectRestResponse<ConcernedCompany> getOne(
        @PathVariable(value = "id") @ApiParam(value = "待查询当事人(单位)ID") Integer id) {
        return this.baseBiz.getOne(id);
    }

}