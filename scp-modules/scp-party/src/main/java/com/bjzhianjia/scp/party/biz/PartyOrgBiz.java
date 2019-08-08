package com.bjzhianjia.scp.party.biz;

import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.bjzhianjia.scp.party.feign.DictFeign;
import com.bjzhianjia.scp.party.vo.PartyOrgVo;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.PartyOrg;
import com.bjzhianjia.scp.party.mapper.PartyOrgMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 党组织表
 *
 * @author bo
 * @version 2019-03-27 21:07:16
 * @email 576866311@qq.com
 */
@Service
public class PartyOrgBiz extends BusinessBiz<PartyOrgMapper, PartyOrg> {

    @Autowired
    private DictFeign dictFeign;

    /**
     * 获取所有未删除的党组织
     *
     * @return
     */
    public List<PartyOrg> getValidateList() {
        Example example = new Example(PartyOrg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);

        List<PartyOrg> partyOrgs = this.selectByExample(example);
        if (BeanUtils.isNotEmpty(partyOrgs)) {
            return partyOrgs;
        }
        return new ArrayList<>();
    }

    /**
     * 获取单个对象
     *
     * @param id
     * @return
     */
    public ObjectRestResponse<PartyOrgVo> getPartyOrg(Integer id) {
        PartyOrg partyOrg = this.selectById(id);
        PartyOrgVo vo = new PartyOrgVo();

        org.springframework.beans.BeanUtils.copyProperties(partyOrg, vo);

        if (BeanUtils.isEmpty(partyOrg)) {
            return new ObjectRestResponse<PartyOrgVo>().data(null);
        }
        Map<String, String> byCode = dictFeign.getByCode(partyOrg.getOrgType());
        if (BeanUtils.isNotEmpty(byCode)) {
            String orgType =
                    byCode.get(partyOrg.getOrgType()) == null ? "" : byCode.get(partyOrg.getOrgType());
            vo.setOrgTypeName(orgType);
        }
        if (-1 != partyOrg.getParentId()) {
            PartyOrg parentOrg = this.selectById(partyOrg.getParentId());
            vo.setParentName(parentOrg.getOrgFullName());
        }

        return new ObjectRestResponse<PartyOrgVo>().data(vo);
    }

    /**
     * @param partyOrgIdSet
     */
    public List<PartyOrg> selectbyIds(Set<Integer> partyOrgIdSet) {
        Example example = new Example(PartyOrg.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        criteria.andIn("id", partyOrgIdSet);

        return this.selectByExample(example);
    }

    /**
     * 整合orgId的子党组织
     *
     * @param org
     * @return
     */
    public List<PartyOrg> mergeSunOrg(PartyOrg org) {
        List<PartyOrg> partyOrgs = this.getValidateList();

        if (BeanUtils.isEmpty(org)) {
            return new ArrayList<>();
        }

        List<PartyOrg> result = new ArrayList<>();
        recursive(org, result, partyOrgs);

        return result;
    }

    private void recursive(PartyOrg org, List<PartyOrg> result, List<PartyOrg> partyOrgs) {
        result.add(org);

        partyOrgs.forEach(tmp -> {
            if (tmp.getParentId().equals(org.getId())) {
                // 说明tmp是org的子节点
                recursive(tmp, result, partyOrgs);
            }
        });
    }
}