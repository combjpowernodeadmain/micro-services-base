package com.bjzhianjia.scp.security.admin.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * BaseGroupMember 用户角色关系表.
 *
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018年10月14日          bo      chenshuai            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
@Table(name = "base_group_member")
public class BaseGroupMember implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private String id;

    //
    @Column(name = "group_id")
    private String groupId;

    //
    @Column(name = "user_id")
    private String userId;

    //
    @Column(name = "description")
    private String description;

    //
    @Column(name = "crt_time")
    private Date crtTime;

    //
    @Column(name = "crt_user")
    private String crtUser;

    //
    @Column(name = "crt_name")
    private String crtName;

    //
    @Column(name = "crt_host")
    private String crtHost;

    //
    @Column(name = "upd_time")
    private Date updTime;

    //
    @Column(name = "upd_user")
    private String updUser;

    //
    @Column(name = "upd_name")
    private String updName;

    //
    @Column(name = "upd_host")
    private String updHost;

    //
    @Column(name = "attr1")
    private String attr1;

    //
    @Column(name = "attr2")
    private String attr2;

    //
    @Column(name = "attr3")
    private String attr3;

    //
    @Column(name = "attr4")
    private String attr4;

    //
    @Column(name = "attr5")
    private String attr5;

    //
    @Column(name = "attr6")
    private String attr6;

    //
    @Column(name = "attr7")
    private String attr7;

    //
    @Column(name = "attr8")
    private String attr8;

    //租户Id
    @Column(name = "tenant_id")
    private String tenantId;


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
     * 设置：
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    /**
     * 获取：
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * 设置：
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取：
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置：
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取：
     */
    public String getDescription() {
        return description;
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
    public void setCrtUser(String crtUser) {
        this.crtUser = crtUser;
    }

    /**
     * 获取：
     */
    public String getCrtUser() {
        return crtUser;
    }

    /**
     * 设置：
     */
    public void setCrtName(String crtName) {
        this.crtName = crtName;
    }

    /**
     * 获取：
     */
    public String getCrtName() {
        return crtName;
    }

    /**
     * 设置：
     */
    public void setCrtHost(String crtHost) {
        this.crtHost = crtHost;
    }

    /**
     * 获取：
     */
    public String getCrtHost() {
        return crtHost;
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
    public void setUpdUser(String updUser) {
        this.updUser = updUser;
    }

    /**
     * 获取：
     */
    public String getUpdUser() {
        return updUser;
    }

    /**
     * 设置：
     */
    public void setUpdName(String updName) {
        this.updName = updName;
    }

    /**
     * 获取：
     */
    public String getUpdName() {
        return updName;
    }

    /**
     * 设置：
     */
    public void setUpdHost(String updHost) {
        this.updHost = updHost;
    }

    /**
     * 获取：
     */
    public String getUpdHost() {
        return updHost;
    }

    /**
     * 设置：
     */
    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    /**
     * 获取：
     */
    public String getAttr1() {
        return attr1;
    }

    /**
     * 设置：
     */
    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    /**
     * 获取：
     */
    public String getAttr2() {
        return attr2;
    }

    /**
     * 设置：
     */
    public void setAttr3(String attr3) {
        this.attr3 = attr3;
    }

    /**
     * 获取：
     */
    public String getAttr3() {
        return attr3;
    }

    /**
     * 设置：
     */
    public void setAttr4(String attr4) {
        this.attr4 = attr4;
    }

    /**
     * 获取：
     */
    public String getAttr4() {
        return attr4;
    }

    /**
     * 设置：
     */
    public void setAttr5(String attr5) {
        this.attr5 = attr5;
    }

    /**
     * 获取：
     */
    public String getAttr5() {
        return attr5;
    }

    /**
     * 设置：
     */
    public void setAttr6(String attr6) {
        this.attr6 = attr6;
    }

    /**
     * 获取：
     */
    public String getAttr6() {
        return attr6;
    }

    /**
     * 设置：
     */
    public void setAttr7(String attr7) {
        this.attr7 = attr7;
    }

    /**
     * 获取：
     */
    public String getAttr7() {
        return attr7;
    }

    /**
     * 设置：
     */
    public void setAttr8(String attr8) {
        this.attr8 = attr8;
    }

    /**
     * 获取：
     */
    public String getAttr8() {
        return attr8;
    }

    /**
     * 设置：租户Id
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取：租户Id
     */
    public String getTenantId() {
        return tenantId;
    }
}
