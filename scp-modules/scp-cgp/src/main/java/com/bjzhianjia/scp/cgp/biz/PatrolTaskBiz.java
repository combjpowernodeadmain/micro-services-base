package com.bjzhianjia.scp.cgp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.mapper.PatrolTaskMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;


/**
 * 巡查任务记录表
 *
 * @author bo
 */
@Service
public class PatrolTaskBiz extends BusinessBiz<PatrolTaskMapper, PatrolTask> {

	@Autowired
	private PatrolTaskMapper patrolTaskMapper;

	/**
	 * 根据查询条件搜索
	 * 
	 * @param patrolTask 巡查任务记录
	 * @param speName    专项任务名称
	 * @param startTime  开始时间
	 * @param endTime    结束时间
	 * @param page       页码
	 * @param limit      页容量
	 * @return
	 */
	public TableResultResponse<Map<String, Object>> selectPatrolTaskList(PatrolTask patrolTask, String speName,
			Date startTime, Date endTime, int page, int limit) {
		Page<Object> result = PageHelper.startPage(page, limit);
		List<Map<String, Object>> list = patrolTaskMapper.selectPatrolTaskList(patrolTask, speName, startTime, endTime);
		return new TableResultResponse<Map<String, Object>>(result.getTotal(), list);
	}

}