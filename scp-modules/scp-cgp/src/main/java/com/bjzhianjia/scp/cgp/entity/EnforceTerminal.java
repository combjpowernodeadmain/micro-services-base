package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.bjzhianjia.scp.merge.annonation.MergeField;
import com.bjzhianjia.scp.cgp.feign.DictFeign;


/**
 * 执法终端
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
@Table(name = "enforce_terminal")
public class EnforceTerminal implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //终端标识号
    @Column(name = "terminal_code")
    @NotEmpty(message="终端标识号不能为空")
    @Length(max=127,message="终端手机号长度不能超过127")
    private String terminalCode;
	
	    //终端手机号
    @Column(name = "terminal_phone")
    @Length(max=15,message="终端手机号长度不能超过15")
    private String terminalPhone;
	
	    //领用部门
    @Column(name = "retrieval_department")
    private String retrievalDepartment;
	
	    //领用人
    @Column(name = "retrieval_user")
    private String retrievalUser;
	
	    //属配类型
    @Column(name = "terminal_type")
    @MergeField(key = "root_biz_trml_t", feign = DictFeign.class, method = "getByCode")
    @NotEmpty(message="属配类型不能为空")
    private String terminalType;
	
	    //创建用户Id
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建用户姓名
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //更新时间
    @Column(name = "upd_time")
    private Date updTime;
	
	    //更新用户ID
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //更新用户姓名
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //租户
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //是否删除（1是/0否）
    @Column(name = "is_deleted")
    private String isDeleted;
    
    @Column(name = "is_enable")
    @MergeField(key = "root_biz_enabled", feign = DictFeign.class, method = "getByCode")
    @NotEmpty(message="是否可用不能为空")
    private String isEnable;

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
	 * 设置：终端标识号
	 */
	public void setTerminalCode(String terminalCode) {
		this.terminalCode = terminalCode;
	}
	/**
	 * 获取：终端标识号
	 */
	public String getTerminalCode() {
		return terminalCode;
	}
	/**
	 * 设置：终端手机号
	 */
	public void setTerminalPhone(String terminalPhone) {
		this.terminalPhone = terminalPhone;
	}
	/**
	 * 获取：终端手机号
	 */
	public String getTerminalPhone() {
		return terminalPhone;
	}
	/**
	 * 设置：领用部门
	 */
	public void setRetrievalDepartment(String retrievalDepartment) {
		this.retrievalDepartment = retrievalDepartment;
	}
	/**
	 * 获取：领用部门
	 */
	public String getRetrievalDepartment() {
		return retrievalDepartment;
	}
	/**
	 * 设置：领用人
	 */
	public void setRetrievalUser(String retrievalUser) {
		this.retrievalUser = retrievalUser;
	}
	/**
	 * 获取：领用人
	 */
	public String getRetrievalUser() {
		return retrievalUser;
	}
	/**
	 * 设置：属配类型
	 */
	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}
	/**
	 * 获取：属配类型
	 */
	public String getTerminalType() {
		return terminalType;
	}
	/**
	 * 设置：创建用户Id
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：创建用户Id
	 */
	public String getCrtUserId() {
		return crtUserId;
	}
	/**
	 * 设置：创建用户姓名
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
	/**
	 * 获取：创建用户姓名
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
	 * 设置：更新用户ID
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：更新用户ID
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
	 * 设置：是否删除（1是/0否）
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：是否删除（1是/0否）
	 */
	public String getIsDeleted() {
		return isDeleted;
	}
	
	/**
	 * 设置是否可用
	 * @param isEnable
	 */
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	
	/**
	 * 获取是否可用
	 * @return
	 */
	public String getIsEnable() {
		return isEnable;
	}
	
}
