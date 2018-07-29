package com.bjzhianjia.scp.security.wf.design.service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.design.entity.WfProcActReModelBean;
import com.bjzhianjia.scp.security.wf.design.entity.WfProcActReProcdefBean;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.exception.WorkflowSqlException;
import com.github.pagehelper.PageInfo;

public interface IWfProcDesignService{

    // 流程发布或部署
    public void deploymentProcess(JSONObject objs);
    public void deploymentModel(JSONObject objs);
    public void modeldelete(JSONObject objs);
    public void wfdel(JSONObject objs);
    public void wfsuspend(JSONObject objs);
    public void wfactive(JSONObject objs);
    public void export(JSONObject objs);
    PageInfo<WfProcActReModelBean> selectModelList(JSONObject objs) throws WorkflowException, WorkflowSqlException;
    PageInfo<WfProcActReProcdefBean> processList(JSONObject objs) throws WorkflowException, WorkflowSqlException;
}
