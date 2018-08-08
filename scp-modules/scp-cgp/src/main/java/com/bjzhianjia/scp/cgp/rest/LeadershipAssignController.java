package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.LeadershipAssignBiz;
import com.bjzhianjia.scp.cgp.entity.LeadershipAssign;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("leadershipAssign")
@CheckClientToken
@CheckUserToken
public class LeadershipAssignController extends BaseController<LeadershipAssignBiz,LeadershipAssign,Integer> {

}