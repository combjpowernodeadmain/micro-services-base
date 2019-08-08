package com.bjzhianjia.scp.party.biz;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.party.entity.TrainActivityGroup;
import com.bjzhianjia.scp.party.vo.TrainActivityVO;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.TrainActivity;
import com.bjzhianjia.scp.party.mapper.TrainActivityMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 百人优培简讯活动表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Service
public class TrainActivityBiz extends BusinessBiz<TrainActivityMapper, TrainActivity> {

    @Autowired
    private TrainActivityGroupBiz trainActivityGroupBiz;


    @Override
    public void insertSelective(TrainActivity trainActivity) {
        trainActivity.setId(UUIDUtils.generateUuid());
        super.insertSelective(trainActivity);
    }

    /**
     * 添加简讯活动
     *
     * @param trainActivityVO 简讯活动信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void addTrainActivity(TrainActivityVO trainActivityVO) {
        String ids = trainActivityVO.getGroupIds();
        TrainActivity trainActivity = new TrainActivity();
        org.springframework.beans.BeanUtils.copyProperties(trainActivityVO, trainActivity);
        this.insertSelective(trainActivity);
        List<String> groupIds;
        if (StringUtils.isNotBlank(ids)) {
            groupIds = Arrays.asList(ids.split(","));
            trainActivityGroupBiz.addTrainActivityGroups(trainActivity.getId(), groupIds);
        }
    }

    /**
     * 更新简讯活动
     *
     * @param trainActivityVO 简讯活动信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateTrainActivity(TrainActivityVO trainActivityVO) {
        // 删除分组关系
        trainActivityGroupBiz.delByTrainActivityId(trainActivityVO.getId());

        // 更新简讯活动
        TrainActivity trainActivity = new TrainActivity();
        org.springframework.beans.BeanUtils.copyProperties(trainActivityVO, trainActivity);
        this.updateSelectiveById(trainActivity);

        //添加分组关系
        String ids = trainActivityVO.getGroupIds();
        List<String> groupIds;
        if (StringUtils.isNotBlank(ids)) {
            groupIds = Arrays.asList(ids.split(","));
            trainActivityGroupBiz.addTrainActivityGroups(trainActivity.getId(), groupIds);
        }
    }

    /**
     * 获取简讯活动信息
     *
     * @param activityId 简讯活动id
     * @return
     */
    public TrainActivityVO getTrainActivityInfo(String activityId) {
        if (StringUtils.isBlank(activityId)) {
            return new TrainActivityVO();
        }
        // 获取简讯活动信息
        Example example = new Example(TrainActivity.class)
                .selectProperties("id", "trainId", "trainActivityTitle", "trainActivityContent", "startDate", "endDate");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", activityId);
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        List<TrainActivity> trainActivityList = this.mapper.selectByExample(example);
        if (BeanUtils.isNotEmpty(trainActivityList)) {
            TrainActivity newTrainActivity = trainActivityList.get(0);
            TrainActivityVO resultData = new TrainActivityVO();
            org.springframework.beans.BeanUtils.copyProperties(newTrainActivity, resultData);
            // 获取分组信息
            List<String> names = this.trainActivityGroupBiz.getByTrainGroupNames(resultData.getId());
            StringBuilder groupIds = new StringBuilder();
            for (int i = 0, len = names.size(); i < len; i++) {
                if (StringUtils.isBlank(names.get(i))) {
                    continue;
                }
                groupIds.append(names.get(i));
                if (i != len - 1) {
                    groupIds.append(",");
                }
            }
            resultData.setGroupIds(groupIds.toString());
            return resultData;
        } else {
            return new TrainActivityVO();
        }
    }

