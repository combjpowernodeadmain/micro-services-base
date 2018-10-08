package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-09-30 15:24:19
 */
@Table(name = "message_center")
public class MessageCenter implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 事件待办类型
	 */
	public static final String CASE_INFO_TYPE="case_info_type";
	/**
	 * 案件待办类型
	 */
	public static final String CASE_REGISTRATION_TYPE="case_registration_type";
	/**
	 * 执法任务
	 */
	public static final String LAW_TASK="law_task";
	
	    //主键
    @Id
    private Integer id;
	
	    //消息来源ID
    @Column(name = "msg_source_id")
    private String msgSourceId;
	
	    //消息来源类型
    @Column(name = "msg_source_type")
    private String msgSourceType;
	
	    //消息名称
    @Column(name = "msg_name")
    private String msgName;
	
	    //消息内容
    @Column(name = "msg_desc")
    private String msgDesc;
	
	    //任务时间(指消息来源的发生时间，用于决定在消息中的排列顺序)
    @Column(name = "task_time")
    private Date taskTime;
	
	    //是否已读(0:未读|1:已读)
    @Column(name = "is_read")
    private String isRead;
	
	    //是否删除(0:未删除|1:已删除)
    @Column(name = "is_deleted")
    private String isDeleted;
	
	    //创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //最后更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //最后更新人姓名
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //最后更新时间
    @Column(name = "upd_time")
    private Date updTime;
	
	    //租户ID
    @Column(name = "tenant_id")
    private String tenantId;
	

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
	 * 设置：消息来源ID
	 */
	public void setMsgSourceId(String msgSourceId) {
		this.msgSourceId = msgSourceId;
	}
	/**
	 * 获取：消息来源ID
	 */
	public String getMsgSourceId() {
		return msgSourceId;
	}
	/**
	 * 设置：消息来源类型
	 */
	public void setMsgSourceType(String msgSourceType) {
		this.msgSourceType = msgSourceType;
	}
	/**
	 * 获取：消息来源类型
	 */
	public String getMsgSourceType() {
		return msgSourceType;
	}
	/**
	 * 设置：消息名称
	 */
	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}
	/**
	 * 获取：消息名称
	 */
	public String getMsgName() {
		return msgName;
	}
	/**
	 * 设置：消息内容
	 */
	public void setMsgDesc(String msgDesc) {
		this.msgDesc = msgDesc;
	}
	/**
	 * 获取：消息内容
	 */
	public String getMsgDesc() {
		return msgDesc;
	}
	/**
	 * 设置：任务时间(指消息来源的发生时间，用于决定在消息中的排列顺序)
	 */
	public void setTaskTime(Date taskTime) {
		this.taskTime = taskTime;
	}
	/**
	 * 获取：任务时间(指消息来源的发生时间，用于决定在消息中的排列顺序)
	 */
	public Date getTaskTime() {
		return taskTime;
	}
	/**
	 * 设置：是否已读(0:未读|1:已读)
	 */
	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}
	/**
	 * 获取：是否已读(0:未读|1:已读)
	 */
	public String getIsRead() {
		return isRead;
	}
	/**
	 * 设置：是否删除(0:未删除|1:已删除)
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：是否删除(0:未删除|1:已删除)
	 */
	public String getIsDeleted() {
		return isDeleted;
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
	 * 设置：最后更新人ID
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：最后更新人ID
	 */
	public String getUpdUserId() {
		return updUserId;
	}
	/**
	 * 设置：最后更新人姓名
	 */
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	/**
	 * 获取：最后更新人姓名
	 */
	public String getUpdUserName() {
		return updUserName;
	}
	/**
	 * 设置：最后更新时间
	 */
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}
	/**
	 * 获取：最后更新时间
	 */
	public Date getUpdTime() {
		return updTime;
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
}
