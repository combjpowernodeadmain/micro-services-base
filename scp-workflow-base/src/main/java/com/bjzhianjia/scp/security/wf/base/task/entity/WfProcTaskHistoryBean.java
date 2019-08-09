
package com.bjzhianjia.scp.security.wf.base.task.entity;

import java.io.Serializable;

public class WfProcTaskHistoryBean implements Serializable{

    private static final long serialVersionUID = -7770104776972370654L;

    private String procInstId;
    private String procCtaskid;
    private String procCtaskcode;
    private String procCtaskname;
    private String procTaskAssignee;
    private String procLicensor;
    private String procTaskCommitter;
    private String procTaskCommittime;
    private String procTaskAssigntime;
    private String procTaskEndtime;
    private String procTaskApprStatus;
    private String procTaskApprOpinion;

    private String procSelfdata1;
    private String procSelfpermission1;

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcCtaskid() {
        return procCtaskid;
    }

    public void setProcCtaskid(String procCtaskid) {
        this.procCtaskid = procCtaskid;
    }

    public String getProcCtaskcode() {
        return procCtaskcode;
    }

    public void setProcCtaskcode(String procCtaskcode) {
        this.procCtaskcode = procCtaskcode;
    }

    public String getProcCtaskname() {
        return procCtaskname;
    }

    public void setProcCtaskname(String procCtaskname) {
        this.procCtaskname = procCtaskname;
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

    public String getProcTaskCommitter() {
        return procTaskCommitter;
    }

    public void setProcTaskCommitter(String procTaskCommitter) {
        this.procTaskCommitter = procTaskCommitter;
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

    public String getProcTaskEndtime() {
        return procTaskEndtime;
    }

    public void setProcTaskEndtime(String procTaskEndtime) {
        this.procTaskEndtime = procTaskEndtime;
    }

    public String getProcTaskApprStatus() {
        return procTaskApprStatus;
    }

    public void setProcTaskApprStatus(String procTaskApprStatus) {
        this.procTaskApprStatus = procTaskApprStatus;
    }

    public String getProcTaskApprOpinion() {
        return procTaskApprOpinion;
    }

    public void setProcTaskApprOpinion(String procTaskApprOpinion) {
        this.procTaskApprOpinion = procTaskApprOpinion;
    }

    public String getProcSelfdata1() {
        return procSelfdata1;
    }

    public void setProcSelfdata1(String procSelfdata1) {
        this.procSelfdata1 = procSelfdata1;
    }
    public String getProcSelfpermission1() {
        return procSelfpermission1;
    }

    public void setProcSelfpermission1(String procSelfpermission1) {
        this.procSelfpermission1 = procSelfpermission1;
    }
}
