/*
 * @(#) IWfMonitorService.java  1.0  August 29, 2016
 *
 * Copyright 2016 by bjzhianjia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * IQB("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with IQB.
 */
package com.bjzhianjia.scp.security.wf.base.monitor.service;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfMyProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;
import com.github.pagehelper.PageInfo;


/**
 * Description: 工作流监控服务接口
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
public interface IWfMonitorService {

    public PageInfo<WfMyProcBackBean> getUserDoneTasks(JSONObject objs) throws WorkflowException;
    public int getUserDoneTaskCount(JSONObject objs) throws WorkflowException;
    
    public PageInfo<WfMyProcBackBean> getActiveProcessList(JSONObject objs) ;

    public PageInfo<WfMyProcBackBean> getOrgProcessList(JSONObject objs) ;

    public PageInfo<WfProcBackBean> getUserToDoTasks(JSONObject objs) throws WorkflowException;
    
    public PageInfo<WfProcBackBean> getAllToDoTasks(JSONObject objs) throws WorkflowException;
    
    public int getUserToDoTaskCount(JSONObject objs) throws WorkflowException;
    
	public PageInfo<WfMyProcBackBean> getProcessDelegateList(JSONObject objs) ;

	public PageInfo<WfMyProcBackBean> geyProcessSummary(JSONObject objs) ;

	public PageInfo<WfMyProcBackBean> getMyProcDelegateList(JSONObject objs) ;
	
	public String exportProcessSummary(JSONObject objs, HttpServletResponse response);
}
