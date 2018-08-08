package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.DeptBiztype;
import com.bjzhianjia.scp.security.common.data.Depart;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-19 09:20:01
 */
public interface DeptBiztypeMapper extends CommonMapper<DeptBiztype> {
	/**
	 * 批量删除
	 * @param ids id列表
	 */
	void deleteByIds(@Param("ids")Integer[] ids, @Param("updUserId")String updUserId, @Param("updUserName")String updUserName, @Param("updTime")Date updTime);
}
