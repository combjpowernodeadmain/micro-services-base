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

import com.ace.cache.annotation.Cache;
import com.ace.cache.annotation.CacheClear;
import com.bjzhianjia.scp.merge.annonation.MergeResult;
import com.bjzhianjia.scp.security.admin.constant.AdminCommonConstant;
import com.bjzhianjia.scp.security.admin.entity.Element;
import com.bjzhianjia.scp.security.admin.mapper.ElementMapper;
import com.bjzhianjia.scp.security.admin.mapper.UserMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author scp
 * @version 1.0 
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ElementBiz extends BusinessBiz<ElementMapper,Element> {
    @Autowired
    private UserMapper userMapper;
    /**
     * 获取用户可以访问的资源
     * @param userId
     * @return
     */
    @Cache(key="permission:ele:u{1}")
    public List<Element> getAuthorityElementByUserId(String userId){
        if(BooleanUtil.BOOLEAN_TRUE.equals(userMapper.selectByPrimaryKey(userId).getIsSuperAdmin())){
            return mapper.selectAllElementPermissions();
        }
       return mapper.selectAuthorityElementByUserId(userId,AdminCommonConstant.RESOURCE_TYPE_VIEW);
    }

    public List<Element> getAuthorityElementByUserId(String userId,String menuId){
        return mapper.selectAuthorityMenuElementByUserId(userId,menuId,AdminCommonConstant.RESOURCE_TYPE_VIEW);
    }

    @Cache(key="permission:ele")
    public List<Element> getAllElementPermissions(){
        return mapper.selectAllElementPermissions();
    }

    @Override
    @CacheClear(keys={"permission:ele","permission"})
    public void insertSelective(Element entity) {
        super.insertSelective(entity);
    }

    @Override
    @CacheClear(keys={"permission:ele","permission"})
    public void updateSelectiveById(Element entity) {
        super.updateSelectiveById(entity);
    }

    @MergeResult
    @Override
    public List<Element> selectByExample(Object example) {
        return super.selectByExample(example);
    }
}
