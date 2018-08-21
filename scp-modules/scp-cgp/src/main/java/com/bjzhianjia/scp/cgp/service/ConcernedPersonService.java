package com.bjzhianjia.scp.cgp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjzhianjia.scp.cgp.biz.ConcernedPersonBiz;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

/**
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class ConcernedPersonService {
	
	@Autowired
	private ConcernedPersonBiz concernedPersonBiz;
	
	/**
	 * 添加单个对象
	 * @author 尚
	 * @param concernedPerson
	 * @return
	 */
	public Result<Void> created(ConcernedPerson concernedPerson){
		Result<Void> result=new Result<>();
		
		concernedPersonBiz.insertSelective(concernedPerson);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	/**
	 * 分页获取对象
	 * @author 尚
	 * @param concernedPerson
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<ConcernedPerson> getList(ConcernedPerson concernedPerson,int page,int limit){
		TableResultResponse<ConcernedPerson> restResult = concernedPersonBiz.getList(concernedPerson, page, limit);
		return restResult;
	}
	
	/**
	 * 获取单个对象
	 * @author 尚
	 * @param id
	 * @return
	 */
	public ObjectRestResponse<ConcernedPerson> getOne(Integer id){
		ConcernedPerson concernedPerson = concernedPersonBiz.selectById(id);
		return new ObjectRestResponse<ConcernedPerson>().data(concernedPerson);
	}
	
	/**
	 * 添加多个对象
	 * @author 尚
	 * @param concernedPersonList 待添加对象集合
	 * @return
	 */
	public Result<Void> created(List<ConcernedPerson> concernedPersonList){
		Result<Void> result=new Result<>();
		result.setIsSuccess(false);
		
		concernedPersonBiz.insertConcernedPersonList(concernedPersonList);
		
		result.setIsSuccess(true);
		return result;
		
	}
}
