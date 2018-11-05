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
     * @param caseRegistration
     *            查询条件
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public List<Map<String, Object>> selectState(@Param("caseRegistration") CaseRegistration caseRegistration,
        @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("gridIds") String gridIds);

    /**
     * 案件来源分布
     * 
     * @param caseRegistration
     *            查询条件
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public List<Map<String, Object>> selectCaseSource(@Param("caseRegistration") CaseRegistration caseRegistration,
        @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("gridIds") String gridIds);

    /**
     * 案件业务条线分布
     * 
     * @param caseRegistration
     *            查询条件
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public List<Map<String, Object>> selectBizLine(@Param("caseRegistration") CaseRegistration caseRegistration,
        @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("gridIds") String gridIds);

    /**
     * 按处理方式对案件进行统计
     * 
     * @param parameters
     * @return
     */
    public JSONArray getStatisByDealType(Map<String, Object> parameters);

    /**
     * 按执法者对案件进行统计
     * 
     * @param parameters
     * @return
     */
    public JSONArray getStatisByDept(@Param("caseRegistration") CaseRegistration caseRegistration,
        @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("gridIds") String gridIds,@Param("deptParam")List<Map<String, String>> deptParam);
    
    /**
     * 案件违法类别统计
     * 
     * @param caseRegistration
     *            查询条件
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public List<Map<String,Object>> selectInspectItem(@Param("caseRegistration") CaseRegistration caseRegistration,
        @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("gridIds") String gridIds);

    /**
     * 查询执法人员部门列
     * @return 执法人员部门列
     */
    List<String> selectDistinctDeptId();

    /**
     * 通过用户id获取案件列表
     * @param userId
     * @return
     */
    List<Map<String,Object>> selectCaseLog(@Param("userId") String userId,@Param("caseName") String caseName);
}
