package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.WritsInstancesBiz;
import com.bjzhianjia.scp.cgp.entity.WritsInstances;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("writsInstances")
@CheckClientToken
@CheckUserToken
public class WritsInstancesController extends BaseController<WritsInstancesBiz,WritsInstances,Integer> {

}