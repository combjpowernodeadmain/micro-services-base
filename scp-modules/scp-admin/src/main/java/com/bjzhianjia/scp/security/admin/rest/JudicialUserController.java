package com.bjzhianjia.scp.security.admin.rest;

import com.bjzhianjia.scp.security.admin.biz.JudicialUserBiz;
import com.bjzhianjia.scp.security.admin.constant.RoleConstant;
import com.bjzhianjia.scp.security.admin.entity.User;
import com.bjzhianjia.scp.security.admin.service.JudicialUserService;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * JudicialUserController 司法用户.
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018/9/29          chenshuai      1.0            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
@RestController
@RequestMapping("judicialUser")
@CheckUserToken
@CheckClientToken
@Api(tags = "司法用户模块")
public class JudicialUserController {

    @Autowired
    private JudicialUserService judicialUserService;

    @Autowired
    private JudicialUserBiz judicialUserBiz;

    @Autowired
    private Environment environment;


    /**
     * 获取案卷登记获取处理部门id
     *
     * @param departId 当前所属部门id
     * @param isOwn    是否包含当前部门。（1 包含|0不包含）
     * @return -1 没有可以处理的部门
     */
    @ApiOperation("获取案卷登记获取处理部门id")
    @GetMapping("/record/caseDeal")
    public String getCaseDeal(@RequestParam(value = "departId", defaultValue = "") @ApiParam("当前所属部门id") String departId,
                              @RequestParam(value = "isOwn", defaultValue = "") @ApiParam("是否包含当前部门") Integer isOwn) {

        return judicialUserService.getCaseDeal(departId, environment.getProperty(RoleConstant.ROLE_DISTRIBUTE), isOwn);
    }

    /**
     * 获取技术人员列表
     *
     * @param userName  用户名称
     * @param departIds 用户部门ids
     * @param major     用户专业
     * @param page      页码
     * @param limit     页容量
     * @return
     */
    @ApiOperation("获取技术人员列表")
    @GetMapping("/major/list")
    public TableResultResponse<Map<String, Object>> getMajorUsers(
            @RequestParam(value = "userName", defaultValue = "") @ApiParam("用户名称") String userName,
            @RequestParam(value = "departIds", defaultValue = "") @ApiParam("用户部门ids") String departIds,
            @RequestParam(value = "major", defaultValue = "") @ApiParam("用户专业") String major,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit) {

        User user = new User();
        user.setName(userName);
        user.setAttr2(major);
        return judicialUserBiz.getMajorUsers(user, environment.getProperty(RoleConstant.ROLE_TECHNICIST), departIds,
                page, limit);
    }

    /**
     * 获取技术人员列表
     *
     * @param userName     用户名称
     * @param departId     用户部门id
     * @param major        用户专业
     * @param areaProvince 省级编码
     * @param areaCity     城市编码
     * @param page         页码
     * @param limit        页容量
     * @return
     */
    @ApiOperation("获取技术人员列表")
    @GetMapping("/technicist/list")
    public TableResultResponse<Map<String, Object>> getTechnicist(
            @RequestParam(value = "userName", defaultValue = "") @ApiParam("用户名称") String userName,
            @RequestParam(value = "departIds", defaultValue = "") @ApiParam("用户部门id") String departId,
            @RequestParam(value = "major", required = false) @ApiParam("用户专业") String major,
            @RequestParam(value = "province", defaultValue = "") @ApiParam("省级编码") String areaProvince,
            @RequestParam(value = "city", defaultValue = "") @ApiParam("城市编码") String areaCity,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit) {

        return judicialUserBiz.getTechnicist(major, userName, departId, areaProvince, areaCity,page, limit);
    }

    /**
     * 获取分案人员列表
     *
     * @param userName  用户名称
     * @param departIds 用户部门ids
     * @param major     用户专业
     * @param page      页码
     * @param limit     页容量
     * @return
     */
    @ApiOperation("获取分案人员列表")
    @GetMapping("/distribute/list")
    public TableResultResponse<Map<String, Object>> getDistributeUsers(
            @RequestParam(value = "userName", defaultValue = "") @ApiParam("用户名称") String userName,
            @RequestParam(value = "departIds", defaultValue = "") @ApiParam("用户部门ids") String departIds,
            @RequestParam(value = "major", defaultValue = "") @ApiParam("用户专业") String major,
            @RequestParam(value = "province", defaultValue = "") @ApiParam("省级编码") String province,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页容量") Integer limit) {

        User user = new User();
        user.setName(userName);
        user.setAttr2(major);
        user.setAttr3(province);
        return judicialUserBiz.getMajorUsers(user, environment.getProperty(RoleConstant.ROLE_DISTRIBUTE), departIds,
                page, limit);
    }

