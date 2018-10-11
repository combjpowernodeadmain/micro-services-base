package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.MessageCenter;
import com.bjzhianjia.scp.cgp.mapper.CaseInfoMapper;
import com.bjzhianjia.scp.cgp.mapper.CaseRegistrationMapper;
import com.bjzhianjia.scp.cgp.mapper.MessageCenterMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
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
public class MessageCenterBiz extends BusinessBiz<MessageCenterMapper, MessageCenter> {

    @Autowired
    private WfMonitorServiceImpl wfMonitorService;

    @Autowired
    private CaseInfoMapper caseInfoMapper;

    @Autowired
    private CaseRegistrationMapper caseRegistrationMapper;

    @Autowired
    private PropertiesProxy propertiesProxy;

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
    public TableResultResponse<JSONObject> getUnReadList(int page, int limit) {
        Example example = new Example(MessageCenter.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("isRead", "0");

        example.setOrderByClause("task_time asc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<MessageCenter> messageCenterList = this.selectByExample(example);
        List<JSONObject> result = new ArrayList<>();
        if (BeanUtil.isNotEmpty(messageCenterList)) {
            for (MessageCenter messageCenter : messageCenterList) {
                try {
                    JSONObject swapProperties =
                        propertiesProxy.swapProperties(messageCenter, "id", "msgName", "msgDesc", "taskTime");
                    result.add(swapProperties);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return new TableResultResponse<>(pageInfo.getTotal(), result);
        }

        return new TableResultResponse<>(0, new ArrayList<>());
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
}