package com.bjzhianjia.scp.cgp.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.security.auth.client.config.FeignApplyConfiguration;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 管理服务
 * @author zzh
 *
 */
@FeignClient(value = "scp-admin",configuration = FeignApplyConfiguration.class)
public interface AdminFeign {

	/**
	 * 按多个ID查询部门<br/>
	 * 
	 * @author 尚
	 * @param id id1,id2,id3
	 * @return ${id}:${部门对象JSON字符串}
	 */
	@RequestMapping(value = "/depart/getByPK/{id}",method = RequestMethod.GET)
    public Map<String,String> getDepart(@PathVariable(value="id") String id);
	
	/**
	 * 根据ID组合获取用户集合，查询 的集合中包含已删除的人员
	 * @author 尚
	 * @param id id集合，多个Id用‘,’隔开，如1,2,3,4
	 * @return ${userId}:${记录对象的JSON字符串}
	 */
	@RequestMapping(value = "/user/getByPK/{id}",method = RequestMethod.GET)
	public Map<String,String> getUser(@PathVariable(value="id") String id);
	
	@RequestMapping(value = "/depart/getLayerByPK/{id}",method = RequestMethod.GET)
    public Map<String,String> getLayerDepart(@PathVariable(value="id") String id);
	
	@RequestMapping(value="/depart/getDepartByParent/{parentId}",method = RequestMethod.GET)
	public JSONArray getDepartByParent(@PathVariable(value="parentId")String parentId);
	
	@RequestMapping(value = "/depart/id/{parentId}", method = RequestMethod.GET)
	public JSONObject getByDeptId(@PathVariable(value="parentId") String parentId);
	
	/**
     * 
     * @param userId 用户Id集合，多个id间用逗号隔开
     * @return
     */
	@RequestMapping(value="/user/get/user/detail")
	public JSONArray getUserDetail(@RequestParam("userId") String userId);
	
	/**
	 * 查询执法分队
	 * @return
	 *     所有的执法分队
	 */
    @RequestMapping(value="/depart/enforcersGroup",method=RequestMethod.GET)
    public JSONArray getEnforcersGroup();
    
    /**
     * 获取部门关联用户
     * @param departId
     * @param userName
     * @return
     */
    @RequestMapping(value="/depart/feign/user",method=RequestMethod.GET)
    public JSONArray getFeignDepartUsers(@RequestParam("departId")String departId, @RequestParam("userName")String userName);
    
    @GetMapping("/depart/list/ids")
    @ApiOperation("以部门ID集合批量查询部门信息")
    public JSONArray getDeptByIds(@RequestParam("deptIds") @ApiParam("以字符串形式表示 的部门ID集合，多个ID之间用逗号隔开")String deptIds);

	@GetMapping("/depart/menu/BydeptId")
	@ApiOperation("过部门ID查询与该部门里人员对应的权限")
	List<JSONObject> getAuthoritiesByDept(@RequestParam(value = "deptId") String deptId);

	/**
	 * 通过用户id获取岗位列表
	 * @param userId 用户id
	 * @return
	 */
	@GetMapping("/position/userId/{userId}")
	@ApiOperation("通过用户id获取岗位列表")
	ObjectRestResponse<List<Map<String,Object>>> getPositionByUserId(@PathVariable("userId") String userId);

	@GetMapping("/user/list/groupIds")
	@ApiOperation("按组获取用户列表")
	List<JSONObject> selectLeaderOrMemberByGroup(@RequestParam("groupIds") String groupIds);

    /**
     * 按部门ID集合查询用户信息<br/>
     * 目前该接口只返回用户ID与用户名(帐号)，如有需要，可在admin服务sql里添加字段
     * @param deptIds
     * @return
     */
	@GetMapping("/user/list/byDeptIds")
	@ApiOperation("按部门ID集合查询用户")
	public List<JSONObject> getUsersByDeptIds(@RequestParam("deptIds") String deptIds);

	/**
	 * 获取用户详情，包括部门及岗位
	 * @param userIds 用户Ids
	 * @return
	 */
	@RequestMapping(value="/external/user/info",method=RequestMethod.GET)
	JSONArray getInfoByUserIds(@RequestParam("userIds") String userIds);

	/**
	 * 批量获取人员信息
	 * @param userIds 用户Ids
	 * @return
	 */
	@RequestMapping(value = "/external/userIds", method = RequestMethod.GET)
	Map<String, String> getUsersByUserIds(@RequestParam("userIds") String userIds);

	/**
	 * 通过部门ids获取部门信息
	 *
	 * @param deptIds 部门ids
	 * @return
	 */
	@RequestMapping(value = "/external/deptIds", method = RequestMethod.GET)
	Map<String, String> getDepartByDeptIds(@RequestParam("deptIds") String deptIds);

	@GetMapping("/depart/list/all")
	@ApiOperation("查询所有部门")
	List<JSONObject> deptListAll();

	@GetMapping("/depart/list/attr")
	@ApiOperation("按部门预留属性查询记录")
	List<JSONObject> listByAttr(@RequestParam(value="attr") String attr,@RequestParam(value = "attrValues") String attrValues);

	/**
	 * 按组CODE获取用户信息，多个CODE之间用逗号隔开
	 * @param groupCode
	 * @return
	 */
	@ApiOperation("按组CODE集合获取用户")
	@RequestMapping(value = "/user/list/groupCode", method = RequestMethod.GET)
	List<JSONObject> getLeaderOrMemberByGroupCode(@RequestParam("groupCode") String groupCode);
}
