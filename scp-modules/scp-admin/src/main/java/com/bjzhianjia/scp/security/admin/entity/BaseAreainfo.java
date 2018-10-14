package com.bjzhianjia.scp.security.admin.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;


/**
 * BaseAreainfo 区县行政编码字典表.
 *
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018年10月12日          bo      chenshuai            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
@Table(name = "base_areainfo")
public class BaseAreainfo implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private Integer id;

    //名称
    @Column(name = "name")
    private String name;

    //层级标识： 1  省份， 2  市， 3  区县
    @Column(name = "arealevel")
    private Integer arealevel;

    //父节点
    @Column(name = "parent_id")
    private Integer parentId;


    /**
     * 设置：
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置：名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：层级标识： 1  省份， 2  市， 3  区县
     */
    public void setArealevel(Integer arealevel) {
        this.arealevel = arealevel;
    }

    /**
     * 获取：层级标识： 1  省份， 2  市， 3  区县
     */
    public Integer getArealevel() {
        return arealevel;
    }

    /**
     * 设置：父节点
     */
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取：父节点
     */
    public Integer getParentId() {
        return parentId;
    }
}
