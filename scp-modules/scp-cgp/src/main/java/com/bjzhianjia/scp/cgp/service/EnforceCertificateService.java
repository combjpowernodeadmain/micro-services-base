package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.biz.EnforceCertificateBiz;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 执法证服务类
 * 
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
	@Autowired
	private AdminFeign adminFeign;

	/**
	 * 分页查询
	 * 
	 * @param page         当前页
	 * @param limit        页大小
	 * @param rightsIssues 查询条件
	 * @return
	 */
	public TableResultResponse<EnforceCertificate> getList(int page, int limit, EnforceCertificate rightsIssues) {

		TableResultResponse<EnforceCertificate> tableResult = enforceCertificateBiz.getList(page, limit, rightsIssues);
		List<EnforceCertificate> list = tableResult.getData().getRows();

		if (list.size() == 0) {
			return tableResult;
		}

		List<String> bizTypes = list.stream().map((o) -> o.getBizLists()).distinct().collect(Collectors.toList());

		/*
		 * 业务条线有可能是null，在聚和数据的时候要去除
		 */
		if (bizTypes != null && bizTypes.size() > 0) {
//			for (int i = 0; i < bizTypes.size(); i++) {
//				if (StringUtils.isBlank(bizTypes.get(i))) {
//					bizTypes.remove(i);
//					i--;
//				}
//			}

			/*
			 * -------------------By尚------------------开始----------------
			 */
			/*
			 * 数据库表中biz_lists字段存放的是业务条线ID集合，用“，”隔开 在此通过Feign联查到业务条线内容
			 */
			Map<String, String> bizTypeMap = dictFeign.getDictValueByID(String.join(",", bizTypes));
			// 在rightsIssues对象中，有可能bizLists有可能是两个业务条线ID
			if (bizTypeMap != null && bizTypeMap.size() > 0) {
				for (EnforceCertificate tmp : list) {
					String bizLists = tmp.getBizLists();
					if(StringUtils.isBlank(bizLists)) {
						continue;
					}
					String[] split = bizLists.split(",");
					List<String> bizListsTmp = new ArrayList<>();
					for (String string : split) {
						String bizTypeJsonStr = bizTypeMap.get(string);
						bizListsTmp.add(bizTypeJsonStr);
					}
//					tmp.setBizLists(String.join(",", bizListsTmp));
					tmp.setBizLists(bizListsTmp.toString());
				}
			}
			
			/*
			 * -------------------By尚------------------结束----------------
			 */

			/*
			 * -------------------原代码-------------------开始---------------
			 */
//			Map<String, String> typeMap = eventTypeBiz.getByIds(String.join(",", bizTypes));
//			
//			for(EnforceCertificate item:list) {
//				if(StringUtil.isEmpty(item.getBizLists())) {
//					continue;
//				}
//				String[] bizAry = item.getBizLists().split(",");
//				List<String> bizTypeNames = new ArrayList<>();
//				for(String bizType:bizAry) {
//					bizTypeNames.add(typeMap.get(bizType));
//				}
//				item.setBizLists(String.join(",", bizTypeNames));
//			}
			/*
			 * -------------------原代码-------------------结束---------------
			 */
		}
		
		//获取执证人联系电话
		List<String> userIds = list.stream().map((o) -> o.getUsrId()).distinct().collect(Collectors.toList());
		if(userIds!=null&&userIds.size()>0) {
			Map<String, String> user = adminFeign.getUser(String.join(",", userIds));
			if(user!=null&&user.size()>0) {
				for(EnforceCertificate enforceCertificate:list) {
					if (StringUtils.isBlank(enforceCertificate.getUsrId())) {
						continue;
					}
					String string = user.get(enforceCertificate.getUsrId());
					enforceCertificate.setUsrId(string);
				}
			}
		}

		try {
			mergeCore.mergeResult(EnforceCertificate.class, list);
		} catch (Exception ex) {
			log.error("merge data exception", ex);
		}

		return tableResult;
	}

	/**
	 * 创建执法证
	 * 
	 * @param enforceCertificate
	 * @return
	 */
	public Result<Void> createEnforceCertificate(EnforceCertificate enforceCertificate) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		EnforceCertificate tempEnforceCertificate = enforceCertificateBiz
				.getByCertCode(enforceCertificate.getCertCode());
		if (tempEnforceCertificate != null) {
			result.setMessage("证件编号已存在");
			return result;
		}

		tempEnforceCertificate = enforceCertificateBiz.getByHolderName(enforceCertificate.getHolderName());
		if (tempEnforceCertificate != null) {
			result.setMessage("持证人已存在");
			return result;
		}

		if (!StringUtil.isEmpty(enforceCertificate.getBizLists())) {

			Map<String, String> bizType = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
			if (bizType == null || bizType.size() == 0) {
				result.setMessage("事件线条不存在");
				return result;
			}
			String[] bizCodes = enforceCertificate.getBizLists().split(",");

			for (String bizCode : bizCodes) {

				if (!bizType.containsKey(bizCode)) {
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
	 * 
	 * @param enforceCertificate
	 * @return
	 */
	public Result<Void> updateEnforceCertificate(EnforceCertificate enforceCertificate) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		EnforceCertificate tempEnforceCertificate = enforceCertificateBiz
				.getByCertCode(enforceCertificate.getCertCode());
		if (tempEnforceCertificate != null && !tempEnforceCertificate.getId().equals(enforceCertificate.getId())) {
			result.setMessage("证件编号已存在");
			return result;
		}

		tempEnforceCertificate = enforceCertificateBiz.getByHolderName(enforceCertificate.getHolderName());
		/*
		 * 持证人唯一性验证<br/>
		 */
		if (tempEnforceCertificate != null) {
			boolean flag = tempEnforceCertificate.getId().equals(enforceCertificate.getId());
			if (!flag) {// 说明在数据库中已经有一条记录，但与enforceCertificate对应的记录不相同
				result.setMessage("持证人已存在");
				return result;
			}
		}

		if (!StringUtil.isEmpty(enforceCertificate.getBizLists())) {

			Map<String, String> bizType = dictFeign.getDictIds(Constances.ROOT_BIZ_TYPE);
			if (bizType == null || bizType.size() == 0) {
				result.setMessage("事件线条不存在");
				return result;
			}
			String[] bizCodes = enforceCertificate.getBizLists().split(",");

			for (String bizCode : bizCodes) {

				if (!bizType.containsKey(bizCode)) {
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
