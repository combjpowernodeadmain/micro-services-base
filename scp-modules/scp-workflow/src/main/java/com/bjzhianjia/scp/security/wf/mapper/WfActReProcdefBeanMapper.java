package com.bjzhianjia.scp.security.wf.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import com.bjzhianjia.scp.security.wf.entity.WfProcActReProcdefBean;


public interface WfActReProcdefBeanMapper{
    List<WfProcActReProcdefBean> processList(JSONObject objs);
}