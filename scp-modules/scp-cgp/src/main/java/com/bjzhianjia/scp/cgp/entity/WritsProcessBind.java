package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 文书与流程绑定
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Table(name = "cle_writs_process_bind")
public class WritsProcessBind implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //流程定义主键
    @Column(name = "process_def_id")
    private String processDefId;
	
	    //流程任务id
    @Column(name = "process_node_id")
    private String processNodeId;
	
	    //文书主键
    @Column(name = "writs_id")
    private Integer writsId;
	
	    //第几级审批等级
    @Column(name = "approval_rating")
    private Integer approvalRating;
	
	    //是否为默认展现(1: 是 0：否)
    @Column(name = "is_default")
    private String isDefault;
	
	    //是否必填
    @Column(name = "is_required")
    private String isRequired;
	
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
	
	    //租房
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
	 * 设置：流程定义主键
	 */
	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}
	/**
	 * 获取：流程定义主键
	 */
	public String getProcessDefId() {
		return processDefId;
	}
	/**
	 * 设置：流程任务id
	 */
	public void setProcessNodeId(String processNodeId) {
		this.processNodeId = processNodeId;
	}
	/**
	 * 获取：流程任务id
	 */
	public String getProcessNodeId() {
		return processNodeId;
	}
	/**
	 * 设置：文书主键
	 */
	public void setWritsId(Integer writsId) {
		this.writsId = writsId;
	}
	/**
	 * 获取：文书主键
	 */
	public Integer getWritsId() {
		return writsId;
	}
	/**
	 * 设置：第几级审批等级
	 */
	public void setApprovalRating(Integer approvalRating) {
		this.approvalRating = approvalRating;
	}
	/**
	 * 获取：第几级审批等级
	 */
	public Integer getApprovalRating() {
		return approvalRating;
	}
	/**
	 * 设置：是否为默认展现(1: 是 0：否)
	 */
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	/**
	 * 获取：是否为默认展现(1: 是 0：否)
	 */
	public String getIsDefault() {
		return isDefault;
	}
	/**
	 * 设置：是否必填
	 */
	public void setIsRequired(String isRequired) {
		this.isRequired = isRequired;
	}
	/**
	 * 获取：是否必填
	 */
	public String getIsRequired() {
		return isRequired;
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
}
