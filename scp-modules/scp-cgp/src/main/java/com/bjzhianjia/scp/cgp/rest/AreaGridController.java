package com.bjzhianjia.scp.cgp.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.Point;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.AreaGridService;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.AreaGridTree;
import com.bjzhianjia.scp.cgp.vo.AreaGridVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.common.util.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * AreaGridController 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月16日          尚      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author 尚
 *
 */
@RestController
@RequestMapping("areaGrid")
@CheckClientToken
@CheckUserToken
@Api(tags = "网格管理")
public class AreaGridController extends BaseController<AreaGridBiz, AreaGrid, Integer> {

    @Autowired
    private AreaGridService areaGridService;
    
    @Autowired
    private AreaGridBiz areaGridBiz;
    
    /**
     * 添加单个对象
     * @param areaGridJObject
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<JSONObject> add(@RequestBody @Validated JSONObject areaGridJObject,
        BindingResult bindingResult) {

        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<Void> result = areaGridService.createAreaGrid(areaGridJObject);

        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        return restResult.data(areaGridJObject);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<JSONObject> update(@RequestBody @Validated JSONObject areaGridJObject,
        BindingResult bindingResult) {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<Void> result = areaGridService.updateAreaGrid(areaGridJObject);

        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        return restResult.data(areaGridJObject);
    }

    @RequestMapping(value = "/remove/{id}", method = RequestMethod.PUT)
    @ApiOperation("删除单个对象")
    public ObjectRestResponse<AreaGrid> removeOne(@PathVariable(value = "id") @ApiParam(name = "待删除对象ID") Integer id) {
        ObjectRestResponse<AreaGrid> restResult = new ObjectRestResponse<>();
        Result<Void> result = areaGridService.removeOne(id);

        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }
        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("分页获取网格列表")
    public TableResultResponse<AreaGridVo> list(@RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "") String gridName,
        @RequestParam(defaultValue = "") String gridLevel,
        @RequestParam(value = "gridParent", required = false) Integer gridParent,
        @RequestParam(value = "gridParentName", defaultValue = "") String gridParentName) {
        AreaGrid areaGrid = new AreaGrid();
        areaGrid.setGridName(gridName);
        areaGrid.setGridLevel(gridLevel);
        areaGrid.setGridParent(gridParent);
        TableResultResponse<AreaGridVo> list = areaGridService.getList(page, limit, areaGrid,gridParentName);
        return list;
    }

    @RequestMapping(value = "/list/level/{levels}", method = RequestMethod.GET)
    @ApiOperation("按网格等级获取网格列表")
    public List<AreaGrid> getByGridLevel(@PathVariable(value = "levels") @ApiParam(name = "网络等级ID") String gridLevel) {
        List<AreaGrid> areaGridList = areaGridService.getByGridLevel(gridLevel);
        return areaGridList;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation("查询 单个对象")
    public ObjectRestResponse<JSONObject> getOne(@PathVariable(value = "id") @ApiParam(name = "待查询网格ID") Integer id) {
        return areaGridService.getOne(id);
    }

    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ApiOperation("获取网格树")
    public List<AreaGridTree> getTree() {
        List<AreaGrid> allAreaGrids = this.baseBiz.getByMap(new HashedMap<>());

        if(BeanUtil.isEmpty(allAreaGrids)){
            return new ArrayList<>();
        }

        List<AreaGridTree> trees = new ArrayList<>();

        Map<Integer, String> gridIdNameMap =
            allAreaGrids.stream().collect(Collectors.toMap(AreaGrid::getId, AreaGrid::getGridName));

        allAreaGrids.forEach(o -> {
            String gridWithParent =
                gridIdNameMap.get(o.getGridParent()) == null ? o.getGridName()
                    : o.getGridName() + "(" + gridIdNameMap.get(o.getGridParent()) + ")";
            trees.add(new AreaGridTree(o.getId(), o.getGridParent(), o.getGridName(),
                o.getGridCode(), gridWithParent, o.getGridLevel()));
        });

        return TreeUtil.bulid(trees, -1, null);
    }

    /**
     *  判断当前坐标是否在一个多边形区域内
     * 
     * @param lng
     *            经度
     * @param lat
     *            纬度
     * @return
     */
    @ApiOperation("判断当前坐标是否在一个多边形区域内")
    @RequestMapping(value="/polygonPoint",method=RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<AreaGrid> isPolygonContainsPoint(
        @RequestParam @ApiParam("经度") Double lng,
        @RequestParam @ApiParam("纬度") Double lat) {
      
        ObjectRestResponse<AreaGrid> resut = new ObjectRestResponse<AreaGrid>();
        resut.setStatus(400);
        
        if(lng == null || lat == null) {
            resut.setMessage("非法参数!");
            return resut;
        }
        
        AreaGrid areaGrid = areaGridBiz.isPolygonContainsPoint(new Point(lng,lat));
        if(areaGrid != null){
            AreaGrid _areaGrid = new AreaGrid();
            _areaGrid.setId(areaGrid.getId());
            resut.setData(_areaGrid);
            resut.setStatus(200);
        }else {
            resut.setMessage("当前位置，不属于系统内置网格范围！");
        }
        return resut;
    }
    
    /**
     *  判断当前坐标是否在一个多边形区域内
     * 
     * @param lng
     *            经度
     * @param lat
     *            纬度
     * @return
     */
    @ApiOperation("判断当前坐标是否在一个多边形区域内,只涉及最低级别网格")
    @RequestMapping(value="/polygonPoint/lowest",method=RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<AreaGrid> isLowestGridContainsPoint(
        @RequestParam @ApiParam("经度") Double lng,
        @RequestParam @ApiParam("纬度") Double lat) {
        
        ObjectRestResponse<AreaGrid> resut = new ObjectRestResponse<AreaGrid>();
        resut.setStatus(400);
        
        if(lng == null || lat == null) {
            resut.setMessage("非法参数!");
            return resut;
        }
        
        AreaGrid areaGrid = areaGridBiz.isLowestGridContainsPoint(new Point(lng,lat));
        if(areaGrid != null){
            AreaGrid areaGridForReturn = new AreaGrid();
            areaGridForReturn.setId(areaGrid.getId());
            areaGridForReturn.setGridName(areaGrid.getGridName());
            resut.setData(areaGridForReturn);
            resut.setStatus(200);
        }else {
            resut.setMessage("当前位置，不属于系统内置网格范围！");
        }
        return resut;
    }

    /**
     * 当前端想查询特定网格等级下的网格时(并没有拿网格等级code值)，调用该接口
     * @param gridLevelKey
     * @return
     */
    @GetMapping("/list/level")
    @ApiOperation("根据网格等级获取网格")
    public TableResultResponse<JSONObject> getByAreaGrid(@RequestParam @ApiParam("待查询网格等级") String gridLevelKey){
        TableResultResponse<JSONObject> restResult = this.baseBiz.getByAreaGrid(gridLevelKey);
            return restResult;
    }

    /**
     * 当前端已获取网络等级code值时(如通过网格等级列表加载的)，调用该接口
     * @param gridLevelKey
     * @return
     */
    @GetMapping("/list/gridLevel")
    @ApiOperation("根据网格等级获取网格(已获取网格等级code)")
    public TableResultResponse<JSONObject> getByAreaGridP(@RequestParam @ApiParam("待查询网格等级") String gridLevelKey){
        TableResultResponse<JSONObject> restResult = this.baseBiz.getByAreaGridP(gridLevelKey);
            return restResult;
    }

    @GetMapping("/all/potition")
    @ApiOperation("网格全部定位")
    public TableResultResponse<AreaGrid> allPotition(
        @RequestParam(defaultValue = "", value = "gridLevel") String gridLevel) {
        AreaGrid areaGrid = new AreaGrid();
        areaGrid.setGridLevel(gridLevel);

        TableResultResponse<AreaGrid> areaGridTableResultResponse =
            this.baseBiz.allPotition(areaGrid);
        return areaGridTableResultResponse;
    }

    @RequestMapping(value = "/tree/gridLevel", method = RequestMethod.GET)
    @ApiOperation("获取网格树")
    public List<AreaGridTree> getTreeGridLevel(@RequestParam(value = "gridLevel") String gridLevel) {
        return this.baseBiz.getAreaGridLevelTrees(gridLevel);
    }

    @RequestMapping(value = "/list/gridLevel/WithoutMapInfo", method = RequestMethod.GET)
    @ApiOperation("分页获取网格列表")
    public List<AreaGrid> gridLevelWithoutMapInfo(
        @RequestParam(value = "gridLevel", defaultValue = "") String gridLevel) {
        AreaGrid areaGrid = new AreaGrid();
        areaGrid.setGridLevel(gridLevel);
        return this.baseBiz.gridLevelWithoutMapInfo(areaGrid);
    }

    @GetMapping("/assessment")
    public TableResultResponse<JSONObject> memAssessment(
            @RequestParam(value = "month", defaultValue = "") @ApiParam("月度,yyyy-MM") String month,
            @RequestParam(value = "gridId", required = false) @ApiParam("行政村ID") Integer gridId,
            @RequestParam(value = "gridMember", defaultValue = "") @ApiParam("姓名") String gridMember,
            @RequestParam(value = "gridRole", defaultValue = "") @ApiParam("人员角色") String gridRole,
            @RequestParam(value = "isPartyMember", defaultValue = "") @ApiParam("是否党员") String isPartyMember,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        return this.baseBiz.getAssessment(month, gridId, gridMember,isPartyMember, gridRole, page, limit);
    }


    @GetMapping("/gridChileren")
    public List<AreaGrid> getAreaGridBindChileren(
            @RequestParam(value = "gridId", defaultValue = "") @ApiParam("网格id") Integer gridId){
        List<AreaGrid> areaGridBindChileren = this.baseBiz.getAreaGridBindChileren(gridId);
        for (int i = 0; i < areaGridBindChileren.size(); i++) {
            if(areaGridBindChileren.get(i).getId().equals(gridId)){
                areaGridBindChileren.remove(i);
            }
        }
        return areaGridBindChileren;
    }

}