package com.bjzhianjia.scp.cgp.biz;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.AttendanceInfo;
import com.bjzhianjia.scp.cgp.mapper.AttendanceInfoMapper;
import com.bjzhianjia.scp.cgp.vo.AttendanceVo;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;


/**
 * 考勤信息
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-30 22:12:53
 */
@Service
public class AttendanceInfoBiz extends BusinessBiz<AttendanceInfoMapper,AttendanceInfo> {
	@Autowired
	private AttendanceInfoMapper attendanceInfoMapper;
	
	/**
	 * 根据查询条件搜索
	 * @param eventType
	 * @return
	 */
	public TableResultResponse<AttendanceVo> getList(Map<String,Object> map) {
		
	    int total = attendanceInfoMapper.getTotal(map);
	    List<AttendanceVo> list = attendanceInfoMapper.getList(map);
	    return new TableResultResponse<AttendanceVo>(total, list);
	}
}