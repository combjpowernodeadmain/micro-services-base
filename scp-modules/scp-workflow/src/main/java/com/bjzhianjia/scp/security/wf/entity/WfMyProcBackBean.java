/*
 * @(#) IqbWfMyProcBackBean.java  1.0  August 29, 2016
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

import java.io.Serializable;

/**
 * Description: 工作流监控数据实体类
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
public class WfMyProcBackBean implements Serializable{

    private static final long serialVersionUID = 5803040105171657719L;

    private String procInstId;
    private String procBizId;
    private String procBiztype;
    private String procOrgcode;
    private String procName;// 流程名称
    private String procTaskid;
    private String procTaskcode;
    private String procTaskname;
    private String procCreatetime;// 流程创建时间
    private String procEndtime;// 流程完成时间
    private String procLicenseTime;
    private String procMemo;// 流程说明
    private String procTaskCommitter;// 提交人
    private String procTaskAssignee;// 处理人
    private String procLicensor; // 委托人
    private String procMandatary;
    private String procTaskCommittime;// 任务到达时间
    private String procTaskAssigntime; // 任务签收时间
    private String procTaskEndime;	// 任务处理时间
    private String procStatus;// 流程状态代码
    private String procStatusName; // 流程状态名称
    private String procTaskstatus; // 流程任务状态
    private String procDisplayurl;// 流程审批URL
    private String procUserType; // 用户类型
    
    public String getProcLicenseTime() {
        return procLicenseTime;
    }

    public void setProcLicenseTime(String procLicenseTime) {
        this.procLicenseTime = procLicenseTime;
    }

    public String getProcMandatary() {
        return procMandatary;
    }

    public void setProcMandatary(String procMandatary) {
        this.procMandatary = procMandatary;
    }

    public String getProcBizId() {
        return procBizId;
    }

    public void setProcBizId(String procBizId) {
        this.procBizId = procBizId;
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

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getProcCreatetime() {
        return procCreatetime;
    }

    public void setProcCreatetime(String procCreatetime) {
        this.procCreatetime = procCreatetime;
    }

    public String getProcEndtime() {
        return procEndtime;
    }

    public void setProcEndtime(String procEndtime) {
        this.procEndtime = procEndtime;
    }

    public String getProcMemo() {
        return procMemo;
    }

    public void setProcMemo(String procMemo) {
        this.procMemo = procMemo;
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

    public String getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(String procStatus) {
        this.procStatus = procStatus;
    }

    public String getProcStatusName() {
        return procStatusName;
    }

    public void setProcStatusName(String procStatusName) {
        this.procStatusName = procStatusName;
    }

    public String getProcDisplayurl() {
        return procDisplayurl;
    }

    public void setProcDisplayurl(String procDisplayurl) {
        this.procDisplayurl = procDisplayurl;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcTaskid() {
        return procTaskid;
    }

    public void setProcTaskid(String procTaskid) {
        this.procTaskid = procTaskid;
    }

    public String getProcTaskcode() {
        return procTaskcode;
    }

    public void setProcTaskcode(String procTaskcode) {
        this.procTaskcode = procTaskcode;
    }

    public String getProcTaskname() {
        return procTaskname;
    }

    public void setProcTaskname(String procTaskname) {
        this.procTaskname = procTaskname;
    }

    public String getProcTaskAssigntime() {
        return procTaskAssigntime;
    }

    public void setProcTaskAssigntime(String procTaskAssigntime) {
        this.procTaskAssigntime = procTaskAssigntime;
    }

    public String getProcTaskEndime() {
		return procTaskEndime;
	}

	public void setProcTaskEndime(String procTaskEndime) {
		this.procTaskEndime = procTaskEndime;
	}

	public String getProcTaskstatus() {
        return procTaskstatus;
    }

    public void setProcTaskstatus(String procTaskstatus) {
        this.procTaskstatus = procTaskstatus;
    }

	public String getProcUserType() {
		return procUserType;
	}

	public void setProcUserType(String procUserType) {
		this.procUserType = procUserType;
	}
}
