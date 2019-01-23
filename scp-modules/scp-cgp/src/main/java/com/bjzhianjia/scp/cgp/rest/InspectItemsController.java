package com.bjzhianjia.scp.cgp.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.InspectItemsBiz;
import com.bjzhianjia.scp.cgp.entity.InspectItems;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.InspectItemsService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("inspectItems")
@CheckClientToken
@CheckUserToken
@Api(tags = "巡查事项管理")
public class InspectItemsController extends BaseController<InspectItemsBiz,InspectItems,Integer> {

	@Autowired
	private InspectItemsBiz inspectItemsBiz;
	
	@Autowired
	private InspectItemsService inspectItemsBizService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public TableResultResponse<InspectItems> page(@RequestParam(defaultValue = "10") @ApiParam(name="页容量") int limit
    				,@RequestParam(defaultValue = "1") @ApiParam(name="当前页") int page
    				,@RequestParam(defaultValue = "") @ApiParam(name="巡查事项编号") String code
    				,@RequestParam(defaultValue = "") @ApiParam(name="巡查事项名称") String name
    				,@RequestParam(defaultValue = "") @ApiParam(name="业务条线") String bizType
    				,@RequestParam(value="eventType",defaultValue = "") @ApiParam(name="事件类别") String eventType
	) {
	    
		InspectItems inspectItems = new InspectItems();
		inspectItems.setCode(code);
		inspectItems.setName(name);
		inspectItems.setBizType(bizType);
		inspectItems.setType(eventType);
	    return inspectItemsBizService.getList(page, limit, inspectItems);
	    
    }
	
	@RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<JSONObject> getInspectItems(@PathVariable @ApiParam(name="待查询对象ID") Integer id){
		return inspectItemsBizService.getInspectItems(id);
    }
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<InspectItems> add(@RequestBody @Validated @ApiParam(name="待添加对象实例") InspectItems inspectItems, BindingResult bindingResult){
		ObjectRestResponse<InspectItems> restResult = new ObjectRestResponse<>();
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = inspectItemsBizService.createInspectItems(inspectItems);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(inspectItems);
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<InspectItems> update(@RequestBody @Validated @ApiParam(name="待更新对象实例") InspectItems inspectItems, BindingResult bindingResult){
    	
		ObjectRestResponse<InspectItems> restResult = new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = inspectItemsBizService.updateInspectItems(inspectItems);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(inspectItems);
    }
	
	@RequestMapping(value = "/remove/{ids}",method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation("批量移除对象")
    public ObjectRestResponse<InspectItems> remove(@PathVariable @ApiParam(name="待删除对象ID集合，多个ID用“，”隔开") Integer[] ids){
		ObjectRestResponse<InspectItems> result = new ObjectRestResponse<>();
		if(ids == null || ids.length == 0) {
			result.setStatus(400);;
			result.setMessage("请选择要删除的项");
			return result;
		}
		inspectItemsBiz.deleteByIds(ids);
        return result;
    }
}