/*
 * @(#) WfProcDesinerRest.java  1.0  August 29, 2016
 *
 * Copyright 2016 by 北京爱钱帮财富科技有限公司
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * SCP("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with SCP.
 */
package com.bjzhianjia.scp.security.wf.rest;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.wf.base.ResultUtils;
import com.bjzhianjia.scp.security.wf.entity.WfProcActReModelBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcActReProcdefBean;
import com.bjzhianjia.scp.security.wf.service.IWfProcDesignService;
import com.github.pagehelper.PageInfo;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

/**
 * Description: 工作流流程设计Rest服务接口
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.29    mayongming       1.0        1.0 Version
 * 2016.09.01    scp       1.0        新增流程部署功能
 * </pre>
 */
@Controller
@CheckUserToken
@SuppressWarnings("rawtypes")
@RequestMapping("/design")
@Api(value = "工作流流程设计Rest服务接口", tags = "流程设计")
@Slf4j
public class WfProcDesignRest {

    @Autowired
    IWfProcDesignService wfProcDesignService;
    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    ProcessEngineConfiguration processEngineConfiguration;
    @Autowired
    ProcessEngineFactoryBean processEngine;

    /**
     * 功能：部署流程
     * 
     * @param objs
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deployementProcess")
    public ObjectRestResponse<Map> deploymentProcess(@RequestBody JSONObject objs) {
            log.debug("SCP信息---部署工作流...开始");
            wfProcDesignService.deploymentProcess(objs);
            log.debug("SCP信息---部署工作流...结束");
            return ResultUtils.success();
    }

    /**
     * 根据Model部署流程
     */
    @ResponseBody
    @RequestMapping(value = "modeldeploy")
    public ObjectRestResponse<Map> deploy(@RequestBody JSONObject objs) {
        log.debug("SCP信息---部署工作流...开始");
        wfProcDesignService.deploymentModel(objs);
        log.debug("SCP信息---部署工作流...结束");
        return ResultUtils.success();
    }

    /**
     * 模型列表
     */
    @RequestMapping(value = "modellist")
    @ResponseBody
    public ObjectRestResponse<PageInfo> modelList(@RequestBody JSONObject objs) {
        log.debug("SCP信息---开始分页查询数据...");
        PageInfo<WfProcActReModelBean> pageInfo = wfProcDesignService.selectModelList(objs);
        log.info("SCP信息---分页查询数据完成.");
        
        return ResultUtils.success(pageInfo);
    }

    /**
     * 功能：流程模型删除
     * 
     * @date 2016-09-13
     * @author scp
     */
    @ResponseBody
    @RequestMapping(value = "/modeldelete")
    public ObjectRestResponse<Map> modeldelete(@RequestBody JSONObject objs) {
            log.debug("SCP信息---删除模型...开始");
            wfProcDesignService.modeldelete(objs);
            // repositoryService.deleteModel(objs.getString(key));
            log.debug("SCP信息---删除模型...结束");
            return ResultUtils.success();

    }

    /**
     * 功能：部署流程删除
     * 
     * @date 2016-09-20
     * @author scp
     */
    @ResponseBody
    @RequestMapping(value = "/wfdel")
    public ObjectRestResponse<Map> wfdel(@RequestBody JSONObject objs) {
        log.debug("SCP信息---删除流程...开始");
        wfProcDesignService.wfdel(objs);
        // repositoryService.deleteModel(objs.getString(key));
        log.debug("SCP信息---删除流程...结束");
        return ResultUtils.success();
    }

    /**
     * 功能：部署流程挂起
     * 
     * @date 2016-09-20
     * @author scp
     */
    @ResponseBody
    @RequestMapping(value = "/wfsuspend")
    public ObjectRestResponse<Map> wfsuspend(@RequestBody JSONObject objs) {
        log.debug("SCP信息---挂起流程...开始");
        wfProcDesignService.wfsuspend(objs);
        // repositoryService.deleteModel(objs.getString(key));
        log.debug("SCP信息---挂起流程...结束");
        return ResultUtils.success();
    }

