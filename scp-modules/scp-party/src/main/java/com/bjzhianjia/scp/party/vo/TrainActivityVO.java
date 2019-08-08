package com.bjzhianjia.scp.party.vo;

import com.bjzhianjia.scp.party.entity.TrainActivity;

/**
 * TrainActivityVO .
 *
 * @author cs
 * @version 1.0
 * @date 2019-05-30 12:00
 * @description
 */
public class TrainActivityVO extends TrainActivity {
    /**
     * 分组ids
     */
    public String groupIds;

    /**
     * 获取：分组ids
     */
    public String getGroupIds() {
        return groupIds;
    }

    /**
     * 设置：分组ids
     */
    public void setGroupIds(String groupIds) {
        this.groupIds = groupIds;
    }

}
