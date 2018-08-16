package com.bjzhianjia.scp.cgp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.RegulaObjectService;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.RegulaObjectVo;
import com.bjzhianjia.scp.cgp.vo.Regula_EnterPriseVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("regulaObject")
@CheckClientToken
@CheckUserToken
@Api(tags = "监管对象管理")
public class RegulaObjectController extends BaseController<RegulaObjectBiz, RegulaObject, Integer> {
	@Autowired
	private RegulaObjectService regulaObjectService;

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation("新增单个对象")
	public ObjectRestResponse<JSONObject> add(@RequestBody @Validated Regula_EnterPriseVo vo,
			BindingResult bindingResult) {
		RegulaObject regulaObject = BeanUtil.copyBean_New(vo, new RegulaObject());
		EnterpriseInfo enterpriseInfo = BeanUtil.copyBean_New(vo, new EnterpriseInfo());

		ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(200);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<Void> result = regulaObjectService.createRegulaObject(regulaObject, enterpriseInfo);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		JSONObject r_JsonObject = JSONObject.parseObject(JSON.toJSONString(regulaObject));
		JSONObject e_JsonObject = JSONObject.parseObject(JSON.toJSONString(enterpriseInfo));

		return restResult.data(BeanUtil.jsonObjectMergeOther(r_JsonObject, e_JsonObject));
	}

	/**
	 * 更新单个对象，须传入两个ID<br/>
	 * "regulaObjectId":1, //监管对象ID<br/>
	 * "enterpriseId":1//企业信息ID
	 * 
	 * @author 尚
	 * @param vo
	 * @param bindingResult
	 * @return
	 */
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	@ApiOperation("更新单个对象")
	public ObjectRestResponse<JSONObject> update(
			@RequestBody @Validated @ApiParam(name = "待更新对象实例") Regula_EnterPriseVo vo, BindingResult bindingResult) {
		RegulaObject regulaObject = BeanUtil.copyBean_New(vo, new RegulaObject());
		EnterpriseInfo enterpriseInfo = BeanUtil.copyBean_New(vo, new EnterpriseInfo());

		regulaObject.setId(vo.getRegulaObjId());

		ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

		if (bindingResult.hasErrors()) {
			restResult.setStatus(400);
			restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
			return restResult;
		}

		Result<Void> result = regulaObjectService.updateRegulaObject(regulaObject, enterpriseInfo);
		if (!result.getIsSuccess()) {
			restResult.setStatus(400);
			restResult.setMessage(result.getMessage());
			return restResult;
		}

		JSONObject r_JsonObject = JSONObject.parseObject(JSON.toJSONString(regulaObject));
		JSONObject e_JsonObject = JSONObject.parseObject(JSON.toJSONString(enterpriseInfo));
		return restResult.data(BeanUtil.jsonObjectMergeOther(r_JsonObject, e_JsonObject));
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ApiOperation("分页获取对象")
	public TableResultResponse<RegulaObjectVo> page(@RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
			@RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
			@ModelAttribute @ApiParam(name = "接收查询条件的实例") RegulaObject regulaObject) {

		return regulaObjectService.getList(regulaObject, page, limit);
	}

	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
	@ApiOperation("获取单个对象")
	public Regula_EnterPriseVo getById(@PathVariable(value = "id") @ApiParam(name = "待查询对象ID") Integer id) {
		Regula_EnterPriseVo regulaObject = regulaObjectService.getById(id);
		return regulaObject;
	}

	@RequestMapping(value = "/remove/{ids}", method = RequestMethod.DELETE)
	@ApiOperation("批量删除对象")
	public ObjectRestResponse<RegulaObject> remove(
			@PathVariable(value = "ids") @ApiParam(name = "待删除对象ID数组") Integer[] ids) {
		ObjectRestResponse<RegulaObject> result = new ObjectRestResponse<>();

		if (ids == null || ids.length == 0) {
			result.setStatus(400);
			result.setMessage("请选择要删除的项");
			return result;
		}

		regulaObjectService.remove(ids);
		return result;
	}

	@RequestMapping(value = "/remove/one/{id}", method = RequestMethod.DELETE)
	@ApiOperation("删除单个对象")
	public ObjectRestResponse<RegulaObject> remove(@PathVariable(value = "id") @ApiParam(name = "待删除对象ID") Integer id) {
		ObjectRestResponse<RegulaObject> result = new ObjectRestResponse<>();

		if (id == null) {
			result.setStatus(400);
			result.setMessage("请选择要删除的项");
			return result;
		}

		Integer[] ids = new Integer[1];
		ids[0] = id;

		regulaObjectService.remove(ids);
		return result;
	}

//	@RequestMapping(value="/upload/img",method=RequestMethod.POST)
//	@ApiOperation("上传图片")
//	public static List<String> uploadFileList(@RequestParam("files") @ApiParam(name="接收图片数组对象")MultipartFile files[], HttpServletRequest request)
//			throws IllegalStateException, IOException{
//		List<String> newFilePathList = new ArrayList<>();
//		try {
//			for (MultipartFile multipartFile : files) {
//				// 文件的原始名称
//				String originalFilename = multipartFile.getOriginalFilename();
//				String newFileName = null;
//				if (multipartFile != null && originalFilename != null && originalFilename.length() > 0) {
//
//					newFileName = UUID.randomUUID() + originalFilename;
//					// 存储图片的物理路径
//					String pic_path = Constances.RegulaObjImg.IMG_URL+newFileName;
//					// 新图片路径
//					File targetFile = new File(pic_path);
//					// 内存数据读入磁盘
//					multipartFile.transferTo(targetFile);
//					newFilePathList.add(pic_path);
//				}
//			}
//
//		} catch (IOException e) {
//			throw new IOException("上传图片异常");
//		}
//		return newFilePathList;
//	}
}