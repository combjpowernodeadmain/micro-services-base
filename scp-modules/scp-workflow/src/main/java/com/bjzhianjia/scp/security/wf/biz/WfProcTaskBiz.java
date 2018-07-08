/*
 * @(#) WfProcTaskBiz.java  1.0  August 22, 2016
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.common.util.DateTools;
import com.bjzhianjia.scp.security.wf.constant.Attr.DictKeyConst;
import com.bjzhianjia.scp.security.wf.constant.Constants.FlowStatus;
import com.bjzhianjia.scp.security.wf.constant.Constants.ProcUserType;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfDataValid;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcDealType;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcParallStatus;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcVotePower;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcVoteRole;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcessStartModeAttr;
import com.bjzhianjia.scp.security.wf.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.entity.WfMyProcBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcDelegateBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcTaskBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcTaskHistoryBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcTaskPropertiesBean;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.mapper.WfMyProcBeanMapper;
import com.bjzhianjia.scp.security.wf.mapper.WfProcBeanMapper;
import com.bjzhianjia.scp.security.wf.mapper.WfProcDelegateBeanMapper;
import com.bjzhianjia.scp.security.wf.mapper.WfProcTaskBeanMapper;
import com.bjzhianjia.scp.security.wf.service.IWfProcTaskCallBackBaseService;
import com.bjzhianjia.scp.security.wf.service.IWfProcTaskCallBackService;
import com.bjzhianjia.scp.security.wf.service.IWfProcTaskNewCallBackService;
import com.bjzhianjia.scp.security.wf.service.IWfProcTaskNotify;
import com.bjzhianjia.scp.security.wf.utils.JSONUtil;
import com.bjzhianjia.scp.security.wf.utils.StringUtil;
import com.bjzhianjia.scp.security.wf.vo.WfProcAuthDataBean;
import com.bjzhianjia.scp.security.wf.vo.WfProcBizDataBean;
import com.bjzhianjia.scp.security.wf.vo.WfProcVariableDataBean;
import com.bjzhianjia.scp.security.wf.vo.WfProcessDataBean;

/**
 * Description: 工作流流程任务服务业务实现
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.22    mayongming       1.0        1.0 Version
 * </pre>
 */
@Component
public class WfProcTaskBiz extends AWfProcTaskBiz {
	/** 日志处理 **/
	private static Logger logger = LoggerFactory.getLogger(WfProcTaskBiz.class);
	private static String DEFAULTUSER = "System";
	@Autowired
	WfProcBeanMapper wfProcBeanMapper;
	@Autowired
	WfProcTaskBeanMapper iqbWfProcTaskBeanMapper;
	@Autowired
	private WfProcDelegateBeanMapper wfProcDelegateBeanMapper;
	@Autowired
	private WfMyProcBeanMapper wfMyProcBeanMapper;
	@Autowired
	WfProcDesinerBiz wfProcDesinerBiz;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	TaskService taskService;
	@Autowired
	RepositoryService repositoryService;
	@Autowired
	HistoryService historyService;
	@Autowired
	IWfProcTaskNotify wfProcTaskNotify;

