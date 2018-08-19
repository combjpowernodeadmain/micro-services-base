package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.bjzhianjia.scp.cgp.entity.ExecuteInfo;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.exception.BizException;
import com.bjzhianjia.scp.cgp.service.ExecuteInfoService;

/**
 * 部门处理中
 * @author 尚
 */
@Service
@Transactional
public class ExecuteProcessingServiceImpl {
	@Autowired
	private ExecuteInfoService executeInfoService;
	
	public void before(String dealType, Map<String, Object> procBizData) throws BizException {
		
	}
	
	/**
	 * 插入处理某一案件的信息
	 * @author 尚
	 * @param dealType
	 * @param procBizData
	 * @throws BizException
	 */
	public void after(String dealType, Map<String, Object> procBizData) throws BizException {
		boolean isInsertConcernPerson=(boolean) procBizData.get("isInsertConcernPerson");//是否涉及当事人
		boolean isInsertExecuteInfo=(boolean) procBizData.get("isInsertExecuteInfo");//是否添加处理案件的那个部门的信息
		
		if(isInsertConcernPerson) {
			
		}
		
		if(isInsertExecuteInfo) {
			insertExecuteInfo(procBizData);
		}
	}

	private void insertExecuteInfo(Map<String, Object> procBizData) throws BizException {
		ExecuteInfo executeInfo = JSON.parseObject(JSON.toJSONString(procBizData), ExecuteInfo.class);
		
		//正在处理的案件ID
		executeInfo.setCaseId((Integer)procBizData.get("procBizId"));
		
		Result<Void> result = executeInfoService.createdExecuteInfo(executeInfo);
		if(!result.getIsSuccess()) {
			throw new BizException(result.getMessage());
		}
	}
}
