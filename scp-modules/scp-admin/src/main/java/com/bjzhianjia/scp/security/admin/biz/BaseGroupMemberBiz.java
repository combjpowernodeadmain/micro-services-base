package com.bjzhianjia.scp.security.admin.biz;

import org.springframework.beans.*;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.admin.entity.BaseGroupMember;
import com.bjzhianjia.scp.security.admin.mapper.BaseGroupMemberMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
}