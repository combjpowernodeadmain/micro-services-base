package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
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
	
	/**
	 * 获取regulaObjIdList集合中所包含的监管对象被巡查的次数
	 * @param regulaObjIdList
	 * @return
	 */
	public List<JSONObject> regulaObjCount(@Param("regulaObjIdList") List<Integer> regulaObjIdList);

	/**
	 * 全部定位
	 * @param objs
	 * @param page
	 * @param limit
	 * @return
	 */
    List<PatrolTask> allPosition(@Param("objs") JSONObject objs, @Param("page") Integer page,
        @Param("limit") Integer limit);

	/**
	 * 按监管对象查询巡查记录
	 * @param queryData
	 * @return
	 */
	List<JSONObject> listOfRegObj(@Param("queryData")JSONObject queryData);
}
