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

package com.bjzhianjia.scp.security.admin.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.admin.biz.MenuBiz;
import com.bjzhianjia.scp.security.admin.biz.PositionBiz;
import com.bjzhianjia.scp.security.admin.biz.UserBiz;
import com.bjzhianjia.scp.security.admin.entity.Menu;
import com.bjzhianjia.scp.security.admin.entity.Position;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.rpc.service.PermissionService;
import com.bjzhianjia.scp.security.admin.service.UserService;
import com.bjzhianjia.scp.security.admin.vo.AuthUser;
import com.bjzhianjia.scp.security.admin.vo.FrontUser;
import com.bjzhianjia.scp.security.admin.vo.MenuTree;
import com.bjzhianjia.scp.security.api.vo.user.UserInfo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * ${DESCRIPTION}
 *
 * @author scp
 * @version 1.0
 */
@RestController
@RequestMapping("user")
@CheckUserToken
@CheckClientToken
@Api(tags = "用户模块")
public class UserController extends BaseController<UserBiz, User, String> {

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private PositionBiz positionBiz;

	@Autowired
	private MenuBiz menuBiz;
	@Autowired
	private UserService userService;

	@IgnoreUserToken
	@ApiOperation("账户密码验证")
	@RequestMapping(value = "/validate", method = RequestMethod.POST)
	public ObjectRestResponse<UserInfo> validate(String username, String password) {
		return new ObjectRestResponse<UserInfo>().data(permissionService.validate(username, password));
	}

	@IgnoreUserToken
	@ApiOperation("根据账户名获取用户信息")
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	public ObjectRestResponse<AuthUser> validate(String username) {
		AuthUser user = new AuthUser();
		BeanUtils.copyProperties(baseBiz.getUserByUsername(username), user);
		return new ObjectRestResponse<AuthUser>().data(user);
	}

	@ApiOperation("账户修改密码")
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ObjectRestResponse<Boolean> changePassword(String oldPass, String newPass) {
		return new ObjectRestResponse<Boolean>().data(baseBiz.changePassword(oldPass, newPass));
	}

	@ApiOperation("获取用户信息接口")
	@RequestMapping(value = "/front/info", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> getUserInfo() throws Exception {
		FrontUser userInfo = permissionService.getUserInfo();
		if (userInfo == null) {
			return ResponseEntity.status(401).body(false);
		} else {
			return ResponseEntity.ok(userInfo);
		}
	}

	@ApiOperation("获取用户访问菜单")
	@RequestMapping(value = "/front/menus", method = RequestMethod.GET)
	public @ResponseBody List<MenuTree> getMenusByUsername() throws Exception {
		return permissionService.getMenusByUsername();
	}

	@ApiOperation("获取所有菜单")
	@RequestMapping(value = "/front/menu/all", method = RequestMethod.GET)
	public @ResponseBody List<Menu> getAllMenus() throws Exception {
		return menuBiz.selectListAll();
	}

	@ApiOperation("获取用户可管辖部门id列表")
	@RequestMapping(value = "/dataDepart", method = RequestMethod.GET)
	public List<String> getUserDataDepartIds(String userId) {
		if (BaseContextHandler.getUserID().equals(userId)) {
			return baseBiz.getUserDataDepartIds(userId);
		}
		return new ArrayList<>();
	}

	@ApiOperation("获取用户流程审批岗位")
	@RequestMapping(value = "/flowPosition", method = RequestMethod.GET)
	public List<String> getUserFlowPositions(String userId) {
		if (BaseContextHandler.getUserID().equals(userId)) {
			return positionBiz.getUserFlowPosition(userId).stream().map(Position::getName).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	@ApiOperation("批量获取人员信息")
	@RequestMapping(value = "/getByPK/{id}", method = RequestMethod.GET)
	public Map<String, String> getUser(@PathVariable String id) {
		return this.baseBiz.getUsers(id);
	}

	/**
	 * @author 尚
	 * @param deptId
	 * @return
	 */
	@ApiOperation("根据部门获取人员列表")
	@RequestMapping(value = "/getByDept", method = RequestMethod.GET)
	public JSONObject getUserByDept(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int limit, @RequestParam(value = "id") String deptId) {
		return userService.getUserByDept(deptId, page, limit);
	}

	/**
	 * @author 尚
	 * @param name
	 * @return
	 */
	@ApiOperation("根据人名进行模糊查询")
	@RequestMapping(value = "/getByName", method = RequestMethod.GET)
	public JSONObject getUsersByName(@RequestParam(value = "name") String name,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit) {
		return userService.getUsersByName(name, page, limit);
	}
	
    /**
     * 根据username查询用户部门ID
     * 
     * @param name
     * @return
     */
    @ApiOperation("根据username查询用户部门ID")
    @RequestMapping(value = "/getDepartIdByUsername", method = RequestMethod.GET)
    public ResponseEntity<String> getDepartIdByUserame(@RequestParam(value = "username") String username) {
        String departId = this.baseBiz.getDepartIdByUsername(username);
        if (StringUtils.isEmpty(departId)) {
            return ResponseEntity.status(401).body("");
        } else {
            return ResponseEntity.ok(departId);
        }
    }
    
    
    /**
     * 根据username查询tenantID
     * 
     * @param name
     * @return
     */
    @ApiOperation("根据username查询用户TenantID")
    @RequestMapping(value = "/getTenantIdByUsername", method = RequestMethod.GET)
    public ResponseEntity<String> getTenantIdByUserame(@RequestParam(value = "username") String username) {
        String tenantId = this.baseBiz.getTenantIdByUsername(username);
        if (StringUtils.isEmpty(tenantId)) {
            return ResponseEntity.status(401).body("");
        } else {
            return ResponseEntity.ok(tenantId);
        }
    }
	
	
}
