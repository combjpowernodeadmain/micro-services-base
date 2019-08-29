package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.config.PropertiesConfig;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.PatrolRecord;
import com.bjzhianjia.scp.cgp.entity.Point;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMapper;
import com.bjzhianjia.scp.cgp.util.*;
import com.bjzhianjia.scp.cgp.vo.AreaGridTree;
import com.bjzhianjia.scp.cgp.vo.AreaGridVo;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.TreeUtil;
import com.bjzhianjia.scp.security.wf.base.utils.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.*;
import java.util.stream.Collectors;


/**
 * AreaGridBiz 网格.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年7月4日          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author bo
 *
 */
@Service
@Slf4j
public class AreaGridBiz extends BusinessBiz<AreaGridMapper, AreaGrid> {

    @Autowired
    private MergeCore mergeCore;
    
    @Autowired
    private PropertiesConfig propertiesConfig;
    @Autowired
    private Environment environment;
    
    @Autowired
    private PropertiesProxy propertiesProxy;

    @Autowired
    private AdminFeign adminFeign;
    @Autowired
    private  PatrolRecordBiz patrolRecordBiz;

    /**
     * 按条件查询未被删除的网格
     * 
     * @author 尚
     * @param conditions
     * @return
     */
    public List<AreaGrid> getByMap(Map<String, String> conditions) {
        Example example = new Example(AreaGrid.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");

        Set<String> keySet = conditions.keySet();
        for (String string : keySet) {
            criteria.andEqualTo(string, conditions.get(string));
        }

        List<AreaGrid> gridAreaList = mapper.selectByExample(example);

        return gridAreaList;
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
        // 去除列表查询中的mapinfo
        Example example = new Example(AreaGrid.class).selectProperties(
            "id",
            "gridCode",
            "gridSort",
            "gridName",
            "gridLevel",
            "gridParent",
            "gridTeam",
            "gridNumbers",
            "gridHousehold",
            "gridPersons",
            "gridAreas",
            "gridRange",
            "mgrDept",
            "isDeleted",
            "isDisabled",
            "mapInfo"
        );
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        example.setOrderByClause("grid_sort asc");

        if (StringUtils.isNotBlank(areaGrid.getGridName())) {
            criteria.andLike("gridName", "%" + areaGrid.getGridName() + "%");
        }
        if (StringUtils.isNotBlank(areaGrid.getGridLevel())) {
            criteria.andEqualTo("gridLevel", areaGrid.getGridLevel());
        }
        if(BeanUtil.isNotEmpty(areaGrid.getGridParent())){
            criteria.andEqualTo("gridParent", areaGrid.getGridParent());
        }
        if(StringUtils.isNotBlank(gridParentName)){
            // 模糊查询gridParentName对应的网格
            List<AreaGrid> areaGrids = this.mapper.selectIdsByGridParentName(gridParentName);
            List<Integer> collect = new ArrayList<>();
            if (BeanUtil.isNotEmpty(areaGrids)) {
                collect =
                    areaGrids.stream().map(o -> o.getId()).distinct().collect(Collectors.toList());
                criteria.andIn("id", collect);
            } else {
                collect.add(-1);
            }
            criteria.andIn("id", collect);
        }

        // 网格按正序排列，20180914
        // example.setOrderByClause("crt_time desc");

        Page<Object> result = PageHelper.startPage(page, limit);
        List<AreaGrid> list = this.mapper.selectByExample(example);

        try {
            mergeCore.mergeResult(AreaGrid.class, list);
        } catch (Exception ex) {
            log.error("merge data exception", ex);
        }

        List<AreaGridVo> voList = new ArrayList<>();
        for (AreaGrid tmp : list) {
            AreaGridVo vo = BeanUtil.copyBean_New(tmp, new AreaGridVo());
            voList.add(vo);
        }
        return new TableResultResponse<>(result.getTotal(), voList);
    }

    /**
     * 按网格等级获取网格列表
     * 
     * @author 尚
     * @param gridLevel
     * @return
     */
    public List<AreaGrid> getByGridLevel(String gridLevel) {
        Example example = new Example(AreaGrid.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");

        String[] split = gridLevel.split(",");
        List<String> gridLevelList = Arrays.asList(split);
        criteria.andIn("gridLevel", gridLevelList);

        example.setOrderByClause("id desc");

        List<AreaGrid> list = this.mapper.selectByExample(example);
        return list;
    }

    /**
     * 按ID集合查询记录
     * 
     * @author 尚
     * @param ids
     * @return
     */
    public List<AreaGrid> getByIds(List<Integer> ids) {
        Example example = new Example(AreaGrid.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        criteria.andIn("id", ids);
        List<AreaGrid> result = this.mapper.selectByExample(example);

        return result;
    }

    /**
     * 获取ID最大的记录
     * 
     * @author 尚
     * @return
     */
    public AreaGrid getMaxGrid() {
        Example example = new Example(AreaGrid.class);
        example.setOrderByClause("id desc");
        PageHelper.startPage(1, 1);
        List<AreaGrid> areaGrid = this.mapper.selectByExample(example);
        if (areaGrid != null && !areaGrid.isEmpty()) {
            return areaGrid.get(0);
        }
        return null;
    }
    
    /**
     *  通过经纬度获取指定网格信息
     * @param point
     * @return
     */
    public AreaGrid isPolygonContainsPoint(Point point) {
        AreaGrid result = null;
        
        List<AreaGrid> areaGridList = this.mapper.selectAll();
        AreaGrid areaGrid = null;
        if(BeanUtil.isNotEmpty(areaGridList)) {
            for (int i = 0; i < areaGridList.size(); i++) {
                areaGrid = areaGridList.get(i);
                if(StringUtils.isBlank(areaGrid.getMapInfo())) {
                    continue;
                }
                //解析mapInfo数据
                String[] mapInfos = areaGrid.getMapInfo().split("-");
                for (String mapInfo : mapInfos) {
                    JSONArray array = null;
                    try {
                        // 当网格没有配坐标时，解析JSONArray将会发生错误
                        array = JSONArray.parseArray(mapInfo);
                    } catch (Exception e) {
                        array = new JSONArray();
                    }
                    List<Point> listPoint = array.toJavaList(Point.class);
                    if (listPoint == null) {
                        continue;
                    }
                    if (SpatialRelationUtil.isPolygonContainsPoint(listPoint, point)) {
                        result = areaGrid;
                        break;
                    }
                }
            }
        }
        return result;
    }
    
    /**
     *  通过经纬度获取指定网格信息
     * @param point
     * @return
     */
    public AreaGrid isLowestGridContainsPoint(Point point) {
        AreaGrid result = null;
        
        // 获取最低级网格方法修改--20181009
        List<AreaGrid> areaGridList = this.selectLowestAreaGrid();
        if(BeanUtil.isEmpty(areaGridList)) {
            return null;
        }
        
        AreaGrid areaGrid = null;
        if(BeanUtil.isNotEmpty(areaGridList)) {
            for (int i = 0; i < areaGridList.size(); i++) {
                areaGrid = areaGridList.get(i);
                if(StringUtils.isBlank(areaGrid.getMapInfo())) {
                    continue;
                }
                //解析mapInfo数据格式
                String[] mapInfos = areaGrid.getMapInfo().split("-");
                for (String mapInfo : mapInfos) {
                    JSONArray array = JSONArray.parseArray(mapInfo);
                    List<Point> listPoint = array.toJavaList(Point.class);
                    if(listPoint == null) {
                        continue;
                    }
                    if(SpatialRelationUtil.isPolygonContainsPoint(listPoint, point)) {
                        result = areaGrid;
                        break;
                    }
                    if(SpatialRelationUtil.isPointInPolygonBoundary(listPoint, point)) {
                        result = areaGrid;
                        break;
                    }
                }
            }
        }

        // 合并该网格的父级网格
        if (BeanUtil.isNotEmpty(result)) {
            AreaGrid parentAreaGrid = getParentAreaGrid(result);
            if (BeanUtil.isNotEmpty(parentAreaGrid)
                && StringUtils.isNotBlank(parentAreaGrid.getGridName())) {
                result.setGridName(result.getGridName() + "(" + parentAreaGrid.getGridName() + ")");
            }
        }
        return result;
    }

    /**
     * 获取最低级网格
     * @return
     */
    private List<AreaGrid> selectLowestAreaGrid() {
        Example example=new Example(AreaGrid.class);
        Criteria criteria = example.createCriteria();
        
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("gridLevel", propertiesConfig.getLowestGridLevel());
        
        List<AreaGrid> areaGridList = this.selectByExample(example);
        if(BeanUtil.isNotEmpty(areaGridList)) {
            return areaGridList;
        }
        return new ArrayList<>();
    }

    /**
     * 根据网格等级获取网格信息(mapInfo/id/gridName)
     * @param gridLevelKey
     * @return
     */
    public TableResultResponse<JSONObject> getByAreaGrid(String gridLevelKey){
        return _gridLevelAssist(gridLevelKey,true);
    }

    /**
     * 根据网格等级获取网格信息(mapInfo/id/gridName)
     * @param gridLevelKey
     * @return
     */
    public TableResultResponse<JSONObject> getByAreaGridP(String gridLevelKey){
        TableResultResponse<JSONObject> fakeResult = _gridLevelAssist(gridLevelKey, false);
        return fakeResult;
    }

    /**
     * 根据网格等级获取网格信息
     * @param gridLevelKey 网格等级，有可能是前端与后端约定的配置文件里的key
     * @param isLoadPropertyFromProfile 是否从配置文件获取属性信息
     * @return
     */
    private TableResultResponse<JSONObject> _gridLevelAssist(String gridLevelKey,boolean isLoadPropertyFromProfile) {
        TableResultResponse<JSONObject> tableResultResponse=new TableResultResponse<>();

        String property;
        if(isLoadPropertyFromProfile){
            property = environment.getProperty(gridLevelKey);
        }else{
            property=gridLevelKey;
        }
        if(StringUtils.isBlank(property)) {
            tableResultResponse.setStatus(400);
            tableResultResponse.setMessage(gridLevelKey+"不存在");
            return tableResultResponse;
        }

        Set<String> properties=new HashSet<>();
        for(String propertyKey:property.split(",")){
            properties.add(propertyKey);
        }

        Example example=new Example(AreaGrid.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andIn("gridLevel", properties);

        List<AreaGrid> areaGridList = this.selectByExample(example);

        List<JSONObject> resultJObj=new ArrayList<>();
        if (BeanUtil.isNotEmpty(areaGridList)) {
            List<Integer> parentIds =
                areaGridList.stream().map(AreaGrid::getGridParent).distinct().collect(Collectors.toList());
            Map<Integer, String> parentIdNameMap = new HashMap<>();
            if (BeanUtils.isNotEmpty(parentIds)) {
                List<AreaGrid> parentList = this.getByIds(parentIds);
                if (BeanUtils.isNotEmpty(parentList)) {
                    parentIdNameMap =
                        parentList.stream().collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridName));
                }
            }

            for (AreaGrid areaGrid : areaGridList) {
                JSONObject propertiesJObj;
                try {
                    // 在返回结果集中添加网格等级
                    propertiesJObj =
                        propertiesProxy.swapProperties(areaGrid, "gridName", "id", "mapInfo",
                            "gridLevel");
                    propertiesJObj.put("parentGridName", parentIdNameMap.get(areaGrid.getGridParent()));
                    resultJObj.add(propertiesJObj);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            tableResultResponse.getData().setRows(resultJObj);
            return tableResultResponse;
        }

        tableResultResponse.setStatus(400);
        tableResultResponse.setMessage("未找到相应数据");
        return tableResultResponse;
    }

    /**
     * 合并areaGridList里网格的父级网格
     * @param areaGridIdList
     * @return
     */
    public Set<String> mergeParentAreaGrid(List<String> areaGridIdList) {
        List<AreaGrid> allList = this.selectListAll();
        Set<String> resultList=new HashSet<>();
        
        //把源网格先找出来
        Set<AreaGrid> sourceAreaGridSet=new HashSet<>();
        for(AreaGrid areaGrid:allList) {
            if(areaGridIdList.contains(String.valueOf(areaGrid.getId()))) {
                //表明源网格应包含当前areaGrid对象
                sourceAreaGridSet.add(areaGrid);
            }
        }
        
        if(BeanUtil.isNotEmpty(areaGridIdList)) {
            for (AreaGrid areaGrid : sourceAreaGridSet) {
                mergeParentAreaGridAssist(allList,resultList,areaGrid);
            }
        }
        
        return resultList;
    }

    private void mergeParentAreaGridAssist(List<AreaGrid> allList, Set<String> resultSet, AreaGrid areaGrid) {
        // 先把当前网格ID加入结果集
        resultSet.add(String.valueOf(areaGrid.getId()));

        for (AreaGrid tmp : allList) {
            if (areaGrid.getGridParent().equals(tmp.getId())) {
                // 说明tmp为areaGrid的父级网格
                resultSet.add(String.valueOf(tmp.getId()));
                if (!"-1".equals(String.valueOf(tmp.getGridParent()))) {
                    // tmp还有父级网格
                    mergeParentAreaGridAssist(allList, resultSet, tmp);
                }
            }
        }
    }

    /**
     * 根据子网格，查询到该子网格的父级网格
     * 
     * @param sunAreaGrid
     *            待合并的子网格
     * @return sunAreaGrid的父级网格或null
     */
    public AreaGrid getParentAreaGrid(AreaGrid sunAreaGrid) {
        if (BeanUtil.isEmpty(sunAreaGrid)) {
            return null;
        }

        if (sunAreaGrid.getGridParent() != null && !"-1".equals(sunAreaGrid.getGridParent())) {
            AreaGrid areaGridParent = this.selectById(sunAreaGrid.getGridParent());
            if (BeanUtil.isNotEmpty(areaGridParent)) {
                return areaGridParent;
            } else {
                return null;
            }
        }

        return null;
    }

    /**
     * 网格全部定位
     * 
     * @param areaGrid
     * @return
     */
    public TableResultResponse<AreaGrid> allPotition(AreaGrid areaGrid) {
        List<AreaGrid> areaGrids = this.mapper.allPotition(areaGrid);
        return new TableResultResponse<>(areaGrids.size(), areaGrids);
    }

    /**
     * 通过用户ids获取网格信息列表
     *
     * @return
     */
    public List<Map<String, Object>> getByUserIds(List<String> userIds) {
        if (BeanUtils.isEmpty(userIds)) {
            userIds = new ArrayList<>();
        }
        List<Map<String, Object>> result = this.mapper.selectByUserIds(userIds);
        return BeanUtils.isEmpty(result) ? new ArrayList<>() : result;
    }

    /**
     * 按网格等级查询网格ID，如果该网格等级下含有子网格，则将其子网格ID一并收集起来<br/>
     * 返回结果为以下形式：{"2","2,3,4,5"}<br/>
     * "2"表示与gridLevel等级对应的某网格，"2,3,4,5"为包含该网格下子网格后的ID集合
     * @param gridLevel
     * @return
     */
    public Map<Integer, Set<String>> getByLevelBindChildren(String gridLevel) {
        if (StringUtils.isBlank(gridLevel)) {
            return new HashMap<>();
        }

        Example example =new Example(AreaGrid.class).selectProperties("id", "gridName", "gridLevel", "gridParent");
        example.createCriteria().andEqualTo("isDeleted", "0");
        if(gridLevel.contains(",")){
        List<String> _gridLevel = Arrays.asList(gridLevel.split(","));
            example.createCriteria().andIn("gridLevel",_gridLevel);
        }
        List<AreaGrid> areaGrids = this.selectByExample(example);

        Map<Integer, Set<String>> gridIdBindChildrenMap = new HashMap<>();
        if (BeanUtil.isNotEmpty(areaGrids)) {
            for (AreaGrid areaGridTmp : areaGrids) {
                if (gridLevel.contains(areaGridTmp.getGridLevel())) {
                    Set<String> gridIdSetBindChildrenInMap = new HashSet<>();
                    _bindChildren(areaGrids, areaGridTmp, gridIdSetBindChildrenInMap);
                    gridIdBindChildrenMap.put(areaGridTmp.getId(), gridIdSetBindChildrenInMap);
                }
            }
        }

        return gridIdBindChildrenMap;
    }



    private void _bindChildren(List<AreaGrid> areaGrids, AreaGrid areaGrid,Set<String> gridIdSetBindChildrenInMap) {
        gridIdSetBindChildrenInMap.add(String.valueOf(areaGrid.getId()));// 先把自己放到结果集内
        for (AreaGrid areaGridTmp : areaGrids) {
            if (areaGrid.getId().equals(areaGridTmp.getGridParent())) {
                // 说明该网格是传入网格id的子网格
                _bindChildren(areaGrids, areaGridTmp,gridIdSetBindChildrenInMap);
            }
        }
    }

    /**
     * 按网格等级获取网格树
     * @return
     */
    public List<AreaGridTree> getAreaGridLevelTrees(String gridLevel) {
        List<AreaGrid> allAreaGrid = this.selectListAll();
        List<AreaGrid> byGridLevel = this.getByGridLevel(gridLevel);

        // 获取byGridLevel的父网格
        Set<AreaGrid> gridBindParent=new HashSet<>();
        if(BeanUtil.isNotEmpty(byGridLevel)
        && BeanUtil.isNotEmpty(allAreaGrid)){
            for(AreaGrid tmp:byGridLevel){
                revursiveParentGrid(allAreaGrid,gridBindParent,tmp);
            }
        }

        List<AreaGridTree> trees = new ArrayList<>();

        Map<Integer, String> gridIdNameMap =
                gridBindParent.stream().collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridName));

        gridBindParent.forEach(o -> {
            String gridWithParent =
                    gridIdNameMap.get(o.getGridParent()) == null ? o.getGridName()
                            : o.getGridName() + "(" + gridIdNameMap.get(o.getGridParent()) + ")";
            trees.add(new AreaGridTree(o.getId(), o.getGridParent(), o.getGridName(),
                    o.getGridCode(), gridWithParent));
        });

        return TreeUtil.bulid(trees, -1, null);
    }

    /**
     * 递归查询父级网格
     * @param allAreaGrid
     * @param gridBindParent
     */
    private void revursiveParentGrid(List<AreaGrid> allAreaGrid, Set<AreaGrid> gridBindParent,
        AreaGrid currentAreaGrid) {
//        gridBindParent.add(currentAreaGrid);
        for (AreaGrid tmp : allAreaGrid) {
            if (StringUtils.equals(String.valueOf(currentAreaGrid.getGridParent()),
                String.valueOf(tmp.getId()))) {
                // 说明tmp是currentAreaGrid的父节点
                gridBindParent.add(tmp);
                if (-1 != tmp.getGridParent()) {
                    revursiveParentGrid(allAreaGrid, gridBindParent, tmp);
                }
            }
        }
    }

    /**
     * 获取网格的父级网格名称
     *
     * @param gridId 网格id
     * @return {
     * "gridName":"网格名称",
     * "parentGridName":"网格父级名称",
     * }
     */
    public Map<String, String> getParentNameById(String gridId) {

        if (StringUtils.isBlank(gridId)) {
            return new HashMap<>();
        }
        return this.mapper.selectParentNameById(gridId);
    }

    /**
     * 按网格等级获取列表，结果集中不包含坐标信息
     * @param areaGrid
     * @return
     */
    public List<AreaGrid> gridLevelWithoutMapInfo(AreaGrid areaGrid) {
        List<AreaGrid> areaGridList = this.mapper.gridLevelWithoutMapInfo(areaGrid);

        return BeanUtils.isEmpty(areaGridList)?new ArrayList<>():areaGridList;
    }

    /**
     * 按ID获取网格及子网格
     * @param gridId
     */
    public List<AreaGrid> getAreaGridBindChileren(Integer gridId) {
        AreaGrid areaGrid = this.selectById(gridId);
        Set<String> gridSet=new HashSet<>();

        if(BeanUtil.isNotEmpty(areaGrid)){
            Example example =
                new Example(AreaGrid.class).selectProperties("id", "gridName", "gridLevel",
                    "gridParent");
            example.createCriteria().andEqualTo("isDeleted", "0");
            List<AreaGrid> areaGrids = this.selectByExample(example);
            this._bindChildren(areaGrids, areaGrid, gridSet);

            Map<Integer, AreaGrid> collect =
                areaGrids.stream().collect(Collectors.toMap(AreaGrid::getId, areagrid -> areagrid));

            List<AreaGrid> result=new ArrayList<>();
            for(String gridStr:gridSet){
                result.add(collect.get(Integer.valueOf(gridStr)));
            }

            return result;
        }else{
            return new ArrayList<>();
        }
    }

    /**
     * 查询所有网格的基本信息
     *
     * @return
     */
    public List<AreaGrid> getAllAreaGridWithBasic() {
        Example example = new Example(AreaGrid.class).selectProperties("id", "gridName", "gridLevel", "gridParent");
        example.createCriteria().andEqualTo("isDeleted", "0");
        List<AreaGrid> areaGrids = this.selectByExample(example);
        return areaGrids == null ? new ArrayList<>() : areaGrids;
    }

    /**
     * 网格员考核
     *
     * @param month
     * @param gridId
     * @param gridMember
     * @param gridRole
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> getAssessment(String month, Integer gridId, String gridMember,String isPartyMember,
                                                         String gridRole, Integer page, Integer limit) {
        TableResultResponse<JSONObject> result = new TableResultResponse<>();

        Date monthStart;
        Date monthEnd;
        //当前月
        Date monthDate;
        //默认当月数据
        if (StringUtils.isEmpty(month)) {
            monthDate = new Date();
        } else {
            //指定月份
            monthDate = DateUtil.dateFromStrToDate(month, "yyyy-MM");
        }
        monthStart = DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(monthDate));
        monthEnd = DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(DateUtil.theDayOfMonthPlus(monthDate, 1)));
        //巡查时长所需参数
        JSONObject addition = new JSONObject();
        addition.put("month", month);
        PatrolRecord patrolRecord = new PatrolRecord();
        Map<String, String> longTime = null;
        //按照网格查询
        Set<Integer> gridIdCollect = null;
        if (BeanUtils.isNotEmpty(gridId)) {
            //子网格ID集合
            List<AreaGrid> areaGridBindChileren = getAreaGridBindChileren(gridId);
            if(BeanUtil.isNotEmpty(areaGridBindChileren)){
                gridIdCollect = areaGridBindChileren.stream().map(AreaGrid::getId).collect(Collectors.toSet());
            }
        }
        //按照姓名查询
        List<String> userList = new ArrayList<>();
        //base_user表
        Map<String, String> memIdNameMap = new HashMap<>();
        //是否进行人员姓名的查询 默认true 查询人员姓名
        boolean flag = true;
        if (StringUtils.isNotBlank(gridMember) || StringUtils.isNotBlank(isPartyMember)) {
            JSONArray usersByName = adminFeign.getByNameAndParty(gridMember,isPartyMember);
            if (BeanUtil.isNotEmpty(usersByName)) {
                for (int i = 0; i < usersByName.size(); i++) {
                    userList.add(usersByName.getJSONObject(i).getString("id"));
                }
                flag = false;
            } else {
                return new TableResultResponse<>();
            }
        }
        //考核列表
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<JSONObject> jsonObjects =
                this.mapper.getAssessment(monthStart, monthEnd, gridIdCollect, userList , gridRole);

        //按照人员姓名查询的情况下，不再从基础服务获取网格人员姓名
        if (flag) {
            //获取ID集合
            userList = jsonObjects.stream().map(o -> o.getString("grid_member")).distinct().collect(Collectors.toList());
        }
        //获取巡查总时长
        longTime = patrolRecordBiz.totalPatrolTimeLength(patrolRecord, addition, userList);
        if (BeanUtils.isNotEmpty(userList)) {
            Map<String, String> usersByUserIds = adminFeign.getUsersByUserIds(StringUtils.join(userList, ","));
            if (BeanUtils.isNotEmpty(usersByUserIds)) {
                for (Map.Entry<String, String> e : usersByUserIds.entrySet()) {
                    JSONObject jsonObject = JSONObject.parseObject(e.getValue());
                    //人员姓名
                    memIdNameMap.put(e.getKey(), jsonObject.getString("name"));
                }
            }
        }
        //  夜间 巡查 时长结算
        Map<String, String> nightPatrol = patrolRecordBiz.nightPatrolTimeLength(addition, userList);
        //整合数据
        for (JSONObject objTmp : jsonObjects) {
            //人员姓名并整合数据
            objTmp.put("userName", memIdNameMap.get(objTmp.getString("grid_member")));
            //查询网格名称并整合数据
            List<JSONObject> grid = getGridsByMemberId(objTmp.getString("grid_member"));
            List<String> gridNameCollect =
                    grid.stream().map(o -> o.getString("grid_name")).distinct().collect(Collectors.toList());
            String join = StringUtils.join(gridNameCollect, ",");
            objTmp.put("gridName", join);
            //上报效率
            if (objTmp.getInteger("reportsTotal") == 0) {
                objTmp.put("reportEfficiency", "0%");
            } else {
                String reportEfficiency = PercentUtil.accuracy((double) objTmp.getInteger("yReported")-(double) objTmp.getInteger("findTermination"), (double) objTmp.getInteger("yReported"), 2);
                objTmp.put("reportEfficiency", reportEfficiency);
            }
            //事件处理总量
            Integer eventTotal = objTmp.getInteger("processingWorkOrder") + objTmp.getInteger("getThouth") + objTmp.getInteger("reportsBack");
            objTmp.put("eventTotal", eventTotal);
            //任务办理率
            double total = (double)objTmp.getInteger("processingWorkOrder") + (double)objTmp.getInteger("getThouth");
            double prc = (double)objTmp.getInteger("processingWorkOrder") + (double)objTmp.getInteger("getThouth") - (double)objTmp.getInteger("reportsBack");
            if (total == 0) {
                objTmp.put("taskEfficiency", "0%");
            } else {
                String taskEfficiency = PercentUtil.accuracy(prc, total, 2);
                objTmp.put("taskEfficiency", taskEfficiency);
            }
            //巡查时长
            objTmp.put("patrolTimeLength", longTime.get(objTmp.getString("grid_member")));
            //夜间巡查时长
            objTmp.put("nightPatrol",nightPatrol.get(objTmp.getString("grid_member")));
        }
        result.getData().setTotal(pageInfo.getTotal());
        result.getData().setRows(jsonObjects);
        return result;
    }

    /**
     * 根据人员id 获取其所在的所有网格
     *
     * @param memberId
     * @return
     */
    public List<JSONObject> getGridsByMemberId(String memberId){
        List<JSONObject> gridsByMemberId = this.mapper.getGridsByMemberId(memberId);
        if(BeanUtils.isNotEmpty(gridsByMemberId)){
            return  gridsByMemberId;
        }
        return new ArrayList<>();
    }

    /**
     * 通过网格id，获取网格所属id，无下限
     * TODO 优化所有网格集，考虑缓存
     *
     * @param gridId 网格id
     * @return ids
     */
    public Set<Integer> getByAreaGridId(Integer gridId) {
        List<AreaGrid> areaGridList = this.getAllAreaGridWithBasic();
        Set<Integer> ids = new HashSet<>();
        for (AreaGrid areaGrid : areaGridList) {
            if (areaGrid.getId().equals(gridId)) {
                ids.add(areaGrid.getId());
                this.getAreaSon(areaGrid.getId(), ids, areaGridList);
            }
        }
        return ids;
    }

    /**
     * 获取网格id的子集
     *
     * @param areaId       网格id
     * @param ids          id集合
     * @param areaGridList 网格集合
     */
    private void getAreaSon(Integer areaId, Set<Integer> ids, List<AreaGrid> areaGridList) {
        for (AreaGrid areaGrid : areaGridList) {
            if (areaId.equals(areaGrid.getGridParent())) {
                ids.add(areaGrid.getId());
                this.getAreaSon(areaGrid.getId(), ids, areaGridList);
            }
        }
    }

}