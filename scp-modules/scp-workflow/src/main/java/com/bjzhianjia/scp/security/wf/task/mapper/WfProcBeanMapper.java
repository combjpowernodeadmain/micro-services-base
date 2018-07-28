package com.bjzhianjia.scp.security.wf.task.mapper;

import org.springframework.stereotype.Repository;

import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import com.bjzhianjia.scp.security.wf.task.entity.WfProcBean;

public interface WfProcBeanMapper {
    int deleteByPrimaryKey(String procInstId);
    int insert(WfProcBean record);
    int insertSelective(WfProcBean record);
    WfProcBean selectByPrimaryKey(String procInstId);
    //更新业务流程表通过流程实例id
    int updateByPrimaryKeySelective(WfProcBean record);
    int updateByPrimaryKey(WfProcBean record);
    //更新业务流程表通过流程id
    int updateWfProcBeanByProcId(WfProcBean record);
    int updateWfProcBeanByInstId(WfProcBean record);
}