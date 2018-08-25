/*
 * @(#) WfProcTaskServiceImpl.java  1.0  August 22, 2016
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
package com.bjzhianjia.scp.security.wf.task.service.impl;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.auth.biz.WfProcUserAuthBiz;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfRequestDataTypeAttr;
import com.bjzhianjia.scp.security.wf.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.task.biz.WfProcTaskBiz;
import com.bjzhianjia.scp.security.wf.task.entity.WfProcTaskBean;
import com.bjzhianjia.scp.security.wf.task.entity.WfProcTaskHistoryBean;
import com.bjzhianjia.scp.security.wf.task.service.IWfProcTaskService;
import com.bjzhianjia.scp.security.wf.utils.JSONUtil;
import com.bjzhianjia.scp.security.wf.vo.WfProcAuthDataBean;
import com.bjzhianjia.scp.security.wf.vo.WfProcBizDataBean;
import com.bjzhianjia.scp.security.wf.vo.WfProcVariableDataBean;
import com.bjzhianjia.scp.security.wf.vo.WfProcessDataBean;
import com.github.pagehelper.PageInfo;

/**
 * Description: 工作流流程任务服务接口
 * 
 * @author mayongming
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.22    mayongming       1.0           1.0 Version
 * </pre>
 */
@Service("wfProcTaskService")
@Slf4j
public class WfProcTaskServiceImpl implements IWfProcTaskService {
    @Autowired
    WfProcTaskBiz wfProcTaskBiz;    
    @Autowired
    WfProcUserAuthBiz wfProcUserAuthBiz; 
    
