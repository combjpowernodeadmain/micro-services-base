package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;

/**
 *处理执法立案逻辑的回调类
 * @author 尚
 */
@Service
public class ClePreCaseCallBackServiceImpl implements IWfProcTaskCallBackService {
	@Autowired
	private CaseRegistrationBiz caseResistrationBiz;

	@Override
	public void before(String dealType, Map<String, Object> procBizData) throws BizException {
		JSONObject bizData = JSONObject.parseObject(String.valueOf(procBizData.get("bizData")));
		
		//添加立案
		caseResistrationBiz.addCase(bizData);
		procBizData.put("procBizId", bizData.getString("procBizId"));
	}
	@Override
	public void after(String dealType, Map<String, Object> procBizData) throws BizException {
	}

}
