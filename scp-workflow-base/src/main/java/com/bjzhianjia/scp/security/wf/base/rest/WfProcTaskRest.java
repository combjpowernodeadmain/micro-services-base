/*
 * @(#) WfProcTaskRest.java  1.0  August 22, 2016
 *
 * Copyright 2016 by bjzhianjia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * BJZAJ("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with BJZAJ.
 */
package com.bjzhianjia.scp.security.wf.base.rest;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.wf.base.biz.ResultUtils;
import com.bjzhianjia.scp.security.wf.base.constant.Constants.WfProcessDataAttr;
import com.bjzhianjia.scp.security.wf.base.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskBean;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskService;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 工作流流程任务Rest服务接口
 * 
 * @author mayongming
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.22    mayongming       1.0        1.0 Version
 * </pre>
 */
@Controller
@CheckUserToken
@SuppressWarnings("rawtypes")
@RequestMapping("/task")
@Api(value = "工作流流程任务Rest服务接口", tags = "流程任务")
@Slf4j
public class WfProcTaskRest {
    
    @Autowired
    IWfProcTaskService wfProcTaskService;
    
    /**
     * 根据流程定义编码启动工作流，通过用户登录信息验证启动流程合法性
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/startProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
	public ObjectRestResponse<Map> startProcess(@RequestBody JSONObject objs, HttpServletRequest request) {
		log.debug("SCP信息---开始启动工作流...");
		String procInstId = wfProcTaskService.startProcessInstance(objs);
		Map<String, Object> resultData = new LinkedHashMap<String, Object>();
		resultData.put(WfProcessDataAttr.PROC_INSTANCEID, procInstId);
		return ResultUtils.success(resultData);
	}
    
    /**
     * 根据流程定义编码启动工作流并提交第一个流程任务，通过用户登录信息验证流程启动合法性
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/startAndCommitProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> startAndCommitProcess(@RequestBody JSONObject objs,HttpServletRequest request) {
        log.debug("SCP信息---开始启动并提交工作流...");
        String procInstId = wfProcTaskService.startAndCompleteProcessInstance(objs);
        Map<String, Object> resultData = new LinkedHashMap<String, Object>();
        resultData.put(WfProcessDataAttr.PROC_INSTANCEID, procInstId);
        
        return ResultUtils.success(resultData);
    }
    
    /**
     * 通过流程任务ID签收当前流程任务
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/claimProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> claimProcess(@RequestBody JSONObject objs,HttpServletRequest request) {
        log.debug("SCP信息---开始流程任务签收操作...");
        WfProcTaskBean taskBean = wfProcTaskService.claimProcessInstance(objs);
        LinkedHashMap<String, Object> resultData = new LinkedHashMap<String, Object>();
        
        // 在此组装返回给前台的数据
        resultData.put(WfProcessDataAttr.PROC_DEFINITIONID, taskBean.getProcId());
        resultData.put(WfProcessDataAttr.PROC_DEFINITIONKEY, taskBean.getProcKey());
        resultData.put(WfProcessDataAttr.PROC_INSTANCEID, taskBean.getProcInstId());
        resultData.put(WfProcessDataAttr.PROC_TASKID, taskBean.getProcCtaskid());
        resultData.put(WfProcessDataAttr.PROC_TASKCODE, taskBean.getProcCtaskcode());
        resultData.put(WfProcessDataAttr.PROC_TASKNAME, taskBean.getProcCtaskname());
        
        return ResultUtils.success(resultData);
    }
    
    /**
     * 通过流程任务ID取消签收当前流程任务
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/unclaimProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> unclaimProcess(@RequestBody JSONObject objs,HttpServletRequest request) {
        log.debug("SCP信息---开始流程任务取消签收操作...");
        WfProcTaskBean taskBean = wfProcTaskService.unclaimProcessInstance(objs);
        LinkedHashMap<String, Object> resultData = new LinkedHashMap<String, Object>();

        // 在此组装返回给前台的数据
        resultData.put(WfProcessDataAttr.PROC_DEFINITIONID, taskBean.getProcId());
        resultData.put(WfProcessDataAttr.PROC_DEFINITIONKEY, taskBean.getProcKey());
        resultData.put(WfProcessDataAttr.PROC_INSTANCEID, taskBean.getProcInstId());
        resultData.put(WfProcessDataAttr.PROC_TASKID, taskBean.getProcCtaskid());
        resultData.put(WfProcessDataAttr.PROC_TASKCODE, taskBean.getProcCtaskcode());
        resultData.put(WfProcessDataAttr.PROC_TASKNAME, taskBean.getProcCtaskname());

        return ResultUtils.success(resultData);
    }
    
    /**
     * 通过流程任务ID审批当前流程任务
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/completeProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> completeProcessInstance(@RequestBody JSONObject objs,HttpServletRequest request) {
        log.debug("SCP信息---开始流程任务审批操作...");
        wfProcTaskService.completeProcessInstance(objs);
        return ResultUtils.success();
    }
    
    /**
     * 通过流程任务ID删除当前流程任务
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/deleteProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> deleteProcessInstance(@RequestBody JSONObject objs,HttpServletRequest request) {
        log.debug("SCP信息---开始流程任务删除...");
        wfProcTaskService.deleteProcessInstance(objs);
        return ResultUtils.success();
    }
    
    /**
     * 通过流程任务代码取消当前流程任务，取消的流程可在我的流程中查询到
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/cancelProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> cancelProcessInstance(@RequestBody JSONObject objs,HttpServletRequest reques) {
        log.debug("SCP信息---开始流程任务取消操作...");
        wfProcTaskService.cancelProcessInstance(objs);
        return ResultUtils.success();
    }
    
    /**
     * 通过流程任务ID撤回当前流程任务，撤回的流程在待办任务中可进行再次处理
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/retrieveProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> retrieveProcessInstance(@RequestBody JSONObject objs,HttpServletRequest reques) {
        log.debug("SCP信息---开始流程任务撤回操作...");
        wfProcTaskService.retrieveProcessInstance(objs);
        return ResultUtils.success();
    }
    
    /**
     * 通过流程任务ID终止当前流程任务，终止的流程可在我的流程中查询到
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/endProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> endProcessInstance(@RequestBody JSONObject objs,HttpServletRequest reques) {
        log.debug("SCP信息---开始流程任务终止操作...");
        wfProcTaskService.endProcessInstance(objs);
        return ResultUtils.success();
    }
    
    /**
     * 流程实例暂停
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/suspend" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> suspendProcess(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始流程实例暂停处理...");
        wfProcTaskService.suspendProcess(objs);
        return ResultUtils.success();
    }

    /**
     * 将暂停的流程实例激活
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/active" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> activeProcess(@RequestBody JSONObject objs) {
        log.debug("SCP信息---开始流程实例激活处理...");
        wfProcTaskService.activeProcess(objs);
        return ResultUtils.success();
    }
    /**
     * 指定流程任务的处理人
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/assign" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> appointAssignee(@RequestBody JSONObject objs, HttpServletRequest reques) {
        log.debug("SCP信息---开始设置流程任务处理人...");
        wfProcTaskService.appointAssignee(objs);
        return ResultUtils.success();
    }
    
    /**
     * 通过流程任务ID委托当前流程任务
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/delegateProcess"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> delegateProcessInstance(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始流程任务委托操作...");
        wfProcTaskService.delegateProcessInstance(objs);
        return ResultUtils.success();
    }
    
    /**
     * 取消流程委托
     * @param objs
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/cancelProcDelegate"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> cancelProcDelegate(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始取消流程委托...");
        wfProcTaskService.cancelProcDelegate(objs);
        return ResultUtils.success();
    }
    
    /**
     * 查询业务流程审批历史
     * 
     * @param objs
     * @param request
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = { "/procApproveHistory"}, method = {RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Map> getProcApprovedHistory(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询业务流程审批历史...");
        LinkedHashMap map = new LinkedHashMap();
        map.put("result", wfProcTaskService.getProcApprovedHistory(objs));
        return ResultUtils.success(map);
    }
}