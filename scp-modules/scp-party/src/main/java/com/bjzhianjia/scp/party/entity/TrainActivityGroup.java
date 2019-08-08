package com.bjzhianjia.scp.party.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 简讯活动和优培分组关系表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Table(name = "train_activity_group")
public class TrainActivityGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    //id
    @Id
    private Integer id;

    //简讯活动id
    @Column(name = "train_activity_id")
    private String trainActivityId;

    //优培分组id
    @Column(name = "train_group_id")
    private Integer trainGroupId;


    /**
     * 设置：id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：id
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置：简讯活动id
     */
    public void setTrainActivityId(String trainActivityId) {
        this.trainActivityId = trainActivityId;
    }

    /**
     * 获取：简讯活动id
     */
    public String getTrainActivityId() {
        return trainActivityId;
    }

    /**
     * 设置：优培分组id
     */
    public void setTrainGroupId(Integer trainGroupId) {
        this.trainGroupId = trainGroupId;
    }

    /**
     * 获取：优培分组id
     */
    public Integer getTrainGroupId() {
        return trainGroupId;
    }
}
