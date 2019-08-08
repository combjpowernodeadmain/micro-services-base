package com.bjzhianjia.scp.party.entity;

import com.bjzhianjia.scp.merge.annonation.MergeField;
import com.bjzhianjia.scp.party.feign.DictFeign;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 党员家庭成员表
 *
 * @author bo
 * @version 2019-03-27 21:07:17
 * @email 576866311@qq.com
 */
@Table(name = "party_member_family")
public class PartyMemberFamily implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private Integer id;

    //党员id
    @Column(name = "party_mem_id")
    private Integer partyMemId;

    //称谓
    @Column(name = "relation_name")
    private String relationName;

    //姓名
    @Column(name = "name")
    private String name;

    //年龄
    @Column(name = "age")
    private Integer age;

    //政治面貌code（政治面貌字典code）
    @Column(name = "political")
    private String political;

    //工作单位
    @Column(name = "work_place")
    private String workPlace;

    //职位
    @Column(name = "work_post")
    private String workPost;

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
     * 设置：党员id
     */
    public void setPartyMemId(Integer partyMemId) {
        this.partyMemId = partyMemId;
    }

    /**
     * 获取：党员id
     */
    public Integer getPartyMemId() {
        return partyMemId;
    }

    /**
     * 设置：称谓
     */
    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    /**
     * 获取：称谓
     */
    public String getRelationName() {
        return relationName;
    }

    /**
     * 设置：姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：年龄
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * 获取：年龄
     */
    public Integer getAge() {
        return age;
    }

    /**
     * 设置：政治面貌code（政治面貌字典code）
     */
    public void setPolitical(String political) {
        this.political = political;
    }

    /**
     * 获取：政治面貌code（政治面貌字典code）
     */
    public String getPolitical() {
        return political;
    }

    /**
     * 设置：工作单位
     */
    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    /**
     * 获取：工作单位
     */
    public String getWorkPlace() {
        return workPlace;
    }

    /**
     * 设置：职位
     */
    public void setWorkPost(String workPost) {
        this.workPost = workPost;
    }

    /**
     * 获取：职位
     */
    public String getWorkPost() {
        return workPost;
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
