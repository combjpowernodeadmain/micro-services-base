package com.bjzhianjia.scp.security.wf.task.entity;

import java.io.Serializable;

public class WfProcTaskBean implements Serializable{

    private static final long serialVersionUID = 4359789445035543306L;

    private Integer id;
    private String procInstId;
    private String procId;
    private String procKey;
//    private String procName;
//    private String procBizid;
//    private String procOrgcode;
//    private String procMemo;
    private String procPtaskid;
    private String procPtaskcode;
    private String procPtaskname;
    private String procTaskCommitter;
    private Integer procTaskCommittime;
    private String procCtaskid;
    private String procCtaskcode;
    private String procCtaskname;
    private String procExecutionid;
    private String procTaskGroup;
    private String procAppointUsers;
    private String procLicensor;
    private String procMandatary;
    private String procTaskAssignee;
    private Integer procTaskAssigntime;
    private Integer procTaskEndtime;
    private String procTaskApprStatus;
    private String procTaskApprOpinion;
    private String procTaskProperties;
    private String procDatapermission;
    private String procRefusetask;
    private String procTaskStatus;
    private String procApproveurl;
    private String procParallel;
    private String procParallelStatus;
    private String procVotetask;
    private String procVotepower;
    private String procVoterule;
    private float procVoteweight;
    private float procVotethreshold;
    private String procVotequickly;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getProcKey() {
        return procKey;
    }

    public void setProcKey(String procKey) {
        this.procKey = procKey;
    }
    
//    public String getProcName() {
//        return procName;
//    }
//    
//    public void setProcName(String procName) {
//        this.procName = procName;
//    }
//    
//    public String getProcBizid() {
//        return procBizid;
//    }
//    
//    public void setProcBizid(String procBizid) {
//        this.procBizid = procBizid;
//    }
//
//    public String getProcOrgcode() {
//        return procOrgcode;
//    }
//
//    public void setProcOrgcode(String procOrgcode) {
//        this.procOrgcode = procOrgcode;
//    }
//    
//    public String getProcMemo() {
//        return procMemo;
//    }
//
//    public void setProcMemo(String procMemo) {
//        this.procMemo = procMemo;
//    }

    public String getProcPtaskid() {
        return procPtaskid;
    }

    public void setProcPtaskid(String procPtaskid) {
        this.procPtaskid = procPtaskid;
    }

    public String getProcPtaskcode() {
        return procPtaskcode;
    }

    public void setProcPtaskcode(String procPtaskcode) {
        this.procPtaskcode = procPtaskcode;
    }

    public String getProcPtaskname() {
        return procPtaskname;
    }

    public void setProcPtaskname(String procPtaskname) {
        this.procPtaskname = procPtaskname;
    }

    public String getProcExecutionid() {
        return procExecutionid;
    }

    public void setProcExecutionid(String procExecutionid) {
        this.procExecutionid = procExecutionid;
    }

    public String getProcTaskCommitter() {
        return procTaskCommitter;
    }

    public void setProcTaskCommitter(String procTaskCommitter) {
        this.procTaskCommitter = procTaskCommitter;
    }

    public Integer getProcTaskCommittime() {
        return procTaskCommittime;
    }

    public void setProcTaskCommittime(Integer procTaskCommittime) {
        this.procTaskCommittime = procTaskCommittime;
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

    public String getProcTaskGroup() {
        return procTaskGroup;
    }

    public void setProcTaskGroup(String procTaskGroup) {
        this.procTaskGroup = procTaskGroup;
    }

    public String getProcAppointUsers() {
        return procAppointUsers;
    }

    public void setProcAppointUsers(String procAppointUsers) {
        this.procAppointUsers = procAppointUsers;
    }

    public String getProcLicensor() {
        return procLicensor;
    }

    public void setProcLicensor(String procLicensor) {
        this.procLicensor = procLicensor;
    }

    public String getProcMandatary() {
        return procMandatary;
    }

    public void setProcMandatary(String procMandatary) {
        this.procMandatary = procMandatary;
    }

    public String getProcTaskAssignee() {
        return procTaskAssignee;
    }

    public void setProcTaskAssignee(String procTaskAssignee) {
        this.procTaskAssignee = procTaskAssignee;
    }

    public Integer getProcTaskAssigntime() {
        return procTaskAssigntime;
    }

    public void setProcTaskAssigntime(Integer procTaskAssigntime) {
        this.procTaskAssigntime = procTaskAssigntime;
    }

    public Integer getProcTaskEndtime() {
        return procTaskEndtime;
    }

    public void setProcTaskEndtime(Integer procTaskEndtime) {
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

    public String getProcTaskProperties() {
        return procTaskProperties;
    }

    public void setProcTaskProperties(String procTaskProperties) {
        this.procTaskProperties = procTaskProperties;
    }

    public String getProcRefusetask() {
        return procRefusetask;
    }

    public void setProcRefusetask(String procRefusetask) {
        this.procRefusetask = procRefusetask;
    }

    public String getProcDatapermission() {
        return procDatapermission;
    }

    public void setProcDatapermission(String procDatapermission) {
        this.procDatapermission = procDatapermission;
    }

    public String getProcTaskStatus() {
        return procTaskStatus;
    }

    public void setProcTaskStatus(String procTaskStatus) {
        this.procTaskStatus = procTaskStatus;
    }

    public String getProcApproveurl() {
        return procApproveurl;
    }

    public void setProcApproveurl(String procApproveurl) {
        this.procApproveurl = procApproveurl;
    }

    public String getProcParallel() {
        return procParallel;
    }

    public void setProcParallel(String procParallel) {
        this.procParallel = procParallel;
    }

    public String getProcParallelStatus() {
        return procParallelStatus;
    }

    public void setProcParallelStatus(String procParallelStatus) {
        this.procParallelStatus = procParallelStatus;
    }

    public String getProcVotetask() {
        return procVotetask;
    }

    public void setProcVotetask(String procVotetask) {
        this.procVotetask = procVotetask;
    }

    public String getProcVotepower() {
        return procVotepower;
    }

    public void setProcVotepower(String procVotepower) {
        this.procVotepower = procVotepower;
    }

    public String getProcVoterule() {
        return procVoterule;
    }

    public void setProcVoterule(String procVoterule) {
        this.procVoterule = procVoterule;
    }

    public float getProcVoteweight() {
        return procVoteweight;
    }

    public void setProcVoteweight(float procVoteweight) {
        this.procVoteweight = procVoteweight;
    }

    public float getProcVotethreshold() {
        return procVotethreshold;
    }

    public void setProcVotethreshold(float procVotethreshold) {
        this.procVotethreshold = procVotethreshold;
    }

    public String getProcVotequickly() {
        return procVotequickly;
    }

    public void setProcVotequickly(String procVotequickly) {
        this.procVotequickly = procVotequickly;
    }
}