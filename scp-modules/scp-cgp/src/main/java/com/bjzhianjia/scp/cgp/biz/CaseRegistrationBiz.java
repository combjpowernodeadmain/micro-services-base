package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.CLEConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.CLEConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.CaseAttachments;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.InspectItems;
import com.bjzhianjia.scp.cgp.entity.LawTask;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.WritsInstances;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.cgp.exception.BizException;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.feign.IUserFeign;
import com.bjzhianjia.scp.cgp.mapper.CaseAttachmentsMapper;
import com.bjzhianjia.scp.cgp.mapper.CaseRegistrationMapper;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.vo.CaseRegistrationVo;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.service.impl.WfMonitorServiceImpl;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskHistoryBean;
import com.bjzhianjia.scp.security.wf.base.task.service.impl.WfProcTaskServiceImpl;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 综合执法 - 案件登记
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:08
 */
@Service
public class CaseRegistrationBiz extends BusinessBiz<CaseRegistrationMapper, CaseRegistration> {

    @Autowired
    private CLEConcernedCompanyBiz cLEConcernedCompanyBiz;

    @Autowired
    private CLEConcernedPersonBiz cLEConcernedPersonBiz;

    @Autowired
    private WritsInstancesBiz writsInstancesBiz;

    @Autowired
    private WfMonitorServiceImpl wfMonitorService;

    @Autowired
    private DictFeign dictFeign;

    @Autowired
    private IUserFeign iUserFeign;

    @Autowired
    private AdminFeign adminFeign;

    @Autowired
    private EventTypeMapper eventTypeMapper;

    @Autowired
    private LawTaskBiz lawTaskBiz;

    @Autowired
    private CaseInfoBiz caseInfoBiz;

    @Autowired
    private ConcernedCompanyBiz concernedCompanyBiz;

    @Autowired
    private ConcernedPersonBiz concernedPersonBiz;

    @Autowired
    private InspectItemsBiz inspectItemsBiz;

    @Autowired
    private EventTypeBiz eventTypeBiz;

    @Autowired
    private AreaGridBiz areaGridBiz;

    @Autowired
    private WfProcTaskServiceImpl wfProcTaskService;

    @Autowired
    private WritsTemplatesBiz writsTemplatesBiz;
    @Autowired
    private CaseAttachmentsBiz caseAttachmentsBiz;

    /**
     * 添加立案记录<br/>
     * 如果 有当事人，则一并添加<br/>
     * 如果 有文书，则一并添加
     * 
     * @author 尚
     * @param procBizData
     */
    public Result<Void> addCase(JSONObject caseRegJObj) {
        Result<Void> result = new Result<>();

        // 添加当事人
        int concernedId = addConcerned(caseRegJObj);

        // 添加立案单
        CaseRegistration caseRegistration = JSON.parseObject(caseRegJObj.toJSONString(), CaseRegistration.class);
        // 生成caseRegistration主键
        String caseId = UUIDUtils.generateUuid();
        caseRegistration.setId(caseId);
        // 当事人主键
        if (concernedId != -1) {
            caseRegistration.setConcernedId(concernedId);
        }

        // 添加文书
        addWritsInstances(caseRegJObj, caseId);

        // 添加附件
        addAttachments(caseRegJObj, caseId);

        this.insertSelective(caseRegistration);
        // 将生成的立案ID装入procBizData带回工作流，在工作流中会对procBizId属性进行是否为“-1”的判断，如果是“-1”，将用该ID替换“-1”
        caseRegJObj.put("procBizId", caseId);

        result.setIsSuccess(true);
        return result;
    }

    /**
     * 如果请求信息中有附件信息，则将其保存到数据库中
     * @param caseRegJObj
     * @param caseId
     */
    private void addAttachments(JSONObject caseRegJObj, String caseId) {
        // 附件信息以JSON数组形式封装在attachments中
        JSONArray attachmentsJArray = caseRegJObj.getJSONArray("attachments");
        List<CaseAttachments> attachmentsList =new ArrayList<>();
        if(BeanUtil.isNotEmpty(attachmentsJArray)) {
            attachmentsList = attachmentsJArray.toJavaList(CaseAttachments.class);
        }
        
        if(BeanUtil.isNotEmpty(attachmentsList)) {
            caseAttachmentsBiz.add(attachmentsList);
        }
    }

    /**
     * 将请求信息中文书信息进行保存
     * 
     * @param caseRegJObj
     * @param caseId
     */
    private void addWritsInstances(JSONObject caseRegJObj, String caseId) {
        JSONArray writsInstancesJArray = caseRegJObj.getJSONArray("writsInstances");

        // 收集前端传入的模板tcode值，可能没有传入，如果没传，则该操作为更新操作
        List<String> tcodeList = new ArrayList<>();
        for (int i = 0; i < writsInstancesJArray.size(); i++) {
            String tcode = writsInstancesJArray.getJSONObject(i).getString("tcode");
            if (StringUtils.isNotBlank(tcode)) {
                tcodeList.add(tcode);
            }
        }

        Map<String, Integer> template_TCODE_ID_Map = new HashMap<>();
        if (BeanUtil.isNotEmpty(tcodeList)) {
            // 如果tcodeList集合为空，说明目前上审批状态，没有要添加新文书的操作，以下将进行更新操作
            List<WritsTemplates> templateList = writsTemplatesBiz.getByTcodes(String.join(",", tcodeList));
            if (BeanUtil.isNotEmpty(templateList)) {
                template_TCODE_ID_Map =
                    templateList.stream().collect(Collectors.toMap(WritsTemplates::getTcode, WritsTemplates::getId));
            }
        }

        // 关联该文书相关的案件
        for (int i = 0; i < writsInstancesJArray.size(); i++) {
            JSONObject writsJObj = writsInstancesJArray.getJSONObject(i);
            WritsInstances writsInstances = writsJObj.toJavaObject(WritsInstances.class);
            writsInstances.setCaseId(caseId);

            /*
             * 判断文书是否进行过暂存(如果请求参数中有文书ID表明暂存过)<br/>
             * 或是一个更新操作（如果请求参数中有文书ID表明是更新操作）
             */
            if (BeanUtil.isEmpty(writsInstances.getId())) {
                /*
                 * 没有暂存过，进行一次添加操作<br/>
                 * 前端并没有办法获取文书ID，然后入的是文书的code值，
                 */
                if (BeanUtil.isEmpty(template_TCODE_ID_Map)) {
                    /*
                     * 如果程序执行到这里，总该有一条记录里tcode不为空，如果tcode为空，
                     * 说明没有进行暂存的那条记录也没有传tcode进来
                     */
                    throw new BizException("请指定模板ID或模板tcode");
                }
                writsInstances.setTemplateId(template_TCODE_ID_Map.get(writsJObj.getString("tcode")));
                writsInstancesBiz.insertSelective(writsInstances);
            } else {
                // 暂存过，进行更新操作，此时传入的参数中包含文书模板
                writsInstancesBiz.updateById(writsInstances);
            }
        }
    }

