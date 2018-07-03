package com.bjzhianjia.scp.security.admin.mapper;

import com.bjzhianjia.scp.security.admin.entity.Depart;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 
 * 
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0 
 */
@Tenant
public interface DepartMapper extends CommonMapper<Depart> {

    List<User> selectDepartUsers(@Param("departId") String departId,@Param("userName") String userName);

    void deleteDepartUser(@Param("departId")String departId, @Param("userId") String userId);

    void insertDepartUser(@Param("id") String id, @Param("departId") String departId, @Param("userId") String userId,@Param("tenantId") String tenantId);

}
