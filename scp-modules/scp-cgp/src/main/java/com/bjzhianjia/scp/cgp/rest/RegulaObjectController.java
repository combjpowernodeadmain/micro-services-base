package com.bjzhianjia.scp.cgp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.RegulaObjectService;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.Regula_EnterPriseVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("regulaObject")
@CheckClientToken
@CheckUserToken
@Api(tags = "监管对象管理")
public class RegulaObjectController extends BaseController<RegulaObjectBiz, RegulaObject, Integer> {
	@Autowired
	private RegulaObjectService regulaObjectService;
	
//	@RequestMapping(value="/add_other",method=RequestMethod.POST)
//	@ApiOperation("新增单个对象")
//	public ObjectRestResponse<JSONObject> add(@RequestBody @Validated Regula_EnterPriseVo vo) {
//		System.out.println(vo);
//		
//		RegulaObject copyBean_New = BeanUtil.copyBean_New(vo, new RegulaObject());
//		EnterpriseInfo copyBean_New2 = BeanUtil.copyBean_New(vo, new EnterpriseInfo());
//		return null;
//	}

	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ApiOperation("新增单个对象")
	public ObjectRestResponse<JSONObject> add(@RequestBody @Validated Regula_EnterPriseVo vo, BindingResult bindingResult) {
		RegulaObject regulaObject = BeanUtil.copyBean_New(vo, new RegulaObject());
		EnterpriseInfo enterpriseInfo = BeanUtil.copyBean_New(vo, new EnterpriseInfo());
		
		ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(200);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<Void> result = regulaObjectService.createRegulaObject(regulaObject, enterpriseInfo);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		JSONObject r_JsonObject=JSONObject.parseObject(JSON.toJSONString(regulaObject));
		JSONObject e_JsonObject=JSONObject.parseObject(JSON.toJSONString(enterpriseInfo));
		
		return restResult.data(BeanUtil.jsonObjectMergeOther(r_JsonObject, e_JsonObject));
	}
	
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	@ApiOperation("更新单个对象")
	public ObjectRestResponse<JSONObject> update(@RequestBody @Validated Regula_EnterPriseVo vo, BindingResult bindingResult){
		RegulaObject regulaObject = BeanUtil.copyBean_New(vo, new RegulaObject());
		EnterpriseInfo enterpriseInfo = BeanUtil.copyBean_New(vo, new EnterpriseInfo());
		
		regulaObject.setId(vo.getRegulaObjectId());
		enterpriseInfo.setId(vo.getEnterpriseId());
		
		ObjectRestResponse<JSONObject> restResult=new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = regulaObjectService.updateRegulaObject(regulaObject, enterpriseInfo);
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		
		JSONObject r_JsonObject=JSONObject.parseObject(JSON.toJSONString(regulaObject));
		JSONObject e_JsonObject=JSONObject.parseObject(JSON.toJSONString(enterpriseInfo));
		return restResult.data(BeanUtil.jsonObjectMergeOther(r_JsonObject, e_JsonObject));
	}

	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ApiOperation("分页获取对象")
	public TableResultResponse<JSONObject> page(@RequestParam(defaultValue = "10") int limit
			,@RequestParam(defaultValue = "1") int page,
			@RequestBody RegulaObject regulaObject){
		
				return null;
	}
}