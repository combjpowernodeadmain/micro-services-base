package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.PopulationInfo;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-09-28 15:50:11
 */
public interface PopulationInfoMapper extends CommonMapper<PopulationInfo> {

    /**
     * 批量插入记录
     * 
     * @param populationInfoList
     */
    void insertPopulationInfoList(@Param("populationInfoList") List<PopulationInfo> populationInfoList);
    
    /**
     * 批量更新记录
     * @param populationInfoList
     */
    void updatePopulationInfoList(@Param("populationInfoList") List<PopulationInfo> populationInfoList);
}
