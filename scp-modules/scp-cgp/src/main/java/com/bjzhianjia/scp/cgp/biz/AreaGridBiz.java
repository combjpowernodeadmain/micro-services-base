package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.config.PropertiesConfig;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.Point;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.cgp.util.SpatialRelationUtil;
import com.bjzhianjia.scp.cgp.vo.AreaGridVo;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;


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

    /**
     * 按条件查询未被删除的网格
     * 
     * @author 尚
     * @param gridCde
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
     * @return
     */
    public TableResultResponse<AreaGridVo> getList(int page, int limit, AreaGrid areaGrid) {
        Example example = new Example(AreaGrid.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");

        if (StringUtils.isNotBlank(areaGrid.getGridName())) {
            criteria.andLike("gridName", "%" + areaGrid.getGridName() + "%");
        }
        if (StringUtils.isNotBlank(areaGrid.getGridLevel())) {
            criteria.andEqualTo("gridLevel", areaGrid.getGridLevel());
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
     * @param areaGrid
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
                JSONArray array = JSONArray.parseArray(areaGrid.getMapInfo());
                List<Point> listPoint = array.toJavaList(Point.class);
                if(listPoint == null) {
                    continue;
                }
                if(SpatialRelationUtil.isPolygonContainsPoint(listPoint, point)) {
                    result = areaGrid;
                    break;
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
                JSONArray array = JSONArray.parseArray(areaGrid.getMapInfo());
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
    
    
    public TableResultResponse<JSONObject> getByAreaGrid(String gridLevelKey){
        TableResultResponse<JSONObject> tableResultResponse=new TableResultResponse<>();
        
        String property = environment.getProperty(gridLevelKey);
        if(StringUtils.isBlank(property)) {
            tableResultResponse.setStatus(400);
            tableResultResponse.setMessage(gridLevelKey+"不存在");
            return tableResultResponse;
        }
        
        Example example=new Example(AreaGrid.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("gridLevel", property);
        
        List<AreaGrid> areaGridList = this.selectByExample(example);
        
        List<JSONObject> resultJObj=new ArrayList<>();
        if(BeanUtil.isNotEmpty(areaGridList)) {
            for (AreaGrid areaGrid : areaGridList) {
                JSONObject propertiesJObj;
                try {
                    propertiesJObj = propertiesProxy.swapProperties(areaGrid, "gridName","id","mapInfo");
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
}