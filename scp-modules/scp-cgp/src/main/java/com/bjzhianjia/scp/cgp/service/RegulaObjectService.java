package com.bjzhianjia.scp.cgp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.EnterpriseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.Query;

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
		
		int maxRegulaObjectId=-1;
		if(theMaxRegulaObject==null) {
			maxRegulaObjectId=1;
		}else {
			maxRegulaObjectId+=1;
		}
		regulaObject.setId(maxRegulaObjectId);// 指定监管对象记录ID
		enterpriseInfo.setRegulaObjId(maxRegulaObjectId);// 指定企业信息的外键

		Result<Void> result = new Result<>();
		
		result=check(regulaObject, enterpriseInfo, false);
		if(!result.getIsSuccess()) {
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
	 * 	2.1業務條線 2.2企業類型 2.3證件類型
	 * 3 验证所选事件类别是否已被删除，如果是未删除，则通过，否则验证失败；<br/>
	 * 4 验证当前监管对象编码的唯一性；<br/>
	 * 5 验证当前监管对象名称的唯一性；<br/>
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

		// 验证所选事件类别是否已被删除
		String eventListJArrayStr = regulaObject.getEventList();
		if (StringUtils.isNotBlank(eventListJArrayStr)) {
			JSONArray eventTypeJArray = JSONArray.parseArray(eventListJArrayStr);
			for (int i = 0; i < eventTypeJArray.size(); i++) {
				JSONObject eventTypeJObject = eventTypeJArray.getJSONObject(i);
				Integer eventTypeId = eventTypeJObject.getInteger("eventType");
				EventType eventType = eventTypeBiz.selectById(eventTypeId);
				if (eventType == null || eventType.getIsDeleted().equals("1")) {
					result.setMessage("所选事件类别已删除");
					return result;
				}
			}
		}

		/*
		 * 验证所选各数据字典选项是否已被删除
		 */

		// 验证业务 条线是否删除
		String bizListJArrayStr = regulaObject.getBizList();
		if (StringUtils.isNotBlank(bizListJArrayStr)) {
			Map<String, String> bizTypes = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
			JSONArray bizTypeJArray = JSONArray.parseArray(bizListJArrayStr);
			for (int i = 0; i < bizTypeJArray.size(); i++) {
				JSONObject bizTypeJObject = bizTypeJArray.getJSONObject(i);
				String bizTypeId = bizTypeJObject.getString("bizType");
				if (!bizTypes.containsKey(bizTypeId)) {
					result.setMessage("所选业务条线不存在");
					return result;
				}
			}
		}

		// 验证企业类型是否已被删除
		String typeCode=enterpriseInfo.getTypeCode();
		if(StringUtils.isNotBlank(typeCode)) {
			Map<String, String> entTypes = dictFeign.getDictIds(Constances.ROOT_BIZ_ENTTYPE);
			if(!entTypes.containsKey(typeCode)) {
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
			Query query = new Query(params);
			TableResultResponse<RegulaObject> regulaObjectTable = regulaObjectBiz.selectByQuery(query);
			List<RegulaObject> rows = regulaObjectTable.getData().getRows();
			if (rows != null) {
				if (isUpdate) {
					for (RegulaObject tmp : rows) {
						if (!tmp.getId().equals(regulaObject.getId())) {
							result.setMessage("所填监管对象编码已存在");
							return result;
						}
					}
				} else {
					if (rows.size() > 0) {
						result.setMessage("所填监管对象编码已存在");
						return result;
					}
				}

			}
		}

		// 验证当前监管对象名称的唯一性
		String objName = regulaObject.getObjName();
		if (StringUtils.isNotBlank(objName)) {
			Map<String, Object> params = new HashMap<>();
			params.put("objName", objName);
			Query query = new Query(params);
			TableResultResponse<RegulaObject> regulaObjectTable = regulaObjectBiz.selectByQuery(query);
			List<RegulaObject> rows = regulaObjectTable.getData().getRows();
			if (rows != null) {
				if (isUpdate) {
					for (RegulaObject tmp : rows) {
						if (!tmp.getId().equals(regulaObject.getId())) {
							result.setMessage("所填监管对象名称已存在");
							return result;
						}
					}
				} else {
					if (rows.size() > 0) {
						result.setMessage("所填监管对象名称已存在");
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
	 * 修改监管对象-经营单位
	 * @author 尚
	 * @param regulaObject
	 * @param enterpriseInfo
	 * @return
	 */
	public Result<Void> updateRegulaObject(RegulaObject regulaObject, EnterpriseInfo enterpriseInfo) {
		Result<Void> result = new Result<>();

		result=check(regulaObject, enterpriseInfo, true);
		if(!result.getIsSuccess()) {
			return result;
		}
		
		regulaObjectBiz.updateSelectiveById(regulaObject);
		enterpriseInfoBiz.updateSelectiveById(enterpriseInfo);

		return result;
	}

	/**
	 * 按条件分页查询
	 * @author 尚
	 * @param regulaObject
	 * @param page
	 * @param limit
	 * @return
	 */
	public TableResultResponse<RegulaObject> getList(RegulaObject regulaObject,int page,int limit){
		TableResultResponse<RegulaObject> tableResult = regulaObjectBiz.getList(regulaObject, page, limit);
		List<RegulaObject> rows=tableResult.getData().getRows();
		
		//进行业务条线聚和
		rows.stream().map((o)->o.getBizList()).distinct().collect(Collectors.toList());
		return null;
	}
}
