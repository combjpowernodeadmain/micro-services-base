package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.EnforceTerminalBiz;
import com.bjzhianjia.scp.cgp.biz.VhclManagementBiz;
import com.bjzhianjia.scp.cgp.entity.Depart;
import com.bjzhianjia.scp.cgp.entity.EnforceTerminal;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.VhclManagement;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.mapper.EnforceTerminalMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.DeptBizTypeVo;
import com.bjzhianjia.scp.cgp.vo.VhclManagementVo;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 车辆管理服务
 * 
 * @author zzh
 *
 */
@Service
@Slf4j
public class VhclManagementService {

	@Autowired
	private VhclManagementBiz vhclManagementBiz;

	@Autowired
	private EnforceTerminalBiz enforceTerminalBiz;
	@Autowired
	private EnforceTerminalMapper terminalMapper;

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
	public TableResultResponse<VhclManagementVo> getList(int page, int limit, VhclManagement vhcl) {

		TableResultResponse<VhclManagement> tableResult = vhclManagementBiz.getList(page, limit, vhcl);

		List<VhclManagement> list = tableResult.getData().getRows();

		try {
			mergeCore.mergeResult(VhclManagement.class, list);
		} catch (Exception ex) {
			log.error("merge data exception", ex);
		}

		List<VhclManagementVo> voList = queryAssist(list);
		return new TableResultResponse<>(tableResult.getData().getTotal(), voList);
	}

	private List<VhclManagementVo> queryAssist(List<VhclManagement> list) {
		List<VhclManagementVo> voList = BeanUtil.copyBeanList_New(list, VhclManagementVo.class);

		Map<String, String> departs = new HashMap<>();
		Map<String, String> layerDepart = new HashMap<>();
		
		List<String> uniqueDeparts=new ArrayList<>();
		for(VhclManagementVo vo:voList) {
			if(StringUtils.isNotBlank(vo.getDepartment())) {
				//车辆管理里部门为选填，所以可能为空，需做非空判断
				uniqueDeparts.add(vo.getDepartment());
			}
		}
//		List<String> uniqueDeparts = list.stream().map((o) -> o.getDepartment()).distinct()
//				.collect(Collectors.toList());

		// 部门存在多级情况，采用getLayerDepart方法进行聚和，弃用下面大段注释
		if (uniqueDeparts != null && !uniqueDeparts.isEmpty()) {
			layerDepart = adminFeign.getLayerDepart(String.join(",", uniqueDeparts));
		}
		if (!layerDepart.isEmpty()) {
			for (VhclManagementVo tmp : voList) {
				String layerDeptInfo = layerDepart.get(tmp.getDepartment());
				if (StringUtils.isNotBlank(layerDeptInfo)) {
					JSONObject layerDeptJObject = JSONObject.parseObject(layerDeptInfo);
					tmp.setDepartmentName(layerDeptJObject.getString("name"));
				}
			}
		}

		// 聚和终端号码
		List<String> terminalIdList = list.stream().map((o) -> o.getTernimalId() + "").distinct()
				.collect(Collectors.toList());
		List<EnforceTerminal> enforceTerminalList = terminalMapper.selectByIds(String.join(",", terminalIdList));
		Map<Integer, String> collect = enforceTerminalList.stream()
				.collect(Collectors.toMap(EnforceTerminal::getId, EnforceTerminal::getTerminalPhone));
		if (enforceTerminalList != null && !enforceTerminalList.isEmpty()) {
			for (VhclManagementVo tmp : voList) {
				tmp.setTerminalPhone(collect.get(tmp.getTernimalId()));
			}
		}
		return voList;

//		if (uniqueDeparts != null && !uniqueDeparts.isEmpty()) {
//			departs = adminFeign.getDepart(String.join(",", uniqueDeparts));
//		}
//
//		List<Depart> deptList = new ArrayList<>();
//		List<String> deptParentIds = new ArrayList<>();
//		Map<String, String> deptParentMap = new HashMap<>();
//		Set<String> keySet = departs.keySet();
//		for (String string : keySet) {
//			String deptStr = departs.get(string);
//			Depart dept = JSON.parseObject(deptStr, Depart.class);
//			deptList.add(dept);
//			deptParentIds.add(dept.getParentId());
//		}
//		// 查询父部门对象
//		if (deptParentIds != null && !deptParentIds.isEmpty()) {
//			deptParentMap = adminFeign.getDepart(String.join(",", deptParentIds));
//		}
//
//		if (!departs.isEmpty()) {
//			for (VhclManagement tmpVhcl : list) {
//				// 子部门JSON串
//				String departName = departs.get(tmpVhcl.getDepartment());
//				if (departName != null) {
//					// 子部门对象
//					Depart dept = JSON.parseObject(departName, Depart.class);
//					if (!dept.getParentId().equals("-1")) {
//						// w父部门JSON串
//						String parentDeptStr = deptParentMap.get(dept.getParentId());
//						// 父部门对象
//						Depart deptParent = JSON.parseObject(parentDeptStr, Depart.class);
//						tmpVhcl.setDepartment(deptParent.getName() + "-" + dept.getName());
//					} else {
//						tmpVhcl.setDepartment(dept.getName());
//					}
//				}
//			}
//		}
	}

