package com.bjzhianjia.scp.party.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;


/**
 * 出境记录表
 *
 * @author bo
 * @version 2019-03-27 21:07:17
 * @email 576866311@qq.com
 */
@Table(name = "export_log")
public class ExportLog implements Serializable {
    private static final long serialVersionUID = 1L;

    //
    @Id
    private Integer id;

    //党员id
    @Column(name = "party_mem_id")
    private Integer partyMemId;

    //出境日期
    @Column(name = "export_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date exportDate;

    //出境原因
    @Column(name = "export_cause")
    private String exportCause;

    //出境后与组织联系情况code（字典code）
    @Column(name = "exeport_status")
    private String exeportStatus;

    //回国日期
    @Column(name = "import_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date importDate;

    //回国后与组织联系情况code（字典code）
    @Column(name = "import_status")
    private String importStatus;

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
     * 设置：党员id
     */
    public void setPartyMemId(Integer partyMemId) {
        this.partyMemId = partyMemId;
    }

    /**
     * 获取：党员id
     */
    public Integer getPartyMemId() {
        return partyMemId;
    }

    /**
     * 设置：出境日期
     */
    public void setExportDate(Date exportDate) {
        this.exportDate = exportDate;
    }

    /**
     * 获取：出境日期
     */
    public Date getExportDate() {
        return exportDate;
    }

    /**
     * 设置：出境原因
     */
    public void setExportCause(String exportCause) {
        this.exportCause = exportCause;
    }

    /**
     * 获取：出境原因
     */
    public String getExportCause() {
        return exportCause;
    }

    /**
     * 设置：出境后与组织联系情况code（字典code）
     */
    public void setExeportStatus(String exeportStatus) {
        this.exeportStatus = exeportStatus;
    }

    /**
     * 获取：出境后与组织联系情况code（字典code）
     */
    public String getExeportStatus() {
        return exeportStatus;
    }

    /**
     * 设置：回国日期
     */
    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    /**
     * 获取：回国日期
     */
    public Date getImportDate() {
        return importDate;
    }

    /**
     * 设置：回国后与组织联系情况code（字典code）
     */
    public void setImportStatus(String importStatus) {
        this.importStatus = importStatus;
    }

    /**
     * 获取：回国后与组织联系情况code（字典code）
     */
    public String getImportStatus() {
        return importStatus;
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
