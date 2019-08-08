package com.bjzhianjia.scp.party.rest;

import com.bjzhianjia.scp.party.vo.PartyBranchFileVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.PartyBranchFileBiz;
import com.bjzhianjia.scp.party.entity.PartyBranchFile;
import com.bjzhianjia.scp.security.common.util.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("partyBranchFile")
@CheckClientToken
@CheckUserToken
@Api(tags = "党建品牌附件")
public class PartyBranchFileController extends BaseController<PartyBranchFileBiz, PartyBranchFile, Integer> {

    @PostMapping("/instance")
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<Void> addBranchFile(@RequestBody @ApiParam("待添加实例对象") PartyBranchFile brandFile,
                                                  BindingResult bindingResult) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.addBranchFile(brandFile);
    }

    @PutMapping("/instance")
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<Void> updateBranchFile(@RequestBody @ApiParam("待添加实例对象") PartyBranchFile brandFile,
                                                     BindingResult bindingResult) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.updateBranchFile(brandFile);
    }

    @DeleteMapping("/instance/{id}")
    @ApiOperation("删除单个对象")
    public ObjectRestResponse<Void> deleteBranchFile(@PathVariable(value = "id") Integer id) {
        return this.baseBiz.deleteBranchFile(id);
    }

    @GetMapping("/list")
    @ApiOperation("分页获取列表")
    public TableResultResponse<PartyBranchFileVo> getPartyBranchFileList(
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit,
            @RequestParam(value = "branchId", required = false) @ApiParam("党建品牌id") Integer branchId,
            @RequestParam(value = "fileUploadDate", defaultValue = "") @ApiParam("附件上传日期,格式yyyy-MM-dd") String fileUploadDate,
            @RequestParam(value = "fileName", defaultValue = "") @ApiParam("品牌附件文件名") String fileName,
            @RequestParam(value = "fileStatus", defaultValue = "") @ApiParam("附件状态--字典code") String fileStatus) {
        PartyBranchFile branch = new PartyBranchFile();
        branch.setBranchId(branchId);
        branch.setFileName(fileName);
        branch.setFileStatus(fileStatus);

        if (StringUtils.isNotBlank(fileUploadDate)) {
            branch.setFileUploadDate(DateTools.dateFromStrToDate(fileUploadDate));
        }

        return this.baseBiz.getPartyBranchFileList(branch, page, limit);
    }

    @GetMapping("/instance/{id}")
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<PartyBranchFileVo> getPartyBranchById(
            @PathVariable(value = "id") @ApiParam("待查询对象ID") Integer id) {
        return this.baseBiz.getPartyBranchFileById(id);
    }

    @ApiOperation("按品牌获取品牌附件")
    @GetMapping("{brandId}/list")
    public List<PartyBranchFile> getpartyBrandFileByBrandId(
            @PathVariable("brandId") Integer brandId
    ) {
        return baseBiz.getpartyBrandFileByBrandId(brandId);
    }
}