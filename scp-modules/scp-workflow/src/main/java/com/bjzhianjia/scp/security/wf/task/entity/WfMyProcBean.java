package com.bjzhianjia.scp.security.wf.task.entity;

import java.io.Serializable;

public class WfMyProcBean implements Serializable{

    private Integer id;
    private String procInstId;
    private String procUser;
    private String procUserType;
    private String procTaskid;
    private String procTaskcode;
    private String procTaskname;
    private String procDisplayurl;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcUser() {
        return procUser;
    }

    public void setProcUser(String procUser) {
        this.procUser = procUser;
    }

    public String getProcUserType() {
        return procUserType;
    }

    public void setProcUserType(String procUserType) {
        this.procUserType = procUserType;
    }

    public String getProcTaskid() {
        return procTaskid;
    }

    public void setProcTaskid(String procTaskid) {
        this.procTaskid = procTaskid;
    }

    public String getProcTaskcode() {
        return procTaskcode;
    }

    public void setProcTaskcode(String procTaskcode) {
        this.procTaskcode = procTaskcode;
    }

    public String getProcTaskname() {
        return procTaskname;
    }

    public void setProcTaskname(String procTaskname) {
        this.procTaskname = procTaskname;
    }

    public String getProcDisplayurl() {
        return procDisplayurl;
    }

    public void setProcDisplayurl(String procDisplayurl) {
        this.procDisplayurl = procDisplayurl;
    }
}