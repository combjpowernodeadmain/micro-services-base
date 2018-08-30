package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.CaseRegistration;

/**
 * @author 尚
 */
public class CaseRegistrationVo extends CaseRegistration {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6020857689965942741L;

	public String gridName;//所属网格名称
	public String eventTypeName;//事件类别名称
	public String getGridName() {
		return gridName;
	}
	public void setGridName(String gridName) {
		this.gridName = gridName;
	}
	public String getEventTypeName() {
		return eventTypeName;
	}
	public void setEventTypeName(String eventTypeName) {
		this.eventTypeName = eventTypeName;
	}
	
}
