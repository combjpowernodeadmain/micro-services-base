package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 个体商户
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
@Table(name = "self_merchants")
public class SelfMerchants implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //社会信用代码
    @Column(name = "credit_code")
    private String creditCode;
	
	    //业主身份证号码
    @Column(name = "owner_credit_code")
    private String ownerCreditCode;
	
	    //注册地址
    @Column(name = "regist_address")
    private String registAddress;
	
	    //实际负责人姓名
    @Column(name = "responser_user_name")
    private String responserUserName;
	
	    //实际负责人手机号码
    @Column(name = "responser_user_phone")
    private String responserUserPhone;
	
	    //经营面积
    @Column(name = "business_area")
    private Float businessArea;
	
	    //仓储面积
    @Column(name = "storage_area")
    private Float storageArea;
	
	    //主要设备名称及台（套）数
    @Column(name = "major_equipment_info")
    private String majorEquipmentInfo;
	
	    //辅助设备名称及台（套）数
    @Column(name = "auxiliary_equipment_info")
    private String auxiliaryEquipmentInfo;
	
	    //年房屋及设备租金
    @Column(name = "annual_rent")
    private Float annualRent;
	
	    //是否租赁经营 1:是 0：否
    @Column(name = "is_rented")
    private String isRented;
	
	    //经营范围及方式
    @Column(name = "business_scope")
    private String businessScope;
	
	    //从业人员数量
    @Column(name = "employees_count")
    private Integer employeesCount;
	
	    //所属集贸市场
    @Column(name = "affiliated_market")
    private String affiliatedMarket;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //创建用户ID
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建用户姓名
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //更新时间
    @Column(name = "upd_time")
    private Date updTime;
	
	    //更新用户id
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //更新用户姓名
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //租户
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //是否删除 1：是 0：否
    @Column(name = "is_deleted")
    private String isDeleted;
	
	    //是否禁用 1：是 0：否
    @Column(name = "is_disabled")
    private String isDisabled;
	

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
	 * 设置：社会信用代码
	 */
	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}
	/**
	 * 获取：社会信用代码
	 */
	public String getCreditCode() {
		return creditCode;
	}
	/**
	 * 设置：业主身份证号码
	 */
	public void setOwnerCreditCode(String ownerCreditCode) {
		this.ownerCreditCode = ownerCreditCode;
	}
	/**
	 * 获取：业主身份证号码
	 */
	public String getOwnerCreditCode() {
		return ownerCreditCode;
	}
	/**
	 * 设置：注册地址
	 */
	public void setRegistAddress(String registAddress) {
		this.registAddress = registAddress;
	}
	/**
	 * 获取：注册地址
	 */
	public String getRegistAddress() {
		return registAddress;
	}
	/**
	 * 设置：实际负责人姓名
	 */
	public void setResponserUserName(String responserUserName) {
		this.responserUserName = responserUserName;
	}
	/**
	 * 获取：实际负责人姓名
	 */
	public String getResponserUserName() {
		return responserUserName;
	}
	/**
	 * 设置：实际负责人手机号码
	 */
	public void setResponserUserPhone(String responserUserPhone) {
		this.responserUserPhone = responserUserPhone;
	}
	/**
	 * 获取：实际负责人手机号码
	 */
	public String getResponserUserPhone() {
		return responserUserPhone;
	}
	/**
	 * 设置：经营面积
	 */
	public void setBusinessArea(Float businessArea) {
		this.businessArea = businessArea;
	}
	/**
	 * 获取：经营面积
	 */
	public Float getBusinessArea() {
		return businessArea;
	}
	/**
	 * 设置：仓储面积
	 */
	public void setStorageArea(Float storageArea) {
		this.storageArea = storageArea;
	}
	/**
	 * 获取：仓储面积
	 */
	public Float getStorageArea() {
		return storageArea;
	}
	/**
	 * 设置：主要设备名称及台（套）数
	 */
	public void setMajorEquipmentInfo(String majorEquipmentInfo) {
		this.majorEquipmentInfo = majorEquipmentInfo;
	}
	/**
	 * 获取：主要设备名称及台（套）数
	 */
	public String getMajorEquipmentInfo() {
		return majorEquipmentInfo;
	}
	/**
	 * 设置：辅助设备名称及台（套）数
	 */
	public void setAuxiliaryEquipmentInfo(String auxiliaryEquipmentInfo) {
		this.auxiliaryEquipmentInfo = auxiliaryEquipmentInfo;
	}
	/**
	 * 获取：辅助设备名称及台（套）数
	 */
	public String getAuxiliaryEquipmentInfo() {
		return auxiliaryEquipmentInfo;
	}
	/**
	 * 设置：年房屋及设备租金
	 */
	public void setAnnualRent(Float annualRent) {
		this.annualRent = annualRent;
	}
	/**
	 * 获取：年房屋及设备租金
	 */
	public Float getAnnualRent() {
		return annualRent;
	}
	/**
	 * 设置：是否租赁经营 1:是 0：否
	 */
	public void setIsRented(String isRented) {
		this.isRented = isRented;
	}
	/**
	 * 获取：是否租赁经营 1:是 0：否
	 */
	public String getIsRented() {
		return isRented;
	}
	/**
	 * 设置：经营范围及方式
	 */
	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	/**
	 * 获取：经营范围及方式
	 */
	public String getBusinessScope() {
		return businessScope;
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
	 * 设置：所属集贸市场
	 */
	public void setAffiliatedMarket(String affiliatedMarket) {
		this.affiliatedMarket = affiliatedMarket;
	}
	/**
	 * 获取：所属集贸市场
	 */
	public String getAffiliatedMarket() {
		return affiliatedMarket;
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
	 * 设置：创建用户姓名
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
	/**
	 * 获取：创建用户姓名
	 */
	public String getCrtUserName() {
		return crtUserName;
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
	 * 设置：更新用户id
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：更新用户id
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
	 * 设置：是否删除 1：是 0：否
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：是否删除 1：是 0：否
	 */
	public String getIsDeleted() {
		return isDeleted;
	}
	/**
	 * 设置：是否禁用 1：是 0：否
	 */
	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}
	/**
	 * 获取：是否禁用 1：是 0：否
	 */
	public String getIsDisabled() {
		return isDisabled;
	}
}
