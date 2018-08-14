package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bjzhianjia.scp.cgp.entity.Depart;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;

/**
 * 
 * @author 尚
 *
 */
@Service
public class DeptUtilBiz {
	@Autowired
	private AdminFeign adminFeign;

	/**
	 * 将deptId封装进list集合，如果deptId部门下包括子部门，则将其子部门ID也封装进list集合
	 * 
	 * @author 尚
	 * @param deptId
	 * @return
	 */
	public List<String> getDeptIds(String deptId) {
		List<String> result = new ArrayList<>();
		result.add(deptId);

//		Map<String, String> departMap = adminFeign.getDepart(deptId);
//		if (departMap != null && departMap.size() > 0) {
//			Set<String> keySet = departMap.keySet();
//			for (String string : keySet) {
//				JSONArray deptJsonArray = adminFeign.getDepartByParent(string);
//				for (int i = 0; i < deptJsonArray.size(); i++) {
//					result.add(deptJsonArray.getJSONObject(i).getString("id"));
//				}
//			}
//		}
		
		//部门之间会存在多级关系，所以采用递归方式查询，弃用以上注释掉代码
		getDeptIdsAssist(deptId, result);
		
		return result;
	}
	
	/**
	 * 查询superDeptID下的所有子部门(包括孙部门)<br/>
	 * 
	 * @author 尚
	 * @param superDeptId
	 * @param result
	 */
	private void getDeptIdsAssist(String superDeptId,List<String> result) {
		Map<String, String> departMap = adminFeign.getDepart(superDeptId);
		if (departMap != null && departMap.size() > 0) {
			Set<String> keySet = departMap.keySet();
			for (String string : keySet) {
				JSONArray deptJsonArray = adminFeign.getDepartByParent(string);
				for (int i = 0; i < deptJsonArray.size(); i++) {
					String subId = deptJsonArray.getJSONObject(i).getString("id");
					result.add(subId);
					getDeptIdsAssist(subId,result);
				}
			}
		}
	}
}
