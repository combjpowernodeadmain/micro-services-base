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

import com.bjzhianjia.scp.security.admin.entity.ResourceAuthority;
import com.bjzhianjia.scp.security.admin.mapper.ResourceAuthorityMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by scp on 2017/6/19.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ResourceAuthorityBiz extends BusinessBiz<ResourceAuthorityMapper,ResourceAuthority> {
    public void deleteByAuthorityIdAndResourceType(String s, String resourceTypeMenu, String type) {
        this.mapper.deleteByAuthorityIdAndResourceType(s,resourceTypeMenu,type);
    }
}
