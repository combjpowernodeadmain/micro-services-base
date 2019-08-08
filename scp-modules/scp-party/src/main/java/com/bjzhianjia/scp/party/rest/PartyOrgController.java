package com.bjzhianjia.scp.party.rest;

import com.bjzhianjia.scp.party.vo.PartyOrgTree;
import com.bjzhianjia.scp.party.vo.PartyOrgVo;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.PartyOrgBiz;
import com.bjzhianjia.scp.party.entity.PartyOrg;
import com.bjzhianjia.scp.security.common.util.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("partyOrg")
@CheckClientToken
@CheckUserToken
@Api(tags = "党组织管理")
public class PartyOrgController extends BaseController<PartyOrgBiz, PartyOrg, Integer> {


    @ApiOperation("获取党组织树")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<PartyOrgTree> getTree() {
        List<PartyOrg> all = this.baseBiz.getValidateList();

        List<PartyOrgTree> trees = new ArrayList<>();
        all.forEach(o -> {
            PartyOrgTree partyOrgTree = new PartyOrgTree(o.getId(), o.getParentId(), o.getOrgFullName(), o.getOrgShortName(), o.getMapInfo());
            trees.add(partyOrgTree);
        });

        return TreeUtil.bulid(trees, -1, null);
    }

    @GetMapping("/instance/{id}")
    public ObjectRestResponse<PartyOrgVo> getPartyOrg(@PathVariable("id") Integer id) {
        return this.baseBiz.getPartyOrg(id);
    }
}