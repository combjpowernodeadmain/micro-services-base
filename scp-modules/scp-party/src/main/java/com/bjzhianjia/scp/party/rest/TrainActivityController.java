package com.bjzhianjia.scp.party.rest;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.vo.TrainActivityVO;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.TrainActivityBiz;
import com.bjzhianjia.scp.party.entity.TrainActivity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.util.Map;


/**
 * 百人优培简讯活动管理
 *
 * @author chenshuai
 * @version 2019-05-29 14:43:49
 */
@RestController
@RequestMapping("trainActivitys")
@CheckClientToken
@CheckUserToken
@Api(tags = "百人优培简讯活动管理")
public class TrainActivityController extends BaseController<TrainActivityBiz, TrainActivity, String> {
    /**
     * 获取百人优培简讯活动列表
     *
     * @param page               页码
     * @param limit              页容量
     * @param trainId            优培批次id
     * @param trainActivityTitle 简讯名称
     * @param startDate          开始日期
     * @param endDate            结束日期
     * @return
     */
    @GetMapping
    @ApiOperation("获取百人优培简讯活动列表")
    public TableResultResponse<JSONObject> trainActivitys(
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页码") Integer limit,
            @RequestParam(value = "trainId", defaultValue = "") @ApiParam("优培批次id") String trainId,
            @RequestParam(value = "trainActivityTitle", defaultValue = "") @ApiParam("简讯名称") String trainActivityTitle,
            @RequestParam(value = "startDate", defaultValue = "") @ApiParam("开始日期") String startDate,
            @RequestParam(value = "endDate", defaultValue = "") @ApiParam("结束日期") String endDate) {
        return this.baseBiz.getTrainActivitys(page, limit, trainId, trainActivityTitle, startDate, endDate);
    }

    /**
     * 新增单个简讯活动
     *
     * @param entity
     * @return
     */
    @PostMapping("/activity")
    @ApiOperation("新增单个简讯活动")
    public ObjectRestResponse<TrainActivityVO> addTrainActivity(@RequestBody @ApiParam("简讯活动信息") TrainActivityVO entity) {
        baseBiz.addTrainActivity(entity);
        return new ObjectRestResponse<TrainActivityVO>().data(entity);
    }

    /**
     * 查询单个简讯活动
     *
     * @param activityId 简讯活动id
     * @return
     */
    @GetMapping("/activity/{activityId}")
    @ApiOperation("查询单个简讯活动")
    public ObjectRestResponse<TrainActivityVO> getTrainActivity(@PathVariable("activityId") @ApiParam("简讯活动id") String activityId) {
        TrainActivityVO resultData = this.baseBiz.getTrainActivityVO(activityId);
        return new ObjectRestResponse<TrainActivityVO>().data(resultData);
    }

    /**
     * 查询单个简讯活动详情
     *
     * @param activityId 简讯活动id
     * @return
     */
    @GetMapping("/activity/{activityId}/info")
    @ApiOperation("查询单个简讯活动详情")
    public ObjectRestResponse<TrainActivityVO> getTrainActivityInfo(@PathVariable("activityId") @ApiParam("简讯活动id") String activityId) {
        TrainActivityVO resultData = this.baseBiz.getTrainActivityInfo(activityId);
        return new ObjectRestResponse<TrainActivityVO>().data(resultData);
    }

    /**
     * 更新单个简讯活动
     *
     * @param entity
     * @return
     */
    @PutMapping("/activity/{activityId}")
    @ApiOperation("更新单个简讯活动")
    public ObjectRestResponse<TrainActivityVO> updateTrainActivity(@PathVariable("activityId") @ApiParam("简讯活动id") String activityId,
                                                                   @RequestBody @ApiParam("简讯活动信息") TrainActivityVO entity) {
        baseBiz.updateTrainActivity(entity);
        return new ObjectRestResponse<TrainActivityVO>().data(entity);
    }

    /**
     * 批量删除简讯活动
     *
     * @param param 参数
     * @return
     */
    @DeleteMapping("/activitys")
    @ApiOperation("批量删除简讯活动")
    public ObjectRestResponse<Void> delTrainActivity(@RequestBody @ApiParam("简讯活动ids") Map<String, String> param) {
        String activityIds = param.get("activityIds");
        baseBiz.deleteByIds(activityIds);
        return new ObjectRestResponse<>();
    }
}