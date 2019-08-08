package com.bjzhianjia.scp.party.biz;

import com.bjzhianjia.scp.security.common.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.TrainActivityGroup;
import com.bjzhianjia.scp.party.mapper.TrainActivityGroupMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import java.util.ArrayList;
import java.util.List;

/**
 * 简讯活动和优培分组关系表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Service
public class TrainActivityGroupBiz extends BusinessBiz<TrainActivityGroupMapper, TrainActivityGroup> {
    /**
     * 通过id删除分组信息
     *
     * @param trainActivityId 简讯活动id
     * @return
     */
    public int delByTrainActivityId(String trainActivityId) {
        if (StringUtils.isBlank(trainActivityId)) {
            return 0;
        }
        TrainActivityGroup trainActivityGroup = new TrainActivityGroup();
        trainActivityGroup.setTrainActivityId(trainActivityId);
        return this.mapper.delete(trainActivityGroup);
    }

    /**
     * 通过简讯id查询分组信息
     *
     * @param trainActivityId 简讯活动id
     * @return
     */
    public List<TrainActivityGroup> getByTrainActivityId(String trainActivityId) {
        if (StringUtils.isBlank(trainActivityId)) {
            return new ArrayList<>();
        }
        TrainActivityGroup trainActivityGroup = new TrainActivityGroup();
        trainActivityGroup.setTrainActivityId(trainActivityId);
        List<TrainActivityGroup> resultData = this.mapper.select(trainActivityGroup);
        if (BeanUtils.isNotEmpty(resultData)) {
            return resultData;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 批量添加简讯活动和分组关系
     *
     * @param trainActivityId 简讯活动id
     * @param groupIds        分组id
     */
    public void addTrainActivityGroups(String trainActivityId, List<String> groupIds) {
        if (BeanUtils.isNotEmpty(groupIds)) {
            for (String id : groupIds) {
                if (StringUtils.isNotBlank(id)) {
                    TrainActivityGroup trainActivityGroup = new TrainActivityGroup();
                    trainActivityGroup.setTrainActivityId(trainActivityId);
                    trainActivityGroup.setTrainGroupId(Integer.valueOf(id));
                    this.mapper.insertSelective(trainActivityGroup);
                }
            }
        }
    }

    /**
     * 通过简讯活动id获取分组成员关系
     *
     * @param activityId 简讯活动id
     * @return
     */
    public List<String> getByTrainGroupNames(String activityId) {
        if (StringUtils.isBlank(activityId)) {
            return new ArrayList<>();
        }
        return this.mapper.selectByTrainGroupNames(activityId);
    }
}