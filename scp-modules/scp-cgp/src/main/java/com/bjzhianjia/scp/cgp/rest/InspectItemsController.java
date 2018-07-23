package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
    public TableResultResponse<InspectItems> page(@RequestParam(defaultValue = "10") int limit
    				,@RequestParam(defaultValue = "1") int page
    				,@RequestParam(defaultValue = "") String code
    				,@RequestParam(defaultValue = "") String name
    				,@RequestParam(defaultValue = "") String bizType) {
	    
		InspectItems inspectItems = new InspectItems();
		inspectItems.setCode(code);
		inspectItems.setName(name);
		inspectItems.setBizType(bizType);
	    return inspectItemsBizService.getList(page, limit, inspectItems);
	    
    }
	
	@RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<InspectItems> get(@PathVariable Integer id){
        ObjectRestResponse<InspectItems> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = inspectItemsBiz.selectById(id);
        InspectItems inspectItems = (InspectItems)o;
       
        entityObjectRestResponse.data(inspectItems);
        
        return entityObjectRestResponse;
    }
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<InspectItems> add(@RequestBody @Validated InspectItems inspectItems, BindingResult bindingResult){
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
    public ObjectRestResponse<InspectItems> update(@RequestBody @Validated InspectItems inspectItems, BindingResult bindingResult){
    	
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
    public ObjectRestResponse<InspectItems> remove(@PathVariable Integer[] ids){
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