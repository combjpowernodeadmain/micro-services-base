package com.bjzhianjia.scp.cgp.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 事件类型服务类
 * @author zzh
 *
 */
@Service
@Slf4j
public class EventTypeService {

	@Autowired
	private EventTypeBiz eventTypeBiz;
	
	@Autowired
	private DictFeign dictFeign;
	
	@Autowired
    private MergeCore mergeCore;
	
	/**
	 * 分页查询
	 * @param page 当前页
	 * @param limit 页大小
	 * @param terminal 查询条件
	 * @return
	 */
	public TableResultResponse<EventType> getList(int page, int limit, EventType eventType) {
		
		TableResultResponse<EventType> tableResult = eventTypeBiz.getList(page, limit, eventType);
		
		List<EventType> list = tableResult.getData().getRows();
		
		//收集需要进行查询的字典code
		
		Set<String> codeSet=new HashSet<>();
		for (EventType eventType2 : list) {
			codeSet.add(eventType2.getIsEnable());
			codeSet.add(eventType2.getBizType());
		}
		
		//进行是否可用(isEnable)字段数据聚积
		Map<String,String> eventTypeMap = new HashMap<>();
		
		if(codeSet != null && !codeSet.isEmpty()) {
			eventTypeMap=dictFeign.getByCodeIn(String.join(",", codeSet));
		}
		
//		List<String> uniqueEvenTypes = list.stream().map((o)->o.getIsEnable()).distinct().collect(Collectors.toList());
		
		if(eventTypeMap!=null&&eventTypeMap.size()>0) {
			for(EventType eventType2:list) {
				//向前台传送code+文本
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("code", eventType2.getIsEnable());
				jsonObject.put("labelDefault", eventTypeMap.get(eventType2.getIsEnable()));
				eventType2.setIsEnable(jsonObject.toJSONString());
			}
		}
		
		if(eventTypeMap!=null&&eventTypeMap.size()>0) {
			for(EventType eventType2:list) {
				//向前台传送code+文本
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("code", eventType2.getBizType());
				jsonObject.put("labelDefault", eventTypeMap.get(eventType2.getBizType()));
				eventType2.setBizType(jsonObject.toJSONString());
			}
		}
		
//		try {
//			mergeCore.mergeResult(EventType.class, list);
//		} catch(Exception ex) {
//			log.error("merge data exception", ex);
//		}
		
		return tableResult;
	}
	
	/**
	 * 创建终端
	 * @param eventType
	 * @return
	 */
	public Result<Void> createEventType(EventType eventType) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		EventType tempEventType = eventTypeBiz.getByTypeCode(eventType.getTypeCode());
		if(tempEventType != null) {
			result.setMessage("事件类型编号已存在");
			return result;
		}
		
		tempEventType = eventTypeBiz.getByTypeName(eventType.getTypeName());
		if(tempEventType != null) {
			result.setMessage("事件类型名称已存在");
			return result;
		}
		
		if(!StringUtil.isEmpty(eventType.getBizType())) {
			//字典在业务库里存在形式(ID-->code)，代码需要进行相应修改--getByCode
			Map<String, String> bizType = dictFeign.getByCode(Constances.ROOT_BIZ_TYPE);
			
			if(bizType == null || !bizType.containsKey(eventType.getBizType())) {
				result.setMessage("业务线条不存在");
				return result;
			}
		}
		
		eventTypeBiz.insertSelective(eventType);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	/**
	 * 修改终端
	 * @param eventType
	 * @return
	 */
	public Result<Void> updateEventType(EventType eventType) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		EventType tempEventType = eventTypeBiz.getByTypeCode(eventType.getTypeCode());
		if(tempEventType != null && !tempEventType.getId().equals(eventType.getId())) {
			result.setMessage("事件类型编号已存在");
			return result;
		}
		
		tempEventType = eventTypeBiz.getByTypeName(eventType.getTypeName());
		if(tempEventType != null && !tempEventType.getId().equals(eventType.getId())) {
			result.setMessage("事件类型名称已存在");
			return result;
		}
		
		if(!StringUtil.isEmpty(eventType.getBizType())) {
			//字典在业务库里存在形式(ID-->code)，代码需要进行相应修改--getByCode
			Map<String, String> bizType = dictFeign.getByCode(Constances.ROOT_BIZ_TYPE);
			
			if(bizType == null || !bizType.containsKey(eventType.getBizType())) {
				result.setMessage("业务线条不存在");
				return result;
			}
		}

		eventTypeBiz.updateSelectiveById(eventType);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
}
