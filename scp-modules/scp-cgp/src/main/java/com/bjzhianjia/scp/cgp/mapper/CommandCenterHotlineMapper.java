package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.CommandCenterHotline;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 记录来自指挥中心热线的事件
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-09-28 19:20:36
 */
public interface CommandCenterHotlineMapper extends CommonMapper<CommandCenterHotline> {

    /**
     * 批量删除
     * 
     * @param ids
     *            id列表
     */
    void deleteByIds(@Param("ids") Integer[] ids, @Param("updUserId") String updUserId,
        @Param("updUserName") String updUserName, @Param("updTime") Date updTime);
}
