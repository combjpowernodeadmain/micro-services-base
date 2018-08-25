package com.bjzhianjia.scp.security.wf.base.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.wf.base.auth.biz.WfProcTokenBiz;
import com.bjzhianjia.scp.security.wf.base.auth.service.IWfProcTokenService;

@Service("wfrocTokenService")
public class WfProcTokenServiceImpl implements IWfProcTokenService {
	@Autowired
	WfProcTokenBiz procTokenBiz;
	
	public boolean isMatched(String tenantId, String tokenUser, String tokenPass) {
		return procTokenBiz.isMatched(tenantId, tokenUser, tokenPass);
	}
}
