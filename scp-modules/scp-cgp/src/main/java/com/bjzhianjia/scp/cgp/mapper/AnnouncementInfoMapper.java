package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.AnnouncementInfo;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 公告信息
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-30 22:12:53
 */
public interface AnnouncementInfoMapper extends CommonMapper<AnnouncementInfo> {
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(@Param("ids")Integer[] ids, @Param("updUserId")String updUserId, @Param("updUserName")String updUserName, @Param("updTime")Date updTime);
}