    /**
     * =生成文书fillContext
     * 
     * @param caseRegJObj
     * @param oldFillContext
     * @return
     */
    private String getWritsFillContext(JSONObject caseRegJObj, String oldFillContext, String ziHao) {
        JSONObject fillContextJObj = JSONObject.parseObject(oldFillContext);
        fillContextJObj = fillContextJObj == null ? new JSONObject() : fillContextJObj;

        CaseRegistration caseRegistration = JSON.parseObject(caseRegJObj.toJSONString(), CaseRegistration.class);

        // 新综立字
        // fillContextJObj.put("XinZLZi",
        // caseRegJObj.getString("XinZLZi") == null ? "" :
        // caseRegJObj.getString("XinZLZi"));
        // fillContextJObj.put("ZiHao",
        // caseRegJObj.getString("ZiHao") == null ? "" :
        // caseRegJObj.getString("ZiHao") + ziHao);
        /*
         * ======================== 案件相关字段信息=============开始=================
         */
        fillContextJObj.put("caseSource",
            caseRegistration.getCaseSource() == null ? "" : caseRegistration.getCaseSource());
        fillContextJObj.put("caseSourceTime",
            caseRegistration.getCaseSourceTime() == null ? "" : caseRegistration.getCaseSourceTime());
        fillContextJObj.put("caseInformer",
            caseRegistration.getCaseInformer() == null ? "" : caseRegistration.getCaseInformer());
        fillContextJObj.put("caseInformerPhone",
            caseRegistration.getCaseInformerPhone() == null ? "" : caseRegistration.getCaseInformerPhone());
        fillContextJObj.put("caseInfomerAddr",
            caseRegistration.getCaseInformerAddr() == null ? "" : caseRegistration.getCaseInformerAddr());
        fillContextJObj.put("caseAddress",
            caseRegistration.getCaseAddress() == null ? "" : caseRegistration.getCaseAddress());
        fillContextJObj.put("caseTime", caseRegistration.getCaseTime() == null ? "" : caseRegistration.getCaseTime());
        fillContextJObj.put("caseContend",
            caseRegistration.getCaseContent() == null ? "" : caseRegistration.getCaseContent());
        fillContextJObj.put("dealSuggest",
            caseRegistration.getDealSuggest() == null ? "" : caseRegistration.getDealSuggest());
        fillContextJObj.put("enforcers",
            caseRegistration.getEnforcers() == null ? "" : caseRegistration.getEnforcers());
        /*
         * ======================== 案件相关字段信息=============结束=================
         */

        /*
         * =三级审批信息===================开始===============
         */
        fillContextJObj.put("SquadronLeaderSuggest", caseRegJObj.getString("SquadronLeaderSuggest") == null ? ""
            : caseRegJObj.getString("SquadronLeaderSuggest"));
        fillContextJObj.put("SquadronLeader",
            caseRegJObj.getString("SquadronLeader") == null ? "" : caseRegJObj.getString("SquadronLeader"));
        fillContextJObj.put("TownLeaderSuggest",
            caseRegJObj.getString("TownLeaderSuggest") == null ? "" : caseRegJObj.getString("TownLeaderSuggest"));
        fillContextJObj.put("TownLeader",
            caseRegJObj.getString("TownLeader") == null ? "" : caseRegJObj.getString("TownLeader"));
        fillContextJObj.put("remark", caseRegJObj.getString("remark") == null ? "" : caseRegJObj.getString("remark"));
        /*
         * =三级审批信息===================结束===============
         */

        /*
         * =判断是否传入当事人信息
         */
        JSONObject concernedJObj = caseRegJObj.getJSONObject("concerned");
        if (concernedJObj == null) concernedJObj = new JSONObject();

        // 当事人以单位形式存在
        CLEConcernedCompany concernedCompany =
            JSON.parseObject(concernedJObj.toJSONString(), CLEConcernedCompany.class);

        fillContextJObj.put("companyName", concernedCompany.getName() == null ? "" : concernedCompany.getName());
        fillContextJObj.put("legalPerson",
            concernedCompany.getLegalPerson() == null ? "" : concernedCompany.getLegalPerson());
        fillContextJObj.put("leadPerson",
            concernedCompany.getLeadPerson() == null ? "" : concernedCompany.getLeadPerson());
        fillContextJObj.put("duties", concernedCompany.getDuties() == null ? "" : concernedCompany.getDuties());
        fillContextJObj.put("concernedPhone", concernedCompany.getPhone() == null ? "" : concernedCompany.getPhone());
        fillContextJObj.put("concernedAddr",
            concernedCompany.getAddress() == null ? "" : concernedCompany.getAddress());

        // 当事人以人个形式存在
        CLEConcernedPerson concernedPerson = JSON.parseObject(concernedJObj.toJSONString(), CLEConcernedPerson.class);
        fillContextJObj = fillContextJObj == null ? new JSONObject() : fillContextJObj;

        fillContextJObj.put("concernedPersonName", concernedPerson.getName() == null ? "" : concernedPerson.getName());
        fillContextJObj.put("concernedCredNum",
            concernedPerson.getCertCode() == null ? "" : concernedPerson.getCertCode());
        fillContextJObj.put("concernedPhone", concernedPerson.getPhone() == null ? "" : concernedPerson.getPhone());
        fillContextJObj.put("concernedAddr", concernedPerson.getAddress() == null ? "" : concernedPerson.getAddress());

        return fillContextJObj.toString();
    }

