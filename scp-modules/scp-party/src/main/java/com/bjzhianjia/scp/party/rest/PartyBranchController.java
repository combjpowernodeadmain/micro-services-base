package com.bjzhianjia.scp.party.rest;

import com.bjzhianjia.scp.party.vo.PartyBranchVo;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.PartyBranchBiz;
import com.bjzhianjia.scp.party.entity.PartyBranch;
import com.bjzhianjia.scp.security.common.util.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.util.List;

@RestController
@RequestMapping("partyBranch")
@CheckClientToken
@CheckUserToken
@Api(tags = "党建品牌")
public class PartyBranchController extends BaseController<PartyBranchBiz, PartyBranch, Integer> {

    @PostMapping("/instance")
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<PartyBranch> addBranch(
            @RequestBody @ApiParam("待添加实例对象") PartyBranchVo partyBranch,
            BindingResult bindingResult
    ) {
        ObjectRestResponse<PartyBranch> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.addPartyBrand(partyBranch);
    }

    @PutMapping("/instance")
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<PartyBranch> updateBranch(
            @RequestBody @ApiParam("待添加实例对象") PartyBranchVo partyBranch,
            BindingResult bindingResult
    ) {
        ObjectRestResponse<PartyBranch> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.updatePartyBrand(partyBranch);
    }

    @DeleteMapping("/instance/{id}")
    @ApiOperation("删除单个对象")
    public ObjectRestResponse<Void> deleteBranch(
            @PathVariable(value = "id") Integer id
    ) {
        return this.baseBiz.deletePartyBranch(id);
    }

    @GetMapping("/list")
    @ApiOperation("分页获取列表")
    public TableResultResponse<PartyBranchVo> getPartyBranchList(
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit,
            @RequestParam(value = "partyOrgId", required = false) @ApiParam("党组织ID") Integer partyOrgId,
            @RequestParam(value = "buildDate", defaultValue = "") @ApiParam("品牌建立时间,格式yyyy-MM-dd") String buildDateStr,
            @RequestParam(value = "branchTitle", defaultValue = "") @ApiParam("品牌名称") String branchTitle,
            @RequestParam(value = "branchStatus", defaultValue = "") @ApiParam("品牌状态--字典code") String branchStatus
    ) {
        PartyBranch branch = new PartyBranch();
        branch.setPartyOrgId(partyOrgId);
        branch.setBranchTitle(branchTitle);
        branch.setBranchStatus(branchStatus);

        if (StringUtils.isNotBlank(buildDateStr)) {
            branch.setBuildDate(DateTools.dateFromStrToDate(buildDateStr));
        }

        return this.baseBiz.getPartyBranchList(branch, page, limit);
    }

    @GetMapping("/instance/{id}")
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<PartyBranchVo> getPartyBranchById(
            @PathVariable(value = "id") @ApiParam("待查询对象ID") Integer id
    ) {
        return this.baseBiz.getPartyBranchById(id);
    }

    @GetMapping("/instance/branchTitle/accurate")
    @ApiOperation("通过名称精确查询品牌")
    @IgnoreUserToken
    @IgnoreClientToken
    public List<PartyBranch> accurateByName(@RequestParam(value = "branchTitle") @ApiParam("品牌名称") String branchTitle) {
        return this.baseBiz.accurateByName(branchTitle);
    }
}