package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectInfoCollectBiz;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectInfoCollect;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("regulaObjectInfoCollect")
@CheckClientToken
@CheckUserToken
public class RegulaObjectInfoCollectController extends BaseController<RegulaObjectInfoCollectBiz,RegulaObjectInfoCollect,Integer> {

}