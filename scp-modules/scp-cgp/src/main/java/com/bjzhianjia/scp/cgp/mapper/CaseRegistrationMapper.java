package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;
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
	
	/**
	 * 按处理方式对案件进行统计
	 * @param parameters
	 * @return
	 */
	public JSONArray getStatisByDealType(Map<String, Object> parameters); 
	/**
	 * 按执法者对案件进行统计
	 * @param parameters
	 * @return
	 */
	public JSONArray getStatisByDept(Map<String, Object> parameters); 
}
