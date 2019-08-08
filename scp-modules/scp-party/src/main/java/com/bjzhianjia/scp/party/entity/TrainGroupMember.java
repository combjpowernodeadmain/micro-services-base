package com.bjzhianjia.scp.party.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 百人优培组和党员关系表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Table(name = "train_group_member")
public class TrainGroupMember implements Serializable {
    private static final long serialVersionUID = 1L;

    //id
    @Id
    private Integer id;

    //优培分组id
    @Column(name = "train_group_id")
    private Integer trainGroupId;

    //党员id
    @Column(name = "party_member_id")
    private Integer partyMemberId;

    //组内角色code
    @Column(name = "party_member_role")
    private String partyMemberRole;

    //下标位置
    @Column(name = "sort_index")
    private Integer sortIndex;


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

    /**
     * 设置：党员id
     */
    public void setPartyMemberId(Integer partyMemberId) {
        this.partyMemberId = partyMemberId;
    }

    /**
     * 获取：党员id
     */
    public Integer getPartyMemberId() {
        return partyMemberId;
    }

    /**
     * 设置：组内角色code
     */
    public void setPartyMemberRole(String partyMemberRole) {
        this.partyMemberRole = partyMemberRole;
    }

    /**
     * 获取：组内角色code
     */
    public String getPartyMemberRole() {
        return partyMemberRole;
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
