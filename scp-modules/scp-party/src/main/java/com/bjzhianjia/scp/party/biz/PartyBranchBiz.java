package com.bjzhianjia.scp.party.biz;

import com.bjzhianjia.scp.party.entity.PartyBranch;
import com.bjzhianjia.scp.party.entity.PartyBranchFile;
import com.bjzhianjia.scp.party.entity.PartyOrg;
import com.bjzhianjia.scp.party.mapper.PartyBranchMapper;
import com.bjzhianjia.scp.party.vo.MechanismsPracticesVo;
import com.bjzhianjia.scp.party.vo.PartyBranchVo;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 党建品牌
 *
 * @author bo
 * @version 2019-03-31 11:36:17
 * @email 576866311@qq.com
 */
@Service
public class PartyBranchBiz extends BusinessBiz<PartyBranchMapper, PartyBranch> {

    @Autowired
    private PartyBranchFileBiz partyBranchFileBiz;

    @Autowired
    private PartyOrgBiz partyOrgBiz;

    /**
     * 添加单个对象
     *
     * @param partyBranch
     * @return
     */
    public ObjectRestResponse<PartyBranch> addPartyBrand(PartyBranchVo partyBranch) {
        ObjectRestResponse<PartyBranch> restResult = new ObjectRestResponse<>();

        check(partyBranch, restResult, false);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        this.insertSelective(partyBranch);

        restResult.setMessage("添加党建品牌成功");
        restResult.setData(partyBranch);
        return restResult;
    }

    private void check(PartyBranch partyBranch, ObjectRestResponse<PartyBranch> restResult, boolean isUpdate) {

    }

    /**
     * 更新单个对象
     *
     * @param partyBranch
     * @return
     */
    @Transactional
    public ObjectRestResponse<PartyBranch> updatePartyBrand(PartyBranchVo partyBranch) {
        ObjectRestResponse<PartyBranch> restResult = new ObjectRestResponse<>();

        check(partyBranch, restResult, true);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        if (StringUtils.isNotBlank(partyBranch.getBrandFile())) {
            updateBrandFile(partyBranch);
        }

        this.updateSelectiveById(partyBranch);

        restResult.setMessage("更新党建品牌成功");
        restResult.setData(partyBranch);
        return restResult;
    }

    /**
     * 在更新党建品牌的同时，更新党建品牌故事
     *
     * @param partyBranch
     */
    private void updateBrandFile(PartyBranchVo partyBranch) {
        PartyBranchFile branchFile = new PartyBranchFile();

        PartyBranchFile delTemplate = new PartyBranchFile();
        delTemplate.setBranchId(partyBranch.getId());
        partyBranchFileBiz.delete(delTemplate);

        branchFile.setFileUrl(partyBranch.getBrandFile());
        branchFile.setBranchId(partyBranch.getId());

        partyBranchFileBiz.insertSelective(branchFile);
    }

    /**
     * 删除单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<Void> deletePartyBranch(Integer id) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        PartyBranch partyBranch = new PartyBranch();

        partyBranch.setId(id);
        partyBranch.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);

        this.updateSelectiveById(partyBranch);

        restResult.setMessage("删除党建品牌成功");
        return restResult;
    }

    /**
     * 分页获取列表
     *
     * @param branch
     * @return
     */
    public TableResultResponse<PartyBranchVo> getPartyBranchList(PartyBranch branch, int page, int limit) {
        Example example = new Example(PartyBranch.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);

        if (BeanUtils.isNotEmpty(branch.getPartyOrgId())) {
            PartyOrg partyOrg = partyOrgBiz.selectById(branch.getPartyOrgId());
            List<PartyOrg> partyOrgs = partyOrgBiz.mergeSunOrg(partyOrg);
            boolean flag = false;
            if (BeanUtils.isNotEmpty(partyOrgs)) {
                Set<Integer> collect = partyOrgs.stream().map(PartyOrg::getId).collect(Collectors.toSet());
                if (BeanUtils.isNotEmpty(collect)) {
                    criteria.andEqualTo("partyOrgId", branch.getPartyOrgId());
                    flag = true;
                }
            }
            if (!flag) {
                // 如果flag没有被置为true，说明应应查询到的结果集为空
                return new TableResultResponse<>(0, new ArrayList<>());
            }
        }
        if (BeanUtils.isNotEmpty(branch.getBuildDate())) {
            criteria.andEqualTo("buildDate", branch.getBuildDate());
        }
        if (BeanUtils.isNotEmpty(branch.getBranchTitle())) {
            criteria.andLike("branchTitle", "%" + branch.getBranchTitle() + "%");
        }
        if (BeanUtils.isNotEmpty(branch.getBranchStatus())) {
            criteria.andEqualTo("branchStatus", branch.getBranchStatus());
        }

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<PartyBranch> partyBranches = this.selectByExample(example);

        if (BeanUtils.isEmpty(partyBranches)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        List<PartyBranchVo> voList = queryAssist(partyBranches);

        return new TableResultResponse<>(pageInfo.getTotal(), voList);
    }

    private List<PartyBranchVo> queryAssist(List<PartyBranch> partyBranches) {
        List<PartyBranchVo> voList = new ArrayList<>();

        Set<Integer> partyOrgIdSet = new HashSet<>();
        partyBranches.forEach(branch -> {
            PartyBranchVo vo = new PartyBranchVo();
            org.springframework.beans.BeanUtils.copyProperties(branch, vo);
            partyOrgIdSet.add(branch.getPartyOrgId());
            voList.add(vo);
        });

        List<PartyOrg> partyOrgs = partyOrgBiz.selectbyIds(partyOrgIdSet);
        Map<Integer, String> partyOrgIdNameMap = new HashMap<>();
        if (BeanUtils.isNotEmpty(partyOrgs)) {
            partyOrgIdNameMap = partyOrgs.stream().collect(Collectors.toMap(PartyOrg::getId, PartyOrg::getOrgFullName));
        }

        for (PartyBranchVo partyBranchVo : voList) {
            partyBranchVo.setPartyOrgName(partyOrgIdNameMap.get(partyBranchVo.getPartyOrgId()));
        }

        return voList;
    }

    /**
     * 获取单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<PartyBranchVo> getPartyBranchById(Integer id) {
        ObjectRestResponse<PartyBranchVo> restResult = new ObjectRestResponse<>();

        PartyBranch partyBranch = this.selectById(id);

        List<PartyBranch> list = new ArrayList<>();
        if (BeanUtils.isNotEmpty(partyBranch)) {
            list.add(partyBranch);
        }

        List<PartyBranchVo> partyBranchVos = queryAssist(list);
        if (BeanUtils.isNotEmpty(partyBranchVos)) {
            restResult.setData(partyBranchVos.get(0));
        }

        return restResult;
    }

    /**
     * 按名称精确查询
     *
     * @param branchTitle
     * @return
     */
    public List<PartyBranch> accurateByName(String branchTitle) {
        if (StringUtils.isBlank(branchTitle)) {
            return new ArrayList<>();
        }

        Example example = new Example(PartyBranch.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        criteria.andIn("branchTitle", Arrays.asList(StringUtils.split(branchTitle, ",")));

        List<PartyBranch> partyBranches = this.selectByExample(example);
        if (BeanUtils.isEmpty(partyBranches)) {
            return new ArrayList<>();
        }

        return partyBranches;
    }
}