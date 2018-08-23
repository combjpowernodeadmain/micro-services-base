package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 巡查任务记录表
 * 
 * @author bo
 */
public interface PatrolTaskMapper extends CommonMapper<PatrolTask> {
	
	/**
	 *  巡查任务记录翻页查询
	 * @param patrolTask
	 * 		巡查记录信息
	 * @param startTime
	 * 		 开始时间
	 * @param patrolName
	 * 		结束时间
	 * @return
	 * 		返回集
	 */
	public List<Map<String,Object>> selectPatrolTaskList(@Param("patrolTask")PatrolTask patrolTask,
			@Param("speName")String speName,
			@Param("startTime")Date startTime,
			@Param("endTime")Date endTime);
}