package com.bjzhianjia.scp.cgp.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.PublicOpinionBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.PublicOpinion;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.service.PublicOpinionService;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.vo.PublicOpinionVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("publicOpinion")
@CheckClientToken
@CheckUserToken
@Api(tags = "舆情")
public class PublicOpinionController extends BaseController<PublicOpinionBiz, PublicOpinion, Integer> {
	@Autowired
	private PublicOpinionService publicOpinionService;
	@Autowired
	private PublicOpinionBiz publicOpinionBiz;
	@Autowired
	private DictFeign dictFeign;

	@RequestMapping(value = "/add/cache", method = RequestMethod.POST)
	@ApiOperation("添加暂存")
	public ObjectRestResponse<PublicOpinion> addPublicOpinooineCache(
			@RequestBody @Validated @ApiParam(name = "待添加对象实例") PublicOpinionVo vo, BindingResult bindingResult) {
		ObjectRestResponse<PublicOpinion> restResult = new ObjectRestResponse<>();
		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		String toExeStatus = CommonUtil.exeStatusUtil(dictFeign, Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_TODO);
		
		vo.setExeStatus(toExeStatus);// 创建时处理状态为【未发起】
		Result<PublicOpinion> result = publicOpinionService.createdPublicOpinionCache(vo);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		restResult.setStatus(200);
		restResult.setMessage("成功");
		restResult.setData(result.getData());
		return restResult;
	}

	/**
	 * 如果用户在添加记录之前点击了暂存，则需要将暂存那条记录的ID传入进来<br/>
	 * 暂存记录ID用在com.bjzhianjia.scp.cgp.rest.PublicOpinionController.addPublicOpinooineCache(PublicOpinionVo,
	 * BindingResult)方法中返回到前端
	 * 
	 * @author 尚
	 * @param vo
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/add/case", method = RequestMethod.POST)
	@ApiOperation("添加单个对象")
	public ObjectRestResponse<PublicOpinionVo> add(
			@RequestBody @Validated @ApiParam(name = "待添加对象实例") PublicOpinionVo vo, BindingResult bindingResult) {
		ObjectRestResponse<PublicOpinionVo> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<Void> result = new Result<>();
		try {
			result = publicOpinionService.createdPublicOpinion(vo);
			if(!result.getIsSuccess()) {
				restResult.setMessage(result.getMessage());
				restResult.setStatus(400);
				return restResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			restResult.setStatus(400);
			restResult.setMessage(StringUtils.isBlank(result.getMessage()) ? e.getMessage() : result.getMessage());
			return restResult;
		}

		restResult.setStatus(200);
		restResult.setMessage("成功");
		return restResult;
	}

	@RequestMapping(value = "/update/cache/{id}", method = RequestMethod.PUT)
	@ApiOperation("更新缓存对象")
	public ObjectRestResponse<PublicOpinion> updateCache(
			@RequestBody @Validated @ApiParam(name = "待更新对象实例") PublicOpinion publicOpinion,
			BindingResult bindingResult) {
		ObjectRestResponse<PublicOpinion> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<PublicOpinion> result = publicOpinionService.updateCache(publicOpinion);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		restResult.setStatus(200);
		restResult.setMessage("成功");
		return restResult;
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	@ApiOperation("更新单个对象")
	public ObjectRestResponse<PublicOpinion> update(
			@RequestBody @Validated @ApiParam(name = "待更新对象实例") PublicOpinionVo vo, BindingResult bindingResult) {
		ObjectRestResponse<PublicOpinion> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<PublicOpinion> result = new Result<>();
		try {
			result = publicOpinionService.update(vo);
			if(!result.getIsSuccess()) {
				restResult.setMessage(result.getMessage());
				restResult.setStatus(400);
				return restResult;
			}
		} catch (Exception e) {
			e.printStackTrace();
			restResult.setStatus(400);
			restResult.setMessage(StringUtils.isBlank(result.getMessage()) ? e.getMessage() : result.getMessage());
			return restResult;
		}

		restResult.setStatus(200);
		restResult.setMessage("成功");
		return restResult;
	}

	/**
	 * 分页获取对象
	 * 
	 * @author 尚
	 * @param page
	 * @param limit
	 * @param opinCode
	 * @param opinLevel
	 * @param opinType
	 * @param exeStatus
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation("分页获取对象")
	public TableResultResponse<PublicOpinionVo> list(@RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
			@RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
			@RequestParam(defaultValue = "") @ApiParam(name = "舆情事件编号") String opinCode,
			@RequestParam(defaultValue = "") @ApiParam(name = "舆情等级") String opinLevel,
			@RequestParam(defaultValue = "") @ApiParam(name = "来源类型") String opinType,
			@RequestParam(defaultValue = "") @ApiParam(name = "处理状态") String exeStatus,
			@RequestParam(defaultValue = "") @ApiParam(name = "按时间查询(起始时间)") String startTime,
			@RequestParam(defaultValue = "") @ApiParam(name = "按时间查询 (终止时间)") String endTime) {
		PublicOpinion publicOpinion = new PublicOpinion();
		publicOpinion.setOpinCode(opinCode);
		publicOpinion.setOpinLevel(opinLevel);
		publicOpinion.setOpinType(opinType);
		publicOpinion.setExeStatus(exeStatus);

		TableResultResponse<PublicOpinionVo> list = publicOpinionService.getList(publicOpinion, page, limit, startTime,
				endTime);
		return list;
	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ApiOperation("查询单个对象")
	public ObjectRestResponse<PublicOpinionVo> getOne(
			@PathVariable(value = "id") @ApiParam(name = "待查询对象ID") Integer id) {
		ObjectRestResponse<PublicOpinionVo> one = publicOpinionService.getOne(id);
		one.setStatus(200);
		one.setMessage("成功");
		return one;
	}

	/**
	 * 该方法查询发起的单个对象，用于修改某记录时的前置查询条件
	 * @author 尚
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/get/toDo/{id}", method = RequestMethod.GET)
	@ApiOperation("查询未发起对象，用于修改某记录时的前置查询条件")
	public ObjectRestResponse<PublicOpinionVo> getToDo(
			@PathVariable(value = "id") @ApiParam(name = "待查询对象ID") Integer id) {
		ObjectRestResponse<PublicOpinionVo> result = publicOpinionService.getOne(id);
		PublicOpinionVo data = result.getData();
		
		String toExeStatus = CommonUtil.exeStatusUtil(dictFeign, Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_TODO);
		
		if (!data.getExeStatus().equals(toExeStatus)) {
			result.setStatus(400);
			result.setMessage("当前记录不能修改，只有未发起的热线记录可修改！");
			result.setData(null);
			return result;
		}

		result.setStatus(200);
		return result;
	}

	@RequestMapping(value = "/remove/{ids}", method = RequestMethod.DELETE)
	@ApiOperation("批量删除对象")
	public ObjectRestResponse<PublicOpinion> remove(
			@PathVariable("ids") @ApiParam(name = "待删除对象ID集合，多个ID用“，”隔开") Integer[] ids) {
		ObjectRestResponse<PublicOpinion> restResult = new ObjectRestResponse<>();
		if (ids == null || ids.length == 0) {
			restResult.setStatus(400);
			restResult.setMessage("请选择要删除的项");
			return restResult;
		}

		Result<Void> result = publicOpinionBiz.remove(ids);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}
		restResult.setStatus(200);
		restResult.setMessage("成功");
		return restResult;
	}
}