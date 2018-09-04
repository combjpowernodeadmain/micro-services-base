package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.EnforceCertificateBiz;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.EnforceCertificateService;

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
@RequestMapping("enforceCertificate")
@CheckClientToken
@CheckUserToken
@Api(tags = "执法证管理")
public class EnforceCertificateController extends BaseController<EnforceCertificateBiz,EnforceCertificate,Integer> {

	@Autowired
	private EnforceCertificateBiz enforceCertificateBiz;
	
	@Autowired
	private EnforceCertificateService enforceCertificateService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public TableResultResponse<EnforceCertificate> page(@RequestParam(defaultValue = "10") int limit
    				,@RequestParam(defaultValue = "1") int page
    				,@RequestParam(defaultValue = "") String certCode
    				,@RequestParam(defaultValue = "") String holderName) {
	    
		EnforceCertificate enforceCertificate = new EnforceCertificate();
		enforceCertificate.setCertCode(certCode);
		enforceCertificate.setHolderName(holderName);
	    return enforceCertificateService.getList(page, limit, enforceCertificate);
	    
    }
	
	@RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<EnforceCertificate> get(@PathVariable Integer id){
        ObjectRestResponse<EnforceCertificate> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = enforceCertificateBiz.selectById(id);
        EnforceCertificate rightsIssues = (EnforceCertificate)o;
       
        entityObjectRestResponse.data(rightsIssues);
        
        return entityObjectRestResponse;
    }
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<EnforceCertificate> add(@RequestBody @Validated EnforceCertificate enforceCertificate, BindingResult bindingResult){
		ObjectRestResponse<EnforceCertificate> restResult = new ObjectRestResponse<>();
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = enforceCertificateService.createEnforceCertificate(enforceCertificate);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(enforceCertificate);
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<EnforceCertificate> update(@RequestBody @Validated EnforceCertificate enforceCertificate, BindingResult bindingResult){
    	
		ObjectRestResponse<EnforceCertificate> restResult = new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = enforceCertificateService.updateEnforceCertificate(enforceCertificate);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(enforceCertificate);
    }
	
	@RequestMapping(value = "/remove/{ids}",method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation("批量移除对象")
    public ObjectRestResponse<EnforceCertificate> remove(@PathVariable Integer[] ids){
		ObjectRestResponse<EnforceCertificate> result = new ObjectRestResponse<>();
		if(ids == null || ids.length == 0) {
			result.setStatus(400);
			result.setMessage("请选择要删除的项");
			return result;
		}
		enforceCertificateBiz.deleteByIds(ids);
        return result;
    }
	
	@RequestMapping(value="/get/certificater")
	@ApiOperation("获取执法人员详情")
	public ObjectRestResponse<JSONObject> getDetailOfCertificater(@RequestParam(value="userId",defaultValue="")String userId){
        return enforceCertificateService.getDetailOfCertificater(userId);
	}
}