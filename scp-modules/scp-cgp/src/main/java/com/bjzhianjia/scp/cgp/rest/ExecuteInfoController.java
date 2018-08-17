package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.ExecuteInfoBiz;
import com.bjzhianjia.scp.cgp.entity.ExecuteInfo;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.ExecuteInfoService;

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
@RequestMapping("executeInfo")
@CheckClientToken
@CheckUserToken
@Api(tags = "事件处理情况")
public class ExecuteInfoController extends BaseController<ExecuteInfoBiz, ExecuteInfo, Integer> {
	
	@Autowired
	private ExecuteInfoService executeInfoService;

	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ApiOperation("添加单个对象")
	public ObjectRestResponse<ExecuteInfo> add(
			@RequestBody @Validated @ApiParam(name = "待添加对象实例") ExecuteInfo executeInfo, BindingResult bindingResult) {
		ObjectRestResponse<ExecuteInfo> restResult=new ObjectRestResponse<>();
		
		Result<Void> result = executeInfoService.createdExecuteInfo(executeInfo);
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		
		restResult.setMessage("成功");
		restResult.setStatus(200);
		return restResult;
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ApiOperation("分页获取对象")
	public TableResultResponse<ExecuteInfo> page(
			@RequestParam(value="page",defaultValue="1")Integer page,
			@RequestParam(value="limit",defaultValue="10")Integer limit
			){
		ExecuteInfo executeInfo=new ExecuteInfo();
		TableResultResponse<ExecuteInfo> restResult = executeInfoService.getList(executeInfo, page, limit);
		return restResult;
	}
	
}