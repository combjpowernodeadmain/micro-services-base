package com.bjzhianjia.scp.security.wf.auth.service;

import java.util.List;

public interface IWfProcUserAuthService {
	public String getUserId();

	public String getUserCode();

	public String getDeptId();

	public String getDeptId(String userCode);

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

	public String getSelfPermissionData1();

	public String getSelfPermissionData1(String userCode);

	public String getSelfPermissionData2();

	public String getSelfPermissionData2(String userCode);

	public String getSelfPermissionData3();

	public String getSelfPermissionData3(String userCode);

	public String getSelfPermissionData4();

	public String getSelfPermissionData4(String userCode);

	public String getSelfPermissionData5();

	public String getSelfPermissionData5(String userCode);
}
