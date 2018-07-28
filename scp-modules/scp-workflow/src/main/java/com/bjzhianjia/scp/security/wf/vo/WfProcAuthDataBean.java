/*
 * @(#) WfProcAuthDataBean.java  1.0  Dec 16, 2016
 *
 * Copyright 2016 by bjzhianjia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * BJZAJ("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with BJZAJ.
 */
package com.bjzhianjia.scp.security.wf.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcessAuthData;

/**
 * Description: 工作流流程认证数据实体类
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.12.16    mayongming       1.0           1.0 Version
 * </pre>
 */
public class WfProcAuthDataBean implements java.io.Serializable{

    private static final long serialVersionUID = 1597515654706105817L;
    private Map<String, Object> authData = new HashMap<String, Object>();

    public String getProcAuthType() {
        return (String) authData.get(WfProcessAuthData.PROC_AUTHTYPE);
    }

    public void setProcAuthType(String procAuthType) {
        put(WfProcessAuthData.PROC_AUTHTYPE, procAuthType);
    }

    public String getProcTokenUser() {
        return (String) authData.get(WfProcessAuthData.PROC_TOKENUSER);
    }

    public void setProcTokenUser(String procTokenUser) {
        put(WfProcessAuthData.PROC_TOKENUSER, procTokenUser);
    }

    public String getProcTokenPass() {
        return (String) authData.get(WfProcessAuthData.PROC_TOKENPASS);
    }

    public void setProcTokenPass(String procTokenPass) {
        put(WfProcessAuthData.PROC_TOKENPASS, procTokenPass);
    }

    public String getProcTaskUser() {
        return (String) authData.get(WfProcessAuthData.PROC_TASKUSER);
    }

    public void setProcTaskUser(String procTaskUser) {
        put(WfProcessAuthData.PROC_TASKUSER, procTaskUser);
    }

    public String getProcTaskRole() {
        return (String) authData.get(WfProcessAuthData.PROC_TASKROLE);
    }

    public void setProcTaskRole(String procTaskRole) {
        put(WfProcessAuthData.PROC_TASKROLE, procTaskRole);
    }

    @SuppressWarnings("unchecked")
    public List<String> getProcTaskRoles() {
        return (List<String>) authData.get(WfProcessAuthData.PROC_TASKROLES);
    }

    public void setProcTaskRoles(List<String> procTaskRoles) {
        put(WfProcessAuthData.PROC_TASKROLES, procTaskRoles);
    }

    public String getProcOrgCode() {
        return (String) authData.get(WfProcessAuthData.PROC_ORGCODE);
    }

    public void setProcOrgCode(String procOrgCode) {
        put(WfProcessAuthData.PROC_ORGCODE, procOrgCode);
    }
    
    public String getProcTenantId() {
        return (String) authData.get(WfProcessAuthData.PROC_TENANTID);
    }

    public void setProcTenantId(String procTenantId) {
        put(WfProcessAuthData.PROC_TENANTID, procTenantId);
    }
    
    @SuppressWarnings("unchecked")
	public List<String> getProcAuthOrgCodes() {
    	return (List<String>) authData.get(WfProcessAuthData.PROC_AUTHORGCODES);
    }

    public void setProcAuthOrgCodes(List<String>  procAuthOrgCodes) {
        put(WfProcessAuthData.PROC_AUTHORGCODES, procAuthOrgCodes);
    }
    
    public Object getAuthData(String key) {
        return authData.get(key);
    }

    public Map<String, Object> getAuthData() {
        return authData;
    }

    public void setAuthData(Map<String, Object> authData) {
        this.authData = authData;
    }

    public void put(String key, Object value) {
        this.authData.put(key, value);
    }

    public void putAll(Map<String, Object> variableData) {
        this.authData.putAll(variableData);
    }

    public boolean isEmpty() {
        return authData == null || authData.isEmpty();
    }
}
