package com.bjzhianjia.scp.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "mechanisms_practices")
public class MechanismsPractices {
    private static final long serialVersionUID = 1L;

    //
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    //党组织id
    @Column(name = "party_org_id")
    private Integer partyOrgId;

    //品牌建立时间
    @Column(name = "build_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date buildDate;

    //品牌名称
    @Column(name = "branch_title")
    private String branchTitle;

    //机制与做法内容
    @Column(name = "branch_content")
    private String branchContent;

    //品牌状态
    @Column(name = "branch_status")
    private String branchStatus;

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
     * 设置：党组织id
     */
    public void setPartyOrgId(Integer partyOrgId) {
        this.partyOrgId = partyOrgId;
    }

    /**
     * 获取：党组织id
     */
    public Integer getPartyOrgId() {
        return partyOrgId;
    }

    /**
     * 设置：品牌建立时间
     */
    public void setBuildDate(Date buildDate) {
        this.buildDate = buildDate;
    }

    /**
     * 获取：品牌建立时间
     */
    public Date getBuildDate() {
        return buildDate;
    }

    /**
     * 设置：品牌名称
     */
    public void setBranchTitle(String branchTitle) {
        this.branchTitle = branchTitle;
    }

    /**
     * 获取：品牌名称
     */
    public String getBranchTitle() {
        return branchTitle;
    }

    /**
     * 设置：党组织内容
     */
    public void setBranchContent(String branchContent) {
        this.branchContent = branchContent;
    }

    /**
     * 获取：党组织内容
     */
    public String getBranchContent() {
        return branchContent;
    }

    /**
     * 设置：品牌状态
     */
    public void setBranchStatus(String branchStatus) {
        this.branchStatus = branchStatus;
    }

    /**
     * 获取：品牌状态
     */
    public String getBranchStatus() {
        return branchStatus;
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
