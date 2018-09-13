/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.bjzhianjia.scp.security.admin.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.mapper.DepartMapper;
import com.bjzhianjia.scp.security.admin.mapper.UserMapper;
import com.bjzhianjia.scp.security.common.biz.BaseBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.common.util.EntityUtils;
import com.bjzhianjia.scp.security.common.util.Query;
import com.bjzhianjia.scp.security.common.util.Sha256PasswordEncoder;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * ${DESCRIPTION}
 *
 * @author scp
 * @version 1.0 
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper, User> {
    @Autowired
    private MergeCore mergeCore;
    @Autowired
    private DepartMapper departMapper;

    private Sha256PasswordEncoder encoder = new Sha256PasswordEncoder();


    @Override
    public User selectById(Object id) {
        User user = super.selectById(id);
        try {
            mergeCore.mergeOne(User.class, user);
            return user;
        } catch (Exception e) {
            return super.selectById(id);
        }
    }

    public Boolean changePassword(String oldPass, String newPass) {
        User user = this.getUserByUsername(BaseContextHandler.getUsername());
        if (encoder.matches(oldPass, user.getPassword())) {
            String password = encoder.encode(newPass);
            user.setPassword(password);
            this.updateSelectiveById(user);
            return true;
        }
        return false;
    }

    @Override
    public void insertSelective(User entity) {
        String password = encoder.encode(entity.getPassword());
        String departId = entity.getDepartId();
        EntityUtils.setCreatAndUpdatInfo(entity);
        entity.setPassword(password);
        entity.setDepartId(departId);
        entity.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        entity.setIsDisabled(BooleanUtil.BOOLEAN_FALSE);
        String userId = UUIDUtils.generateUuid();
        entity.setTenantId(BaseContextHandler.getTenantID());
        entity.setId(userId);
        entity.setIsSuperAdmin(BooleanUtil.BOOLEAN_FALSE);
        // 如果非超级管理员,无法修改用户的租户信息
        if (BooleanUtil.BOOLEAN_FALSE.equals(mapper.selectByPrimaryKey(BaseContextHandler.getUserID()).getIsSuperAdmin())) {
            entity.setIsSuperAdmin(BooleanUtil.BOOLEAN_FALSE);
        }
        departMapper.insertDepartUser(UUIDUtils.generateUuid(), entity.getDepartId(), entity.getId(),BaseContextHandler.getTenantID());
        super.insertSelective(entity);
    }

    @Override
    public void updateSelectiveById(User entity) {
        EntityUtils.setUpdatedInfo(entity);
        User user = mapper.selectByPrimaryKey(entity.getId());
        if (!user.getDepartId().equals(entity.getDepartId())) {
            departMapper.deleteDepartUser(user.getDepartId(), entity.getId());
            departMapper.insertDepartUser(UUIDUtils.generateUuid(), entity.getDepartId(), entity.getId(),BaseContextHandler.getTenantID());
        }
        // 如果非超级管理员,无法修改用户的租户信息
        if (BooleanUtil.BOOLEAN_FALSE.equals(mapper.selectByPrimaryKey(BaseContextHandler.getUserID()).getIsSuperAdmin())) {
            entity.setTenantId(BaseContextHandler.getTenantID());
        }
        // 如果非超级管理员,无法修改用户的租户信息
        if (BooleanUtil.BOOLEAN_FALSE.equals(mapper.selectByPrimaryKey(BaseContextHandler.getUserID()).getIsSuperAdmin())) {
            entity.setIsSuperAdmin(BooleanUtil.BOOLEAN_FALSE);
        }
        super.updateSelectiveById(entity);
    }

    @Override
    public void deleteById(Object id) {
        User user = mapper.selectByPrimaryKey(id);
        user.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);
        this.updateSelectiveById(user);
    }

    @Override
    public List<User> selectByExample(Object obj) {
        Example example = (Example) obj;
        example.createCriteria().andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        List<User> users = super.selectByExample(example);
        try {
            mergeCore.mergeResult(User.class, users);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return users;
        }
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     */
    public User getUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        user.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        user.setIsDisabled(BooleanUtil.BOOLEAN_FALSE);
        return mapper.selectOne(user);
    }

    @Override
    public void query2criteria(Query query, Example example) {
        if (query.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                Example.Criteria criteria = example.createCriteria();
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
                example.or(criteria);
            }
        }
    }

    public List<String> getUserDataDepartIds(String userId) {
        return mapper.selectUserDataDepartIds(userId);
    }
    
    /**
      *  根据ID批量获取人员
     * @param departIDs
     * @return
     */
    public Map<String,String> getUsers(String userIds){
        if(StringUtils.isBlank(userIds)) {
            return new HashMap<>();
        }
        userIds = "'"+userIds.replaceAll(",","','")+"'";
        List<User> users = mapper.selectByIds(userIds);
        return users.stream().collect(Collectors.toMap(User::getId, user -> JSONObject.toJSONString(user)));
    }
    
    /**
     * 查询部门deptId下的人员
     * @author 尚
     * @param deptId
     * @return
     */
    public TableResultResponse<User> getUserByDept(String deptId,int page,int limit){
    	
    	Page<Object> result =PageHelper.startPage(page, limit);
    	List<User> userList = departMapper.selectDepartUsers(deptId, null);
    	
//    	Example example=new Example(User.class);
//    	Example.Criteria criteria=example.createCriteria();
//    	criteria.andEqualTo("departId", deptId);
//    	criteria.andEqualTo("isDeleted","0");
//    	example.orderBy("id");
//    	Page<Object> result =PageHelper.startPage(page, limit);
//    	List<User> userList = mapper.selectByExample(example);
    	return new TableResultResponse<User>(result.getTotal(), userList);
    }
    
    /**
     * 按人名进行模糊查询
     * @author 尚
     * @param name
     * @return
     */
    public List<User> getUsersByFakeName(String name){
    	Example example=new Example(User.class);
    	Example.Criteria criteria=example.createCriteria();
    	criteria.andLike("name", "%"+name+"%");
    	List<User> userList = mapper.selectByExample(example);
    	if(userList == null) {
    	   userList = new ArrayList<>();
    	}    	    
    	return userList;
    }
    
    /***********************************************
     * 仅供工作流使用
     */
    
    /**
     * 根据userid查询部门ID
     * 
     * @param userid
     * @return
     */
    public String getDepartIdByUserId(String userid) {
        return mapper.selectDepartIdByUserId(userid);
    }
    
    /**
     * 根据userid查询部门ID
     * 
     * @param userid
     * @return
     */
    public String getTenantIdByUserId(String userid) {
        return mapper.selectTenantIdByUserId(userid);
    }
    
    
    /**
     * 根据UserId获取角色codes
     * 
     * @return
     */
    public List<String> getGroupCodesByUserId(String userid) {
        List<String> leaderGroupCodes = mapper.selectLearderGroupCodesByUserId(userid);
        leaderGroupCodes.addAll(mapper.selectMemberGroupCodesByUserId(userid));
        return leaderGroupCodes;
    }


    /**
     * 获取用户详情，包括部门及岗位
     * @param userId
     * @return
     */
    public JSONArray getUserDetail(String userId) {
        List<Map<String, String>> userDetail = mapper.getUserDetail(userId);
        return JSONArray.parseArray(JSON.toJSONString(userDetail));
    }
    /**
     * 通过用户ids查询
     * @param userIds
     * @return
     */
    public JSONArray getByUserIds(String userIds) {
        JSONArray array = new JSONArray();
        Example example=new Example(User.class);
        
        List<String> _userIds = Arrays.asList(userIds.split(","));
        Example.Criteria criteria = example.createCriteria();
        if(_userIds != null && !_userIds.isEmpty()) {
            criteria.andIn("id", _userIds);
        }
        List<User> userList =  mapper.selectByExample(example);
        if(userList != null && !userList.isEmpty()) {
            JSONObject obj = null;
            for(User user : userList) {
                 obj = JSONObject.parseObject(JSONObject.toJSONString(user));
                 array.add(obj);
            }
        }
        return array;
    }
}
