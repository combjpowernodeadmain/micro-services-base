package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.vo.PatrolRecordVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PatrolRecord;
import com.bjzhianjia.scp.cgp.mapper.PatrolRecordMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 巡查记录表，用于记录某一次巡查信息
 *
 * @author bo
 * @version 2019-05-26 16:43:21
 * @email 576866311@qq.com
 */
@Service
public class PatrolRecordBiz extends BusinessBiz<PatrolRecordMapper, PatrolRecord> {

    @Autowired
    private LawEnforcePathBiz lawEnforcePathBiz;

    /**
     * 添加单条记录
     *
     * @param patrolRecord
     * @return
     */
    public ObjectRestResponse<PatrolRecord> startPatrolRecored() {
        ObjectRestResponse<PatrolRecord> result = new ObjectRestResponse<>();

        try {
            PatrolRecord unfinishedPatrol = this.getUnfinishedPatrol(BaseContextHandler.getUserID());
            if (BeanUtil.isNotEmpty(unfinishedPatrol)) {
                result.setMessage("您有未完成巡查，请联系管理员！");
                result.setStatus(400);
                return result;
            }
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setStatus(400);
            return result;
        }

        PatrolRecord patrolRecord = new PatrolRecord();
        patrolRecord.setStartTime(new Date());
        patrolRecord.setPatrolStatus("0");
        patrolRecord.setCrtUserId(BaseContextHandler.getUserID());
        patrolRecord.setCrtUserName(BaseContextHandler.getUsername());
        patrolRecord.setTenantId(BaseContextHandler.getTenantID());
        return addPatrolRecord(patrolRecord);
    }

    /**
     * 添加单个记录
     *
     * @param patrolRecord
     * @return
     */
    public ObjectRestResponse<PatrolRecord> addPatrolRecord(PatrolRecord patrolRecord) {
        ObjectRestResponse<PatrolRecord> result = new ObjectRestResponse<>();
        check(patrolRecord, result, false);
        if (result.getStatus() != 200) {
            return result;
        }

        this.insertSelective(patrolRecord);
        result.setStatus(200);
        result.setMessage("开始巡查");
        return result;
    }

    private void check(PatrolRecord patrolRecord, ObjectRestResponse<PatrolRecord> result, boolean isUpdate) {

    }

    /**
     * 更新记录
     *
     * @param deptBiztype
     * @return
     */
    public ObjectRestResponse<PatrolRecord> endPatrolRecored() {
        ObjectRestResponse<PatrolRecord> result = new ObjectRestResponse<>();

        PatrolRecord patrolRecord=new PatrolRecord();
        try {
            PatrolRecord unfinishedPatrol = this.getUnfinishedPatrol(BaseContextHandler.getUserID());
            if(BeanUtil.isEmpty(unfinishedPatrol)){
                result.setMessage("巡查记录异常，请联系管理员!");
                result.setStatus(400);
                return result;
            }

            patrolRecord.setId(unfinishedPatrol.getId());
            patrolRecord.setPatrolStatus("1");
            patrolRecord.setEndTime(new Date());
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            result.setStatus(400);
            return result;
        }

        return updatePatrolRecord(patrolRecord);
    }

    private ObjectRestResponse<PatrolRecord> updatePatrolRecord(PatrolRecord patrolRecord) {
        ObjectRestResponse<PatrolRecord> result = new ObjectRestResponse<>();
        check(patrolRecord, result, true);
        if (result.getStatus() != 200) {
            return result;
        }

        this.updateSelectiveById(patrolRecord);
        result.setStatus(200);
        result.setMessage("结束巡查");
        return result;
    }

