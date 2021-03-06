package com.bjzhianjia.scp.security.wf.base.monitor.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfMyProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;

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
     * 个人完成量统计
     * @param objs
     * @return
     */
    List<Map<String,Object>> selectUserStatisTask(JSONObject objs);
    
    
    /**
     *  查询所有待办流程任务列表
     * 排除 “已终止”、“已结案”、“事件重复”
     * @param objs
     * @return
     */
    List<WfProcBackBean> getAllToDoTasks(JSONObject objs);
    
    /**
     *  查询流程任务列表
     * @param objs
     * @return
     */
    List<WfProcBackBean> getAllTasks(JSONObject objs);
    
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
    /**
     *  通过业务ids查询流程实例id
     */
    List<Map<String,Object>> selectProcInstIdByUserId(JSONObject objs);

    /**
     * 按业务ID(taskId)查询流程
     * @param objs
     * @return
     */
    JSONObject selectProcByTaskId(JSONObject objs);

    /**
     * 根据流程类型及业务ID查询待办任务
     * 对于一特定的流程类型，满足一个业务ID只对应一个待办任务，或者是待签收，或者是待处理
     * @param objs
     * @return
     */
    JSONObject getUserTodoTaskBizId(@Param("objs") JSONObject objs);

    /**
     * 按procKey,ProcCTaskCode,procBizId查询记录
     * @param objs
     * @return
     */
    List<JSONObject> selectByProcKeyAndCTaskCodeAndBizId(@Param("objs")JSONObject objs);

    /**
     * 查询与用户关联的，待办或已办的，且正在进行的任务
     *
     * @param objs
     * @return
     */
    List<WfProcBackBean> getUserToDoOrDoneTasksOfDoing(JSONObject objs);
}
