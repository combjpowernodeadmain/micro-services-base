package com.bjzhianjia.scp.party.mapper;

import com.bjzhianjia.scp.party.entity.TrainGroup;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 百人优培批注分组表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
public interface TrainGroupMapper extends CommonMapper<TrainGroup> {
    /**
     * 通过优培批次id删除分组和成员关系
     *
     * @param trainId 优培批次id
     * @return
     */
    Integer deleteByTrainId(@Param("trainId") String trainId);
}
