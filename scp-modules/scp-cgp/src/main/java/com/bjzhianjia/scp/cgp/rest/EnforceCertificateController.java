package com.bjzhianjia.scp.cgp.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.EnforceCertificateBiz;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.EnforceCertificateService;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * EnforceCertificateController 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月16日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
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
    public TableResultResponse<JSONObject> page(@RequestParam(defaultValue = "10") int limit
    				,@RequestParam(defaultValue = "1") int page
    				,@RequestParam(defaultValue = "") String certCode
    				,@RequestParam(defaultValue = "") String holderName
    				,@RequestParam(defaultValue = "") String deptId
    				,@RequestParam(defaultValue = "") String gridMemberName
    				) {
	    
		EnforceCertificate enforceCertificate = new EnforceCertificate();
		enforceCertificate.setCertCode(certCode);

		if (StringUtils.isNotBlank(gridMemberName) && StringUtils.isBlank(holderName)) {
			holderName = gridMemberName;
		}
		enforceCertificate.setHolderName(holderName);
	    return enforceCertificateService.getList(page, limit, enforceCertificate,deptId);
	    
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
	@ApiOperation("获取执法人员个人详情")
	public ObjectRestResponse<JSONObject> getDetailOfCertificater(@RequestParam(value="userId",defaultValue="")String userId){
        return enforceCertificateService.getDetailOfCertificater(userId);
	}
	/**
	 * 通过用户id获取执法人员详情
	 * @param userId
	 *        用户id
	 * @return
	 */
	@ApiOperation("通过用户id获取执法人员详情")
    @RequestMapping(value="/userId/{userId}")
	public ObjectRestResponse<EnforceCertificate> getEnforceCertificateByUserId(@PathVariable("userId") String userId) {
	    ObjectRestResponse<EnforceCertificate> result = new ObjectRestResponse<>();
	    
	    EnforceCertificate enforceCertificate = enforceCertificateBiz.getEnforceCertificateByUserId(userId);
	    if(enforceCertificate != null) {
	        result.setData(enforceCertificate);
	        result.setStatus(200);
	    }else {
	        result.setMessage("当前用户，没有执法证！");
            result.setStatus(400);
	    }
	    return result; 
	}


	@GetMapping("/all/potition")
	@ApiOperation("执法人员全部定位")
	public TableResultResponse<JSONObject> allPotition(){
		TableResultResponse<JSONObject> tableResult = this.baseBiz.allPosition();
		return tableResult;
	}

	/**
	 * 辅助执法人员列表
	 *
	 * @param limit
	 * @param page
	 * @param name 用户名称
	 * @return
	 */
	@RequestMapping(value = "/fuzhu/list", method = RequestMethod.GET)
	@ApiOperation("辅助执法人员列表")
	public TableResultResponse<JSONObject> getFuZhuList(
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "page", defaultValue = "1") Integer page,
			@RequestParam(value = "name", defaultValue = "") String name) {
		return enforceCertificateService.fuzhuList(page, limit, name);
	}

	/**
	 * 辅助执法人员全部定位
	 *
	 * @return
	 */
	@GetMapping("/assist/potition")
	@ApiOperation("辅助执法人员全部定位")
	public TableResultResponse<JSONObject> assistPotition() {
		TableResultResponse<JSONObject> tableResult = this.baseBiz.assistPotition();
		return tableResult;
	}
}