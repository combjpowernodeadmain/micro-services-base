package com.bjzhianjia.scp.cgp.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.vo.PartyOrgTree;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.PartyOrgBiz;
import com.bjzhianjia.scp.cgp.entity.PartyOrg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

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
     * @param orgShortName 党组织简称
     * @param page         页码
     * @param limit        页容量
     * @return
     */
    @GetMapping
    @ApiOperation("党组织翻页列表")
    public TableResultResponse<JSONObject> getPartyOrgList(
            @RequestParam(value = "orgShortName", defaultValue = "") @ApiParam("党组织简称") String orgShortName,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit) {
        PartyOrg partyOrg = new PartyOrg();
        partyOrg.setOrgShortName(orgShortName);
        return this.baseBiz.getPartyOrgList(partyOrg, page, limit);
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

    /**
     * 通过主键获取党组织详情
     *
     * @param partyOrgId 主键id
     * @return
     */
    @GetMapping("/partyOrgId/{partyOrgId}")
    @ApiOperation("通过主键获取党组织详情")
    public ObjectRestResponse<JSONObject> getByPartyOrgId(
            @PathVariable("partyOrgId") @ApiParam("主键id") String partyOrgId) {
        ObjectRestResponse<JSONObject> result = new ObjectRestResponse<>();
        result.setData(this.baseBiz.getByPartyOrgId(partyOrgId));
        return result;
    }

    /**
     * 删除党组织
     *
     * @param partyOrgId 主键id
     * @return
     */
    @DeleteMapping("/partyOrgId/{partyOrgId}")
    @ApiOperation("删除党组织")
    public ObjectRestResponse<Void> delPartyOrg(
            @PathVariable("partyOrgId") @ApiParam("主键id") String partyOrgId) {
        ObjectRestResponse<Void> result = new ObjectRestResponse<>();
        try {
            this.baseBiz.deleteById(partyOrgId);
        } catch (Exception e) {
            result.setStatus(400);
            result.setMessage(e.getMessage());
        }
        return result;
    }
}