package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.WritsInstancesBiz;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;

import lombok.extern.slf4j.Slf4j;

/**
 * 执法案件审批处理类
 * 
 * @author 尚
 */
@Service
@Slf4j
public class CleCaseRegistrationCallBackService implements IWfProcTaskCallBackService {
	@Autowired
	private WritsInstancesBiz writsInstanceBiz;

	@Override
	public void before(String dealType, Map<String, Object> procBizData) throws BizException {
		String bizType = String.valueOf(procBizData.get(PROC_BIZTYPE));
		log.debug("*********************************enter into call_back program*************************************");
		log.debug("*********************************bizType:" + bizType + "*************************************");

		switch (bizType) {
		case PROC_APPROVE:
			// 审批操作
			writsInstanceBiz.updateOrInsert(JSONObject.parseObject(JSON.toJSONString(procBizData)));
			break;
		case PROC_END:
			// 流程走向结束
		case "termination":
			// 流程走向中止(非正常中止流程)
			break;
		default:
			break;
		}

	}

	@Override
	public void after(String dealType, Map<String, Object> procBizData) throws BizException {
	}

}
