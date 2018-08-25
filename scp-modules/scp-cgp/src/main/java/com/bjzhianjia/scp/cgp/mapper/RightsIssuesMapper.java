package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 权利事项数据访问接口
 * 
 * @author zzh
 *
 */
//@Tenant
public interface RightsIssuesMapper extends CommonMapper<RightsIssues> {

	/**
	 * 批量删除
	 * 
	 * @param ids id列表
	 */
	public void deleteByIds(@Param("ids") Integer[] ids, @Param("updUserId") String updUserId,
			@Param("updUserName") String updUserName, @Param("updTime") Date updTime);
}
