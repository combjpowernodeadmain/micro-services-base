package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.ApiOperation;

import com.bjzhianjia.scp.cgp.biz.AttendanceInfoBiz;
import com.bjzhianjia.scp.cgp.entity.AttendanceInfo;
import com.bjzhianjia.scp.cgp.service.AttendanceInfoService;
import com.bjzhianjia.scp.cgp.vo.AttendanceVo;

import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("attendanceInfo")
@CheckClientToken
@CheckUserToken
public class AttendanceInfoController extends BaseController<AttendanceInfoBiz,AttendanceInfo,Integer> {

	@Autowired
	private AttendanceInfoService attendanceInfoService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public TableResultResponse<AttendanceVo> page(@RequestParam(defaultValue = "10") int limit
    				,@RequestParam(defaultValue = "1") int page
    				,@RequestParam(defaultValue = "") String name
    				,@RequestParam(defaultValue = "") String userName
    				,@RequestParam(defaultValue = "") String departId
    				,@RequestParam(defaultValue = "") String status
    				,@RequestParam(defaultValue = "") String startDate
    				,@RequestParam(defaultValue = "") String endDate) {
	    
		Map<String, Object> map = new HashMap<>();
		map.put("limit", limit);
		int startIndex = (page-1)*limit;
		map.put("startIndex", startIndex);
		map.put("name", name);
		map.put("account", userName);
		map.put("status", status);
		map.put("departId", departId);
		map.put("startDate", startDate);	
		map.put("endDate", endDate);
	    return attendanceInfoService.getList(map);
	    
    }
}