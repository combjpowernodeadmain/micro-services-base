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

import com.bjzhianjia.scp.cgp.biz.LeadershipAssignBiz;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.LeadershipAssign;
import com.bjzhianjia.scp.cgp.entity.PublicOpinion;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.service.LeadershipAssignService;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.vo.LeadershipAssignVo;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("leadershipAssign")
@CheckClientToken
@CheckUserToken
@Api(tags = "领导交办")
public class LeadershipAssignController extends BaseController<LeadershipAssignBiz, LeadershipAssign, Integer> {

    @Autowired
    private LeadershipAssignService leadershipAssignService;

    @Autowired
    private LeadershipAssignBiz leadershipAssignBiz;

    @Autowired
    private DictFeign dictFeign;

    @RequestMapping(value = "/add/cache", method = RequestMethod.POST)
    @ApiOperation("添加暂存")
    public ObjectRestResponse<LeadershipAssign> addLeaderAssignCache(
        @RequestBody @Validated @ApiParam(name = "待添加对象实例") LeadershipAssignVo vo, BindingResult bindingResult) {
        ObjectRestResponse<LeadershipAssign> restResult = new ObjectRestResponse<>();
        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        String toDoExeStatus =
            CommonUtil.exeStatusUtil(dictFeign, Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_TODO);

        vo.setExeStatus(toDoExeStatus);// 创建时处理状态为【未发起】
        Result<LeadershipAssign> result = new Result<>();
        try {
            result = leadershipAssignService.createdLeaderAssignCache(vo);
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
        restResult.setData(result.getData());
        return restResult;
    }

    @RequestMapping(value = "/add/case", method = RequestMethod.POST)
    @ApiOperation("添加单个对象")
    public ObjectRestResponse<LeadershipAssignVo> add(
        @RequestBody @Validated @ApiParam(name = "待添加对象实例") LeadershipAssignVo vo, BindingResult bindingResult) {
        ObjectRestResponse<LeadershipAssignVo> restResult = new ObjectRestResponse<>();

        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<Void> result = new Result<>();
        try {
            result = leadershipAssignService.createdLeadershipAssign(vo);
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

    @RequestMapping(value = "/update/cache/{id}", method = RequestMethod.PUT)
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<LeadershipAssign> updateCache(
        @RequestBody @Validated @ApiParam(name = "待更新对象实例") LeadershipAssign leadershipAssign,
        BindingResult bindingResult) {
        ObjectRestResponse<LeadershipAssign> restResult = new ObjectRestResponse<>();
        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        String toDoExeStatus =
            CommonUtil.exeStatusUtil(dictFeign, Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_TODO);

        leadershipAssign.setExeStatus(toDoExeStatus);
        Result<LeadershipAssign> result = leadershipAssignService.updateCache(leadershipAssign);
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
    @ApiOperation("更新单个对象")
    public ObjectRestResponse<LeadershipAssign> update(
        @RequestBody @Validated @ApiParam(name = "待更新对象实例") LeadershipAssignVo leadershipAssign,
        BindingResult bindingResult) {
        ObjectRestResponse<LeadershipAssign> restResult = new ObjectRestResponse<>();
        if (bindingResult.hasErrors()) {
            restResult.setStatus(400);
            restResult.setMessage(bindingResult.getAllErrors().get(0).getDefaultMessage());
            return restResult;
        }

        Result<LeadershipAssign> result = new Result<>();
        try {
            result = leadershipAssignService.update(leadershipAssign);
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
    @ApiOperation("分页获取对象列表")
    public TableResultResponse<LeadershipAssignVo> list(
        @RequestParam(defaultValue = "1") @ApiParam(name = "当前页") int page,
        @RequestParam(defaultValue = "10") @ApiParam(name = "页容量") int limit,
        @RequestParam(defaultValue = "") @ApiParam(name = "交办事项编号") String taskCode,
        @RequestParam(defaultValue = "") @ApiParam(name = "交界事项标题") String taskTitle,
        @RequestParam(defaultValue = "") @ApiParam(name = "地点") String taskAddr,
        @RequestParam(defaultValue = "") @ApiParam(name = "处理状态") String exeStatus,
        @RequestParam(defaultValue = "") @ApiParam(name = "按时间查询(起始时间)") String startTime,
        @RequestParam(defaultValue = "") @ApiParam(name = "按时间查询 (终止时间)") String endTime) {
        LeadershipAssign leadershipAssign = new LeadershipAssign();
        leadershipAssign.setTaskCode(taskCode);
        leadershipAssign.setTaskTitle(taskTitle);
        leadershipAssign.setTaskAddr(taskAddr);
        leadershipAssign.setExeStatus(exeStatus);

        TableResultResponse<LeadershipAssignVo> list =
            leadershipAssignService.getList(leadershipAssign, page, limit, startTime, endTime);
        return list;
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<LeadershipAssignVo> getOne(
        @PathVariable(value = "id") @ApiParam(name = "待查询对象ID") Integer id) {
        ObjectRestResponse<LeadershipAssignVo> one = leadershipAssignService.getOne(id);
        one.setStatus(200);
        one.setMessage("成功");
        return one;
    }

    @RequestMapping(value = "/get/toDo/{id}", method = RequestMethod.GET)
    @ApiOperation("查询单个对象")
    public ObjectRestResponse<LeadershipAssignVo> getToDo(
        @PathVariable(value = "id") @ApiParam(name = "待查询对象ID") Integer id) {
        ObjectRestResponse<LeadershipAssignVo> result = leadershipAssignService.getOne(id);

        LeadershipAssignVo data = result.getData();

        String toDoExeStatus =
            CommonUtil.exeStatusUtil(dictFeign, Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_TODO);

        if (!data.getExeStatus().equals(toDoExeStatus)) {
            result.setStatus(400);
            result.setMessage("当前记录不能修改，只有未发起的热线记录可修改！");
            result.setData(null);
            return result;
        }

        result.setStatus(200);
        result.setMessage("成功");
        return result;
    }

    @RequestMapping(value = "/remove/{ids}", method = RequestMethod.DELETE)
    @ApiOperation("批量删除对象")
    public ObjectRestResponse<PublicOpinion> remove(
        @PathVariable("ids") @ApiParam(name = "待删除对象ID集合，多个ID用“，”隔开") Integer[] ids) {
        ObjectRestResponse<PublicOpinion> result = new ObjectRestResponse<>();
        if (ids == null || ids.length == 0) {
            result.setStatus(400);
            result.setMessage("请选择要删除的项");
            return result;
        }

        Result<Void> _result = leadershipAssignBiz.remove(ids);

        if (!_result.getIsSuccess()) {
            result.setStatus(400);
            result.setMessage(_result.getMessage());
            return result;
        }

        result.setStatus(200);
        result.setMessage("成功");
        return result;
    }
}