package com.bjzhianjia.scp.cgp.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridMemberBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AreaGridMemberController 类描述.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月20日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@RestController
@RequestMapping("areaGridMember")
@CheckClientToken
@CheckUserToken
public class AreaGridMemberController extends BaseController<AreaGridMemberBiz, AreaGridMember, Integer> {

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("分页获取记录")
    public TableResultResponse<JSONObject> getList(@RequestParam(value = "memId", defaultValue = "") String memId,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {

        AreaGridMember areaGridMember = new AreaGridMember();
        areaGridMember.setGridMember(memId);

        return this.baseBiz.getList(areaGridMember, page, limit);
    }

    @RequestMapping(value = "/get/member/detail", method = RequestMethod.GET)
    @ApiOperation("查询单个对象详情")
    public ObjectRestResponse<JSONObject> getDetailOfAeraMem(
        @RequestParam(value = "memID", defaultValue = "") @ApiParam("待查询网格员ID") String memId) {
        return this.baseBiz.getDetailOfAeraMem(memId);
    }

    /**
     * 通过用户id查询所属网格信息
     * 
     * @param userId
     *            用户id
     * @return
     */
    @GetMapping("/userId/{userId}")
    @ApiOperation("通过用户id查询所属网格信息")
    public ObjectRestResponse<List<Map<String, Object>>> getGridByUserId(
        @PathVariable("userId") @ApiParam("待查询用户id") String userId) {

        ObjectRestResponse<List<Map<String, Object>>> result = new ObjectRestResponse<>();
        if (StringUtils.isBlank(userId)) {
            result.setMessage("非法参数");
            result.setStatus(400);
            return result;
        }
        List<Map<String, Object>> data = this.baseBiz.getGridByUserId(userId);
        result.setData(data);
        return result;
    }

    @GetMapping("/all/potition")
    @ApiOperation("网格员全部定位")
    public TableResultResponse<JSONObject> allPotition() {
        TableResultResponse<JSONObject> tableResult = this.baseBiz.allPosition();
        return tableResult;
    }

    @RequestMapping(value = "/list/excludeRole", method = RequestMethod.GET)
    @ApiOperation("分页获取记录")
    public TableResultResponse<JSONObject> getListExcludeRole(
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit,
        @RequestParam(value = "gridMemberName",defaultValue = "")String gridMemberName,
        @RequestParam(value = "gridRole", defaultValue = "")String gridRole) {

        AreaGridMember areaGridMember = new AreaGridMember();
        areaGridMember.setGridMember(gridMemberName);
        return this.baseBiz.getListExcludeRole(areaGridMember, page, limit,gridRole);
    }

    /**
     * 通过网格id、网格角色编码，获取成员列表
     *
     * @param gridId   所属网格id
     * @param gridRole 网格角色编码
     * @param page     页码
     * @param limit    页容量
     * @return
     */
    @GetMapping("/gridId/{gridId}/gridRole/{gridRole}")
    @ApiOperation("通过网格id、网格角色编码，获取成员列表")
    public TableResultResponse<JSONObject> getByGridIdAndGridRole(
            @PathVariable("gridId") @ApiParam("所属网格id") String gridId,
            @PathVariable("gridRole") @ApiParam("网格角色编码") String gridRole,
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "20") @ApiParam("页容量") Integer limit) {
        return this.baseBiz.getByGridIdAndGridRole(gridId, gridRole, page, limit);
    }

    @GetMapping("/assessment")
    public TableResultResponse<JSONObject> memAssessment(
        @RequestParam(value = "month", defaultValue = "") @ApiParam("月度,yyyy-MM") String month,
        @RequestParam(value = "gridId", required = false) @ApiParam("网格ID") Integer gridId,
        @RequestParam(value = "gridMember", defaultValue = "") @ApiParam("姓名") String gridMember,
        @RequestParam(value = "gridRole", defaultValue = "") @ApiParam("人员角色") String gridRole,
        @RequestParam(value = "page", defaultValue = "1") Integer page,
        @RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        /*
                "timecounts": "巡查时间",
                "finishedExecount": "办结数",
                "unfinishedExecount": "接收数",
                "pkcount": "上报数",
                "backcount": 退回数,
                "gridName": "(下蜀村)网格4,\n(下蜀村)网格5,\n(下蜀村)网格1,\n(下蜀村)网格2,\n(下蜀村)网格3,\n(蜀居)网格3",
                "gridMember": "f4769cabc5fa43edb37f4d69018a1048",
                "ccount": "巡查次数",
                "userName": "邓鹏宇"
         */
        return this.baseBiz.memAssessment(month, gridId, gridMember, gridRole, page, limit);
    }
}