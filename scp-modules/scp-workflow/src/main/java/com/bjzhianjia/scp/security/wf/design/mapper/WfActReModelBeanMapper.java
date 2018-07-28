package com.bjzhianjia.scp.security.wf.design.mapper;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.wf.design.entity.WfProcActReModelBean;

public interface WfActReModelBeanMapper {

	WfProcActReModelBean selectByPrimaryKey(String id);

    int deleteByPrimaryKey(String id);
    
    List<WfProcActReModelBean> selectModelList(JSONObject objs);
    
}