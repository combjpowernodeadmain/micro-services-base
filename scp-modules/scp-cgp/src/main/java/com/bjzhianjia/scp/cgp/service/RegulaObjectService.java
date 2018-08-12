package com.bjzhianjia.scp.cgp.service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.EnterpriseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectTypeBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.Regula_EnterPriseVo;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import tk.mybatis.mapper.entity.Example;

/**
 * 
 * @author 尚
 *
 */
@Service
public class RegulaObjectService {
	@Autowired
	private AreaGridBiz areaGridBiz;
	@Autowired
	private EventTypeBiz eventTypeBiz;
	@Autowired
	private DictFeign dictFeign;
	@Autowired
	private RegulaObjectBiz regulaObjectBiz;
	@Autowired
	private EnterpriseInfoBiz enterpriseInfoBiz;
	@Autowired
	private MergeCore mergeCore;
	@Autowired
	private RegulaObjectTypeBiz regulaObjectTypeBiz;

	/**
	 * 添加监管对象-经营单位
	 * 
	 * @author 尚
	 * @param regulaObject
	 * @param enterpriseInfo
	 * @return
	 */
	public Result<Void> createRegulaObject(RegulaObject regulaObject, EnterpriseInfo enterpriseInfo) {
		RegulaObject theMaxRegulaObject = regulaObjectBiz.getTheMaxOne();

		int maxRegulaObjectId = -1;
		if (theMaxRegulaObject == null) {
			maxRegulaObjectId = 1;
		} else {
			maxRegulaObjectId = theMaxRegulaObject.getId() + 1;
		}
		regulaObject.setId(maxRegulaObjectId);// 指定监管对象记录ID
		enterpriseInfo.setRegulaObjId(maxRegulaObjectId);// 指定企业信息的外键

		Result<Void> result = new Result<>();

		result = check(regulaObject, enterpriseInfo, false);
		if (!result.getIsSuccess()) {
			return result;
		}

		regulaObjectBiz.insertSelective(regulaObject);
		enterpriseInfoBiz.insertSelective(enterpriseInfo);

		return result;
	}

	/**
	 * 对添加、修改操作的逻辑进行验证<br/>
	 * 1 验证所选网格是否已被删除，如果是未删除，则通过，否则验证失败；<br/>
	 * 2 验证所选各数据字典选项是否已被删除，如果是未删除，则通过，否则验证失败；<br/>
	 * 2.1業務條線 2.2企業類型 2.3證件類型 3 验证所选事件类别是否已被删除，如果是未删除，则通过，否则验证失败；<br/>
	 * 4 验证当前监管对象编码的唯一性；<br/>
	 * 5 验证当前监管对象名称的唯一性；<br/>
	 * 
	 * @author 尚
	 * @param regulaObject
	 * @param enterpriseInfo
	 * @param isUpdate
	 * @return
	 */
	private Result<Void> check(RegulaObject regulaObject, EnterpriseInfo enterpriseInfo, boolean isUpdate) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		// 验证所选网格是否已被删除
		if (regulaObject.getGriId() != null) {
			AreaGrid areaGrid = areaGridBiz.selectById(regulaObject.getGriId());
			if (areaGrid == null || areaGrid.getIsDeleted().equals("1")) {
				result.setMessage("所选网格已删除");
				return result;
			}
		}

		/*
		 * 验证所选事件类别是否已被删除 事件类别存储结构
		 * 22,23;25;26,27
		 */
		String eventListLongStr = regulaObject.getEventList();
		if (StringUtils.isNotBlank(eventListLongStr)) {
			List<String> eventIdList=new ArrayList<>();
			
			String[] eventIDGroups = eventListLongStr.split(";");
			for (String eventIDGroup : eventIDGroups) {
				String[] eventIDs = eventIDGroup.split(",");
				for (String eventID : eventIDs) {
					if(!eventID.equals("-")) {
						eventIdList.add(eventID);
					}
				}
			}
			if(eventIdList!=null&&!eventIdList.isEmpty()) {
				List<EventType> eventTypeList = eventTypeBiz.getByIds(eventIdList);
				for (EventType eventType : eventTypeList) {
					if (eventType == null || eventType.getIsDeleted().equals("1")) {
						result.setMessage("所选事件类别已删除");
						return result;
					}
				}
			}
			
//			JSONArray eventTypeJArray = JSONArray.parseArray(eventListLongStr);
//			for (int i = 0; i < eventTypeJArray.size(); i++) {
//				JSONObject eventTypeJObject = eventTypeJArray.getJSONObject(i);
//				String eventTypeId = eventTypeJObject.getString("eventList");
//
//				String[] split = eventTypeId.split(",");
//				List<String> eventTypeIds = Arrays.asList(split);
//				List<EventType> eventTypeList = eventTypeBiz.getByIds(eventTypeIds);
//				for (EventType eventType : eventTypeList) {
//					if (eventType == null || eventType.getIsDeleted().equals("1")) {
//						result.setMessage("所选事件类别已删除");
//						return result;
//					}
//				}
//			}
		}

