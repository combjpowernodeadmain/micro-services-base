/*
 * @(#) WfProcTokenBean.java  1.0  August 29, 2016
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
package com.bjzhianjia.scp.security.wf.auth.entity;

import java.io.Serializable;

/**
 * Description: 工作流Token认证实体类
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
public class WfProcTokenBean implements Serializable {
	private static final long serialVersionUID = -1433209802492739618L;

	private String id;
	private String procTenantId;
	private String procDepartId;
	private String procTokenUser;
	private String procTokenPass;
	private String procSystemName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcTenantId() {
		return procTenantId;
	}

	public void setProcTenantId(String procTenantId) {
		this.procTenantId = procTenantId;
	}

	public String getProcDepartId() {
		return procDepartId;
	}

	public void setProcDepartId(String procDepartId) {
		this.procDepartId = procDepartId;
	}

	public String getProcTokenUser() {
		return procTokenUser;
	}

	public void setProcTokenUser(String procTokenUser) {
		this.procTokenUser = procTokenUser;
	}

	public String getProcTokenPass() {
		return procTokenPass;
	}

	public void setProcTokenPass(String procTokenPass) {
		this.procTokenPass = procTokenPass;
	}

	public String getProcSystemName() {
		return procSystemName;
	}

	public void setProcSystemName(String procSystemName) {
		this.procSystemName = procSystemName;
	}
}
