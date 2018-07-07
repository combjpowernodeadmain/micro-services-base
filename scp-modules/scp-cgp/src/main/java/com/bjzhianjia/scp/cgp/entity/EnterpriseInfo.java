package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 企业信息
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
@Table(name = "enterprise_info")
public class EnterpriseInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //工商注册号
    @Column(name = "trade_regist_code")
    private String tradeRegistCode;
	
	    //企业类型
    @Column(name = "type_code")
    private String typeCode;
	
	    //统一社会信用代码
    @Column(name = "credit_code")
    private String creditCode;
	
	    //成立时间
    @Column(name = "established_date")
    private Date establishedDate;
	
	    //注册资本
    @Column(name = "regist_capital")
    private Float registCapital;
	
	    //经营范围
    @Column(name = "business_scope")
    private String businessScope;
	
	    //经营有效期-起始时间
    @Column(name = "operating_start_date")
    private Date operatingStartDate;
	
	    //经营有效期-截至时间
    @Column(name = "operating_end_date")
    private Date operatingEndDate;
	
	    //法定代表人
    @Column(name = "legal_represent")
    private String legalRepresent;
	
	    //法人电话
    @Column(name = "represent_phone")
    private String representPhone;
	
	    //证件类型
    @Column(name = "certificate_type")
    private String certificateType;
	
	    //证件号码
    @Column(name = "cerificate_code")
    private String cerificateCode;
	
	    //消防证件号
    @Column(name = "firefight_cert_code")
    private String firefightCertCode;
	
	    //消防证发证时间
    @Column(name = "firefight_release_time")
    private Date firefightReleaseTime;
	
	    //企业类型/经济性质
    @Column(name = "economic_code")
    private String economicCode;
	
	    //所属行业
    @Column(name = "industry_code")
    private String industryCode;
	
	    //企业状态
    @Column(name = "status")
    private String status;
	
	    //企业信用等级
    @Column(name = "credit_level")
    private String creditLevel;
	
	    //企业所属范围
    @Column(name = "industry_scope")
    private String industryScope;
	
	    //企业规模
    @Column(name = "business_scale")
    private String businessScale;
	
	    //从业人员数量
    @Column(name = "employees_count")
    private Integer employeesCount;
	
	    //下岗失业人员数量
    @Column(name = "laid_off_count")
    private Integer laidOffCount;
	
	    //实发工资总额
    @Column(name = "real_wages")
    private Float realWages;
	
	    //月平均工资
    @Column(name = "average_salary")
    private Float averageSalary;
	
	    //参保人数
    @Column(name = "social_insurance_count")
    private Float socialInsuranceCount;
	
	    //创建用户ID
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //更新时间
    @Column(name = "upd_time")
    private Date updTime;
	
	    //更新用户ID
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //更新用户姓名
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //租户
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //是否删除（1是/0否）
    @Column(name = "id_deleted")
    private String idDeleted;
	
	    //是否禁用（1是/0否）
    @Column(name = "id_disabled")
    private String idDisabled;
	

	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：工商注册号
	 */
	public void setTradeRegistCode(String tradeRegistCode) {
		this.tradeRegistCode = tradeRegistCode;
	}
	/**
	 * 获取：工商注册号
	 */
	public String getTradeRegistCode() {
		return tradeRegistCode;
	}
	/**
	 * 设置：企业类型
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	/**
	 * 获取：企业类型
	 */
	public String getTypeCode() {
		return typeCode;
	}
	/**
	 * 设置：统一社会信用代码
	 */
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	/**
	 * 获取：统一社会信用代码
	 */
	public String getCreditCode() {
		return creditCode;
	}
	/**
	 * 设置：成立时间
	 */
	public void setEstablishedDate(Date establishedDate) {
		this.establishedDate = establishedDate;
	}
	/**
	 * 获取：成立时间
	 */
	public Date getEstablishedDate() {
		return establishedDate;
	}
	/**
	 * 设置：注册资本
	 */
	public void setRegistCapital(Float registCapital) {
		this.registCapital = registCapital;
	}
	/**
	 * 获取：注册资本
	 */
	public Float getRegistCapital() {
		return registCapital;
	}
	/**
	 * 设置：经营范围
	 */
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	/**
	 * 获取：经营范围
	 */
	public String getBusinessScope() {
		return businessScope;
	}
	/**
	 * 设置：经营有效期-起始时间
	 */
	public void setOperatingStartDate(Date operatingStartDate) {
		this.operatingStartDate = operatingStartDate;
	}
	/**
	 * 获取：经营有效期-起始时间
	 */
	public Date getOperatingStartDate() {
		return operatingStartDate;
	}
	/**
	 * 设置：经营有效期-截至时间
	 */
	public void setOperatingEndDate(Date operatingEndDate) {
		this.operatingEndDate = operatingEndDate;
	}
	/**
	 * 获取：经营有效期-截至时间
	 */
	public Date getOperatingEndDate() {
		return operatingEndDate;
	}
	/**
	 * 设置：法定代表人
	 */
	public void setLegalRepresent(String legalRepresent) {
		this.legalRepresent = legalRepresent;
	}
	/**
	 * 获取：法定代表人
	 */
	public String getLegalRepresent() {
		return legalRepresent;
	}
	/**
	 * 设置：法人电话
	 */
	public void setRepresentPhone(String representPhone) {
		this.representPhone = representPhone;
	}
	/**
	 * 获取：法人电话
	 */
	public String getRepresentPhone() {
		return representPhone;
	}
	/**
	 * 设置：证件类型
	 */
	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}
	/**
	 * 获取：证件类型
	 */
	public String getCertificateType() {
		return certificateType;
	}
	/**
	 * 设置：证件号码
	 */
	public void setCerificateCode(String cerificateCode) {
		this.cerificateCode = cerificateCode;
	}
	/**
	 * 获取：证件号码
	 */
	public String getCerificateCode() {
		return cerificateCode;
	}
	/**
	 * 设置：消防证件号
	 */
	public void setFirefightCertCode(String firefightCertCode) {
		this.firefightCertCode = firefightCertCode;
	}
	/**
	 * 获取：消防证件号
	 */
	public String getFirefightCertCode() {
		return firefightCertCode;
	}
	/**
	 * 设置：消防证发证时间
	 */
	public void setFirefightReleaseTime(Date firefightReleaseTime) {
		this.firefightReleaseTime = firefightReleaseTime;
	}
	/**
	 * 获取：消防证发证时间
	 */
	public Date getFirefightReleaseTime() {
		return firefightReleaseTime;
	}
	/**
	 * 设置：企业类型/经济性质
	 */
	public void setEconomicCode(String economicCode) {
		this.economicCode = economicCode;
	}
	/**
	 * 获取：企业类型/经济性质
	 */
	public String getEconomicCode() {
		return economicCode;
	}
	/**
	 * 设置：所属行业
	 */
	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}
	/**
	 * 获取：所属行业
	 */
	public String getIndustryCode() {
		return industryCode;
	}
	/**
	 * 设置：企业状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 获取：企业状态
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * 设置：企业信用等级
	 */
	public void setCreditLevel(String creditLevel) {
		this.creditLevel = creditLevel;
	}
	/**
	 * 获取：企业信用等级
	 */
	public String getCreditLevel() {
		return creditLevel;
	}
	/**
	 * 设置：企业所属范围
	 */
	public void setIndustryScope(String industryScope) {
		this.industryScope = industryScope;
	}
	/**
	 * 获取：企业所属范围
	 */
	public String getIndustryScope() {
		return industryScope;
	}
	/**
	 * 设置：企业规模
	 */
	public void setBusinessScale(String businessScale) {
		this.businessScale = businessScale;
	}
	/**
	 * 获取：企业规模
	 */
	public String getBusinessScale() {
		return businessScale;
	}
	/**
	 * 设置：从业人员数量
	 */
	public void setEmployeesCount(Integer employeesCount) {
		this.employeesCount = employeesCount;
	}
	/**
	 * 获取：从业人员数量
	 */
	public Integer getEmployeesCount() {
		return employeesCount;
	}
	/**
	 * 设置：下岗失业人员数量
	 */
	public void setLaidOffCount(Integer laidOffCount) {
		this.laidOffCount = laidOffCount;
	}
	/**
	 * 获取：下岗失业人员数量
	 */
	public Integer getLaidOffCount() {
		return laidOffCount;
	}
	/**
	 * 设置：实发工资总额
	 */
	public void setRealWages(Float realWages) {
		this.realWages = realWages;
	}
	/**
	 * 获取：实发工资总额
	 */
	public Float getRealWages() {
		return realWages;
	}
	/**
	 * 设置：月平均工资
	 */
	public void setAverageSalary(Float averageSalary) {
		this.averageSalary = averageSalary;
	}
	/**
	 * 获取：月平均工资
	 */
	public Float getAverageSalary() {
		return averageSalary;
	}
	/**
	 * 设置：参保人数
	 */
	public void setSocialInsuranceCount(Float socialInsuranceCount) {
		this.socialInsuranceCount = socialInsuranceCount;
	}
	/**
	 * 获取：参保人数
	 */
	public Float getSocialInsuranceCount() {
		return socialInsuranceCount;
	}
	/**
	 * 设置：创建用户ID
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：创建用户ID
	 */
	public String getCrtUserId() {
		return crtUserId;
	}
	/**
	 * 设置：创建人姓名
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
	/**
	 * 获取：创建人姓名
	 */
	public String getCrtUserName() {
		return crtUserName;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 设置：更新时间
	 */
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getUpdTime() {
		return updTime;
	}
	/**
	 * 设置：更新用户ID
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：更新用户ID
	 */
	public String getUpdUserId() {
		return updUserId;
	}
	/**
	 * 设置：更新用户姓名
	 */
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	/**
	 * 获取：更新用户姓名
	 */
	public String getUpdUserName() {
		return updUserName;
	}
	/**
	 * 设置：租户
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：租户
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * 设置：是否删除（1是/0否）
	 */
	public void setIdDeleted(String idDeleted) {
		this.idDeleted = idDeleted;
	}
	/**
	 * 获取：是否删除（1是/0否）
	 */
	public String getIdDeleted() {
		return idDeleted;
	}
	/**
	 * 设置：是否禁用（1是/0否）
	 */
	public void setIdDisabled(String idDisabled) {
		this.idDisabled = idDisabled;
	}
	/**
	 * 获取：是否禁用（1是/0否）
	 */
	public String getIdDisabled() {
		return idDisabled;
	}
}
