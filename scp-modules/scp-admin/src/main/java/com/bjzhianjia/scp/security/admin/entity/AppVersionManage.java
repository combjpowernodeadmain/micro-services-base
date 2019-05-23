package com.bjzhianjia.scp.security.admin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * app版本管理表
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2019-03-24 11:35:35
 */
@Table(name = "app_version_manage")
public class AppVersionManage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //app版本
    @Column(name = "app_version")
    private Long appVersion;
	
	    //更新版本类型(0:BUG更新|1:功能更新)
    @Column(name = "update_type")
    private String updateType;
	
	    //更新内容
    @Column(name = "update_content")
    private String updateContent;
	
	    //app下载地址
    @Column(name = "download_url")
    private String downloadUrl;
	
	    //手机系统类型()
    @Column(name = "os_type")
    private String osType;
	
	    //手机系统版本
    @Column(name = "os_version")
    private String osVersion;
	
	    //手机分辨率
    @Column(name = "resolution")
    private String resolution;
	
	    //手机运存
    @Column(name = "phone_ram")
    private String phoneRam;
	
	    //app版本更新时间
    @Column(name = "create_time")
    private Date createTime;
	
	    //打包标识
    @Column(name = "package_md5")
    private String packageMd5;
	
	    //是否部分更新
    @Column(name = "is_part_update")
    private Boolean isPartUpdate;
	
	    //
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //
    @Column(name = "upd_time")
    private Date updTime;
	
	    //
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
	 * 设置：app版本
	 */
	public void setAppVersion(Long appVersion) {
		this.appVersion = appVersion;
	}
	/**
	 * 获取：app版本
	 */
	public Long getAppVersion() {
		return appVersion;
	}
	/**
	 * 设置：更新版本类型(0:BUG更新|1:功能更新)
	 */
	public void setUpdateType(String updateType) {
		this.updateType = updateType;
	}
	/**
	 * 获取：更新版本类型(0:BUG更新|1:功能更新)
	 */
	public String getUpdateType() {
		return updateType;
	}
	/**
	 * 设置：更新内容
	 */
	public void setUpdateContent(String updateContent) {
		this.updateContent = updateContent;
	}
	/**
	 * 获取：更新内容
	 */
	public String getUpdateContent() {
		return updateContent;
	}
	/**
	 * 设置：app下载地址
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	/**
	 * 获取：app下载地址
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}
	/**
	 * 设置：手机系统类型()
	 */
	public void setOsType(String osType) {
		this.osType = osType;
	}
	/**
	 * 获取：手机系统类型()
	 */
	public String getOsType() {
		return osType;
	}
	/**
	 * 设置：手机系统版本
	 */
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	/**
	 * 获取：手机系统版本
	 */
	public String getOsVersion() {
		return osVersion;
	}
	/**
	 * 设置：手机分辨率
	 */
	public void setResolution(String resolution) {
		this.resolution = resolution;
	}
	/**
	 * 获取：手机分辨率
	 */
	public String getResolution() {
		return resolution;
	}
	/**
	 * 设置：手机运存
	 */
	public void setPhoneRam(String phoneRam) {
		this.phoneRam = phoneRam;
	}
	/**
	 * 获取：手机运存
	 */
	public String getPhoneRam() {
		return phoneRam;
	}
	/**
	 * 设置：app版本更新时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：app版本更新时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：打包标识
	 */
	public void setPackageMd5(String packageMd5) {
		this.packageMd5 = packageMd5;
	}
	/**
	 * 获取：打包标识
	 */
	public String getPackageMd5() {
		return packageMd5;
	}
	/**
	 * 设置：是否部分更新
	 */
	public void setIsPartUpdate(Boolean isPartUpdate) {
		this.isPartUpdate = isPartUpdate;
	}
	/**
	 * 获取：是否部分更新
	 */
	public Boolean getIsPartUpdate() {
		return isPartUpdate;
	}
	/**
	 * 设置：
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：
	 */
	public String getCrtUserId() {
		return crtUserId;
	}
	/**
	 * 设置：
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
	/**
	 * 获取：
	 */
	public String getCrtUserName() {
		return crtUserName;
	}
	/**
	 * 设置：
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 设置：
	 */
	public void setUpdUserId(String updUserId) {
		this.updUserId = updUserId;
	}
	/**
	 * 获取：
	 */
	public String getUpdUserId() {
		return updUserId;
	}
	/**
	 * 设置：
	 */
	public void setUpdUserName(String updUserName) {
		this.updUserName = updUserName;
	}
	/**
	 * 获取：
	 */
	public String getUpdUserName() {
		return updUserName;
	}
	/**
	 * 设置：
	 */
	public void setUpdTime(Date updTime) {
		this.updTime = updTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdTime() {
		return updTime;
	}
	/**
	 * 设置：
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：
	 */
	public String getTenantId() {
		return tenantId;
	}
}
