package com.bjzhianjia.scp.cgp.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridMemberBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("areaGridMember")
@CheckClientToken
@CheckUserToken
public class AreaGridMemberController
    extends BaseController<AreaGridMemberBiz, AreaGridMember, Integer> {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("分页获取记录")
    public TableResultResponse<JSONObject> getList(
        @RequestParam(value = "memId", defaultValue = "") String memId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        AreaGridMember areaGridMember = new AreaGridMember();
        areaGridMember.setGridMember(memId);

        return this.baseBiz.getList(areaGridMember, page, limit);
    }

    @RequestMapping(value="/get/member/detail")
    public ObjectRestResponse<JSONObject> getDetailOfAeraMem(
        @RequestParam(value = "memID", defaultValue = "") @ApiParam("待查询网格员ID") String memId) {
        return null;
    }
}