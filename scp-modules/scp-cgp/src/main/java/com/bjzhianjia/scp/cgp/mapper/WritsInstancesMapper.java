package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.WritsInstances;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 文书模板实例
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
public interface WritsInstancesMapper extends CommonMapper<WritsInstances> {
    void insertWritsInstancesList(@Param("writsInstancesList")List<WritsInstances> writsInstancesList);
}
