/*
 * @(#) WfProcessDefinitionBean.java  1.0  August 23, 2016
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
 * Description: 流程定义信息实体类
 * 
 * @author mayongming
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.23    mayongming       1.0        1.0 Version
 * </pre>
 */
public class WfProcDefinitionBean implements java.io.Serializable {
    private static final long serialVersionUID = 2098913217917562040L;

    private String definitionId;// 流程定义ID
    private String definitionKey;// 流程定义Key
    private String definitionName;// 流程定义名称
    private String deploymentId;// 流程定义部署ID
    private String resourceName;// 流程定义资源名称
    private String diagramResourceName;// 流程定义图片名称
    private String description;// 流程定义描述
    private boolean isSuspended;// 流程定义是否挂起
    private int version;// 流程定义的版本

    public String getDefinitionId() {
        return definitionId;
    }

    public void setDefinitionId(String definitionId) {
        this.definitionId = definitionId;
    }

    public String getDefinitionKey() {
        return definitionKey;
    }

    public void setDefinitionKey(String definitionKey) {
        this.definitionKey = definitionKey;
    }

    public String getDefinitionName() {
        return definitionName;
    }

    public void setDefinitionName(String definitionName) {
        this.definitionName = definitionName;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDiagramResourceName() {
        return diagramResourceName;
    }

    public void setDiagramResourceName(String diagramResourceName) {
        this.diagramResourceName = diagramResourceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setSuspended(boolean isSuspended) {
        this.isSuspended = isSuspended;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
