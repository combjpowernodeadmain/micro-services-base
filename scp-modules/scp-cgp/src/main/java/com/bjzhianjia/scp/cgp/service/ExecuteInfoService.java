package com.bjzhianjia.scp.cgp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjzhianjia.scp.cgp.biz.ExecuteInfoBiz;
import com.bjzhianjia.scp.cgp.entity.ExecuteInfo;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

/**
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class ExecuteInfoService {
	
	@Autowired
	private ExecuteInfoBiz executeInfoBiz;

	/**
	 * 添加单个对象
	 * @author 尚
	 * @param executeInfo
	 * @return
	 */
	public Result<Void> createdExecuteInfo(ExecuteInfo executeInfo){
		Result<Void> result=new Result<>();
		result.setIsSuccess(false);
		
		executeInfoBiz.insertSelective(executeInfo);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	/**
	 * 分布查询对象
	 * @author 尚
	 * @param executeInfo
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<ExecuteInfo> getList(ExecuteInfo executeInfo,int page,int limit){
		return executeInfoBiz.getList(executeInfo, page, limit);
	}
}
