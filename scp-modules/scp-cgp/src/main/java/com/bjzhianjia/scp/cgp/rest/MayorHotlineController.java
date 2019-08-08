package com.bjzhianjia.scp.cgp.rest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bjzhianjia.scp.cgp.biz.MayorHotlineBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.MayorHotlineService;
import com.bjzhianjia.scp.cgp.vo.MayorHotlineVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("mayorHotline")
@CheckClientToken
@CheckUserToken
@Api(tags = "市长热线12345")
public class MayorHotlineController extends BaseController<MayorHotlineBiz, MayorHotline, Integer> {

    @Autowired
    private MayorHotlineService mayorHotlineService;

    @Autowired
    private MayorHotlineBiz mayorHotLineBiz;

    @RequestMapping(value = "/add/cache", method = RequestMethod.POST)
    @ApiOperation("添加暂存")
    public ObjectRestResponse<MayorHotline> addMayorHotlineCache(
        @RequestBody @Validated @ApiParam(name = "待添加对象实例") MayorHotlineVo mayorHotline, BindingResult bindingResult) {
        ObjectRestResponse<MayorHotline> restResult = new ObjectRestResponse<>();
        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<MayorHotline> result =
            mayorHotlineService.createdMayorHotlineCache(mayorHotline,
                Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_TODO);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        restResult.setStatus(200);
        restResult.setMessage("成功");
        restResult.setData(result.getData());
        return restResult;
    }

    @RequestMapping(value = "/add/case", method = RequestMethod.POST)
    @ApiOperation("添加预立案对象")
    public ObjectRestResponse<MayorHotlineVo> add(@RequestBody @Validated @ApiParam(name = "待添加对象实例") MayorHotlineVo vo,
        BindingResult bindingResult) {
        ObjectRestResponse<MayorHotlineVo> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<Void> result = new Result<>();
        try {
            result = mayorHotlineService.createdMayorHotline(vo);
        } catch (Exception e) {
            e.printStackTrace();
            restResult.setStatus(400);
            restResult.setMessage(StringUtils.isBlank(result.getMessage()) ? e.getMessage() : result.getMessage());
            return restResult;
        }

        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult.data(vo);
    }

    @RequestMapping(value = "/update/cache/{id}", method = RequestMethod.PUT)
    @ApiOperation("更新缓存对象")
    public ObjectRestResponse<MayorHotline> updateCache(
        @RequestBody @Validated @ApiParam(name = "待更新对象实例") MayorHotline mayorHotline, BindingResult bindingResult) {
        ObjectRestResponse<MayorHotline> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<MayorHotline> result = mayorHotlineService.updateCache(mayorHotline);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }

        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
    @ApiOperation("更新预立案单")
    public ObjectRestResponse<MayorHotline> update(
        @RequestBody @Validated @ApiParam(name = "待更新对象实例") MayorHotlineVo vo, BindingResult bindingResult) {
        ObjectRestResponse<MayorHotline> restResult = new ObjectRestResponse<>();

        Result<MayorHotline> result = new Result<>();
        try {
            result = mayorHotlineService.update(vo);
            if (!result.getIsSuccess()) {
                restResult.setStatus(400);
                restResult.setMessage(result.getMessage());
                return restResult;
            }
        } catch (Exception e) {
            e.printStackTrace();
            restResult.setStatus(400);
            restResult.setMessage(StringUtils.isBlank(result.getMessage()) ? e.getMessage() : result.getMessage());
            return restResult;
        }

        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ApiOperation("分页获取对象集合")
    public TableResultResponse<MayorHotlineVo> list(@RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "") @ApiParam(name = "热线标题") String hotlnTitle,
        @RequestParam(defaultValue = "") @ApiParam(name = "热线事项编号") String hotlnCode,
        @RequestParam(defaultValue = "") @ApiParam(name = "诉求人") String appealPerson,
        @RequestParam(defaultValue = "") @ApiParam(name = "热线事件分类") String hotlnType,
        @RequestParam(defaultValue = "") @ApiParam(name = "处理状态") String exeStatus,
        @RequestParam(defaultValue = "") @ApiParam(name = "热线来源") String hotlnSource,
        @RequestParam(defaultValue = "") @ApiParam(name = "排序方式") String sortColumn,
        @RequestParam(defaultValue = "") @ApiParam(name = "按时间查询(起始时间)") String startTime,
        @RequestParam(defaultValue = "") @ApiParam(name = "按时间查询 (终止时间)") String endTime) {
        MayorHotline mayorHotline = new MayorHotline();
        mayorHotline.setHotlnTitle(hotlnTitle.trim());
        mayorHotline.setAppealPerson(appealPerson);
        mayorHotline.setHotlnType(hotlnType);
        mayorHotline.setExeStatus(exeStatus);
        mayorHotline.setHotlnSource(hotlnSource);
        mayorHotline.setHotlnCode(hotlnCode.trim());
        TableResultResponse<MayorHotlineVo> list = mayorHotlineService.getList(mayorHotline, page, limit, startTime, endTime,sortColumn);
        return list;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<MayorHotlineVo> getOne(
        @PathVariable(value = "id") @ApiParam(name = "待查询对象ID") Integer id) {
        ObjectRestResponse<MayorHotlineVo> one = mayorHotlineService.getOne(id);
        return one;
    }

    @RequestMapping(value = "/get/toDo/{id}", method = RequestMethod.GET)
    @ApiOperation("查询未启动对象")
    public ObjectRestResponse<MayorHotlineVo> getToDo(
        @PathVariable(value = "id") @ApiParam(name = "待查询对象ID") Integer id) {
        ObjectRestResponse<MayorHotlineVo> result = mayorHotlineService.getOne(id);

        MayorHotlineVo data = result.getData();
        if (!Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_TODO.equals(data.getExeStatus())) {
            result.setStatus(400);
            result.setMessage("当前记录不能修改，只有未发起的热线记录可修改！");
            result.setData(null);
            return result;
        }

        result.setStatus(200);
        return result;
    }

    @RequestMapping(value = "/remove/{ids}", method = RequestMethod.DELETE)
    @ApiOperation("批量删除对象")
    public ObjectRestResponse<MayorHotline> remove(
        @PathVariable("ids") @ApiParam(name = "待删除对象ID集合，多个ID用“，”隔开") Integer[] ids) {
        ObjectRestResponse<MayorHotline> restResult = new ObjectRestResponse<>();
        if (ids == null || ids.length == 0) {
            restResult.setStatus(400);
            restResult.setMessage("请选择要删除的项");
            return restResult;
        }

        Result<Void> result = mayorHotLineBiz.remove(ids);
        if (!result.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(result.getMessage());
            return restResult;
        }
        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }

    @RequestMapping(value = "/reply/{id}", method = RequestMethod.PUT)
    @ApiOperation("12345反馈")
    public ObjectRestResponse<MayorHotline> checkResponse(
            @PathVariable(value = "id") @ApiParam(name = "待反馈对象ID") Integer id,
            @RequestBody @ApiParam(value = "反馈意见说明") MayorHotline mayorHotline) {
        ObjectRestResponse<MayorHotline> restResult = new ObjectRestResponse<>();
        mayorHotline.setId(id);
        Result<Void> reply = mayorHotlineService.reply(mayorHotline);
        if (!reply.getIsSuccess()) {
            restResult.setStatus(400);
            restResult.setMessage(reply.getMessage());
            return restResult;
        }
        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }
}