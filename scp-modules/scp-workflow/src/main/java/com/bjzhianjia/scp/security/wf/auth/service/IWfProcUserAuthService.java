package com.bjzhianjia.scp.security.wf.auth.service;

import java.util.List;

public interface IWfProcUserAuthService {
	/**
	 * 获取当前登录用户ID
	 * @return String   当前登录用户ID
	 */
	public String getUserId();

	/**
	 * 获取当前登录用户编码
	 * @return String   当前登录用户编码
	 */
	public String getUserCode();

	/**
	 * 获取当前登录用户所属部门ID
	 * @return String   当前登录用户所属部门ID
	 */
	public String getDeptId();

	/**
	 * 获取指定用户所属部门ID
	 * @param userCode  用户代码
	 * @return String   指定用户所属部门ID
	 */
	public String getDeptId(String userCode);

	/**
	 * 获取当前登录用户所属机构ID
	 * @return String   当前登录用户所属机构ID
	 */
	public String getOrgId();

	/**
	 * 获取指定用户所属机构ID
	 * @param userCode  用户代码
	 * @return String   指定用户所属机构ID
	 */
	public String getOrgId(String userCode);
	
	/**
	 * 获取当前登录用户所属租户ID
	 * @return String   当前登录用户所属租户ID
	 */
	public String getTenantId();

	/**
	 * 获取指定用户所属租户ID
	 * @param userCode  用户代码
	 * @return String   指定用户所属租户ID
	 */
	public String getTenantId(String userCode);

	/**
	 * 获取当前登录用户所属机构编码
	 * @return String   当前登录用户所属机构编码
	 */
	public String getOrgCode();

	/**
	 * 获取指定用户所属机构编码
	 * @param userCode  用户代码
	 * @return String   指定用户所属机构编码
	 */
	public String getOrgCode(String userCode);

	/**
	 * 获取当前登录用户分配的角色列表
	 * @return List   当前登录用户分配的角色列表
	 */
	public List<String> getRoleCodes();

	/**
	 * 获取指定用户分配的角色列表
	 * @param userCode  用户代码
	 * @return List   指定用户分配的角色列表
	 */
	public List<String> getRoleCodes(String userCode);

	/**
	 * 获取当前登录用户分配的机构列表
	 * @return List   当前登录用户分配的机构列表
	 */
	public List<String> getAuthOrgCodes();

	/**
	 * 获取指定用户分配的机构列表
	 * @param userCode  用户代码
	 * @return List   指定用户分配的机构列表
	 */
	public List<String> getAuthOrgCodes(String userCode);

	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData1();

	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	procTenantId  	租户ID
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData1(String userCode);
	
	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData2();
	
	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	procTenantId  	租户ID
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData2(String userCode);

	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData3();
	
	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	procTenantId  	租户ID
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData3(String userCode);

	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData4();
	
	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	procTenantId  	租户ID
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData4(String userCode);

	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData5();
	
	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	procTenantId  	租户ID
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData5(String userCode);
}
