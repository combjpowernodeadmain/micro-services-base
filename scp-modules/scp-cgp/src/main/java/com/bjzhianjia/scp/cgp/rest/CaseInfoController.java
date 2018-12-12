package com.bjzhianjia.scp.cgp.rest;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bjzhianjia.scp.core.context.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit,
        @RequestParam(value = "caseCode", defaultValue = "") @ApiParam(name = "事件编号") String caseCode,
        @RequestParam(value = "caseTitle", defaultValue = "") @ApiParam(name = "事件标题") String caseTitle
        ) {
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setId(id);
        caseInfo.setCaseCode(caseCode);
        caseInfo.setCaseTitle(caseTitle);

        TableResultResponse<CaseInfo> restResult = caserInfoService.getList(caseInfo, page, limit, true);

        return restResult;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation("获取单个对象")
    public ObjectRestResponse<JSONObject> getInfo(@PathVariable("id") @ApiParam(name = "待查询对象ID") Integer id) {
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
        Result<Void> result = caserInfoService.completeProcess(objs);
        if(!result.getIsSuccess()){
            restResult.setMessage(result.getMessage());
            restResult.setStatus(400);
            return restResult;
        }

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
        caseInfo.setSourceType(sourceType);
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
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime,
        @RequestParam(defaultValue = "") @ApiParam("事件来源类型") String sourceType
        ) {
        
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setBizList(bizList);
        caseInfo.setEventTypeList(eventTypeList);
        caseInfo.setCaseLevel(caseLevel);
        caseInfo.setSourceType(sourceType);
        
        Calendar calendar =  Calendar.getInstance();
        String _startTime = startTime;
        String _endTime = endTime;
        //没有日期时，则默认设置为当年1月1日到当月-当日
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            //当年当月
            _endTime = DateUtil.dateFromDateToStr(calendar.getTime(),DateUtil.DEFAULT_DATE_FORMAT);
            //当年第一月第一天
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            _startTime = DateUtil.dateFromDateToStr(calendar.getTime(),DateUtil.DEFAULT_DATE_FORMAT);
        }
        //默认结束日期增加1天
        if (StringUtils.isNotBlank(startTime) || StringUtils.isNotBlank(endTime)) {
            calendar.setTime(DateUtil.dateFromStrToDate(startTime, DateUtil.DEFAULT_DATE_FORMAT));
            calendar.add(Calendar.DATE, 1); 
        }
      
        ObjectRestResponse<List<Map<String,Object>>> result = new ObjectRestResponse<>();
        
        List<Map<String,Object>>  data = caseInfoBiz.getStatisCaseInfo(caseInfo, _startTime, _endTime,gridIds);
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
    public TableResultResponse<JSONObject> getGrid(
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
        TableResultResponse<JSONObject> result  = caseInfoBiz.getGrid(caseInfo, gridLevel, startTime, endTime, page, limit);
        return result;
    }
    
    @RequestMapping(value = "/list/sourceType", method = RequestMethod.GET)
    @ApiOperation("分页获取对象")
    public TableResultResponse<CaseInfo> sourceType(
        @RequestParam(value = "sourceTypeKeyPatrol") @ApiParam("监管对象") String sourceTypeKeyPatrol,
        @RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit) {
        TableResultResponse<CaseInfo> restResult = caserInfoService.getListSourceType(sourceTypeKeyPatrol, page, limit, false);

        return restResult;
    }
    
    @GetMapping("/list/patrol")
    @ApiOperation("查询类型为专项管理的事件")
    public TableResultResponse<JSONObject> patrolCaseInfo(
        @RequestParam(value="patrolId") Integer patrolId
        ){
        return this.baseBiz.patrolCaseInfo(patrolId);
    }
    
    @RequestMapping(value="/instance/{id}",method=RequestMethod.GET)
    @ApiOperation("按ID查询案件详情，只涉及案件信息")
    public ObjectRestResponse<JSONObject> caseInfoInstance(@PathVariable(value="id") @ApiParam("待查询对象ID") String id){
        
        return this.caserInfoService.caseInfoInstance(id);
    }

    @GetMapping("/all/potition/patrol")
    @ApiOperation("专项事件全部定位")
    public TableResultResponse<JSONObject> allPotitionPatrol(){
        TableResultResponse<JSONObject> tableResult = this.baseBiz.allPositionPatrol();
        return tableResult;
    }

    @PostMapping("/all/potition")
    @ApiOperation("专项事件全部定位")
    public TableResultResponse<JSONObject> allPotition(@RequestBody JSONObject objs, HttpServletRequest request){
        TableResultResponse<JSONObject> tableResult = caserInfoService.allPosition(objs);
        return tableResult;
    }

    /**
     * 通过事件等级和部门id查询
     * @param page 页码
     * @param limit 页数
     * @param caseLevel 事件等级
     * @return
     */
    @GetMapping("/caseLevel")
    @ApiOperation("通过事件等级查询事件列表")
    public TableResultResponse<Map<String,Object>> getCaseInfoByDeptId(
            @RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit,
            @RequestParam(value = "caseLevel", defaultValue = "") @ApiParam(name = "案件等级") String caseLevel){
        //TODO 多个部门
        return caserInfoService.getCaseInfoByDeptId(caseLevel, BaseContextHandler.getDepartID(),page,limit);
    }

    /**
     * 执法片区网格事件统计
     *
     * @return
     */
    @ApiOperation("执法片区网格事件统计")
    @RequestMapping(value = "/statis/grid/zfpq", method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> getGridZFPQ(){
        return this.baseBiz.getGridZFPQ();
    }
    
    @GetMapping("/heatMap")
    @ApiOperation("事件热力学统计")
    public TableResultResponse<JSONObject> caseHeatMap(
        @RequestParam(value = "caseLevel", defaultValue = "") String caseLevel,
        @RequestParam(value = "sourceType", defaultValue = "") String sourcetype,
        @RequestParam(value = "grid", required = false) Integer grid,
        @RequestParam(value = "startDate", defaultValue = "") String startDate,
        @RequestParam(value = "endDate", defaultValue = "") String endDate) {
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setCaseLevel(caseLevel);
        caseInfo.setSourceType(sourcetype);
        caseInfo.setGrid(grid);

        return this.baseBiz.heatMap(caseInfo, startDate, endDate);
    }

    /**
     * 不分页查询事件列表
     * 
     * @param sourceType
     * @param isFinished
     * @return
     */
    @GetMapping("/list/unpage")
    @ApiOperation("不分页查询事件列表")
    public List<CaseInfo> getList(
        @RequestParam(value = "sourceType", defaultValue = "") @ApiParam(value = "事件来源类型") String sourceType,
        @RequestParam(value = "specialEventId") @ApiParam(value = "事件来源类型") Integer specialEventId,
        @RequestParam(value = "isFinished", defaultValue = "") @ApiParam(value = "事件完成状态") String isFinished) {
        CaseInfo queryCaseInfo = new CaseInfo();
        queryCaseInfo.setSourceType(sourceType);
        queryCaseInfo.setIsFinished(isFinished);
        return this.baseBiz.getList(queryCaseInfo, specialEventId);
    }

    @GetMapping("/list/home/statistics")
    @ApiOperation("首页--查询某监管对象上发生的事件(专项)")
    public TableResultResponse<JSONObject> getList(
        @RequestParam(value = "caseSourceType") @ApiParam(value = "事件来源类型") String caseSourceType,
        @RequestParam(value = "patrolTaskSourceType") @ApiParam(value = "巡查事项来源类型") String patrolTaskSourceType,
        @RequestParam(value = "sourceTaskId") @ApiParam(value = "巡查事项来源ID") String sourceTaskId,
        @RequestParam(value = "startTime", defaultValue = "") @ApiParam(value = "查询开始时间") String startTime,
        @RequestParam(value = "endTime", defaultValue = "") @ApiParam(value = "查询结束时间") String endTime,
        @RequestParam(value = "regulaObjectId", defaultValue = "") @ApiParam(value = "监管对象ID") String regulaObjectId,
        @RequestParam(value = "regulaObjectTypeId", defaultValue = "") @ApiParam(value = "监管对象类型ID") String regulaObjectTypeId) {

        JSONObject queryData = new JSONObject();
        queryData.put("caseSourceType", caseSourceType);
        queryData.put("patrolTaskSourceType", patrolTaskSourceType);
        queryData.put("sourceTaskId", sourceTaskId);
        queryData.put("startTime", startTime);
        queryData.put("endTime", endTime);
        queryData.put("regulaObjectId", regulaObjectId);
        queryData.put("regulaObjectTypeId", regulaObjectTypeId);

        return this.baseBiz.getListForHome(queryData);
    }

    /**
     * 只通过业务ID查询带有工作流信息的结果
     * @param id
     * @return
     */
    @GetMapping(value = "/caseInfo/userToDoTask")
    @ApiOperation("查询详细待办任务")
    public ObjectRestResponse<JSONObject> getCaseInfoWithWfData(@RequestParam(value="id") Integer id) {
        return this.caserInfoService.getCaseInfoWithWfData(id);
    }

    @GetMapping("/nodeHistory")
    @ApiOperation("查询详细待办任务")
    public List<JSONObject> approveHistoryOfSpeNode(
        @RequestParam(value = "procInstId") String procInstId,
        @RequestParam(value = "procPropsKey",defaultValue = "") String procPropsKey,
        @RequestParam(value = "procCtaskCode") String procCtaskCode) {

        JSONObject queryData = new JSONObject();
        queryData.put("procInstId", procInstId);
        queryData.put("procPropsKey",
            StringUtils.isEmpty(procPropsKey) ? null : Arrays.asList(procPropsKey.split(",")));
        queryData.put("procCtaskCode", procCtaskCode);
        return caserInfoService.approveHistoryOfSpeNode(queryData);
    }
}