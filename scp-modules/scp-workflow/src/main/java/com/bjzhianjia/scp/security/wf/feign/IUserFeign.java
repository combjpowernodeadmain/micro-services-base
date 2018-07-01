/*
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */

package com.bjzhianjia.scp.security.wf.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;

import java.util.List;

/**
 * @author scp
 * @create 2018/2/11.
 */
@FeignClient(value = "scp-admin",configuration = FeignApplyConfiguration.class)
public interface IUserFeign {
    /**
     * 获取当前用户工作流的岗位Id列表
     * @return
     */
    @RequestMapping(value="/user/flowPosition",method = RequestMethod.GET)
    List<String> getUserFlowPositions(@RequestParam("userId") String userId);
}
