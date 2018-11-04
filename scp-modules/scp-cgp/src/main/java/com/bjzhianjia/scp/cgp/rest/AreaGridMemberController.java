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
}