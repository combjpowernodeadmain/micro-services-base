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

import com.bjzhianjia.scp.security.admin.biz.GroupBiz;
import com.bjzhianjia.scp.security.admin.biz.ResourceAuthorityBiz;
import com.bjzhianjia.scp.security.admin.constant.AdminCommonConstant;
import com.bjzhianjia.scp.security.admin.entity.Element;
import com.bjzhianjia.scp.security.admin.entity.Group;
import com.bjzhianjia.scp.security.admin.vo.AuthorityMenuTree;
import com.bjzhianjia.scp.security.admin.vo.GroupTree;
import com.bjzhianjia.scp.security.admin.vo.GroupUsers;
import com.bjzhianjia.scp.security.admin.vo.MenuTree;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.common.util.TreeUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * ${DESCRIPTION}
 *
 * @author scp
 * @version 1.0 
 */
@RestController
@RequestMapping("group")
@Api("角色模块")
@CheckUserToken
@CheckClientToken
public class GroupController extends BaseController<GroupBiz, Group,String> {
    @Autowired
    private ResourceAuthorityBiz resourceAuthorityBiz;
    @ApiOperation("获取角色列表")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Group> list(String name, String groupType) {
        if (StringUtils.isBlank(name) && StringUtils.isBlank(groupType)) {
            return new ArrayList<Group>();
        }
        Example example = new Example(Group.class);
        if (StringUtils.isNotBlank(name)) {
            example.createCriteria().andLike("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(groupType)) {
            example.createCriteria().andEqualTo("groupType", groupType);
        }

        return baseBiz.selectByExample(example);
    }

    @ApiOperation("用户关联角色")
    @RequestMapping(value = "/{id}/user", method = RequestMethod.PUT)
    @ResponseBody
    public ObjectRestResponse modifiyUsers(@PathVariable String id, String members, String leaders) {
        baseBiz.modifyGroupUsers(id, members, leaders);
        return new ObjectRestResponse();
    }

    @ApiOperation("获取角色关联用户")
    @RequestMapping(value = "/{id}/user", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<GroupUsers> getUsers(@PathVariable String id) {
        return new ObjectRestResponse<GroupUsers>().data(baseBiz.getGroupUsers(id));
    }

    @ApiOperation("分配角色可访问菜单")
    @RequestMapping(value = "/{id}/authority/menu", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse modifyMenuAuthority(@PathVariable String id, String menuTrees) {
        String[] menus = menuTrees.split(",");
        baseBiz.modifyAuthorityMenu(id, menus, AdminCommonConstant.RESOURCE_TYPE_VIEW);
        return new ObjectRestResponse();
    }

    @ApiOperation("获取角色可访问菜单")
    @RequestMapping(value = "/{id}/authority/menu", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<AuthorityMenuTree>> getMenuAuthority(@PathVariable String id) {
        return new ObjectRestResponse().data(baseBiz.getAuthorityMenu(id, AdminCommonConstant.RESOURCE_TYPE_VIEW));
    }

    @ApiOperation("角色分配菜单可访问资源")
    @RequestMapping(value = "/{id}/authority/element/add", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse addElementAuthority(@PathVariable String id, String menuId, String elementId) {
        baseBiz.modifyAuthorityElement(id, menuId, elementId, AdminCommonConstant.RESOURCE_TYPE_VIEW);
        return new ObjectRestResponse();
    }

    @ApiOperation("角色移除菜单可访问资源")
    @RequestMapping(value = "/{id}/authority/element/remove", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse removeElementAuthority(@PathVariable String id, String menuId, String elementId) {
        baseBiz.removeAuthorityElement(id, elementId, AdminCommonConstant.RESOURCE_TYPE_VIEW);
        return new ObjectRestResponse();
    }

    @ApiOperation("获取角色可访问资源")
    @RequestMapping(value = "/{id}/authority/element", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<String>> getElementAuthority(@PathVariable String id) {
        return new ObjectRestResponse().data(baseBiz.getAuthorityElement(id, AdminCommonConstant.RESOURCE_TYPE_VIEW));
    }

    @ApiOperation("分配角色可授权菜单")
    @RequestMapping(value = "/{id}/authorize/menu", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse modifyMenuAuthorize(@PathVariable String id, String menuTrees) {
        String[] menus = menuTrees.split(",");
        baseBiz.modifyAuthorityMenu(id, menus, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE);
        return new ObjectRestResponse();
    }

    @ApiOperation("获取角色可授权菜单")
    @RequestMapping(value = "/{id}/authorize/menu", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<AuthorityMenuTree>> getMenuAuthorize(@PathVariable String id) {
        return new ObjectRestResponse().data(baseBiz.getAuthorityMenu(id, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE));
    }

    @ApiOperation("角色分配菜单可授权资源")
    @RequestMapping(value = "/{id}/authorize/element/add", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse addElementAuthorize(@PathVariable String id, String menuId, String elementId) {
        baseBiz.modifyAuthorityElement(id, menuId, elementId, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE);
        return new ObjectRestResponse();
    }

    @ApiOperation("角色移除菜单可授权资源")
    @RequestMapping(value = "/{id}/authorize/element/remove", method = RequestMethod.POST)
    @ResponseBody
    public ObjectRestResponse removeElementAuthorize(@PathVariable String id, String menuId, String elementId) {
        baseBiz.removeAuthorityElement(id, elementId, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE);
        return new ObjectRestResponse();
    }

    @ApiOperation("获取角色可授权资源")
    @RequestMapping(value = "/{id}/authorize/element", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<String>> getElementAuthorize(@PathVariable String id) {
        return new ObjectRestResponse().data(baseBiz.getAuthorityElement(id, AdminCommonConstant.RESOURCE_TYPE_AUTHORISE));
    }

    @ApiOperation("获取角色树")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    @ResponseBody
    public List<GroupTree> tree(@RequestParam(value = "name",defaultValue = "") String name,
                                @RequestParam(value = "groupType",defaultValue = "")String groupType) {
        Example example = new Example(Group.class);
        if (StringUtils.isNotBlank(name)) {
            example.createCriteria().andLike("name", "%" + name + "%");
        }
        if (StringUtils.isNotBlank(groupType)) {
            example.createCriteria().andEqualTo("groupType", groupType);
        }
        return getTree(baseBiz.selectByExample(example), AdminCommonConstant.ROOT);
    }

    /**
     * 获取可管理的资源
     * @param menuId
     * @return
     */
    @ApiOperation("获取用户可管理资源")
    @RequestMapping(value = "/element/authorize/list", method = RequestMethod.GET)
    public TableResultResponse<Element> getAuthorizeElement(String menuId) {
        List<Element> elements = baseBiz.getAuthorizeElements(menuId);
        return new TableResultResponse<Element>(elements.size(), elements);
    }

    /**
     * 获取可管理的菜单
     * @return
     */
    @ApiOperation("获取用户可管理菜单")
    @RequestMapping(value = "/menu/authorize/list", method = RequestMethod.GET)
    public List<MenuTree> getAuthorizeMenus() {
        return TreeUtil.bulid(baseBiz.getAuthorizeMenus(), AdminCommonConstant.ROOT, null);
    }

    private List<GroupTree> getTree(List<Group> groups, String root) {
        List<GroupTree> trees = new ArrayList<GroupTree>();
        GroupTree node = null;
        for (Group group : groups) {
            node = new GroupTree();
            node.setLabel(group.getName());
            BeanUtils.copyProperties(group, node);
            trees.add(node);
        }
        return TreeUtil.bulid(trees, root, null);
    }

}
