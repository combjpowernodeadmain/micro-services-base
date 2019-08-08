package com.bjzhianjia.scp.party.biz;

import com.bjzhianjia.scp.security.common.util.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.party.entity.TrainGroup;
import com.bjzhianjia.scp.party.mapper.TrainGroupMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * 百人优培批注分组表
 *
 * @author chenshuai
 * @version 2019-05-27 14:43:49
 */
@Service
public class TrainGroupBiz extends BusinessBiz<TrainGroupMapper, TrainGroup> {

    @Autowired
    private TrainGroupMemberBiz trainGroupMemberBiz;

    /**
     * 通过优培批次id删除分组和成员关系
     *
     * @param trainId 优培id
     * @return
     */
    public int deleteByTrainId(String trainId) {
        if (StringUtils.isBlank(trainId)) {
            return 0;
        }
        return this.mapper.deleteByTrainId(trainId);
    }

    /**
     * 通过优培批次id获取分组关系
     *
     * @param trainId 优培关系表
     * @return
     */
    public List<TrainGroup> getTrainGroupsByTrainId(String trainId) {
        if (StringUtils.isBlank(trainId)) {
            return new ArrayList<>();
        }
        Example example = new Example(TrainGroup.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("trainId", trainId);
        List<TrainGroup> resultData = this.mapper.selectByExample(example);
        if (BeanUtils.isEmpty(resultData)) {
            return new ArrayList<>();
        } else {
            return resultData;
        }
    }
}