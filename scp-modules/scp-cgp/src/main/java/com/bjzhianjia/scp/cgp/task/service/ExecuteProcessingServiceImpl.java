package com.bjzhianjia.scp.cgp.task.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.ExecuteInfo;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.exception.BizException;
import com.bjzhianjia.scp.cgp.service.ConcernedPersonService;
import com.bjzhianjia.scp.cgp.service.ExecuteInfoService;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;

/**
 * 部门处理中
 * @author 尚
 */
@Service
@Transactional
public class ExecuteProcessingServiceImpl implements IWfProcTaskCallBackService{
	@Autowired
	private ExecuteInfoService executeInfoService;
	@Autowired
	private ConcernedPersonService concernedPersonService;
	
	@Override
	public void before(String dealType, Map<String, Object> procBizData) throws BizException {
		
	}
	
	/**
	 * 插入处理某一案件的信息
	 * @author 尚
	 * @param dealType
	 * @param procBizData
	 * @throws BizException
	 */
	@Override
	public void after(String dealType, Map<String, Object> procBizData) throws BizException {
//		String isInsertConcernPerson=String.valueOf(procBizData.get("isInsertConcernPerson"));//是否涉及当事人
//		String isInsertExecuteInfo=String.valueOf(procBizData.get("isInsertExecuteInfo"));//是否添加处理案件的那个部门的信息
//		
//		if("true".equals(isInsertConcernPerson)) {
//			insertConcernPersons(procBizData);
//		}
//		
//		if("true".equals(isInsertExecuteInfo)) {
//			insertExecuteInfo(procBizData);
//		}
	}

	private void insertConcernPersons(Map<String, Object> procBizData) {
		/*
		 * 当事人可能涉及到多个，前端将其封装为jsonArray传入
		 * 格式如[{当事人1},{当事人2},{当事人3}]
		 */
		String concernPersonsJArray=(String) procBizData.get("concernedPersons");
		List<ConcernedPerson> concernedPersonList = JSONArray.parseArray(concernPersonsJArray, ConcernedPerson.class);
		
		for (ConcernedPerson concernedPerson : concernedPersonList) {
			//为每个当事人指定案件ID
			concernedPerson.setCaseId((Integer)procBizData.get("procBizId"));
		}
		
		Result<Void> result = concernedPersonService.created(concernedPersonList);
		if(!result.getIsSuccess()) {
			throw new BizException(result.getMessage());
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
