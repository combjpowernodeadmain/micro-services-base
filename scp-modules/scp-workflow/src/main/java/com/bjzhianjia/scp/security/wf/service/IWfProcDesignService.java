/*
 * 软件著作权：bjzhianjia
 * 项目名称：
 *
 * NAME : IFlowDesign.java
 *
 * PURPOSE : 
 *
 * AUTHOR : zhaomingli
 *
 *
 * 创建日期: 2016年8月12日
 * HISTORY：
 * 变更日期 
 */
package com.bjzhianjia.scp.security.wf.service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.entity.WfProcActReModelBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcActReProcdefBean;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.exception.WorkflowSqlException;
import com.github.pagehelper.PageInfo;

/**
 * 功能：流程设计器接口
 * 
 * @date 2016-08-12
 * @author zhaomingli
 */
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
