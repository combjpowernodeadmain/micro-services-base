package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.annonation.MergeField;

/**
 * 专项管理
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-07 17:40:06
 */
@Table(name = "special_event")
public class SpecialEvent implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	@Id
	private Integer id;

	// 专项编号
	@Column(name = "spe_code")
	private String speCode;

	// 专项名称
	@Column(name = "spe_name")
	private String speName;

	// 发布人
	@Column(name = "publisher")
	private String publisher;

	// 专项开始时间
	@Column(name = "spe_start_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date speStartDate;

	// 专项结束时间
	@Column(name = "spe_end_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date speEndDate;

	// 专项状态
	@Column(name = "spe_status")
	@MergeField(key="root_biz_specialType",feign=DictFeign.class,method="getByCode")
	private String speStatus;

	// 专项说明
	@Column(name = "spe_desc")
	private String speDesc;

	// 涉及业务条件
	@Column(name = "biz_list")
	private String bizList;

	// 涉及事件类别
	@Column(name = "event_type_list")
	private String eventTypeList;

	// 其它检查项
	@Column(name = "other_items")
	private String otherItems;

	// 涉及监管对象(监管对象ID组合，逗号隔开)
	@Column(name = "reg_obj_list")
	private String regObjList;

	// 其它监管对象
	@Column(name = "other_reg_obj")
	private String otherRegObj;

	// 是否删除 1：是 0：否
	@Column(name = "is_deleted")
	private String isDeleted;

	// 创建时间
	@Column(name = "crt_time")
	private Date crtTime;

	// 创建人ID
	@Column(name = "crt_user_id")
	private String crtUserId;

	// 创建人姓名
	@Column(name = "crt_user_name")
	private String crtUserName;

	// 更新时间
	@Column(name = "upd_time")
	private Date updTime;

	// 更新人ID
	@Column(name = "upd_user_id")
	private String updUserId;

	// 更新用户姓名
	@Column(name = "upd_user_name")
	private String updUserName;

	// 租户
	@Column(name = "tenant_id")
	private String tenantId;

	//
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
	 * 设置：专项编号
	 */
	public void setSpeCode(String speCode) {
		this.speCode = speCode;
	}

	/**
	 * 获取：专项编号
	 */
	public String getSpeCode() {
		return speCode;
	}

	/**
	 * 设置：专项名称
	 */
	public void setSpeName(String speName) {
		this.speName = speName;
	}

	/**
	 * 获取：专项名称
	 */
	public String getSpeName() {
		return speName;
	}

	/**
	 * 设置：发布人
	 */
	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	/**
	 * 获取：发布人
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * 设置：专项开始时间
	 */
	public void setSpeStartDate(Date speStartDate) {
		this.speStartDate = speStartDate;
	}

	/**
	 * 获取：专项开始时间
	 */
	public Date getSpeStartDate() {
		return speStartDate;
	}

	/**
	 * 设置：专项结束时间
	 */
	public void setSpeEndDate(Date speEndDate) {
		this.speEndDate = speEndDate;
	}

	/**
	 * 获取：专项结束时间
	 */
	public Date getSpeEndDate() {
		return speEndDate;
	}

	/**
	 * 设置：专项状态(0:未启动|1:进行中|2:已终止|3:已完成|)
	 */
	public void setSpeStatus(String speStatus) {
		this.speStatus = speStatus;
	}

	/**
	 * 获取：专项状态(0:未启动|1:进行中|2:已终止|3:已完成|)
	 */
	public String getSpeStatus() {
		return speStatus;
	}

	/**
	 * 设置：专项说明
	 */
	public void setSpeDesc(String speDesc) {
		this.speDesc = speDesc;
	}

	/**
	 * 获取：专项说明
	 */
	public String getSpeDesc() {
		return speDesc;
	}

	/**
	 * 设置：涉及业务条件
	 */
	public void setBizList(String bizList) {
		this.bizList = bizList;
	}

	/**
	 * 获取：涉及业务条件
	 */
	public String getBizList() {
		return bizList;
	}

	/**
	 * 设置：涉及事件类别
	 */
	public void setEventTypeList(String eventTypeList) {
		this.eventTypeList = eventTypeList;
	}

	/**
	 * 获取：涉及事件类别
	 */
	public String getEventTypeList() {
		return eventTypeList;
	}

	/**
	 * 设置：其它检查项
	 */
	public void setOtherItems(String otherItems) {
		this.otherItems = otherItems;
	}

	/**
	 * 获取：其它检查项
	 */
	public String getOtherItems() {
		return otherItems;
	}

	/**
	 * 设置：涉及监管对象(监管对象ID组合，逗号隔开)
	 */
	public void setRegObjList(String regObjList) {
		this.regObjList = regObjList;
	}

	/**
	 * 获取：涉及监管对象(监管对象ID组合，逗号隔开)
	 */
	public String getRegObjList() {
		return regObjList;
	}

	/**
	 * 设置：其它监管对象
	 */
	public void setOtherRegObj(String otherRegObj) {
		this.otherRegObj = otherRegObj;
	}

	/**
	 * 获取：其它监管对象
	 */
	public String getOtherRegObj() {
		return otherRegObj;
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
	 * 设置：
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取：
	 */
	public String getDeptId() {
		return deptId;
	}
}
