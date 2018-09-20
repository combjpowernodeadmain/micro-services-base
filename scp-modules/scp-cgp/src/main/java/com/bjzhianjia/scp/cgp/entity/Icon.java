package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;


/**
 * Icon 图标实体.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月19日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshuai
 *
 */
@Table(name = "icon")
public class Icon implements Serializable {

    private static final long serialVersionUID = 1L;

    //
    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    // 图标标题
    @Column(name = "icon_title")
    private String iconTitle;

    // 图标值
    @Column(name = "icon_value")
    private String iconValue;

    // 图标分组[数据字典code]
    @Column(name = "group_code")
    private String groupCode;

    // 图标类型[1图片|2...]
    @Column(name = "type_code")
    private String typeCode;

    // 是否删除[1是|0否]
    @Column(name = "is_deleted")
    private String isDeleted;

    // 创建时间
    @Column(name = "crt_time")
    private Date crtTime;

    // 创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;

    // 创建人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;

    // 更新时间
    @Column(name = "upd_time")
    private Date updTime;

    // 更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;

    // 更新人姓名
    @Column(name = "upd_user_name")
    private String updUserName;

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
     * 设置：图标标题
     */
    public void setIconTitle(String iconTitle) {
        this.iconTitle = iconTitle;
    }

    /**
     * 获取：图标标题
     */
    public String getIconTitle() {
        return iconTitle;
    }

    /**
     * 设置：图标值
     */
    public void setIconValue(String iconValue) {
        this.iconValue = iconValue;
    }

    /**
     * 获取：图标值
     */
    public String getIconValue() {
        return iconValue;
    }

    /**
     * 设置：图标分组[数据字典code]
     */
    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    /**
     * 获取：图标分组[数据字典code]
     */
    public String getGroupCode() {
        return groupCode;
    }

    /**
     * 设置：图标类型[1图片|2...]
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * 获取：图标类型[1图片|2...]
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * 设置：是否删除[1是|0否]
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取：是否删除[1是|0否]
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置：创建时间
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * 获取：创建时间
     */
    public Date getCrtTime() {
        return crtTime;
    }

    /**
     * 设置：创建人ID
     */
    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    /**
     * 获取：创建人ID
     */
    public String getCrtUserId() {
        return crtUserId;
    }

    /**
     * 设置：创建人姓名
     */
    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    /**
     * 获取：创建人姓名
     */
    public String getCrtUserName() {
        return crtUserName;
    }

    /**
     * 设置：更新时间
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * 获取：更新时间
     */
    public Date getUpdTime() {
        return updTime;
    }

    /**
     * 设置：更新人ID
     */
    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    /**
     * 获取：更新人ID
     */
    public String getUpdUserId() {
        return updUserId;
    }

    /**
     * 设置：更新人姓名
     */
    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }

    /**
     * 获取：更新人姓名
     */
    public String getUpdUserName() {
        return updUserName;
    }
}