    /**
     * 添加当事人记录
     * 
     * @author 尚
     * @param caseRegJObj
     * @return 返回数据库自增长的ID，如果没有进行插入数据 操作则返回-1
     */
    private int addConcerned(JSONObject caseRegJObj) {
        int resultId = -1;

        // 判断是否传入当事人信息
        JSONObject concernedJObj = caseRegJObj.getJSONObject("concerned");
        if (concernedJObj != null) {
            // 有当事人信息
            String concernedType = caseRegJObj.getString("concernedType");

            switch (concernedType) {
                case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG:
                    // 当事人以单位形式存在
                    CLEConcernedCompany concernedCompany =
                        JSON.parseObject(concernedJObj.toJSONString(), CLEConcernedCompany.class);
                    cLEConcernedCompanyBiz.insertSelective(concernedCompany);
                    resultId = concernedCompany.getId();
                    break;
                case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON:
                    // 当事人以人个形式存在
                    CLEConcernedPerson concernedPerson =
                        JSON.parseObject(concernedJObj.toJSONString(), CLEConcernedPerson.class);
                    cLEConcernedPersonBiz.insertSelective(concernedPerson);
                    resultId = concernedPerson.getId();
                    break;
            }
        }

        return resultId;
    }

    /**
     * 分页获取列表
     * 
     * @author 尚
     * @param ids
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<CaseRegistrationVo> getListByIds(String ids, int page, int limit) {
        Set<String> idSet = new HashSet<>();

        String[] split = ids.split(",");
        for (String string : split) {
            idSet.add(string);
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseRegistrationVo> list = this.mapper.getListByIds(idSet, page, limit);

        return new TableResultResponse<CaseRegistrationVo>(pageInfo.getTotal(), list);
    }

    /**
     * 按执法人分页查询对象
     * 
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<CaseRegistration> getListByExecutePerson(String userId, int page, int limit) {
        // enforcers
        TableResultResponse<CaseRegistration> restResult = new TableResultResponse<>();

        Example example = new Example(CaseRegistration.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("enforcers", userId);

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseRegistration> rows = this.selectByExample(example);

        restResult.getData().setTotal(pageInfo.getTotal());
        restResult.getData().setRows(rows);
        restResult.setStatus(200);
        return restResult;
    }

    /**
     * 按分页获取记录
     * 
     * @param caseRegistration
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<CaseRegistration> getList(CaseRegistration caseRegistration, int page, int limit) {
        Example example = new Example(CaseRegistration.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (caseRegistration.getGirdId() != null) {
            criteria.andEqualTo("girdId", caseRegistration.getGirdId());
        }

        example.setOrderByClause("id desc");
        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseRegistration> rows = this.selectByExample(example);

        return new TableResultResponse<CaseRegistration>(pageInfo.getTotal(), rows);
    }

    /**
     * 查询个人待办任务(工作流)
     * 
     * @param objs
     *            {"bizData":{}, "procData":{}, "authData":{"procAuthType":"2"},
     *            "variableData":{}, "queryData":{ } }<br/>
     * @return
     */
    public TableResultResponse<JSONObject> getUserToDoTasks(JSONObject objs) {
        List<JSONObject> result = new ArrayList<>();

        CaseRegistration queryCaseRegistration = new CaseRegistration();
        // 工作流查询条件
        JSONObject bizData = objs.getJSONObject("bizData");
        // 事件工作流的定义代码
        bizData.put("prockey", "LawEnforcementProcess");
        objs.put("bizData", bizData);

        // 查询待办工作流任务
        PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getUserAllToDoTasks(objs);
        List<WfProcBackBean> wfProcBackBeanList = pageInfo.getList();

        List<String> commiterList =
            wfProcBackBeanList.stream().map(o -> o.getProcTaskCommitter()).distinct().collect(Collectors.toList());
        Map<String, String> userMap = adminFeign.getUser(String.join(",", commiterList));
        if (userMap == null) {
            userMap = new HashMap<>();
        }

        if (wfProcBackBeanList != null && !wfProcBackBeanList.isEmpty()) {

            // 查询业务列表
            // 业务ids
            Set<String> bizIds = this.getBizIds(wfProcBackBeanList);
            // 查询与工作流任务对应的业务
            TableResultResponse<CaseRegistration> tableResult =
                this.getList(queryCaseRegistration, bizIds, objs.getJSONObject("queryData"));
            List<CaseRegistration> caseRegistrationList = tableResult.getData().getRows();
            if (caseRegistrationList != null && !caseRegistrationList.isEmpty()) {
                // 封装业务数据
                Map<String, CaseRegistration> caseRegistration_ID_Entity_Map = new HashMap<>();
                for (CaseRegistration caseRegistration : caseRegistrationList) {
                    caseRegistration_ID_Entity_Map.put(caseRegistration.getId(), caseRegistration);
                }

                JSONObject obj = null;
                CaseRegistration caseRegistration = null;
                String isUrge = "0";
                String isSupervise = "0";
                WfProcBackBean wfProcBackBean = null;
                for (int i = 0; i < wfProcBackBeanList.size(); i++) {
                    wfProcBackBean = wfProcBackBeanList.get(i);
                    // 案件id
                    String bizId = wfProcBackBean.getProcBizid();
                    caseRegistration = caseRegistration_ID_Entity_Map.get(bizId);
                    // 没有匹配业务数据，则跳过
                    if (caseRegistration == null) continue;
                    obj = JSONObject.parseObject(JSON.toJSONString(wfProcBackBean));
                    obj.put("procTaskCommitterName",
                        CommonUtil.getValueFromJObjStr(userMap.get(wfProcBackBean.getProcTaskCommitter()), "name"));

                    // 封装督办催办
                    if (caseRegistration != null) {
                        isUrge = caseRegistration.getIsSupervise();
                        isSupervise = caseRegistration.getIsUrge();
                    }
                    obj.put("isSupervise", isUrge);
                    obj.put("isUrge", isSupervise);
                    //完成期限
                    Date caseEnd = caseRegistration.getCaseEnd();
                    String exeStatus = caseRegistration.getExeStatus();
                    //默认没有超时
                    boolean isOvertime = false;
                    if(caseEnd != null) {
                        //处理结束
                        if(CaseRegistration.EXESTATUS_STATE_STOP.equals(exeStatus) || CaseRegistration.EXESTATUS_STATE_FINISH.equals(exeStatus) ) {
                            //结案时间
                            Date finishTime = caseRegistration.getUpdTime();
                            isOvertime = caseEnd.before(finishTime);
                        }else {
                            //处理中
                            isOvertime = caseEnd.before(new Date());
                        }
                    }
                    //是否超时
                    obj.put("isOvertime", isOvertime);
                    result.add(obj);
                }
                return new TableResultResponse<>(tableResult.getData().getTotal(), result);
            } else {
                // 无待办任务
                return new TableResultResponse<>(0, result);
            }
        } else {
            // 无待办任务
            return new TableResultResponse<>(0, result);
        }
    }

