package com.bjzhianjia.scp.cgp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

/**
 * 立案管理
 * @author 尚
 */
@Service
public class CaseInfoService {
	@Autowired
	private CaseInfoBiz caseInfoBiz;

	/**
	 * 更新单个对象
	 * @author 尚
	 * @param caseInfo
	 * @return
	 */
	public Result<Void> update(CaseInfo caseInfo){
		Result<Void> result=new Result<>();
		result.setIsSuccess(false);
		
		this.caseInfoBiz.updateSelectiveById(caseInfo);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	/**
	 * 按分布获取对象
	 * @author 尚
	 * @param caseInfo
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<CaseInfo> getList(CaseInfo caseInfo,int page,int limit){
		return caseInfoBiz.getList(caseInfo, page, limit);
	}
	
	/**
	 * 获取单个对象
	 * @author 尚
	 * @param id
	 * @return
	 */
	public ObjectRestResponse<CaseInfo> get(Integer id){
		CaseInfo caseInfo = this.caseInfoBiz.selectById(id);
		return new ObjectRestResponse<CaseInfo>().data(caseInfo);
	}
}
