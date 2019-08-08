package com.bjzhianjia.scp.party.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 百人优培批注分组表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Table(name = "train_group")
public class TrainGroup implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //分组名称
    @Column(name = "train_group_name")
    private String trainGroupName;

    //优培批次id
    @Column(name = "train_id")
    private String trainId;

    //下标位置
    @Column(name = "sort_index")
    private Integer sortIndex;


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
     * 设置：分组名称
     */
    public void setTrainGroupName(String trainGroupName) {
        this.trainGroupName = trainGroupName;
    }

    /**
     * 获取：分组名称
     */
    public String getTrainGroupName() {
        return trainGroupName;
    }

    /**
     * 设置：优培批次id
     */
    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    /**
     * 获取：优培批次id
     */
    public String getTrainId() {
        return trainId;
    }

    /**
     * 获取：下标位置
     */
    public Integer getSortIndex() {
        return sortIndex;
    }

    /**
     * 设置：下标位置
     */
    public void setSortIndex(Integer sortIndex) {
        this.sortIndex = sortIndex;
    }
}
