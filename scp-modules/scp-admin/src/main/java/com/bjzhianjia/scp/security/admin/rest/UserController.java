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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
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
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
		User oldUser = baseBiz.getUserByUsername(username);
		//没有查询到数据，则返回对象
		if(oldUser == null){
			return new ObjectRestResponse<AuthUser>().data(user);
		}
		BeanUtils.copyProperties(oldUser, user);
		return new ObjectRestResponse<AuthUser>().data(user);
	}
	
	@ApiOperation("账户修改密码")
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public ObjectRestResponse<Boolean> changePassword(String oldPass, String newPass) {
		return new ObjectRestResponse<Boolean>().data(baseBiz.changePassword(oldPass, newPass));
	}
	/**
	 * 管理员重置密码
	 * @param username 用户账号
	 * @param newPass 用户新密码
	 * @return
	 */
    @ApiOperation("管理员重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ObjectRestResponse<Boolean> resetPassword(String username,String newPass) {
        ObjectRestResponse<Boolean> result = new ObjectRestResponse<>();
        if(StringUtils.isBlank(username)){
            result.setMessage("非法参数");
            result.setStatus(400);
            return result;
        }
        if(StringUtils.isBlank(newPass)){
            result.setMessage("请输入密码!");
            result.setStatus(400);
            return result;
        }
        return baseBiz.resetPassword(username,newPass);
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
	public JSONArray getUsersByName(@RequestParam(value = "name") String name) {
		return userService.getUsersByName(name);
	}
	
	/**
	 * 根据人名进行模糊查询，翻页
     * @param name
     * @return
     */
    @ApiOperation("根据人名进行模糊查询")
    @RequestMapping(value = "/userNames", method = RequestMethod.GET)
    public TableResultResponse<Map<String,Object>>  getUsersByName(@RequestParam(value = "name",defaultValue="") String name,
        @RequestParam(value="page", defaultValue="1") Integer page,
        @RequestParam(value="limit", defaultValue="10") Integer limit) {
       return this.baseBiz.getUsersByFakeName(name, page, limit);
    }
	
	
	/**
	 * 
	 * @param userId 用户Id
	 * @return
	 */
	@ApiOperation("获取用户详情，包括部门及岗位")
	@RequestMapping(value="/get/user/detail",method=RequestMethod.GET)
	public JSONArray getUserDetail(@RequestParam(value="userId") @ApiParam("待查询人员ID") String userId) {
        return this.baseBiz.getUserDetail(userId);
	}
	/**
     * 通过用户ids查询
     * @param userIds
     * @return
     */
    @ApiOperation("通过用户ids查询")
    @RequestMapping(value="/ids",method=RequestMethod.GET)
    public JSONArray getByUserIds(@RequestParam(value="userIds") @ApiParam("待查询人员Ids") String userIds) {
        if(StringUtils.isBlank(userIds)) {
            return new JSONArray();
        }
        return this.baseBiz.getByUserIds(userIds);
    }

	/**
	 *	通过用户id获取信息
	 * @param userId 用户Id
	 * @return
	 */
	@ApiOperation("获取用户基本详情")
	@GetMapping(value="/{userId}/info")
	public User getUserInfo(@PathVariable(value="userId") @ApiParam("待查询人员ID") String userId) {
		if(StringUtils.isBlank(userId)) {
			return new User();
		}
		return this.baseBiz.selectById(userId);
	}

	@ApiOperation("按组获取用户信息")
	@GetMapping(value="/list/squadronLeader")
	public List<JSONObject> getSquadronLeader(){
		return this.baseBiz.getSquadronLeader();
	}

	@ApiOperation("通讯录列表")
	@GetMapping(value="/phoneList")
	public TableResultResponse<Map<String,Object>> phoneList(
			@RequestParam(value = "userName",defaultValue = "") @ApiParam("用户名称")  String userName,
			@RequestParam(value ="deptIds",defaultValue = "") @ApiParam("用户部门ids")  String deptIds,
        	@RequestParam(value="page", defaultValue="1") Integer page,
			@RequestParam(value="limit", defaultValue="10") Integer limit){
		List<String> ids = null;
		if(StringUtils.isNotEmpty(deptIds)){
			ids = Arrays.asList(deptIds.split(","));
		}
		return this.baseBiz.getPhoneList(userName,ids,page,limit);
	}

	@ApiOperation("按组ID集合获取用户")
	@RequestMapping(value = "/list/groupIds", method = RequestMethod.GET)
	@ResponseBody
	public List<JSONObject> getUsersForReturnJObj(@RequestParam("groupIds") String groupIds) {
		return baseBiz.selectLeaderOrMemberByGroupId(groupIds);
	}

	@GetMapping("/list/byDeptIds")
	@ApiOperation("按部门ID集合查询用户")
	public List<JSONObject> getUsersByDeptIds(@RequestParam("deptIds") String deptIds){
		return baseBiz.getUsersByDeptIds(deptIds);
	}
	@IgnoreUserToken
	@GetMapping("/count")
	@ApiOperation("获取非删除的所有用户总数")
	public ObjectRestResponse<Integer> getAllCount(){
		ObjectRestResponse<Integer> result = new ObjectRestResponse();
		result.setData(baseBiz.getAllCount());
		return result;
	}

	@ApiOperation("按组CODE集合获取用户")
	@RequestMapping(value = "/list/groupCode", method = RequestMethod.GET)
	@ResponseBody
	public List<JSONObject> getLeaderOrMemberByGroupCode(@RequestParam("groupCode") String groupCode) {
		return baseBiz.selectLeaderOrMemberByGroupCode(groupCode);
	}
}
