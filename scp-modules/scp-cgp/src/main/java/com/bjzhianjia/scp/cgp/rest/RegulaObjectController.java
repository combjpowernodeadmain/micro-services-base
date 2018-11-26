package com.bjzhianjia.scp.cgp.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.RegulaObjectService;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.RegulaObjectVo;
import com.bjzhianjia.scp.cgp.vo.Regula_EnterPriseVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("regulaObject")
@CheckClientToken
@CheckUserToken
@Api(tags = "监管对象管理")
public class RegulaObjectController extends BaseController<RegulaObjectBiz, RegulaObject, Integer> {

    @Autowired
    private RegulaObjectService regulaObjectService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<JSONObject> add(@RequestBody @Validated Regula_EnterPriseVo vo,
        BindingResult bindingResult) {
        RegulaObject regulaObject = BeanUtil.copyBean_New(vo, new RegulaObject());
        EnterpriseInfo enterpriseInfo = BeanUtil.copyBean_New(vo, new EnterpriseInfo());
        enterpriseInfo.setAddress(regulaObject != null ? regulaObject.getObjAddress() : "");
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }
        //mapinfo {"lng":"xxx","lat":"xxx"}
        JSONObject mapinfo = JSONObject.parseObject(vo.getMapInfo());
        if(mapinfo != null && regulaObject != null){
            regulaObject.setLatitude(mapinfo.getFloat("lat"));
            regulaObject.setLongitude(mapinfo.getFloat("lng"));
        }
        Result<Void> result = regulaObjectService.createRegulaObject(regulaObject, enterpriseInfo);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        JSONObject r_JsonObject = JSONObject.parseObject(JSON.toJSONString(regulaObject));
        JSONObject e_JsonObject = JSONObject.parseObject(JSON.toJSONString(enterpriseInfo));

