package com.bjzhianjia.scp.cgp.rest;

import com.alibaba.fastjson.JSONArray;
import com.bjzhianjia.scp.cgp.entity.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.SpecialEventBiz;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.SpecialEvent;
import com.bjzhianjia.scp.cgp.service.SpecialEventService;
import com.bjzhianjia.scp.cgp.vo.SpecialEventVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
			@RequestParam(defaultValue = "") @ApiParam(name = "排序方式") String sortColumn,
			@RequestParam(defaultValue = "") @ApiParam(name = "当专项编号")String speCode, @RequestParam(defaultValue = "") String speName,
			@RequestParam(defaultValue = "") @ApiParam(name = "发布人")String publisher, @RequestParam(defaultValue = "") String speStatus,
			@RequestParam(defaultValue = "") @ApiParam(name = "涉及业务条线")String bizType) {
		SpecialEventVo vo = new SpecialEventVo();
		vo.setSpeCode(speCode);
		vo.setSpeName(speName);
		vo.setPublisher(publisher);
		vo.setSpeStatus(speStatus);
		vo.setBizList(bizType);
		vo.setSortColumn(sortColumn);
		return specialEventService.getList(vo, page, limit);
	}
	
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	@ApiOperation("获取单个对象")
	public ObjectRestResponse<SpecialEventVo> getOne(@PathVariable("id") Integer id){
		SpecialEventVo one = specialEventService.getOne(id);
		
		return new ObjectRestResponse<SpecialEventVo>().data(one);
	}

	/**
	 * 获取单个对象，并判断该对象状态是否为未发起
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/get/todo/{id}",method=RequestMethod.GET)
	@ApiOperation("获取单个对象")
	public ObjectRestResponse<SpecialEventVo> getOneToDo(@PathVariable("id") Integer id){
		ObjectRestResponse<SpecialEventVo> restResult=new ObjectRestResponse<>();

		Result<SpecialEventVo> result = specialEventService.getOneToDo(id);

		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		restResult.setMessage("成功");
		restResult.setStatus(200);
		restResult.setData(result.getData());

		return restResult;
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

    /**
     * 查询专项id下的业务条线
     * 
     * @param id
     * @return
     */
    @GetMapping("/bizType")
    @ApiOperation("查询专项id下的业务条线")
    public JSONArray bizTypeInSpeEvent(@RequestParam(value = "id") Integer id) {
        return this.specialEventService.bizTypeInSpeEvent(id);
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/eventType")
    @ApiOperation("查询专项id下，与bizList对应的事件类别")
    public TableResultResponse<EventType> eventTypeInSpeEvent(
        @RequestParam(value = "id") Integer id, @RequestParam(value = "bizList") String bizList) {
        return this.baseBiz.eventTypeInSApeEvent(id, bizList);
    }
}