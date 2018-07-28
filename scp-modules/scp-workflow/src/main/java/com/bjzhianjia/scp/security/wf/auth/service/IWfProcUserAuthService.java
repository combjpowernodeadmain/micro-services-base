package com.bjzhianjia.scp.security.wf.auth.service;

import java.util.List;

import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.vo.WfProcAuthDataBean;

public interface IWfProcUserAuthService {
	public String getUserId();

	public String getUserCode();

	public String getOrgId();

	public String getTenantId();

	public String getTenantId(String userCode);

	public String getOrgId(String userCode);

	public String getOrgCode();

	public String getOrgCode(String userCode);

	public List<String> getRoleCodes();

	public List<String> getRoleCodes(String userCode);

	public List<String> getAuthOrgCodes();

	public List<String> getAuthOrgCodes(String userCode);

	public void userAuthenticate(WfProcAuthDataBean authData, boolean checkOrg,
			boolean checkRole) throws WorkflowException;
}
