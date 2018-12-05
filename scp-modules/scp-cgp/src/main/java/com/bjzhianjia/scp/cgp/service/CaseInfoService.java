package com.bjzhianjia.scp.cgp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.AreaGridMemberBiz;
import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.CommandCenterHotlineBiz;
import com.bjzhianjia.scp.cgp.biz.ConcernedCompanyBiz;
import com.bjzhianjia.scp.cgp.biz.ConcernedPersonBiz;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.ExecuteInfoBiz;
import com.bjzhianjia.scp.cgp.biz.LeadershipAssignBiz;
import com.bjzhianjia.scp.cgp.biz.MayorHotlineBiz;
import com.bjzhianjia.scp.cgp.biz.PatrolResBiz;
import com.bjzhianjia.scp.cgp.biz.PatrolTaskBiz;
import com.bjzhianjia.scp.cgp.biz.PublicOpinionBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.CommandCenterHotline;
import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.ExecuteInfo;
import com.bjzhianjia.scp.cgp.entity.LeadershipAssign;
import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import com.bjzhianjia.scp.cgp.entity.PatrolRes;
import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.entity.PublicOpinion;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.CaseInfoMapper;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.merge.core.MergeCore;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.constant.Constants;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.service.impl.WfMonitorServiceImpl;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskHistoryBean;
import com.bjzhianjia.scp.security.wf.base.task.service.impl.WfProcTaskServiceImpl;
import com.bjzhianjia.scp.security.wf.base.utils.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * 立案管理
 * 
 * @author 尚
 */
@Service
@Slf4j
@Transactional
public class CaseInfoService {

    @Autowired
    private CaseInfoBiz caseInfoBiz;

    @Autowired
    private WfMonitorServiceImpl wfMonitorService;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private EventTypeMapper eventTypeMapper;

    @Autowired
    private WfProcTaskServiceImpl wfProcTaskService;

    @Autowired
    private MayorHotlineBiz mayorHotlineBiz;

    @Autowired
    private PublicOpinionBiz publicOpinionBiz;

    @Autowired
    private LeadershipAssignBiz leadershipAssignBiz;

    @Autowired
    private RegulaObjectMapper regulaObjectMapper;

    @Autowired
    private RegulaObjectBiz regulaObjectBiz;

    @Autowired
    private ConcernedPersonBiz concernedPersonBiz;

    @Autowired
    private EventTypeBiz eventTypeBiz;

    @Autowired
    private AreaGridBiz areaGridBiz;

    @Autowired
    private ExecuteInfoBiz executeInfoBiz;

    @Autowired
    private ConcernedCompanyBiz concernedCompanyBiz;

    @Autowired
    private ConcernedCompanyService concernedCompanyService;

    @Autowired
    private PatrolTaskService patreolTaskService;

    @Autowired
    private PatrolTaskBiz patrolTaskBiz;
    
    @Autowired
    private PatrolResBiz patrolResBiz;

    @Autowired
    private MergeCore mergeCore;

    @Autowired
    private CommandCenterHotlineBiz commandCenterHotlineBiz;

    @Autowired
    private CommandCenterHotlineService commandCenterHotlineService;
    
    @Autowired
    private Environment environment;

    @Autowired
    private CaseInfoMapper caseInfoMapper;

    @Autowired
    private AreaGridMemberBiz areaGridMemberBiz;

    @Autowired
    @Qualifier("transactionManager")
    private PlatformTransactionManager platformTransactionManager;

    /**
     * 更新单个对象
     * 
     * @author 尚
     * @param caseInfo
     * @return
     */
    public Result<Void> update(CaseInfo caseInfo) {
        Result<Void> result = new Result<>();
        result.setIsSuccess(false);

        this.caseInfoBiz.updateSelectiveById(caseInfo);

        result.setIsSuccess(true);
        result.setMessage("成功");
        return result;
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
        return caseInfoBiz.getList(caseInfo, page, limit, isNoFinish);
    }

    /**
     * 获取单个对象
     * 
     * @author 尚
     * @param id
     * @return
     */
    public ObjectRestResponse<JSONObject> get(Integer id) {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();
        CaseInfo caseInfo = this.caseInfoBiz.selectById(id);

        if (BeanUtil.isEmpty(caseInfo)) {
            restResult.setData(new JSONObject());
            return restResult;
        }
        //业务条线code
        String bizListCode = caseInfo.getBizList();

        List<CaseInfo> list = new ArrayList<>();
        list.add(caseInfo);
        
        String sourceType = caseInfo.getSourceType();
        
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

        // 整合办理信息
        List<ExecuteInfo> exeInfoList = executeInfoBiz.getListByCaseInfoId(caseInfo.getId());
        ExecuteInfo executeInfo = new ExecuteInfo();
        if (BeanUtil.isNotEmpty(exeInfoList)) {
            executeInfo = exeInfoList.get(0);
        }

        JSONObject caseInfoJObj = JSONObject.parseObject(JSON.toJSONString(caseInfo));
        // 将业务条线code回填
        caseInfoJObj.put("bizListCode", bizListCode);

        // 整合事件类别
        String eventTypeList = caseInfo.getEventTypeList();
        if (StringUtils.isNotBlank(eventTypeList)) {
            List<EventType> eventTypes = eventTypeMapper.selectByIds(eventTypeList);
            if (BeanUtil.isNotEmpty(eventTypeList)) {
                List<String> eventTypeName =
                    eventTypes.stream().map(o -> o.getTypeName()).distinct()
                        .collect(Collectors.toList());
                caseInfoJObj.put("eventTypeListName", String.join(",", eventTypeName));
            }
        }
        if (StringUtil.isBlank(caseInfoJObj.getString("eventTypeListName"))) {
            caseInfoJObj.put("eventTypeListName", "");
        }

        if (BeanUtil.isNotEmpty(executeInfo)) {
            caseInfoJObj.put("exePerson", executeInfo.getExePerson());
            caseInfoJObj.put("exeFinishTime", executeInfo.getFinishTime());
            caseInfoJObj.put("exeInfoPicture",executeInfo.getPicture());
        }

        // 判断事件是否为巡查上报，如果是，则需要整合巡查上报的图片
        if (environment.getProperty("sourceTypeKeyPatrol").equals(sourceType)) {
            PatrolTask patrolTask = patrolTaskBiz.selectById(Integer.valueOf(caseInfo.getSourceCode()));
            String patrolTaskPicUrl = "";
            if (BeanUtil.isNotEmpty(patrolTask)) {
                Example resExample = new Example(PatrolRes.class);
                Criteria resCriteria = resExample.createCriteria();
                resCriteria.andEqualTo("patrolTaskId", patrolTask.getId());
                List<PatrolRes> patrolRes = patrolResBiz.selectByExample(resExample);
                if (BeanUtil.isNotEmpty(patrolRes)) {
                    List<String> uriList =
                        patrolRes.stream().map(o -> o.getUrl()).distinct().collect(Collectors.toList());
                    patrolTaskPicUrl = String.join(",", uriList);
                }
            }
            caseInfoJObj.put("patrolTaskPicUrl", patrolTaskPicUrl);
        }
        restResult.setData(caseInfoJObj);
        return restResult;
    }

    /**
     * 查询个人待办任务(工作流)
     * 
     * @author 尚
     * @param objs
     *            {"bizData":{}, "procData":{}, "authData":{"procAuthType":"2"},
     *            "variableData":{}, "queryData":{ "isQuery":"false" } }<br/>
     *            如果存在查询条件，则将"isQuery"设为true,并在"queryData"大项内添加查询条件
     * @return
     */
    public TableResultResponse<JSONObject> getUserToDoTasks(JSONObject objs) {
        CaseInfo queryCaseInfo = new CaseInfo();
        JSONObject queryData = objs.getJSONObject("queryData");

        JSONObject bizData = objs.getJSONObject("bizData");
        // 事件工作流的定义代码
        bizData.put("prockey", "comprehensiveManage");
        objs.put("bizData", bizData);

        if ("true".equals(queryData.getString("isQuery"))) {
            queryCaseInfo = JSONObject.parseObject(queryData.toJSONString(), CaseInfo.class);
            if (StringUtils.isNotBlank(queryData.getString("procCtaskname"))) {
                // 是否按进度进行查找(即任务表中·PROC_CTASKNAME·字段)
                bizData = objs.getJSONObject("bizData");
                bizData.put("procCtaskname", queryData.getString("procCtaskname"));
                objs.put("bizData", bizData);
            }
        }

        List<JSONObject> jObjList = new ArrayList<>();

        // 查询待办工作流任务
        PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getUserAllToDoTasks(objs);
        List<WfProcBackBean> list = pageInfo.getList();

        if (list != null && !list.isEmpty()) {
            // 有待办任务
            return queryAssist(queryCaseInfo, queryData, jObjList, pageInfo, objs);
        } else {
            // 无待办任务
            return new TableResultResponse<>(0, jObjList);
        }
    }

    /**
     * 查询所有待办任务(工作流)
     * 
     * @author chenshuai
     * @param objs
     * @return
     */
    public TableResultResponse<JSONObject> getAllToDoTasks(JSONObject objs) {

        CaseInfo queryCaseInfo = new CaseInfo();

        JSONObject queryData = objs.getJSONObject("queryData");

        JSONObject bizData = objs.getJSONObject("bizData");
        // 事件工作流的定义代码
        bizData.put("prockey", "comprehensiveManage");
        objs.put("bizData", bizData);

        if ("true".equals(queryData.getString("isQuery"))) {
            queryCaseInfo = JSONObject.parseObject(queryData.toJSONString(), CaseInfo.class);
            if (StringUtils.isNotBlank(queryData.getString("procCtaskname"))) {
                // 是否按进度进行查找(即任务表中·PROC_CTASKNAME·字段)
                bizData = objs.getJSONObject("bizData");
                bizData.put("procCtaskname", queryData.getString("procCtaskname"));
                objs.put("bizData", bizData);
            }
        }

        List<JSONObject> jObjList = new ArrayList<>();

        // 查询待办工作流任务
        PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getAllToDoTasks(objs);
        List<WfProcBackBean> list = pageInfo.getList();

        if (list != null && !list.isEmpty()) {
            // 有待办任务
            TableResultResponse<JSONObject> restResult =
                queryAssist(queryCaseInfo, queryData, jObjList, pageInfo, objs);
            return restResult;
        } else {
            // 无待办任务
            return new TableResultResponse<>(0, jObjList);
        }
    }

