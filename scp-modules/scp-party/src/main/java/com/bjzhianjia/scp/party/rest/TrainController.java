package com.bjzhianjia.scp.party.rest;

import com.alibaba.fastjson.JSONArray;
import com.bjzhianjia.scp.party.biz.TrainGroupBiz;
import com.bjzhianjia.scp.party.entity.TrainGroup;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.rest.BaseController;
import com.bjzhianjia.scp.party.biz.TrainBiz;
import com.bjzhianjia.scp.party.entity.Train;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckClientToken;
import com.bjzhianjia.scp.security.auth.client.annotation.CheckUserToken;

import java.util.List;
import java.util.Map;

/**
 * 百人优培管理
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@RestController
@RequestMapping("trains")
@CheckClientToken
@CheckUserToken
@Api(tags = "百人优培管理")
public class TrainController extends BaseController<TrainBiz, Train, String> {

    @Autowired
    private TrainGroupBiz trainGroupBiz;

    /**
     * 获取百人优培列表
     *
     * @param page      页码
     * @param limit     页容量
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    @GetMapping
    @ApiOperation("获取百人优培列表")
    public TableResultResponse<Map<String, Object>> trains(
            @RequestParam(value = "page", defaultValue = "1") @ApiParam("页码") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") @ApiParam("页码") Integer limit,
            @RequestParam(value = "startDate", defaultValue = "") @ApiParam("开始日期") String startDate,
            @RequestParam(value = "endDate", defaultValue = "") @ApiParam("结束日期") String endDate) {
        return this.baseBiz.trains(page, limit, startDate, endDate);
    }

    /**
     * 添加优培批次分组用户信息
     *
     * @param trainId          优培批次
     * @param trainGroupMember 分组成员信息JSON
     *                         数据格式
     *                         [
     *                         {
     *                         "user0":{"id":1,"name":"党员名字1","workPost":"职务1","groupRole":"root_party_train_group_role_leader"},
     *                         "user3":{"id":3,"name":"党员名字3","workPost":"职务3","groupRole":"root_party_train_group_role_member"},
     *                         "groupIndex":0 //分组一
     *                         },
     *                         {
     *                         "user1":{"id":1,"name":"党员名字1","workPost":"职务1","groupRole":"root_party_train_group_role_member"},
     *                         "groupIndex":9 //分组十
     *                         }
     *                         ]
     * @return
     */
    @PostMapping("/{trainId}/group/member")
    public ObjectRestResponse<Void> addTrainGroupMember(
            @PathVariable("trainId") @ApiParam("优培批次id") String trainId,
            @RequestBody @ApiParam("优培批次分组信息") JSONArray trainGroupMember) {
        ObjectRestResponse<Void> result = new ObjectRestResponse<>();
        this.baseBiz.addTrainGroupMember(trainId, trainGroupMember);
        return result;
    }

    /**
     * 添加优培批次分组用户信息
     *
     * @param trainId 优培批次
     * @return 数据格式
     * [
     * {
     * "user0":{"id":1,"name":"党员名字1","workPost":"职务1","groupRole":"root_party_train_group_role_leader"},
     * "user3":{"id":3,"name":"党员名字3","workPost":"职务3","groupRole":"root_party_train_group_role_member"},
     * "groupIndex":0 //分组一
     * },
     * {
     * "user1":{"id":1,"name":"党员名字1","workPost":"职务1","groupRole":"root_party_train_group_role_member"},
     * "groupIndex":9 //分组十
     * }
     * ]
     */
    @GetMapping("/{trainId}/group/member")
    public ObjectRestResponse<JSONArray> getTrainGroupMember(@PathVariable("trainId") @ApiParam("优培批次id") String trainId) {
        ObjectRestResponse<JSONArray> result = new ObjectRestResponse<>();
        result.setData(this.baseBiz.getTrainGroupMember(trainId));
        return result;
    }

    /**
     * 通过优培批次id获取分组关系
     *
     * @param trainId 优培关系表
     * @return
     */
    @GetMapping("/{trainId}/groups")
    public ObjectRestResponse<List<TrainGroup>> getTrainGroupsByTrainId(@PathVariable("trainId") @ApiParam("优培批次id") String trainId) {
        ObjectRestResponse<List<TrainGroup>> result = new ObjectRestResponse<>();
        result.setData(trainGroupBiz.getTrainGroupsByTrainId(trainId));
        return result;
    }
}