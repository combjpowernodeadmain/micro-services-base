package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * 文书模板
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:07
 */
public interface WritsTemplatesMapper extends CommonMapper<WritsTemplates> {

    void deleteByIds(@Param("ids") Integer[] ids, @Param("updUserId") String userID,
        @Param("updUserName") String username, @Param("updTime") Date date);
}
