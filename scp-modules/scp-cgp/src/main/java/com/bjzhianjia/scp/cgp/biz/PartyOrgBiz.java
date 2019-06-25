package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.vo.PartyOrgTree;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.common.util.EntityUtils;
import com.bjzhianjia.scp.security.common.util.TreeUtil;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PartyOrg;
import com.bjzhianjia.scp.cgp.mapper.PartyOrgMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 党组织
 *
 * @author chenshuai
 * @version 2019-06-25
 * @email cs4380@163.com
 */
@Service
public class PartyOrgBiz extends BusinessBiz<PartyOrgMapper, PartyOrg> {
    /**
     * 通过党组织id信息，获取党组织实体
     * <p>
     * 排除逻辑删除
     *
     * @param id 党组织id
     * @return
     */
    @Override
    public PartyOrg selectById(Object id) {
        PartyOrg partyOrg = new PartyOrg();
        if (id == null) {
            return partyOrg;
        }
        partyOrg.setId(String.valueOf(id));
        // 排除逻辑删除
        partyOrg.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        partyOrg = mapper.selectOne(partyOrg);
        if (partyOrg != null) {
            return partyOrg;
        } else {
            return new PartyOrg();
        }
    }


    /**
     * 添加党组织
     *
     * @param entity 党组织信息
     */
    @Override
    public void insertSelective(PartyOrg entity) {
        entity.setId(UUIDUtils.generateUuid());
        EntityUtils.setCreatAndUpdatInfo(entity);
        super.insertSelective(entity);
    }

    /**
     * 删除党组织
     * <p>
     * 逻辑删除
     *
     * @param id 党组织id
     */
    @Override
    public void deleteById(Object id) {
        if (id == null) {
            throw new RuntimeException("请选择需要删除的党组织!");
        }
        PartyOrg partyOrg = this.selectById(id);
        partyOrg.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);
        this.updateById(partyOrg);
    }

    /**
     * 获取党组织树
     *
     * @return
     */
    public List<PartyOrgTree> getPartyOrgTree() {
        // 根节点id
        String root = "-1";
        // 获取全部数据
        List<PartyOrg> partyOrgList = this.mapper.selectPartyOrgAll();
        // 缓存树型结构数据
        List<PartyOrgTree> tree = new ArrayList<>();
        // 构造数据
        if (BeanUtils.isNotEmpty(partyOrgList)) {
            for (PartyOrg partyOrg : partyOrgList) {
                if (root.equals(partyOrg.getParentOrgId())) {
                    // 根节点展开
                    tree.add(new PartyOrgTree(partyOrg.getId(), partyOrg.getParentOrgId(), partyOrg.getOrgShortName()));
                } else {
                    // 二级节点不展开
                    tree.add(new PartyOrgTree(partyOrg.getId(), partyOrg.getParentOrgId(), partyOrg.getOrgShortName(), false));
                }
            }
        }
        List<PartyOrgTree> result = TreeUtil.buildByRecursive(tree, root);
        return BeanUtils.isEmpty(result) ? new ArrayList<>() : result;
    }

    /**
     * 获取翻页列表
     *
     * @param partyOrg
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> getPartyOrgList(PartyOrg partyOrg, int page, int limit) {
        Example example = new Example(PartyOrg.class)
                .selectProperties("id", "parentOrgId", "mapInfo", "orgType");
        Example.Criteria criteria = example.createCriteria();
        // 党组织简称
        if (StringUtils.isNotBlank(partyOrg.getOrgShortName())) {
            criteria.andLike("partyOrg", partyOrg.getOrgShortName().trim());
        }
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        example.setOrderByClause("id desc");
        PageHelper.startPage(page, limit);
        List<PartyOrg> partyOrgList = this.mapper.selectByExample(example);
        if (BeanUtils.isNotEmpty(partyOrgList)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        } else {
            return new TableResultResponse<>(0, new ArrayList<>());
        }
    }

    /**
     * 党组织全部定位
     *
     * @return
     */
    public List<PartyOrg> getPartyOrgAllPosition() {
        Example example = new Example(PartyOrg.class).selectProperties("mapInfo");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        // 排除根目录
        criteria.andNotEqualTo("parentOrgId", "-1");
        List<PartyOrg> partyOrgList = this.mapper.selectByExample(example);
        if (BeanUtils.isNotEmpty(partyOrgList)) {
            return partyOrgList;
        } else {
            return new ArrayList<>();
        }
    }
}