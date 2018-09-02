package com.bjzhianjia.scp.cgp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.WritsProcessBindBiz;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsProcessBind;
import com.bjzhianjia.scp.cgp.vo.WritsProcessBindVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("writsProcessBind")
@CheckClientToken
@CheckUserToken
@Api(tags = "综合执法 - 案件登记 文书与流程绑定")
public class WritsProcessBindController extends BaseController<WritsProcessBindBiz, WritsProcessBind, Integer> {
	@Autowired
	private WritsProcessBindBiz writsProcessBindBiz;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation("添加单个对象")
	public ObjectRestResponse<WritsProcessBind> add(
			@RequestBody @ApiParam(name = "待添加对象实例") @Validated WritsProcessBind writsProcessBind,
			BindingResult bindingResult) {
		ObjectRestResponse<WritsProcessBind> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<Void> result = writsProcessBindBiz.createWritsProcessBind(writsProcessBind);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		restResult.setMessage("成功");
		return restResult;
	}
	@RequestMapping(value = "/add/name", method = RequestMethod.POST)
	@ApiOperation("添加单个对象")
	public ObjectRestResponse<WritsProcessBind> addByTemplateName(
			@RequestParam("writsTemplateName") @ApiParam(name="模板名称") String writsTemplateName,
			@ModelAttribute @ApiParam(name = "待添加对象实例") @Validated WritsProcessBind writsProcessBind,
			BindingResult bindingResult) {
		ObjectRestResponse<WritsProcessBind> restResult = new ObjectRestResponse<>();
		
		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result = writsProcessBindBiz.createWritsProcessBind(writsProcessBind,writsTemplateName);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		
		restResult.setMessage("成功");
		return restResult;
	}

	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	@ApiOperation("更新单个对象")
	public ObjectRestResponse<WritsProcessBind> update(
			@RequestBody @ApiParam(name = "封装有查询条件的实体类") @Validated WritsProcessBind writsProcessBind,
			BindingResult bindingResult) {
		ObjectRestResponse<WritsProcessBind> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}
		
		Result<Void> result=writsProcessBindBiz.update(writsProcessBind);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		
		restResult.setMessage("成功");
		return restResult;
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation("分页查询列表")
	public TableResultResponse<WritsProcessBindVo> getList(
			@RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page,
			@RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit,
			@RequestBody @ApiParam(name = "封装有查询条件的实体类") @Validated WritsProcessBind writsProcessBind,
			BindingResult bindingResult) {
		TableResultResponse<WritsProcessBindVo> restResult = new TableResultResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		TableResultResponse<WritsProcessBindVo> list = writsProcessBindBiz.getList(writsProcessBind, page, limit);

		return list;
	}
	@RequestMapping(value = "/list/currentNode", method = RequestMethod.GET)
	@ApiOperation("分页查询列表")
	public TableResultResponse<WritsProcessBindVo> getListOfCurrentNode(
			@RequestParam(value = "page", defaultValue = "1") @ApiParam(name = "当前页") Integer page,
			@RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit,
			@RequestParam(value="processDefId",defaultValue="") @ApiParam(name="流程定义主键")String processDefId,
			@RequestParam(value="processNodeId",defaultValue="") @ApiParam(name="流程定义主键")String processNodeId) {
		WritsProcessBind writsProcessBind=new WritsProcessBind();
		writsProcessBind.setProcessDefId(processDefId);
		writsProcessBind.setProcessNodeId(processNodeId);
		
		TableResultResponse<WritsProcessBindVo> list = writsProcessBindBiz.getListOfCurrentNode(writsProcessBind, page, limit);
		
		return list;
	}
}