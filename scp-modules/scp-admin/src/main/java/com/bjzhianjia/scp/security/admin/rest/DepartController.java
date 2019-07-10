package com.bjzhianjia.scp.security.admin.rest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.admin.biz.DepartBiz;
import com.bjzhianjia.scp.security.admin.biz.MenuBiz;
import com.bjzhianjia.scp.security.admin.entity.Depart;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.vo.DepartTree;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.IgnoreUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.security.common.util.TreeUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Autowired
    private MenuBiz menuBiz;

    @ApiOperation("获取部门树")
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public List<DepartTree> getTree() {
        List<Depart> departs = this.baseBiz.selectListAll();
        List<DepartTree> trees = new ArrayList<>();
        departs.forEach(dictType -> {
            DepartTree departTree =
                new DepartTree(dictType.getId(), dictType.getParentId(), dictType.getName(), dictType.getCode());
            departTree.setOrderNum(dictType.getAttr2());
            trees.add(departTree);
        });
        return TreeUtil.bulid(trees, "-1", (Comparator<DepartTree>) (o1, o2) -> {
            try {
                return o1.getOrderNum().compareTo(o2.getOrderNum());
            } catch (Exception e) {
                return 0;
            }
        });
    }

    @ApiOperation("获取部门关联用户")
    @RequestMapping(value = "user", method = RequestMethod.GET)
    public TableResultResponse<User> getDepartUsers(
            @RequestParam(value = "departId",defaultValue = "") @ApiParam("部门id") String departId,
            @RequestParam(value = "userName",defaultValue = "") @ApiParam("用户名称") String userName) {
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

    /**
     * 查询执法分队
     * @return
     */
    @IgnoreClientToken
    @ApiOperation("查询执法分队")
    @RequestMapping(value="/enforcersGroup",method=RequestMethod.GET)
    public JSONArray getEnforcersGroup() {
        List<Depart> deptList =  this.baseBiz.getEnforcersGroup();
        if(deptList != null && !deptList.isEmpty()) {
            return JSONArray.parseArray(JSON.toJSONString(deptList));
        }
        return new JSONArray();
    }

     /** 通过部门id获取所在部门和子部门的集合
     *
     * @return
     */
    @IgnoreClientToken
    @ApiOperation("通过部门id获取所在部门和子部门的集合")
    @GetMapping("/trees")
    public ObjectRestResponse<List<DepartTree>> getDeptSonByDepartId(
            @RequestParam @ApiParam(value = "部门id") String departId,
            @RequestParam(defaultValue="") @ApiParam(value = "部门名称") String name) {

        ObjectRestResponse<List<DepartTree>> result = new ObjectRestResponse<>();
        if (StringUtils.isBlank(departId)) {
            result.setStatus(400);
            result.setMessage("非法参数！");
            return result;
        }
        result = this.baseBiz.getDeptSonByDepartId(departId, name);
        return result;
    }

//    @IgnoreClientToken
    @ApiOperation("获取部门关联用户,用于服务器调用")
    @RequestMapping(value = "/feign/user", method = RequestMethod.GET)
    public JSONArray getFeignDepartUsers(String departId, String userName) {
        TableResultResponse<User> departUsers = this.baseBiz.getDepartUsers(departId, userName);
        if(departUsers!=null) {
        	return JSONArray.parseArray(JSON.toJSONString(departUsers.getData().getRows()));
        }
        return new JSONArray();
    }
    
    @GetMapping("/list/ids")
    @ApiOperation("以部门ID集合批量查询部门信息")
    public JSONArray getDeptByIds(@RequestParam("deptIds") @ApiParam("以字符串形式表示 的部门ID集合，多个ID之间用逗号隔开")String deptIds) {
    	return this.baseBiz.getDeptByIds(deptIds);
    }

    @GetMapping("/menu/BydeptId")
    @ApiOperation("过部门ID查询与该部门里人员对应的权限")
    public List<JSONObject> getAuthoritiesByDept(@RequestParam(value="deptId")String deptId){
        return this.menuBiz.getAuthoritiesByDept(deptId);
    }

    @GetMapping("/list/all")
    @ApiOperation("查询所有部门,该方法可用于服务间调用")
    public List<JSONObject> listAll(){
        return this.baseBiz.listAll();
    }

    @GetMapping("/list/attr")
    @ApiOperation("按部门预留属性查询记录")
    public List<JSONObject> listByAttr(@RequestParam(value="attr") String attr,@RequestParam(value = "attrValues") String attrValues){
        return this.baseBiz.listByAttr(attr,attrValues);
    }

    @ApiOperation("获取部门树")
    @RequestMapping(value = "/i/tree", method = RequestMethod.GET)
    @IgnoreClientToken
    @IgnoreUserToken
    public List<DepartTree> getITree() {
        List<Depart> departs = this.baseBiz.getAll();
        List<DepartTree> trees = new ArrayList<>();
        departs.forEach(dictType -> {
            DepartTree departTree =
                new DepartTree(dictType.getId(), dictType.getParentId(), dictType.getName(), dictType.getCode());
            departTree.setOrderNum(dictType.getAttr2());
            trees.add(departTree);
        });
        return TreeUtil.bulid(trees, "-1", (Comparator<DepartTree>) (o1, o2) -> {
            try {
                return o1.getOrderNum().compareTo(o2.getOrderNum());
            } catch (Exception e) {
                return 0;
            }
        });
    }
}