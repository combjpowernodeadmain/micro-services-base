package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.AttendanceInfoBiz;
import com.bjzhianjia.scp.cgp.entity.AttendanceInfo;
import com.bjzhianjia.scp.cgp.service.AttendanceInfoService;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.vo.AttendanceVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

/**
 * 考勤前端控制器
 * 
 * @author admin
 *
 */
@RestController
@RequestMapping("attendanceInfo")
@CheckClientToken
@CheckUserToken
@Api(value = "考勤信息")
public class AttendanceInfoController extends BaseController<AttendanceInfoBiz, AttendanceInfo, Integer> {

    @Autowired
    private AttendanceInfoService attendanceInfoService;

    @Autowired
    private AttendanceInfoBiz attendanceInfoBiz;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public TableResultResponse<AttendanceVo> page(@RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "") String name,
        @RequestParam(defaultValue = "") String userName, @RequestParam(defaultValue = "") String departId,
        @RequestParam(defaultValue = "") String status, @RequestParam(defaultValue = "") String startDate,
        @RequestParam(defaultValue = "") String endDate) {
        // By尚
        if (StringUtils.isBlank(startDate) || StringUtils.isBlank(endDate)) {
            endDate = DateUtil.dateFromDateToStr(DateUtils.addDays(new Date(), 1), "yyyy-MM-dd");
            Date _startDate = DateUtils.addDays(new Date(), -6);
            startDate = DateUtil.dateFromDateToStr(_startDate, "yyyy-MM-dd");
        } else {
            // 查询结束日期结止至当天24点，即次日0点
            endDate =
                DateUtil.dateFromDateToStr(DateUtils.addDays(DateUtil.dateFromStrToDate(endDate, "yyyy-MM-dd"), 1),
                    "yyyy-MM-dd");
        }

        Map<String, Object> map = new HashMap<>();
        map.put("limit", limit);
        int startIndex = (page - 1) * limit;
        map.put("startIndex", startIndex);
        map.put("name", name);
        map.put("account", userName);
        map.put("status", status);
        map.put("departId", departId);
        map.put("startDate", startDate);
        map.put("endDate", endDate);
        return attendanceInfoService.getList(map);

    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户签到")
    public ObjectRestResponse<AttendanceInfo> add(
        @RequestBody @Validated @ApiParam(name = "待添加对象实例") AttendanceInfo attendanceInfo,
        BindingResult bindingResult) {
        ObjectRestResponse<AttendanceInfo> restResult = new ObjectRestResponse<>();
        restResult.setStatus(400);
        if (bindingResult.hasErrors()) {
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }
        attendanceInfo.setCrtUsername(BaseContextHandler.getUsername());
        attendanceInfoBiz.insertSelective(attendanceInfo);

        restResult.setMessage("成功");
        restResult.setStatus(200);
        return restResult;

    }

    @GetMapping("/user/list")
    @ApiOperation("获取当前登陆人的考勤记录")
    public TableResultResponse<AttendanceVo> getUserAttendanceInfo(
        @RequestParam(defaultValue = "10") @ApiParam("页容量")int limit,
        @RequestParam(defaultValue = "1") @ApiParam("当前页")int page,
        @RequestParam(defaultValue = "") @ApiParam("查询开始日期")String startDate,
        @RequestParam(defaultValue = "") @ApiParam("查询结束日期")String endDate){
        return attendanceInfoService.getUserAttendanceInfo(page, limit, startDate, endDate);
    }
}