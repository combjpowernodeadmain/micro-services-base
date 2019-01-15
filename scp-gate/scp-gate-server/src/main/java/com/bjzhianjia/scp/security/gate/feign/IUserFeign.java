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

package com.bjzhianjia.scp.security.gate.feign;

import com.bjzhianjia.scp.security.api.vo.authority.PermissionInfo;
import com.bjzhianjia.scp.security.gate.config.FeignConfiguration;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author scp
 * @version 1.0 
 */
@FeignClient(value = "scp-admin",configuration = FeignConfiguration.class)
public interface IUserFeign {
  /**
   * 获取用户的菜单和按钮权限
   * @return
     */
  @RequestMapping(value="/api/user/permissions",method = RequestMethod.GET)
  public List<PermissionInfo> getPermissionByUsername();

  /**
   * 获取所有菜单和按钮权限
   * @return
     */
  @RequestMapping(value="/api/permissions",method = RequestMethod.GET)
  List<PermissionInfo> getAllPermissionInfo();
}
