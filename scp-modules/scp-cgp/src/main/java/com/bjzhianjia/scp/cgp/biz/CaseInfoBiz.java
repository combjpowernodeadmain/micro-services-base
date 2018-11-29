package com.bjzhianjia.scp.cgp.biz;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.SpecialEvent;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.CaseInfoMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.wf.base.constant.Constants;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.monitor.service.impl.WfMonitorServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * CaseInfoBiz 预立案信息.
 *
 *
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2018年8月8日          bo      1.0            ADD
 * </pre>
 * 
 *
 * @version 1.0
 * @author bo
 *
 */
@Service
public class CaseInfoBiz extends BusinessBiz<CaseInfoMapper, CaseInfo> {

    @Autowired
    private MergeCore mergeCore;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private AreaGridBiz areaGridBiz;
    
    @Autowired
    private Environment environment;
    
    @Autowired
    private PropertiesProxy propertiesProxy;

    @Autowired
    private WfMonitorServiceImpl wfMonitorService;

    @Autowired
    private PatrolTaskBiz patrolTaskBiz;

    @Autowired
    private SpecialEventBiz specialEventBiz;
    
    @Autowired
    private RegulaObjectMapper regulaObjectMapper;

    /**
     * 查询未删除的总数
     * 
     * @return
     */
    public Integer getCount() {
        CaseInfo caseInfo = new CaseInfo();
        caseInfo.setIsDeleted("0");
        return this.mapper.selectCount(caseInfo);
    }

    /**
     * 查询ID最大的那条记录
     * 
     * @author 尚
     * @return
     */
    public CaseInfo getMaxOne() {
        Example example = new Example(CaseInfo.class);
        example.setOrderByClause("id desc");
        PageHelper.startPage(1, 1);
        List<CaseInfo> caseInfoList = this.mapper.selectByExample(example);
        if (caseInfoList == null || caseInfoList.isEmpty()) {
            return null;
        }
        return caseInfoList.get(0);
    }

    /**
     * 按条件查询
     * 
     * @author 尚
     * @param conditions
     * @return
     */
    public List<CaseInfo> getByMap(Map<String, Object> conditions) {
        Example example = new Example(CaseInfo.class);
        Criteria criteria = example.createCriteria();

        Set<String> keySet = conditions.keySet();
        for (String key : keySet) {
            criteria.andEqualTo(key, conditions.get(key));
        }

        List<CaseInfo> list = this.selectByExample(example);
        return list;
    }

