package com.bjzhianjia.scp.party.biz;

import com.bjzhianjia.scp.party.entity.ExportLog;
import com.bjzhianjia.scp.party.entity.PartyMember;
import com.bjzhianjia.scp.party.feign.DictFeign;
import com.bjzhianjia.scp.party.mapper.PartyMemberMapper;
import com.bjzhianjia.scp.party.vo.ExportLogVo;
import com.bjzhianjia.scp.party.vo.PartyMemberFamilyVo;
import com.bjzhianjia.scp.party.vo.PartyMemberVo;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.PartyMemberFamily;
import com.bjzhianjia.scp.party.mapper.PartyMemberFamilyMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 党员家庭成员表
 *
 * @author bo
 * @version 2019-03-27 21:07:17
 * @email 576866311@qq.com
 */
@Service
public class PartyMemberFamilyBiz extends BusinessBiz<PartyMemberFamilyMapper, PartyMemberFamily> {

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private PartyMemberMapper partyMemberMapper;

    /**
     * @param params
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<PartyMemberFamilyVo> getPartyMemberFamilyList(PartyMemberFamily params, Integer page, Integer limit) {
        TableResultResponse<PartyMemberFamilyVo> tableResult = new TableResultResponse<>();

        Example example = new Example(PartyMemberFamily.class);

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);

        if (StringUtils.isNotBlank(params.getName())) {
            criteria.andLike("name", "%" + params.getName() + "%");
        }

        if (StringUtils.isNotBlank(params.getPolitical())) {
            criteria.andEqualTo("political", params.getPolitical());
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<PartyMemberFamily> partyMembers = this.selectByExample(example);

        if (BeanUtils.isEmpty(partyMembers)) {
            return tableResult;
        }

        List<PartyMemberFamilyVo> voList = queryAssist(partyMembers);

        tableResult.getData().setRows(voList);
        tableResult.getData().setTotal(pageInfo.getTotal());
        return tableResult;
    }

    private List<PartyMemberFamilyVo> queryAssist(List<PartyMemberFamily> partyMemberFamilys) {
        // 收集字典CODE
        Set<String> dictCodeSet = new HashSet<>();

        List<PartyMemberFamilyVo> voList = new ArrayList<>();
        Set<Integer> partyMemIds = new HashSet<>();

        partyMemberFamilys.forEach(partyMemberFimily -> {
            dictCodeSet.add(partyMemberFimily.getPolitical());

            partyMemIds.add(partyMemberFimily.getPartyMemId());

            PartyMemberFamilyVo vo = new PartyMemberFamilyVo();
            org.springframework.beans.BeanUtils.copyProperties(partyMemberFimily, vo);
            voList.add(vo);
        });

        Map<String, String> byCodeIn = null;
        if (BeanUtils.isNotEmpty(dictCodeSet)) {
            byCodeIn = dictFeign.getByCodeIn(StringUtils.join(dictCodeSet, ","));
        }

        Map<Integer, String> partyMemIdNameMap = null;
        if (BeanUtils.isNotEmpty(partyMemIds)) {
            List<PartyMember> partyMembers = partyMemberMapper.selectByIds(StringUtils.join(partyMemIds, ","));
            if (BeanUtils.isNotEmpty(partyMembers)) {
                partyMemIdNameMap = partyMembers.stream().collect(Collectors.toMap(PartyMember::getId, PartyMember::getName));
            }
        }

        if (BeanUtils.isNotEmpty(byCodeIn)) {
            voList.forEach(vo -> {
                vo.setPoliticalName(vo.getPolitical());
            });
        }

        if (BeanUtils.isNotEmpty(partyMemIdNameMap)) {
            Map<Integer, String> finalPartyMemIdNameMap = partyMemIdNameMap;
            voList.forEach(vo -> vo.setPartyMemName(finalPartyMemIdNameMap.get(vo.getPartyMemId())));
        }

        return voList;
    }
}