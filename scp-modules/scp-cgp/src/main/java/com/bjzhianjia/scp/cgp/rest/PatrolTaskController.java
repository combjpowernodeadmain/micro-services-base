package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.PatrolTaskBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.PatrolTaskService;
import com.bjzhianjia.scp.cgp.util.DateUtil;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

/**
 * 巡查任务记录前端控制器
 * 
 * @author chenshuai
 */
@RestController
@RequestMapping("patrolTask")
@CheckClientToken
@CheckUserToken
@Api(tags = "巡查任务记录")
public class PatrolTaskController extends BaseController<PatrolTaskBiz, PatrolTask, Integer> {

	@Autowired
	private PatrolTaskService patrolTaskService;
	@Autowired
	private PatrolTaskBiz patrolTaskBiz;
	
	/**
	 * 创建巡查任务
	 * 
	 * @param patrolTask    巡查任务信息
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("新增巡查任务记录")
	public ObjectRestResponse<Void> add(@RequestBody @ApiParam(name = "待添加对象实例") JSONObject json) {
		ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();
		restResult.setStatus(200);
		//TODO 数据验证
		Result<CaseInfo> result = null;
		try {
			result = patrolTaskService.createPatrolTask(json);
		} catch (Exception e) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
		}
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		return restResult;
	}
	
	
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation("分页列表")
	public TableResultResponse<Map<String, Object>> page(@RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
			@RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
			@RequestParam(defaultValue = "") @ApiParam(name = "专项任务名称") String speName,
			@RequestParam(defaultValue = "") @ApiParam(name = "巡查来源类型") String sourceType,
			@RequestParam(defaultValue = "") @ApiParam(name = "巡查记录名称") String patrolName,
			@RequestParam(defaultValue = "") @ApiParam(name = "业务条线") String bizTypeId,
			@RequestParam(defaultValue = "") @ApiParam(name = "查询记录状态") String status,
			@RequestParam(defaultValue = "") @ApiParam(name = "开始时间") String startTime,
			@RequestParam(defaultValue = "") @ApiParam(name = "结束时间") String endTime
			) {
		
		Date _startTime =null;
		Date _endTimeTmp = null;
		if(StringUtils.isNoneBlank(startTime) && StringUtils.isNoneBlank(endTime)) {
			_startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
			_endTimeTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
			_endTimeTmp = DateUtils.addDays(_endTimeTmp, 1);
		}
		
		PatrolTask patrolTask = new PatrolTask();
		patrolTask.setSourceType(sourceType);
		patrolTask.setPatrolName(patrolName);
		patrolTask.setBizTypeId(bizTypeId);
		patrolTask.setStatus(status);
		
		return patrolTaskBiz.selectPatrolTaskList(patrolTask, speName, _startTime, _endTimeTmp, page, limit);
	}
	
	
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	@ApiOperation("巡查记录详情")
	public ObjectRestResponse<Map<String ,Object>> getOne(@PathVariable(value="id") @ApiParam(name="待查询巡查任务id") Integer id){
		ObjectRestResponse<Map<String ,Object>> restResult = new ObjectRestResponse<>();
		Map<String,Object> data = patrolTaskService.getByIdInfo(id);
		restResult.setData(data);
		
		return restResult;
	}
	
}