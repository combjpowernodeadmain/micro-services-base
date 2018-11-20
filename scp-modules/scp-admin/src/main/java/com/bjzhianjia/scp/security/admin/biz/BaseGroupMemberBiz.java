package com.bjzhianjia.scp.security.admin.biz;

import com.ace.cache.annotation.CacheClear;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.admin.entity.BaseGroupMember;
import com.bjzhianjia.scp.security.admin.mapper.BaseGroupMemberMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * BaseGroupMember 用户角色关系.
 *
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018年10月14日          bo      chenshuai            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
@Service
@Transactional
public class BaseGroupMemberBiz extends BusinessBiz<BaseGroupMemberMapper, BaseGroupMember> {

    /**
     * 通过用户id和角色id查询
     *
     * @param userId  用户id
     * @param groupId 角色id
     * @return
     */
    public BaseGroupMember getBaseGroupMemberByUserIdAndGroupId(String userId, String groupId) {
        BaseGroupMember baseGroupMember = new BaseGroupMember();
        baseGroupMember.setUserId(userId);
        baseGroupMember.setGroupId(groupId);
        return this.selectOne(baseGroupMember);
    }

    /**
     * 通过用户id查询用户角色id列表
     *
     * @param userId 用户id
     * @return GroupId, GroupId2, GroupId3
     */
    public List<String> getGroupIdUserId(String userId) {
        List<String> result = new ArrayList<>();
        BaseGroupMember baseGroupMember = new BaseGroupMember();
        baseGroupMember.setUserId(userId);
        List<BaseGroupMember> data = this.selectList(baseGroupMember);
        if (data != null && !data.isEmpty()) {
            for (BaseGroupMember _baseGroupMember : data) {
                result.add(_baseGroupMember.getGroupId());
            }
        }
        return result;
    }

    /**
     * 用户角色列表
     * @param userId 用户id
     * @return
     */
    public List<Map<String, Object>> getNameByUserId(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> result = this.mapper.selectNameByUserId(userId);
        return com.bjzhianjia.scp.security.common.util.BeanUtils.isNotEmpty(result) ? result : new ArrayList<>();
    }

    /**
     * 更新指定用户角色列表
     *
     * @param userId   用户id
     * @param groupIds 角色id
     * @return
     */
    @CacheClear(pre = "permission")
    public void update(String userId, String groupIds) {
        if (StringUtils.isEmpty(userId)) {
            throw new RuntimeException("请选择需要修改的用户！");
        }
        //删除原有角色关系
        this.mapper.deleteByUserId(userId);
        //添加新关系数据
        String[] newGroupids = groupIds.split(",");
        BaseGroupMember baseGroupMember = null;
        for (int i = 0; i < newGroupids.length; i++) {
            if(StringUtils.isBlank(newGroupids[i])){
                continue;
            }
            baseGroupMember = new BaseGroupMember();
            baseGroupMember.setId(UUIDUtils.generateUuid());
            baseGroupMember.setGroupId(newGroupids[i]);
            baseGroupMember.setUserId(userId);
            this.mapper.insertSelective(baseGroupMember);
        }
    }
}