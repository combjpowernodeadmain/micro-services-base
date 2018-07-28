/*
 * @(#) WfProcTaskPropertyBean.java  1.0  September 9, 2016
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
package com.bjzhianjia.scp.security.wf.task.entity;

/**
 * Description: 工作流任务表单数据实体类
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.09.09    mayongming       1.0           1.0 Version
 * </pre>
 */
public class WfProcTaskPropertyBean implements java.io.Serializable{
    private static final long serialVersionUID = -4012541394911894696L;

    private String id;
    private String name;
    private String type;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