    /**
     * 功能：部署流程恢复
     * 
     * @date 2016-09-20
     * @author scp
     */
    @ResponseBody
    @RequestMapping(value = "/wfactive")
    public ObjectRestResponse<Map> wfactive(@RequestBody JSONObject objs) {
        log.debug("SCP信息---恢复流程...开始");
        wfProcDesignService.wfactive(objs);
        // repositoryService.deleteModel(objs.getString(key));
        log.debug("SCP信息---恢复流程...结束");
        return ResultUtils.success();
    }

    /**
     * 功能：查询已经部署的流程{@暂时不用}
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryDeplPro")
    public void queryDeploymentProcessList() {
        List<Object[]> objects = new ArrayList<Object[]>();
        ProcessDefinitionQuery processDefinitionQuery =
            repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
        List<ProcessDefinition> processDefinitionList = processDefinitionQuery.listPage(1, 2);
        for (ProcessDefinition processDefinition : processDefinitionList) {
            String deploymentId = processDefinition.getDeploymentId();
            Deployment deployment =
                repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
            System.out.println(deployment);
            System.out.println(processDefinition.getId());
            System.out.println(processDefinition.getDeploymentId());
            System.out.println(processDefinition.getKey());
            System.out.println(processDefinition.getName());
            System.out.println(deployment.getId());
            objects.add(new Object[] { processDefinition, deployment });
        }
    }

    /**
     * 流程定义列表{@正式代码}
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/processlist")
    public ObjectRestResponse processList(@RequestBody JSONObject objs) {
        log.debug("SCP信息---开始分页查询数据...");
        PageInfo<WfProcActReProcdefBean> pageInfo = wfProcDesignService.processList(objs);
        log.info("SCP信息---分页查询数据完成.");
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        linkedHashMap.put("result", pageInfo);
        return ResultUtils.success(pageInfo);
    }

    /**
     * 功能：获取流程图片{@正式代码}
     * 
     * @date 2016-09-26
     * @author scp
     */
    @RequestMapping(value = "/openimage")
    public void loadByDeployment(@RequestParam("procDefId") String procDefId,
        HttpServletResponse response) throws Exception {
        try {
            ProcessDefinition procDef =
                repositoryService.createProcessDefinitionQuery().processDefinitionId(procDefId)
                    .singleResult();
            String diagramResourceName = procDef.getDiagramResourceName();
            InputStream pic =
                repositoryService.getResourceAsStream(procDef.getDeploymentId(),
                    diagramResourceName);
            byte[] b = new byte[1024];
            int len = -1;
            while ((len = pic.read(b, 0, 1024)) != -1) {
                response.setContentType("image/png");
                response.getOutputStream().write(b, 0, len);
            }
        } catch (Exception e) {
            log.error("获取图片失败。。。");
            e.printStackTrace();
        }
    }

    /**
     * 功能：获取流程高亮显示当前节点图片{@暂时不用}
     * http://localhost/etep.front/WfDesign/openimage2?executionId=edb4858f-943f-11e6-b980-64006a272fcf
     * @date 2016-09-26
     * executionId 流程实例ID
     * @author scp
     */
    @RequestMapping(value = "/openimage2")
    public void readResource(@RequestParam("executionId") String executionId, HttpServletResponse response)
        throws Exception {
	    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId).singleResult();
	    BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
	    List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
	    // 不使用spring请使用下面的两行代码
	//ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl) ProcessEngines.getDefaultProcessEngine();
	//Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());
	
	    // 使用spring注入引擎请使用下面的这行代码
	    processEngineConfiguration = processEngine.getProcessEngineConfiguration();
	    Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
	
	    ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
	    InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);
	
	    // 输出资源内容到相应对象
	    byte[] b = new byte[1024];
	    int len;
	    while ((len = imageStream.read(b, 0, 1024)) != -1) {
	        response.getOutputStream().write(b, 0, len);
	    }
	}
    
    /**
     * 功能：导出xml{@正式代码}
     * 
     * @date 2016-10-10
     * @author scp
     */
    @ResponseBody
    @RequestMapping(value = "/export")
    public ObjectRestResponse<Map> export(@RequestBody JSONObject objs) {
        log.debug("SCP信息---导出模型...开始");
        wfProcDesignService.export(objs);
        log.debug("SCP信息---导出模型...结束");
        return ResultUtils.success();
    }

}