    /**
     * 查询所有用户待办任务(工作流)
     * 
     * @param objs
     *            {"bizData":{}, "procData":{}, "authData":{"procAuthType":"2"},
     *            "variableData":{}, "queryData":{ } }<br/>
     * @return
     */
    public TableResultResponse<JSONObject> getUserAllToDoTasks(JSONObject objs) {
        List<JSONObject> result = new ArrayList<>();

        // 业务查询条件
        JSONObject queryData = objs.getJSONObject("queryData");
        CaseRegistration queryCaseRegistration = new CaseRegistration();
        queryCaseRegistration = JSONObject.parseObject(queryData.toJSONString(), CaseRegistration.class);

        // 工作流查询条件
        JSONObject bizData = objs.getJSONObject("bizData");
        // 事件工作流的定义代码
        bizData.put("prockey", "LawEnforcementProcess");
        objs.put("bizData", bizData);
        if (StringUtils.isNotBlank(queryData.getString("procCtaskname"))) {
            // 是否按进度进行查找(即任务表中·PROC_CTASKNAME·字段)
            bizData = objs.getJSONObject("bizData");
            bizData.put("procCtaskname", queryData.getString("procCtaskname"));
            objs.put("bizData", bizData);
        }

        // 查询所有工作流任务
        PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getAllToDoTasks(objs);
        List<WfProcBackBean> wfProcBackBeanList = pageInfo.getList();

        if (wfProcBackBeanList != null && !wfProcBackBeanList.isEmpty()) {
            // 有待办任务
            return queryAssist(queryCaseRegistration, queryData, result, wfProcBackBeanList);
        } else {
            // 无待办任务
            return new TableResultResponse<>(0, result);
        }
    }

    /**
     * 案件综合查询
     * 
     * @param objs
     *            {"bizData":{}, "procData":{}, "authData":{"procAuthType":"2"},
     *            "variableData":{}, "queryData":{ } }<br/>
     * @return
     */
    public TableResultResponse<JSONObject> getAllTasks(JSONObject objs) {
        List<JSONObject> result = new ArrayList<>();

        // 业务查询条件
        JSONObject queryData = objs.getJSONObject("queryData");
        CaseRegistration queryCaseRegistration = new CaseRegistration();
        queryCaseRegistration = JSONObject.parseObject(queryData.toJSONString(), CaseRegistration.class);

        // 工作流查询条件
        JSONObject bizData = objs.getJSONObject("bizData");
        // 事件工作流的定义代码
        bizData.put("prockey", "LawEnforcementProcess");
        objs.put("bizData", bizData);
        if (StringUtils.isNotBlank(queryData.getString("procCtaskname"))) {
            // 是否按进度进行查找(即任务表中·PROC_CTASKNAME·字段)
            bizData = objs.getJSONObject("bizData");
            bizData.put("procCtaskname", queryData.getString("procCtaskname"));
            objs.put("bizData", bizData);
        }

        // 查询所有工作流任务
        PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getAllTasks(objs);
        List<WfProcBackBean> wfProcBackBeanList = pageInfo.getList();

        if (wfProcBackBeanList != null && !wfProcBackBeanList.isEmpty()) {
            // 有待办任务
            return queryAssist(queryCaseRegistration, queryData, result, wfProcBackBeanList);
        } else {
            // 无待办任务
            return new TableResultResponse<>(0, result);
        }
    }

    /**
     * 工作流中获取业务ids
     * 
     * @param procBackBeanList
     *            工作流列表
     * @return
     */
    private Set<String> getBizIds(List<WfProcBackBean> procBackBeanList) {
        Set<String> bizIds = new HashSet<>();
        if (procBackBeanList != null && !procBackBeanList.isEmpty()) {
            for (int i = 0; i < procBackBeanList.size(); i++) {
                WfProcBackBean wfProcBackBean = procBackBeanList.get(i);
                try {
                    bizIds.add(wfProcBackBean.getProcBizid());
                } catch (NumberFormatException e) {
                    continue;
                }
            }
        }
        return bizIds;
    }

