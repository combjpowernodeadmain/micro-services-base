package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.Result;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("caseRegistration")
@CheckClientToken
@CheckUserToken
@Api(tags="综合执法 - 案件登记")
public class CaseRegistrationController extends BaseController<CaseRegistrationBiz,CaseRegistration,String> {

	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ApiOperation("业务--添加单个对象")
	public ObjectRestResponse<Void> addCase(@RequestBody @ApiParam(name="待添加对象实例") @Validated JSONObject caseRegJObj) {
		ObjectRestResponse<Void> restResult=new ObjectRestResponse<>();
		
		Result<Void> result = this.baseBiz.addCase(caseRegJObj);
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		
		restResult.setMessage("成功");
		return restResult;
	}
}