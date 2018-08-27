package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.WritsTemplatesBiz;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("writsTemplates")
@CheckClientToken
@CheckUserToken
public class WritsTemplatesController extends BaseController<WritsTemplatesBiz,WritsTemplates,Integer> {

}