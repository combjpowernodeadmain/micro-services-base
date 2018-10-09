package com.bjzhianjia.scp.cgp.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.AreaGridService;
import com.bjzhianjia.scp.cgp.entity.Point;
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
     * 
     * @author 尚
     * @param areaGrid
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
        // AreaGrid areaGrid = new AreaGrid();
        // areaGrid.setId(id);
        // areaGrid.setIsDeleted("1");
        // this.baseBiz.updateSelectiveById(areaGrid);
        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("分页获取网格列表")
    public TableResultResponse<AreaGridVo> list(@RequestParam(defaultValue = "10") int limit,
        @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "") String gridName,
        @RequestParam(defaultValue = "") String gridLevel) {
        AreaGrid areaGrid = new AreaGrid();
        areaGrid.setGridName(gridName);
        areaGrid.setGridLevel(gridLevel);
        TableResultResponse<AreaGridVo> list = areaGridService.getList(page, limit, areaGrid);
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
        TableResultResponse<AreaGridVo> list = areaGridService.getList(1, 2147483647, new AreaGrid());
        List<AreaGridVo> all = list.getData().getRows();

        List<AreaGridTree> trees = new ArrayList<>();

        all.forEach(o -> {
            trees.add(new AreaGridTree(o.getId(), o.getGridParent(), o.getGridName(), o.getGridCode()));
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
}