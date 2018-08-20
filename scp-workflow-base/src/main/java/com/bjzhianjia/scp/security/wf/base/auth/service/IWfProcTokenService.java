package com.bjzhianjia.scp.security.wf.base.auth.service;

public interface IWfProcTokenService {
	public boolean isMatched(String tenantId, String tokenUser, String tokenPass);
}
