/*
 * @(#) WfProcTaskNotifyImpl.java  1.0  Dec 13, 2016
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
package com.bjzhianjia.scp.security.wf.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.wf.biz.WfProcTaskNotifyBiz;
import com.bjzhianjia.scp.security.wf.entity.WfMyProcBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcTaskBean;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.service.IWfProcTaskNotify;

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
