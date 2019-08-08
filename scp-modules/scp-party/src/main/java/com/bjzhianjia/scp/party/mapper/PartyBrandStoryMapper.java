package com.bjzhianjia.scp.party.mapper;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.entity.PartyBrandStory;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 党建品牌故事（活动）表
 *
 * @author bo
 * @version 2019-03-31 11:36:17
 * @email 576866311@qq.com
 */
public interface PartyBrandStoryMapper extends CommonMapper<PartyBrandStory> {

    /**
     * 党建品牌名称以及照片地址
     *
     * @param state
     * @param isdel
     * @return
     * @author gt
     */
    public List<JSONObject> getBrandByTimeAndState(@Param(value = "state") String state, @Param(value = "isdel") String isdel);

    /**
     * 对应党组织名称以及照片地址
     *
     * @param id
     * @param isNotState
     * @param isdel
     * @return
     */
    public List<JSONObject> getBrandByBrandId(@Param(value = "id") Integer id, @Param(value = "isNotState") String isNotState, @Param(value = "isdel") String isdel);

    /**
     * 党建活动当天记录数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public Map<String, Object> getBrandStoryByTime(@Param(value = "startTime") String startTime, @Param(value = "endTime") String endTime);
}
