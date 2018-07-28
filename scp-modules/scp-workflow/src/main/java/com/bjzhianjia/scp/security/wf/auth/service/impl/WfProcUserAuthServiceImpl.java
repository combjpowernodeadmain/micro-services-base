package com.bjzhianjia.scp.security.wf.auth.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.wf.auth.biz.WfProcUserAuthBiz;
import com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.vo.WfProcAuthDataBean;

@Service("wfProcUserAuthService")
public class WfProcUserAuthServiceImpl implements IWfProcUserAuthService {
	@Autowired
	WfProcUserAuthBiz wfProcUserAuthBiz;
	
	@Override
	public String getUserId() {
		return wfProcUserAuthBiz.getUserId();
	}

	@Override
	public String getUserCode() {
		return wfProcUserAuthBiz.getUserCode();
	}

	@Override
	public String getTenantId() {
		return wfProcUserAuthBiz.getTenantId();
	}

	@Override
	public String getTenantId(String userCode) {
		return wfProcUserAuthBiz.getTenantId(userCode);
	}
	
	@Override
	public String getOrgId() {
		return wfProcUserAuthBiz.getOrgId();
	}

	@Override
	public String getOrgId(String userCode) {
		return wfProcUserAuthBiz.getOrgId(userCode);
	}

	@Override
	public String getOrgCode() {
		return wfProcUserAuthBiz.getOrgCode();
	}

	@Override
	public String getOrgCode(String userCode) {
		return wfProcUserAuthBiz.getOrgCode(userCode);
	}

	@Override
	public List<String> getRoleCodes() {
		return wfProcUserAuthBiz.getRoleCodes();
	}

	@Override
	public List<String> getRoleCodes(String userCode) {
		return wfProcUserAuthBiz.getRoleCodes(userCode);
	}

	@Override
	public List<String> getAuthOrgCodes() {
		return wfProcUserAuthBiz.getAuthOrgCodes();
	}

	@Override
	public List<String> getAuthOrgCodes(String userCode) {
		return wfProcUserAuthBiz.getAuthOrgCodes(userCode);
	}

	public void userAuthenticate(WfProcAuthDataBean authData, boolean checkOrg,
			boolean checkRole) throws WorkflowException {
		wfProcUserAuthBiz.userAuthenticate(authData, checkOrg, checkRole);
	}
}