    /**
     * 根据流程定义编码启动对应的工作流，通过用户登录信息验证启动流程合法性
     * 
     * @param objs  接口参数Json对象
     * @return  流程实例ID
     * 
     * @throws WorkflowException
     */
    public String startProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            WfProcBizDataBean bizData = parseBizData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
            return wfProcTaskBiz
                .startProcessInstanceByKey(procData, procVarData, authData, bizData);
        } catch (WorkflowException wfe) {
            throw wfe;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
        }
    }

    /**
     * 根据流程定义编码启动工作流并提交第一个流程任务，通过用户登录信息验证流程操作合法性
     * 
     * @param objs  接口参数Json对象
     * @return  流程实例ID
     * 
     * @throws WorkflowException
     */
    public String startAndCompleteProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            WfProcBizDataBean bizData = parseBizData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
			return wfProcTaskBiz.startAndCompleteProcessInstanceByKey(procData,
					procVarData, authData, bizData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
        	log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
        }
    }

    /**
     * 通过流程任务ID签收当前流程任务
     * 
     * @param objs  接口参数Json对象
     * @return 流程任务信息
     * 
     * @throws WorkflowException
     */
    public WfProcTaskBean claimProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            WfProcBizDataBean bizData = parseBizData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, true);
            
			return wfProcTaskBiz.claimProcessInstance(procData, procVarData,
					authData, bizData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020399, e);
        }
    }
    
    /**
     * 通过流程任务ID取消签收当前流程任务
     * 
     * @param objs 接口参数Json对象
     * @return 流程任务信息
     * @throws WorkflowException
     */
    public WfProcTaskBean unclaimProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            WfProcBizDataBean bizData = parseBizData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
			return wfProcTaskBiz.unclaimProcessInstance(procData, procVarData,
					authData, bizData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020499, e);
        }
    }
    
    /**
     * 通过流程任务ID审批当前流程任务
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void completeProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            WfProcBizDataBean bizData = parseBizData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, true);
            
            // 调用流程任务审批服务接口
            wfProcTaskBiz.completeProcessInstance(procData, procVarData, authData, bizData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020599, e);
        }
    }
    
    /**
     * 通过流程任务ID删除当前流程任务，删除的流程不可以在我的流程中查询到
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void deleteProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            WfProcBizDataBean bizData = parseBizData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
            wfProcTaskBiz.deleteProcessInstance(procData, procVarData, authData, bizData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020699, e);
        }
    }
    
    /**
     * 通过流程实例取消当前流程，取消的流程可在我的流程中查询到
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void cancelProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            WfProcBizDataBean bizData = parseBizData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
            wfProcTaskBiz.cancelProcessInstance(procData, procVarData, authData, bizData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021099, e);
        }
    }
    
    /**
     * 通过流程实例撤回当前流程任务，撤回的流程在待办任务中可进行再次处理
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void retrieveProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            WfProcBizDataBean bizData = parseBizData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
            wfProcTaskBiz.retrieveProcessInstance(procData, procVarData, authData, bizData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021199, e);
        }
    }
    
    /**
     * 通过流程实例终止流程，终止的流程可在我的流程中查询到
     * 
     * @param objs  接口参数Json对象，包括：
     * 
     * @throws WorkflowException
     * @throws Exception
     */
    public void endProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            WfProcBizDataBean bizData = parseBizData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
            wfProcTaskBiz.endProcessInstance(procData, procVarData, authData, bizData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021299, e);
        }
    }
    
    /**
     * 通过流程任务ID委托当前流程
     * @param objs  接口参数Json对象
     * @throws WorkflowException
     */
    public void delegateProcessInstance(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, true);
            
            wfProcTaskBiz.delegateProcessInstance(procData, procVarData, authData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020999, e);
        }
    }
    
    /**
     * 取消流程委托
     * @param objs  接口参数Json对象
     * @throws WorkflowException
     */
    public void cancelProcDelegate(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, true);
            
            wfProcTaskBiz.cancelProcDelegate(procData, procVarData, authData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020999, e);
        }
    }
    
    /**
     * 通过流程任务ID指定流程任务处理人
     * @param objs
     * @throws WorkflowException
     */
    public void appointAssignee(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcVariableDataBean procVarData = parseVariableData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
            wfProcTaskBiz.appointAssignee(procData, procVarData, authData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020999, e);
        }
    }
    
    /**
     * 通过流程实例ID将审批中的流程实例暂停，暂停的流程实例只可以被查询，流出任务不可以被处理
     * @param objs
     * @throws WorkflowException
     */
    public void suspendProcess(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
            wfProcTaskBiz.suspendProcess(procData, authData);
        } catch (WorkflowException wfe) {
            throw wfe;
        }
        catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020999, e);
        }
    }
    
    /**
     * 通过流程实例ID将暂停的流程实例激活，激活的流程实例状态变更为审批中，流程任务可以被处理
     * @param objs
     * @throws WorkflowException
     */
    public void activeProcess(JSONObject objs) throws WorkflowException {
        try {
            WfProcessDataBean procData = parseProcessData(objs);
            WfProcAuthDataBean authData = parseAuthData(objs);
            wfProcUserAuthBiz.userAuthenticate(authData, false, false);
            
            wfProcTaskBiz.activeProcess(procData, authData);
        } catch (WorkflowException wfe) {
            throw wfe;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020999, e);
        }
    }
    
    /**
     * 查询业务流程审批历史
     * @param objs  接口参数Json对象
     * @throws WorkflowException
     */
	public PageInfo<WfProcTaskHistoryBean> getProcApprovedHistory(
			JSONObject objs) throws WorkflowException {
		try {
			WfProcessDataBean procData = parseProcessData(objs);
			WfProcAuthDataBean authData = parseAuthData(objs);
			wfProcUserAuthBiz.userAuthenticate(authData, false, false);

			return new PageInfo<WfProcTaskHistoryBean>(
					wfProcTaskBiz.getProcApprovedHistory(procData, authData));
		} catch (WorkflowException wfe) {
			throw wfe;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020799, e);
		}
	}
    
    /**
     * 解析流程数据
     * @param objs  请求Json数据
     * @return
     * @throws WorkflowException
     */
    private WfProcessDataBean parseProcessData(JSONObject objs)
        throws WorkflowException {
        if (objs.containsKey(WfRequestDataTypeAttr.PROC_PROCDATA)) {
            return JSONUtil.toJavaObject(objs.getJSONObject(WfRequestDataTypeAttr.PROC_PROCDATA),
                WfProcessDataBean.class);
        } else {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020000);
        }
    }

    /**
     * 解析流程变量数据
     * @param objs  请求Json数据
     * @return
     * @throws WorkflowException
     */
    private WfProcVariableDataBean parseVariableData(JSONObject objs) throws WorkflowException {
        if (objs.containsKey(WfRequestDataTypeAttr.PROC_VARIABLEDATA)) {
            WfProcVariableDataBean variableData =
                JSONUtil.toJavaObject(objs, WfProcVariableDataBean.class);
            return (variableData == null || variableData.isEmpty()) ? new WfProcVariableDataBean()
                : variableData;
        } else {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020000);
        }
    }

    /**
     * 解析流程认证数据
     * @param objs  请求Json数据
     * @return
     * @throws WorkflowException
     */
    private WfProcAuthDataBean parseAuthData(JSONObject objs) throws WorkflowException {
        WfProcAuthDataBean authData = null;
        if (objs.containsKey(WfRequestDataTypeAttr.PROC_AUTHDATA)) {
            authData =
                JSONUtil.toJavaObject(objs.getJSONObject(WfRequestDataTypeAttr.PROC_AUTHDATA),
                    WfProcAuthDataBean.class);
        }
        
        if (authData == null || authData.isEmpty()) {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020000);
        }
        
        return authData;
    }
    
    /**
     * 解析流程业务数据
     * @param objs  请求Json数据
     * @return
     * @throws WorkflowException
     */
    private WfProcBizDataBean parseBizData(JSONObject objs) throws WorkflowException {
        if (objs.containsKey(WfRequestDataTypeAttr.PROC_BIZDATA)) {
            WfProcBizDataBean bizData = JSONUtil.toJavaObject(objs, WfProcBizDataBean.class);
            return (bizData == null || bizData.isEmpty()) ? new WfProcBizDataBean() : bizData;
        } else {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020000);
        }
    }  
}
