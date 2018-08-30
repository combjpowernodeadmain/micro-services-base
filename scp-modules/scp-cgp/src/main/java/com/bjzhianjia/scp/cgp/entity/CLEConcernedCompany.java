package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 综合执法 - 当事人（企业）表
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Table(name = "cle_concerned_company")
public class CLEConcernedCompany implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
	
	    //单位名称
    @Column(name = "name")
    private String name;
	
	    //单位地址
    @Column(name = "address")
    private String address;
	
	    //法定代表人
    @Column(name = "legal_person")
    private String legalPerson;
	
	    //负责人
    @Column(name = "lead_person")
    private String leadPerson;
	
	    //职务
    @Column(name = "duties")
    private String duties;
	
	    //联系电话
    @Column(name = "phone")
    private String phone;
	
	    //公司简介
    @Column(name = "info")
    private String info;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //更新时间
    @Column(name = "upd_time")
    private Date updTime;
	
	    //更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //更新人姓名
    @Column(name = "upd_user_name")
    private String updUserName;
	

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：单位名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：单位名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：单位地址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：单位地址
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：法定代表人
	 */
	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}
	/**
	 * 获取：法定代表人
	 */
	public String getLegalPerson() {
		return legalPerson;
	}
	/**
	 * 设置：负责人
	 */
	public void setLeadPerson(String leadPerson) {
		this.leadPerson = leadPerson;
	}
	/**
	 * 获取：负责人
	 */
	public String getLeadPerson() {
		return leadPerson;
	}
	/**
	 * 设置：职务
	 */
	public void setDuties(String duties) {
		this.duties = duties;
	}
	/**
	 * 获取：职务
	 */
	public String getDuties() {
		return duties;
	}
	/**
	 * 设置：联系电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：联系电话
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：公司简介
	 */
	public void setInfo(String info) {
		this.info = info;
	}
	/**
	 * 获取：公司简介
	 */
	public String getInfo() {
		return info;
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
	 * 设置：创建人ID
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：创建人ID
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
	 * 设置：更新人ID
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：更新人ID
	 */
	public String getUpdUserId() {
		return updUserId;
	}
	/**
	 * 设置：更新人姓名
	 */
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	/**
	 * 获取：更新人姓名
	 */
	public String getUpdUserName() {
		return updUserName;
	}
}
