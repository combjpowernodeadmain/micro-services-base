/*
 * @(#) WfProcDesinerBiz.java  1.0  August 23, 2016
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
package com.bjzhianjia.scp.security.wf.biz;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.validation.ProcessValidator;
import org.activiti.validation.ProcessValidatorFactory;
import org.activiti.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.base.WfBaseBiz;
import com.bjzhianjia.scp.security.wf.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcessDataAttr;
import com.bjzhianjia.scp.security.wf.entity.WfProcActReModelBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcActReProcdefBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcDefinitionBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcTaskPropertiesBean;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.mapper.WfActReModelBeanMapper;
import com.bjzhianjia.scp.security.wf.mapper.WfActReProcdefBeanMapper;
import com.bjzhianjia.scp.security.wf.utils.StringUtil;
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
    FormService formService;
    @Autowired
    WfActReModelBeanMapper iqbWfActReModelBeanMapper;
    @Autowired
    WfActReProcdefBeanMapper iqbWfActReProcdefBeanMapper;

    /**
     * 查询当前最新的流程定义列表
     * 
     * @return
     * @throws WorkflowException
     */
    public List<WfProcDefinitionBean> findAllLatestProcessDifinition() throws WorkflowException {
        List<WfProcDefinitionBean> list = new ArrayList<WfProcDefinitionBean>();

        try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            // 查询当前最新的流程定义信息
            List<ProcessDefinition> processDefinitions =
                processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion()
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
    public WfProcDefinitionBean findLatestProcessDifinitionByKey(String processDefinitionKey)
        throws WorkflowException {
        try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            // 流程定义key查询流程定义信息
            ProcessDefinition processDefinition =
                processEngine.getRepositoryService().createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey).latestVersion().singleResult();// 返回唯一结果集

            WfProcDefinitionBean wfDefinitionBean = new WfProcDefinitionBean();
            wfDefinitionBean.setDefinitionId(processDefinition.getId());
            wfDefinitionBean.setDefinitionKey(processDefinition.getKey());
            wfDefinitionBean.setDefinitionName(processDefinition.getName());
            wfDefinitionBean.setDeploymentId(processDefinition.getDeploymentId());
            wfDefinitionBean.setResourceName(processDefinition.getResourceName());
            wfDefinitionBean.setDiagramResourceName(processDefinition.getDiagramResourceName());
            wfDefinitionBean.setDescription(processDefinition.getDescription());
            wfDefinitionBean.setSuspended(processDefinition.isSuspended());
            wfDefinitionBean.setVersion(processDefinition.getVersion());

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
    public WfProcDefinitionBean findLatestProcessDifinitionById(String processDefinitionId)
        throws WorkflowException {
        try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            // 流程定义key查询流程定义信息
            ProcessDefinition processDefinition =
                processEngine.getRepositoryService().createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId).latestVersion().singleResult();// 返回唯一结果集

            WfProcDefinitionBean wfDefinitionBean = new WfProcDefinitionBean();
            wfDefinitionBean.setDefinitionId(processDefinition.getId());
            wfDefinitionBean.setDefinitionKey(processDefinition.getKey());
            wfDefinitionBean.setDefinitionName(processDefinition.getName());
            wfDefinitionBean.setDeploymentId(processDefinition.getDeploymentId());
            wfDefinitionBean.setResourceName(processDefinition.getResourceName());
            wfDefinitionBean.setDiagramResourceName(processDefinition.getDiagramResourceName());
            wfDefinitionBean.setDescription(processDefinition.getDescription());
            wfDefinitionBean.setSuspended(processDefinition.isSuspended());
            wfDefinitionBean.setVersion(processDefinition.getVersion());

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
    public List<WfProcDefinitionBean> findProcessDifinitionHistoryByKey(String processDefinitionKey)
        throws WorkflowException {
        List<WfProcDefinitionBean> list = new ArrayList<WfProcDefinitionBean>();

        try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            // 流程定义key查询流程定义信息
            List<ProcessDefinition> processDefinitions =
                processEngine.getRepositoryService().createProcessDefinitionQuery()
                    .processDefinitionKey(processDefinitionKey).orderByProcessDefinitionVersion()
                    .desc().list();

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
    public List<WfProcDefinitionBean> findProcessDifinitionHistoryById(String processDefinitionId)
        throws WorkflowException {
        List<WfProcDefinitionBean> list = new ArrayList<WfProcDefinitionBean>();

        try {
            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

            // 流程定义key查询流程定义信息
            List<ProcessDefinition> processDefinitions =
                processEngine.getRepositoryService().createProcessDefinitionQuery()
                    .processDefinitionId(processDefinitionId).orderByProcessDefinitionVersion()
                    .desc().list();

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
    public void deleteProcessDifinition(String deploymentId) throws WorkflowException {
        try {
            if (StringUtil.isNull(deploymentId)) {
                throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02011001);
            }

            // 创建默认流程引擎
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            processEngine.getRepositoryService().deleteDeployment(deploymentId, true);
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
    public synchronized ProcessDefinitionEntity getDeployedProcessDefinition(String procId)
        throws WorkflowException {
        if (!processDefEntity.containsKey(procId)) {
            ProcessDefinitionEntity definition =
                (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
                    .getDeployedProcessDefinition(procId);

            if (definition != null) {
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
    public Map<String, ActivityImpl> getProcessDefinitionActivities(String procDefinitionId)
        throws WorkflowException {
        // 获取流程部署实体
        ProcessDefinitionEntity procDef = getDeployedProcessDefinition(procDefinitionId);

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
     * @author zhaomingli
     * @date 2016-09-01
     * @param procDefId
     *            //流程定义id
     * @param procDefName
     *            //流程定义name
     */
    public void deploymentProcess(JSONObject objs) {
        // 流程定义id
        String procDefId = objs.getString(WfProcessDataAttr.PROC_DEFINITIONID);                              
        // 流程定义name
        String procDefName = objs.getString(WfProcessDataAttr.PROC_DEFINITIONNAME);
        // 流程定义id不能为空
        if (StringUtil.isNull(procDefId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010001);
        }
        if (StringUtil.isNull(procDefName)) {
            procDefName = procDefId;// 给流程部署name默认值
        }
        //
        String procUrl = bpmnurl + procDefId + ".bpmn";
        repositoryService.createDeployment().name(procDefName).addClasspathResource(procUrl)
            .addClasspathResource(procUrl).deploy();
    }
    /**
     * 功能：流程部署
     * 
     * @author zhaomingli
     * @date 2016-09-01
     * @param procDefId
     *            //流程定义id
     * @param procDefName
     *            //流程定义name
     */
    public void deploymentModel(JSONObject objs) {
        // 流程模型id
        String modelId = objs.getString(WfProcessDataAttr.PROC_MODELID);
        // 流程模型id不能为空
        if (StringUtil.isNull(modelId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010101);
        }
        
        Model modelData = repositoryService.getModel(modelId);
        ObjectNode modelNode;
		try {
			modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
		} catch (IOException e) {
			e.printStackTrace();
			throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010003, e);
		}
        byte[] bpmnBytes = null;

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        String processName = modelData.getName() + ".bpmn20.xml";
        repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
        
    }

    /**
     * 功能：流程模型删除
     * 
     * @author zhaomingli
     * @date 2016-09-14
     * @param modelId
     *            模型ID
     */
    public void modeldelete(JSONObject objs) {
        String modelId = objs.getString(WfProcessDataAttr.PROC_MODELID);
        if (StringUtil.isNull(modelId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010101);
        }
        repositoryService.deleteModel(modelId);
    }
    
    /**
     * 功能：流程删除
     * 
     * @author zhaomingli
     * @date 2016-09-20
     * @param modelId
     *            模型ID
     */
    public void wfdel(JSONObject objs) {
        String deploymentId = objs.getString(WfProcessDataAttr.PROC_DEPLOYMENTID);
        if (StringUtil.isNull(deploymentId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010102);
        }
        repositoryService.deleteDeployment(deploymentId);
    }
    /**
     * 功能：流程导出
     * 
     * @author zhaomingli
     * @date 2016-10-10
     * @param modelId
     *            模型ID
     * @throws IOException 
     * @throws JsonProcessingException 
     */
    public void export(JSONObject objs) throws JsonProcessingException, IOException {
        String modelId = objs.getString(WfProcessDataAttr.PROC_MODELID);
        if (StringUtil.isNull(modelId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010105);
        }
        String type = "bpmn";
        Model modelData = repositoryService.getModel(modelId);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

        JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
        BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
        String filename = "";
        byte[] exportBytes = null;

        String mainProcessId = bpmnModel.getMainProcess().getId();

        if (type.equals("bpmn")) {
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            exportBytes = xmlConverter.convertToXML(bpmnModel);

            filename = mainProcessId + ".bpmn";
        } else if (type.equals("json")) {

            exportBytes = modelEditorSource;
            filename = mainProcessId + ".json";

        }
        File file = new File("C:\\export\\"+filename);
        System.out.println(file.getParentFile().exists());
        if(!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        
        // 建立输出字节流
        FileOutputStream fos = new FileOutputStream(file);
        // 用FileOutputStream 的write方法写入字节数组
        fos.write(exportBytes);
        System.out.println("写入成功");
        // 为了节省IO流的开销，需要关闭
        fos.close();
    }
    
    /**
     * 功能：流程挂起
     * 
     * @author zhaomingli
     * @date 2016-09-20
     * @param modelId
     *            模型ID
     */
    public void wfsuspend(JSONObject objs) {
        String procDefId = objs.getString(WfProcessDataAttr.PROC_DEFINITIONID);
        if (StringUtil.isNull(procDefId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02010201);
        }
        repositoryService.suspendProcessDefinitionById(procDefId, true, null);
        System.out.println("******************挂起成功******");
    }
    
    /**
     * 功能：流程恢复
     * 
     * @author zhaomingli
     * @date 2016-09-20
     * @param modelId
     *            模型ID
     */
    public void wfactive(JSONObject objs) {
        String procDefId = objs.getString(WfProcessDataAttr.PROC_DEFINITIONID);
        if (StringUtil.isNull(procDefId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02010210);
        }
        repositoryService.activateProcessDefinitionById(procDefId, true, null);
        System.out.println("******************恢复成功******");
    }

    /**
     * 功能：流程模型列表
     * 
     * @author zhaomingli
     * @date 2016-09-14
     */
    public List<WfProcActReModelBean> selectModelList(JSONObject objs) {
        // setDb(0, super.SLAVE); 
        // 开始分页,采用分页插件分页,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        return iqbWfActReModelBeanMapper.selectModelList(objs);
    }
    
    /**
     * 功能：流程部署列表
     * 
     * @author zhaomingli
     * @date 2016-09-14
     */
    public List<WfProcActReProcdefBean> processList(JSONObject objs) {
        // setDb(0, super.SLAVE); 
        // 开始分页,采用分页插件分页,底层使用拦截器,所以XML中不用关心分页参数
        PageHelper.startPage(getPagePara(objs));
        return iqbWfActReProcdefBeanMapper.processList(objs);
    }
    
}
