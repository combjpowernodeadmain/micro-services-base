package com.bjzhianjia.scp.security.wf.auth.service;

public interface IWfProcTokenService {
	public boolean isMatched(String tenantId, String tokenUser, String tokenPass);
}
