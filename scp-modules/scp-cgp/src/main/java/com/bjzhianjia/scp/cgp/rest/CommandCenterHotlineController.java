package com.bjzhianjia.scp.cgp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public TableResultResponse<JSONObject> getList(@RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "") @ApiParam(name = "热线标题") String hotlnTitle,
        @RequestParam(defaultValue = "") @ApiParam(name = "诉求人") String appealPerson,
        @RequestParam(defaultValue = "") @ApiParam(name = "热线事件分类") String bizType,
        @RequestParam(defaultValue = "") @ApiParam(name = "处理状态") String exeStatus) {

        CommandCenterHotline commandCenterHotline = new CommandCenterHotline();
        commandCenterHotline.setHotlnTitle(hotlnTitle);
        commandCenterHotline.setAppealPerson(appealPerson);
        commandCenterHotline.setBizType(bizType);
        commandCenterHotline.setExeStatus(exeStatus);

        return commandCenterHotlineServoce.getList(commandCenterHotline, page, limit);
    }
}