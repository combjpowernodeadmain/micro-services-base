package com.bjzhianjia.scp.cgp.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.LawPatrolObject;
import com.bjzhianjia.scp.cgp.mapper.LawPatrolObjectMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 *
 * @author chenshuai
 * @email 
 * @version 2018-08-30 13:52:18
 */
@Service
public class LawPatrolObjectBiz extends BusinessBiz<LawPatrolObjectMapper,LawPatrolObject> {
	
	@Autowired
	private LawPatrolObjectMapper lawPatrolObjectMapper;
	
	/**
	 * 通过执法任务id删除记录表
	 * @param lawTaskId
	 * @return
	 * 		影响行数
	 */
	public int delByLawTaskId(Integer lawTaskId) {
		Example example = new Example(LawPatrolObject.class);
		Criteria criteria = example.createCriteria();
		if(lawTaskId != null) {
			criteria.andEqualTo("lawTaskId",lawTaskId);
			return lawPatrolObjectMapper.deleteByExample(example);
		}
		return 0;
	}
	
}