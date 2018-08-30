package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 催办记录表
 * 
 * @author chenshuai
 * @version 2018-08-26 09:02:28
 */
@Table(name = "urge_record")
public class UrgeRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	//id
	@Id
	private Integer id;

	// 立案单id
	@NotNull
	@Column(name = "case_info_id")
	private Integer caseInfoId;

	// 标题
	@Column(name = "title")
	@NotBlank(message="标题不能为空")
	@Size(min=1, max=127,message="标题长度异常") 
	private String title;

	// 内容
	@NotBlank(message="标题不能为空")
	@Size(min=1, max=1024,message="内容长度异常") 
	@Column(name = "content")
	private String content;

	// 创建人
	@Column(name = "crt_user_name")
	private String crtUserName;

	// 创建人ID
	@Column(name = "crt_user_id")
	private String crtUserId;

	// 创建时间
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
	 * 设置：立案单id
	 */
	public void setCaseInfoId(Integer caseInfoId) {
		this.caseInfoId = caseInfoId;
	}

	/**
	 * 获取：立案单id
	 */
	public Integer getCaseInfoId() {
		return caseInfoId;
	}

	/**
	 * 设置：标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取：标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置：内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取：内容
	 */
	public String getContent() {
		return content;
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
}
