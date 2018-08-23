package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 巡查任务记录表
 * 
 * @author bo
 */
@Table(name = "patrol_task")
public class PatrolTask implements Serializable {
	private static final long serialVersionUID = 1L;
	//自增主键
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;

	// 网格id
	@Column(name = "area_grid_id")
	private Integer areaGridId;

	// 网格编号
	@Column(name = "area_grid_code")
	private String areaGridCode;
	
	// 来源任务id
	@Column(name = "source_task_id")
	private Integer sourceTaskId;

	// 巡查类型 (1 专项 | 2任务 | 3日常巡查)
	@Column(name = "source_type")
	private String sourceType;

	// 巡查事件编号
	@Column(name = "patrol_code")
	private String patrolCode;

	// 巡查事件名称
	@Column(name = "patrol_name")
	private String patrolName;

	// 巡查事件级别(数据字典id)
	@Column(name = "patrol_level")
	private String patrolLevel;

	// 监管对象类型id
	@Column(name = "regula_object_type_id")
	private Integer regulaObjectTypeId;

	// 监管对象id
	@Column(name = "regula_object_id")
	private Integer regulaObjectId;

	// 业务条线id(数据字典id：业务条线的子分类id)
	@Column(name = "biz_type_id")
	private String bizTypeId;

	// 事件类别id
	@Column(name = "event_type_id")
	private Integer eventTypeId;

	// 详细地理位置
	@Column(name = "address")
	private String address;

	// 经纬度
	@Column(name = "map_info")
	private String mapInfo;

	// 巡查事项内容
	@Column(name = "content")
	private String content;

	// 当事人id
	@Column(name = "concerned_id")
	private Integer concernedId;

	// 当事人类型  数据字典id （个人 | 单位）
	@Column(name = "concerned_type")
	private String concernedType;

	// 巡查记录状态 (数据字典id)
	@Column(name = "status")
	private String status;

	// 租户
	@Column(name = "tenant_id")
	private String tenantId;

	//
	@Column(name = "dept_id")
	private String deptId;
	
	// 上报人用户id
	@Column(name = "crt_user_name")
	private String crtUserName;
	
	// 上报人姓名
	@Column(name = "release_user_name")
	private String releaseUserName;

	// 上报时间
	@Column(name = "crt_time")
	private Date crtTime;

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
	 * 设置：网格id
	 */
	public void setAreaGridId(Integer areaGridId) {
		this.areaGridId = areaGridId;
	}

	/**
	 * 获取：网格id
	 */
	public Integer getAreaGridId() {
		return areaGridId;
	}

	/**
	 * 设置：来源任务id
	 */
	public void setSourceTaskId(Integer sourceTaskId) {
		this.sourceTaskId = sourceTaskId;
	}

	/**
	 * 获取：来源任务id
	 */
	public Integer getSourceTaskId() {
		return sourceTaskId;
	}

	/**
	 * 设置：巡查类型 (1 专项 | 2任务 | 3日常巡查)
	 */
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	/**
	 * 获取：巡查类型 (1 专项 | 2任务 | 3日常巡查)
	 */
	public String getSourceType() {
		return sourceType;
	}

	/**
	 * 设置：巡查事件编号
	 */
	public void setPatrolCode(String patrolCode) {
		this.patrolCode = patrolCode;
	}

	/**
	 * 获取：巡查事件编号
	 */
	public String getPatrolCode() {
		return patrolCode;
	}

	/**
	 * 设置：巡查事件名称
	 */
	public void setPatrolName(String patrolName) {
		this.patrolName = patrolName;
	}

	/**
	 * 获取：巡查事件名称
	 */
	public String getPatrolName() {
		return patrolName;
	}

	/**
	 * 设置：巡查事件级别(数据字典id)
	 */
	public void setPatrolLevel(String patrolLevel) {
		this.patrolLevel = patrolLevel;
	}

	/**
	 * 获取：巡查事件级别(数据字典id)
	 */
	public String getPatrolLevel() {
		return patrolLevel;
	}

	/**
	 * 设置：监管对象类型id
	 */
	public void setRegulaObjectTypeId(Integer regulaObjectTypeId) {
		this.regulaObjectTypeId = regulaObjectTypeId;
	}

	/**
	 * 获取：监管对象类型id
	 */
	public Integer getRegulaObjectTypeId() {
		return regulaObjectTypeId;
	}

	/**
	 * 设置：监管对象id
	 */
	public void setRegulaObjectId(Integer regulaObjectId) {
		this.regulaObjectId = regulaObjectId;
	}

	/**
	 * 获取：监管对象id
	 */
	public Integer getRegulaObjectId() {
		return regulaObjectId;
	}

	/**
	 * 设置：业务条线id(数据字典id：业务条线的子分类id)
	 */
	public void setBizTypeId(String bizTypeId) {
		this.bizTypeId = bizTypeId;
	}

	/**
	 * 获取：业务条线id(数据字典id：业务条线的子分类id)
	 */
	public String getBizTypeId() {
		return bizTypeId;
	}

	/**
	 * 设置：事件类别id
	 */
	public void setEventTypeId(Integer eventTypeId) {
		this.eventTypeId = eventTypeId;
	}

	/**
	 * 获取：事件类别id
	 */
	public Integer getEventTypeId() {
		return eventTypeId;
	}

	/**
	 * 设置：详细地理位置
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 获取：详细地理位置
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置：经纬度
	 */
	public void setMapInfo(String mapInfo) {
		this.mapInfo = mapInfo;
	}

	/**
	 * 获取：经纬度
	 */
	public String getMapInfo() {
		return mapInfo;
	}

	/**
	 * 设置：巡查事项内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取：巡查事项内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置：当事人id
	 */
	public void setConcernedId(Integer concernedId) {
		this.concernedId = concernedId;
	}

	/**
	 * 获取：当事人id
	 */
	public Integer getConcernedId() {
		return concernedId;
	}

	/**
	 * 设置：当事人类型
	 */
	public void setConcernedType(String concernedType) {
		this.concernedType = concernedType;
	}

	/**
	 * 获取：当事人类型  (数据字典id)
	 */
	public String getConcernedType() {
		return concernedType;
	}

	/**
	 * 设置：巡查记录状态 (数据字典id)
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * 获取：巡查记录状态 (数据字典id)
	 */
	public String getStatus() {
		return status;
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
	 * 设置：
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	/**
	 * 获取：
	 */
	public String getDeptId() {
		return deptId;
	}

	/**
	 * 设置：上报人
	 */
	public void setReleaseUserName(String releaseUserName) {
		this.releaseUserName = releaseUserName;
	}

	/**
	 * 获取：上报人
	 */
	public String getReleaseUserName() {
		return releaseUserName;
	}

	/**
	 * 设置：上报时间
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}

	/**
	 * 获取：上报时间
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 获取：网格编号
	 * @return
	 */
	public String getAreaGridCode() {
		return areaGridCode;
	}
	/**
	 * 设置：网格编号
	 */
	public void setAreaGridCode(String areaGridCode) {
		this.areaGridCode = areaGridCode;
	}
	
	/**
	 * 获取：用户id
	 */
	public String getCrtUserName() {
		return crtUserName;
	}
	/**
	 * 设置：用户id
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
}