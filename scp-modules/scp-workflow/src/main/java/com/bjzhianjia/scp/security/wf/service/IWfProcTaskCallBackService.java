/*
 * @(#) IWfProcTaskCallBackService.java  1.0  September 7, 2016
 *
 * Copyright 2016 by 北京爱钱帮财富科技有限公司
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * IQB("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with IQB.
 */
package com.bjzhianjia.scp.security.wf.service;

import java.util.Map;

import com.bjzhianjia.scp.security.wf.exception.WorkflowException;

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
     * @throws IqbException
     */
    public void before(String dealType, Map<String, Object> procBizData) throws WorkflowException;

    /**
     * 流程任务处理完成后的业务回调接口
     * @param dealType  流程处理类型
     * @param procBizData   流程业务数据
     * @throws IqbException
     */
    public void after(String dealType, Map<String, Object> procBizData) throws WorkflowException;
}
