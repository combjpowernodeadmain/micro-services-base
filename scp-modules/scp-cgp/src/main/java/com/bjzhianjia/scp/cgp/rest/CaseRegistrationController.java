package com.bjzhianjia.scp.cgp.rest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("caseRegistration")
@CheckClientToken
@CheckUserToken
@Api(tags = "综合执法 - 案件登记")
public class CaseRegistrationController
    extends BaseController<CaseRegistrationBiz, CaseRegistration, String> {

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("业务--添加单个对象")
    public ObjectRestResponse<Void> addCase(
        @RequestBody @ApiParam(name = "待添加对象实例") @Validated JSONObject caseRegJObj) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        Result<Void> result = this.baseBiz.addCase(caseRegJObj);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/get",method=RequestMethod.GET)
    @ApiOperation("查询 单条记录")
    public ObjectRestResponse<JSONObject> getUserTaskDetail(
        @RequestBody @ApiParam("封装查询条件的对象") @Validated JSONObject jobs,
        BindingResult bindingResult) {
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
    
    @RequestMapping(value="/list",method=RequestMethod.GET)
    @ApiOperation("分页获取对象列表")
    public TableResultResponse<CaseRegistration> getList(
        @RequestParam(value = "gridId", defaultValue = "") @ApiParam("网格ID") Integer gridId,
        @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit
        ){
        CaseRegistration caseRegistration=new CaseRegistration();
        caseRegistration.setGirdId(gridId);
        
        return this.baseBiz.getList(caseRegistration, page, limit);
    }
}