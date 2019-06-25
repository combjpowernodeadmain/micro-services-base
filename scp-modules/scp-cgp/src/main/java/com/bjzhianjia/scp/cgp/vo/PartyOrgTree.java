package com.bjzhianjia.scp.cgp.vo;

import com.bjzhianjia.scp.security.common.vo.TreeNodeVO;

/**
 * PartyOrgTree 党组织树结构.
 *
 * @author chenshuai
 * @version 1.0
 * @date 2019-06-25
 */
public class PartyOrgTree extends TreeNodeVO<AreaGridTree> {
    /**
     * 节点标签
     */
    private String label;

    /**
     * 默认构造方法
     */
    public PartyOrgTree() {
    }

    /**
     * 初始化节点，默认为展开
     *
     * @param id       节点id
     * @param parentId 父节点id
     * @param label    节点标签
     */
    public PartyOrgTree(Object id, Object parentId, String label) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
    }

    /**
     * 初始化节点
     *
     * @param id       节点id
     * @param parentId 父节点id
     * @param label    节点标签
     */
    public PartyOrgTree(Object id, Object parentId, String label, boolean expand) {
        this.id = id;
        this.parentId = parentId;
        this.label = label;
    }

    /**
     * 获取：节点标签
     *
     * @return
     */
    public String getLabel() {
        return label;
    }

    /**
     * 设置：节点标签
     *
     * @return
     */
    public void setLabel(String title) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "DictTypeTreeVO{" +
                "label='" + label + '\'' +
                ", id=" + id +
                ", parentId=" + parentId +
                ", children=" + getChildren() +
                '}';
    }
}
