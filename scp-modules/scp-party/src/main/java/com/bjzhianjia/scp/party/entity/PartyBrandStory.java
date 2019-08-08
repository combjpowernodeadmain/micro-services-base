package com.bjzhianjia.scp.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 党建品牌故事（活动）表
 *
 * @author bo
 * @version 2019-03-31 11:36:17
 * @email 576866311@qq.com
 */
@Table(name = "party_brand_story")
public class PartyBrandStory implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    //党建品牌id
    @Column(name = "brand_id")
    private Integer brandId;

    //品牌故事标题
    @Column(name = "story_title")
    private String storyTitle;

    //品牌故事内容
    @Column(name = "story_content")
    private String storyContent;

    //品牌故事日期
    @Column(name = "story_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date storyDate;

    //故事发布日期
    @Column(name = "story_upd_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date storyUpdDate;

    //故事状态code(字典)
    @Column(name = "story_state_code")
    private String storyStateCode;

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
    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    /**
     * 获取：党建品牌id
     */
    public Integer getBrandId() {
        return brandId;
    }

    /**
     * 设置：品牌故事标题
     */
    public void setStoryTitle(String storyTitle) {
        this.storyTitle = storyTitle;
    }

    /**
     * 获取：品牌故事标题
     */
    public String getStoryTitle() {
        return storyTitle;
    }

    /**
     * 设置：品牌故事内容
     */
    public void setStoryContent(String storyContent) {
        this.storyContent = storyContent;
    }

    /**
     * 获取：品牌故事内容
     */
    public String getStoryContent() {
        return storyContent;
    }

    /**
     * 设置：品牌故事日期
     */
    public void setStoryDate(Date storyDate) {
        this.storyDate = storyDate;
    }

    /**
     * 获取：品牌故事日期
     */
    public Date getStoryDate() {
        return storyDate;
    }

    /**
     * 设置：故事发布日期
     */
    public void setStoryUpdDate(Date storyUpdDate) {
        this.storyUpdDate = storyUpdDate;
    }

    /**
     * 获取：故事发布日期
     */
    public Date getStoryUpdDate() {
        return storyUpdDate;
    }

    /**
     * 设置：故事状态code(字典)
     */
    public void setStoryStateCode(String storyStateCode) {
        this.storyStateCode = storyStateCode;
    }

    /**
     * 获取：故事状态code(字典)
     */
    public String getStoryStateCode() {
        return storyStateCode;
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
