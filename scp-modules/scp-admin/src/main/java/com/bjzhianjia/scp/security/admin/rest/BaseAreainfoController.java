package com.bjzhianjia.scp.security.admin.rest;

import com.bjzhianjia.scp.security.admin.biz.BaseAreainfoBiz;
import com.bjzhianjia.scp.security.admin.entity.BaseAreainfo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @param level 区域等级
     * @param id    区域id
     * @return
     */
    @GetMapping("/son/{id}/{level}")
    public ObjectRestResponse<List<BaseAreainfo>> get(@PathVariable("id") @ApiParam(value = "区域id") Integer id,
                                                      @PathVariable("level") @ApiParam(value = "区域等级") Integer level) {
        ObjectRestResponse<List<BaseAreainfo>> result = new ObjectRestResponse<>();
        result.setData(baseAreainfoBiz.getByParentIdAndLevel(id, level));
        return result;
    }
}