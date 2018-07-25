package com.bjzhianjia.scp.cgp.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
		
		//进行是否可用(isEnable)字段数据聚积
		
		try {
			mergeCore.mergeResult(EventType.class, tableResult.getData().getRows());
		} catch(Exception ex) {
			log.error("merge data exception", ex);
		}
		
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
