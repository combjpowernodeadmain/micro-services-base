package com.bjzhianjia.scp.security.wf.task.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bjzhianjia.scp.security.wf.task.entity.WfMyProcBean;

@Repository
public interface WfMyProcBeanMapper {
	int deleteByPrimaryKey(Integer id);

	int deleteByInstIdandUserId(@Param("instId") Integer instId,
			@Param("userId") String userId,
			@Param("procTenantId") String procTenantId,
			@Param("procDepartId") String procDepartId);

	int insert(WfMyProcBean record);

	int insertSelective(WfMyProcBean record);

	WfMyProcBean selectByPrimaryKey(Integer id);

	List<WfMyProcBean> selectByProcInstId(
			@Param("procInstId") String procInstId,
			@Param("procTenantId") String procTenantId,
			@Param("procDepartId") String procDepartId);

	int updateByPrimaryKeySelective(WfMyProcBean record);

	int updateByPrimaryKey(WfMyProcBean record);

	WfMyProcBean getMyProcessByUser(@Param("procInstId") String procInstId,
			@Param("userId") String userId,
			@Param("procTenantId") String procTenantId,
			@Param("procDepartId") String procDepartId);

	List<WfMyProcBean> getMyProcessByUsers(
			@Param("procInstId") String procInstId,
			@Param("userId") List<String> userId,
			@Param("procTenantId") String procTenantId,
			@Param("procDepartId") String procDepartId);
}