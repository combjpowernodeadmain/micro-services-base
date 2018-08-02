package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.cgp.entity.AreaGrid;

/**
 * 
 * @author 尚
 *
 */
public class AreaGridVo extends AreaGrid {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1429378436730890658L;
	private String gridParentName;
	public String getGridParentName() {
		return gridParentName;
	}
	public void setGridParentName(String gridParentName) {
		this.gridParentName = gridParentName;
	}
}
