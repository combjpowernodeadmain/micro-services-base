package com.bjzhianjia.scp.party.mapper;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.entity.Train;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 百人优培批次表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
public interface TrainMapper extends CommonMapper<Train> {
    /**
     * 获取百人优培列表
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    List<Map<String, Object>> selectTrains(@Param("startDate") String startDate,
                                           @Param("endDate") String endDate);

    /**
     * 通过优培批次获取分组和成员信息
     *
     * @param trainId 优培批次id
     * @return
     */
    List<JSONObject> selectTrainGroupMember(@Param("trainId") String trainId);
}
