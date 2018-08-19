package com.bjzhianjia.scp.security.wf.task.service.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.task.service.IWfProcTaskCallBackService;

@Service
public class CallBackTestServiceImpl implements IWfProcTaskCallBackService {

	@Override
	public void before(String dealType, Map<String, Object> procBizData) throws WorkflowException {
		JSONObject parseObject = JSONObject.parseObject(JSON.toJSONString(procBizData));
		System.out.println(parseObject);
	}

	@Override
	public void after(String dealType, Map<String, Object> procBizData) throws WorkflowException {
	}

}
