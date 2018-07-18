package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.biz.EnforceCertificateBiz;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 执法证服务类
 * @author zzh
 *
 */
@Service
@Slf4j
public class EnforceCertificateService {

	@Autowired
	private EnforceCertificateBiz enforceCertificateBiz;
	
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
	public TableResultResponse<EnforceCertificate> getList(int page, int limit, EnforceCertificate rightsIssues) {
		
		TableResultResponse<EnforceCertificate> tableResult = enforceCertificateBiz.getList(page, limit, rightsIssues);
		List<EnforceCertificate> list = tableResult.getData().getRows();
		
		if(list.size() == 0) {
			return tableResult;
		}
		
		List<String> bizTypes = list.stream().map((o)->o.getBizLists()).distinct().collect(Collectors.toList());
		
		if(bizTypes != null && bizTypes.size() > 0) {
			Map<String, String> typeMap = eventTypeBiz.getByIds(String.join(",", bizTypes));
			
			for(EnforceCertificate item:list) {
				if(StringUtil.isEmpty(item.getBizLists())) {
					continue;
				}
				String[] bizAry = item.getBizLists().split(",");
				List<String> bizTypeNames = new ArrayList<>();
				for(String bizType:bizAry) {
					bizTypeNames.add(typeMap.get(bizType));
				}
				item.setBizLists(String.join(",", bizTypeNames));
			}
		}
		
		try {
			mergeCore.mergeResult(RightsIssues.class, list);
		} catch(Exception ex) {
			log.error("merge data exception", ex);
		}
		
		return tableResult;
	}
	
	/**
	 * 创建执法证
	 * @param enforceCertificate
	 * @return
	 */
	public Result<Void> createEnforceCertificate(EnforceCertificate enforceCertificate) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		EnforceCertificate tempEnforceCertificate = enforceCertificateBiz.getByCertCode(enforceCertificate.getCertCode());
		if(tempEnforceCertificate != null) {
			result.setMessage("证件编号已存在");
			return result;
		}
		
		tempEnforceCertificate = enforceCertificateBiz.getByHolderName(enforceCertificate.getHolderName());
		if(tempEnforceCertificate != null) {
			result.setMessage("持证人已存在");
			return result;
		}
		
		if(!StringUtil.isEmpty(enforceCertificate.getBizLists())) {
			
			Map<String, String> bizType = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
			if(bizType == null || bizType.size() == 0) {
				result.setMessage("事件线条不存在");
				return result;
			}
			String[] bizCodes = enforceCertificate.getBizLists().split(",");
			
			for(String bizCode : bizCodes) {
				
				if(!bizType.containsKey(bizCode)) {
					result.setMessage("事件线条不存在");
					return result;
				}
			}
		}
		
		enforceCertificateBiz.insertSelective(enforceCertificate);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	/**
	 * 修改执法证
	 * @param enforceCertificate
	 * @return
	 */
	public Result<Void> updateEnforceCertificate(EnforceCertificate enforceCertificate) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		EnforceCertificate tempEnforceCertificate = enforceCertificateBiz.getByCertCode(enforceCertificate.getCertCode());
		if(tempEnforceCertificate != null && !tempEnforceCertificate.getId().equals(enforceCertificate.getId())) {
			result.setMessage("证件编号已存在");
			return result;
		}
		
		tempEnforceCertificate = enforceCertificateBiz.getByHolderName(enforceCertificate.getHolderName());
		if(tempEnforceCertificate != null) {
			result.setMessage("持证人已存在");
			return result;
		}
		
		if(!StringUtil.isEmpty(enforceCertificate.getBizLists())) {
			
			Map<String, String> bizType = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
			if(bizType == null || bizType.size() == 0) {
				result.setMessage("事件线条不存在");
				return result;
			}
			String[] bizCodes = enforceCertificate.getBizLists().split(",");
			
			for(String bizCode : bizCodes) {
				
				if(!bizType.containsKey(bizCode)) {
					result.setMessage("事件线条不存在");
					return result;
				}
			}
		}
		
		enforceCertificateBiz.updateSelectiveById(enforceCertificate);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
}
