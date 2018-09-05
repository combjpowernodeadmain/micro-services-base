package com.bjzhianjia.scp.cgp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.CLESuperviseRecordBiz;
import com.bjzhianjia.scp.cgp.entity.CLESuperviseRecord;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * CLESuperviseRecordController 案件督办前台控制器.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年9月5日          chenshuai      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author chenshuai
 *
 */
@RestController
@RequestMapping("cleSuperviseRecord")
@CheckClientToken
@CheckUserToken
@Api(tags = "案件督办管理")
public class CLESuperviseRecordController extends BaseController<CLESuperviseRecordBiz, CLESuperviseRecord, Integer> {

    @Autowired
    private CLESuperviseRecordBiz cleSuperviseRecordBiz;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation("添加督办")
    public ObjectRestResponse<CLESuperviseRecord> add(
        @RequestBody @Validated @ApiParam(value = "待添加督办实例") CLESuperviseRecord cleSuperviseRecord,
        BindingResult bindingResult) {

        ObjectRestResponse<CLESuperviseRecord> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }
        BeanUtil.beanAttributeValueTrim(cleSuperviseRecord);

        try {
            cleSuperviseRecordBiz.createSuperviseRecord(cleSuperviseRecord);
        } catch (Exception e) {
            restResult.setStatus(400);
            restResult.setMessage("内部异常！");
            return restResult;
        }
        restResult.setData(cleSuperviseRecord);

        return restResult;
    }

    /**
     * 通过立案单id，翻页查询
     * 
     * @param page
     *            页码
     * @param limit
     *            页容量
     * @param caseInfoId
     *            案件id
     */
    @RequestMapping(value = "/list/{id}", method = RequestMethod.GET)
    @ApiOperation("翻页查询")
    public TableResultResponse<CLESuperviseRecord> page(
        @RequestParam(defaultValue = "10") @ApiParam(value = "页容量") Integer limit,
        @RequestParam(defaultValue = "1") @ApiParam(value = "当前页") Integer page,
        @PathVariable(value = "id") @ApiParam(value = "案件id") String caseInfoId) {
        return cleSuperviseRecordBiz.getList(page, limit, caseInfoId);
    }
}