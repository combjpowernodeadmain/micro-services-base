package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.alibaba.fastjson.JSONArray;
import com.bjzhianjia.scp.cgp.biz.PopulationInfoBiz;
import com.bjzhianjia.scp.cgp.entity.PopulationInfo;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("populationInfo")
@CheckClientToken
@CheckUserToken
@Api(tags = "人口数量管理")
public class PopulationInfoController extends BaseController<PopulationInfoBiz, PopulationInfo, Integer> {

    @PostMapping(value = "/list")
    @ApiOperation("批量插入数据")
    public ObjectRestResponse<PopulationInfo> add(
        @RequestBody @Validated @ApiParam("待添加对象实例集合") JSONArray populationInfoListJArray,
        BindingResult bindingResult) {
        ObjectRestResponse<PopulationInfo> restResult = new ObjectRestResponse<>();

        List<PopulationInfo> populationInfoList = populationInfoListJArray.toJavaList(PopulationInfo.class);

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage("参数绑定错误");
        }

        restResult = this.baseBiz.insertPopulationInfoList(populationInfoList);

        return restResult;
    }
    
    @GetMapping(value="/all/partical")
    @ApiOperation("获取全部数据，返回部分字段")
    public TableResultResponse<PopulationInfo> getAll(){
        return this.baseBiz.getAllWithParticalFields();
    }
}