        return restResult.data(BeanUtil.jsonObjectMergeOther(r_JsonObject, e_JsonObject));
    }

    /**
     * 更新单个对象，须传入两个ID<br/>
     * "regulaObjectId":1, //监管对象ID<br/>
     * "enterpriseId":1//企业信息ID
     * 
     * @author 尚
     * @param vo
     * @param bindingResult
     * @return
     */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<JSONObject> update(
        @RequestBody @Validated @ApiParam(name = "待更新对象实例") Regula_EnterPriseVo vo, BindingResult bindingResult) {
        RegulaObject regulaObject = BeanUtil.copyBean_New(vo, new RegulaObject());
        EnterpriseInfo enterpriseInfo = BeanUtil.copyBean_New(vo, new EnterpriseInfo());

        regulaObject.setId(vo.getRegulaObjId());

        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }
        //mapinfo {"lng":"xxx","lat":"xxx"}
        JSONObject mapinfo = JSONObject.parseObject(vo.getMapInfo());
        if(mapinfo != null && regulaObject != null){
            regulaObject.setLatitude(mapinfo.getFloat("lat"));
            regulaObject.setLongitude(mapinfo.getFloat("lng"));
        }
        Result<Void> result = regulaObjectService.updateRegulaObject(regulaObject, enterpriseInfo);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        JSONObject r_JsonObject = JSONObject.parseObject(JSON.toJSONString(regulaObject));
        JSONObject e_JsonObject = JSONObject.parseObject(JSON.toJSONString(enterpriseInfo));
        return restResult.data(BeanUtil.jsonObjectMergeOther(r_JsonObject, e_JsonObject));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("分页获取对象")
    public TableResultResponse<RegulaObjectVo> page(
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @ModelAttribute @ApiParam(name = "接收查询条件的实例") RegulaObject regulaObject) {
        return regulaObjectService.getList(regulaObject, page, limit, false);
    }

    @RequestMapping(value = "/list/objType", method = RequestMethod.GET)
    @ApiOperation("获取公共机构及企业下的监管对象")
    public TableResultResponse<RegulaObjectVo> page_ObjType(
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @ModelAttribute @ApiParam(name = "接收查询条件的实例") RegulaObject regulaObject) {
        return regulaObjectService.getList(regulaObject, page, limit, true);
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation("获取单个对象")
    public Regula_EnterPriseVo getById(@PathVariable(value = "id") @ApiParam(name = "待查询对象ID") Integer id) {
        Regula_EnterPriseVo regulaObject = regulaObjectService.getById(id);
        return regulaObject;
    }

    @RequestMapping(value = "/remove/{ids}", method = RequestMethod.DELETE)
    @ApiOperation("批量删除对象")
    public ObjectRestResponse<RegulaObject> remove(
        @PathVariable(value = "ids") @ApiParam(name = "待删除对象ID数组") Integer[] ids) {
        ObjectRestResponse<RegulaObject> result = new ObjectRestResponse<>();

        if (ids == null || ids.length == 0) {
            result.setStatus(400);
            result.setMessage("请选择要删除的项");
            return result;
        }

        regulaObjectService.remove(ids);
        return result;
    }

    @RequestMapping(value = "/remove/one/{id}", method = RequestMethod.DELETE)
    @ApiOperation("删除单个对象")
    public ObjectRestResponse<RegulaObject> remove(@PathVariable(value = "id") @ApiParam(name = "待删除对象ID") Integer id) {
        ObjectRestResponse<RegulaObject> result = new ObjectRestResponse<>();

        if (id == null) {
            result.setStatus(400);
            result.setMessage("请选择要删除的项");
            return result;
        }

        Integer[] ids = new Integer[1];
        ids[0] = id;

        regulaObjectService.remove(ids);
        return result;
    }


    @RequestMapping(value = "distance", method = { RequestMethod.GET })
    @ApiOperation("获取指定范围内的监管对象")
    public ObjectRestResponse<List<Map<String, Object>>> distance(
        @RequestParam(value = "longitude") @ApiParam("经度") Double longitude,
        @RequestParam(value = "latitude") @ApiParam("纬度") Double latitude,
        @RequestParam(value = "objType",defaultValue = "") @ApiParam("监管对象类型id") Integer objType,
        @RequestParam(value = "objName",defaultValue = "") @ApiParam("监管对象名称") String objName,
        @RequestParam(value = "size", defaultValue = "500") @ApiParam("监管对象范围大小（单位：米）") Double size,
        @RequestParam(value = "limit", defaultValue = "10") @ApiParam(name = "页容量") Integer limit,
        @RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page) {

        ObjectRestResponse<List<Map<String, Object>>> result = new ObjectRestResponse<>();

        if (longitude == null) {
            result.setStatus(400);
            result.setMessage("经度不能为空！");
            return result;
        }
        if (latitude == null) {
            result.setStatus(400);
            result.setMessage("纬度不能为空！");
            return result;
        }
        List<Map<String, Object>> objs = regulaObjectService.getByDistance(longitude, latitude, objType,objName, size
                ,limit,page);
        result.setData(objs);
        return result;
    }

    @RequestMapping(value = "/patrol/count", method = RequestMethod.GET)
    @ApiOperation("查询监管对象被巡查次数信息")
    public TableResultResponse<JSONObject> getRegObjPatrolInfo(
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @RequestParam(value = "regObjIds", required = false) @ApiParam(name = "监管对象Id") String regObjIds,
        @RequestParam(value = "regObjType", required = false) @ApiParam(name = "监管对象类型") Integer objType
        ) {

        RegulaObject regulaObject = new RegulaObject();
        regulaObject.setObjType(objType);
        return this.baseBiz.getRegObjPatrolInfo(regulaObject,regObjIds, page, limit);
    }
    
    @GetMapping("/list/ids/{ids}")
    @ApiOperation("按ID集合获取列表")
    public TableResultResponse<JSONObject> getByIds(@PathVariable("ids") @ApiParam("待查询列表ID集合") Integer[] ids){
        return this.baseBiz.getByIds(ids);
    }
    
    @RequestMapping(value = "/list/areaGrid", method = RequestMethod.GET)
    @ApiOperation("查询分页，不进行分页")
    public TableResultResponse<Integer> listByAreaGrid(
        @RequestParam("areaGridIds") @ApiParam(name = "网格ID集合，多个网格ID用逗号隔开") String areaGridIds) {
        return this.baseBiz.getListNoPage(areaGridIds);
    }
    
    @RequestMapping(value = "/list/objTypes", method = RequestMethod.GET)
    @ApiOperation("按监管对象类型集合查询监管对象")
    public TableResultResponse<RegulaObjectVo> listByObjType(
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @RequestParam(value="objTypes",required=false) @ApiParam(name = "监管对象类型ID集合") String objTypes,
        @RequestParam(value="objName",required=false) @ApiParam(name = "监管对象类型名称") String name) {

        return regulaObjectService.listByObjType(page, limit, objTypes, name);
    }

    @GetMapping("/isRegObjEnterprise/{regObjId}")
    @ApiOperation("判断一个监管对象是否为企业")
    public ObjectRestResponse<Boolean> isRegObjEnterprise(@PathVariable("regObjId") Integer regObjId) {
        return this.baseBiz.isRegObjEnterprise(regObjId);
    }

    @GetMapping("/all/potition")
    @ApiOperation("监管对象全部定位")
    public TableResultResponse<RegulaObjectVo> allPotition(
        @RequestParam(required = false, value = "objType") Integer objType) {
        RegulaObject regulaObject = new RegulaObject();
        regulaObject.setObjType(objType);

        TableResultResponse<RegulaObjectVo> tableResult = regulaObjectService.allPotition(regulaObject);
        return tableResult;
    }


    @PostMapping(value = "/info/collect")
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<JSONObject> regulaObjectInfoCollect(@RequestBody @Validated JSONObject vo,
                                              BindingResult bindingResult) {

        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }
        Result<Void> result = regulaObjectService.regulaObjectInfoCollect(vo);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        restResult.setStatus(200);
        restResult.setMessage("监管对象信息提交成功");
        return restResult;
    }
}