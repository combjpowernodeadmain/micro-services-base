package com.bjzhianjia.scp.cgp.biz;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PatrolRes;
import com.bjzhianjia.scp.cgp.mapper.PatrolResMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

/**
 * 巡查任务资料关系表
 *
 * @author bo
 */
@Service
public class PatrolResBiz extends BusinessBiz<PatrolResMapper,PatrolRes> {
}