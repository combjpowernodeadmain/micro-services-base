package com.bjzhianjia.scp.cgp.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.vo.PartyOrgTree;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.PartyOrgBiz;
import com.bjzhianjia.scp.cgp.entity.PartyOrg;
import com.bjzhianjia.scp.security.common.util.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 党组织管理
 */
@RestController
@RequestMapping("partyOrgs")
@CheckClientToken
@CheckUserToken
@Api(tags = "党组织管理")
public class PartyOrgController extends BaseController<PartyOrgBiz, PartyOrg, String> {

    /**
     * 获取党组织树
     *
     * @return
     */
    @GetMapping("/tree")
    @ApiOperation("获取党组织树")
    public ObjectRestResponse<List<PartyOrgTree>> getPartyOrgTree() {
        ObjectRestResponse<List<PartyOrgTree>> result = new ObjectRestResponse<>();
        result.setData(this.baseBiz.getPartyOrgTree());
        return result;
    }

    /**
     * 党组织翻页列表
     *
     * @return
     */
    @GetMapping
    @ApiOperation("党组织翻页列表")
    public TableResultResponse<JSONObject> getPartyOrgList() {
        return this.baseBiz.getPartyOrgList(null, 0, 0);
    }

    /**
     * 党组织全部定位
     *
     * @return
     */
    @GetMapping("/positions")
    @ApiOperation("党组织全部定位")
    public ObjectRestResponse<List<PartyOrg>> getPartyOrgAllPosition() {
        ObjectRestResponse<List<PartyOrg>> result = new ObjectRestResponse<>();
        result.setData(this.baseBiz.getPartyOrgAllPosition());
        return result;
    }
}