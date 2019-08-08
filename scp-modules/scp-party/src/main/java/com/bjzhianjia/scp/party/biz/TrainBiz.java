package com.bjzhianjia.scp.party.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.entity.TrainGroup;
import com.bjzhianjia.scp.party.entity.TrainGroupMember;
import com.bjzhianjia.scp.security.common.exception.base.BusinessException;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.Train;
import com.bjzhianjia.scp.party.mapper.TrainMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 百人优培批次
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Service
public class TrainBiz extends BusinessBiz<TrainMapper, Train> {

    @Autowired
    private TrainActivityBiz trainActivityBiz;

    @Autowired
    private TrainGroupBiz trainGroupBiz;

    @Autowired
    private TrainGroupMemberBiz trainGroupMemberBiz;

    @Override
    public void insertSelective(Train train) {
        train.setId(UUIDUtils.generateUuid());
        super.insertSelective(train);
    }

    /**
     * 逻辑删除
     *
     * @param id 主键id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Object id) {
        String trainId = String.valueOf(id);
        // 判断是否存在简讯
        int count = trainActivityBiz.getCountByTrain(trainId);
        if (count > 0) {
            throw new RuntimeException("请先删除该期优培的所有简讯!");
        }
        // 删除优培分组和人员关系
        trainGroupBiz.deleteByTrainId(trainId);

        Train train = new Train();
        train.setId(trainId);
        train.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);
        this.updateSelectiveById(train);
    }

    /**
     * 获取百人优培列表
     *
     * @param page      页码
     * @param limit     页容量
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @return
     */
    public TableResultResponse<Map<String, Object>> trains(Integer page, Integer limit, String startDate, String endDate) {
        Page pageHelper = PageHelper.startPage(page, limit);
        List<Map<String, Object>> resultData = this.mapper.selectTrains(startDate, endDate);
        if (BeanUtils.isNotEmpty(resultData)) {
            return new TableResultResponse<>(pageHelper.getTotal(), resultData);
        } else {
            return new TableResultResponse<>(0, new ArrayList<>());
        }
    }

    /**
     * 添加优培批次分组用户信息
     * TODO 优化此方法
     *
     * @param tranId           优培批次
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
     */
    @Transactional(rollbackFor = Exception.class)
    public void addTrainGroupMember(String tranId, JSONArray trainGroupMember) {
        // 党员列表位置总长度
        int userLength = 10;

        JSONObject group = null;
        JSONObject user = null;
        // 清空优培批次分组和成员关系
        trainGroupBiz.deleteByTrainId(tranId);
        //添加优培批次和成员关系
        for (int i = 0, len = trainGroupMember.size(); i < len; i++) {
            group = trainGroupMember.getJSONObject(i);
            // 保存分组
            TrainGroup trainGroup = new TrainGroup();
            trainGroup.setTrainId(tranId);
            trainGroup.setSortIndex(group.getInteger("groupIndex"));
            trainGroup.setTrainGroupName(group.getString("groupName"));
            trainGroupBiz.insertSelective(trainGroup);
            // 保存分组成员
            for (int j = 0; j < userLength; j++) {
                user = group.getJSONObject("user" + j);
                // 当前位置没有党员
                if (user != null) {
                    TrainGroupMember groupMember = new TrainGroupMember();
                    groupMember.setPartyMemberId(user.getInteger("id"));
                    groupMember.setTrainGroupId(trainGroup.getId());
                    groupMember.setSortIndex(j);
                    groupMember.setPartyMemberRole(group.getString("groupRole"));
                    trainGroupMemberBiz.insertSelective(groupMember);
                }
            }
        }
    }

    /**
     * 通过优培批次获取分组和成员信息
     *
     * @param tranId 优培批次id
     * @return
     */
    public JSONArray getTrainGroupMember(String tranId) {
        List<JSONObject> resultData = this.mapper.selectTrainGroupMember(tranId);

        JSONArray data = new JSONArray();
        // 缓存行数据
        JSONObject tempRow = null;
        // 缓存用户数据
        JSONObject tempUser = null;
        JSONObject tempResultData = null;
        if (BeanUtils.isNotEmpty(resultData)) {
            // 分组成员
            Map<Integer, List<JSONObject>> tempGroupData = new HashMap<>();
            List<JSONObject> tempGroupUser = null;

            for (int i = 0, len = resultData.size(); i < len; i++) {
                tempResultData = resultData.get(i);
                if (tempResultData == null) {
                    continue;
                }
                int groupIndex = tempResultData.getInteger("groupIndex");
                tempGroupUser = tempGroupData.get(groupIndex);
                if (tempGroupUser == null) {
                    tempGroupUser = new ArrayList<>();
                    tempGroupUser.add(tempResultData);
                    tempGroupData.put(groupIndex, tempGroupUser);
                } else {
                    tempGroupUser.add(tempResultData);
                    tempGroupData.put(groupIndex, tempGroupUser);
                }
            }
            // 封装前端数据集
            Set<Integer> keys = tempGroupData.keySet();
            for (Integer key : keys) {
                tempGroupUser = tempGroupData.get(key);
                tempRow = new JSONObject();
                // 设置下标
                tempRow.put("groupIndex", key);
                for (int j = 0, len = tempGroupUser.size(); j < len; j++) {
                    tempUser = tempGroupUser.get(j);
                    if (tempUser == null) {
                        continue;
                    }
                    Integer userIndex = tempUser.getInteger("userIndex");
                    Integer userId = tempUser.getInteger("id");
                    String name = tempUser.getString("name");
                    String workPost = tempUser.getString("workPost");
                    // 设置用户信息
                    tempUser = new JSONObject();
                    tempUser.put("userIndex", userIndex);
                    tempUser.put("id", userId);
                    tempUser.put("name", name);
                    tempUser.put("workPost", workPost);
                    tempRow.put("user" + userIndex, tempUser);
                }
                data.add(tempRow);
            }
        }
        return data;
    }
}