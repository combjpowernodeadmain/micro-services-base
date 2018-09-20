package com.bjzhianjia.scp.cgp.rest;

import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

import com.bjzhianjia.scp.cgp.biz.IconBiz;
import com.bjzhianjia.scp.cgp.entity.Icon;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

/**
 * IconController 图标管理控制器.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月19日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@RestController
@RequestMapping("icon")
@CheckClientToken
@CheckUserToken
@Api(tags = "图标管理")
public class IconController extends BaseController<IconBiz, Icon, Integer> {

    @Autowired
    private IconBiz iconBiz;

    /**
     * 通过图标分组查询
     * 
     * @param groupCode
     *            图标分组（数据字典code）
     * @return
     */
    @GetMapping(value = "/groupCode/{groupCode}")
    public ObjectRestResponse<List<Map<String, Object>>> getIconByGroupCode(
        @PathVariable("groupCode") @ApiParam("图标分组（数据字典code）") String groupCode) {
        ObjectRestResponse<List<Map<String, Object>>> result = new ObjectRestResponse<>();
        if (StringUtils.isBlank(groupCode)) {
            result.setMessage("非法参数!");
            result.setStatus(400);
            return result;
        }
        List<Map<String, Object>> resultData = iconBiz.getIconByGroupCode(groupCode);
        result.setData(resultData);
        return result;
    }
}