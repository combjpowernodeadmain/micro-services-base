package com.bjzhianjia.scp.security.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.bjzhianjia.scp.security.admin.feign.DictFeign;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.admin.biz.UserBiz;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.mapper.PositionMapper;
import com.bjzhianjia.scp.security.admin.vo.PositionVo;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

@Service
public class UserService {
	@Autowired
	private UserBiz userBiz;
	@Autowired
	private PositionMapper positionMapper;

	@Autowired
	private DictFeign dictFeign;

	public JSONArray getUsersByName(String name) {
	    List<User> rows = userBiz.getUsersByFakeName(name,null);
		List<JSONObject> jsonObjects = JSON.parseArray(JSON.toJSONString(rows), JSONObject.class);
		JSONArray jsonArray = bindPositionToUser(jsonObjects);
		if(jsonArray == null) {
		    jsonArray = new JSONArray();
		}
		return jsonArray;
	}

	public JSONArray getByNameAndParty(String name, String isPartyMember) {
		List<User> rows = userBiz.getUsersByFakeName(name, isPartyMember);
		List<JSONObject> jsonObjects = JSON.parseArray(JSON.toJSONString(rows), JSONObject.class);
		JSONArray jsonArray = bindPositionToUser(jsonObjects);
		if (jsonArray == null) {
			jsonArray = new JSONArray();
		}
		return jsonArray;
	}

	/**
	 * 将人员与其所在的职位进行绑定
	 * 
	 * @author 尚
	 * @param userList
	 * @return
	 */
	private JSONArray bindPositionToUser(List<JSONObject> userList) {

		List<String> userIdList =
				userList.stream().map((o) -> o.getString("id")).distinct().collect(Collectors.toList());

		String join = String.join(",", userIdList);
		join = "'" + join.replaceAll(",", "','") + "'";

		List<PositionVo> positionMapList = positionMapper.selectPositionByUser(join);

//		Map<String, String> departMap = departBiz.getDeparts(String.join(",", userIdList));
		JSONArray userListJsonArray = JSONArray.parseArray(JSON.toJSONString(userList));

		if (positionMapList != null && positionMapList.size() > 0) {
			for (int i = 0; i < userListJsonArray.size(); i++) {
				JSONObject userJsonObject = userListJsonArray.getJSONObject(i);
				List<String> positionNameList = new ArrayList<>();
				for (int j = 0; j < positionMapList.size(); j++) {
					PositionVo positionVo = positionMapList.get(j);
					if (positionVo.getUserId().equals(userJsonObject.getString("id"))) {
						if(StringUtils.isNotBlank(positionVo.getName())) {
							positionNameList.add(positionVo.getName());
						}
					}
				}
				boolean flag=positionNameList.isEmpty();
				userJsonObject.put("position", positionNameList.isEmpty() ? "" : String.join(",", positionNameList));
			}
		}
		return userListJsonArray;
	}

	/**
	 * 根据部门获取人员列表
	 * 
	 * @author 尚
	 * @param deptId
	 * @return
	 */
	public JSONObject getUserByDept(String deptId, int page, int limit) {
		
		
		TableResultResponse<JSONObject> result = userBiz.getUserByDept(deptId, page, limit);
		TableResultResponse<JSONObject>.TableData<JSONObject> data = result.getData();

		JSONObject object = new JSONObject();
		List<JSONObject> rows = data.getRows();
		if(BeanUtils.isEmpty(rows)){
			object.put("total", 0);
			object.put("row", new JSONArray());
		}

		Map<String, String> sexValue = dictFeign.getByCode("comm_sex");
		if(BeanUtils.isNotEmpty(sexValue)){
			rows.forEach(row-> row.put("sex", sexValue.get(row.getString("sex"))));
		}

		long total = data.getTotal();

		object.put("total", total);
		JSONArray jsonArray = bindPositionToUser(rows);
		object.put("row", jsonArray);

		return object;
	}
}
