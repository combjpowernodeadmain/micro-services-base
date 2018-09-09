package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.PatrolTaskPath;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * PatrolTaskPathMapper 巡查轨迹记录.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月6日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
public interface PatrolTaskPathMapper extends CommonMapper<PatrolTaskPath> {
    public List<PatrolTaskPath> getByUserIds(@Param("userIds")String useIds);
}
