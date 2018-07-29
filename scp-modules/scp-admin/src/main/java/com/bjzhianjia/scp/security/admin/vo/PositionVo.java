package com.bjzhianjia.scp.security.admin.vo;

import com.bjzhianjia.scp.security.admin.entity.Position;

/**
 * 创建该类之初用于按用户查询岗位功能<br/>
 * userId做为标识，将Position对象聚和到相应属性中<br/>
 * 亦可做其它扩展
 * @author 尚
 *
 */
public class PositionVo extends Position {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7750297643077584487L;
	private String userId;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
}