    /**
     * 联合查询，返回前端json
     * 
     * @param queryCaseRegistration
     *            业务条件
     * @param queryData
     *            业务条件
     * @param result
     *            前端结果集
     * @param procBackBeanList
     *            工作流任列表
     * @return
     */
    private TableResultResponse<JSONObject> queryAssist(CaseRegistration queryCaseRegistration, JSONObject queryData,
        List<JSONObject> result, List<WfProcBackBean> procBackBeanList) {

        // 封装工作流任务
        Map<String, WfProcBackBean> wfProcBackBean_ID_Entity_Map = new HashMap<>();
        for (WfProcBackBean wfProcBackBean : procBackBeanList) {
            wfProcBackBean_ID_Entity_Map.put(wfProcBackBean.getProcBizid(), wfProcBackBean);
        }

        // 查询业务列表
        // 业务ids
        Set<String> bizIds = this.getBizIds(procBackBeanList);
        // 查询与工作流任务对应的业务
        TableResultResponse<CaseRegistration> bizResult = this.getList(queryCaseRegistration, bizIds, queryData);
        List<CaseRegistration> caseRegistrationList = bizResult.getData().getRows();

        // 数据字典code封装
        List<String> eventTypeIdStrList = new ArrayList<>(); // 案件类别
        Set<String> rootBizIdSet = new HashSet<>(); // 数据字典code
        for (CaseRegistration caseRegistration : caseRegistrationList) {
            rootBizIdSet.add(caseRegistration.getBizType());
            rootBizIdSet.add(caseRegistration.getCaseSourceType());
            eventTypeIdStrList.add(caseRegistration.getEventType());
        }

        // 查询业务条线，查询案件来源
        Map<String, String> rootBizList = new HashMap<>();
        if (rootBizIdSet != null && !rootBizIdSet.isEmpty()) {
            rootBizList = dictFeign.getByCodeIn(String.join(",", rootBizIdSet));
        }

        // 查询案件类别
        Map<String, String> eventType_ID_NAME_Map = new HashMap<>();
//        String eventTypeName = "";
        if (eventTypeIdStrList != null && !eventTypeIdStrList.isEmpty()) {
            List<EventType> eventTypeList = new ArrayList<>();
            eventTypeList = eventTypeMapper.selectByIds(String.join(",", eventTypeIdStrList));
//            List<String> eventTypeNameList = new ArrayList<>();
            for (EventType eventType : eventTypeList) {
//                if (StringUtils.isNotBlank(eventType.getTypeName())) {
//                    eventTypeNameList.add(eventType.getTypeName());
//                }
                eventType_ID_NAME_Map.put(String.valueOf(eventType.getId()), eventType.getTypeName());
            }
//            eventTypeName = String.join(",", eventTypeNameList);
        }

        JSONObject objResult = null;
        WfProcBackBean wfProcBackBean = null;
        for (CaseRegistration caseRegistration : caseRegistrationList) {
            objResult = JSONObject.parseObject(JSON.toJSONString(caseRegistration));
            wfProcBackBean = wfProcBackBean_ID_Entity_Map.get(objResult.get("id"));

            // 定位坐标
            objResult.put("mapInfo", "{\"lng\":\"" + caseRegistration.getCaseOngitude() + "\",\"lat\":\""
                + caseRegistration.getCaseLatitude() + "\"}");

            // 处理状态
            String procCtaskname = "";
            if (wfProcBackBean != null) {
                procCtaskname = wfProcBackBean.getProcCtaskname();
                if (CaseRegistration.EXESTATUS_STATE_FINISH.equals(caseRegistration.getExeStatus())) {
                    procCtaskname = "已结案";
                }

                if (CaseRegistration.EXESTATUS_STATE_STOP.equals(caseRegistration.getExeStatus())) {
                    procCtaskname = "已终止";
                }
                objResult.put("procCtaskname", procCtaskname);
                objResult.put("procInstId", wfProcBackBean.getProcInstId());
                objResult.put("procBizid", wfProcBackBean.getProcBizid());
            }

            // 业务条线
            objResult.put("bizListName", getRootBizTypeName(caseRegistration.getBizType(), rootBizList));
            // 事件类别
            objResult.put("eventTypeListName",
                getEventTypeName(eventType_ID_NAME_Map, caseRegistration.getEventType()));
            // 事件来源
            objResult.put("sourceTypeName", getRootBizTypeName(caseRegistration.getCaseSourceType(), rootBizList));
            // 具体来源id
            String sourceId = caseRegistration.getCaseSource();
            // 具体来源标题
            String sourceTitle = "";
            // 执法任务
            if (StringUtils.isNotBlank(sourceId)) {
                if (CaseRegistration.CASE_SOURCE_TYPE_TASK.equals(caseRegistration.getCaseSourceType())) {
                    LawTask lawTask = lawTaskBiz.selectById(Integer.valueOf(sourceId));
                    if (lawTask != null) {
                        sourceTitle = lawTask.getLawTitle();
                    }
                } else if (CaseRegistration.CASE_SOURCE_TYPE_CENTER.equals(caseRegistration.getCaseSourceType())) { // 中心交办
                    CaseInfo caseInfo = caseInfoBiz.selectById(Integer.valueOf(sourceId));
                    if (caseInfo != null) {
                        sourceTitle = caseInfo.getCaseTitle();
                    }
                }
            }
            objResult.put("sourceId", sourceId);
            objResult.put("sourceTitle", sourceTitle);
            objResult.put("isUrge", "0".equals(caseRegistration.getIsUrge()) ? false : true);
            objResult.put("isSupervise", "0".equals(caseRegistration.getIsSupervise()) ? false : true);
            //完成期限
            Date caseEnd = caseRegistration.getCaseEnd();
            String exeStatus = caseRegistration.getExeStatus();
            //默认没有超时
            boolean isOvertime = false;
            if(caseEnd != null) {
                //处理结束
                if(CaseRegistration.EXESTATUS_STATE_STOP.equals(exeStatus) || CaseRegistration.EXESTATUS_STATE_FINISH.equals(exeStatus) ) {
                    //结案时间
                    Date finishTime = caseRegistration.getUpdTime();
                    isOvertime = caseEnd.before(finishTime);
                }else {
                    //处理中
                    isOvertime = caseEnd.before(new Date());
                }
            }
            //是否超时
            objResult.put("isOvertime", isOvertime);
            result.add(objResult);
        }
        return new TableResultResponse<>(bizResult.getData().getTotal(), result);
    }

    private String getEventTypeName(Map<String, String> eventType_ID_NAME_Map, String eventTypeList) {
        if (eventTypeList != null) {
            List<String> nameList = new ArrayList<>();
            String[] split = eventTypeList.split(",");
            for (String string : split) {
                nameList.add(eventType_ID_NAME_Map.get(string));
            }

            return String.join(",", nameList);
        }
        return "";
    }

