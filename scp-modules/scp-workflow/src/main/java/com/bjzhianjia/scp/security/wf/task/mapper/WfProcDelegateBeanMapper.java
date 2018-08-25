package com.bjzhianjia.scp.security.wf.task.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bjzhianjia.scp.security.wf.task.entity.WfProcDelegateBean;

@Repository
public interface WfProcDelegateBeanMapper {

	WfProcDelegateBean selectByPrimaryKey(Integer id);

	WfProcDelegateBean selectByInstAndLicens(
			@Param("procInstId") String procInstId,
			@Param("procLicensor") String procLicensor,
			@Param("procTenantId") String procTenantId);

	WfProcDelegateBean selectByInstAndMand(
			@Param("procInstId") String procInstId,
			@Param("mandatary") String mandatary,
			@Param("procTenantId") String procTenantId);

	int deleteByPrimaryKey(Integer id);

	int insert(WfProcDelegateBean record);

	int insertSelective(WfProcDelegateBean record);

	int updateByPrimaryKeySelective(WfProcDelegateBean record);

	int cancelProcDelegate(WfProcDelegateBean record);

	int updateByPrimaryKey(WfProcDelegateBean record);

	List<WfProcDelegateBean> getProcInstDelegateList(
			@Param("procInstId") String procInstId,
			@Param("mandatary") String mandatary,
			@Param("procTenantId") String procTenantId);
}