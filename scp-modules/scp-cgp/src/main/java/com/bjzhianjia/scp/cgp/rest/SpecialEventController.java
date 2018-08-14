package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.SpecialEventBiz;
import com.bjzhianjia.scp.cgp.entity.SpecialEvent;
import com.bjzhianjia.scp.cgp.service.SpecialEventService;
import com.bjzhianjia.scp.cgp.vo.SpecialEventVo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("specialEvent")
@CheckClientToken
@CheckUserToken
@Api(tags ="专项管理")
public class SpecialEventController extends BaseController<SpecialEventBiz, SpecialEvent, Integer> {
	@Autowired
	private SpecialEventService specialEventService;
	@Autowired
	private SpecialEventBiz specialEventBiz;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation("添加单个对象")
	public ObjectRestResponse<SpecialEventVo> add(
			@RequestBody @Validated @ApiParam(name = "待添加对象实例") SpecialEventVo specialEventVo,
			BindingResult bindingResult) {
		ObjectRestResponse<SpecialEventVo> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		return specialEventService.createSpecialEvent(specialEventVo);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@ApiOperation("更新单个对象")
	public ObjectRestResponse<SpecialEventVo> update(
			@RequestBody @Validated @ApiParam(name = "待更新对象实例") SpecialEventVo specialEventVo,
			BindingResult bindingResult) {
		ObjectRestResponse<SpecialEventVo> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			restResult.setStatus(400);
			return restResult;
		}
		restResult= specialEventService.update(specialEventVo);
		return restResult;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation("按分页获取对象")
	public TableResultResponse<SpecialEventVo> list(
			@RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
			@RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
			@RequestParam(defaultValue = "") String speCode, @RequestParam(defaultValue = "") String speName,
			@RequestParam(defaultValue = "") String publisher, @RequestParam(defaultValue = "") String speStatus,
			@RequestParam(defaultValue = "") String bizType) {
		SpecialEventVo vo = new SpecialEventVo();
		vo.setSpeCode(speCode);
		vo.setSpeName(speName);
		vo.setPublisher(publisher);
		vo.setSpeStatus(speStatus);
		vo.setBizList(bizType);

		return specialEventService.getList(vo, page, limit);
	}
	
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	@ApiOperation("获取单个对象")
	public ObjectRestResponse<SpecialEventVo> getOne(@PathVariable("id") Integer id){
		SpecialEventVo one = specialEventService.getOne(id);
		
		return new ObjectRestResponse<SpecialEventVo>().data(one);
	}
	
	@RequestMapping(value="/remove/{ids}",method=RequestMethod.DELETE)
	@ApiOperation("批量删除对象")
	public ObjectRestResponse<SpecialEvent> remove(@PathVariable(value="ids") @ApiParam(name="待删除对象ID集合，多个ID用“，”隔开") Integer[] ids){
		ObjectRestResponse<SpecialEvent> result=new ObjectRestResponse<>();
		
		if(ids==null || ids.length==0) {
			result.setStatus(400);;
			result.setMessage("请选择要删除的项");
			return result;
		}
		
		specialEventBiz.remove(ids);
		
		result.setStatus(200);;
		result.setMessage("成功");
		return result;
	}
	
	@RequestMapping(value="/remove/one/{id}",method=RequestMethod.DELETE)
	@ApiOperation("删除单个对象")
	public ObjectRestResponse<SpecialEvent> remove(@PathVariable(value="id") @ApiParam(name="待删除对象ID") Integer id){
		ObjectRestResponse<SpecialEvent> result=new ObjectRestResponse<>();
		Integer[] ids=new Integer[1];
		ids[0]=id;
		specialEventBiz.remove(ids);
		
		result.setStatus(200);;
		result.setMessage("成功");
		return result;
	}
}