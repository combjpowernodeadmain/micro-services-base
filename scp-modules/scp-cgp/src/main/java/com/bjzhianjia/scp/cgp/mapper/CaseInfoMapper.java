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
     * @param caseInfo  查询条件
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param gridIds  网格范围ids
     * @return
     *         source_type1=count1，
     *         source_type2=count2，
     *         source_type3=count3
     */
    public List<Map<String, Object>> getStatisEventSource(@Param("caseInfo") CaseInfo caseInfo,
        @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("gridIds") String gridIds);

    /**
     * 事件量趋势统计
     * 
     * @param caseInfo 查询条件
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param gridIds  网格范围ids
     * @return
     */
    public List<Map<String, Object>> getStatisCaseInfo(@Param("caseInfo") CaseInfo caseInfo,
        @Param("startTime") String startTime, @Param("endTime") String endTime ,@Param("gridIds") String gridIds);

    /**
     * 事件超时统计
     * @param caseInfo 查询条件
     * @param startTime  开始时间
     * @param endTime   结束时间
     * @return 超时的事件个数
     */
    public Integer selectOvertime(@Param("caseInfo") CaseInfo caseInfo, @Param("startTime") String startTime,
        @Param("endTime") String endTime , @Param("gridIds") String gridIds);

    /**
     * 事件处理状态统计
     * @param caseInfo 查询条件
     * @param startTime  开始时间
     * @param endTime   结束时间
     * @param gridIds  网格范围ids
     * @return
     *         处理中、已完成、已终止
     */
    public List<Map<String, Integer>> selectState(@Param("caseInfo") CaseInfo caseInfo,
        @Param("startTime") String startTime, @Param("endTime") String endTime,@Param("gridIds") String gridIds);

    /**
     * 业务条线分布统计 
     * @param caseInfo 查询条件
     * @param gridIds 网格ids
     * @param startTime  开始时间
     * @param endTime   结束时间
     * @return
     */
    public List<Map<String ,Object>> selectBizLine(@Param("caseInfo") CaseInfo caseInfo,@Param("gridIds")String gridIds,
        @Param("startTime") String startTime, @Param("endTime") String endTime);
    
    /**
     * 网格事件统计 
     * @param caseInfo 查询条件
     * @param gridIds 网格ids
     * @param startTime  开始时间
     * @param endTime   结束时间
     * @return
     */
    public List<Map<String ,Object>> selectGrid(@Param("caseInfo") CaseInfo caseInfo,@Param("startTime") String startTime, 
        @Param("endTime") String endTime,@Param("gridIds")String gridIds);
    
}
