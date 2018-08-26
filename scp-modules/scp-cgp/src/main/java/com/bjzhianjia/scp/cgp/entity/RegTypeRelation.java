package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 用于配置监管对象类型与业务之间的关系，如在某种情况下只需要某几个类型的监管对象类型
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 11:04:06
 */
@Table(name = "reg_type_relation")
public class RegTypeRelation implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	@Id
	private Integer id;

	// 业务标识，该标识应唯一且不可变
	@Column(name = "code")
	private String code;

	// 监管对象类型ID，多个ID间用逗号隔开
	@Column(name = "reg_obj_id")
	private String regObjId;

	// 项目标识，表明是综合执法还是观察使治理，或是其它
	@Column(name = "project_sign")
	private String projectSign;

	public RegTypeRelation() {
	}

	public RegTypeRelation(String code, String projectSign) {
		super();
		this.code = code;
		this.projectSign = projectSign;
	}

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
	 * 设置：业务标识，该标识应唯一且不可变
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取：业务标识，该标识应唯一且不可变
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置：监管对象类型ID，多个ID间用逗号隔开
	 */
	public void setRegObjId(String regObjId) {
		this.regObjId = regObjId;
	}

	/**
	 * 获取：监管对象类型ID，多个ID间用逗号隔开
	 */
	public String getRegObjId() {
		return regObjId;
	}

	/**
	 * 设置：项目标识，表明是综合执法还是观察使治理，或是其它
	 */
	public void setProjectSign(String projectSign) {
		this.projectSign = projectSign;
	}

	/**
	 * 获取：项目标识，表明是综合执法还是观察使治理，或是其它
	 */
	public String getProjectSign() {
		return projectSign;
	}
}
