package com.bjzhianjia.scp.cgp.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.WritsInstancesBiz;
import com.bjzhianjia.scp.cgp.entity.WritsInstances;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("writsInstances")
@CheckClientToken
@CheckUserToken
@Api(tags = "综合执法 - 案件登记文书记录")
public class WritsInstancesController extends BaseController<WritsInstancesBiz, WritsInstances, Integer> {

    @Autowired
    private WritsInstancesBiz writsInstancesBiz;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("查询文书列表")
    public TableResultResponse<WritsInstances> getList(
        @RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit,
        @RequestParam(value = "templatedId", required = false) @ApiParam(name = "文书模板") Integer templatedId,
        @RequestParam(value = "templateCodes", defaultValue = "") @ApiParam(name = "文书模板Code集合，多个用逗号隔开") String templateCodes,
        @RequestParam(value = "procTaskId", defaultValue = "") @ApiParam(name = "流程任务ID") String procTaskId,
        @RequestParam(value = "caseId", defaultValue = "") @ApiParam(name = "案件 ID") String caseId) {
        TableResultResponse<WritsInstances> restResult = new TableResultResponse<>();

        JSONObject queryJObj = new JSONObject();
        queryJObj.put("templatedId", templatedId);
        queryJObj.put("templateCodes", templateCodes);
        queryJObj.put("procTaskId", procTaskId);
        queryJObj.put("caseId", caseId);

        restResult = writsInstancesBiz.getList(queryJObj, page, limit);

        return restResult;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("添加单对象")
    public ObjectRestResponse<WritsInstances> add(@RequestBody @ApiParam(name = "待添加对象实例") @Validated JSONObject jobs,
        BindingResult bindingResult) {

        ObjectRestResponse<WritsInstances> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        JSONObject bizData = jobs.getJSONObject("bizData");
        writsInstancesBiz.updateOrInsert(bizData);

        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ApiOperation("获取文书路径")
    public ObjectRestResponse<String> getWritsInstance(
        @RequestParam(value = "writsInstanceId", defaultValue = "") Integer writsInstanceId) {
        ObjectRestResponse<String> restResult = this.baseBiz.getWritsInstance(writsInstanceId);
        return restResult;
    }

    @RequestMapping(value = "/list/by_template", method = RequestMethod.GET)
    @ApiOperation("获取文书模板及其下的文书")
    public TableResultResponse<JSONObject> getWithWritsInstance(
        @RequestParam(value = "templateCodes", defaultValue = "") @ApiParam(name = "文书模板Code集合，多个用逗号隔开") String templateCodes,
        @RequestParam(value = "procTaskId", defaultValue = "") @ApiParam(name = "流程任务ID") String procTaskId,
        @RequestParam(value = "caseId", defaultValue = "") @ApiParam(name = "案件 ID") String caseId) {
        TableResultResponse<JSONObject> restResult = new TableResultResponse<>();

        restResult = this.baseBiz.getWithWritsInstance(templateCodes, procTaskId, caseId);
        return restResult;
    }

    @RequestMapping(value = "/add/cache", method = RequestMethod.POST)
    @ApiOperation("添加文书暂存")
    public ObjectRestResponse<WritsInstances> addCache(
        @RequestBody @ApiParam("待暂存对象实例") @Validated JSONObject writsInstances, BindingResult bindingResult) {
        ObjectRestResponse<WritsInstances> restResult = new ObjectRestResponse<>();
        this.baseBiz.addCache(writsInstances);
        return restResult;
    }

    @RequestMapping(value = "/writs/history",method=RequestMethod.GET)
    @ApiOperation("过程文书与附件")
    public ObjectRestResponse<JSONArray> writsAndAttachmentHistory(
        @RequestParam(value = "caseId", defaultValue = "") String caseId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        return this.baseBiz.writsAndAttachmentHistory(caseId);
    }
    
    /**
     * @param writsInstancesList
     * @param bindingResult
     * @return
     */
    @RequestMapping(value="/add/list",method=RequestMethod.POST)
    @ApiOperation("批量添加记录")
    public ObjectRestResponse<WritsInstances> addList(@RequestBody @Validated @ApiParam("待添加对象实例集合")List<WritsInstances> writsInstancesList,BindingResult bindingResult){
        ObjectRestResponse<WritsInstances> resetResult = this.baseBiz.addList(writsInstancesList);
        return resetResult;
    }
}