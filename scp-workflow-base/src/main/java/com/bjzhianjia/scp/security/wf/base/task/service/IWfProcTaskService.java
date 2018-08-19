/*
 * @(#) IWfProcTaskService.java  1.0  August 22, 2016
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
package com.bjzhianjia.scp.security.wf.base.task.service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskBean;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskHistoryBean;
import com.github.pagehelper.PageInfo;

/**
 * Description: 工作流流程任务相关处理接口类
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
public interface IWfProcTaskService {

    /**
     * 根据流程定义编码启动对应的工作流
     * 
     * @param objs  接口参数Json对象
     * @return  流程实例ID
     * 
     * @throws WorkflowException
     */
    public String startProcessInstance(JSONObject objs) throws WorkflowException;
    
    
    /**
     * 根据流程定义编码启动工作流并提交第一个流程任务，通过用户登录信息验证启动流程合法性
     * 
     * @param objs  接口参数Json对象
     * @return  流程实例ID
     * 
     * @throws WorkflowException
     */
    public String startAndCompleteProcessInstance(JSONObject objs) throws WorkflowException;
    
    /**
     * 通过流程任务ID签收当前流程任务
     * 
     * @param objs  接口参数Json对象
     * @return 流程任务信息
     * 
     * @throws WorkflowException
     */
    public WfProcTaskBean claimProcessInstance(JSONObject objs) throws WorkflowException;

    /**
     * 通过流程任务ID取消签收当前流程任务
     * 
     * @param objs 接口参数Json对象
     * @return 流程任务信息
     * @throws WorkflowException
     */
    public WfProcTaskBean unclaimProcessInstance(JSONObject objs) throws WorkflowException;
    
    /**
     * 通过流程任务ID审批当前流程任务
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void completeProcessInstance(JSONObject objs) throws WorkflowException;
    
        
    /**
     * 通过流程任务ID删除当前流程任务，删除的流程可以在我的流程中查询到
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void deleteProcessInstance(JSONObject objs) throws WorkflowException;
    
    /**
     * 通过流程实例取消当前流程任务，取消的流程可在我的流程中查询到
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void cancelProcessInstance(JSONObject objs) throws WorkflowException;
    
    /**
     * 通过流程实例撤回当前流程任务，撤回的流程在待办任务中可进行再次处理
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void retrieveProcessInstance(JSONObject objs) throws WorkflowException;
    
    /**
     * 通过流程实例终止当前流程任务，终止的流程可在我的流程中查询到
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void endProcessInstance(JSONObject objs) throws WorkflowException;
    
    /**
     * 通过流程任务ID委托当前流程
     * @param objs  接口参数Json对象
     * @throws WorkflowException
     */
    public void delegateProcessInstance(JSONObject objs) throws WorkflowException;
    
    /**
     * 取消流程委托
     * @param objs  接口参数Json对象
     * @throws WorkflowException
     */
    public void cancelProcDelegate(JSONObject objs) throws WorkflowException;
    
    /**
     * 通过流程任务ID指定流程任务处理人
     * @param objs
     * @throws WorkflowException
     */
    public void appointAssignee(JSONObject objs) throws WorkflowException;
    
    /**
     * 通过流程实例ID将审批中的流程实例暂停，暂停的流程实例只可以被查询，流出任务不可以被处理
     * @param objs
     * @throws WorkflowException
     */
    public void suspendProcess(JSONObject objs) throws WorkflowException;
    
    /**
     * 通过流程实例ID将暂停的流程实例激活，激活的流程实例状态变更为审批中，流程任务可以被处理
     * @param objs
     * @throws WorkflowException
     */
    public void activeProcess(JSONObject objs) throws WorkflowException;
    
    /**
     * 查询业务流程审批历史
     * 
     * @param objs
     *            接口参数Json对象
     * @throws WorkflowException
     */
    public PageInfo<WfProcTaskHistoryBean> getProcApprovedHistory(JSONObject objs)
        throws WorkflowException;
}
