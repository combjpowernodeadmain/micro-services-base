package com.bjzhianjia.scp.cgp.biz;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMapper;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMemberMapper;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * AreaGridMemberBiz 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018-07-04 00:41:37          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author bo
 *
 */
@Service
public class AreaGridMemberBiz extends BusinessBiz<AreaGridMemberMapper, AreaGridMember> {

    @Autowired
    private MergeCore mergeCore;

    @Autowired
    private AreaGridMapper areaGridMapper;

    /**
     * 按条件获取网格成员对象集合
     * 
     * @author 尚
     * @param conditions
     * @return
     */
    public List<AreaGridMember> getByMap(Map<String, Object> conditions) {
        Example example = new Example(AreaGridMember.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");

        Set<String> keySet = conditions.keySet();
        for (String string : keySet) {
            criteria.andEqualTo(string, conditions.get(string));
        }

        List<AreaGridMember> list = this.mapper.selectByExample(example);

        return list;
    }

    /**
     * 
     * @author 尚
     * @param conditionList
     * @return
     */
    public List<AreaGridMember> getAreaGridMember(List<AreaGridMember> conditionList) {
        Example example = new Example(AreaGridMember.class);

        /*
         * 当condigionList为空时，说明前端没有插入网格员，这时进行查询时会按一个新example进行查询，检索出数据库中所有记录,
         * 需进行非空验证
         */
        if (conditionList != null && !conditionList.isEmpty()) {
            for (AreaGridMember areaGridMember : conditionList) {
                Criteria criteria = example.or();
                criteria.andEqualTo("isDeleted", "0");
                criteria.andEqualTo("gridMember", areaGridMember.getGridMember());
                criteria.andEqualTo("gridRole", areaGridMember.getGridRole());
                criteria.andEqualTo("gridId", areaGridMember.getGridId());
            }

            List<AreaGridMember> list = this.mapper.selectByExample(example);
            return list;
        } else {
            return null;
        }
    }

    /**
     * 按gridId删除网格成员记录
     * 
     * @author 尚
     * @param ids
     */
    public void deleteByGridId(List<Integer> delList) {
        Example example = new Example(AreaGridMember.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andIn("gridId", delList);

        this.mapper.deleteByExample(example);
    }

    /**
     * 按gridId删除网格成员记录
     * 
     * @author 尚
     * @param ids
     */
    public void deleteByGridId(Integer id) {
        Example example = new Example(AreaGridMember.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("gridId", id);

        this.mapper.deleteByExample(example);
    }

    /**
     * 分页获取列表
     * 
     * @param areaGridMember
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> getList(AreaGridMember areaGridMember, int page,
        int limit) {
        // 返回信息中有需要进行数据聚和的字段，用JSONObject对象封装
        TableResultResponse<JSONObject> restResult = new TableResultResponse<>();

        Example example = new Example(AreaGridMember.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("idDeleted", "0");
        if (StringUtils.isNotBlank(areaGridMember.getGridMember())) {
            criteria.andEqualTo("gridMember", areaGridMember.getGridMember());
        }

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<AreaGridMember> rows = this.selectByExample(example);

        if (rows == null || rows.isEmpty()) {
            // 如果查询到结果集为空，则直接返回，不执行下面逻辑
            List<JSONObject> jRows = new ArrayList<>();
            restResult.getData().setTotal(0);
            restResult.getData().setRows(jRows);
            return restResult;
        }

        try {
            mergeCore.mergeResult(AreaGridMember.class, rows);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        List<JSONObject> jsonRows = queryAssist(rows);

        restResult.getData().setTotal(pageInfo.getTotal());
        restResult.getData().setRows(jsonRows);
        return restResult;
    }

    private List<JSONObject> queryAssist(List<AreaGridMember> rows) {
        List<JSONObject> jListResult = new ArrayList<>();

        JSONArray jArray = JSONArray.parseArray(JSON.toJSONString(rows));

        // 聚和所属网格
        List<String> gridIdList =
            rows.stream().map(o -> String.valueOf(o.getGridId())).distinct()
                .collect(Collectors.toList());
        Map<Integer, String> areaGrid_ID_NAME_Map = new HashMap<>();
        if (gridIdList != null && !gridIdList.isEmpty()) {
            List<AreaGrid> gridList = this.areaGridMapper.selectByIds(String.join(",", gridIdList));
            areaGrid_ID_NAME_Map =
                gridList.stream().collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridName));
        }

        for (int i = 0; i < jArray.size(); i++) {
            JSONObject jsonObject = jArray.getJSONObject(i);
            jsonObject.put("gridName", areaGrid_ID_NAME_Map.get(jsonObject.getInteger("id")));
            jListResult.add(jsonObject);
        }
        return jListResult;
    }
}