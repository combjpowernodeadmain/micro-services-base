package com.bjzhianjia.scp.cgp.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;


/**
 * RegulaObjectTypeMapper 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年7月7日          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author bo
 *
 */
public interface RegulaObjectTypeMapper extends CommonMapper<RegulaObjectType> {

    /**
     * 批量删除
     * 
     * @param ids
     *            id列表
     */
    void deleteByIds(@Param("ids") Integer[] ids, @Param("updUserId") String updUserId,
        @Param("updUserName") String updUserName, @Param("updTime") Date updTime);

    /**
     * 查询所有监管对象类型id和父id
     */
    List<RegulaObjectType> selectIdAll();
}