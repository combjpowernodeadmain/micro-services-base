package com.bjzhianjia.scp.security.wf.base.design.entity;

import java.io.Serializable;

/**
 * 流程属性定义表
 * 
 * @author
 *
 */
public class WfProcPropsBean implements Serializable {
	private Integer id;
	/* 流程代码 */
	private String procId;
	/* 流程任务编号 */
	private String procTaskCode;
	/* 流程属性类型1：流程属性2：流程任务属性, */
	private String procPropsType;
	/* 流程属性key */
	private String procPropsKey;
	/* 流程属性value */
	private String procPropsValue;
	/* 流程属性创建人 */
	private String procPropsCreator;
	/* 流程属性创建时间 */
	private Integer procPropsCreatetime;
	/* 流程属性最后更新人 */
	private String procPropsUpdator;
	/* 流程属性最后更新时间 */
	private Integer procPropsUpdatetime;
	private Integer procPropsVersion;
	private String procTenantId;
	private String procDepartId;

	private static final long serialVersionUID = 1L;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getProcId() {
		return procId;
	}

	public void setProcId(String procId) {
		this.procId = procId;
	}

	public String getProcTaskCode() {
		return procTaskCode;
	}

	public void setProcTaskCode(String procTaskCode) {
		this.procTaskCode = procTaskCode;
	}

	public String getProcPropsType() {
		return procPropsType;
	}

	public void setProcPropsType(String procPropsType) {
		this.procPropsType = procPropsType;
	}

	public String getProcPropsKey() {
		return procPropsKey;
	}

	public void setProcPropsKey(String procPropsKey) {
		this.procPropsKey = procPropsKey;
	}

	public String getProcPropsValue() {
		return procPropsValue;
	}

	public void setProcPropsValue(String procPropsValue) {
		this.procPropsValue = procPropsValue;
	}

	public String getProcPropsCreator() {
		return procPropsCreator;
	}

	public void setProcPropsCreator(String procPropsCreator) {
		this.procPropsCreator = procPropsCreator;
	}

	public Integer getProcPropsCreatetime() {
		return procPropsCreatetime;
	}

	public void setProcPropsCreatetime(Integer procPropsCreatetime) {
		this.procPropsCreatetime = procPropsCreatetime;
	}

	public String getProcPropsUpdator() {
		return procPropsUpdator;
	}

	public void setProcPropsUpdator(String procPropsUpdator) {
		this.procPropsUpdator = procPropsUpdator;
	}

	public Integer getProcPropsUpdatetime() {
		return procPropsUpdatetime;
	}

	public void setProcPropsUpdatetime(Integer procPropsUpdatetime) {
		this.procPropsUpdatetime = procPropsUpdatetime;
	}

	public Integer getProcPropsVersion() {
		return procPropsVersion;
	}

	public void setProcPropsVersion(Integer procPropsVersion) {
		this.procPropsVersion = procPropsVersion;
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
}