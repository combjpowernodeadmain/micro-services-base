package com.bjzhianjia.scp.cgp.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.RightsIssuesBiz;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.cgp.service.RightsIssuesService;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("rightsIssues")
@CheckClientToken
@CheckUserToken
@Api(tags = "权利事项管理")
public class RightsIssuesController extends BaseController<RightsIssuesBiz, RightsIssues, Integer> {

    @Autowired
    private RightsIssuesBiz rightsIssuesBiz;

    @Autowired
    private RightsIssuesService rightsIssuesService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("终端查询分页列表")

    public TableResultResponse<RightsIssues> page(@RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @RequestParam(defaultValue = "") @ApiParam(name = "权利事项编号") String code,
        @RequestParam(defaultValue = "") @ApiParam(name = "业务条线") String bizType,
        @RequestParam(defaultValue = "") @ApiParam(name = "违法行为") String unlawfulAct,
        @RequestParam(defaultValue = "") @ApiParam(name = "是否可用") String isEnable,
        @RequestParam(required = false) @ApiParam(name = "事件类别") Integer eventType) {

        RightsIssues rightsIssues = new RightsIssues();
        rightsIssues.setCode(code);
        rightsIssues.setBizType(bizType);
        rightsIssues.setUnlawfulAct(unlawfulAct);
        rightsIssues.setIsEnable(isEnable);
        rightsIssues.setType(eventType);
        return rightsIssuesService.getList(page, limit, rightsIssues);

    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<RightsIssues> get(@PathVariable @ApiParam(name = "待查询对象ID") Integer id) {
        /*
         * 返回内容需要进行业务条线数据聚和，聚和内容包括：业务条线ID+业务条线名称
         */
        ObjectRestResponse<RightsIssues> entityObjectRestResponse = new ObjectRestResponse<>();

        RightsIssues rightsIssues = rightsIssuesService.getByID(id);
        // Object o = rightsIssuesBiz.selectById(id);
        // RightsIssues rightsIssues = (RightsIssues)o;

        entityObjectRestResponse.data(rightsIssues);

        return entityObjectRestResponse;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("新增单个对象")
    public ObjectRestResponse<RightsIssues> add(
        @RequestBody @Validated @ApiParam(name = "待添加对象实例") RightsIssues rightsIssues, BindingResult bindingResult) {
        ObjectRestResponse<RightsIssues> restResult = new ObjectRestResponse<>();
        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<Void> result = rightsIssuesService.createRightsIssues(rightsIssues);

        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }
        return restResult.data(rightsIssues);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ResponseBody
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<RightsIssues> update(
        @RequestBody @Validated @ApiParam(name = "待更新对象实例") RightsIssues rightsIssues, BindingResult bindingResult) {

        ObjectRestResponse<RightsIssues> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<Void> result = rightsIssuesService.updateRightsIssues(rightsIssues);

        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }
        return restResult.data(rightsIssues);
    }

    @RequestMapping(value = "/remove/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    @ApiOperation("批量移除对象")
    public ObjectRestResponse<RightsIssues> remove(
        @PathVariable @ApiParam(name = "待删除对象ID集合，多个ID用“，”隔开") Integer[] ids) {
        ObjectRestResponse<RightsIssues> result = new ObjectRestResponse<>();
        if (ids == null || ids.length == 0) {
            result.setStatus(400);
            ;
            result.setMessage("请选择要删除的项");
            return result;
        }
        rightsIssuesBiz.deleteByIds(ids);
        return result;
    }
}
