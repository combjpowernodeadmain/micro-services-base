package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.ConcernedPersonBiz;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("cleConcernedPerson")
@CheckClientToken
@CheckUserToken
public class CLEConcernedPersonController extends BaseController<ConcernedPersonBiz,ConcernedPerson,Integer> {

}