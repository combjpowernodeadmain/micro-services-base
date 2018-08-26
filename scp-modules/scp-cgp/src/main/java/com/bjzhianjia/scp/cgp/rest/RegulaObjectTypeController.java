package com.bjzhianjia.scp.cgp.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectTypeBiz;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.RegulaObjectTypeService;
import com.bjzhianjia.scp.cgp.vo.RegulaObjTypeTree;
import com.bjzhianjia.scp.cgp.vo.RegulaObjectTypeVo;
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
 * Created by mengfanguang on 2018-07-31 14:19:12
 */
@RestController
@RequestMapping("/regulaObjectType")
@CheckClientToken
@CheckUserToken
@Api(tags = "监管对象类型表")
public class RegulaObjectTypeController extends BaseController<RegulaObjectTypeBiz, RegulaObjectType, Integer> {

	@Autowired
	private RegulaObjectTypeService regulaObjectTypeService;
	@Autowired
	private RegulaObjectTypeBiz regulaObjectTypeBiz;
	@Autowired
	private RegulaObjectBiz regulaObjectBiz;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("新增单个监测对象类别")
	public ObjectRestResponse<RegulaObjectType> add(@RequestBody @Validated RegulaObjectType regulaObjectType,
			BindingResult bindingResult) {

		ObjectRestResponse<RegulaObjectType> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		Result<Void> result = regulaObjectTypeService.createRegulaObjectType(regulaObjectType);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		return restResult.data(regulaObjectType);
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation("修改单监测个对象类别")
	public ObjectRestResponse<RegulaObjectType> update(@RequestBody @Validated RegulaObjectType regulaObjectType,
			BindingResult bindingResult) {

		ObjectRestResponse<RegulaObjectType> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<Void> result = regulaObjectTypeService.updateRegulaObject(regulaObjectType);

		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		return restResult.data(regulaObjectType);
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation("按分页查询对象")
	public TableResultResponse<RegulaObjectTypeVo> list(
			@RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
			@RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
			@RequestParam(defaultValue = "") @ApiParam(name = "监管对象类别编号") String objectTypeCode,
			@RequestParam(defaultValue = "") @ApiParam(name = "监管对象类型名称") String objectTypeName,
			@RequestParam(defaultValue = "") @ApiParam(name = "是否可用") String isEnable) {

		RegulaObjectType regulaObjectType = new RegulaObjectType();
		regulaObjectType.setObjectTypeCode(objectTypeCode);
		regulaObjectType.setObjectTypeName(objectTypeName);
		regulaObjectType.setIsEnable(isEnable);

		TableResultResponse<RegulaObjectTypeVo> list = regulaObjectTypeService.getList(page, limit, regulaObjectType);

		return list;
	}

	@RequestMapping(value = "/remove/{ids}", method = RequestMethod.DELETE)
	@ApiOperation("批量删除对象")
	public ObjectRestResponse<RegulaObjectType> remove(
			@PathVariable @ApiParam(name = "待删除对象ID集合，多个ID用“，”隔开") Integer[] ids) {
		ObjectRestResponse<RegulaObjectType> result = new ObjectRestResponse<>();

		if (ids == null || ids.length == 0) {
			result.setStatus(400);
			result.setMessage("请选择要删除的项");
			return result;
		}

		regulaObjectTypeBiz.remove(ids);
		return result;
	}

	@RequestMapping(value = "/remove/one/{id}", method = RequestMethod.DELETE)
	@ApiOperation("删除单个对象")
	public ObjectRestResponse<RegulaObjectType> removeOne(
			@PathVariable @ApiParam(name = "待删除对象ID集合，多个ID用“，”隔开") Integer id) {
		ObjectRestResponse<RegulaObjectType> result = new ObjectRestResponse<>();

		if (id == null) {
			result.setStatus(400);
			result.setMessage("请选择要删除的项");
			return result;
		}

		Integer[] ids = new Integer[1];
		ids[0] = id;

		regulaObjectTypeBiz.remove(ids);
		return result;
	}

	@ApiOperation("获取监管对象类型树")
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public List<RegulaObjTypeTree> getTree() {
		TableResultResponse<RegulaObjectType> list = regulaObjectTypeBiz.getList(1, 2147483647, new RegulaObjectType());
		List<RegulaObjectType> all = list.getData().getRows();

		List<Map<String, String>> regulaObjCountByTypeMap = regulaObjectBiz.selectRegulaObjCountByType();
		
		Map<Object, Object> mapTmp = new HashMap<>();
		if(regulaObjCountByTypeMap!=null) {
			for (Map<String, String> map : regulaObjCountByTypeMap) {
				mapTmp.put(map.get("objType"), map.get("regulaCount"));
			}
		}

		List<RegulaObjTypeTree> trees = new ArrayList<>();
		all.forEach(o -> {
			trees.add(new RegulaObjTypeTree(o.getId() + "", o.getParentObjectTypeId() + "", o.getObjectTypeName(),
					o.getObjectTypeCode(), o.getTempletType(), mapTmp.get(o.getId())));
		});

		return TreeUtil.bulid(trees, "-1", null);
	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ApiOperation("获取单个对象")
	public ObjectRestResponse<JSONObject> getOne(@PathVariable @ApiParam(name = "待查询对象ID") Integer id) {
		ObjectRestResponse<JSONObject> result = regulaObjectTypeService.get(id);
		return result;
	}
	
	/**
	 * 根据`reg_type_relation`表中配置的信息查询监管对象类型
	 * @author 尚
	 * @return
	 */
	@RequestMapping(value="/list/relagtion",method=RequestMethod.GET)
	@ApiOperation("根据`reg_type_relation`表中配置的信息查询监管对象类型")
	public TableResultResponse<RegulaObjectType> getByRelation(@RequestParam(value="page",defaultValue="1")Integer page,
			@RequestParam(value="limit",defaultValue="10")Integer limit){
		TableResultResponse<RegulaObjectType> restResult = regulaObjectTypeService.getByRelation(page, limit);
		return restResult;
	}
}
