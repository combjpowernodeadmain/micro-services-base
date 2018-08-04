package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.DeptBiztypeBiz;
import com.bjzhianjia.scp.cgp.entity.DeptBiztype;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.DeptBiztypeService;
import com.bjzhianjia.scp.cgp.vo.DeptBizTypeVo;

import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("deptBiztype")
@CheckClientToken
@CheckUserToken
@Api(tags="部门业务条线配置")
public class DeptBiztypeController extends BaseController<DeptBiztypeBiz,DeptBiztype,Integer> {
	private static final Logger LOGGER=LoggerFactory.getLogger(DeptBiztypeController.class);
	
	@Autowired
	private DeptBiztypeService deptBiztypeService;
	@Autowired
	private DeptBiztypeBiz deptBiztypeBiz;
	
	@RequestMapping(value="/list",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation("分页查询业务条线记录")
	public TableResultResponse<DeptBizTypeVo> page(@RequestParam(defaultValue="1") @ApiParam(name="当前页")int page,
			@RequestParam(defaultValue="10")  @ApiParam(name="页容量") int limit,
			@RequestParam(defaultValue="") @ApiParam(name="业务条线") String bizType,
			@RequestParam(defaultValue="") @ApiParam(name="部门") String department){
		DeptBiztype deptBiztype=new DeptBiztype();
		deptBiztype.setBizType(bizType);
		deptBiztype.setDepartment(department);
		TableResultResponse<DeptBizTypeVo> result = deptBiztypeService.getList(page, limit, deptBiztype);
		return result;
	}
	
	@RequestMapping(value="/get/{id}",method = RequestMethod.GET)
	@ResponseBody
	@ApiOperation("查询单个对象")
	public TableResultResponse<DeptBizTypeVo> get(@PathVariable(value="id") @ApiParam(name="待查询对象ID")String id){
		DeptBiztype deptBiztype=new DeptBiztype();
		deptBiztype.setId(Integer.valueOf(id));
		TableResultResponse<DeptBizTypeVo> result = deptBiztypeService.get(id);
		return result;
	}
	
	@RequestMapping(value="/add",method = RequestMethod.POST)
	@ResponseBody
	@ApiOperation("新增单个对象")
	public ObjectRestResponse<DeptBiztype> add(@RequestBody @Validated @ApiParam(name="待添加对象实例") DeptBiztype deptBiztype,BindingResult bindingResult
			,HttpServletResponse response) throws IOException{
		ObjectRestResponse<DeptBiztype> restResult=new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			
			return restResult;
		}
		
		Result<Void> result=deptBiztypeService.createDeptBiztype(deptBiztype);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
//			response.sendError(400, bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		ObjectRestResponse<DeptBiztype> data = restResult.data(deptBiztype);
		return data;
	}
	
	@RequestMapping(value="/update/{id}",method=RequestMethod.PUT)
	@ResponseBody
	@ApiOperation("更新单个对象")
	public ObjectRestResponse<DeptBiztype> update(@RequestBody @Validated @ApiParam(name="待更新对象实例") DeptBiztype deptBiztype,BindingResult bindingResult){
		LOGGER.info("deptBiztype/update/"+deptBiztype.getId()+"更新部门业务条线操作");
		LOGGER.info("待更新部门："+deptBiztype.getDepartment()+",更新为业务条线："+deptBiztype.getBizType());
		
		ObjectRestResponse<DeptBiztype> restResult=new ObjectRestResponse<>();
		
		if(bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result=deptBiztypeService.updateDeptBiztype(deptBiztype);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		
		return restResult.data(deptBiztype);
	}
	
	@RequestMapping(value="/remove/{id}",method = RequestMethod.PUT)
	@ResponseBody
	@ApiOperation("删除单个对象")
	public ObjectRestResponse<DeptBiztype> remove(@RequestBody @ApiParam(name="待删除对象实例") DeptBiztype deptBiztype){
		ObjectRestResponse<DeptBiztype> restResult=new ObjectRestResponse<>();
//		System.out.println(deptBiztype);
		deptBiztype.setIsDeleted("1");
		this.deptBiztypeBiz.updateSelectiveById(deptBiztype);
		
		return restResult.data(deptBiztype);
	}
	
	@RequestMapping(value="/remove/{ids}",method = RequestMethod.DELETE)
	@ResponseBody
	@ApiOperation("批量删除对象")
	public ObjectRestResponse<DeptBiztype> removeMore(@PathVariable @ApiParam(name="待删除对象ID集合，多个ID用“，”隔开") Integer[] ids){
		ObjectRestResponse<DeptBiztype> result = new ObjectRestResponse<DeptBiztype>();
		
//		DeptBiztype deptBiztype=new DeptBiztype();
//		deptBiztype.setUpdUserName(BaseContextHandler.getUsername());
//		deptBiztype.setUpdTime(new Date());
//		deptBiztype.setUpdUserId(BaseContextHandler.getUserID());
		
		if(ids == null || ids.length == 0) {
			result.setStatus(400);
			result.setMessage("请选择要删除的项");
			return result;
		}
		deptBiztypeBiz.deleteByIds(ids);
        return result;
	}
}