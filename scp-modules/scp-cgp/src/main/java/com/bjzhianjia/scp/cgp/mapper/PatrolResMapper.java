package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.PatrolRes;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 巡查任务资料关系表
 * 
 * @author bo
 */
public interface PatrolResMapper extends CommonMapper<PatrolRes> {
    /**
     * 批量添加记录
     * @param patrolResList
     */
    void insertList(@Param("patrolResList") List<PatrolRes> patrolResList);
}
