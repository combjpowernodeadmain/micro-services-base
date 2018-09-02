package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * 执法任务管理
 * 
 * @author chenshuai
 * @email
 * @version 2018-08-30 13:52:18
 */
@Table(name = "law_task")
public class LawTask implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 执法任务状态
	 */
	public static final String ROOT_BIZ_LAWTASKS = "root_biz_lawTaskS";
	/**
	 * 已完成
	 */
	public static final String ROOT_BIZ_LAWTASKS_FINISH = "root_biz_lawTaskS_finish";
	
	/**
	 *  未发起
	 */
	public static final String ROOT_BIZ_LAWTASKS_TODO = "root_biz_lawTaskS_todo";
	/**
	 * 处理中
	 */
	public static final String ROOT_BIZ_LAWTASKS_DOING = "root_biz_lawTaskS_doing";
	/**
	 * 已终止
	 */
	public static final String ROOT_BIZ_LAWTASKS_STOP = "root_biz_lawTaskS_stop";
	
	//
	@Id
	@GeneratedValue(generator="JDBC")
	private Integer id;

	// 任务编码
	@Column(name = "law_task_code")
	private String lawTaskCode;

	// 任务开始日期
	@NotNull
	@Column(name = "start_time")
	private Date startTime;

	// 任务结束日期
	@NotNull
	@Column(name = "end_time")
	private Date endTime;

	// 任务状态（数据字典）
	@NotNull
	@Column(name = "state")
	private String state;

	// 任务要求
	@NotNull
	@Column(name = "info")
	private String info;

	// 业务线条（数据字典code）
	@Column(name = "biz_type_code")
	private String bizTypeCode;

	// 事件类别
	@NotNull
	@Column(name = "event_type_id")
	private String eventTypeId;

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

	// 更新人姓名
	@Column(name = "upd_user_name")
	private String updUserName;

	// 租户id
	@Column(name = "tenant_id")
	private String tenantId;

	// 是否删除(1:删除|0:未删除)
	@Column(name = "is_deleted")
	private String isDeleted;

	// 执法队员
	private String executePerson;

	// 巡查对象（企业）
	private String patrolObject;
	
	/**
	 * 设置：执法队员
	 */
	public void setExecutePerson(String executePerson) {
		this.executePerson = executePerson;
	}

	/**
	 * 获取：执法队员
	 */
	public String getExecutePerson() {
		return executePerson;
	}

	/**
	 * 设置：巡查对象（企业）
	 */
	public void setPatrolObject(String patrolObject) {
		this.patrolObject = patrolObject;
	}

	/**
	 * 获取：巡查对象（企业）
	 */
	public String getPatrolObject() {
		return patrolObject;
	}
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
	 * 设置：任务编码
	 */
	public void setLawTaskCode(String lawTaskCode) {
		this.lawTaskCode = lawTaskCode;
	}

	/**
	 * 获取：任务编码
	 */
	public String getLawTaskCode() {
		return lawTaskCode;
	}

	/**
	 * 设置：任务开始日期
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	/**
	 * 获取：任务开始日期
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * 设置：任务结束日期
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	/**
	 * 获取：任务结束日期
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * 设置：任务状态（数据字典）
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 获取：任务状态（数据字典）
	 */
	public String getState() {
		return state;
	}

	/**
	 * 设置：任务要求
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * 获取：任务要求
	 */
	public String getInfo() {
		return info;
	}

	/**
	 * 设置：业务线条（数据字典code）
	 */
	public void setBizTypeCode(String bizTypeCode) {
		this.bizTypeCode = bizTypeCode;
	}

	/**
	 * 获取：业务线条（数据字典code）
	 */
	public String getBizTypeCode() {
		return bizTypeCode;
	}

	/**
	 * 设置：事件类别
	 */
	public void setEventTypeId(String eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	/**
	 * 获取：事件类别
	 */
	public String getEventTypeId() {
		return eventTypeId;
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
	 * 设置：租户id
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	/**
	 * 获取：租户id
	 */
	public String getTenantId() {
		return tenantId;
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
	

}
