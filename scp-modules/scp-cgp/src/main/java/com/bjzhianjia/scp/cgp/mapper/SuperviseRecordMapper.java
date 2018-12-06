package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.SuperviseRecord;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 督办记录
 * 
 * @author chenshuai
 * @email 
 * @version 2018-08-26 09:02:29
 */
public interface SuperviseRecordMapper extends CommonMapper<SuperviseRecord> {
	List<SuperviseRecord> selectLastSupervise(@Param("caseInfoIds") Set<String> caseInfoIds);
}
