package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 立案附件
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
@Table(name = "cle_case_attachments")
public class CaseAttachments implements Serializable {
	private static final long serialVersionUID = 1L;
	
	    //主键
    @Id
    private Integer id;
	
	    //附件地址
    @Column(name = "doc_url")
    private String docUrl;
	
	    //案件主键
    @Column(name = "case_id")
    private String caseId;
	
	    //上传阶段编码
    @Column(name = "upload_phrase_code")
    private String uploadPhraseCode;
	
	    //上传阶段名称
    @Column(name = "upload_phrase_title")
    private String uploadPhraseTitle;
	
	    //是否删除(0未删除 1已删除)
    @Column(name = "is_deleted")
    private String isDeleted;
	
	    //上传时间
    @Column(name = "crt_time")
    private Date crtTime;
	
	    //上传人id
    @Column(name = "crt_user_id")
    private String crtUserId;
	
	    //上传人名
    @Column(name = "crt_user_name")
    private String crtUserName;
	

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
	 * 设置：附件地址
	 */
	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}
	/**
	 * 获取：附件地址
	 */
	public String getDocUrl() {
		return docUrl;
	}
	/**
	 * 设置：案件主键
	 */
	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}
	/**
	 * 获取：案件主键
	 */
	public String getCaseId() {
		return caseId;
	}
	/**
	 * 设置：上传阶段编码
	 */
	public void setUploadPhraseCode(String uploadPhraseCode) {
		this.uploadPhraseCode = uploadPhraseCode;
	}
	/**
	 * 获取：上传阶段编码
	 */
	public String getUploadPhraseCode() {
		return uploadPhraseCode;
	}
	/**
	 * 设置：上传阶段名称
	 */
	public void setUploadPhraseTitle(String uploadPhraseTitle) {
		this.uploadPhraseTitle = uploadPhraseTitle;
	}
	/**
	 * 获取：上传阶段名称
	 */
	public String getUploadPhraseTitle() {
		return uploadPhraseTitle;
	}
	/**
	 * 设置：是否删除(0未删除 1已删除)
	 */
	public void setIsDeleted(String isDeleted) {
		this.isDeleted = isDeleted;
	}
	/**
	 * 获取：是否删除(0未删除 1已删除)
	 */
	public String getIsDeleted() {
		return isDeleted;
	}
	/**
	 * 设置：上传时间
	 */
	public void setCrtTime(Date crtTime) {
		this.crtTime = crtTime;
	}
	/**
	 * 获取：上传时间
	 */
	public Date getCrtTime() {
		return crtTime;
	}
	/**
	 * 设置：上传人id
	 */
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	/**
	 * 获取：上传人id
	 */
	public String getCrtUserId() {
		return crtUserId;
	}
	/**
	 * 设置：上传人名
	 */
	public void setCrtUserName(String crtUserName) {
		this.crtUserName = crtUserName;
	}
	/**
	 * 获取：上传人名
	 */
	public String getCrtUserName() {
		return crtUserName;
	}
}