		/*
		 * 验证所选各数据字典选项是否已被删除
		 */

		// 验证业务 条线是否删除
		String bizListJArrayStr = regulaObject.getBizList();
		if (StringUtils.isNotBlank(bizListJArrayStr)) {
			Map<String, String> bizTypes = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
			
			String[] byzTypeIds = bizListJArrayStr.split(",");
			for (String bizTypeId : byzTypeIds) {
				if(!bizTypeId.equals("-")) {
					if (!bizTypes.containsKey(bizTypeId)) {
						result.setMessage("所选业务条线不存在");
						return result;
					}
				}
			}
			
//			JSONArray bizTypeJArray = JSONArray.parseArray(bizListJArrayStr);
//			for (int i = 0; i < bizTypeJArray.size(); i++) {
//				JSONObject bizTypeJObject = bizTypeJArray.getJSONObject(i);
//				String bizTypeId = bizTypeJObject.getString("bizList");
//				if(StringUtils.isNotBlank(bizTypeId)) {
//					if (!bizTypes.containsKey(bizTypeId)) {
//						result.setMessage("所选业务条线不存在");
//						return result;
//					}
//				}
//			}
		}

		// 验证企业类型是否已被删除
		String typeCode = enterpriseInfo.getTypeCode();
		if (StringUtils.isNotBlank(typeCode)) {
			Map<String, String> entTypes = dictFeign.getDictIds(Constances.ROOT_BIZ_ENTTYPE);
			if (!entTypes.containsKey(typeCode)) {
				result.setMessage("所选企业类型不存在");
				return result;
			}
		}

//				验证证件类型是否删除
		String certificateType = enterpriseInfo.getCertificateType();
		if (StringUtils.isNotBlank(certificateType)) {
			Map<String, String> sertificateTypeMap = dictFeign.getDictIds(Constances.ROOT_BIZ_CERT_T);
			if (!sertificateTypeMap.containsKey(certificateType)) {
				result.setMessage("所选证件类型不存在");
				return result;
			}
		}

		// 验证当前监管对象编码的唯一性
		String objCode = regulaObject.getObjCode();
		if (StringUtils.isNotBlank(objCode)) {
			Map<String, Object> params = new HashMap<>();
			params.put("objCode", objCode);
			TableResultResponse<RegulaObject> regulaObjectTable = regulaObjectBiz.getByMap(params);
			List<RegulaObject> rows = regulaObjectTable.getData().getRows();
			if (rows != null) {
				if (isUpdate) {
					for (RegulaObject tmp : rows) {
						if (!tmp.getId().equals(regulaObject.getId())&&!tmp.getIsDeleted().equals("1")) {
							result.setMessage("所填监管对象编码已存在");
							return result;
						}
					}
				} else {
					for (RegulaObject tmp : rows) {
						if (!tmp.getIsDeleted().equals("1")) {
							result.setMessage("所填监管对象编码已存在");
							return result;
						}
					}
					
//					if (rows.size() > 0) {
//						result.setMessage("所填监管对象编码已存在");
//						return result;
//					}
				}

			}
		}

		// 验证当前监管对象名称的唯一性
		String objName = regulaObject.getObjName();
		if (StringUtils.isNotBlank(objName)) {
			Map<String, Object> params = new HashMap<>();
			params.put("objName", objName);
			TableResultResponse<RegulaObject> regulaObjectTable = regulaObjectBiz.getByMap(params);
			List<RegulaObject> rows = regulaObjectTable.getData().getRows();
			if (rows != null) {
				if (isUpdate) {
					for (RegulaObject tmp : rows) {
						if (!tmp.getId().equals(regulaObject.getId())&&!tmp.getIsDeleted().equals("1")) {
							result.setMessage("所填监管对象名称已存在");
							return result;
						}
					}
				} else {
					for (RegulaObject tmp : rows) {
						if (!tmp.getIsDeleted().equals("1")) {
							result.setMessage("所填监管对象名称已存在");
							return result;
						}
					}
				}
			}
		}

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 修改监管对象-经营单位
	 * 
	 * @author 尚
	 * @param regulaObject
	 * @param enterpriseInfo
	 * @return
	 */
	public Result<Void> updateRegulaObject(RegulaObject regulaObject, EnterpriseInfo enterpriseInfo) {
		Map<String, Object> conditions=new HashMap<>();
		conditions.put("regulaObjId", regulaObject.getId());
		List<EnterpriseInfo> enterpriseInfoList=enterpriseInfoBiz.getByMap(conditions);
		if(enterpriseInfoList!=null&&!enterpriseInfoList.isEmpty()) {
			enterpriseInfo.setId(enterpriseInfoList.get(0).getId());
		}else {
			throw new NullPointerException("没有与待更新的监管对象对应的企业信息");
		}
		
		Result<Void> result = new Result<>();

		result = check(regulaObject, enterpriseInfo, true);
		if (!result.getIsSuccess()) {
			return result;
		}

		regulaObjectBiz.updateSelectiveById(regulaObject);
		enterpriseInfoBiz.updateSelectiveById(enterpriseInfo);

		return result;
	}

