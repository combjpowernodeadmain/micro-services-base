package com.bjzhianjia.scp.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 党员表
 *
 * @author bo
 * @version 2019-03-27 21:07:17
 * @email 576866311@qq.com
 */
@Table(name = "party_member")
public class PartyMember implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private Integer id;

    // 所属网格id
    @Column(name = "grid_id")
    private String gridId;

    //姓名
    @Column(name = "name")
    private String name;

    //身份证号
    @Column(name = "identity_num")
    private String identityNum;

    //性别
    @Column(name = "sex")
    private String sex;

    //出生年月日
    @Column(name = "birthday")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    //民族
    @Column(name = "nation")
    private String nation;

    //籍贯
    @Column(name = "origin")
    private String origin;

    //婚姻状况
    @Column(name = "marital_status")
    private String maritalStatus;

    //入党日期
    @Column(name = "party_join_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date partyJoinDate;

    //入党时所在支部
    @Column(name = "pre_join_org")
    private Integer preJoinOrg;

    //转正日期
    @Column(name = "party_full_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date partyFullDate;

    //转正时所在支部
    @Column(name = "party_full_org")
    private Integer partyFullOrg;

    //全日制教育学历（学历字典code）
    @Column(name = "ft_edu_id")
    private String ftEduId;

    //全日制教育学位（学位字典code）
    @Column(name = "ft_degree_id")
    private String ftDegreeId;

    //全日制毕业院校
    @Column(name = "ft_school")
    private String ftSchool;

    //全日制毕业专业
    @Column(name = "ft_speciality")
    private String ftSpeciality;

    //在职教育学历（学历字典code）
    @Column(name = "pt_edu_id")
    private String ptEduId;

    //在职教育学位（学位字典code）
    @Column(name = "pt_degree_id")
    private String ptDegreeId;

    //在职毕业院校
    @Column(name = "pt_school")
    private String ptSchool;

    //在职毕业专业
    @Column(name = "pt_speciality")
    private String ptSpeciality;

    //工作单位
    @Column(name = "work_place")
    private String workPlace;

    //职务
    @Column(name = "work_post")
    private String workPost;

    //组织关系所在单位
    @Column(name = "work_unit")
    private String workUnit;

    //户籍所在地
    @Column(name = "origin_register")
    private String originRegister;

    //现居住地
    @Column(name = "addr")
    private String addr;

    //现任党内职务
    @Column(name = "party_duty")
    private String partyDuty;

    //入党介绍人
    @Column(name = "party_sponsor")
    private String partySponsor;

    //月应交纳党费
    @Column(name = "party_fee")
    private Double partyFee;

    //
    @Column(name = "crt_user_id")
    private String crtUserId;

    //
    @Column(name = "crt_user_name")
    private String crtUserName;

    //
    @Column(name = "crt_time")
    private Date crtTime;

    //
    @Column(name = "upd_user_id")
    private String updUserId;

    //
    @Column(name = "upd_user_name")
    private String updUserName;

    //
    @Column(name = "upd_time")
    private Date updTime;

    //
    @Column(name = "depart_id")
    private Date departId;

    //
    @Column(name = "tenant_id")
    private Date tenantId;

    //
    @Column(name = "is_deleted")
    private String isDeleted;


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
     * 获取：所属网格id
     */
    public String getGridId() {
        return gridId;
    }

    /**
     * 设置：所属网格id
     */
    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    /**
     * 设置：姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取：姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置：身份证号
     */
    public void setIdentityNum(String identityNum) {
        this.identityNum = identityNum;
    }

    /**
     * 获取：身份证号
     */
    public String getIdentityNum() {
        return identityNum;
    }

    /**
     * 设置：性别
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取：性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置：出生年月日
     */
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    /**
     * 获取：出生年月日
     */
    public Date getBirthday() {
        return birthday;
    }

    /**
     * 设置：民族
     */
    public void setNation(String nation) {
        this.nation = nation;
    }

    /**
     * 获取：民族
     */
    public String getNation() {
        return nation;
    }

    /**
     * 设置：籍贯
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * 获取：籍贯
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * 设置：婚姻状况
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * 获取：婚姻状况
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * 设置：入党日期
     */
    public void setPartyJoinDate(Date partyJoinDate) {
        this.partyJoinDate = partyJoinDate;
    }

    /**
     * 获取：入党日期
     */
    public Date getPartyJoinDate() {
        return partyJoinDate;
    }

    /**
     * 设置：入党时所在支部
     */
    public void setPreJoinOrg(Integer preJoinOrg) {
        this.preJoinOrg = preJoinOrg;
    }

    /**
     * 获取：入党时所在支部
     */
    public Integer getPreJoinOrg() {
        return preJoinOrg;
    }

    /**
     * 设置：转正日期
     */
    public void setPartyFullDate(Date partyFullDate) {
        this.partyFullDate = partyFullDate;
    }

    /**
     * 获取：转正日期
     */
    public Date getPartyFullDate() {
        return partyFullDate;
    }

    /**
     * 设置：转正时所在支部
     */
    public void setPartyFullOrg(Integer partyFullOrg) {
        this.partyFullOrg = partyFullOrg;
    }

    /**
     * 获取：转正时所在支部
     */
    public Integer getPartyFullOrg() {
        return partyFullOrg;
    }

    /**
     * 设置：全日制教育学历（学历字典code）
     */
    public void setFtEduId(String ftEduId) {
        this.ftEduId = ftEduId;
    }

    /**
     * 获取：全日制教育学历（学历字典code）
     */
    public String getFtEduId() {
        return ftEduId;
    }

    /**
     * 设置：全日制教育学位（学位字典code）
     */
    public void setFtDegreeId(String ftDegreeId) {
        this.ftDegreeId = ftDegreeId;
    }

    /**
     * 获取：全日制教育学位（学位字典code）
     */
    public String getFtDegreeId() {
        return ftDegreeId;
    }

    /**
     * 设置：全日制毕业院校
     */
    public void setFtSchool(String ftSchool) {
        this.ftSchool = ftSchool;
    }

    /**
     * 获取：全日制毕业院校
     */
    public String getFtSchool() {
        return ftSchool;
    }

    /**
     * 设置：全日制毕业专业
     */
    public void setFtSpeciality(String ftSpeciality) {
        this.ftSpeciality = ftSpeciality;
    }

    /**
     * 获取：全日制毕业专业
     */
    public String getFtSpeciality() {
        return ftSpeciality;
    }

    /**
     * 设置：在职教育学历（学历字典code）
     */
    public void setPtEduId(String ptEduId) {
        this.ptEduId = ptEduId;
    }

    /**
     * 获取：在职教育学历（学历字典code）
     */
    public String getPtEduId() {
        return ptEduId;
    }

    /**
     * 设置：在职教育学位（学位字典code）
     */
    public void setPtDegreeId(String ptDegreeId) {
        this.ptDegreeId = ptDegreeId;
    }

    /**
     * 获取：在职教育学位（学位字典code）
     */
    public String getPtDegreeId() {
        return ptDegreeId;
    }

    /**
     * 设置：在职毕业院校
     */
    public void setPtSchool(String ptSchool) {
        this.ptSchool = ptSchool;
    }

    /**
     * 获取：在职毕业院校
     */
    public String getPtSchool() {
        return ptSchool;
    }

    /**
     * 设置：在职毕业专业
     */
    public void setPtSpeciality(String ptSpeciality) {
        this.ptSpeciality = ptSpeciality;
    }

    /**
     * 获取：在职毕业专业
     */
    public String getPtSpeciality() {
        return ptSpeciality;
    }

    /**
     * 设置：工作单位
     */
    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    /**
     * 获取：工作单位
     */
    public String getWorkPlace() {
        return workPlace;
    }

    /**
     * 设置：职务
     */
    public void setWorkPost(String workPost) {
        this.workPost = workPost;
    }

    /**
     * 获取：职务
     */
    public String getWorkPost() {
        return workPost;
    }

    /**
     * 设置：组织关系所在单位
     */
    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    /**
     * 获取：组织关系所在单位
     */
    public String getWorkUnit() {
        return workUnit;
    }

    /**
     * 设置：户籍所在地
     */
    public void setOriginRegister(String originRegister) {
        this.originRegister = originRegister;
    }

    /**
     * 获取：户籍所在地
     */
    public String getOriginRegister() {
        return originRegister;
    }

    /**
     * 设置：现居住地
     */
    public void setAddr(String addr) {
        this.addr = addr;
    }

    /**
     * 获取：现居住地
     */
    public String getAddr() {
        return addr;
    }

    /**
     * 设置：现任党内职务
     */
    public void setPartyDuty(String partyDuty) {
        this.partyDuty = partyDuty;
    }

    /**
     * 获取：现任党内职务
     */
    public String getPartyDuty() {
        return partyDuty;
    }

    /**
     * 设置：入党介绍人
     */
    public void setPartySponsor(String partySponsor) {
        this.partySponsor = partySponsor;
    }

    /**
     * 获取：入党介绍人
     */
    public String getPartySponsor() {
        return partySponsor;
    }

    /**
     * 设置：月应交纳党费
     */
    public void setPartyFee(Double partyFee) {
        this.partyFee = partyFee;
    }

    /**
     * 获取：月应交纳党费
     */
    public Double getPartyFee() {
        return partyFee;
    }

    /**
     * 设置：
     */
    public void setCrtUserId(String crtUserId) {
        this.crtUserId = crtUserId;
    }

    /**
     * 获取：
     */
    public String getCrtUserId() {
        return crtUserId;
    }

    /**
     * 设置：
     */
    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    /**
     * 获取：
     */
    public String getCrtUserName() {
        return crtUserName;
    }

    /**
     * 设置：
     */
    public void setCrtTime(Date crtTime) {
        this.crtTime = crtTime;
    }

    /**
     * 获取：
     */
    public Date getCrtTime() {
        return crtTime;
    }

    /**
     * 设置：
     */
    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }

    /**
     * 获取：
     */
    public String getUpdUserId() {
        return updUserId;
    }

    /**
     * 设置：
     */
    public void setUpdUserName(String updUserName) {
        this.updUserName = updUserName;
    }

    /**
     * 获取：
     */
    public String getUpdUserName() {
        return updUserName;
    }

    /**
     * 设置：
     */
    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    /**
     * 获取：
     */
    public Date getUpdTime() {
        return updTime;
    }

    /**
     * 设置：
     */
    public void setDepartId(Date departId) {
        this.departId = departId;
    }

    /**
     * 获取：
     */
    public Date getDepartId() {
        return departId;
    }

    /**
     * 设置：
     */
    public void setTenantId(Date tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取：
     */
    public Date getTenantId() {
        return tenantId;
    }

    /**
     * 设置：
     */
    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * 获取：
     */
    public String getIsDeleted() {
        return isDeleted;
    }
}
