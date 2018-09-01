package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 *  执法任务和执法队员关系表
 * 
 * @author chenshuai
 */
@Table(name = "law_execute_person")
public class LawExecutePerson implements Serializable {
	private static final long serialVersionUID = 1L;

	//id
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;

	// 执法任务id
	@Column(name = "law_task_id")
	private Integer lawTaskId;

	// 执法队员用户id
	@Column(name = "user_id")
	private String userId;

	// 执法队员用户id
	@Column(name = "user_name")
	private String userName;
	
	// 分队id（部门）
	@Column(name="depart_id")
	private String departId;

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
	 * 设置：执法任务id
	 */
	public void setLawTaskId(Integer lawTaskId) {
		this.lawTaskId = lawTaskId;
	}

	/**
	 * 获取：执法任务id
	 */
	public Integer getLawTaskId() {
		return lawTaskId;
	}

	/**
	 * 设置：执法队员用户id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * 获取：执法队员用户id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * 设置：执法队员用户id
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 获取：执法队员用户id
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * 获取：分队id（部门）
	 */
	public String getDepartId() {
		return departId;
	}

	/**
	 *  设置：分队id（部门）
	 */
	public void setDepartId(String departId) {
		this.departId = departId;
	}
}
