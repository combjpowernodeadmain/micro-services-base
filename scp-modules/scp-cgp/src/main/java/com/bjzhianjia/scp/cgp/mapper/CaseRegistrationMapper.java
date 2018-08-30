package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.vo.CaseRegistrationVo;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 综合执法 - 案件登记
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:08
 */
public interface CaseRegistrationMapper extends CommonMapper<CaseRegistration> {
	public List<CaseRegistrationVo> getListByIds(@Param("ids") Set<String> ids, @Param("page") int page,
			@Param("limit") int limit);
}