    /**
     * 通过条件查询案件
     * 
     * @param caseRegistration
     *            案件查询信息
     * @param ids
     *            案件id
     * @param queryData
     *            查询条件
     * @return
     */
    public TableResultResponse<CaseRegistration> getList(CaseRegistration caseRegistration, Set<String> ids,
        JSONObject queryData) {
        // 查询参数
        int page = StringUtils.isBlank(queryData.getString("page")) ? 1 : Integer.valueOf(queryData.getString("page"));
        int limit =
            StringUtils.isBlank(queryData.getString("limit")) ? 10 : Integer.valueOf(queryData.getString("limit"));
        String startQueryTime = queryData.getString("startQueryTime");
        String endQueryTime = queryData.getString("endQueryTime");

        String isSupervise = queryData.getString("isSupervise");
        String isUrge = queryData.getString("isUrge");
        String exeStatus = queryData.getString("procCtaskname");// 1:已结案2:已终止
        String caseSourceType = queryData.getString("caseSourceType");// 来源类型
        String caseSource = queryData.getString("caseSource");// 来源id

        Example example = new Example(CaseRegistration.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (StringUtils.isNotBlank(caseRegistration.getCaseName())) {
            criteria.andLike("caseName", "%" + caseRegistration.getCaseName() + "%");
        }
        if (StringUtils.isNotBlank(caseRegistration.getBizType())) {
            criteria.andLike("bizType", "%" + caseRegistration.getBizType() + "%");
        }
        if (StringUtils.isNotBlank(caseRegistration.getEventType())) {
            criteria.andLike("eventType", "%" + caseRegistration.getEventType() + "%");
        }
        if (StringUtils.isNotBlank(caseRegistration.getCaseSourceType())) {
            criteria.andEqualTo("caseSourceType", caseRegistration.getCaseSourceType());
        }
        if (StringUtils.isNotBlank(caseRegistration.getCaseSource())) {
            criteria.andEqualTo("caseSource", caseRegistration.getCaseSource());
        }
        if (!(StringUtils.isBlank(startQueryTime) || StringUtils.isBlank(endQueryTime))) {
            Date start = DateUtil.dateFromStrToDate(startQueryTime, "yyyy-MM-dd HH:mm:ss");
            Date end = DateUtils.addDays(DateUtil.dateFromStrToDate(endQueryTime, "yyyy-MM-dd HH:mm:ss"), 1);
            criteria.andBetween("case_source_time", start, end);
        }
        if (ids != null && !ids.isEmpty()) {
            criteria.andIn("id", ids);
        }
        // 是否添加督办 (1:是|0:否)
        if (StringUtils.isNotBlank(isSupervise) && "1".equals(isSupervise)) {
            criteria.andEqualTo("isSupervise", caseRegistration.getIsSupervise());
        }
        // 是否添加崔办(1:是|0:否)
        if (StringUtils.isNotBlank(isUrge) && "1".equals(isUrge)) {
            criteria.andEqualTo("isUrge", caseRegistration.getIsUrge());
        }
        // 处理状态：0处理中|1:已结案2:已终止
        if (StringUtils.isNotBlank(exeStatus) && !CaseRegistration.EXESTATUS_STATE_TODO.equals(exeStatus)) {
            // 只查询1:已结案2:已终止
            if (CaseRegistration.EXESTATUS_STATE_FINISH.equals(queryData.getString("procCtaskname"))
                && CaseRegistration.EXESTATUS_STATE_STOP.equals(queryData.getString("procCtaskname"))) {
                criteria.andEqualTo("exeStatus", exeStatus);
            }
        }
        // 处理状态
        if (StringUtils.isNotBlank(caseSourceType)) {
            criteria.andEqualTo("caseSourceType", caseSourceType);
        }

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<CaseRegistration> list = this.mapper.selectByExample(example);

        return new TableResultResponse<CaseRegistration>(pageInfo.getTotal(), list);
    }

    /**
     * 通过数据字典code获取value
     * 
     * @param codes
     * @param rootBizList
     * @return
     */
    private String getRootBizTypeName(String codes, Map<String, String> rootBizList) {
        if (StringUtils.isNotBlank(codes)) {
            String[] split = codes.split(",");
            List<String> nameList = new ArrayList<>();
            for (String string : split) {
                nameList.add(rootBizList.get(string));
            }
            return String.join(",", nameList);
        }
        return "";
    }

    /**
     * 案件详情
     * 
     * @param id
     * @return
     */
    public JSONObject getInfoById(JSONObject objs) {
        JSONObject result = null;
        JSONObject queryData = objs.getJSONObject("queryData");
        CaseRegistration caseRegistration = this.selectById(queryData.get("caseRegistrationId"));
        if (caseRegistration != null) {

            result = JSONObject.parseObject(JSONObject.toJSONString(caseRegistration));
            if (result != null) {
                // org:单位，person:个人
                String concernedType = "org";
                JSONObject concernedResult = null;
                // 当事人：单位
                if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG.equals(caseRegistration.getConcernedType())) {
                    ConcernedCompany concernedCompany =
                        concernedCompanyBiz.selectById(Integer.valueOf(caseRegistration.getConcernedId()));
                    if (concernedCompany != null) {
                        concernedResult = JSONObject.parseObject(JSONObject.toJSONString(concernedCompany));
                    }
                }
                // 当事人：个人
                if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON.equals(caseRegistration.getConcernedType())) {
                    ConcernedPerson concernedPerson =
                        concernedPersonBiz.selectById(Integer.valueOf(caseRegistration.getConcernedId()));
                    if (concernedPerson != null) {
                        concernedResult = JSONObject.parseObject(JSONObject.toJSONString(concernedPerson));
                        Map<String, String> credMap =
                            dictFeign.getByCodeIn(concernedPerson.getCredType() + "," + concernedPerson.getSex());
                        if (credMap != null && !credMap.isEmpty()) {
                            concernedResult.put("credTypeName", credMap.get(concernedPerson.getCredType()));
                            concernedResult.put("sexName", credMap.get(concernedPerson.getSex()));
                        }
                    }
                    concernedType = "person";
                }
                // 当事人类型
                result.put("concernedType", concernedType);
                // 当事人详情
                result.put("concernedResult", concernedResult);

                // 业务条线
                Map<String, String> bizMap = dictFeign.getByCode(caseRegistration.getBizType());
                if (bizMap != null && !bizMap.isEmpty()) {
                    result.put("bizName", bizMap.get(caseRegistration.getBizType()));
                }

                // 事件类别
                EventType eventType = eventTypeBiz.selectById(Integer.valueOf(caseRegistration.getEventType()));
                if (eventType != null) {
                    result.put("eventTypeName", eventType.getTypeName());
                }

                // 案件来源
                Map<String, String> caseSourceMap = dictFeign.getByCode(caseRegistration.getCaseSource());
                if (caseSourceMap != null && !caseSourceMap.isEmpty()) {
                    result.put("caseSourceName", caseSourceMap.get(caseRegistration.getCaseSource()));
                }

                // 违法行为
                InspectItems inspectItems =
                    inspectItemsBiz.selectById(Integer.valueOf(caseRegistration.getInspectItem()));
                if (inspectItems != null) {
                }
                result.put("inspectName", inspectItems.getName());

                // 举报人姓名
                JSONArray informerUser = iUserFeign.getByUserIds(caseRegistration.getCaseInformer());
                String caseInformerName = "";
                if (informerUser != null && !informerUser.isEmpty()) {
                    for (int i = 0; i < informerUser.size(); i++) {
                        caseInformerName = informerUser.getJSONObject(i).getString("name");
                    }
                }
                result.put("caseInformerName", caseInformerName);

                // 执法者用户名
                JSONArray userList = null;
                try {
                    userList = iUserFeign.getByUserIds(caseRegistration.getEnforcers());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (userList != null && !userList.isEmpty()) {
                    StringBuilder userName = new StringBuilder();
                    // 最后一条记录
                    int size = userList.size() - 1;
                    for (int i = 0; i < userList.size(); i++) {
                        if (i == size) {
                            userName.append(userList.getJSONObject(i).getString("name"));
                        } else {
                            userName.append(userList.getJSONObject(i).getString("name")).append(",");
                        }

                    }
                    result.put("enforcersName", userName.toString());
                }

                // 网格名称
                AreaGrid areaGrid = areaGridBiz.selectById(caseRegistration.getGirdId());
                if (areaGrid != null) {
                    result.put("gridName", areaGrid.getGridName());
                }
                // 移送部门
                String transferDepart = caseRegistration.getTransferDepart();
                if (StringUtils.isNotBlank(transferDepart)) {
                    JSONObject dept = adminFeign.getByDeptId(transferDepart);
                    if (dept != null) {
                        result.put("transferDeptName", dept.getString("name"));
                    }
                }

                // 查询流程历史记录
                PageInfo<WfProcTaskHistoryBean> procApprovedHistory = wfProcTaskService.getProcApprovedHistory(objs);
                List<WfProcTaskHistoryBean> procHistoryList = procApprovedHistory.getList();
                JSONArray procHistoryJArray = JSONArray.parseArray(JSON.toJSONString(procHistoryList));
                List<String> procTaskAssigneeIdList =
                    procHistoryList.stream().map(o -> o.getProcTaskAssignee()).distinct().collect(Collectors.toList());
                if (procTaskAssigneeIdList != null && !procTaskAssigneeIdList.isEmpty()) {
                    Map<String, String> assignMap = adminFeign.getUser(String.join(",", procTaskAssigneeIdList));
                    if (assignMap != null && !assignMap.isEmpty()) {
                        for (int i = 0; i < procHistoryJArray.size(); i++) {
                            JSONObject procHistoryJObj = procHistoryJArray.getJSONObject(i);
                            JSONObject nameJObj =
                                JSONObject.parseObject(assignMap.get(procHistoryJObj.getString("procTaskAssignee")));
                            if (nameJObj != null) {
                                procHistoryJObj.put("procTaskAssigneeName", nameJObj.getString("name"));
                                procHistoryJObj.put("procTaskAssigneeTel", nameJObj.getString("mobilePhone"));
                            }
                        }
                    }
                }
                result.put("procHistory", procHistoryJArray);
            }
        }
        return result;
    }

