package com.bjzhianjia.scp.cgp.vo;

import java.io.Serializable;
import java.util.Date;


import org.springframework.format.annotation.DateTimeFormat;

public class Regula_EnterPriseVo implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5656912162147079843L;
	//监管对象ID
	private Integer regulaObjectId;
	private String objCode;
    private String objName;
    private String objType;
    private Integer objId;//要删除
    private String objAddress;
    private String linkman;
    private String linkmanPhone;
    private String introduction;
    private String remark;
    private String picBefore;
    private Float longitude;
    private Integer griId;
    private Float latitude;
    private String gatherer;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date gatherTime;
    private String bizList;
    private String eventList;
    private String mapInfo;
    
    //企业信息ID
    private Integer enterpriseId;
    private Integer regulaObjId;//要删除
    private String tradeRegistCode;
    private String typeCode;
    private String creditCode;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date establishedDate;
    private Float registCapital;
    private String businessScope;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date operatingStartDate;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date operatingEndDate;
    private String legalRepresent;
    private String representPhone;
    private String certificateType;
    private String cerificateCode;
    private String firefightCertCode;
    private Date firefightReleaseTime;
    private String economicCode;
    private String industryCode;
    private String status;
    private String creditLevel;
    private String industryScope;
    private String businessScale;
    private Integer employeesCount;
    private Integer laidOffCount;
    private Float realWages;
    private Float averageSalary;
    private Float socialInsuranceCount;
	public Integer getRegulaObjectId() {
		return regulaObjectId;
	}
	public void setRegulaObjectId(Integer regulaObjectId) {
		this.regulaObjectId = regulaObjectId;
	}
	public Integer getEnterpriseId() {
		return enterpriseId;
	}
	public void setEnterpriseId(Integer enterpriseId) {
		this.enterpriseId = enterpriseId;
	}
	public String getObjCode() {
		return objCode;
	}
	public void setObjCode(String objCode) {
		this.objCode = objCode;
	}
	public String getObjName() {
		return objName;
	}
	public void setObjName(String objName) {
		this.objName = objName;
	}
	public String getObjType() {
		return objType;
	}
	public void setObjType(String objType) {
		this.objType = objType;
	}
	public Integer getObjId() {
		return objId;
	}
	public void setObjId(Integer objId) {
		this.objId = objId;
	}
	public String getObjAddress() {
		return objAddress;
	}
	public void setObjAddress(String objAddress) {
		this.objAddress = objAddress;
	}
	public String getLinkman() {
		return linkman;
	}
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	public String getLinkmanPhone() {
		return linkmanPhone;
	}
	public void setLinkmanPhone(String linkmanPhone) {
		this.linkmanPhone = linkmanPhone;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPicBefore() {
		return picBefore;
	}
	public void setPicBefore(String picBefore) {
		this.picBefore = picBefore;
	}
	public Float getLongitude() {
		return longitude;
	}
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	public Integer getGriId() {
		return griId;
	}
	public void setGriId(Integer griId) {
		this.griId = griId;
	}
	public Float getLatitude() {
		return latitude;
	}
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	public String getGatherer() {
		return gatherer;
	}
	public void setGatherer(String gatherer) {
		this.gatherer = gatherer;
	}
	public Date getGatherTime() {
		return gatherTime;
	}
	public void setGatherTime(Date gatherTime) {
		this.gatherTime = gatherTime;
	}
	public String getBizList() {
		return bizList;
	}
	public void setBizList(String bizList) {
		this.bizList = bizList;
	}
	public String getEventList() {
		return eventList;
	}
	public void setEventList(String eventList) {
		this.eventList = eventList;
	}
	public String getMapInfo() {
		return mapInfo;
	}
	public void setMapInfo(String mapInfo) {
		this.mapInfo = mapInfo;
	}
	public Integer getRegulaObjId() {
		return regulaObjId;
	}
	public void setRegulaObjId(Integer regulaObjId) {
		this.regulaObjId = regulaObjId;
	}
	public String getTradeRegistCode() {
		return tradeRegistCode;
	}
	public void setTradeRegistCode(String tradeRegistCode) {
		this.tradeRegistCode = tradeRegistCode;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getCreditCode() {
		return creditCode;
	}
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	public Date getEstablishedDate() {
		return establishedDate;
	}
	public void setEstablishedDate(Date establishedDate) {
		this.establishedDate = establishedDate;
	}
	public Float getRegistCapital() {
		return registCapital;
	}
	public void setRegistCapital(Float registCapital) {
		this.registCapital = registCapital;
	}
	public String getBusinessScope() {
		return businessScope;
	}
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	public Date getOperatingStartDate() {
		return operatingStartDate;
	}
	public void setOperatingStartDate(Date operatingStartDate) {
		this.operatingStartDate = operatingStartDate;
	}
	public Date getOperatingEndDate() {
		return operatingEndDate;
	}
	public void setOperatingEndDate(Date operatingEndDate) {
		this.operatingEndDate = operatingEndDate;
	}
	public String getLegalRepresent() {
		return legalRepresent;
	}
	public void setLegalRepresent(String legalRepresent) {
		this.legalRepresent = legalRepresent;
	}
	public String getRepresentPhone() {
		return representPhone;
	}
	public void setRepresentPhone(String representPhone) {
		this.representPhone = representPhone;
	}
	public String getCertificateType() {
		return certificateType;
	}
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	public String getCerificateCode() {
		return cerificateCode;
	}
	public void setCerificateCode(String cerificateCode) {
		this.cerificateCode = cerificateCode;
	}
	public String getFirefightCertCode() {
		return firefightCertCode;
	}
	public void setFirefightCertCode(String firefightCertCode) {
		this.firefightCertCode = firefightCertCode;
	}
	public Date getFirefightReleaseTime() {
		return firefightReleaseTime;
	}
	public void setFirefightReleaseTime(Date firefightReleaseTime) {
		this.firefightReleaseTime = firefightReleaseTime;
	}
	public String getEconomicCode() {
		return economicCode;
	}
	public void setEconomicCode(String economicCode) {
		this.economicCode = economicCode;
	}
	public String getIndustryCode() {
		return industryCode;
	}
	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getCreditLevel() {
		return creditLevel;
	}
	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}
	public String getIndustryScope() {
		return industryScope;
	}
	public void setIndustryScope(String industryScope) {
		this.industryScope = industryScope;
	}
	public String getBusinessScale() {
		return businessScale;
	}
	public void setBusinessScale(String businessScale) {
		this.businessScale = businessScale;
	}
	public Integer getEmployeesCount() {
		return employeesCount;
	}
	public void setEmployeesCount(Integer employeesCount) {
		this.employeesCount = employeesCount;
	}
	public Integer getLaidOffCount() {
		return laidOffCount;
	}
	public void setLaidOffCount(Integer laidOffCount) {
		this.laidOffCount = laidOffCount;
	}
	public Float getRealWages() {
		return realWages;
	}
	public void setRealWages(Float realWages) {
		this.realWages = realWages;
	}
	public Float getAverageSalary() {
		return averageSalary;
	}
	public void setAverageSalary(Float averageSalary) {
		this.averageSalary = averageSalary;
	}
	public Float getSocialInsuranceCount() {
		return socialInsuranceCount;
	}
	public void setSocialInsuranceCount(Float socialInsuranceCount) {
		this.socialInsuranceCount = socialInsuranceCount;
	}
}
