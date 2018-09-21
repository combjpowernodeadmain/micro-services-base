package com.bjzhianjia.scp.cgp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.transform.TimedInterruptibleASTTransformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.RegTypeRelationBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectTypeBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.RegTypeRelation;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectTypeMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.RegulaObjectTypeVo;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

@Service
public class RegulaObjectTypeService {

	@Autowired
	private RegulaObjectTypeMapper regulaObjectTypeMapper;
	@Autowired
	private DictFeign dictFeign;
	@Autowired
	private RegulaObjectTypeBiz regulaObjectTypeBiz;
	@Autowired
	private MergeCore mergeCore;
	@Autowired
	private RegTypeRelationBiz regTypeRelationBiz;

	/**
	 * 新增监管对象类别
	 * 
	 * @author 尚
	 * @param regulaObjectType
	 * @return
	 */
	public Result<Void> createRegulaObjectType(RegulaObjectType regulaObjectType) {
		Result<Void> result = new Result<>();

		result = this.check(regulaObjectType, false);

		if (!result.getIsSuccess()) {
			return result;
		}

		regulaObjectTypeBiz.insertSelective(regulaObjectType);
		return result;
	}

	private Result<Void> check(RegulaObjectType regulaObjectType, boolean isUpdate) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		// 验证所属监管对象类型是否存在，验证当前监管对象类型的层级是否超过三级
		Integer parentObjectTypeId = regulaObjectType.getParentObjectTypeId();
		if (parentObjectTypeId != null) {
			// 父类型不能是它自己
			if (parentObjectTypeId.equals(regulaObjectType.getId())) {
				result.setMessage("所属监管对象类型不能是其自身");
				return result;
			}

			// 查询所选父类型是存在
			Map<String, Object> conditions = new HashMap<>();
			conditions.put("id", parentObjectTypeId);
			List<RegulaObjectType> list = regulaObjectTypeBiz.getByMap(conditions, false);
			RegulaObjectType parentIns = this.regulaObjectTypeBiz.selectById(parentObjectTypeId);
			if (list == null || list.isEmpty()) {
				result.setMessage("所属监管对象类型不存在");
				return result;
			}

			if (checkHierarchy(parentIns) >= 3) {
				// 说明待添加对象的低级是三级，那么本对象不能进行添加
				result.setMessage("所属监管对象类型应大于三级");
				return result;
			}

		} else {
			regulaObjectType.setParentObjectTypeId(-1);
		}

		// 验证当前监管对象类型编码的唯一性
		String objectTypeCode = regulaObjectType.getObjectTypeCode();
		if (StringUtils.isNotBlank(objectTypeCode)) {
			Map<String, Object> conditions = new HashMap<>();
			conditions.put("objectTypeCode", objectTypeCode);
			List<RegulaObjectType> list = regulaObjectTypeBiz.getByMap(conditions, false);
			if (list != null) {
				if (isUpdate) {
					for (RegulaObjectType tmp : list) {
						if (!tmp.getId().equals(regulaObjectType.getId()) && tmp.getIsDeleted().equals("0")) {
							result.setMessage("所填监管对象类型编码已存在");
							return result;
						}
					}
				} else {
					if (!(list == null || list.isEmpty())) {
						result.setMessage("所填监管对象类型编码已存在");
						return result;
					}
				}
			}
		}

		// 验证当前监管对象类型名称的唯一性
		String objectTypeName = regulaObjectType.getObjectTypeName();
		if (StringUtils.isNotBlank(objectTypeName)) {
			Map<String, Object> conditions = new HashMap<>();
			conditions.put("objectTypeName", objectTypeName);
			List<RegulaObjectType> list = regulaObjectTypeBiz.getByMap(conditions, false);
			if (list != null) {
				if (isUpdate) {
					for (RegulaObjectType tmp : list) {
						if (!tmp.getId().equals(regulaObjectType.getId()) && tmp.getIsDeleted().equals("0")) {
							result.setMessage("所填监管对象类型名称已存在");
							return result;
						}
					}
				} else {
					if (!(list == null || list.isEmpty())) {
						result.setMessage("所填监管对象类型名称已存在");
						return result;
					}
				}
			}
		}

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 验证监管对象类型parentIns是第几级
	 * @param parentIns
	 * @return 监管对象类型parentIns的级数
	 */
	private int checkHierarchy(RegulaObjectType parentIns) {
		List<RegulaObjectType> all = regulaObjectTypeBiz.getList(1, 2147483647, new RegulaObjectType()).getData()
				.getRows();

		Map<Integer, RegulaObjectType> regObjTMap = new HashMap<>();
		for (RegulaObjectType tmp : all) {
			regObjTMap.put(tmp.getId(), tmp);
		}

		int loopCount = 1;// 待添加对象父级所处的层级数
		boolean isLoop = true;
		do {
			if (parentIns.getParentObjectTypeId().equals(-1)) {
				break;
			}
			parentIns = regObjTMap.get(parentIns.getParentObjectTypeId());
			loopCount++;

			if (loopCount > all.size()) {
				isLoop = false;
			}
		} while (isLoop);

		return loopCount;
	}

