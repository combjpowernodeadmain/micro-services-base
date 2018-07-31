package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.AttendanceInfoBiz;
import com.bjzhianjia.scp.cgp.entity.AttendanceInfo;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("attendanceInfo")
@CheckClientToken
@CheckUserToken
public class AttendanceInfoController extends BaseController<AttendanceInfoBiz,AttendanceInfo,Integer> {

}