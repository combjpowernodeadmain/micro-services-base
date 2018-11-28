package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectInfoCollectBiz;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectInfoCollect;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.ApiParam;

import java.util.Arrays;
import java.util.HashSet;

@RestController
@RequestMapping("regulaObjectInfoCollect")
@CheckClientToken
@CheckUserToken
public class RegulaObjectInfoCollectController extends BaseController<RegulaObjectInfoCollectBiz,RegulaObjectInfoCollect,Integer> {

    /**
     * 查询信息采集列表
     * @param page
     * @param limit
     * @param regulaObjectName
     * @param objId
     * @param regulaObjectType
     * @param infoCommitterTime
     * @param infoApproveStatus
     * @param infoCommitType
     * @param infoCommitter
     * @param infoCommitterName
     * @param infoApproverName
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询信息采集列表")
    public TableResultResponse<JSONObject> getList(
        @RequestParam(value = "page", defaultValue = "1") @ApiParam(value="当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam(value="页容量") Integer limit,
        @RequestParam(value = "regulaObjectName", defaultValue = "") @ApiParam(value="监管对象名称") String regulaObjectName,
        @RequestParam(value = "objId", defaultValue = "") @ApiParam(value="监管对象ID") String objId,
        @RequestParam(value = "regulaObjectType", required = false) @ApiParam(value="监管对象类型") Integer regulaObjectType,
        @RequestParam(value = "infoCommitterTime", defaultValue = "") @ApiParam(value="开始日期(yyyy-MM-dd)") String infoCommitterTime,
        @RequestParam(value = "infoApproveStatus", defaultValue = "") @ApiParam(value="审核状态") String infoApproveStatus,
        @RequestParam(value = "infoCommitType", defaultValue = "") @ApiParam(value="监管对象状态：0-新增|1-更改") String infoCommitType,
        @RequestParam(value = "infoCommitter", defaultValue = "") @ApiParam(value="办理人") String infoCommitter,
        @RequestParam(value = "infoCommitterName", defaultValue = "") @ApiParam(value="办理人姓名") String infoCommitterName,
        @RequestParam(value = "infoApproverName", defaultValue = "") @ApiParam(value="审批人姓名") String infoApproverName
        ) {

        JSONObject queryJObj = new JSONObject();
        queryJObj.put("regulaObjectName", regulaObjectName);
        queryJObj.put("objId", objId);
        queryJObj.put("regulaObjectType", regulaObjectType);
        queryJObj.put("infoCommitterTime", infoCommitterTime);
        /*
         * 默认查询【已通过】【退回】【作废】的列表
         * 对于每一监管对象信息审批记录来说，上面三种状态互斥且唯一
         */
        queryJObj.put("infoApproveStatusSet",
                StringUtils.isBlank(infoApproveStatus) ? new HashSet<String>()
                : new HashSet<>(Arrays.asList(infoApproveStatus.split(","))));
        queryJObj.put("infoCommitType", infoCommitType);
        queryJObj.put("infoCommitter", infoCommitter);
        queryJObj.put("infoCommitterName", infoCommitterName);
        queryJObj.put("infoApproverName", infoApproverName);

        return this.baseBiz.getList(queryJObj,page,limit);
    }

    @PutMapping("/approve")
    @ApiOperation("进行信息审批")
    public ObjectRestResponse<RegulaObjectInfoCollect> approve(
        @RequestBody @ApiParam("待审批对象") RegulaObjectInfoCollect regulaObjectInfoCollect,
        BindingResult bindingResult) {

        ObjectRestResponse<RegulaObjectInfoCollect> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.approve(regulaObjectInfoCollect);
    }
}