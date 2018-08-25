package com.bjzhianjia.scp.security.wf.base.task.entity;

import java.io.Serializable;

public class WfProcBean implements Serializable{

    private static final long serialVersionUID = -2311332483926010387L;

    private String procInstId;
    private String procId;
    private String procKey;
    private String procName;
    private String procCreator;
    private Integer procCreatetime;
    private Integer procEndtime;
    private String procBizid;
    private String procBiztype;
    private String procOrgcode;
    private String procMemo;
    private String procStatus;
    private Integer procVersion;
    private String procDisplayurl;
    private String procTenantId;
    private String procDepartId;
    
    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
    }

    public String getProcKey() {
        return procKey;
    }

    public void setProcKey(String procKey) {
        this.procKey = procKey;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getProcCreator() {
        return procCreator;
    }

    public void setProcCreator(String procCreator) {
        this.procCreator = procCreator;
    }

    public Integer getProcCreatetime() {
        return procCreatetime;
    }

    public void setProcCreatetime(Integer procCreatetime) {
        this.procCreatetime = procCreatetime;
    }

    public Integer getProcEndtime() {
        return procEndtime;
    }

    public void setProcEndtime(Integer procEndtime) {
        this.procEndtime = procEndtime;
    }

    public String getProcBizid() {
        return procBizid;
    }

    public void setProcBizid(String procBizid) {
        this.procBizid = procBizid;
    }

    public String getProcBiztype() {
        return procBiztype;
    }

    public void setProcBiztype(String procBiztype) {
        this.procBiztype = procBiztype;
    }

    public String getProcOrgcode() {
        return procOrgcode;
    }

    public void setProcOrgcode(String procOrgcode) {
        this.procOrgcode = procOrgcode;
    }

    public String getProcMemo() {
        return procMemo;
    }

    public void setProcMemo(String procMemo) {
        this.procMemo = procMemo;
    }

    public String getProcStatus() {
        return procStatus;
    }

    public void setProcStatus(String procStatus) {
        this.procStatus = procStatus;
    }

    public Integer getProcVersion() {
        return procVersion;
    }

    public void setProcVersion(Integer procVersion) {
        this.procVersion = procVersion;
    }

    public String getProcDisplayurl() {
        return procDisplayurl;
    }

    public void setProcDisplayurl(String procDisplayurl) {
        this.procDisplayurl = procDisplayurl;
    }

	public String getProcTenantId() {
		return procTenantId;
	}

	public void setProcTenantId(String procTenantId) {
		this.procTenantId = procTenantId;
	}

	public String getProcDepartId() {
		return procDepartId;
	}

	public void setProcDepartId(String procDepartId) {
		this.procDepartId = procDepartId;
	}
}