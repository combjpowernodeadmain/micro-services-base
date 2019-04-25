package com.bjzhianjia.scp.cgp.rest;

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
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CommandCenterHotlineBiz;
import com.bjzhianjia.scp.cgp.entity.CommandCenterHotline;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.CommandCenterHotlineService;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.Date;

@RestController
@RequestMapping("commandCenterHotline")
@CheckClientToken
@CheckUserToken
@Api(tags = "指挥中心热线")
public class CommandCenterHotlineController
    extends BaseController<CommandCenterHotlineBiz, CommandCenterHotline, Integer> {

    @Autowired
    private CommandCenterHotlineService commandCenterHotlineServoce;
    
    

    @PostMapping(value = "/cache")
    @ApiOperation("添加暂存")
    public ObjectRestResponse<CommandCenterHotline> addCommandCenterHotlineCache(
        @RequestBody @Validated @ApiParam(name = "待添加对象实例") CommandCenterHotline commandCenterHotline,
        BindingResult bindingResult) {
        ObjectRestResponse<CommandCenterHotline> restResult = new ObjectRestResponse<>();
        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<Void> result = this.baseBiz.createCache(commandCenterHotline, null);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }

    @GetMapping("/list")
    @ApiOperation("分页获取列表")
    public TableResultResponse<JSONObject> getList(
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "") @ApiParam(name = "热线标题") String hotlnTitle,
        @RequestParam(defaultValue = "") @ApiParam(name = "热线编号") String hotlnCode,
        @RequestParam(defaultValue = "") @ApiParam(name = "诉求人") String appealPerson,
        @RequestParam(defaultValue = "") @ApiParam(name = "热线事件分类") String bizType,
        @RequestParam(defaultValue = "") @ApiParam(name = "处理状态") String exeStatus,
        @RequestParam(required = false) @ApiParam("查询开始时间") String startTime,
        @RequestParam(required = false) @ApiParam("查询结束时间") String endTime) {

        CommandCenterHotline commandCenterHotline = new CommandCenterHotline();
        commandCenterHotline.setHotlnTitle(hotlnTitle.trim());
        commandCenterHotline.setAppealPerson(appealPerson);
        commandCenterHotline.setBizType(bizType);
        commandCenterHotline.setExeStatus(exeStatus);
        commandCenterHotline.setHotlnCode(hotlnCode.trim());

        return commandCenterHotlineServoce.getList(commandCenterHotline, page, limit, startTime,
            endTime);
    }
    
    @GetMapping("/one/{id}")
    @ApiOperation("获取单个对象")
    public JSONObject getOne(@PathVariable("id") @ApiParam("待查询对象ID")Integer id){
        return commandCenterHotlineServoce.selectById(id);
    }
    
    @RequestMapping(value = "/get/toDo/{id}", method = RequestMethod.GET)
    @ApiOperation("查询未启动对象")
    public ObjectRestResponse<JSONObject> getToDo(@PathVariable(value = "id") @ApiParam(name = "待查询对象ID") Integer id) {
        ObjectRestResponse<JSONObject> restResult = commandCenterHotlineServoce.getToDo(id);
        return restResult;
    }

    @RequestMapping(value = "/remove/{ids}", method = RequestMethod.DELETE)
    @ApiOperation("批量删除对象")
    public ObjectRestResponse<CommandCenterHotline> remove(
            @PathVariable("ids") @ApiParam(name = "待删除对象ID集合，多个ID用“，”隔开") Integer[] ids) {
        ObjectRestResponse<CommandCenterHotline> restResult = new ObjectRestResponse<>();
        if (ids == null || ids.length == 0) {
            restResult.setStatus(400);
            restResult.setMessage("请选择要删除的项");
            return restResult;
        }

        Result<Void> result = this.baseBiz.remove(ids);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }
        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }
}