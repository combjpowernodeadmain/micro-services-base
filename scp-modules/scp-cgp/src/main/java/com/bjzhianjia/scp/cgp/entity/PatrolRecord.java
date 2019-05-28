package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 巡查记录表，用于记录某一次巡查信息
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2019-05-26 16:43:21
 */
@Table(name = "patrol_record")
public class PatrolRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //
    @Id
    private Integer id;
	
	    //巡查开始时间
    @Column(name = "start_time")
    private Date startTime;
	
	    //巡查结束时间
    @Column(name = "end_time")
    private Date endTime;
	
	    //巡查状态(0:巡查中|1:巡查完成)
    @Column(name = "patrol_status")
    private String patrolStatus;
	
	    //巡查人
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //创建人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;
	
	    //
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
	 * 设置：巡查开始时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：巡查开始时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：巡查结束时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：巡查结束时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：巡查状态(0:巡查中|1:巡查完成)
	 */
	public void setPatrolStatus(String patrolStatus) {
		this.patrolStatus = patrolStatus;
	}
	/**
	 * 获取：巡查状态(0:巡查中|1:巡查完成)
	 */
	public String getPatrolStatus() {
		return patrolStatus;
	}
	/**
	 * 设置：巡查人
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：巡查人
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
