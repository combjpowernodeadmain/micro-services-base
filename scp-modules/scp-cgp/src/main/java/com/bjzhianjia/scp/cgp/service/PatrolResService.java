package com.bjzhianjia.scp.cgp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjzhianjia.scp.cgp.biz.PatrolResBiz;
import com.bjzhianjia.scp.cgp.entity.PatrolRes;
import com.bjzhianjia.scp.cgp.entity.Result;


@Service
@Transactional
public class PatrolResService {

	@Autowired
	private PatrolResBiz patrolResBiz;
	
	/**
	 * 添加单个对象
	 * @author chenshuai
	 * @param concernedPerson
	 * @return
	 */
	public Result<Void> created(PatrolRes patrolRes){
		Result<Void> result=new Result<>();
		
		patrolResBiz.insertSelective(patrolRes);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	
}
