package com.bjzhianjia.scp.security.admin.rest;

import com.bjzhianjia.scp.security.admin.biz.BaseAreainfoBiz;
import com.bjzhianjia.scp.security.admin.entity.BaseAreainfo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * BaseAreainfoController 区县行政编码字典表.
 *
 *
 * <pre>
 * Modification History:
 * Date             Author      Version         Description
 * ------------------------------------------------------------------
 * 2018年10月12日          bo      chenshuai            ADD
 * </pre>
 *
 * @author chenshuai
 * @version 1.0
 */
@Api(tags = "区县行政编码字典")
@RestController
@RequestMapping("baseAreainfo")
@CheckClientToken
@CheckUserToken
public class BaseAreainfoController {

    @Autowired
    private BaseAreainfoBiz baseAreainfoBiz;

    /**
     * 通过区域id和等级获取子区域列表
     *
     * @param level 区域等级（1省级|2市|3区/县）
     * @param id    区域id
     * @return
     */
    @ApiOperation(value = "通过区域id和等级获取子区域列表")
    @GetMapping("/son/{id}/{level}")
    public ObjectRestResponse<List<BaseAreainfo>> get(@PathVariable("id") @ApiParam(value = "区域id") Integer id,
                                                      @PathVariable("level") @ApiParam(value = "区域等级") Integer level) {
        ObjectRestResponse<List<BaseAreainfo>> result = new ObjectRestResponse<>();
        result.setData(baseAreainfoBiz.getByParentIdAndLevel(id, level));
        return result;
    }

    /**
     * 通过ids获取区域名称
     *
     * @param ids 区域等级
     * @return 格式：  id : name
     */
    @ApiOperation(value = "通过ids获取区域名称")
    @GetMapping("/ids/{ids}")
    public ObjectRestResponse<Map<String, String>> getNameByIds(@PathVariable("ids") @ApiParam(value = "区域id集") String ids) {
        ObjectRestResponse<Map<String, String>> result = new ObjectRestResponse<>();
        if (StringUtils.isBlank(ids)) {
            result.setStatus(400);
            result.setData(new HashMap<String, String>());
            return result;
        }
        List idList = Arrays.asList(ids.split(","));
        Map<String, String> resultData = baseAreainfoBiz.getNameByIds(idList);
        result.setData(resultData);
        return result;
    }

    /**
     * 通过区域id，获取区域子集
     *
     * @param parenId 区域等级
     * @return 格式：  id : name
     */
    @ApiOperation(value = "通过ids获取区域名称")
    @GetMapping("/son/{parenId}")
    public ObjectRestResponse<Map<String, String>> getSonNameById(
            @PathVariable("parenId") @ApiParam(value = "区域id集") Integer parenId) {
        ObjectRestResponse<Map<String, String>> result = new ObjectRestResponse<>();
        Map<String, String> resultData = baseAreainfoBiz.getSonNameById(parenId);
        result.setData(resultData);
        return result;
    }


}