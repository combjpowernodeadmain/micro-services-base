/*
 * @(#) WfProcDesinerBiz.java  1.0  August 23, 2016
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
package com.bjzhianjia.scp.security.wf.base.design.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.FormService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.base.biz.WfBaseBiz;
import com.bjzhianjia.scp.security.wf.base.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.base.design.entity.WfProcActReModelBean;
import com.bjzhianjia.scp.security.wf.base.design.entity.WfProcActReProcdefBean;
import com.bjzhianjia.scp.security.wf.base.design.entity.WfProcDefinitionBean;
import com.bjzhianjia.scp.security.wf.base.design.mapper.WfActReModelBeanMapper;
import com.bjzhianjia.scp.security.wf.base.design.mapper.WfActReProcdefBeanMapper;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskPropertiesBean;
import com.bjzhianjia.scp.security.wf.base.utils.StringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageHelper;

/**
 * Description: 流程定义业务实现类
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.23    mayongming       1.0        1.0 Version
 * </pre>
 */
@Component
public class WfProcDesinerBiz extends WfBaseBiz{
    private static Map<String, ProcessDefinitionEntity> processDefEntity =
        new HashMap<String, ProcessDefinitionEntity>();
    
    public static String bpmnurl = "diagram/";
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    FormService formService;
    @Autowired
    WfActReModelBeanMapper wfActReModelBeanMapper;
    @Autowired
    WfActReProcdefBeanMapper wfActReProcdefBeanMapper;
    @Autowired
	ProcessEngineConfiguration processEngineConfiguration;
	@Autowired
	ProcessEngineFactoryBean processEngine;
    
