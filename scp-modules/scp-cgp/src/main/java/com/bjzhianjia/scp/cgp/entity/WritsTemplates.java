package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 文书模板
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Table(name = "cle_writs_templates")
public class WritsTemplates implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //模板名称
    @Column(name = "name")
    private String name;
	
	    //文书文档路径
    @Column(name = "doc_url")
    private String docUrl;
	
	    //文书表单code
    @Column(name = "form_code")
    private String formCode;
	
	    //所属类型
    @Column(name = "type_code")
    private String typeCode;
	
	    //是否必填
    @Column(name = "is_filled")
    private String isFilled;
	
	    //流程模板变量
    @Column(name = "template_vars")
    private String templateVars;
	
	    //需要审批等级数
    @Column(name = "approval_rating")
    private Integer approvalRating;
	
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
	
	    //租户
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
	 * 设置：模板名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：模板名称
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：文书文档路径
	 */
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
	/**
	 * 获取：文书文档路径
	 */
	public String getDocUrl() {
		return docUrl;
	}
	/**
	 * 设置：文书表单code
	 */
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	/**
	 * 获取：文书表单code
	 */
	public String getFormCode() {
		return formCode;
	}
	/**
	 * 设置：所属类型
	 */
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	/**
	 * 获取：所属类型
	 */
	public String getTypeCode() {
		return typeCode;
	}
	/**
	 * 设置：是否必填
	 */
	public void setIsFilled(String isFilled) {
		this.isFilled = isFilled;
	}
	/**
	 * 获取：是否必填
	 */
	public String getIsFilled() {
		return isFilled;
	}
	/**
	 * 设置：流程模板变量
	 */
	public void setTemplateVars(String templateVars) {
		this.templateVars = templateVars;
	}
	/**
	 * 获取：流程模板变量
	 */
	public String getTemplateVars() {
		return templateVars;
	}
	/**
	 * 设置：需要审批等级数
	 */
	public void setApprovalRating(Integer approvalRating) {
		this.approvalRating = approvalRating;
	}
	/**
	 * 获取：需要审批等级数
	 */
	public Integer getApprovalRating() {
		return approvalRating;
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
}
