package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 网格成员
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
//@Tenant
public interface AreaGridMemberMapper extends CommonMapper<AreaGridMember> {
	/**
	 * 批量插入网格成员
	 * @author 尚
	 * @param areaGridMembers
	 */
	void insertAreaGridMemberList(@Param("areaGridMembers") List<AreaGridMember> areaGridMembers);
	
	
	/**
	 * 按gridId删除网格成员
	 * @param gridId id列表
	 */
	void deleteByGridId(@Param("id")Integer gridId, @Param("updUserId")String updUserId, @Param("updUserName")String updUserName, @Param("updTime")Date updTime);
}
