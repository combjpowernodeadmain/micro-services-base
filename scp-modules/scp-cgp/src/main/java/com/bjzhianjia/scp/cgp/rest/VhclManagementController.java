package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.VhclManagementBiz;
import com.bjzhianjia.scp.cgp.entity.VhclManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("vhclManagement")
@CheckClientToken
@CheckUserToken
public class VhclManagementController extends BaseController<VhclManagementBiz,VhclManagement,Integer> {

}