	/**
	 * 按条件分页查询
	 * 
	 * @author 尚
	 * @param regulaObject
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<RegulaObject> getList(RegulaObject regulaObject, int page, int limit) {
		TableResultResponse<RegulaObject> tableResult = regulaObjectBiz.getList(regulaObject, page, limit);
		List<RegulaObject> rows = tableResult.getData().getRows();

		/*
		 * 进行业务条线聚和 业务条线存储结构[{"bizList","$业务条线ID"},{"bizList","$业务条线ID"}]
		 */
		queryAssist(rows);
		
		try {
			mergeCore.mergeResult(RegulaObject.class, rows);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		return tableResult;
	}

	private void queryAssist(List<RegulaObject> rows) {
		List<String> bizTypeIds = new ArrayList<String>();

		for (RegulaObject tmp : rows) {
			String bizList = tmp.getBizList();
			String[] split = bizList.split(",");
			for (String string : split) {
				bizTypeIds.add(string);
			}
		}
		
		//聚和业务条线及事件类别
		if (bizTypeIds != null && !bizTypeIds.isEmpty()) {
			Map<String, String> bizTypeMap = dictFeign.getDictValueByID(String.join(",", bizTypeIds));
			
			for (RegulaObject tmp : rows) {
				JSONArray bizResultJArray = new JSONArray(); 
				
				String bizList = tmp.getBizList();
				String[] bizListSplit = bizList.split(",");
				
				for (int i=0;i<bizListSplit.length;i++) {
					String string=bizListSplit[i];
					JSONObject bizResultJObject=new JSONObject();
					if(!string.equals("-")) {
						String bizType = bizTypeMap.get(string);
						bizResultJObject = JSONObject.parseObject(bizType);
					}
					bizResultJArray.add(bizResultJObject);
				}
				
				tmp.setBizList(bizResultJArray.toJSONString());
			}
		}
	}
	
	/**
	 * 获取单个对象
	 * @author 尚
	 * @param id
	 * @return
	 */
	public Regula_EnterPriseVo getById(Integer id){
		//查询监管对象
		RegulaObject regulaObject = regulaObjectBiz.selectById(id);
		
		if(regulaObject==null) {
			return null;
		}else if(regulaObject.getIsDeleted().equals("1")) {
			return null;
		}
		
		//查询企业信息
		Example example=new Example(EnterpriseInfo.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("regulaObjId",id);
		EnterpriseInfo enterpriseInfo = enterpriseInfoBiz.selectByExample(example).get(0);
		
//		List<RegulaObject> list=new ArrayList<>();
//		list.add(regulaObject);
		
		//聚和监管对象类别
		//监管对象类别放弃使用字典值，类型使用监管对象类别表，数据类型为Integer
		Integer objTypeId = regulaObject.getObjType();
		RegulaObjectType regulaObjectType = regulaObjectTypeBiz.selectById(objTypeId);
//		Map<String, String> objTypeMap = dictFeign.getDictValueByID(objTypeId);
//		regulaObject.setObjType(objTypeMap.get(objTypeId));
//		queryAssist(list);
		
		/*
		 * 聚和企业类型与证件类型
		 */
		String typeCodeId = enterpriseInfo.getTypeCode();
		String certificateTypeId = enterpriseInfo.getCertificateType();
		
		Map<String, String> map = dictFeign.getDictValueByID(typeCodeId+","+certificateTypeId);
		enterpriseInfo.setTypeCode(map.get(typeCodeId));
		enterpriseInfo.setCertificateType(map.get(certificateTypeId));
		
		String regulaObjectJStr = JSON.toJSONString(regulaObject);
		String enterpriseInfoJStr=JSON.toJSONString(enterpriseInfo);
		
		JSONObject other = BeanUtil.jsonObjectMergeOther(JSONObject.parseObject(regulaObjectJStr), JSONObject.parseObject(enterpriseInfoJStr));
		
		Regula_EnterPriseVo result = JSON.parseObject(other.toJSONString(), Regula_EnterPriseVo.class);
		result.setObjType(JSON.toJSONString(regulaObjectType));
		return result;
	}

	/**
	 * 批量删除对象
	 * 
	 * @author 尚
	 * @param ids 待删除对象的ID数组
	 */
	public void remove(Integer[] ids) {
		regulaObjectBiz.remove(ids);
	}
}