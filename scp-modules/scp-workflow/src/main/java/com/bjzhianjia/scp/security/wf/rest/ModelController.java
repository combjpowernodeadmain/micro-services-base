package com.bjzhianjia.scp.security.wf.rest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.wf.base.ResultUtils;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcessBizDataAttr;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcessDataAttr;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.extern.slf4j.Slf4j;

/**
 * 流程模型控制器
 *
 * @author scp
 */
@Controller
@RequestMapping(value = "/model")
@Slf4j
public class ModelController {

    @Autowired
    RepositoryService repositoryService;

    /**
     * 模型列表
     */
    @RequestMapping(value = "list")
    @ResponseBody
    public ModelAndView modelList() {
        ModelAndView mav = new ModelAndView("workflow/model-list");
        List<Model> list = repositoryService.createModelQuery().list();
        return mav;
    }

    /**
     * 创建模型
     * @throws UnsupportedEncodingException 
     */
    @RequestMapping(value = "create")
    @ResponseBody
    public ObjectRestResponse<Object> create(@RequestBody JSONObject objs, HttpServletResponse response) throws UnsupportedEncodingException {
        log.info("SCP信息 -- 开始创建模型：{}", objs);
    	String name=objs.getString(WfProcessDataAttr.PROC_DEFINITIONNAME);
    	String key=objs.getString(WfProcessDataAttr.PROC_DEFINITIONID);  
    	String description=objs.getString(WfProcessBizDataAttr.PROC_BIZMEMO);//procBizMemo 摘要
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        Model modelData = repositoryService.newModel();

        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        description = StringUtils.defaultString(description);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelData.setMetaInfo(modelObjectNode.toString());
        modelData.setName(name);
        modelData.setKey(StringUtils.defaultString(key));

        repositoryService.saveModel(modelData);
        repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
        return ResultUtils.success(modelData);
       // response.sendRedirect("http://localhost/etep.web/modeler.html?modelId=" + modelData.getId());
    }

    /**
     * 根据Model部署流程
     * @throws IOException 
     * @throws JsonProcessingException 
     */
    @RequestMapping(value = "deploy")
    public String deploy(RedirectAttributes redirectAttributes) throws JsonProcessingException, IOException {
    	
    	String modelId="180001";
        Model modelData = repositoryService.getModel(modelId);
        ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        byte[] bpmnBytes = null;

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
        redirectAttributes.addFlashAttribute("message", "部署成功，部署ID=" + deployment.getId());
        return "redirect:/workflow/model/list";
    }

    /**
     * 导出model对象为指定类型
     *
     * @param modelId 模型ID
     * @param type    导出文件类型(bpmn\json)
     */
    @RequestMapping(value = "export")
    public void export(
                       HttpServletResponse response) {
    	String modelId="180001";
    	String type="bpmn";
        try {
            Model modelData = repositoryService.getModel(modelId);
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            byte[] modelEditorSource = repositoryService.getModelEditorSource(modelData.getId());

            JsonNode editorNode = new ObjectMapper().readTree(modelEditorSource);
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);

            // 处理异常
            if (bpmnModel.getMainProcess() == null) {
                response.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
                response.getOutputStream().println("no main process, can't export for type: " + type);
                response.flushBuffer();
                return;
            }

            String filename = "";
            byte[] exportBytes = null;

            String mainProcessId = bpmnModel.getMainProcess().getId();

            if (type.equals("bpmn")) {

                BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
                exportBytes = xmlConverter.convertToXML(bpmnModel);

                filename = mainProcessId + ".bpmn20.xml";
            } else if (type.equals("json")) {

                exportBytes = modelEditorSource;
                filename = mainProcessId + ".json";

            }

            ByteArrayInputStream in = new ByteArrayInputStream(exportBytes);
            IOUtils.copy(in, response.getOutputStream());

            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.flushBuffer();
        } catch (Exception e) {
            log.error("导出model的xml文件失败：modelId={}, type={}", modelId, type, e);
        }
    }

    @RequestMapping(value = "delete/{modelId}")
    public String delete(@PathVariable("modelId") String modelId) {
        repositoryService.deleteModel(modelId);
        return "redirect:/workflow/model/list";
    }

    
    @RequestMapping(value = "/resource/read")
    public void loadByDeployment(HttpServletResponse response) throws Exception {
    	String processDefinitionId="process:5:132504";
    	String resourceType="xml";
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
        String resourceName = "";
        if (resourceType.equals("image")) {
            resourceName = processDefinition.getDiagramResourceName();
        } else if (resourceType.equals("xml")) {
            resourceName = processDefinition.getResourceName();
        }
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
        byte[] b = new byte[1024];
        int len = -1;
        while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
            response.getOutputStream().write(b, 0, len);
        }
    }
}
