package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.RegTypeRelationBiz;
import com.bjzhianjia.scp.cgp.entity.RegTypeRelation;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("regTypeRelation")
@CheckClientToken
@CheckUserToken
public class RegTypeRelationController extends BaseController<RegTypeRelationBiz,RegTypeRelation,Integer> {

}