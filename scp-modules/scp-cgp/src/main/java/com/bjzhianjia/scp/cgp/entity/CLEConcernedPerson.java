package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 综合执法 - 立案相关个人
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Table(name = "cle_concerned_person")
public class CLEConcernedPerson implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
	
	    //当事人姓名
    @Column(name = "name")
    private String name;
	
	    //当事人性别
    @Column(name = "gender")
    private String gender;
	
	    //当事人年龄
    @Column(name = "age")
    private Integer age;
	
	    //证件类型
    @Column(name = "cert_type")
    private String certType;
	
	    //证件号码
    @Column(name = "cert_code")
    private String certCode;
	
	    //住址
    @Column(name = "address")
    private String address;
	
	    //电话
    @Column(name = "phone")
    private String phone;
	
	    //是否删除(1:删除|0:未删除)
    @Column(name = "is_deleted")
    private String isDeleted;
	
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
	
	    //租户ID
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //部门ID
    @Column(name = "dept_id")
    private String deptId;
	

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
	 * 设置：当事人姓名
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：当事人姓名
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：当事人性别
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * 获取：当事人性别
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * 设置：当事人年龄
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 * 获取：当事人年龄
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * 设置：证件类型
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}
	/**
	 * 获取：证件类型
	 */
	public String getCertType() {
		return certType;
	}
	/**
	 * 设置：证件号码
	 */
	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}
	/**
	 * 获取：证件号码
	 */
	public String getCertCode() {
		return certCode;
	}
	/**
	 * 设置：住址
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * 获取：住址
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * 设置：电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：电话
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：是否删除(1:删除|0:未删除)
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：是否删除(1:删除|0:未删除)
	 */
	public String getIsDeleted() {
		return isDeleted;
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
	/**
	 * 设置：租户ID
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：租户ID
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * 设置：部门ID
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：部门ID
	 */
	public String getDeptId() {
		return deptId;
	}
}
