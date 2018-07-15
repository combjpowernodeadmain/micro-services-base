package com.bjzhianjia.scp.cgp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.RightsIssuesBiz;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.cgp.service.RightsIssuesService;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.github.pagehelper.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("rightsIssues")
@CheckClientToken
@CheckUserToken
@Api(tags = "权利事项管理")
public class RightsIssuesController extends BaseController<RightsIssuesBiz,RightsIssues,Integer> {

	@Autowired
	private RightsIssuesBiz rightsIssuesBiz;
	
	@Autowired
	private RightsIssuesService rightsIssuesService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public TableResultResponse<RightsIssues> page(@RequestParam(defaultValue = "10") int limit
    				,@RequestParam(defaultValue = "1") int page
    				,@RequestParam(defaultValue = "") String code
    				,@RequestParam(defaultValue = "") String bizType
    				,@RequestParam(defaultValue = "") String unlawfulAct 
    				,@RequestParam(defaultValue = "") String isEnable) {
	    
		RightsIssues rightsIssues = new RightsIssues();
		rightsIssues.setCode(code);
		rightsIssues.setBizType(bizType);
		rightsIssues.setUnlawfulAct(unlawfulAct);
		rightsIssues.setIsEnable(isEnable);
	    return rightsIssuesService.getList(page, limit, rightsIssues);
	    
    }
	
	@RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<RightsIssues> get(@PathVariable Integer id){
        ObjectRestResponse<RightsIssues> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = rightsIssuesBiz.selectById(id);
        RightsIssues rightsIssues = (RightsIssues)o;
       
        entityObjectRestResponse.data(rightsIssues);
        
        return entityObjectRestResponse;
    }
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<RightsIssues> add(@RequestBody @Validated RightsIssues rightsIssues, BindingResult bindingResult){
		ObjectRestResponse<RightsIssues> restResult = new ObjectRestResponse<>();
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = rightsIssuesService.createRightsIssues(rightsIssues);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(rightsIssues);
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<RightsIssues> update(@RequestBody @Validated RightsIssues rightsIssues, BindingResult bindingResult){
    	
		ObjectRestResponse<RightsIssues> restResult = new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = rightsIssuesService.updateRightsIssues(rightsIssues);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(rightsIssues);
    }
	
	@RequestMapping(value = "/remove/{ids}",method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation("批量移除对象")
    public ObjectRestResponse<RightsIssues> remove(@PathVariable String ids){
		ObjectRestResponse<RightsIssues> result = new ObjectRestResponse<>();
		if(StringUtil.isEmpty(ids)) {
			result.setStatus(400);;
			result.setMessage("请选择要删除的项");
			return result;
		}
		rightsIssuesBiz.deleteByIds(ids);
        return result;
    }
}
