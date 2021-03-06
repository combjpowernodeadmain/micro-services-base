
package com.bjzhianjia.scp.security.admin.biz;

import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.admin.constant.RoleConstant;
import com.bjzhianjia.scp.security.admin.entity.BaseGroupMember;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.feign.DictFeign;
import com.bjzhianjia.scp.security.admin.mapper.GroupMapper;
import com.bjzhianjia.scp.security.admin.mapper.JudicialUserMapper;
import com.bjzhianjia.scp.security.common.biz.BaseBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
@Service
@Transactional(rollbackFor = Exception.class)
public class JudicialUserBiz extends BaseBiz<JudicialUserMapper, User> {

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private GroupMapper groupMapper;

    @Autowired
    private UserBiz userBiz;

    @Autowired
    private BaseGroupMemberBiz baseGroupMemberBiz;

    @Autowired
    private Environment environment;


    /**
     * 获取分案人总数
     *
     * @param departId 部门id
     * @return
     */
    public Integer getUserByDepartAndGroup(String departId, String groupId) {
        if (StringUtils.isBlank(departId)) {
            return 0;
        }
        return this.mapper.selectUserByDepartAndGroup(departId, groupId);
    }

    /**
     * 获取会检人员列表
     *
     * @param roleId    角色id
     * @param departIds 用户部门ids
     * @param page      页码
     * @param limit     页容量
     * @return
     */
    public TableResultResponse<Map<String, Object>> getPartnerList(User user, String roleId, String departIds, int page,
                                                                   int limit) {
        Page<Object> pageList = PageHelper.startPage(page, limit);
        List<Map<String, Object>> userList = this.mapper.selectMajorUser(user, departIds, roleId);
        Map<String, String> dictMajorMap = dictFeign.getByCode(User.JUDICIAL_PROFESSIONAL);
        if (userList != null && !userList.isEmpty()) {
            if (dictMajorMap != null && !dictMajorMap.isEmpty()) {
                this.getMajorsName(userList, dictMajorMap);
            }
        } else {
            userList = new ArrayList<>();
        }
        return new TableResultResponse<>(pageList.getTotal(), userList);
    }

    /**
     * 获取指定角色列表
     *
     * @param roleId    角色id
     * @param departIds 用户部门ids
     * @param page      页码
     * @param limit     页容量
     * @return
     */
    public TableResultResponse<Map<String, Object>> getMajorUsers(User user, String roleId, String departIds, int page,
                                                                  int limit) {
        Page<Object> pageList = PageHelper.startPage(page, limit);
        List<Map<String, Object>> userList = this.mapper.selectMajorUser(user, departIds, roleId);
        Map<String, String> dictMajorMap = dictFeign.getByCode(User.JUDICIAL_PROFESSIONAL);
        if (userList != null && !userList.isEmpty()) {
            if (dictMajorMap != null && !dictMajorMap.isEmpty()) {
                this.getMajorsName(userList, dictMajorMap);
            }
        } else {
            userList = new ArrayList<>();
        }
        return new TableResultResponse<>(pageList.getTotal(), userList);
    }


    /**
     * 用户配置指定角色
     *
     * @param user   用户信息
     * @param roleId 角色id
     */
    public void distributionRole(User user, String roleId) {
        if (StringUtils.isBlank(user.getId())) {
            user.setId(null);
            //判断是否存在账号
            User _user = new User();
            _user.setUsername(user.getUsername());
            _user = this.selectOne(_user);
            //数据库中存在指定username用户
            if (_user != null) {
                //存在并且逻辑删除了，则修改username值（userId+"_"+userName)
                if (BooleanUtil.BOOLEAN_TRUE.equals(_user.getIsDeleted())) {
                    _user.setUsername(_user.getId() + "_" + _user.getUsername());
                    this.updateSelectiveById(_user);
                } else {
                    throw new RuntimeException("用户账户已存在，请重新填写！");
                }
            }
            userBiz.insertSelective(user);
        } else {
            User _user = userBiz.selectOne(user);
            if(_user == null){
                throw new RuntimeException("未找到当前用户所属部门！");
            }
            user.setDepartId(_user.getDepartId());
            userBiz.updateSelectiveById(user);
        }
        //判断是否已添加指定角色
        BaseGroupMember baseGroupMember = baseGroupMemberBiz.getBaseGroupMemberByUserIdAndGroupId(user.getId(),
                roleId);
        if (baseGroupMember != null) {
            throw new RuntimeException("用户已添加指定角色，请勿重复添加！");
        }
        //添加角色关系
        baseGroupMember = new BaseGroupMember();
        baseGroupMember.setId(UUIDUtils.generateUuid());
        baseGroupMember.setGroupId(roleId);
        baseGroupMember.setUserId(user.getId());
        baseGroupMember.setTenantId(BaseContextHandler.getTenantID());
        baseGroupMemberBiz.insertSelective(baseGroupMember);
    }

