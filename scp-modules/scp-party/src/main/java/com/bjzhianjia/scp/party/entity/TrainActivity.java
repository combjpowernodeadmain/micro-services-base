package com.bjzhianjia.scp.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 百人优培简讯活动表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Table(name = "train_activity")
public class TrainActivity implements Serializable {
    private static final long serialVersionUID = 1L;

    //id
    @Id
    private String id;

    //优培id
    @Column(name = "train_id")
    private String trainId;

    //简讯名称
    @Column(name = "train_activity_title")
    private String trainActivityTitle;

    //简讯信息
    @Column(name = "train_activity_content")
    private String trainActivityContent;

    //开始日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "start_date")
    private Date startDate;

    //结束日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_date")
    private Date endDate;

    //是否删除（0否 | 1是）
    @Column(name = "is_deleted")
    private String isDeleted;

    //是否全员参与（0否|1是）
    @Column(name = "is_whole")
    private String isWhole;

    //创建时间
    @Column(name = "crt_time")
    private Date crtTime;

    //创建人id
    @Column(name = "crt_user_id")
    private String crtUserId;

    //创建人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;

    //最后更新时间
    @Column(name = "upd_time")
    private Date updTime;

    //最后更新人id
    @Column(name = "upd_user_id")
    private String updUserId;

    //最后更新人名称
    @Column(name = "upd_user_name")
    private String updUserName;


    /**
     * 设置：id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取：id
     */
    public String getId() {
        return id;
    }

    /**
     * 获取：优培id
     */
    public String getTrainId() {
        return trainId;
    }

    /**
     * 设置：优培id
     */
    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    /**
     * 设置：简讯名称
     */
    public void setTrainActivityTitle(String trainActivityTitle) {
        this.trainActivityTitle = trainActivityTitle;
    }

    /**
     * 获取：简讯名称
     */
    public String getTrainActivityTitle() {
        return trainActivityTitle;
    }

    /**
     * 设置：简讯信息
     */
    public void setTrainActivityContent(String trainActivityContent) {
        this.trainActivityContent = trainActivityContent;
    }

    /**
     * 获取：简讯信息
     */
    public String getTrainActivityContent() {
        return trainActivityContent;
    }

    /**
     * 设置：开始日期
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取：开始日期
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 设置：结束日期
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取：结束日期
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置：是否删除（0否 | 1是）
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取：是否删除（0否 | 1是）
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * 获取：是否全员参与（0否|1是）
     */
    public String getIsWhole() {
        return isWhole;
    }

    /**
     * 设置：是否全员参与（0否|1是）
     */
    public void setIsWhole(String isWhole) {
        this.isWhole = isWhole;
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
     * 设置：创建人id
     */
    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    /**
     * 获取：创建人id
     */
    public String getCrtUserId() {
        return crtUserId;
    }

    /**
     * 设置：创建人姓名
     */
    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    /**
     * 获取：创建人姓名
     */
    public String getCrtUserName() {
        return crtUserName;
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
     * 设置：最后更新人id
     */
    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    /**
     * 获取：最后更新人id
     */
    public String getUpdUserId() {
        return updUserId;
    }

    /**
     * 设置：最后更新人名称
     */
    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }

    /**
     * 获取：最后更新人名称
     */
    public String getUpdUserName() {
        return updUserName;
    }
}
