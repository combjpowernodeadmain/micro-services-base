package com.bjzhianjia.scp.security.wf.design.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.design.entity.WfProcActReProcdefBean;

@Repository
public interface WfActReProcdefBeanMapper{
    List<WfProcActReProcdefBean> processList(JSONObject objs);
}