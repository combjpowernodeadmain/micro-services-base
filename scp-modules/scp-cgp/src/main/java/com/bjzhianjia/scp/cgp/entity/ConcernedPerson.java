package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 立案信息当事人
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-16 19:16:02
 */
@Table(name = "concerned_person")
public class ConcernedPerson implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;
	
	    //当事人姓名
    @Column(name = "name")
    private String name;
	
	    //当事人电话
    @Column(name = "phone")
    private String phone;
	
	    //当事人证件类型(字典)
    @Column(name = "cred_type")
    private String credType;
    
	    //当事人证件号码
    @Column(name = "cred_code")
    private String credCode;
	
	    //当事人所在单位名称
    @Column(name = "company")
    private String company;
	
	    //当事人单位地址
    @Column(name = "company_addr")
    private String companyAddr;
    
    	//后加字段
    @Column(name="case_id")
    private Integer caseId;
    
    //用户年龄
    @Column(name = "age")
    private Integer age;
    
    //用户年龄
    @Column(name = "sex")
    private String sex;

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
	 * 设置：当事人电话
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 获取：当事人电话
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * 设置：当事人证件类型(字典)
	 */
	public void setCredType(String credType) {
		this.credType = credType;
	}
	/**
	 * 获取：当事人证件类型(字典)
	 */
	public String getCredType() {
		return credType;
	}
	/**
	 * 设置：当事人证件号码
	 */
	public void setCredCode(String credCode) {
		this.credCode = credCode;
	}
	/**
	 * 获取：当事人证件号码
	 */
	public String getCredCode() {
		return credCode;
	}
	/**
	 * 设置：当事人所在单位名称
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * 获取：当事人所在单位名称
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * 设置：当事人单位地址
	 */
	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}
	/**
	 * 获取：当事人单位地址
	 */
	public String getCompanyAddr() {
		return companyAddr;
	}
	/**
	 * 获取：涉及案件ID
	 */
	public Integer getCaseId() {
		return caseId;
	}
	/**
	 * 设置：涉及案件ID
	 */
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	/**
	 *  获取：年龄
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * 设置：年龄
	 */
	public void setAge(Integer age) {
		this.age = age;
	}
	/**
	 *  获取：性别
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * 设置：性别
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
