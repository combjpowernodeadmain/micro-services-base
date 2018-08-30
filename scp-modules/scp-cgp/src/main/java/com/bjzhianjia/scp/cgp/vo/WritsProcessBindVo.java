package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.WritsProcessBind;

/**
 * @author 尚
 */
public class WritsProcessBindVo extends WritsProcessBind {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1798068232755789510L;

	private String writsName;//该节点下文本名称

	public String getWritsName() {
		return writsName;
	}

	public void setWritsName(String writsName) {
		this.writsName = writsName;
	}
	
	
}
