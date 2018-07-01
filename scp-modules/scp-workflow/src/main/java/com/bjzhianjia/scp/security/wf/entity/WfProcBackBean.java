/*
 * @(#) IqbWfProcBackBean.java  1.0  August 29, 2016
 *
 * Copyright 2016 by 北京爱钱帮财富科技有限公司
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * IQB("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with IQB.
 */
package com.bjzhianjia.scp.security.wf.entity;

/**
 * Description: 工作流监控信息
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.29    mayongming       1.0        1.0 Version
 * </pre>
 */
public class WfProcBackBean implements java.io.Serializable{

    private static final long serialVersionUID = -1426876992577740727L;

    private Integer id;
    private String procBizid;
    private String procBiztype;
    private String procOrgcode;
    private String procInstId;
    private String procId;
    private String procKey;
    private String procName;
    private String procMemo;
    private String procCtaskId;
    private String procCtaskcode;
    private String procCtaskname;
    private String procTaskStatus;
    private String procTaskStatusName;
    private String procTaskCommitter;
    private String procTaskAssignee;
    private String procLicensor;
    private String procMandatary;
    private String procTaskCommittime;
    private String procTaskAssigntime;
    private String procRefusetask;
    private String procTaskProperties;
    private String procApproveurl;// 流程审批URL

    public String getProcApproveurl() {
        return procApproveurl;
    }

    public void setProcApproveurl(String procApproveurl) {
        this.procApproveurl = procApproveurl;
    }

    public String getProcTaskProperties() {
        return procTaskProperties;
    }

    public void setProcTaskProperties(String procTaskProperties) {
        this.procTaskProperties = procTaskProperties;
    }

    public String getProcCtaskId() {
        return procCtaskId;
    }

    public void setProcCtaskId(String procCtaskId) {
        this.procCtaskId = procCtaskId;
    }

    public String getProcKey() {
        return procKey;
    }

    public void setProcKey(String procKey) {
        this.procKey = procKey;
    }

    public String getProcCtaskcode() {
        return procCtaskcode;
    }

    public void setProcCtaskcode(String procCtaskcode) {
        this.procCtaskcode = procCtaskcode;
    }

    public String getProcMandatary() {
        return procMandatary;
    }

    public void setProcMandatary(String procMandatary) {
        this.procMandatary = procMandatary;
    }

    public String getProcBizid() {
        return procBizid;
    }

    public void setProcBizid(String procBizid) {
        this.procBizid = procBizid;
    }

    public String getProcBiztype() {
        return procBiztype;
    }

    public void setProcBiztype(String procBiztype) {
        this.procBiztype = procBiztype;
    }

    public String getProcOrgcode() {
        return procOrgcode;
    }

    public void setProcOrgcode(String procOrgcode) {
        this.procOrgcode = procOrgcode;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getProcMemo() {
        return procMemo;
    }

    public void setProcMemo(String procMemo) {
        this.procMemo = procMemo;
    }

    public String getProcTaskStatus() {
        return procTaskStatus;
    }

    public void setProcTaskStatus(String procTaskStatus) {
        this.procTaskStatus = procTaskStatus;
    }

    public String getProcTaskStatusName() {
        return procTaskStatusName;
    }

    public void setProcTaskStatusName(String procTaskStatusName) {
        this.procTaskStatusName = procTaskStatusName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcTaskCommitter() {
        return procTaskCommitter;
    }

    public void setProcTaskCommitter(String procTaskCommitter) {
        this.procTaskCommitter = procTaskCommitter;
    }

    public String getProcTaskAssignee() {
        return procTaskAssignee;
    }

    public void setProcTaskAssignee(String procTaskAssignee) {
        this.procTaskAssignee = procTaskAssignee;
    }

    public String getProcLicensor() {
        return procLicensor;
    }

    public void setProcLicensor(String procLicensor) {
        this.procLicensor = procLicensor;
    }

    public String getProcTaskCommittime() {
        return procTaskCommittime;
    }

    public void setProcTaskCommittime(String procTaskCommittime) {
        this.procTaskCommittime = procTaskCommittime;
    }

    public String getProcTaskAssigntime() {
        return procTaskAssigntime;
    }

    public void setProcTaskAssigntime(String procTaskAssigntime) {
        this.procTaskAssigntime = procTaskAssigntime;
    }

    public String getProcCtaskname() {
        return procCtaskname;
    }

    public void setProcCtaskname(String procCtaskname) {
        this.procCtaskname = procCtaskname;
    }

    public String getProcRefusetask() {
        return procRefusetask;
    }

    public void setProcRefusetask(String procRefusetask) {
        this.procRefusetask = procRefusetask;
    }
}