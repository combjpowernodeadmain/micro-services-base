package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.MayorHotline;

public class MayorHotlineVo extends MayorHotline {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3401804172273275902L;
	private String caseSource;
	private String exeStatusName;
	private String caseCode;
	private String caseId;
	private String hotlnSourceName;

	public String getCaseSource() {
		return caseSource;
	}

	public void setCaseSource(String caseSource) {
		this.caseSource = caseSource;
	}

	public String getExeStatusName() {
		return exeStatusName;
	}

	public void setExeStatusName(String exeStatusName) {
		this.exeStatusName = exeStatusName;
	}

	public String getCaseCode() {
		return caseCode;
	}

	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getHotlnSourceName() {
		return hotlnSourceName;
	}

	public void setHotlnSourceName(String hotlnSourceName) {
		this.hotlnSourceName = hotlnSourceName;
	}
}
