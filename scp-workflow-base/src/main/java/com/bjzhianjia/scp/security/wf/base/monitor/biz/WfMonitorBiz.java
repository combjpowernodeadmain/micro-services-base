/*
 * @(#) WfMonitorBiz.java  1.0  August 29, 2016
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
package com.bjzhianjia.scp.security.wf.base.monitor.biz;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.base.biz.WfBaseBiz;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfMyProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.mapper.WfProcMonitorBeanMapper;
import com.github.pagehelper.PageHelper;

/**
 * Description: 工作流监控服务业务实现类
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.29    mayongming       1.0        1.0 Version
 * </pre>
 */
@Component
public class WfMonitorBiz extends WfBaseBiz {
    private static Logger logger = LoggerFactory.getLogger(WfMonitorBiz.class);
    @Autowired
    WfProcMonitorBeanMapper iqbWfMyProcBeanMapper;
    
    /**
     * 流程汇总查询
     * @param objs
     * @return
     */
    public List<WfMyProcBackBean> geyProcessSummary(JSONObject objs) throws WorkflowException {
        logger.info("流程汇总查询--开始查询流程汇总信息...");

        try {
            // setDb(0, super.SLAVE);  
            PageHelper.startPage(getPagePara(objs));
            return iqbWfMyProcBeanMapper.selectProcessSummary(objs);
        } finally {
            logger.info("流程汇总查询--查询流程汇总信息完成.");
        }
    }
    
    /**
     * 查询用户已办流程任务列表
     * @param objs
     * @return
     */
    public List<WfMyProcBackBean> getUserDoneTasks(JSONObject objs) throws WorkflowException {
        logger.info("用户已办任务查询--开始查询用户已办任务...");

        try {
            // setDb(0, super.SLAVE);  
            PageHelper.startPage(getPagePara(objs));
            return iqbWfMyProcBeanMapper.selectMyProcTasks(objs);
        } finally {
            logger.info("用户已办任务查询--查询用户已办任务完成.");
        }
    }
    
    /**
     * 查询用户已办流程任务数量
     * @param objs
     * @return
     */
    public int getUserDoneTaskCount(JSONObject objs) throws WorkflowException {
        logger.info("用户已办任务查询--开始查询用户已办任务数量...");

        try {
            // setDb(0, super.SLAVE);  
            return iqbWfMyProcBeanMapper.getMyProcTaskCount(objs);
        } finally {
            logger.info("用户已办任务查询--查询用户已办任务数量完成.");
        }
    }
    
    
    /**
     * 查询用户待办流程任务列表
     * @param objs
     * @return
     */
    public List<WfProcBackBean> getUserToDoTasks(JSONObject objs) throws WorkflowException {
        logger.info("用户待办任务查询--开始查询用户待办任务...");

        try {
            // setDb(0, super.SLAVE); 
            PageHelper.startPage(getPagePara(objs));
            return iqbWfMyProcBeanMapper.getUserToDoTasks(objs);
        } finally {
            logger.info("用户待办任务查询--查询用户待办任务完成.");
        }
    }
    
    /**
     * 用户统计
     * @param objs
     * @return
     */
    public List<Map<String,Object>> getUserStatisTask(JSONObject objs) throws WorkflowException {
        logger.info("用户统计查询--开始查询用户统计...");

        try {
            return iqbWfMyProcBeanMapper.selectUserStatisTask(objs);
        } finally {
            logger.info("用户统计查询--查询用户统计查询完成.");
        }
    }
    
    /**
     * 查询用户所有待办流程任务列表
     * @param objs
     * @return
     */
    public List<WfProcBackBean> getUserAllToDoTasks(JSONObject objs) throws WorkflowException {
        logger.info("用户待办任务查询--开始查询用户待办任务...");

        try {
            return iqbWfMyProcBeanMapper.getUserToDoTasks(objs);
        } finally {
            logger.info("用户待办任务查询--查询用户待办任务完成.");
        }
    }
    /**
     * 查询所有待办流程任务列表
     * @param objs
     * @return
     */
    public List<WfProcBackBean> getAllToDoTasks(JSONObject objs) throws WorkflowException {
        logger.info("用户待办任务查询--开始查询用户待办任务...");

        try {
            return iqbWfMyProcBeanMapper.getAllToDoTasks(objs);
        } finally {
            logger.info("用户待办任务查询--查询用户待办任务完成.");
        }
    }
    /**
     * 查询流程任务列表
     * @param objs
     * @return
     */
    public List<WfProcBackBean> getAllTasks(JSONObject objs) throws WorkflowException {
        logger.info("任务列表查询--开始查询任务列表...");

        try {
            return iqbWfMyProcBeanMapper.getAllTasks(objs);
        } finally {
            logger.info("任务列表查询--查询任务列表任务完成.");
        }
    }
    
    /**
     * 查询用户待办流程任务数量
     * @param objs
     * @return
     */
    public int getUserToDoTaskCount(JSONObject objs) throws WorkflowException {
        logger.info("用户待办任务查询--开始查询用户待办任务数量...");

        try {
            // setDb(0, super.SLAVE); 
            return iqbWfMyProcBeanMapper.getUserToDoTaskCount(objs);
        } finally {
            logger.info("用户待办任务查询--查询用户待办任务数量完成.");
        }
    }
    
