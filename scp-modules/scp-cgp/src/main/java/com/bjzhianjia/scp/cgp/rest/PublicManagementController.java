package com.bjzhianjia.scp.cgp.rest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.PublicManagementBiz;
import com.bjzhianjia.scp.cgp.entity.PublicManagement;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("publicManagement")
@CheckClientToken
@CheckUserToken
@Api(tags = "公共管理")
public class PublicManagementController extends BaseController<PublicManagementBiz, PublicManagement, Integer> {

    @GetMapping("/list")
    @ApiOperation("获取全部记录")
    public TableResultResponse<JSONObject> getAll() {
        return this.baseBiz.getAll();
    }

    @DeleteMapping("/list/{ids}")
    @ApiOperation("批量删除对象")
    public ObjectRestResponse<Void> remove(@PathVariable @ApiParam(name = "待删除对象ID集合，多个ID用“，”隔开") Integer[] ids) {
        ObjectRestResponse<Void> restResult = this.baseBiz.remove(ids);
        restResult.setMessage("删除成功");
        restResult.setStatus(200);
        return restResult;
    }
}