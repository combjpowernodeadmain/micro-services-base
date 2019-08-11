package com.bjzhianjia.scp.party.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.entity.BrandStoryFile;
import com.bjzhianjia.scp.party.entity.PartyBrandStory;
import com.bjzhianjia.scp.party.mapper.PartyBrandStoryMapper;
import com.bjzhianjia.scp.party.util.DateUtil;
import com.bjzhianjia.scp.party.vo.PartyBrandStoryVo;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 党建品牌故事（活动）表
 *
 * @author bo
 * @version 2019-03-31 11:36:17
 * @email 576866311@qq.com
 */
@Service
public class PartyBrandStoryBiz extends BusinessBiz<PartyBrandStoryMapper, PartyBrandStory> {

    @Autowired
    private BrandStoryFileBiz brandStoryFileBiz;

    @Autowired
    private PartyBrandStoryMapper partyBrandStoryMapper;

    @Autowired
    private Environment environment;

    /**
     * 添加单个对象
     *
     * @param brandStory
     * @return
     */
    public ObjectRestResponse<PartyBrandStory> addPartyBrandStory(PartyBrandStoryVo brandStory) {
        ObjectRestResponse<PartyBrandStory> restResult = new ObjectRestResponse<>();

        check(brandStory, restResult, false);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        this.insertSelective(brandStory);

        if (StringUtils.isNotBlank(brandStory.getStoryFile())) {
            BrandStoryFile file = new BrandStoryFile();
            file.setStoryId(brandStory.getId());
            file.setFileUrl(brandStory.getStoryFile());
            updateStoryFile(brandStory);
        }

        restResult.setMessage("添加党建品牌故事成功");
        restResult.setData(brandStory);
        return restResult;
    }

    private void check(PartyBrandStory brandStory, ObjectRestResponse<PartyBrandStory> restResult, boolean b) {

    }

    /**
     * 更新单个对象
     *
     * @param partyBrandStory
     * @return
     */
    public ObjectRestResponse<PartyBrandStory> updatePartyBrand(PartyBrandStoryVo partyBrandStory) {
        ObjectRestResponse<PartyBrandStory> restResult = new ObjectRestResponse<>();

        check(partyBrandStory, restResult, true);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        if (StringUtils.isNotBlank(partyBrandStory.getStoryFile())) {
            updateStoryFile(partyBrandStory);
        }

        this.updateSelectiveById(partyBrandStory);

        restResult.setMessage("更新党建品牌故事成功");
        return restResult;
    }

    /**
     * 更新党建品牌故事的同时更新故事附件
     *
     * @param partyBrandStory
     */
    private void updateStoryFile(PartyBrandStoryVo partyBrandStory) {
        BrandStoryFile branchFile = new BrandStoryFile();

        BrandStoryFile delTemplate = new BrandStoryFile();
        delTemplate.setStoryId(partyBrandStory.getId());
        brandStoryFileBiz.delete(delTemplate);

        branchFile.setFileUrl(partyBrandStory.getStoryFile());
        branchFile.setStoryId(partyBrandStory.getId());

        brandStoryFileBiz.insertSelective(branchFile);
    }

