package com.bjzhianjia.scp.party.rest;

import com.bjzhianjia.scp.party.entity.PartyBranch;
import com.bjzhianjia.scp.party.vo.BrandStoryFileVo;
import com.bjzhianjia.scp.party.vo.PartyBranchVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.BrandStoryFileBiz;
import com.bjzhianjia.scp.party.entity.BrandStoryFile;
import com.bjzhianjia.scp.security.common.util.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brandStoryFile")
@CheckClientToken
@CheckUserToken
@Api(tags = "党建品牌故事附件")
public class BrandStoryFileController extends BaseController<BrandStoryFileBiz, BrandStoryFile, Integer> {
    @PostMapping("/instance")
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<Void> addBranch(
            @RequestBody @ApiParam("待添加实例对象") BrandStoryFile storyFile,
            BindingResult bindingResult
    ) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.addBrandStoryFile(storyFile);
    }

    @PutMapping("/instance")
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<Void> updateBranch(
            @RequestBody @ApiParam("待添加实例对象") BrandStoryFile storyFile,
            BindingResult bindingResult
    ) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.updateBrandStoryFile(storyFile);
    }

    @DeleteMapping("/instance/{id}")
    @ApiOperation("删除单个对象")
    public ObjectRestResponse<Void> deleteBranch(
            @PathVariable(value = "id") Integer id
    ) {
        return this.baseBiz.deleteBrandStoryFile(id);
    }

    @GetMapping("/list")
    @ApiOperation("分页获取列表")
    public TableResultResponse<BrandStoryFileVo> getPartyBranchList(
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit,
            @RequestParam(value = "storyId", required = false) @ApiParam("党建品牌故事id") Integer storyId,
            @RequestParam(value = "fileUploadDate", defaultValue = "") @ApiParam("品牌建立时间,格式yyyy-MM-dd") String fileUploadDateStr,
            @RequestParam(value = "fileName", defaultValue = "") @ApiParam("品牌故事附件文件名") String fileName,
            @RequestParam(value = "fileState", defaultValue = "") @ApiParam("故事附件状态字典code") String fileState
    ) {
        BrandStoryFile storyFile = new BrandStoryFile();
        storyFile.setStoryId(storyId);
        storyFile.setFileName(fileName);
        storyFile.setFileState(fileState);

        if (StringUtils.isNotBlank(fileUploadDateStr)) {
            storyFile.setFileUploadDate(DateTools.dateFromStrToDate(fileUploadDateStr));
        }

        return this.baseBiz.getPartyBranchList(storyFile, page, limit);
    }

    @GetMapping("/instance/{id}")
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<BrandStoryFileVo> getBrandStoryFileById(
            @PathVariable(value = "id") @ApiParam("待查询对象ID") Integer id
    ) {
        return this.baseBiz.getBrandStoryFileById(id);
    }

    @ApiOperation("按故事获取故事附件")
    @GetMapping("/{storyId}/list")
    public List<BrandStoryFile> getByStoryId(@PathVariable(value = "storyId") Integer storyId) {
        return this.baseBiz.getByStoryId(storyId);
    }
}