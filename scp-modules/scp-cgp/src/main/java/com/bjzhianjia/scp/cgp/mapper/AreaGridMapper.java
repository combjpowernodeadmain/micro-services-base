package com.bjzhianjia.scp.cgp.mapper;

import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * 网格
 * 
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-07-04 00:41:37
 */
//@Tenant
public interface AreaGridMapper extends CommonMapper<AreaGrid> {

    /**
     * 网格全部定位
     * 
     * @param areaGrid
     * @return
     */
    List<AreaGrid> allPotition(@Param("areaGrid") AreaGrid areaGrid);

    /**
     *  通过用户ids获取网格信息列表
     * @return
     */
    List<Map<String,Object>> selectByUserIds(@Param("userIds") List<String> userIds);

    List<AreaGrid> selectIdsByGridParentName(@Param("gridParentName") String gridParentName);
}
