package com.bjzhianjia.scp.cgp.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;


/**
 * AffairCache 第三方事件数据缓存.
 *
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018年12月4日          chenshuai      1.0            ADD
 * </pre>
 *
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@Table(name = "affair_cache")
public class AffairCache implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    //事件编号
    @Column(name = "affair_code")
    private String affairCode;

    //事项名称
    @Column(name = "affair_name")
    private String affairName;

    //受理时间
    @Column(name = "accept_time")
    private Date acceptTime;

    //经办人
    @Column(name = "agent_name")
    private String agentName;

    //办事人
    @Column(name = "clerk_name")
    private String clerkName;

    //当前状态
    @Column(name = "state")
    private String state;

    //记录时间
    @Column(name = "crt_time")
    private Date crtTime;


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
     * 设置：事件编号
     */
    public void setAffairCode(String affairCode) {
        this.affairCode = affairCode;
    }

    /**
     * 获取：事件编号
     */
    public String getAffairCode() {
        return affairCode;
    }

    /**
     * 设置：事项名称
     */
    public void setAffairName(String affairName) {
        this.affairName = affairName;
    }

    /**
     * 获取：事项名称
     */
    public String getAffairName() {
        return affairName;
    }

    /**
     * 设置：受理时间
     */
    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    /**
     * 获取：受理时间
     */
    public Date getAcceptTime() {
        return acceptTime;
    }

    /**
     * 设置：经办人
     */
    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    /**
     * 获取：经办人
     */
    public String getAgentName() {
        return agentName;
    }

    /**
     * 设置：办事人
     */
    public void setClerkName(String clerkName) {
        this.clerkName = clerkName;
    }

    /**
     * 获取：办事人
     */
    public String getClerkName() {
        return clerkName;
    }

    /**
     * 设置：当前状态
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * 获取：当前状态
     */
    public String getState() {
        return state;
    }

    /**
     * 设置：记录时间
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * 获取：记录时间
     */
    public Date getCrtTime() {
        return crtTime;
    }
}
