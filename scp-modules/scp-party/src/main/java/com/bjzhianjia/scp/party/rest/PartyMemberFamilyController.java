package com.bjzhianjia.scp.party.rest;

import com.bjzhianjia.scp.party.entity.PartyMember;
import com.bjzhianjia.scp.party.vo.PartyMemberFamilyVo;
import com.bjzhianjia.scp.party.vo.PartyMemberVo;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.PartyMemberFamilyBiz;
import com.bjzhianjia.scp.party.entity.PartyMemberFamily;
import com.bjzhianjia.scp.security.common.util.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("partyMemberFamily")
@CheckClientToken
@CheckUserToken
@Api(tags = "党员家庭成员管理")
public class PartyMemberFamilyController extends BaseController<PartyMemberFamilyBiz, PartyMemberFamily, Integer> {

    @GetMapping("/list")
    @ApiOperation("分页获取党员列表")
    public TableResultResponse<PartyMemberFamilyVo> getPartyMemberlist(
            @RequestParam(value = "partyMemId", defaultValue = "") @ApiParam(value = "党员id") Integer partyMemId,
            @RequestParam(value = "name", defaultValue = "") @ApiParam(value = "姓名") String name,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam(value = "当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam(value = "页容量") Integer limit
    ) {

        PartyMemberFamily params = new PartyMemberFamily();

        params.setName(name);
        params.setPartyMemId(partyMemId);

        return this.baseBiz.getPartyMemberFamilyList(params, page, limit);
    }
}