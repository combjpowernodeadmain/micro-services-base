package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

import tk.mybatis.mapper.common.ids.DeleteByIdsMapper;

/**
 * 执法证管理
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
public interface EnforceCertificateMapper extends CommonMapper<EnforceCertificate>, DeleteByIdsMapper<EnforceCertificate>  {
	
}
