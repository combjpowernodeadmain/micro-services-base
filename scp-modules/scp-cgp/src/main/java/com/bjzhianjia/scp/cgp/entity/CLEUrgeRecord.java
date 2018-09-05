package com.bjzhianjia.scp.cgp.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.Column;

/**
 * 
 * CLEUrgeRecord 案件催办记录表.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月5日          chenshaui      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author chenshaui
 *
 */
@Table(name = "cle_urge_record")
public class CLEUrgeRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    //
    @Id
    @GeneratedValue(generator="JDBC")
    private Integer id;

    // 案件id
    @Column(name = "cle_case_id")
    private String cleCaseId;

    // 标题
    @Column(name = "title")
    private String title;

    // 内容
    @Column(name = "content")
    private String content;

    // 创建人
    @Column(name = "crt_user_name")
    private String crtUserName;

    // 创建人ID
    @Column(name = "crt_user_id")
    private String crtUserId;

    // 创建时间
    @Column(name = "crt_time")
    private Date crtTime;

    // 租户ID
    @Column(name = "tenant_id")
    private String tenantId;

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
     * 设置：案件id
     */
    public void setCleCaseId(String cleCaseId) {
        this.cleCaseId = cleCaseId;
    }

    /**
     * 获取：案件id
     */
    public String getCleCaseId() {
        return cleCaseId;
    }

    /**
     * 设置：标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取：标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置：内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取：内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置：创建人
     */
    public void setCrtUserName(String crtUserName) {
        this.crtUserName = crtUserName;
    }

    /**
     * 获取：创建人
     */
    public String getCrtUserName() {
        return crtUserName;
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
     * 设置：租户ID
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * 获取：租户ID
     */
    public String getTenantId() {
        return tenantId;
    }
}
