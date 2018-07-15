package com.bjzhianjia.scp.cgp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.biz.EnforceTerminalBiz;
import com.bjzhianjia.scp.cgp.biz.VhclManagementBiz;
import com.bjzhianjia.scp.cgp.entity.EnforceTerminal;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.VhclManagement;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 车辆管理服务
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
	private AdminFeign adminFeign;
	
	@Autowired
	private MergeCore mergeCore;
	
	/**
	 * 分页查询
	 * @param page 当前页
	 * @param limit 页大小
	 * @param terminal 查询条件
	 * @return
	 */
	public TableResultResponse<VhclManagement> getList(int page, int limit, VhclManagement vhcl) {
		
		TableResultResponse<VhclManagement> tableResult = vhclManagementBiz.getList(page, limit, vhcl);
		
		Map<String,String> departs = new HashMap<>();
		List<VhclManagement> list = tableResult.getData().getRows();
		List<String> uniqueDeparts = list.stream().map((o)->o.getDepartment()).distinct().collect(Collectors.toList());
		if(uniqueDeparts != null && !uniqueDeparts.isEmpty()) {
			departs = adminFeign.getDepart(String.join(",", uniqueDeparts));
		}
		
		if(!departs.isEmpty()) {
			for(VhclManagement tmpVhcl : list) {
				String departName = departs.get(tmpVhcl.getDepartment());
				tmpVhcl.setDepartment(departName);
			}
		}
		
		try {
			mergeCore.mergeResult(VhclManagement.class, list);
		} catch(Exception ex) {
			log.error("merge data exception", ex);
		}
		
		return tableResult;
	}
	/**
	 * 创建车辆
	 * @param vhcl
	 * @return
	 */
	public Result<Void> createVhclManagement(VhclManagement vhcl) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		EnforceTerminal tempTerminal = enforceTerminalBiz.getById(vhcl.getTernimalId());
		if(tempTerminal == null) {
			result.setMessage("终端终端不存在");
			return result;
		}
		
		VhclManagement tempVhcl = vhclManagementBiz.getByVehicleNum(vhcl.getVehicleNum());
		if(tempVhcl != null) {
			result.setMessage("终端车辆号已存在");
			return result;
		}
		
		if(!StringUtil.isEmpty(vhcl.getDepartment())) {
			Map<String,String> depart = adminFeign.getDepart(vhcl.getDepartment());
			
			if(depart == null || depart.isEmpty()) {
				result.setMessage("终端部门不存在");
				return result;
			}
		}
		
		vhclManagementBiz.insertSelective(vhcl);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
	
	/**
	 * 修改车辆
	 * @param vhcl
	 * @return
	 */
	public Result<Void> updateVhclManagement(VhclManagement vhcl) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		EnforceTerminal tempTerminal = enforceTerminalBiz.getById(vhcl.getTernimalId());
		if(tempTerminal == null) {
			result.setMessage("终端终端不存在");
			return result;
		}
		
		VhclManagement tempVhcl = vhclManagementBiz.getByVehicleNum(vhcl.getVehicleNum());
		if(tempVhcl != null && !tempVhcl.getId().equals(vhcl.getId())) {
			result.setMessage("终端车俩号已存在");
			return result;
		}
		
		if(!StringUtil.isEmpty(vhcl.getDepartment())) {
			Map<String,String> depart = adminFeign.getDepart(vhcl.getDepartment());
			
			if(depart == null || depart.isEmpty()) {
				result.setMessage("终端部门不存在");
				return result;
			}
		}
		
		vhclManagementBiz.updateSelectiveById(vhcl);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
}
