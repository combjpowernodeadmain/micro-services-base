package com.bjzhianjia.scp.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 党建品牌附件表
 *
 * @author bo
 * @version 2019-03-31 11:36:17
 * @email 576866311@qq.com
 */
@Table(name = "party_branch_file")
public class PartyBranchFile implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private Integer id;

    //党建品牌id
    @Column(name = "branch_id")
    private Integer branchId;

    //品牌附件文件名
    @Column(name = "file_name")
    private String fileName;

    //文件地址
    @Column(name = "file_url")
    private String fileUrl;

    //品牌附件说明
    @Column(name = "file_comment")
    private String fileComment;

    //附件上传日期
    @Column(name = "file_upload_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fileUploadDate;

    //附件状态id（字典表CODE
    @Column(name = "file_status")
    private String fileStatus;

    //
    @Column(name = "crt_user_id")
    private String crtUserId;

    //
    @Column(name = "crt_user_name")
    private String crtUserName;

    //
    @Column(name = "crt_time")
    private Date crtTime;

    //
    @Column(name = "upd_user_id")
    private String updUserId;

    //
    @Column(name = "upd_user_name")
    private String updUserName;

    //
    @Column(name = "upd_time")
    private Date updTime;

    //
    @Column(name = "dept_id")
    private String deptId;

    //
    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "is_deleted")
    private String isDeleted;


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
     * 设置：党建品牌id
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * 获取：党建品牌id
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * 设置：品牌附件文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取：品牌附件文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置：文件地址
     */
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    /**
     * 获取：文件地址
     */
    public String getFileUrl() {
        return fileUrl;
    }

    /**
     * 设置：品牌附件说明
     */
    public void setFileComment(String fileComment) {
        this.fileComment = fileComment;
    }

    /**
     * 获取：品牌附件说明
     */
    public String getFileComment() {
        return fileComment;
    }

    /**
     * 设置：附件上传日期
     */
    public void setFileUploadDate(Date fileUploadDate) {
        this.fileUploadDate = fileUploadDate;
    }

    /**
     * 获取：附件上传日期
     */
    public Date getFileUploadDate() {
        return fileUploadDate;
    }

    /**
     * 设置：附件状态id（字典表CODE
     */
    public void setFileStatus(String fileStatus) {
        this.fileStatus = fileStatus;
    }

    /**
     * 获取：附件状态id（字典表CODE
     */
    public String getFileStatus() {
        return fileStatus;
    }

    /**
     * 设置：
     */
    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    /**
     * 获取：
     */
    public String getCrtUserId() {
        return crtUserId;
    }

    /**
     * 设置：
     */
    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    /**
     * 获取：
     */
    public String getCrtUserName() {
        return crtUserName;
    }

    /**
     * 设置：
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * 获取：
     */
    public Date getCrtTime() {
        return crtTime;
    }

    /**
     * 设置：
     */
    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    /**
     * 获取：
     */
    public String getUpdUserId() {
        return updUserId;
    }

    /**
     * 设置：
     */
    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }

    /**
     * 获取：
     */
    public String getUpdUserName() {
        return updUserName;
    }

    /**
     * 设置：
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * 获取：
     */
    public Date getUpdTime() {
        return updTime;
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

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
