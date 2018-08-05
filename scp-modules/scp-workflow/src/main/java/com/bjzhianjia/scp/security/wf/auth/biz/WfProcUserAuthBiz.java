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
     * @throws WorkflowException
     */
    public void userAuthenticate(WfProcAuthDataBean authData, boolean checkOrg, boolean checkRole)
        throws WorkflowException {
        String authType = authData.getProcAuthType();
        List<String> roleCodes = new ArrayList<String>();
        
        if (StringUtil.isNull(authType)) {
            throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000003); 
        } 
        
        switch (authType) {
            case WfUserAuthType.PROC_AUTH_TOKEN:
                if (StringUtil.isNull(authData.getProcTaskUser())
                    || (checkOrg && StringUtil.isNull(authData.getProcOrgCode()))
					|| (checkRole && StringUtil.isNull(authData.getProcTaskRole()))
					|| !wfProcTokenService.isMatched(
							authData.getProcTenantId(),
							authData.getProcTokenUser(),
							authData.getProcTokenPass())) {
				throw new WorkflowException(
						WorkflowEnumResults.WF_COMM_02000004);
                }
                
                // 创建操作角色列表，并放到流程变量数据中
                roleCodes.add(authData.getProcTaskRole());
                authData.setProcOrgCode(getOrgCode(authData.getProcTaskUser()));
                authData.setProcDeptId(getDeptId(authData.getProcTaskUser()));// todo:Token方式是否可以
                authData.setProcAuthOrgCodes(getAuthOrgCodes(authData.getProcTaskUser()));
                authData.setProcTaskRoles(roleCodes);
                
                break;
            case WfUserAuthType.PROC_AUTH_SESSION:
            	String userCode = getUserCode();
            	String tenantID = getTenantId();
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
                break;
            default:
                throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000006);   
        }
    }
    
    public String getUserId() {
    	return wfProcUserAuthService.getUserId();
    }

	public String getUserCode() {
		return wfProcUserAuthService.getUserCode();
	}

	public String getDeptId() {
		return wfProcUserAuthService.getDeptId();
	}

	public String getDeptId(String userCode) {
		return wfProcUserAuthService.getDeptId(userCode);
	}

	public String getOrgId() {
		return wfProcUserAuthService.getOrgId();
	}

	public String getTenantId() {
		return wfProcUserAuthService.getTenantId();
	}

	public String getTenantId(String userCode) {
		return wfProcUserAuthService.getTenantId(userCode);
	}

	public String getOrgId(String userCode) {
		return wfProcUserAuthService.getOrgId(userCode);
	}

	public String getOrgCode() {
		return wfProcUserAuthService.getOrgCode();
	}

	public String getOrgCode(String userCode) {
		return wfProcUserAuthService.getOrgCode(userCode);
	}

	public List<String> getRoleCodes() {
		return wfProcUserAuthService.getRoleCodes();
	}

	public List<String> getRoleCodes(String userCode) {
		return wfProcUserAuthService.getRoleCodes(userCode);
	}

	public List<String> getAuthOrgCodes() {
		return wfProcUserAuthService.getAuthOrgCodes();
	}

	public List<String> getAuthOrgCodes(String userCode) {
		return wfProcUserAuthService.getAuthOrgCodes(userCode);
	}

	public String getSelfPermissionData1() {
		return wfProcUserAuthService.getSelfPermissionData1();
	}

	public String getSelfPermissionData1(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData1(userCode);
	}

	public String getSelfPermissionData2() {
		return wfProcUserAuthService.getSelfPermissionData2();
	}

	public String getSelfPermissionData2(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData2(userCode);
	}

	public String getSelfPermissionData3() {
		return wfProcUserAuthService.getSelfPermissionData3();
	}

	public String getSelfPermissionData3(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData3(userCode);
	}

	public String getSelfPermissionData4() {
		return wfProcUserAuthService.getSelfPermissionData4();
	}

	public String getSelfPermissionData4(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData4(userCode);
	}

	public String getSelfPermissionData5() {
		return wfProcUserAuthService.getSelfPermissionData5();
	}

	public String getSelfPermissionData5(String userCode) {
		return wfProcUserAuthService.getSelfPermissionData5(userCode);
	}
}
