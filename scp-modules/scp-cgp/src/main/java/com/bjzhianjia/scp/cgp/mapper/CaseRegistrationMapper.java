package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.vo.CaseRegistrationVo;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * CaseRegistrationMapper 综合执法 - 案件登记.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年8月26日          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author bo
 *
 */
public interface CaseRegistrationMapper extends CommonMapper<CaseRegistration> {
	public List<CaseRegistrationVo> getListByIds(@Param("ids") Set<String> ids, @Param("page") int page,
			@Param("limit") int limit);
	/**
     * 案件处理类型统计
     * 
     * @param caseRegistration  查询条件
     * @param startTime 开始时间
     * @param endTime 结束时间
	 * @return
	 */
	public List<Map<String,Object>> selectState(@Param("caseRegistration") CaseRegistration caseRegistration,
        @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("gridIds") String gridIds);
	
	 /**
     * 案件来源分布
     * 
     * @param caseRegistration  查询条件
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<Map<String,Object>> selectCaseSource(@Param("caseRegistration") CaseRegistration caseRegistration,
        @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("gridIds") String gridIds);
    
    /**
     * 案件业务条线分布
     * 
     * @param caseRegistration  查询条件
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<Map<String,Object>> selectBizLine(@Param("caseRegistration") CaseRegistration caseRegistration,
        @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("gridIds") String gridIds);
}
