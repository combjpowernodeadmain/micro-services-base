package com.bjzhianjia.scp.cgp.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.RightsIssuesBiz;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * 权利事项服务类
 * @author zzh
 *
 */
@Service
@Slf4j
public class RightsIssuesService {

	@Autowired
	private RightsIssuesBiz rightsIssuesBiz;
	
	@Autowired
	private EventTypeBiz eventTypeBiz;
	
	@Autowired
    private MergeCore mergeCore;
	
	/**
	 * 分页查询
	 * @param page 当前页
	 * @param limit 页大小
	 * @param rightsIssues 查询条件
	 * @return
	 */
	public TableResultResponse<RightsIssues> getList(int page, int limit, RightsIssues rightsIssues) {
		
		List<EventType> eventTypes = eventTypeBiz.getByBizType(rightsIssues.getBizType());
		List<Integer> eventIds = eventTypes.stream().map(e -> e.getId()).collect(Collectors.toList());
		TableResultResponse<RightsIssues> tableResult = rightsIssuesBiz.getList(page, limit, eventIds, rightsIssues);
		List<RightsIssues> list = tableResult.getData().getRows();
		
		try {
			mergeCore.mergeResult(RightsIssues.class, list);
		} catch(Exception ex) {
			log.error("merge data exception", ex);
		}
		
		return tableResult;
	}
	
	/**
	 * 创建权利事项
	 * @param rightsIssues
	 * @return
	 */
	public Result<Void> createRightsIssues(RightsIssues rightsIssues) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		RightsIssues tempRightsIssues = rightsIssuesBiz.getByCode(rightsIssues.getCode());
		if(tempRightsIssues != null) {
			result.setMessage("终端标识号已存在");
			return result;
		}
		
		tempRightsIssues = rightsIssuesBiz.getByUnlawfulAct(rightsIssues.getUnlawfulAct());
		if(tempRightsIssues != null) {
			result.setMessage("终端手机号已存在");
			return result;
		}
		
		EventType eventType = eventTypeBiz.getById(rightsIssues.getType());
		
		if(eventType != null) {
			result.setMessage("事件类型不存在");
			return result;
		}
		
		rightsIssuesBiz.insertSelective(tempRightsIssues);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	/**
	 * 修改权利事项
	 * @param rightsIssues
	 * @return
	 */
	public Result<Void> updateRightsIssues(RightsIssues rightsIssues) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		RightsIssues tempRightsIssues = rightsIssuesBiz.getByCode(rightsIssues.getCode());
		if(tempRightsIssues != null && !tempRightsIssues.getId().equals(rightsIssues.getId())) {
			result.setMessage("终端标识号已存在");
			return result;
		}
		
		tempRightsIssues = rightsIssuesBiz.getByUnlawfulAct(rightsIssues.getUnlawfulAct());
		if(tempRightsIssues != null && !tempRightsIssues.getId().equals(rightsIssues.getId())) {
			result.setMessage("终端手机号已存在");
			return result;
		}
		
		EventType eventType = eventTypeBiz.getById(rightsIssues.getType());
		
		if(eventType != null) {
			result.setMessage("事件类型不存在");
			return result;
		}
		
		rightsIssuesBiz.updateSelectiveById(tempRightsIssues);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
}
