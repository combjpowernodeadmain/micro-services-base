package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;
import java.util.List;
import java.util.Set;

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

    /**
     * 获取userIds集合中所包含人员的定位信息
     * @param useIds
     * @param date
     * @return
     */
    List<LawEnforcePath> getListExcludeRole(@Param("userIdSet") Set<String> useIds, @Param("date") Date date);
}
