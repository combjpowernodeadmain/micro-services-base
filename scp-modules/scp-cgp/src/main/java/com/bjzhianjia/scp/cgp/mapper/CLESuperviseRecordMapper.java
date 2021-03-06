package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.CLESuperviseRecord;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;


/**
 * CLESuperviseRecordMapper 案件督办记录.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月5日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
public interface CLESuperviseRecordMapper extends CommonMapper<CLESuperviseRecord> {
    List<CLESuperviseRecord> selectLastSupervise(@Param("caseInfoIds")Set<String> caseInfoIds);
}
