package com.bjzhianjia.scp.party.mapper;

import com.bjzhianjia.scp.party.entity.TrainActivityGroup;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 简讯活动和优培分组关系表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
public interface TrainActivityGroupMapper extends CommonMapper<TrainActivityGroup> {

    /**
     * 通过简讯活动id获取分组成员关系
     *
     * @param activityId 简讯活动id
     * @return
     */
    List<String> selectByTrainGroupNames(@Param("activityId") String activityId);
}
