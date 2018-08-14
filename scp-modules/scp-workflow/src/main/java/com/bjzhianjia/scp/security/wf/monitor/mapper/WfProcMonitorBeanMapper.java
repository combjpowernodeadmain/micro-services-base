package com.bjzhianjia.scp.security.wf.monitor.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.monitor.entity.WfMyProcBackBean;
import com.bjzhianjia.scp.security.wf.monitor.entity.WfProcBackBean;

@Repository
public interface WfProcMonitorBeanMapper {
    /**
     * 查询用户待办流程任务列表
     * 
     * @param objs
     * @return
     */
    List<WfProcBackBean> getUserToDoTasks(JSONObject objs);
    
    /**
     * 查询用户待办流程任务数量
     * 
     * @param objs
     * @return
     */
    int getUserToDoTaskCount(JSONObject objs);
    
    /**
     * 查询我的已办任务列表
     * @param objs
     * @return
     */
    List<WfMyProcBackBean> selectMyProcTasks(JSONObject objs);
    
    /**
     * 查询我的已办任务数量
     * @param objs
     * @return
     */
    int getMyProcTaskCount(JSONObject objs);
    
    /**
     * 流程实例汇总查询
     * @param objs
     * @return
     */
    List<WfMyProcBackBean> selectProcessSummary(JSONObject objs);
    
    /**
     * 查询未完成的流程列表
     * @param objs
     * @return
     */
    List<WfMyProcBackBean> selectActiveProcessList(JSONObject objs);
    
    /**
     * 通过授权机构代码查询未完成的流程列表
     * @param objs
     * @return
     */
    List<WfMyProcBackBean> selectOrgProcessList(JSONObject objs);
    
    /**
     * 流程委托记录查询
     * @param objs
     * @return
     */
    List<WfMyProcBackBean> selectProcessDelegate(JSONObject objs);
    
    /**
     * 流程委托记录查询
     * @param objs
     * @return
     */
    List<WfMyProcBackBean> selectMyProcessDelegate(JSONObject objs);
}
