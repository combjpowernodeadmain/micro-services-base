package com.bjzhianjia.scp.security.admin.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.admin.biz.DepartBiz;
import com.bjzhianjia.scp.security.admin.biz.PositionBiz;
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
	private DepartBiz departBiz;
	@Autowired
	private PositionMapper positionMapper;
	@Autowired
	private PositionBiz positionBiz;

	public JSONObject getUsersByName(String name, int page, int limit) {
		TableResultResponse<User> result = userBiz.getUsersByFakeName(name, page, limit);
		TableResultResponse<User>.TableData<User> data = result.getData();
		List<User> rows = data.getRows();
		long total = data.getTotal();

		JSONArray jsonArray = bindPositionToUser(rows);

		JSONObject jsonObject = new JSONObject();
		jsonObject.put("total", total);
		jsonObject.put("rows", jsonArray);
		return jsonObject;
	}

	/**
	 * 将人员与其所在的职位进行绑定
	 * 
	 * @author 尚
	 * @param userList
	 * @return
	 */
	private JSONArray bindPositionToUser(List<User> userList) {

		List<String> userIdList = userList.stream().map((o) -> o.getId()).distinct().collect(Collectors.toList());

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
		
		
		TableResultResponse<User> result = userBiz.getUserByDept(deptId, page, limit);
		TableResultResponse<User>.TableData<User> data = result.getData();

		List<User> rows = data.getRows();
		long total = data.getTotal();

		JSONObject object = new JSONObject();
		object.put("total", total);
		JSONArray jsonArray = bindPositionToUser(rows);
		object.put("row", jsonArray);

		return object;
	}
}
