package com.bjzhianjia.scp.party.mapper;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.entity.TrainActivity;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 百人优培简讯活动表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
public interface TrainActivityMapper extends CommonMapper<TrainActivity> {
    /**
     * 获取全部优培批次简讯
     *
     * @param trainId            优培批次id
     * @param startDate          开始日期
     * @param endDate            结束日期
     * @param trainActivityTitle 简讯名称
     * @return
     */
    List<JSONObject> selectTrainActivitys(@Param("trainId") String trainId, @Param("startDate") String startDate,
                                          @Param("endDate") String endDate,
                                          @Param("trainActivityTitle") String trainActivityTitle);
}
