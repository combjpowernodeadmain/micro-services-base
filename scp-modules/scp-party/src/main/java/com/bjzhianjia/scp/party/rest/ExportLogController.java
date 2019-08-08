package com.bjzhianjia.scp.party.rest;

import com.bjzhianjia.scp.party.entity.PartyMember;
import com.bjzhianjia.scp.party.vo.ExportLogVo;
import com.bjzhianjia.scp.party.vo.PartyMemberVo;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.ExportLogBiz;
import com.bjzhianjia.scp.party.entity.ExportLog;
import com.bjzhianjia.scp.security.common.util.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("exportLog")
@CheckClientToken
@CheckUserToken
@Api(tags = "党员出境记录")
public class ExportLogController extends BaseController<ExportLogBiz, ExportLog, Integer> {

    @GetMapping("/list")
    @ApiOperation("分页获取党员列表")
    public TableResultResponse<ExportLogVo> getPartyMemberlist(
            @RequestParam(value = "partyMemId", defaultValue = "") @ApiParam(value = "党员ID") Integer partyMemId,
            @RequestParam(value = "exportDate", defaultValue = "") @ApiParam(value = "出境日期") String exportDateStr,
            @RequestParam(value = "importDate", defaultValue = "") @ApiParam(value = "回国日期") String importDateStr,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam(value = "当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam(value = "页容量") Integer limit
    ) {

        ExportLog params = new ExportLog();

        params.setPartyMemId(partyMemId);
        if (StringUtils.isNotBlank(exportDateStr)) {
            params.setExportDate(DateTools.dateFromStrToDate(exportDateStr));
        }
        if (StringUtils.isNotBlank(importDateStr)) {
            params.setImportDate(DateTools.dateFromStrToDate(importDateStr));
        }

        return this.baseBiz.getExportLogList(params, page, limit);
    }
}