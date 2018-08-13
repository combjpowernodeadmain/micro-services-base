package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.VhclManagement;

public class VhclManagementVo extends VhclManagement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2981994274431776116L;

	private String terminalPhone;
	private String departmentName;

	public String getTerminalPhone() {
		return terminalPhone;
	}

	public void setTerminalPhone(String terminalPhone) {
		this.terminalPhone = terminalPhone;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}
