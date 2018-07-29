/*
 * @(#) WfProcTaskNotifyImpl.java  1.0  Dec 13, 2016
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
package com.bjzhianjia.scp.security.wf.notify.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.notify.biz.WfProcTaskNotifyBiz;
import com.bjzhianjia.scp.security.wf.notify.service.IWfProcTaskNotify;
import com.bjzhianjia.scp.security.wf.task.entity.WfMyProcBean;
import com.bjzhianjia.scp.security.wf.task.entity.WfProcBean;
import com.bjzhianjia.scp.security.wf.task.entity.WfProcTaskBean;

/**
 * Description: 工作流流程任务通知服务接口
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.12.13    mayongming       1.0           1.0 Version
 * </pre>
 */
@Service("wfProcTaskNotify")
public class WfProcTaskNotifyImpl implements IWfProcTaskNotify{

    @Autowired
    WfProcTaskNotifyBiz wfProcTaskNotify;

    /**
     * 流程任务通知发布服务接口，通知用户有新任务到达
     * 
     * @param wfProc
     *            流程数据
     * @param wfProcTasks
     *            流程任务数据
     * @throws IqbException
     */
    public void notify(WfProcBean wfProc, List<WfProcTaskBean> wfProcTasks)
        throws WorkflowException {
        wfProcTaskNotify.notify(wfProc, wfProcTasks);
    }

    /**
     * 流程任务通知发布服务接口，通知该流程所有处理用户流程已经处理结束。
     * @param wfProc        流程数据
     * @param myProcess     该流程所有处理用户
     * @throws IqbException
     */
    public void notifyAll(WfProcBean wfProc, List<WfMyProcBean> myProcess)
        throws WorkflowException {
        wfProcTaskNotify.notifyAll(wfProc, myProcess);
    }
}