    /**
     * 删除用户角色关系
     *
     * @param userId
     * @param roleId
     */
    public void deleteUserRole(String userId, String roleId) {
        //判断是否已添加指定角色
        BaseGroupMember baseGroupMember = baseGroupMemberBiz.getBaseGroupMemberByUserIdAndGroupId(userId,
                roleId);
        if (baseGroupMember != null) {
            //删除角色关系
            baseGroupMemberBiz.delete(baseGroupMember);
        } else {
            throw new RuntimeException("用户未找到角色关系！");
        }
    }


    /**
     * 获取排除角色后的用户列表
     *
     * @param user   用户信息
     * @param roleId 排除的角色id
     * @return
     */
    public TableResultResponse<Map<String, Object>> getUserByDebarRole(User user, String roleId, int page, int limit) {
        Page<Object> pageList = PageHelper.startPage(page, limit);
        List<Map<String, Object>> userList = this.mapper.selectUserDebarRole(user, roleId);
        if (userList == null && userList.isEmpty()) {
            userList = new ArrayList<>();
        }
        return new TableResultResponse<>(pageList.getTotal(), userList);
    }

    /**
     * 更新用户专业和手机号
     *
     * @param user
     */
    public void upTechnologist(User user) {
        User _user = this.selectById(user.getId());
        if (_user == null) {
            throw new RuntimeException("未找到用户信息！");
        }
        user.setDepartId(_user.getDepartId());
        userBiz.updateSelectiveById(user);
    }

    /**
     * 分案时获取主办人（技术人员）列表
     *
     * @param major        专业
     * @param userName     用户名字
     * @param departId     部门id
     * @param areaProvince 省级编码
     * @param areaCity     城市编码
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<Map<String, Object>> getTechnicist(String major, String userName, String departId,
                                                                  String areaProvince, String areaCity,
                                                                  String areaCounty, int page,int limit) {
        Page<Object> pageList = PageHelper.startPage(page, limit);
        String roleId = environment.getProperty(RoleConstant.ROLE_TECHNICIST);
        List<Map<String, Object>> userList = this.mapper.selectTechnicist(major, userName, departId, areaProvince,
                areaCity,areaCounty,roleId);
        Map<String, String> dictMajorMap = dictFeign.getByCode(User.JUDICIAL_PROFESSIONAL);
        if (userList != null && !userList.isEmpty()) {
            if (dictMajorMap != null && !dictMajorMap.isEmpty()) {
                this.getMajorsName(userList, dictMajorMap);
            }
        } else {
            userList = new ArrayList<>();
        }
        return new TableResultResponse<>(pageList.getTotal(), userList);
    }
    /**
     * 封装专业名称
     *
     * @param userList     用户信息集合
     * @param dictMajorMap 专业编码和专业名称集合
     */
    private void getMajorsName(List<Map<String, Object>> userList, Map<String, String> dictMajorMap) {
        String _major;
        String[] majors;
        StringBuilder majorsName = new StringBuilder();
        for (Map<String, Object> userMap : userList) {
            _major = String.valueOf(userMap.get("major"));
            if (StringUtils.isNotBlank(_major)) {
                majors = _major.split(",");
                if (BeanUtils.isNotEmpty(majors)) {
                    majorsName = new StringBuilder();
                    for (int i = 0; i < majors.length; i++) {
                        if(StringUtils.isNotBlank(dictMajorMap.get(majors[i]))){
                            //专业名称
                            majorsName.append(dictMajorMap.get(majors[i]));
                            if (i != majors.length - 1) {
                                majorsName.append(",");
                            }
                        }
                    }
                }
            }
            userMap.put("majorName", majorsName.toString());
        }
    }
}
