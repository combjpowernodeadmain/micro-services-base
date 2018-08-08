package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 记录来自市长热线的事件
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-09 00:06:36
 */
@Table(name = "mayor_hotline")
public class MayorHotline implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //热线事项编号
    @Column(name = "hotln_code")
    private String hotlnCode;
	
	    //热线事项标题
    @Column(name = "hotln_title")
    private String hotlnTitle;
	
	    //热线类型
    @Column(name = "hotln__type")
    private String hotlnType;
	
	    //热线子类
    @Column(name = "hotln_sub_type")
    private String hotlnSubType;
	
	    //诉求人电话
    @Column(name = "appeal_tel")
    private String appealTel;
	
	    //诉求人姓名
    @Column(name = "appeal_person")
    private String appealPerson;
	
	    //诉求时间
    @Column(name = "appeal_datetime")
    private Date appealDatetime;
	
	    //诉求内容
    @Column(name = "appeal_desc")
    private String appealDesc;
	
	    //反馈时间
    @Column(name = "reply_datetime")
    private Date replyDatetime;
	
	    //处理状态
    @Column(name = "exe_status")
    private String exeStatus;
	
	    //是否删除(1:删除|0:未删除)
    @Column(name = "is_deleted")
    private String isDeleted;
	
	    //创建时间(上报时间)
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //创建人ID(上报人)
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
	
	    //租房
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
	 * 设置：热线事项编号
	 */
	public void setHotlnCode(String hotlnCode) {
		this.hotlnCode = hotlnCode;
	}
	/**
	 * 获取：热线事项编号
	 */
	public String getHotlnCode() {
		return hotlnCode;
	}
	/**
	 * 设置：热线事项标题
	 */
	public void setHotlnTitle(String hotlnTitle) {
		this.hotlnTitle = hotlnTitle;
	}
	/**
	 * 获取：热线事项标题
	 */
	public String getHotlnTitle() {
		return hotlnTitle;
	}
	/**
	 * 设置：热线类型
	 */
	public void setHotlnType(String hotlnType) {
		this.hotlnType = hotlnType;
	}
	/**
	 * 获取：热线类型
	 */
	public String getHotlnType() {
		return hotlnType;
	}
	/**
	 * 设置：热线子类
	 */
	public void setHotlnSubType(String hotlnSubType) {
		this.hotlnSubType = hotlnSubType;
	}
	/**
	 * 获取：热线子类
	 */
	public String getHotlnSubType() {
		return hotlnSubType;
	}
	/**
	 * 设置：诉求人电话
	 */
	public void setAppealTel(String appealTel) {
		this.appealTel = appealTel;
	}
	/**
	 * 获取：诉求人电话
	 */
	public String getAppealTel() {
		return appealTel;
	}
	/**
	 * 设置：诉求人姓名
	 */
	public void setAppealPerson(String appealPerson) {
		this.appealPerson = appealPerson;
	}
	/**
	 * 获取：诉求人姓名
	 */
	public String getAppealPerson() {
		return appealPerson;
	}
	/**
	 * 设置：诉求时间
	 */
	public void setAppealDatetime(Date appealDatetime) {
		this.appealDatetime = appealDatetime;
	}
	/**
	 * 获取：诉求时间
	 */
	public Date getAppealDatetime() {
		return appealDatetime;
	}
	/**
	 * 设置：诉求内容
	 */
	public void setAppealDesc(String appealDesc) {
		this.appealDesc = appealDesc;
	}
	/**
	 * 获取：诉求内容
	 */
	public String getAppealDesc() {
		return appealDesc;
	}
	/**
	 * 设置：反馈时间
	 */
	public void setReplyDatetime(Date replyDatetime) {
		this.replyDatetime = replyDatetime;
	}
	/**
	 * 获取：反馈时间
	 */
	public Date getReplyDatetime() {
		return replyDatetime;
	}
	/**
	 * 设置：处理状态
	 */
	public void setExeStatus(String exeStatus) {
		this.exeStatus = exeStatus;
	}
	/**
	 * 获取：处理状态
	 */
	public String getExeStatus() {
		return exeStatus;
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
	 * 设置：创建时间(上报时间)
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：创建时间(上报时间)
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 设置：创建人ID(上报人)
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：创建人ID(上报人)
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
	 * 设置：租房
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：租房
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