    /**
     * 分配技术人员
     * 添加用户和角色的关系
     *
     * @param user 用户信息
     * @return
     */
    @ApiOperation(value = "添加（分配）技术人员")
    @PostMapping("/role/technologist")
    public ObjectRestResponse<User> technologist(@RequestBody @ApiParam(value = "用户信息") User user) {
        ObjectRestResponse<User> result = new ObjectRestResponse<>();
        //attr2 专业 attr3 省份 departId所属检察院
        if (StringUtils.isBlank(user.getUsername())) {
            result.setMessage("用户账号不能为空！");
            result.setStatus(400);
            return result;
        }
        if (StringUtils.isBlank(user.getPassword())) {
            result.setMessage("用户密码不能为空！");
            result.setStatus(400);
            return result;
        }
        if (StringUtils.isBlank(user.getAttr2())) {
            result.setMessage("用户专业不能为空！");
            result.setStatus(400);
            return result;
        }
        if (StringUtils.isBlank(user.getDepartId())) {
            result.setMessage("用户所属检察院不能为空！");
            result.setStatus(400);
            return result;
        }
        try {
            judicialUserBiz.distributionRole(user, environment.getProperty(RoleConstant.ROLE_TECHNICIST));
        } catch (RuntimeException re) {
            result.setMessage(re.getMessage());
            result.setStatus(400);
        } catch (Exception e) {
            result.setMessage("系统内部异常！");
            result.setStatus(400);
            e.printStackTrace();
        }
        result.setData(user);
        return result;
    }

    /**
     * 直接分配技术人员
     * 添加用户和角色的关系
     *
     * @param userId 用户id
     * @return
     */
    @ApiOperation(value = "直接分配技术人员")
    @PostMapping("/role/technologist/userId/{userId}")
    public ObjectRestResponse<Void> division(@PathVariable("userId") @ApiParam(value = "用户id") String userId) {
        ObjectRestResponse<Void> result = new ObjectRestResponse<>();
        if (StringUtils.isBlank(userId)) {
            result.setMessage("请选择分配的用户！");
            result.setStatus(400);
            return result;
        }
        User user = new User();
        user.setId(userId);
        try {
            judicialUserBiz.distributionRole(user, environment.getProperty(RoleConstant.ROLE_TECHNICIST));
        } catch (RuntimeException re) {
            result.setMessage(re.getMessage());
            result.setStatus(400);
        }
        return result;
    }

    /**
     * 分配分案人员
     * 添加用户和角色的关系
     *
     * @param userId 用户id
     * @return
     */
    @ApiOperation(value = "分配分案人员")
    @PostMapping("/role/division/{userId}/departId/{departId}")
    public ObjectRestResponse<User> division(@PathVariable("userId") @ApiParam(value = "用户id") String userId,
                                             @PathVariable("departId") @ApiParam(value = "所属检察院id") String departId) {
        ObjectRestResponse<User> result = new ObjectRestResponse<>();
        if (StringUtils.isBlank(userId)) {
            result.setMessage("请选择分配的用户！");
            result.setStatus(400);
            return result;
        }
        if (StringUtils.isBlank(departId)) {
            result.setMessage("请选择分配的部门！");
            result.setStatus(400);
            return result;
        }

        User user = new User();
        user.setId(userId);
        user.setDepartId(departId);
        try {
            judicialUserBiz.distributionRole(user, environment.getProperty(RoleConstant.ROLE_DISTRIBUTE));
        } catch (RuntimeException re) {
            result.setMessage(re.getMessage());
            result.setStatus(400);
        } catch (Exception e) {
            result.setMessage("系统异常！");
            result.setStatus(400);
            e.printStackTrace();
        }
        result.setData(user);
        return result;
    }

    /**
     * 删除分案人员
     * 删除用户和角色的关系
     *
     * @param userId 用户id
     * @return
     */
    @ApiOperation(value = "删除分案人员")
    @DeleteMapping("/role/division/{userId}")
    public ObjectRestResponse<Void> delRoleDivision(@PathVariable("userId") @ApiParam(value = "用户id") String userId) {

        ObjectRestResponse<Void> result = new ObjectRestResponse<>();
        if (StringUtils.isBlank(userId)) {
            result.setMessage("请选择删除的用户！");
            result.setStatus(400);
            return result;
        }
        try {
            judicialUserBiz.deleteUserRole(userId, environment.getProperty(RoleConstant.ROLE_DISTRIBUTE));
        } catch (RuntimeException re) {
            result.setMessage(re.getMessage());
            result.setStatus(400);
        }
        return result;
    }

