package com.bjzhianjia.scp.party.biz;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.entity.PartyOrg;
import com.bjzhianjia.scp.party.feign.DictFeign;
import com.bjzhianjia.scp.party.mapper.PartyOrgMapper;
import com.bjzhianjia.scp.party.vo.PartyMemberVo;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.PartyMember;
import com.bjzhianjia.scp.party.mapper.PartyMemberMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 党员表
 *
 * @author bo
 * @version 2019-03-27 21:07:17
 * @email 576866311@qq.com
 */
@Service
public class PartyMemberBiz extends BusinessBiz<PartyMemberMapper, PartyMember> {

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private PartyOrgMapper partyOrgMapper;

    /**
     * 分页获取列表
     *
     * @param params
     * @param additionQuery
     * @return
     */
    public TableResultResponse<PartyMemberVo> getPartyMemberList(PartyMember params, int page, int limit,
                                                                 JSONObject additionQuery) {
        TableResultResponse<PartyMemberVo> tableResult = new TableResultResponse<>();

        Example example = new Example(PartyMember.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);

        if (StringUtils.isNotBlank(params.getName())) {
            criteria.andLike("name", "%" + params.getName() + "%");
        }

        if (StringUtils.isNotBlank(params.getIdentityNum())) {
            criteria.andLike("identityNum", "%" + params.getIdentityNum() + "%");
        }

        if (StringUtils.isNotBlank(params.getSex())) {
            criteria.andEqualTo("sex", params.getSex());
        }

        if (StringUtils.isNotBlank(params.getNation())) {
            criteria.andLike("nation", "%" + params.getNation() + "%");
        }

        if (StringUtils.isNotBlank(params.getOrigin())) {
            criteria.andLike("origin", "%" + params.getOrigin() + "%");
        }

        if (BeanUtils.isNotEmpty(params.getPartyJoinDate())) {
            criteria.andEqualTo("partyJoinDate", params.getPartyJoinDate());
        }

        if (BeanUtils.isNotEmpty(params.getPartyFullOrg())) {
            criteria.andEqualTo("partyFullOrg", params.getPartyFullOrg());
        }

        if (BeanUtils.isNotEmpty(params.getPreJoinOrg())) {
            // 如果进行了按党组织查询，则包含其子党组织
            List<PartyOrg> orgs = bindSonOrg(params.getPreJoinOrg());

            Set<Integer> orgIds = orgs.stream().map(PartyOrg::getId).collect(Collectors.toSet());
            if (BeanUtils.isNotEmpty(orgIds)) {
                criteria.andIn("preJoinOrg", orgIds);
            } else {
                tableResult.getData().setTotal(0);
                tableResult.getData().setRows(new ArrayList<>());
                return tableResult;
            }
        }

        // 入党时间，按范围查询
        if (BeanUtils.isNotEmpty(additionQuery.getDate("queryJoinDateStart")) && BeanUtils
                .isNotEmpty(additionQuery.getDate("queryJoinDateEnd"))) {
            criteria.andBetween("partyJoinDate", additionQuery.getDate("queryJoinDateStart"),
                    additionQuery.getDate("queryJoinDateEnd"));
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<PartyMember> partyMembers = this.selectByExample(example);

        if (BeanUtils.isEmpty(partyMembers)) {
            return tableResult;
        }

        List<PartyMemberVo> voList = queryAssist(partyMembers);

        tableResult.getData().setRows(voList);
        tableResult.getData().setTotal(pageInfo.getTotal());
        return tableResult;
    }

    /**
     * 整合preJonOrg的子部门
     *
     * @param preJoinOrg
     * @return
     */
    private List<PartyOrg> bindSonOrg(Integer preJoinOrg) {
        List<PartyOrg> result = new ArrayList<>();

        List<PartyOrg> allOrg = partyOrgMapper.selectAll();
        if (BeanUtils.isEmpty(allOrg)) {
            return result;
        }

        Map<Integer, PartyOrg> orgIdInstanceMap = allOrg.stream().collect(Collectors.toMap(PartyOrg::getId, org -> org));
        PartyOrg currentOrg = orgIdInstanceMap.get(preJoinOrg);

        bindSonOrgAssist(currentOrg, result, allOrg);

        return result;
    }

    private void bindSonOrgAssist(PartyOrg currentOrg, List<PartyOrg> result, List<PartyOrg> allOrg) {
        result.add(currentOrg);

        allOrg.forEach(org -> {
            if (currentOrg.getId().equals(org.getParentId())) {
                // 说明org是currentOrg的子节点
                bindSonOrgAssist(org, result, allOrg);
            }
        });
    }

    private List<PartyMemberVo> queryAssist(List<PartyMember> partyMembers) {
        // 收集字典CODE
        Set<String> dictCodeSet = new HashSet<>();

        List<PartyMemberVo> voList = new ArrayList<>();
        Set<String> prePartyOrgIds = new HashSet<>();

        partyMembers.forEach(partyMember -> {
            dictCodeSet.add(partyMember.getSex());
            dictCodeSet.add(partyMember.getFtEduId());
            dictCodeSet.add(partyMember.getFtDegreeId());
            dictCodeSet.add(partyMember.getPtEduId());
            dictCodeSet.add(partyMember.getPtDegreeId());

            PartyMemberVo vo = new PartyMemberVo();
            org.springframework.beans.BeanUtils.copyProperties(partyMember, vo);
            voList.add(vo);

            if (BeanUtils.isNotEmpty(partyMember.getPreJoinOrg())) {
                prePartyOrgIds.add(String.valueOf(partyMember.getPreJoinOrg()));
            }
        });

        Map<String, String> byCodeIn = dictFeign.getByCodeIn(StringUtils.join(dictCodeSet, ","));
        Map<Integer, String> collectMap = new HashMap<>();

        if (BeanUtils.isNotEmpty(prePartyOrgIds)) {
            List<PartyOrg> partyOrgs = partyOrgMapper.selectByIds(String.join(",", prePartyOrgIds));
            if (BeanUtils.isNotEmpty(partyOrgs)) {
                collectMap =
                        partyOrgs.stream().collect(Collectors.toMap(PartyOrg::getId, PartyOrg::getOrgFullName));
            }
        }

        if (BeanUtils.isNotEmpty(byCodeIn)) {
            Map<Integer, String> finalCollectMap = collectMap;
            voList.forEach(vo -> {
                vo.setSexName(byCodeIn.get(vo.getSex()));
                vo.setFtEduName(byCodeIn.get(vo.getFtEduId()));
                vo.setFtEduName(byCodeIn.get(vo.getFtDegreeId()));
                vo.setPtEduName(byCodeIn.get(vo.getPtEduId()));
                vo.setPtDegreeName(byCodeIn.get(vo.getPtDegreeId()));
                vo.setPreJoinOrgName(finalCollectMap.get(vo.getPreJoinOrg()));
            });
        }

        return voList;
    }

    /**
     * 获取单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<PartyMemberVo> getPartyMemberById(Integer id) {
        ObjectRestResponse<PartyMemberVo> result = new ObjectRestResponse<>();

        PartyMember partyMember = this.selectById(id);
        if (BeanUtils.isEmpty(partyMember)) {
            result.setData(new PartyMemberVo());
            return result;
        }

        List<PartyMember> list = new ArrayList<>();
        list.add(partyMember);

        List<PartyMemberVo> partyMemberVos = queryAssist(list);
        if (BeanUtils.isNotEmpty(partyMemberVos)) {
            result.setData(partyMemberVos.get(0));
        }

        return result;
    }

    /**
     * 添加单个对象
     *
     * @param partyMember
     * @return
     */
    public ObjectRestResponse<PartyMember> addPartyMember(PartyMember partyMember) {
        ObjectRestResponse<PartyMember> restResponse = new ObjectRestResponse<>();

        check(partyMember, restResponse, false);
        if (restResponse.getStatus() == 400) {
            return restResponse;
        }

        this.insertSelective(partyMember);
        restResponse.setMessage("添加党员成功");
        restResponse.setStatus(200);
        return restResponse;
    }

    private void check(PartyMember partyMember, ObjectRestResponse<PartyMember> restResponse, boolean isUpdate) {

    }

    /**
     * 更新单个对象
     *
     * @param partyMember
     * @return
     */
    public ObjectRestResponse<PartyMember> updatePartyMember(PartyMember partyMember) {
        return null;
    }

    /**
     * 通过所属网格id获取党员人员名称列表
     *
     * @param gridId 所属网格id
     * @return
     */
    public List<PartyMember> getPartyMemberNamesByGridId(String gridId) {
        if (StringUtils.isBlank(gridId)) {
            return new ArrayList<>();
        }
        Example example = new Example(PartyMember.class).selectProperties("name");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        criteria.andEqualTo("gridId", gridId);
        return this.mapper.selectByExample(example);
    }
}