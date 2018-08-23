package com.bjzhianjia.scp.cgp.biz;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PatrolRes;
import com.bjzhianjia.scp.cgp.mapper.PatrolResMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 巡查任务资料关系表
 *
 * @author bo
 */
@Service
public class PatrolResBiz extends BusinessBiz<PatrolResMapper,PatrolRes> {
	
	@Autowired
	private PatrolResMapper patrolResMapper;
	
	/**
	 * 通过巡查任务记录id获取资源集
	 * @param patrolTaskId
	 * 		巡查任务记录id
	 * @return
	 * 		图片地址集
	 */
	public List<PatrolRes> getByPatrolTaskId(Integer patrolTaskId){
		Example example = new Example(PatrolRes.class);
		Criteria criteria = example.createCriteria();
		criteria.andEqualTo("patrolTaskId", patrolTaskId);
		return patrolResMapper.selectByExample(example);
	}
}