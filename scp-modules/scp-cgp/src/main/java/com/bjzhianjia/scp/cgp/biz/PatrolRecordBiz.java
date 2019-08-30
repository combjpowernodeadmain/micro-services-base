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
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.cgp.entity.PatrolRecord;
import com.bjzhianjia.scp.cgp.mapper.PatrolRecordMapper;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import tk.mybatis.mapper.entity.Example;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

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
            DateUtil.dateFromDateToStr(recordInDB.getEndTime(), DateUtil.DEFAULT_DATE_FORMAT));

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



    /**
     * 个人/多人 某个时间段的巡查总时长
     *
     * @param patrolRecord
     * @param addition
     * @param userIds
     * @return
     */
    public Map<String,String> totalPatrolTimeLength(PatrolRecord patrolRecord, JSONObject addition, List<String> userIds){
        Map<String,String> map = new HashedMap<>();
        Example example = new Example(patrolRecord.getClass());
        Example.Criteria criteria = example.createCriteria();

        if (StringUtils.isNotEmpty(patrolRecord.getCrtUserId())) {
            criteria.andEqualTo("crtUserId", patrolRecord.getCrtUserId());
        }
        if(BeanUtil.isNotEmpty(userIds)) {
            criteria.andIn("crtUserId",userIds);
        }
        String  queryStartTimeStr;
        String  queryEndTimeStr;
        if (StringUtils.isNotBlank(addition.getString("month"))) {
            queryStartTimeStr = DateUtil.dateFromDateToStr(
                    DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(DateUtil.dateFromStrToDate(addition.getString("month"), "yyyy-MM"))),
                    DateUtil.DEFAULT_DATE_FORMAT);
            queryEndTimeStr = DateUtil.dateFromDateToStr(DateUtil.getDayStartTime(
                    DateUtil.theFirstDayOfMonth(DateUtil.theDayOfMonthPlus(DateUtil.dateFromStrToDate(addition.getString("month"), "yyyy-MM"), 1)
                    )), DateUtil.DEFAULT_DATE_FORMAT);
            criteria.andBetween("startTime", queryStartTimeStr, queryEndTimeStr);
        }

        List<PatrolRecord> patrolRecords = this.selectByExample(example);
        DecimalFormat format = new DecimalFormat("0.00");
        //根据不同的userId进行时间总和计算
        /*long totalLengthTimes;
        if(BeanUtil.isNotEmpty(patrolRecord.getCrtUserId())){
            //单个人的时长计算
            totalLengthTimes = 0;
            for (PatrolRecord record : patrolRecords) {
                if(BeanUtil.isNotEmpty(record.getEndTime())){
                    long lengthTimes = record.getEndTime().getTime() - record.getStartTime().getTime();
                    totalLengthTimes += lengthTimes;
                }
            }
            String lengthHours = format.format(totalLengthTimes * 1.0 / DateUtils.MILLIS_PER_HOUR);
            map.put(patrolRecord.getCrtUserId(),lengthHours);
        }*/
        //多人查询
        if(BeanUtil.isNotEmpty(userIds)){
            for (String userId : userIds) {
                long totalLengthTimesUser = 0;
                for (PatrolRecord record : patrolRecords) {
                    if(userId.equals(record.getCrtUserId()) && BeanUtil.isNotEmpty(record.getEndTime())){
                        long lengthTimes = record.getEndTime().getTime() - record.getStartTime().getTime();
                        totalLengthTimesUser += lengthTimes;
                    }
                }
                if(totalLengthTimesUser == 0) {
                    map.put(userId,"0.00");
                }else{
                    String lengthHours = format.format(totalLengthTimesUser * 1.0 / DateUtils.MILLIS_PER_HOUR);
                    map.put(userId,lengthHours);
                }
            }
        }
        return map;
    }

    /**
     * 夜间巡查时长 总时长
     *      夜间寻常定义范围限制：  以巡查开始事件算起 18:00-6:00
     *              时间格式 xx:xx:xx
     *      以小时判定范围
     * @author tg
     * @param addition
     * @param userIds
     * @return
     */
    /*public Map<String,String> nightPatrolTimeLength(JSONObject addition, List<String> userIds){
        Example example = new Example(PatrolRecord.class);
        Example.Criteria criteria = example.createCriteria();
        if(BeanUtil.isNotEmpty(userIds)){
            criteria.andIn("crtUserId",userIds);
        }else{
            return new HashMap<>();
        }
        //根据月份筛选
        String  queryStartTimeStr;
        String  queryEndTimeStr;
        if (StringUtils.isNotBlank(addition.getString("month"))) {
            queryStartTimeStr = DateUtil.dateFromDateToStr(
                    DateUtil.getDayStartTime(DateUtil.theFirstDayOfMonth(DateUtil.dateFromStrToDate(addition.getString("month"), "yyyy-MM"))),
                    DateUtil.DEFAULT_DATE_FORMAT);
            queryEndTimeStr = DateUtil.dateFromDateToStr(DateUtil.getDayStartTime(
                    DateUtil.theFirstDayOfMonth(DateUtil.theDayOfMonthPlus(DateUtil.dateFromStrToDate(addition.getString("month"), "yyyy-MM"), 1)
                    )), DateUtil.DEFAULT_DATE_FORMAT);
            criteria.andBetween("startTime", queryStartTimeStr, queryEndTimeStr);
        }
        List<PatrolRecord> prList = this.selectByExample(example);

        DecimalFormat format = new DecimalFormat("0.00");
        *//**
         * 定义夜 巡查 界定 boolean  下午 18:00:00  到 凌晨 06:00:00 未夜间巡查
         *          判断条件：   开始巡查时间在界定时间内  为   夜间巡查
         *//*
        //时间界定范围
        int limitTimeStart = 18;
        int limitTimeEnd = 6;
        //计算总时长
        Map<String,String> nightRecordMap = new HashedMap<>();
        userIds.forEach(item->{
            long nightLengthTimesUser = 0;
            for (PatrolRecord pr : prList) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(pr.getStartTime());
                int i = calendar.get(Calendar.HOUR);
                pr.getStartTime();
               Boolean is_night = i > limitTimeStart || i < limitTimeEnd;
                if(item.equals(pr.getCrtUserId()) && is_night){
                    long lengthTimes = pr.getEndTime().getTime() - pr.getStartTime().getTime();
                    nightLengthTimesUser += lengthTimes;
                }
            }
            if(nightLengthTimesUser == 0) {
                nightRecordMap.put(item,"0.00");
            }else{
                String lengthHours = format.format(nightLengthTimesUser * 1.0 / DateUtils.MILLIS_PER_HOUR);
                nightRecordMap.put(item,lengthHours);
            }
        });
        return  nightRecordMap;
    }*/
}