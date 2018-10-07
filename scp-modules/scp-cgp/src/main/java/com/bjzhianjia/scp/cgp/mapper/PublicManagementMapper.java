package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.PublicManagement;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-09-29 15:59:04
 */
public interface PublicManagementMapper extends CommonMapper<PublicManagement> {

    public void remove(@Param("ids") Integer[] ids, @Param("updUserId") String updUserId,
        @Param("updUserName") String updUserName, @Param("updTime") Date updTime);
}
