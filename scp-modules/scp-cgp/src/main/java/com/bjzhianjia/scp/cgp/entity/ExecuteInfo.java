package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 案件办理情况
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-09 00:06:36
 */
@Table(name = "execute_info")
public class ExecuteInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //办理人
    @Column(name = "exe_person")
    private String exePerson;
	
	    //办理部门
    @Column(name = "department")
    private String department;
	
	    //办结时间
    @Column(name = "finish_time")
    private Date finishTime;
	
	    //现场照片
    @Column(name = "picture")
    private String picture;
	
	    //办理情况说明
    @Column(name = "exe_desc")
    private String exeDesc;
	
	    //办理案件Id
    @Column(name = "case_id")
    private Integer caseId;
	

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
	 * 设置：办理人
	 */
	public void setExePerson(String exePerson) {
		this.exePerson = exePerson;
	}
	/**
	 * 获取：办理人
	 */
	public String getExePerson() {
		return exePerson;
	}
	/**
	 * 设置：办理部门
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * 获取：办理部门
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * 设置：办结时间
	 */
	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}
	/**
	 * 获取：办结时间
	 */
	public Date getFinishTime() {
		return finishTime;
	}
	/**
	 * 设置：现场照片
	 */
	public void setPicture(String picture) {
		this.picture = picture;
	}
	/**
	 * 获取：现场照片
	 */
	public String getPicture() {
		return picture;
	}
	/**
	 * 设置：办理情况说明
	 */
	public void setExeDesc(String exeDesc) {
		this.exeDesc = exeDesc;
	}
	/**
	 * 获取：办理情况说明
	 */
	public String getExeDesc() {
		return exeDesc;
	}
	/**
	 * 设置：办理案件Id
	 */
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	/**
	 * 获取：办理案件Id
	 */
	public Integer getCaseId() {
		return caseId;
	}
}
