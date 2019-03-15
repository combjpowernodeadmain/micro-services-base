package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.wf.base.utils.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.LawEnforcePathBiz;
import com.bjzhianjia.scp.cgp.entity.LawEnforcePath;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.vo.LawEnforcePathVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;

import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

/**
 * 
 * LawEnforcePathController 执法轨迹记录控制器.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月4日          admin      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author admin
 *
 */
@RestController
@RequestMapping("lawEnforcePath")
@CheckClientToken
@CheckUserToken
@Api(tags="执法轨迹记录管理")
public class LawEnforcePathController extends BaseController<LawEnforcePathBiz,LawEnforcePath,Integer> {
    @Autowired
    private LawEnforcePathBiz lawEnforcePathBiz;

    /**
     * 添加执法轨迹
     * 
     * @param LawEnforcePathVo
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.POST )
    @ResponseBody
    @ApiOperation(value="添加执法轨迹")
    public ObjectRestResponse<Void> add(
        @RequestBody  @ApiParam(value="执法轨迹") LawEnforcePathVo lawEnforcePathVo) {

        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();
        restResult.setStatus(400);
        restResult.setMessage("非法参数");

        if (StringUtils.isBlank(lawEnforcePathVo.getMapInfo())) {
            return restResult;
        }

        //mapinfo转换为单独的字段
        ObjectRestResponse<Void> voidObjectRestResponse = this.baseBiz.addLawEnforcePath(lawEnforcePathVo);
        if (voidObjectRestResponse.getStatus()==400) return restResult;
        restResult.setStatus(200);
        restResult.setMessage("ok");
        return restResult;
    }



    /**
     * 查询指定用户，指定时间段的行为轨迹
     * 
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    @RequestMapping(value = "user", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value="执法轨迹")
    public ObjectRestResponse<JSONArray> getByUserIdAndDate(
        @RequestParam("") @ApiParam(value="用户id") String userId,
        @RequestParam("") @ApiParam(value="开始时间") String startTime,
        @RequestParam("") @ApiParam(value="结束时间") String endTime) {

        ObjectRestResponse<JSONArray> result = new ObjectRestResponse<>();
        result.setStatus(400);
        result.setMessage("非法参数");
        if (StringUtil.isBlank(userId) || StringUtil.isBlank(startTime)
            || StringUtil.isBlank(endTime)) {
            return result;
        }

        JSONArray array = lawEnforcePathBiz.getByUserIdAndDate(userId, startTime, endTime);

        if (!array.isEmpty()) {
            result.setStatus(200);
            result.setMessage("ok");
            result.setData(array);
        }else {
            result.setMessage("没有定位记录！");
        }
        return result;
    }
    /**
     * 通过用户id查询定位
     * @param userId
     * @return
     */
    @RequestMapping(value = "user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询指定用户定位")
    public ObjectRestResponse<JSONObject> getByUserId(
        @PathVariable("userId") @ApiParam("用户id") String userId) {
        ObjectRestResponse<JSONObject> result = new ObjectRestResponse<>();
        result.setStatus(400);
        result.setMessage("非法参数");

        if (StringUtil.isBlank(userId)) {
            return result;
        }
        JSONObject obj = lawEnforcePathBiz.getMapInfoByUserId(userId);
        if(obj != null) {
            result.setStatus(200);
            result.setMessage("ok");
            result.setData(obj);
        }
        return result;
    }
}