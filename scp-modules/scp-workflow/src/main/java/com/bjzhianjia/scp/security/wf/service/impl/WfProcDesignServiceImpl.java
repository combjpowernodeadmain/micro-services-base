/*
 * 软件著作权：bjzhianjia
 * 项目名称：
 *
 * NAME : FlowDesignServiceImpl.java
 *
 * PURPOSE : 
 *
 * AUTHOR : scp
 *
 *
 * 创建日期: 2016年8月12日
 * HISTORY：
 * 变更日期 
 */
package com.bjzhianjia.scp.security.wf.service.impl;

import java.io.IOException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.biz.WfProcDesinerBiz;
import com.bjzhianjia.scp.security.wf.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.entity.WfProcActReModelBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcActReProcdefBean;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.exception.WorkflowSqlException;
import com.bjzhianjia.scp.security.wf.service.IWfProcDesignService;
import com.github.pagehelper.PageInfo;

/**
 * 功能：流程设计器接口
 * 
 * @date 2016-08-12
 * @author scp
 */
@Service("wfProcDesignService")
public class WfProcDesignServiceImpl implements IWfProcDesignService{

    @Autowired
    WfProcDesinerBiz wfProcDesinerBiz;


    /**
     * 功能：流程部署
     * 
     * @date 2016-09-01
     * @author scp
     */
    public void deploymentProcess(JSONObject objs) {
        wfProcDesinerBiz.deploymentProcess(objs);

    }
    /**
     * 功能：流程模型部署
     * 
     * @date 2016-09-01
     * @author scp
     */
    public void deploymentModel(JSONObject objs) {
        wfProcDesinerBiz.deploymentModel(objs);
        
    }


    /**
     * 功能：流程模型列表
     * 
     * @date 2016-09-13
     * @author scp
     */
    public PageInfo<WfProcActReModelBean> selectModelList(JSONObject objs) throws WorkflowException, WorkflowSqlException {
        return new PageInfo<WfProcActReModelBean>(wfProcDesinerBiz.selectModelList(objs));
    }
    
    /**
     * 功能：流程部署列表
     * 
     * @date 2016-09-13
     * @author scp
     */
    public PageInfo<WfProcActReProcdefBean> processList(JSONObject objs) throws WorkflowException, WorkflowSqlException {
        return new PageInfo<WfProcActReProcdefBean>(wfProcDesinerBiz.processList(objs));
    }
    
    /**
     * 功能：删除流程
     * 
     * @date 2016-09-13
     * @author scp
     */
    public void wfdel(JSONObject objs) {
        try {
            wfProcDesinerBiz.wfdel(objs);
        } catch(PersistenceException iqbe) {
            //存在外键
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010103, iqbe);
        }
        
    }
    /**
     * 功能：导出流程
     * 
     * @date 2016-09-13
     * @author scp
     */
    public void export(JSONObject objs) {
        try {
            wfProcDesinerBiz.export(objs);
        } catch(PersistenceException | IOException iqbe) {
            //存在外键
            throw new WorkflowException(WorkflowEnumResults.WF_DESIGN_02010104, iqbe);
        }
        
    }
    
    /**
     * 功能：挂起流程
     * 
     * @date 2016-09-20
     * @author scp
     */
    public void wfsuspend(JSONObject objs) {
            wfProcDesinerBiz.wfsuspend(objs);
    }
    /**
     * 功能：挂起流程
     * 
     * @date 2016-09-20
     * @author scp
     */
    public void wfactive(JSONObject objs) {
        wfProcDesinerBiz.wfactive(objs);
    }


    /**
     * 功能：流程模型删除
     * 
     * @date 2016-09-13
     * @author scp
     */
    @Override
    public void modeldelete(JSONObject objs) {
        wfProcDesinerBiz.modeldelete(objs);
    }

}
