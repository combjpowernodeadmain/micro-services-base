package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.PublicOpinion;

public class PublicOpinionVo extends PublicOpinion {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3352273692613668691L;
	private String caseSource;
	private String exeStatusName;
	private String opinTypeName;//舆情来源类型名称
	private String opinLevelName;//舆情等级名称
	private String caseCode;//相关立案编号
	
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
	public String getOpinTypeName() {
		return opinTypeName;
	}
	public void setOpinTypeName(String opinTypeName) {
		this.opinTypeName = opinTypeName;
	}
	public String getOpinLevelName() {
		return opinLevelName;
	}
	public void setOpinLevelName(String opinLevelName) {
		this.opinLevelName = opinLevelName;
	}
	public String getCaseCode() {
		return caseCode;
	}
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
}