    /**
     * 删除技术人员
     * 删除用户和角色的关系
     *
     * @param userId 用户id
     * @return
     */
    @ApiOperation(value = "删除技术人员")
    @DeleteMapping("/role/technologist/{userId}")
    public ObjectRestResponse<Void> delRoleTechnologist(@PathVariable("userId") @ApiParam(value = "用户id") String userId) {

        ObjectRestResponse<Void> result = new ObjectRestResponse<>();
        if (StringUtils.isBlank(userId)) {
            result.setMessage("请选择删除的用户！");
            result.setStatus(400);
            return result;
        }
        try {
            judicialUserBiz.deleteUserRole(userId, environment.getProperty(RoleConstant.ROLE_TECHNICIST));
        } catch (RuntimeException re) {
            result.setMessage(re.getMessage());
            result.setStatus(400);
        }
        return result;
    }

    /**
     * 技术人员选择列表
     *
     * @param userName
     * @param major
     * @param province
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "技术人员选择列表")
    @GetMapping("/debar/technologist")
    public TableResultResponse<Map<String, Object>> getUserDebarTechnologist(
            @RequestParam(value = "userName", defaultValue = "") @ApiParam("用户名称") String userName,
            @RequestParam(value = "departId", defaultValue = "") @ApiParam("部门id") String departId,
            @RequestParam(value = "major", defaultValue = "") @ApiParam("用户专业") String major,
            @RequestParam(value = "province", defaultValue = "") @ApiParam("省级编码") String province,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "100") @ApiParam("页容量") Integer limit) {

        User user = new User();
        user.setName(userName);
        //专业
        user.setAttr2(major);
        //省份
        user.setAttr3(province);
        user.setDepartId(departId);
        return judicialUserBiz.getUserByDebarRole(user, environment.getProperty(RoleConstant.ROLE_TECHNICIST), page,
                limit);
    }

    /**
     * 分案人员选择列表
     *
     * @param userName
     * @param major
     * @param province
     * @param page
     * @param limit
     * @return
     */
    @ApiOperation(value = "分案人员选择列表")
    @GetMapping("/debar/division")
    public TableResultResponse<Map<String, Object>> getUserDebarDivision(
            @RequestParam(value = "userName", defaultValue = "") @ApiParam("用户名称") String userName,
            @RequestParam(value = "major", defaultValue = "") @ApiParam("用户专业") String major,
            @RequestParam(value = "province", defaultValue = "") @ApiParam("省级编码") String province,
            @RequestParam(value = "departId", defaultValue = "") @ApiParam(value = "所属检察院id") String departId,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "100") @ApiParam("页容量") Integer limit) {

        User user = new User();
        user.setName(userName);
        user.setAttr2(major);
        user.setAttr3(province);
        user.setDepartId(departId);
        return judicialUserBiz.getUserByDebarRole(user, environment.getProperty(RoleConstant.ROLE_DISTRIBUTE), page,
                limit);
    }

    /**
     * 编辑技术人员信息
     * 专业和手机号
     *
     * @param userId 用户id
     * @return
     */
    @ApiOperation(value = "编辑技术人员信息")
    @PutMapping("/role/technologist/{userId}")
    public ObjectRestResponse<Void> upTechnologist(
            @PathVariable("userId") @ApiParam(value = "用户id") String userId,
            @RequestParam(value = "mobilePhone") @ApiParam("手机号") String mobilePhone,
            @RequestParam(value = "major") @ApiParam("用户专业") String major) {

        ObjectRestResponse<Void> result = new ObjectRestResponse<>();
        if (StringUtils.isBlank(userId)) {
            result.setMessage("请选择编辑的用户！");
            result.setStatus(400);
            return result;
        }
        try {
            User user = new User();
            user.setId(userId);
            user.setAttr2(major);
            user.setMobilePhone(mobilePhone);
            judicialUserBiz.upTechnologist(user);
        } catch (RuntimeException re) {
            result.setMessage(re.getMessage());
            result.setStatus(400);
        }
        return result;
    }
}
