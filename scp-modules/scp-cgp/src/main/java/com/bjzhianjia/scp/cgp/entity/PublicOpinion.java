package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * 记录来自舆情的事件
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-09 09:11:11
 */
@Table(name = "public_opinion")
public class PublicOpinion implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //舆情事项编号
    @Column(name = "opin_code")
    private String opinCode;
	
	    //舆情来源类型
    @Column(name = "opin_type")
    private String opinType;
	
	    //舆情事件标题
    @Column(name = "opin_title")
    private String opinTitle;
	
	    //舆情等级
    @Column(name = "opin_level")
    private String opinLevel;
	
	    //发布时间
    @Column(name = "publish_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date publishTime;
	
	    //事件处理状态
    @Column(name = "exe_status")
    private String exeStatus;
	
	    //原链接
    @Column(name = "origin_link")
    private String originLink;
	
	    //舆情事件发生地点
    @Column(name = "opin_addr")
    private String opinAddr;
	
	    //事件内容
    @Column(name = "opin_desc")
    private String opinDesc;
	
	    //站点
    @Column(name = "opin_port")
    private String opinPort;
	
	    //是否删除(1:删除|0:未删除)
    @Column(name = "is_deleted")
    private String isDeleted;
	
	    //创建时间
    @Column(name = "crt_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date crtTime;
	
	    //创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //更新时间
    @Column(name = "upd_time")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updTime;
	
	    //更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;
	
	    //更新人姓名
    @Column(name = "upd_user_name")
    private String updUserName;
	
	    //租房
    @Column(name = "tenant_id")
    private String tenantId;
	
	    //部门ID
    @Column(name = "dept_id")
    private String deptId;
	

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
	 * 设置：舆情事项编号
	 */
	public void setOpinCode(String opinCode) {
		this.opinCode = opinCode;
	}
	/**
	 * 获取：舆情事项编号
	 */
	public String getOpinCode() {
		return opinCode;
	}
	/**
	 * 设置：舆情来源类型
	 */
	public void setOpinType(String opinType) {
		this.opinType = opinType;
	}
	/**
	 * 获取：舆情来源类型
	 */
	public String getOpinType() {
		return opinType;
	}
	/**
	 * 设置：舆情事件标题
	 */
	public void setOpinTitle(String opinTitle) {
		this.opinTitle = opinTitle;
	}
	/**
	 * 获取：舆情事件标题
	 */
	public String getOpinTitle() {
		return opinTitle;
	}
	/**
	 * 设置：舆情等级
	 */
	public void setOpinLevel(String opinLevel) {
		this.opinLevel = opinLevel;
	}
	/**
	 * 获取：舆情等级
	 */
	public String getOpinLevel() {
		return opinLevel;
	}
	/**
	 * 设置：发布时间
	 */
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	/**
	 * 获取：发布时间
	 */
	public Date getPublishTime() {
		return publishTime;
	}
	/**
	 * 设置：事件处理状态
	 */
	public void setExeStatus(String exeStatus) {
		this.exeStatus = exeStatus;
	}
	/**
	 * 获取：事件处理状态
	 */
	public String getExeStatus() {
		return exeStatus;
	}
	/**
	 * 设置：原链接
	 */
	public void setOriginLink(String originLink) {
		this.originLink = originLink;
	}
	/**
	 * 获取：原链接
	 */
	public String getOriginLink() {
		return originLink;
	}
	/**
	 * 设置：舆情事件发生地点
	 */
	public void setOpinAddr(String opinAddr) {
		this.opinAddr = opinAddr;
	}
	/**
	 * 获取：舆情事件发生地点
	 */
	public String getOpinAddr() {
		return opinAddr;
	}
	/**
	 * 设置：事件内容
	 */
	public void setOpinDesc(String opinDesc) {
		this.opinDesc = opinDesc;
	}
	/**
	 * 获取：事件内容
	 */
	public String getOpinDesc() {
		return opinDesc;
	}
	/**
	 * 设置：站点
	 */
	public void setOpinPort(String opinPort) {
		this.opinPort = opinPort;
	}
	/**
	 * 获取：站点
	 */
	public String getOpinPort() {
		return opinPort;
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
	 * 设置：租房
	 */
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	/**
	 * 获取：租房
	 */
	public String getTenantId() {
		return tenantId;
	}
	/**
	 * 设置：部门ID
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * 获取：部门ID
	 */
	public String getDeptId() {
		return deptId;
	}
}