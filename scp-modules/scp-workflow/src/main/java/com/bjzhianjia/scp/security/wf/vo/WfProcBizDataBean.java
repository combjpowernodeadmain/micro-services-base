/*
 * @(#) WfProcBizDataBean.java  1.0  September 12, 2016
 *
 * Copyright 2016 by bjzhianjia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * IQB("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with IQB.
 */
package com.bjzhianjia.scp.security.wf.vo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcessBizDataAttr;

/**
 * Description: 工作流流程业务数据实体类
 * 
 * @author mayongming
 * @version 1.0
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.09.12    mayongming       1.0           1.0 Version
 * </pre>
 */
public class WfProcBizDataBean implements java.io.Serializable {
    private static final long serialVersionUID = -5989482882383451649L;

    private Map<String, Object> bizData = new HashMap<String, Object>();
    
    public String getProcBizId() {
        return (String)bizData.get(WfProcessBizDataAttr.PROC_BIZID);
    }
    
    public void setProcBizId(String procBizId) {
        put(WfProcessBizDataAttr.PROC_BIZID, procBizId);
    }

    public String getProcBizType() {
        return (String)bizData.get(WfProcessBizDataAttr.PROC_BIZTYPE);
    }
    
    public void setProcBizType(String procBizType) {
        put(WfProcessBizDataAttr.PROC_BIZTYPE, procBizType);
    }
    
    public String getProcOrgCode() {
        return (String)bizData.get(WfProcessBizDataAttr.PROC_ORGCODE);
    }
    
    public void setProcOrgCode(String procOrgCode) {
        put(WfProcessBizDataAttr.PROC_ORGCODE, procOrgCode);
    }

    public String getProcBizMemo() {
        return (String)bizData.get(WfProcessBizDataAttr.PROC_BIZMEMO);
    }

    public void setProcBizMemo(String procBizMemo) {
        put(WfProcessBizDataAttr.PROC_BIZMEMO, procBizMemo);
    }

    public String getProcApprStatus() {
        return (String)bizData.get(WfProcessBizDataAttr.PROC_APPRSTATUS);
    }
    
    public void setProcApprStatus(String procApprStatus) {
        put(WfProcessBizDataAttr.PROC_APPRSTATUS, procApprStatus);
    }
    
    public String getCurrTask() {
        return (String)bizData.get(WfProcessBizDataAttr.PROC_CURRTASK);
    }

    public void setCurrTask(String currTask) {
        put(WfProcessBizDataAttr.PROC_CURRTASK, currTask);
    }
    
    @SuppressWarnings("unchecked")
    public List<String> getNextTasks() {
        return (List<String>)bizData.get(WfProcessBizDataAttr.PROC_NEXTTASKS);
    }
    
    public void setNextTasks(List<String> nextTasks) {
        put(WfProcessBizDataAttr.PROC_NEXTTASKS, nextTasks);
    }

    public Map<String, Object> getBizData() {
        return bizData;
    }

    public void setBizData(Map<String, Object> bizData) {
        this.bizData = bizData;
    }
    
    public void put(String key, Object value) {
        this.bizData.put(key, value);
    }
    
    public void putAll(Map<String, Object> bizData) {
        this.bizData.putAll(bizData);
    }
    
    public Object getBizData(String key) {
        return bizData.get(key);
    }
    
    public boolean isEmpty() {
        return bizData == null || bizData.isEmpty();
    }
}
