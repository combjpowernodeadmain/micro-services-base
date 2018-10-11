package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.MessageCenter;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;

/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-09-30 15:24:19
 */
public interface MessageCenterMapper extends CommonMapper<MessageCenter> {

    void addMessageCenterList(@Param("list") List<MessageCenter> messageCenters);

    void updateMessageCenterList(@Param("list") Integer[] messageCenters, @Param("updTime") Date updTime,
        @Param("updUserId") String updUserId, @Param("updUserName") String updUserName);
}
