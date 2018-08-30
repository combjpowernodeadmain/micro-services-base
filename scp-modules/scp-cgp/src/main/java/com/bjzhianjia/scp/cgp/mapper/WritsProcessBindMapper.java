package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.WritsProcessBind;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 文书与流程绑定
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
public interface WritsProcessBindMapper extends CommonMapper<WritsProcessBind> {
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(@Param("ids")Integer[] ids, @Param("updUserId")String updUserId, @Param("updUserName")String updUserName, @Param("updTime")Date updTime);
}