    /**
     * 查询所有任务
     * 
     * @author chenshuai
     * @param objs
     * @return
     */
    public TableResultResponse<JSONObject> getAllTasks(JSONObject objs) {
        CaseInfo queryCaseInfo = new CaseInfo();
        JSONObject queryData = objs.getJSONObject("queryData");

        JSONObject bizData = objs.getJSONObject("bizData");
        // 事件工作流的定义代码
        bizData.put("prockey", "comprehensiveManage");
        if ("true".equals(queryData.getString("isQuery"))) {
            queryCaseInfo = JSONObject.parseObject(queryData.toJSONString(), CaseInfo.class);
            if (StringUtils.isNotBlank(queryData.getString("procCtaskname"))) {
                // 是否按进度进行查找(即任务表中·PROC_CTASKNAME·字段)
                if (!CaseInfo.FINISHED_STATE_FINISH.equals(queryData.getString("procCtaskname"))
                    && !CaseInfo.FINISHED_STATE_STOP.equals(queryData.getString("procCtaskname"))) {
                    bizData.put("procCtaskname", queryData.getString("procCtaskname"));
                }
                
                /*
                 * 当按状态进行查询时，待结案节点比较特殊
                 * 1 流程走完后，状态会停留在待结案，所以无论查询待结案还是已结案，都会将该结节查出，对结果集造成混淆
                 * 2 对查询条件进行处理，如果查询的是待结案的状态，则在与业务数据进行整合时，只整合未完成的数据，去除
                 * 已完成数据对待结案查询的干扰
                 */
                if ("待结案".equals(queryData.getString("procCtaskname"))) {
                    queryData.put("toFinish", CaseInfo.FINISHED_STATE_TODO);
                }
            }
        }
        objs.put("bizData", bizData);

        List<JSONObject> jObjList = new ArrayList<>();

        // 查询待办工作流任务
        PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getAllTasks(objs);
        List<WfProcBackBean> list = pageInfo.getList();

        if (list != null && !list.isEmpty()) {
            // 有待办任务
            TableResultResponse<JSONObject> restResult =
                queryAssist(queryCaseInfo, queryData, jObjList, pageInfo, objs);
            return restResult;
        } else {
            // 无待办任务
            return new TableResultResponse<>(0, jObjList);
        }
    }

