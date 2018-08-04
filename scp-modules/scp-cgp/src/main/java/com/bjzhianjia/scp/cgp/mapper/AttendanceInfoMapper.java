package com.bjzhianjia.scp.cgp.mapper;

import java.util.List;
import java.util.Map;

import com.bjzhianjia.scp.cgp.entity.AttendanceInfo;
import com.bjzhianjia.scp.cgp.vo.AttendanceVo;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 考勤信息
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-30 22:12:53
 */
public interface AttendanceInfoMapper extends CommonMapper<AttendanceInfo> {
	
	public int getTotal(Map<String, Object> map);
	
	public List<AttendanceVo> getList(Map<String, Object> map);
}
