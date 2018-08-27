package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.UrgeRecordBiz;
import com.bjzhianjia.scp.cgp.entity.UrgeRecord;

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

/**
 * 催办前台控制器
 * @author chenshuai
 *
 */
@RestController
@RequestMapping("urgeRecord")
@CheckClientToken
@CheckUserToken
public class UrgeRecordController extends BaseController<UrgeRecordBiz,UrgeRecord,Integer> {
	
	@Autowired
	private UrgeRecordBiz urgeRecordBiz; 
	
	
	@RequestMapping(value="/add" ,method = RequestMethod.POST)
	@ApiOperation("添加催办")
	public ObjectRestResponse<UrgeRecord> add(
			@RequestBody @Validated @ApiParam(name = "待添加催办实例") UrgeRecord urgeRecord,
			BindingResult bindingResult){
		
		ObjectRestResponse<UrgeRecord> restResult = new ObjectRestResponse<>();
		
		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		String content = urgeRecord.getContent().trim();
		String title = urgeRecord.getTitle().trim();
		urgeRecord.setContent(content);
		urgeRecord.setTitle(title);
		try {
			urgeRecordBiz.createUrgeRecord(urgeRecord);
		}catch (Exception e) {
			restResult.setStatus(400);
			restResult.setMessage("内部异常！");
			return restResult;
		}
		restResult.setData(urgeRecord);
		
		return restResult;
	}
	
	/**
	 * 通过立案单id，翻页查询
	 * 
	 * @param page       页码
	 * @param limit      页容量
	 * @param caseInfoId 立案单id
	 */
	@RequestMapping(value="/list/{id}",method= RequestMethod.GET)
	@ApiOperation("翻页查询")
	public TableResultResponse<UrgeRecord> page(
			@RequestParam(defaultValue="10") @ApiParam(name="页容量") Integer limit,
			@RequestParam(defaultValue="1") @ApiParam(name="当前页") Integer page,
			@PathVariable(value="id") @ApiParam(name="立案单id") Integer caseInfoId){
		return urgeRecordBiz.getList(page, limit,caseInfoId);
	}
	
}