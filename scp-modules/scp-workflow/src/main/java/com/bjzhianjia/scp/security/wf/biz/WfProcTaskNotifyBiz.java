/*
 * @(#) WfProcTaskBiz.java  1.0  Dec 13, 2016
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
package com.bjzhianjia.scp.security.wf.biz;

import java.util.List;

import org.springframework.stereotype.Component;

import com.bjzhianjia.scp.security.wf.base.WfBaseBiz;
import com.bjzhianjia.scp.security.wf.entity.WfMyProcBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcTaskBean;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;

import lombok.extern.slf4j.Slf4j;

/**
 * Description: 工作流流程任务通知实现接口
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.12.13    mayongming       1.0        1.0 Version
 * </pre>
 */
@Component
@Slf4j
public class WfProcTaskNotifyBiz extends WfBaseBiz{

    /**
     * 流程任务通知发布处理
     * 
     * @param wfProc
     *            流程数据
     * @param wfProcTasks
     *            流程任务数据
     * @throws IqbException
     */
    public void notify(WfProcBean wfProc, List<WfProcTaskBean> wfProcTasks)
        throws WorkflowException {
        if (wfProcTasks != null) {
            for (WfProcTaskBean wfProcTask : wfProcTasks) {
                log.info("流程名称：" + wfProc.getProcName());
                log.info("流程业务ID：" + wfProc.getProcBizid());
                log.info("流程机构代码：" + wfProc.getProcOrgcode());
                log.info("流程业务摘要：" + wfProc.getProcMemo());
                log.info("流程任务名称：" + wfProcTask.getProcCtaskname());
                log.info("流程提交人：" + wfProcTask.getProcTaskCommitter());
                log.info("流程提交时间：" + wfProcTask.getProcTaskCommittime());
            }
        } else {
            log.info("没有流程任务数据");
        }
    }
    
    /**
     * 流程任务通知发布服务接口，通知该流程所有处理用户流程已经处理结束。
     * @param wfProc        流程数据
     * @param myProcess     该流程所有处理用户
     * @throws IqbException
     */
    public void notifyAll(WfProcBean wfProc, List<WfMyProcBean> myProcesses)
        throws WorkflowException {
        if (myProcesses != null) {
            for (WfMyProcBean myProcess : myProcesses) {
                log.info("流程名称：" + wfProc.getProcName());
                log.info("流程业务ID：" + wfProc.getProcBizid());
                log.info("流程机构代码：" + wfProc.getProcOrgcode());
                log.info("流程业务摘要：" + wfProc.getProcMemo());
                log.info("流程通知用户：" + myProcess.getProcUser());
                log.info("流程完成时间：" + wfProc.getProcEndtime());
            }
        } else {
            log.info("没有流程任务数据");
        }
    }
}
