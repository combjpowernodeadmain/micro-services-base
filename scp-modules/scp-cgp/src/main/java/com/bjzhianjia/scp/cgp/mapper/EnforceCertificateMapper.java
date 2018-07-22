package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 执法证管理
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
public interface EnforceCertificateMapper extends CommonMapper<EnforceCertificate>  {
	
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	public void deleteByIds(@Param("ids")Integer[] ids, @Param("updUserId")String updUserId, @Param("updUserName")String updUserName, @Param("updTime")Date updTime);
}
