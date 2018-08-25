package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.SpecialEvent;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 专项管理
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-07 10:24:58
 */
public interface SpecialEventMapper extends CommonMapper<SpecialEvent> {
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(@Param("ids")Integer[] ids, @Param("updUserId")String updUserId, @Param("updUserName")String updUserName, @Param("updTime")Date updTime);
}
