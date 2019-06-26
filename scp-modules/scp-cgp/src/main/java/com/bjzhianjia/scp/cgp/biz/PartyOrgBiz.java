package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.vo.PartyOrgTree;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.common.util.TreeUtil;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PartyOrg;
import com.bjzhianjia.scp.cgp.mapper.PartyOrgMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 党组织
 *
 * @author chenshuai
 * @version 2019-06-25
 * @email cs4380@163.com
 */
@Service
public class PartyOrgBiz extends BusinessBiz<PartyOrgMapper, PartyOrg> {

    @Autowired
    private DictFeign dictFeign;

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
            // 排除敏感信息
            partyOrg.setIsDeleted(null);
            partyOrg.setDepartId(null);
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
        // 党组织根部门
        String root = "root";
        entity.setId(UUIDUtils.generateUuid());
        // 自动设置排序编码
        PartyOrg parentPartyOrg = this.mapper.selectByPrimaryKey(entity.getParentOrgId());
        if (parentPartyOrg != null) {
            // 判断是否二级部门
            if (root.equals(parentPartyOrg.getOrgCode())) {
                // 二级部门
                String orderSort = this.mapper.selectMaxOrderById(parentPartyOrg.getId());
                entity.setOrderSort(CommonUtil.addOne(orderSort, 100));
            } else {
                String orderSort = this.mapper.selectMaxOrderById(parentPartyOrg.getId());
                entity.setOrderSort(CommonUtil.addOne(orderSort, 1));
            }
        }
        super.insertSelective(entity);
    }

    /**
     * 通过党组织信息查询列表
     * <p>
     * 逻辑删除
     *
     * @param entity 党组织信息
     * @return
     */
    @Override
    public List<PartyOrg> selectList(PartyOrg entity) {
        entity.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        List<PartyOrg> data = mapper.select(entity);
        if (BeanUtils.isNotEmpty(data)) {
            return data;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 通过上级id，获取子集
     *
     * @param parentOrgId 上级id
     * @return
     */
    public List<PartyOrg> getByParentOrgId(String parentOrgId) {
        PartyOrg partyOrg = new PartyOrg();
        partyOrg.setParentOrgId(parentOrgId);
        return this.selectList(partyOrg);
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
        // 判断当前组织是否存在子组织
        List<PartyOrg> partyOrgList = this.getByParentOrgId(String.valueOf(id));
        if (BeanUtils.isNotEmpty(partyOrgList)) {
            throw new RuntimeException("请删除子组织!");
        }
        PartyOrg partyOrg = this.selectById(id);
        partyOrg.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);
        this.updateSelectiveById(partyOrg);
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
     * @param partyOrg 党组织信息
     * @param page     页码
     * @param limit    页容量
     * @return 数据格式：
     * id=主键,orgShortName=党组织简称,orgTypeName=党组织类型
     */
    public TableResultResponse<JSONObject> getPartyOrgList(PartyOrg partyOrg, int page, int limit) {
        // 获取党组织列表
        Example example = new Example(PartyOrg.class)
                .selectProperties("id", "mapInfo", "orgType", "orgShortName");
        Example.Criteria criteria = example.createCriteria();
        // 党组织简称
        if (StringUtils.isNotBlank(partyOrg.getOrgShortName())) {
            criteria.andLike("orgShortName", "%" + partyOrg.getOrgShortName().trim() + "%");
        }
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        example.setOrderByClause("order_sort ASC");
        Page pageHelper = PageHelper.startPage(page, limit);
        List<PartyOrg> partyOrgList = this.mapper.selectByExample(example);

        // 党组织类型
        Map<String, String> dictData = dictFeign.getListByTypeCode(Constances.ROOT_BIZ_PARTY);

        // 封装结果集
        List<JSONObject> resultData = new ArrayList<>();
        if (BeanUtils.isNotEmpty(partyOrgList)) {
            JSONObject data;
            for (PartyOrg obj : partyOrgList) {
                data = new JSONObject();
                data.put("id", obj.getId());
                data.put("orgShortName", obj.getOrgShortName());
                data.put("orgTypeName", dictData.get(obj.getOrgType()));
                resultData.add(data);
            }
            return new TableResultResponse<>(pageHelper.getTotal(), resultData);
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

    /**
     * 通过主键获取党组织详情
     *
     * @param partyOrgId 主键id
     * @return
     */
    public JSONObject getByPartyOrgId(String partyOrgId) {
        if (StringUtils.isBlank(partyOrgId)) {
            return new JSONObject();
        }
        JSONObject data = this.mapper.selectBaseInfoById(partyOrgId);
        if (data != null) {
            // 党组织类型
            Map<String, String> dictData = dictFeign.getListByTypeCode(Constances.ROOT_BIZ_PARTY);
            data.put("orgTypeName", dictData.get(data.getString("orgType")));
            return data;
        } else {
            return new JSONObject();
        }
    }
}