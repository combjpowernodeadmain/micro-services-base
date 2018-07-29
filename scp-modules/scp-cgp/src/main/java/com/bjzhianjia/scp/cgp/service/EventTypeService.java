package com.bjzhianjia.scp.cgp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.util.StringUtil;
import com.netflix.infix.lang.infix.antlr.EventFilterParser.null_predicate_return;

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
		
		//进行是否可用(isEnable)字段数据聚积
		Map<String,String> eventTypeMap = new HashMap<>();
		List<String> uniqueEvenTypes = list.stream().map((o)->o.getIsEnable()).distinct().collect(Collectors.toList());
		if(uniqueEvenTypes != null && !uniqueEvenTypes.isEmpty()) {
			eventTypeMap = dictFeign.getDictValueByID(String.join(",", uniqueEvenTypes));
		}
		
		if(eventTypeMap!=null&&eventTypeMap.size()>0) {
			for(EventType eventType2:list) {
				String string = eventTypeMap.get(eventType2.getIsEnable());
				//向前台传送ID+文本
				JSONObject parseObject = JSONObject.parseObject(string);
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id", parseObject.getString("id"));
				jsonObject.put("labelDefault", parseObject.getString("labelDefault"));
				eventType2.setIsEnable(jsonObject.toJSONString());
			}
		}
		
		
		eventTypeMap.clear();
		uniqueEvenTypes.clear();
		uniqueEvenTypes = list.stream().map((o)->o.getBizType()).distinct().collect(Collectors.toList());
		if(uniqueEvenTypes != null && !uniqueEvenTypes.isEmpty()) {
			eventTypeMap = dictFeign.getDictValueByID(String.join(",", uniqueEvenTypes));
		}
		
		if(eventTypeMap!=null&&eventTypeMap.size()>0) {
			for(EventType eventType2:list) {
				String string = eventTypeMap.get(eventType2.getBizType());
				//向前台传送ID+文本
				JSONObject parseObject = JSONObject.parseObject(string);
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("id", parseObject.getString("id"));
				jsonObject.put("labelDefault", parseObject.getString("labelDefault"));
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
			
			Map<String, String> bizType = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
			
			if(bizType == null || !bizType.containsKey(eventType.getBizType())) {
				result.setMessage("事件线条不存在");
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
			
			Map<String, String> bizType = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
			
			if(bizType == null || !bizType.containsKey(eventType.getBizType())) {
				result.setMessage("事件线条不存在");
				return result;
			}
		}

		eventTypeBiz.updateSelectiveById(eventType);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
}
