package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.LawTaskBiz;
import com.bjzhianjia.scp.cgp.entity.LawTask;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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

/**
 *  执法任务
 * 
 * @author chenshuai
 *
 */
@RestController
@RequestMapping("lawTask")
@CheckClientToken
@CheckUserToken
@Api(tags = "执法任务")
public class LawTaskController extends BaseController<LawTaskBiz, LawTask, Integer> {

	@Autowired
	private LawTaskBiz lawTaskBiz;
	
	@RequestMapping(value = "add", method = RequestMethod.POST)
	@ApiOperation("新增执法任务")
	public ObjectRestResponse<LawTask> add(@RequestBody @Validated @ApiParam("执法任务实例") LawTask lawTask,
			BindingResult bindingResult) {
		ObjectRestResponse<LawTask> result = new ObjectRestResponse<>();
		result.setStatus(400);
		result.setMessage("非法参数");
		
		if(bindingResult.hasErrors()) {
			return result;
		}
		if(!StringUtils.isNotBlank(lawTask.getExecutePerson())) {
			return result;
		}
		if(!StringUtils.isNotBlank(lawTask.getPatrolObject())) {
			return result;
		}
		BeanUtil.beanAttributeValueTrim(lawTask);
		try {
			lawTaskBiz.createLawTask(lawTask);
		}catch (Exception e) {
			result.setMessage("新增失败");
			return result;
		}
		result.setStatus(200);
		result.setData(lawTask);
		result.setMessage(null);
		return result;
	}
	
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ApiOperation("更新执法任务")
	public ObjectRestResponse<LawTask> update(@RequestBody @Validated @ApiParam("执法任务实例") LawTask lawTask,
			BindingResult bindingResult) {
		ObjectRestResponse<LawTask> result = new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			result.setStatus(400);
			result.setMessage("非法参数");
			return result;
		}
		BeanUtil.beanAttributeValueTrim(lawTask);
		lawTaskBiz.update(lawTask);
		return result;
	}
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ApiOperation("执法任务列表")
	public TableResultResponse<Map<String , Object>> list(
			@RequestParam(defaultValue="")  @ApiParam("队员名称") String userName,
			@RequestParam(defaultValue="")  @ApiParam("开始日期") String startTime,
			@RequestParam(defaultValue="")  @ApiParam("结束日期") String endTime,
			@RequestParam(defaultValue="")  @ApiParam("任务状态") String state,
			@RequestParam(defaultValue="")  @ApiParam("巡查对象") String regulaObjectName,
			@RequestParam(defaultValue = "10") @ApiParam(name="页容量") int limit,
			@RequestParam(defaultValue = "1") @ApiParam(name="当前页") int page) {
		
		TableResultResponse<Map<String , Object>> result = null;
		
		Date _startTime =null;
		Date _endTimeTmp = null;
		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			_startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
			_endTimeTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
			_endTimeTmp = DateUtils.addDays(_endTimeTmp, 1);
		}
		
		result = lawTaskBiz.getLawTaskList(userName, regulaObjectName, state, _startTime, _endTimeTmp, page, limit);
		return result;
	}
	
	@RequestMapping(value="delete/{id}",method=RequestMethod.DELETE)
	@ApiOperation("删除单个执法任务")
	public ObjectRestResponse<Void> delete(@PathVariable("id") @ApiParam("执法任务的id") Integer id){
		ObjectRestResponse<Void> result = new ObjectRestResponse<>();
		if(id == null) {
			result.setStatus(400);
			return result;
		}
		LawTask lawTask = new LawTask();
		lawTask.setId(id);
		lawTask.setIsDeleted("1");
		lawTaskBiz.updateSelectiveById(lawTask);
		
		return result;
	}
	
	@RequestMapping(value="randomLawTask",method=RequestMethod.GET)
	public ObjectRestResponse<Void> randomLawTask(
			@RequestParam(defaultValue = "") @ApiParam(name="每组队员数 ") Integer peopleNumber,
			@RequestParam(defaultValue = "") @ApiParam(name="巡查对象数") Integer regulaObjNumber,
			@RequestParam(defaultValue = "") @ApiParam(name="网格ids") String griIds,
			@RequestParam(defaultValue = "") @ApiParam(name="监管对象类型ids") String objType,
			@RequestParam(defaultValue = "") @ApiParam(name="开始时间") String startTime,
			@RequestParam(defaultValue = "") @ApiParam(name="结束时间") String endTime,
			@RequestParam(defaultValue = "") @ApiParam(name="任务要求") String info){
		
		ObjectRestResponse<Void> result = new ObjectRestResponse<Void>();
		Date _startTime =null;
		Date _endTimeTmp = null;
		if(StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
			_startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
			_endTimeTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
		}
		try {
			lawTaskBiz.randomLawTask(objType,griIds,peopleNumber,regulaObjNumber,_startTime,_endTimeTmp,info);
		}catch (Exception e) {
			result.setStatus(400);
		}
		
		return result;
	}
	
}