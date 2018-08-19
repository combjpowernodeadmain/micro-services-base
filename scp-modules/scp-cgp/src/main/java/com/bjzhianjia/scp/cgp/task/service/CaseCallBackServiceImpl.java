package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.exception.BizException;
import com.bjzhianjia.scp.cgp.service.CaseInfoService;

/**
 * 立案单处理
 * 
 * @author 尚
 */
@Service
@Transactional
public class CaseCallBackServiceImpl {
	@Autowired
	private CaseInfoService caseInfoService;

	public void before(String dealType, Map<String, Object> procBizData) throws BizException {
		
	}

	private void check(Map<String, Object> procBizData, CaseInfo caseInfo) throws BizException {
		Integer caseInfoId1 = caseInfo.getId();
		String caseInfoId2 = String.valueOf(procBizData.get("procBizId"));

		if (caseInfoId1==null && !StringUtils.isBlank(caseInfoId2)) {
			caseInfo.setId(Integer.valueOf(caseInfoId2));
		}
	}

	public void after(String dealType, Map<String, Object> procBizData) throws BizException {
		CaseInfo caseInfo = JSON.parseObject(JSON.toJSONString(procBizData), CaseInfo.class);

		//对procBizDate的procBizId及caseInfo.getId进行验证，避免前端未在caseInfo中添加ID
		check(procBizData, caseInfo);

		Result<Void> result = caseInfoService.update(caseInfo);
		if(!result.getIsSuccess()) {
			throw new BizException(result.getMessage());
		}
	}
}
