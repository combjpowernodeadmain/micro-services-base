package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.InspectItems;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 巡查事项
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-16 16:08:44
 */
@Tenant
public interface InspectItemsMapper extends CommonMapper<InspectItems> {

	/**
	 * 批量删除
	 * 
	 * @param ids id列表
	 */
	public void deleteByIds(@Param("ids") Integer[] ids, @Param("updUserId") String updUserId,
			@Param("updUserName") String updUserName, @Param("updTime") Date updTime);
}
