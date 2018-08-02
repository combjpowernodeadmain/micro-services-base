package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import io.swagger.annotations.ApiOperation;

import com.bjzhianjia.scp.cgp.biz.AnnouncementInfoBiz;
import com.bjzhianjia.scp.cgp.entity.AnnouncementInfo;

import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
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

import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

@RestController
@RequestMapping("announcementInfo")
@CheckClientToken
@CheckUserToken
public class AnnouncementInfoController extends BaseController<AnnouncementInfoBiz,AnnouncementInfo,Integer> {

	@Autowired
	private AnnouncementInfoBiz announcementInfoBiz;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public TableResultResponse<AnnouncementInfo> page(@RequestParam(defaultValue = "10") int limit
    				,@RequestParam(defaultValue = "1") int page
    				,@RequestParam(defaultValue = "") String title
    				,@RequestParam(defaultValue = "") String status
    				,@RequestParam(defaultValue = "") String isStick
    				,@RequestParam(defaultValue = "") String startDate
    				,@RequestParam(defaultValue = "") String endDate) {
	    
		Map<String, String> announcementInfo = new HashMap<>();
		announcementInfo.put("title", title);
		announcementInfo.put("status", status);
		announcementInfo.put("isStick", isStick);
		announcementInfo.put("startDate", startDate);	
		announcementInfo.put("endDate", endDate);
	    return announcementInfoBiz.getList(page, limit, announcementInfo);
	    
    }
	
	@RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<AnnouncementInfo> get(@PathVariable Integer id){
        ObjectRestResponse<AnnouncementInfo> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = announcementInfoBiz.selectById(id);
        AnnouncementInfo announcementInfo = (AnnouncementInfo)o;
       
        entityObjectRestResponse.data(announcementInfo);
        
        return entityObjectRestResponse;
    }
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<AnnouncementInfo> add(@RequestBody @Validated AnnouncementInfo announcementInfo, BindingResult bindingResult){
		ObjectRestResponse<AnnouncementInfo> restResult = new ObjectRestResponse<>();
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		announcementInfoBiz.insertSelective(announcementInfo);
		
        return restResult.data(announcementInfo);
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<AnnouncementInfo> update(@RequestBody @Validated AnnouncementInfo announcementInfo, BindingResult bindingResult){
    	
		ObjectRestResponse<AnnouncementInfo> restResult = new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		announcementInfoBiz.updateSelectiveById(announcementInfo);
		
        return restResult.data(announcementInfo);
    }
	
	@RequestMapping(value = "/remove/{ids}",method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation("批量移除对象")
    public ObjectRestResponse<AnnouncementInfo> remove(@PathVariable Integer[] ids){
		ObjectRestResponse<AnnouncementInfo> result = new ObjectRestResponse<>();
		if(ids == null || ids.length == 0) {
			result.setStatus(400);;
			result.setMessage("请选择要删除的项");
			return result;
		}
		announcementInfoBiz.deleteByIds(ids);
        return result;
    }
}