    /**
     * 流程监控任务查询
     * @param objs
     * @return
     */
    public List<WfMyProcBackBean> getActiveProcessList(JSONObject objs) throws WorkflowException {
        logger.info("流程监控任务查询--开始查询流程监控任务列表...");

        try {
            // setDb(0, super.SLAVE); 
            PageHelper.startPage(getPagePara(objs));
            return iqbWfMyProcBeanMapper.selectActiveProcessList(objs);
        } finally {
            logger.info("流程监控任务查询--查询流程监控任务列表完成.");
        }
    }
    
    /**
     * 流程监控任务查询
     * @param objs
     * @return
     */
    public List<WfMyProcBackBean> getOrgProcessList(JSONObject objs) throws WorkflowException {
        logger.info("流程异常处理--开始按照授权机构查询未结束的流程任务列表...");

        try {
            // setDb(0, super.SLAVE); 
            PageHelper.startPage(getPagePara(objs));
            return iqbWfMyProcBeanMapper.selectOrgProcessList(objs);
        } finally {
            logger.info("流程异常处理--按照机构查询未结束的流程任务列表完成.");
        }
    }
    
    /**
     * 流程委托查询
     * @param objs
     * @return
     */
    public List<WfMyProcBackBean> getProcessDelegateList(JSONObject objs) throws WorkflowException {
        logger.info("流程委托查询--开始查询流程委托记录...");

        try {
            // setDb(0, super.SLAVE); 
            PageHelper.startPage(getPagePara(objs));
            return iqbWfMyProcBeanMapper.selectProcessDelegate(objs);
        } finally {
            logger.info("流程委托查询--查询流程委托记录完成.");
        }
    }
    
    /**
     * 查询我的流程委托记录
     * @param objs
     * @return
     */
    public List<WfMyProcBackBean> getMyProcDelegateList(JSONObject objs) throws WorkflowException {
        logger.info("流程委托查询--开始查询我的流程委托记录...");

        try {
            // setDb(0, super.SLAVE); 
            PageHelper.startPage(getPagePara(objs));
            return iqbWfMyProcBeanMapper.selectMyProcessDelegate(objs);
        } finally {
            logger.info("流程委托查询--查询我的流程委托记录完成.");
        }
    }
    /**
     * 导出流程汇总查询
     * @param objs
     * @return
     */
    public List<WfMyProcBackBean> getProcessSummary(JSONObject objs) throws WorkflowException {
        logger.info("流程汇总查询--开始查询流程汇总信息...");
        try {
            return iqbWfMyProcBeanMapper.selectProcessSummary(objs);
        } finally {
            logger.info("流程汇总查询--查询流程汇总信息完成.");
        }
    }
    /**
     * 通过业务ids查询流程实例id
     * @param objs
     * @return
     */
    public List<Map<String,Object>> getProcInstIdByUserId(JSONObject objs) throws WorkflowException {
        logger.info("流程查询--开始查询流程实例ids...");
        try {
            return iqbWfMyProcBeanMapper.selectProcInstIdByUserId(objs);
        } finally {
            logger.info("流程查询--查询流程实例ids完成.");
        }
    }

    /**
     * 通过业务ids查询流程实例id
     * @param objs
     * @return
     */
    public JSONObject selectProcByTaskId(JSONObject objs) throws WorkflowException {
        logger.info("流程查询--开始查询流程实例ids...");
        try {
            return iqbWfMyProcBeanMapper.selectProcByTaskId(objs);
        } finally {
            logger.info("流程查询--查询流程实例ids完成.");
        }
    }

    /**
     * 根据流程类型及业务ID查询待办任务
     * 对于一特定的流程类型，满足一个业务ID只对应一个待办任务，或者是待签收，或者是待处理
     * @param objs
     * @return
     * @throws WorkflowException
     */
    public JSONObject getUserTodoTaskBizId(JSONObject objs) throws WorkflowException {
        logger.info("流程查询--开始查询流程实例...");
        try {
            return iqbWfMyProcBeanMapper.getUserTodoTaskBizId(objs);
        } finally {
            logger.info("流程查询--查询流程实例ids完成.");
        }
    }

    /**
     * 按procKey,ProcCTaskCode,procBizId查询记录
     * @param objs
     * @return
     */
    public List<JSONObject> selectByProcKeyAndCTaskCodeAndBizId(JSONObject objs) throws WorkflowException{
        logger.info("流程查询--开始查询流程实例...");
        try {
            return iqbWfMyProcBeanMapper.selectByProcKeyAndCTaskCodeAndBizId(objs);
        } finally {
            logger.info("流程查询--查询流程实例完成.");
        }
    }

    /**
     * 查询与用户关联的，待办或已办的，且正在进行的任务
     * @param objs
     * @return
     */
    public List<WfProcBackBean> getUserToDoOrDoneTasksOfDoing(JSONObject objs) throws WorkflowException {
        logger.info("用户待办任务查询--开始查询用户待办任务...");

        try {
            return iqbWfMyProcBeanMapper.getUserToDoOrDoneTasksOfDoing(objs);
        } finally {
            logger.info("用户待办任务查询--查询用户待办任务完成.");
        }
    }
}
