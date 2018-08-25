package com.bjzhianjia.scp.security.wf.auth.biz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.wf.auth.service.IWfProcTokenService;
import com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService;
import com.bjzhianjia.scp.security.wf.base.WfBaseBiz;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfUserAuthType;
import com.bjzhianjia.scp.security.wf.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.utils.StringUtil;
import com.bjzhianjia.scp.security.wf.vo.WfProcAuthDataBean;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WfProcUserAuthBiz extends WfBaseBiz {
	@Autowired
    private static IWfProcTokenService wfProcTokenService;
	@Autowired
	private IWfProcUserAuthService wfProcUserAuthService;
	
	/**
     * 用户认证，未指定认证方式，或者认证不通过的请求抛出WorkflowException，不能进行后续流程处理
     * @param authData      用户认证信息
     * @param checkOrg		是否检查用户机构
     * @param checkRole		是否检查用户角色
     * @throws WorkflowException
     */
	public void userAuthenticate(WfProcAuthDataBean authData, boolean checkOrg,
			boolean checkRole) throws WorkflowException {
		initAndCheckUserAuth(authData, checkOrg, checkRole, false);
	}
	
	/**
     * 用户认证，未指定认证方式，或者认证不通过的请求抛出WorkflowException，不能进行后续流程处理
     * @param authData      用户认证信息
     * @param checkOrg		是否检查用户机构
     * @param checkRole		是否检查用户角色
     * @param initUserSelfData	是否初始化当前用户自定义权限数据
     * @throws WorkflowException
     */
	public void userAuthenticate(WfProcAuthDataBean authData, boolean checkOrg,
			boolean checkRole, boolean initUserSelfData) throws WorkflowException {
		initAndCheckUserAuth(authData, checkOrg, checkRole, initUserSelfData);
	}
	
	/**
     * 用户认证，未指定认证方式，或者认证不通过的请求抛出WorkflowException，不能进行后续流程处理
     * @param authData      用户认证信息
     * @param checkOrg		是否检查用户机构
     * @param checkRole		是否检查用户角色
     * @param initUserSelfData	是否初始化当前用户自定义权限数据
     * @throws WorkflowException
     */
	private void initAndCheckUserAuth(WfProcAuthDataBean authData,
			boolean checkOrg, boolean checkRole, boolean initUserSelfData)
			throws WorkflowException {
		String authType = authData.getProcAuthType();
		String userCode = null;
		String orgCode = null;
		String tenantID = null;
		List<String> roleCodes = new ArrayList<String>();
        		
        if (StringUtil.isNull(authType)) {
            log.warn("尚未制定授权信息：authType值。");
            throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000003); 
        } 
        
        switch (authType) {
            case WfUserAuthType.PROC_AUTH_TOKEN:
            	tenantID = getTenantId(authData.getProcTaskUser());
            	orgCode = getOrgCode(authData.getProcTaskUser());
            	roleCodes = getRoleCodes(authData.getProcTaskUser());
            	List<String> authOrgCodes = getAuthOrgCodes(authData.getProcTaskUser());
            	
                if (StringUtil.isNull(authData.getProcTaskUser())
                    || (checkOrg && StringUtil.isNull(orgCode))
					|| (checkRole && (roleCodes == null || roleCodes.isEmpty()))
					|| (StringUtil.isNull(tenantID) || tenantID == null)
					|| !wfProcTokenService.isMatched(tenantID,
							authData.getProcTokenUser(),
							authData.getProcTokenPass())) {
                	throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000004);
                }
                
                // 创建操作角色列表，并放到流程变量数据中
                authData.setProcOrgCode(orgCode);
                authData.setProcTaskRoles(roleCodes);
                authData.setProcDeptId(getDeptId(authData.getProcTaskUser()));
                authData.setProcAuthOrgCodes(authOrgCodes);
                authData.setProcTaskRoles(roleCodes);
                
                if (initUserSelfData) {
                	authData.setProcSelfPermissionData1(getSelfPermissionData1(authData.getProcTaskUser()));
                	authData.setProcSelfPermissionData2(getSelfPermissionData2(authData.getProcTaskUser()));
                	authData.setProcSelfPermissionData3(getSelfPermissionData3(authData.getProcTaskUser()));
                	authData.setProcSelfPermissionData4(getSelfPermissionData4(authData.getProcTaskUser()));
                	authData.setProcSelfPermissionData5(getSelfPermissionData5(authData.getProcTaskUser()));
                }
                break;
            case WfUserAuthType.PROC_AUTH_SESSION:
            	userCode = getUserCode();
            	tenantID = getTenantId();
                roleCodes = getRoleCodes();
                
                if (StringUtil.isNull(userCode) || userCode == null) {
                    log.error("用户授权信息不完整，没有用户ID，不能进行流程操作。", WorkflowEnumResults.WF_COMM_02000005);
                	throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000005);
                } 
                
                if(checkRole && (roleCodes == null || roleCodes.isEmpty())) {
                    log.error("用户授权信息不完整，没有角色Id，不能进行流程操作。", WorkflowEnumResults.WF_COMM_02000005);
                    throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000005);
                }

                if(StringUtil.isNull(tenantID) || tenantID == null) {
                    log.error("用户授权信息不完整，没有租户Id，不能进行流程操作。", WorkflowEnumResults.WF_COMM_02000005);
                    throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000005);
                }
                
                authData.setProcTaskUser(userCode);
                authData.setProcDeptId(getDeptId());
                authData.setProcTenantId(tenantID);
                authData.setProcTaskRoles(roleCodes);
                authData.setProcOrgCode(getOrgCode());
                authData.setProcAuthOrgCodes(getAuthOrgCodes());
                
                if (initUserSelfData) {
                	authData.setProcSelfPermissionData1(getSelfPermissionData1());
                	authData.setProcSelfPermissionData2(getSelfPermissionData2());
                	authData.setProcSelfPermissionData3(getSelfPermissionData3());
                	authData.setProcSelfPermissionData4(getSelfPermissionData4());
                	authData.setProcSelfPermissionData5(getSelfPermissionData5());
                }
                
                break;
            default:
                throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000006);   
        }
    }

    /**
	 * 获取当前登录用户ID
	 * @return String   当前登录用户ID
	 */
	public String getUserId() {
		return wfProcUserAuthService.getUserId();
	}

	/**
	 * 获取当前登录用户编码
	 * @return String   当前登录用户编码
	 */
	public String getUserCode() {
		return wfProcUserAuthService.getUserCode();
	}

	/**
	 * 获取当前登录用户所属部门ID
	 * @return String   当前登录用户所属部门ID
	 */
	public String getDeptId() {
		return wfProcUserAuthService.getDeptId();
	}

	/**
	 * 获取指定用户所属部门ID
	 * @param userCode  用户代码
	 * @return String   指定用户所属部门ID
	 */
	public String getDeptId(String userCode) {
		return wfProcUserAuthService.getDeptId(userCode);
	}

	/**
	 * 获取当前登录用户所属机构ID
	 * @return String   当前登录用户所属机构ID
	 */
	public String getOrgId() {
		return wfProcUserAuthService.getOrgId();
	}

	/**
	 * 获取指定用户所属机构ID
	 * @param userCode  用户代码
	 * @return String   指定用户所属机构ID
	 */
	public String getOrgId(String userCode) {
		return wfProcUserAuthService.getOrgId(userCode);
	}
	
	/**
	 * 获取当前登录用户所属租户ID
	 * @return String   当前登录用户所属租户ID
	 */
	public String getTenantId() {
		return wfProcUserAuthService.getTenantId();
	}

	/**
	 * 获取指定用户所属租户ID
	 * @param userCode  用户代码
	 * @return String   指定用户所属租户ID
	 */
	public String getTenantId(String userCode) {
		return wfProcUserAuthService.getTenantId(userCode);
	}

	/**
	 * 获取当前登录用户所属机构编码
	 * @return String   当前登录用户所属机构编码
	 */
	public String getOrgCode() {
		return wfProcUserAuthService.getOrgCode();
	}

	/**
	 * 获取指定用户所属机构编码
	 * @param userCode  用户代码
	 * @return String   指定用户所属机构编码
	 */
	public String getOrgCode(String userCode) {
		return wfProcUserAuthService.getOrgCode(userCode);
	}

	/**
	 * 获取当前登录用户分配的角色列表
	 * @return List   当前登录用户分配的角色列表
	 */
	public List<String> getRoleCodes() {
		return wfProcUserAuthService.getRoleCodes();
	}

	/**
	 * 获取指定用户分配的角色列表
	 * @param userCode  用户代码
	 * @return List   指定用户分配的角色列表
	 */
	public List<String> getRoleCodes(String userCode) {
		return wfProcUserAuthService.getRoleCodes(userCode);
	}

	/**
	 * 获取当前登录用户分配的机构列表
	 * @return List   当前登录用户分配的机构列表
	 */
	public List<String> getAuthOrgCodes() {
		return wfProcUserAuthService.getAuthOrgCodes();
	}

	/**
	 * 获取指定用户分配的机构列表
	 * @param userCode  用户代码
	 * @return List   指定用户分配的机构列表
	 */
	public List<String> getAuthOrgCodes(String userCode) {
		return wfProcUserAuthService.getAuthOrgCodes(userCode);
	}

	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData1() {
		return wfProcUserAuthService.getSelfPermissionData1();
	}

	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	userCode  	用户代码
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData1(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData1(userCode);
	}
	
	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData2() {
		return wfProcUserAuthService.getSelfPermissionData2();
	}
	
	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	userCode  	用户代码
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData2(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData2(userCode);
	}

	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData3() {
		return wfProcUserAuthService.getSelfPermissionData3();
	}
	
	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	userCode  	用户代码
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData3(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData3(userCode);
	}

	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData4() {
		return wfProcUserAuthService.getSelfPermissionData4();
	}
	
	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	userCode  	用户代码
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData4(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData4(userCode);
	}

	/**
	 * 获取当前登录用户所属租户自定义数据权限配置的数据
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData5() {
		return wfProcUserAuthService.getSelfPermissionData5();
	}
	
	/**
	 * 获取指定用户所属租户自定义数据权限配置的数据
	 * @param 	userCode  	用户代码
	 * @return 	List   			自定义权限数据
	 */
	public String getSelfPermissionData5(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData5(userCode);
	}
}
