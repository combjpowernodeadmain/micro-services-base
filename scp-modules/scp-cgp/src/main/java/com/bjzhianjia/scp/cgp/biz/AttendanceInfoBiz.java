package com.bjzhianjia.scp.cgp.biz;

import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.AttendanceInfo;
import com.bjzhianjia.scp.cgp.mapper.AttendanceInfoMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

/**
 * 考勤信息
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-30 22:12:53
 */
@Service
public class AttendanceInfoBiz extends BusinessBiz<AttendanceInfoMapper,AttendanceInfo> {
}