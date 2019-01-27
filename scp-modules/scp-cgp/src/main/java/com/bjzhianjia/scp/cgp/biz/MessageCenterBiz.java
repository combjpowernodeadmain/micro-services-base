package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bjzhianjia.scp.cgp.entity.AreaGridMember;
import com.bjzhianjia.scp.cgp.entity.CLESuperviseRecord;
import com.bjzhianjia.scp.cgp.entity.CLEUrgeRecord;
import com.bjzhianjia.scp.cgp.entity.LawExecutePerson;
import com.bjzhianjia.scp.cgp.entity.SuperviseRecord;
import com.bjzhianjia.scp.cgp.entity.UrgeRecord;
import com.bjzhianjia.scp.cgp.mapper.CLESuperviseRecordMapper;
import com.bjzhianjia.scp.cgp.mapper.CLEUrgeRecordMapper;
import com.bjzhianjia.scp.cgp.mapper.SuperviseRecordMapper;
import com.bjzhianjia.scp.cgp.mapper.UrgeRecordMapper;
import com.bjzhianjia.scp.security.wf.base.constant.Constants;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.MessageCenter;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.mapper.CaseInfoMapper;
import com.bjzhianjia.scp.cgp.mapper.CaseRegistrationMapper;
import com.bjzhianjia.scp.cgp.mapper.MessageCenterMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.service.impl.WfMonitorServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-09-30 15:24:19
 */
@Service
@Log4j
public class MessageCenterBiz extends BusinessBiz<MessageCenterMapper, MessageCenter> {

    @Autowired
    private WfMonitorServiceImpl wfMonitorService;

    @Autowired
    private CaseInfoMapper caseInfoMapper;

    @Autowired
    private CaseRegistrationMapper caseRegistrationMapper;

    @Autowired
    private PropertiesProxy propertiesProxy;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private AreaGridMemberBiz areaGridMemberBiz;

    @Autowired
    private UrgeRecordMapper urgeRecordMapper;

    @Autowired
    private SuperviseRecordMapper superviseRecordMapper;

    @Autowired
    private CLEUrgeRecordMapper cLEUrgeRecordMapper;

    @Autowired
    private CLESuperviseRecordMapper cLESuperviseRecordMapper;
    
    @Autowired
    private Environment environment;

    @Autowired
    private LawExecutePersonBiz lawExecutePersonBiz;

    // @Autowired
    // private WebSocketService webSocketService;

    /**
     * 消息中心
     * 
     * @return
     */
    public ObjectRestResponse<JSONObject> msgCenter() {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();
        /*
         * 查询工作流数据库，获取源数据
         */
        // 事件工作流
        List<MessageCenter> caseInfoMsgC = caseInfoQueryParam();

        // 案件工作流
        List<MessageCenter> caseRegistrationMsgC = caseRegistrationQueryParam();

        // 执法任务

        List<MessageCenter> list = new ArrayList<>();
        list.addAll(caseInfoMsgC);
        list.addAll(caseRegistrationMsgC);

        for (MessageCenter messageCenter : list) {
            messageCenter.setCrtUserId(BaseContextHandler.getUserID());
            messageCenter.setCrtUserName(BaseContextHandler.getUsername());
            messageCenter.setTenantId(BaseContextHandler.getTenantID());
            messageCenter.setCrtTime(new Date());
        }

        if (BeanUtil.isNotEmpty(list)) {
            this.mapper.addMessageCenterList(list);
        }
        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }

    private List<MessageCenter> caseInfoQueryParam() {
        /*
         * {"bizData":{"prockey":"comprehensiveManage"},
         * "procData":{},
         * "authData":{"procAuthType":"2"},
         * "variableData":{},
         * "queryData":{
         * "isQuery":"true",
         * "page":"1",
         * "limit":"50"
         * }
         * }
         */
        JSONObject caseInfoQueryParam = new JSONObject();

        JSONObject caseInfoBizData = new JSONObject();
        caseInfoBizData.put("prockey", "comprehensiveManage");

        JSONObject caseInfoProcData = new JSONObject();

        JSONObject caseInfoAuthData = new JSONObject();
        caseInfoAuthData.put("procAuthType", "2");

        JSONObject caseInfoVariableData = new JSONObject();

        caseInfoQueryParam.put("bizData", caseInfoBizData);
        caseInfoQueryParam.put("procData", caseInfoProcData);
        caseInfoQueryParam.put("authData", caseInfoAuthData);
        caseInfoQueryParam.put("variableData", caseInfoVariableData);

        PageInfo<WfProcBackBean> userAllToDoTasks = wfMonitorService.getUserAllToDoTasks(caseInfoQueryParam);
        List<WfProcBackBean> userAllToDoTaskList = userAllToDoTasks.getList();

        // 收集待办任务ID，与消息中心中的msg_source_id进行比较
        List<String> wfProcBackBeanIdList = new ArrayList<>();
        List<String> bizDataIdList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(userAllToDoTaskList)) {
            wfProcBackBeanIdList =
                userAllToDoTaskList.stream().map(o -> String.valueOf(o.getId())).distinct()
                    .collect(Collectors.toList());
            bizDataIdList =
                userAllToDoTaskList.stream().map(o -> String.valueOf(o.getProcBizid())).distinct()
                    .collect(Collectors.toList());
        }

