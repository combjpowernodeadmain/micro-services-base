package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.annonation.MergeField;


/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-01 17:23:41
 */
@Table(name = "regula_object_type")
public class RegulaObjectType implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //监管对象类型编码
    @Column(name = "object_type_code")
    private String objectTypeCode;
	
	    //监管对象类型名称
    @Column(name = "object_type_name")
    private String objectTypeName;
	
	    //所属监管对象类型
    @Column(name = "parent_object_type_id")
    @MergeField(key="root_biz_objType",feign=DictFeign.class,method="getDictIds")
    private Integer parentObjectTypeId;
	
	    //是否可用；关联数据字典
    @Column(name = "is_enable")
    @MergeField(key="root_biz_enabled",feign=DictFeign.class,method="getDictIds")
    private String isEnable;
	
	    //是否删除；1：是；0: 否
    @Column(name = "is_deleted")
    private String isDeleted;
	
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
	
	    //部门ID
    @Column(name = "depart_id")
    private String departId;
    
    //监管对象信息模板----后加字段
    @Column(name="templet_type")
    private String templetType;
	

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
	 * 设置：监管对象类型编码
	 */
	public void setObjectTypeCode(String objectTypeCode) {
		this.objectTypeCode = objectTypeCode;
	}
	/**
	 * 获取：监管对象类型编码
	 */
	public String getObjectTypeCode() {
		return objectTypeCode;
	}
	/**
	 * 设置：监管对象类型名称
	 */
	public void setObjectTypeName(String objectTypeName) {
		this.objectTypeName = objectTypeName;
	}
	/**
	 * 获取：监管对象类型名称
	 */
	public String getObjectTypeName() {
		return objectTypeName;
	}
	/**
	 * 设置：所属监管对象类型
	 */
	public void setParentObjectTypeId(Integer parentObjectTypeId) {
		this.parentObjectTypeId = parentObjectTypeId;
	}
	/**
	 * 获取：所属监管对象类型
	 */
	public Integer getParentObjectTypeId() {
		return parentObjectTypeId;
	}
	/**
	 * 设置：是否可用；关联数据字典
	 */
	public void setIsEnable(String isEnable) {
		this.isEnable = isEnable;
	}
	/**
	 * 获取：是否可用；关联数据字典
	 */
	public String getIsEnable() {
		return isEnable;
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
	 * 设置：部门ID
	 */
	public void setDepartId(String departId) {
		this.departId = departId;
	}
	/**
	 * 获取：部门ID
	 */
	public String getDepartId() {
		return departId;
	}
	/**
	 * 获取：监管对象信息模板
	 */
	public String getTempletType() {
		return templetType;
	}
	/**
	 * 设置：监管对象信息模板
	 */
	public void setTempletType(String templetType) {
		this.templetType = templetType;
	}
}
