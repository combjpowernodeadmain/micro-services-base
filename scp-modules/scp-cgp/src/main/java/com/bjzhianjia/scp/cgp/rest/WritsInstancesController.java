package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.WritsInstancesBiz;
import com.bjzhianjia.scp.cgp.entity.WritsInstances;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("writsInstances")
@CheckClientToken
@CheckUserToken
@Api(tags = "综合执法 - 案件登记文书记录")
public class WritsInstancesController extends BaseController<WritsInstancesBiz, WritsInstances, Integer> {

	@Autowired
	private WritsInstancesBiz writsInstancesBiz;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation("查询文书列表")
	public TableResultResponse<WritsInstances> getList(
			@RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page,
			@RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit,
			@RequestBody @ApiParam(name = "封装有查询条件的实体类") WritsInstances writsInstances) {
		TableResultResponse<WritsInstances> restResult = new TableResultResponse<>();

		restResult = writsInstancesBiz.getList(writsInstances, page, limit);

		return restResult;
	}

	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ApiOperation("添加单对象")
	public ObjectRestResponse<WritsInstances> add(
			@RequestBody @ApiParam(name = "待添加对象实例") @Validated JSONObject jobs,
			BindingResult bindingResult) {

		ObjectRestResponse<WritsInstances> restResult=new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		JSONObject bizData = jobs.getJSONObject("bizData");
		writsInstancesBiz.updateOrInsert(bizData);
		
		restResult.setMessage("成功");
		return restResult;
	}
}