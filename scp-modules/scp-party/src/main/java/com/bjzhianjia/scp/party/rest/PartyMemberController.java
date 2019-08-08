package com.bjzhianjia.scp.party.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.util.DateUtil;
import com.bjzhianjia.scp.party.vo.PartyMemberVo;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.PartyMemberBiz;
import com.bjzhianjia.scp.party.entity.PartyMember;
import com.bjzhianjia.scp.security.common.util.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("partyMember")
@CheckClientToken
@CheckUserToken
@Api(tags = "党员管理")
public class PartyMemberController extends BaseController<PartyMemberBiz, PartyMember, Integer> {

    @GetMapping("/list")
    @ApiOperation("分页获取党员列表")
    public TableResultResponse<PartyMemberVo> getPartyMemberlist(
            @RequestParam(value = "name", defaultValue = "") @ApiParam(value = "党员姓名") String name,
            @RequestParam(value = "identityNum", defaultValue = "") @ApiParam(value = "党员身份号") String identityNum,
            @RequestParam(value = "sex", defaultValue = "") @ApiParam(value = "性别") String sex,
            @RequestParam(value = "nation", defaultValue = "") @ApiParam(value = "民族") String nation,
            @RequestParam(value = "origin", defaultValue = "") @ApiParam(value = "籍贯") String origin,
            @RequestParam(value = "partyJoinDate", defaultValue = "") @ApiParam(value = "入党日期") String partyJoinDate,
            @RequestParam(value = "queryJoinDateStart", defaultValue = "") @ApiParam(value = "入党日期查询开始时间") String queryJoinDateStartStr,
            @RequestParam(value = "queryJoinDateEnd", defaultValue = "") @ApiParam(value = "入党日期查询结束时间") String queryJoinDateEndStr,
            @RequestParam(value = "partyFullOrg", required = false) @ApiParam(value = "转正时所在支部") Integer partyFullOrg,
            @RequestParam(value = "preJoinOrg", required = false) @ApiParam(value = "转正时所在支部") Integer preJoinOrg,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam(value = "当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam(value = "页容量") Integer limit
    ) {

        PartyMember params = new PartyMember();

        params.setName(name);
        params.setIdentityNum(identityNum);
        params.setSex(sex);
        params.setNation(nation);
        params.setOrigin(origin);

        JSONObject additionQuery = new JSONObject();
        if (StringUtils.isNotBlank(queryJoinDateStartStr) && StringUtils.isNotBlank(queryJoinDateEndStr)) {
            additionQuery
                    .put("queryJoinDateStart", DateUtil.dateFromStrToDate(queryJoinDateStartStr, DateUtil.DATE_FORMAT_DF));
            additionQuery.put("queryJoinDateEnd",
                    DateUtil.theDayOfTommorrow(DateUtil.dateFromStrToDate(queryJoinDateEndStr, DateUtil.DATE_FORMAT_DF)));
        }

        if (StringUtils.isNotBlank(partyJoinDate)) {
            params.setPartyJoinDate(DateTools.dateFromStrToDate(partyJoinDate));
        }

        params.setPartyFullOrg(partyFullOrg);
        params.setPreJoinOrg(preJoinOrg);
        return this.baseBiz.getPartyMemberList(params, page, limit, additionQuery);
    }

    @GetMapping("/instance/{id}")
    @ApiOperation("获取单个对象")
    public ObjectRestResponse<PartyMemberVo> getPartyMember(@PathVariable("id") Integer id) {
        return this.baseBiz.getPartyMemberById(id);
    }

    // TODO 对党的添加、编辑及删除
    @PostMapping("/instance")
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<PartyMember> addPartyMember(
            @RequestBody @ApiParam("待添加对象") PartyMember partyMember,
            BindingResult bindingResult) {

        ObjectRestResponse<PartyMember> restResponse = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResponse.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            restResponse.setStatus(400);
            return restResponse;
        }

        return this.baseBiz.addPartyMember(partyMember);
    }

    @PutMapping("/instance")
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<PartyMember> updatePartyMember(
            @RequestBody @ApiParam("待添加对象") PartyMember partyMember,
            BindingResult bindingResult) {

        ObjectRestResponse<PartyMember> restResponse = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResponse.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            restResponse.setStatus(400);
            return restResponse;
        }

        return this.baseBiz.updatePartyMember(partyMember);
    }

    /**
     * 通过所属网格id获取党员人员名称列表
     *
     * @param gridId 所属网格id
     * @return
     */
    @GetMapping("/memberNames")
    public ObjectRestResponse<List<PartyMember>> getPartyMemberNamesByGridId(
            @RequestParam(value = "gridId", defaultValue = "") @ApiParam("所属网格id") String gridId) {
        ObjectRestResponse<List<PartyMember>> restResponse = new ObjectRestResponse<>();
        restResponse.setData(this.baseBiz.getPartyMemberNamesByGridId(gridId));
        return restResponse;
    }
}