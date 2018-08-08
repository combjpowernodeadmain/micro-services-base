package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;


public interface RegulaObjectTypeMapper extends CommonMapper<RegulaObjectType> {
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	void deleteByIds(@Param("ids")Integer[] ids, @Param("updUserId")String updUserId, @Param("updUserName")String updUserName, @Param("updTime")Date updTime);
}