package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.merge.annonation.MergeField;

/**
 * 综合执法 - 案件登记
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:08
 */
@Table(name = "cle_case_registration")
public class CaseRegistration implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 处理状态：处理中
     */
    public static final String EXESTATUS_STATE_TODO = "0";

    /**
     * 处理状态：已完成
     */
    public static final String EXESTATUS_STATE_FINISH = "1";

    /**
     * 处理状态：已终止
     */
    public static final String EXESTATUS_STATE_STOP = "2";

    /**
     * 案件来源：执法任务
     */
    public static final String CASE_SOURCE_TYPE_TASK = "root_biz_caseSourceT_task";

    /**
     * 案件来源：日常执法
     */
    public static final String CASE_SOURCE_TYPE_NORMAL = "root_biz_caseSourceT_normal";

    /**
     * 案件来源：中心交办
     */
    public static final String CASE_SOURCE_TYPE_CENTER = "root_biz_caseSourceT_center";

    // 主键
    @Id
    private String id;

    // 当事人类型(1: 个人 2: 单位)
    @Column(name = "concerned_type")
    private String concernedType;

    // 当事人信息主键
    @Column(name = "concerned_id")
    private Integer concernedId;
    
    //案件编号
    @Column(name = "case_code")
    private String caseCode;

    // 所属业务线条
    @Column(name = "biz_type")
    @MergeField(key = Constances.ROOT_BIZ_TYPE, method = "getByCode", feign = DictFeign.class)
    private String bizType;

    // 事件类型
    @Column(name = "event_type")
    private String eventType;

    // 巡查事项 - （违法行为）
    @Column(name = "inspect_item")
    private Integer inspectItem;

    // 案件名称
    @Column(name = "case_name")
    private String caseName;

    // 案发时间
    @Column(name = "case_time")
    private Date caseTime;

    // 完成期限 *
    @Column(name = "case_end")
    private Integer caseEnd;

    // 案件来源 *
    @Column(name = "case_source")
    private String caseSource;

    // 案件来源类型
    @Column(name = "case_source_type")
    private String caseSourceType;

    // 来源时间
    @Column(name = "case_source_time")
    private Date caseSourceTime;

    // 举报人
    @Column(name = "case_informer")
    private String caseInformer;

    // 举报人联系地址
    @Column(name = "case_informer_addr")
    private String caseInformerAddr;

    // 举报人联系电话
    @Column(name = "case_informer_phone")
    private String caseInformerPhone;

    // 执法者
    @Column(name = "enforcers")
    private String enforcers;

    // 案件内容
    @Column(name = "case_content")
    private String caseContent;

    // 案发地点
    @Column(name = "case_address")
    private String caseAddress;

    // 所属网格
    @Column(name = "gird_id")
    private Integer girdId;

    // 经度
    @Column(name = "case_ongitude")
    private String caseOngitude;

    // 纬度
    @Column(name = "case_latitude")
    private String caseLatitude;

    // 处理方式
    @Column(name = "deal_type")
    private String dealType;

    // 处理意见
    @Column(name = "deal_suggest")
    private String dealSuggest;

    // 移送部门
    @Column(name = "transfer_depart")
    private String transferDepart;

    // 是否删除(1:删除|0:未删除)
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

    // 租户
    @Column(name = "tenant_id")
    private String tenantId;

    // 部门ID
    @Column(name = "dept_id")
    private String deptId;

    // 处理状态
    @Column(name = "exe_status")
    private String exeStatus;

    // 是否督办（0否| 1是）
    @Column(name = "is_supervise")
    private String isSupervise;

    // 是否催办（0否| 1是）
    @Column(name = "is_urge")
    private String isUrge;

    /**
     * 设置：主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取：主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置：当事人类型(1: 个人 2: 单位)
     */
    public void setConcernedType(String concernedType) {
        this.concernedType = concernedType;
    }

    /**
     * 获取：当事人类型(1: 个人 2: 单位)
     */
    public String getConcernedType() {
        return concernedType;
    }

    /**
     * 设置：当事人信息主键
     */
    public void setConcernedId(Integer concernedId) {
        this.concernedId = concernedId;
    }

    /**
     * 获取：当事人信息主键
     */
    public Integer getConcernedId() {
        return concernedId;
    }

    /**
     * 设置：所属业务线条
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    /**
     * 获取：所属业务线条
     */
    public String getBizType() {
        return bizType;
    }

    /**
     * 设置：事件类型
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * 获取：事件类型
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * 设置：巡查事项 - （违法行为）
     */
    public void setInspectItem(Integer inspectItem) {
        this.inspectItem = inspectItem;
    }

    /**
     * 获取：巡查事项 - （违法行为）
     */
    public Integer getInspectItem() {
        return inspectItem;
    }

    /**
     * 设置：案件名称
     */
    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    /**
     * 获取：案件名称
     */
    public String getCaseName() {
        return caseName;
    }

    /**
     * 设置：案发时间
     */
    public void setCaseTime(Date caseTime) {
        this.caseTime = caseTime;
    }

    /**
     * 获取：案发时间
     */
    public Date getCaseTime() {
        return caseTime;
    }

    /**
     * 设置：完成期限 *
     */
    public void setCaseEnd(Integer caseEnd) {
        this.caseEnd = caseEnd;
    }

    /**
     * 获取：完成期限 *
     */
    public Integer getCaseEnd() {
        return caseEnd;
    }

    /**
     * 设置：案件来源 *
     */
    public void setCaseSource(String caseSource) {
        this.caseSource = caseSource;
    }

    /**
     * 获取：案件来源 *
     */
    public String getCaseSource() {
        return caseSource;
    }

    /**
     * 设置：来源时间
     */
    public void setCaseSourceTime(Date caseSourceTime) {
        this.caseSourceTime = caseSourceTime;
    }

    /**
     * 获取：来源时间
     */
    public Date getCaseSourceTime() {
        return caseSourceTime;
    }

    /**
     * 设置：举报人
     */
    public void setCaseInformer(String caseInformer) {
        this.caseInformer = caseInformer;
    }

    /**
     * 获取：举报人
     */
    public String getCaseInformer() {
        return caseInformer;
    }

    /**
     * 设置：举报人联系地址
     */
    public void setCaseInformerAddr(String caseInformerAddr) {
        this.caseInformerAddr = caseInformerAddr;
    }

    /**
     * 获取：举报人联系地址
     */
    public String getCaseInformerAddr() {
        return caseInformerAddr;
    }

    /**
     * 设置：举报人联系电话
     */
    public void setCaseInformerPhone(String caseInformerPhone) {
        this.caseInformerPhone = caseInformerPhone;
    }

    /**
     * 获取：举报人联系电话
     */
    public String getCaseInformerPhone() {
        return caseInformerPhone;
    }

    /**
     * 设置：执法者
     */
    public void setEnforcers(String enforcers) {
        this.enforcers = enforcers;
    }

    /**
     * 获取：执法者
     */
    public String getEnforcers() {
        return enforcers;
    }

    /**
     * 设置：案件内容
     */
    public void setCaseContent(String caseContent) {
        this.caseContent = caseContent;
    }

    /**
     * 获取：案件内容
     */
    public String getCaseContent() {
        return caseContent;
    }

    /**
     * 设置：案发地点
     */
    public void setCaseAddress(String caseAddress) {
        this.caseAddress = caseAddress;
    }

    /**
     * 获取：案发地点
     */
    public String getCaseAddress() {
        return caseAddress;
    }

    /**
     * 设置：所属网格
     */
    public void setGirdId(Integer girdId) {
        this.girdId = girdId;
    }

    /**
     * 获取：所属网格
     */
    public Integer getGirdId() {
        return girdId;
    }

    /**
     * 设置：经度
     */
    public void setCaseOngitude(String caseOngitude) {
        this.caseOngitude = caseOngitude;
    }

    /**
     * 获取：经度
     */
    public String getCaseOngitude() {
        return caseOngitude;
    }

    /**
     * 设置：纬度
     */
    public void setCaseLatitude(String caseLatitude) {
        this.caseLatitude = caseLatitude;
    }

    /**
     * 获取：纬度
     */
    public String getCaseLatitude() {
        return caseLatitude;
    }

    /**
     * 设置：处理方式
     */
    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    /**
     * 获取：处理方式
     */
    public String getDealType() {
        return dealType;
    }

    /**
     * 设置：处理意见
     */
    public void setDealSuggest(String dealSuggest) {
        this.dealSuggest = dealSuggest;
    }

    /**
     * 获取：处理意见
     */
    public String getDealSuggest() {
        return dealSuggest;
    }

    /**
     * 设置：移送部门
     */
    public void setTransferDepart(String transferDepart) {
        this.transferDepart = transferDepart;
    }

    /**
     * 获取：移送部门
     */
    public String getTransferDepart() {
        return transferDepart;
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

    /**
     * 设置：租户
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取：租户
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

    /**
     * 获取：处理状态
     */
    public String getExeStatus() {
        return exeStatus;
    }

    /**
     * 设置：处理状态
     */
    public void setExeStatus(String exeStatus) {
        this.exeStatus = exeStatus;
    }

    /**
     * 获取：是否督办（0否| 1是）
     */
    public String getIsSupervise() {
        return isSupervise;
    }

    /**
     * 设置获取：是否督办（0否| 1是）
     */
    public void setIsSupervise(String isSupervise) {
        this.isSupervise = isSupervise;
    }

    /**
     * 获取：是否催办（0否| 1是）
     */
    public String getIsUrge() {
        return isUrge;
    }

    /**
     * 设置获取：是否催办（0否| 1是）
     */
    public void setIsUrge(String isUrge) {
        this.isUrge = isUrge;
    }

    /**
     * 获取：案件来源类型
     */
    public String getCaseSourceType() {
        return caseSourceType;
    }

    /**
     * 设置：案件来源类型
     */
    public void setCaseSourceType(String caseSourceType) {
        this.caseSourceType = caseSourceType;
    }
    
    /**
     * 获取：案件编号
     */
    public String getCaseCode() {
        return caseCode;
    }
    /**
     * 设置：案件编号
     */
    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }
}
