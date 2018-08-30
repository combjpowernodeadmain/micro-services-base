package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
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
		
		String bizType = String.valueOf(procBizData.get(PROC_BIZTYPE));
		
		switch (bizType) {
		case PROC_APPROVE:
			//审批操作
			writsInstanceBiz.updateOrInsert(JSONObject.parseObject(JSON.toJSONString(procBizData)));
			break;
		case PROC_END:
			//流程走向终止
			break;
		default:
			break;
		}
		
	}

	@Override
	public void after(String dealType, Map<String, Object> procBizData) throws BizException {
	}

}
