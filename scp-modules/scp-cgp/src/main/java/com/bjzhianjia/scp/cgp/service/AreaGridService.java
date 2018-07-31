package com.bjzhianjia.scp.cgp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.vo.AreaGridVo;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

/**
 * 
 * @author 尚
 *
 */
@Service
public class AreaGridService {
	@Autowired
	private AdminFeign adminFeign;
	@Autowired
	private AreaGridBiz areaGridBiz;
	@Autowired
	private DictFeign dictFeign;
	
	public Result<Void> createAreaGrid(AreaGrid areaGrid){
		Result<Void> result=new Result<>();
		result.setIsSuccess(false);
		
		//验证所选管理部门是否存在
		if(StringUtils.isNotBlank(areaGrid.getMgrDept())) {
			Map<String, String> depart = adminFeign.getDepart(areaGrid.getMgrDept());
			if(depart==null||depart.isEmpty()) {
				result.setMessage("所增添部门不存在");
				return result;
			}
		}
		
		//验证当前网格编码的唯一性
		Map<String, String> conditions=new HashMap<>();
		if(StringUtils.isNotBlank(areaGrid.getGridCode())) {
			conditions.put("gridCode", areaGrid.getGridCode());
			List<AreaGrid> areaGridList = areaGridBiz.getByConditions(conditions);
			if(areaGridList!=null) {
				if(areaGridList.size()>0) {
					result.setMessage("网格编号已存在");
					return result;
				}
			}
		}
		
		//验证当前网格名称的唯一性
		conditions.clear();
		if(StringUtils.isNotBlank(areaGrid.getGridName())) {
			conditions.put("gridName", areaGrid.getGridName());
			List<AreaGrid> areaGridList = areaGridBiz.getByConditions(conditions);
			if(areaGridList!=null) {
				if(areaGridList.size()>0) {
					result.setMessage("网格名称已存在");
					return result;
				}
			}
		}
		
		areaGridBiz.insertSelective(areaGrid);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		
		return result;
	}
	
	/**
	 * 更新单个对象
	 * @author 尚
	 * @param areaGrid
	 * @return
	 */
	public Result<Void> updateAreaGrid(AreaGrid areaGrid){
		Result<Void> result=new Result<>();
		result.setIsSuccess(false);
		
		//验证所选管理部门是否存在
		if(StringUtils.isNotBlank(areaGrid.getMgrDept())) {
			Map<String, String> depart = adminFeign.getDepart(areaGrid.getMgrDept());
			if(depart==null||depart.isEmpty()) {
				result.setMessage("所增添部门不存在");
				return result;
			}
		}
		
		//验证当前网格编码的唯一性
		Map<String, String> conditions=new HashMap<>();
		if(StringUtils.isNotBlank(areaGrid.getGridCode())) {
			conditions.put("gridCode", areaGrid.getGridCode());
			List<AreaGrid> areaGridList = areaGridBiz.getByConditions(conditions);
			if(areaGridList!=null) {
				boolean flag=false;
				for (AreaGrid tmp : areaGridList) {
					if(!tmp.getId().equals(areaGrid.getId())) {
						flag=true;
						break;
					}
				}
				if(flag) {
					result.setMessage("网格编号已存在");
					return result;
				}
			}
		}
		
		//验证当前网格名称的唯一性
		conditions.clear();
		if(StringUtils.isNotBlank(areaGrid.getGridName())) {
			conditions.put("gridName", areaGrid.getGridName());
			List<AreaGrid> areaGridList = areaGridBiz.getByConditions(conditions);
			if(areaGridList!=null) {
				boolean flag=false;
				for (AreaGrid tmp : areaGridList) {
					if(!tmp.getId().equals(areaGrid.getId())) {
						flag=true;
						break;
					}
				}
				if(flag) {
					result.setMessage("网格编号已存在");
					return result;
				}
			}
		}
		
		areaGridBiz.updateSelectiveById(areaGrid);
		
		result.setIsSuccess(true);
		result.setMessage("成功");
		
		return result;
	}
	
	/**
	 * 分页按条件获取网格数据
	 * @author 尚
	 * @param page
	 * @param limit
	 * @param areaGrid
	 * @return
	 */
	public TableResultResponse<AreaGridVo> getList(int page,int limit,AreaGrid areaGrid){
		TableResultResponse<AreaGridVo> tableResult=areaGridBiz.getList(page, limit, areaGrid);
		
		List<AreaGridVo> rows=tableResult.getData().getRows();
		
		//如果网格有上级网格，则进行聚和
		List<Integer> gridParentIdList = rows.stream().map((o)->o.getGridParent()).distinct().collect(Collectors.toList());
		if(gridParentIdList!=null&&!gridParentIdList.isEmpty()) {
			List<AreaGrid> areaGridParentList = areaGridBiz.getByIds(gridParentIdList);
			for(AreaGridVo tmp1:rows) {
				for(AreaGrid areaGridParent:areaGridParentList) {
					if(tmp1.getGridParent().equals(areaGridParent.getId())) {
						tmp1.setGridParentName(areaGridParent.getGridName());
						break;
					}
				}
			}
		}
		
		//聚积部门名称
		List<String> mrgDeptIdList = rows.stream().map((o)->o.getMgrDept()).distinct().collect(Collectors.toList());
		if(mrgDeptIdList!=null&&!mrgDeptIdList.isEmpty()) {
			Map<String, String> departMap = adminFeign.getDepart(String.join(",", mrgDeptIdList));
			if(departMap!=null&&!departMap.isEmpty()) {
				for(AreaGridVo vo:rows) {
					String string = departMap.get(vo.getMgrDept());
					if(StringUtils.isNotBlank(string)) {
						JSONObject jsonObject = JSONObject.parseObject(string);
						vo.setMgrDept(jsonObject.getString("name"));
					}
				}
			}
		}
		
		return tableResult;
	}
	
	/**
	 * 按网格等级获取网格列表
	 * @author 尚
	 * @param areaGrid
	 * @return
	 */
	public JSONArray getByGridLevel(String gridLevel){
		JSONArray result=new JSONArray();
		
		List<AreaGrid> tableResult=areaGridBiz.getByGridLevel(gridLevel);
		List<String> gridLevelIdList = tableResult.stream().map((o)->o.getGridLevel()).distinct().collect(Collectors.toList());
		
		if(gridLevelIdList!=null&&!gridLevelIdList.isEmpty()) {
			Map<String, String> gridLevelListMap = dictFeign.getDictValueByID(String.join(",", gridLevelIdList));
			for(AreaGrid areaGrid:tableResult) {
				JSONObject object=new JSONObject();
				object.put("areaId", areaGrid.getId());
				
				String string = gridLevelListMap.get(areaGrid.getGridLevel());
				object.put("gridLevel", JSONObject.parseObject(string).getString("labelDefault"));
				result.add(object);
			}
		}
		return result;
	}
}
