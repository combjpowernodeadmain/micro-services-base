package com.bjzhianjia.scp.security.admin.biz;

import com.bjzhianjia.scp.security.admin.entity.Tenant;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.mapper.TenantMapper;
import com.bjzhianjia.scp.security.admin.mapper.UserMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 租户表
 *
 * @author Mr.AG
 * @email 576866311@qq.com
 * @version 1.0 
 */
@Service
public class TenantBiz extends BusinessBiz<TenantMapper,Tenant> {
    @Autowired
    private UserMapper userMapper;

    public void updateUser(String id, String userId) {
        Tenant tenant = this.mapper.selectByPrimaryKey(id);
        tenant.setOwner(userId);
        updateSelectiveById(tenant);
        User user = userMapper.selectByPrimaryKey(userId);
        user.setTenantId(id);
        userMapper.updateByPrimaryKeySelective(user);
    }
}