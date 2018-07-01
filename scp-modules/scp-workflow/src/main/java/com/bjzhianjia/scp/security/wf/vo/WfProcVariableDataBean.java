/*
 * @(#) WfProcVariableDataBean.java  1.0  September 12, 2016
 *
 * Copyright 2016 by 北京爱钱帮财富科技有限公司
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
import java.util.Map;

import com.bjzhianjia.scp.security.wf.constant.Constants.WfProcessVariableDataAttr;

/**
 * Description: 工作流流程变量实体类
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
public class WfProcVariableDataBean implements java.io.Serializable {
    private static final long serialVersionUID = -7560767181896098778L;
    private Map<String, Object> variableData = new HashMap<String, Object>();
     
    public String getProcAssignee() {
        return (String)variableData.get(WfProcessVariableDataAttr.PROC_ASSIGNEE);
    }
    
    public void setProcAssignee(String procAssignee) {
        put(WfProcessVariableDataAttr.PROC_ASSIGNEE, procAssignee);
    }
    
    public String getProcAppointTask() {
        return (String)variableData.get(WfProcessVariableDataAttr.PROC_APPOINTTASK);
    }
    
    public void setProcAppointTask(String procAppointTask) {
        put(WfProcessVariableDataAttr.PROC_APPOINTTASK, procAppointTask);
    }

    public String getProcApprStatus() {
        return (String)variableData.get(WfProcessVariableDataAttr.PROC_APPRSTATUS);
    }
    
    public void setProcApprStatus(String procApprStatus) {
        put(WfProcessVariableDataAttr.PROC_APPRSTATUS, procApprStatus);
    }
    
    public String getProcApprOpinion() {
        return (String)variableData.get(WfProcessVariableDataAttr.PROC_APPROPINION);
    }
    
    public void setProcApprOpinion(String procApprOpinion) {
        put(WfProcessVariableDataAttr.PROC_APPROPINION, procApprOpinion);
    }

    public String getProcBiztype() {
        return (String)variableData.get(WfProcessVariableDataAttr.PROC_BIZTYPE);
    }
    
    public void setProcBiztype(String procBiztype) {
        put(WfProcessVariableDataAttr.PROC_BIZTYPE, procBiztype);
    }
    
    public String getProcOrgcode() {
        return (String)variableData.get(WfProcessVariableDataAttr.PROC_ORGCODE);
    }
    
    public void setProcOrgcode(String procOrgcode) {
        put(WfProcessVariableDataAttr.PROC_ORGCODE, procOrgcode);
    }
    
    public String getProcSpecialDesc() {
        return (String)variableData.get(WfProcessVariableDataAttr.PROC_SPECIALDESC);
    }
    
    public void setProcSpecialDesc(String procSpecialDesc) {
        put(WfProcessVariableDataAttr.PROC_SPECIALDESC, procSpecialDesc);
    }
    
    
    public Map<String, Object> getVariableData() {
        return variableData;
    }
    
    public void setVariableData(Map<String, Object> variableData) {
        this.variableData = variableData;
    }
    
    public void put(String key, Object value) {
        this.variableData.put(key, value);
    }
    
    public void putAll(Map<String, Object> variableData) {
        this.variableData.putAll(variableData);
    }
    
    public Object getVariableData(String key) {
        return variableData.get(key);
    }
    
    public boolean isEmpty() {
        return variableData == null || variableData.isEmpty();
    }
}