	/**
	 * 修改监管对象-经营单位
	 * 
	 * @author meng
	 * @return
	 */
	public Result<Void> updateRegulaObject(RegulaObjectType regulaObjectType) {
		Result<Void> result = new Result<>();

		result = check(regulaObjectType, true);

		if (!result.getIsSuccess()) {
			return result;
		}

		regulaObjectTypeBiz.updateSelectiveById(regulaObjectType);

		return result;
	}

	/**
	 * 分页查询记录
	 * 
	 * @author 尚
	 * @param page
	 * @param limit
	 * @param regulaObjectType
	 * @return
	 */
	public TableResultResponse<RegulaObjectTypeVo> getList(int page, int limit, RegulaObjectType regulaObjectType) {
		TableResultResponse<RegulaObjectType> tableResult = regulaObjectTypeBiz.getList(page, limit, regulaObjectType);
		List<RegulaObjectType> list = tableResult.getData().getRows();
		
		if(BeanUtil.isEmpty(list)) {
		    return new TableResultResponse<>(0, new ArrayList<RegulaObjectTypeVo>());
		}

		/*
		 * 进行数据聚和
		 */
		try {
			mergeCore.mergeResult(RegulaObjectType.class, list);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		List<RegulaObjectTypeVo> voList = queryAssist(list);

		return new TableResultResponse<RegulaObjectTypeVo>(tableResult.getData().getTotal(), voList);
	}

	private List<RegulaObjectTypeVo> queryAssist(List<RegulaObjectType> list) {
		List<RegulaObjectTypeVo> voList = BeanUtil.copyBeanList_New(list, RegulaObjectTypeVo.class);

		List<String> parentIdList = voList.stream().map(o -> o.getParentObjectTypeId() + "").distinct()
				.collect(Collectors.toList());

		List<RegulaObjectType> byParentInstance = new ArrayList<>();
		if(BeanUtil.isNotEmpty(parentIdList)) {
		    byParentInstance = regulaObjectTypeBiz.selectByIds(String.join(",", parentIdList));
		}
		Map<Integer, String> type_ID_NAME_Map = byParentInstance.stream()
				.collect(Collectors.toMap(RegulaObjectType::getId, RegulaObjectType::getObjectTypeName));

		for (RegulaObjectTypeVo vo : voList) {
			if (!"-1".equals(vo.getParentObjectTypeId() + "")) {
				Integer parentId = vo.getParentObjectTypeId();
				vo.setParentObjectTypeName(type_ID_NAME_Map.get(parentId));
			}
		}

		return voList;
	}

	/**
	 * 获取单个对象
	 * 
	 * @author 尚
	 * @param id 待获取对象ID
	 * @return
	 */
	public ObjectRestResponse<JSONObject> get(Integer id) {
		RegulaObjectType regulaObjectType = regulaObjectTypeBiz.selectById(id);

		if (regulaObjectType == null || regulaObjectType.getIsDeleted().equals("1")) {
			return null;
		}

		// 查询该对象父级对象
		JSONObject jsonObj = JSONObject.parseObject(JSON.toJSONString(regulaObjectType));
		if (!regulaObjectType.getParentObjectTypeId().equals(-1)) {
			RegulaObjectType parent = regulaObjectTypeBiz.selectById(regulaObjectType.getParentObjectTypeId());

			jsonObj.put("parentObjectTypeName", parent.getObjectTypeName());
		}

		return new ObjectRestResponse<JSONObject>().data(jsonObj);
	}

	/**
	 * 根据`reg_type_relation`表中配置的信息查询监管对象类型
	 * 
	 * @author 尚
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<RegulaObjectType> getByRelation(int page, int limit) {
		String regulaObjTypeIds = "";

		RegTypeRelation reTypeRelation = new RegTypeRelation(Constances.RegTypeRelation.CONCERNED_COMPANY,
				Constances.RegTypeRelation.Z_Z);
		List<RegTypeRelation> list = regTypeRelationBiz.getList(reTypeRelation);
		if (list != null && !list.isEmpty()) {
			regulaObjTypeIds = list.get(0).getRegObjId();
		}

		TableResultResponse<RegulaObjectType> result = regulaObjectTypeBiz.getByRelation(regulaObjTypeIds, page, limit);
		return result;
	}
}