    /**
     * 查询当前最新的流程定义列表
     * 
     * @return
     * @throws WorkflowException
     */
	public List<WfProcDefinitionBean> findAllLatestProcessDifinition(
			String tenantId) throws WorkflowException {
		List<WfProcDefinitionBean> list = new ArrayList<WfProcDefinitionBean>();

        try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

			// 查询当前最新的流程定义信息
			List<ProcessDefinition> processDefinitions = processEngine
					.getRepositoryService().createProcessDefinitionQuery()
					.processDefinitionTenantId(tenantId).latestVersion()
					.orderByProcessDefinitionKey().list();

            if (processDefinitions != null && processDefinitions.size() > 0) {
                for (ProcessDefinition processDefinition : processDefinitions) {
                    WfProcDefinitionBean wfDefinitionBean = new WfProcDefinitionBean();
                    wfDefinitionBean.setDefinitionId(processDefinition.getId());
                    wfDefinitionBean.setDefinitionKey(processDefinition.getKey());
                    wfDefinitionBean.setDefinitionName(processDefinition.getName());
                    wfDefinitionBean.setDeploymentId(processDefinition.getDeploymentId());
                    wfDefinitionBean.setResourceName(processDefinition.getResourceName());
                    wfDefinitionBean.setDiagramResourceName(processDefinition
                        .getDiagramResourceName());
                    wfDefinitionBean.setDescription(processDefinition.getDescription());
                    wfDefinitionBean.setSuspended(processDefinition.isSuspended());
                    wfDefinitionBean.setVersion(processDefinition.getVersion());
                    wfDefinitionBean.setTenantId(processDefinition.getTenantId());
                    list.add(wfDefinitionBean);
                }
            }
        } catch (Exception e) { // 其他异常情况
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010399, e);
        }

        return list;
    }

    /**
     * 根据流程定义key查询该流程定义的当前版本
     * 
     * @param processDefinitionKey
     * @return
     * @throws WorkflowException
     */
	public WfProcDefinitionBean findLatestProcessDifinitionByKey(
			String processDefinitionKey, String tenantId)
			throws WorkflowException {
		try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            WfProcDefinitionBean wfDefinitionBean = new WfProcDefinitionBean();
            
			// 流程定义key查询流程定义信息
			ProcessDefinition processDefinition = processEngine
					.getRepositoryService().createProcessDefinitionQuery()
					.processDefinitionKey(processDefinitionKey)
					.processDefinitionTenantId(tenantId).latestVersion()
					.singleResult();// 返回唯一结果集

            if (processDefinition != null) {
            	wfDefinitionBean.setDefinitionId(processDefinition.getId());
                wfDefinitionBean.setDefinitionKey(processDefinition.getKey());
                wfDefinitionBean.setDefinitionName(processDefinition.getName());
                wfDefinitionBean.setDeploymentId(processDefinition.getDeploymentId());
                wfDefinitionBean.setResourceName(processDefinition.getResourceName());
                wfDefinitionBean.setDiagramResourceName(processDefinition.getDiagramResourceName());
                wfDefinitionBean.setDescription(processDefinition.getDescription());
                wfDefinitionBean.setSuspended(processDefinition.isSuspended());
                wfDefinitionBean.setVersion(processDefinition.getVersion());
                wfDefinitionBean.setTenantId(tenantId);
            }
            
            return wfDefinitionBean;
        } catch (ActivitiObjectNotFoundException aonfe) { // 未找到指定的流程定义
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010301, aonfe);
        } catch (ActivitiException ae) { // 得到多条流程定义
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010302, ae);
        } catch (Exception e) { // 其他异常情况
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010399, e);
        }
    }

    /**
     * 根据流程定义Id查询该流程定义的当前版本
     * 
     * @param processDefinitionKey
     * @return
     * @throws WorkflowException
     */
	public WfProcDefinitionBean findLatestProcessDifinitionById(
			String processDefinitionId, String tenantId)
			throws WorkflowException {
		try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            WfProcDefinitionBean wfDefinitionBean = new WfProcDefinitionBean();
            
            // 流程定义key查询流程定义信息
			ProcessDefinition processDefinition = processEngine
					.getRepositoryService().createProcessDefinitionQuery()
					.processDefinitionId(processDefinitionId)
					.processDefinitionTenantId(tenantId).latestVersion()
					.singleResult();// 返回唯一结果集

            if (processDefinition != null) {
            	wfDefinitionBean.setDefinitionId(processDefinition.getId());
                wfDefinitionBean.setDefinitionKey(processDefinition.getKey());
                wfDefinitionBean.setDefinitionName(processDefinition.getName());
                wfDefinitionBean.setDeploymentId(processDefinition.getDeploymentId());
                wfDefinitionBean.setResourceName(processDefinition.getResourceName());
                wfDefinitionBean.setDiagramResourceName(processDefinition.getDiagramResourceName());
                wfDefinitionBean.setDescription(processDefinition.getDescription());
                wfDefinitionBean.setSuspended(processDefinition.isSuspended());
                wfDefinitionBean.setVersion(processDefinition.getVersion());
                wfDefinitionBean.setTenantId(tenantId);
            }
            
            return wfDefinitionBean;
        } catch (ActivitiObjectNotFoundException aonfe) { // 未找到指定的流程定义
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010301, aonfe);
        } catch (ActivitiException ae) { // 得到多条流程定义
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010302, ae);
        } catch (Exception e) { // 其他异常情况
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010399, e);
        }
    }

    /**
     * 根据流程定义key查询该流程定义的各历史版本
     * 
     * @param processDefinitionKey
     * @return
     * @throws WorkflowException
     */
	public List<WfProcDefinitionBean> findProcessDifinitionHistoryByKey(
			String processDefinitionKey, String tenantId)
			throws WorkflowException {
		List<WfProcDefinitionBean> list = new ArrayList<WfProcDefinitionBean>();

        try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            // 流程定义key查询流程定义信息
			List<ProcessDefinition> processDefinitions = processEngine
					.getRepositoryService().createProcessDefinitionQuery()
					.processDefinitionKey(processDefinitionKey)
					.processDefinitionTenantId(tenantId)
					.orderByProcessDefinitionVersion().desc().list();

            if (processDefinitions != null && processDefinitions.size() > 0) {
                for (ProcessDefinition processDefinition : processDefinitions) {
                    WfProcDefinitionBean wfDefinitionBean = new WfProcDefinitionBean();
                    wfDefinitionBean.setDefinitionId(processDefinition.getId());
                    wfDefinitionBean.setDefinitionKey(processDefinition.getKey());
                    wfDefinitionBean.setDefinitionName(processDefinition.getName());
                    wfDefinitionBean.setDeploymentId(processDefinition.getDeploymentId());
                    wfDefinitionBean.setResourceName(processDefinition.getResourceName());
                    wfDefinitionBean.setDiagramResourceName(processDefinition
                        .getDiagramResourceName());
                    wfDefinitionBean.setDescription(processDefinition.getDescription());
                    wfDefinitionBean.setSuspended(processDefinition.isSuspended());
                    wfDefinitionBean.setVersion(processDefinition.getVersion());
                    wfDefinitionBean.setTenantId(tenantId);
                    list.add(wfDefinitionBean);
                }
            }
        } catch (ActivitiObjectNotFoundException aonfe) { // 未找到指定的流程定义
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010301, aonfe);
        } catch (Exception e) { // 其他异常情况
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010399, e);
        }

        return list;
    }

    /**
     * 根据流程定义Id查询该流程定义的各历史版本
     * 
     * @param processDefinitionKey
     * @return
     * @throws WorkflowException
     */
	public List<WfProcDefinitionBean> findProcessDifinitionHistoryById(
			String processDefinitionId, String tenantId)
			throws WorkflowException {
        List<WfProcDefinitionBean> list = new ArrayList<WfProcDefinitionBean>();

        try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

			// 流程定义key查询流程定义信息
			List<ProcessDefinition> processDefinitions = processEngine
					.getRepositoryService().createProcessDefinitionQuery()
					.processDefinitionId(processDefinitionId)
					.processDefinitionTenantId(tenantId)
					.orderByProcessDefinitionVersion().desc().list();

            if (processDefinitions != null && processDefinitions.size() > 0) {
                for (ProcessDefinition processDefinition : processDefinitions) {
                    WfProcDefinitionBean wfDefinitionBean = new WfProcDefinitionBean();
                    wfDefinitionBean.setDefinitionId(processDefinition.getId());
                    wfDefinitionBean.setDefinitionKey(processDefinition.getKey());
                    wfDefinitionBean.setDefinitionName(processDefinition.getName());
                    wfDefinitionBean.setDeploymentId(processDefinition.getDeploymentId());
                    wfDefinitionBean.setResourceName(processDefinition.getResourceName());
                    wfDefinitionBean.setDiagramResourceName(processDefinition
                        .getDiagramResourceName());
                    wfDefinitionBean.setDescription(processDefinition.getDescription());
                    wfDefinitionBean.setSuspended(processDefinition.isSuspended());
                    wfDefinitionBean.setVersion(processDefinition.getVersion());
                    wfDefinitionBean.setTenantId(tenantId);
                    list.add(wfDefinitionBean);
                }
            }
        } catch (ActivitiObjectNotFoundException aonfe) { // 未找到指定的流程定义
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010301, aonfe);
        } catch (Exception e) { // 其他异常情况
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010399, e);
        }

        return list;
    }

    /**
     * 删除流程定义
     * 
     * @param deploymentId
     * @throws WorkflowException
     */
	public void deleteProcessDifinition(String deploymentId, String tenantId)
			throws WorkflowException {
		try {
            if (StringUtil.isNull(deploymentId)) {
                
            }

            // 创建默认流程引擎
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
			long count = processEngine.getRepositoryService()
					.createDeploymentQuery().deploymentId(deploymentId)
					.deploymentTenantId(tenantId).count();
			if (count > 0) {
				processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
			} else {
				throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02011002);
			}
			
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02011099, e);
        }
    }

    /**
     * 验证流程定义是否有效
     * 
     * @param bpmnModel
     */
    public List<String> validateProcess(BpmnModel bpmnModel) {
        ProcessValidatorFactory processValidatorFactory = new ProcessValidatorFactory();
        ProcessValidator defaultProcessValidator =
            processValidatorFactory.createDefaultProcessValidator();

        // 验证失败信息封装ValidationError列表
        List<ValidationError> errorList = defaultProcessValidator.validate(bpmnModel);

        if (errorList != null && errorList.size() > 0) {
            List<String> errorMsgs = new ArrayList<String>();

            for (ValidationError error : errorList) {
                errorMsgs.add("Error Line: " + error.getXmlLineNumber() + "Error Activity Name:"
                    + error.getActivityName() + "Error Message:" + error.getProblem());
            }
            
            return errorMsgs;
        }

        return null;
    }

    /**
     * 通过流程定义ID获取发布的流程定义信息
     * 
     * @param procId
     *            流程定义ID
     * @return
     * @throws WorkflowException
     */
	public synchronized ProcessDefinitionEntity getDeployedProcessDefinition(
			String procId, String tenantId) throws WorkflowException {
        if (!processDefEntity.containsKey(procId)) {
            ProcessDefinitionEntity definition =
                (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(procId);

            if (definition != null && tenantId.equals(definition.getTenantId())) {
                processDefEntity.put(procId, definition);
            } else {
                throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020011);
            }
        }

        return processDefEntity.get(procId);
    }

    /**
     * 从流程定义信息中获取指定任务代码的流程活动
     * @param definition    流程定义ID
     * @param taskCode
     * @return
     */
    public ActivityImpl getActivity(ProcessDefinitionEntity definition, String taskCode) {
        return ((ProcessDefinitionImpl) definition).findActivity(taskCode);
    }
    
    /**
     * 获取指定流程定义的各任务节点
     * 
     * @param procDefinitionId
     *            流程定义ID
     * @return
     */
	public Map<String, ActivityImpl> getProcessDefinitionActivities(
			String procDefinitionId, String tenantId) throws WorkflowException {
        // 获取流程部署实体
        ProcessDefinitionEntity procDef = getDeployedProcessDefinition(procDefinitionId, tenantId);

        if (procDef == null) {
        	throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020011);
        }
        
        List<ActivityImpl> activityList = procDef.getActivities();
        Map<String, ActivityImpl> activityMap = new HashMap<String, ActivityImpl>();

        if (activityList != null && activityList.size() > 0) {
            for (ActivityImpl activityImpl : activityList) {
                activityMap.put(activityImpl.getId(), activityImpl);
            }
        }

        return activityMap;
    }
    
    /**
     * 得到指定流程定义节点的候选用户组列表
     * 
     * @param activities
     * @param taskDefinitionKey
     *            流程节点代码
     * @return
     */
    public String getTaskCandidateGroups(Map<String, ActivityImpl> activities,
        String taskDefinitionKey) {
        ActivityImpl activityImpl = activities.get(taskDefinitionKey);
        StringBuilder groups = new StringBuilder();

        if (activityImpl != null
            && "userTask".equalsIgnoreCase(activityImpl.getProperty("type").toString())) {
            TaskDefinition taskDefinition =
                ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            Iterator<Expression> iterator =
                taskDefinition.getCandidateGroupIdExpressions().iterator();

            while (iterator.hasNext()) {
                groups.append(iterator.next()).append(",");
            }

        }

        return groups.toString().endsWith(",") ? groups.toString().substring(0,
            groups.toString().length() - 1) : groups.toString();
    }

    /**
     * 得到指定流程定义节点的候选用户列表
     * 
     * @param activities
     * @param taskDefinitionKey
     *            流程节点代码
     * @return
     */
    public String getTaskCandidateUsers(Map<String, ActivityImpl> activities,
        String taskDefinitionKey) {
        ActivityImpl activityImpl = activities.get(taskDefinitionKey);
        StringBuilder users = new StringBuilder();

        if (activityImpl != null
            && "userTask".equalsIgnoreCase(activityImpl.getProperty("type").toString())) {
            TaskDefinition taskDefinition =
                ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            Iterator<Expression> iterator =
                taskDefinition.getCandidateUserIdExpressions().iterator();

            while (iterator.hasNext()) {
                users.append(iterator.next()).append(",");
            }
        }
        return users.toString().endsWith(",") ? users.toString().substring(0,
            users.toString().length() - 1) : users.toString();
    }

    /**
     * 得到指定流程定义节点的指定处理人
     * 
     * @param activities
     * @param taskDefinitionKey
     *            流程节点代码
     * @return
     */
    public String getTaskAssignee(Map<String, ActivityImpl> activities, String taskDefinitionKey) {
        ActivityImpl activityImpl = activities.get(taskDefinitionKey);
        if (activityImpl != null
            && "userTask".equalsIgnoreCase(activityImpl.getProperty("type").toString())) {
            TaskDefinition taskDefinition =
                ((UserTaskActivityBehavior) activityImpl.getActivityBehavior()).getTaskDefinition();
            Expression expression = taskDefinition.getAssigneeExpression();

            return expression == null ? null : expression.getExpressionText();
        }
        return null;
    }

    /**
     * 获取流程任务节点表单定义的属性数据，表单数据支持的类型包括： 
     * string   org.activiti.engine.impl.form.StringFormType 
     * long     org.activiti.engine.impl.form.LongFormType 
     * enum     org.activiti.engine.impl.form.EnumFormType 
     * date     org.activiti.engine.impl.form.DateFormType 
     * boolean  org.activiti.engine.impl.form.BooleanFormType
     * 
     * @param taskId
     *            任务ID
     * @return
     */
    public WfProcTaskPropertiesBean getTaskProperties(String taskId) {
        TaskFormData taskFormData = formService.getTaskFormData(taskId);
        List<FormProperty> formProperties = taskFormData.getFormProperties();
        if (formProperties != null && formProperties.size() > 0) {
            WfProcTaskPropertiesBean taskProperties = new WfProcTaskPropertiesBean();

            for (FormProperty formProperty : formProperties) {
                String id = formProperty.getId();
                String type = formProperty.getType().getName();
                String value = formProperty.getValue();

                // 如果id,type,value有空值，则不计入流程任务属性
                if (StringUtil.isNull(id) || StringUtil.isNull(type) || StringUtil.isNull(value)) {
                    continue;
                }
                taskProperties.add(id, formProperty.getName(), type, value);
            }
            return taskProperties;
        } else {
            return null;
        }
    }

    /**
     * 功能：流程部署
     * 
     * @param procDefId
     *            //流程定义id
     * @param procDefName
     *            //流程定义name
     */
	public void deploymentProcess(String procDefId, String procDefName,
			String tenantId) throws WorkflowException {
		String procUrl = bpmnurl + procDefId + ".bpmn";
		
		if (StringUtil.isNull(procDefName)) {
			procDefName = procDefId;// 给流程部署name默认值
		}

		// 当前用户所属租户与流程定义所属租户不一致，当前用户没有权限部署该流程
		ProcessDefinition procDef = repositoryService.getProcessDefinition(procDefId);
		if (procDef == null || !tenantId.equals(procDef.getTenantId())) {
			throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010002);
		}

		repositoryService.createDeployment().name(procDefName)
				.tenantId(tenantId).addClasspathResource(procUrl)
				.addClasspathResource(procUrl).deploy();
	}
	
    /**
     * 功能：流程部署
     * 
     * @param procDefId
     *            //流程定义id
     * @param procDefName
     *            //流程定义name
     */
	public void deploymentModel(String modelId, String tenantId)
			throws WorkflowException {
        Model modelData = repositoryService.getModel(modelId);
        ObjectNode modelNode;
        
        // 当前用户所属租户与流程定义所属租户不一致，当前用户没有权限部署该流程
        if (!tenantId.equals(modelData.getTenantId())) {
        	throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010002);
        }
        
		try {
			modelNode = (ObjectNode) new ObjectMapper()
					.readTree(repositoryService.getModelEditorSource(modelData.getId()));
		} catch (IOException e) {
			throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010003, e);
		}
        
        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
        String processName = modelData.getName() + ".bpmn20.xml";
        
		repositoryService.createDeployment().name(modelData.getName())
				.tenantId(modelData.getTenantId())
				.addString(processName, new String(bpmnBytes)).deploy();
	}

    /**
     * 功能：流程模型删除
     * 
     * @param modelId
     *            模型ID
     */
	public void deleteModel(String modelId, String tenantId)
			throws WorkflowException {
		Model modelData = repositoryService.getModel(modelId);
	
    	// 当前用户所属租户与流程定义所属租户不一致，当前用户没有权限删除该流程
        if (modelData == null || !tenantId.equals(modelData.getTenantId())) {
        	throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010106);
        }
        
        repositoryService.deleteModel(modelId);
    }
    
    /**
     * 功能：流程删除
     * 
     * @param modelId
     *            模型ID
     */
	public void wfdel(String deploymentId, String tenantId)
			throws WorkflowException {
		long count = repositoryService.createDeploymentQuery()
				.deploymentId(deploymentId).deploymentTenantId(tenantId).count();
		
		if (count > 0) {
			repositoryService.deleteDeployment(deploymentId, true);
		} else {
			throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010107);
		}
    }
    /**
     * 功能：流程导出
     * 
     * @param modelId
     *            模型ID
     * @throws IOException 
     * @throws JsonProcessingException 
     */
	public void export(String type, String modelId, String tenantId)
			throws JsonProcessingException, IOException {
		Model modelData = repositoryService.getModel(modelId);
        
        if (modelData == null || tenantId.equals(modelData.getTenantId())) {
        	throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010108);
        }
        
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

        JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
        String filename = bpmnModel.getMainProcess().getId();
        byte[] exportBytes = null;

        if (type.equals("bpmn")) {
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            exportBytes = xmlConverter.convertToXML(bpmnModel);
            filename = filename + ".bpmn";
        } else if (type.equals("json")) {
            exportBytes = modelEditorSource;
            filename = filename + ".json";
        }
        
        File file = new File("C:\\export\\"+filename);
        
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        
        // 建立输出字节流
        FileOutputStream fos = new FileOutputStream(file);
        // 用FileOutputStream 的write方法写入字节数组
        fos.write(exportBytes);
        fos.flush();
        // 为了节省IO流的开销，需要关闭
        fos.close();
    }
    
    /**
     * 功能：流程挂起
     * 
     * @param modelId
     *            模型ID
     */
	public void wfsuspend(String procDefId, String tenantId)
			throws WorkflowException {
		long count = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(procDefId)
				.processDefinitionTenantId(tenantId).count();
		
		if (count > 0) {
			repositoryService.suspendProcessDefinitionById(procDefId, true, null);
		} else {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02010202);
		}
	}
    
    /**
     * 功能：流程恢复
     * 
     * @param modelId
     *            模型ID
     */
    public void wfactive(String procDefId, String tenantId)
			throws WorkflowException {
    	long count = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(procDefId)
				.processDefinitionTenantId(tenantId).count();
		
		if (count > 0) {
			repositoryService.activateProcessDefinitionById(procDefId, true, null);
		} else {
			throw new WorkflowException(WorkflowEnumResults.WF_TASK_02010211);
		}
    }

    /**
     * 功能：流程模型列表
     * 
     */
    public List<WfProcActReModelBean> selectModelList(JSONObject objs) {
        // setDb(0, super.SLAVE); 
        // 开始分页,采用分页插件分页,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        return wfActReModelBeanMapper.selectModelList(objs);
    }
    
    /**
     * 功能：流程部署列表
     * 
     * @author 
     * @date 2016-09-14
     */
    public List<WfProcActReProcdefBean> processList(JSONObject objs) {
        // setDb(0, super.SLAVE); 
        // 开始分页,采用分页插件分页,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        return wfActReProcdefBeanMapper.processList(objs);
    }
    
	public InputStream openImageByProcessDefinition(String procDefId,
			String tenantId) throws WorkflowException {
		ProcessDefinitionEntity procDef = getDeployedProcessDefinition(
				procDefId, tenantId);
		String diagramResourceName = procDef.getDiagramResourceName();
		return repositoryService.getResourceAsStream(procDef.getDeploymentId(),
				diagramResourceName);
	}

	public InputStream openImageByProcessInstance(String executionId,
			String tenantId) throws WorkflowException {
		ProcessInstance processInstance = runtimeService
				.createProcessInstanceQuery().processInstanceId(executionId)
				.processInstanceTenantId(tenantId).singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance
				.getProcessDefinitionId());
		List<String> activeActivityIds = runtimeService
				.getActiveActivityIds(executionId);

		// 不使用spring请使用下面的两行代码
//		ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines
//				.getDefaultProcessEngine();
//		Context.setProcessEngineConfiguration(defaultProcessEngine
//				.getProcessEngineConfiguration());

		// 使用spring注入引擎请使用下面的这行代码
		processEngineConfiguration = processEngine
				.getProcessEngineConfiguration();
		Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

		ProcessDiagramGenerator diagramGenerator = processEngineConfiguration
				.getProcessDiagramGenerator();
		return diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
	}
}
