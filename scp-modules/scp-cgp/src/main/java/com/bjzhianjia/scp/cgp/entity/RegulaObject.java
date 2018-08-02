package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 监管对象
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:26
 */
@Table(name = "regula_object")
public class RegulaObject implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //监管对象编码
    @Column(name = "obj_code")
    private String objCode;
	
	    //监管对象名称
    @Column(name = "obj_name")
    private String objName;
	
	    //监管对象类型
    @Column(name = "obj_type")
    private String objType;
	
	    //监管对象ID
    @Column(name = "obj_id")
    private Integer objId;
	
	    //监管对象地址
    @Column(name = "obj_address")
    private String objAddress;
	
	    //联系人
    @Column(name = "linkman")
    private String linkman;
	
	    //联系电话
    @Column(name = "linkman_phone")
    private String linkmanPhone;
	
	    //简介
    @Column(name = "introduction")
    private String introduction;
	
	    //备注
    @Column(name = "remark")
    private String remark;
	
	    //办理前图片
    @Column(name = "pic_before")
    private String picBefore;
	
	    //经度
    @Column(name = "longitude")
    private Float longitude;
	
	    //所属网格
    @Column(name = "gri_id")
    private Integer griId;
	
	    //纬度
    @Column(name = "latitude")
    private Float latitude;
	
	    //采集人
    @Column(name = "gatherer")
    private String gatherer;
	
	    //采集时间
    @Column(name = "gather_time")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date gatherTime;
	
	    //业务条线
    @Column(name = "biz_list")
    private String bizList;
	
	    //事件类别
    @Column(name = "event_list")
    private String eventList;
	
	    //地理信息
    @Column(name = "map_info")
    private String mapInfo;
	
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
	 * 设置：监管对象编码
	 */
	public void setObjCode(String objCode) {
		this.objCode = objCode;
	}
	/**
	 * 获取：监管对象编码
	 */
	public String getObjCode() {
		return objCode;
	}
	/**
	 * 设置：监管对象名称
	 */
	public void setObjName(String objName) {
		this.objName = objName;
	}
	/**
	 * 获取：监管对象名称
	 */
	public String getObjName() {
		return objName;
	}
	/**
	 * 设置：监管对象类型
	 */
	public void setObjType(String objType) {
		this.objType = objType;
	}
	/**
	 * 获取：监管对象类型
	 */
	public String getObjType() {
		return objType;
	}
	/**
	 * 设置：监管对象ID
	 */
	public void setObjId(Integer objId) {
		this.objId = objId;
	}
	/**
	 * 获取：监管对象ID
	 */
	public Integer getObjId() {
		return objId;
	}
	/**
	 * 设置：监管对象地址
	 */
	public void setObjAddress(String objAddress) {
		this.objAddress = objAddress;
	}
	/**
	 * 获取：监管对象地址
	 */
	public String getObjAddress() {
		return objAddress;
	}
	/**
	 * 设置：联系人
	 */
	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}
	/**
	 * 获取：联系人
	 */
	public String getLinkman() {
		return linkman;
	}
	/**
	 * 设置：联系电话
	 */
	public void setLinkmanPhone(String linkmanPhone) {
		this.linkmanPhone = linkmanPhone;
	}
	/**
	 * 获取：联系电话
	 */
	public String getLinkmanPhone() {
		return linkmanPhone;
	}
	/**
	 * 设置：简介
	 */
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	/**
	 * 获取：简介
	 */
	public String getIntroduction() {
		return introduction;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：办理前图片
	 */
	public void setPicBefore(String picBefore) {
		this.picBefore = picBefore;
	}
	/**
	 * 获取：办理前图片
	 */
	public String getPicBefore() {
		return picBefore;
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
	 * 设置：所属网格
	 */
	public void setGriId(Integer griId) {
		this.griId = griId;
	}
	/**
	 * 获取：所属网格
	 */
	public Integer getGriId() {
		return griId;
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
	 * 设置：采集人
	 */
	public void setGatherer(String gatherer) {
		this.gatherer = gatherer;
	}
	/**
	 * 获取：采集人
	 */
	public String getGatherer() {
		return gatherer;
	}
	/**
	 * 设置：采集时间
	 */
	public void setGatherTime(Date gatherTime) {
		this.gatherTime = gatherTime;
	}
	/**
	 * 获取：采集时间
	 */
	public Date getGatherTime() {
		return gatherTime;
	}
	/**
	 * 设置：业务条线
	 */
	public void setBizList(String bizList) {
		this.bizList = bizList;
	}
	/**
	 * 获取：业务条线
	 */
	public String getBizList() {
		return bizList;
	}
	/**
	 * 设置：事件类别
	 */
	public void setEventList(String eventList) {
		this.eventList = eventList;
	}
	/**
	 * 获取：事件类别
	 */
	public String getEventList() {
		return eventList;
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
}
