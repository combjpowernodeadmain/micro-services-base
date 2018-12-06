package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.UrgeRecord;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 催办记录表
 * 
 * @author chenshuai
 * @email 
 * @version 2018-08-26 09:02:28
 */
public interface UrgeRecordMapper extends CommonMapper<UrgeRecord> {

    List<UrgeRecord> selectLastUrge(@Param("caseInfoIds") Set<String> caseInfoIds);
}
