package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 * LawEnforcePath 执法轨迹记录
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月4日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@Table(name = "law_enforce_path")
public class LawEnforcePath implements Serializable {

    private static final long serialVersionUID = 1L;

    // 主键
    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    // 经度
    @Column(name = "lng")
    private Double lng;

    // 纬度
    @Column(name = "lat")
    private Double lat;

    // 终端ID
    @Column(name = "terminal_id")
    private Integer terminalId;

    // 上报人(被记录轨迹的人)
    @Column(name = "crt_user_id")
    private String crtUserId;

    // 上报人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;

    // 创建时间
    @Column(name = "crt_time")
    private Date crtTime;

    // 部门ID
    @Column(name = "dept_id")
    private String deptId;

    // 租户ID
    @Column(name = "tanent_id")
    private String tanentId;

    /**
     * 该点是否要展示
     * 1:在网格范围内的
     * 0:不在网格范围内
     */
    @Column(name = "is_enable")
    private String isEnable;

    /**
     * 设置：主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * 设置：经度
     */
    public void setLng(Double lng) {
        this.lng = lng;
    }

    /**
     * 获取：经度
     */
    public Double getLng() {
        return lng;
    }

    /**
     * 设置：纬度
     */
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * 获取：纬度
     */
    public Double getLat() {
        return lat;
    }

    /**
     * 设置：终端ID
     */
    public void setTerminalId(Integer terminalId) {
        this.terminalId = terminalId;
    }

    /**
     * 获取：终端ID
     */
    public Integer getTerminalId() {
        return terminalId;
    }

    /**
     * 设置：上报人(被记录轨迹的人)
     */
    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    /**
     * 获取：上报人(被记录轨迹的人)
     */
    public String getCrtUserId() {
        return crtUserId;
    }

    /**
     * 设置：上报人姓名
     */
    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    /**
     * 获取：上报人姓名
     */
    public String getCrtUserName() {
        return crtUserName;
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
     * 设置：部门ID
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * 获取：部门ID
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * 设置：租户ID
     */
    public void setTanentId(String tanentId) {
        this.tanentId = tanentId;
    }

    /**
     * 获取：租户ID
     */
    public String getTanentId() {
        return tanentId;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
}
