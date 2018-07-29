package com.bjzhianjia.scp.security.wf.auth.biz;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.wf.auth.service.IWfProcTokenService;
import com.bjzhianjia.scp.security.wf.auth.service.IWfProcUserAuthService;
import com.bjzhianjia.scp.security.wf.base.WfBaseBiz;
import com.bjzhianjia.scp.security.wf.constant.Constants.WfUserAuthType;
import com.bjzhianjia.scp.security.wf.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.utils.SpringBeanUtil;
import com.bjzhianjia.scp.security.wf.utils.StringUtil;
import com.bjzhianjia.scp.security.wf.vo.WfProcAuthDataBean;

@Service
public class WfProcUserAuthBiz extends WfBaseBiz {
	@Autowired
    IWfProcTokenService wfProcTokenService;
	
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
                	|| StringUtil.isNull(authData.getProcTenantId())
                    || (checkOrg && StringUtil.isNull(authData.getProcOrgCode()))
					|| (checkRole && StringUtil.isNull(authData
							.getProcTaskRole()))
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
                	throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000005);
                } 
                
                if(roleCodes == null || roleCodes.isEmpty()) {
                    throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000005);
                }

                if(StringUtil.isNull(tenantID) || tenantID == null) {
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
		return BaseContextHandler.getUserID();
	}
	
	public String getUserCode() {
		return BaseContextHandler.getUserID();
	}
	
	public String getDeptId() {
		return BaseContextHandler.getDepartID(); 
	}
	
	public String getDeptId(String userCode) {
		return BaseContextHandler.getDepartID(); 
	}
	
	public String getTenantId() {
		return BaseContextHandler.getTenantID();
	}
	
	public String getTenantId(String userCode) {
		return BaseContextHandler.getTenantID();
	}
	
	public String getOrgId() {
		return BaseContextHandler.getDepartID();
	}
	
	public String getOrgId(String userCode) {
		return BaseContextHandler.getDepartID();
	}
	
	public String getOrgCode() {
		return BaseContextHandler.getDepartID();
	}
	
	public String getOrgCode(String userCode) {
		return BaseContextHandler.getDepartID();
	}
	
	public List<String> getRoleCodes() {
		return null;
	}
	
	public List<String> getRoleCodes(String userCode) {
		return null;
	}
	
	public List<String> getAuthOrgCodes() {
		return null;
	}
	
	public List<String> getAuthOrgCodes(String userCode) {
		return null;
	}
	
	/**
     * 根据类名称实例化对象
     * 
     * @param className
     *            类名称
     * @return
     * @throws WorkFlowException
     */
    @SuppressWarnings("unchecked")
    protected IWfProcUserAuthService createUserAuthService(String className)
        throws WorkflowException {
        try {
            if (!StringUtil.isNull(className)) {
                Class<IWfProcUserAuthService> clz =
                    (Class<IWfProcUserAuthService>) Class.forName(className);
                
                return (IWfProcUserAuthService) SpringBeanUtil.getBean(clz);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000002, e);
        }
    }
}
