package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 企业信息
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
@Tenant
public interface EnterpriseInfoMapper extends CommonMapper<EnterpriseInfo> {
	/**
	 * 批量删除
	 * 
	 * @param ids id列表
	 */
	public void deleteByRegulaObjIds(@Param("regulaObjIds") Integer[] regulaObjIds, @Param("updUserId") String updUserId,
			@Param("updUserName") String updUserName, @Param("updTime") Date updTime);
}
