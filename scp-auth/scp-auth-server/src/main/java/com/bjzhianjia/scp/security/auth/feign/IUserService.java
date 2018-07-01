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

package com.bjzhianjia.scp.security.auth.feign;

import com.bjzhianjia.scp.security.auth.configuration.FeignConfiguration;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;


/**
 * ${DESCRIPTION}
 *
 * @author scp
 * @version 1.0 
 */
@FeignClient(value = "${jwt.user-service}",configuration = FeignConfiguration.class)
public interface IUserService {
  /**
   * 通过账户\密码的方式登陆
   * @param username
   * @param password
   * @return
     */
  @RequestMapping(value = "/user/validate", method = RequestMethod.POST)
  public ObjectRestResponse<Map<String,String>> validate(@RequestParam("username") String username, @RequestParam("password") String password);
  @RequestMapping(value = "/user/info", method = RequestMethod.POST)
  public ObjectRestResponse<Map<String,String>> getUserInfoByUsername(@RequestParam("username") String username);
}
