package com.bjzhianjia.scp.security.admin.vo;

import com.bjzhianjia.scp.security.common.vo.TreeNodeVO;

/**
 * @author scp
 * @create 2018/2/4.
 */
public class DepartTree extends TreeNodeVO<DepartTree> {
    String label;
    String code;
    private String orderNum;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public DepartTree(){

    }
    public DepartTree(Object id, Object parentId, String label, String code) {
        this.label = label;
        this.code = code;
        this.setId(id);
        this.setParentId(parentId);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
