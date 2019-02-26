package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2019-02-26 21:39:06
 */
@Table(name = "staff_info_collection")
public class StaffInfoCollection implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //员工名称
    @Column(name = "staff_name")
    private String staffName;

    @Column(name = "sex")
    private String sex;
	
	    //本人手机号
    @Column(name = "mibile_phone")
    private String mibilePhone;
	
	    //工作终端手机号
    @Column(name = "terminal_phone")
    private String terminalPhone;
	
	    //所属部门
    @Column(name = "depart_id")
    private String departId;
	
	    //手机标识码
    @Column(name = "phone_code")
    private String phoneCode;
	
	    //
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //
    @Column(name = "upd_time")
    private Date updTime;
	
	    //
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //
    @Column(name = "is_deleted")
    private String isDeleted;
	

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
	 * 设置：员工名称
	 */
	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
	/**
	 * 获取：员工名称
	 */
	public String getStaffName() {
		return staffName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 设置：本人手机号
	 */
	public void setMibilePhone(String mibilePhone) {
		this.mibilePhone = mibilePhone;
	}
	/**
	 * 获取：本人手机号
	 */
	public String getMibilePhone() {
		return mibilePhone;
	}
	/**
	 * 设置：工作终端手机号
	 */
	public void setTerminalPhone(String terminalPhone) {
		this.terminalPhone = terminalPhone;
	}
	/**
	 * 获取：工作终端手机号
	 */
	public String getTerminalPhone() {
		return terminalPhone;
	}
	/**
	 * 设置：所属部门
	 */
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	/**
	 * 获取：所属部门
	 */
	public String getDepartId() {
		return departId;
	}
	/**
	 * 设置：手机标识码
	 */
	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}
	/**
	 * 获取：手机标识码
	 */
	public String getPhoneCode() {
		return phoneCode;
	}
	/**
	 * 设置：
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：
	 */
	public String getCrtUserId() {
		return crtUserId;
	}
	/**
	 * 设置：
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 设置：
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
	/**
	 * 获取：
	 */
	public String getCrtUserName() {
		return crtUserName;
	}
	/**
	 * 设置：
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：
	 */
	public String getUpdUserId() {
		return updUserId;
	}
	/**
	 * 设置：
	 */
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	/**
	 * 获取：
	 */
	public String getUpdUserName() {
		return updUserName;
	}
	/**
	 * 设置：
	 */
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdTime() {
		return updTime;
	}
	/**
	 * 设置：
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * 设置：
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：
	 */
	public String getIsDeleted() {
		return isDeleted;
	}
}
