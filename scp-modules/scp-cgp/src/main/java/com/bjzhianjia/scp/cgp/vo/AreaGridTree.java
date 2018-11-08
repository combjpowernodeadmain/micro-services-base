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
	private String gridWithParentName;// 带有父节点名称的节点名称，如：第三网格(社士村)

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

	public AreaGridTree(Object id, Object parentId, String gridName, String gridCode, String gridWithParentName) {
		this.gridName = gridName;
		this.gridCode = gridCode;
		this.gridWithParentName = gridWithParentName;
		super.setId(id);
		super.setParentId(parentId);
	}

	public AreaGridTree() {
	}

    public String getGridWithParentName() {
        return gridWithParentName;
    }

    public void setGridWithParentName(String gridWithParentName) {
        this.gridWithParentName = gridWithParentName;
    }
}
