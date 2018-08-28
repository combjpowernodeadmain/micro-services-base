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
		
		TableResultResponse<CaseInfo> restResult = caserInfoService.getList(caseInfo, page, limit,false);
		
		return restResult;
	}
	
	@RequestMapping(value="/list/unFinish/{id}",method=RequestMethod.GET)
	@ApiOperation("分页获取对象")
	public TableResultResponse<CaseInfo> pageNoFinish(@PathVariable("id") Integer id,@RequestParam(value="page",defaultValue="1") @ApiParam(name="当前页")Integer page,
			@RequestParam(value="limit",defaultValue="10") @ApiParam(name="页容量") Integer limit){
		CaseInfo caseInfo=new CaseInfo();
		caseInfo.setId(id);
		
		TableResultResponse<CaseInfo> restResult = caserInfoService.getList(caseInfo, page, limit,true);
		
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
    
	/**
     * 查询用户待办流程任务列表
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("查询所有待办")
    @ResponseBody
    @RequestMapping(value = { "/allToDoTasks" }, method = RequestMethod.POST)
    public TableResultResponse<JSONObject> getAllToDoTasks(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询所有待办任务列表...");
        
        TableResultResponse<JSONObject> userToDoTasks = caserInfoService.getAllToDoTasks(objs);
        return userToDoTasks;
    }
    
	/**
     * 查询流程任务列表
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("综合查询列表")
    @ResponseBody
    @RequestMapping(value = { "/allTasks" }, method = RequestMethod.POST)
    public TableResultResponse<JSONObject> getAllTasks(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询所有任务列表...");
        
        TableResultResponse<JSONObject> tasks = caserInfoService.getAllTasks(objs);
        return tasks;
    }
    
    @ApiOperation("完成任务")
    @ResponseBody
    @RequestMapping(value = { "/completeProcess" }, method = RequestMethod.POST)
    public ObjectRestResponse<JSONObject> completeProcess(@RequestBody JSONObject objs, HttpServletRequest request) {
    	log.debug("SCP信息---开始启动并提交工作流...");
    	
    	ObjectRestResponse<JSONObject> restResult=new ObjectRestResponse<>();
    	caserInfoService.completeProcess(objs);
    	
    	restResult.setMessage("成功");
    	return restResult;
    }
    
    
    @RequestMapping(value="/get/userToDoTask")
    @ApiOperation("查询详细待办任务")
    public ObjectRestResponse<JSONObject> getUserToDoTask(@RequestBody JSONObject objs, HttpServletRequest request){
    	
    	return caserInfoService.getUserToDoTask(objs);
    }
    
    
    /**
     * 通过流程任务ID终止当前流程任务，终止的流程可在我的流程中查询到
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/endProcess"}, method =  RequestMethod.POST )
    public ObjectRestResponse<JSONObject> endProcessInstance(@RequestBody JSONObject objs,HttpServletRequest reques) {
    	ObjectRestResponse<JSONObject> restResult=new ObjectRestResponse<>();
    	
    	caserInfoService.endProcess(objs);
    	
    	restResult.setMessage("成功");
    	return restResult;
    }
}