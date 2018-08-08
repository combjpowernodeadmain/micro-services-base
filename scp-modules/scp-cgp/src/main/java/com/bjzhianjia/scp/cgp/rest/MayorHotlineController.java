package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.MayorHotlineBiz;
import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("mayorHotline")
@CheckClientToken
@CheckUserToken
public class MayorHotlineController extends BaseController<MayorHotlineBiz,MayorHotline,Integer> {

}