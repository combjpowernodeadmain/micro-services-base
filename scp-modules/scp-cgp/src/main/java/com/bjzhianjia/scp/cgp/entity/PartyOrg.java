package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 党组织
 *
 * @author chenshuai
 * @version 2019-06-25
 * @email cs4380@163.com
 */
@Table(name = "party_org")
public class PartyOrg implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private String id;

    //上级党组织
    @Column(name = "parent_org_id")
    private String parentOrgId;

    //机构代码
    @Column(name = "org_code")
    private String orgCode;

    //党组织全称
    @Column(name = "org_full_name")
    private String orgFullName;

    //党组织简称
    @Column(name = "org_short_name")
    private String orgShortName;

    //党组织类型（字典）
    @Column(name = "org_type")
    private String orgType;

    //成立时间
    @Column(name = "org_build_date")
    private Date orgBuildDate;

    //党委书记
    @Column(name = "org_leader")
    private String orgLeader;

    //党员总数
    @Column(name = "org_member_count")
    private Integer orgMemberCount;

    //党组织联系人
    @Column(name = "contacts_user")
    private String contactsUser;

    //联系电话
    @Column(name = "mobile_phone")
    private String mobilePhone;

    //党组织地址
    @Column(name = "address")
    private String address;

    //党组织地理坐标,数据格式：{"lng":xxx,"lat":xxx}
    @Column(name = "map_info")
    private String mapInfo;

    //是否删除(0否|1是）
    @Column(name = "is_deleted")
    private String isDeleted;

    //创建人
    @Column(name = "crt_user_name")
    private String crtUserName;

    //创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;

    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;

    //最后更新人
    @Column(name = "upd_user_name")
    private String updUserName;

    //最后更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;

    //最后更新时间
    @Column(name = "upd_time")
    private Date updTime;

    //操作人的主部门id
    @Column(name = "depart_id")
    private String departId;


    /**
     * 设置：
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public String getId() {
        return id;
    }

    /**
     * 设置：上级党组织
     */
    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    /**
     * 获取：上级党组织
     */
    public String getParentOrgId() {
        return parentOrgId;
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
     * 设置：党组织类型（字典）
     */
    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    /**
     * 获取：党组织类型（字典）
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
     * 设置：党委书记
     */
    public void setOrgLeader(String orgLeader) {
        this.orgLeader = orgLeader;
    }

    /**
     * 获取：党委书记
     */
    public String getOrgLeader() {
        return orgLeader;
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
     * 设置：党组织联系人
     */
    public void setContactsUser(String contactsUser) {
        this.contactsUser = contactsUser;
    }

    /**
     * 获取：党组织联系人
     */
    public String getContactsUser() {
        return contactsUser;
    }

    /**
     * 设置：联系电话
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * 获取：联系电话
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * 设置：党组织地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取：党组织地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置：党组织地理坐标,数据格式：{"lng":xxx,"lat":xxx}
     */
    public void setMapInfo(String mapInfo) {
        this.mapInfo = mapInfo;
    }

    /**
     * 获取：党组织地理坐标,数据格式：{"lng":xxx,"lat":xxx}
     */
    public String getMapInfo() {
        return mapInfo;
    }

    /**
     * 设置：是否删除(0否|1是）
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取：是否删除(0否|1是）
     */
    public String getIsDeleted() {
        return isDeleted;
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

    /**
     * 设置：最后更新人
     */
    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }

    /**
     * 获取：最后更新人
     */
    public String getUpdUserName() {
        return updUserName;
    }

    /**
     * 设置：最后更新人ID
     */
    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    /**
     * 获取：最后更新人ID
     */
    public String getUpdUserId() {
        return updUserId;
    }

    /**
     * 设置：最后更新时间
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * 获取：最后更新时间
     */
    public Date getUpdTime() {
        return updTime;
    }

    /**
     * 设置：操作人的主部门id
     */
    public void setDepartId(String departId) {
        this.departId = departId;
    }

    /**
     * 获取：操作人的主部门id
     */
    public String getDepartId() {
        return departId;
    }
}
