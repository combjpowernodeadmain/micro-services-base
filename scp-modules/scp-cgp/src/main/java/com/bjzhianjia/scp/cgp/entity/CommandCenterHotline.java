package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.alibaba.fastjson.annotation.JSONField;
import com.bjzhianjia.scp.cgp.constances.CommonConstances;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.annonation.MergeField;

/**
 * 记录来自指挥中心热线的事件
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-09-28 19:20:36
 */
@Table(name = "command_center_hotline")
public class CommandCenterHotline implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 诉求类别--type
     */
    public static final String ROOT_BIZ_APPEALTYPE = "root_biz_appealType";

    // 主键
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    // 热线事项编号
    @Column(name = "hotln_code")
    private String hotlnCode;

    // 热线事项标题
    @Column(name = "hotln_title")
    private String hotlnTitle;

    // 业务条线(字典)
    @Column(name = "biz_type")
    @MergeField(key = Constances.ROOT_BIZ_TYPE, feign = DictFeign.class, method = "getByCode")
    private String bizType;

    // 事件类别
    @Column(name = "event_type")
    private Integer eventType;

    // 诉求类型(字典)
    @Column(name = "appeal_type")
    @MergeField(key = ROOT_BIZ_APPEALTYPE, feign = DictFeign.class, method = "getByCode")
    private String appealType;

    // 诉求人电话
    @Column(name = "appeal_tel")
    private String appealTel;

    // 诉求人姓名
    @Column(name = "appeal_person")
    private String appealPerson;

    // 诉求时间
    @Column(name = "appeal_datetime")
    @JSONField(format = CommonConstances.DATE_FORMAT_FULL)
    private Date appealDatetime;

    // 诉求内容
    @Column(name = "appeal_desc")
    private String appealDesc;

    // 反馈时间
    @Column(name = "reply_datetime")
    @JSONField(format = CommonConstances.DATE_FORMAT_FULL)
    private Date replyDatetime;

    // 处理状态
    @Column(name = "exe_status")
    // 借用市长热线的处理状态
    @MergeField(key = Constances.ROOT_BIZ_12345STATE, feign = DictFeign.class, method = "getByCode")
    private String exeStatus;

    // 是否删除(1:删除|0:未删除)
    @Column(name = "is_deleted")
    private String isDeleted;

    // 创建时间(上报时间)
    @Column(name = "crt_time")
    @JSONField(format = CommonConstances.DATE_FORMAT_FULL)
    private Date crtTime;

    // 创建人ID(上报人)
    @Column(name = "crt_user_id")
    private String crtUserId;

    // 创建人姓名
    @Column(name = "crt_user_name")
    private String crtUserName;

    // 更新时间
    @Column(name = "upd_time")
    @JSONField(format = CommonConstances.DATE_FORMAT_FULL)
    private Date updTime;

    // 更新人ID
    @Column(name = "upd_user_id")
    private String updUserId;

    // 更新人姓名
    @Column(name = "upd_user_name")
    private String updUserName;

    // 租房
    @Column(name = "tenant_id")
    private String tenantId;

    // 部门ID
    @Column(name = "dept_id")
    private String deptId;

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
     * 设置：热线事项编号
     */
    public void setHotlnCode(String hotlnCode) {
        this.hotlnCode = hotlnCode;
    }

    /**
     * 获取：热线事项编号
     */
    public String getHotlnCode() {
        return hotlnCode;
    }

    /**
     * 设置：热线事项标题
     */
    public void setHotlnTitle(String hotlnTitle) {
        this.hotlnTitle = hotlnTitle;
    }

    /**
     * 获取：热线事项标题
     */
    public String getHotlnTitle() {
        return hotlnTitle;
    }

    /**
     * 设置：业务条线(字典)
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    /**
     * 获取：业务条线(字典)
     */
    public String getBizType() {
        return bizType;
    }

    /**
     * 设置：事件类别
     */
    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    /**
     * 获取：事件类别
     */
    public Integer getEventType() {
        return eventType;
    }

    /**
     * 设置：诉求类型(字典)
     */
    public void setAppealType(String appealType) {
        this.appealType = appealType;
    }

    /**
     * 获取：诉求类型(字典)
     */
    public String getAppealType() {
        return appealType;
    }

    /**
     * 设置：诉求人电话
     */
    public void setAppealTel(String appealTel) {
        this.appealTel = appealTel;
    }

    /**
     * 获取：诉求人电话
     */
    public String getAppealTel() {
        return appealTel;
    }

    /**
     * 设置：诉求人姓名
     */
    public void setAppealPerson(String appealPerson) {
        this.appealPerson = appealPerson;
    }

    /**
     * 获取：诉求人姓名
     */
    public String getAppealPerson() {
        return appealPerson;
    }

    /**
     * 设置：诉求时间
     */
    public void setAppealDatetime(Date appealDatetime) {
        this.appealDatetime = appealDatetime;
    }

    /**
     * 获取：诉求时间
     */
    public Date getAppealDatetime() {
        return appealDatetime;
    }

    /**
     * 设置：诉求内容
     */
    public void setAppealDesc(String appealDesc) {
        this.appealDesc = appealDesc;
    }

    /**
     * 获取：诉求内容
     */
    public String getAppealDesc() {
        return appealDesc;
    }

    /**
     * 设置：反馈时间
     */
    public void setReplyDatetime(Date replyDatetime) {
        this.replyDatetime = replyDatetime;
    }

    /**
     * 获取：反馈时间
     */
    public Date getReplyDatetime() {
        return replyDatetime;
    }

    /**
     * 设置：处理状态
     */
    public void setExeStatus(String exeStatus) {
        this.exeStatus = exeStatus;
    }

    /**
     * 获取：处理状态
     */
    public String getExeStatus() {
        return exeStatus;
    }

    /**
     * 设置：是否删除(1:删除|0:未删除)
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取：是否删除(1:删除|0:未删除)
     */
    public String getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置：创建时间(上报时间)
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * 获取：创建时间(上报时间)
     */
    public Date getCrtTime() {
        return crtTime;
    }

    /**
     * 设置：创建人ID(上报人)
     */
    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    /**
     * 获取：创建人ID(上报人)
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

    /**
     * 设置：租房
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取：租房
     */
    public String getTenantId() {
        return tenantId;
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
}