    /**
     * 获取userId未完成的巡查轨迹
     *
     * @param userId
     * @return
     */
    public TableResultResponse<JSONObject> unfinishedPatrolPath(String userId) {
        TableResultResponse<JSONObject> result = new TableResultResponse<>();

        /*
         * 1-> 查询userId未完成的记录
         * 2-> 根据巡查开始时间获取轨迹
         */
        // 1->
        boolean isFastDead = false;
        PatrolRecord recordInDB = null;
        try {
            recordInDB = getUnfinishedPatrol(userId);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            isFastDead = true;
        }
        if (BeanUtil.isEmpty(recordInDB) || isFastDead) {
            result.getData().setTotal(0);
            result.getData().setRows(new ArrayList<>());
            return result;
        }
        //2->
        JSONArray byUserIdAndDate = lawEnforcePathBiz.getByUserIdAndDate(userId,
            DateUtil.dateFromDateToStr(recordInDB.getStartTime(), DateUtil.DEFAULT_DATE_FORMAT),
            DateUtil.dateFromDateToStr(new Date(), DateUtil.DEFAULT_DATE_FORMAT));

        List<JSONObject> mapInfoList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(byUserIdAndDate)) {
            result.getData().setTotal(byUserIdAndDate.size());
            for (int i = 0; i < byUserIdAndDate.size(); i++) {
                mapInfoList.add(JSONObject.parseObject(byUserIdAndDate.getJSONObject(i).getString("mapInfo")));
            }
        }
        result.getData().setRows(mapInfoList);
        return result;
    }

    private PatrolRecord getUnfinishedPatrol(String userId) throws Exception {
        PatrolRecord queryRecord = new PatrolRecord();
        queryRecord.setPatrolStatus("0");
        queryRecord.setCrtUserId(userId);
        List<PatrolRecord> unfinishedPatrolList = this.mapper.select(queryRecord);
        if (BeanUtil.isNotEmpty(unfinishedPatrolList)) {
            if (unfinishedPatrolList.size() > 1) {
                throw new Exception("巡查记录异常，请联系管理员!");
            } else {
                return unfinishedPatrolList.get(0);
            }
        }

        return null;
    }

    /**
     * 分页获取记录
     *
     * @param patrolRecord
     * @param addition
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<PatrolRecordVo> getPatrolRecordLiset(PatrolRecord patrolRecord, JSONObject addition,
        Integer page, Integer limit) {
        Example example = new Example(patrolRecord.getClass());
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotEmpty(patrolRecord.getCrtUserId())) {
            criteria.andEqualTo("crtUserId", patrolRecord.getCrtUserId());
        }
        if (StringUtils.isNotEmpty(addition.getString("queryStartTimeStr")) && StringUtils
            .isNotEmpty(addition.getString("queryEndTimeStr"))) {
            Date queryStartTime =
                DateUtil.dateFromStrToDate(addition.getString("queryStartTimeStr"), DateUtil.DEFAULT_DATE_FORMAT);
            Date queryEndTime =
                DateUtil.dateFromStrToDate(addition.getString("queryEndTimeStr"), DateUtil.DEFAULT_DATE_FORMAT);

            criteria.andBetween("startTime", queryStartTime, queryEndTime);
        }
        if (StringUtils.isNotEmpty(patrolRecord.getPatrolStatus())) {
            criteria.andEqualTo("patrolStatus", patrolRecord.getPatrolStatus());
        }

        example.setOrderByClause("id desc");

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<PatrolRecord> patrolRecords = this.selectByExample(example);

        if (BeanUtil.isEmpty(patrolRecords)) {
            patrolRecords = new ArrayList<>();
        }

        List<PatrolRecordVo> patrolRecordVos = queryAssist(patrolRecords);

        return new TableResultResponse<>(pageInfo.getTotal(), patrolRecordVos);
    }

    private List<PatrolRecordVo> queryAssist(List<PatrolRecord> patrolRecords) {
        List<PatrolRecordVo> voList = new ArrayList<>();
        DecimalFormat format = new DecimalFormat("0.00");
        for (PatrolRecord record : patrolRecords) {
            PatrolRecordVo vo = new PatrolRecordVo();
            BeanUtils.copyProperties(record, vo);
            switch (record.getPatrolStatus()) {
                case "0":
                    vo.setPatrolStatusName("正在巡查");
                    break;
                case "1":
                    vo.setPatrolStatusName("巡查完成");
                    break;
                default:
            }

            // 巡查时长
            if (BeanUtil.isNotEmpty(vo.getEndTime())) {
                long lengthTimes = vo.getEndTime().getTime() - vo.getStartTime().getTime();
                String lengthHours = format.format(lengthTimes * 1.0 / DateUtils.MILLIS_PER_HOUR);
                vo.setPatrolTimeLength(lengthHours);
            } else {
                vo.setPatrolTimeLength("-");
            }

            voList.add(vo);
        }

        return voList;
    }

    /**
     * 获取某人（userId）在某次巡查中（patrolRecordId）的巡查轨迹
     *
     * @param userId
     * @param patrolRecordId
     * @return
     */
    public TableResultResponse<JSONObject> finishedPatrolPath(String patrolRecordId) {
        TableResultResponse<JSONObject> result = new TableResultResponse<>();

        /*
         * 1-> 查询userId未完成的记录
         * 2-> 根据巡查开始时间获取轨迹
         */
        // 1->
        PatrolRecord recordInDB = this.selectById(Integer.valueOf(patrolRecordId));
        if (BeanUtil.isEmpty(recordInDB)) {
            result.getData().setTotal(0);
            result.getData().setRows(new ArrayList<>());
            return result;
        }
        //2->
        JSONArray byUserIdAndDate = lawEnforcePathBiz.getByUserIdAndDate(recordInDB.getCrtUserId(),
            DateUtil.dateFromDateToStr(recordInDB.getStartTime(), DateUtil.DEFAULT_DATE_FORMAT),
            DateUtil.dateFromDateToStr(new Date(), DateUtil.DEFAULT_DATE_FORMAT));

        List<JSONObject> mapInfoList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(byUserIdAndDate)) {
            result.getData().setTotal(byUserIdAndDate.size());
            for (int i = 0; i < byUserIdAndDate.size(); i++) {
                mapInfoList.add(JSONObject.parseObject(byUserIdAndDate.getJSONObject(i).getString("mapInfo")));
            }
        }
        result.getData().setRows(mapInfoList);
        return result;
    }

    /**
     * 获取当前人员最新的一条记录
     * @return
     */
    public ObjectRestResponse<PatrolRecord> getLastestPatrolRecord() {
        ObjectRestResponse<PatrolRecord> restResponse=new ObjectRestResponse<>();

        Example example=new Example(PatrolRecord.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("crtUserId",BaseContextHandler.getUserID());

        example.setOrderByClause("id desc");
        PageHelper.startPage(1, 1);
        List<PatrolRecord> patrolRecords = this.selectByExample(example);

        if(BeanUtil.isNotEmpty(patrolRecords)){
            restResponse.setStatus(200);
            restResponse.setData(patrolRecords.get(0));
        }
        return restResponse;
    }
}