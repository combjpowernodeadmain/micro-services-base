package com.bjzhianjia.scp.security.admin.mapper;

import com.bjzhianjia.scp.security.admin.entity.BaseGroupMember;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author chenshuai
 * @email 
 * @version 2018-10-14 07:01:23
 */
public interface BaseGroupMemberMapper extends CommonMapper<BaseGroupMember> {

    /**
     * 用户角色列表
     * @param userId 用户id
     * @return
     */
    List<Map<String,Object>> selectNameByUserId(@Param("userId") String userId);

    /**
     *  删除用户角色关系
     * @param userId 用户id
     * @return
     */
    int deleteByUserId(@Param("userId") String userId);
}
