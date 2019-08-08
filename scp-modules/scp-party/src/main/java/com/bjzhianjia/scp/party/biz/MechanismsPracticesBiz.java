package com.bjzhianjia.scp.party.biz;

import com.bjzhianjia.scp.party.entity.MechanismsPractices;
import com.bjzhianjia.scp.party.entity.PartyBranchFile;
import com.bjzhianjia.scp.party.entity.PartyOrg;
import com.bjzhianjia.scp.party.mapper.MechanismsPracticesMapper;
import com.bjzhianjia.scp.party.vo.MechanismsPracticesVo;
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

@Service
public class MechanismsPracticesBiz extends BusinessBiz<MechanismsPracticesMapper, MechanismsPractices> {

    @Autowired
    private PartyOrgBiz partyOrgBiz;
    @Autowired
    private PartyBranchFileBiz partyBranchFileBiz;


    /**
     * 分页获取列表
     *
     * @param mp
     * @return
     */
    public TableResultResponse<MechanismsPracticesVo> getMechanismsPracticesList(MechanismsPractices mp, int page, int limit) {
        Example example = new Example(MechanismsPractices.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);

        if (BeanUtils.isNotEmpty(mp.getPartyOrgId())) {
            PartyOrg partyOrg = partyOrgBiz.selectById(mp.getPartyOrgId());
            List<PartyOrg> partyOrgs = partyOrgBiz.mergeSunOrg(partyOrg);
            boolean flag = false;
            if (BeanUtils.isNotEmpty(partyOrgs)) {
                Set<Integer> collect = partyOrgs.stream().map(PartyOrg::getId).collect(Collectors.toSet());
                if (BeanUtils.isNotEmpty(collect)) {
                    criteria.andEqualTo("partyOrgId", mp.getPartyOrgId());
                    flag = true;
                }
            }
            if (!flag) {
                // 如果flag没有被置为true，说明应应查询到的结果集为空
                return new TableResultResponse<>(0, new ArrayList<>());
            }
        }
        if (BeanUtils.isNotEmpty(mp.getBuildDate())) {
            criteria.andEqualTo("buildDate", mp.getBuildDate());
        }
        if (BeanUtils.isNotEmpty(mp.getBranchTitle())) {
            criteria.andLike("branchTitle", "%" + mp.getBranchTitle() + "%");
        }
        if (BeanUtils.isNotEmpty(mp.getBranchStatus())) {
            criteria.andEqualTo("branchStatus", mp.getBranchStatus());
        }

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<MechanismsPractices> mechanismsPractices = this.selectByExample(example);

        if (BeanUtils.isEmpty(mechanismsPractices)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        List<MechanismsPracticesVo> voList = queryAssist(mechanismsPractices);

        return new TableResultResponse<>(pageInfo.getTotal(), voList);
    }

    private List<MechanismsPracticesVo> queryAssist(List<MechanismsPractices> mechanismsPractices) {
        List<MechanismsPracticesVo> voList = new ArrayList<>();

        Set<Integer> partyOrgIdSet = new HashSet<>();
        mechanismsPractices.forEach(mp -> {
            MechanismsPracticesVo vo = new MechanismsPracticesVo();
            org.springframework.beans.BeanUtils.copyProperties(mp, vo);
            partyOrgIdSet.add(mp.getPartyOrgId());
            voList.add(vo);
        });

        List<PartyOrg> partyOrgs = partyOrgBiz.selectbyIds(partyOrgIdSet);
        Map<Integer, String> partyOrgIdNameMap = new HashMap<>();
        if (BeanUtils.isNotEmpty(partyOrgs)) {
            partyOrgIdNameMap = partyOrgs.stream().collect(Collectors.toMap(PartyOrg::getId, PartyOrg::getOrgFullName));
        }

        for (MechanismsPracticesVo mechanismsPracticesVo : voList) {
            mechanismsPracticesVo.setPartyOrgName(partyOrgIdNameMap.get(mechanismsPracticesVo.getPartyOrgId()));
        }

        return voList;
    }

    /**
     * 删除单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<Void> deleteMechanismsPractices(Integer id) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        MechanismsPractices mechanismsPractices = new MechanismsPractices();

        mechanismsPractices.setId(id);
        mechanismsPractices.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);

        this.updateSelectiveById(mechanismsPractices);

        restResult.setMessage("删除党建品牌成功");
        return restResult;
    }

    /**
     * 添加单个对象
     *
     * @param MechanismsPractices
     * @return
     */
    public ObjectRestResponse<MechanismsPractices> addPartyBrand(MechanismsPracticesVo MechanismsPractices) {
        ObjectRestResponse<MechanismsPractices> restResult = new ObjectRestResponse<>();

        check(MechanismsPractices, restResult, false);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        this.insertSelective(MechanismsPractices);

        restResult.setMessage("添加党建品牌成功");
        restResult.setData(MechanismsPractices);
        return restResult;
    }

    private void check(MechanismsPractices MechanismsPractices, ObjectRestResponse<MechanismsPractices> restResult, boolean isUpdate) {

    }

    /**
     * 更新单个对象
     *
     * @param MechanismsPractices
     * @return
     */
    @Transactional
    public ObjectRestResponse<MechanismsPractices> updatePartyBrand(MechanismsPracticesVo MechanismsPractices) {
        ObjectRestResponse<MechanismsPractices> restResult = new ObjectRestResponse<>();

        check(MechanismsPractices, restResult, true);
        if (restResult.getStatus() == 400) {
            return restResult;
        }

        if (StringUtils.isNotBlank(MechanismsPractices.getBrandFile())) {
            updateBrandFile(MechanismsPractices);
        }

        this.updateSelectiveById(MechanismsPractices);

        restResult.setMessage("更新党建品牌成功");
        restResult.setData(MechanismsPractices);
        return restResult;
    }

    /**
     * 在更新党建品牌的同时，更新党建品牌故事
     *
     * @param MechanismsPractices
     */
    private void updateBrandFile(MechanismsPracticesVo MechanismsPractices) {
        PartyBranchFile branchFile = new PartyBranchFile();

        PartyBranchFile delTemplate = new PartyBranchFile();
        delTemplate.setBranchId(MechanismsPractices.getId());
        partyBranchFileBiz.delete(delTemplate);

        branchFile.setFileUrl(MechanismsPractices.getBrandFile());
        branchFile.setBranchId(MechanismsPractices.getId());

        partyBranchFileBiz.insertSelective(branchFile);
    }

    /**
     * 获取单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<MechanismsPracticesVo> getMechanismsPracticesById(Integer id) {
        ObjectRestResponse<MechanismsPracticesVo> restResult = new ObjectRestResponse<>();

        MechanismsPractices MechanismsPractices = this.selectById(id);

        List<MechanismsPractices> list = new ArrayList<>();
        if (BeanUtils.isNotEmpty(MechanismsPractices)) {
            list.add(MechanismsPractices);
        }

        List<MechanismsPracticesVo> MechanismsPracticesVos = queryAssist(list);
        if (BeanUtils.isNotEmpty(MechanismsPracticesVos)) {
            restResult.setData(MechanismsPracticesVos.get(0));
        }

        return restResult;
    }

    /**
     * 按名称精确查询
     *
     * @param branchTitle
     * @return
     */
    public List<MechanismsPractices> accurateByName(String branchTitle) {
        if (StringUtils.isBlank(branchTitle)) {
            return new ArrayList<>();
        }

        Example example = new Example(MechanismsPractices.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        criteria.andIn("branchTitle", Arrays.asList(StringUtils.split(branchTitle, ",")));

        List<MechanismsPractices> MechanismsPracticeses = this.selectByExample(example);
        if (BeanUtils.isEmpty(MechanismsPracticeses)) {
            return new ArrayList<>();
        }

        return MechanismsPracticeses;
    }

}
