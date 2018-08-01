package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.EnforceTerminalBiz;
import com.bjzhianjia.scp.cgp.entity.EnforceTerminal;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.util.StringUtil;

/**
 * 终端服务类
 * 
 * @author zzh
 *
 */
@Service
@Slf4j
public class EnforceTerminalService {

	@Autowired
	private EnforceTerminalBiz enforceTerminalBiz;

	@Autowired
	private AdminFeign adminFeign;

	@Autowired
	private MergeCore mergeCore;

	/**
	 * 分页查询
	 * 
	 * @param page     当前页
	 * @param limit    页大小
	 * @param terminal 查询条件
	 * @return
	 */
	public TableResultResponse<EnforceTerminal> getList(int page, int limit, EnforceTerminal terminal) {

		TableResultResponse<EnforceTerminal> tableResult = enforceTerminalBiz.getList(page, limit, terminal);
		List<EnforceTerminal> list = tableResult.getData().getRows();

		queryAssist(list);

		try {
			mergeCore.mergeResult(EnforceTerminal.class, list);
		} catch (Exception ex) {
			log.error("merge data exception", ex);
		}

		return tableResult;
	}

	private void queryAssist(List<EnforceTerminal> list) {
		Map<String, String> departs = new HashMap<>();
		Map<String, String> users = new HashMap<>();
//		List<String> uniqueDeparts = list.stream().map((o)->o.getRetrievalDepartment()).distinct().collect(Collectors.toList());

		/*
		 * 数据库中，领用人与领用部门都是可空，所以如果当这两个字段为空时，uniqueDeparts和uniqueUsers字符串中将带有null字样
		 * 进行adminFeign查询时用发生异常
		 */

		List<String> uniqueDeparts = new ArrayList<>();
		for (EnforceTerminal enforceTerminal : list) {
			if (StringUtils.isNotBlank(enforceTerminal.getRetrievalDepartment())) {
				uniqueDeparts.add(enforceTerminal.getRetrievalDepartment());
			}
		}

		if (uniqueDeparts != null && !uniqueDeparts.isEmpty()) {
			departs = adminFeign.getLayerDepart(String.join(",", uniqueDeparts));
		}

//		List<String> uniqueUsers = list.stream().map((o)->o.getRetrievalUser()).distinct().collect(Collectors.toList());
		List<String> uniqueUsers = new ArrayList<>();
		for (EnforceTerminal enforceTerminal : list) {
			if (StringUtils.isNotBlank(enforceTerminal.getRetrievalUser())) {
				uniqueUsers.add(enforceTerminal.getRetrievalUser());
			}
		}
		if (uniqueUsers != null && !uniqueUsers.isEmpty()) {
			users = adminFeign.getUser(String.join(",", uniqueUsers));
		}

		if (!departs.isEmpty() || !users.isEmpty()) {
			for (EnforceTerminal tmpTerminal : list) {
				String departName = departs.get(tmpTerminal.getRetrievalDepartment());
				tmpTerminal.setRetrievalDepartment(departName);

				String jsonUser = users.get(tmpTerminal.getRetrievalUser());
				JSONObject jsonUserObj = JSONObject.parseObject(jsonUser);
				if (jsonUserObj != null) {
					tmpTerminal.setRetrievalUser(jsonUser);
				}
			}
		}
	}

	/**
	 * 创建终端
	 * 
	 * @param terminal
	 * @return
	 */
	public Result<Void> createTerminal(EnforceTerminal terminal) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		EnforceTerminal tempTerminal = enforceTerminalBiz.getByTerminalCode(terminal.getTerminalCode());
		if (tempTerminal != null) {
			result.setMessage("终端标识号已存在");
			return result;
		}

		if (!StringUtil.isEmpty(terminal.getTerminalPhone())) {
			tempTerminal = enforceTerminalBiz.getByTerminalPhone(terminal.getTerminalPhone());
			if (tempTerminal != null) {
				result.setMessage("终端手机号已存在");
				return result;
			}
		}

		if (!StringUtil.isEmpty(terminal.getRetrievalDepartment())) {
			Map<String, String> depart = adminFeign.getDepart(terminal.getRetrievalDepartment());

			if (depart == null || depart.isEmpty()) {
				result.setMessage("终端部门不存在");
				return result;
			}
		}

		if (!StringUtil.isEmpty(terminal.getRetrievalUser())) {
			Map<String, String> user = adminFeign.getUser(terminal.getRetrievalUser());

			if (user == null || user.isEmpty()) {
				result.setMessage("终端人员不存在");
				return result;
			}
		}

		enforceTerminalBiz.insertSelective(terminal);

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 修改终端
	 * 
	 * @param terminal
	 * @return
	 */
	public Result<Void> updateTerminal(EnforceTerminal terminal) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		EnforceTerminal tempTerminal = enforceTerminalBiz.getByTerminalCode(terminal.getTerminalCode());
		if (tempTerminal != null && !tempTerminal.getId().equals(terminal.getId())) {
			result.setMessage("终端标识号已存在");
			return result;
		}

		if (!StringUtil.isEmpty(terminal.getTerminalPhone())) {
			tempTerminal = enforceTerminalBiz.getByTerminalPhone(terminal.getTerminalPhone());
			if (tempTerminal != null && !tempTerminal.getId().equals(terminal.getId())) {
				result.setMessage("终端手机号已存在");
				return result;
			}
		}

		if (!StringUtil.isEmpty(terminal.getRetrievalDepartment())) {
			Map<String, String> depart = adminFeign.getDepart(terminal.getRetrievalDepartment());

			if (depart == null || depart.isEmpty()) {
				result.setMessage("终端部门不存在");
				return result;
			}
		}

		if (!StringUtil.isEmpty(terminal.getRetrievalUser())) {
			Map<String, String> user = adminFeign.getUser(terminal.getRetrievalUser());

			if (user == null || user.isEmpty()) {
				result.setMessage("终端人员不存在");
				return result;
			}
		}

		enforceTerminalBiz.updateSelectiveById(terminal);

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 获取单个对象
	 * 
	 * @param id
	 * @return
	 */
	public EnforceTerminal get(Integer id) {
		/*
		 * 获取单个对象前端需要返回【是否可用】的ID
		 */
		EnforceTerminal enforceTerminal = enforceTerminalBiz.selectById(id);
		String isEnable = enforceTerminal.getIsEnable();
		List<EnforceTerminal> list = new ArrayList<>();
		list.add(enforceTerminal);
		queryAssist(list);

		try {
			mergeCore.mergeResult(EnforceTerminal.class, list);
		} catch (Exception ex) {
			log.error("merge data exception", ex);
		}
		enforceTerminal.setIsEnable(isEnable);
		return enforceTerminal;
	}
}
