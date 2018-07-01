package com.bjzhianjia.scp.security.wf.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import com.bjzhianjia.scp.security.wf.entity.WfMyProcBackBean;
import com.bjzhianjia.scp.security.wf.entity.WfProcBackBean;

public interface WfProcMonitorBeanMapper {
    /**
     * 查询用户待办流程任务列表
     * 
     * @param objs
     * @return
     */
    List<WfProcBackBean> getUserToDoTasks(JSONObject objs);
    
    /**
     * 查询我的已办任务列表
     * @param objs
     * @return
     */
    List<WfMyProcBackBean> selectMyProcTasks(JSONObject objs);
    
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
     * 通过机构代码查询未完成的流程列表
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
