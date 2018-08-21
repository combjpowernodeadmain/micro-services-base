package com.bjzhianjia.scp.cgp.rest;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.CaseInfoService;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("caseInfo")
@CheckClientToken
@CheckUserToken
@Api(tags="立案管理")
@Slf4j
public class CaseInfoController extends BaseController<CaseInfoBiz,CaseInfo,Integer> {
	@Autowired
	private CaseInfoService caserInfoService;

	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT)
	@ApiOperation("更新单个对象")
	private ObjectRestResponse<CaseInfo> update(@RequestBody @Validated @ApiParam(name="待更新对象实例")CaseInfo caseInfo,BindingResult bindingResult){
		ObjectRestResponse<CaseInfo> restResult=new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = caserInfoService.update(caseInfo);
		if(!result.getIsSuccess()) {
			
		}
		
		restResult.setStatus(400);
		restResult.setMessage("成功");
		return null;
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ApiOperation("分页获取对象")
	public TableResultResponse<CaseInfo> page(@RequestParam(value="page",defaultValue="1") @ApiParam(name="当前页")Integer page,
			@RequestParam(value="limit",defaultValue="10") @ApiParam(name="页容量") Integer limit){
		CaseInfo caseInfo=new CaseInfo();
		
		TableResultResponse<CaseInfo> restResult = caserInfoService.getList(caseInfo, page, limit);
		
		return restResult;
	}
	
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	@ApiOperation("获取单个对象")
	public ObjectRestResponse<CaseInfo> get(@PathVariable("id") @ApiParam(name="待查询对象ID") Integer id){
		return caserInfoService.get(id);
	}
	
	
	/**
     * 查询用户待办流程任务列表
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("查询我的待办")
    @ResponseBody
    @RequestMapping(value = { "/userToDoTasks" }, method = RequestMethod.POST)
    public TableResultResponse<JSONObject> getUserToDoTasks(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询用户待办任务列表...");
        
        TableResultResponse<JSONObject> userToDoTasks = caserInfoService.getUserToDoTasks(objs);
        return userToDoTasks;
    }
}