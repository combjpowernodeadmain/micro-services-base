package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.AreaGridMemberBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.AreaGridMemberMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.vo.AreaGridVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

/**
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class AreaGridService {
	@Autowired
	private AdminFeign adminFeign;
	@Autowired
	private AreaGridBiz areaGridBiz;
	@Autowired
	private DictFeign dictFeign;
	@Autowired
	private AreaGridMemberMapper areaGridMemberMapper;
	@Autowired
	private AreaGridMemberBiz areaGridMemberBiz;

	public Result<Void> createAreaGrid(JSONObject areaGridJObject) {
		AreaGrid areaGrid = JSON.parseObject(areaGridJObject.toJSONString(), AreaGrid.class);

		AreaGrid maxGrid = areaGridBiz.getMaxGrid();
		int CurrAreaGridId = -1;
		if (maxGrid == null) {
			CurrAreaGridId = 1;
		} else {
			CurrAreaGridId = maxGrid.getId() + 1;
		}
		areaGrid.setId(CurrAreaGridId);

		// 插入网格
		Result<Void> result = insertAreaGrid(areaGrid);
		if (!result.getIsSuccess()) {
			return result;
		}

		// 插入网格成员
		if (areaGridJObject.getBooleanValue("flag")) {
			Result<List<AreaGridMember>> resultM = insertAreaGridMember(areaGridJObject, areaGrid.getId());
			if (!resultM.getIsSuccess()) {
				result.setMessage(resultM.getMessage());
				return result;
			}

			List<AreaGridMember> areaGridMemberList = resultM.getData();
			areaGridMemberMapper.insertAreaGridMemberList(areaGridMemberList);
		}

		areaGridBiz.insertSelective(areaGrid);
		return result;
	}

	public Result<Void> insertAreaGrid(AreaGrid areaGrid) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		// 验证所选管理部门是否存在
		if (StringUtils.isNotBlank(areaGrid.getMgrDept())) {
			Map<String, String> depart = adminFeign.getDepart(areaGrid.getMgrDept());
			if (depart == null || depart.isEmpty()) {
				result.setMessage("所选部门不存在");
				return result;
			}
		}

		// 验证当前网格编码的唯一性
		Map<String, String> conditions = new HashMap<>();
		if (StringUtils.isNotBlank(areaGrid.getGridCode())) {
			conditions.put("gridCode", areaGrid.getGridCode());
			List<AreaGrid> areaGridList = areaGridBiz.getByMap(conditions);
			if (areaGridList != null) {
				if (areaGridList.size() > 0) {
					result.setMessage("网格编号已存在");
					return result;
				}
			}
		}

		// 验证当前网格名称的唯一性
		conditions.clear();
		if (StringUtils.isNotBlank(areaGrid.getGridName())) {
			conditions.put("gridName", areaGrid.getGridName());
			List<AreaGrid> areaGridList = areaGridBiz.getByMap(conditions);
			if (areaGridList != null) {
				if (areaGridList.size() > 0) {
					result.setMessage("网格名称已存在");
					return result;
				}
			}
		}

		if (areaGrid.getGridParent() == null) {
			areaGrid.setGridParent(-1);
		}

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	public Result<List<AreaGridMember>> insertAreaGridMember(JSONObject areaGridJObject, int gridId) {
		Result<List<AreaGridMember>> result = new Result<>();
		result.setIsSuccess(false);

		/*
		 * areaGridMember:[{'gridMember':'m11,m12','gridRole':'r1'},{'gridMember':'m2',
		 * 'gridRole':'r2'},{'gridMember':'m3','gridRole':'r3'},{'
		 * gridMember':'m4','gridRole':'r4'},{'gridMember':'m5',' gridRole':'r5'}]
		 */
		String areaGridMemberJArrayStr = areaGridJObject.getString("areaGridMember");
		JSONArray areaGridMemberJArray = JSONArray.parseArray(areaGridMemberJArrayStr);
		List<AreaGridMember> areaGridMemberList = new ArrayList<>();

		for (int i = 0; i < areaGridMemberJArray.size(); i++) {
			JSONObject areaGridMemJObject = areaGridMemberJArray.getJSONObject(i);

			AreaGridMember areaGridMember = new AreaGridMember();
			areaGridMember.setGridId(gridId);
			areaGridMember.setGridRole(areaGridMemJObject.getString("gridRole"));

			areaGridMember.setCrtUserName(BaseContextHandler.getUsername());
			areaGridMember.setCrtUserId(BaseContextHandler.getUserID());
			areaGridMember.setCrtTime(new Date());

			areaGridMember.setTenantId(BaseContextHandler.getTenantID());

			String areaGridMemStrs = areaGridMemJObject.getString("gridMember");
			if (StringUtils.isNotBlank(areaGridMemStrs)) {
				String[] split = areaGridMemStrs.split(",");
				for (String string : split) {
					AreaGridMember tmpMem = BeanUtil.copyBean_New(areaGridMember, new AreaGridMember());
					tmpMem.setGridMember(string);
					areaGridMemberList.add(tmpMem);
				}
			}
		}

		// 验证：同一网格-同一人-同一岗位
		List<AreaGridMember> areaGridMemberInDB = areaGridMemberBiz.getAreaGridMember(areaGridMemberList);
		if (!(areaGridMemberInDB == null || areaGridMemberInDB.isEmpty())) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("【").append(areaGridMemberInDB.get(0).getGridId()).append("-")
					.append(areaGridMemberInDB.get(0).getGridRole()).append("-")
					.append(areaGridMemberInDB.get(0).getGridMember()).append("】已存在");
			result.setMessage(buffer.toString());
			return result;
		}

		result.setData(areaGridMemberList);
		result.setIsSuccess(true);
		result.setMessage("成功");

		return result;
	}

	/**
	 * 
	 * 更新单个对象
	 * 
	 * @author 尚
	 * @param areaGrid
	 * @return
	 */
	public Result<Void> updateAreaGrid(JSONObject areaGridJObject) {
		AreaGrid areaGrid = JSONObject.parseObject(areaGridJObject.toJSONString(), AreaGrid.class);

		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		// 验证所选管理部门是否存在
		if (StringUtils.isNotBlank(areaGrid.getMgrDept())) {
			Map<String, String> depart = adminFeign.getDepart(areaGrid.getMgrDept());
			if (depart == null || depart.isEmpty()) {
				result.setMessage("所选部门不存在");
				return result;
			}
		}

		// 验证当前网格编码的唯一性
		Map<String, String> conditions = new HashMap<>();
		if (StringUtils.isNotBlank(areaGrid.getGridCode())) {
			conditions.put("gridCode", areaGrid.getGridCode());
			List<AreaGrid> areaGridList = areaGridBiz.getByMap(conditions);
			if (areaGridList != null) {
				boolean flag = false;
				for (AreaGrid tmp : areaGridList) {
					if (!tmp.getId().equals(areaGrid.getId())) {
						flag = true;
						break;
					}
				}
				if (flag) {
					result.setMessage("网格编号已存在");
					return result;
				}
			}
		}

		// 验证当前网格名称的唯一性
		conditions.clear();
		if (StringUtils.isNotBlank(areaGrid.getGridName())) {
			conditions.put("gridName", areaGrid.getGridName());
			List<AreaGrid> areaGridList = areaGridBiz.getByMap(conditions);
			if (areaGridList != null) {
				boolean flag = false;
				for (AreaGrid tmp : areaGridList) {
					if (!tmp.getId().equals(areaGrid.getId())) {
						flag = true;
						break;
					}
				}
				if (flag) {
					result.setMessage("网格编号已存在");
					return result;
				}
			}
		}

		if (areaGridJObject.getBooleanValue("flag")) {
			areaGridMemberBiz.deleteByGridId(areaGrid.getId());
			Result<List<AreaGridMember>> resultM = insertAreaGridMember(areaGridJObject, areaGrid.getId());
			if (!resultM.getIsSuccess()) {
				result.setMessage(resultM.getMessage());
				return result;
			}

			List<AreaGridMember> areaGridMemberList = resultM.getData();
			areaGridMemberMapper.insertAreaGridMemberList(areaGridMemberList);
		}

		areaGridBiz.updateSelectiveById(areaGrid);

		result.setIsSuccess(true);
		result.setMessage("成功");

		return result;
	}

	/**
	 * 分页按条件获取网格数据
	 * 
	 * @author 尚
	 * @param page
	 * @param limit
	 * @param areaGrid
	 * @return
	 */
	public TableResultResponse<AreaGridVo> getList(int page, int limit, AreaGrid areaGrid) {
		TableResultResponse<AreaGridVo> tableResult = areaGridBiz.getList(page, limit, areaGrid);

		List<AreaGridVo> rows = tableResult.getData().getRows();

		queryAssist(rows);

		return tableResult;
	}

	public void queryAssist(List<AreaGridVo> rows) {
		// 如果网格有上级网格，则进行聚和
		List<Integer> gridParentIdList = rows.stream().map((o) -> o.getGridParent()).distinct()
				.collect(Collectors.toList());
		if (gridParentIdList != null && !gridParentIdList.isEmpty()) {
			//查询上一级网格集合
			List<AreaGrid> areaGridParentList = areaGridBiz.getByIds(gridParentIdList);
			for (AreaGridVo tmp1 : rows) {
				for (AreaGrid areaGridParent : areaGridParentList) {
					if (tmp1.getGridParent().equals(areaGridParent.getId())) {
						//当前网格的上一级网格ID与刚查询到的上一级网格集合中某一个相同，由进行聚和
						tmp1.setGridParentName(areaGridParent.getGridName());
						break;
					}
				}
			}
		}

		// 聚积部门名称
		List<String> mrgDeptIdList = rows.stream().map((o) -> o.getMgrDept()).distinct().collect(Collectors.toList());
		if (mrgDeptIdList != null && !mrgDeptIdList.isEmpty()) {
			Map<String, String> departMap = adminFeign.getDepart(String.join(",", mrgDeptIdList));
			if (departMap != null && !departMap.isEmpty()) {
				for (AreaGridVo vo : rows) {
					String string = departMap.get(vo.getMgrDept());
					if (StringUtils.isNotBlank(string)) {
						JSONObject jsonObject = JSONObject.parseObject(string);
						vo.setMgrDept(jsonObject.getString("name"));
					}
				}
			}
		}
	}

	/**
	 * 按网格等级获取网格列表
	 * 
	 * @author 尚
	 * @param areaGrid
	 * @return
	 */
	public List<AreaGrid> getByGridLevel(String gridLevel) {

		List<AreaGrid> tableResult = areaGridBiz.getByGridLevel(gridLevel);

		return tableResult;
	}

	/**
	 * 按ID获取单个对象
	 * 
	 * @author 尚
	 * @param id
	 * @return
	 */
	public ObjectRestResponse<JSONObject> getOne(Integer id) {
		ObjectRestResponse<JSONObject> result = new ObjectRestResponse<>();
		AreaGrid areaGrid = areaGridBiz.selectById(id);

		if (areaGrid == null || areaGrid.getIsDeleted().equals("1")) {
			result.setStatus(400);
			result.setMessage("该记录不存在或已删除");
			return result;
		}

		AreaGridVo areaGridVo = BeanUtil.copyBean_New(areaGrid, new AreaGridVo());
		List<AreaGridVo> rows = new ArrayList<>();
		rows.add(areaGridVo);
		queryAssist(rows);

		// 查询网格是否有成员
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("gridId", areaGrid.getId());
		List<AreaGridMember> areaGridMemberList = areaGridMemberBiz.getByMap(conditions);

		// 处理回传信息中"gridMember"字段除非依然信息
		String gridMember = mergeGridMember(areaGridMemberList);

		JSONObject resultJObj = JSONObject.parseObject(JSON.toJSONString(areaGridVo));
		resultJObj.put("gridMember", gridMember);

		result.setData(resultJObj);
		return result;
	}

	public String mergeGridMember(List<AreaGridMember> areaGridMemberList) {
		String gridMember = "";
		if (areaGridMemberList != null && !areaGridMemberList.isEmpty()) {
			// 按岗位收集网格员，形式如--------"岗位":"网格员1，网格员2，网格员3"
			Map<String, List<String>> positionUserMap = new HashMap<>();
			for (AreaGridMember areaGridMember : areaGridMemberList) {
				List<String> pUList = positionUserMap.get(areaGridMember.getGridRole());
				if (pUList == null || pUList.isEmpty()) {
					pUList = new ArrayList<>();
					pUList.add(areaGridMember.getGridMember());
					positionUserMap.put(areaGridMember.getGridRole(), pUList);
				} else {
					pUList.add(areaGridMember.getGridMember());
				}
			}

			/*
			 * 说明该网格下有网格成员 回传前台数据：[{\
			 * "gridMember\":\"网格员ID1，网格员ID2\",\"gridRole\":\"岗位\",\"gridRoleName\":\"网格员\",\"gridMemberName\":\"网格员名1,网格员名2\"
			 * }]
			 */
			List<String> collect = areaGridMemberList.stream().map((o) -> o.getGridMember()).distinct()
					.collect(Collectors.toList());
			Map<String, String> userMap = adminFeign.getUser(String.join(",", collect));
			Map<String, String> gridMemberMap = dictFeign.getDictIds(Constances.ROOT_BIZ_GRID_ROLE);

			JSONArray memberJArray = new JSONArray();
			Set<String> keySet = positionUserMap.keySet();
			for (String gridRole : keySet) {
				// 以gridRold为基准，拼接需要回传的JSON数组
				JSONObject memberJObject = new JSONObject();
				memberJObject.put("gridRole", gridRole);
				memberJObject.put("gridRoleName", gridMemberMap.get(gridRole));

				// 同一岗位下的多个人员
				List<String> list = positionUserMap.get(gridRole);

				List<String> memberTmpList = new ArrayList<>();
				List<String> memberNameTmpList = new ArrayList<>();
				for (String gridMeber : list) {
					memberTmpList.add(gridMeber);
					String userJStr = userMap.get(gridMeber);
					JSONObject userJObj = JSONObject.parseObject(userJStr);
					memberNameTmpList.add(userJObj.getString("name"));
				}
				memberJObject.put("gridMember", String.join(",", memberTmpList));
				memberJObject.put("gridMemberName", String.join(",", memberNameTmpList));
				memberJArray.add(memberJObject);
			}
			gridMember = memberJArray.toJSONString();
		}
		return gridMember;
	}

	/**
	 * 删除网格<br/>
	 * 删除网格的同时，会将网格成员表中与之相对应的记录删除<br/>
	 * 删除采用逻辑删除
	 * 
	 * @author 尚
	 * @param id
	 * @return
	 */
	public Result<Void> removeOne(Integer id) {
		Result<Void> result = new Result<>();

		AreaGrid areaGrid = new AreaGrid();
		areaGrid.setId(id);
		areaGrid.setIsDeleted("1");
		this.areaGridBiz.updateSelectiveById(areaGrid);

		this.areaGridMemberMapper.deleteByGridId(id, BaseContextHandler.getUserID(), BaseContextHandler.getUsername(),
				new Date());

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}
}