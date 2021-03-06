package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.core.context.*;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.LawTaskBiz;
import com.bjzhianjia.scp.cgp.entity.LawTask;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;


/**
 * LawTaskController 执法任务.
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
@RequestMapping("lawTask")
@CheckClientToken
@CheckUserToken
@Api(tags = "执法任务")
public class LawTaskController extends BaseController<LawTaskBiz, LawTask, Integer> {

    @Autowired
    private LawTaskBiz lawTaskBiz;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ApiOperation("新增执法任务")
    public ObjectRestResponse<LawTask> add(
        @RequestBody @Validated @ApiParam("执法任务实例") LawTask lawTask, BindingResult bindingResult) {
        ObjectRestResponse<LawTask> result = new ObjectRestResponse<>();
        result.setStatus(400);
        result.setMessage("非法参数");

        if (bindingResult.hasErrors()) {
            return result;
        }
        if (!StringUtils.isNotBlank(lawTask.getExecutePerson())) {
            return result;
        }
        if (!StringUtils.isNotBlank(lawTask.getPatrolObject())) {
            return result;
        }
        BeanUtil.beanAttributeValueTrim(lawTask);
        try {
            lawTaskBiz.createLawTask(lawTask);
        } catch (Exception e) {
            result.setMessage("新增失败");
            return result;
        }
        result.setStatus(200);
        result.setData(lawTask);
        result.setMessage(null);
        return result;
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ApiOperation("更新执法任务")
    public ObjectRestResponse<LawTask> update(
        @RequestBody @Validated @ApiParam("执法任务实例") LawTask lawTask, BindingResult bindingResult) {
        ObjectRestResponse<LawTask> result = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            result.setStatus(400);
            result.setMessage("非法参数");
            return result;
        }
        BeanUtil.beanAttributeValueTrim(lawTask);
        lawTaskBiz.update(lawTask);
        return result;
    }

    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ApiOperation("执法任务列表")
    public TableResultResponse<Map<String, Object>> list(
        @RequestParam(defaultValue = "") @ApiParam("队员名称") String userName,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime,
        @RequestParam(defaultValue = "") @ApiParam("任务状态") String state,
        @RequestParam(defaultValue = "") @ApiParam("执法任务编号或执法任务名称") String lawTaskCodeOrLawTitle,
        @RequestParam(defaultValue = "") @ApiParam("巡查对象") String regulaObjectName,
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page) {

        TableResultResponse<Map<String, Object>> result = null;

        Date _startTime = null;
        Date _endTimeTmp = null;
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            _startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            _endTimeTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _endTimeTmp = DateUtils.addDays(_endTimeTmp, 1);
        }
        result = lawTaskBiz.getLawTaskList(userName, regulaObjectName, state, _startTime, _endTimeTmp, page, limit,lawTaskCodeOrLawTitle);
        return result;
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ApiOperation("删除单个执法任务")
    public ObjectRestResponse<Void> delete(@PathVariable("id") @ApiParam("执法任务的id") Integer id) {
        ObjectRestResponse<Void> result = new ObjectRestResponse<>();
        if (id == null) {
            result.setStatus(400);
            return result;
        }
        LawTask lawTask = new LawTask();
        lawTask.setId(id);
        lawTask.setIsDeleted("1");
        lawTaskBiz.updateSelectiveById(lawTask);

        return result;
    }

    @RequestMapping(value = "randomLawTask", method = RequestMethod.GET)
    @ApiOperation("分配执法任务")
    public ObjectRestResponse<JSONObject> randomLawTask(
        @RequestParam(defaultValue = "2") @ApiParam("每组队员数 ") Integer peopleNumber,
        @RequestParam(defaultValue = "2") @ApiParam("巡查对象数") Integer regulaObjNumber,
        @RequestParam(defaultValue = "") @ApiParam("网格ids") String griIds,
        @RequestParam(defaultValue = "") @ApiParam("监管对象类型ids") String objType,
        @RequestParam(defaultValue = "") @ApiParam("开始时间") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束时间") String endTime,
        @RequestParam(defaultValue = "") @ApiParam("任务要求") String info,
        @RequestParam(defaultValue = "") @ApiParam("业务条线") String bizTypeCode,
        @RequestParam(defaultValue = "") @ApiParam("事件类别ids") String eventTypeId,
        @RequestParam(defaultValue = "") @ApiParam("执法任务名称") String lawTitle,
        @RequestParam(value = "randomNum",defaultValue = "1")@ApiParam("随机数量") Integer randomNum) {

        ObjectRestResponse<JSONObject> result = new ObjectRestResponse<>();

        if(StringUtils.isBlank(startTime)||StringUtils.isBlank(endTime)){
            result.setStatus(400);
            result.setMessage("任务开始时间或结束时间为空，请重新选择。");
            return result;
        }

        if (StringUtils.isBlank(griIds) || StringUtils.isBlank(objType)
            || StringUtils.isBlank(startTime) || StringUtils.isBlank(endTime)|| StringUtils.isBlank(info)) {
            result.setStatus(400);
            result.setMessage("非法参数");
            return result;
        }

        Date _startTime = null;
        Date _endTimeTmp = null;
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            _startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            _endTimeTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
        }

        try {
            Result<JSONObject>  _result = lawTaskBiz.randomLawTask(objType, griIds, peopleNumber, regulaObjNumber, _startTime,
                _endTimeTmp, info, bizTypeCode, eventTypeId,lawTitle,randomNum);
            // 错误反馈
            if(!_result.getIsSuccess()) {
                result.setStatus(400);
                result.setMessage(_result.getMessage());
            }

            JSONObject data = _result.getData();
            result.setData(data);
        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage("分配异常");
        }
        return result;
    }
    
    /**
     * 执法任务详情
     * @param id
     *      执法任务的id
     * @return
     */
    @RequestMapping(value = "id/{id}", method = RequestMethod.GET)
    @ApiOperation("执法任务详情")
    public ObjectRestResponse<JSONObject> getById(@PathVariable("id") @ApiParam("执法任务的id") Integer id) {
        ObjectRestResponse<JSONObject> result = new ObjectRestResponse<>();
        result.setStatus(400);
        if (id == null) {
            return result;
        }
        JSONObject reslutObj =  lawTaskBiz.getById(id);
        if(reslutObj != null) {
            result.setData(reslutObj); 
            result.setStatus(200);
        }
        return result;
    }

    @RequestMapping(value = "/list/todo", method = RequestMethod.GET)
    @ApiOperation("执法任务列表")
    public TableResultResponse<Map<String, Object>> listToDo(
        @RequestParam(defaultValue = "") @ApiParam("队员名称") String userName,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime,
        @RequestParam(defaultValue = "") @ApiParam("任务状态") String state,
        @RequestParam(defaultValue = "") @ApiParam("执法任务编号或执法任务名称") String lawTaskCodeOrLawTitle,
        @RequestParam(defaultValue = "") @ApiParam("巡查对象") String regulaObjectName,
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page) {

        TableResultResponse<Map<String, Object>> result = null;

        Date _startTime = null;
        Date _endTimeTmp = null;
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            _startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            _endTimeTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _endTimeTmp = DateUtils.addDays(_endTimeTmp, 1);
        }

        result =
            lawTaskBiz.getLawTaskToDoList(userName, regulaObjectName, _startTime, _endTimeTmp, page, limit,lawTaskCodeOrLawTitle);
        return result;
    }
    
    @RequestMapping(value = "/list/partical", method = RequestMethod.GET)
    @ApiOperation("执法任务列表")
    public TableResultResponse<Map<String, Object>> listSimple(
        @RequestParam(defaultValue = "") @ApiParam("队员名称") String userName,
        @RequestParam(defaultValue = "") @ApiParam("开始日期") String startTime,
        @RequestParam(defaultValue = "") @ApiParam("结束日期") String endTime,
        @RequestParam(defaultValue = "") @ApiParam("任务状态") String state,
        @RequestParam(defaultValue = "") @ApiParam("执法任务编号或执法任务名称") String lawTaskCodeOrLawTitle,
      //  @RequestParam(defaultValue = "") @ApiParam("执法任务名称") String lawTitle,
        @RequestParam(defaultValue = "") @ApiParam("巡查对象") String regulaObjectName,
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page) {
        
        TableResultResponse<Map<String, Object>> result = null;
        
        Date _startTime = null;
        Date _endTimeTmp = null;
        if (StringUtils.isNotBlank(startTime) && StringUtils.isNotBlank(endTime)) {
            _startTime = DateUtil.dateFromStrToDate(startTime, "yyyy-MM-dd HH:mm:ss");
            _endTimeTmp = DateUtil.dateFromStrToDate(endTime, "yyyy-MM-dd HH:mm:ss");
            _endTimeTmp = DateUtils.addDays(_endTimeTmp, 1);
        }

        JSONObject queryJObj=new JSONObject();
        queryJObj.put("userName", userName);
        queryJObj.put("startTime", _startTime);
        queryJObj.put("endTime", _endTimeTmp);
        queryJObj.put("state", state);
        queryJObj.put("regulaObjectName", regulaObjectName);
       // queryJObj.put("lawTaskCode", lawTaskCode);
       // queryJObj.put("lawTitle", lawTitle);
        queryJObj.put("lawTaskCodeOrLawTitle", lawTaskCodeOrLawTitle);

        result = lawTaskBiz.listSimple(queryJObj, page, limit);
        return result;
    }

    /**
     * 获取当前执法队员的执法任务列表
     *
     * @param limit 页容量
     * @param page 当前页
     * @return
     */
    @ApiOperation("获取当前执法队员的执法任务列表")
    @GetMapping("/user")
    public TableResultResponse<Map<String, Object>> getLawTaskByUserId(
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page) {
        return lawTaskBiz.getLawTaskByUserId(BaseContextHandler.getUserID(), LawTask.ROOT_BIZ_LAWTASKS_DOING, limit, page);
    }
}