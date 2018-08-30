package com.bjzhianjia.scp.cgp.rest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.WritsTemplatesBiz;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("writsTemplates")
@CheckClientToken
@CheckUserToken
@Api(tags="综合执法 - 案件登记  文书模板")
public class WritsTemplatesController extends BaseController<WritsTemplatesBiz,WritsTemplates,Integer> {

	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ApiOperation("添加单个对象")
	public ObjectRestResponse<WritsTemplates> add(@RequestBody @ApiParam(name="待添加对象实例") @Validated WritsTemplates writsTemplates,
			BindingResult bindingResult){
		ObjectRestResponse<WritsTemplates> restResult=new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			restResult.setStatus(400);
			return restResult;
		}
		Result<Void> result = this.baseBiz.created(writsTemplates);
		if(!result.getIsSuccess()) {
			restResult.setMessage(result.getMessage());
			restResult.setStatus(400);
			return restResult;
		}
		
		restResult.setMessage("成功");
		return restResult;
	}
}