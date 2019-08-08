package com.bjzhianjia.scp.party.biz;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.TrainGroupMember;
import com.bjzhianjia.scp.party.mapper.TrainGroupMemberMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

/**
 * 百人优培组和党员关系表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Service
public class TrainGroupMemberBiz extends BusinessBiz<TrainGroupMemberMapper, TrainGroupMember> {
}