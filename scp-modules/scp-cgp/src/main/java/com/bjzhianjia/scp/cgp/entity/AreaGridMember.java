package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 网格成员
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
@Table(name = "area_grid_member")
public class AreaGridMember implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
    private Integer id;
	
	    //网格ID
    @Column(name = "grid_id")
    private Integer gridId;
	
	    //网格成员
    @Column(name = "grid_member")
    private Integer gridMember;
	
	    //成员所属岗位
    @Column(name = "grid_role")
    private String gridRole;
	
	    //是否删除；1：是；0: 否
    @Column(name = "is_deleted")
    private String isDeleted;
	
	    //是否禁用；1：是；0: 否
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
	
	    //租户
    @Column(name = "tenant_id")
    private String tenantId;
	

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
	 * 设置：网格ID
	 */
	public void setGridId(Integer gridId) {
		this.gridId = gridId;
	}
	/**
	 * 获取：网格ID
	 */
	public Integer getGridId() {
		return gridId;
	}
	/**
	 * 设置：网格成员
	 */
	public void setGridMember(Integer gridMember) {
		this.gridMember = gridMember;
	}
	/**
	 * 获取：网格成员
	 */
	public Integer getGridMember() {
		return gridMember;
	}
	/**
	 * 设置：成员所属岗位
	 */
	public void setGridRole(String gridRole) {
		this.gridRole = gridRole;
	}
	/**
	 * 获取：成员所属岗位
	 */
	public String getGridRole() {
		return gridRole;
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
	 * 设置：是否禁用；1：是；0: 否
	 */
	public void setIsDisabled(String isDisabled) {
		this.isDisabled = isDisabled;
	}
	/**
	 * 获取：是否禁用；1：是；0: 否
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
