package com.bjzhianjia.scp.cgp.biz;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

/**
 * 事件类型
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-07 16:48:27
 */
@Service
public class EventTypeBiz extends BusinessBiz<EventTypeMapper,EventType> {
}