package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.bjzhianjia.scp.cgp.biz.EnforceTerminalBiz;
import com.bjzhianjia.scp.cgp.entity.EnforceTerminal;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.EnforceTerminalService;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("enforceTerminal")
@CheckClientToken
@CheckUserToken
@Api(tags = "终端管理")
public class EnforceTerminalController extends BaseController<EnforceTerminalBiz,EnforceTerminal,Integer> {

	@Autowired
	private EnforceTerminalBiz enforceTerminalBiz;
	
	@Autowired
	private EnforceTerminalService terminalService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public TableResultResponse<EnforceTerminal> page(@RequestParam(defaultValue = "10") int limit
    				,@RequestParam(defaultValue = "1") int page
    				,@RequestParam(defaultValue = "") String terminalPhone
    				,@RequestParam(defaultValue = "") String retrievalDepartment
    				,@RequestParam(defaultValue = "") String retrievalUser
    				,@RequestParam(defaultValue = "") String terminalType
    				,@RequestParam(defaultValue = "") String isEnable) {
	    
		EnforceTerminal terminal = new EnforceTerminal();
		terminal.setTerminalPhone(terminalPhone);
		terminal.setRetrievalDepartment(retrievalDepartment);
		terminal.setRetrievalUser(retrievalUser);
		terminal.setTerminalType(terminalType);
		terminal.setIsEnable(isEnable);
	    return terminalService.getList(page, limit, terminal);
	    
    }
	
	@RequestMapping(value = "/map", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public Map<Integer,String> map() {

	    return enforceTerminalBiz.map();
	    
    }
	
	@RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<EnforceTerminal> get(@PathVariable Integer id){
        ObjectRestResponse<EnforceTerminal> entityObjectRestResponse = new ObjectRestResponse<>();
        Object o = baseBiz.selectById(id);
        EnforceTerminal terminal = (EnforceTerminal)o;
        if(terminal != null && terminal.getIsDeleted().equals("1")) {
        	entityObjectRestResponse.data(null);
        } else {
        	entityObjectRestResponse.data(terminal);
        }
        
        return entityObjectRestResponse;
    }
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<EnforceTerminal> add(@RequestBody @Validated EnforceTerminal terminal, BindingResult bindingResult){
		ObjectRestResponse<EnforceTerminal> restResult = new ObjectRestResponse<>();
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = terminalService.createTerminal(terminal);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(terminal);
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<EnforceTerminal> update(@RequestBody @Validated EnforceTerminal terminal, BindingResult bindingResult){
    	
		ObjectRestResponse<EnforceTerminal> restResult = new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = terminalService.updateTerminal(terminal);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(terminal);
    }
	
	@RequestMapping(value = "/remove/{ids}",method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation("批量移除对象")
    public ObjectRestResponse<EnforceTerminal> remove(@PathVariable Integer[] ids){
		ObjectRestResponse<EnforceTerminal> result = new ObjectRestResponse<EnforceTerminal>();
		if(ids == null || ids.length == 0) {
			result.setStatus(400);;
			result.setMessage("请选择要删除的项");
			return result;
		}
		enforceTerminalBiz.deleteByIds(ids);
        return result;
    }
}