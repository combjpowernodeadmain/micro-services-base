package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 * 巡查任务资料关系表
 * 
 * @author bo
 */
@Table(name = "patrol_res")
public class PatrolRes implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;

	// 巡查任务记录id
	@Column(name = "patrol_task_id")
	private Integer patrolTaskId;

	// 图片地址
	@Column(name = "url")
	private String url;

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
	 * 设置：巡查任务记录id
	 */
	public void setPatrolTaskId(Integer patrolTaskId) {
		this.patrolTaskId = patrolTaskId;
	}

	/**
	 * 获取：巡查任务记录id
	 */
	public Integer getPatrolTaskId() {
		return patrolTaskId;
	}

	/**
	 * 设置：图片地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取：图片地址
	 */
	public String getUrl() {
		return url;
	}
}
