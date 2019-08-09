package com.bjzhianjia.scp.security.admin.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.admin.biz.AppVersionManageBiz;
import com.bjzhianjia.scp.security.admin.entity.AppVersionManage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.endpoint.jmx.DataEndpointMBean;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("appVersionManage")
@CheckClientToken
@CheckUserToken
public class AppVersionManageController extends BaseController<AppVersionManageBiz,AppVersionManage,Integer> {

    @PostMapping("/instance")
    @ApiOperation("添加单个记录")
    public ObjectRestResponse<Void> addAppVersion(@RequestBody @ApiParam(value = "待添加记录实例") AppVersionManage appVersionManage, BindingResult bindingResult){
        ObjectRestResponse<Void> restResponse=new ObjectRestResponse<>();
        if(bindingResult.hasErrors()){
            restResponse.setStatus(400);
            restResponse.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResponse;
        }

        return this.baseBiz.addAppVersion(appVersionManage);
    }

    @PutMapping("/instance")
    @ApiOperation("更新单个记录")
    public ObjectRestResponse<Void> updateAppVersion(@RequestBody @ApiParam(value = "待添加记录实例") AppVersionManage appVersionManage, BindingResult bindingResult){
        ObjectRestResponse<Void> restResponse=new ObjectRestResponse<>();
        if(bindingResult.hasErrors()){
            restResponse.setStatus(400);
            restResponse.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResponse;
        }

        return this.baseBiz.updateAppVersion(appVersionManage);
    }

    @GetMapping("/instance/lastestVsrsion")
    @ApiOperation("获取最新版本")
    @IgnoreUserToken
    @IgnoreClientToken
    public ObjectRestResponse<AppVersionManage> getLastestVsrsion(
        @RequestParam(value = "osType",defaultValue = "") String osType,
        @RequestParam(value = "osVersion",defaultValue = "") String osVersion
    ){
        AppVersionManage appVersionManage = new AppVersionManage();
        appVersionManage.setOsType(osType);
        appVersionManage.setOsVersion(osVersion);
        return this.baseBiz.getLastestVsrsion(appVersionManage);
    }

    @GetMapping("/instance/downLoadUrl")
    @ApiOperation("获取下载路径")
    @IgnoreUserToken
    @IgnoreClientToken
    public ObjectRestResponse< List<JSONObject>> getDownloadUrl(){
        ObjectRestResponse< List<JSONObject>> result = new ObjectRestResponse< List<JSONObject>>();
        List<JSONObject> data= this.baseBiz.getDownloadUrl();
        result.setData(data);
        return result;
    }


    @GetMapping("/list")
    @ApiOperation("分页获取记录")
    public TableResultResponse<JSONObject> getAppVersionList(
        @RequestParam(value = "updateType",defaultValue = "") String updateType,
        @RequestParam(value = "appVersion",required = false) Long appVersion,
        @RequestParam(value = "page",defaultValue = "1") Integer page,
        @RequestParam(value = "limit",defaultValue = "10") Integer limit
    ) {
        AppVersionManage appVersionManage=new AppVersionManage();
        appVersionManage.setAppVersion(appVersion);
        appVersionManage.setUpdateType(updateType);

        return this.baseBiz.getAppVersionList(appVersionManage,page,limit,null,null);
    }

    @GetMapping("/instance/{id}")
    @ApiOperation("获取最新版本")
    public ObjectRestResponse<JSONObject> getAppVsrsionById(
        @PathVariable(value = "id") Integer id
    ){
        return this.baseBiz.getAppVsrsionById(id);
    }
}