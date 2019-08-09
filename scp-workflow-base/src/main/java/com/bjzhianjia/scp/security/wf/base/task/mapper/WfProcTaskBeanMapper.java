package com.bjzhianjia.scp.security.wf.base.task.mapper;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskBean;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskHistoryBean;

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

	List<WfProcTaskBean> getParallelProcTask(
			@Param("procInstId") String procInstId,
			@Param("taskCode") List<String> taskCode,
			@Param("procTenantId") String procTenantId);

	WfProcTaskBean findTaskByTaskId(@Param("taskId") String taskId,
			@Param("taskStatus") List<String> taskStatus,
			@Param("procTenantId") String procTenantId);

	List<WfProcTaskBean> getProcTasksByParent(@Param("taskId") String taskId,
			@Param("procTenantId") String procTenantId);

	List<WfProcTaskBean> getProcessTasks(
			@Param("procInstId") String procInstId,
			@Param("taskStatus") List<String> taskStatus,
			@Param("procTenantId") String procTenantId);

	WfProcTaskBean getFinishedTask(@Param("procInstId") String procInstId,
			@Param("taskCode") String taskCode,
			@Param("procTenantId") String procTenantId);
            
    WfProcTaskBean getActiveTask(@Param("procInstId") String procInstId,
        @Param("taskCode") String taskCode, @Param("taskStatus") List<String> taskStatus);


	WfProcTaskBean getStartTask(@Param("procInstId") String procInstId,
			@Param("procTenantId") String procTenantId);

	int getTaskCount(@Param("taskid") String taskid,
			@Param("procTenantId") String procTenantId);

	int getDelegatedTaskCount(@Param("procInstId") String procInstId,
			@Param("mandatary") String mandatary,
			@Param("procTenantId") String procTenantId);

	int getActiveTaskCount(@Param("procInstId") String procInstId,
			@Param("procTenantId") String procTenantId);

	int endProcessTasks(WfProcTaskBean wfProcTaskBean);

	/**
	 * 查询流程审批历史
	 * 
	 * @param objs
	 * @return
	 */
	List<WfProcTaskHistoryBean> selectApprovedHistory(@Param("procInstId") String procInstId,
			@Param("procTenantId") String procTenantId);

	List<JSONObject> swpByuserIds(@Param("startTime")Date startTime,
								  @Param("endTime")Date endTime,
								  @Param("userId")String userId);
}