    /**
     * 案件处理类型统计
     * 
     * @param caseRegistration
     *            查询条件
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param gridIds
     *            网格范围ids
     * @return
     */
    public JSONArray getStatisState(CaseRegistration caseRegistration, String startTime, String endTime,
        String gridIds) {
        JSONArray result = new JSONArray();

        Map<String, String> stateCode = dictFeign.getByCode(Constances.ROOT_BIZ_CASEDEALTYPE);
        List<Map<String, Object>> caseList = this.mapper.selectState(caseRegistration, startTime, endTime, gridIds);

        if (BeanUtil.isNotEmpty(caseList)) {
            // 封装数据集
            Map<String, Object> tempData = new HashMap<>();
            Set<String> setKey = null;
            Object count = null;
            for (Map<String, Object> map : caseList) {
                tempData.put(String.valueOf(map.get("dealType")), map.get("count"));
            }
            // 封装返回集
            JSONObject obj = null;
            if (stateCode != null && !stateCode.isEmpty()) {
                setKey = stateCode.keySet();

                for (String key : setKey) {
                    obj = new JSONObject();
                    // 类型code
                    obj.put("code", key);
                    // 类型名称
                    obj.put("name", stateCode.get(key));
                    // 类型数量
                    count = tempData.get(key);
                    obj.put("count", count == null ? 0 : count);
                    result.add(obj);
                }
            }
        }
        return result;
    }

