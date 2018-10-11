package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.RightsIssuesBiz;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.RightsIssuesVo;
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
	@Autowired
	private DictFeign dictFeign;
	@Autowired
	private EventTypeMapper eventTypeMapper;
	
	/**
	 * 分页查询
	 * @param page 当前页
	 * @param limit 页大小
	 * @param rightsIssues 查询条件
	 * @return
	 */
	public TableResultResponse<RightsIssuesVo> getList(int page, int limit, RightsIssues rightsIssues) {
		
//		List<EventType> eventTypes = eventTypeBiz.getByBizType(rightsIssues.getBizType());
//		List<Integer> eventIds = eventTypes.stream().map(e -> e.getId()).collect(Collectors.toList());
		TableResultResponse<RightsIssues> tableResult = rightsIssuesBiz.getList(page, limit, null, rightsIssues);
		List<RightsIssues> list = tableResult.getData().getRows();
		
		if(BeanUtil.isEmpty(list)) {
		    return new TableResultResponse<RightsIssuesVo>(0, new ArrayList<RightsIssuesVo>());
		}
		
		try {
			mergeCore.mergeResult(RightsIssues.class, list);
		} catch(Exception ex) {
			log.error("merge data exception", ex);
		}
		
		List<RightsIssuesVo> voList = queryAssist(list);
		
		return new TableResultResponse<>(tableResult.getData().getTotal(), voList);
	}
	
	private List<RightsIssuesVo> queryAssist(List<RightsIssues> list) {
	    List<RightsIssuesVo> voList = BeanUtil.copyBeanList_New(list, RightsIssuesVo.class);
	    List<String> eventTypeIdList = voList.stream().map(o->String.valueOf(o.getType())).distinct().collect(Collectors.toList());
	    if(BeanUtil.isNotEmpty(eventTypeIdList)) {
	        List<EventType> eventTypeList = eventTypeMapper.selectByIds(String.join(",", eventTypeIdList));
	        if(BeanUtil.isNotEmpty(eventTypeList)) {
	            Map<Integer, String> eventType_ID_NAME_Map = eventTypeList.stream().collect(Collectors.toMap(EventType::getId, EventType::getTypeName));
	            for (RightsIssuesVo vo : voList) {
                    vo.setEventTypeName(eventType_ID_NAME_Map.get(vo.getType()));
                }
	        }
	    }
	    
	    return voList;
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
			result.setMessage("权利事项编号已存在");
			return result;
		}
		
		tempRightsIssues = rightsIssuesBiz.getByUnlawfulAct(rightsIssues.getUnlawfulAct());
		if(tempRightsIssues != null) {
			result.setMessage("法则已存在");
			return result;
		}
		
		EventType eventType = eventTypeBiz.getById(rightsIssues.getType());
		
//		if(eventType != null) {
		if(eventType == null) {
			result.setMessage("事件类型不存在");
			return result;
		}
		
		rightsIssuesBiz.insertSelective(rightsIssues);
		
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
			result.setMessage("权利事项编号已存在");
			return result;
		}
		
		tempRightsIssues = rightsIssuesBiz.getByUnlawfulAct(rightsIssues.getUnlawfulAct());
		if(tempRightsIssues != null && !tempRightsIssues.getId().equals(rightsIssues.getId())) {
			result.setMessage("法则已存在");
			return result;
		}
		
		EventType eventType = eventTypeBiz.getById(rightsIssues.getType());
		
//		if(eventType != null) {//原代码
		if(eventType == null) {//后代码
			result.setMessage("事件类型不存在");
			return result;
		}
		
		rightsIssuesBiz.updateSelectiveById(rightsIssues);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	/**
	 * 获取单条对象
	 * @author 尚
	 * @param id
	 * @return
	 */
	public RightsIssues getByID(Integer id) {
		RightsIssues rightsIssues = rightsIssuesBiz.selectById(id);
		
		if(rightsIssues==null||rightsIssues.getIsDeleted().equals("1")) {
			return null;
		}
		
		//进行业务条线数据聚和
		//业务条线为非必填项，有可能为空，须做非空判断，避免dictFeign.getByCodeIn()方法调用失败
		if(StringUtils.isBlank(rightsIssues.getBizType())) {
		    log.debug("该权力事项业务条线为空");
		}else {
		    Map<String, String> bizTypeMap = dictFeign.getByCodeIn(rightsIssues.getBizType());
		    if(bizTypeMap!=null&&bizTypeMap.size()>0) {
		        JSONObject jsonObject=new JSONObject();
		        jsonObject.put("code", rightsIssues.getBizType());
		        jsonObject.put("labelDefault", bizTypeMap.get(rightsIssues.getBizType()));
		        rightsIssues.setBizType(jsonObject.toJSONString());
		    }
		}
		
		return rightsIssues;
	}
}