        // 以ProcBackBeanIdList为基准查询消息中心表
        List<MessageCenter> msgCenterList = this.selectBySourceIds(wfProcBackBeanIdList);

        // 验证哪些待办记录没被添加到消息中心
        List<Integer> msgCenterIdList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(msgCenterList)) {
            msgCenterIdList =
                msgCenterList.stream().map(o -> Integer.valueOf(o.getMsgSourceId())).distinct()
                    .collect(Collectors.toList());
        }

        List<CaseInfo> caseInfoList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(bizDataIdList)) {
            caseInfoList = caseInfoMapper.selectByIds(String.join(",", bizDataIdList));
        }
        Map<Integer, String> caseInfo_ID_TITLE_Map = new HashMap<>();
        Map<Integer, String> caseInfo_ID_DESC_Map = new HashMap<>();
        Map<Integer, Date> caseInfo_ID_OCCURTIME_Map = new HashMap<>();
        for (CaseInfo caseInfo : caseInfoList) {
            caseInfo_ID_TITLE_Map.put(caseInfo.getId(), caseInfo.getCaseTitle());
            caseInfo_ID_DESC_Map.put(caseInfo.getId(), caseInfo.getCaseDesc());
            caseInfo_ID_OCCURTIME_Map.put(caseInfo.getId(), caseInfo.getOccurTime());
        }

        List<MessageCenter> caseInfoMsgCList = new ArrayList<>();
        for (WfProcBackBean wfProcBackBeanTmp : userAllToDoTaskList) {
            if (!msgCenterIdList.contains(wfProcBackBeanTmp.getId())) {
                // 在已有的消息记录中，不包含某一待办任务
                MessageCenter messageCenter = new MessageCenter();
                messageCenter.setMsgSourceId(String.valueOf(wfProcBackBeanTmp.getId()));
                messageCenter.setMsgSourceType(MessageCenter.CASE_INFO_TYPE);
                messageCenter.setMsgName(caseInfo_ID_TITLE_Map.get(Integer.valueOf(wfProcBackBeanTmp.getProcBizid())));
                messageCenter.setMsgDesc(caseInfo_ID_DESC_Map.get(Integer.valueOf(wfProcBackBeanTmp.getProcBizid())));
                messageCenter
                    .setTaskTime(caseInfo_ID_OCCURTIME_Map.get(Integer.valueOf(wfProcBackBeanTmp.getProcBizid())));
                caseInfoMsgCList.add(messageCenter);
            }
        }

        return caseInfoMsgCList;
    }

    private List<MessageCenter> caseRegistrationQueryParam() {
        /*
         * {"bizData":{"prockey":"LawEnforcementProcess"},
         * "procData":{},
         * "authData":{"procAuthType":"2"},
         * "variableData":{},
         * "queryData":{
         * "isQuery":"true",
         * "page":"1",
         * "limit":"50"
         * }
         * }
         */
        JSONObject caseInfoQueryParam = new JSONObject();

        JSONObject caseInfoBizData = new JSONObject();
        caseInfoBizData.put("prockey", "LawEnforcementProcess");

        JSONObject caseInfoProcData = new JSONObject();

        JSONObject caseInfoAuthData = new JSONObject();
        caseInfoAuthData.put("procAuthType", "2");

        JSONObject caseInfoVariableData = new JSONObject();

        caseInfoQueryParam.put("bizData", caseInfoBizData);
        caseInfoQueryParam.put("procData", caseInfoProcData);
        caseInfoQueryParam.put("authData", caseInfoAuthData);
        caseInfoQueryParam.put("variableData", caseInfoVariableData);

        PageInfo<WfProcBackBean> userAllToDoTasks = wfMonitorService.getUserAllToDoTasks(caseInfoQueryParam);
        List<WfProcBackBean> userAllToDoTaskList = userAllToDoTasks.getList();

        // 收集待办任务ID，与消息中心中的msg_source_id进行比较
        List<String> wfProcBackBeanIdList = new ArrayList<>();
        List<String> bizDataIdList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(userAllToDoTaskList)) {
            wfProcBackBeanIdList =
                userAllToDoTaskList.stream().map(o -> String.valueOf(o.getId())).distinct()
                    .collect(Collectors.toList());
            bizDataIdList =
                userAllToDoTaskList.stream().map(o -> String.valueOf(o.getProcBizid())).distinct()
                    .collect(Collectors.toList());
        }

        // 以ProcBackBeanIdList为基准查询消息中心表
        List<MessageCenter> msgCenterList = selectBySourceIds(wfProcBackBeanIdList);

        // 验证哪些待办记录没被添加到消息中心
        List<Integer> msgCenterSourceIdList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(msgCenterList)) {
            msgCenterSourceIdList =
                msgCenterList.stream().map(o -> Integer.valueOf(o.getMsgSourceId())).distinct()
                    .collect(Collectors.toList());
        }

        List<CaseRegistration> caseRegistrationList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(bizDataIdList)) {
            Example caseRegistrationExample = new Example(CaseRegistration.class);
            caseRegistrationExample.createCriteria().andIn("id", bizDataIdList);
            caseRegistrationList = caseRegistrationMapper.selectByExample(caseRegistrationExample);
        }
        Map<String, String> caseInfo_ID_TITLE_Map = new HashMap<>();
        Map<String, String> caseInfo_ID_DESC_Map = new HashMap<>();
        Map<String, Date> caseInfo_ID_OCCURTIME_Map = new HashMap<>();
        for (CaseRegistration caseRegistration : caseRegistrationList) {
            caseInfo_ID_TITLE_Map.put(caseRegistration.getId(), caseRegistration.getCaseName());
            caseInfo_ID_DESC_Map.put(caseRegistration.getId(), caseRegistration.getCaseContent());
            caseInfo_ID_OCCURTIME_Map.put(caseRegistration.getId(), caseRegistration.getCaseTime());
        }

        List<MessageCenter> caseRegistrationMsgCList = new ArrayList<>();
        for (WfProcBackBean wfProcBackBeanTmp : userAllToDoTaskList) {
            if (!msgCenterSourceIdList.contains(wfProcBackBeanTmp.getId())) {
                // 在已有的消息记录中，不包含某一待办任务
                MessageCenter messageCenter = new MessageCenter();
                messageCenter.setMsgSourceId(String.valueOf(wfProcBackBeanTmp.getId()));
                messageCenter.setMsgSourceType(MessageCenter.CASE_REGISTRATION_TYPE);
                messageCenter.setMsgName(caseInfo_ID_TITLE_Map.get(wfProcBackBeanTmp.getProcBizid()));
                messageCenter.setMsgDesc(caseInfo_ID_DESC_Map.get(wfProcBackBeanTmp.getProcBizid()));
                messageCenter.setTaskTime(caseInfo_ID_OCCURTIME_Map.get(wfProcBackBeanTmp.getProcBizid()));
                caseRegistrationMsgCList.add(messageCenter);
            }
        }
        return caseRegistrationMsgCList;
    }

    public List<MessageCenter> selectBySourceIds(List<String> wfProcBackBeanIdList) {
        Example example = new Example(MessageCenter.class);
        example.createCriteria().andIn("msgSourceId", wfProcBackBeanIdList);

        List<MessageCenter> selectByExample = this.selectByExample(example);

        if (BeanUtil.isNotEmpty(selectByExample)) {
            return selectByExample;
        }

        return new ArrayList<>();
    }

    /**
     * 获取未读消息
     * 
     * @return
     */
    public JSONObject getUnReadList(MessageCenter messageCenter,int page, int limit,String requestFrom) {
        return msgListAssist(messageCenter, page, limit,"0",requestFrom);
    }

    private JSONObject msgListAssist(MessageCenter messageCenter, int page, int limit,String isRead,
                                     String requestFrom) {
        JSONObject jObjResult=new JSONObject();

        Example example = new Example(MessageCenter.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("isRead", isRead);
        //消息提醒以个人为查询依据
        criteria.andEqualTo("crtUserId", BaseContextHandler.getUserID());
        if(StringUtils.isNotBlank(messageCenter.getMsgSourceType())){
            criteria.andIn("msgSourceType",
                Arrays.asList(messageCenter.getMsgSourceType().split(",")));
        }

        example.setOrderByClause("crt_time desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<MessageCenter> messageCenterList = this.selectByExample(example);

        // 查询与我相关的消息的总条数
        example.clear();
        example.createCriteria().andEqualTo("isDeleted", "0").andEqualTo("isRead", isRead)
            .andEqualTo("crtUserId", BaseContextHandler.getUserID());
        int allTotal = this.selectCountByExample(example);

        List<JSONObject> result = new ArrayList<>();
        if (BeanUtil.isNotEmpty(messageCenterList)) {
            List<JSONObject> messageCenterJObjList;
            if (StringUtils.equals("1", requestFrom)) {
                messageCenterJObjList = clearMsgAboartToWEB(messageCenterList);
            } else {
                messageCenterJObjList =
                    messageCenterList.stream()
                        .map(o -> JSONObject.parseObject(JSONObject.toJSONString(o, SerializerFeature.WriteDateUseDateFormat)))
                        .collect(Collectors.toList());
            }

            for (JSONObject messageCenterTmp : messageCenterJObjList) {
                try {
                    JSONObject swapProperties =new JSONObject();
                    // JSONObject swapProperties =
                    // propertiesProxy.swapProperties(messageCenterTmp, "id", "msgName", "msgDesc", "taskTime","msgSourceId","msgSourceType","crtTime");
                    swapProperties.put("id", messageCenterTmp.getString("id"));
                    swapProperties.put("msgName", messageCenterTmp.getString("msgName"));
                    swapProperties.put("msgDesc", messageCenterTmp.getString("msgDesc"));
                    swapProperties.put("taskTime", messageCenterTmp.getString("taskTime"));
                    swapProperties.put("msgSourceId", messageCenterTmp.getString("msgSourceId"));
                    swapProperties.put("msgSourceType", messageCenterTmp.getString("msgSourceType"));
                    swapProperties.put("crtTime", messageCenterTmp.getString("crtTime"));
                    swapProperties.put("isToAppOperator", messageCenterTmp.getBooleanValue("isToAppOperator"));

                    // 当前人查到的消息记录肯定为本人的，所以整合姓名时，可从BaseContextHandler中获取
                    swapProperties.put("executor", BaseContextHandler.getUsername());
                    result.add(swapProperties);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

        }

        checkIsOperated(result);
        // 对结果集进行返回前的处理
        mergeResult(result,messageCenter);

        jObjResult.put("status", 200);
        jObjResult.put("rows", result);
        jObjResult.put("total", pageInfo.getTotal());
        jObjResult.put("allTotal", allTotal);
        return jObjResult;
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
     * 获取已读读消息
     *
     * @return
     */
    public JSONObject getReadList(MessageCenter messageCenter,int page, int limit,String requestFrom) {
        return msgListAssist(messageCenter, page, limit,"1",requestFrom);
    }

    /**
     * 对结果集进行返回前的处理
     * @param result
     * @param queryMessageCenter
     */
    private void mergeResult(List<JSONObject> result, MessageCenter queryMessageCenter) {
        // 合并催办人与督办人
        mergeUrgeAndSupervise(result,queryMessageCenter);
    }

    /**
     * 合并催办人与督办人
     * @param result
     * @param queryMessageCenter
     */
    private void mergeUrgeAndSupervise(List<JSONObject> result, MessageCenter queryMessageCenter) {
        if(BeanUtil.isEmpty(result)){
            return;
        }

        List<String> msgSourceIdList =
            result.stream().map(o -> o.getString("msgSourceId")).distinct()
                .collect(Collectors.toList());

        String msgSourceType = queryMessageCenter.getMsgSourceType();
        if(StringUtils.isNotBlank(msgSourceType)){
            List<String> msgSourceTypes = Arrays.asList(msgSourceType.split(","));
            if(msgSourceTypes.contains("case_info_01")){
                // 事件催办
                List<UrgeRecord> urgeRecords = urgeRecordMapper.selectLastUrge(new HashSet<>(msgSourceIdList));
                if(BeanUtil.isNotEmpty(urgeRecords)){
                    Map<String,String> collectMap=new HashMap<>(10);
                    for(UrgeRecord tmp:urgeRecords){
                        collectMap.put(String.valueOf(tmp.getCaseInfoId()), tmp.getCrtUserName());
                    }

                    for(JSONObject tmp:result){
                        if("case_info_01".equals(tmp.getString("msgSourceType"))){
                            tmp.put("leaderPerson", collectMap.get(tmp.getString("msgSourceId")));
                        }
                    }
                }
            }
            if(msgSourceTypes.contains("case_info_02")){
                // 事件督办
                List<SuperviseRecord> superviseRecords = superviseRecordMapper.selectLastSupervise(new HashSet<>(msgSourceIdList));
                if(BeanUtil.isNotEmpty(superviseRecords)){
                    Map<String,String> collectMap=new HashMap<>(10);
                    for(SuperviseRecord tmp:superviseRecords){
                        collectMap.put(String.valueOf(tmp.getCaseInfoId()), tmp.getCrtUserName());
                    }

                    for(JSONObject tmp:result){
                        if("case_info_02".equals(tmp.getString("msgSourceType"))){
                            tmp.put("leaderPerson", collectMap.get(tmp.getString("msgSourceId")));
                        }
                    }
                }
            }
            if(msgSourceTypes.contains("case_registration_01")){
                // 事件督办
                List<CLEUrgeRecord> cleUrgeRecords = cLEUrgeRecordMapper.selectLastUrge(new HashSet<>(msgSourceIdList));
                if(BeanUtil.isNotEmpty(cleUrgeRecords)){
                    Map<String,String> collectMap=new HashMap<>(10);
                    for(CLEUrgeRecord tmp:cleUrgeRecords){
                        collectMap.put(String.valueOf(tmp.getCleCaseId()), tmp.getCrtUserName());
                    }

                    for(JSONObject tmp:result){
                        if("case_registration_01".equals(tmp.getString("msgSourceType"))){
                            tmp.put("leaderPerson", collectMap.get(tmp.getString("msgSourceId")));
                        }
                    }
                }
            }
            if(msgSourceTypes.contains("case_registration_02")){
                // 事件督办
                List<CLESuperviseRecord> cleSuperviseRecords = cLESuperviseRecordMapper.selectLastSupervise(new HashSet<>(msgSourceIdList));
                if(BeanUtil.isNotEmpty(cleSuperviseRecords)){
                    Map<String,String> collectMap=new HashMap<>(10);
                    for(CLESuperviseRecord tmp:cleSuperviseRecords){
                        collectMap.put(String.valueOf(tmp.getCleCaseId()), tmp.getCrtUserName());
                    }

                    for(JSONObject tmp:result){
                        if("case_registration_02".equals(tmp.getString("msgSourceType"))){
                            tmp.put("leaderPerson", collectMap.get(tmp.getString("msgSourceId")));
                        }
                    }
                }
            }
        }
    }

    /**
     * 修改消息为已读
     */
    public ObjectRestResponse<JSONObject> readMsg(Integer[] ids) {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();

        if (BeanUtil.isEmpty(ids)) {
            restResult.setStatus(400);
            restResult.setMessage("请指定待更新记录ID");
            return restResult;
        }

        this.mapper.updateMessageCenterList(ids, new Date(), BaseContextHandler.getUserID(),
            BaseContextHandler.getUsername());
        restResult.setStatus(200);
        restResult.setMessage("成功");
        return restResult;
    }

    /**
     * 按受理人或候选组添加消息通知
     * @param procBizData
     */
    public void addMsgCenterRecord(Map<String, Object> procBizData) {
        JSONObject procTaskBeanJObj = (JSONObject) procBizData.get("procTaskBean");
        JSONArray wfNextTasksBeanJArray = (JSONArray) procBizData.get("wfNextTasksBean");

        Set<String> nextAssignList = new HashSet<>();
        Set<String> nextCandidateGroupsList = new HashSet<>();
        Set<String> nextDeptIdList = new HashSet<>();
        Set<String> nextGridIdList = new HashSet<>();

        // 流程签收人或是候选组
        if (BeanUtil.isNotEmpty(wfNextTasksBeanJArray)) {
            for (int i = 0; i < wfNextTasksBeanJArray.size(); i++) {
                JSONObject procTaskBeanInArray = wfNextTasksBeanJArray.getJSONObject(i);

                if (StringUtils.isNotBlank(procTaskBeanInArray.getString("procTaskAssignee"))) {
                    nextAssignList.add(procTaskBeanInArray.getString("procTaskAssignee"));
                    continue;
                }

                if (StringUtils.isNotBlank(procTaskBeanInArray.getString("procDepartId"))
                        && !"0".equals(procTaskBeanInArray.getString("procDeptpermission"))) {
                    nextDeptIdList.add(procTaskBeanInArray.getString("procDepartId"));
                    continue;
                }
                if (StringUtils.isNotBlank(procTaskBeanInArray.getString("procSelfdata1"))
                        && !"0".equals(procTaskBeanInArray.getString("procSelfpermission1"))) {
                    nextGridIdList.add(procTaskBeanInArray.getString("procSelfdata1"));
                    continue;
                }
                if (StringUtils.isNotBlank(procTaskBeanInArray.getString("procTaskGroup"))) {
                    nextCandidateGroupsList.add(procTaskBeanInArray.getString("procTaskGroup"));
                }
            }
        }

        /*
         * 在工作流系统中，com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskBean中业务ID叫procBizid
         * com.bjzhianjia.scp.security.wf.base.vo.WfProcBizDataBean中业务ID叫procBizId
         * 注意ID在小写
         */
        String procBizId = procTaskBeanJObj.getString("procBizid");
        String procKey = procTaskBeanJObj.getString("procKey");

        List<MessageCenter> listToInsert = new ArrayList<>();

        // 查询业务
        MessageCenter fakeMessageCenter = new MessageCenter();
        switch (procKey) {
            case "comprehensiveManage":
                // 事件流
                CaseInfo caseInfo = caseInfoMapper.selectByPrimaryKey(Integer.valueOf(procBizId));
                fakeMessageCenter.setMsgSourceId(String.valueOf(caseInfo.getId()));
                fakeMessageCenter.setMsgSourceType("case_info_00");
                fakeMessageCenter.setMsgName(caseInfo.getCaseTitle());
                fakeMessageCenter.setMsgDesc(caseInfo.getCaseDesc());
                fakeMessageCenter.setTaskTime(caseInfo.getOccurTime());
                break;
            case "LawEnforcementProcess":
                // 案件流
                CaseRegistration caseRegistration =
                        caseRegistrationMapper.selectByPrimaryKey(procBizId);
                fakeMessageCenter.setMsgSourceId(String.valueOf(caseRegistration.getId()));
                fakeMessageCenter.setMsgSourceType("case_registration_00");
                fakeMessageCenter.setMsgName(caseRegistration.getCaseName());
                fakeMessageCenter.setMsgDesc("");
                fakeMessageCenter.setTaskTime(caseRegistration.getCaseTime());
                break;
        }

        fakeMessageCenter.setIsRead("0");
        fakeMessageCenter.setIsDeleted("0");
        fakeMessageCenter.setCrtTime(new Date());
        fakeMessageCenter.setTenantId(BaseContextHandler.getTenantID());

        // 按受理人生成消息对象
        if (BeanUtil.isNotEmpty(nextAssignList)) {
            for (String nextAssign : nextAssignList) {
                MessageCenter messageCenter =
                        BeanUtil.copyBean_New(fakeMessageCenter, new MessageCenter());
                messageCenter.setCrtUserId(nextAssign);
                listToInsert.add(messageCenter);
            }
        }

        // 按候选组生成消息对象
        List<JSONObject> userJObjList = null;
        if (BeanUtil.isNotEmpty(nextCandidateGroupsList)) {
            userJObjList = adminFeign.selectLeaderOrMemberByGroup(String.join(",", nextCandidateGroupsList));
        }
        if (BeanUtil.isNotEmpty(userJObjList)) {
            List<String> userIds = userJObjList.stream().map(o -> o.getString("userId")).collect(Collectors.toList());
            for (String userId : userIds) {
                MessageCenter messageCenter =
                        BeanUtil.copyBean_New(fakeMessageCenter, new MessageCenter());
                messageCenter.setCrtUserId(userId);
                listToInsert.add(messageCenter);
            }
        }

        // 按部门生成消息对象
        List<JSONObject> usersByDeptIds = null;
        if (BeanUtil.isNotEmpty(nextDeptIdList)) {
            usersByDeptIds = adminFeign.getUsersByDeptIds(String.join(",", nextDeptIdList));
            if (BeanUtil.isNotEmpty(usersByDeptIds)) {
                List<String> userIds = usersByDeptIds.stream().map(o -> o.getString("userId")).distinct().collect(Collectors.toList());

                for (String userId : userIds) {
                    MessageCenter messageCenter =
                            BeanUtil.copyBean_New(fakeMessageCenter, new MessageCenter());
                    messageCenter.setCrtUserId(userId);
                    listToInsert.add(messageCenter);
                }
            }
        }

        // 按网格生成消息对象
        if(BeanUtil.isNotEmpty(nextGridIdList)){
            List<AreaGridMember> byGridIds = areaGridMemberBiz.getByGridIds(nextGridIdList);
            if(BeanUtil.isNotEmpty(byGridIds)){
                List<String> memberCollect = byGridIds.stream().map(o -> o.getGridMember()).distinct().collect(Collectors.toList());
                for(String member:memberCollect){
                    MessageCenter messageCenter =
                            BeanUtil.copyBean_New(fakeMessageCenter, new MessageCenter());
                    messageCenter.setCrtUserId(member);
                    listToInsert.add(messageCenter);
                }
            }
        }

        // 添加消息记录
        if (BeanUtil.isNotEmpty(listToInsert)) {
            addMessageCenterListToDB(listToInsert);
        }
    }

    /**
     * 当执行的任务进行状态改变时，调用该方法插入新状态的消息记录<br/>
     * 如:某一事件进行了催办，该方法会在原事件的基础上，添加一条催办记录
     * @param messageCenterToInsertTemplate
     */
    public void addMsgCenterRecord(MessageCenter messageCenterToInsertTemplate, JSONObject sourceJObj){
        if (StringUtils.isBlank(messageCenterToInsertTemplate.getMsgSourceId())
            || StringUtils.isBlank(messageCenterToInsertTemplate.getMsgSourceType())) {
            throw new BizException("请指定待更新消息来源");
        }

        Example example = new Example(MessageCenter.class);
        example.createCriteria().andEqualTo("isDeleted", "0")
            .andEqualTo("msgSourceType", sourceJObj.getString("msgSourceType"))
            .andEqualTo("msgSourceId", messageCenterToInsertTemplate.getMsgSourceId());

        List<MessageCenter> messageCentersInDB = this.selectByExample(example);
        if(BeanUtil.isNotEmpty(messageCentersInDB)){
            for(MessageCenter tmp:messageCentersInDB){
                tmp.setMsgSourceType(messageCenterToInsertTemplate.getMsgSourceType());
                tmp.setIsRead("0");
                tmp.setCrtTime(new Date());
            }
        }

        // 当待添加对象不为空时，才执行添加操作
        if(BeanUtil.isNotEmpty(messageCentersInDB)){
            addMessageCenterListToDB(messageCentersInDB);
        }
    }

    /**
     * 添加消息
     * 该方法消息来源于业务服务，与工作流无关
     * @param procBizData
     */
    public void add12345Msg(MessageCenter simpleMsg) {
        if(BeanUtil.isEmpty(simpleMsg)){
            return ;
        }

        simpleMsg.setIsRead("0");
        simpleMsg.setIsDeleted("0");
        simpleMsg.setCrtTime(new Date());
        simpleMsg.setTenantId(BaseContextHandler.getTenantID());

        List<MessageCenter> listToInsert = new ArrayList<>();

        List<JSONObject> leaderOrMemberByGroupCode =
            adminFeign
                .getLeaderOrMemberByGroupCode(environment.getProperty("baseGroup.code.mayerLine"));

        if(BeanUtil.isEmpty(leaderOrMemberByGroupCode)){
            // 说明没有人需要接收到消息
            return;
        }

        List<String> userIdList =
            leaderOrMemberByGroupCode.stream().map(o -> o.getString("userId")).distinct()
                .collect(Collectors.toList());

        for (String userId : userIdList) {
            MessageCenter messageCenter = new MessageCenter();

            BeanUtils.copyProperties(simpleMsg, messageCenter);
            messageCenter.setCrtUserId(userId);
            listToInsert.add(messageCenter);
        }

        // 添加消息记录
        if (BeanUtil.isNotEmpty(listToInsert)) {
            addMessageCenterListToDB(listToInsert);
        }
    }

    /**
     * 添加执法任务消息
     * 该方法消息来源于业务服务，与工作流无关
     * @param simpleMsg
     */
    public void addLawTaskMessage(MessageCenter simpleMsg) {
        if(BeanUtil.isEmpty(simpleMsg)){
            return ;
        }

        simpleMsg.setIsRead("0");
        simpleMsg.setIsDeleted("0");
        simpleMsg.setCrtTime(new Date());
        simpleMsg.setTenantId(BaseContextHandler.getTenantID());

        List<MessageCenter> listToInsert = new ArrayList<>();

        List<LawExecutePerson> lawExecutePeople =
            lawExecutePersonBiz.getByLawTaskId(simpleMsg.getMsgSourceId());

        List<String> userIdList =
            lawExecutePeople.stream().map(o -> o.getUserId()).distinct()
                .collect(Collectors.toList());

        for (String userId : userIdList) {
            MessageCenter messageCenter = new MessageCenter();

            BeanUtils.copyProperties(simpleMsg, messageCenter);
            messageCenter.setCrtUserId(userId);
            listToInsert.add(messageCenter);
        }

        // 添加消息记录
        if (BeanUtil.isNotEmpty(listToInsert)) {
            addMessageCenterListToDB(listToInsert);
        }
    }

    /**
     * 将记录添加到数据中
     * @param list
     */
    private void addMessageCenterListToDB(List<MessageCenter> list){
        if(BeanUtil.isEmpty(list)){
            return;
        }
        
        // TODO 主动推送以后实现
        // pushMessageToWEB(list);
        // pushMessageToAPP(list);
        this.mapper.addMessageCenterList(list);
    }

    /**
     * 将待添加到数据库中的消息以主动推送形式，推送给手机端
     * @param list 待推送消息
     */
    private void pushMessageToAPP(List<MessageCenter> list) {

    }

    /**
     * 将待添加到数据库中的消息以主动推送形式，推送给前端
     * @param list 待推送消息
     */
    private void pushMessageToWEB(List<MessageCenter> list){
        List<JSONObject> messageAndReceiver=new ArrayList<>();

        clearMsgAboartToWEB(list);

        try {
            list.forEach(tmp->{
                JSONObject jObj=new JSONObject();
                jObj.put("userId", tmp.getCrtUserId());
                jObj.put("message", JSONObject.toJSONString(tmp));
                messageAndReceiver.add(jObj);
            });
            // webSocketService.sendMessageTo(messageAndReceiver);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 清理
     * @param list
     */
    private List<JSONObject> clearMsgAboartToWEB(List<MessageCenter> list) {
        List<JSONObject> result=new ArrayList<>();

        /*
         * 事前事后核查，专项任务，执法任务，部门处理任务
         * 目前推送消息中并不包含--专项任务，执法任务，部门处理任务
         * 只需要实现事前事后核查即可
         */

        if(BeanUtil.isEmpty(list)){
            return new ArrayList<>();
        }

        Set<String> procCTaskCode=new HashSet<>();
        procCTaskCode.add("audit");
        procCTaskCode.add("finishCheck");

        List<String> caseInfoMsgSourceType = Arrays.asList(
                StringUtils.split(environment.getProperty("messageCenter.caseInfo"), ","));
        List<String> procBizId=new ArrayList<>();
        
        list.forEach(messageCenter -> {
            if (caseInfoMsgSourceType.contains(messageCenter.getMsgSourceType())) {
                procBizId.add(messageCenter.getMsgSourceId());
            }
        }

        );

        JSONObject objs=new JSONObject();
        objs.put("procKey", "comprehensiveManage");
        objs.put("procCTaskCode", procCTaskCode);
        objs.put("procBizId", procBizId);

        boolean flag=true;
        if(BeanUtil.isNotEmpty(procBizId)){
            // 当事件业务ID不为空时，才进行查询
            List<JSONObject> jsonObjects = wfMonitorService.selectByProcKeyAndCTaskCodeAndBizId(objs);
            if (BeanUtil.isNotEmpty(jsonObjects)) {
                List<String> collect =
                    jsonObjects.stream().map(o -> o.getString("procBizId")).distinct()
                        .collect(Collectors.toList());
                for (MessageCenter tmp : list) {
                    JSONObject jObjTmp =
                        JSONObject.parseObject(
                            JSONObject.toJSONString(tmp, SerializerFeature.WriteDateUseDateFormat));
                    if(collect.contains(tmp.getMsgSourceId())){
                        // 说明该事件不应该提醒到WEB
                        jObjTmp.put("isToAppOperator", true);
                    }else{
                        jObjTmp.put("isToAppOperator", false);
                    }
                    result.add(jObjTmp);
                }
                flag=false;
            }
        }

        if(flag){
            result =
                    list.stream()
                            .map(o -> JSONObject.parseObject(JSONObject.toJSONString(o)))
                            .collect(Collectors.toList());
        }

        return result;
    }

    /**
     * 清理
     * @param list
     */
    private void checkIsOperated(List<JSONObject> toCheck) {
        String[] procKeys={"comprehensiveManage","LawEnforcementProcess"};
        for(String procKey:procKeys){
            _checkIsOperated(toCheck,procKey);
        }
    }

    private void _checkIsOperated(List<JSONObject> toCheck,String procKey) {
        /*
         * 2-> 查询msgSourceId中待签收或已签收的数据
         * 3-> 如果步骤2中返回的业务id包含步骤1中的msgSourceId，则表明该任务还没被处理
         */
        JSONObject bizData=new JSONObject();
        bizData.put("prockey", procKey);

        JSONObject objs = this.initWorkflowQuery(bizData);

        PageInfo<WfProcBackBean> userAllToDoTasks = wfMonitorService.getUserAllToDoTasks(objs);

        List<String> todoBizId = new ArrayList<>();
        Map<String,WfProcBackBean> wrBizIdInstanceMap=new HashMap<>();
        List<String> procAssigneeList=new ArrayList<>();
        if(BeanUtil.isNotEmpty(userAllToDoTasks)){
            List<WfProcBackBean> list = userAllToDoTasks.getList();
            if(BeanUtil.isNotEmpty(list)){
                for(WfProcBackBean wfTmp:list){
                    todoBizId.add(wfTmp.getProcBizid());
                    procAssigneeList.add(wfTmp.getProcTaskAssignee());
                    wrBizIdInstanceMap.put(wfTmp.getProcBizid(), wfTmp);
                }
            }
        }

        Map<String,JSONObject> userIdInstance=new HashMap<>();
        if(BeanUtil.isNotEmpty(procAssigneeList)){
            JSONArray infoByUserIds = adminFeign.getInfoByUserIds(StringUtils.join(procAssigneeList, ","));
            if(BeanUtil.isNotEmpty(infoByUserIds)){
                for(int i=0;i<infoByUserIds.size();i++){
                    JSONObject jsonObject = infoByUserIds.getJSONObject(i);
                    userIdInstance.put(jsonObject.getString("userId"), jsonObject);
                }
            }
        }

        for(JSONObject tmp:toCheck){
            if(!todoBizId.contains(tmp.getString("msgSourceId"))){
                // 说明该条事件已被其它人处理
                tmp.put("isHasOperated", true);
            }else{
                tmp.put("isHasOperated", false);
            }
        }
    }
}
