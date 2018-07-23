package com.bjzhianjia.scp.cgp.mapper;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.VhclManagement;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 车辆管理
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
@Tenant
public interface VhclManagementMapper extends CommonMapper<VhclManagement> {
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	void deleteByIds(@Param("ids")Integer[] ids);
}
