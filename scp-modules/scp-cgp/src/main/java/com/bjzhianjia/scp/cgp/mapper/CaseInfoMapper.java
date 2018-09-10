package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * CaseInfoMapper 预立案信息.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年8月8日          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author bo
 *
 */
public interface CaseInfoMapper extends CommonMapper<CaseInfo> {

    /**
     * 事件来源分布统计
     * 
     * @param caseInfo 查询条件
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     *     source_type1=count1，
     *     source_type2=count2，
     *     source_type3=count3
     */
    public List<Map<String, Object>> getStatisEventSource(@Param("caseInfo") CaseInfo caseInfo,
        @Param("startTime") String startTime, @Param("endTime") String endTime);
    
    /**
     * 事件量趋势统计
     * 
     * @param caseInfo 查询条件
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public List<Map<String, Object>> getStatisCaseInfo(@Param("caseInfo") CaseInfo caseInfo,
        @Param("startTime") String startTime, @Param("endTime") String endTime);
}
