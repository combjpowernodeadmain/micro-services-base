package com.bjzhianjia.scp.cgp.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.CaseInfoService;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

/**
 * CaseInfoController 事件控制器.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月9日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@RestController
@RequestMapping("caseInfo")
@CheckClientToken
@CheckUserToken
@Api(tags = "事件管理")
@Slf4j
public class CaseInfoController extends BaseController<CaseInfoBiz, CaseInfo, Integer> {

    @Autowired
    private CaseInfoService caserInfoService;

    @Autowired
    private CaseInfoBiz caseInfoBiz;

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ApiOperation("更新单个对象")
    private ObjectRestResponse<CaseInfo> update(@RequestBody @Validated @ApiParam(name = "待更新对象实例") CaseInfo caseInfo,
        BindingResult bindingResult) {
        ObjectRestResponse<CaseInfo> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<Void> result = caserInfoService.update(caseInfo);
        if (!result.getIsSuccess()) {

        }

        restResult.setStatus(400);
        restResult.setMessage("成功");
        return null;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("分页获取对象")
    public TableResultResponse<CaseInfo> page(
        @RequestParam(value = "gridId", required = false) @ApiParam("网格ID") Integer gridId,
        @RequestParam(value = "regulaObjList", required = false) @ApiParam("监管对象") String regulaObjList,
        @RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit) {
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setGrid(gridId);
        caseInfo.setRegulaObjList(regulaObjList);
        TableResultResponse<CaseInfo> restResult = caserInfoService.getList(caseInfo, page, limit, false);

        return restResult;
    }

    @RequestMapping(value = "/list/unFinish/{id}", method = RequestMethod.GET)
    @ApiOperation("分页获取对象")
    public TableResultResponse<CaseInfo> pageNoFinish(@PathVariable("id") Integer id,
        @RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit) {
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setId(id);

        TableResultResponse<CaseInfo> restResult = caserInfoService.getList(caseInfo, page, limit, true);

        return restResult;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation("获取单个对象")
    public ObjectRestResponse<CaseInfo> get(@PathVariable("id") @ApiParam(name = "待查询对象ID") Integer id) {
        return caserInfoService.get(id);
    }

    /**
     * 查询用户待办流程任务列表
     * 
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("查询我的待办")
    @ResponseBody
    @RequestMapping(value = { "/userToDoTasks" }, method = RequestMethod.POST)
    public TableResultResponse<JSONObject> getUserToDoTasks(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询用户待办任务列表...");

        TableResultResponse<JSONObject> userToDoTasks = caserInfoService.getUserToDoTasks(objs);
        return userToDoTasks;
    }

    /**
     * 查询用户待办流程任务列表
     * 
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("查询所有待办")
    @ResponseBody
    @RequestMapping(value = { "/allToDoTasks" }, method = RequestMethod.POST)
    public TableResultResponse<JSONObject> getAllToDoTasks(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询所有待办任务列表...");

        TableResultResponse<JSONObject> userToDoTasks = caserInfoService.getAllToDoTasks(objs);
        return userToDoTasks;
    }

    /**
     * 查询流程任务列表
     * 
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("综合查询列表")
    @ResponseBody
    @RequestMapping(value = { "/allTasks" }, method = RequestMethod.POST)
    public TableResultResponse<JSONObject> getAllTasks(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始查询所有任务列表...");

        TableResultResponse<JSONObject> tasks = caserInfoService.getAllTasks(objs);
        return tasks;
    }

    @ApiOperation("完成任务")
    @ResponseBody
    @RequestMapping(value = { "/completeProcess" }, method = RequestMethod.POST)
    public ObjectRestResponse<JSONObject> completeProcess(@RequestBody JSONObject objs, HttpServletRequest request) {
        log.debug("SCP信息---开始启动并提交工作流...");

        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();
        caserInfoService.completeProcess(objs);

        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/get/userToDoTask", method = RequestMethod.POST)
    @ApiOperation("查询详细待办任务")
    public ObjectRestResponse<JSONObject> getUserToDoTask(@RequestBody JSONObject objs, HttpServletRequest request) {

        return caserInfoService.getUserToDoTask(objs);
    }

    /**
     * 通过流程任务ID终止当前流程任务，终止的流程可在我的流程中查询到
     * 
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("中止任务")
    @RequestMapping(value = { "/endProcess" }, method = RequestMethod.POST)
    public ObjectRestResponse<JSONObject> endProcessInstance(@RequestBody JSONObject objs, HttpServletRequest reques) {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

        caserInfoService.endProcess(objs);

        restResult.setMessage("成功");
        return restResult;
    }

    /**
     * 事件处理状态统计
     * 
     * @return
     */
    @ApiOperation("事件处理状态统计")
    @RequestMapping(value = "/statis/caseState", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONObject> getStatisCaseState(
        @RequestParam(defaultValue = "") @ApiParam("业务条线") String bizList,
        @RequestParam(defaultValue = "") @ApiParam("事件类别") String eventTypeList,
        @RequestParam(defaultValue = "") @ApiParam("网格范围") String gridIds,
        @RequestParam(defaultValue = "") @ApiParam("事件来源类型") String sourceType,
        @RequestParam(defaultValue = "") @ApiParam("事件级别") String caseLevel,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime) {
        ObjectRestResponse<JSONObject> result = new ObjectRestResponse<>();

        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setBizList(bizList);
        caseInfo.setEventTypeList(eventTypeList);
        caseInfo.setCaseLevel(caseLevel);
        if (StringUtils.isNotBlank(endTime)) {
            Date _end = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtils.addDays(_end, 1);
            endTime = DateUtil.dateFromDateToStr(_end, "yyyy-MM-dd HH:mm:ss");
        }
        JSONObject data = caseInfoBiz.getStatisCaseState(caseInfo, startTime, endTime,gridIds);
        if (data != null) {
            result.setData(data);
        } else {
            result.setStatus(400);
        }

        return result;
    }

    /**
     * 事件来源分布统计
     * 
     * @return
     */
    @ApiOperation("事件来源分布统计")
    @RequestMapping(value = "/statis/eventSource", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONArray> getStatisEventSource(
        @RequestParam(defaultValue = "") @ApiParam("业务条线") String bizList,
        @RequestParam(defaultValue = "") @ApiParam("事件类别") String eventTypeList,
        @RequestParam(defaultValue = "") @ApiParam("网格范围") String gridIds,
        @RequestParam(defaultValue = "") @ApiParam("事件级别") String caseLevel,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime) {
        ObjectRestResponse<JSONArray> result = new ObjectRestResponse<>();

        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setBizList(bizList);
        caseInfo.setEventTypeList(eventTypeList);
        caseInfo.setCaseLevel(caseLevel);
        if (StringUtils.isNotBlank(endTime)) {
            Date _end = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtils.addDays(_end, 1);
            endTime = DateUtil.dateFromDateToStr(_end, "yyyy-MM-dd HH:mm:ss");
        }
        JSONArray data = caseInfoBiz.getStatisEventSource(caseInfo, startTime, endTime,gridIds);
        if (data != null) {
            result.setData(data);
        } else {
            result.setStatus(400);
        }
        return result;
    }

    /**
     * 事件量趋势统计
     * 
     * @return
     */
    @ApiOperation("事件量趋势统计")
    @RequestMapping(value = "/statis/caseInfo", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<Map<String,Object>>> getStatisCaseInfo(
        @RequestParam(defaultValue = "") @ApiParam("业务条线") String bizList,
        @RequestParam(defaultValue = "") @ApiParam("事件类别") String eventTypeList,
        @RequestParam(defaultValue = "") @ApiParam("网格范围") String gridIds,
        @RequestParam(defaultValue = "") @ApiParam("事件级别") String caseLevel,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime) {
        
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setBizList(bizList);
        caseInfo.setEventTypeList(eventTypeList);
        caseInfo.setCaseLevel(caseLevel);
        
        Date _start = null;
        Date _end = null;
        if (StringUtils.isNotBlank(endTime) && StringUtils.isNotBlank(startTime)) {
            _start = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtils.addDays(_end, 1);
        }
      
        ObjectRestResponse<List<Map<String,Object>>> result = new ObjectRestResponse<>();
        
        List<Map<String,Object>>  data = caseInfoBiz.getStatisCaseInfo(caseInfo, _start, _end,gridIds);
        result.setData(data);
        
        return result;
    }
    /**
     *  业务条线分布
     * 
     * @return
     */
    @ApiOperation("业务条线分布")
    @RequestMapping(value = "/statis/bizLine", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONArray> getStatisBizLine(
        @RequestParam(defaultValue = "") @ApiParam("事件来源") String sourceType,
        @RequestParam(defaultValue = "") @ApiParam("网格等级") String gridLevel,
        @RequestParam(defaultValue = "") @ApiParam("事件级别") String caseLevel,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime) {
        
        ObjectRestResponse<JSONArray> result = new ObjectRestResponse<>();

        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setSourceType(sourceType);
        caseInfo.setCaseLevel(caseLevel);
        if (StringUtils.isNotBlank(endTime)) {
            Date _end = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtils.addDays(_end, 1);
            endTime = DateUtil.dateFromDateToStr(_end, "yyyy-MM-dd HH:mm:ss");
        }
        JSONArray data = caseInfoBiz.getStatisBizLine(caseInfo,gridLevel, startTime, endTime);

        if (data != null) {
            result.setData(data);
        } else {
            result.setStatus(400);
        }
        return result;
    }
    

    /**
     * 网格事件统计
     * 
     * @return
     */
    @ApiOperation("网格事件统计")
    @RequestMapping(value = "/statis/grid", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<Map<String,Object>> getGrid(
        @RequestParam(defaultValue = "") @ApiParam("业务条线") String bizList,
        @RequestParam(defaultValue = "") @ApiParam("事件类别") String eventTypeList,
        @RequestParam(defaultValue = "") @ApiParam("网格等级") String gridLevel,
        @RequestParam(defaultValue = "") @ApiParam("事件来源") String sourceType,
        @RequestParam(defaultValue = "") @ApiParam("事件级别") String caseLevel,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime,
        @RequestParam(defaultValue = "0") @ApiParam("页码") Integer page,
        @RequestParam(defaultValue = "10") @ApiParam("页容量") Integer limit) {

        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setBizList(bizList);
        caseInfo.setEventTypeList(eventTypeList);
        caseInfo.setCaseLevel(caseLevel);
        caseInfo.setSourceType(sourceType);
        caseInfo.setCaseLevel(caseLevel);
        
        if (StringUtils.isNotBlank(endTime)) {
            Date _end = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtils.addDays(_end, 1);
            endTime = DateUtil.dateFromDateToStr(_end, "yyyy-MM-dd HH:mm:ss");
        }
        TableResultResponse<Map<String,Object>> result  = caseInfoBiz.getGrid(caseInfo, gridLevel, startTime, endTime, page, limit);
        return result;
    }
    
}