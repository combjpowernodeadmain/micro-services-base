package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 
 * 执法任务和巡查对象关系表
 * 
 * @author chenshuai
 */
@Table(name = "law_patrol_object")
public class LawPatrolObject implements Serializable {
	private static final long serialVersionUID = 1L;

	// id
	@Id
	@GeneratedValue(generator = "JDBC")
	private Integer id;

	// 执法任务id
	@Column(name = "law_task_id")
	private Integer lawTaskId;

	// 监管对象id
	@Column(name = "regula_object_id")
	private Integer regulaObjectId;

	// 监管对象名称
	@Column(name = "regula_object_name")
	private String regulaObjectName;

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
	 * 设置：监管对象名称
	 */
	public void setRegulaObjectName(String regulaObjectName) {
		this.regulaObjectName = regulaObjectName;
	}

	/**
	 * 获取：监管对象名称
	 */
	public String getRegulaObjectName() {
		return regulaObjectName;
	}
}
