package com.bjzhianjia.scp.cgp.mapper;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 事件类型
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
public interface EventTypeMapper extends CommonMapper<EventType> {
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(@Param("ids")Integer[] ids);
}
