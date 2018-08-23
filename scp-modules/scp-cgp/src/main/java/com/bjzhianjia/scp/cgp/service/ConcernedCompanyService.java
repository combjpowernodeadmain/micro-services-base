package com.bjzhianjia.scp.cgp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjzhianjia.scp.cgp.biz.ConcernedCompanyBiz;
import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.Result;

/**
 * 	当事人（企业）逻辑层
 * @author chenshuai
 */
@Service
@Transactional
public class ConcernedCompanyService {

	@Autowired
	private ConcernedCompanyBiz concernedCompanyBiz;
	
	
	/**
	 * 添加单个对象
	 * @author chenshuai
	 * @param concernedPerson
	 * @return
	 */
	public Result<Void> created(ConcernedCompany concernedCompany){
		Result<Void> result=new Result<>();
		
		concernedCompanyBiz.insertSelective(concernedCompany);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	/**
	 * 通过当事人id查询
	 * @param id
	 * @return
	 */
	public ConcernedCompany selectById(Integer id) {
		return concernedCompanyBiz.selectById(id);
	}
}
