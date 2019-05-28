package com.bjzhianjia.scp.cgp.mapper;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Set;

// @Tenant
/**
 * AreaGridMemberMapper 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018-07-04 00:41:37          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0 
 * @author bo
 *
 */
public interface AreaGridMemberMapper extends CommonMapper<AreaGridMember> {

    /**
     * 批量插入网格成员
     * 
     * @author 尚
     * @param areaGridMembers
     */
    void insertAreaGridMemberList(@Param("areaGridMembers") List<AreaGridMember> areaGridMembers);

    /**
     * 按gridId删除网格成员
     * 
     * @param gridId
     *            id列表
     */
    void deleteByGridId(@Param("id") Integer gridId, @Param("updUserId") String updUserId,
        @Param("updUserName") String updUserName, @Param("updTime") Date updTime);
    
    public int countOfAreaMember();

    List<String> distinctGridMember();

    List<AreaGridMember> getListExcludeRole(@Param("areaGridMember") AreaGridMember areaGridMember);

    List<JSONObject> memAssessment(
        @Param("monthStart") Date monthStart,
        @Param("monthEnd") Date monthEnd,
        @Param("gridIdS")Set<Integer> gridIds,
        @Param("gridMembers") Set<String> gridMembers,
        @Param("gridRole") String gridRole
    );
}