	/**
	 * 创建车辆
	 * 
	 * @param vhcl
	 * @return
	 */
	public Result<Void> createVhclManagement(VhclManagement vhcl) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		// 终端号为先填
		if (vhcl.getTernimalId() != null) {
			EnforceTerminal tempTerminal = enforceTerminalBiz.getById(vhcl.getTernimalId());
			if (tempTerminal == null) {
				result.setMessage("终端终端不存在");
				return result;
			}
		}

		VhclManagement tempVhcl = vhclManagementBiz.getByVehicleNum(vhcl.getVehicleNum());
		if (tempVhcl != null) {
			result.setMessage("终端车辆号已存在");
			return result;
		}
		// 终端部门为先填
		if (StringUtils.isNotBlank(vhcl.getDepartment())) {
			if (!StringUtil.isEmpty(vhcl.getDepartment())) {
				Map<String, String> depart = adminFeign.getDepart(vhcl.getDepartment());

				if (depart == null || depart.isEmpty()) {
					result.setMessage("终端部门不存在");
					return result;
				}
			}
		}

		vhclManagementBiz.insertSelective(vhcl);

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 修改车辆
	 * 
	 * @param vhcl
	 * @return
	 */
	public Result<Void> updateVhclManagement(VhclManagement vhcl) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		if (vhcl.getTernimalId() != null) {
			EnforceTerminal tempTerminal = enforceTerminalBiz.getById(vhcl.getTernimalId());
			if (tempTerminal == null) {
				result.setMessage("终端号码不存在");
				return result;
			}
		}

		VhclManagement tempVhcl = vhclManagementBiz.getByVehicleNum(vhcl.getVehicleNum());
		if (tempVhcl != null && !tempVhcl.getId().equals(vhcl.getId())) {
			result.setMessage("终端车俩号已存在");
			return result;
		}

		if (!StringUtil.isEmpty(vhcl.getDepartment())) {
			Map<String, String> depart = adminFeign.getDepart(vhcl.getDepartment());

			if (depart == null || depart.isEmpty()) {
				result.setMessage("终端部门不存在");
				return result;
			}
		}

		vhclManagementBiz.updateSelectiveById(vhcl);

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 获取单个车辆
	 * 
	 * @param id
	 * @return
	 */
	public VhclManagementVo get(int id) {
		VhclManagement vhcl = vhclManagementBiz.selectById(id);

		if (vhcl == null || vhcl.getIsDeleted().equals("1")) {
			return null;
		}

		List<VhclManagement> vhclList = new ArrayList<>();
		vhclList.add(vhcl);
		List<VhclManagementVo> voList = queryAssist(vhclList);
		return voList.get(0);
	}
}