    /**
     * 按分布获取对象
     * 
     * @author 尚
     * @param caseInfo
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<CaseInfo> getList(CaseInfo caseInfo, int page, int limit, boolean isNoFinish) {
        Example example = new Example(CaseInfo.class);

        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        if (isNoFinish) {
            criteria.andEqualTo("isFinished", "0");
            criteria.andNotEqualTo("id", caseInfo.getId());
        }

        if (caseInfo.getGrid() != null) {
            criteria.andEqualTo("grid", caseInfo.getGrid());
        }
        if (StringUtils.isNotBlank(caseInfo.getRegulaObjList())) {
            criteria.andIn("regulaObjList", Arrays.asList(caseInfo.getRegulaObjList().split(",")));
        }
        if(StringUtils.isNotBlank(caseInfo.getCaseTitle())) {
            criteria.andLike("caseTitle", "%" + caseInfo.getCaseTitle() + "%");
        }
        if(StringUtils.isNotBlank(caseInfo.getCaseCode())) {
            criteria.andLike("caseCode", "%" + caseInfo.getCaseCode() + "%");
        }
        example.setOrderByClause("crt_time desc");

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseInfo> list = this.mapper.selectByExample(example);

        try {
            mergeCore.mergeResult(CaseInfo.class, list);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new TableResultResponse<CaseInfo>(pageInfo.getTotal(), list);
    }

    /**
     * 按分布获取对象
     * 
     * @author 尚
     * @param caseInfo
     * @return
     */
    public TableResultResponse<CaseInfo> getList(CaseInfo caseInfo, Set<Integer> ids, JSONObject queryData) {
        // 查询参数
        int page = StringUtils.isBlank(queryData.getString("page")) ? 1 : Integer.valueOf(queryData.getString("page"));
        int limit =
            StringUtils.isBlank(queryData.getString("limit")) ? 10 : Integer.valueOf(queryData.getString("limit"));
        String startQueryTime = queryData.getString("startQueryTime");
        String endQueryTime = queryData.getString("endQueryTime");

        String isSupervise = queryData.getString("isSupervise");
        String isUrge = queryData.getString("isUrge");
        String isOverTime = queryData.getString("isOverTime");
        String isFinished = queryData.getString("procCtaskname");// 1:已结案2:已终止
        String sourceType = queryData.getString("sourceType");// 来源类型
        String sourceCode = queryData.getString("sourceCode");// 来源id

        Example example = new Example(CaseInfo.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        // 事件标题
        if (StringUtils.isNotBlank(caseInfo.getCaseTitle())) {
            if (caseInfo.getCaseTitle().length() > 127) {
                throw new BizException("事件名称应该小于127个字符");
            }
            criteria.andLike("caseTitle", "%" + caseInfo.getCaseTitle() + "%");
        }
        // 事件相关业务条线
        if (StringUtils.isNotBlank(caseInfo.getBizList())) {
            criteria.andLike("bizList", "%" + caseInfo.getBizList() + "%");
        }
        // 事件相关事件类别
        if (StringUtils.isNotBlank(caseInfo.getEventTypeList())) {
            criteria.andLike("eventTypeList", "%" + caseInfo.getEventTypeList() + "%");
        }
        // 事件来源
        if (StringUtils.isNotBlank(caseInfo.getSourceType())) {
            criteria.andEqualTo("sourceType", caseInfo.getSourceType());
        }
        // 事发起止时间
        if (!(StringUtils.isBlank(startQueryTime) || StringUtils.isBlank(endQueryTime))) {
            Date start = DateUtil.dateFromStrToDate(startQueryTime, "yyyy-MM-dd HH:mm:ss");
            Date end = DateUtils.addDays(DateUtil.dateFromStrToDate(endQueryTime, "yyyy-MM-dd HH:mm:ss"), 1);
            criteria.andBetween("occurTime", start, end);
        }
        // 事件级别
        if (StringUtils.isNotBlank(caseInfo.getCaseLevel())) {
            criteria.andEqualTo("caseLevel", caseInfo.getCaseLevel());
        }
        // 按ID集合查询
        if (ids != null && !ids.isEmpty()) {
            criteria.andIn("id", ids);
        }
        // 是否添加督办 (1:是|0:否)
        if (StringUtils.isNotBlank(isSupervise) && "1".equals(isSupervise)) {
            criteria.andEqualTo("isSupervise", caseInfo.getIsSupervise());
        }
        // 是否添加崔办(1:是|0:否)
        if (StringUtils.isNotBlank(isUrge) && "1".equals(isUrge)) {
            criteria.andEqualTo("isUrge", caseInfo.getIsUrge());
        }
        // 超时时间 (1:是|0:否)
        if (StringUtils.isNotBlank(isOverTime) && "1".equals(isOverTime)) {
            // 任务没有结束，当前日期和期限日期进行判断，任务结束，则判断完成日期和期限日期
            String date = DateUtil.dateFromDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
            // 将OR关键字两侧条件作为一个整体
            criteria.andCondition(
                "((dead_line < '" + date + "' and is_finished=0) or (dead_line < finish_time and is_finished in(1,2)))");
        }
        // 处理状态：已结案(0:未完成|1:已结案2:已终止)
        if (StringUtils.isNotBlank(isFinished)){
            // 为true表明需要只查询已结案或已终止
            boolean isFinishFlag =
                    CaseInfo.FINISHED_STATE_FINISH.equals(isFinished)
                            || CaseInfo.FINISHED_STATE_STOP.equals(isFinished);
            /*
             * isFinished变量由procCtaskname参数自前端带入，如果该值不为空，表明前端按条件进行查询
             */
            if (isFinishFlag) {
                // 只查询1:已结案2:已终止
                criteria.andEqualTo("isFinished", isFinished);
            } else {
                // 处理状态：只查询未完成
                criteria.andEqualTo("isFinished", CaseInfo.FINISHED_STATE_TODO);
            }
        }

        if (StringUtils.isNotBlank(sourceType) && StringUtils.isNotBlank(sourceCode)
            && Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK.equals(sourceType)) {
            criteria.andEqualTo("sourceType", sourceType);
            criteria.andEqualTo("sourceCode", sourceCode);
        }
        // 事件编号
        if (StringUtils.isNotBlank(caseInfo.getCaseCode())) {
            criteria.andLike("caseCode", "%" + caseInfo.getCaseCode() + "%");
        }
        example.setOrderByClause("crt_time desc");

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseInfo> list = this.mapper.selectByExample(example);

        return new TableResultResponse<CaseInfo>(pageInfo.getTotal(), list);
    }

    /**
     * 按ID集合查询对象集合
     * 
     * @author 尚
     * @param idList
     * @return
     */
    public List<CaseInfo> getListByIds(List<String> idList) {
        return this.mapper.selectByIds(String.join(",", idList));
    }

    /**
     * 事件处理统计
     * 
     * @return
     */
    public JSONObject getStatisCaseState(CaseInfo caseInfo, String startTime, String endTime, String grids) {
        JSONObject result = new JSONObject();
        // 已终止、已完成、处理中、总数
        String[] stateKey = { "stop", "finish", "todo", "total" };

        // 超时统计
        Integer overtime = this.mapper.selectOvertime(caseInfo, startTime, endTime, grids);
        // 处理状态统计
        List<Map<String, Integer>> finishedState = this.mapper.selectState(caseInfo, startTime, endTime, grids);
        String stateName = "";
        for (Map<String, Integer> state : finishedState) {
            switch (String.valueOf(state.get("state"))) {
                case CaseInfo.FINISHED_STATE_STOP:
                    stateName = stateKey[0];
                    break;
                case CaseInfo.FINISHED_STATE_FINISH:
                    stateName = stateKey[1];
                    break;
                case CaseInfo.FINISHED_STATE_TODO:
                    stateName = stateKey[2];
                    break;

            }
            result.put(stateName, state.get("count"));
        }

        // 封装处理中、总数
        if (overtime != null && BeanUtil.isNotEmpty(finishedState)) {
            result.put("overtime", overtime);
            // 已终止的
            Integer stopCount = result.getInteger(stateKey[0]);
            stopCount = stopCount == null ? 0 : stopCount;
            // 已完成
            Integer finishCount = result.getInteger(stateKey[1]);
            finishCount = finishCount == null ? 0 : stopCount;
            // 已完成 = 已完成 + 已终止
            result.put(stateKey[1], stopCount + finishCount);

            // 事件总数
            result.put(stateKey[3], this.getCount());
            // 删除多余字段
            result.remove(stateKey[0]);
        }
        return result;
    }

    /**
     * 事件来源分布统计
     * 
     */
    public JSONArray getStatisEventSource(CaseInfo caseInfo, String startTime, String endTime, String gridIds) {
        JSONArray result = new JSONArray();

        List<Map<String, Object>> eventSourceList =
            this.mapper.getStatisEventSource(caseInfo, startTime, endTime, gridIds);
        Map<String, String> eventtypeMap = dictFeign.getByCode(Constances.ROOT_BIZ_EVENTTYPE);
        if (BeanUtil.isNotEmpty(eventtypeMap)) {
            // 事件来源数量
            Integer count = 0;
            // 事件来源类型集处理
            Map<String, Integer> eventSourceMap = new HashMap<>();
            for (Map<String, Object> eventSource : eventSourceList) {
                String key = String.valueOf(eventSource.get("sourceType"));
                count = Integer.valueOf(String.valueOf(eventSource.get("count")));
                eventSourceMap.put(key, count);
            }

            // 封装前台显示数据
            JSONObject obj = null;
            Set<String> eventtypeKey = eventtypeMap.keySet();
            if (eventtypeKey != null && !eventtypeKey.isEmpty()) {
                for (String key : eventtypeKey) {
                    obj = new JSONObject();
                    obj.put("sourceTypeName", eventtypeMap.get(key));
                    count = eventSourceMap.get(key);
                    obj.put("count", count != null ? count : 0);
                    result.add(obj);
                }
            }
        }
        return result;
    }

    /**
     * 
     * @param caseInfo
     *            查询参数
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param gridIds
     * @return
     */
    public List<Map<String, Object>> getStatisCaseInfo(CaseInfo caseInfo, String startTime, String endTime,
        String gridIds) {
        List<Map<String, Object>> list = this.mapper.getStatisCaseInfo(caseInfo, startTime, endTime, gridIds);

        if (BeanUtil.isNotEmpty(list)) {
            Map<String, Map<String, Object>> tempData = new HashMap<>();
            // 年月
            String ymKey = "";
            for (Map<String, Object> map : list) {
                ymKey = String.valueOf(map.get("cyear")) + String.valueOf(map.get("cmonth"));
                tempData.put(ymKey, map);
            }

            // 封装返回集，并补充为空的数据
            list.clear();

            // 开始日期
            Calendar calStart = Calendar.getInstance();
            calStart.setTime(DateUtil.dateFromStrToDate(startTime, DateUtil.DEFAULT_DATE_FORMAT));
            // 结束日期
            Calendar calEnd = Calendar.getInstance();
            calEnd.setTime(DateUtil.dateFromStrToDate(endTime, DateUtil.DEFAULT_DATE_FORMAT));

            int startYear = 0;
            int startMonth = 0;

            while (calStart.before(calEnd) || (calStart.get(Calendar.MONTH) == calEnd.get(Calendar.MONTH))) {
                startYear = calStart.get(Calendar.YEAR);
                startMonth = calStart.get(Calendar.MONTH) + 1;

                ymKey = startYear + "" + startMonth;
                Map<String, Object> resultData = tempData.get(ymKey);
                // 当月没有数据则初始化为0
                if (resultData == null) {
                    resultData = new HashMap<>();
                    resultData.put("cyear", startYear); // 年
                    resultData.put("cmonth", startMonth); // 月
                    resultData.put("total", 0); // 总数
                    resultData.put(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_TEJI, 0); // 特急
                    resultData.put(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_JINJI, 0);// 紧急
                    resultData.put(Constances.EventLevel.ROOT_BIZ_EVENTLEVEL_NORMAL, 0);// 一般
                }
                list.add(resultData);

                calStart.add(Calendar.MONTH, 1);
            }

        } else {
            list = new ArrayList<>();
        }
        return list;
    }

    /**
     * 业务条线分布统计
     * 
     * @param caseInfo
     *            查询条件
     * @param gridLevel
     *            网格等级
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public JSONArray getStatisBizLine(CaseInfo caseInfo, String gridLevel, String startTime, String endTime) {
        JSONArray result = new JSONArray();

        // 网格等级
        List<AreaGrid> areaGridList = null;
        // 网格ids
        String gridIds="";
        Set<String> gridIdSet=new HashSet<>();
        if (StringUtils.isNotBlank(gridLevel)) {
            areaGridList = areaGridBiz.getByGridLevel(gridLevel);
            if(BeanUtil.isNotEmpty(areaGridList)){
                for(AreaGrid areaGridTmp:areaGridList){
                    gridIdSet.add(String.valueOf(areaGridTmp.getId()));
                }
                gridIds=String.join(",", gridIdSet);
            }
        }

        Map<String, String> bizType = dictFeign.getByCode(Constances.ROOT_BIZ_TYPE);
        if (BeanUtil.isEmpty(bizType)) {
            bizType = new HashMap<>();
        }

        JSONObject obj = null;
        // 返回集初始化
        Map<String, JSONObject> temp = new HashMap<>();
        Set<String> setKey = bizType.keySet();
        for (String key : setKey) {
            obj = new JSONObject();
            obj.put("bizList", key);
            obj.put("count", 0);
            obj.put("bizListName", bizType.get(key));
            temp.put(key, obj);
        }
        // 封装数据库中的数据
        System.out.println(gridIds.toString());
        List<Map<String, Object>> bizLineList =
            this.mapper.selectBizLine(caseInfo, gridIds, startTime, endTime);
        if (BeanUtil.isNotEmpty(bizLineList)) {
            for (Map<String, Object> bizLineMap : bizLineList) {
                obj = new JSONObject();
                String bizList = String.valueOf(bizLineMap.get("bizList"));
                if(StringUtils.isNotBlank(bizType.get(bizList))){
                    // 业务条线
                    obj.put("bizList", bizList);
                    obj.put("count", bizLineMap.get("count"));
                    obj.put("bizListName", bizType.get(bizList));
                    temp.put(bizList, obj);
                }
            }
        }
        // 封装返回集数据
        setKey = temp.keySet();
        for (String key : setKey) {
            result.add(temp.get(key));
        }
        return result;
    }

    /**
     * 网格事件统计
     * 
     * @param caseInfo
     *            查询条件
     * @param gridLevel
     *            网格等级
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public TableResultResponse<JSONObject> getGrid(CaseInfo caseInfo, String gridLevel, String startTime,
        String endTime, Integer page, Integer limit) {
        // 网格等级
        List<AreaGrid> areaGridList = null;
        // 网格ids
        StringBuilder gridIds = new StringBuilder();
        if (StringUtils.isNotBlank(gridLevel)) {
            areaGridList = areaGridBiz.getByGridLevel(gridLevel);
            int size = areaGridList.size();
            for (int i = 0; i < size; i++) {
                AreaGrid areaGrid = areaGridList.get(i);
                if (i == size - 1) {
                    gridIds.append(areaGrid.getId());
                } else {
                    gridIds.append(areaGrid.getId()).append(",");
                }
            }
        }

        Map<String, String> level = dictFeign.getByCode(Constances.ROOT_BIZ_GRID_LEVEL);
        if (BeanUtil.isEmpty(level)) {
            level = new HashMap<>();
        }
        Page<Object> result = PageHelper.startPage(page, limit);
        List<JSONObject> list = this.mapper.selectByGrid(caseInfo, startTime, endTime, gridIds.toString(),gridLevel);

        if (BeanUtil.isNotEmpty(list)) {
            for (JSONObject map : list) {
                String levelName = level.get(map.get("gridLevel"));
                map.put("gridLevelName", levelName);
                /*
                 * 在mysql里，count为关键字，修改sql语句加下划线，但前端已按count字样对接
                 * 在此做下处理
                 */
                map.put("total", map.getInteger("total_") == null ? 0 : map.getInteger("total_"));
                map.put("count", map.getInteger("count_") == null ? 0 : map.getInteger("count_"));
                map.remove("total_");
                map.remove("count_");
            }
            return new TableResultResponse<>(result.getTotal(), list);
        }
        return new TableResultResponse<>(0, null);

    }
    
    /**
     * 查询类型为专项管理的事件
     * @param patrolId
     * @return
     */
    public TableResultResponse<JSONObject> patrolCaseInfo(Integer patrolId){
        TableResultResponse<JSONObject> restResult=new TableResultResponse<>();
        
        Example example=new Example(CaseInfo.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("sourceType", environment.getProperty("sourceTypeKeyPatrol"));
        // 对是否输入了按patrolId进行查询进行判断，如果为null，则认为查询所有巡查事项类型的事件
        if(patrolId!=null){
            criteria.andEqualTo("sourceCode", patrolId);
        }

        List<CaseInfo> caseInfo = this.selectByExample(example);
        List<JSONObject> resultJObjList=new ArrayList<>();
        if(BeanUtil.isNotEmpty(caseInfo)) {
            for (CaseInfo caseInfo2 : caseInfo) {
                try {
                    JSONObject caseInfoJobj = propertiesProxy.swapProperties(caseInfo2, "id","caseTitle","mapInfo");
                    resultJObjList.add(caseInfoJobj);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            
            restResult.setStatus(200);
            restResult.getData().setTotal(resultJObjList.size());
            restResult.getData().setRows(resultJObjList);
            return restResult;
        }
        
        restResult.getData().setRows(new ArrayList<>());
        restResult.getData().setTotal(0);
        return restResult;
    }

    /**
     * 专项事件全部定位
     * @return
     */
    public TableResultResponse<JSONObject> allPositionPatrol() {
        return this.patrolCaseInfo(null);
    }

    /**
     * 通过部门id和事件等级获取事件列表
     *
     * @param caseLevel 事件等级
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<Map<String, Object>> getCaseInfoByDeptId(String caseLevel, String deptId, int page,
                                                                        int limit) {
        Page<Object> pageHelper = PageHelper.startPage(page, limit);
        List<Map<String, Object>> result = this.mapper.selectCaseInfoByDeptId(deptId, caseLevel);
        if (BeanUtil.isEmpty(result)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }
        //拼接业务ids
        StringBuilder procBizid = new StringBuilder();
        Map<String, Object> tmap;
        for (int i = 0; i < result.size(); i++) {
            tmap = result.get(i);
            if (BeanUtils.isEmpty(tmap)) {
                continue;
            }
            procBizid.append("'").append(tmap.get("id")).append("'");
            if (i < result.size() - 1) {
                procBizid.append(",");
            }
        }
        //工作流业务
        JSONObject  bizData = new JSONObject();
        //事件流程编码
        bizData.put("procKey","comprehensiveManage");
        //业务ids
        bizData.put(Constants.WfProcessBizDataAttr.PROC_BIZID,procBizid.toString());
        JSONObject objs = this.initWorkflowQuery(bizData);

        //缓存工作流实例id
        Map<String,Map<String,Object>> tempProcInstIds = new HashMap<>();
        List<Map<String,Object>>  procInstIdList = wfMonitorService.getProcInstIdByUserId(objs);
        for(Map<String,Object> map : procInstIdList){
            //procInstId实例id , procBizid 业务id
            tempProcInstIds.put(String.valueOf(map.get(Constants.WfProcessBizDataAttr.PROC_BIZID)),map);
        }

        Map<String,String>  dictTemp = dictFeign.getByCode(Constances.ROOT_BIZ_EVENTLEVEL);

        for(Map<String,Object> map : result){
            String caseLevelName = dictTemp.get(map.get("caseLevel"));
            map.put("caseLevelName",caseLevelName);
            map.put("isSupervise","0".equals(map.get("isSupervise"))?false:true);
            map.put("isUrge","0".equals(map.get("isUrge"))?false:true);

            // 向结果集添加工作流数据
            Map<String, Object> wfTaskMap = tempProcInstIds.get(String.valueOf(map.get("id")));
            if(BeanUtil.isNotEmpty(wfTaskMap)){
                map.put("procInstId",wfTaskMap.get("procInstId"));
                map.put("procTaskCommittime",wfTaskMap.get("procTaskCommittime"));
                map.put("procCtaskName",wfTaskMap.get("procCtaskName"));
            }
        }
        return new TableResultResponse<>(pageHelper.getTotal(), result);
    }
    /**
     * 初始化工作流参数
     * @param bizData 工作流参数
     * @return
     */
    private JSONObject initWorkflowQuery(JSONObject  bizData){
        JSONObject objs = new JSONObject();
        //流程参数
        JSONObject  procData = new JSONObject();
        //用户认证方式
        JSONObject  authData = new JSONObject();
        authData.put("procAuthType",2);
        authData.put(Constants.WfProcessAuthData.PROC_DEPATID, BaseContextHandler.getDepartID());
        //流程变量
        JSONObject  variableData = new JSONObject();
        //工作流参数
        objs.put(Constants.WfRequestDataTypeAttr.PROC_BIZDATA,bizData);
        objs.put(Constants.WfRequestDataTypeAttr.PROC_PROCDATA,procData);
        objs.put(Constants.WfRequestDataTypeAttr.PROC_AUTHDATA,authData);
        objs.put(Constants.WfRequestDataTypeAttr.PROC_VARIABLEDATA,variableData);
        return objs;
    }
    /**
     * 执法片区事件统计
     * @return
     */
    public List<JSONObject> getGridZFPQ(){
        List<JSONObject> gridZFPQListTodo = this.mapper.getGridZFPQ("0");
        List<JSONObject> gridZFPQListFinish = this.mapper.getGridZFPQ("1");

        List<AreaGrid> areaGridList=areaGridBiz.getByGridLevel(environment.getProperty("areaGrid.gridLevel.zrwg.zfpq"));

        List<JSONObject> resultJObjList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(areaGridList)) {
            Map<Integer, JSONObject> zfpqIdJObjMapTodo = new HashMap<>();
            Map<Integer, JSONObject> zfpqIdJObjMapFinish = new HashMap<>();
            if (BeanUtil.isNotEmpty(gridZFPQListTodo)) {
                for (JSONObject gridJobjTmp : gridZFPQListTodo) {
                    zfpqIdJObjMapTodo.put(gridJobjTmp.getInteger("zfqp"), gridJobjTmp);
                }
            }
            if (BeanUtil.isNotEmpty(gridZFPQListFinish)) {
                for (JSONObject gridJobjTmp : gridZFPQListFinish) {
                    zfpqIdJObjMapFinish.put(gridJobjTmp.getInteger("zfqp"), gridJobjTmp);
                }
            }

            // 整合处理结果
            for (AreaGrid areaGridTmp : areaGridList) {
                JSONObject resultJobj = new JSONObject();

                JSONObject jsonObjectTodo =
                    zfpqIdJObjMapTodo.get(areaGridTmp.getId()) == null ? new JSONObject()
                        : zfpqIdJObjMapTodo.get(areaGridTmp.getId());
                JSONObject jsonObjectFinish =
                    zfpqIdJObjMapFinish.get(areaGridTmp.getId()) == null ? new JSONObject()
                        : zfpqIdJObjMapFinish.get(areaGridTmp.getId());

                resultJobj.put("grid", areaGridTmp.getId());
                resultJobj.put("stateTodo", jsonObjectTodo.getInteger("ccount") == null ? 0
                    : jsonObjectTodo.getInteger("ccount"));
                resultJobj.put("stateFinish", jsonObjectFinish.getInteger("ccount") == null ? 0
                    : jsonObjectFinish.getInteger("ccount"));
                resultJobj.put("gridLevel", areaGridTmp.getGridLevel());
                resultJobj.put("gridName", areaGridTmp.getGridName());
                resultJobj.put("gridCode", areaGridTmp.getGridCode());

                resultJObjList.add(resultJobj);
            }
        }

        return resultJObjList;
    }

    /**
     * 事件势力学统计
     * @param caseInfo
     */
    public TableResultResponse<JSONObject> heatMap(CaseInfo caseInfo,String startDate,String endDate) {
        DateTime dateTime = new DateTime(DateUtil.dateFromStrToDate(endDate, "yyyy-MM").getTime());
        DateTime dateTime1 = dateTime.plusMonths(1);
        endDate =
                DateUtil.dateFromDateToStr(new Date(dateTime1.plusMonths(1).getMillis()), "yyyy-MM");

        List<JSONObject> rows = this.mapper.heatMap(caseInfo, startDate,endDate);
        if(BeanUtil.isNotEmpty(rows)){
            return new TableResultResponse<>(0, rows);
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

    /**
     * 按条件查询事件列表，查询结果不进行分页
     * @param queryCaseInfo
     * @param specialEventId
     */
    public List<CaseInfo> getList(CaseInfo queryCaseInfo, Integer specialEventId) {
        // 查询与专项管理记录对应的巡查事项
        Map<String,Object> conditionMap=new HashMap<>();
        conditionMap.put("sourceType", environment.getProperty("patrolType.special"));
        conditionMap.put("sourceTaskId", specialEventId);
        List<PatrolTask> patrolTaskList = patrolTaskBiz.getByMap(conditionMap);
        List<Integer> patrolTaskIdList;
        if(BeanUtil.isNotEmpty(patrolTaskList)){
            patrolTaskIdList = patrolTaskList.stream().map(o -> o.getId()).distinct().collect(Collectors.toList());
        }else{
            // 如果没有查到对应的专项管理记录，则不会有专项巡查及专项巡查对应的事件存在
            return new ArrayList<>();
        }

        Example example=new Example(CaseInfo.class).selectProperties("id","mapInfo","caseTitle");
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");

        if(StringUtils.isNotBlank(queryCaseInfo.getSourceType())){
            criteria.andEqualTo("sourceType", queryCaseInfo.getSourceType());
        }
        if(StringUtils.isNotBlank(queryCaseInfo.getIsFinished())){
            criteria.andEqualTo("isFinished", queryCaseInfo.getIsFinished());
        }
        if(BeanUtil.isNotEmpty(patrolTaskIdList)){
            criteria.andIn("sourceCode", patrolTaskIdList);
        }

        List<CaseInfo> caseInfoList = this.selectByExample(example);
        if(BeanUtil.isNotEmpty(caseInfoList)){
            return caseInfoList;
        }

        return new ArrayList<>();
    }

    /**
     * 为指挥中心首页查询列表
     * 
     * @param queryData
     * @return
     */
    public TableResultResponse<JSONObject> getListForHome(JSONObject queryData) {
        TableResultResponse<JSONObject> tableResult = new TableResultResponse<>();

        // 查询专项管理
        SpecialEvent specialEvent =
            specialEventBiz.selectById(Integer.valueOf(queryData.getString("sourceTaskId")));
        List<String> objIdList;
        if (BeanUtil.isNotEmpty(specialEvent)
            && StringUtils.isNotBlank(specialEvent.getRegObjList())) {
            objIdList = Arrays.asList(specialEvent.getRegObjList().split(","));
        } else {
            tableResult.setMessage("未找到相关监管对象");
            tableResult.setStatus(400);
            return tableResult;
        }

        queryData.put("regulaObjectId", objIdList);

        List<JSONObject> listForHome = this.mapper.getListForHome(queryData);
        if (BeanUtil.isNotEmpty(listForHome)) {
            List<Integer> regulaObjectIds =
                listForHome.stream().map(o -> o.getInteger("regulaObjectId")).distinct()
                    .collect(Collectors.toList());

            // 查询专项下的监管对象
            List<RegulaObject> regulaObjects =
                regulaObjectMapper.selectByIds(specialEvent.getRegObjList());
            // 将还没有巡查到的监管对象进行整合
            if (BeanUtil.isNotEmpty(regulaObjects)) {
                Map<Integer, RegulaObject> collectMap = new HashMap<>();
                for (RegulaObject tmpReg : regulaObjects) {
                    if ("0".equals(tmpReg.getIsDeleted())) {
                        collectMap.put(tmpReg.getId(), tmpReg);
                    }
                }
                for (Map.Entry<Integer, RegulaObject> e : collectMap.entrySet()) {
                    if (!regulaObjectIds.contains(e.getKey())) {
                        // 说明查到的结果集里不包含该监管对象，需要补充到结果集里
                        JSONObject tmpJobj = new JSONObject();
                        tmpJobj.put("patrolCount", 0);
                        tmpJobj.put("objName", e.getValue().getObjName());
                        tmpJobj.put("mapInfo", e.getValue().getMapInfo());
                        tmpJobj.put("regulaObjectId", e.getKey());
                        tmpJobj.put("pCountWithProblem", 0);
                        listForHome.add(tmpJobj);
                    }
                }
            }

            tableResult.getData().setRows(listForHome);
            tableResult.getData().setTotal(listForHome.size());
            return tableResult;
        }
        return tableResult;
    }
}