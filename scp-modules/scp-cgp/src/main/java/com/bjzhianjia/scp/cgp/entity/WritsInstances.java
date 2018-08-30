package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 文书模板实例
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Table(name = "cle_writs_instances")
public class WritsInstances implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //案件主键
    @Column(name = "case_id")
    private String caseId;
	
	    //流程任务标识
    @Column(name = "proc_task_id")
    private String procTaskId;
	
	    //文书模板主键
    @Column(name = "template_id")
    private Integer templateId;
	
	    //文书填充内容
    @Column(name = "fill_context")
    private String fillContext;
	
	    //文号单位简称
    @Column(name = "ref_unit_abbrev")
    private String refUnitAbbrev;
	
	    //文号执法种类
    @Column(name = "ref_enforce_type")
    private String refEnforceType;
	
	    //文号文书简称
    @Column(name = "ref_abbrev")
    private String refAbbrev;
	
	    //文号年份
    @Column(name = "ref_year")
    private String refYear;
	
	    //文号序号
    @Column(name = "ref_no")
    private String refNo;
	
	    //文书排序
    @Column(name = "iorder")
    private Integer iorder;
	
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
	 * 设置：案件主键
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/**
	 * 获取：案件主键
	 */
	public String getCaseId() {
		return caseId;
	}
	/**
	 * 设置：流程任务标识
	 */
	public void setProcTaskId(String procTaskId) {
		this.procTaskId = procTaskId;
	}
	/**
	 * 获取：流程任务标识
	 */
	public String getProcTaskId() {
		return procTaskId;
	}
	/**
	 * 设置：文书模板主键
	 */
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	/**
	 * 获取：文书模板主键
	 */
	public Integer getTemplateId() {
		return templateId;
	}
	/**
	 * 设置：文书填充内容
	 */
	public void setFillContext(String fillContext) {
		this.fillContext = fillContext;
	}
	/**
	 * 获取：文书填充内容
	 */
	public String getFillContext() {
		return fillContext;
	}
	/**
	 * 设置：文号单位简称
	 */
	public void setRefUnitAbbrev(String refUnitAbbrev) {
		this.refUnitAbbrev = refUnitAbbrev;
	}
	/**
	 * 获取：文号单位简称
	 */
	public String getRefUnitAbbrev() {
		return refUnitAbbrev;
	}
	/**
	 * 设置：文号执法种类
	 */
	public void setRefEnforceType(String refEnforceType) {
		this.refEnforceType = refEnforceType;
	}
	/**
	 * 获取：文号执法种类
	 */
	public String getRefEnforceType() {
		return refEnforceType;
	}
	/**
	 * 设置：文号文书简称
	 */
	public void setRefAbbrev(String refAbbrev) {
		this.refAbbrev = refAbbrev;
	}
	/**
	 * 获取：文号文书简称
	 */
	public String getRefAbbrev() {
		return refAbbrev;
	}
	/**
	 * 设置：文号年份
	 */
	public void setRefYear(String refYear) {
		this.refYear = refYear;
	}
	/**
	 * 获取：文号年份
	 */
	public String getRefYear() {
		return refYear;
	}
	/**
	 * 设置：文号序号
	 */
	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}
	/**
	 * 获取：文号序号
	 */
	public String getRefNo() {
		return refNo;
	}
	/**
	 * 设置：文书排序
	 */
	public void setIorder(Integer iorder) {
		this.iorder = iorder;
	}
	/**
	 * 获取：文书排序
	 */
	public Integer getIorder() {
		return iorder;
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
}
