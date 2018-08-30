package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.WritsInstancesBiz;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;

/**
 * 执法案件审批处理类
 * @author 尚
 */
@Service
public class CleCaseRegistrationCallBackService implements IWfProcTaskCallBackService {
	@Autowired
	private WritsInstancesBiz writsInstanceBiz;

	@Override
	public void before(String dealType, Map<String, Object> procBizData) throws BizException {
		JSONObject bizData = JSONObject.parseObject(String.valueOf(procBizData.get("bizData")));
		
		writsInstanceBiz.updateOrInsert(bizData);
	}

	@Override
	public void after(String dealType, Map<String, Object> procBizData) throws BizException {
	}

}
