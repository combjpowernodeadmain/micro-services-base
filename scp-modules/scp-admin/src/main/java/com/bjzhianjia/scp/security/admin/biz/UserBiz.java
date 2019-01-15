
package com.bjzhianjia.scp.security.admin.biz;

import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.common.util.EntityUtils;
import com.bjzhianjia.scp.security.common.util.Query;
import com.bjzhianjia.scp.security.common.util.Sha256PasswordEncoder;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.feign.DictFeign;
import com.bjzhianjia.scp.security.admin.mapper.DepartMapper;
import com.bjzhianjia.scp.security.admin.mapper.PositionMapper;
import com.bjzhianjia.scp.security.admin.mapper.UserMapper;
import com.bjzhianjia.scp.security.admin.vo.PositionVo;
import com.bjzhianjia.scp.security.common.biz.BaseBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    @Autowired
    private PositionMapper positionMapper;

    private Sha256PasswordEncoder encoder = new Sha256PasswordEncoder();
    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private Environment environment;

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
    /**
     * 管理员重置用户密码
     * @param oldPass
     * @param newPass
     * @return
     */
    public ObjectRestResponse<Boolean> resetPassword(String username , String newPass) {
        ObjectRestResponse<Boolean> result = new ObjectRestResponse<>();
        result.setStatus(400);
        result.setData(false);

        // 如果非超级管理员,无法重置用户的密码
        // if
        // (BooleanUtil.BOOLEAN_FALSE.equals(mapper.selectByPrimaryKey(BaseContextHandler.getUserID()).getIsSuperAdmin()))
        // {
        // result.setMessage("当前用户没有权限！");
        // return result;
        // }

        User user = this.getByUsername(username);
        if (user != null && StringUtils.isNotBlank(user.getPassword())) {
            String password = encoder.encode(newPass);
            user.setPassword(password);
            this.updateSelectiveById(user);

            result.setStatus(200);
            result.setData(true);
            return result;
        }
        return result;
    }


    @Override
    public void insertSelective(User entity) {
        //判断是否存在旧用户
        User user = new User();
        user.setUsername(entity.getUsername());
        User oldUser = mapper.selectOne(user);
        if (oldUser != null) {
            //存在并且逻辑删除了，则修改username值（userId+"_"+userName)
            if (BooleanUtil.BOOLEAN_TRUE.equals(oldUser.getIsDeleted())) {
                oldUser.setUsername(oldUser.getId() + "_" + oldUser.getUsername());
                this.updateSelectiveById(oldUser);
            } else {
                throw new RuntimeException("当前账户已存在，请重新填写！");
            }
        }
        //添加新用户
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
     *      去除删除用户和禁用用户
     */
    public User getUserByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        user.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        user.setIsDisabled(BooleanUtil.BOOLEAN_FALSE);
        return mapper.selectOne(user);
    }

    /**
     * 根据用户名获取用户信息
     *
     * @param username
     * @return
     *     去除删除用户
     */
    public User getByUsername(String username) {
        User user = new User();
        user.setUsername(username);
        user.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        return mapper.selectOne(user);
    }

    @Override
    public void query2criteria(Query query, Example example) {
        if (query.entrySet().size() > 0) {
            for (Map.Entry<String, Object> entry : query.entrySet()) {
                Example.Criteria criteria = example.createCriteria();
                //查询未删除的用户
                criteria.andEqualTo("isDeleted",BooleanUtil.BOOLEAN_FALSE);
                criteria.andLike(entry.getKey(), "%" + entry.getValue().toString() + "%");
                example.or(criteria);
            }
        }
        //创建和更新时间降序
        example.setOrderByClause(" crt_time desc , upd_time desc");
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
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        List<User> userList = mapper.selectByExample(example);
        if(userList == null) {
            userList = new ArrayList<>();
        }
        return userList;
    }

    /**
     * 按人名进行模糊查询，翻页
     * @param name 用户名称
     * @param page 页面
     * @param limit 页容量
     * @return
     */
    public TableResultResponse<Map<String, Object>> getUsersByFakeName(String name, int page, int limit) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andLike("name", "%" + name + "%");
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        Page<Object> pageList = PageHelper.startPage(page, limit);
        List<User> userList = mapper.selectByExample(example);
        if (userList == null) {
            userList = new ArrayList<>();
        }
        List<Map<String, Object>> data = this.bindPositionToUser(userList);
        if(data == null) {
            data = new ArrayList<>();
        }
        return new TableResultResponse<>(pageList.getTotal(),data);
    }
    /**
     * 将人员与其所在的职位进行绑定
     *
     * @author 尚
     * @param userList
     * @return
     */
    private List<Map<String , Object>> bindPositionToUser(List<User> userList) {
        List<Map<String , Object>> result = new ArrayList<>();
        List<String> userIdList = userList.stream().map((o) -> o.getId()).distinct().collect(Collectors.toList());

        String join = String.join(",", userIdList);
        join = "'" + join.replaceAll(",", "','") + "'";

        List<PositionVo> positionMapList = positionMapper.selectPositionByUser(join);

        //封装用户职务
        //数据格式：userId：username1，username2
        Map<String , String> positionMap = new HashMap<>();
        if(positionMapList != null) {
            for(PositionVo positionVo : positionMapList) {
                String userName = positionMap.get(positionVo.getUserId());
                if(StringUtils.isNotBlank(userName)) {
                    positionMap.put(positionVo.getUserId(), userName+","+positionVo.getName());
                }else {
                    positionMap.put(positionVo.getUserId(), positionVo.getName());
                }
            }
        }
        if(userList != null) {
            Map<String , Object> data = null;
            for(User user : userList) {
                data = new HashMap<>();
                data.put("id", user.getId());
                data.put("name", user.getName());
                data.put("position", positionMap.get(user.getId()));
                result.add(data);
            }
        }
        return result;
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
		userId = "'" + userId.replaceAll(",", "','") + "'";
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

    /**
     * 获取中队长信息
     * @return
     */
    public List<JSONObject> getSquadronLeader(){
    	List<JSONObject> userList = this.mapper.getUserByPosition(environment.getProperty("gruop.code"));
    	return userList;
    }

    /**
     * 获取通讯录（排除超级管理员）
     *
     * @param userName 用户名称
     * @param deptIds  部门ids
     * @return
     */
    public TableResultResponse<Map<String, Object>> getPhoneList(String userName, List<String> deptIds, int page,
                                                                 int limit) {
        Page<Object> pageHelper = PageHelper.startPage(page, limit);
        List<Map<String, Object>> result = this.mapper.selectPhoneList(userName, deptIds);
        //拼接用户id
        StringBuilder userIds = new StringBuilder();
        if (BeanUtils.isNotEmpty(result)) {
            Map<String, Object> map;
            for (int i = 0; i < result.size(); i++) {
                map = result.get(i);
                if (map == null) {
                    continue;
                }
                userIds.append("'").append(map.get("userId")).append("'");
                if (i < result.size() - 1) {
                    userIds.append(",");
                }
            }

            //获取用户职位信息
            List<PositionVo> positionList;
            if(StringUtils.isNotBlank(userIds.toString())){
                positionList = positionMapper.selectPositionByUser(userIds.toString());
            }else{
                positionList = new ArrayList<>();
            }
            //positionMap 格式：userid：position,position2
            Map<String, String> positionMap = new HashMap<>();
            for (PositionVo positionVo : positionList) {
                String positions = positionMap.get(positionVo.getUserId());
                if (positions == null) {
                    positionMap.put(positionVo.getUserId(), positionVo.getName());
                } else {
                    positionMap.put(positionVo.getUserId(), positions + "," + positionVo.getName());
                }
            }
            //结果集封装岗位信息
            for (Map<String, Object> tmap : result) {
                String positions = positionMap.get(tmap.get("userId"));
                tmap.put("positions", positions);
            }
        }

        result = BeanUtils.isEmpty(result) ? new ArrayList<>() : result;
        return new TableResultResponse<>(pageHelper.getTotal(), result);
    }

    /**
     * 按组ID集合获取用户
     * @param groupIds
     * @return
     */
    public List<JSONObject> selectLeaderOrMemberByGroupId(String groupIds){
        if(org.apache.commons.lang3.StringUtils.isBlank(groupIds)){
            return new ArrayList<>();
        }

        Set<String> groupIdSet=new HashSet<>(Arrays.asList(groupIds.split(",")));
        return this.mapper.selectLeaderOrMemberByGroupId(groupIdSet);
    }

    /**
     * 按部门ID集合获取用户
     * @param deptIds
     * @return
     */
    public List<JSONObject> getUsersByDeptIds(String deptIds){
        if(org.apache.commons.lang3.StringUtils.isBlank(deptIds)){
            return new ArrayList<>();
        }

        Set<String> deptIdSet=new HashSet<>(Arrays.asList(deptIds.split(",")));
        return this.mapper.selectUserListByDepts(deptIdSet);
    }

    /**
     * 获取非删除的所有用户总数
     * @return
     */
    public int getAllCount(){
        Example example = new Example(User.class);
        Example.Criteria citeria = example.createCriteria();
        citeria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        return this.mapper.selectCountByExample(example);
    }

    /**
     * 按组CODE集合获取用户
     * @param groupCode 组ID
     * @return
     */
    public List<JSONObject> selectLeaderOrMemberByGroupCode(String groupCode) {
        if(org.apache.commons.lang3.StringUtils.isBlank(groupCode)){
            return new ArrayList<>();
        }

        Set<String> groupCodeSet=new HashSet<>(Arrays.asList(groupCode.split(",")));
        return this.mapper.selectLeaderOrMemberByGroupCode(groupCodeSet);
    }
}
