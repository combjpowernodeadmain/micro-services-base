package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.LeadershipAssign;

public class LeadershipAssignVo extends LeadershipAssign {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6855954963860622168L;
	private String caseSource;
	private String exeStatusName;
	private String regulaObjName;
	private String caseCode;//相关立案编号
	private String leaderNames;//交办领导姓名
	
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
	public String getRegulaObjName() {
		return regulaObjName;
	}
	public void setRegulaObjName(String regulaObjName) {
		this.regulaObjName = regulaObjName;
	}
	public String getCaseCode() {
		return caseCode;
	}
	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	public String getLeaderNames() {
		return leaderNames;
	}
	public void setLeaderNames(String leaderNames) {
		this.leaderNames = leaderNames;
	}
}
