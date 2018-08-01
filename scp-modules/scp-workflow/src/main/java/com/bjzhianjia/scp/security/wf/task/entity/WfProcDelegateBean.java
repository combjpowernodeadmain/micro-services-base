package com.bjzhianjia.scp.security.wf.task.entity;

import java.io.Serializable;

public class WfProcDelegateBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String procInstId;
	private String procLicensor;
	private String procLicensorRole;
	private String procMandatary;
	private Integer procLicenseTime;
	private Integer procCancelTime;
	private String procLicenseIsvalid;
	private String procTenantId;

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

	public String getProcLicensorRole() {
		return procLicensorRole;
	}

	public void setProcLicensorRole(String procLicensorRole) {
		this.procLicensorRole = procLicensorRole;
	}

	public Integer getProcLicenseTime() {
		return procLicenseTime;
	}

	public void setProcLicenseTime(Integer procLicenseTime) {
		this.procLicenseTime = procLicenseTime;
	}

	public Integer getProcCancelTime() {
		return procCancelTime;
	}

	public void setProcCancelTime(Integer procCancelTime) {
		this.procCancelTime = procCancelTime;
	}

	public String getProcLicenseIsvalid() {
		return procLicenseIsvalid;
	}

	public void setProcLicenseIsvalid(String procLicenseIsvalid) {
		this.procLicenseIsvalid = procLicenseIsvalid;
	}

	public String getProcTenantId() {
		return procTenantId;
	}

	public void setProcTenantId(String procTenantId) {
		this.procTenantId = procTenantId;
	}
}