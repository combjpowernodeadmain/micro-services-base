package com.bjzhianjia.scp.cgp.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.vo.PatrolRecordVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.PatrolRecordBiz;
import com.bjzhianjia.scp.cgp.entity.PatrolRecord;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.util.Date;

@RestController
@RequestMapping("patrolRecord")
@CheckClientToken
@CheckUserToken
@Api(tags = "巡查记录")
@Slf4j
public class PatrolRecordController extends BaseController<PatrolRecordBiz, PatrolRecord, Integer> {

    @PostMapping("/startPatrol")
    @ApiOperation("开始巡查")
    public ObjectRestResponse<PatrolRecord> startPatrolRecored() {
        log.debug("添加巡查记录--》开始巡查,巡查人" + BaseContextHandler.getUserID());
        return this.baseBiz.startPatrolRecored();
    }

    @PutMapping(value = "/endPatrol")
    @ApiOperation("结束巡查")
    public ObjectRestResponse<PatrolRecord> endPatrol() {
        log.debug("添加巡查记录--》结束巡查,巡查人" + BaseContextHandler.getUserID());
        return this.baseBiz.endPatrolRecored();
    }

    @GetMapping("/{userId}/patrol/unfinished/path")
    @ApiOperation("获取未完成的巡查轨迹")
    public TableResultResponse<JSONObject> unfinishedPatrolPath(@PathVariable("userId") String userId) {
        return this.baseBiz.unfinishedPatrolPath(userId);
    }

    @GetMapping("/{patrolRecordId}/finished/path")
    @ApiOperation("获取某人在某次已完成的巡查中（patrolRecordId）的巡查轨迹")
    public TableResultResponse<JSONObject> finishedPatrolPath(@PathVariable("patrolRecordId") String patrolRecordId) {
        return this.baseBiz.finishedPatrolPath(patrolRecordId);
    }

    @GetMapping("/current/list")
    @ApiOperation("获取当前人巡查记录")
    public TableResultResponse<PatrolRecordVo> unfinishedPatrol(
        @RequestParam(value = "patrolStatus", defaultValue = "") @ApiParam(value = "巡查状态") String patrolStatus,
        @RequestParam(value = "queryStartTime", defaultValue = "") @ApiParam(value = "按开始时间范围查询")
            String queryStartTimeStr,
        @RequestParam(value = "queryEndTime", defaultValue = "") @ApiParam(value = "按开始时间范围查询") String queryEndTimeStr,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        return this
            .getPatrolRecordList(BaseContextHandler.getUserID(), patrolStatus, queryStartTimeStr, queryEndTimeStr,"", page,
                limit);
    }

    @GetMapping("/list")
    @ApiOperation("巡查历史")
    public TableResultResponse<PatrolRecordVo> getPatrolRecordList(
        @RequestParam(value = "userId", defaultValue = "") @ApiParam(value = "巡查人员") String userId,
        @RequestParam(value = "patrolStatus", defaultValue = "") @ApiParam(value = "巡查状态") String patrolStatus,
        @RequestParam(value = "queryStartTime", defaultValue = "") @ApiParam(value = "按开始时间范围查询")
            String queryStartTimeStr,
        @RequestParam(value = "queryEndTime", defaultValue = "") @ApiParam(value = "按开始时间范围查询") String queryEndTimeStr,
        @RequestParam(value = "month",defaultValue = "") @ApiParam(value = "月度") String month,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        PatrolRecord patrolRecord = new PatrolRecord();
        patrolRecord.setCrtUserId(userId);
        patrolRecord.setPatrolStatus(patrolStatus);

        if (StringUtils.isNotBlank(month) && (StringUtils.isBlank(queryStartTimeStr) && StringUtils
            .isBlank(queryEndTimeStr))) {
            queryStartTimeStr = DateUtil.dateFromDateToStr(
                DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(DateUtil.dateFromStrToDate(month, "yyyy-MM"))),
                DateUtil.DEFAULT_DATE_FORMAT);
            queryEndTimeStr = DateUtil.dateFromDateToStr(DateUtil.getDayStartTime(
                DateUtil.theFirstDayOfMonth(DateUtil.theDayOfMonthPlus(DateUtil.dateFromStrToDate(month, "yyyy-MM"), 1)

                )), DateUtil.DEFAULT_DATE_FORMAT);
        }

        JSONObject addition = new JSONObject();
        addition.put("queryStartTimeStr", queryStartTimeStr);
        addition.put("queryEndTimeStr", queryEndTimeStr);

        return this.baseBiz.getPatrolRecordLiset(patrolRecord, addition, page, limit);
    }

    @GetMapping("/lastest")
    @ApiOperation("获取当前人员最新的一条记录")
    public ObjectRestResponse<PatrolRecord> getLastestPatrolRecord() {
        return this.baseBiz.getLastestPatrolRecord();
    }
}