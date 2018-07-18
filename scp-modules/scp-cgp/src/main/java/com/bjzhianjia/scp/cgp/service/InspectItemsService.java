package com.bjzhianjia.scp.cgp.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.InspectItemsBiz;
import com.bjzhianjia.scp.cgp.biz.RightsIssuesBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.InspectItems;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 巡查事项服务类
 * @author zzh
 *
 */
@Service
@Slf4j
public class InspectItemsService {

	@Autowired
	private InspectItemsBiz inspectItemsBiz;
	
	@Autowired
	private RightsIssuesBiz rightsIssuesBiz;
	
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
	 * @param rightsIssues 查询条件
	 * @return
	 */
	public TableResultResponse<InspectItems> getList(int page, int limit, InspectItems inspectItems) {
		
		TableResultResponse<InspectItems> tableResult = inspectItemsBiz.getList(page, limit, inspectItems);
		List<InspectItems> list = tableResult.getData().getRows();
		
		if(list.size() == 0) {
			return tableResult;
		}
		
		List<String> bizTypes = list.stream().map((o)->o.getBizType()).distinct().collect(Collectors.toList());
		
		if(bizTypes != null && bizTypes.size() > 0) {
			Map<String, String> typeMap = eventTypeBiz.getByIds(String.join(",", bizTypes));
			
			for(InspectItems item:list) {
				item.setType(typeMap.get(item.getType()));
			}
		}
		
		try {
			mergeCore.mergeResult(InspectItems.class, list);
		} catch(Exception ex) {
			log.error("merge data exception", ex);
		}
		return tableResult;
	}
	
	/**
	 * 创建巡查事项
	 * @param inspectItems
	 * @return
	 */
	public Result<Void> createInspectItems(InspectItems inspectItems) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		InspectItems tempInspectItems = inspectItemsBiz.getByCode(inspectItems.getCode());
		if(tempInspectItems != null) {
			result.setMessage("巡查事项边哈已存在");
			return result;
		}
		
		tempInspectItems = inspectItemsBiz.getByName(inspectItems.getName());
		if(tempInspectItems != null) {
			result.setMessage("巡查事项名称已存在");
			return result;
		}
		
		RightsIssues rightsIssues = rightsIssuesBiz.getByCode(inspectItems.getItemNum());
		
		if(rightsIssues == null) {
			result.setMessage("权利事项编号不存在");
			return result;
		}
		
		Map<String, String> bizType = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
		
		if(bizType == null || !bizType.containsKey(inspectItems.getBizType())) {
			result.setMessage("事件线条不存在");
			return result;
		}
		
		EventType eventType = eventTypeBiz.getByTypeCode(inspectItems.getType());
		
		if(eventType != null) {
			result.setMessage("事件类型不存在");
			return result;
		}
		
		inspectItemsBiz.insertSelective(inspectItems);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	/**
	 * 修改巡查事项
	 * @param inspectItems
	 * @return
	 */
	public Result<Void> updateInspectItems(InspectItems inspectItems) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		InspectItems tempInspectItems = inspectItemsBiz.getByCode(inspectItems.getCode());
		if(tempInspectItems != null && !tempInspectItems.getId().equals(inspectItems.getId())) {
			result.setMessage("巡查事项边哈已存在");
			return result;
		}
		
		tempInspectItems = inspectItemsBiz.getByName(inspectItems.getName());
		if(tempInspectItems != null && !tempInspectItems.getId().equals(inspectItems.getId())) {
			result.setMessage("巡查事项名称已存在");
			return result;
		}
		
		RightsIssues rightsIssues = rightsIssuesBiz.getByCode(inspectItems.getItemNum());
		
		if(rightsIssues == null) {
			result.setMessage("权利事项编号不存在");
			return result;
		}
		
		Map<String, String> bizType = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
		
		if(bizType == null || !bizType.containsKey(inspectItems.getBizType())) {
			result.setMessage("事件线条不存在");
			return result;
		}
		
		EventType eventType = eventTypeBiz.getByTypeCode(inspectItems.getType());
		
		if(eventType != null) {
			result.setMessage("事件类型不存在");
			return result;
		}
		
		inspectItemsBiz.updateSelectiveById(inspectItems);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
}
