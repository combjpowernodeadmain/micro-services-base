package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.LawTask;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 执法任务管理
 * 
 * @author chenshuai
 * @email 
 * @version 2018-08-30 13:52:18
 */
public interface LawTaskMapper extends CommonMapper<LawTask> {
	
	/**
	 * 执法任务翻页查询
	 * @param userName         执法者姓名
	 * @param regulaObjectName 巡查对象名称
	 * @param state            任务状态
	 * @param startTime        开始日期
	 * @param endTime          结束日期
	 */
	public List<Map<String,Object>> selectLawTaskList(@Param("userName") String userName,
			@Param("regulaObjectName") String regulaObjectName,
			@Param("state") String state,
			@Param("startTime") Date startTime,
			@Param("endTime") Date endTime);
	
	/**
	 *  处理中的 执法任务翻页查询
	 * @param userName         执法者姓名
	 * @param regulaObjectName 巡查对象名称
	 * @param state            任务状态
	 * @param startTime        开始日期
	 * @param endTime          结束日期
	 */
	public List<Map<String,Object>> selectByLawTasDoing(@Param("userName") String userName,
			@Param("regulaObjectName") String regulaObjectName,
			@Param("state") String state,
			@Param("startTime") Date startTime,
			@Param("endTime") Date endTime);
}
