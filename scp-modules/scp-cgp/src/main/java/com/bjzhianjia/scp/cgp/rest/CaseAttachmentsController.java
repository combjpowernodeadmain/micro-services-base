package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.CaseAttachmentsBiz;
import com.bjzhianjia.scp.cgp.entity.CaseAttachments;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

/**
 * CaseAttachmentsController 类描述.综合执法 - 案件登记 附件
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月9日          can      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author can
 *
 */
@RestController
@RequestMapping("caseAttachments")
@CheckClientToken
@CheckUserToken
@Api(tags = "综合执法 - 案件登记 附件")
public class CaseAttachmentsController extends BaseController<CaseAttachmentsBiz, CaseAttachments, Integer> {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("分页查询对象")
    public TableResultResponse<CaseAttachments> getList(
        @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit,
        @RequestParam(value = "caseId", defaultValue = "") @ApiParam("案件ID") String caseId) {
        TableResultResponse<CaseAttachments> restResult = new TableResultResponse<>();

        CaseAttachments caseAttachments = new CaseAttachments();
        caseAttachments.setCaseId(caseId);

        restResult = this.baseBiz.getList(caseAttachments, page, limit);

        restResult.setStatus(200);
        return restResult;
    }

    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<CaseAttachments> add(
        @RequestBody @Validated @ApiParam("待添加对象实例") List<CaseAttachments> caseAttachments, BindingResult bindingResult) {
        ObjectRestResponse<CaseAttachments> restResult = new ObjectRestResponse<>();
        this.baseBiz.add(caseAttachments);
        return restResult;
    }
}