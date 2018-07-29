package com.bjzhianjia.scp.security.wf.task.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.task.entity.WfProcTaskBean;
import com.bjzhianjia.scp.security.wf.task.entity.WfProcTaskHistoryBean;

@Repository
public interface WfProcTaskBeanMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(WfProcTaskBean record);

    int insertSelective(WfProcTaskBean record);

    WfProcTaskBean selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WfProcTaskBean record);

    int updateByPrimaryKey(WfProcTaskBean record);

    int updateByInstIdSelective(WfProcTaskBean record);

    int updateTaskByTaskid(WfProcTaskBean record);
    
    int deleteProcessTask(WfProcTaskBean record);
    
    int cancelProcessTasks(WfProcTaskBean record);
    
    int updateForUnclaim(WfProcTaskBean record);

    int updateForDelete(WfProcTaskBean record);
    
    List<WfProcTaskBean> getParallelProcTask(@Param("procInstId") String procInstId,
        @Param("taskCode") List<String> taskCode);

	WfProcTaskBean findTaskByTaskId(@Param("taskId") String taskId,
			@Param("taskStatus") List<String> taskStatus);
//			@Param("procTenantId") String procTenantId,
//			@Param("procDepartId") String procDepartId);

    List<WfProcTaskBean> getProcTasksByParent(@Param("taskId") String taskId);
    
    List<WfProcTaskBean> getProcessTasks(@Param("procInstId") String procInstId,
        @Param("taskStatus") List<String> taskStatus);

    WfProcTaskBean getFinishedTask(@Param("procInstId") String procInstId,
        @Param("taskCode") String taskCode);
    
    WfProcTaskBean getActiveTask(@Param("procInstId") String procInstId,
        @Param("taskCode") String taskCode, @Param("taskStatus") List<String> taskStatus);

    WfProcTaskBean getStartTask(@Param("procInstId") String procInstId);
    
    int getTaskCount(@Param("taskid")String taskid);
    
    int getDelegatedTaskCount(@Param("procInstId") String procInstId,
        @Param("mandatary") String mandatary);

    int getActiveTaskCount(@Param("procInstId")String procInstId);
    
    int endProcessTasks(WfProcTaskBean wfProcTaskBean);
    
    /**
     * 查询流程审批历史
     * @param objs
     * @return
     */
    List<WfProcTaskHistoryBean> selectApprovedHistory(JSONObject objs);
}