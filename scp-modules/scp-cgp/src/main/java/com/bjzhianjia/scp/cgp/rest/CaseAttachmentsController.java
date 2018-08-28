package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;

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
@Api(tags="综合执法 - 案件登记 附件")
public class CaseAttachmentsController extends BaseController<CaseAttachmentsBiz,CaseAttachments,Integer> {

}