    /**
     * 删除单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<Void> deletePartyBranch(Integer id) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        PartyBrandStory brandStory = new PartyBrandStory();

        brandStory.setId(id);
        brandStory.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);

        this.updateSelectiveById(brandStory);

        restResult.setMessage("删除党建品牌故事成功");
        return restResult;
    }

    /**
     * 分页获取列表
     *
     * @param branchStory
     * @param page
     * @param limit
     * @param additionQuery
     * @return
     */
    public TableResultResponse<PartyBrandStoryVo> getPartyBranchStoryList(PartyBrandStory branchStory, Integer page,
                                                                          Integer limit, JSONObject additionQuery, String isPage) {
        Example example = new Example(PartyBrandStory.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);

        if (BeanUtils.isNotEmpty(branchStory.getBrandId())) {
            criteria.andEqualTo("brandId", branchStory.getBrandId());
        }
        if (BeanUtils.isNotEmpty(branchStory.getStoryUpdDate())) {
            criteria.andEqualTo("storyUpdDate", branchStory.getStoryUpdDate());
        }
        if (BeanUtils.isNotEmpty(branchStory.getStoryTitle())) {
            criteria.andLike("storyTitle", "%" + branchStory.getStoryTitle() + "%");
        }
        if (BeanUtils.isNotEmpty(branchStory.getStoryStateCode())) {
            criteria.andEqualTo("storyStateCode", branchStory.getStoryStateCode());
        }
        // 对故事发布日期用范围查询
        if (BeanUtils.isNotEmpty(additionQuery.getDate("storyUpdDateStart"))) {
            criteria.andBetween("storyUpdDate", additionQuery.getDate("storyUpdDateStart"),
                    additionQuery.getDate("storyUpdDateEnd"));
        }

        Page<Object> pageInfo = null;

        if (BooleanUtil.BOOLEAN_TRUE.equals(isPage)) {
            example.setOrderByClause("id desc");
            pageInfo = PageHelper.startPage(page, limit);
        }
        example.setOrderByClause("story_date desc");
        List<PartyBrandStory> partyBranchStories = this.selectByExample(example);

        if (BeanUtils.isEmpty(partyBranchStories)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        List<PartyBrandStoryVo> voList = queryAssist(partyBranchStories);
        if (BooleanUtil.BOOLEAN_TRUE.equals(isPage)) {
            return new TableResultResponse<>(pageInfo.getTotal(), voList);
        } else {
            return new TableResultResponse<>(partyBranchStories.size(), voList);
        }

    }

    private List<PartyBrandStoryVo> queryAssist(List<PartyBrandStory> partyBranches) {
        List<PartyBrandStoryVo> voList = new ArrayList<>();

        partyBranches.forEach(branch -> {
            PartyBrandStoryVo vo = new PartyBrandStoryVo();
            org.springframework.beans.BeanUtils.copyProperties(branch, vo);
            voList.add(vo);
        });

        return voList;
    }

    /**
     * 获取单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<PartyBrandStoryVo> getPartyBranchStoryById(Integer id) {
        ObjectRestResponse<PartyBrandStoryVo> restResult = new ObjectRestResponse<>();

        PartyBrandStory partyBranch = this.selectById(id);

        List<PartyBrandStory> list = new ArrayList<>();
        if (BeanUtils.isNotEmpty(partyBranch)) {
            list.add(partyBranch);
        }

        List<PartyBrandStoryVo> partyBranchStoryVos = queryAssist(list);
        if (BeanUtils.isNotEmpty(partyBranchStoryVos)) {
            restResult.setData(partyBranchStoryVos.get(0));
        }

        return restResult;
    }

    /**
     * 获取按最新时间排序指定条数的品牌故事名称和地址照片
     *
     * @param limit
     * @param state
     * @return
     */
    public List<JSONObject> getPartyBranchFileByTime(Integer page, Integer limit, String state) {
        //未被删除
        String isdel = BooleanUtil.BOOLEAN_FALSE;
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<JSONObject> listJsonObj = partyBrandStoryMapper.getBrandByTimeAndState(state, isdel);

        //判断数据非空
        if (BeanUtils.isNotEmpty(listJsonObj)) {
            List<JSONObject> list = new ArrayList<>();
            for (JSONObject jsonObject : listJsonObj) {
                JSONObject json = new JSONObject();
                String str = jsonObject.getString("fileUrl");
                JSONArray jsonArray = JSONArray.parseArray(str);
                Object o = jsonArray.get(0);
                String stortTitle = jsonObject.getString("storyTitle");
                json.put("title", stortTitle);
                json.put("fileUrl", o);
                list.add(json);
            }
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 根据党组织ID查询其下活动列表
     *
     * @param page
     * @param limit
     * @return
     */
    public List<JSONObject> getPartyBranchFileById(Integer page, Integer limit, Integer id, String isNotState) {
        //未被删除
        String isdel = BooleanUtil.BOOLEAN_FALSE;
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<JSONObject> listJsonObj = partyBrandStoryMapper.getBrandByBrandId(id,isdel);

        //判断数据非空
        if (BeanUtils.isNotEmpty(listJsonObj)) {
            List<JSONObject> list = new ArrayList<>();
            for (JSONObject jsonObject : listJsonObj) {
                JSONObject json = new JSONObject();
                String str = jsonObject.getString("fileUrl");
                JSONArray jsonArray = JSONArray.parseArray(str);
                Object o = jsonArray.get(0);
                String stortTitle = jsonObject.getString("storyTitle");
                json.put("title", stortTitle);
                json.put("fileUrl", o);
                json.put("fileName",jsonObject.getString("fileName"));
                json.put("storyContent",jsonObject.getString("storyContent"));
                json.put("storyDate",jsonObject.getString("storyDate"));
                list.add(json);
            }
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 无状态区分根据当天时间获取党建故事的记录数
     *
     * @return
     */
    public JSONObject getBrandStoryByTime() {
        JSONObject jsonObject = new JSONObject();
        Example example = new Example(PartyBrandStory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andBetween("crtTime", DateUtil.getDayStartTime(new Date()), DateUtil.getDayEndTime(new Date()));
        List<PartyBrandStory> partyBrandStories = this.selectByExample(example);
        //判断是否为空
        if (BeanUtils.isNotEmpty(partyBrandStories)) {
            jsonObject.put("total", partyBrandStories.size());
            return jsonObject;
        } else {
            jsonObject.put("total", 0);
        }
        return jsonObject;
    }
}