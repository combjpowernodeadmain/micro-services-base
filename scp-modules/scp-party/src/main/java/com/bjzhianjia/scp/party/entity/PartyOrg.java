package com.bjzhianjia.scp.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 党组织表
 *
 * @author bo
 * @version 2019-03-27 21:07:16
 * @email 576866311@qq.com
 */
@Table(name = "party_org")
public class PartyOrg implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private Integer id;

    //机构代码
    @Column(name = "org_code")
    private String orgCode;

    //党组织全称
    @Column(name = "org_full_name")
    private String orgFullName;

    //党组织简称
    @Column(name = "org_short_name")
    private String orgShortName;

    //党组织类型（党组织类型字典code）
    @Column(name = "org_type")
    private String orgType;

    //成立时间
    @Column(name = "org_build_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orgBuildDate;

    //应换届时间
    @Column(name = "org_change_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orgChangeDate;

    //实际换届时间
    @Column(name = "org_real_change_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orgRealChangeDate;

    //委员会人数
    @Column(name = "org_committee_count")
    private Integer orgCommitteeCount;

    //党员总数
    @Column(name = "org_member_count")
    private Integer orgMemberCount;

    //正式党员数
    @Column(name = "org_full_count")
    private Integer orgFullCount;

    //预备党员数
    @Column(name = "org_probationary_Count")
    private Integer orgProbationaryCount;

    //极分子数
    @Column(name = "org_activist_count")
    private Integer orgActivistCount;

    //上级党组织
    @Column(name = "parent_id")
    private Integer parentId;

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

    // 党组织地理坐标
    @Column(name = "map_info")
    private String mapInfo;

    @Column(name = "org_secretary")
    private String orgSecretary;

    @Column(name = "contacts_user")
    private String contactsUser;

    @Column(name = "mobile_phone")
    private String mobilePhone;

    @Column(name = "address")
    private String address;

    /**
     * 获取：党组织联系人
     */
    public String getContactsUser() {
        return contactsUser;
    }

    /**
     * 设置：党组织联系人
     */
    public void setContactsUser(String contactsUser) {
        this.contactsUser = contactsUser;
    }

    /**
     * 设置：党组织联系方式
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * 获取：党组织联系方式
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * 获取：党组织地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置：党组织地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 设置：党组织书记
     */
    public String getOrgSecretary() {
        return orgSecretary;
    }

    /**
     * 获取：党组织书记
     */
    public void setOrgSecretary(String orgSecretary) {
        this.orgSecretary = orgSecretary;
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
     * 设置：机构代码
     */
    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    /**
     * 获取：机构代码
     */
    public String getOrgCode() {
        return orgCode;
    }

    /**
     * 设置：党组织全称
     */
    public void setOrgFullName(String orgFullName) {
        this.orgFullName = orgFullName;
    }

    /**
     * 获取：党组织全称
     */
    public String getOrgFullName() {
        return orgFullName;
    }

    /**
     * 设置：党组织简称
     */
    public void setOrgShortName(String orgShortName) {
        this.orgShortName = orgShortName;
    }

    /**
     * 获取：党组织简称
     */
    public String getOrgShortName() {
        return orgShortName;
    }

    /**
     * 设置：党组织类型（党组织类型字典code）
     */
    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    /**
     * 获取：党组织类型（党组织类型字典code）
     */
    public String getOrgType() {
        return orgType;
    }

    /**
     * 设置：成立时间
     */
    public void setOrgBuildDate(Date orgBuildDate) {
        this.orgBuildDate = orgBuildDate;
    }

    /**
     * 获取：成立时间
     */
    public Date getOrgBuildDate() {
        return orgBuildDate;
    }

    /**
     * 设置：应换届时间
     */
    public void setOrgChangeDate(Date orgChangeDate) {
        this.orgChangeDate = orgChangeDate;
    }

    /**
     * 获取：应换届时间
     */
    public Date getOrgChangeDate() {
        return orgChangeDate;
    }

    /**
     * 设置：实际换届时间
     */
    public void setOrgRealChangeDate(Date orgRealChangeDate) {
        this.orgRealChangeDate = orgRealChangeDate;
    }

    /**
     * 获取：实际换届时间
     */
    public Date getOrgRealChangeDate() {
        return orgRealChangeDate;
    }

    /**
     * 设置：委员会人数
     */
    public void setOrgCommitteeCount(Integer orgCommitteeCount) {
        this.orgCommitteeCount = orgCommitteeCount;
    }

    /**
     * 获取：委员会人数
     */
    public Integer getOrgCommitteeCount() {
        return orgCommitteeCount;
    }

    /**
     * 设置：党员总数
     */
    public void setOrgMemberCount(Integer orgMemberCount) {
        this.orgMemberCount = orgMemberCount;
    }

    /**
     * 获取：党员总数
     */
    public Integer getOrgMemberCount() {
        return orgMemberCount;
    }

    /**
     * 设置：正式党员数
     */
    public void setOrgFullCount(Integer orgFullCount) {
        this.orgFullCount = orgFullCount;
    }

    /**
     * 获取：正式党员数
     */
    public Integer getOrgFullCount() {
        return orgFullCount;
    }

    /**
     * 设置：预备党员数
     */
    public void setOrgProbationaryCount(Integer orgProbationaryCount) {
        this.orgProbationaryCount = orgProbationaryCount;
    }

    /**
     * 获取：预备党员数
     */
    public Integer getOrgProbationaryCount() {
        return orgProbationaryCount;
    }

    /**
     * 设置：极分子数
     */
    public void setOrgActivistCount(Integer orgActivistCount) {
        this.orgActivistCount = orgActivistCount;
    }

    /**
     * 获取：极分子数
     */
    public Integer getOrgActivistCount() {
        return orgActivistCount;
    }

    /**
     * 设置：上级党组织
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取：上级党组织
     */
    public Integer getParentId() {
        return parentId;
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

    /**
     * 获取：党组织地理坐标
     */
    public String getMapInfo() {
        return mapInfo;
    }

    /**
     * 设置：党组织地理坐标
     */
    public void setMapInfo(String mapInfo) {
        this.mapInfo = mapInfo;
    }
}
