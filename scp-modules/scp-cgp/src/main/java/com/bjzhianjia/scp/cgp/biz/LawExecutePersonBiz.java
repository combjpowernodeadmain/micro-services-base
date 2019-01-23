package com.bjzhianjia.scp.cgp.biz;

import com.bjzhianjia.scp.cgp.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.LawExecutePerson;
import com.bjzhianjia.scp.cgp.mapper.LawExecutePersonMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author chenshuai
 * @email 
 * @version 2018-08-30 13:52:18
 */
@Service
public class LawExecutePersonBiz extends BusinessBiz<LawExecutePersonMapper,LawExecutePerson> {
	
	@Autowired
	private LawExecutePersonMapper lawExecutePersonMapper;
	
	/**
	 * 通过执法任务id删除记录表
	 * @param lawTaskId
	 * @return
	 * 		影响行数
	 */
	public int delByLawTaskId(Integer lawTaskId) {
		Example example = new Example(LawExecutePerson.class);
		Criteria criteria = example.createCriteria();
		if(lawTaskId != null) {
			criteria.andEqualTo("lawTaskId",lawTaskId);
			return lawExecutePersonMapper.deleteByExample(example);
		}
		return 0;
	}

	/**
	 * 按执法任务ID查询执法任务人员记录
	 * @param lawTaskId
	 * @return
	 */
	public List<LawExecutePerson> getByLawTaskId(String lawTaskId){
		Example example=new Example(LawExecutePerson.class);
		example.createCriteria().andEqualTo("lawTaskId", lawTaskId);
		List<LawExecutePerson> lawExecutePeople = lawExecutePersonMapper.selectByExample(example);
		if(BeanUtil.isEmpty(lawExecutePeople)){
			return new ArrayList<>();
		}

		return lawExecutePeople;
	}
}