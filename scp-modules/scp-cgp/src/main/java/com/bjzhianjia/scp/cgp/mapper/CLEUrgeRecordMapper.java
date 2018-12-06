package com.bjzhianjia.scp.cgp.mapper;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.CLEUrgeRecord;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

import java.util.List;
import java.util.Set;


/**
 * CLEUrgeRecordMapper 案件催办记录表.
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
public interface CLEUrgeRecordMapper extends CommonMapper<CLEUrgeRecord> {
	List<CLEUrgeRecord> selectLastUrge(@Param("caseInfoIds") Set<String> caseInfoIds);
}
