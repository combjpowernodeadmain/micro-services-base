package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.PublicOpinionBiz;
import com.bjzhianjia.scp.cgp.entity.PublicOpinion;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("publicOpinion")
@CheckClientToken
@CheckUserToken
public class PublicOpinionController extends BaseController<PublicOpinionBiz,PublicOpinion,Integer> {

}