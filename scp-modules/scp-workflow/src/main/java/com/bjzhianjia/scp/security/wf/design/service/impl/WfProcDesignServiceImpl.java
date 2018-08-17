package com.bjzhianjia.scp.security.wf.design.service.impl;

import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.auth.biz.WfProcUserAuthBiz;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcessDataAttr;
import com.bjzhianjia.scp.security.wf.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.design.biz.WfProcDesinerBiz;
import com.bjzhianjia.scp.security.wf.design.entity.WfProcActReModelBean;
import com.bjzhianjia.scp.security.wf.design.entity.WfProcActReProcdefBean;
import com.bjzhianjia.scp.security.wf.design.service.IWfProcDesignService;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.exception.WorkflowSqlException;
import com.bjzhianjia.scp.security.wf.utils.StringUtil;
import com.github.pagehelper.PageInfo;

/**
 * 功能：流程设计器接口
 * 
 * @author scp
 */
@Service("wfProcDesignService")
public class WfProcDesignServiceImpl implements IWfProcDesignService{
    @Autowired
    WfProcDesinerBiz wfProcDesinerBiz;
    @Autowired
    WfProcUserAuthBiz wfProcUserAuthBiz; 

    /**
     * 功能：流程部署
     * 
     * @author scp
     */
	public void deploymentProcess(JSONObject objs) throws WorkflowException {
		// 流程定义id
		String procDefId = objs.getString(WfProcessDataAttr.PROC_DEFINITIONID);
		// 流程定义name
		String procDefName = objs.getString(WfProcessDataAttr.PROC_DEFINITIONNAME);

		// 流程定义id不能为空
		if (StringUtil.isNull(procDefId)) {
			throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010001);
		}

		wfProcDesinerBiz.deploymentProcess(procDefId, procDefName,
				wfProcUserAuthBiz.getTenantId());
	}
	
    /**
     * 功能：流程模型部署
     * 
     * @author scp
     */
	public void deploymentModel(JSONObject objs) throws WorkflowException {
		// 流程模型id
		String modelId = objs.getString(WfProcessDataAttr.PROC_MODELID);

		// 流程模型id不能为空
		if (StringUtil.isNull(modelId)) {
			throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010101);
		}

		wfProcDesinerBiz.deploymentModel(modelId, wfProcUserAuthBiz.getTenantId());
	}


    /**
     * 功能：流程模型列表
     * 
     * @author scp
     */
	public PageInfo<WfProcActReModelBean> selectModelList(JSONObject objs)
			throws WorkflowException, WorkflowSqlException {
		objs.put(WfProcessDataAttr.PROC_TENANTID,
				wfProcUserAuthBiz.getTenantId());
		return new PageInfo<WfProcActReModelBean>(
				wfProcDesinerBiz.selectModelList(objs));
	}

    /**
     * 功能：流程部署列表
     * 
     * @author scp
     */
	public PageInfo<WfProcActReProcdefBean> processList(JSONObject objs)
			throws WorkflowException, WorkflowSqlException {
		objs.put(WfProcessDataAttr.PROC_TENANTID,
				wfProcUserAuthBiz.getTenantId());
		return new PageInfo<WfProcActReProcdefBean>(
				wfProcDesinerBiz.processList(objs));
	}
    
    /**
     * 功能：删除流程
     * 
     * @author scp
     */
    public void wfdel(JSONObject objs) throws WorkflowException {
    	String deploymentId = objs.getString(WfProcessDataAttr.PROC_DEPLOYMENTID);
    	
        if (StringUtil.isNull(deploymentId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010102);
        }
        
        wfProcDesinerBiz.wfdel(deploymentId, wfProcUserAuthBiz.getTenantId());
    }
    
    /**
     * 功能：导出流程
     * 
     * @author scp
     */
    public void export(JSONObject objs) throws WorkflowException {
    	String modelId = objs.getString(WfProcessDataAttr.PROC_MODELID);
    	
        if (StringUtil.isNull(modelId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010105);
        } 
        
        try {
        	wfProcDesinerBiz.export("bpmn", modelId, wfProcUserAuthBiz.getTenantId());
        } catch(PersistenceException | IOException e) {
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010104, e);
        }
    }
    
    /**
     * 功能：挂起流程
     * 
     * @author scp
     */
    public void wfsuspend(JSONObject objs) throws WorkflowException {
    	String procDefId = objs.getString(WfProcessDataAttr.PROC_DEFINITIONID);
    	
        if (StringUtil.isNull(procDefId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02010201);
        }
        
        wfProcDesinerBiz.wfsuspend(procDefId, wfProcUserAuthBiz.getTenantId());
    }
    
    /**
     * 功能：挂起流程
     * 
     * @author scp
     */
    public void wfactive(JSONObject objs) throws WorkflowException {
    	String procDefId = objs.getString(WfProcessDataAttr.PROC_DEFINITIONID);
    	
        if (StringUtil.isNull(procDefId)) {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02010210);
        }
        
        wfProcDesinerBiz.wfactive(procDefId, wfProcUserAuthBiz.getTenantId());
    }

    /**
     * 功能：流程模型删除
     * 
     * @author scp
     */
    @Override
    public void modeldelete(JSONObject objs) throws WorkflowException {
    	 String modelId = objs.getString(WfProcessDataAttr.PROC_MODELID);
    	 
         if (StringUtil.isNull(modelId)) {
             throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010101);
         }
         
        wfProcDesinerBiz.deleteModel(modelId, wfProcUserAuthBiz.getTenantId());
    }
    
	public InputStream openImageByProcessDefinition(String procDefId)
			throws WorkflowException {
		return wfProcDesinerBiz.openImageByProcessDefinition(procDefId,
				wfProcUserAuthBiz.getTenantId());
	}

	public InputStream openImageByProcessInstance(String executionId)
			throws WorkflowException {
		return wfProcDesinerBiz.openImageByProcessInstance(executionId,
				wfProcUserAuthBiz.getTenantId());
	}
}
