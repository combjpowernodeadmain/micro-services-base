package com.bjzhianjia.scp.security.wf.base.auth.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.security.wf.base.auth.entity.WfProcTokenBean;

/**
 * 
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-28 21:33:54
 */
public interface WfProcTokenMapper {
    
	/**
	 * Token已被删除
	 */
	public static final String TOKEN_DELETED = "1";
	
	/**
	 * Token正常，没有被删除
	 */
	public static final String TOKEN_UNDELETED = "0";
	
	/**
	 * TOKEN已经禁用
	 */
	public static final String TOKEN_DISABLED = "0";
	
	/**
	 * TOKEN启用中
	 */
	public static final String TOKEN_ENABLE = "1";
	
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
    WfProcTokenBean selectByPrimaryKey(String id);
    

    /**
     * 根据用户tenantId查询token信息
     * 
     * @param procTokenId
     * @return
     */
    WfProcTokenBean selectByTenantId(@Param("tenantId")String tenantId);
    

    /**
     * 根据用户查询token信息
     * 
     * @param procTokenId
     * @return
     */
    List<WfProcTokenBean> selectAll(@Param("index")int index, @Param("pageSize")int pageSize);
    
    
    /**
     * 根据主键删除token
     * 
     * @param id
     * @param flag
     * @return
     */
    int deleteOrRecoverByPrimaryKey(String id, String flag);
    

    /**
     * 根据主键禁用/启用token
     * 
     * @param id
     * @param flag
     */
    int enableOrDisableByPrimaryKey(String id, String flag);
    
}
