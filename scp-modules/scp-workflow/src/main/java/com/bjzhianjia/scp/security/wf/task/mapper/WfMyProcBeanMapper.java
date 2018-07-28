package com.bjzhianjia.scp.security.wf.task.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import com.bjzhianjia.scp.security.wf.task.entity.WfMyProcBean;

public interface WfMyProcBeanMapper {
	int deleteByPrimaryKey(Integer id);

	int deleteByInstIdandUserId(@Param("instId") Integer instId,
			@Param("userId") String userId);

	int insert(WfMyProcBean record);

	int insertSelective(WfMyProcBean record);

	WfMyProcBean selectByPrimaryKey(Integer id);

	List<WfMyProcBean> selectByProcInstId(
			@Param("procInstId") String procInstId);

	int updateByPrimaryKeySelective(WfMyProcBean record);

	int updateByPrimaryKey(WfMyProcBean record);

	WfMyProcBean getMyProcessByUser(@Param("procInstId") String procInstId,
			@Param("userId") String userId);

	List<WfMyProcBean> getMyProcessByUsers(
			@Param("procInstId") String procInstId,
			@Param("userId") List<String> userId);
}