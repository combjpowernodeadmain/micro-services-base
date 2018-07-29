package com.bjzhianjia.scp.security.wf.auth.mapper;

import com.bjzhianjia.scp.security.wf.auth.entity.WfProcTokenBean;

/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-28 21:33:54
 */
public interface WfProcTokenMapper {
    /**
     * 根据主键删除token
     * 
     * @param procTokenId
     * @return
     */
    int deleteByPrimaryKey(String procTokenId);
    
    /**
     * 插入token实体
     * 
     * @param bean
     * @return
     */
    int insert(WfProcTokenBean bean);
    
    /**
     * 选择性插入token实体
     * @param record
     * @return
     */
    int insertSelective(WfProcTokenBean record);
    
    /**
     * 根据主键查询token信息
     * 
     * @param procTokenId
     * @return
     */
    WfProcTokenBean selectByPrimaryKey(String procTokenId);
}
