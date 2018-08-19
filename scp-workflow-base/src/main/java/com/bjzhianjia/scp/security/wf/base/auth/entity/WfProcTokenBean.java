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
package com.bjzhianjia.scp.security.wf.base.auth.entity;

import java.io.Serializable;
import java.util.Date;

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
	private String isDeleted;
	private String isEnable;
	private Date enableOperateTime;
	private Date deleteOperateTime;
	

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

    
    /**
     * @return the isDeleted
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    
    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    
    /**
     * @return the isEnable
     */
    public String getIsEnable() {
        return isEnable;
    }

    
    /**
     * @param isEnable the isEnable to set
     */
    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    
    /**
     * @return the enableOperateTime
     */
    public Date getEnableOperateTime() {
        return enableOperateTime;
    }

    
    /**
     * @param enableOperateTime the enableOperateTime to set
     */
    public void setEnableOperateTime(Date enableOperateTime) {
        this.enableOperateTime = enableOperateTime;
    }

    
    /**
     * @return the deleteOperateTime
     */
    public Date getDeleteOperateTime() {
        return deleteOperateTime;
    }

    
    /**
     * @param deleteOperateTime the deleteOperateTime to set
     */
    public void setDeleteOperateTime(Date deleteOperateTime) {
        this.deleteOperateTime = deleteOperateTime;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "WfProcTokenBean [id=" + id + ", procTenantId=" + procTenantId + ", procDepartId="
            + procDepartId + ", procTokenUser=" + procTokenUser + ", procTokenPass=" + procTokenPass
            + ", procSystemName=" + procSystemName + ", isDeleted=" + isDeleted + ", isEnable="
            + isEnable + ", enableOperateTime=" + enableOperateTime + ", deleteOperateTime="
            + deleteOperateTime + "]";
    }
    
}
