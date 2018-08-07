package com.bjzhianjia.scp.cgp.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.AreaGridService;
import com.bjzhianjia.scp.cgp.vo.AreaGridTree;
import com.bjzhianjia.scp.cgp.vo.AreaGridVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.common.util.TreeUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("areaGrid")
@CheckClientToken
@CheckUserToken
@Api(tags = "网格管理")
public class AreaGridController extends BaseController<AreaGridBiz, AreaGrid, Integer> {
	@Autowired
	private AreaGridService areaGridService;

	/**
	 * 添加单个对象
	 * 
	 * @author 尚
	 * @param areaGrid
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation("新增单个对象")
	public ObjectRestResponse<JSONObject> add(@RequestBody @Validated JSONObject areaGridJObject, BindingResult bindingResult) {
		
		ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<Void> result = areaGridService.createAreaGrid(areaGridJObject);

		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		return restResult.data(areaGridJObject);
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@ApiOperation("更新单个对象")
	public ObjectRestResponse<JSONObject> update(@RequestBody @Validated JSONObject areaGridJObject, BindingResult bindingResult) {
		ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<Void> result = areaGridService.updateAreaGrid(areaGridJObject);

		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		return restResult.data(areaGridJObject);
	}

	@RequestMapping(value = "/remove/{id}", method = RequestMethod.PUT)
	@ApiOperation("删除单个对象")
	public ObjectRestResponse<AreaGrid> removeOne(@PathVariable(value="id") @ApiParam(name="待删除对象ID") Integer id) {
		ObjectRestResponse<AreaGrid> restResult = new ObjectRestResponse<>();
		Result<Void> result = areaGridService.removeOne(id);
		
		if(!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
//		AreaGrid areaGrid = new AreaGrid();
//		areaGrid.setId(id);
//		areaGrid.setIsDeleted("1");
//		this.baseBiz.updateSelectiveById(areaGrid);
		restResult.setStatus(200);
		restResult.setMessage("成功");
		return restResult;
	}

	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ApiOperation("分页获取网格列表")
	public TableResultResponse<AreaGridVo> list(@RequestParam(defaultValue = "10") int limit,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "") String gridName,
			@RequestParam(defaultValue = "") String gridLevel) {
		AreaGrid areaGrid=new AreaGrid();
		areaGrid.setGridName(gridName);
		areaGrid.setGridLevel(gridLevel);
		TableResultResponse<AreaGridVo> list = areaGridService.getList(page, limit, areaGrid);
		return list;
	}
	
	@RequestMapping(value="/list/level/{levels}",method=RequestMethod.GET)
	@ApiOperation("按网格等级获取网格列表")
	public List<AreaGrid> getByGridLevel(@PathVariable(value="levels") @ApiParam(name="网络等级ID") String gridLevel){
		List<AreaGrid> areaGridList = areaGridService.getByGridLevel(gridLevel);
		return areaGridList;
	}
	
	@RequestMapping(value="/get/{id}",method=RequestMethod.GET)
	@ApiOperation("查询 单个对象")
	public ObjectRestResponse<JSONObject> getOne(@PathVariable(value="id") @ApiParam(name="待查询网格ID") Integer id){
		return areaGridService.getOne(id);
	}
	
	@RequestMapping(value="/tree",method=RequestMethod.GET)
	@ApiOperation("获取网格树")
	public List<AreaGridTree> getTree(){
		TableResultResponse<AreaGridVo> list = areaGridService.getList(1, 2147483647, new AreaGrid());
		List<AreaGridVo> all = list.getData().getRows();
		
		List<AreaGridTree> trees=new ArrayList<>();
		
		all.forEach(o->{
			trees.add(new AreaGridTree(o.getId(), o.getGridParent(), o.getGridName(), o.getGridCode()));
		});
		
		return TreeUtil.bulid(trees, -1, null);
	}
}