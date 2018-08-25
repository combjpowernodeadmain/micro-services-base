package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 立案信息当事人
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-16 19:16:02
 */
public interface ConcernedPersonMapper extends CommonMapper<ConcernedPerson> {
	public int insertConcernPersonList(@Param("concernedPersonList")List<ConcernedPerson> concernedPersonList);
}
