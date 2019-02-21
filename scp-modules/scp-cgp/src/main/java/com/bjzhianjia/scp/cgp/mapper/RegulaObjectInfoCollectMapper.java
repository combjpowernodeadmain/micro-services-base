package com.bjzhianjia.scp.cgp.mapper;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectInfoCollect;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

import java.util.List;

/**
 * 监管对象信息采集过程记录
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-11-22 17:30:13
 */
public interface RegulaObjectInfoCollectMapper extends CommonMapper<RegulaObjectInfoCollect> {
    /**
     * 按每件查询列表
     * @param queryJObj
     * @return
     */
	List<JSONObject> list(@Param("queryJObj")JSONObject queryJObj);

    void updateToFinished(@Param("objId") Integer objId);
}
