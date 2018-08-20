package com.bjzhianjia.scp.security.wf.base.task.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcBean;

@Repository
public interface WfProcBeanMapper {
	int deleteByPrimaryKey(@Param("procInstId") String procInstId,
			@Param("procTenantId") String procTenantId);

	int insert(WfProcBean record);

	// int insertSelective(WfProcBean record);

	WfProcBean selectByPrimaryKey(@Param("procInstId") String procInstId,
			@Param("procTenantId") String procTenantId);

	// 更新业务流程表通过流程实例id
	// int updateByPrimaryKeySelective(WfProcBean record);

	// int updateByPrimaryKey(WfProcBean record);

	// 更新业务流程表通过流程id
	// int updateWfProcBeanByProcId(WfProcBean record);

	int updateWfProcBeanByInstId(WfProcBean record);
}