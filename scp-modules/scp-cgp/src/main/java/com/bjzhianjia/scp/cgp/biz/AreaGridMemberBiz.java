package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMapper;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMemberMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.merge.core.MergeCore;
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
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private AreaGridMemberBiz areaGridMemberBiz;

    @Autowired
    private AreaGridBiz areaGridBiz;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private PatrolTaskPathBiz patrolTaskPathBiz;

    @Autowired
    private LawEnforcePathBiz lawEnforcePathBiz;

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
    public TableResultResponse<JSONObject> getList(AreaGridMember areaGridMember, int page, int limit) {
        // 返回信息中有需要进行数据聚和的字段，用JSONObject对象封装
        TableResultResponse<JSONObject> restResult = new TableResultResponse<>();

        Example example = new Example(AreaGridMember.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
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
            rows.stream().map(o -> String.valueOf(o.getGridId())).distinct().collect(Collectors.toList());
        Map<Integer, String> areaGrid_ID_NAME_Map = new HashMap<>();
        Map<Integer, Integer> areaGrid_ID_PARENTID_Map = new HashMap<>();
        if (gridIdList != null && !gridIdList.isEmpty()) {
            List<AreaGrid> allGrid = new ArrayList<>();
            List<AreaGrid> gridList = this.areaGridMapper.selectByIds(String.join(",", gridIdList));

            // 查询父级网格
            List<String> gridParentIdList =
                gridList.stream().map(o -> String.valueOf(o.getGridParent())).distinct().collect(Collectors.toList());
            List<AreaGrid> gridParentList = new ArrayList<>();
            if (BeanUtil.isNotEmpty(gridParentIdList)) {
                gridParentList = this.areaGridMapper.selectByIds(String.join(",", gridParentIdList));
            }

            allGrid.addAll(gridList);
            allGrid.addAll(gridParentList);

            // TODO去除allGrid中重复的元素
            Set<Integer> idSet = new HashSet<>();
            for (int i = 0; i < allGrid.size(); i++) {
                if (idSet.contains(allGrid.get(i).getId())) {
                    allGrid.remove(i);
                    i--;
                } else {
                    idSet.add(allGrid.get(i).getId());
                }
            }

            if (BeanUtil.isNotEmpty(allGrid)) {
                areaGrid_ID_NAME_Map =
                    allGrid.stream().collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridName));
                areaGrid_ID_PARENTID_Map =
                    allGrid.stream().collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridParent));
            }
        }

        // 将结果集转化为List<JSONObject>并在其中添加gridName字段
        for (int i = 0; i < jArray.size(); i++) {
            JSONObject jsonObject = jArray.getJSONObject(i);
            jsonObject.put("gridName", areaGrid_ID_NAME_Map.get(jsonObject.getInteger("gridId")));

            jsonObject.put("gridParentName",
                areaGrid_ID_NAME_Map.get(areaGrid_ID_PARENTID_Map.get(jsonObject.getInteger("gridId"))));
            jListResult.add(jsonObject);
        }

        // 收集网格员ID
        List<String> gridMemIds = rows.stream().map(o -> o.getGridMember()).distinct().collect(Collectors.toList());

        // 网格员名称与联系方式
        Map<String, String> userMap = adminFeign.getUser(String.join(",", gridMemIds));
        if (userMap != null && !userMap.isEmpty()) {
            for (JSONObject tmpJObj : jListResult) {
                tmpJObj.put("gridMemberName",
                    CommonUtil.getValueFromJObjStr(userMap.get(tmpJObj.getString("gridMember")), "name"));
                tmpJObj.put("gridMemberPhone",
                    CommonUtil.getValueFromJObjStr(userMap.get(tmpJObj.getString("gridMember")), "mobilePhone"));
                // 是否党员
                tmpJObj.put("isPartyMember",
                        CommonUtil.getValueFromJObjStr(userMap.get(tmpJObj.getString("gridMember")), "attr4"));
            }
        }

        // 聚和网格员定位
        Map<String, JSONObject> memMap = new HashMap<>();
        if (gridMemIds != null && !gridMemIds.isEmpty()) {
            memMap = lawEnforcePathBiz.getByUserIds(String.join(",", gridMemIds));
//            memMap = patrolTaskPathBiz.getByUserIds(String.join(",", gridMemIds));
        }

        if (memMap != null && !memMap.isEmpty()) {
            for (JSONObject tmpJObj : jListResult) {
                tmpJObj.put("mapInfo", memMap.get(tmpJObj.getString("gridMember")));
            }
        }
        return jListResult;
    }

    /**
     * 获取网格员详细信息
     * 
     * @param memId
     * @return
     */
    public ObjectRestResponse<JSONObject> getDetailOfAeraMem(String memId) {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();
        if (StringUtils.isBlank(memId)) {
            restResult.setStatus(400);
            restResult.setMessage("请指定网格员ID");
            return restResult;
        }

        JSONObject jsonObject = new JSONObject();

        // 查询执法人员

        /*
         * userDetail内信息
         * SELECT bu.`name` userName,bu.`id` userId,bu.`sex`,bu.`mobile_phone`
         * mobilePhone,
         * bd.`name` deptName,bd.`id` deptId,
         * bp.`name` positionName,bp.`id` positionId
         */
        JSONArray userDetail = adminFeign.getUserDetail(memId);
        if (userDetail != null && !userDetail.isEmpty()) {
            // 按ID进行查询，如果有返回值 ，则返回值必定唯一
            jsonObject = userDetail.getJSONObject(0);
        }

        /*
         * 人员所属网格及角色===============开始======================
         */
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("gridMember", memId);
        List<AreaGridMember> gridMemList = areaGridMemberBiz.getByMap(conditions);
        List<Integer> gridIdList = new ArrayList<>();
        List<String> roleList = new ArrayList<>();

        if (gridMemList != null && !gridMemList.isEmpty()) {
            gridIdList = gridMemList.stream().map(o -> o.getGridId()).distinct().collect(Collectors.toList());
            roleList = gridMemList.stream().map(o -> o.getGridRole()).distinct().collect(Collectors.toList());
        }

        // 所属网格
        List<String> gridNameList = new ArrayList<>();

        if (gridIdList != null && !gridIdList.isEmpty()) {
            List<AreaGrid> gridList = areaGridBiz.getByIds(gridIdList);
            if (gridList != null && !gridList.isEmpty()) {
                // 查询父级网格名称
                List<Integer> parentIdList =
                    gridList.stream().map(o -> o.getGridParent()).distinct().collect(Collectors.toList());
                List<AreaGrid> parentAreaGridList = new ArrayList<>();
                if (BeanUtil.isNotEmpty(parentIdList)) {
                    parentAreaGridList = areaGridBiz.getByIds(parentIdList);
                }

                Map<Integer, String> parent_ID_NAME_Map = new HashMap<>();
                if (BeanUtil.isNotEmpty(parentAreaGridList)) {
                    parent_ID_NAME_Map =
                        parentAreaGridList.stream().collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridName));
                }

                for (AreaGrid tmp : gridList) {
                    if (tmp.getGridParent() != -1) {
                        gridNameList.add(parent_ID_NAME_Map.get(tmp.getGridParent()) + "(" + tmp.getGridName() + ")");
                    } else {
                        gridNameList.add(tmp.getGridName());
                    }

                }
            }
        }

        // 所属角色
        List<String> roleNameList = new ArrayList<>();
        if (roleList != null && !roleList.isEmpty()) {
            Map<String, String> roleMap = dictFeign.getByCodeIn(String.join(",", roleList));
            roleNameList = new ArrayList<>(roleMap.values());
        }
        /*
         * 人员所属网格及角色===============结束======================
         */

        // 网格人员定位
        JSONObject mapInfoAndTime = lawEnforcePathBiz.getMapInfoByUserId(memId);

        jsonObject.put("gridName", String.join(",", gridNameList));
        jsonObject.put("gridRoleName", String.join(",", roleNameList));
        jsonObject.put("mapInfo", mapInfoAndTime.isEmpty() ? null : mapInfoAndTime);

        restResult.setStatus(200);
        restResult.setData(jsonObject);
        return restResult;
    }

    /**
     * 通过用户id查询网信息
     * 
     * @param userId 用户id
     * @return
     *         网格id、网格名称
     */
    public List<Map<String, Object>> getGridByUserId(String userId) {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        
        AreaGridMember areaGridMember = new AreaGridMember();
        areaGridMember.setIsDeleted("0");
        areaGridMember.setIsDisabled("0");
        areaGridMember.setGridMember(userId);
        List<AreaGridMember> list = this.mapper.select(areaGridMember);
        
        if (BeanUtil.isNotEmpty(list)) {
            Set<Integer> grids = new HashSet<>();
            for(AreaGridMember _areaGridMember : list) {
                grids.add(_areaGridMember.getGridId());
            }
            
            AreaGrid areaGrid = null;
            for(Integer grid: grids) {
                areaGrid = new AreaGrid();
                areaGrid.setId(grid);
                areaGrid = areaGridMapper.selectOne(areaGrid);
                if(areaGrid == null) {
                   continue;
                }
                resultMap = new HashMap<>();
                resultMap.put("gridName",areaGrid.getGridName());
                resultMap.put("gridId", grid);
                result.add(resultMap);
            }
        }else {
            resultMap.put("gridId", -1);
            resultMap.put("gridName", "您当前没有所属网格！");
            result.add(resultMap);
        }
        return result;
    }
    
    /**
     * 通过网格员查询记录
     * 
     * @param userId 用户id
     * @return
     *         网格id、网格名称
     */
    public List<AreaGridMember> getGridByMemId(String memId) {
        AreaGridMember areaGridMember = new AreaGridMember();
        areaGridMember.setIsDeleted("0");
        areaGridMember.setIsDisabled("0");
        areaGridMember.setGridMember(memId);
        List<AreaGridMember> list = this.mapper.select(areaGridMember);
        
        if (BeanUtil.isNotEmpty(list)) {
            return list;
        }else {
            return new ArrayList<>();
        }
    }

    /**
     * 全部定位
     * 
     * @return
     */
    public TableResultResponse<JSONObject> allPosition() {
        List<String> gridMemIdList = this.mapper.distinctGridMember();

        // 聚和网格员定位
        Map<String, JSONObject> memMap = new HashMap<>();
        Map<String, String> userMap = null;
        if (gridMemIdList != null && !gridMemIdList.isEmpty()) {
            memMap = lawEnforcePathBiz.getByUserIds(String.join(",", gridMemIdList));
            userMap = adminFeign.getUser(String.join(",", gridMemIdList));
            if (BeanUtil.isEmpty(userMap)) {
                userMap = new HashMap<>();
            }
        }

        List<JSONObject> jListResult = new ArrayList<>();
        /*
         * gridMember=7ac2dac5133747b2b3c2b2c31ee8fe69
         * mapInfo=(null)
         */
        if (memMap != null && !memMap.isEmpty()) {
            for (String gridMember : gridMemIdList) {
                JSONObject tmpJObj = new JSONObject();
                tmpJObj.put("gridMember", gridMember);
                tmpJObj.put("isPartyMember", CommonUtil.getValueFromJObjStr(userMap.get(gridMember), "attr4"));
                tmpJObj.put("mapInfo", memMap.get(gridMember));
                jListResult.add(tmpJObj);
            }
        }

        return new TableResultResponse<>(jListResult.size(), jListResult);
    }

    /**
     * 按网格ID获取网格员列表
     * 
     * @param gridIds
     * @return
     */
    public List<AreaGridMember> getByGridIds(Set<String> gridIds) {
        Example example =
            new Example(AreaGridMember.class).selectProperties("id", "gridId", "gridMember",
                "gridRole", "isDeleted");
        example.createCriteria().andEqualTo("isDeleted", "0").andIn("gridId", gridIds);

        List<AreaGridMember> areaGridMembers = this.selectByExample(example);

        if (BeanUtil.isNotEmpty(areaGridMembers)) {
            return areaGridMembers;
        }

        return new ArrayList<>();
    }

    /**
     * 查询网格员列表
     * 忽略网格员的角色
     * @param areaGridMember
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> getListExcludeRole(AreaGridMember areaGridMember,
        Integer page, Integer limit) {
        boolean isFastDead = false;

        if (StringUtils.isNotBlank(areaGridMember.getGridMember())) {
            JSONArray usersByName = adminFeign.getUsersByName(areaGridMember.getGridMember());
            if (BeanUtil.isNotEmpty(usersByName)) {
                List<String> userIdList = new ArrayList<>();
                for (int i = 0; i < usersByName.size(); i++) {
                    JSONObject userJObj = usersByName.getJSONObject(i);
                    String userId;
                    if (StringUtils.isNotBlank(userId = userJObj.getString("id"))) {
                        userIdList.add(userId);
                    }
                }
                if (BeanUtil.isNotEmpty(userIdList)) {
                    String join = StringUtils.join(userIdList, ",");
                    areaGridMember.setGridMember("'" + join.replaceAll(",", "','") + "'");
                } else {
                    isFastDead = true;
                }
            } else {
                isFastDead = true;
            }
        }

        if(isFastDead){
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<AreaGridMember> list = this.mapper.getListExcludeRole(areaGridMember);
        if (BeanUtil.isNotEmpty(list)) {
            List<JSONObject> jsonObjects = queryAssist(list);
            return new TableResultResponse<>(pageInfo.getTotal(), jsonObjects);
        }

        return new TableResultResponse<>(0, new ArrayList<>());
    }

    /**
     * 通过网格id、网格角色编码，获取成员列表
     *
     * @param gridId   所属网格id
     * @param gridRole 网格角色编码
     * @param page     页码
     * @param limit    页容量
     * @return
     */
    public TableResultResponse<JSONObject> getByGridIdAndGridRole(String gridId, String gridRole, int page, int limit) {
        TableResultResponse<JSONObject> result = new TableResultResponse<>();
        List<JSONObject> resultData = new ArrayList<>();

        // 获取网格名称
        Map<String, String> grid = areaGridBiz.getParentNameById(gridId);
        String gridName;
        // 判断是否存在父级网格
        if (StringUtils.isNotBlank(grid.get("parentGridName"))) {
            gridName = grid.get("gridName") + "（" + grid.get("parentGridName") + "）";
        } else {
            gridName = grid.get("gridName");
        }
        // 获取网格成员列表
        Example example = new Example(AreaGridMember.class).selectProperties("gridMember");
        Criteria criteria = example.createCriteria();
        // 网格id，网格角色编码
        criteria.andEqualTo("gridId", gridId.trim());
        criteria.andEqualTo("gridRole", gridRole.trim());
        // 未删除，未禁用
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        criteria.andEqualTo("isDisabled", BooleanUtil.BOOLEAN_FALSE);

        Page pageObj = PageHelper.startPage(page, limit);
        List<AreaGridMember> areaGridMemberList = this.mapper.selectByExample(example);

        if (BeanUtils.isEmpty(areaGridMemberList)) {
            result.setStatus(200);
            result.setMessage("未在网格中找到成员列表！");
            return result;
        }
        // 获取用户ids
        StringBuilder userIds = new StringBuilder();
        AreaGridMember areaGridMember = null;
        for (int i = 0, size = areaGridMemberList.size(); i < size; i++) {
            areaGridMember = areaGridMemberList.get(i);
            userIds.append(areaGridMember.getGridMember());
            if (i != size - 1) {
                userIds.append(",");
            }
        }

        JSONArray users = adminFeign.getUserDetail(userIds.toString());
        if (BeanUtils.isEmpty(users)) {
            result.setStatus(200);
            result.setMessage("未找到所属网格成员信息！");
            return result;
        }
        // 获取成员信息
        JSONObject obj = null;
        JSONObject user = null;
        for (int i = 0, size = users.size(); i < size; i++) {
            obj = users.getJSONObject(i);
            user = new JSONObject();
            user.put("gridName", gridName);
            user.put("userName", obj.getString("userName"));
            user.put("mobilePhone", obj.getString("mobilePhone"));
            resultData.add(user);
        }
        return  new TableResultResponse<>(pageObj.getEndRow(), resultData);
    }

    /**
     * 网格员考核统计
     * @return
     */
    public TableResultResponse<JSONObject> memAssessment(String month, Integer gridId, String gridMember,
        String gridRole, Integer page, Integer limit) {
        TableResultResponse<JSONObject> result = new TableResultResponse<>();

        Date monthStart;
        Date monthEnd;

        boolean isFastDead = false;

        Date sourceDate;
        if (StringUtils.isEmpty(month)) {
            // 默认查询当月
            sourceDate = new Date();
        } else {
            sourceDate = DateUtil.dateFromStrToDate(month, "yyyy-MM");
        }

        monthStart = DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(sourceDate));
        monthEnd = DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(DateUtil.theDayOfMonthPlus(sourceDate, 1)));

        // 是否按网格查询
        Set<Integer> gridIdCollect = null;
        if (BeanUtil.isNotEmpty(gridId)) {
            List<AreaGrid> areaGridList = areaGridBiz.getAreaGridBindChileren(gridId);
            gridIdCollect = areaGridList.stream().map(AreaGrid::getId).collect(Collectors.toSet());
            if (BeanUtil.isEmpty(gridIdCollect)) {
                isFastDead = true;
            }
        }

        // 是否按姓名查询
        Set<String> userIdList = null;
        JSONArray usersByName;
        Map<String, String> memIdNameMap = new HashMap<>();
        boolean hasLoadMem = false;
        if (StringUtils.isNotBlank(gridMember)) {
            userIdList = new HashSet<>();
            usersByName = adminFeign.getUsersByName(gridMember);
            hasLoadMem = true;
            if (BeanUtil.isNotEmpty(usersByName)) {
                for (int i = 0; i < usersByName.size(); i++) {
                    JSONObject userJObj = usersByName.getJSONObject(i);
                    userIdList.add(userJObj.getString("id"));
                    memIdNameMap.put(userJObj.getString("id"), userJObj.getString("name"));
                }
                if (BeanUtil.isEmpty(userIdList)) {
                    isFastDead = true;
                }
            } else {
                isFastDead = true;
            }
        }

        if (isFastDead) {
            // 快速死亡
            result.getData().setTotal(0);
            result.getData().setRows(new ArrayList<>());
            return result;
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<JSONObject> jsonObjects =
            this.mapper.memAssessment(monthStart, monthEnd, gridIdCollect, userIdList, gridRole);

        if (BeanUtil.isNotEmpty(jsonObjects)) {
            List<AreaGridMember> allMem = this.getByMap(new HashMap<>());
            if (BeanUtil.isEmpty(allMem)) {
                allMem = new ArrayList<>();
            }
            // 用于统计某一网格员属于几个网格
            Map<String, Set<Integer>> userIdGirdIdListMap = new HashMap<>();
            for (AreaGridMember mem : allMem) {
                Set<Integer> integers = userIdGirdIdListMap.get(mem.getGridMember());
                if (BeanUtil.isEmpty(integers)) {
                    integers = new HashSet<>();
                }
                integers.add(mem.getGridId());
                userIdGirdIdListMap.put(mem.getGridMember(), integers);
            }

            // 收集所以网格基本信息
            List<AreaGrid> allAreaGridWithBasic = this.areaGridBiz.getAllAreaGridWithBasic();
            Map<Integer, String> gridIdNameMap =
                allAreaGridWithBasic.stream().collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridName));

            for (AreaGrid tmp : allAreaGridWithBasic) {
                if (tmp.getGridParent() != -1) {
                    gridIdNameMap
                        .put(tmp.getId(), "(" + gridIdNameMap.get(tmp.getGridParent()) + ")" + tmp.getGridName());
                }
            }

            if (!hasLoadMem) {
                // 如果按网格员名称查询，则在之前就已经从admin服务摘取过人员，无法再进行查询
                List<String> gridMemberCollect =
                    jsonObjects.stream().map(o -> o.getString("gridMember")).distinct().collect(Collectors.toList());
                if (BeanUtil.isNotEmpty(gridMemberCollect)) {
                    Map<String, String> usersByUserIds =
                        adminFeign.getUsersByUserIds(StringUtils.join(gridMemberCollect, ","));
                    if (BeanUtil.isNotEmpty(usersByUserIds)) {
                        for (Map.Entry<String, String> e : usersByUserIds.entrySet()) {
                            JSONObject jsonObject = JSONObject.parseObject(e.getValue());
                            memIdNameMap.put(e.getKey(), jsonObject.getString("name"));
                        }
                    }
                }
            }

            DecimalFormat format = new DecimalFormat("0.00");
            // 整合信息
            for (JSONObject objTmp : jsonObjects) {
                Set<Integer> gridIdSet = userIdGirdIdListMap.get(objTmp.getString("gridMember"));
                if (BeanUtil.isNotEmpty(gridIdSet)) {
                    Set<String> gridNameSet = new HashSet<>();
                    for (Integer gridIdTmp : gridIdSet) {
                        String gridTm;
                        if (BeanUtil.isNotEmpty(gridTm = gridIdNameMap.get(gridIdTmp))) {
                            gridNameSet.add(gridTm);
                        }
                    }
                    objTmp.put("gridName", StringUtils.join(gridNameSet, ",\n"));
                } else {
                    objTmp.put("gridName", "");
                }

                Integer secondDiff = objTmp.getInteger("secondDiff");

                objTmp.put("hourDiff", format.format(secondDiff * 1.0 / 3600));
                objTmp.put("userName", memIdNameMap.get(objTmp.getString("gridMember")));
            }
        }

        result.getData().setTotal(pageInfo.getTotal());
        result.getData().setRows(jsonObjects);
        return result;
    }
}