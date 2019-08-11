package com.bjzhianjia.scp.party.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.biz.PartyBrandStoryBiz;
import com.bjzhianjia.scp.party.entity.PartyBrandStory;
import com.bjzhianjia.scp.party.util.DateUtil;
import com.bjzhianjia.scp.party.vo.PartyBrandStoryVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.DateTools;
import com.netflix.discovery.converters.Auto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("partyBrandStory")
@CheckClientToken
@CheckUserToken
@Api(tags = "党建品牌故事")
public class PartyBrandStoryController extends BaseController<PartyBrandStoryBiz, PartyBrandStory, Integer> {

    @PostMapping("/instance")
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<PartyBrandStory> addPartyBrandStory(
            @RequestBody @ApiParam("待添加实例对象") PartyBrandStoryVo brandStory,
            BindingResult bindingResult
    ) {
        ObjectRestResponse<PartyBrandStory> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.addPartyBrandStory(brandStory);
    }

    @PutMapping("/instance")
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<PartyBrandStory> updatePartyBrandStory(
            @RequestBody @ApiParam("待添加实例对象") PartyBrandStoryVo partyBrandStory,
            BindingResult bindingResult
    ) {
        ObjectRestResponse<PartyBrandStory> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        return this.baseBiz.updatePartyBrand(partyBrandStory);
    }

    @DeleteMapping("/instance/{id}")
    @ApiOperation("删除单个对象")
    public ObjectRestResponse<Void> deletePartyBrandStoryh(
            @PathVariable(value = "id") Integer id
    ) {
        return this.baseBiz.deletePartyBranch(id);
    }

    @GetMapping("/list")
    @ApiOperation("分页获取列表")
    public TableResultResponse<PartyBrandStoryVo> getPartyBranchStoryList(
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit,
            @RequestParam(value = "brandId", required = false) @ApiParam("党建品牌id") Integer brandId,
            @RequestParam(value = "storyUpdDate", defaultValue = "") @ApiParam("故事发布日期,格式yyyy-MM-dd") String storyUpdDate,
            @RequestParam(value = "storyUpdDateStart", defaultValue = "") @ApiParam("故事查询开始日期,格式yyyy-MM-dd") String storyUpdDateStartStr,
            @RequestParam(value = "storyUpdDateEnd", defaultValue = "") @ApiParam("故事查询结束日期,格式yyyy-MM-dd") String storyUpdDateEndStr,
            @RequestParam(value = "storyTitle", defaultValue = "") @ApiParam("品牌故事标题") String storyTitle,
            @RequestParam(value = "storyStateCode", defaultValue = "") @ApiParam("故事状态--字典code") String storyStateCode,
            @RequestParam(value = "isPage", defaultValue = "1") @ApiParam("是否分页") String isPage
    ) {
        PartyBrandStory branch = new PartyBrandStory();
        branch.setBrandId(brandId);
        branch.setStoryTitle(storyTitle);
        branch.setStoryStateCode(storyStateCode);

        JSONObject additionQuery = new JSONObject();

        if (StringUtils.isNotBlank(storyUpdDate)) {
            branch.setStoryUpdDate(DateTools.dateFromStrToDate(storyUpdDate));
        }
        if (StringUtils.isNotBlank(storyUpdDateStartStr) && StringUtils.isNotBlank(storyUpdDateEndStr)) {
            additionQuery.put("storyUpdDateStart", DateUtil.dateFromStrToDate(storyUpdDateStartStr, false));
            additionQuery.put("storyUpdDateEnd",
                    DateUtil.theDayOfTommorrow(DateUtil.dateFromStrToDate(storyUpdDateEndStr, false)));
        }

        return this.baseBiz.getPartyBranchStoryList(branch, page, limit, additionQuery, isPage);
    }

    @GetMapping("/instance/{id}")
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<PartyBrandStoryVo> getPartyBranchStoryById(
            @PathVariable(value = "id") @ApiParam("待查询对象ID") Integer id
    ) {
        return this.baseBiz.getPartyBranchStoryById(id);
    }

    @GetMapping("/instance/list")
    @ApiOperation("查询品牌故事列表")
    public List<JSONObject> getPartyBranchFileByTime(
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "6") @ApiParam("页容量") Integer limit,
            @RequestParam(value = "state", defaultValue = "root_biz_brandStory_zy") @ApiParam("查询状态") String state
    ) {
        return this.baseBiz.getPartyBranchFileByTime(page, limit, state);
    }

    @GetMapping("/instance/{id}/list")
    @ApiOperation("根据党组织id查询品牌故事列表")
    public List<JSONObject> getPartyBranchFileByTime(
            @PathVariable("id") @ApiParam(value = "党组织id") Integer id,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("当前页") Integer page,
            @RequestParam(value = "limit", defaultValue = "4") @ApiParam("页容量") Integer limit,
            @RequestParam(value = "isNotState", defaultValue = "") @ApiParam("查询状态") String isNotState
    ) {
        return this.baseBiz.getPartyBranchFileById(page, limit, id, isNotState);
    }

    @GetMapping("/brandStroy/total")
    @ApiOperation("根据时间范围查询品牌故事的记录数")
    public ObjectRestResponse<JSONObject> getBrandStoryByTime() {
        ObjectRestResponse<JSONObject> objectObjectRestResponse = new ObjectRestResponse<>();
        objectObjectRestResponse.setMessage("请求成功");
        JSONObject brandStoryByTime = this.baseBiz.getBrandStoryByTime();
        objectObjectRestResponse.setData(brandStoryByTime);
        return objectObjectRestResponse;
    }
}