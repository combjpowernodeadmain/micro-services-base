package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.AreaGridMemberBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMemberMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.AreaGridVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

/**
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class AreaGridService {

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private AreaGridBiz areaGridBiz;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private AreaGridMemberMapper areaGridMemberMapper;

    @Autowired
    private AreaGridMemberBiz areaGridMemberBiz;

    public Result<Void> createAreaGrid(JSONObject areaGridJObject) {
        AreaGrid areaGrid = JSON.parseObject(areaGridJObject.toJSONString(), AreaGrid.class);

        AreaGrid maxGrid = areaGridBiz.getMaxGrid();
        int CurrAreaGridId = -1;
        if (maxGrid == null) {
            CurrAreaGridId = 1;
        } else {
            CurrAreaGridId = maxGrid.getId() + 1;
        }
        areaGrid.setId(CurrAreaGridId);

        // 检验网格
        Result<Void> result = checkAreaGrid(areaGrid);
        if (!result.getIsSuccess()) {
            return result;
        }

        // 插入网格成员
        if (areaGridJObject.getBooleanValue("flag")) {// flag为true，说明使用了带添加网格成员的网页模板，但并不一定添加网格成员
            Result<List<AreaGridMember>> resultM = checkAreaGridMember(areaGridJObject, areaGrid.getId());
            if (!resultM.getIsSuccess()) {
                result.setMessage(resultM.getMessage());
                result.setIsSuccess(false);
                return result;
            }

            List<AreaGridMember> areaGridMemberList = resultM.getData();
            /*
             * 当areaGridMemberList为空时，说明前端没有插入网格员，这时进行查询时会按一个新example进行查询，
             * 检索出数据库中所有记录,
             * 需进行非空验证
             */
            if (areaGridMemberList != null && !areaGridMemberList.isEmpty()) {
                areaGridMemberMapper.insertAreaGridMemberList(areaGridMemberList);
            }
        }

        areaGridBiz.insertSelective(areaGrid);
        return result;
    }

    public Result<Void> checkAreaGrid(AreaGrid areaGrid) {
        Result<Void> result = new Result<>();
        result.setIsSuccess(false);

        // 验证所选管理部门是否存在
        if (StringUtils.isNotBlank(areaGrid.getMgrDept())) {
            Map<String, String> depart = adminFeign.getDepart(areaGrid.getMgrDept());
            if (depart == null || depart.isEmpty()) {
                result.setMessage("所选部门不存在");
                return result;
            }
        }

        // 验证当前网格编码的唯一性
        Map<String, String> conditions = new HashMap<>();
        if (StringUtils.isNotBlank(areaGrid.getGridCode())) {
            conditions.put("gridCode", areaGrid.getGridCode());
            List<AreaGrid> areaGridList = areaGridBiz.getByMap(conditions);
            if (areaGridList != null) {
                if (areaGridList.size() > 0) {
                    result.setMessage("网格编号已存在");
                    return result;
                }
            }
        }

        // 验证当前网格名称的唯一性,2018-09-13去除该逻辑
        // conditions.clear();
        // if (StringUtils.isNotBlank(areaGrid.getGridName())) {
        // conditions.put("gridName", areaGrid.getGridName());
        // List<AreaGrid> areaGridList = areaGridBiz.getByMap(conditions);
        // if (areaGridList != null) {
        // if (areaGridList.size() > 0) {
        // result.setMessage("网格名称已存在");
        // return result;
        // }
        // }
        // }

        if (areaGrid.getGridParent() == null) {
            areaGrid.setGridParent(-1);
        }

        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
    }

    public Result<List<AreaGridMember>> checkAreaGridMember(JSONObject areaGridJObject, int gridId) {
        Result<List<AreaGridMember>> result = new Result<>();
        result.setIsSuccess(false);

        /*
         * areaGridMember:[{'gridMember':'m11,m12','gridRole':'r1'},{'gridMember
         * ':'m2',
         * 'gridRole':'r2'},{'gridMember':'m3','gridRole':'r3'},{'
         * gridMember':'m4','gridRole':'r4'},{'gridMember':'m5','
         * gridRole':'r5'}]
         */
        String areaGridMemberJArrayStr = areaGridJObject.getString("areaGridMember");
        JSONArray areaGridMemberJArray = JSONArray.parseArray(areaGridMemberJArrayStr);
        List<AreaGridMember> areaGridMemberList = new ArrayList<>();

        for (int i = 0; i < areaGridMemberJArray.size(); i++) {
            JSONObject areaGridMemJObject = areaGridMemberJArray.getJSONObject(i);

            AreaGridMember areaGridMember = new AreaGridMember();
            areaGridMember.setGridId(gridId);
            areaGridMember.setGridRole(areaGridMemJObject.getString("gridRole"));

            areaGridMember.setCrtUserName(BaseContextHandler.getUsername());
            areaGridMember.setCrtUserId(BaseContextHandler.getUserID());
            areaGridMember.setCrtTime(new Date());

            areaGridMember.setTenantId(BaseContextHandler.getTenantID());

            String areaGridMemStrs = areaGridMemJObject.getString("gridMember");
            /*
             * gridMember中有可能包含多个人，如果是多个人，则每个人需要对应一条记录，
             * 所以将areaGridMember对象按gridMember中人数转化为areaGridMember集合。
             * gridMember中也可能没有人，这时不需要进行插入记录，areaGridMemberList
             * 空集合即可
             */
            if (StringUtils.isNotBlank(areaGridMemStrs)) {
                String[] split = areaGridMemStrs.split(",");
                for (String string : split) {
                    AreaGridMember tmpMem = BeanUtil.copyBean_New(areaGridMember, new AreaGridMember());
                    tmpMem.setGridMember(string);
                    areaGridMemberList.add(tmpMem);
                }
            }
        }

        // 验证：同一网格-同一人-同一岗位
        List<AreaGridMember> areaGridMemberInDB = areaGridMemberBiz.getAreaGridMember(areaGridMemberList);
        if (!(areaGridMemberInDB == null || areaGridMemberInDB.isEmpty())) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("网格内人员重复，请核实");
            // buffer.append("【").append(areaGridMemberInDB.get(0).getGridId()).append("-")
            // .append(areaGridMemberInDB.get(0).getGridRole()).append("-")
            // .append(areaGridMemberInDB.get(0).getGridMember()).append("】已存在");
            result.setMessage(buffer.toString());
            return result;
        }

        result.setData(areaGridMemberList);
        result.setIsSuccess(true);
        result.setMessage("成功");

        return result;
    }

    /**
     * 
     * 更新单个对象
     * 
     * @author 尚
     * @param areaGridJObject
     * @return
     */
    public Result<Void> updateAreaGrid(JSONObject areaGridJObject) {
        AreaGrid areaGrid = JSONObject.parseObject(areaGridJObject.toJSONString(), AreaGrid.class);

        Result<Void> result = new Result<>();
        result.setIsSuccess(false);

        if (areaGridJObject.getBooleanValue("flag")
            || StringUtils.isNotBlank(areaGridJObject.getString("areaGridMember"))) {
            // 验证所选网格管理人员是否存在

            /*
             * areaGridMember:[{'gridMember':'m11,m12','gridRole':'r1'},{'
             * gridMember':'m2',
             * 'gridRole':'r2'},{'gridMember':'m3','gridRole':'r3'},{'
             * gridMember':'m4','gridRole':'r4'},{'gridMember':'m5','
             * gridRole':'r5'}]
             */
            String areaGridMemberJArrayStr = areaGridJObject.getString("areaGridMember");
            JSONArray areaGridMemberJArray = JSONArray.parseArray(areaGridMemberJArrayStr);
            List<String> areaGridMemberIdList = new ArrayList<>();

            // 收集前端传入的用户ID集合
            for (int i = 0; i < areaGridMemberJArray.size(); i++) {
                JSONObject jsonObject = areaGridMemberJArray.getJSONObject(i);
                String[] split = jsonObject.getString("gridMember").split(",");
                for (String string : split) {
                    if (StringUtils.isNotBlank(string)) {
                        areaGridMemberIdList.add(string);
                    }
                }
            }

            Map<String, String> userMap = adminFeign.getUser(String.join(",", areaGridMemberIdList));
            for (String memberId : areaGridMemberIdList) {
                if (!userMap.containsKey(memberId)) {
                    // 如果从数据库中查询到的人物集合不包括前端传入的某个ID，说明该风格员不存在
                    result.setIsSuccess(false);
                    result.setMessage("网格管理人员不存在");
                    return result;
                }
            }

        } else {
            // 验证所选管理部门是否存在
            if (StringUtils.isNotBlank(areaGrid.getMgrDept())) {
                Map<String, String> depart = adminFeign.getDepart(areaGrid.getMgrDept());
                if (depart == null || depart.isEmpty()) {
                    result.setMessage("所选部门不存在");
                    return result;
                }
            }
        }

        // 验证当前网格编码的唯一性
        Map<String, String> conditions = new HashMap<>();
        if (StringUtils.isNotBlank(areaGrid.getGridCode())) {
            conditions.put("gridCode", areaGrid.getGridCode());
            List<AreaGrid> areaGridList = areaGridBiz.getByMap(conditions);
            if (areaGridList != null) {
                boolean flag = false;
                for (AreaGrid tmp : areaGridList) {
                    if (!tmp.getId().equals(areaGrid.getId())) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    result.setMessage("网格编号已存在");
                    return result;
                }
            }
        }

        // 验证当前网格名称的唯一性，2018-09-13去除该逻辑
        // conditions.clear();
        // if (StringUtils.isNotBlank(areaGrid.getGridName())) {
        // conditions.put("gridName", areaGrid.getGridName());
        // List<AreaGrid> areaGridList = areaGridBiz.getByMap(conditions);
        // if (areaGridList != null) {
        // boolean flag = false;
        // for (AreaGrid tmp : areaGridList) {
        // if (!tmp.getId().equals(areaGrid.getId())) {
        // flag = true;
        // break;
        // }
        // }
        // if (flag) {
        // result.setMessage("网格名称已存在");
        // return result;
        // }
        // }
        // }

        if (areaGridJObject.getBooleanValue("flag")
            || StringUtils.isNotBlank(areaGridJObject.getString("areaGridMember"))) {
            areaGridMemberBiz.deleteByGridId(areaGrid.getId());
            Result<List<AreaGridMember>> resultM = checkAreaGridMember(areaGridJObject, areaGrid.getId());
            if (!resultM.getIsSuccess()) {
                result.setMessage(resultM.getMessage());
                return result;
            }
            List<AreaGridMember> areaGridMemberList = resultM.getData();

            if (areaGridMemberList != null && !areaGridMemberList.isEmpty()) {
                areaGridMemberMapper.insertAreaGridMemberList(areaGridMemberList);
            }

        }

        areaGridBiz.updateSelectiveById(areaGrid);

        result.setIsSuccess(true);
        result.setMessage("成功");

        return result;
    }

    /**
     * 分页按条件获取网格数据
     * 
     * @author 尚
     * @param page
     * @param limit
     * @param areaGrid
     * @param gridParentName
     * @return
     */
    public TableResultResponse<AreaGridVo> getList(int page, int limit, AreaGrid areaGrid, String gridParentName) {
        TableResultResponse<AreaGridVo> tableResult = areaGridBiz.getList(page, limit, areaGrid,gridParentName);

        List<AreaGridVo> rows = tableResult.getData().getRows();

        if (BeanUtil.isEmpty(rows)) {
            return tableResult;
        }

        _queryAssist(rows);

        return tableResult;
    }

    private void _queryAssist(List<AreaGridVo> rows) {
        // 如果网格有上级网格，则进行聚和
        List<Integer> gridParentIdList =
            rows.stream().map((o) -> o.getGridParent()).distinct().collect(Collectors.toList());
        if (gridParentIdList != null && !gridParentIdList.isEmpty()) {
            // 查询上一级网格集合
            List<AreaGrid> areaGridParentList = areaGridBiz.getByIds(gridParentIdList);
            Map<Integer, String> parent_ID_NAME_Map = new HashMap<>();
            if (BeanUtil.isNotEmpty(areaGridParentList)) {
                parent_ID_NAME_Map =
                    areaGridParentList.stream().collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridName));
            }
            for (AreaGridVo tmp1 : rows) {
                String parentName = parent_ID_NAME_Map.get(tmp1.getGridParent());
                if (StringUtils.isNotBlank(parentName)) {
                    tmp1.setGridParentName(parentName);
                }
            }
        }

        // 聚积部门名称
        List<String> mrgDeptIdList = rows.stream().map((o) -> o.getMgrDept()).distinct().collect(Collectors.toList());
        if (mrgDeptIdList != null && !mrgDeptIdList.isEmpty()) {
            Map<String, String> departMap = adminFeign.getDepart(String.join(",", mrgDeptIdList));
            if (departMap != null && !departMap.isEmpty()) {
                for (AreaGridVo vo : rows) {
                    String string = departMap.get(vo.getMgrDept());
                    if (StringUtils.isNotBlank(string)) {
                        JSONObject jsonObject = JSONObject.parseObject(string);
                        vo.setMgrDept(jsonObject.getString("name"));
                    }
                }
            }
        }
    }

    /**
     * 按网格等级获取网格列表
     * 
     * @author 尚
     * @param gridLevel
     * @return
     */
    public List<AreaGrid> getByGridLevel(String gridLevel) {

        List<AreaGrid> tableResult = areaGridBiz.getByGridLevel(gridLevel);

        return tableResult;
    }

    /**
     * 按ID获取单个对象
     * 
     * @author 尚
     * @param id
     * @return
     */
    public ObjectRestResponse<JSONObject> getOne(Integer id) {
        ObjectRestResponse<JSONObject> result = new ObjectRestResponse<>();
        AreaGrid areaGrid = areaGridBiz.selectById(id);

        // 根据不同情况，向前端返回不同提交
        if ("1".equals(areaGrid.getIsDeleted())) {
            result.setStatus(400);
            result.setMessage("网格" + areaGrid.getGridName() + "已删除");
            return result;
        }
        if (areaGrid == null) {
            result.setStatus(400);
            result.setMessage("该网格不存在或已删除");
            return result;
        }

        AreaGridVo areaGridVo = BeanUtil.copyBean_New(areaGrid, new AreaGridVo());
        List<AreaGridVo> rows = new ArrayList<>();
        rows.add(areaGridVo);
        _queryAssist(rows);

        // 查询网格是否有成员
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("gridId", areaGrid.getId());
        List<AreaGridMember> areaGridMemberList = areaGridMemberBiz.getByMap(conditions);

        // 处理回传信息中"gridMember"字段除非依然信息
        String gridMember = mergeGridMember(areaGridMemberList);

        JSONObject resultJObj = JSONObject.parseObject(JSON.toJSONString(areaGridVo));
        resultJObj.put("gridMember", gridMember);

        result.setData(resultJObj);
        return result;
    }

    public String mergeGridMember(List<AreaGridMember> areaGridMemberList) {
        String gridMember = "";
        if (areaGridMemberList != null && !areaGridMemberList.isEmpty()) {
            // 按岗位收集网格员，形式如--------"岗位":"网格员1，网格员2，网格员3"
            // positionUserMap内的List集合用于存在同一角色下的多个网格员的ID
            Map<String, List<String>> positionUserMap = new HashMap<>();
            for (AreaGridMember areaGridMember : areaGridMemberList) {
                List<String> pUList = positionUserMap.get(areaGridMember.getGridRole());
                if (pUList == null || pUList.isEmpty()) {
                    pUList = new ArrayList<>();
                    pUList.add(areaGridMember.getGridMember());
                    positionUserMap.put(areaGridMember.getGridRole(), pUList);
                } else {
                    pUList.add(areaGridMember.getGridMember());
                }
            }

            /*
             * 说明该网格下有网格成员 回传前台数据：[{\
             * "gridMember\":\"网格员ID1，网格员ID2\",\"gridRole\":\"岗位\",\"gridRoleName\":\"网格员\",\"gridMemberName\":\"网格员名1,网格员名2\"
             * }]
             */
            List<String> collect =
                areaGridMemberList.stream().map((o) -> o.getGridMember()).distinct().collect(Collectors.toList());
            Map<String, String> userMap = adminFeign.getUser(String.join(",", collect));

            // 字典在业务库里存在形式(ID-->code)，代码需要进行相应修改
            Map<String, String> gridMemberMap = dictFeign.getByCode(Constances.ROOT_BIZ_GRID_ROLE);

            JSONArray memberJArray = new JSONArray();
            Set<String> keySet = positionUserMap.keySet();
            for (String gridRole : keySet) {
                // 以gridRold为基准，拼接需要回传的JSON数组
                JSONObject memberJObject = new JSONObject();
                memberJObject.put("gridRole", gridRole);
                memberJObject.put("gridRoleName", gridMemberMap.get(gridRole));

                // 同一岗位(角色)下的多个人员
                List<String> list = positionUserMap.get(gridRole);

                List<String> memberTmpList = new ArrayList<>();
                List<String> memberNameTmpList = new ArrayList<>();
                List<String> isPartyMemberList = new ArrayList<>();
                for (String gridMeber : list) {
                    memberTmpList.add(gridMeber);
                    String userJStr = userMap.get(gridMeber);
                    if (StringUtils.isNotBlank(userJStr)) {
                        /*
                         * 可能因为在网格成员表里引入了某个人员，但该人员在base_user表里进行了删除，所以admimFeign
                         * .getUser方法
                         * 返回的map集合中没有该人员，造成空指针
                         */
                        JSONObject userJObj = JSONObject.parseObject(userJStr);
                        memberNameTmpList.add(userJObj.getString("name"));
                        // 是否党员
                        isPartyMemberList.add(userJObj.getString("attr4"));
                    }
                }
                memberJObject.put("gridMember", String.join(",", memberTmpList));
                memberJObject.put("gridMemberName", String.join(",", memberNameTmpList));
                memberJObject.put("isPartyMember", String.join(",", isPartyMemberList));
                memberJArray.add(memberJObject);
            }
            gridMember = memberJArray.toJSONString();
        }
        return gridMember;
    }

    /**
     * 删除网格<br/>
     * 删除网格的同时，会将网格成员表中与之相对应的记录删除<br/>
     * 删除采用逻辑删除
     * 
     * @author 尚
     * @param id
     * @return
     */
    public Result<Void> removeOne(Integer id) {
        Result<Void> result = new Result<>();

        AreaGrid areaGrid = new AreaGrid();
        areaGrid.setId(id);
        areaGrid.setIsDeleted("1");
        this.areaGridBiz.updateSelectiveById(areaGrid);

        this.areaGridMemberMapper.deleteByGridId(id, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(),
            new Date());

        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
    }
}
