package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.cgp.biz.CaseAttachmentsBiz;
import com.bjzhianjia.scp.cgp.entity.CaseAttachments;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("caseAttachments")
@CheckClientToken
@CheckUserToken
public class CaseAttachmentsController extends BaseController<CaseAttachmentsBiz,CaseAttachments,Integer> {

}