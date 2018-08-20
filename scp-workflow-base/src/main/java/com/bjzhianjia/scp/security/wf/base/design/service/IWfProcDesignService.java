package com.bjzhianjia.scp.security.wf.base.design.service;

import java.io.InputStream;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.base.design.entity.WfProcActReModelBean;
import com.bjzhianjia.scp.security.wf.base.design.entity.WfProcActReProcdefBean;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowSqlException;
import com.github.pagehelper.PageInfo;

public interface IWfProcDesignService {
	// 流程发布或部署
	public void deploymentProcess(JSONObject objs) throws WorkflowException;

	public void deploymentModel(JSONObject objs) throws WorkflowException;

	public void modeldelete(JSONObject objs) throws WorkflowException;

	public void wfdel(JSONObject objs) throws WorkflowException;

	public void wfsuspend(JSONObject objs) throws WorkflowException;

	public void wfactive(JSONObject objs) throws WorkflowException;

	public void export(JSONObject objs) throws WorkflowException;

	PageInfo<WfProcActReModelBean> selectModelList(JSONObject objs)
			throws WorkflowException, WorkflowSqlException;

	PageInfo<WfProcActReProcdefBean> processList(JSONObject objs)
			throws WorkflowException, WorkflowSqlException;

	public InputStream openImageByProcessDefinition(String procDefId)
			throws WorkflowException;

	public InputStream openImageByProcessInstance(String executionId)
			throws WorkflowException;
}