    private TableResultResponse<JSONObject> queryAssist(CaseInfo queryCaseInfo, JSONObject queryData,
        List<JSONObject> jObjList, PageInfo<WfProcBackBean> pageInfo, JSONObject objs) {
        List<WfProcBackBean> procBackBeanList = pageInfo.getList();

        // 封装工作流任务
        Map<String, WfProcBackBean> wfProcBackBean_ID_Entity_Map = new HashMap<>();
        for (WfProcBackBean wfProcBackBean : procBackBeanList) {
            wfProcBackBean_ID_Entity_Map.put(wfProcBackBean.getProcBizid(), wfProcBackBean);
        }

        Set<Integer> bizIds = new HashSet<>();
        Set<String> eventTypeIdStrSet = new HashSet<>();

        Set<String> rootBizIdSet = new HashSet<>();

        if (procBackBeanList != null && !procBackBeanList.isEmpty()) {
            for (int i = 0; i < procBackBeanList.size(); i++) {
                WfProcBackBean wfProcBackBean = procBackBeanList.get(i);
                try {
                    bizIds.add(Integer.valueOf(wfProcBackBean.getProcBizid()));
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }

        // 查询与工作流任务对应的业务
        TableResultResponse<CaseInfo> tableResult = caseInfoBiz.getList(queryCaseInfo, bizIds, queryData);

        List<CaseInfo> caseInfoList = tableResult.getData().getRows();

        for (CaseInfo caseInfo : caseInfoList) {

            rootBizIdSet.add(caseInfo.getBizList());
            rootBizIdSet.add(caseInfo.getSourceType());
            rootBizIdSet.add(caseInfo.getCaseLevel());
            eventTypeIdStrSet.add(caseInfo.getEventTypeList());
        }

        /*
         * 查询业务条线，查询事件来源，查询事件级别
         */
        Map<String, String> rootBizList = new HashMap<>();
        if (rootBizIdSet != null && !rootBizIdSet.isEmpty()) {
            rootBizList = dictFeign.getByCodeIn(String.join(",", rootBizIdSet));
        }

        // 查询事件类别
        Map<String, String> eventType_ID_NAME_Map = new HashMap<>();
        if (eventTypeIdStrSet != null && !eventTypeIdStrSet.isEmpty()) {
            List<EventType> eventTypeList;
            eventTypeList = eventTypeMapper.selectByIds(String.join(",", eventTypeIdStrSet));
            List<String> eventTypeNameList = new ArrayList<>();
            for (EventType eventType : eventTypeList) {
                if (StringUtils.isNotBlank(eventType.getTypeName())) {
                    eventTypeNameList.add(eventType.getTypeName());
                }
                eventType_ID_NAME_Map.put(String.valueOf(eventType.getId()), eventType.getTypeName());
            }
        }
        
        for (CaseInfo caseInfo : caseInfoList) {

            JSONObject wfJObject = JSONObject.parseObject(JSON.toJSONString(caseInfo));

            wfJObject.put("bizListName", getRootBizTypeName(caseInfo.getBizList(), rootBizList));
            wfJObject.put("eventTypeListName", getEventTypeName(eventType_ID_NAME_Map, caseInfo.getEventTypeList()));
            wfJObject.put("sourceTypeName", getRootBizTypeName(caseInfo.getSourceType(), rootBizList));
            wfJObject.put("caseLevelName", getRootBizTypeName(caseInfo.getCaseLevel(), rootBizList));

            wfJObject.put("isUrge", "0".equals(caseInfo.getIsUrge()) ? false : true);
            wfJObject.put("isSupervise", "0".equals(caseInfo.getIsSupervise()) ? false : true);

            Date deadLine = caseInfo.getDeadLine();
            Date finishTime = caseInfo.getFinishTime();
            boolean isOvertime = false;
            if (deadLine != null) {
                if (CaseInfo.FINISHED_STATE_TODO.equals(caseInfo.getIsFinished())) {
                    // 任务未完成判断是否超时
                    isOvertime = deadLine.compareTo(new Date()) > 0 ? false : true;
                } else {
                    // 完成任务判断是否超时
                    isOvertime = deadLine.compareTo(finishTime) > 0 ? false : true;
                }
            }
            // 是否超时
            wfJObject.put("isOvertime", isOvertime);

            WfProcBackBean wfProcBackBean = wfProcBackBean_ID_Entity_Map.get(String.valueOf(caseInfo.getId()));
            if (wfProcBackBean != null) {
                wfJObject.put("procCtaskname", wfProcBackBean.getProcCtaskname());
                wfJObject.put("procInstId", wfProcBackBean.getProcInstId());
                wfJObject.put("procBizid", wfProcBackBean.getProcBizid());
                wfJObject.put("procCtaskId", wfProcBackBean.getProcCtaskId());
                wfJObject.put("procTaskStatusName", wfProcBackBean.getProcTaskStatusName());
                wfJObject.put("procTaskStatus", wfProcBackBean.getProcTaskStatus());
                wfJObject.put("procCtaskcode", wfProcBackBean.getProcCtaskcode());
                // 在返回列表里添加任务到达时间与处理时间
                wfJObject.put("procTaskCommittime",wfProcBackBean.getProcTaskCommittime());
                wfJObject.put("procTaskAssigntime",wfProcBackBean.getProcTaskAssigntime());
            }

            wfJObject.put("caseInfoId", caseInfo.getId());
            if (CaseInfo.FINISHED_STATE_FINISH.equals(caseInfo.getIsFinished())) {
                wfJObject.put("procCtaskname", "已结案");
            }

            if (CaseInfo.FINISHED_STATE_STOP.equals(caseInfo.getIsFinished())) {
                wfJObject.put("procCtaskname", wfJObject.getString("procCtaskname") + "(已终止)");
            }

            if("1".equals(caseInfo.getIsDuplicate())){
                CaseInfo dupCaseInfo = caseInfoBiz.selectById(caseInfo.getDuplicateWith());
                if (BeanUtil.isNotEmpty(dupCaseInfo)) {
                    wfJObject.put("procCtaskname", "重复事件(" + dupCaseInfo.getCaseCode() + ")");
                } else {
                    wfJObject.put("procCtaskname", "重复事件");
                }
            }

            jObjList.add(wfJObject);
        }

        return new TableResultResponse<>(tableResult.getData().getTotal(), jObjList);
    }

    private String getEventTypeName(Map<String, String> eventType_ID_NAME_Map, String eventTypeList) {
        if (eventTypeList != null) {
            List<String> nameList = new ArrayList<>();
            String[] split = eventTypeList.split(",");
            for (String string : split) {

                if (StringUtils.isNotBlank(eventType_ID_NAME_Map.get(string))) {
                    nameList.add(eventType_ID_NAME_Map.get(string));
                }
            }

            return String.join(",", nameList);
        }
        return "";
    }

    private void sourceTypeHistoryAssist(JSONObject wfJObject, CaseInfo caseInfo) {

        // 查询登记历史
        // 业务数据库中字典数据用字典表的code去表示，因为caseInfo内保存的就是code，无需再进行id换code操作
        String key = caseInfo.getSourceType();

        JSONObject resultJObjct = new JSONObject();
        List<String> zhaiyaoList = new ArrayList<>();
        String reportPersonId = "";
        switch (key) {
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_12345:
                // 市长热线
                MayorHotline mayorHotline = mayorHotlineBiz.selectById(Integer.valueOf(caseInfo.getSourceCode()));
                // 对查到的来源进行非空判断
                if (mayorHotline != null) {
                    reportPersonId = mayorHotline.getCrtUserId();
                    resultJObjct = JSONObject.parseObject(JSON.toJSONString(mayorHotline));
                    resultJObjct.put("eventSourceType", "市长热线12345");
                    resultJObjct.put("sourceCode", mayorHotline.getHotlnCode());
                    //添加主办人姓名
                    resultJObjct.put("crtUserName", mayorHotline.getCrtUserName());

                    zhaiyaoList.add(mayorHotline.getHotlnType());
                    zhaiyaoList.add(mayorHotline.getHotlnSubType());
                    zhaiyaoList.add(mayorHotline.getAppealTel());
                    if (mayorHotline.getReplyDatetime() != null) {
                        zhaiyaoList.add(DateUtil.dateFromDateToStr(mayorHotline.getReplyDatetime(), "yyyy-MM-dd HH:mm:ss"));
                    }
                }
                break;
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CONSENSUS:
                // 舆情
                PublicOpinion poinion = publicOpinionBiz.selectById(Integer.valueOf(caseInfo.getSourceCode()));

                // 对查到的数据进行非空判断
                if (poinion != null) {
                    reportPersonId = poinion.getCrtUserId();

                    // 查询字典里对应的值
                    List<String> dictIdList = new ArrayList<>();
                    dictIdList.add(poinion.getOpinType());
                    dictIdList.add(poinion.getOpinLevel());
                    resultJObjct = JSONObject.parseObject(JSON.toJSONString(poinion));
                    resultJObjct.put("eventSourceType", "舆情");
                    resultJObjct.put("sourceCode", poinion.getOpinCode());
                    //添加主办人姓名
                    resultJObjct.put("crtUserName", poinion.getCrtUserName());

                    Map<String, String> dictValueMap = dictFeign.getByCodeIn(String.join(",", dictIdList));
                    if (dictValueMap != null && !dictValueMap.isEmpty()) {
                        zhaiyaoList.add(dictValueMap.get(poinion.getOpinType()));
                        zhaiyaoList.add(dictValueMap.get(poinion.getOpinLevel()));
                        zhaiyaoList.add(poinion.getOpinPort());
                    }
                }
                break;
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_LEADER:
                // 领导交办
                LeadershipAssign leadershipAssign =
                    leadershipAssignBiz.selectById(Integer.valueOf(caseInfo.getSourceCode()));
                // 对查到的数据进行非空判断
                if(leadershipAssign!=null){
                    resultJObjct = JSONObject.parseObject(JSON.toJSONString(leadershipAssign));
                    resultJObjct.put("eventSourceType", "领导交办");
                    resultJObjct.put("sourceCode", leadershipAssign.getTaskCode());
                    //添加主办人姓名
                    resultJObjct.put("crtUserName", leadershipAssign.getCrtUserName());

                    reportPersonId = leadershipAssign.getCrtUserId();

                    // 涉及监管对象名称
                    String regulaObjList = leadershipAssign.getRegulaObjList();
                    List<RegulaObject> regulaObjectList = new ArrayList<>();
                    if (StringUtils.isNotBlank(regulaObjList)) {
                        regulaObjectList = regulaObjectMapper.selectByIds(regulaObjList);
                    }
                    List<String> regulaObjNameList = new ArrayList<>();
                    if (regulaObjectList != null && !regulaObjectList.isEmpty()) {
                        for (RegulaObject regulaObject : regulaObjectList) {
                            regulaObjNameList.add(regulaObject.getObjName());
                        }
                    }

                    zhaiyaoList.add(String.join(",", leadershipAssign.getTaskLeader()));
                    zhaiyaoList.add(String.join(",", regulaObjNameList));
                }
                break;
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK:
                // 巡查上报
                ObjectRestResponse<PatrolTask> patrolTaskResult =
                    patreolTaskService.get(Integer.valueOf(caseInfo.getSourceCode()));

                // By尚
                PatrolTask patrolTask = patrolTaskResult == null ? new PatrolTask() : patrolTaskResult.getData();

                resultJObjct = JSONObject.parseObject(JSON.toJSONString(patrolTask));
                resultJObjct.put("eventSourceType", "巡查上报");
                resultJObjct.put("sourceCode", patrolTask.getPatrolCode());
                //添加主办人姓名
                resultJObjct.put("crtUserName", patrolTask.getCrtUserName());

                // 查询现场检查图片
                List<PatrolRes> patrolResList = patrolResBiz.getByPatrolTaskId(patrolTask.getId());
                if (BeanUtil.isNotEmpty(patrolResList)) {
                    List<String> urls =
                        patrolResList.stream().map(o -> o.getUrl()).distinct()
                            .collect(Collectors.toList());
                    resultJObjct.put("url", String.join(",", urls));
                } else {
                    resultJObjct.put("url", null);
                }

                reportPersonId = patrolTask.getCrtUserId();
                break;
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_COMMAND_LINE:
                // 指挥中心热线
                JSONObject centerHotlineJObj =
                    commandCenterHotlineService.selectById(Integer.valueOf(caseInfo.getSourceCode()));

                resultJObjct = centerHotlineJObj;

                // 对查询到的数据进行非空判断
                if (resultJObjct != null) {
                    resultJObjct.put("eventSourceType", "指挥中心热线");
                    resultJObjct.put("sourceCode", centerHotlineJObj.getString("hotlnCode"));
                    //添加主办人姓名
                    resultJObjct.put("crtUserName", centerHotlineJObj.getString("crtUserName"));

                    reportPersonId = centerHotlineJObj.getString("crtUserId");

                    zhaiyaoList
                            .add(centerHotlineJObj.getString("bizType") == null ? "" : centerHotlineJObj.getString("bizType"));
                    zhaiyaoList.add(centerHotlineJObj.getString("eventTypeName") == null ? ""
                            : centerHotlineJObj.getString("eventTypeName"));
                    zhaiyaoList.add(
                            centerHotlineJObj.getString("appealType") == null ? "" : centerHotlineJObj.getString("appealType"));
                    zhaiyaoList.add(
                            centerHotlineJObj.getString("appealTel") == null ? "" : centerHotlineJObj.getString("appealTel"));
                    if (centerHotlineJObj.getDate("appealDatetime") != null) {
                        zhaiyaoList.add(
                                DateUtil.dateFromDateToStr(centerHotlineJObj.getDate("appealDatetime"), "yyyy-MM-dd HH:mm:ss"));
                    }
                }
                break;
            default:
                break;
        }

        for (int i = 0; i < zhaiyaoList.size(); i++) {
            String zhy = zhaiyaoList.get(i);
            if (StringUtils.isBlank(zhy) || "null".equals(zhy)) {
                zhaiyaoList.remove(i);
                i--;
            }
        }
        resultJObjct.put("zhaiyao", String.join("-", zhaiyaoList));
        // 上报人
        JSONArray userDetailJArray = adminFeign.getInfoByUserIds(String.join(",", reportPersonId));

        JSONObject userMap = null;
        if (BeanUtil.isNotEmpty(userDetailJArray)) {
            userMap = userDetailJArray.getJSONObject(0);
        } else {
            userMap = new JSONObject();
        }

        resultJObjct.put("crtUserTel",
            userMap.getString("mobilePhone") == null ? "" : userMap.getString("mobilePhone"));
        resultJObjct.put("groupName",
            userMap.getString("groupName") == null ? "" : userMap.getString("groupName"));
        resultJObjct.put("deptName",
            userMap.getString("deptName") == null ? "" : userMap.getString("deptName"));
        resultJObjct.put("potitionName",
            userMap.getString("positionName") == null ? "" : userMap.getString("positionName"));

        wfJObject.put("sourceTypeHistory", resultJObjct);
    }

    private String getRootBizTypeName(String ids, Map<String, String> rootBizList) {
        if (StringUtils.isNotBlank(ids)) {
            String[] split = ids.split(",");
            List<String> nameList = new ArrayList<>();
            for (String string : split) {
                nameList.add(rootBizList.get(string));
            }

            return String.join(",", nameList);
        }
        return "";
    }

    /**
     * 
     * @author 尚
     * @param objs
     */
    @Transactional
    public Result<Void> completeProcess(@RequestBody JSONObject objs) {
        log.debug("进行事件审批，参数结构为："+objs.toString());
        Result<Void> result=new Result<>();

        /*
         * 该方法改用手动提交事务
         * 签收操作与审批操作是两个动作
         * 需要将任务签收后才能完成后续的审批
         */
        DefaultTransactionDefinition def=new DefaultTransactionDefinition();

        // 进行签收操作
        claimBeforeComplete(def,objs);

        // 开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status=platformTransactionManager.getTransaction(def);

        try {
            /*
             * ===============更新业务数据===================开始=============
             */
            JSONObject bizDataJObject = objs.getJSONObject("bizData");
            JSONObject caseInfoJObj = bizDataJObject.getJSONObject("caseInfo");// 立案单参数
            JSONObject variableDataJObject = objs.getJSONObject("variableData");// 流程参数
            JSONObject concernedPersonJObj = bizDataJObject.getJSONObject("concernedPerson");// 当事人(个人信息)
            JSONObject concernedCompanyJObj = bizDataJObject.getJSONObject("concernedCompany");// 当事人(单位)

            CaseInfo caseInfo = new CaseInfo();//要更新到数据库里的实例信息
            if (caseInfoJObj != null) {
                caseInfo = JSON.parseObject(caseInfoJObj.toJSONString(), CaseInfo.class);
            }
            caseInfo.setId(Integer.valueOf(bizDataJObject.getString("procBizId")));

            // 获取流程走向，以判断流程是否结束
            String flowDirection = variableDataJObject.getString("flowDirection");

            // 判断流向是否走向部门处理中
            if (environment.getProperty("isCheckProcessingAuth").equals(flowDirection)) {
                String deptId = objs.getJSONObject("authData").getString("procDeptId");
                if (StringUtils.isBlank(deptId)) {
                    throw new BizException("请指定部门ID");
                }
                List<JSONObject> authoritiesByDept = adminFeign.getAuthoritiesByDept(deptId);
                if (BeanUtil.isNotEmpty(authoritiesByDept)) {
                    List<String> codeList =
                        authoritiesByDept.stream().map(o -> o.getString("code")).distinct()
                            .collect(Collectors.toList());
                    if (!codeList.contains(environment.getProperty("wfTodoList"))) {
                        result.setMessage("该部门尚无权限处理该事件，请更换部门。");
                        result.setIsSuccess(false);
                        return result;
                    }
                } else {
                    // 如果authoritiesByDept为空，也认为是无处理权限
                    result.setMessage("该部门尚无权限处理该事件，请更换部门。");
                    result.setIsSuccess(false);
                    return result;
                }
            }

            // 判断流向是否走向立案核查及待结案核查
            String isCheckAreaGridAuth = environment.getProperty("isCheckAreaGridAuth");
            List<String> isCheckAreaGridAuthList = Arrays.asList(isCheckAreaGridAuth.split(","));
            if(isCheckAreaGridAuthList.contains(flowDirection)){
                // 表明需要进行网格员认证
                String gridId = caseInfoJObj.getString("grid");

                if(StringUtils.isEmpty(gridId)){
                    throw new BizException("请指定网格");
                }

                List<AreaGridMember> areaGridMemberList =
                    areaGridMemberBiz.getByGridIds(new HashSet<>(Arrays.asList(gridId.split(","))));
                if (BeanUtil.isNotEmpty(areaGridMemberList)) {
                    // 如果有网格员配置，则进行下一步验证
                    if (areaGridMemberList.size() == 1
                        && areaGridMemberList.get(0).getGridMember() == null) {
                        // 与指定网格对应的只有一条网格员记录，并且gridMember字段为null
                        result.setMessage("该网格下未配备网格员");
                        result.setIsSuccess(false);
                        return result;
                    }
                } else {
                    result.setMessage("该网格下未配备网格员");
                    result.setIsSuccess(false);
                    return result;
                }
            }

            if (Constances.ProcFlowWork.TOFINISHWORKFLOW.equals(flowDirection)) {
                /*
                 * 任务已走向结束<br/> 去执行相应业务应该完成的操作<br/> 1 立案单isFinished：1<br/> 2 来源各变化
                 */
                log.info("该请求流向【结束】，流程即将结束。");
                // 查询数据库中的caseInfo,以确定与caseInfo相对应的登记表那条记录的ID
                CaseInfo caseInfoInDB = caseInfoBiz.selectById(Integer.valueOf(bizDataJObject.getString("procBizId")));

                caseInfo.setSourceCode(caseInfoInDB.getSourceCode());
                caseInfo.setIsFinished(CaseInfo.FINISHED_STATE_FINISH);
                caseInfo.setFinishTime(new Date());// 结案时间
                caseInfo.setSourceType(caseInfoInDB.getSourceType());
                gotoFinishSource(caseInfo, false);// 去更新事件来源的状态
            } else if (Constances.ProcFlowWork.TOFINISHWORKFLOW_DUP.equals(flowDirection)) {
                // 因重覆而结束
                log.info("该请求流向【结束】（因事件重复），流程即将结束。");
                // 查询数据库中的caseInfo,以确定与caseInfo相对应的登记表那条记录的ID
                CaseInfo caseInfoInDB = caseInfoBiz.selectById(Integer.valueOf(bizDataJObject.getString("procBizId")));

                caseInfo.setSourceCode(caseInfoInDB.getSourceCode());
                caseInfo.setIsFinished(CaseInfo.FINISHED_STATE_FINISH);
                caseInfo.setIsDuplicate("1");
                caseInfo.setFinishTime(new Date());// 结案时间
                caseInfo.setSourceType(caseInfoInDB.getSourceType());
                gotoFinishSource(caseInfo, false);// 去更新事件来源的状态
            }

            // 判断是否有当事人信息concernedPerson
            if (concernedPersonJObj != null) {
                /*
                 * 该处逻辑有问题，每当进行一次受理员审批时，都会向数据库添加一条当事人的记录
                 * 1-> 如果存在与事件对应的当事人，则更新操作
                 * 2-> 如果不存在与事件对应的当事人，则进行插入操作
                 */
                ConcernedPerson concernedPerson =
                    JSON.parseObject(concernedPersonJObj.toJSONString(), ConcernedPerson.class);

                //判断当前事件是否已关联了当事人
                CaseInfo caseInfoInDB = caseInfoBiz.selectById(caseInfo.getId());
                if (BeanUtil.isNotEmpty(caseInfoInDB.getConcernedPerson())
                    && Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON
                        .equals(caseInfoInDB.getConcernedType())) {
                    // 已经关联了类别为个人的当事人,应执行更新操作
                    log.debug("事件审批，已存在以个人形式的当事人，当事人ID为" + caseInfoInDB.getConcernedPerson());
                    concernedPerson.setId(Integer.valueOf(caseInfoInDB.getConcernedPerson()));
                    concernedPersonBiz.updateSelectiveById(concernedPerson);
                } else {
                    concernedPersonBiz.insertSelective(concernedPerson);
                    caseInfo.setConcernedPerson(String.valueOf(concernedPerson.getId()));
                }
                // 当事人类型为"root_biz_concernedT_person"
                caseInfo.setConcernedType(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON);//
            }

            // 判断是否有当事人(单位)信息
            if (concernedCompanyJObj != null) {
                /*
                 * 该处逻辑有问题，每当进行一次受理员审批时，都会向数据库添加一条当事人的记录
                 * 1-> 如果存在与事件对应的当事人，则更新操作
                 * 2-> 如果不存在与事件对应的当事人，则进行插入操作
                 */
                ConcernedCompany concernedCompany =
                    JSONObject.parseObject(concernedCompanyJObj.toJSONString(), ConcernedCompany.class);

                CaseInfo caseInfoInDB = caseInfoBiz.selectById(caseInfo.getId());
                if (BeanUtil.isNotEmpty(caseInfoInDB.getConcernedPerson())
                    && Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG
                        .equals(caseInfoInDB.getConcernedType())) {
                    // 已经关联了类别为单位的当事人,应执行更新操作
                    log.debug("事件审批，已存在以单位形式的当事人，当事人ID为" + caseInfoInDB.getConcernedPerson());
                    concernedCompany.setId(Integer.valueOf(caseInfoInDB.getConcernedPerson()));
                    concernedCompanyBiz.updateSelectiveById(concernedCompany);
                }else{
                    concernedCompanyBiz.insertSelective(concernedCompany);
                    caseInfo.setConcernedPerson(String.valueOf(concernedCompany.getId()));
                }

                // 当事人类型为"root_biz_concernedT_org"
                caseInfo.setConcernedType(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG);//
            }

            // 判断 是否有处理情况信息
            /*
             * 该处逻辑有问题，每当进行一次受理员审批时，都会向数据库添加一条处理情况的记录
             * 1-> 如果存在与事件对应的处理情况，则更新操作
             * 2-> 如果不存在与事件对应的处理情况，则进行插入操作
             */
            JSONObject executeInfoJObj = bizDataJObject.getJSONObject("executeInfoJObj");
            if (executeInfoJObj != null) {
                Integer procBizId = bizDataJObject.getInteger("procBizId");

                ExecuteInfo executeInfoForQuery = new ExecuteInfo();
                executeInfoForQuery.setCaseId(procBizId);
                List<ExecuteInfo> executeInfosInDB = executeInfoBiz.selectList(executeInfoForQuery);
                if (BeanUtil.isNotEmpty(executeInfosInDB)) {
                    // 存在处理情况
                    ExecuteInfo executeInfoForUpdate =
                        JSON.parseObject(executeInfoJObj.toJSONString(), ExecuteInfo.class);
                    executeInfoForUpdate.setId(executeInfosInDB.get(0).getId());
                    executeInfoForUpdate.setDepartment(BaseContextHandler.getDepartID());
                    // 对请求参数中的图片做处理，图片地址可能前后可能会带有空格
                    _checkPictureInCaseInfo(null, executeInfoForUpdate);
                    executeInfoBiz.updateSelectiveById(executeInfoForUpdate);
                } else {
                    ExecuteInfo executeInfoForInsert =
                        JSON.parseObject(executeInfoJObj.toJSONString(), ExecuteInfo.class);
                    executeInfoForInsert.setCaseId(caseInfo.getId());
                    executeInfoForInsert.setDepartment(BaseContextHandler.getDepartID());
                    // 对请求参数中的图片做处理，图片地址可能前后可能会带有空格
                    _checkPictureInCaseInfo(null, executeInfoForInsert);
                    executeInfoBiz.insertSelective(executeInfoForInsert);
                }

            }

            /*
             * 判断是否有立案核查及待结案核查审批内容
             * 立案核查审批内容与待结案核查审批内容在业务表与工作流表都有，且内容一致
             */
            _checkProcApproOpinion(objs, caseInfo);

            // 对请求参数中的图片做处理，图片地址可能前后可能会带有空格
            _checkPictureInCaseInfo(caseInfo,null);

            // 更新业务数据(caseInfo)
            caseInfoBiz.updateSelectiveById(caseInfo);

            // 完成已签收的任务，将工作流向下推进
            wfProcTaskService.completeProcessInstance(objs);

            platformTransactionManager.commit(status);
        } catch (Exception e) {
            platformTransactionManager.rollback(status);
            e.printStackTrace();
        }
        result.setIsSuccess(true);
        return result;
    }

    /**
     * 在审批前签收任务，该任务事务会完成提交操作
     * 
     * @param def
     * @param objs
     */
    private void claimBeforeComplete(DefaultTransactionDefinition def, JSONObject objs) {
        // 开启新事务
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 获得事务状态
        TransactionStatus status = platformTransactionManager.getTransaction(def);

        try {
            JSONObject bizData = objs.getJSONObject("bizData");
            JSONObject procData = objs.getJSONObject("procData");
            JSONObject authData = objs.getJSONObject("authData");
            JSONObject variableData = objs.getJSONObject("variableData");

            JSONObject objsForClaim = new JSONObject();

            // 请求参数会在签收操作中发生变化，为避免影响下面审批操作重新生成去签收的请求结构
            JSONObject objsForClaimBizData = new JSONObject();
            JSONObject objsForClaimProcData = new JSONObject();
            JSONObject objsForClaimAuthData = new JSONObject();
            JSONObject objsForClaimVariableData = new JSONObject();

            objsForClaimBizData.put("procBizId", bizData.getString("procBizId"));
            objsForClaimBizData.put("procTaskId", procData.getString("procTaskId"));
            objsForClaimBizData.put("procOrgCode", bizData.getString("procOrgCode"));
            objsForClaimProcData.put("procTaskId", procData.getString("procTaskId"));
            objsForClaimAuthData.put("procAuthType", authData.getString("procAuthType"));
            objsForClaimVariableData.put("taskUrl", variableData.getString("taskUrl"));
            objsForClaimVariableData.put("procApprStatus",
                variableData.getString("procApprStatus"));

            objsForClaim.put("bizData", objsForClaimBizData);
            objsForClaim.put("procData", objsForClaimProcData);
            objsForClaim.put("authData", objsForClaimAuthData);
            objsForClaim.put("variableData", objsForClaimVariableData);

            JSONObject jsonObject = wfMonitorService.selectProcByTaskId(objsForClaim);
            if(Constants.FlowStatus.TASK01.getRetCode().equals(jsonObject.getString("procTaskStatus"))){
                // 该流程处于待签收状态
                objsForClaim.getJSONObject("bizData").remove("procTaskId");
                wfProcTaskService.claimProcessInstance(objsForClaim);
            }

            platformTransactionManager.commit(status);
        } catch (Exception e) {
            platformTransactionManager.rollback(status);
            e.printStackTrace();
        }
    }

    private void _checkPictureInCaseInfo(CaseInfo caseInfo,ExecuteInfo executeInfo) {
        /*
         * 1 检查图片checkPic
         * 2 结案核查图片finish_check_pic
         * 3 办理图片
         */
        try {
            if (BeanUtil.isNotEmpty(caseInfo)) {
                if (StringUtils.isNotBlank(caseInfo.getCheckPic())) {
                    String[] splits = caseInfo.getCheckPic().split(",");
                    List<String> checkPicList = new ArrayList<>();
                    for (String split : splits) {
                        checkPicList.add(StringUtils.trim(split));
                    }
                    caseInfo.setCheckPic(String.join(",", checkPicList));
                }
                if (StringUtils.isNotBlank(caseInfo.getFinishCheckPic())) {
                    String[] splits = caseInfo.getFinishCheckPic().split(",");
                    List<String> checkPicList = new ArrayList<>();
                    for (String split : splits) {
                        checkPicList.add(StringUtils.trim(split));
                    }
                    caseInfo.setFinishCheckPic(String.join(",", checkPicList));
                }
            }

            if (BeanUtil.isNotEmpty(executeInfo)) {
                if (StringUtils.isNotBlank(executeInfo.getPicture())) {
                    String[] splits = executeInfo.getPicture().split(",");
                    List<String> pictures = new ArrayList<>();
                    for (String split : splits) {
                        pictures.add(StringUtils.trim(split));
                    }
                    executeInfo.setPicture(String.join(",", pictures));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 检查是否有审批意见
     * @param objs
     * @param caseInfo
     */
    private void _checkProcApproOpinion(JSONObject objs, CaseInfo caseInfo) {
        // 立案核查
        JSONObject variableDataJObj = objs.getJSONObject("variableData");

        if (StringUtils.isNotBlank(caseInfo.getCheckOpinion())) {
            // 说明有立案核查信息
            if (StringUtils.isBlank(variableDataJObj.getString("procApprOpinion"))) {
                // 业务中有立案核查信息，但工作流请求参数中无立案核查信息
                variableDataJObj.put("procApprOpinion", caseInfo.getCheckOpinion());
            }
        }
        // 待结案核查
        if (StringUtils.isNotBlank(caseInfo.getFinishCheckOpinion())) {
            // 说明有立案核查信息
            if (StringUtils.isBlank(variableDataJObj.getString("procApprOpinion"))) {
                // 业务中有立案核查信息，但工作流请求参数中无立案核查信息
                variableDataJObj.put("procApprOpinion", caseInfo.getFinishCheckOpinion());
            }
        }

        // 将添加审批意见的variableData重新放回objs
        objs.put("variableData", variableDataJObj);
    }

    /**
     * 事件来源走向结束(终止或正常结束)的处理方法
     * 
     * @author 尚
     * @param caseInfo
     *            与事件来源对应的立案单
     * @param isTermination
     *            true:终止操作|false:正常结束操作
     */
    private void gotoFinishSource(CaseInfo caseInfo, boolean isTermination) {
        // 业务数据库中字典数据用字典表的code去表示，因为caseInfo内保存的就是code，无需再进行id换code操作
        String key = caseInfo.getSourceType();

        JSONObject assist = new JSONObject();
        switch (key) {
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_12345:
                // 市长热线
                if (isTermination) {
                    // 事件终止操作
                    log.info("更新市长热线事件为【已中止】");
                    assist =
                        assist(caseInfo.getSourceCode(), Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_STOP);
                } else {
                    log.info("更新市长热线事件为【已处理】");
                    assist =
                        assist(caseInfo.getSourceCode(), Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DONE);
                }
                MayorHotline mayorHotline = JSON.parseObject(JSON.toJSONString(assist), MayorHotline.class);
                mayorHotlineBiz.updateSelectiveById(mayorHotline);
                break;
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CONSENSUS:
                // 舆情
                if (isTermination) {
                    // 事件终止操作
                    assist = assist(caseInfo.getSourceCode(), Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_STOP);
                    log.info("更新舆情事件为【已中止】");
                } else {
                    log.info("更新舆情事件为【已完成】");
                    assist =
                        assist(caseInfo.getSourceCode(), Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_FINISH);
                }
                PublicOpinion publicOpinion = JSON.parseObject(assist.toJSONString(), PublicOpinion.class);
                publicOpinionBiz.updateSelectiveById(publicOpinion);
                break;
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_LEADER:
                // 领导交办
                if (isTermination) {
                    // 事件终止操作
                    log.info("更新领导交办事件为【已中止】");
                    assist = assist(caseInfo.getSourceCode(), Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_STOP);
                } else {
                    log.info("更新领导交办事件为【已完成】");
                    assist = assist(caseInfo.getSourceCode(), Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_FINISH);
                }
                LeadershipAssign leadershipAssign = JSON.parseObject(assist.toJSONString(), LeadershipAssign.class);
                leadershipAssignBiz.updateSelectiveById(leadershipAssign);
                break;
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK:
                // 巡查上报
                PatrolTask patrolTask = new PatrolTask();
                patrolTask.setId(Integer.valueOf(caseInfo.getSourceCode()));
                if (isTermination) {
                    log.info("更新巡查上报事件为【已中止】");
                    // 事件终止操作
                    patrolTask.setStatus(Constances.PartolTaskStatus.ROOT_BIZ_PARTOLTASKT_STOP);
                } else {
                    log.info("更新巡查上报事件为【已完成】");
                    patrolTask.setStatus(Constances.PartolTaskStatus.ROOT_BIZ_PARTOLTASKT_FINISH);
                }
                patrolTaskBiz.updateSelectiveById(patrolTask);
                break;
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_COMMAND_LINE:
                // 指挥中心热线
                if (isTermination) {
                    // 事件终止操作
                    log.info("指挥中心热线事件【已中止】");
                    assist =
                        assist(caseInfo.getSourceCode(), Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_STOP);
                } else {
                    log.info("指挥中心热线事件【已完成】");
                    assist =
                        assist(caseInfo.getSourceCode(), Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DONE);
                }
                CommandCenterHotline commandCenterHotline =
                    JSON.parseObject(assist.toJSONString(), CommandCenterHotline.class);
                commandCenterHotlineBiz.updateSelectiveById(commandCenterHotline);
                break;
            default:
                break;
        }
    }

    /**
     * 根据事件来源ID及处理状态（code表示），返回一个包含ID及处理状态(ID表示)在内的JSONObject
     * 
     * @author 尚
     * @param sourceCode
     *            事件来源ID
     * @param exeStatus
     *            事件处理状态（code表示）
     * @return sourceCode="source_abc",code="code_abc"(与之对应的字典中ID为"dict_id_abc"),则返回<br/>
     *         {"source_abc":"dict_id_abc"}
     */
    private JSONObject assist(String sourceCode, String exeStatus) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", sourceCode);
        jsonObject.put("exeStatus", exeStatus);

        return jsonObject;
    }

    /**
     * 查询详细待办任务
     * 
     * @author 尚
     * @param objs
     *            请求参数 {<br/>
     *            "bizData":{"procBizId":"1"},<br/>
     *            "procData":{"procInstId":"240067"},<br/>
     *            "authData":{"procAuthType":"2"},<br/>
     *            "variableData":{},<br/>
     *            "queryData":{}<br/>
     *            }
     * @return
     */
    public ObjectRestResponse<JSONObject> getUserToDoTask(JSONObject objs) {
        JSONObject resultJObj = new JSONObject();

        /*
         * 最后记得将多次进行查询admin及dict的部分进行合并查询，完成后去掉该注释
         */

        /*
         * 详细待办任务包括：1 流程历史<br/> 2 来源历史<br/> 3 立案单详情<br/>
         */

        /*
         * =================查询历史记录=====附带来源信息======开始==========
         */
        // 查询流程历史记录
        JSONObject procDataJObj = objs.getJSONObject("procData");
        if (procDataJObj == null) {
            throw new BizException("请指定待查询流程组件");
        }
        PageInfo<WfProcTaskHistoryBean> procApprovedHistory = wfProcTaskService.getProcApprovedHistory(objs);
        List<WfProcTaskHistoryBean> procHistoryList = procApprovedHistory.getList();
        JSONArray procHistoryJArray = JSONArray.parseArray(JSON.toJSONString(procHistoryList));

        List<String> procTaskAssigneeIdList =
            procHistoryList.stream().map(o -> o.getProcTaskAssignee()).distinct().collect(Collectors.toList());
        if (procTaskAssigneeIdList != null && !procTaskAssigneeIdList.isEmpty()) {
            JSONArray userDetailJArray = adminFeign.getInfoByUserIds(String.join(",", procTaskAssigneeIdList));
            Map<String, JSONObject> assignMap = new HashMap<>();
            if(BeanUtil.isNotEmpty(userDetailJArray)) {
                for(int i=0;i<userDetailJArray.size();i++) {
                    JSONObject userDetailJObj = userDetailJArray.getJSONObject(i);
                    assignMap.put(userDetailJObj.getString("userId"), userDetailJObj);
                }
            }
            if (assignMap != null && !assignMap.isEmpty()) {
                for (int i = 0; i < procHistoryJArray.size(); i++) {
                    JSONObject procHistoryJObj = procHistoryJArray.getJSONObject(i);

                    /*
                     * IF ( task.PROC_TASK_STATUS = '1', task.PROC_TASK_GROUP,
                     * task.PROC_TASK_ASSIGNEE ) procTaskAssignee
                     * 以上为查询历史任务详情的SQL，当未签收时，会将procTaskGroup作为签收人查询出来
                     * 但用procTaskGroup去查base_user表时，查不出数据，即assignMap.get(
                     * procHistoryJObj.getString(
                     * "procTaskAssignee"))为空 需要进行非空判断
                     */
                    JSONObject nameJObj = assignMap.get(procHistoryJObj.getString("procTaskAssignee"));

                    if (nameJObj != null) {
                        procHistoryJObj.put("procTaskAssigneeName",
                            nameJObj.getString("userName") == null ? "" : nameJObj.getString("userName"));
                        procHistoryJObj.put("procTaskAssigneeTel",
                            nameJObj.getString("mobilePhone") == null ? "" : nameJObj.getString("mobilePhone"));
                        procHistoryJObj.put("procTaskAssigneeGroupName",
                            nameJObj.getString("groupName") == null ? "" : nameJObj.getString("groupName"));
                        procHistoryJObj.put("procTaskAssigneeDeptName",
                            nameJObj.getString("deptName") == null ? "" : nameJObj.getString("deptName"));
                        procHistoryJObj.put("procTaskAssigneePotitionName",
                            nameJObj.getString("positionName") == null ? "" : nameJObj.getString("positionName"));
                    }
                }
            }
        }

        resultJObj.put("procHistory", procHistoryJArray);

        // 查询来源历史
        // 1->查询与该业务对应的立案单
        JSONObject bizDataJObj = objs.getJSONObject("bizData");
        if (bizDataJObj == null) {
            throw new BizException("请指定待查询业务组件");
        }
        String bizIdStr = bizDataJObj.getString("procBizId");
        int bizId = 0;
        if (StringUtils.isNotBlank(bizIdStr)) {
            bizId = Integer.valueOf(bizIdStr);
        } else {
            throw new BizException("请指定待查询业务ID");
        }

        CaseInfo caseInfo = caseInfoBiz.selectById(bizId);

        // 验证事件是否结束
        if (CaseInfo.FINISHED_STATE_FINISH.equals(caseInfo.getIsFinished())
            || CaseInfo.FINISHED_STATE_STOP.equals(caseInfo.getIsFinished())) {
            // 已完成或已中止
            JSONObject procHistory = procHistoryJArray.getJSONObject(procHistoryJArray.size() - 1);
            String procCtrasknameSuffix = "";
            if (CaseInfo.FINISHED_STATE_FINISH.equals(caseInfo.getIsFinished())) {
                procCtrasknameSuffix =
                    new String(environment.getProperty("caseInfo.isFinished.one")
                        .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            } else if (CaseInfo.FINISHED_STATE_STOP.equals(caseInfo.getIsFinished())) {
                procCtrasknameSuffix =
                        new String(environment.getProperty("caseInfo.isFinished.two")
                                .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            }
            procHistory.put("procCtaskname",
                procHistory.getString("procCtaskname") + procCtrasknameSuffix);
        }

        queryAssist(resultJObj, procHistoryList, caseInfo);

        return new ObjectRestResponse<JSONObject>().data(resultJObj);
    }

    private void queryAssist(JSONObject resultJObj, List<WfProcTaskHistoryBean> procHistoryList, CaseInfo caseInfo) {
        sourceTypeHistoryAssist(resultJObj, caseInfo);// 来源历史查询帮助
        /*
         * =================查询历史记录======附带来源信息=====结束==========
         */
        // 通过root_biz进行模糊查询业务字典，这样查询数据量会稍大，但可以减少请求次数
        Map<String, String> manyDictValuesMap = dictFeign.getByCode(Constances.ROOT_BIZ);

        Set<String> adminIdList = new HashSet<>();
        if (caseInfo.getCheckPerson() != null) {
            adminIdList.add(caseInfo.getCheckPerson());
        }
        for (WfProcTaskHistoryBean wfProcTaskHistoryBean : procHistoryList) {
            if (StringUtils.isNotBlank(wfProcTaskHistoryBean.getProcTaskCommitter())) {
                adminIdList.add(wfProcTaskHistoryBean.getProcTaskCommitter());
            }
        }
        for (WfProcTaskHistoryBean wfProcTaskHistoryBean : procHistoryList) {
            adminIdList.add(wfProcTaskHistoryBean.getProcTaskAssignee());
        }
        if (StringUtils.isNotBlank(caseInfo.getFinishCheckPerson())) {
            adminIdList.add(caseInfo.getFinishCheckPerson());
        }
        if (StringUtils.isNotBlank(caseInfo.getFinishPerson())) {
            adminIdList.add(caseInfo.getFinishPerson());
        }

        // 将多次向adminFeign的请求集中到这里进行查询，在经之上的代码即对需要进行查询 ID的收集
        JSONArray manyUsersJArray = adminFeign.getInfoByUserIds(String.join(",", adminIdList));
        Map<String, JSONObject> manyUsersMap=new HashMap<>();
        if(BeanUtil.isNotEmpty(manyUsersJArray)){
            for(int i=0;i<manyUsersJArray.size();i++){
                JSONObject manyUsersJObj = manyUsersJArray.getJSONObject(i);
                manyUsersMap.put(manyUsersJObj.getString("userId"), manyUsersJObj);
            }
        }

        /*
         * =================查询基础信息===========开始==========
         */
        JSONObject baseInfoJObj = new JSONObject();
        baseInfoJObj.put("sourceType", caseInfo.getSourceType());
        baseInfoJObj.put("caseCode", caseInfo.getCaseCode());
        baseInfoJObj.put("caseTitle", caseInfo.getCaseTitle());
        baseInfoJObj.put("caseLevel", caseInfo.getCaseLevel());
        baseInfoJObj.put("id", caseInfo.getId());
        String sourceTypeName = "";
        if (manyDictValuesMap != null && !manyDictValuesMap.isEmpty()) {
            if (caseInfo.getCaseLevel() != null) {
                baseInfoJObj.put("caseLevelName", manyDictValuesMap.get(caseInfo.getCaseLevel()));
            }
            sourceTypeName = manyDictValuesMap.get(caseInfo.getSourceType());
        }
        //来源类型名称
        baseInfoJObj.put("sourceTypeName", sourceTypeName);
        baseInfoJObj.put("caseDesc", caseInfo.getCaseDesc());

        /*
         * =================查询基础信息===========结束==========
         */

        /*
         * =================查询当事人信息(当事人类型(1个人，2公司）)===========开始==========
         */
        ConcernedPerson concernedPerson = null;
        ConcernedCompany concernedCompany = null;
        JSONObject concernedPersonJObj = new JSONObject();
        JSONObject concernedCompanyJObj = new JSONObject();
        if (StringUtils.isNotBlank(caseInfo.getConcernedPerson())) {
            if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON.equals(caseInfo.getConcernedType())) {
                // 当事人以个人形式存在
                concernedPerson = concernedPersonBiz.selectById(Integer.valueOf(caseInfo.getConcernedPerson()));
                if (concernedPerson != null) {
                    concernedPersonJObj = JSONObject.parseObject(JSON.toJSONString(concernedPerson));
                    if (manyDictValuesMap != null && !manyDictValuesMap.isEmpty()) {
                        if (StringUtils.isNotBlank(concernedPerson.getCredType())) {
                            concernedPersonJObj.put("credTypeName",
                                manyDictValuesMap.get(concernedPerson.getCredType()));

                            // 查询性别字典,manyDictValuesMap没有性别信息
                            Map<String, String> comm_sex = dictFeign.getByCode("comm_sex");
                            if(BeanUtil.isNotEmpty(comm_sex)&&StringUtils.isNotBlank(concernedPerson.getSex())){
                                concernedPersonJObj.put("sexName", comm_sex.get(concernedPerson.getSex()));
                            }else{
                                concernedPersonJObj.put("sexName", "");
                            }
                        }
                    }
                }
            } else if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG.equals(caseInfo.getConcernedType())) {
                // 当事人以单位形式存在
                concernedCompany = concernedCompanyBiz.selectById(Integer.valueOf(caseInfo.getConcernedPerson()));
                if (concernedCompany != null) {
                    concernedCompanyJObj = JSONObject.parseObject(JSON.toJSONString(concernedCompany));
                    // 与该当事人(单位)对应的监管对象
                    if (concernedCompany.getRegulaObjectId() != null) {
                        RegulaObject regulaObj = regulaObjectBiz.selectById(concernedCompany.getRegulaObjectId());
                        if (regulaObj != null) {
                            concernedCompanyJObj.put("objName", regulaObj.getObjName());
                            concernedCompanyJObj.put("objAddress", regulaObj.getObjAddress());
                            concernedCompanyJObj.put("linkman", regulaObj.getLinkman());
                            concernedCompanyJObj.put("linkmanPhone", regulaObj.getLinkmanPhone());
                            concernedCompanyJObj.put("introduction", regulaObj.getIntroduction());
                        }
                    }

                }
            }
        }

        /*
         * =================查询当事人信息(个人)===========结束 ==========
         */

        /*
         * =================查询当事人信息(单位)===========开始==========
         */
        // 在个人那处理了
        /*
         * =================查询当事人信息(单位)===========结束==========
         */

        /*
         * =================事件分类===========开始==========
         */
        JSONObject eventTypeJObj = new JSONObject();
        // 业务条线（单选）
        eventTypeJObj.put("bizList", caseInfo.getBizList());
        if (caseInfo.getBizList() != null) {
            // Map<String, String> bizListMap =
            // dictFeign.getDictValueByID(caseInfo.getBizList());//>>>>>>>>>>>>>>>查询了字典>>>>>>>>>>>>>>>>>>>>>>>>>
            if (manyDictValuesMap != null && !manyDictValuesMap.isEmpty()) {
                eventTypeJObj.put("bizListName", manyDictValuesMap.get(caseInfo.getBizList()));
            }
        }
        // 事件类别（单选）
        eventTypeJObj.put("eventTypeList", caseInfo.getEventTypeList());
        EventType eventType = new EventType();
        if (StringUtils.isNotBlank(caseInfo.getEventTypeList())) {
            try {
                eventType = eventTypeBiz.selectById(Integer.valueOf(caseInfo.getEventTypeList()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                
                //caseInfo.getEventTypeList()在数据库中应为数字，如果出现格式错误，表示数据出现问题，此catch使异常不至于因此而中断
                eventType=null;
            }
            if (eventType != null) {
                eventTypeJObj.put("eventTypeListName", eventType.getTypeName());
            }
        }
        // 监管对象,为多选
        List<String> regulaObjectNameList = new ArrayList<>();
        List<String> regulaObjectAddrList=new ArrayList<>();
        if (StringUtils.isNotBlank(caseInfo.getRegulaObjList())) {
            List<RegulaObject> regulaObjList = regulaObjectMapper.selectByIds(caseInfo.getRegulaObjList());
            for (RegulaObject regulaObject : regulaObjList) {
                regulaObjectNameList.add(regulaObject.getObjName());
                //在返回集中新加监管对象地址信息，用于自动填充
                regulaObjectAddrList.add(regulaObject.getObjAddress());
            }
        }
        eventTypeJObj.put("regulaObjList", caseInfo.getRegulaObjList());
        eventTypeJObj.put("regulaObjListName", String.join(",", regulaObjectNameList));
        //在返回集中新加监管对象地址信息，用于自动填充
        eventTypeJObj.put("regulaObjListAddr", String.join(",", regulaObjectAddrList));
        /*
         * =================事件分类===========结束==========
         */

        /*
         * =================地理信息===========开始==========
         */
        JSONObject mapInfoJObj = new JSONObject();
        // 所属网格
        mapInfoJObj.put("grid", caseInfo.getGrid());
        AreaGrid grid = areaGridBiz.selectById(caseInfo.getGrid());
        if (grid != null) {
            String areaGridName = grid.getGridName();
            AreaGrid parentAreaGrid = areaGridBiz.getParentAreaGrid(grid);
            if (BeanUtil.isNotEmpty(parentAreaGrid)
                && StringUtils.isNotBlank(parentAreaGrid.getGridName())) {
                areaGridName = areaGridName + "(" + parentAreaGrid.getGridName() + ")";
            }
            mapInfoJObj.put("gridName", areaGridName);
        }
        mapInfoJObj.put("occurAddr", caseInfo.getOccurAddr());
        mapInfoJObj.put("mapInfo", caseInfo.getMapInfo());
        /*
         * =================地理信息===========结束==========
         */

        /*
         * =================立案检查情况===========开始==========
         */
        JSONObject checkJObj = new JSONObject();
        String checkIsExist = "";
        if (StringUtils.isNotBlank(caseInfo.getCheckIsExist())) {
            checkIsExist = "0".equals(caseInfo.getCheckIsExist()) ? "不存在" : "存在";
        }
        checkJObj.put("checkIsExist", checkIsExist);
        checkJObj.put("checkOpinion", caseInfo.getCheckOpinion());
        if (caseInfo.getCheckPerson() != null) {
            checkJObj.put("checkPerson", caseInfo.getCheckPerson());
            if (manyUsersMap != null && !manyUsersMap.isEmpty()) {
                JSONObject checkPersonJObj = manyUsersMap.get(caseInfo.getCheckPerson());
                // 有可能caseInfo.getCheckPerson()所对应的人在base_user表中被删除，这样会导致checkPersonJObj=NULL
                if(BeanUtil.isNotEmpty(checkPersonJObj)){
                    checkJObj.put("checkPersonName", checkPersonJObj.getString("userName"));
                    checkJObj.put("checkPersonTel", checkPersonJObj.getString("mobilePhone"));
                }
            }
        }
        if (caseInfo.getCheckTime() != null) {
            checkJObj.put("checkTime", DateUtil.dateFromDateToStr(caseInfo.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
        }
        checkJObj.put("checkPic", caseInfo.getCheckPic());
        /*
         * =================立案检查情况===========结束==========
         */

        /*
         * =================事项要求===========开始=========
         */
        JSONObject requiredJObj = new JSONObject();
        // 办理期限日期格式精确到“日”
        requiredJObj.put("deadLine", BeanUtil.isEmpty(caseInfo.getDeadLine()) ? null
            : DateUtil.dateFromDateToStr(caseInfo.getDeadLine(), DateUtil.DATE_FORMAT_DF));
        String executedDeptName = "";
        requiredJObj.put("executedDept", caseInfo.getExecuteDept());
        if (caseInfo.getExecuteDept() != null) {
            Map<String, String> executeDeptMap = adminFeign.getDepartByDeptIds(caseInfo.getExecuteDept());
            if (executeDeptMap != null && !executeDeptMap.isEmpty()) {
                executedDeptName =
                    CommonUtil.getValueFromJObjStr(executeDeptMap.get(caseInfo.getExecuteDept()), "name");
            }
        }
        requiredJObj.put("executedDeptName", executedDeptName);
        requiredJObj.put("requirements", caseInfo.getRequirements());
        /*
         * =================事项要求===========结束==========
         */

        /*
         * =================指挥长审批===========开始==========
         */
        JSONArray commanderApproveJArray = new JSONArray();
        for (WfProcTaskHistoryBean wfProcTaskHistoryBean : procHistoryList) {
            /*
             * 指挥长相关信息用工作流表进行保存，在业务表里，只对指挥长审批意见进行了保存，弃用
             */
            if (Constances.ProcCTaskCode.COMMANDERAPPROVE.equals(wfProcTaskHistoryBean.getProcCtaskcode())) {
                JSONObject commanderApproveJObj = JSONObject.parseObject(JSON.toJSONString(wfProcTaskHistoryBean));
                // 查询指挥长
                if (StringUtils.isNotBlank(wfProcTaskHistoryBean.getProcTaskAssignee())) {
                    if (manyUsersMap != null && !manyUsersMap.isEmpty()) {
                        // 流程的审批人应为procTaskAssignee
                        /*
                         * IF ( task.PROC_TASK_STATUS = '1',
                         * task.PROC_TASK_GROUP,
                         * task.PROC_TASK_ASSIGNEE ) procTaskAssignee
                         * 以上为查询历史任务详情的SQL，当未签收时，会将procTaskGroup作为签收人查询出来
                         * 但用procTaskGroup去查base_user表时，查不出数据，即manyUsersMap.get(
                         * wfProcTaskHistoryBean.getProcTaskAssignee())为空
                         * 需要进行非空判断
                         */
                        JSONObject jObjTmp =manyUsersMap.get(wfProcTaskHistoryBean.getProcTaskAssignee());
                        if(BeanUtil.isNotEmpty(jObjTmp)){
                            commanderApproveJObj.put("procTaskCommitterName", jObjTmp.getString("userName"));
                            commanderApproveJObj.put("commanderTel", jObjTmp.getString("mobilePhone"));// 审批人联系方法
                        }
                    }
                    //流程审批时间
                    commanderApproveJObj.put("procTaskEndtime", wfProcTaskHistoryBean.getProcTaskEndtime());// 审批时间
                    commanderApproveJObj.put("procTaskApprOpinion", wfProcTaskHistoryBean.getProcTaskApprOpinion());// 审批意见
                    commanderApproveJArray.add(commanderApproveJObj);
                }
            }
        }
        /*
         * =================指挥长审批===========结束==========
         */

        /*
         * =================事件处理===========开始==========
         */
        JSONArray executeInfoJArray = new JSONArray();
        List<ExecuteInfo> executeInfoList = executeInfoBiz.getListByCaseInfoId(caseInfo.getId());
        /*
         * 在ExecuteInfo对应的数据库 表中保存的是人名，不是ID<br/>
         */
        if (executeInfoList != null && !executeInfoList.isEmpty()) {

            for (ExecuteInfo executeInfo : executeInfoList) {
                JSONObject executeInfoJObj;
                executeInfoJObj = JSONObject.parseObject(JSON.toJSONString(executeInfo));
                if (executeInfo != null) {
                    executeInfoJObj.put("exePersonName", executeInfo.getExePerson());// 办理人
                    // executeInfoJObj.put("exePsersonTel", executeInfo.get);//
                    // 办理人联系方式
                    executeInfoJObj.put("finishTime", executeInfo.getFinishTime());// 办结时间
                    executeInfoJObj.put("exeDesc", executeInfo.getExeDesc());// 情况说明
                    executeInfoJObj.put("picture", executeInfo.getPicture());

                    JSONObject sourceType = resultJObj.getJSONObject("sourceTypeHistory");
                    executeInfoJObj.put("recordPserson", sourceType.getString("crtUserId"));// 登记人ID
                    executeInfoJObj.put("recordPsersonName", sourceType.getString("crtUserName"));// 登记人
                    // 登记人电话
                    String recordPersonTel = "";
                    if (StringUtils.isNotBlank(sourceType.getString("crtUserId"))) {
                        Map<String, String> recordUser = adminFeign.getUsersByUserIds(sourceType.getString("crtUserId"));
                        recordPersonTel =
                            CommonUtil.getValueFromJObjStr(recordUser.get(sourceType.getString("crtUserId")),
                                "mobilePhone");
                    }
                    executeInfoJObj.put("recordPersonTel", recordPersonTel);
                    executeInfoJObj.put("recordTime", sourceType.getDate("crtTime"));// 登记时间
                    executeInfoJArray.add(executeInfoJObj);
                }
            }
        }

        /*
         * =================事件处理===========结束==========
         */

        /*
         * =================结案检查===========开始==========
         */
        JSONObject finishCheckJObj = new JSONObject();
        finishCheckJObj.put("finishCheckIsExist", caseInfo.getFinishCheckIsExist());
        String finishCheckIsExistName = "";
        if (StringUtils.isNotBlank(caseInfo.getFinishCheckIsExist())) {
            finishCheckIsExistName = "0".equals(caseInfo.getFinishCheckIsExist()) ? "不存在" : "存在";
        }
        finishCheckJObj.put("finishCheckIsExistName", finishCheckIsExistName);
        finishCheckJObj.put("finishCheckOpinion", caseInfo.getFinishCheckOpinion());
        finishCheckJObj.put("finishCheckTime", caseInfo.getFinishCheckTime());
        finishCheckJObj.put("finishCheckPic", caseInfo.getFinishCheckPic());
        if (StringUtils.isNotBlank(caseInfo.getFinishCheckPerson())) {
            if (manyUsersMap != null && !manyUsersMap.isEmpty()) {
                finishCheckJObj.put("finishCheckPerson", caseInfo.getFinishCheckPerson());

                JSONObject finishCheckPersonJObj =manyUsersMap.get(caseInfo.getFinishCheckPerson());
                // 有可能caseInfo.getFinishCheckPerson()所对应的人在base_user表中被删除，finishCheckPersonJObj=NULL
                if(BeanUtil.isNotEmpty(finishCheckPersonJObj)){
                    finishCheckJObj.put("finishCheckPersonName", finishCheckPersonJObj.getString("userName"));
                    finishCheckJObj.put("finishCheckPersonTel", finishCheckPersonJObj.getString("mobilePhone"));
                }
            }
        }
        /*
         * =================结案检查==========结束==========
         */

        /*
         * =================结案说明==========开始==========
         */
        JSONObject finishJObj = new JSONObject();
        finishJObj.put("finishDesc", caseInfo.getFinishDesc());
        finishJObj.put("finishTime", caseInfo.getFinishTime());
        if (StringUtils.isNotBlank(caseInfo.getFinishPerson())) {
            if (manyUsersMap != null && !manyUsersMap.isEmpty()) {
                finishJObj.put("finishPerson", caseInfo.getFinishPerson());

                JSONObject finishPersonJObj = manyUsersMap.get(caseInfo.getFinishPerson());
                // 有可能caseInfo.getFinishPerson()所对应的人在base_user表中被删除，getFinishPerson=NULL
                if(BeanUtil.isNotEmpty(finishPersonJObj)){
                    finishJObj.put("finishPersonName", finishPersonJObj.getString("name"));
                    finishJObj.put("finishPersonTel", finishPersonJObj.getString("mobilePhone"));
                }
            }
        }

        /*
         * =================结案说明==========结束==========
         */

        resultJObj.put("baseInfo", baseInfoJObj);// 基本信息
        resultJObj.put("concernedPerson", concernedPersonJObj);// 当事人
        resultJObj.put("eventTypeJObj", eventTypeJObj);// 事件分类
        resultJObj.put("mapInfoJObj", mapInfoJObj);// 地理信息
        resultJObj.put("checkJObj", checkJObj);// 立案检查情况
        resultJObj.put("requiredJObj", requiredJObj);// 事项要求
        resultJObj.put("commanderApproveJArray", commanderApproveJArray);// 指挥长审批
        resultJObj.put("executeInfoJObj", executeInfoJArray);// 事件处理情况
        resultJObj.put("finishCheckJObj", finishCheckJObj);// 结束检查
        resultJObj.put("finishJObj", finishJObj);// 结束检查
        resultJObj.put("concernedCompanyJObj", concernedCompanyJObj);// 当事人(单位)
    }

    /**
     * 终止流程
     * 
     * @author 尚
     * @param objs
     */
    public void endProcess(JSONObject objs) {
        /*
         * 任务已走向结束<br/> 去执行相应业务应该完成的操作<br/> 1 立案单isFinished：1<br/> 2 来源各变化
         */
        log.info("该请求流向【结束】，流程即将结束。");
        wfProcTaskService.endProcessInstance(objs);
        CaseInfo caseInfo = new CaseInfo();

        // 更新业务数据
        JSONObject bizDataJObject = objs.getJSONObject("bizData");

        // 查询数据库中的caseInfo,以确定与caseInfo相对应的登记表那条记录的ID
        CaseInfo caseInfoInDB = caseInfoBiz.selectById(Integer.valueOf(bizDataJObject.getString("procBizId")));

        caseInfo.setSourceCode(caseInfoInDB.getSourceCode());
        caseInfo.setId(Integer.valueOf(bizDataJObject.getString("procBizId")));
        caseInfo.setSourceType(caseInfoInDB.getSourceType());
        caseInfo.setIsFinished(CaseInfo.FINISHED_STATE_STOP);
        caseInfo.setFinishTime(new Date());// 结案时间

        gotoFinishSource(caseInfo, true);// 去更新事件来源的状态

        // 更新业务数据(caseInfo)
        caseInfoBiz.updateSelectiveById(caseInfo);
    }
    
    /**
     * 按来源类型查询记录
     * 
     * @author 尚
     * @param sourceTypeKeyPatrol
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<CaseInfo> getListSourceType(String sourceTypeKeyPatrol, int page, int limit, boolean isNoFinish) {
        String property = environment.getProperty(sourceTypeKeyPatrol);
        Example example=new Example(CaseInfo.class);
        example.createCriteria().andEqualTo("sourceType", property);
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseInfo> rows = caseInfoBiz.selectByExample(example);
        if(BeanUtil.isNotEmpty(rows)) {
            return new TableResultResponse<>(pageInfo.getTotal(), rows);
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
    
    /**
     * 查询事件记录，只涉及业务数据
     * @param id
     * @return
     */
    public ObjectRestResponse<JSONObject> caseInfoInstance(String id){
        JSONObject resultJObj=new JSONObject();
        
        CaseInfo caseInfo = this.caseInfoBiz.selectById(Integer.valueOf(id));
        
        if(BeanUtil.isNotEmpty(caseInfo)) {
            queryAssist(resultJObj, new ArrayList<>(), caseInfo);
        }
        
        return new ObjectRestResponse<JSONObject>().data(resultJObj);
    }

    /**
     * 事件全部定位
     * @return
     * @param objs
     */
    public TableResultResponse<JSONObject> allPosition(JSONObject objs) {
        TableResultResponse<JSONObject> allTasks = this.getAllTasks(objs);

        List<JSONObject> resultList=new ArrayList<>();
        if(BeanUtil.isNotEmpty(allTasks))
        {
            List<JSONObject> rows = allTasks.getData().getRows();
            if(BeanUtil.isNotEmpty(rows)){
                for(JSONObject tmpJObj:rows){
                    JSONObject resultJObj=new JSONObject();
                    resultJObj.put("id", tmpJObj.get("id"));
                    resultJObj.put("procBizid", tmpJObj.get("procBizid"));
                    resultJObj.put("procInstId", tmpJObj.get("procInstId"));
                    resultJObj.put("mapInfo", tmpJObj.get("mapInfo"));
                    resultList.add(resultJObj);
                }
            }
        }

        return new TableResultResponse<>(resultList.size(), resultList);
    }

    /**
     * 通过部门id和事件等级获取事件列表
     *
     * @param caseLevel 事件等级
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<Map<String, Object>> getCaseInfoByDeptId(JSONObject objs,String caseLevel,
                                                                        String deptId,int page,int limit) {
        Page<Object> pageHelper = PageHelper.startPage(page, limit);

        // 查询待办工作流任务
        PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getAllTasks(objs);
        List<WfProcBackBean> list = pageInfo.getList();


        List<Map<String, Object>> result = caseInfoMapper.selectCaseInfoByDeptId(deptId, caseLevel);
        if (BeanUtil.isEmpty(result)) {
            return new TableResultResponse<>(0, new ArrayList<>());
        }
        Map<String,String>  dictTemp = dictFeign.getByCode(Constances.ROOT_BIZ_EVENTLEVEL);

        for(Map<String,Object> map : result){
            String caseLevelName = dictTemp.get(map.get("caseLevel"));
            map.put("caseLevelName",caseLevelName);
            map.put("isSupervise","0".equals(map.get("isSupervise"))?false:true);
            map.put("isUrge","0".equals(map.get("isUrge"))?false:true);
        }
        return new TableResultResponse<>(pageHelper.getTotal(), result);
    }

    /**
     * 只通过业务ID查询带有工作流信息的结果
     * @param id
     * @return
     */
    public ObjectRestResponse<JSONObject> getCaseInfoWithWfData(Integer id) {
        /*
         * 1 查询与id事件关联的工作流实例ID
         * 2 生成工作流需要的请求参数结构
         */

        // 工作流业务
        JSONObject bizData = new JSONObject();
        // 事件流程编码
        bizData.put("procKey", "comprehensiveManage");
        // 业务ids
        bizData.put(Constants.WfProcessBizDataAttr.PROC_BIZID, String.valueOf(id));
        JSONObject objs = initWorkflowQuery(bizData);
        // 查询流程实例ID
        List<Map<String, Object>> procInstIdList = wfMonitorService.getProcInstIdByUserId(objs);
        if (BeanUtil.isNotEmpty(procInstIdList)) {
            // 请求中，只通过一个id进行查询，所以返回结果如果不为空，则长度一定为1
            String procInstId = String.valueOf(procInstIdList.get(0).get("procInstId"));
            objs.getJSONObject("procData").put("procInstId", procInstId);

            return this.getUserToDoTask(objs);
        }

        return new ObjectRestResponse<>();
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
}