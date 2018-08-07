package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.security.common.vo.TreeNodeVO;

/**
 * 网格树
 * 
 * @author 尚
 *
 */
public class AreaGridTree extends TreeNodeVO<AreaGridTree> {
	private String gridName;
	private String gridCode;

	public String getGridName() {
		return gridName;
	}

	public void setGridName(String gridName) {
		this.gridName = gridName;
	}

	public String getGridCode() {
		return gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	public AreaGridTree(Object id, Object parentId, String gridName, String gridCode) {
		this.gridName = gridName;
		this.gridCode = gridCode;
		super.setId(id);
		super.setParentId(parentId);
	}

	public AreaGridTree() {
	}
}
