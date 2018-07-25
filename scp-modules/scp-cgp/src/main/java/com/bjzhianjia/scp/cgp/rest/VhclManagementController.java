package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.bjzhianjia.scp.cgp.biz.VhclManagementBiz;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.VhclManagement;
import com.bjzhianjia.scp.cgp.service.VhclManagementService;

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
@RequestMapping("vhclManagement")
@CheckClientToken
@CheckUserToken
@Api(tags = "车辆管理")
public class VhclManagementController extends BaseController<VhclManagementBiz,VhclManagement,Integer> {

	@Autowired
	private VhclManagementBiz vhclManagementBiz;
	
	@Autowired
	private VhclManagementService vhclService;
	
	@RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")
    public TableResultResponse<VhclManagement> page(@RequestParam(defaultValue = "10") int limit
    				,@RequestParam(defaultValue = "1") int page
    				,@RequestParam(defaultValue = "") String vehicleNum
    				,@RequestParam(defaultValue = "") String vehicleType
    				,@RequestParam(defaultValue = "") String department) {

		VhclManagement vhcl = new VhclManagement();
		vhcl.setVehicleNum(vehicleNum);
		vhcl.setVehicleType(vehicleType);
		vhcl.setDepartment(department);
	    return vhclService.getList(page, limit, vhcl);
    }
	
	@RequestMapping(value = "/get/{id}",method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<VhclManagement> get(@PathVariable Integer id){
        ObjectRestResponse<VhclManagement> entityObjectRestResponse = new ObjectRestResponse<>();
        VhclManagement o = vhclService.get(id);
        VhclManagement vhcl = (VhclManagement)o;
        if(vhcl != null && vhcl.getIsDeleted().equals("1")) {
        	entityObjectRestResponse.data(null);
        } else {
        	entityObjectRestResponse.data(vhcl);
        }
        
        return entityObjectRestResponse;
    }
	
	@RequestMapping(value = "/add",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<VhclManagement> add(@RequestBody @Validated VhclManagement vhcl, BindingResult bindingResult){
		
		ObjectRestResponse<VhclManagement> restResult = new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = vhclService.createVhclManagement(vhcl);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
        return restResult.data(vhcl);
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<VhclManagement> update(@RequestBody @Validated VhclManagement vhcl, BindingResult bindingResult) {
    	
    	ObjectRestResponse<VhclManagement> restResult = new ObjectRestResponse<>();
    	
    	if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
    	
    	Result<Void> result = vhclService.updateVhclManagement(vhcl);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		
        return restResult.data(vhcl);
    }
	
	@RequestMapping(value = "/remove/{ids}",method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation("批量移除对象")
    public ObjectRestResponse<VhclManagement> remove(@PathVariable Integer[] ids){
		ObjectRestResponse<VhclManagement> result = new ObjectRestResponse<VhclManagement>();
		if(ids == null || ids.length == 0) {
			result.setStatus(400);;
			result.setMessage("请选择要删除的项");
			return result;
		}
		vhclManagementBiz.deleteByIds(ids);
        return result;
    }
}