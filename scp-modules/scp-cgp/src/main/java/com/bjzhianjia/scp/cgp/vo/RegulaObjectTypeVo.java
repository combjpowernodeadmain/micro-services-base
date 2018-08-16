package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;

/**
 * 
 * @author 尚
 *
 */
public class RegulaObjectTypeVo extends RegulaObjectType {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3849063492439285771L;

	private String parentObjectTypeName;

	public String getParentObjectTypeName() {
		return parentObjectTypeName;
	}

	public void setParentObjectTypeName(String parentObjectTypeName) {
		this.parentObjectTypeName = parentObjectTypeName;
	}
}
