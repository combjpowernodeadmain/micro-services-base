package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.annonation.MergeField;


/**
 * 车辆管理
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
@Table(name = "vhcl_management")
public class VhclManagement implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
    
    @Column(name = "ternimal_id")
//    @NotNull(message="终端不能为空")
    //终端号为先填
    private Integer ternimalId;
	
	    //车辆牌号
    @Column(name = "vehicle_num")
    @Length(max=10,message="车牌号长度不能超过10")
    private String vehicleNum;
	
	    //车辆类型
    @Column(name = "vehicle_type")
    @MergeField(key = "root_biz_vhcl_t", feign = DictFeign.class, method = "getByCode")
    private String vehicleType;
	
	    //所属部门
    @Column(name = "department")
    private String department;
	
	    //车辆说明
    @Column(name = "vehicle_desc")
    private String vehicleDesc;
	
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
	
	    //更新人id
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //更新用户姓名
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //是否删除 1：是 0：否
    @Column(name = "is_deleted")
    private String isDeleted;
	
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
	 * 设置: 终端标识
	 */
	public void setTernimalId(Integer ternimalId) {
		this.ternimalId = ternimalId;
	}
	
	/**
	 * 获取: 终端标识
	 */
	public Integer getTernimalId() {
		return ternimalId;
	}
	/**
	 * 设置：车辆牌号
	 */
	public void setVehicleNum(String vehicleNum) {
		this.vehicleNum = vehicleNum;
	}
	/**
	 * 获取：车辆牌号
	 */
	public String getVehicleNum() {
		return vehicleNum;
	}
	/**
	 * 设置：车辆类型
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	/**
	 * 获取：车辆类型
	 */
	public String getVehicleType() {
		return vehicleType;
	}
	/**
	 * 设置：所属部门
	 */
	public void setDepartment(String department) {
		this.department = department;
	}
	/**
	 * 获取：所属部门
	 */
	public String getDepartment() {
		return department;
	}
	/**
	 * 设置：车辆说明
	 */
	public void setVehicleDesc(String vehicleDesc) {
		this.vehicleDesc = vehicleDesc;
	}
	/**
	 * 获取：车辆说明
	 */
	public String getVehicleDesc() {
		return vehicleDesc;
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
	 * 设置：更新人id
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：更新人id
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
