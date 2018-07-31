package com.bjzhianjia.scp.cgp.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 考勤信息
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-30 22:12:53
 */
@Data
@Table(name = "attendance_info")
public class AttendanceInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //经度
    @Column(name = "longitude")
    private Float longitude;
	
	    //纬度
    @Column(name = "latitude")
    private Float latitude;
	
	    //地理信息
    @Column(name = "map_info")
    private String mapInfo;
	
	    //创建人
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //租户ID
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //部门ID
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
	 * 设置：经度
	 */
	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}
	/**
	 * 获取：经度
	 */
	public Float getLongitude() {
		return longitude;
	}
	/**
	 * 设置：纬度
	 */
	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}
	/**
	 * 获取：纬度
	 */
	public Float getLatitude() {
		return latitude;
	}
	/**
	 * 设置：地理信息
	 */
	public void setMapInfo(String mapInfo) {
		this.mapInfo = mapInfo;
	}
	/**
	 * 获取：地理信息
	 */
	public String getMapInfo() {
		return mapInfo;
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
}
