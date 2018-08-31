package com.bjzhianjia.scp.security.admin.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.admin.biz.DepartBiz;
import com.bjzhianjia.scp.security.admin.entity.Depart;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.vo.DepartTree;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.common.util.TreeUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author scp
 */
@RestController
@RequestMapping("depart")
@CheckClientToken
@CheckUserToken
@Api(tags = "部门管理")
public class DepartController extends BaseController<DepartBiz, Depart, String> {
	@Autowired
	private DepartBiz departBiz;

	@ApiOperation("获取部门树")
	@RequestMapping(value = "/tree", method = RequestMethod.GET)
	public List<DepartTree> getTree() {
		List<Depart> departs = this.baseBiz.selectListAll();
		List<DepartTree> trees = new ArrayList<>();
		departs.forEach(dictType -> {
			trees.add(new DepartTree(dictType.getId(), dictType.getParentId(), dictType.getName(), dictType.getCode()));
		});
		return TreeUtil.bulid(trees, "-1", null);
	}

	@ApiOperation("获取部门关联用户")
	@RequestMapping(value = "user", method = RequestMethod.GET)
	public TableResultResponse<User> getDepartUsers(String departId, String userName) {
		return this.baseBiz.getDepartUsers(departId, userName);
	}

	@ApiOperation("部门添加用户")
	@RequestMapping(value = "user", method = RequestMethod.POST)
	public ObjectRestResponse<Boolean> addDepartUser(String departId, String userIds) {
		this.baseBiz.addDepartUser(departId, userIds);
		return new ObjectRestResponse<Boolean>().data(true);
	}

	@ApiOperation("部门移除用户")
	@RequestMapping(value = "user", method = RequestMethod.DELETE)
	public ObjectRestResponse<Boolean> delDepartUser(String departId, String userId) {
		this.baseBiz.delDepartUser(departId, userId);
		return new ObjectRestResponse<Boolean>().data(true);
	}

	@ApiOperation("获取部门信息")
	@RequestMapping(value = "getByPK/{id}", method = RequestMethod.GET)
	public Map<String, String> getDepart(@PathVariable String id) {
		return this.baseBiz.getDeparts(id);
	}

	@ApiOperation("获取部门层级信息")
	@RequestMapping(value = "getLayerByPK/{id}", method = RequestMethod.GET)
	public Map<String, String> getLayerDepart(@PathVariable String id) {
		return this.baseBiz.getLayerDeparts(id);
	}

	/**
	 * 根据父部门获取子部门
	 * 
	 * @author 尚
	 * @param parentId
	 * @return
	 */
	@ApiOperation("根据父部门获取子部门")
	@RequestMapping(value = "/getDepartByParent/{parentId}", method = RequestMethod.GET)
	public JSONArray getDeptByParent(@PathVariable(value = "parentId") String parentId) {
		List<Depart> deptList = departBiz.getDeptByParent(parentId);
		return JSONArray.parseArray(JSON.toJSONString(deptList));
	}
	@ApiOperation("通过部门id获取")
	@RequestMapping(value = "/id/{parentId}", method = RequestMethod.GET)
	public JSONObject getByDeptId(@PathVariable(value = "parentId") String parentId) {
		return  JSONObject.parseObject(JSON.toJSONString(this.baseBiz.selectById(parentId)));
	}
}