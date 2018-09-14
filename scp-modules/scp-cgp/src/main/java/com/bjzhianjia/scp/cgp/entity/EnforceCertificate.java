package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.bjzhianjia.scp.cgp.constances.CommonConstances;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.annonation.MergeField;


/**
 * 执法证管理
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
@Table(name = "enforce_certificate")
public class EnforceCertificate implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //持证人姓名
    @Column(name = "holder_name")
    @NotEmpty(message="持证人姓名不能为空")
    private String holderName;
	
	    //证件类型
    @Column(name = "cert_type")
    @NotEmpty(message="证件类型不能为空")
    @MergeField(key = "root_biz_zfzType", feign = DictFeign.class, method = "getByCode")
    private String certType;
	
	    //证件编号
    @Column(name = "cert_code")
    @NotEmpty(message="证件编号不能为空")
    @Length(max=32,message="证件编号长度不能超过32")
    private String certCode;
	
	    //证件有效期起始
    @Column(name = "valid_start")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format=CommonConstances.DATE_FORMAT)
    private Date validStart;
	
	    //证件有效期终止
    @Column(name = "valid_end")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JSONField(format=CommonConstances.DATE_FORMAT)
    private Date validEnd;
	
	    //持证人ID
    @Column(name = "usr_id")
    private String usrId;
	
	    //是否删除；1：是；0: 否
    @Column(name = "is_deleted")
    private String isDeleted;
	
	    //是否禁用；1：是；0：否
    @Column(name = "is_disabled")
    private String isDisabled;
	
	    //创建人
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //最后更新人
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //最后更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //最后更新时间
    @Column(name = "upd_time")
    private Date updTime;
	
	    //租户ID
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //涵盖业务线
    @Column(name = "biz_lists")
    private String bizLists;
    
    //部门id
	@Column(name = "depart_id")
	private String departId;

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
	 * 设置：持证人姓名
	 */
	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}
	/**
	 * 获取：持证人姓名
	 */
	public String getHolderName() {
		return holderName;
	}
	/**
	 * 设置：证件类型
	 */
	public void setCertType(String certType) {
		this.certType = certType;
	}
	/**
	 * 获取：证件类型
	 */
	public String getCertType() {
		return certType;
	}
	/**
	 * 设置：证件编号
	 */
	public void setCertCode(String certCode) {
		this.certCode = certCode;
	}
	/**
	 * 获取：证件编号
	 */
	public String getCertCode() {
		return certCode;
	}
	/**
	 * 设置：证件有效期起始
	 */
	public void setValidStart(Date validStart) {
		this.validStart = validStart;
	}
	/**
	 * 获取：证件有效期起始
	 */
	public Date getValidStart() {
		return validStart;
	}
	/**
	 * 设置：证件有效期终止
	 */
	public void setValidEnd(Date validEnd) {
		this.validEnd = validEnd;
	}
	/**
	 * 获取：证件有效期终止
	 */
	public Date getValidEnd() {
		return validEnd;
	}
	/**
	 * 设置：持证人ID
	 */
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	/**
	 * 获取：持证人ID
	 */
	public String getUsrId() {
		return usrId;
	}
	/**
	 * 设置：是否删除；1：是；0: 否
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：是否删除；1：是；0: 否
	 */
	public String getIsDeleted() {
		return isDeleted;
	}
	/**
	 * 设置：是否禁用；1：是；0：否
	 */
	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}
	/**
	 * 获取：是否禁用；1：是；0：否
	 */
	public String getIsDisabled() {
		return isDisabled;
	}
	/**
	 * 设置：创建人
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
	/**
	 * 获取：创建人
	 */
	public String getCrtUserName() {
		return crtUserName;
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
	 * 设置：最后更新人
	 */
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	/**
	 * 获取：最后更新人
	 */
	public String getUpdUserName() {
		return updUserName;
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
	/**
	 * 设置：涵盖业务线
	 */
	public void setBizLists(String bizLists) {
		this.bizLists = bizLists;
	}
	/**
	 * 获取：涵盖业务线
	 */
	public String getBizLists() {
		return bizLists;
	}
	/**
	 * 获取：部门id
	 */
	public String getDepartId() {
		return departId;
	}
	/**
	 * 设置：部门id
	 */
	public void setDepartId(String departId) {
		this.departId = departId;
	}
}
