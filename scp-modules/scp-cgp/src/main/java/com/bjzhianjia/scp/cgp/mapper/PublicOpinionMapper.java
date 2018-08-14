package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.PublicOpinion;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 记录来自舆情的事件
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-09 09:11:11
 */
public interface PublicOpinionMapper extends CommonMapper<PublicOpinion> {
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	void deleteByIds(@Param("ids")Integer[] ids, @Param("updUserId")String updUserId, @Param("updUserName")String updUserName, @Param("updTime")Date updTime);
}
