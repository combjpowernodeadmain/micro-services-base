/*
 * @(#) WfProcessDataBean.java  1.0  September 12, 2016
 *
 * Copyright 2016 by bjzhianjia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * IQB("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with IQB.
 */
package com.bjzhianjia.scp.security.wf.base.vo;

/**
 * Description: 工作流流程数据实体类
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.09.12    mayongming       1.0           1.0 Version
 * </pre>
 */
public class WfProcessDataBean implements java.io.Serializable{

    private static final long serialVersionUID = 3475799760542035480L;

    private String procDefId;
    private String procDefKey;
    private String procDefName;
    private int procDefVersion;
    private String modelId;
    private String deploymentId;
    private String procInstId;
    private String procInstName;
    private String procTaskId;
    private String procTaskCode;
    private String procTaskName;
    private String procTaskGroup;

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcDefKey() {
        return procDefKey;
    }

    public void setProcDefKey(String procDefKey) {
        this.procDefKey = procDefKey;
    }

    public String getProcDefName() {
        return procDefName;
    }

    public void setProcDefName(String procDefName) {
        this.procDefName = procDefName;
    }

    public int getProcDefVersion() {
        return procDefVersion;
    }

    public void setProcDefVersion(int procDefVersion) {
        this.procDefVersion = procDefVersion;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcInstName() {
        return procInstName;
    }

    public void setProcInstName(String procInstName) {
        this.procInstName = procInstName;
    }

    public String getProcTaskId() {
        return procTaskId;
    }

    public void setProcTaskId(String procTaskId) {
        this.procTaskId = procTaskId;
    }

    public String getProcTaskCode() {
        return procTaskCode;
    }

    public void setProcTaskCode(String procTaskCode) {
        this.procTaskCode = procTaskCode;
    }

    public String getProcTaskName() {
        return procTaskName;
    }

    public void setProcTaskName(String procTaskName) {
        this.procTaskName = procTaskName;
    }

    public String getProcTaskGroup() {
        return procTaskGroup;
    }

    public void setProcTaskGroup(String procTaskGroup) {
        this.procTaskGroup = procTaskGroup;
    }
}