    /**
     * 获取简讯活动信息
     *
     * @param activityId 简讯活动id
     * @return
     */
    public TrainActivityVO getTrainActivityVO(String activityId) {
        if (StringUtils.isBlank(activityId)) {
            return new TrainActivityVO();
        }
        // 获取简讯活动信息
        Example example = new Example(TrainActivity.class)
                .selectProperties("id", "trainId", "trainActivityTitle", "trainActivityContent", "startDate", "endDate", "isWhole");
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", activityId);
        criteria.andEqualTo("isDeleted", BooleanUtil.BOOLEAN_FALSE);
        List<TrainActivity> trainActivityList = this.mapper.selectByExample(example);

        if (BeanUtils.isNotEmpty(trainActivityList)) {
            TrainActivity newTrainActivity = trainActivityList.get(0);
            TrainActivityVO resultData = new TrainActivityVO();
            org.springframework.beans.BeanUtils.copyProperties(newTrainActivity, resultData);
            // 获取活动分组人员信息
            List<TrainActivityGroup> activityGroupList = trainActivityGroupBiz.getByTrainActivityId(newTrainActivity.getId());
            StringBuilder groupIds = new StringBuilder();
            TrainActivityGroup trainActivityGroup;
            for (int i = 0, len = activityGroupList.size(); i < len; i++) {
                trainActivityGroup = activityGroupList.get(i);
                groupIds.append(trainActivityGroup.getTrainGroupId());
                if (i != len - 1) {
                    groupIds.append(",");
                }
            }
            resultData.setGroupIds(groupIds.toString());
            return resultData;
        } else {
            return new TrainActivityVO();
        }
    }

    /**
     * 逻辑删除
     *
     * @param id 主键id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Object id) {
        String trainActivityId = String.valueOf(id);
        // 删除简讯关系和分组
        TrainActivityGroup trainActivityGroup = new TrainActivityGroup();
        trainActivityGroup.setTrainActivityId(trainActivityId);
        trainActivityGroupBiz.delete(trainActivityGroup);
        // 逻辑删除简讯
        TrainActivity trainActivity = new TrainActivity();
        trainActivity.setId(trainActivityId);
        trainActivity.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);
        this.updateSelectiveById(trainActivity);
    }

    /**
     * 逻辑批量删除
     *
     * @param ids 简讯ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            String[] trainActivityIds = ids.split(",");
            for (String trainActivityId : trainActivityIds) {
                // 逻辑删除简讯
                TrainActivity trainActivity = new TrainActivity();
                trainActivity.setId(trainActivityId);
                trainActivity.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);
                this.updateSelectiveById(trainActivity);

                // 删除简讯关系和分组
                TrainActivityGroup trainActivityGroup = new TrainActivityGroup();
                trainActivityGroup.setTrainActivityId(trainActivityId);
                trainActivityGroupBiz.delete(trainActivityGroup);
            }
        }
    }

    /**
     * 通过优培id获取简讯数量
     *
     * @param trainId 优培id
     * @return
     */
    public int getCountByTrain(String trainId) {
        if (StringUtils.isBlank(trainId)) {
            return 0;
        }
        TrainActivity trainActivity = new TrainActivity();
        trainActivity.setTrainId(trainId);
        trainActivity.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
        return this.mapper.selectCount(trainActivity);
    }

    /**
     * 获取全部优培批次简讯
     *
     * @param page               页码
     * @param limit              页容量
     * @param trainId            优培批次id
     * @param startDate          开始日期
     * @param endDate            结束日期
     * @param trainActivityTitle 简讯名称
     * @return
     */
    public TableResultResponse<JSONObject> getTrainActivitys(Integer page, Integer limit, String trainId,
                                                             String trainActivityTitle, String startDate, String endDate) {
        if (StringUtils.isBlank(trainId)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }
        Page pageHelper = PageHelper.startPage(page, limit);
        List<JSONObject> data = this.mapper.selectTrainActivitys(trainId, startDate, endDate, trainActivityTitle);
        if (BeanUtils.isEmpty(data)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        } else {
            return new TableResultResponse<>(pageHelper.getTotal(), data);
        }
    }
}