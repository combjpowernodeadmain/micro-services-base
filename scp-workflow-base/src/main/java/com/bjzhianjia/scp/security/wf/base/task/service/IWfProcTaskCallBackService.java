/*
 * @(#) IWfProcTaskCallBackService.java  1.0  September 7, 2016
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

import java.util.Map;

import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;

/**
 * Description: 工作流流程任务审批前后回调函数接口
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.09.07    mayongming       1.0           1.0 Version
 * </pre>
 */
public interface IWfProcTaskCallBackService extends IWfProcTaskCallBackBaseService {
    /**
     * 流程任务处理前的业务回调接口
     * @param dealType  流程处理类型
     * @param procBizData   流程业务数据
     * @throws WorkflowException
     */
    public void before(String dealType, Map<String, Object> procBizData) throws WorkflowException;

    /**
     * 流程任务处理完成后的业务回调接口
     * @param dealType  流程处理类型
     * @param procBizData   流程业务数据
     * @throws WorkflowException
     */
    public void after(String dealType, Map<String, Object> procBizData) throws WorkflowException;
}
