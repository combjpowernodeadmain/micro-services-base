package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.ConcernedPersonBiz;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.ConcernedPersonService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("concernedPerson")
@CheckClientToken
@CheckUserToken
@Api(tags = "事件当事人管理")
public class ConcernedPersonController extends BaseController<ConcernedPersonBiz, ConcernedPerson, Integer> {

	@Autowired
	private ConcernedPersonService concernedPersonService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation("添加单个对象")
	public ObjectRestResponse<ConcernedPerson> add(
			@RequestBody @ApiParam(name = "待添加对象实例") @Validated ConcernedPerson concernedPerson,
			BindingResult bindingResult) {
		ObjectRestResponse<ConcernedPerson> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<Void> result = concernedPersonService.created(concernedPerson);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		restResult.setMessage("成功");
		return restResult;
	}

	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ApiOperation("分页获取对象")
	public TableResultResponse<ConcernedPerson> page(@RequestParam(value = "page", defaultValue = "1") @ApiParam(name="当前页")Integer page,
			@RequestParam(value = "limit", defaultValue = "10") @ApiParam(name="页容量")Integer limit) {
		ConcernedPerson concernedPerson = new ConcernedPerson();
		TableResultResponse<ConcernedPerson> restResult = concernedPersonService.getList(concernedPerson, page, limit);
		return restResult;
	}
	
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	@ApiOperation("获取单个对象")
	public ObjectRestResponse<ConcernedPerson> getOne(@PathVariable(value="id") @ApiParam(name="待查询对象ID")Integer id){
		return concernedPersonService.getOne(id);
	}
}