package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.DeptBiztype;

public class DeptBizTypeVo extends DeptBiztype{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5241125608763629183L;
	private String departmentName;
	private String bizTypeName;
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getBizTypeName() {
		return bizTypeName;
	}
	public void setBizTypeName(String bizTypeName) {
		this.bizTypeName = bizTypeName;
	}
	
}
