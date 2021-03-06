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
package com.bjzhianjia.scp.security.wf.base.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjzhianjia.scp.security.wf.base.constant.Constants;
import com.bjzhianjia.scp.security.wf.base.constant.Constants.WfProcessAuthData;

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
public class WfProcAuthDataBean implements java.io.Serializable {
    private static final long serialVersionUID = 1597515654706105817L;
    private Map<String, Object> authData = new HashMap<String, Object>();

    public String getProcAuthType() {
        return (String) authData.get(WfProcessAuthData.PROC_AUTHTYPE);
    }

    public void setProcAuthType(String procAuthType) {
        put(WfProcessAuthData.PROC_AUTHTYPE, procAuthType);
    }

    public String getProcSelfPermissionData1() {
        return (String) authData.get(WfProcessAuthData.PROC_SELFPERMISSIONDATA1);
    }

    public void setProcSelfPermissionData1(String procSelfPermissionData) {
        put(WfProcessAuthData.PROC_SELFPERMISSIONDATA1, procSelfPermissionData);
    }
    
    public String getProcSelfPermissionData2() {
        return (String) authData.get(WfProcessAuthData.PROC_SELFPERMISSIONDATA2);
    }

    public void setProcSelfPermissionData2(String procSelfPermissionData) {
        put(WfProcessAuthData.PROC_SELFPERMISSIONDATA2, procSelfPermissionData);
    }
    
    public String getProcSelfPermissionData3() {
        return (String) authData.get(WfProcessAuthData.PROC_SELFPERMISSIONDATA3);
    }

    public void setProcSelfPermissionData3(String procSelfPermissionData) {
        put(WfProcessAuthData.PROC_SELFPERMISSIONDATA3, procSelfPermissionData);
    }
    
    public String getProcSelfPermissionData4() {
        return (String) authData.get(WfProcessAuthData.PROC_SELFPERMISSIONDATA4);
    }

    public void setProcSelfPermissionData4(String procSelfPermissionData) {
        put(WfProcessAuthData.PROC_SELFPERMISSIONDATA4, procSelfPermissionData);
    }
    
    public String getProcSelfPermissionData5() {
        return (String) authData.get(WfProcessAuthData.PROC_SELFPERMISSIONDATA5);
    }

    public void setProcSelfPermissionData5(String procSelfPermissionData) {
        put(WfProcessAuthData.PROC_SELFPERMISSIONDATA5, procSelfPermissionData);
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

    public String getProcDeptId() {
        return (String) authData.get(WfProcessAuthData.PROC_DEPATID);
    }

    public void setProcDeptId(String procTaskUser) {
        put(WfProcessAuthData.PROC_DEPATID, procTaskUser);
    }

    /**
     * 获取流程任务用户所属部门集
     *
     * @return
     */
    public List<String> getProcDeptIds() {
        return (List<String>) authData.get(WfProcessAuthData.PROC_DEPATIDS);
    }

    /**
     * 设置流程任务用户所属部门集
     *
     * @return
     */
    public void setProcDeptIds(List<String> procTaskUser) {
        put(WfProcessAuthData.PROC_DEPATIDS, procTaskUser);
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

    public void setProcTaskGroup (String procTaskGroup){
        this.put(Constants.WfProcessDataAttr.PROC_TASKGROUP,procTaskGroup);
    }

    public String getProcTaskGroup(){
        return (String)authData.get(Constants.WfProcessDataAttr.PROC_TASKGROUP);
    }

    public String getDeptPermission () {
        return (String)authData.get(Constants.WfProcTaskProperty.PROC_DEPTPERMISSION);
    }

    public void setDeptPermission(String deptPermission){
        this.put(Constants.WfProcTaskProperty.PROC_DEPTPERMISSION, deptPermission);
    }


    public String getSelfPermission1(){
        return (String)authData.get(Constants.WfProcTaskProperty.PROC_SELFPERMISSION1);
    }

    public void setSelfPermission1(String selfPermission1){
        put(Constants.WfProcTaskProperty.PROC_SELFPERMISSION1, selfPermission1);
    }
}
