package com.bjzhianjia.scp.security.wf.auth.service;

import java.util.List;

/**
 * 获取用户信息接口。  在不同的平台使用工作流的时候，需要实现此接口。
 * 
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * Jul 29, 2018          ric_w      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @date Jul 29, 2018
 * @author ric_w
 *
 */
public interface IWfProcUserAuthService {
    
	/**
	 * 获取用户主键ID
	 * 
	 * @return
	 */
	public String getUserId();

	/**
	 * 获取用户编码
	 * 
	 * @return
	 */
	public String getUserCode();

	/**
	 * 获取部门主键ID
	 * 
	 * @return
	 */
	public String getDeptId();

	/**
	 * 获取用户的部门主键
	 * 
	 * @param userCode
	 * @return
	 */
	public String getDeptId(String userCode);

	/**
	 * 获取组织主键ID
	 * 
	 * @return
	 */
	public String getOrgId();

	/**
	 * 获取租户ID
	 * 
	 * @return
	 */
	public String getTenantId();

	/**
	 * 根据用户主键获取租户ID
	 * 
	 * @param userCode
	 * @return
	 */
	public String getTenantId(String userCode);

	/**
	 * 根据用户主键获取其组织id
	 * 
	 * @param userCode
	 * @return
	 */
	public String getOrgId(String userCode);

	/**
	 * 获取当前用户的机构编码
	 * 
	 * @return
	 */
	public String getOrgCode();

	/**
	 * 根据用户编码获取机构编码
	 * 
	 * @param userCode
	 * @return
	 */
	public String getOrgCode(String userCode);

	/**
	 * 获取当前用户的角色
	 * 
	 * @return
	 */
	public List<String> getRoleCodes();

	/**
	 * 根据用户编码获取角色编码
	 * 
	 * @param userCode
	 * @return
	 */
	public List<String> getRoleCodes(String userCode);

	/**
	 * 获取当前用户的授权组织编码列表
	 * 
	 * @return
	 */
	public List<String> getAuthOrgCodes();

	/**
	 * 根据用户的编码获取授权组织编码列表
	 * 
	 * @param userCode
	 * @return
	 */
	public List<String> getAuthOrgCodes(String userCode);

	/**
	 * 获取当前用户权限数据1
	 * 
	 * @return
	 */
	public String getSelfPermissionData1();

	/**
	 * 根据用户code获取权限1
	 * 
	 * @param userCode
	 * @return
	 */
	public String getSelfPermissionData1(String userCode);

	/**
	 * 获取当前用户权限数据2
	 * @return
	 */
	public String getSelfPermissionData2();

	/**
	 * 根据用户code获取权限2
	 * @param userCode
	 * @return
	 */
	public String getSelfPermissionData2(String userCode);

	/**
	 * 获取当前用户权限数据3
	 * @return
	 */
	public String getSelfPermissionData3();

	/**
	 * 根据用户code获取权限3
	 * @param userCode
	 * @return
	 */
	public String getSelfPermissionData3(String userCode);

	/**
	 * 获取当前用户权限数据4
	 * @return
	 */
	public String getSelfPermissionData4();

	/**
	 * 根据用户code获取权限5
	 * @param userCode
	 * @return
	 */
	public String getSelfPermissionData4(String userCode);

	/**
	 * 获取当前用户权限数据5
	 * @return
	 */
	public String getSelfPermissionData5();

	/**
	 * 根据用户code获取权限5
	 * @param userCode
	 * @return
	 */
	public String getSelfPermissionData5(String userCode);
}
