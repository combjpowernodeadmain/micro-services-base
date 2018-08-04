package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.EventTypeService;

import org.springframework.web.bind.annotation.RestController;

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

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("eventType")
@CheckClientToken
@CheckUserToken
@Api(tags = "事件类型管理")
public class EventTypeController extends BaseController<EventTypeBiz,EventType,Integer> {

	@Autowired
	private EventTypeBiz eventTypeBiz;
	
	@Autowired
	private EventTypeService eventTypeService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public TableResultResponse<EventType> page(@RequestParam(defaultValue = "10") @ApiParam(name="页容量") int limit
    				,@RequestParam(defaultValue = "1") @ApiParam(name="当前页") int page
    				,@RequestParam(defaultValue = "") @ApiParam(name="事件类别名称") String typeName
    				,@RequestParam(defaultValue = "") @ApiParam(name="所属业务条线") String bizType
    				,@RequestParam(defaultValue = "") @ApiParam(name="是否可用") String isEnable) {
	    
		EventType eventType = new EventType();
		eventType.setTypeName(typeName);
		eventType.setBizType(bizType);
		eventType.setIsEnable(isEnable);
	    return eventTypeService.getList(page, limit, eventType);
	    
    }
	
	@RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<EventType> get(@PathVariable @ApiParam(name="待查询事件类别ID") Integer id){
        ObjectRestResponse<EventType> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = eventTypeBiz.selectById(id);
        EventType eventType = (EventType)o;
        if(eventType != null && eventType.getIsDeleted().equals("1")) {
        	entityObjectRestResponse.data(null);
        } else {
        	entityObjectRestResponse.data(eventType);
        }
        
        return entityObjectRestResponse;
    }
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<EventType> add(@RequestBody @Validated @ApiParam(name="待添加对象实例") EventType eventType, BindingResult bindingResult){
		ObjectRestResponse<EventType> restResult = new ObjectRestResponse<>();
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = eventTypeService.createEventType(eventType);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(eventType);
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<EventType> update(@RequestBody @Validated @ApiParam(name="待更新对象实例") EventType eventType, BindingResult bindingResult){
    	
		ObjectRestResponse<EventType> restResult = new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = eventTypeService.updateEventType(eventType);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(eventType);
    }
	
	@RequestMapping(value = "/remove/{ids}",method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation("批量移除对象")
    public ObjectRestResponse<EventType> remove(@PathVariable @ApiParam(name="待删除对象ID集合，多个ID用“，”隔开") Integer[] ids){
		ObjectRestResponse<EventType> result = new ObjectRestResponse<>();
		if(ids == null || ids.length == 0) {
			result.setStatus(400);;
			result.setMessage("请选择要删除的项");
			return result;
		}
		eventTypeBiz.deleteByIds(ids);
        return result;
    }

	@RequestMapping(value="list/bizType/{bizTypeId}",method=RequestMethod.GET)
	@ApiOperation("按业务条线加载事件类别")
	public List<EventType> getByBizType(@PathVariable(value="bizTypeId") @ApiParam(name="业务条线ID") String bizType){
		List<EventType> eventTypes = eventTypeBiz.getByBizType(bizType);
		return eventTypes;
	}
}