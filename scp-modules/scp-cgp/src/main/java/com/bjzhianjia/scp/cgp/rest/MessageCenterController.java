package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.MessageCenterBiz;
import com.bjzhianjia.scp.cgp.entity.MessageCenter;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("messageCenter")
@CheckClientToken
@CheckUserToken
public class MessageCenterController extends BaseController<MessageCenterBiz, MessageCenter, Integer> {

    /**
     * 消息中心
     * 
     * @return
     */
    @PostMapping("/msgCenter")
    @ApiOperation("添加消息中心信息")
    public ObjectRestResponse<JSONObject> msgCenter() {
        return this.baseBiz.msgCenter();
    }

    @GetMapping("/list")
    @ApiOperation("获取未读消息")
    public JSONObject getUnReadList(
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @RequestParam(value="msgSourceType",defaultValue = "") @ApiParam(value="消息来源类型") String msgSourceType
    ) {
        MessageCenter messageCenter=new MessageCenter();
        messageCenter.setMsgSourceType(msgSourceType);
        return this.baseBiz.getUnReadList(messageCenter,page, limit);
    }

    @PutMapping("/read/{ids}")
    @ApiOperation("修改消息为已读")
    public ObjectRestResponse<JSONObject> readMsg(@PathVariable("ids") @ApiParam("已读消息ID集合") Integer[] ids) {
        return this.baseBiz.readMsg(ids);
    }
}