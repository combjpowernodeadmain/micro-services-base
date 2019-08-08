package com.bjzhianjia.scp.party.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.OrgMemberRelationBiz;
import com.bjzhianjia.scp.party.entity.OrgMemberRelation;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("orgMemberRelation")
@CheckClientToken
@CheckUserToken
@Api(tags = "党组织党员关系")
public class OrgMemberRelationController extends BaseController<OrgMemberRelationBiz, OrgMemberRelation, Integer> {

}