/*
 * @(#) WfProcMonitorRest.java  1.0  August 29, 2016
 *
 * Copyright 2016 by bjzhianjia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * SCP("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with SCP.
 */
package com.bjzhianjia.scp.security.wf.rest;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.wf.base.ResultUtils;
import com.bjzhianjia.scp.security.wf.monitor.service.IWfMonitorService;
import com.bjzhianjia.scp.security.wf.task.service.IWfProcTaskService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 工作流流程监控Rest服务接口
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.29    mayongming       1.0        1.0 Version
 * </pre>
 */
@Controller
@CheckUserToken
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping("/monitor")
@Api(value = "工作流流程监控Rest服务接口", tags = "流程监控")
@Slf4j
public class WfProcMonitorRest {

    @Autowired
    IWfMonitorService wfMonitorService;
    @Autowired
    IWfProcTaskService wfProcTaskService;

    /**
     * 流程监控列表查询
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/activeProcessList" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<PageInfo> getActiveProcessList(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询未完成流程列表...");
        return ResultUtils.success(wfMonitorService.getActiveProcessList(objs));
    }

    /**
     * 流程监控列表查询
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/orgProcessList" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<PageInfo> getOrgProcessList(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询未完成流程列表...");
        return ResultUtils.success(wfMonitorService.getOrgProcessList(objs));
    }
    
    /**
     * 查询我的已办流程列表
     * @param objs
     * @return
     */
    @ApiOperation("查询我的已办")
    @ResponseBody
    @RequestMapping(value = { "/userDoneTasks" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<PageInfo> geyUserDoneTasks(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询用户已办任务列表...");
        return ResultUtils.success(wfMonitorService.getUserDoneTasks(objs));
    }
    
    /**
     * 查询我的已办流程任务数
     * @param objs
     * @return
     */
    @ApiOperation("查询我的已办任务数")
    @ResponseBody
    @RequestMapping(value = { "/userDoneTaskCount" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Integer> getUserDoneTaskCount(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询用户已办任务列表...");
        return ResultUtils.success(wfMonitorService.getUserDoneTaskCount(objs));
    }
    
    /**
     * 查询用户待办流程任务列表
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("查询我的待办")
    @ResponseBody
    @RequestMapping(value = { "/userToDoTasks" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<PageInfo> getUserToDoTasks(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询用户待办任务列表...");
        return ResultUtils.success( wfMonitorService.getUserToDoTasks(objs));
    }

    /**
     * 查询用户待办流程任务数量
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("查询我的待办任务数量")
    @ResponseBody
    @RequestMapping(value = { "/userToDoTaskCount" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<Integer> getUserToDoTaskCount(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询用户待办任务列表...");
        return ResultUtils.success(wfMonitorService.getUserToDoTaskCount(objs));
    }
        
    /**
     * 流程实例汇总查询
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/processSummary" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<PageInfo> geyProcessSummary(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始流程实例汇总查询...");
        return ResultUtils.success(wfMonitorService.geyProcessSummary(objs));
    }
    /**
     * 导出流程实例汇总
     * 
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/exportProcessSummary"}, method = {RequestMethod.GET, RequestMethod.POST})
    public ObjectRestResponse<Map> exportProcessSummary(HttpServletRequest request, HttpServletResponse response) {
    	Map<String, Object> linkedHashMap = new LinkedHashMap<>();
        try {
			request.setCharacterEncoding("utf-8");
            log.debug("开始导出流程实例汇总报表数据");
            Map<String, String> data = new HashMap<>();
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paraName = paramNames.nextElement();
                String para = request.getParameter(paraName);
                data.put(paraName, para.trim());
            }
            String json = JSON.toJSONString(data);
            JSONObject objs = JSONObject.parseObject(json);
            String result = wfMonitorService.exportProcessSummary(objs, response);
            log.debug("导出流程实例汇总报表数据完成.结果：{}", result);
            linkedHashMap.put("result", result);
        } catch (UnsupportedEncodingException e) {
        	log.error("UnsupportedEncodingException: ", e);
        	e.printStackTrace();
        }
        return ResultUtils.success(linkedHashMap);
    }
    /**
     * 流程委托查询
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/processDelegate" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<PageInfo> getProcessDelegateList(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始流程委托查询...");
        return ResultUtils.success(wfMonitorService.getProcessDelegateList(objs));
    }
    
    /**
     * 流程委托查询
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = { "/myProcDelegate" }, method = { RequestMethod.GET, RequestMethod.POST })
    public ObjectRestResponse<PageInfo> getMyProcDelegateList(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始我的流程委托查询...");
        return ResultUtils.success(wfMonitorService.getMyProcDelegateList(objs));
    }
}
