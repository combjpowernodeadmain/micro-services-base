package com.bjzhianjia.scp.cgp.mapper;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


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

    /**
     * 获取网格的父级网格名称
     *
     * @param gridId 网格id
     * @return {
     * "gridName":"网格名称",
     * "parentGridName":"网格父级名称",
     * }
     */
    Map<String,String> selectParentNameById(@Param("gridId") String gridId);

    List<AreaGrid> gridLevelWithoutMapInfo(@Param("areaGrid") AreaGrid areaGrid);

    /**
     * 网格人员考核展示列表
     *
     * @param monthStart
     * @param monthEnd
     * @param gridIds
     * @param memberIds
     * @param gridRole
     * @return
     */
    List<JSONObject> getAssessment(
            @Param("monthStart") Date monthStart,
            @Param("monthEnd") Date monthEnd,
            @Param("gridIds") Set<Integer> gridIds,
            @Param("memberIds") String memberIds,
            @Param("gridRole") String gridRole
    );

    /**
     * 人员——网格对应 一对多
     *
     * @param memberId
     * @return
     */
    List<JSONObject> getGridsByMemberId(@Param("memberId") String memberId);
}
