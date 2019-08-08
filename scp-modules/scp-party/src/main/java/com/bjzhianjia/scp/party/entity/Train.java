package com.bjzhianjia.scp.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * 百人优培批次表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Table(name = "train")
public class Train implements Serializable {
    private static final long serialVersionUID = 1L;

    //id
    @Id
    private String id;

    //优培批次
    @Column(name = "train_batch")
    private String trainBatch;

    //优培主题
    @Column(name = "train_title")
    private String trainTitle;

    //开始日期
    @Column(name = "start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    //结束日期
    @Column(name = "end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    //是否删除（0否 | 1是）
    @Column(name = "is_deleted")
    private String isDeleted;

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
     * 设置：优培批次
     */
    public void setTrainBatch(String trainBatch) {
        this.trainBatch = trainBatch;
    }

    /**
     * 获取：优培批次
     */
    public String getTrainBatch() {
        return trainBatch;
    }

    /**
     * 设置：优培主题
     */
    public void setTrainTitle(String trainTitle) {
        this.trainTitle = trainTitle;
    }

    /**
     * 获取：优培主题
     */
    public String getTrainTitle() {
        return trainTitle;
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