	/**
	 * 根据流程定义Key启动对应的工作流
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 * @return 流程实例ID
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public String startProcessInstanceByKey(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException, Exception {
		checkStartingProcessInstanc(procData, procVarData, authData, bizData,
				WfProcessStartModeAttr.PROC_DEFKEY);
		return startProcessInstance(procData, procVarData, authData, bizData,
				WfProcessStartModeAttr.PROC_DEFKEY);
	}

	/**
	 * 根据流程定义ID启动对应的工作流
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 * @return 流程实例ID
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public String startProcessInstanceById(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException, Exception {
		checkStartingProcessInstanc(procData, procVarData, authData, bizData,
				WfProcessStartModeAttr.PROC_DEFID);
		return startProcessInstance(procData, procVarData, authData, bizData,
				WfProcessStartModeAttr.PROC_DEFKEY);
	}

	/**
	 * 流程启动前的参数校验
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @param type
	 *            流程启动方式
	 * @throws WorkflowException
	 */
	private void checkStartingProcessInstanc(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData, int type) throws WorkflowException {
		if (type == WfProcessStartModeAttr.PROC_DEFKEY) {
			// 如果没有指定流程定义编码，则不能启动工作流，抛出异常
			if (StringUtil.isNull(procData.getProcDefKey())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020101);
			}
		} else {
			// 如果没有指定流程定义ID，则不能启动工作流，抛出异常
			if (StringUtil.isNull(procData.getProcDefId())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020102);
			}
		}
	}

	/**
	 * 根据输入参数启动工作流
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @param type
	 *            流程启动方式
	 * @throws WorkflowException
	 * @throws Exception
	 * @return 流程实例ID
	 */
	private String startProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData, int type) throws WorkflowException, Exception {
		// 启动流程
		ProcessInstance processInstance = null;

		if (type == WfProcessStartModeAttr.PROC_DEFKEY) {
			processInstance = startProcessByKey(procData.getProcDefKey(),
					bizData.getProcBizId(), procVarData.getVariableData());
		} else {
			processInstance = startProcessById(procData.getProcDefId(),
					bizData.getProcBizId(), procVarData.getVariableData());
		}

		ProcessDefinitionEntity procDef = wfProcDesinerBiz
				.getDeployedProcessDefinition(processInstance
						.getProcessDefinitionId());

		// 业务流程数据准备
		WfProcBean wfProcBean = creatProcessData(procVarData, authData,
				processInstance.getId(), procDef);

		// 得到启动节点流程任务
		logger.debug("启动流程--获取启动流程任务...");
		Task task = getProcTaskEntityByInstance(processInstance.getId());
		logger.debug("启动流程--获取启动流程任务结束.");

		// 获取流程定义任务节点配置的任务属性数据
		WfProcTaskPropertiesBean properties = wfProcDesinerBiz
				.getTaskProperties(task.getId());

		// 流程任务数据准备
		WfProcTaskBean wfProcTaskBean = createProcStartTaskData(wfProcBean, task, properties);

		// 对业务数据设置当前流程任务代码，没有下级审批任务和审批结论
		ProcTaskData procTaskData = new ProcTaskData();
		procTaskData.setProcInstanceId(processInstance.getId());
		procTaskData.setProcTaskCode(task.getTaskDefinitionKey());
		procTaskData.setProcTaskId(wfProcTaskBean.getProcCtaskid());

		// 获取当前流程任务自定义属性
		Map<String, String> procTaskSelfProps = getProcTaskSelfProperties(properties);

		logger.info("启动流程--开始启动业务流程(" + processInstance.getId() + "),业务ID("
				+ bizData.getProcBizId() + ")...");

		try {
			// 在流程任务启动前，调用任务回调函数
			String callbackClass = getProcTaskCallback(properties);
			beforeCallback(WfProcDealType.PROC_START, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			// 如果没有指定关联业务ID，不能启动工作流，抛出异常
			if (StringUtil.isNull(bizData.getProcBizId())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020103);
			}

			// 如果没有指定关联业务的所属机构，不能启动工作流
			if (StringUtil.isNull(bizData.getProcOrgCode())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020104);
			}

			wfProcBean.setProcBizid(bizData.getProcBizId());
			wfProcBean.setProcBiztype(bizData.getProcBizType());
			wfProcBean.setProcOrgcode(bizData.getProcOrgCode());
			wfProcBean.setProcMemo(bizData.getProcBizMemo());

			// 对流程开始节点任务进行任务自动签收操作
			taskService.claim(wfProcTaskBean.getProcCtaskid(),
					wfProcTaskBean.getProcTaskAssignee());

			// 在流程任务提交后，调用任务回调函数
			afterCallback(WfProcDealType.PROC_START, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			insertWfProcess(wfProcBean);
			insertWfProcessTask(wfProcTaskBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		} finally {
			logger.info("启动流程--启动业务流程(" + processInstance.getId() + "),业务ID("
					+ bizData.getProcBizId() + ")结束.");
		}

		return processInstance.getId();
	}

	/**
	 * 通过流程定义key启动工作流引擎
	 * 
	 * @param processDefinitionKey
	 *            流程定义编码
	 * @param variables
	 *            流程参数
	 * @return
	 * @throws WorkflowException
	 */
	private ProcessInstance startProcessByKey(String processDefinitionKey,
			String businessKey, Map<String, Object> variables)
			throws WorkflowException {
		try {
			if (null != variables) {
				return runtimeService.startProcessInstanceByKey(
						processDefinitionKey, businessKey, variables);
			} else {
				return runtimeService.startProcessInstanceByKey(
						processDefinitionKey, businessKey);
			}
		} catch (ActivitiObjectNotFoundException aonfe) { // 未找到指定的流程定义
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020105, aonfe);
		} catch (ActivitiException ae) { // 流程定义已挂起
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020106, ae);
		} catch (Exception e) { // 其他异常情况
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		}
	}

	/**
	 * 通过流程定义Id启动工作流引擎
	 * 
	 * @param processDefinitionId
	 *            流程定义ID
	 * @param variables
	 *            流程参数
	 * @return
	 * @throws WorkflowException
	 */
	private ProcessInstance startProcessById(String processDefinitionId,
			String businessKey, Map<String, Object> variables)
			throws WorkflowException {
		try {
			// 创建默认流程引擎
			ProcessEngine processEngine = ProcessEngines
					.getDefaultProcessEngine();
			RuntimeService runtimeService = processEngine.getRuntimeService();

			if (null != variables) {
				return runtimeService.startProcessInstanceById(
						processDefinitionId, businessKey, variables);
			} else {
				return runtimeService.startProcessInstanceById(
						processDefinitionId, businessKey);
			}
		} catch (ActivitiObjectNotFoundException aonfe) { // 未找到指定的流程定义
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020105, aonfe);
		} catch (ActivitiException ae) { // 流程定义已挂起
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020106, ae);
		} catch (Exception e) { // 其他异常情况
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		}
	}

	/**
	 * 通过流程实例Id得到工作流引擎
	 * 
	 * @param procInstid
	 *            流程实例Id
	 * @return
	 */
	private ProcessInstance getProcessInstance(String procInstid) {
		return runtimeService.createProcessInstanceQuery()
				.processInstanceId(procInstid).singleResult();
	}

	/**
	 * 通过流程实例Id得到工作流引擎列表
	 * 
	 * @param procInstid
	 *            流程实例Id
	 * @return
	 */
	private List<ProcessInstance> getProcessInstances(String procInstid) {
		return runtimeService.createProcessInstanceQuery()
				.processInstanceId(procInstid).list();

	}

	/**
	 * 流程任务完成前的业务回调函数
	 * 
	 * @param dealType
	 *            流程处理类型
	 * @param procTaskData
	 *            流程任务数据
	 * @param procTaskSelfProps
	 *            流程任务自定义属性
	 * @param callbackClass
	 *            流程任务回调类
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 */
	private void beforeCallback(String dealType, ProcTaskData procTaskData,
			Map<String, String> procTaskSelfProps, String callbackClass,
			Map<String, Object> bizData) throws WorkflowException {
		IWfProcTaskCallBackBaseService service = createProcTaskCallBackService(callbackClass);

		try {
			if (service != null) {
				logger.info("流程处理--开始流程任务(" + procTaskData.getProcTaskId()
						+ ")处理前业务回调处理(" + service.getClass().getName() + ")...");
				Map<String, Object> taskData = getProcTaskData(procTaskData);
				
				if (service instanceof IWfProcTaskCallBackService) {
					bizData.putAll(taskData);
					bizData.putAll(procTaskSelfProps);
					
					((IWfProcTaskCallBackService)service).before(dealType, bizData);
				} else if (service instanceof IWfProcTaskNewCallBackService) {
					((IWfProcTaskNewCallBackService) service).before(dealType,
							taskData, procTaskSelfProps, bizData);
				} else {
					throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020804);
				}

				logger.info("流程处理--流程任务(" + procTaskData.getProcTaskId()
						+ ")处理前业务回调处理(" + service.getClass().getName() + ")结束.");
			}
		} catch (Exception e) {
			logger.error("调用回调类" + service.getClass().getName() + "失败：", e);
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020802, e);
		}
	}

	/**
	 * 流程任务数据转换
	 * 
	 * @param procTaskData
	 *            流程任务数据
	 * @return
	 */
	private Map<String, Object> getProcTaskData(ProcTaskData procTaskData) {
		Map<String, Object> taskData = new HashMap<String, Object>();
		
		if (null != procTaskData.getProcInstanceId()) {
			taskData.put(IWfProcTaskCallBackBaseService.PROC_INSTANCEID,
					procTaskData.getProcInstanceId());
		}
		
		if (null != procTaskData.getProcTaskCode()) {
			taskData.put(IWfProcTaskCallBackBaseService.PROC_CURRTASK,
					procTaskData.getProcTaskCode());
		}
		
		if (null != procTaskData.getProcApprStatus()) {
			taskData.put(IWfProcTaskCallBackBaseService.PROC_APPRSTATUS,
					procTaskData.getProcApprStatus());
		}
		
		if (null != procTaskData.getProcNextTasks()) {
			taskData.put(IWfProcTaskCallBackBaseService.PROC_NEXTTASKS,
					procTaskData.getProcNextTasks());
		}
		
		if (null != procTaskData.getProcEnded()) {
			taskData.put(IWfProcTaskCallBackBaseService.PROC_ENDED,
					procTaskData.getProcEnded());
		}
		
		return taskData;
	}
	
	/**
	 * 流程任务完成后的业务回调函数
	 * 
	 * @param dealType
	 *            流程处理类型
	 * @param procTaskData
	 *            流程任务数据
	 * @param procTaskSelfProps
	 *            流程任务自定义属性
	 * @param callbackClass
	 *            流程任务回调类
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 */
	private void afterCallback(String dealType, ProcTaskData procTaskData,
			Map<String, String> procTaskSelfProps, String callbackClass,
			Map<String, Object> bizData) throws WorkflowException {
		IWfProcTaskCallBackBaseService service = createProcTaskCallBackService(callbackClass);
		
		try {
			if (service != null) {
				logger.info("流程处理--开始流程任务(" + procTaskData.getProcTaskId()
						+ ")处理后业务回调处理(" + service.getClass().getName() + ")...");
				Map<String, Object> taskData = getProcTaskData(procTaskData);
				
				if (service instanceof IWfProcTaskCallBackService) {
					bizData.putAll(taskData);
					bizData.putAll(procTaskSelfProps);
					
					((IWfProcTaskCallBackService)service).after(dealType, bizData);
				} else if (service instanceof IWfProcTaskNewCallBackService) {
					((IWfProcTaskNewCallBackService) service).after(dealType,
							taskData, procTaskSelfProps, bizData);
				} else {
					throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020804);
				}
				logger.info("流程处理--流程任务(" + procTaskData.getProcTaskId()
						+ ")处理后业务回调处理(" + service.getClass().getName() + ")结束.");
			}
		} catch (Exception e) {
			logger.error("调用回调类" + service.getClass().getName() + "失败：", e);
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020803, e);
		}
	}

	/**
	 * 根据流程定义Key启动对应的工作流并自动完成启动任务的提交
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 * @return 流程实例ID
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public String startAndCompleteProcessInstanceByKey(
			WfProcessDataBean procData, WfProcVariableDataBean procVarData,
			WfProcAuthDataBean authData, WfProcBizDataBean bizData)
			throws WorkflowException, Exception {
		checkStartingProcessInstanc(procData, procVarData, authData, bizData,
				WfProcessStartModeAttr.PROC_DEFKEY);
		return startAndCompleteProcessInstance(procData, procVarData, authData,
				bizData, WfProcessStartModeAttr.PROC_DEFKEY);
	}

	/**
	 * 根据流程定义ID启动对应的工作流并自动完成启动任务的提交
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 * @return 流程实例ID
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public String startAndCompleteProcessInstanceById(
			WfProcessDataBean procData, WfProcVariableDataBean procVarData,
			WfProcAuthDataBean authData, WfProcBizDataBean bizData)
			throws WorkflowException, Exception {
		checkStartingProcessInstanc(procData, procVarData, authData, bizData,
				WfProcessStartModeAttr.PROC_DEFID);
		return startAndCompleteProcessInstance(procData, procVarData, authData,
				bizData, WfProcessStartModeAttr.PROC_DEFID);
	}

	/**
	 * 根据输入参数启动工作流并自动完成启动任务的提交
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @param type
	 *            流程启动方式
	 * @throws WorkflowException
	 * @throws Exception
	 * @return 流程实例ID
	 */
	private String startAndCompleteProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData, int type) throws WorkflowException, Exception {
		// 启动流程
		ProcessInstance processInstance = null;

		if (type == WfProcessStartModeAttr.PROC_DEFKEY) {
			processInstance = startProcessByKey(procData.getProcDefKey(),
					bizData.getProcBizId(), procVarData.getVariableData());
		} else {
			processInstance = startProcessById(procData.getProcDefId(),
					bizData.getProcBizId(), procVarData.getVariableData());
		}

		ProcessDefinitionEntity procDef = wfProcDesinerBiz
				.getDeployedProcessDefinition(processInstance
						.getProcessDefinitionId());

		// 业务流程数据准备
		WfProcBean wfProcBean = creatProcessData(procVarData, authData,
				processInstance.getId(), procDef);

		// 得到启动节点流程任务
		Task task = getProcTaskEntityByInstance(processInstance.getId());

		// 获取流程定义任务节点配置的任务属性数据
		WfProcTaskPropertiesBean properties = wfProcDesinerBiz
				.getTaskProperties(task.getId());

		// 获取流程实例下一任务列表
		List<WfProcTaskBean> wfNextTasksBean = new ArrayList<WfProcTaskBean>();
		List<WfProcTaskBean> wfProcTaskBeans = new ArrayList<WfProcTaskBean>();

		// 流程任务数据准备
		WfProcTaskBean wfProcTaskBean = createProcStartTaskData(wfProcBean,
				task, properties);
		wfProcTaskBean.setProcTaskEndtime(wfProcBean.getProcCreatetime()); // 启动流程并提交的完成时间
		wfProcTaskBean.setProcTaskApprStatus(FlowStatus.CHECK01.getRetCode()); // 启动流程并提交，状态为通过
		wfProcTaskBean.setProcTaskApprOpinion(StringUtil.isNull(procVarData
				.getProcApprOpinion()) ? "申请并提交" : procVarData
				.getProcApprOpinion()); // 启动流程并提交，流程审批意见
		wfProcTaskBean.setProcTaskStatus(FlowStatus.TASK03.getRetCode()); // 启动流程并提交，流程任务状态为已处理
		wfProcTaskBeans.add(wfProcTaskBean);

		// 我的流程任务列表信息。包括流程创建者、流程处理人、流程委托人、流程订阅者等
		// 启动节点不允许使用委托授权方式，所以不生成委托人的我的流程数据
		List<WfMyProcBean> wfMyProcBeans = new ArrayList<WfMyProcBean>();

		String displayUrl = getProcDisplayUrl(properties);
		String detailUrl = getProcDetailUrl(properties);
		detailUrl = StringUtil.isNull(detailUrl) ? displayUrl : detailUrl;

		// 设置流程详情页面URL
		wfProcBean.setProcDisplayurl(detailUrl);

		java.util.Set<String> myTaskUsers = new java.util.HashSet<String>();

		// 生成流程创建者的我的流程数据
		logger.debug("启动流程--开始生成流程创建者的流程(" + processInstance.getId() + ")数据...");
		wfMyProcBeans.add(createMyProcess(wfProcTaskBean,
				authData.getProcTaskUser(),
				ProcUserType.USERTYPE01.getRetCode(), displayUrl));
		myTaskUsers.add(authData.getProcTaskUser());
		logger.debug("启动流程--生成流程创建者的流程(" + processInstance.getId() + ")数据结束.");

		// 生成流程订阅者的我的流程数据
		logger.debug("启动流程--开始生成流程订阅者的流程(" + processInstance.getId() + ")数据...");
		wfMyProcBeans.addAll(createSubscriberProcess(wfProcTaskBean,
				myTaskUsers, bizData.getProcOrgCode(), displayUrl));
		logger.debug("启动流程--生成流程订阅者的流程(" + processInstance.getId() + ")数据结束.");

		// 对业务数据设置当前流程任务代码和审批结论，未提交没有下级审批任务
		ProcTaskData procTaskData = new ProcTaskData();
		procTaskData.setProcInstanceId(processInstance.getId());
		procTaskData.setProcTaskCode(task.getTaskDefinitionKey());
		procTaskData.setProcApprStatus(FlowStatus.CHECK01.getRetCode());
		procTaskData.setProcTaskId(task.getId());

		// 获取当前流程任务自定义属性
		Map<String, String> procTaskSelfProps = getProcTaskSelfProperties(properties);

		logger.info("启动流程--开始启动业务流程(" + processInstance.getId() + "),业务ID("
				+ bizData.getProcBizId() + ")...");

		try {
			// 在流程任务审批提交前，调用任务审批提交前的任务回调函数
			String callbackClass = getProcTaskCallback(properties);
			beforeCallback(WfProcDealType.PROC_COMMIT, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			// 如果没有指定关联业务ID，不能启动工作流，抛出异常
			if (StringUtil.isNull(bizData.getProcBizId())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020103);
			}

			// 如果没有指定关联业务的所属机构，不能启动工作流
			if (StringUtil.isNull(bizData.getProcOrgCode())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020104);
			}

			wfProcBean.setProcBizid(bizData.getProcBizId());
			wfProcBean.setProcBiztype(bizData.getProcBizType());
			wfProcBean.setProcOrgcode(bizData.getProcOrgCode());
			wfProcBean.setProcMemo(bizData.getProcBizMemo());

			procVarData.setProcBiztype(bizData.getProcBizId());
			procVarData.setProcOrgcode(bizData.getProcOrgCode());

			// 对流程开始节点任务进行自动提交操作
			logger.debug("启动流程--开始提交业务流程(" + processInstance.getId()
					+ "),业务ID(" + bizData.getProcBizId() + ")...");
			taskService.complete(task.getId(), procVarData.getVariableData());
			logger.debug("启动流程--提交业务流程(" + processInstance.getId() + "),业务ID("
					+ bizData.getProcBizId() + ")结束.");

			// 初始化下一级审批任务列表
			List<String> nextTasks = new ArrayList<String>();

			// 判断流程实例是否结束。主要解决只有一个流程节点的情况。
			if (isProcessEnded(wfProcBean.getProcInstId())) {
				// 如果流程实例已结束，则重置业务流程表流程状态数据和流程结束时间
				wfProcBean.setProcStatus(FlowStatus.PROC16.getRetCode());
				wfProcBean.setProcEndtime(wfProcBean.getProcCreatetime());

				// 对业务数据设置流程是否结束标识
				procTaskData.setProcEnded(DictKeyConst.YESORNO_YES);
			} else {
				// 获取流程实例下一任务列表
				wfNextTasksBean = getNextProcTasks(wfProcTaskBean, true,
						DictKeyConst.YESORNO_NO, procVarData.getProcAssignee(),
						wfProcBean.getProcCreatetime());

				// 将下一任务列表添加到List列表中，并更新到流程任务列表中
				if (wfNextTasksBean != null && wfNextTasksBean.size() > 0) {
					for (WfProcTaskBean iqbWfProcTaskBean : wfNextTasksBean) {
						wfProcTaskBeans.add(iqbWfProcTaskBean);
						nextTasks.add(iqbWfProcTaskBean.getProcCtaskcode());
					}
				}

				// 对业务数据设置流程是否结束标识
				procTaskData.setProcEnded(DictKeyConst.YESORNO_NO);
			}

			// 对业务数据设置下级审批任务列表
			procTaskData.setProcNextTasks(nextTasks);

			// 在流程任务审批完成后，调用任务审批完成后的任务回调函数
			afterCallback(WfProcDealType.PROC_COMMIT, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			// 写入流程业务数据
			insertWfProcess(wfProcBean);

			// 写入流程任务数据
			insertWfProcessTasks(wfProcTaskBeans);

			// 写入我的流程数据
			insertWfMyProcess(wfMyProcBeans);

			// 发送流程新任务到达通知
			wfProcTaskNotify.notify(wfProcBean, wfNextTasksBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		} finally {
			logger.info("启动流程--启动业务流程(" + processInstance.getId() + "),业务ID("
					+ bizData.getProcBizId() + ")结束.");
		}

		return processInstance.getId();
	}

	/**
	 * 生成我的流程数据
	 * 
	 * @param wfProcTask
	 *            流程任务
	 * @param procUser
	 *            流程用户
	 * @param procUserType
	 *            流程用户类型
	 * @param displayUrl
	 *            流程查看页面URL
	 * @return
	 */
	private WfMyProcBean createMyProcess(WfProcTaskBean wfProcTask,
			String procUser, String procUserType, String displayUrl) {
		WfMyProcBean wfMyProcBean = new WfMyProcBean();
		wfMyProcBean.setProcInstId(wfProcTask.getProcInstId());
		wfMyProcBean.setProcUser(procUser);
		wfMyProcBean.setProcUserType(procUserType);
		wfMyProcBean.setProcTaskid(wfProcTask.getProcCtaskid());
		wfMyProcBean.setProcTaskcode(wfProcTask.getProcCtaskcode());
		wfMyProcBean.setProcTaskname(wfProcTask.getProcCtaskname());
		wfMyProcBean.setProcDisplayurl(displayUrl);

		return wfMyProcBean;
	}

	/**
	 * 根据相关参数查询流程订阅者，并生成我的流程数据列表
	 * 
	 * @param wfProcTask
	 *            流程任务
	 * @param myTaskUsers
	 *            排除的用户列表
	 * @param procOrgCode
	 *            机构代码
	 * @param displayUrl
	 *            流程查看页面URL
	 * @return
	 */
	private List<WfMyProcBean> createSubscriberProcess(
			WfProcTaskBean wfProcTask, java.util.Set<String> myTaskUsers,
			String procOrgCode, String displayUrl) {
		List<WfMyProcBean> wfMyProcBeans = new ArrayList<WfMyProcBean>();

		// 生成流程订阅者的我的流程数据
		List<String> subscribeUsers = getProcSubscribeUsers(
				wfProcTask.getProcKey(), procOrgCode);
		if (subscribeUsers != null && subscribeUsers.size() > 0) {
			for (String subscribeUser : subscribeUsers) {
				if (!myTaskUsers.contains(subscribeUser)) {
					WfMyProcBean wfSubscribeProcBean = new WfMyProcBean();
					wfSubscribeProcBean.setProcInstId(wfProcTask
							.getProcInstId());
					wfSubscribeProcBean.setProcUser(subscribeUser);
					wfSubscribeProcBean.setProcUserType(ProcUserType.USERTYPE03
							.getRetCode());
					wfSubscribeProcBean.setProcTaskid(wfProcTask
							.getProcCtaskid());
					wfSubscribeProcBean.setProcTaskcode(wfProcTask
							.getProcCtaskcode());
					wfSubscribeProcBean.setProcTaskname(wfProcTask
							.getProcCtaskname());
					wfSubscribeProcBean.setProcDisplayurl(displayUrl);
					wfMyProcBeans.add(wfSubscribeProcBean);
				}
			}
		}

		return wfMyProcBeans;
	}

	/**
	 * 根据流程定义Id和机构代码获取该流程的订阅用户列表
	 * 
	 * @param procDefKey
	 *            流程定义ID
	 * @param procOrgCode
	 *            机构代码
	 * @return
	 */
	private List<String> getProcSubscribeUsers(String procDefKey,
			String procOrgCode) {
		return null;
	}

	/**
	 * 判断流程是否已经结束，通过流程实例ID查询流程实例，如果流程实例为空则表示该流程已经结束，否则未结束
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @return 流程已经结束则返回true，否则返回false
	 */
	private boolean isProcessEnded(String procInstId) {
		List<ProcessInstance> procInsts = getProcessInstances(procInstId);

		return (procInsts == null || procInsts.size() == 0);
	}

	/**
	 * 根据输入参数对流程任务进行签收
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public synchronized WfProcTaskBean claimProcessInstance(
			WfProcessDataBean procData, WfProcVariableDataBean procVarData,
			WfProcAuthDataBean authData, WfProcBizDataBean bizData)
			throws WorkflowException, Exception {
		// 如果没有指定流程任务ID，则不能进行流程任务签收操作
		if (StringUtil.isNull(procData.getProcTaskId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020301);
		}

		// 查询可签收流程任务，流程任务状态为待签收
		WfProcTaskBean procTask = getProcessTask(procData.getProcTaskId(),
				getProcTaskStatus(1));

		return claimProcessInstance(procData, procVarData, authData, bizData,
				procTask);
	}

	/**
	 * 根据输入参数对流程任务进行签收
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @param procTask
	 *            流程任务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	private WfProcTaskBean claimProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData, WfProcTaskBean procTask)
			throws WorkflowException, Exception {
		// 通过流程任务ID没有找到流程任务，不能进行流程任务签收
		if (procTask == null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020302);
		}

		// 流程状态校验，只有审批中的流程才能进行流程任务签收操作
		checkProcessStatus(procTask.getProcInstId());

		// 是否委托授权处理
		boolean isDelegate = false;
		WfProcTaskBean wfProcTaskBean = new WfProcTaskBean();

		logger.info("签收流程--开始签收业务流程(" + procTask.getProcInstId() + "),流程任务("
				+ procTask.getProcCtaskname() + ")...");

		logger.debug("签收流程--开始处理签收业务(" + procTask.getProcInstId() + "),流程任务("
				+ procTask.getProcCtaskname() + ")的委托关系...");
		// 从委托授权表中获取当前流程任务的委托授权人列表
		List<WfProcDelegateBean> delegates = getProcInstDelegateList(
				procTask.getProcInstId(), authData.getProcTaskUser());

		// 循环处理委托关系，委托授权人所属角色与流程任务候选用户组一致时委托关系才能成立
		for (WfProcDelegateBean delegate : delegates) {
			if (procTask.getProcTaskGroup().equals(
					delegate.getProcLicensorRole())) {
				wfProcTaskBean.setProcLicensor(delegate.getProcLicensor()); // 流程任务委托授权人
				wfProcTaskBean.setProcMandatary(authData.getProcTaskUser()); // 流程任务委托处理人
				isDelegate = true;
				break;
			}
		}

		logger.debug("签收流程--处理签收业务(" + procTask.getProcInstId() + "),流程任务("
				+ procTask.getProcCtaskname() + ")的委托关系结束.");

		// 如果非委托授权处理，且候选用户组与处理用户所属角色不同时，则该用户不具有流程任务签收权限
		if (!isDelegate
				&& !authData.getProcTaskRoles().contains(
						procTask.getProcTaskGroup())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020303);
		}

		wfProcTaskBean.setProcCtaskid(procTask.getProcCtaskid());
		wfProcTaskBean.setProcTaskAssignee(authData.getProcTaskUser()); // 流程任务签收人为流程任务处理人
		wfProcTaskBean.setProcTaskAssigntime(DateTools.getCurrTime()); // 流程任务签收时间
		wfProcTaskBean.setProcTaskStatus(FlowStatus.TASK02.getRetCode()); // 流程签收后，流程任务状态为待处理

		// 获取流程任务属性
		WfProcTaskPropertiesBean taskProperties = parseTaskProperties(procTask
				.getProcTaskProperties());

		// 对业务数据设置当前流程任务代码
		ProcTaskData procTaskData = new ProcTaskData();
		procTaskData.setProcTaskCode(procTask.getProcCtaskcode());
		procTaskData.setProcTaskId(procTask.getProcCtaskid());

		// 获取当前流程任务自定义属性
		Map<String, String> procTaskSelfProps = getProcTaskSelfProperties(taskProperties);

		// 在流程任务签收前，调用任务签收处理前的任务回调函数
		String callbackClass = getProcTaskCallback(taskProperties);
		beforeCallback(WfProcDealType.PROC_CLAIM, procTaskData,
				procTaskSelfProps, callbackClass, bizData.getBizData());

		try {
			// 对流程任务进行签收操作
			taskService.claim(procTask.getProcCtaskid(), authData.getProcTaskUser());

			// 在流程任务签收完成后，调用任务签收处理完成后的任务回调函数
			afterCallback(WfProcDealType.PROC_CLAIM, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			// 更新流程任务数据，将流程任务设置为已签收
			updateWfProcTask(wfProcTaskBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		} finally {
			logger.info("签收流程--签收业务流程(" + procTask.getProcInstId() + "),流程任务("
					+ procTask.getProcCtaskname() + ")结束.");
		}

		// 生成返回结果
		procTask.setProcLicensor(wfProcTaskBean.getProcLicensor());
		procTask.setProcMandatary(wfProcTaskBean.getProcMandatary());
		procTask.setProcTaskAssignee(wfProcTaskBean.getProcTaskAssignee());
		procTask.setProcTaskAssigntime(wfProcTaskBean.getProcTaskAssigntime());
		procTask.setProcTaskStatus(wfProcTaskBean.getProcTaskStatus());

		return procTask;
	}

	/**
	 * 判断流程实例是否已暂停。目前从业务流程表流程状态进行判断，未来扩展通过Execution判断每个流程任务的挂起状态
	 * ProcessEngines.getDefaultProcessEngine
	 * ().getRuntimeService().createExecutionQuery
	 * ().executionId(exceId).singleResult().isSuspended(); 如果流程状态是审批中则返回true
	 * 如果流程状态是暂停则抛出WorkflowException异常 如果流程状态是已终止、已删除、已完成时，则抛出WorkflowException异常
	 * 
	 * @param procInstId
	 * @return
	 * @throws WorkflowException
	 */
	private boolean checkProcessStatus(String procInstId) throws WorkflowException {
		return checkProcessStatus(getWfProcess(procInstId));
	}

	/**
	 * 判断流程实例是否已暂停。目前从业务流程表流程状态进行判断，未来扩展通过Execution判断每个流程任务的挂起状态
	 * ProcessEngines.getDefaultProcessEngine
	 * ().getRuntimeService().createExecutionQuery
	 * ().executionId(exceId).singleResult().isSuspended(); 如果流程状态是审批中则返回true
	 * 如果流程状态是暂停则抛出WorkflowException异常 如果流程状态是已终止、已删除、已完成时，则抛出WorkflowException异常
	 * 
	 * @param wfProcBean
	 * @return
	 * @throws WorkflowException
	 */
	private boolean checkProcessStatus(WfProcBean wfProcBean)
			throws WorkflowException {
		if (wfProcBean != null) {
			if (FlowStatus.PROC13.getRetCode().equalsIgnoreCase(
					wfProcBean.getProcStatus())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020003);
			} else if (FlowStatus.PROC10.getRetCode().equalsIgnoreCase(
					wfProcBean.getProcStatus())) {
				return true;
			} else {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020002);
			}
		} else {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020001);
		}
	}

	/**
	 * 更新流程任务签收数据
	 * 
	 * @param wfProcTaskBeans
	 * @throws WorkflowException
	 */
	private int updateWfProcTask(WfProcTaskBean wfProcTaskBean)
			throws WorkflowException {
		if (null != wfProcTaskBean) {
			// setDb(0, super.MASTER);
			return iqbWfProcTaskBeanMapper.updateTaskByTaskid(wfProcTaskBean);
		} else {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020202);
		}
	}

	/**
	 * 批量更新流程任务数据
	 * 
	 * @param wfProcTasks
	 *            流程任务数据列表
	 * @throws WorkflowException
	 */
	private void updateWfProcTasks(List<WfProcTaskBean> wfProcTasks)
			throws WorkflowException {
		if (null != wfProcTasks && wfProcTasks.size() > 0) {
			// setDb(0, super.MASTER);
			for (WfProcTaskBean task : wfProcTasks) {
				iqbWfProcTaskBeanMapper.updateTaskByTaskid(task);
			}
		} else {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020202);
		}
	}

	/**
	 * 取消流程任务签收
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public WfProcTaskBean unclaimProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException, Exception {
		// 如果没有指定流程任务ID，则不能进行流程任务取消签收操作
		if (StringUtil.isNull(procData.getProcTaskId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020402);
		}

		// 查询可取消签收流程任务，流程任务状态为待处理
		WfProcTaskBean procTask = getProcessTask(procData.getProcTaskId(),
				getProcTaskStatus(2));

		return unclaimProcessInstance(procData, procVarData, authData, bizData,
				procTask);
	}

	/**
	 * 取消流程任务签收
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @param procTask
	 *            流程任务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	private WfProcTaskBean unclaimProcessInstance(
			WfProcessDataBean procData, WfProcVariableDataBean procVarData,
			WfProcAuthDataBean authData, WfProcBizDataBean bizData,
			WfProcTaskBean procTask) throws WorkflowException, Exception {
		// 通过流程任务ID没有找到流程任务，不能取消流程任务签收
		if (procTask == null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020401);
		}

		// 流程状态校验，只有审批中的流程才能进行流程任务取消签收操作
		checkProcessStatus(procTask.getProcInstId());

		// 流程任务指定处理人不为空，则表示上一任务指定了具体的处理人，不能进行流程任务取消签收操作
		if (!StringUtil.isNull(procTask.getProcAppointUsers())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020406);
		}

		// 流程任务签收人与当前用户不一致，不能取消流程任务签收
		if (!authData.getProcTaskUser().equals(procTask.getProcTaskAssignee())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020403);
		}

		WfProcTaskBean wfProcTaskBean = new WfProcTaskBean();
		wfProcTaskBean.setProcCtaskid(procTask.getProcCtaskid());
		wfProcTaskBean.setProcLicensor(null); // 流程任务委托授权人
		wfProcTaskBean.setProcMandatary(null); // 流程任务委托处理人
		wfProcTaskBean.setProcTaskAssignee(null); // 流程任务签收人为流程任务处理人
		wfProcTaskBean.setProcTaskAssigntime(null); // 流程任务签收时间
		wfProcTaskBean.setProcTaskStatus(FlowStatus.TASK01.getRetCode()); // 流程任务状态为待签收

		// 获取流程任务属性
		WfProcTaskPropertiesBean taskProperties = parseTaskProperties(procTask
				.getProcTaskProperties());

		// 对业务数据设置当前流程任务代码
		ProcTaskData procTaskData = new ProcTaskData();
		procTaskData.setProcTaskCode(procTask.getProcCtaskcode());
		procTaskData.setProcTaskId(procTask.getProcCtaskid());

		// 获取当前流程任务自定义属性
		Map<String, String> procTaskSelfProps = getProcTaskSelfProperties(taskProperties);

		// 在流程任务取消签收前，调用任务取消签收前的任务回调函数
		String callbackClass = getProcTaskCallback(taskProperties);
		beforeCallback(WfProcDealType.PROC_UNCLAIM, procTaskData,
				procTaskSelfProps, callbackClass, bizData.getBizData());

		logger.info("取消签收流程--开始取消签收业务流程(" + procTask.getProcInstId()
				+ "),流程任务(" + procTask.getProcCtaskname() + ")...");

		try {
			// 对流程任务取消签收操作
			taskService.unclaim(procTask.getProcCtaskid());

			// 在流程任务取消签收处理完成后，调用任务取消签收完成的任务回调函数
			afterCallback(WfProcDealType.PROC_UNCLAIM, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			// 更新流程任务数据，将流程任务设置为待签收
			updateWfProcTaskForUnclaim(wfProcTaskBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		} finally {
			logger.info("取消签收流程--取消签收业务流程(" + procTask.getProcInstId()
					+ "),流程任务(" + procTask.getProcCtaskname() + ")结束.");
		}

		// 生成返回结果
		procTask.setProcLicensor(wfProcTaskBean.getProcLicensor());
		procTask.setProcMandatary(wfProcTaskBean.getProcMandatary());
		procTask.setProcTaskAssignee(wfProcTaskBean.getProcTaskAssignee());
		procTask.setProcTaskAssigntime(wfProcTaskBean.getProcTaskAssigntime());
		procTask.setProcTaskStatus(wfProcTaskBean.getProcTaskStatus());

		return procTask;
	}

	/**
	 * 流程任务取消签收
	 * 
	 * @param wfProcTaskBeans
	 * @throws WorkflowException
	 */
	private int updateWfProcTaskForUnclaim(WfProcTaskBean wfProcTaskBean)
			throws WorkflowException {
		if (null != wfProcTaskBean) {
			// setDb(0, super.MASTER);
			return iqbWfProcTaskBeanMapper.updateForUnclaim(wfProcTaskBean);
		} else {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020202);
		}
	}

	/**
	 * 通过流程任务ID进行流程委托操作
	 * 
	 * @param procData
	 * @param procVarData
	 * @throws WorkflowException
	 */
	public void delegateProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData)
			throws WorkflowException {
		// 流程不能委托给自己
		if (authData.getProcTaskUser().equals(procVarData.getProcAssignee())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020901);
		}

		// 如果没有指定流程任务ID，则不能进行流程委托操作
		if (StringUtil.isNull(procData.getProcTaskId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020902);
		}

		// 查询可委托流程任务，流程任务状态为待签收
		WfProcTaskBean procTask = getProcessTask(procData.getProcTaskId(),
				getProcTaskStatus(1));

		// 找不到流程数据，不能进行流程委托处理
		if (procTask == null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020903);
		}

		// 登录用户角色与流程任务指定角色不匹配
		if (!authData.getProcTaskRoles().contains(procTask.getProcTaskGroup())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020904);
		}

		delegateProcessInstance(procData, procVarData, authData, procTask);
	}

	/**
	 * 流程委托处理
	 * 
	 * @param procData
	 * @param procVarData
	 * @param procTask
	 * @throws WorkflowException
	 */
	private void delegateProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcTaskBean procTask) throws WorkflowException {
		WfProcDelegateBean wfDelegateMandatary = getDelegateMandatary(
				procTask.getProcInstId(), procVarData.getProcAssignee());

		// 流程实例已经被委托，则不能被重复委托
		if (wfDelegateMandatary != null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020905);
		}

		WfProcDelegateBean wfProcDelegate = getDelegateLicensor(
				procTask.getProcInstId(), authData.getProcTaskUser());

		if (wfProcDelegate != null) {
			wfProcDelegate.setProcMandatary(procVarData.getProcAssignee());
			wfProcDelegate.setProcLicenseTime(DateTools.getCurrTime());
			wfProcDelegate.setProcLicenseIsvalid(WfDataValid.PROC_DATA_VALID);
			updateProcInstDelegate(wfProcDelegate);
		} else {
			wfProcDelegate = new WfProcDelegateBean();
			wfProcDelegate.setProcInstId(procTask.getProcInstId());
			wfProcDelegate.setProcLicensor(authData.getProcTaskUser());
			wfProcDelegate.setProcLicensorRole(procTask.getProcTaskGroup());
			wfProcDelegate.setProcMandatary(procVarData.getProcAssignee());
			wfProcDelegate.setProcLicenseTime(DateTools.getCurrTime());
			wfProcDelegate.setProcLicenseIsvalid(WfDataValid.PROC_DATA_VALID);
			insertProcInstDelegate(wfProcDelegate);
		}
	}

	/**
	 * 通过流程任务ID进行流程委托操作
	 * 
	 * @param procData
	 * @param procVarData
	 * @throws WorkflowException
	 */
	public void cancelProcDelegate(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData)
			throws WorkflowException {
		// 如果没有指定流程实例ID，则不能进行取消流程委托操作
		if (StringUtil.isNull(procData.getProcInstId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021601);
		}

		// 判断受托人是否有待处理的流程任务，如果有待处理的流程任务则不能取消委托
		if (getDelegatedTaskCount(procData.getProcInstId(),
				authData.getProcTaskUser()) > 0) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021602);
		}

		WfProcDelegateBean wfProcDelegate = new WfProcDelegateBean();
		wfProcDelegate.setProcInstId(procData.getProcInstId());
		wfProcDelegate.setProcLicensor(authData.getProcTaskUser());
		wfProcDelegate.setProcLicenseIsvalid(WfDataValid.PROC_DATA_INVALID);
		wfProcDelegate.setProcCancelTime(DateTools.getCurrTime());

		cancelProcDelegate(wfProcDelegate);
	}

	/**
	 * 审批流程任务
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public void completeProcessInstanceByTaskId(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException, Exception {
		// 如果没有指定流程任务ID，则不能进行流程任务提交处理
		if (StringUtil.isNull(procData.getProcTaskId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020502);
		}

		// 如果没有指定流程任务审批结论，则不能进行流程任务提交处理
		if (StringUtil.isNull(procVarData.getProcApprStatus())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020504);
		}

		// 查询可审批流程任务，流程任务状态为待签收和待处理
		WfProcTaskBean procTask = getProcessTask(procData.getProcTaskId(),
				getProcTaskStatus(3));

		completeProcessInstance(procData, procVarData, authData, bizData,
				procTask);
	}

	/**
	 * 审批流程任务
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public void completeProcessInstanceByTaskCode(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException, Exception {
		// 如果没有指定流程实例ID，则不能进行流程任务取消签收操作
		if (StringUtil.isNull(procData.getProcInstId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020505);
		}

		// 如果没有指定流程任务代码，则不能进行流程任务取消签收操作
		if (StringUtil.isNull(procData.getProcTaskCode())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020506);
		}

		// 如果没有指定流程任务审批结论，则不能进行流程任务提交处理
		if (StringUtil.isNull(procVarData.getProcApprStatus())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020504);
		}

		// 查询可审批流程任务，流程任务状态为待签收和待处理
		WfProcTaskBean procTask = getProcessTask(procData.getProcInstId(),
				procData.getProcTaskCode(), getProcTaskStatus(3));

		completeProcessInstance(procData, procVarData, authData, bizData,
				procTask);
	}

	/**
	 * 审批流程任务
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @param procTask
	 *            流程任务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	private void completeProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData, WfProcTaskBean procTask)
			throws WorkflowException, Exception {
		// 通过流程任务ID没有找到流程任务，不能进行流程任务提交操作
		if (procTask == null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020501);
		}

		// 流程任务没有指定处理候选用户组并且没有签收人，不能进行流程任务提交操作
		if (StringUtil.isNull(procTask.getProcTaskAssignee())
				&& StringUtil.isNull(procTask.getProcTaskGroup())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020503);
		}

		// 流程任务已签收时，流程任务签收人与当前用户不一致，不能进行流程任务审批操作
		if (FlowStatus.TASK02.getRetCode().equals(procTask.getProcTaskStatus())
				&& !authData.getProcTaskUser().equals(
						procTask.getProcTaskAssignee())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020508);
		}

		// 获取业务流程数据
		WfProcBean wfProcBean = getWfProcess(procTask.getProcInstId());

		// 流程状态校验，只有审批中的流程才能进行流程任务提交操作
		checkProcessStatus(wfProcBean);

		// 我的流程数据，包括流程处理人、流程委托人
		List<WfMyProcBean> wfMyProcBeans = new ArrayList<WfMyProcBean>();

		// 判断是否为流程起始任务节点，如果是起始任务节点，则需要生成订阅者列表
		boolean isStartTask = StringUtil.isNull(procTask.getProcPtaskid());

		// 初始节点不能进行拒绝处理
		if (isStartTask
				&& FlowStatus.CHECK00.getRetCode().equals(
						procVarData.getProcApprStatus())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020511);
		}

		// 获取流程任务属性
		WfProcTaskPropertiesBean taskProperties = parseTaskProperties(procTask
				.getProcTaskProperties());
		String displayUrl = getProcDisplayUrl(taskProperties);
		String detailUrl = getProcDetailUrl(taskProperties);
		detailUrl = StringUtil.isNull(detailUrl) ? displayUrl : detailUrl;

		int datetime = DateTools.getCurrTime();

		java.util.Set<String> myTaskUsers = new java.util.HashSet<String>();

		// 流程未签收，需要先进行流程签收处理
		if (FlowStatus.TASK01.getRetCode().equals(procTask.getProcTaskStatus())) {
			// 是否委托授权处理
			boolean isDelegate = false;

			logger.debug("审批流程--开始处理审批业务(" + procTask.getProcInstId()
					+ "),流程任务(" + procTask.getProcCtaskname() + ")的委托关系...");
			// 从委托授权表中获取当前流程任务的委托授权人列表
			List<WfProcDelegateBean> delegates = getProcInstDelegateList(
					procTask.getProcInstId(), authData.getProcTaskUser());

			// 循环处理委托关系，委托授权人所属角色与流程任务候选用户组一致时委托关系才能成立
			for (WfProcDelegateBean delegate : delegates) {
				if (procTask.getProcTaskGroup().equals(
						delegate.getProcLicensorRole())) {
					procTask.setProcLicensor(delegate.getProcLicensor()); // 流程任务委托授权人
					procTask.setProcMandatary(authData.getProcTaskUser()); // 流程任务委托处理人
					isDelegate = true;
					break;
				}
			}

			logger.debug("审批流程--处理审批业务(" + procTask.getProcInstId() + "),流程任务("
					+ procTask.getProcCtaskname() + ")的委托关系结束.");

			// 如果非委托授权处理，且候选用户组与当前用户所属角色不同时，则该用户不具有流程任务审批权限
			if (!isDelegate
					&& !authData.getProcTaskRoles().contains(
							procTask.getProcTaskGroup())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020507);
			}

			procTask.setProcTaskAssignee(authData.getProcTaskUser()); // 流程任务签收人为流程任务处理人
			procTask.setProcTaskAssigntime(datetime); // 流程任务签收时间

			// 生成流程处理人的我的流程数据
			logger.debug("审批流程--开始生成流程处理人的流程(" + procTask.getProcInstId()
					+ ")数据...");
			wfMyProcBeans.add(createMyProcess(procTask,
					authData.getProcTaskUser(),
					ProcUserType.USERTYPE02.getRetCode(), displayUrl));
			myTaskUsers.add(authData.getProcTaskUser());
			logger.debug("审批流程--生成流程处理人的流程(" + procTask.getProcInstId()
					+ ")数据结束.");

			// 生成流程委托人的我的流程数据
			if (isDelegate) {
				logger.debug("审批流程--开始生成流程委托人的流程(" + procTask.getProcInstId()
						+ ")数据...");
				wfMyProcBeans.add(createMyProcess(procTask,
						procTask.getProcLicensor(),
						ProcUserType.USERTYPE04.getRetCode(), displayUrl));
				myTaskUsers.add(procTask.getProcLicensor());
				logger.debug("审批流程--生成流程委托人的流程(" + procTask.getProcInstId()
						+ ")数据结束.");
			}
		} else {
			// 生成流程处理人的我的流程数据
			logger.debug("审批流程--开始生成流程处理人的流程(" + procTask.getProcInstId()
					+ ")数据...");
			wfMyProcBeans.add(createMyProcess(procTask,
					procTask.getProcTaskAssignee(),
					ProcUserType.USERTYPE02.getRetCode(), displayUrl));
			myTaskUsers.add(procTask.getProcTaskAssignee());
			logger.debug("审批流程--生成流程处理人的流程(" + procTask.getProcInstId()
					+ ")数据结束.");

			// 生成流程委托人的我的流程数据
			if (!StringUtil.isNull(procTask.getProcLicensor())) {
				logger.debug("审批流程--开始生成流程委托人的流程(" + procTask.getProcInstId()
						+ ")数据...");
				wfMyProcBeans.add(createMyProcess(procTask,
						procTask.getProcLicensor(),
						ProcUserType.USERTYPE04.getRetCode(), displayUrl));
				myTaskUsers.add(procTask.getProcLicensor());
				logger.debug("审批流程--生成流程委托人的流程(" + procTask.getProcInstId()
						+ ")数据结束.");
			}
		}

		// 生成流程订阅者的我的流程数据
		if (isStartTask) {
			logger.debug("审批流程--开始生成流程订阅者的流程(" + procTask.getProcInstId()
					+ ")数据...");
			wfMyProcBeans.addAll(createSubscriberProcess(procTask, myTaskUsers,
					bizData.getProcOrgCode(), displayUrl));
			logger.debug("审批流程--生成流程订阅者的流程(" + procTask.getProcInstId()
					+ ")数据结束.");
		}

		// 对业务数据设置当前流程任务代码和审批结论，未提交没有下级审批任务
		ProcTaskData procTaskData = new ProcTaskData();
		procTaskData.setProcTaskCode(procTask.getProcCtaskcode());
		procTaskData.setProcApprStatus(procVarData.getProcApprStatus());
		procTaskData.setProcTaskId(procTask.getProcCtaskid());

		// 获取当前流程任务自定义属性
		Map<String, String> procTaskSelfProps = getProcTaskSelfProperties(taskProperties);
		
		List<String> nextTasks = new ArrayList<String>();
		List<WfProcTaskBean> wfNextTasksBean = null;
		List<WfMyProcBean> allNotifyProcess = null;

		// 在流程任务审批提交前，调用任务审批提交前的任务回调函数
		String callbackClass = getProcTaskCallback(taskProperties);
		beforeCallback(WfProcDealType.PROC_APPROVE, procTaskData,
				procTaskSelfProps, callbackClass, bizData.getBizData());

		WfProcBean wfProcBean1 = new WfProcBean();
		wfProcBean1.setProcInstId(wfProcBean.getProcInstId());
		wfProcBean1.setProcDisplayurl(detailUrl);
		// 如果传入了流程摘要且与原流程摘要不同，则更新业务流程摘要信息
		if (!StringUtil.isNull(bizData.getProcBizMemo())
				&& !bizData.getProcBizMemo().equals(wfProcBean.getProcMemo())) {
			wfProcBean1.setProcMemo(bizData.getProcBizMemo());
		}

		boolean isPassed = FlowStatus.CHECK01.getRetCode().equals(
				procVarData.getProcApprStatus());
		boolean isRetruned = FlowStatus.CHECK02.getRetCode().equals(
				procVarData.getProcApprStatus());

		String appointTask = null;
		if (isPassed) {
			// 流程任务审批通过，获取下个处理任务，如果指定指定则跳转到指定任务，否则跳转到流程配置的任务
			appointTask = procVarData.getProcAppointTask();
		} else if (isRetruned) {
			// 流程任务审批被退回，获取回退节点，将任务跳转到回退节点
			appointTask = getProcAppointTask(taskProperties,
					procVarData.getProcAppointTask());
		} else {
			// 流程任务被拒绝，则按照流程配置结束流程
		}

		// 开始流程审批处理
		logger.info("审批流程--开始审批业务流程(" + procTask.getProcInstId() + "),流程任务("
				+ procTask.getProcCtaskname() + ")...");
		List<WfProcTaskBean> wfProcTaskList = new ArrayList<WfProcTaskBean>();

		try {
			String parallel = DictKeyConst.YESORNO_NO;
			boolean isVoteTask = DictKeyConst.YESORNO_YES.equals(procTask
					.getProcVotetask());
			String votePower = procTask.getProcVotepower();

			// 设置业务类型和组织机构的流程参数
			procVarData.setProcBiztype(wfProcBean.getProcBiztype());
			procVarData.setProcOrgcode(wfProcBean.getProcOrgcode());

			// 设置当前流程任务审批信息
			procTask.setProcTaskEndtime(datetime); // 流程任务处理完成时间
			procTask.setProcTaskStatus(FlowStatus.TASK03.getRetCode()); // 流程任务状态为已处理
			procTask.setProcTaskApprStatus(procVarData.getProcApprStatus()); // 流程任务审批结论
			procTask.setProcTaskApprOpinion(procVarData.getProcApprOpinion()); // 流程任务审批意见

			// 当前任务是并行任务
			if (DictKeyConst.YESORNO_YES.equals(procTask.getProcParallel())) {
				// 取得流程定义
				ProcessDefinitionEntity definition = wfProcDesinerBiz
						.getDeployedProcessDefinition(procTask.getProcId());

				// 取得当前活动节点
				ActivityImpl currActivity = wfProcDesinerBiz.getActivity(
						definition, procTask.getProcCtaskcode());

				// 获取当前流程任务所有出口节点连线
				List<PvmTransition> outTransitionList = currActivity
						.getOutgoingTransitions();
				// 如当前任务为并行任务且参与投票决策，之后必须是并行汇聚网关
				PvmActivity outActivity = outTransitionList.get(0)
						.getDestination();

				// 当前任务出口只能是唯一的，否则是流程配置不正确（通过并行网关和互斥网关实现多分支）
				if (outTransitionList == null || outTransitionList.size() == 0
						|| outTransitionList.size() > 1) {
					throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020512);
				}

				boolean isParallelGateway = "parallelGateway"
						.equals(outActivity.getProperty("type"));
				// 未来并行投票的阈值和速决标识的属性要配置到汇聚网关上，目前配置到任务节点上

				// 如果当前任务需参与投票决策，但出口不是汇聚网关，那么流程配置不正确
				if (isVoteTask && !isParallelGateway) {
					throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020513);
				}

				if (isParallelGateway) {
					// 出口为汇聚网关，需要进行并行网关决策判断处理
					List<PvmTransition> inTransitionList = outActivity
							.getIncomingTransitions();
					List<String> parallelTaskCodes = new ArrayList<String>();

					// 汇聚到并行网关的所有用户任务，加入到待处理并行任务列表中
					for (PvmTransition inTransition : inTransitionList) {
						PvmActivity inActivity = inTransition.getSource();
						if ("userTask".equals(inActivity.getProperty("type"))) {
							parallelTaskCodes.add(inActivity.getId());
						}
					}

					List<WfProcTaskBean> parallelTasks = getParallelProcTask(
							procTask.getProcInstId(), parallelTaskCodes);
					boolean flag = parallelTaskCodes.size() == parallelTasks
							.size();

					if (isVoteTask) { // 当前任务是决策任务节点的处理逻辑
						if (DictKeyConst.YESORNO_YES.equals(procTask
								.getProcVotequickly())) { // 流程速决处理逻辑
							// 速决且一票通过或一票拒绝，当前任务处理结束，未完成的任务结束掉
							if (flag
									&& isPassed
									&& WfProcVotePower.PASS.getRetCode()
											.equals(votePower)) {
								parallel = DictKeyConst.YESORNO_NO;
								isPassed = true;
								wfProcTaskList = processTaskVoteQuickly(
										parallelTasks, procTask, procVarData,
										isPassed);
							} else if (flag
									&& !isPassed
									&& WfProcVotePower.VETO.getRetCode()
											.equals(votePower)) {
								parallel = DictKeyConst.YESORNO_NO;
								isPassed = false;
								wfProcTaskList = processTaskVoteQuickly(
										parallelTasks, procTask, procVarData,
										isPassed);
							} else { // 速决，遍历所有并行节点，判断是否达到审批通过阈值或审批拒绝阈值
								List<Float> voteResult = processTaskVoteCaculate(
										parallelTasks, procTask, isPassed, flag);
								float totalVoteWeight = voteResult.get(0)
										.floatValue();
								float passVoteWeight = voteResult.get(1)
										.floatValue();
								float vetoVoteWeight = voteResult.get(2)
										.floatValue();

								// 审批通过的累计数达到了投票阈值，审批通过
								// 审批拒绝的累计数达到了投票总数减投票阈值，审批被拒绝
								if (flag
										&& passVoteWeight >= procTask
												.getProcVotethreshold()) {
									parallel = DictKeyConst.YESORNO_NO;
									isPassed = true;
									wfProcTaskList = processTaskVoteQuickly(
											parallelTasks, procTask,
											procVarData, isPassed);
								} else if (flag
										&& vetoVoteWeight >= (totalVoteWeight - procTask
												.getProcVotethreshold())) {
									parallel = DictKeyConst.YESORNO_NO;
									isPassed = false;
									wfProcTaskList = processTaskVoteQuickly(
											parallelTasks, procTask,
											procVarData, isPassed);
								} else {
									// 审批结果没有达到速决条件，需要等待其他并行任务处理结果
									parallel = DictKeyConst.YESORNO_YES;
									procTask.setProcParallelStatus(WfProcParallStatus.NOTAPPROVED
											.getRetCode());
									taskService.complete(
											procTask.getProcCtaskid(),
											procVarData.getVariableData());
									wfProcTaskList.add(procTask);
								}
							}
						} else { // 并行非速决处理逻辑
							if (flag
									&& isLastParallTask(parallelTasks, procTask)) { // 最后一个并行任务
								List<Float> voteResult = processTaskVoteCaculate(
										parallelTasks, procTask, isPassed, flag);
								float passVoteWeight = voteResult.get(1)
										.floatValue();
								parallel = DictKeyConst.YESORNO_NO;

								if (passVoteWeight >= procTask
										.getProcVotethreshold()) {
									procTask.setProcParallelStatus(WfProcParallStatus.APPROVED
											.getRetCode());
									isPassed = true;
									procVarData
											.setProcApprStatus(FlowStatus.CHECK01
													.getRetCode());
									taskService.complete(
											procTask.getProcCtaskid(),
											procVarData.getVariableData());
								} else {
									procTask.setProcParallelStatus(WfProcParallStatus.APPROVED
											.getRetCode());
									isPassed = false;
									procVarData
											.setProcApprStatus(FlowStatus.CHECK00
													.getRetCode());
									taskService.complete(
											procTask.getProcCtaskid(),
											procVarData.getVariableData());
								}

								// 并行审批完成，重置并行审批任务的并行审批状态
								for (WfProcTaskBean task : parallelTasks) {
									if (task.getProcCtaskid().equals(
											procTask.getProcCtaskid())) {
										wfProcTaskList.add(procTask);
									} else {
										task.setProcParallelStatus(WfProcParallStatus.APPROVED
												.getRetCode());
										wfProcTaskList.add(task);
									}
								}
							} else { // 还有未处理的并行任务，只处理当前任务
								taskService.complete(procTask.getProcCtaskid(),
										procVarData.getVariableData());

								parallel = DictKeyConst.YESORNO_YES;
								procTask.setProcParallelStatus(WfProcParallStatus.NOTAPPROVED
										.getRetCode());
								wfProcTaskList.add(procTask);
							}
						}
					} else { // 当前任务为非决策任务节点的处理逻辑
						if (flag && isLastParallTask(parallelTasks, procTask)) { // 最后一个并行任务
							List<Float> voteResult = processTaskVoteCaculate(
									parallelTasks, procTask, isPassed, flag);
							float passVoteWeight = voteResult.get(1)
									.floatValue();
							parallel = DictKeyConst.YESORNO_NO;

							if (passVoteWeight >= procTask
									.getProcVotethreshold()) {
								procTask.setProcParallelStatus(WfProcParallStatus.APPROVED
										.getRetCode());
								isPassed = true;
								procVarData
										.setProcApprStatus(FlowStatus.CHECK01
												.getRetCode());
								taskService.complete(procTask.getProcCtaskid(),
										procVarData.getVariableData());
							} else {
								procTask.setProcParallelStatus(WfProcParallStatus.APPROVED
										.getRetCode());
								isPassed = false;
								procVarData
										.setProcApprStatus(FlowStatus.CHECK00
												.getRetCode());
								taskService.complete(procTask.getProcCtaskid(),
										procVarData.getVariableData());
							}

							// 并行审批完成，重置并行审批任务的并行审批状态
							for (WfProcTaskBean task : parallelTasks) {
								if (task.getProcCtaskid().equals(
										procTask.getProcCtaskid())) {
									wfProcTaskList.add(procTask);
								} else {
									task.setProcParallelStatus(WfProcParallStatus.APPROVED
											.getRetCode());
									wfProcTaskList.add(task);
								}
							}
						} else {
							// 还有未处理的并行任务，只处理当前任务
							taskService.complete(procTask.getProcCtaskid(),
									procVarData.getVariableData());

							parallel = DictKeyConst.YESORNO_YES;
							procTask.setProcParallelStatus(WfProcParallStatus.NOTAPPROVED
									.getRetCode());
							wfProcTaskList.add(procTask);
						}
					}
				} else {
					// 出口为非汇聚网关，如系统指定了下一任务则提交到指定任务，否则直接调用流程引擎提交当前任务
					if (StringUtil.isNull(appointTask)) {
						// 没有指定任务提交节点，则提交到流程配置的流程任务目标节点
						taskService.complete(procTask.getProcCtaskid(),
								procVarData.getVariableData());
					} else {
						// 指定了流程节点，则提交到业务指定的流程任务目标节点
						commitToAppointTask(procTask, appointTask,
								procVarData.getVariableData());
					}

					procTask.setProcParallelStatus(WfProcParallStatus.APPROVED
							.getRetCode()); // 设置当前并行审批状态为已审批
					parallel = DictKeyConst.YESORNO_YES; // 并行任务未汇聚，下一任务也是并行任务
					wfProcTaskList.add(procTask);
				}
			} else {
				// 流程任务为顺序任务的处理过程
				if (StringUtil.isNull(appointTask)) {
					// 没有指定任务提交节点，则提交到流程配置的流程任务目标节点
					taskService.complete(procTask.getProcCtaskid(),
							procVarData.getVariableData());
				} else {
					// 指定了流程节点，则提交到业务指定的流程任务目标节点
					commitToAppointTask(procTask, appointTask,
							procVarData.getVariableData());
				}

				parallel = DictKeyConst.YESORNO_NO;
				procTask.setProcParallelStatus(WfProcParallStatus.APPROVED
						.getRetCode());
				wfProcTaskList.add(procTask);
			}

			// 判断流程实例是否结束。如果已经结束，则更新业务流程表
			if (isProcessEnded(procTask.getProcInstId())) {
				// 如果流程实例已结束，则重置业务流程表流程状态数据和流程结束时间
				wfProcBean1.setProcStatus(FlowStatus.PROC16.getRetCode());
				wfProcBean1.setProcEndtime(datetime);

				// 对业务数据设置流程是否结束标识
				procTaskData.setProcEnded(DictKeyConst.YESORNO_YES);

				// 流程结束，获取处理该流程所有用户
				allNotifyProcess = getMyProcessByProcInstId(procTask.getProcInstId());
			} else {
				// 获取流程实例下一任务列表
				wfNextTasksBean = getNextProcTasks(procTask, isPassed,
						parallel, procVarData.getProcAssignee(), datetime);
				if (wfNextTasksBean != null && wfNextTasksBean.size() > 0) {
					for (WfProcTaskBean iqbWfProcTaskBean : wfNextTasksBean) {
						nextTasks.add(iqbWfProcTaskBean.getProcCtaskcode());
					}
				}

				// 对业务数据设置流程是否结束标识
				procTaskData.setProcEnded(DictKeyConst.YESORNO_NO);
			}

			try {
				// 对业务数据设置下级审批任务列表
				procTaskData.setProcNextTasks(nextTasks);
				// 在流程任务审批后，调用任务审批完成后的任务回调函数
				afterCallback(WfProcDealType.PROC_APPROVE, procTaskData,
						procTaskSelfProps, callbackClass, bizData.getBizData());

				// 更新业务流程表数据
				updateWfProcess(wfProcBean1);

				// 更新流程任务处理结果
				updateWfProcTasks(wfProcTaskList);

				// 写入我的流程数据
				insertWfMyProcess(wfMyProcBeans);

				// 写入新的流程任务数据
				insertWfProcessTasks(wfNextTasksBean);

				// 发送流程任务通知
				notify(wfProcBean, wfNextTasksBean, allNotifyProcess, taskProperties);
			} catch (WorkflowException iqbe) {
				throw iqbe;
			} catch (Exception e) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020599, e);
			}
		} finally {
			logger.info("审批流程--审批业务流程(" + procTask.getProcInstId() + "),流程任务("
					+ procTask.getProcCtaskname() + ")结束.");
		}
	}

	/**
	 * 得到指定流程的并行任务数据
	 * 
	 * @param instId
	 *            流程实例ID
	 * @param parallelTasks
	 *            流程任务代码
	 * @return
	 */
	private List<WfProcTaskBean> getParallelProcTask(String instId,
			List<String> parallelTasks) {
		// setDb(0, super.MASTER);
		return iqbWfProcTaskBeanMapper.getParallelProcTask(instId,
				parallelTasks);
	}

	/**
	 * 流程任务速决审批处理
	 * 
	 * @param parallelTaskList
	 *            并行流程任务列表
	 * @param currProcTask
	 *            当前处理流程任务
	 * @param procVarData
	 *            流程变量
	 * @return
	 */
	private List<WfProcTaskBean> processTaskVoteQuickly(
			List<WfProcTaskBean> parallelTasks,
			WfProcTaskBean currProcTask, WfProcVariableDataBean procVarData,
			boolean isPassed) {
		String status = isPassed ? FlowStatus.CHECK01.getRetCode()
				: procVarData.getProcApprStatus();

		for (WfProcTaskBean parallelTask : parallelTasks) {
			if (parallelTask.getProcCtaskid().equals(
					currProcTask.getProcCtaskid())) {
				taskService.complete(parallelTask.getProcCtaskid(),
						procVarData.getVariableData());

				parallelTask.setProcTaskAssignee(currProcTask
						.getProcTaskAssignee()); // 流程任务签收人
				parallelTask.setProcTaskAssigntime(currProcTask
						.getProcTaskAssigntime()); // 流程任务签收时间
				parallelTask.setProcTaskEndtime(currProcTask
						.getProcTaskEndtime()); // 流程任务处理完成时间
				parallelTask.setProcTaskStatus(FlowStatus.TASK03.getRetCode()); // 流程任务状态为已处理
				parallelTask.setProcTaskApprStatus(procVarData
						.getProcApprStatus()); // 流程任务审批结论
				parallelTask.setProcTaskApprOpinion(procVarData
						.getProcApprOpinion()); // 流程任务审批意见
				parallelTask.setProcParallelStatus(WfProcParallStatus.APPROVED
						.getRetCode());
			} else if (FlowStatus.TASK03.getRetCode().equals(
					parallelTask.getProcTaskStatus())) {
				parallelTask.setProcParallelStatus(WfProcParallStatus.APPROVED
						.getRetCode());
			} else {
				taskService.complete(parallelTask.getProcCtaskid(),
						procVarData.getVariableData());

				if (FlowStatus.TASK01.getRetCode().equals(
						parallelTask.getProcTaskStatus())) {
					parallelTask.setProcTaskAssignee(DEFAULTUSER); // 流程任务签收人
					parallelTask.setProcTaskAssigntime(currProcTask
							.getProcTaskEndtime()); // 流程任务签收时间
				}
				parallelTask.setProcTaskEndtime(currProcTask
						.getProcTaskEndtime()); // 流程任务处理完成时间
				parallelTask.setProcTaskStatus(FlowStatus.TASK03.getRetCode()); // 流程任务状态为已处理
				parallelTask.setProcTaskApprStatus(status); // 流程任务审批结论
				parallelTask.setProcTaskApprOpinion("流程速决自动审批"); // 流程任务审批意见
				parallelTask.setProcParallelStatus(WfProcParallStatus.APPROVED
						.getRetCode());
			}
		}

		return parallelTasks;
	}

	/**
	 * 计算并行任务投票结果
	 * 
	 * @param procTasks
	 *            并行任务列表
	 * @param currentTask
	 *            当前处理任务
	 * @return
	 * @throws WorkflowException
	 */
	private List<Float> processTaskVoteCaculate(
			List<WfProcTaskBean> procTasks, WfProcTaskBean currentTask,
			boolean isPassed, boolean flag) throws WorkflowException {
		List<Float> voteResult = new ArrayList<Float>();
		float totalVoteWeight = 0;
		float passVoteWeight = 0;
		float vetoVoteWeight = 0;

		for (WfProcTaskBean task : procTasks) {
			if (DictKeyConst.YESORNO_NO.equals(task.getProcVotetask())) {
				continue;
			}

			// 投票审批任务配置的投票阈值不一致，不能进行流程任务审批
			if (currentTask.getProcVotethreshold() != task
					.getProcVotethreshold()) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020514);
			}

			// 投票审批任务配置的投票规则不一致，不能进行流程任务审批
			if (!currentTask.getProcVoterule().equals(task.getProcVoterule())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020515);
			}

			// 计算当前审批任务的投票数据
			if (task.getProcCtaskid().equals(currentTask.getProcCtaskid())) {
				if (WfProcVoteRole.VOTECOUNT.getRetCode().equals(
						task.getProcVoterule())) {
					totalVoteWeight += 1;
					passVoteWeight += isPassed ? 1 : 0;
					vetoVoteWeight += isPassed ? 0 : 1;
				} else {
					totalVoteWeight += currentTask.getProcVoteweight();
					passVoteWeight += isPassed ? currentTask
							.getProcVoteweight() : 0;
					vetoVoteWeight += isPassed ? 0 : currentTask
							.getProcVoteweight();
				}
			} else if (FlowStatus.TASK03.getRetCode().equals(
					task.getProcTaskStatus())) {
				boolean b = FlowStatus.CHECK01.getRetCode().equals(
						task.getProcTaskApprStatus());
				if (WfProcVoteRole.VOTECOUNT.getRetCode().equals(
						task.getProcVoterule())) {
					totalVoteWeight += 1;
					passVoteWeight += b ? 1 : 0;
					vetoVoteWeight += b ? 0 : 1;
				} else {
					totalVoteWeight += task.getProcVoteweight();
					passVoteWeight += b ? task.getProcVoteweight() : 0;
					vetoVoteWeight += b ? 0 : task.getProcVoteweight();
				}
			} else {
				if (WfProcVoteRole.VOTECOUNT.getRetCode().equals(
						task.getProcVoterule())) {
					totalVoteWeight += 1;
				} else {
					totalVoteWeight += task.getProcVoteweight();
				}
			}
		}

		// 投票总数小于投票阈值，配置不正确
		if (flag && totalVoteWeight < currentTask.getProcVotethreshold()) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020516);
		}

		voteResult.add(0, totalVoteWeight);
		voteResult.add(1, passVoteWeight);
		voteResult.add(2, vetoVoteWeight);

		return voteResult;
	}

	/**
	 * 判断当前并行任务是否为最后一个任务
	 * 
	 * @param procTasks
	 *            并行任务列表
	 * @param currentTask
	 *            当前处理任务
	 * @return
	 */
	private boolean isLastParallTask(List<WfProcTaskBean> procTasks,
			WfProcTaskBean currentTask) {
		boolean isLast = true;

		for (WfProcTaskBean task : procTasks) {
			if (!task.getProcCtaskid().equals(currentTask.getProcCtaskid())
					&& (FlowStatus.TASK01.getRetCode().equals(
							task.getProcTaskStatus()) || FlowStatus.TASK02
							.getRetCode().equals(task.getProcTaskStatus()))) {
				isLast = false;
			}
		}

		return isLast;
	}

	/**
	 * 获取当前流程任务提交后指定的任务
	 * 
	 * @param properties
	 *            流程任务属性
	 * @param procAppointTask
	 *            指定的流程任务
	 * @return
	 * @throws WorkflowException
	 */
	private String getProcAppointTask(WfProcTaskPropertiesBean properties,
			String procAppointTask) throws WorkflowException {
		if (StringUtil.isNull(procAppointTask)) {
			return getProcRefuseTask(properties);
		} else {
			return procAppointTask;
		}
	}

	/**
	 * 当前流程任务提交后，生成指定流程任务
	 * 
	 * @param procTask
	 *            当前流程任务信息
	 * @param newTask
	 *            新的流程任务代码
	 * @param procVariable
	 *            流程变量
	 */
	private void commitToAppointTask(WfProcTaskBean procTask,
			String newTask, Map<String, Object> procVariable)
			throws WorkflowException {
		// 取得流程定义
		ProcessDefinitionEntity definition = wfProcDesinerBiz
				.getDeployedProcessDefinition(procTask.getProcId());

		// 取得当前活动节点
		ActivityImpl currActivity = wfProcDesinerBiz.getActivity(definition,
				procTask.getProcCtaskcode());

		// 获取当前流程任务所有出口节点连线
		List<PvmTransition> pvmTransitionList = currActivity
				.getOutgoingTransitions();

		// 将当前流程任务所有出口节点连线缓存起来，在流程回退处理完成后恢复原有连线关系
		List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
		for (PvmTransition pvmTransition : pvmTransitionList) {
			oriPvmTransitionList.add(pvmTransition);
		}
		pvmTransitionList.clear();

		// 通过目标流程任务ID获取新的目标流程任务
		ActivityImpl nextActivityImpl = wfProcDesinerBiz.getActivity(
				definition, newTask);

		// 将当前任务出口转向新的流程任务节点
		TransitionImpl newTransition = currActivity.createOutgoingTransition();
		newTransition.setDestination(nextActivityImpl);

		taskService.complete(procTask.getProcCtaskid(), procVariable);

		// 清除临时任务出口节点连线
		currActivity.getOutgoingTransitions().remove(newTransition);

		// 恢复原有流程任务出口节点连线
		for (PvmTransition pvmTransition : oriPvmTransitionList) {
			currActivity.getOutgoingTransitions().add(pvmTransition);
		}
	}

	/**
	 * 将流程任务属性解析为Bean
	 * 
	 * @param taskProperties
	 * @return
	 */
	private WfProcTaskPropertiesBean parseTaskProperties(String taskProperties) {
		if (StringUtil.isNull(taskProperties)) {
			return null;
		} else {
			JSONObject obj = JSONUtil.strToJSON(taskProperties);
			return JSONUtil.toJavaObject(obj, WfProcTaskPropertiesBean.class);
		}
	}

	/**
	 * 删除流程任务，只有当前流程任务是申请节点，且当前处理用户是业务申请用户时才允许删除流程
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public void deleteProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException, Exception {
		// 如果没有指定流程任务ID，则不能进行流程任务删除处理
		if (StringUtil.isNull(procData.getProcTaskId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020602);
		}

		// 查询可删除流程任务，流程任务状态为待签收和待处理
		WfProcTaskBean procTask = getProcessTask(procData.getProcTaskId(),
				getProcTaskStatus(3));

		deleteProcessInstance(procData, procVarData, authData, bizData,
				procTask);
	}

	/**
	 * 删除流程任务，只有当前流程任务是申请节点，且当前处理用户是业务申请用户时才允许删除流程
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @param procTask
	 *            流程任务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	private void deleteProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData, WfProcTaskBean procTask)
			throws WorkflowException, Exception {
		// 未找到将要删除的流程任务
		if (procTask == null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020601);
		}

		// 如果当前任务不是申请任务，则查询该流程的起始任务
		WfProcTaskBean startTask = null;
		if (StringUtil.isNull(procTask.getProcPtaskid())) {
			startTask = procTask;
		} else {
			startTask = getStartTask(procTask.getProcInstId());
		}

		// 流程起始任务为空，或者当前流程任务与流程起始任务不是同一任务，不能删除
		if (startTask == null
				|| !procTask.getProcCtaskcode().equals(
						startTask.getProcCtaskcode())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020603);
		}

		// 流程起始任务提交人不是当前操作用户，该流程任务不能被删除
		if (!authData.getProcTaskUser()
				.equals(startTask.getProcTaskCommitter())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020604);
		}

		// 流程状态校验，只有审批中的流程才能进行流程任务删除操作
		checkProcessStatus(procTask.getProcInstId());

		// 当前流程实例的当前待审批任务数量大于1个，说明除了当前任务外还有其他待审批任务，不能删除该流程
		if (getActiveTaskCount(procTask.getProcInstId()) > 1) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020605);
		}

		// 重置流程业务表中的流程状态为已删除，流程结束时间
		int datetime = DateTools.getCurrTime();
		WfProcBean wfProcBean = new WfProcBean();
		wfProcBean.setProcInstId(procTask.getProcInstId());
		wfProcBean.setProcStatus(FlowStatus.PROC15.getRetCode());
		wfProcBean.setProcEndtime(datetime);

		// 将流程任务表中的待处理和待签收的流程任务状态修改为已终止
		WfProcTaskBean wfProcTaskBean = new WfProcTaskBean();
		wfProcTaskBean.setProcInstId(procTask.getProcInstId());
		wfProcTaskBean.setProcTaskAssignee(authData.getProcTaskUser());
		wfProcTaskBean.setProcTaskAssigntime(datetime);
		wfProcTaskBean.setProcTaskStatus(FlowStatus.TASK04.getRetCode());
		wfProcTaskBean.setProcTaskApprStatus(FlowStatus.CHECK03.getRetCode());
		wfProcTaskBean.setProcTaskApprOpinion(StringUtil.isNull(procVarData
				.getProcSpecialDesc()) ? "流程申请人删除流程" : procVarData
				.getProcSpecialDesc());
		wfProcTaskBean.setProcTaskEndtime(datetime);
		wfProcTaskBean.setProcParallelStatus(WfProcParallStatus.APPROVED
				.getRetCode());

		// 获取流程任务属性
		WfProcTaskPropertiesBean taskProperties = parseTaskProperties(procTask
				.getProcTaskProperties());

		// 对业务数据设置当前流程任务代码
		ProcTaskData procTaskData = new ProcTaskData();
		procTaskData.setProcTaskCode(procTask.getProcCtaskcode());
		procTaskData.setProcTaskId(procTask.getProcCtaskid());

		// 获取当前流程任务自定义属性
		Map<String, String> procTaskSelfProps = getProcTaskSelfProperties(taskProperties);

		// 在流程任务删除前，调用任务删除前的任务回调函数
		String callbackClass = getProcTaskCallback(taskProperties);
		beforeCallback(WfProcDealType.PROC_DELETE, procTaskData,
				procTaskSelfProps, callbackClass, bizData.getBizData());

		logger.info("删除流程--开始删除业务流程(" + procTask.getProcInstId() + ")...");
		try {
			// 调用工作流引擎将流程任务删除掉
			ProcessInstance processInstance = getProcessInstance(procTask.getProcInstId());
			if (processInstance.isEnded()) {
				historyService.deleteHistoricProcessInstance(procTask.getProcInstId());
			} else {
				runtimeService.deleteProcessInstance(procTask.getProcInstId(), "删除流程");
				historyService.deleteHistoricProcessInstance(procTask.getProcInstId());
			}

			// 在流程任务删除完成后，调用任务删除处理完成后的任务回调函数
			afterCallback(WfProcDealType.PROC_DELETE, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			updateWfProcess(wfProcBean);
			deleteProcess(wfProcTaskBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		} finally {
			logger.info("删除流程--删除业务流程(" + procTask.getProcInstId() + ")结束.");
		}
	}

	/**
	 * 通过流程实例取消当前流程，取消的流程可在我的流程中查询到 1、只有流程申请人才能取消流程任务；
	 * 2、流程任务在申请节点或者已提交到下一任务节点但没有签收和审批
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public void cancelProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException, Exception {
		// 如果没有指定流程实例ID，则不能进行流程任务取消操作
		if (StringUtil.isNull(procData.getProcInstId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021002);
		}

		// 通过流程实例ID查询流程的起始任务
		WfProcTaskBean startTask = getStartTask(procData.getProcInstId());

		cancelProcessInstance(procData, procVarData, authData, bizData,
				startTask);
	}

	/**
	 * 取消当前流程任务，取消的流程可在我的流程中查询到 1、只有流程申请人才能取消流程任务；
	 * 2、流程任务在申请节点或者已提交到下一任务节点但没有签收和审批
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @param procTasks
	 *            流程任务数据列表
	 * @throws WorkflowException
	 * @throws Exception
	 */
	private void cancelProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData, WfProcTaskBean startTask)
			throws WorkflowException, Exception {
		if (startTask == null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021001);
		}

		// 当前处理人不是流程创建人
		if (!startTask.getProcTaskCommitter()
				.equals(authData.getProcTaskUser())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021003);
		}

		// 流程状态校验，只有审批中的流程才能进行流程任务取消操作
		checkProcessStatus(procData.getProcInstId());

		// 通过流程实例查询该流程全部任务列表
		List<WfProcTaskBean> procTasks = getProcessTasks(
				procData.getProcInstId(), getProcTaskStatus(99));

		// 未找到需要取消的业务流程
		if (procTasks == null || procTasks.size() == 0) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021001);
		}

		// 获取可取消的一个流程任务（流程任务待签收，流程任务为起始任务的下级任务）
		WfProcTaskBean procTask = null;
		for (WfProcTaskBean task : procTasks) {
			if (task.getProcTaskStatus().equals(FlowStatus.TASK01.getRetCode())
					&& startTask.getProcCtaskcode().equals(
							task.getProcPtaskcode())) {
				procTask = task;
				break;
			}
		}

		// 没找到可以取消的流程任务
		if (procTask == null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021004);
		}

		for (WfProcTaskBean task : procTasks) {
			if (procTask.getProcPtaskid().equals(task.getProcPtaskid())) {
				// 可以取消的流程任务，流程任务已经被签收或审批，不能取消流程
				if (!task.getProcTaskStatus().equals(
						FlowStatus.TASK01.getRetCode())) {
					throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021005);
				}

				// 可以取消的流程任务，流程任务提交人不是流程创建人，不能取消流程
				if (!task.getProcTaskCommitter().equals(
						startTask.getProcTaskCommitter())) {
					throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021006);
				}
			} else {
				// 除可取消流程任务外，还有其他不能被取消的流程未被处理，不能取消流程
				if (task.getProcTaskStatus().equals(
						FlowStatus.TASK01.getRetCode())
						|| task.getProcTaskStatus().equals(
								FlowStatus.TASK02.getRetCode())) {
					throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021007);
				}
			}
		}

		int datetime = DateTools.getCurrTime();
		// 重置流程业务表中的流程状态为已取消，流程终止时间
		WfProcBean wfProcBean = new WfProcBean();
		wfProcBean.setProcInstId(procTask.getProcInstId());
		wfProcBean.setProcStatus(FlowStatus.PROC17.getRetCode());
		wfProcBean.setProcEndtime(datetime);

		// 将流程任务表中的待处理和待签收的流程任务状态修改为已终止
		WfProcTaskBean wfProcTaskBean = new WfProcTaskBean();
		wfProcTaskBean.setProcInstId(procTask.getProcInstId());
		wfProcTaskBean.setProcTaskAssignee(authData.getProcTaskUser());
		wfProcTaskBean.setProcTaskAssigntime(datetime);
		wfProcTaskBean.setProcTaskStatus(FlowStatus.TASK04.getRetCode());
		wfProcTaskBean.setProcTaskEndtime(datetime);
		wfProcTaskBean.setProcTaskApprStatus(FlowStatus.CHECK04.getRetCode());
		wfProcTaskBean.setProcTaskApprOpinion(StringUtil.isNull(procVarData
				.getProcSpecialDesc()) ? "流程申请人取消流程" : procVarData
				.getProcSpecialDesc());
		wfProcTaskBean.setProcParallelStatus(WfProcParallStatus.APPROVED
				.getRetCode());

		// 获取流程任务属性
		WfProcTaskPropertiesBean taskProperties = parseTaskProperties(procTask
				.getProcTaskProperties());

		// 对业务数据设置当前流程任务代码
		ProcTaskData procTaskData = new ProcTaskData();
		procTaskData.setProcTaskCode(procTask.getProcCtaskcode());
		procTaskData.setProcTaskId(procTask.getProcCtaskid());

		// 获取当前流程任务自定义属性
		Map<String, String> procTaskSelfProps = getProcTaskSelfProperties(taskProperties);

		// 在流程任务取消前，调用任务取消前的任务回调函数
		String callbackClass = getProcTaskCallback(taskProperties);
		beforeCallback(WfProcDealType.PROC_CANCEL, procTaskData,
				procTaskSelfProps, callbackClass, bizData.getBizData());

		logger.info("取消流程--开始取消业务流程(" + procTask.getProcInstId() + ")...");
		try {
			// 调用工作流引擎将流程任务删除
			ProcessInstance processInstance = getProcessInstance(procTask.getProcInstId());

			if (processInstance.isEnded()) {
				historyService.deleteHistoricProcessInstance(procTask.getProcInstId());
			} else {
				runtimeService.deleteProcessInstance(procTask.getProcInstId(), "取消流程");
				historyService.deleteHistoricProcessInstance(procTask.getProcInstId());
			}

			// 在流程任务取消完成后，调用任务取消处理完成后的任务回调函数
			afterCallback(WfProcDealType.PROC_CANCEL, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			updateWfProcess(wfProcBean);
			cancelProcess(wfProcTaskBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		} finally {
			logger.info("取消流程--取消业务流程(" + procTask.getProcInstId() + ")结束.");
		}
	}

	/**
	 * 通过流程任务ID撤回当前流程任务，撤回的流程在待办任务中可进行再次处理
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public void retrieveProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException, Exception {
		// 如果没有指定流程任务ID，则不能进行流程任务撤回处理
		if (StringUtil.isNull(procData.getProcTaskId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021102);
		}

		// 通过流程任务ID查询已经审批完成的流程任务数据
		WfProcTaskBean procTask = getProcessTask(procData.getProcTaskId(),
				getProcTaskStatus(4));
		retrieveProcessInstance(procData, procVarData, authData, bizData,
				procTask);
	}

	/**
	 * 撤回当前流程任务，撤回的流程在待办任务中可进行再次处理
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @param procTask
	 *            流程任务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	private void retrieveProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData, WfProcTaskBean procTask)
			throws WorkflowException, Exception {
		if (procTask == null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021101);
		}

		// 当前任务审批人不是当前用户
		if (!authData.getProcTaskUser().equals(procTask.getProcTaskAssignee())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021103);
		}

		// 获取流程任务节点可撤回标识
		String flag = getProcRetrieve(parseTaskProperties(procTask
				.getProcTaskProperties()));
		if (DictKeyConst.YESORNO_NO.equals(flag)) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021106);
		}

		// 并行任务汇聚，不允许撤回
		if (!checkRetrieveProcess(procTask.getProcId(),
				procTask.getProcCtaskcode())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021104);
		}

		// 通过上级流程任务ID查询可撤回流程任务列表
		List<WfProcTaskBean> procTasks = getProcTasksByParent(procTask
				.getProcCtaskid());

		// 未找到需要撤回的业务流程
		if (procTasks == null || procTasks.size() == 0) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021101);
		}

		// 检查流程任务是否都是未签收状态，如果已签收或审批，则不能进行撤回操作
		for (WfProcTaskBean task : procTasks) {
			if (!FlowStatus.TASK01.getRetCode()
					.equals(task.getProcTaskStatus())) {
				throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021105);
			}
		}

		// 流程状态校验，只有审批中的流程才能进行流程任务撤回操作
		checkProcessStatus(procTask.getProcInstId());

		// 获取流程任务属性
		WfProcTaskPropertiesBean taskProperties = parseTaskProperties(procTask
				.getProcTaskProperties());

		// 对业务数据设置当前流程任务代码
		ProcTaskData procTaskData = new ProcTaskData();
		procTaskData.setProcTaskCode(procTask.getProcCtaskcode());
		procTaskData.setProcTaskId(procTask.getProcCtaskid());

		// 获取当前流程任务自定义属性
		Map<String, String> procTaskSelfProps = getProcTaskSelfProperties(taskProperties);
				
		try {
			// 在流程任务取消前，调用任务取消前的任务回调函数
			String callbackClass = getProcTaskCallback(taskProperties);
			beforeCallback(WfProcDealType.PROC_RETRIEVE, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			logger.info("撤回流程任务--开始撤回业务流程(" + procTask.getProcInstId() + ")...");
			int datetime = DateTools.getCurrTime();
			List<WfProcTaskBean> wfProcTasks = new ArrayList<WfProcTaskBean>();
			for (int i = 0; i < procTasks.size(); i++) {
				logger.info("撤回流程任务--开始撤回流程任务("
						+ procTasks.get(i).getProcCtaskname() + ")...");
				TaskEntity currentTask = getProcTaskEntityByTask(procTasks.get(
						i).getProcCtaskid());

				if (i == (procTasks.size() - 1)) {
					commitToAppointTask(procTasks.get(i), procTasks.get(i)
							.getProcPtaskcode(), null);
				} else {
					// 不能删除当前正在执行的任务，所以要先清除掉关联
					currentTask.setExecutionId(null);
					taskService.saveTask(currentTask);
					// 级联删除，delete historic information
					taskService.deleteTask(currentTask.getId(), true);
				}

				WfProcTaskBean wfProcTaskBean = new WfProcTaskBean();
				wfProcTaskBean
						.setProcCtaskid(procTasks.get(i).getProcCtaskid());
				wfProcTaskBean.setProcTaskAssignee(authData.getProcTaskUser());
				wfProcTaskBean.setProcTaskAssigntime(datetime);
				wfProcTaskBean
						.setProcTaskStatus(FlowStatus.TASK04.getRetCode());
				wfProcTaskBean.setProcTaskEndtime(datetime);
				wfProcTaskBean.setProcTaskApprStatus(FlowStatus.CHECK02
						.getRetCode());
				wfProcTaskBean.setProcTaskApprOpinion("用户主动撤回流程任务");
				wfProcTaskBean
						.setProcParallelStatus(WfProcParallStatus.APPROVED
								.getRetCode());
				wfProcTasks.add(wfProcTaskBean);

				logger.info("撤回流程任务--撤回流程任务("
						+ procTasks.get(i).getProcCtaskname() + ")完成.");
			}

			// 获取撤回流程产生的下一任务列表
			List<WfProcTaskBean> wfNextTasksBean = getNextProcTasks(
					procTask, false, procTask.getProcParallel(), null, datetime);
			List<String> nextTasks = new ArrayList<String>();
			if (wfNextTasksBean != null && wfNextTasksBean.size() > 0) {
				for (WfProcTaskBean iqbWfProcTaskBean : wfNextTasksBean) {
					nextTasks.add(iqbWfProcTaskBean.getProcCtaskcode());
				}
			}

			// 对业务数据设置流程是否结束标识
			procTaskData.setProcEnded(DictKeyConst.YESORNO_NO);
			// 对业务数据设置下级审批任务列表
			procTaskData.setProcNextTasks(nextTasks);

			// 在流程任务取消完成后，调用任务取消处理完成后的任务回调函数
			afterCallback(WfProcDealType.PROC_RETRIEVE, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			// 更新流程任务处理结果
			updateWfProcTasks(wfProcTasks);

			// 写入新的流程任务数据
			insertWfProcessTasks(wfNextTasksBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		} finally {
			logger.info("撤回流程--撤回业务流程(" + procTask.getProcInstId() + ")结束.");
		}
	}

	/**
	 * 通过流程配置信息判定流程任务是否可以撤回
	 * 
	 * @param procId
	 *            流程ID
	 * @param procTaskcode
	 *            流程任务代码
	 * @return
	 */
	private boolean checkRetrieveProcess(String procId, String procTaskcode)
			throws WorkflowException {
		boolean flag = true;
		ProcessDefinitionEntity definition = wfProcDesinerBiz
				.getDeployedProcessDefinition(procId);
		ActivityImpl currActivity = wfProcDesinerBiz.getActivity(definition,
				procTaskcode);
		List<PvmTransition> pvmTransitionList = currActivity
				.getOutgoingTransitions();

		for (PvmTransition pvmTransition : pvmTransitionList) {
			if ("parallelGateway".equals(pvmTransition.getDestination()
					.getProperty("type"))) {
				if (pvmTransition.getDestination().getIncomingTransitions()
						.size() > 1) {
					flag = false;
					break;
				}
			}
		}

		return flag;
	}

	/**
	 * 通过流程任务代码终止当前流程任务，终止的流程可在我的流程中查询到
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @param bizData
	 *            流程业务数据
	 * @throws WorkflowException
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public void endProcessInstance(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData,
			WfProcBizDataBean bizData) throws WorkflowException, Exception {
		// 如果没有指定流程实例ID，则不能进行流程任务终止操作
		if (StringUtil.isNull(procData.getProcInstId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021201);
		}

		// 流程状态校验，只有审批中的流程才能进行流程任务终止操作
		checkProcessStatus(procData.getProcInstId());

		// 通过流程任务代码查询可终止流程任务，流程任务状态为待签收和已签收
		List<WfProcTaskBean> procTasks = getProcessTasks(
				procData.getProcInstId(), getProcTaskStatus(3));

		// 未找到需要终止的业务流程
		if (procTasks == null || procTasks.size() == 0) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021202);
		}

		// 重置流程业务表中的流程状态为已终止，流程终止时间
		int datetime = DateTools.getCurrTime();
		WfProcBean wfProcBean = new WfProcBean();
		wfProcBean.setProcInstId(procData.getProcInstId());
		wfProcBean.setProcStatus(FlowStatus.PROC14.getRetCode());
		wfProcBean.setProcEndtime(datetime);

		// 将流程任务表中的待处理和待签收的流程任务状态修改为已终止
		WfProcTaskBean wfProcTaskBean = new WfProcTaskBean();
		wfProcTaskBean.setProcInstId(procData.getProcInstId());
		wfProcTaskBean.setProcTaskAssignee(authData.getProcTaskUser());
		wfProcTaskBean.setProcTaskAssigntime(datetime);
		wfProcTaskBean.setProcTaskStatus(FlowStatus.TASK04.getRetCode());
		wfProcTaskBean.setProcTaskEndtime(datetime);
		wfProcTaskBean.setProcTaskApprStatus(FlowStatus.CHECK05.getRetCode());
		wfProcTaskBean.setProcTaskApprOpinion(StringUtil.isNull(procVarData
				.getProcSpecialDesc()) ? "用户强行终止流程" : procVarData
				.getProcSpecialDesc());
		wfProcTaskBean.setProcParallelStatus(WfProcParallStatus.APPROVED
				.getRetCode());

		// 获取流程任务属性
		WfProcTaskPropertiesBean taskProperties = parseTaskProperties(procTasks
				.get(0).getProcTaskProperties());

		// 对业务数据设置当前流程任务代码
		ProcTaskData procTaskData = new ProcTaskData();
		procTaskData.setProcTaskCode(procTasks.get(0).getProcCtaskcode());
		procTaskData.setProcTaskId(procTasks.get(0).getProcCtaskid());

		// 获取当前流程任务自定义属性
		Map<String, String> procTaskSelfProps = getProcTaskSelfProperties(taskProperties);

		// 在流程任务终止前，调用任务终止前的任务回调函数
		String callbackClass = getProcTaskCallback(taskProperties);
		beforeCallback(WfProcDealType.PROC_END, procTaskData,
				procTaskSelfProps, callbackClass, bizData.getBizData());

		logger.info("终止流程--开始终止业务流程(" + procData.getProcInstId() + ")...");
		try {
			// 调用工作流引擎将流程任务删除
			ProcessInstance processInstance = getProcessInstance(procData.getProcInstId());

			if (processInstance.isEnded()) {
				historyService.deleteHistoricProcessInstance(procData.getProcInstId());
			} else {
				runtimeService.deleteProcessInstance(procData.getProcInstId(), "终止流程");
				historyService.deleteHistoricProcessInstance(procData.getProcInstId());
			}

			// 在流程任务终止完成后，调用任务终止处理完成后的任务回调函数
			afterCallback(WfProcDealType.PROC_END, procTaskData,
					procTaskSelfProps, callbackClass, bizData.getBizData());

			updateWfProcess(wfProcBean);
			endProcessTasks(wfProcTaskBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020199, e);
		} finally {
			logger.info("终止流程--终止业务流程(" + procData.getProcInstId() + ")结束.");
		}
	}

	/**
	 * 指定流程任务处理人
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ)
	public void appointAssignee(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData)
			throws WorkflowException, Exception {
		// 校验业务处理的数据完整性
		checkAssignee(procData, procVarData);

		logger.info("指定流程任务处理人--开始指定流程任务(" + procData.getProcTaskId()
				+ ")处理人...");
		try {
			// 设置流程引擎中的处理人
			taskService.setAssignee(procData.getProcTaskId(),
					procVarData.getProcAssignee());

			WfProcTaskBean wfProcTaskBean = new WfProcTaskBean();
			wfProcTaskBean.setProcCtaskid(procData.getProcTaskId());
			wfProcTaskBean.setProcTaskAssignee(procVarData.getProcAssignee());
			wfProcTaskBean.setProcAppointUsers(procVarData.getProcAssignee());
			wfProcTaskBean.setProcTaskAssigntime(DateTools.getCurrTime());
			wfProcTaskBean.setProcTaskStatus(FlowStatus.TASK02.getRetCode());

			// 设置流程任务表中的处理人
			updateWfProcTask(wfProcTaskBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021599, e);
		} finally {
			logger.info("指定流程任务处理人--指定流程任务(" + procData.getProcTaskId()
					+ ")处理人结束.");
		}
	}

	/**
	 * 校验流程任务指定处理人数据的合法性
	 * 
	 * @param procData
	 * @param procVarData
	 * @throws WorkflowException
	 */
	private void checkAssignee(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData) throws WorkflowException {
		if (StringUtil.isNull(procData.getProcTaskId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021501);
		}

		if (StringUtil.isNull(procVarData.getProcAssignee())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021502);
		}
	}

	/**
	 * 通过流程实例ID将审批中的流程实例暂停，暂停的流程实例只可以被查询，流出任务不可以被处理
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @return
	 */
	// @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.REPEATABLE_READ)
	public void suspendProcess(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData)
			throws WorkflowException, Exception {
		// 校验业务处理的数据完整性
		if (StringUtil.isNull(procData.getProcInstId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021301);
		}

		// 流程状态校验，只有审批中的流程才可以被暂停
		checkProcessStatus(procData.getProcInstId());

		logger.info("流程暂停--开始暂停流程实例(" + procData.getProcInstId() + ")...");
		try {
			// 通过流程引擎将流程实例暂停，暂时不通过流程引擎中更新流程挂起状态，只在流程业务层面进行状态控制
			// runtimeService.suspendProcessInstanceById(procData.getProcInstId());

			WfProcBean wfProcBean = new WfProcBean();
			wfProcBean.setProcInstId(procData.getProcInstId());
			wfProcBean.setProcStatus(FlowStatus.PROC13.getRetCode());

			// 更新流程状态为已暂停
			updateWfProcess(wfProcBean);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021399, e);
		} finally {
			logger.info("流程暂停--暂停流程实例(" + procData.getProcInstId() + ")处理结束.");
		}
	}

	/**
	 * 通过流程实例ID将暂停的流程实例激活，激活的流程实例状态变更为审批中，流程任务可以被处理
	 * 
	 * @param procData
	 *            流程数据
	 * @param procVarData
	 *            流程参数
	 * @param authData
	 *            流程认证数据
	 * @return
	 */
	// @Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.REPEATABLE_READ)
	public void activeProcess(WfProcessDataBean procData,
			WfProcVariableDataBean procVarData, WfProcAuthDataBean authData)
			throws WorkflowException, Exception {
		// 校验业务处理的数据完整性
		if (StringUtil.isNull(procData.getProcInstId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021401);
		}

		// 获取业务流程数据
		WfProcBean wfProcBean = getWfProcess(procData.getProcInstId());

		// 流程实例不存在，不能进行流程实例激活处理
		if (wfProcBean == null) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020001);
		}

		// 流程实例状态不为暂停状态，不能进行流程实例激活处理
		if (!FlowStatus.PROC13.getRetCode().equals(wfProcBean.getProcStatus())) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021402);
		}

		logger.info("流程激活--开始激活流程实例(" + procData.getProcInstId() + ")...");
		try {
			// 通过流程引擎将流程实例激活，暂时不通过流程引擎中更新流程激活状态，只在流程业务层面进行状态控制
			// runtimeService.activateProcessInstanceById(procData.getProcInstId());

			WfProcBean wfProcBean1 = new WfProcBean();
			wfProcBean1.setProcInstId(procData.getProcInstId());
			wfProcBean1.setProcStatus(FlowStatus.PROC10.getRetCode());

			// 更新流程状态为审批中
			updateWfProcess(wfProcBean1);
		} catch (WorkflowException iqbe) {
			throw iqbe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02021499, e);
		} finally {
			logger.info("流程激活--激活流程实例(" + procData.getProcInstId() + ")处理结束.");
		}
	}

	/**
	 * 流程启动时更新业务流程表
	 * 
	 * @param wfProcBean
	 * @throws WorkflowException
	 */
	public void insertWfProcess(WfProcBean wfProcBean) throws WorkflowException {
		if (null != wfProcBean) {
			// setDb(0, super.MASTER);
			wfProcBeanMapper.insert(wfProcBean);
		} else {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020201);
		}
	}

	/**
	 * 更新业务流程表
	 * 
	 * @param wfProcBean
	 * @throws WorkflowException
	 */
	private void updateWfProcess(WfProcBean wfProcBean) throws WorkflowException {
		if (null != wfProcBean) {
			// setDb(0, super.MASTER);
			wfProcBeanMapper.updateWfProcBeanByInstId(wfProcBean);
		} else {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020201);
		}
	}

	/**
	 * 更新业务流程表
	 * 
	 * @param wfProcBean
	 * @throws WorkflowException
	 */
	private WfProcBean getWfProcess(String procInstId) throws WorkflowException {
		// setDb(0, super.MASTER);
		return wfProcBeanMapper.selectByPrimaryKey(procInstId);
	}

	/**
	 * 循环插入流程任务数据
	 * 
	 * @param wfProcTaskBeans
	 *            流程任务数据
	 * @throws WorkflowException
	 */
	public void insertWfProcessTask(WfProcTaskBean wfProcTaskBean)
			throws WorkflowException {
		// setDb(0, super.MASTER);
		iqbWfProcTaskBeanMapper.insert(wfProcTaskBean);
	}

	/**
	 * 循环插入流程任务数据
	 * 
	 * @param wfProcTaskBeans
	 *            流程任务数据
	 * @throws WorkflowException
	 */
	public void insertWfProcessTasks(List<WfProcTaskBean> wfProcTaskBeans)
			throws WorkflowException {
		if (null != wfProcTaskBeans && wfProcTaskBeans.size() > 0) {
			// setDb(0, super.MASTER);

			for (WfProcTaskBean wfProcTaskBean : wfProcTaskBeans) {
				iqbWfProcTaskBeanMapper.insert(wfProcTaskBean);
			}
		}
	}

	/**
	 * 通过流程任务ID查询指定流程任务状态的流程任务数据
	 * 
	 * @param procTaskId
	 *            流程任务ID
	 * @param taskStatus
	 *            流程任务状态
	 * @return
	 */
	private WfProcTaskBean getProcessTask(String procTaskId,
			List<String> taskStatus) {
		// setDb(0, super.SLAVE); 
		return iqbWfProcTaskBeanMapper.findTaskByTaskId(procTaskId, taskStatus);
	}

	/**
	 * 查询某流程实例中指定任务的信息
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @param taskCode
	 *            流程任务代码
	 * @param taskStatus
	 *            流程任务状态
	 * @return
	 * @throws WorkflowException
	 */
	private WfProcTaskBean getProcessTask(String procInstId,
			String taskCode, List<String> taskStatus) throws WorkflowException {
		// setDb(0, super.SLAVE); 
		return iqbWfProcTaskBeanMapper.getActiveTask(procInstId, taskCode,
				taskStatus);
	}

	/**
	 * 通过上级流程任务ID查询流程任务数据
	 * 
	 * @param procTaskId
	 *            流程任务ID
	 * @return
	 */
	private List<WfProcTaskBean> getProcTasksByParent(String procTaskId) {
		// setDb(0, super.SLAVE); 
		return iqbWfProcTaskBeanMapper.getProcTasksByParent(procTaskId);
	}

	/**
	 * 通过流程实例ID查询指定流程任务状态的流程任务数据
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @param taskStatus
	 *            流程任务状态
	 * @return
	 */
	private List<WfProcTaskBean> getProcessTasks(String procInstId,
			List<String> taskStatus) {
		// setDb(0, super.SLAVE); 
		return iqbWfProcTaskBeanMapper.getProcessTasks(procInstId, taskStatus);
	}

	/**
	 * 得到指定流程实例的起始任务
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @return
	 * @throws WorkflowException
	 */
	private WfProcTaskBean getStartTask(String procInstId)
			throws WorkflowException {
		// setDb(0, super.SLAVE); 
		return iqbWfProcTaskBeanMapper.getStartTask(procInstId);
	}

	/**
	 * 查询指定流程实例的当前待审批任务数量
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @return
	 */
	public int getActiveTaskCount(String procInstId) {
		// setDb(0, super.SLAVE); 
		return iqbWfProcTaskBeanMapper.getActiveTaskCount(procInstId);
	}

	/**
	 * 查询某流程实例中指定任务数量
	 * 
	 * @param taskid
	 *            流程任务ID
	 * @return
	 * @throws WorkflowException
	 */
	protected int getTaskCount(String taskid) throws WorkflowException {
		// setDb(0, super.MASTER);
		return iqbWfProcTaskBeanMapper.getTaskCount(taskid);
	}

	/**
	 * 查询被委托人已被委托的流程数量
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @param mandatary
	 *            被委托人
	 * @return
	 * @throws WorkflowException
	 */
	protected int getDelegatedTaskCount(String procInstId, String mandatary) {
		// setDb(0, super.MASTER);
		return iqbWfProcTaskBeanMapper.getDelegatedTaskCount(procInstId,
				mandatary);
	}

	/**
	 * 查询某个流程实例指定任务已完成任务信息
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @param taskCode
	 *            流程任务代码
	 * @return
	 * @throws WorkflowException
	 */
	protected WfProcTaskBean getFinishedTask(String procInstId,
			String taskCode) throws WorkflowException {
		// setDb(0, super.SLAVE); 
		return iqbWfProcTaskBeanMapper.getFinishedTask(procInstId, taskCode);
	}

	/**
	 * 逻辑删除指定的流程任务
	 * 
	 * @param wfProcTaskBean
	 *            流程任务数据
	 * @return
	 */
	public int deleteProcess(WfProcTaskBean wfProcTaskBean) {
		// setDb(0, super.MASTER);
		return iqbWfProcTaskBeanMapper.deleteProcessTask(wfProcTaskBean);
	}

	/**
	 * 取消指定的流程
	 * 
	 * @param wfProcTaskBean
	 *            流程任务数据
	 * @return
	 */
	public int cancelProcess(WfProcTaskBean wfProcTaskBean) {
		// setDb(0, super.MASTER);
		return iqbWfProcTaskBeanMapper.cancelProcessTasks(wfProcTaskBean);
	}

	/**
	 * 逻辑删除指定的流程任务
	 * 
	 * @param wfProcTaskBean
	 *            流程任务数据
	 * @return
	 */
	public int endProcessTasks(WfProcTaskBean wfProcTaskBean) {
		// setDb(0, super.MASTER);
		return iqbWfProcTaskBeanMapper.endProcessTasks(wfProcTaskBean);
	}

	/**
	 * 通过流程实例提交前的流程任务信息和流程变量，生成该流程实例提交后所有待办流程任务
	 * 
	 * @param wfPreProcTaskBean
	 *            流程实例提交前的流程任务信息
	 * @param isPassed
	 *            是否审批通过
	 * @param procParallel
	 *            流程任务并行状态
	 * @param datetime
	 *            流程处理时间
	 * @return
	 * @throws WorkflowException
	 * @throws Exception
	 */
	private List<WfProcTaskBean> getNextProcTasks(
			WfProcTaskBean wfPreProcTaskBean, boolean isPassed,
			String procParallel, String procAssignee, int datetime)
			throws WorkflowException, Exception {
		List<WfProcTaskBean> wfProcTaskBeans = new ArrayList<>();
		Map<String, ActivityImpl> activities = wfProcDesinerBiz
				.getProcessDefinitionActivities(wfPreProcTaskBean.getProcId());

		// 获取下一任务节点列表
		List<Task> tasks = getProcTasks(wfPreProcTaskBean.getProcInstId());
		procParallel = tasks.size() > 1 ? DictKeyConst.YESORNO_YES
				: procParallel;

		// 循环流程实例的当前任务列表，将每个待办任务写入业务流程任务表
		for (Task newTask : tasks) {
			if (getTaskCount(newTask.getId()) > 0) {
				continue;
			}

			// 生成流程开始节点的流程已办任务信息
			WfProcTaskBean wfProcTaskBean = new WfProcTaskBean();
			wfProcTaskBean.setProcInstId(wfPreProcTaskBean.getProcInstId()); // 流程实例ID
			wfProcTaskBean.setProcId(wfPreProcTaskBean.getProcId());
			wfProcTaskBean.setProcKey(wfPreProcTaskBean.getProcKey());
			wfProcTaskBean.setProcPtaskid(wfPreProcTaskBean.getProcCtaskid());// 前置任务ID
			wfProcTaskBean.setProcPtaskcode(wfPreProcTaskBean
					.getProcCtaskcode()); // 前置任务代码
			wfProcTaskBean.setProcPtaskname(wfPreProcTaskBean
					.getProcCtaskname()); // 前置任务名称
			wfProcTaskBean.setProcExecutionid(newTask.getExecutionId());
			wfProcTaskBean.setProcTaskCommitter(wfPreProcTaskBean
					.getProcTaskAssignee()); // 流程任务提交人
			wfProcTaskBean.setProcTaskCommittime(datetime); // 流程任务提交时间
			wfProcTaskBean.setProcCtaskid(newTask.getId()); // 当前流程任务ID
			wfProcTaskBean.setProcCtaskcode(newTask.getTaskDefinitionKey()); // 当前流程任务代码
			wfProcTaskBean.setProcCtaskname(newTask.getName());// 当前流程任务名称
			wfProcTaskBean.setProcParallel(procParallel);
			wfProcTaskBean.setProcParallelStatus(WfProcParallStatus.NOTAPPROVED
					.getRetCode());

			// 如果是任务拒绝处理，则设置该任务为拒绝任务
			if (isPassed) {
				wfProcTaskBean.setProcRefusetask(DictKeyConst.YESORNO_NO);
			} else {
				wfProcTaskBean.setProcRefusetask(DictKeyConst.YESORNO_YES);
			}

			// 获取流程定义任务节点中配置的表单数据
			WfProcTaskPropertiesBean properties = wfProcDesinerBiz
					.getTaskProperties(newTask.getId());
			String procTastProperties = JSONUtil.objToJson(properties);
			wfProcTaskBean.setProcTaskProperties(procTastProperties);

			// 设置流程任务数据权限类型
			wfProcTaskBean
					.setProcDatapermission(getProcTaskDataPermission(properties));
			// 设置流程任务参与决策标识
			wfProcTaskBean.setProcVotetask(getProcVoteTask(properties));
			// 设置流程任务特殊决策权
			wfProcTaskBean.setProcVotepower(getProcVotePower(properties));
			// 设置流程任务参与投票规则
			wfProcTaskBean.setProcVoterule(getProcVoteRule(properties));
			// 设置流程任务投票权重
			wfProcTaskBean.setProcVoteweight(getProcVoteWeight(properties));
			// 设置流程任务投票阈值
			wfProcTaskBean
					.setProcVotethreshold(getProcVoteThreshold(properties));
			// 设置流程任务速决标识
			wfProcTaskBean.setProcVotequickly(getProcVoteQuickly(properties));
			// 设置流程任务审批页面URL
			wfProcTaskBean.setProcApproveurl(getProcTaskUrl(properties));

			String assignee = null;

			// 如果是拒绝处理，则取该任务的当初处理人，并将该任务自动由该处理人签收
			if (!isPassed) {
				WfProcTaskBean bean = getFinishedTask(
						wfPreProcTaskBean.getProcInstId(),
						newTask.getTaskDefinitionKey());

				if (bean != null) {
					assignee = bean.getProcTaskAssignee();
				}
			} else {
				// 如果流程定义中指定了受理人，则设置受理人并自动完成任务签收。这里不做受理人角色与任务节点配置的候选角色相同的验证
				if (StringUtil.isNull(procAssignee)) {
					assignee = wfProcDesinerBiz.getTaskAssignee(activities,
							newTask.getTaskDefinitionKey());
				} else {
					assignee = procAssignee;
				}
			}

			if (!StringUtil.isNull(assignee)) {
				// 流程指定了受理人则默认该受理人自动签收
				taskService.claim(newTask.getId(), assignee);
				wfProcTaskBean.setProcAppointUsers(assignee); // 指定受理人
				wfProcTaskBean.setProcTaskAssignee(assignee); // 流程受理人为指定受理人
				wfProcTaskBean.setProcTaskAssigntime(datetime); // 流程任务签收时间
				wfProcTaskBean
						.setProcTaskStatus(FlowStatus.TASK02.getRetCode()); // 流程任务指定受理人，任务状态待处理
			} else {
				// 没有指定受理人，则从流程定义中获取候选用户组
				String candidateGroups = wfProcDesinerBiz
						.getTaskCandidateGroups(activities,
								newTask.getTaskDefinitionKey());
				wfProcTaskBean.setProcTaskGroup(candidateGroups);
				wfProcTaskBean
						.setProcTaskStatus(FlowStatus.TASK01.getRetCode()); // 流程任务没有指定受理人，任务状态未签收
			}

			wfProcTaskBeans.add(wfProcTaskBean);
		}

		return wfProcTaskBeans;
	}

	/**
	 * 流程任务结束通知
	 * 
	 * @param wfProcBean
	 *            流程数据
	 * @param wfNextTasksBean
	 *            下一流程任务数据
	 * @param allNotifyProcess
	 *            流程任务通知人列表
	 * @param taskProperties
	 *            流程任务属性数据
	 * @throws WorkflowException
	 */
	private void notify(WfProcBean wfProcBean,
			List<WfProcTaskBean> wfNextTasksBean,
			List<WfMyProcBean> allNotifyProcess,
			WfProcTaskPropertiesBean taskProperties) throws WorkflowException {
		String isNotify = getProcNotify(taskProperties);
		String notifyKey = getProcNotifyKey(taskProperties);
		String notifyUsersBySms = getProcNotifyUsersBySms(taskProperties);
		String notifyUsersByMail = getProcNotifyUsersByMail(taskProperties);
		String notifyUsersByInnMsg = getProcNotifyUsersByInnMsg(taskProperties);
		String notifyUsersByWechat = getProcNotifyUsersByWechat(taskProperties);

		// 流程任务完成是否发送通知
		if (DictKeyConst.YESORNO_YES.equals(isNotify)) {
			// 发送流程新任务到达通知
			if (wfNextTasksBean != null && wfNextTasksBean.size() > 0) {
				wfProcTaskNotify.notify(wfProcBean, wfNextTasksBean);
			}

			// 发送流程结束通知
			if (allNotifyProcess != null && allNotifyProcess.size() > 0) {
				wfProcTaskNotify.notifyAll(wfProcBean, allNotifyProcess);
			}
		}
	}
	
	/**
	 * 通过流程实例ID获取当前的流程任务列表
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @return
	 */
	private List<Task> getProcTasks(String procInstId) {
		return taskService.createTaskQuery().processInstanceId(procInstId)
				.list();
	}

	/**
	 * 通过流程实例ID获取当前唯一的流程任务
	 * 
	 * @param procInstId
	 * @return
	 */
	private TaskEntity getProcTaskEntityByInstance(String procInstId) {
		return (TaskEntity) taskService.createTaskQuery()
				.processInstanceId(procInstId).singleResult();
	}

	/**
	 * 通过流程任务ID获取当前唯一的流程任务实体
	 * 
	 * @param procTaskid
	 * @return
	 */
	private TaskEntity getProcTaskEntityByTask(String procTaskid) {
		return (TaskEntity) taskService.createTaskQuery().taskId(procTaskid)
				.singleResult();
	}

	/**
	 * 更新我的流程数据
	 * 
	 * @param wfMyProcBeans
	 * @throws WorkflowException
	 */
	protected void insertWfMyProcess(List<WfMyProcBean> wfMyProcBeans)
			throws WorkflowException {
		List<String> myProcessUsers = new ArrayList<String>();

		if (null != wfMyProcBeans && wfMyProcBeans.size() > 0) {
			// setDb(0, super.MASTER);

			// 循环获取用户列表
			List<String> users = new ArrayList<String>();
			for (WfMyProcBean wfMyProcBean : wfMyProcBeans) {
				users.add(wfMyProcBean.getProcUser());
			}

			// 查询指定用户已经写入的我的流程数据
			List<WfMyProcBean> temp = getMyProcessByUsers(
					wfMyProcBeans.get(0).getProcInstId(), users);

			Map<String, WfMyProcBean> hmMyProcess = new HashMap<String, WfMyProcBean>();
			for (WfMyProcBean wfMyProcBean : temp) {
				hmMyProcess.put(wfMyProcBean.getProcUser(), wfMyProcBean);
			}

			for (WfMyProcBean wfMyProcBean : wfMyProcBeans) {
				if (myProcessUsers.contains(wfMyProcBean.getProcUser())) {
					// 判断是否已存在当前流程的订阅者流程数据，如果生成了则不会重复生成
					continue;
				} else {
					myProcessUsers.add(wfMyProcBean.getProcUser());
					if (hmMyProcess.containsKey(wfMyProcBean.getProcUser())) {
						WfMyProcBean myProcess = hmMyProcess
								.get(wfMyProcBean.getProcUser());
						myProcess.setProcUserType(wfMyProcBean
								.getProcUserType());
						myProcess.setProcTaskid(wfMyProcBean.getProcTaskid());
						myProcess.setProcTaskcode(wfMyProcBean
								.getProcTaskcode());
						myProcess.setProcTaskname(wfMyProcBean
								.getProcTaskname());
						myProcess.setProcDisplayurl(wfMyProcBean
								.getProcDisplayurl());
						wfMyProcBeanMapper
								.updateByPrimaryKeySelective(myProcess);
					} else {
						wfMyProcBeanMapper.insert(wfMyProcBean);
					}
				}
			}
		}
	}

	/**
	 * 通过流程实例ID获取所有的我的流程数据
	 * 
	 * @param procInstId
	 * @return
	 * @throws WorkflowException
	 */
	protected List<WfMyProcBean> getMyProcessByProcInstId(String procInstId)
			throws WorkflowException {
		// setDb(0, super.SLAVE); 
		return wfMyProcBeanMapper.selectByProcInstId(procInstId);
	}

	/**
	 * 通过用户查询我的流程数据
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @param procUser
	 *            用户代码
	 * @return
	 */
	protected WfMyProcBean getMyProcessByUser(String procInstId,
			String procUser) {
		// setDb(0, super.MASTER);
		return wfMyProcBeanMapper.getMyProcessByUser(procInstId, procUser);
	}

	/**
	 * 通过用户查询我的流程数据
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @param procUsers
	 *            用户代码列表
	 * @return
	 */
	protected List<WfMyProcBean> getMyProcessByUsers(String procInstId,
			List<String> procUsers) {
		// setDb(0, super.MASTER);
		return wfMyProcBeanMapper.getMyProcessByUsers(procInstId, procUsers);
	}

	/**
	 * 插入流程委托数据
	 * 
	 * @param delegateBean
	 *            流程委托数据
	 * @throws WorkflowException
	 */
	protected void insertProcInstDelegate(WfProcDelegateBean delegateBean)
			throws WorkflowException {
		// setDb(0, super.MASTER);
		wfProcDelegateBeanMapper.insert(delegateBean);
	}

	/**
	 * 更新流程委托数据
	 * 
	 * @param delegateBean
	 *            流程委托数据
	 * @throws WorkflowException
	 */
	protected void updateProcInstDelegate(WfProcDelegateBean delegateBean)
			throws WorkflowException {
		// setDb(0, super.MASTER);
		wfProcDelegateBeanMapper.updateByPrimaryKeySelective(delegateBean);
	}

	/**
	 * 更新取消流程委托数据
	 * 
	 * @param delegateBean
	 *            流程委托数据
	 * @throws WorkflowException
	 */
	protected void cancelProcDelegate(WfProcDelegateBean delegateBean)
			throws WorkflowException {
		// setDb(0, super.MASTER);
		wfProcDelegateBeanMapper.cancelProcDelegate(delegateBean);
	}

	/**
	 * 获取流程实例委托代理人信息
	 * 
	 * @param procInstId
	 * @param mandatary
	 * @return
	 * @throws WorkflowException
	 */
	protected List<WfProcDelegateBean> getProcInstDelegateList(
			String procInstId, String mandatary) throws WorkflowException {
		// setDb(0, super.SLAVE); 
		return wfProcDelegateBeanMapper.getProcInstDelegateList(procInstId,
				mandatary);
	}

	/**
	 * 通过受托人查询流程委托数据
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @param procMandatary
	 *            流程受托人代码
	 * @return
	 */
	protected WfProcDelegateBean getDelegateMandatary(String procInstId,
			String procMandatary) {
		// setDb(0, super.SLAVE); 
		return wfProcDelegateBeanMapper.selectByInstAndMand(procInstId,
				procMandatary);
	}

	/**
	 * 通过委托人查询流程委托数据
	 * 
	 * @param procInstId
	 *            流程实例ID
	 * @param procLicensor
	 *            流程委托人代码
	 * @return
	 */
	protected WfProcDelegateBean getDelegateLicensor(String procInstId,
			String procLicensor) {
		// setDb(0, super.SLAVE); 
		return wfProcDelegateBeanMapper.selectByInstAndLicens(procInstId,
				procLicensor);
	}

	/**
	 * 查询业务流程审批历史
	 * 
	 * @param objs
	 *            接口参数Json对象
	 * @throws WorkflowException
	 */
	public List<WfProcTaskHistoryBean> getProcApprovedHistory(JSONObject objs)
			throws WorkflowException {
		// setDb(0, super.SLAVE); 
		return iqbWfProcTaskBeanMapper.selectApprovedHistory(objs);
	}

	class ProcTaskData {
		String procInstanceId;
		String procTaskId;
		String procTaskCode;
		String procApprStatus;
		String procEnded;
		List<String> procNextTasks;

		public String getProcInstanceId() {
			return procInstanceId;
		}

		public void setProcInstanceId(String procInstanceId) {
			this.procInstanceId = procInstanceId;
		}

		public String getProcTaskId() {
			return procTaskId;
		}

		public void setProcTaskId(String procTaskId) {
			this.procTaskId = procTaskId;
		}

		public String getProcTaskCode() {
			return procTaskCode;
		}

		public void setProcTaskCode(String procTaskCode) {
			this.procTaskCode = procTaskCode;
		}

		public String getProcApprStatus() {
			return procApprStatus;
		}

		public void setProcApprStatus(String procApprStatus) {
			this.procApprStatus = procApprStatus;
		}

		public String getProcEnded() {
			return procEnded;
		}

		public void setProcEnded(String procEnded) {
			this.procEnded = procEnded;
		}

		public List<String> getProcNextTasks() {
			return procNextTasks;
		}

		public void setProcNextTasks(List<String> procNextTasks) {
			this.procNextTasks = procNextTasks;
		}
	}
}
