package com.bjzhianjia.scp.party.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 党组织党员关系表
 *
 * @author bo
 * @version 2019-03-27 21:07:17
 * @email 576866311@qq.com
 */
@Table(name = "org_member_relation")
public class OrgMemberRelation implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private Integer id;

    //党组织id
    @Column(name = "org_id")
    private Integer orgId;

    //党员id
    @Column(name = "member_id")
    private Integer memberId;

    //职务字典id
    @Column(name = "duty_class")
    private String dutyClass;

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
    @Column(name = "depart_id")
    private Date departId;

    //
    @Column(name = "tenant_id")
    private Date tenantId;

    //
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
    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    /**
     * 获取：党组织id
     */
    public Integer getOrgId() {
        return orgId;
    }

    /**
     * 设置：党员id
     */
    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    /**
     * 获取：党员id
     */
    public Integer getMemberId() {
        return memberId;
    }

    /**
     * 设置：职务字典id
     */
    public void setDutyClass(String dutyClass) {
        this.dutyClass = dutyClass;
    }

    /**
     * 获取：职务字典id
     */
    public String getDutyClass() {
        return dutyClass;
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
    public void setDepartId(Date departId) {
        this.departId = departId;
    }

    /**
     * 获取：
     */
    public Date getDepartId() {
        return departId;
    }

    /**
     * 设置：
     */
    public void setTenantId(Date tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取：
     */
    public Date getTenantId() {
        return tenantId;
    }

    /**
     * 设置：
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取：
     */
    public String getIsDeleted() {
        return isDeleted;
    }
}
