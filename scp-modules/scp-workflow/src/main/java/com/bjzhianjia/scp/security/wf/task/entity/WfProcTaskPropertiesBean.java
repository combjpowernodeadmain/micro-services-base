/*
 * @(#) WfProcTaskPropertiesBean.java  1.0  August 26, 2016
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
package com.bjzhianjia.scp.security.wf.task.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Description: 工作流任务表单数据实体类
 * 
 * @author mayongming
 * @version 1.1
 * 
 *          <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.08.26    mayongming       1.0           1.0 Version
 * 2016.09.09    mayongming       1.1           1.1 Version
 * 
 * </pre>
 */
public class WfProcTaskPropertiesBean implements java.io.Serializable{
    private static final long serialVersionUID = 5759763782354947658L;
    
    private List<WfProcTaskPropertyBean> taskProperties = new ArrayList<WfProcTaskPropertyBean>();
    private Map<String, WfProcTaskPropertyBean> hmTaskProperty = new HashMap<String, WfProcTaskPropertyBean>();
    
    public List<WfProcTaskPropertyBean> getTaskProperties() {
        return taskProperties;
    }
    
    public WfProcTaskPropertyBean getTaskProperty(String id) {
        return hmTaskProperty.get(id);
    }
    
    public void setTaskProperties(List<WfProcTaskPropertyBean> taskProperties) {
        if (taskProperties != null && taskProperties.size() > 0) {
            for (WfProcTaskPropertyBean taskFormBean : taskProperties) {
                add(taskFormBean);
            }
        }
    }
    
    public void add(WfProcTaskPropertyBean taskProperty) {
        taskProperties.add(taskProperty);
        hmTaskProperty.put(taskProperty.getId(), taskProperty);
    }
    
    public void add(String id, String name, String type, String value) {
        WfProcTaskPropertyBean taskProperty = new WfProcTaskPropertyBean();
        taskProperty.setId(id);
        taskProperty.setName(name);
        taskProperty.setType(type);
        taskProperty.setValue(value);
        
        add(taskProperty);
    }
    
    public void remove(WfProcTaskPropertyBean taskProperty) {
        taskProperties.remove(taskProperty);
        hmTaskProperty.remove(taskProperty.getId());
    }
    
    public void remove(String id) {
        if (containsKey(id)) {
            WfProcTaskPropertyBean taskProperty = hmTaskProperty.get(id);
            taskProperties.remove(taskProperty);
            hmTaskProperty.remove(id);
        }
    }
    
    public boolean containsKey(String id) {
        return hmTaskProperty.containsKey(id);
    }

    public String getName(String id) {
        return containsKey(id) ? hmTaskProperty.get(id).getName() : null;
    }

    public String getType(String id) {
        return containsKey(id) ? hmTaskProperty.get(id).getType() : null;
    }

    public String getValue(String id) {
        return containsKey(id) ? hmTaskProperty.get(id).getValue() : null;
    }

    public boolean isEmpty() {
        return hmTaskProperty.isEmpty();
    }

    public Set<String> keySet() {
        return hmTaskProperty.keySet();
    }
    
    public int size() {
        return hmTaskProperty.size();
    }
}
