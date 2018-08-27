package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.SuperviseRecordBiz;
import com.bjzhianjia.scp.cgp.entity.SuperviseRecord;

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
 * 督办前台控制器
 * @author admin
 *
 */
@RestController
@RequestMapping("superviseRecord")
@CheckClientToken
@CheckUserToken
public class SuperviseRecordController extends BaseController<SuperviseRecordBiz,SuperviseRecord,Integer> {
	@Autowired
	private SuperviseRecordBiz superviseRecordBiz; 
	
	
	@RequestMapping(value="/add" ,method = RequestMethod.POST)
	@ApiOperation("添加督办")
	public ObjectRestResponse<SuperviseRecord> add(
			@RequestBody @Validated @ApiParam(name = "待添加督办实例") SuperviseRecord superviseRecord,
			BindingResult bindingResult){
		
		ObjectRestResponse<SuperviseRecord> restResult = new ObjectRestResponse<>();
		
		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		String content = superviseRecord.getContent().trim();
		String title = superviseRecord.getTitle().trim();
		superviseRecord.setContent(content);
		superviseRecord.setTitle(title);
		try {
			superviseRecordBiz.createSuperviseRecord(superviseRecord);
		}catch (Exception e) {
			restResult.setStatus(400);
			restResult.setMessage("内部异常！");
			return restResult;
		}
		restResult.setData(superviseRecord);
		
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
	public TableResultResponse<SuperviseRecord> page(
			@RequestParam(defaultValue="10") @ApiParam(name="页容量") Integer limit,
			@RequestParam(defaultValue="1") @ApiParam(name="当前页") Integer page,
			@PathVariable(value="id") @ApiParam(name="立案单id") Integer caseInfoId){
		return superviseRecordBiz.getList(page, limit,caseInfoId);
	}
}