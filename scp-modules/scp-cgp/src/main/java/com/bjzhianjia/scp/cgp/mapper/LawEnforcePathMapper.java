package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.LawEnforcePath;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 执法轨迹记录
 * 
 * @author chenshuai
 * @email
 * @version 2018-09-04 10:47:26
 */
public interface LawEnforcePathMapper extends CommonMapper<LawEnforcePath> {

    public List<LawEnforcePath> getByUserIds(@Param("userIds") String useIds);
}
