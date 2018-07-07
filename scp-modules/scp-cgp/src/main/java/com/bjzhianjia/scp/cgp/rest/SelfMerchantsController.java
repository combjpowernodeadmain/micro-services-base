package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.SelfMerchantsBiz;
import com.bjzhianjia.scp.cgp.entity.SelfMerchants;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("selfMerchants")
@CheckClientToken
@CheckUserToken
public class SelfMerchantsController extends BaseController<SelfMerchantsBiz,SelfMerchants,Integer> {

}