    /**
     * 案件来源分布
     * 
     * @param caseRegistration
     *            查询条件
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public JSONArray getCaseSource(CaseRegistration caseRegistration, String startTime, String endTime,
        String gridIds) {
        JSONArray result = new JSONArray();

        Map<String, String> sourceType = dictFeign.getByCode(Constances.CASE_SOURCE_TYPE);
        List<Map<String, Object>> caseList =
            this.mapper.selectCaseSource(caseRegistration, startTime, endTime, gridIds);

        if (BeanUtil.isNotEmpty(caseList)) {
            // 封装数据集
            Map<String, Object> tempData = new HashMap<>();
            Set<String> setKey = null;
            Object count = null;
            for (Map<String, Object> map : caseList) {
                tempData.put(String.valueOf(map.get("caseSourceType")), map.get("count"));
            }
            // 封装返回集
            JSONObject obj = null;
            if (sourceType != null && !sourceType.isEmpty()) {
                setKey = sourceType.keySet();

                for (String key : setKey) {
                    obj = new JSONObject();
                    // 类型code
                    obj.put("code", key);
                    // 类型名称
                    obj.put("name", sourceType.get(key));
                    // 类型数量
                    count = tempData.get(key);
                    obj.put("count", count == null ? 0 : count);
                    result.add(obj);
                }
            }
        }
        return result;
    }

    /**
     * 案件业务条线分布
     * 
     * @param caseRegistration
     *            查询条件
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @return
     */
    public JSONArray getBizType(CaseRegistration caseRegistration, String startTime, String endTime, String gridIds) {
        JSONArray result = new JSONArray();

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
            obj.put("bizType", key);
            obj.put("count", 0);
            obj.put("bitTypeName", bizType.get(key));
            temp.put(key, obj);
        }
        // 封装数据库中的数据
        List<Map<String, Object>> bizLineList =
            this.mapper.selectBizLine(caseRegistration, startTime, endTime, gridIds);
        if (BeanUtil.isNotEmpty(bizLineList)) {
            for (Map<String, Object> bizLineMap : bizLineList) {
                obj = new JSONObject();
                String bitType = String.valueOf(bizLineMap.get("bizType"));
                // 业务条线
                obj.put("bizType", bitType);
                obj.put("count", bizLineMap.get("count"));
                obj.put("bitTypeName", bizType.get(bitType));
                temp.put(bitType, obj);
            }
        }
        // 封装返回集数据
        setKey = temp.keySet();
        for (String key : setKey) {
            result.add(temp.get(key));
        }
        return result;
    }

    public ObjectRestResponse<JSONObject> getStatisZhDuiCase(JSONObject caseRegistrationJObj, String startTime,
        String endTime) {

        // 转化查询条件
        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setBizType(caseRegistrationJObj.getString("bizType"));
        caseRegistration.setCaseSourceType(caseRegistrationJObj.getString("caseSourceType"));

        ObjectRestResponse<JSONObject> restResponse = new ObjectRestResponse<>();
        JSONObject resultJobj = new JSONObject();

        /*
         * ========进行查询操作============开始=============
         */
        // 转化按风格范围查询的条件
        if (StringUtils.isNotBlank(caseRegistrationJObj.getString("gridIds"))) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("'").append(caseRegistrationJObj.getString("gridIds").replaceAll(",", "','")).append("'");
            caseRegistrationJObj.put("gridIds", buffer.toString());
        } else {
            caseRegistrationJObj.put("gridIds", null);
        }

        // 收集执法中队，用于动态生成SQL
        JSONArray enforcersGroup = adminFeign.getEnforcersGroup();

        Map<String, String> dept_ID_NAME_Map = new HashMap<>();
        List<Map<String, String>> deptParam = new ArrayList<>();// 添补动态SQL的参数
        List<String> deptIdList = new ArrayList<>();// 用于数据整合时判断结果集里的某一条key是否为部门ID
        for (int i = 0; i < enforcersGroup.size(); i++) {
            JSONObject enforcersJObj = enforcersGroup.getJSONObject(i);
            dept_ID_NAME_Map.put(enforcersJObj.getString("id"), enforcersJObj.getString("name"));

            Map<String, String> deptParamTmp = new HashMap<>();
            deptParamTmp.put("deptId", enforcersJObj.getString("id"));
            deptParamTmp.put("deptName", enforcersJObj.getString("name"));
            deptParam.add(deptParamTmp);

            deptIdList.add(enforcersJObj.getString("id"));
        }

        JSONArray byDept =
            this.mapper.getStatisByDept(caseRegistration, startTime, endTime, caseRegistrationJObj.getString("gridIds"),
                deptParam);
        /*
         * ========进行查询操作============结束=============
         */

        /*
         * ========进行数据整合============开始=============
         */
        for (int i = 0; i < byDept.size(); i++) {
            JSONObject deptJObj = byDept.getJSONObject(i);
            JSONArray deptJArray = new JSONArray();
            for (String deptIdKey : deptJObj.keySet()) {
                if (deptIdList.contains(deptIdKey)) {
                    // 说明该deptIdkey为部门ID
                    JSONObject deptInner = new JSONObject();
                    deptInner.put("deptId", deptIdKey);
                    deptInner.put("deptName", dept_ID_NAME_Map.get(deptIdKey));
                    deptInner.put("count", deptJObj.getInteger(deptIdKey));
                    deptJArray.add(deptInner);
                }
            }
            deptJObj.put("zhd", deptJArray);
        }

        Date startDate = DateUtil.dateFromStrToDate(startTime, true);
        Date endDate = DateUtil.dateFromStrToDate(endTime, true);

        DateTime dd = new DateTime(startDate.getTime());
        DateTime dl1 = new DateTime(dd.getYear(), dd.getMonthOfYear(), 1, 0, 0);

        int count = byDept.size();

        List<String> dateTimeIn = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            JSONObject deptJObj = byDept.getJSONObject(i);
            dateTimeIn.add(deptJObj.getString("cyear") + deptJObj.getString("cmonth"));
        }

        do {
            String sample = String.valueOf(dl1.getYear()) + String.valueOf(dl1.getMonthOfYear());
            if (!dateTimeIn.contains(sample)) {
                // 说明该年该月下没有数据
                JSONObject fDate3 = new JSONObject();
                fDate3.put("cyear", dl1.getYear());
                fDate3.put("cmonth", dl1.getMonthOfYear());
                fDate3.put("total", 0);

                for (int i = 0; i < byDept.size(); i++) {
                    JSONArray deptJArray = new JSONArray();
                    JSONObject deptJObj = byDept.getJSONObject(i);
                    for (String deptIdKey : deptJObj.keySet()) {
                        if (deptIdList.contains(deptIdKey)) {
                            // 说明该deptIdkey为部门ID
                            JSONObject deptInner = new JSONObject();
                            deptInner.put("deptId", deptIdKey);
                            deptInner.put("deptName", dept_ID_NAME_Map.get(deptIdKey));
                            deptInner.put("count", 0);
                            deptJArray.add(deptInner);
                            fDate3.put("zhd", deptJArray);
                        }
                    }
                }

                byDept.add(fDate3);
            }
            dl1 = dl1.plusMonths(1);

        } while (dl1.getMillis() <= endDate.getTime());
        /*
         * ============
         */
        byDept.sort(new Comparator<Object>() {

            @Override
            public int compare(Object o1, Object o2) {
                JSONObject j1 = (JSONObject) o1;
                JSONObject j2 = (JSONObject) o2;

                if ((j1.getInteger("cyear") - j2.getInteger("cyear")) != 0) {
                    return j1.getInteger("cyear") - j2.getInteger("cyear");
                } else {
                    return j1.getInteger("cmonth") - j2.getInteger("cmonth");
                }
            }
        });
        resultJobj.put("byDept", byDept);

        restResponse.setStatus(200);
        restResponse.setData(resultJobj);
        return restResponse;
    }

    /**
     * 案件业务条线分布
     * 
     * @param caseRegistration
     *            查询条件
     * @param startTime
     *            开始时间
     * @param endTime
     *            结束时间
     * @param page
     *            页码
     * @param limit
     *            页容量
     * @return
     */
    public TableResultResponse<Map<String, Object>> getInspectItem(CaseRegistration caseRegistration, String startTime,
        String endTime, String gridIds, Integer page, Integer limit) {

        Page<Object> result = PageHelper.startPage(page, limit);
        List<Map<String, Object>> list = this.mapper.selectInspectItem(caseRegistration, startTime, endTime, gridIds);
        if (BeanUtil.isEmpty(list)) {
            return new TableResultResponse<Map<String, Object>>(0, null);
        }
        return new TableResultResponse<Map<String, Object>>(result.getTotal(), list);
    }
}