package com.bjzhianjia.scp.party.rest;

import com.bjzhianjia.scp.party.biz.MechanismsPracticesBiz;
import com.bjzhianjia.scp.party.entity.MechanismsPractices;
import com.bjzhianjia.scp.party.vo.MechanismsPracticesVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.common.util.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("mechanismspractices")
@CheckClientToken
@CheckUserToken
@Api(tags = "机制和做法")
public class MechanismsPracticesController extends BaseController<MechanismsPracticesBiz, MechanismsPractices, Integer> {

    @GetMapping("/list")
    @ApiOperation("分页获取列表")
    public TableResultResponse<MechanismsPracticesVo> getMechanismsPracticesList(
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit,
            @RequestParam(value = "partyOrgId", required = false) @ApiParam("党组织ID") Integer partyOrgId,
            @RequestParam(value = "buildDate", defaultValue = "") @ApiParam("品牌建立时间,格式yyyy-MM-dd") String buildDateStr,
            @RequestParam(value = "branchTitle", defaultValue = "") @ApiParam("品牌名称") String branchTitle,
            @RequestParam(value = "branchStatus", defaultValue = "") @ApiParam("品牌状态--字典code") String branchStatus
    ) {
        MechanismsPractices mp = new MechanismsPractices();
        mp.setPartyOrgId(partyOrgId);
        mp.setBranchTitle(branchTitle);
        mp.setBranchStatus(branchStatus);

        if (StringUtils.isNotBlank(buildDateStr)) {
            mp.setBuildDate(DateTools.dateFromStrToDate(buildDateStr));
        }

        return this.baseBiz.getMechanismsPracticesList(mp, page, limit);
    }

    @DeleteMapping("/instance/{id}")
    @ApiOperation("删除单个对象")
    public ObjectRestResponse<Void> deleteBranch(
            @PathVariable(value = "id") Integer id
    ) {
        return this.baseBiz.deleteMechanismsPractices(id);
    }

    @PostMapping("/instance")
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<MechanismsPractices> addBranch(
            @RequestBody @ApiParam("待添加实例对象") MechanismsPracticesVo MechanismsPractices,
            BindingResult bindingResult
    ) {
        ObjectRestResponse<MechanismsPractices> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.addPartyBrand(MechanismsPractices);
    }

    @PutMapping("/instance")
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<MechanismsPractices> updateBranch(
            @RequestBody @ApiParam("待添加实例对象") MechanismsPracticesVo MechanismsPractices,
            BindingResult bindingResult
    ) {
        ObjectRestResponse<MechanismsPractices> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.updatePartyBrand(MechanismsPractices);
    }

    @GetMapping("/instance/{id}")
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<MechanismsPracticesVo> getMechanismsPracticesById(
            @PathVariable(value = "id") @ApiParam("待查询对象ID") Integer id
    ) {
        return this.baseBiz.getMechanismsPracticesById(id);
    }

    @GetMapping("/instance/branchTitle/accurate")
    @ApiOperation("通过名称精确查询品牌")
    @IgnoreUserToken
    @IgnoreClientToken
    public List<MechanismsPractices> accurateByName(@RequestParam(value = "branchTitle") @ApiParam("品牌名称") String branchTitle) {
        return this.baseBiz.accurateByName(branchTitle);
    }

    @GetMapping("/instance/orgId/{orgId}")
    @ApiOperation("根据党组织id查询机制做法对象")
    public ObjectRestResponse<List<MechanismsPractices>> getMechanismsPracticesByOrgId(
            @PathVariable(value = "orgId") @ApiParam("待查询对象ID") Integer orgId
    ) {
        return this.baseBiz.getMechanismsPracticesByOrgId(orgId);
    }

}
