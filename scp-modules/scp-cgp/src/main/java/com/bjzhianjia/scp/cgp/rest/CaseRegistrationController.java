package com.bjzhianjia.scp.cgp.rest;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.CaseRegistrationService;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 
 * CaseRegistrationController 案件登记.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月7日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@RestController
@RequestMapping("caseRegistration")
@CheckClientToken
@CheckUserToken
@Api(tags = "综合执法 - 案件登记")
public class CaseRegistrationController extends BaseController<CaseRegistrationBiz, CaseRegistration, String> {
    @Autowired
    private CaseRegistrationService caseRegistrationService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("业务--添加单个对象")
    public ObjectRestResponse<Void> addCase(@RequestBody @ApiParam(name = "待添加对象实例") @Validated JSONObject objs) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        JSONObject caseRegiJObj = objs.getJSONObject("bizData");

        Result<Void> result = this.baseBiz.addCase(caseRegiJObj);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation("查询 单条记录")
    public ObjectRestResponse<JSONObject> getUserTaskDetail(
        @RequestBody @ApiParam("封装查询条件的对象") @Validated JSONObject jobs, BindingResult bindingResult) {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            restResult.setStatus(400);
            return restResult;
        }

        return null;
    }

    @RequestMapping(value = "/list/enforcers", method = RequestMethod.GET)
    @ApiOperation("按执法人分页查询对象")
    public TableResultResponse<CaseRegistration> getListByExecutePerson(
        @RequestParam(value = "userId", defaultValue = "") @ApiParam("案件处理人ID") String userId,
        @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit) {
        return this.baseBiz.getListByExecutePerson(userId, page, limit);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("分页获取对象列表")
    public TableResultResponse<CaseRegistration> getList(
        @RequestParam(value = "gridId", defaultValue = "") @ApiParam("网格ID") Integer gridId,
        @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit) {
        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setGirdId(gridId);

        return this.baseBiz.getList(caseRegistration, page, limit);
    }

    /**
     * 查询个人案件代办
     * 
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("个人案件代办")
    @ResponseBody
    @RequestMapping(value = { "/userToDoTasks" }, method = RequestMethod.POST)
    public TableResultResponse<JSONObject> getUserToDoTasks(@RequestBody JSONObject objs, HttpServletRequest request) {

        TableResultResponse<JSONObject> userToDoTasks = this.baseBiz.getUserToDoTasks(objs);
        return userToDoTasks;
    }

    /**
     * 案件综合查询
     * 
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("案件综合查询")
    @ResponseBody
    @RequestMapping(value = { "/allTasks" }, method = RequestMethod.POST)
    public TableResultResponse<JSONObject> getUserAllToDoTasks(@RequestBody JSONObject objs,
        HttpServletRequest request) {
        TableResultResponse<JSONObject> userToDoTasks = this.baseBiz.getAllTasks(objs);
        return userToDoTasks;
    }

    /**
     * 所有用户案件代办查询
     * 
     * @param objs
     * @param request
     * @return
     */
    @ApiOperation("所有用户案件代办查询")
    @ResponseBody
    @RequestMapping(value = { "/userAllToDoTasks" }, method = RequestMethod.POST)
    public TableResultResponse<JSONObject> getAllToDoTasks(@RequestBody JSONObject objs, HttpServletRequest request) {

        TableResultResponse<JSONObject> userToDoTasks = this.baseBiz.getUserAllToDoTasks(objs);
        return userToDoTasks;
    }

    /**
     * 案件详情页
     * 
     * @param id
     * @return
     */
    @ApiOperation("案件详情页")
    @RequestMapping(value = "/id", method = RequestMethod.POST)
    public ObjectRestResponse<JSONObject> getInfoById(@RequestBody @ApiParam("请求参数") JSONObject objs) {
        ObjectRestResponse<JSONObject> result = new ObjectRestResponse<>();
        if (objs != null) {
            result.setData(this.baseBiz.getInfoById(objs));
        } else {
            result.setStatus(400);
            result.setMessage("非法参数");
        }
        return result;
    }

    /**
     * 案件处理类型分布
     * 
     * @return
     */
    @ApiOperation("案件处理类型分布")
    @RequestMapping(value = "/statis/state", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONArray> getStatisState(
        @RequestParam(defaultValue = "") @ApiParam("业务条线") String bizType,
        @RequestParam(defaultValue = "") @ApiParam("处理类型") String dealType,
        @RequestParam(defaultValue = "") @ApiParam("网格范围") String gridIds,
        @RequestParam(defaultValue = "") @ApiParam("案件来源类型") String caseSourceType,
        @RequestParam(defaultValue = "") @ApiParam("执法分队") String deptId,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime) {
        ObjectRestResponse<JSONArray> result = new ObjectRestResponse<>();

        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setBizType(bizType);
        caseRegistration.setDealType(dealType);
        caseRegistration.setCaseSourceType(caseSourceType);
        caseRegistration.setDeptId(deptId);

        if (StringUtils.isNotBlank(endTime)) {
            Date _end = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtils.addDays(_end, 1);
            endTime = DateUtil.dateFromDateToStr(_end, "yyyy-MM-dd HH:mm:ss");
        }

        JSONArray data = this.baseBiz.getStatisState(caseRegistration, startTime, endTime, gridIds);
        if (data != null && data.size() > 0) {
            result.setData(data);
        } else {
            result.setStatus(400);
        }
        return result;
    }

    /**
     * 案件来源分布
     * 
     * @return
     */
    @ApiOperation("案件来源分布")
    @RequestMapping(value = "/statis/caseSource", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONArray> getCaseSource(
        @RequestParam(defaultValue = "") @ApiParam("业务条线") String bizType,
        @RequestParam(defaultValue = "") @ApiParam("案件处理类型") String dealType,
        @RequestParam(defaultValue = "") @ApiParam("网格范围") String gridIds,
        @RequestParam(defaultValue = "") @ApiParam("执法分队") String deptId,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime) {
        ObjectRestResponse<JSONArray> result = new ObjectRestResponse<>();

        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setBizType(bizType);
        caseRegistration.setDealType(dealType);
        caseRegistration.setDeptId(deptId);

        if (StringUtils.isNotBlank(endTime)) {
            Date _end = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtils.addDays(_end, 1);
            endTime = DateUtil.dateFromDateToStr(_end, "yyyy-MM-dd HH:mm:ss");
        }

        JSONArray data = this.baseBiz.getCaseSource(caseRegistration, startTime, endTime, gridIds);
        if (data != null && data.size() > 0) {
            result.setData(data);
        } else {
            result.setStatus(400);
        }
        return result;
    }

    /**
     * 案件业务条线分布
     * 
     * @return
     */
    @ApiOperation("案件业务条线分布")
    @RequestMapping(value = "/statis/bizType", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONArray> getBizType(
        @RequestParam(defaultValue = "") @ApiParam("案件来源类型") String caseSourceType,
        @RequestParam(defaultValue = "") @ApiParam("案件处理类型") String dealType,
        @RequestParam(defaultValue = "") @ApiParam("网格范围") String gridIds,
        @RequestParam(defaultValue = "") @ApiParam("执法分队") String deptId,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime) {
        ObjectRestResponse<JSONArray> result = new ObjectRestResponse<>();

        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setCaseSourceType(caseSourceType);
        caseRegistration.setDealType(dealType);
        caseRegistration.setDeptId(deptId);

        if (StringUtils.isNotBlank(endTime)) {
            Date _end = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtils.addDays(_end, 1);
            endTime = DateUtil.dateFromDateToStr(_end, "yyyy-MM-dd HH:mm:ss");
        }

        JSONArray data = this.baseBiz.getBizType(caseRegistration, startTime, endTime, gridIds);
        if (data != null && data.size() > 0) {
            result.setData(data);
        } else {
            result.setStatus(400);
        }
        return result;
    }

    @RequestMapping(value = "/statis/ZhDui", method = RequestMethod.GET)
    @ApiOperation("执法中队案件量趋势分析")
    public ObjectRestResponse<JSONObject> getStatisZhDuiCase(
        @RequestParam(value = "bizType", defaultValue = "") @ApiParam("业务条线") String bizType,
        @RequestParam(value = "caseSourceType", defaultValue = "") @ApiParam("案件来源类型") String caseSourceType,
        @RequestParam(value = "gridIds", defaultValue = "") @ApiParam("网格范围") String gridIds,
        @RequestParam(value = "startTime", defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(value = "endTime", defaultValue = "") @ApiParam("结束日期") String endTime) {
        
        
        
        JSONObject caseRegistrationJObj = new JSONObject();
        caseRegistrationJObj.put("bizType", bizType);
        caseRegistrationJObj.put("caseSourceType", caseSourceType);
        caseRegistrationJObj.put("gridIds", gridIds);
        
        String _startTime = startTime;
        String _endTime = endTime;
        if (StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)) {
            Calendar calendar =  Calendar.getInstance();
            //当年当月
            _endTime = DateUtil.dateFromDateToStr(calendar.getTime(),DateUtil.DEFAULT_DATE_FORMAT);
            //当年第一月第一天
            calendar.set(Calendar.MONTH, 0);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            _startTime = DateUtil.dateFromDateToStr(calendar.getTime(),DateUtil.DEFAULT_DATE_FORMAT);
        }
        
        return this.baseBiz.getStatisZhDuiCase(caseRegistrationJObj, _startTime, _endTime);
    }

    /**
     * 案件违法统计
     * 
     * @return
     */
    @ApiOperation("案件违法统计")
    @GetMapping("/statis/inspectItem")
    @ResponseBody
    public TableResultResponse<Map<String, Object>> getInspectItem(
        @RequestParam(defaultValue = "") @ApiParam("业务条线") String bizType,
        @RequestParam(defaultValue = "") @ApiParam("案件处理类型") String dealType,
        @RequestParam(defaultValue = "") @ApiParam("网格范围") String gridIds,
        @RequestParam(defaultValue = "") @ApiParam("案件来源类型") String caseSourceType,
        @RequestParam(defaultValue = "") @ApiParam("执法分队") String deptId,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime,
        @RequestParam(defaultValue = "0") @ApiParam("页码") Integer page,
        @RequestParam(defaultValue = "10") @ApiParam("页容量") Integer limit) {

        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setCaseSourceType(caseSourceType);
        caseRegistration.setDealType(dealType);
        caseRegistration.setDeptId(deptId);
        caseRegistration.setBizType(bizType);

        if (StringUtils.isNotBlank(endTime)) {
            Date _end = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _end = DateUtils.addDays(_end, 1);
            endTime = DateUtil.dateFromDateToStr(_end, "yyyy-MM-dd HH:mm:ss");
        }

        TableResultResponse<Map<String, Object>> result =
            this.baseBiz.getInspectItem(caseRegistration, startTime, endTime, gridIds, page, limit);

        return result;
    }

    /**
     * 查询流程历史
     * 
     * @return
     */
    @RequestMapping(value = "/proc/history", method = RequestMethod.POST)
    @ApiOperation("查询流程历史")
    public JSONArray procHistory(@RequestBody(required=true) JSONObject objs) {
        /*
         * 请求参数格式
         * {"bizData":{},
         * "procData":{"procInstId":"627501"},
         * "authData":{"procAuthType":"2"},
         * "variableData":{},
         * "queryData":{}
         * }
         */
        JSONArray procApproveHistory = caseRegistrationService.getProcApproveHistory(objs);
        return procApproveHistory;
    }
}