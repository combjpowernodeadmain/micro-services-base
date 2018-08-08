package com.bjzhianjia.scp.cgp.biz;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import com.bjzhianjia.scp.cgp.mapper.MayorHotlineMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

/**
 * 记录来自市长热线的事件
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-09 00:06:36
 */
@Service
public class MayorHotlineBiz extends BusinessBiz<MayorHotlineMapper,MayorHotline> {
}