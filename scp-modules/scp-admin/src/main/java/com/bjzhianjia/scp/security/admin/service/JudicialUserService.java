package com.bjzhianjia.scp.security.admin.service;

import com.bjzhianjia.scp.security.admin.biz.DepartBiz;
import com.bjzhianjia.scp.security.admin.biz.JudicialUserBiz;
import com.bjzhianjia.scp.security.admin.entity.Depart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * JudicialUserController 司法用户.
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/9/29          admin      1.0            ADD
 * </pre>
 *
 * @author admin
 * @version 1.0
 */
@Service
public class JudicialUserService {

	@Autowired
	private JudicialUserBiz judicialUserBiz;
	@Autowired
	private DepartBiz departBiz;

	/**
	 * 案卷登记获取处理部门id
	 *
	 * @param departId 当前所属部门id
	 * @param groupId 角色id
	 * @param professional 专业id（数据字典code）
	 * @param isOwn 是否包含当前部门。（1 包含|0不包含）
	 * @return
	 *         -1 没有可以处理的部门
	 */
	public String getCaseDeal(String departId, String groupId, String professional,Integer isOwn) {
		//没有可以处理的检察院（部门）
		String notDepart = "-1";

		Depart depart = departBiz.selectById(departId);
		if(depart == null){
			return notDepart;
		}else{
			//不包含，跳过当前部门判断
			if(isOwn == 0){
				departId = depart.getParentId();
			}
		}

		//分案人所在部门id
		String caseDealId = "0";
		while ("0".equals(caseDealId)) {
			depart = departBiz.selectById(departId);
			if (depart != null) {
				Integer count = judicialUserBiz.getUserByDepartAndGroup(depart.getId(), groupId, professional);
				if (count > 0) {
					caseDealId = depart.getId();
					break;
				}
				departId = depart.getParentId();
			} else {
				//没有可以处理的部门
				caseDealId = notDepart;
				break;
			}
		}
		return caseDealId;
	}
}
