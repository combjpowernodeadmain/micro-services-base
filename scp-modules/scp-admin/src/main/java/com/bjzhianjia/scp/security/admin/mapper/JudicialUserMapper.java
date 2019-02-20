
package com.bjzhianjia.scp.security.admin.mapper;

import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.common.data.Tenant;
import com.bjzhianjia.scp.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * JudicialUserController 司法用户.
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/9/29          admin      1.0            ADD
 * </pre>
 *
 * @author admin
 * @version 1.0
 */
@Tenant()
public interface JudicialUserMapper extends CommonMapper<User> {

    /**
     * 获取分案人总数
     *
     * @param departId 部门id
     * @param groupId  角色id
     * @return
     */
    Integer selectUserByDepartAndGroup(@Param("departId") String departId, @Param("groupId") String groupId);

    /**
     * 获取指定角色列表
     *
     * @param user      用户信息
     * @param departIds 用户部门ids
     * @param groupId   角色id
     * @return
     */
    List<Map<String, Object>> selectMajorUser(@Param("user") User user,
                                              @Param("departIds") String departIds,
                                              @Param("groupId") String groupId);
    /**
     * 获取排除角色后的用户列表
     *
     * @param user      用户信息
     * @param groupId   排除的角色id
     * @return
     */
    List<Map<String, Object>> selectUserDebarRole(@Param("user") User user,
                                                  @Param("groupId") String groupId);

    /**
     * 分案时获取主办人（技术人员）列表
     *
     * @param major        专业
     * @param userName     用户名字
     * @param departId     部门id
     * @param areaProvince 省级编码
     * @param areaCounty   区县编码
     * @param groupId      角色id
     * @return
     */
    List<Map<String, Object>> selectTechnicist(@Param("major") String major,
                                               @Param("userName") String userName,
                                               @Param("departId") String departId,
                                               @Param("areaProvince") String areaProvince,
                                               @Param("areaCity") String areaCity,
                                               @Param("areaCounty") String areaCounty,
                                               @Param("groupId") String groupId);
}
