package com.bjzhianjia.scp.cgp.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.CLEConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.CLEConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.CaseAttachments;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EnterpriseInfo;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.LawPatrolObject;
import com.bjzhianjia.scp.cgp.entity.LawTask;
import com.bjzhianjia.scp.cgp.entity.Point;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.RightsIssues;
import com.bjzhianjia.scp.cgp.entity.WritsInstances;
import com.bjzhianjia.scp.cgp.entity.WritsTemplates;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.feign.IUserFeign;
import com.bjzhianjia.scp.cgp.mapper.CaseInfoMapper;
import com.bjzhianjia.scp.cgp.mapper.CaseRegistrationMapper;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.mapper.LawTaskMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.cgp.vo.CaseRegistrationVo;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.common.util.BeanUtils;
import com.bjzhianjia.scp.security.common.util.BooleanUtil;
import com.bjzhianjia.scp.security.common.util.UUIDUtils;
import com.bjzhianjia.scp.security.wf.base.constant.Constants;
import com.bjzhianjia.scp.security.wf.base.design.entity.WfProcPropsBean;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;
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
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 综合执法 - 案件登记
 *
 * @author bo
 * @email 576866311@qq.com
 * @version 2018-08-26 20:07:08
 */
@Service
@Slf4j
@Transactional
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
    private EventTypeBiz eventTypeBiz;

    @Autowired
    private AreaGridBiz areaGridBiz;

    @Autowired
    private WfProcTaskServiceImpl wfProcTaskService;

    @Autowired
    private WritsTemplatesBiz writsTemplatesBiz;

    @Autowired
    private CaseAttachmentsBiz caseAttachmentsBiz;

    @Autowired
    private Environment environment;
    
    @Autowired
    private PropertiesProxy propertiesProxy;

    @Autowired
    private RightsIssuesBiz rightsIssuesBiz;
    
    @Autowired
    private LawTaskMapper lawTaskMapper;
    
    @Autowired
    private CaseInfoMapper caseInfoMapper;

    @Autowired
    private LawPatrolObjectBiz lawPatrolObjectBiz;

    @Autowired
    private RegulaObjectBiz regulaObjectBiz;

    @Autowired
    private EnterpriseInfoBiz enterpriseInfoBiz;

    /**
     * 添加立案记录<br/>
     * 如果 有当事人，则一并添加<br/>
     * 如果 有文书，则一并添加
     * 
     * @author 尚
     * @param caseRegJObj
     */
    public Result<Void> addCase(JSONObject caseRegJObj) {
        Result<Void> result = new Result<>();

        // 检查是否需要自动生成案件名称
        checkCaseRegistrationName(caseRegJObj);

        // 添加当事人
        int concernedId = addConcerned(caseRegJObj);

        // 添加立案单
        CaseRegistration caseRegistration = JSON.parseObject(caseRegJObj.toJSONString(), CaseRegistration.class);
        if (StringUtils.isNotBlank(caseRegJObj.getString("brief"))
            && StringUtils.isBlank(caseRegistration.getDealSuggest())) {
            // 说明前端将说明信息传入了brief变量中，将其取出赋给dealSuggest
            caseRegistration.setDealSuggest(caseRegJObj.getString("brief"));
        }
        // 案件定位
        JSONObject mapInfo = caseRegJObj.getJSONObject("mapInfo");
        if(BeanUtil.isNotEmpty(mapInfo)){
            caseRegistration.setCaseLatitude(mapInfo.getString("lat"));
            caseRegistration.setCaseOngitude(mapInfo.getString("lng"));
        }

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

        // 确定执法人员的部门
        addEnforcerDept(caseRegistration);

        // 如果案件有图片信息，将图片路径中的空格去除掉
        _trimPicPathWhiteSpace(caseRegistration);

        this.insertSelective(caseRegistration);
        // 将生成的立案ID装入procBizData带回工作流，在工作流中会对procBizId属性进行是否为“-1”的判断，如果是“-1”，将用该ID替换“-1”
        caseRegJObj.put("procBizId", caseId);

        result.setIsSuccess(true);
        return result;
    }

    private void _trimPicPathWhiteSpace(CaseRegistration caseRegistration) {
        if (StringUtils.isNotBlank(caseRegistration.getCaseSpotPic())) {
            List<String> picList = new ArrayList<>();
            for (String string : StringUtils.split(caseRegistration.getCaseSpotPic(), ",")) {
                picList.add(StringUtils.trim(string));
            }
            caseRegistration.setCaseSpotPic(String.join(",", picList));
        }
    }

    /**
     * 找出执法人员的部门，并填充到caseRegistration对象中
     *
     * @param caseRegistration
     */
    private void addEnforcerDept(CaseRegistration caseRegistration) {
        String enforcers = caseRegistration.getEnforcers();

        JSONArray enforcerJArray;
        Set<String> enforcerDeptIdSet = new HashSet<>();

        if (StringUtils.isNotEmpty(enforcers)) {
            enforcerJArray = adminFeign.getUserDetail(enforcers);
            if (BeanUtil.isNotEmpty(enforcerJArray)) {
                for (int i = 0; i < enforcerJArray.size(); i++) {
                    JSONObject enforcerDeptJObj = enforcerJArray.getJSONObject(i);
                    String deptId = enforcerDeptJObj.getString("deptId");
                    if(StringUtils.isNotBlank(deptId)){
                        // 因人员配置不健全，导致某人员还未在“部门--人员”表进行配置，将导致将null字样插入业务数据库
                        enforcerDeptIdSet.add(deptId);
                    }
                }
            }
        }

        caseRegistration.setDeptId(String.join(",", enforcerDeptIdSet));
    }

    /**
     * 如果请求信息中有附件信息，则将其保存到数据库中
     * 
     * @param caseRegJObj
     * @param caseId
     */
    private void addAttachments(JSONObject caseRegJObj, String caseId) {
        // 附件信息以JSON数组形式封装在attachments中
        JSONArray attachmentsJArray = caseRegJObj.getJSONArray("attachments");
        List<CaseAttachments> attachmentsList = new ArrayList<>();

        if (BeanUtil.isNotEmpty(attachmentsJArray)) {
            attachmentsList = attachmentsJArray.toJavaList(CaseAttachments.class);
        }

        for (CaseAttachments caseAttachments : attachmentsList) {
            caseAttachments.setCaseId(caseId);
        }

        if (BeanUtil.isNotEmpty(attachmentsList)) {
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

        if(BeanUtil.isEmpty(writsInstancesJArray)){
            return;
        }

        // 收集前端传入的模板tcode值，可能没有传入，如果没传，则该操作为更新操作
        List<String> tcodeList = new ArrayList<>();
        for (int i = 0; i < writsInstancesJArray.size(); i++) {
            JSONObject fillContextJObj = writsInstancesJArray.getJSONObject(i);
            String tcode = fillContextJObj.getString("tcode");
            if (StringUtils.isNotBlank(tcode)) {
                tcodeList.add(tcode);
            }

            /*
             * 三级审批信息===================开始===============
             */
            fillContextJObj.put("SquadronLeaderSuggest", caseRegJObj.getString("SquadronLeaderSuggest") == null ? ""
                : caseRegJObj.getString("SquadronLeaderSuggest"));
            fillContextJObj.put("SquadronLeader",
                caseRegJObj.getString("SquadronLeader") == null ? "" : caseRegJObj.getString("SquadronLeader"));
            fillContextJObj.put("TownLeaderSuggest",
                caseRegJObj.getString("TownLeaderSuggest") == null ? "" : caseRegJObj.getString("TownLeaderSuggest"));
            fillContextJObj.put("TownLeaderSuggest",
                caseRegJObj.getString("TownLeaderSuggest") == null ? "" : caseRegJObj.getString("TownLeaderSuggest"));
            fillContextJObj.put("remark",
                caseRegJObj.getString("remark") == null ? "" : caseRegJObj.getString("remark"));
            /*
             * 三级审批信息===================结束===============
             */
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
                writsInstancesBiz.updateSelectiveById(writsInstances);
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
    @SuppressWarnings("unused")
    @Deprecated
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
                    concernedCompany.setId(null);//避免前端传入当事人ID，在此设置为null
                    cLEConcernedCompanyBiz.insertSelective(concernedCompany);
                    resultId = concernedCompany.getId();
                    break;
                case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON:
                    // 当事人以人个形式存在
                    CLEConcernedPerson concernedPerson =
                        JSON.parseObject(concernedJObj.toJSONString(), CLEConcernedPerson.class);
                    concernedPerson.setId(null);//避免前端传入当事人ID，在此设置为null
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
        /*
         * 在数据库中，执法人员使用用户ID进行保存，而不再使用执法证ID保存，故无需再通过执法人员
         * 在执法证管理表中将相应的执法证ID找出
         */
        criteria.andLike("enforcers", "%"+userId+"%");

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
    public TableResultResponse<CaseRegistration> getList(CaseRegistration caseRegistration,String regulaobjectId, int page, int limit) {
        Example example = new Example(CaseRegistration.class);
        Criteria criteria = example.createCriteria();

        criteria.andEqualTo("isDeleted", "0");
        if (caseRegistration.getGirdId() != null) {
            criteria.andEqualTo("girdId", caseRegistration.getGirdId());
        }
        // 添加按案件状态查询的逻辑
        if (StringUtils.isNotBlank(caseRegistration.getExeStatus())) {
            criteria.andEqualTo("exeStatus", caseRegistration.getExeStatus());
        }
        // 案件编号
        if(StringUtils.isNotBlank(caseRegistration.getCaseCode())){
            criteria.andLike("caseCode", "%"+caseRegistration.getCaseCode()+"%");
        }
        // 案件名称
        if(StringUtils.isNotBlank(caseRegistration.getCaseName())){
            criteria.andLike("caseName", caseRegistration.getCaseName());
        }

        // 按监管对象查询
        if(BeanUtils.isNotEmpty(regulaobjectId)){
            CLEConcernedCompany company=new CLEConcernedCompany();
            company.setRegulaObjectId(Integer.valueOf(regulaobjectId));
            List<CLEConcernedCompany> companyList = cLEConcernedCompanyBiz.getList(company);
            boolean flag=true;
            if(BeanUtil.isNotEmpty(companyList)){
                Set<Integer> collect = companyList.stream().map(CLEConcernedCompany::getId).collect(Collectors.toSet());
                if(BeanUtil.isNotEmpty(collect)){
                    criteria.andIn("concernedId",collect);
                    criteria.andEqualTo("concernedType", "root_biz_concernedT_org");
                    flag=false;
                }
            }

            if(flag){
                return new TableResultResponse<>(0,new ArrayList<>());
            }
        }
        example.setOrderByClause("crt_time desc");
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
        // 查询条件
        JSONObject queryData = objs.getJSONObject("queryData");
        if (queryData == null) {
            queryData = new JSONObject();
        }
        queryCaseRegistration.setIsSupervise(queryData.getString("isSupervise"));
        queryCaseRegistration.setIsUrge(queryData.getString("isUrge"));
        queryCaseRegistration.setCaseName(queryData.getString("caseName"));

        // 工作流查询条件
        JSONObject bizData = objs.getJSONObject("bizData");
        // 事件工作流的定义代码
        bizData.put("prockey", "LawEnforcementProcess");
        objs.put("bizData", bizData);

        // 查询待办工作流任务
        PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getUserAllToDoTasks(objs);
        List<WfProcBackBean> wfProcBackBeanList = pageInfo.getList();

        /*
         * By尚
         */
        if (BeanUtil.isEmpty(wfProcBackBeanList)) {
            return new TableResultResponse<>(0, new ArrayList<JSONObject>());
        }

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
            TableResultResponse<CaseRegistration> tableResult = this.getList(queryCaseRegistration, bizIds, queryData);
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

                    /*
                     * By尚
                     * 在返回结果中添加案件名称
                     */
                    obj.put("caseName", caseRegistration.getCaseName());

                    obj.put("procTaskCommitterName",
                        CommonUtil.getValueFromJObjStr(userMap.get(wfProcBackBean.getProcTaskCommitter()), "name"));

                    // 封装督办催办
                    if (caseRegistration != null) {
                        isUrge = caseRegistration.getIsSupervise();
                        isSupervise = caseRegistration.getIsUrge();
                    }
                    obj.put("isSupervise", "1".equals(isUrge));
                    obj.put("isUrge", "1".equals(isSupervise));
                    // 完成期限
                    Date caseEnd = caseRegistration.getCaseEnd();
                    String exeStatus = caseRegistration.getExeStatus();
                    // 默认没有超时
                    boolean isOvertime = false;
                    if (caseEnd != null) {
                        // 处理结束
                        if (CaseRegistration.EXESTATUS_STATE_STOP.equals(exeStatus)
                            || CaseRegistration.EXESTATUS_STATE_FINISH.equals(exeStatus)) {
                            // 结案时间
                            Date finishTime = caseRegistration.getUpdTime();
                            isOvertime = caseEnd.before(finishTime);
                        } else {
                            // 处理中
                            isOvertime = caseEnd.before(new Date());
                        }
                    }
                    // 是否超时
                    obj.put("isOvertime", isOvertime);
                    obj.put("crtTime", caseRegistration.getCrtTime());
                    // 在列表中添加当事人类型
                    obj.put("concernedType", caseRegistration.getConcernedType());
                    obj.put("caseCode", caseRegistration.getCaseCode() == null ? ""
                        : caseRegistration.getCaseCode());
                    result.add(obj);
                }

                // 对结果集按创建时间顺序进行排序
                result.sort(new Comparator<JSONObject>() {

                    @Override
                    public int compare(JSONObject o1, JSONObject o2) {
                        if (BeanUtil.isNotEmpty(o1.getDate("crtTime"))
                            && BeanUtil.isNotEmpty(o2.getDate("crtTime"))) {
                            if (o1.getDate("crtTime").before(o2.getDate("crtTime"))) {
                                return 1;
                            }else if(o1.getDate("crtTime").after(o2.getDate("crtTime"))){
                                return -1;
                            }else{
                                return 0;
                            }
                        }
                        return 0;
                    }
                });

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
        return allTasksAssist(objs,false);
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
        Set<String> eventTypeIdStrList = new HashSet<>(); // 案件类别
        Set<String> rootBizIdSet = new HashSet<>(); // 数据字典code
        for (CaseRegistration caseRegistration : caseRegistrationList) {
            if (StringUtils.isNotBlank(caseRegistration.getBizType())) {
                rootBizIdSet.add(caseRegistration.getBizType());
            }
            if (StringUtils.isNotBlank(caseRegistration.getCaseSourceType())) {
                rootBizIdSet.add(caseRegistration.getCaseSourceType());
            }
            // 收集案件类型
            if (StringUtils.isNotBlank(caseRegistration.getDealType())) {
                rootBizIdSet.add(caseRegistration.getDealType());
            }
            if (StringUtils.isNotBlank(caseRegistration.getEventType())) {
                eventTypeIdStrList.add(caseRegistration.getEventType());
            }
        }

        // 查询业务条线，查询案件来源
        Map<String, String> rootBizList = new HashMap<>();
        if (rootBizIdSet != null && !rootBizIdSet.isEmpty()) {
            rootBizList = dictFeign.getByCodeIn(String.join(",", rootBizIdSet));
        }

        // 查询案件类别
        Map<String, String> eventType_ID_NAME_Map = new HashMap<>();
        if (eventTypeIdStrList != null && !eventTypeIdStrList.isEmpty()) {
            List<EventType> eventTypeList = new ArrayList<>();
            eventTypeList = eventTypeMapper.selectByIds(String.join(",", eventTypeIdStrList));
            for (EventType eventType : eventTypeList) {
                eventType_ID_NAME_Map.put(String.valueOf(eventType.getId()), eventType.getTypeName());
            }
        }

        /*
         * 根据不同的案件来源，收集来源编号，进行一次查询
         */
        // 收集来源ID
        Set<String> caseSourceLaw = new HashSet<>();
        Set<String> caseSourceCenter = new HashSet<>();
        for (CaseRegistration caseRegistration : caseRegistrationList) {
            if (CaseRegistration.CASE_SOURCE_TYPE_TASK.equals(caseRegistration.getCaseSourceType())) {
                // 执法任务
                if (StringUtils.isNotBlank(caseRegistration.getCaseSource())) {
                    caseSourceLaw.add(caseRegistration.getCaseSource());
                }
            } else if (CaseRegistration.CASE_SOURCE_TYPE_CENTER.equals(caseRegistration.getCaseSourceType())) {
                // 中心交办
                if (StringUtils.isNotBlank(caseRegistration.getCaseSource())) {
                    caseSourceCenter.add(caseRegistration.getCaseSource());
                }
            }
        }
        // 查询来源信息
        List<LawTask> lawTaskList = null;
        if (BeanUtil.isNotEmpty(caseSourceLaw)) {
            lawTaskList = lawTaskMapper.selectByIds(String.join(",", caseSourceLaw));
        }
        List<CaseInfo> caseInfoList = null;
        if (BeanUtil.isNotEmpty(caseSourceCenter)) {
            caseInfoList = caseInfoMapper.selectByIds(String.join(",", caseSourceCenter));
        }
        // ID编号Map
        Map<Integer, String> lawTaskIdCodeMap = new HashMap<>();
        // ID名称Map
        Map<Integer, String> lawTaskIdNameMap = new HashMap<>();
        if (BeanUtil.isNotEmpty(lawTaskList)) {
            for (LawTask lawtaskTmp : lawTaskList) {
                lawTaskIdCodeMap.put(lawtaskTmp.getId(), lawtaskTmp.getLawTaskCode());
                lawTaskIdNameMap.put(lawtaskTmp.getId(), lawtaskTmp.getLawTitle());
            }
        }

        // ID编号Map
        Map<Integer, String> caseInfoIdCodeMap = new HashMap<>();
        // ID名称Map
        Map<Integer, String> caseInfoIdNameMap = new HashMap<>();
        if (BeanUtil.isNotEmpty(caseInfoList)) {
            for (CaseInfo caseInfoTmp : caseInfoList) {
                caseInfoIdCodeMap.put(caseInfoTmp.getId(), caseInfoTmp.getCaseCode());
                caseInfoIdNameMap.put(caseInfoTmp.getId(), caseInfoTmp.getCaseTitle());
            }
        }

        JSONObject objResult = null;
        WfProcBackBean wfProcBackBean = null;
        for (CaseRegistration caseRegistration : caseRegistrationList) {
            objResult = JSONObject.parseObject(JSON.toJSONString(caseRegistration));
            wfProcBackBean = wfProcBackBean_ID_Entity_Map.get(objResult.get("id"));

            // 定位坐标
            JSONObject mapInfoJobj = null;
            if (StringUtils.isNotBlank(caseRegistration.getCaseOngitude())
                && StringUtils.isNotBlank(caseRegistration.getCaseLatitude())) {
                // 经度与纬度同时不为空时才生成地理信息
                mapInfoJobj = new JSONObject();
                mapInfoJobj.put("lng", caseRegistration.getCaseOngitude());
                mapInfoJobj.put("lat", caseRegistration.getCaseLatitude());
            }
            objResult.put("mapInfo", mapInfoJobj == null ? null : mapInfoJobj.toJSONString());

            // 处理状态
            String procCtaskname = "";
            if (wfProcBackBean != null) {
                procCtaskname = wfProcBackBean.getProcCtaskname();
                if (CaseRegistration.EXESTATUS_STATE_FINISH.equals(caseRegistration.getExeStatus())) {
                    procCtaskname = "已结案(" + procCtaskname + ")";
                }

                if (CaseRegistration.EXESTATUS_STATE_STOP.equals(caseRegistration.getExeStatus())) {
                    procCtaskname = "已终止(" + procCtaskname + ")";
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
            // 案件处理方式--后添加
            objResult.put("dealTypeName", getRootBizTypeName(caseRegistration.getDealType(), rootBizList));

            // 具体来源id
            String sourceId = caseRegistration.getCaseSource();
            // 具体来源标题
            String sourceTitle = "";
            // 执法任务
            if (StringUtils.isNotBlank(sourceId)) {
                if (CaseRegistration.CASE_SOURCE_TYPE_TASK.equals(caseRegistration.getCaseSourceType())) {
                    // 替换之前在循环体内查询数据库的操作
                    sourceTitle = lawTaskIdNameMap.get(Integer.valueOf(sourceId));
                    objResult.put("caseSourceCode", lawTaskIdCodeMap.get(Integer.valueOf(sourceId)));
                } else if (CaseRegistration.CASE_SOURCE_TYPE_CENTER.equals(caseRegistration.getCaseSourceType())) { // 中心交办
                    // 替换之前在循环体内查询数据库的操作
                    sourceTitle = caseInfoIdNameMap.get(Integer.valueOf(sourceId));
                    objResult.put("caseSourceCode", caseInfoIdCodeMap.get(Integer.valueOf(sourceId)));
                }
            }
            objResult.put("sourceId", sourceId);
            objResult.put("sourceTitle", sourceTitle);
            objResult.put("isUrge", "0".equals(caseRegistration.getIsUrge()) ? false : true);
            objResult.put("isSupervise", "0".equals(caseRegistration.getIsSupervise()) ? false : true);
            // 完成期限
            Date caseEnd = caseRegistration.getCaseEnd();
            String exeStatus = caseRegistration.getExeStatus();
            // 默认没有超时
            boolean isOvertime = false;
            if (caseEnd != null) {
                // 处理结束
                if (CaseRegistration.EXESTATUS_STATE_STOP.equals(exeStatus)
                        || CaseRegistration.EXESTATUS_STATE_FINISH.equals(exeStatus)) {
                    // 结案时间
                    Date finishTime = caseRegistration.getUpdTime();
                    isOvertime = caseEnd.before(finishTime);
                } else {
                    // 处理中
                    isOvertime = caseEnd.before(new Date());
                }
            }
            // 是否超时
            objResult.put("isOvertime", isOvertime);
            
            // 整合网格信息
            if (BeanUtil.isNotEmpty(caseRegistration.getGirdId())) {
                AreaGrid areaGrid = areaGridBiz.selectById(caseRegistration.getGirdId());
                if (BeanUtil.isNotEmpty(areaGrid)) {
                    String areaGridName = areaGrid.getGridName();
                    AreaGrid parentAreaGrid = areaGridBiz.getParentAreaGrid(areaGrid);
                    if (BeanUtil.isNotEmpty(parentAreaGrid)
                        && StringUtils.isNotBlank(parentAreaGrid.getGridName())) {
                        areaGridName = areaGridName + "(" + parentAreaGrid.getGridName() + ")";
                    }
                    objResult.put("areaGridName", areaGridName);
                }
            }
            result.add(objResult);
        }
        return new TableResultResponse<>(bizResult.getData().getTotal(), result);
    }

    private String getEventTypeName(Map<String, String> eventType_ID_NAME_Map, String eventTypeList) {
        if (eventTypeList != null) {
            List<String> nameList = new ArrayList<>();
            String[] split = eventTypeList.split(",");
            for (String string : split) {
                // 如果取出结果为null，则用空字符串填充，防止在前端出现"null"字样
                nameList.add(eventType_ID_NAME_Map.get(string) == null ? "" : eventType_ID_NAME_Map.get(string));
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
        String isOverTime = queryData.getString("isOverTime");
        // 1:已结案2:已终止
        String exeStatus = queryData.getString("procCtaskname");
        // 来源类型
        String caseSourceType = queryData.getString("caseSourceType");
        // 来源id
        String caseSource = queryData.getString("caseSource");
        String isDeleted = queryData.getString("isDeleted");

        Example example = new Example(CaseRegistration.class);
        Criteria criteria = example.createCriteria();

        if(queryData.getBooleanValue("isIntegratedQuery")){
            // 案件管理有需求按是否删除来进行查询
            if(StringUtils.isNotBlank(isDeleted)){
                criteria.andEqualTo("isDeleted", isDeleted);
            }
        }else{
            // 非综合查询，只能查询到未删除的
            criteria.andEqualTo("isDeleted", "0");
        }
        if (StringUtils.isNotBlank(caseRegistration.getCaseName())) {
            criteria.andLike("caseName", "%" + caseRegistration.getCaseName() + "%");
        }
        if (StringUtils.isNotBlank(caseRegistration.getBizType())) {
            criteria.andLike("bizType", "%" + caseRegistration.getBizType() + "%");
        }
        if (StringUtils.isNotBlank(caseRegistration.getEventType())) {
            criteria.andLike("eventType", "%" + caseRegistration.getEventType() + "%");
        }
        if (!(StringUtils.isBlank(startQueryTime) || StringUtils.isBlank(endQueryTime))) {
            Date start = DateUtil.dateFromStrToDate(startQueryTime, "yyyy-MM-dd HH:mm:ss");
            Date end = DateUtils.addDays(DateUtil.dateFromStrToDate(endQueryTime, "yyyy-MM-dd HH:mm:ss"), 1);
            // 案发时间为caseTime
            criteria.andBetween("caseTime", start, end);
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
        // 超时时间 (1:是|0:否)
        if (StringUtils.isNotBlank(isOverTime) && "1".equals(isOverTime)) {
            String date = DateUtil.dateFromDateToStr(new Date(), "yyyy-MM-dd HH:mm:ss");
            // 处理中，当前日期和期限日期进行判断， 结束，则判断完成日期和期限日期
            // 将OR关键字两侧条件作为一个整体
            criteria.andCondition(
                "(('" + date + "' > case_end AND exe_status = 0) OR ( case_end < upd_time AND exe_status IN (1, 2)))");
        }

        // 处理状态：0处理中|1:已结案2:已终止
        if (StringUtils.isNotBlank(exeStatus) && !CaseRegistration.EXESTATUS_STATE_TODO.equals(exeStatus)) {
            // 只查询1:已结案2:已终止
            if (CaseRegistration.EXESTATUS_STATE_FINISH.equals(exeStatus)
                || CaseRegistration.EXESTATUS_STATE_STOP.equals(exeStatus)) {
                criteria.andEqualTo("exeStatus", exeStatus);
            }
        }
        // 案件来源方式
        if (StringUtils.isNotBlank(caseSourceType)) {
            criteria.andEqualTo("caseSourceType", caseSourceType);
        }
        // 案件来源id
        if (StringUtils.isNotBlank(caseSource)) {
            criteria.andEqualTo("caseSource", caseSource);
        }
        example.setOrderByClause("crt_time desc");

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
                // 如果取出结果为null，则用空字符串填充，防止在前端出现"null"字样
                nameList.add(rootBizList.get(string) == null ? "" : rootBizList.get(string));
            }
            return String.join(",", nameList);
        }
        return "";
    }

    /**
     * 案件详情
     * 
     * @param objs
     * @return
     */
    public JSONObject getInfoById(JSONObject objs) {
        JSONObject result = null;
        JSONObject queryData = objs.getJSONObject("queryData");
        JSONObject bizData = objs.getJSONObject("bizData");

        String caseRegistrationId = queryData.getString("caseRegistrationId");
        if(BeanUtil.isEmpty(caseRegistrationId)){
            /*
             * 在工作流请求参数结构中，对于业务ID应通过bizData中的procBizId参数传参
             * 介于之前有通过queryData中的caseRegistrationId传参的情况，对此，保留之前的请求方式
             * 如果在queryData中的caseRegistrationId参数为空，则获取bizData中的procBizId参数
             */
            caseRegistrationId=bizData.getString("procBizId");
        }
        CaseRegistration caseRegistration = this.selectById(caseRegistrationId);

        if (caseRegistration != null) {

            result = JSONObject.parseObject(JSONObject.toJSONString(caseRegistration));
            if (result != null) {
                //案件来源名称
                String caseSourceTypeName = "";
                if(StringUtils.isNotBlank(caseRegistration.getCaseSourceType())){
                    Map<String, String> dictMap = dictFeign.getByCode(caseRegistration.getCaseSourceType());
                    caseSourceTypeName = dictMap.get(caseRegistration.getCaseSourceType());
                    caseSourceTypeName = caseSourceTypeName != null ? caseSourceTypeName : "";
                }
                result.put("caseSourceTypeName",caseSourceTypeName);

                getOneQueryAssist(caseRegistration, result);

                // 查询流程历史记录
                PageInfo<WfProcTaskHistoryBean> procApprovedHistory = wfProcTaskService.getProcApprovedHistory(objs);
                List<WfProcTaskHistoryBean> procHistoryList = procApprovedHistory.getList();
                JSONArray procHistoryJArray = JSONArray.parseArray(JSON.toJSONString(procHistoryList));
                List<String> procTaskAssigneeIdList =
                    procHistoryList.stream().map(o -> o.getProcTaskAssignee()).distinct().collect(Collectors.toList());
                if (procTaskAssigneeIdList != null && !procTaskAssigneeIdList.isEmpty()) {
                    JSONArray userDetailJArray = adminFeign.getUserDetail(String.join(",", procTaskAssigneeIdList));
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
                                procHistoryJObj.put("crtUserTel",
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
                result.put("procHistory", procHistoryJArray);

                /*
                 * 验证案件是否结束
                 * 当工作流在某个节点结束的时候，流程将一直停留在当前节点，在流程历史中无法判断流程是刚好走到该节点还是在该结点已经结束
                 * 如果在某节点流程结束，则在节点名称后加“已结束”或“已终止”标记
                 */
                if (CaseRegistration.EXESTATUS_STATE_STOP.equals(caseRegistration.getExeStatus())
                    || CaseRegistration.EXESTATUS_STATE_FINISH
                        .equals(caseRegistration.getExeStatus())) {
                    // 已完成或已中止
                    JSONObject lastHistory =
                        procHistoryJArray.getJSONObject(procHistoryJArray.size() - 1);
                    String procCtrasknameSuffix = "";
                    if (CaseRegistration.EXESTATUS_STATE_STOP
                        .equals(caseRegistration.getExeStatus())) {
                        procCtrasknameSuffix ="("+
                            new String(environment.getProperty("caseRegistration.exeStatus.two")
                                .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)+")";
                    } else if (CaseRegistration.EXESTATUS_STATE_FINISH
                        .equals(caseRegistration.getExeStatus())) {
                        procCtrasknameSuffix ="("+
                                new String(environment.getProperty("caseRegistration.exeStatus.one")
                                        .getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)+")";
                    }

                    lastHistory.put("procCtaskname",
                        lastHistory.getString("procCtaskname") + procCtrasknameSuffix);
                }
            }

            // 向前端返回流程实例相关的流程参数
            List<WfProcPropsBean> wfProcPropsList = wfProcTaskService.getWfProcPropsList(objs);
            Map<String, String> procpropsMap = new HashMap<>();
            if (BeanUtil.isNotEmpty(wfProcPropsList)) {
                for (WfProcPropsBean wfProcPropsBean : wfProcPropsList) {
                    procpropsMap.put(wfProcPropsBean.getProcPropsKey(), wfProcPropsBean.getProcPropsValue());
                }
            }
            result.put("procProps", procpropsMap);
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

        Map<String, Object> tempData = new HashMap<>();
        Set<String> setKey = null;

        if (BeanUtil.isNotEmpty(caseList)) {
            // 封装数据集
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
        }else{
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
                    obj.put("count", 0);
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

        Set<String> setKey = null;
        // 封装返回集
        JSONObject obj = null;
        Object count = null;
        if (BeanUtil.isNotEmpty(caseList)) {
            // 封装数据集
            Map<String, Object> tempData = new HashMap<>();
            for (Map<String, Object> map : caseList) {
                tempData.put(String.valueOf(map.get("caseSourceType")), map.get("count"));
            }
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
        }else{
            if (sourceType != null && !sourceType.isEmpty()) {
                setKey = sourceType.keySet();

                for (String key : setKey) {
                    obj = new JSONObject();
                    // 类型code
                    obj.put("code", key);
                    // 类型名称
                    obj.put("name", sourceType.get(key));
                    // 类型数量
                    obj.put("count", 0);
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
        Map<String, JSONObject> temp = new LinkedHashMap<>();
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
                // 避免因数据库中空字符串对结果造成影响，对其进行去除
                if(StringUtils.isNotBlank(StringUtils.trim(bitType))){
                    // 业务条线
                    obj.put("bizType", bitType);
                    obj.put("count", bizLineMap.get("count"));
                    obj.put("bitTypeName", bizType.get(bitType));
                    temp.put(bitType, obj);
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

    public ObjectRestResponse<JSONObject> getStatisZhDuiCase(JSONObject caseRegistrationJObj, String startTime,
        String endTime) {

        // 转化查询条件
        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setBizType(caseRegistrationJObj.getString("bizType"));
        caseRegistration.setCaseSourceType(caseRegistrationJObj.getString("caseSourceType"));

        ObjectRestResponse<JSONObject> restResponse = new ObjectRestResponse<>();

        /*
         * ========进行查询操作============开始=============
         */
        // 转化按网格范围查询的条件
        if (StringUtils.isNotBlank(caseRegistrationJObj.getString("gridIds"))) {
            StringBuffer buffer = new StringBuffer();
            buffer.append("'").append(caseRegistrationJObj.getString("gridIds").replaceAll(",", "','")).append("'");
            caseRegistrationJObj.put("gridIds", buffer.toString());
        } else {
            caseRegistrationJObj.put("gridIds", null);
        }

        // 收集执法中队，用于动态生成SQL
        JSONArray enforcersGroup = adminFeign.getEnforcersGroup();

        /*
         * 用于进行查询的部门ID，该数据来自两个地方
         * 1 admin服务里设定的中队ID集合 2 已经在案件表里添加了的部门ID(可能是多部门联查情况)
         * <foreach collection="deptParam" item="item"  separator=",">
		 *	MAX( CASE deptId WHEN #{item.deptId} THEN COUNT ELSE 0 END ) #{item.deptId}
		 * </foreach>
         */
        Set<String> deptIdForQuery=new HashSet<>();
        List<Map<String, String>> deptParam = new ArrayList<>();// 添补动态SQL的参数

        Map<String, String> dept_ID_NAME_Map = new LinkedHashMap<>();
        Set<String> neatDeptIdSet = new HashSet<>();// 干净的执法人员部门ID
        //List<String> deptIdList = new ArrayList<>();// 用于数据整合时判断结果集里的某一条key是否为部门ID
        for (int i = 0; i < enforcersGroup.size(); i++) {
            JSONObject enforcersJObj = enforcersGroup.getJSONObject(i);
            dept_ID_NAME_Map.put(enforcersJObj.getString("id"), enforcersJObj.getString("name"));

            deptIdForQuery.add(enforcersJObj.getString("id"));

            neatDeptIdSet.add(enforcersJObj.getString("id"));
        }

        // 查询执法人员部门序列
        List<String> deptIdListInDB = this.mapper.selectDistinctDeptId();
        if (BeanUtil.isNotEmpty(deptIdListInDB)) {
            for (String deptIdTim : deptIdListInDB) {
                if(StringUtils.isNotBlank(deptIdTim)){
                    // 从案件表里查询到的部门ID，有可能是NULL。如果是NULL不需要添加到deptIdForQuery
                    deptIdForQuery.add(deptIdTim);
                }
            }
        }

        // 在deptIdForQuery里已经收集在来自admin部门表及案件表里有关的部门ID
        for (String deptIdTmp : deptIdForQuery) {
            Map<String, String> deptIdMap = new HashMap<>();
            deptIdMap.put("deptId", deptIdTmp);
            deptParam.add(deptIdMap);
        }

        /*
         * 一个案件可能由多个部门联合执法，在数据库中以逗号的形式将多个部门ID隔开
         * 查到数据后，需要将多个部门联合执法的情况整合到结果集中
         * 查询到的结果可能为以下形式：
         * cyeay    cmonth  totla   dept1   dept2   dept3   dept1,dept3
         * 2018     1       5       2       2       2       3
         * 2018     2       4       1       4       5       2
         */
        JSONArray byDept =
            this.mapper.getStatisByDept(caseRegistration, startTime, endTime, caseRegistrationJObj.getString("gridIds"),
                deptParam);
        log.debug("按中队进行案件统计，查询结果为：" + byDept.toJSONString());
        
        if (BeanUtil.isNotEmpty(byDept)) {
            for (int i = 0; i < byDept.size(); i++) {
                JSONObject statusJObj = byDept.getJSONObject(i);
                for (String statusJObjKey : statusJObj.keySet()) {
                    if (statusJObjKey.contains(",")) {
                        // 如果key中包含逗号，说明该字段为多部门联合执法的情况
                        for (String statusJObjKey02 : neatDeptIdSet) {
                            if (statusJObjKey.contains(statusJObjKey02)
                                && !statusJObjKey.equals(statusJObjKey02)) {
                                statusJObj.put(statusJObjKey02,
                                    statusJObj.getInteger(statusJObjKey02)// 干净的部门ID对应的案件数量，如注释中的dept1
                                        + statusJObj.getInteger(statusJObjKey));// 不干净的部门ID对应的案件数量，如注释中的dept1,dept3
                            }
                        }
                    }
                }
            }
        }
        
        /*
         * ========进行查询操作============结束=============
         */

        /*
         * ========进行数据整合============开始=============
         */
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

        //将没有数据的月份补0
        do {
            String sample = String.valueOf(dl1.getYear()) + String.valueOf(dl1.getMonthOfYear());
            if (!dateTimeIn.contains(sample)) {
                // 说明该年该月下没有数据
                JSONObject fDate3 = new JSONObject();
                fDate3.put("cyear", dl1.getYear());
                fDate3.put("cmonth", dl1.getMonthOfYear());
                fDate3.put("total", 0);

                for(String deptId:dept_ID_NAME_Map.keySet()){
                    fDate3.put(deptId, 0);
                }

                byDept.add(fDate3);
            }
            dl1 = dl1.plusMonths(1);

        } while (dl1.getMillis() <= endDate.getTime());

        //将补0后的结果集进行排序
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

        //整合数据至前端结构
        JSONObject lastest=new JSONObject();

        String[] months=new String[byDept.size()];
        for (int i = 0; i < byDept.size(); i++) {
            JSONObject fakeResultJobj = byDept.getJSONObject(i);
            months[i] = fakeResultJobj.getString("cmonth") + "月份";
        }

        JSONArray countJArray=new JSONArray();
        for (String deptId : dept_ID_NAME_Map.keySet()) {
            JSONObject countJObj = new JSONObject();
            countJObj.put("title", dept_ID_NAME_Map.get(deptId));//部门名称，即在图表里要显示的title

            String[] ccounts=new String[byDept.size()];//保存数量
            for (int i = 0; i < byDept.size(); i++) {
                JSONObject fakeResultJobj = byDept.getJSONObject(i);
                ccounts[i]=fakeResultJobj.getString(deptId);
            }
            countJObj.put("counth", ccounts);

            countJArray.add(countJObj);
        }

        lastest.put("month", months);
        lastest.put("counth", countJArray);

        restResponse.setStatus(200);
        restResponse.setData(lastest);
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

        /*
         * 获取案件处理类型，用于动态生成SQL
         * 避免在添加新的处理类型后，SQL语句报错或查不到结果
         */
        Map<String, String> dealTypeMap = dictFeign.getByCode(Constances.ROOT_BIZ_CASEDEALTYPE);
        Set<String> dealTypes;
        if (BeanUtil.isNotEmpty(dealTypeMap)) {
            dealTypes = dealTypeMap.keySet();
        } else {
            TableResultResponse<Map<String, Object>> result = new TableResultResponse<>();
            result.setStatus(400);
            result.setMessage("未找到案件处理类型，请进行配置。");
            return result;
        }

        Page<Object> result = PageHelper.startPage(page, limit);
        List<Map<String, Object>> list = this.mapper.selectInspectItem(caseRegistration, startTime, endTime, gridIds,dealTypes);
        if (BeanUtil.isEmpty(list)) {
            return new TableResultResponse<Map<String, Object>>(0, null);
        }
        return new TableResultResponse<Map<String, Object>>(result.getTotal(), list);
    }

    /**
     * 按ID查询案件详情，只涉及案件信息
     * 
     * @param id
     * @return
     */
    public ObjectRestResponse<JSONObject> caseRegistration(String id) {
        ObjectRestResponse<JSONObject> restResult = new ObjectRestResponse<>();
        CaseRegistration caseRegistration = this.selectById(id);
        
        JSONObject result = null;
        
        if(BeanUtil.isNotEmpty(caseRegistration)) {
            result = JSONObject.parseObject(JSONObject.toJSONString(caseRegistration));
            if (result != null) {
                getOneQueryAssist(caseRegistration, result);
            }
        }
        
        restResult.setData(result);
        return restResult;
    }

    /**
     * 查询单条案件记录帮助处理方法，在该方法内只对案件记录的业务数据进行处理，不涉及工作流数据
     * @param caseRegistration
     * @param result
     */
    private void getOneQueryAssist(CaseRegistration caseRegistration, JSONObject result) {
        // org:单位，person:个人
        String concernedType = "org";
        JSONObject concernedResult = null;
        // 当事人：单位
        if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG.equals(caseRegistration.getConcernedType())) {
            CLEConcernedCompany concernedCompany =
                cLEConcernedCompanyBiz.selectById(Integer.valueOf(caseRegistration.getConcernedId()));
            if (concernedCompany != null) {
                concernedResult = JSONObject.parseObject(JSONObject.toJSONString(concernedCompany));
            }
        }
        // 当事人：个人
        if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON.equals(caseRegistration.getConcernedType())) {
            CLEConcernedPerson concernedPerson =
                cLEConcernedPersonBiz.selectById(Integer.valueOf(caseRegistration.getConcernedId()));
            if (concernedPerson != null) {
                concernedResult = JSONObject.parseObject(JSONObject.toJSONString(concernedPerson));
                Map<String, String> credMap =
                    dictFeign.getByCodeIn(concernedPerson.getCertType() + "," + concernedPerson.getGender());
                if (credMap != null && !credMap.isEmpty()) {
                    concernedResult.put("credTypeName", credMap.get(concernedPerson.getCertType()));
                    concernedResult.put("sexName", credMap.get(concernedPerson.getGender()));
                }
            }
            concernedType = "person";
        }
        // 当事人类型
        result.put("concernedType", concernedType);
        // 当事人详情
        result.put("concernedResult", concernedResult);

        result.put("caseCheckTime", caseRegistration.getCaseCheckTime());
        result.put("caseSpotCheck", caseRegistration.getCaseSpotCheck());
        
        List<String> dictKeyList = new ArrayList<>();
        String dictKey = "";
        if (StringUtils.isNotBlank(caseRegistration.getBizType())) {
            dictKeyList.add(caseRegistration.getBizType());
        }
        if (StringUtils.isNotBlank(caseRegistration.getCaseSource())) {
            dictKeyList.add(caseRegistration.getCaseSourceType());
        }
        if (StringUtil.isNotBlank(caseRegistration.getDealType())) {
            dictKeyList.add(caseRegistration.getDealType());
        }

        dictKey = String.join(",", dictKeyList);

        Map<String, String> dictValueMap = new HashMap<>();
        if (StringUtils.isNotBlank(dictKey)) {
            dictValueMap = dictFeign.getByCodeIn(dictKey);
        }

        // 业务条线,案件来源,处理方式
        if (BeanUtil.isNotEmpty(dictValueMap)) {
            result.put("bizName", dictValueMap.get(caseRegistration.getBizType()));
            result.put("caseSourceName", dictValueMap.get(caseRegistration.getCaseSourceType()));
            result.put("dealTypeName", dictValueMap.get(caseRegistration.getDealType()));
        }

        // 事件类别
        if (StringUtils.isNotBlank(caseRegistration.getEventType())) {
            EventType eventType = eventTypeBiz.selectById(Integer.valueOf(caseRegistration.getEventType()));
            if (eventType != null) {
                result.put("eventTypeName", eventType.getTypeName());
            }
        }

        // 违法行为
        if (BeanUtil.isNotEmpty(caseRegistration.getInspectItem())) {
            RightsIssues rightsIssues = rightsIssuesBiz.selectById(caseRegistration.getInspectItem());
            if (rightsIssues != null) {
                result.put("inspectName", rightsIssues.getUnlawfulAct());
            }
        }

        /*
         * 举报人姓名,举报人保存的为举报人的姓名，并不是系统内的人员，不能保存ID，无需转化
         * 将之前获取举报人姓名的逻辑去掉
         */

        // 执法者用户名
        JSONArray userList = null;
        try {
            if (StringUtils.isNotBlank(caseRegistration.getEnforcers())) {
                userList = iUserFeign.getByUserIds(caseRegistration.getEnforcers());
            }
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
        AreaGrid parentAreaGrid = areaGridBiz.getParentAreaGrid(areaGrid);
        if (areaGrid != null) {
            String gridName = "";
            if (BeanUtil.isNotEmpty(parentAreaGrid)) {
                gridName = areaGrid.getGridName() + "(" + parentAreaGrid.getGridName() + ")";
            } else {
                gridName = areaGrid.getGridName();
            }
            result.put("gridName", gridName);
        }
        // 移送部门
        String transferDepart = caseRegistration.getTransferDepart();
        if (StringUtils.isNotBlank(transferDepart)) {
            JSONObject dept = adminFeign.getByDeptId(transferDepart);
            if (dept != null) {
                result.put("transferDeptName", dept.getString("name"));
            }
        }

        // 添加案件定位信息
        JSONObject mapInfo = null;
        if (!(caseRegistration.getCaseOngitude() == null
            || caseRegistration.getCaseLatitude() == null)) {
            mapInfo=new JSONObject();
            mapInfo.put("lng", caseRegistration.getCaseOngitude());
            mapInfo.put("lat", caseRegistration.getCaseLatitude());
        }
        result.put("mapInfo", mapInfo == null ? null : mapInfo.toJSONString());
    }
    
    /**
     * 获取执法任务案件
     * 
     * @param ids
     * @return
     */
    public TableResultResponse<JSONObject> listLawTask(Integer[] ids) {
        TableResultResponse<JSONObject> restResult = new TableResultResponse<>();

        Example example = new Example(CaseRegistration.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");

        // 对是否进行了按Ids集合查询做判断，如果没有输入ids，则认为是查询所有执法任务的案件
        if(BeanUtil.isNotEmpty(ids)){
            criteria.andIn("caseSource", Arrays.asList(ids));
        }
        criteria.andEqualTo("caseSourceType", environment.getProperty("caseSourceTypeLawTask"));

        List<CaseRegistration> caseRegi = this.selectByExample(example);
        List<JSONObject> result = new ArrayList<>();
        if (BeanUtil.isNotEmpty(caseRegi)) {
            for (CaseRegistration caseRegistration : caseRegi) {
                try {
                    JSONObject resultJObj =
                        propertiesProxy.swapProperties(caseRegistration, "id", "caseOngitude", "caseLatitude",
                            "caseName");
                    JSONObject mapInfoJObj = new JSONObject();
                    mapInfoJObj.put("lat", caseRegistration.getCaseLatitude());
                    mapInfoJObj.put("lng", caseRegistration.getCaseOngitude());
                    resultJObj.put("mapInfo", mapInfoJObj.toJSONString());
                    result.add(resultJObj);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }

            restResult.setStatus(200);
            restResult.setMessage("成功");
            restResult.getData().setTotal(result.size());
            restResult.getData().setRows(result);
            return restResult;
        }

        restResult.getData().setRows(new ArrayList<>());
        return restResult;
    }

    /**
     * 执法任务全部定位
     * @return
     */
    public TableResultResponse<JSONObject> allPotitionLawTask() {
        TableResultResponse<JSONObject> allTasks = this.listLawTask(null);
        return allTasks;
    }

    /**
     * 案件全部定位
     * @return
     */
    public TableResultResponse<JSONObject> allPotition(JSONObject objs) {
        TableResultResponse<JSONObject> allTasks = this.getAllTasks(objs);

        List<JSONObject> resultList=new ArrayList<>();
        if(BeanUtil.isNotEmpty(allTasks)){
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
     * 手机端案件登记
     *
     * @param caseRegJObj
     */
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRED)
    public Result<Void> addCaseClient(JSONObject caseRegJObj) {
        Result<Void> result = new Result<>();
        // 检查是否需要自动生成案件名称
        checkCaseRegistrationName(caseRegJObj);

        CaseRegistration caseRegistration =
            JSON.parseObject(caseRegJObj.toJSONString(), CaseRegistration.class);

        result = _addCaseRegistrationClient(caseRegJObj, result, caseRegistration);

        return result;
    }

    /**
     * 手机端添加案件信息帮助方法
     * 当案件没有进行暂存过时调用该方法
     * @param caseRegJObj
     * @param result
     * @param caseRegistration
     * @return
     */
    private Result<Void> _addCaseRegistrationClient(JSONObject caseRegJObj, Result<Void> result,
        CaseRegistration caseRegistration) {
        // 添加当事人
        int concernedId = addConcerned(caseRegJObj);

        /*
         * 对于网格：1 如果有网格ID，则直接拿过来 2 如果没有，则通过经纬度定位到相应网格
         * 3 如果不满足第1第2 ，则提示前端
         */
        if (BeanUtil.isEmpty(caseRegistration.getGirdId())) {
            // 前端没有传入网格数据，定位获取
            JSONObject mapInfoJObj = caseRegJObj.getJSONObject("mapInfo");
            if (BeanUtil.isEmpty(mapInfoJObj)) {
                // throw new BizException("添加案件登记失败，未找到相应网格信息。");
            }else{
                /*
                 * 起始：如果没有地址信息，则抛出异常
                 * 现在：将地理信息必传限制去掉，如果没有地理信息，则跳过，有则填充
                 */
                try {
                    caseRegistration.setCaseLatitude(mapInfoJObj.getString("lat"));
                    caseRegistration.setCaseOngitude(mapInfoJObj.getString("lng"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Point point = new Point(mapInfoJObj.getDouble("lng"), mapInfoJObj.getDouble("lat"));
                AreaGrid areaGridReturn = areaGridBiz.isPolygonContainsPoint(point);
                if(BeanUtil.isNotEmpty(areaGridReturn)){
                    caseRegistration.setGirdId(areaGridReturn.getId());
                }
            }
        }

        /*
         * 生成caseRegistration主键
         * 手机端有可能发起了案件暂存，以前端是否传入了案件ID来判断是否进行过案件暂存
         */
        String caseId = caseRegistration.getId();
        if(StringUtils.isBlank(caseId)){
            throw new BizException("请指定案件ID");
        }

        // 当事人主键
        if (concernedId != -1) {
            caseRegistration.setConcernedId(concernedId);
        }

        caseRegistration.setId(caseId);

        // 添加文书
        addWritsInstances(caseRegJObj, caseId);

        // 添加附件
        addAttachments(caseRegJObj, caseId);

        // 确定执法人员的部门
        addEnforcerDept(caseRegistration);

        // 如果案件有图片信息，将图片路径中的空格去除掉
        _trimPicPathWhiteSpace(caseRegistration);

        this.insertSelective(caseRegistration);
        // 将生成的立案ID装入procBizData带回工作流，在工作流中会对procBizId属性进行是否为“-1”的判断，如果是“-1”，将用该ID替换“-1”
        caseRegJObj.put("procBizId", caseId);

        result.setIsSuccess(true);

        return result;
    }

    /**
     * 添加手机端文书实例
     * @param bizData
     */
    public void insertInstanceClient(JSONObject bizData) {
        this.addWritsInstances(bizData, bizData.getString("caseId"));
    }

    /**
     *  通过用户id获取案件列表
     * @param userId
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<Map<String, Object>> getCaseLog(String userId,String caseName, int page, int limit) {
        Page<Object> pageHelper = PageHelper.startPage(page, limit);
        List<Map<String, Object>> result = this.mapper.selectCaseLog(userId,caseName);
        if(BeanUtil.isEmpty(result)){
            return new TableResultResponse<>(0,new ArrayList<>());
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
        //案件流程编码
        bizData.put("procKey","LawEnforcementProcess");
        //业务ids
        bizData.put(Constants.WfProcessBizDataAttr.PROC_BIZID,procBizid.toString());
        JSONObject objs = this.initWorkflowQuery(bizData);

        //缓存工作流实例id
        Map<String,String> tempProcInstIds = new HashMap<>();
        List<Map<String,Object>>  procInstIdList = wfMonitorService.getProcInstIdByUserId(objs);
        for(Map<String,Object> map : procInstIdList){
            //procInstId实例id , procBizid 业务id
            tempProcInstIds.put(String.valueOf(map.get(Constants.WfProcessBizDataAttr.PROC_BIZID)),String.valueOf(map.get("procInstId")));
        }

        Map<String,String> dictTemp = dictFeign.getByCode(Constances.CASE_SOURCE_TYPE);
        for(Map<String,Object> map : result){
            map.put("sourceTypeName",dictTemp.get(map.get("caseSourceType")));
            map.put("procInstId",tempProcInstIds.get(String.valueOf(map.get("id"))));
        }
        return new TableResultResponse<>(pageHelper.getTotal(),result);
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
     * 案件基本信息
     *
     * @param id 案件id
     * @return
     */
    public Map<String, Object> getCaseRegistrationInfo(String id) {
        Map<String, Object> caseRegistration = this.mapper.selectBaseInfoById(id);
        if (caseRegistration == null) {
            throw new RuntimeException("没有找到案件信息！");
        }
        StringBuilder dictCodes = new StringBuilder();
        //业务条线、案件来源、处理方式，数据字典查询
        dictCodes.append(caseRegistration.get("bizType")).append(",")
                .append(caseRegistration.get("caseSourceType")).append(",")
                .append(caseRegistration.get("dealType"));

        Map<String, String> dictMap = dictFeign.getByCodeIn(dictCodes.toString());

        //返回结果集
        Map<String, Object> result = new HashMap<>();
        result.putAll(caseRegistration);

        //当事人类型（默认：个人）
        String concernedType="person";
        //当事人信息
        Map<String, Object> concernedResult = new HashMap<>();
        switch (String.valueOf(caseRegistration.get("concernedType"))) {
            case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON:
                //个人
                CLEConcernedPerson cLEConcernedPerson = cLEConcernedPersonBiz.selectById(caseRegistration.get("concernedId"));
                concernedResult.put("name",cLEConcernedPerson.getName());
                Map<String, String> credMap = dictFeign.getByCodeIn(cLEConcernedPerson.getCertType() + ","
                        + cLEConcernedPerson.getGender());
                if(cLEConcernedPerson != null){
                    concernedResult.put("name",cLEConcernedPerson.getName());
                    concernedResult.put("credTypeName", credMap.get(cLEConcernedPerson.getCertType()));
                    concernedResult.put("sexName", credMap.get(cLEConcernedPerson.getGender()));
                    concernedResult.put("age", cLEConcernedPerson.getAge());
                    concernedResult.put("address", cLEConcernedPerson.getAddress());
                    concernedResult.put("phone", cLEConcernedPerson.getPhone());
                }
                break;
            case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG:
                //单位
                concernedType="org";
                CLEConcernedCompany cLEConcernedCompany = cLEConcernedCompanyBiz.selectById(caseRegistration.get("concernedId"));
                if(cLEConcernedCompany != null){
                    concernedResult.put("name", cLEConcernedCompany.getName());
                    concernedResult.put("address", cLEConcernedCompany.getAddress());
                    concernedResult.put("legalPerson", cLEConcernedCompany.getLegalPerson());
                    concernedResult.put("leadPerson", cLEConcernedCompany.getLeadPerson());
                    concernedResult.put("duties", cLEConcernedCompany.getDuties());
                    concernedResult.put("phone", cLEConcernedCompany.getPhone());
                    concernedResult.put("info", cLEConcernedCompany.getInfo());
                }
                break;
        }

        //移送部门名称
        String transferDepartName = "";
        String deptId = String.valueOf(caseRegistration.get("transferDepart"));
        JSONObject deptInfo = adminFeign.getByDeptId(deptId);
        if(deptInfo != null){
            transferDepartName = deptInfo.getString("name");
        }
        result.put("transferDepartName", transferDepartName);

        result.put("concernedType", concernedType);
        result.put("concernedResult", concernedResult);
        //业务条线
        result.put("bizName", dictMap.get(caseRegistration.get("bizType")));
        //案件来源
        result.put("sourceTypeName", dictMap.get(caseRegistration.get("caseSourceType")));
        //处理方式
        result.put("dealTypeName", dictMap.get(caseRegistration.get("dealType")));

        //拼接执法者用户列表
        JSONArray userList = iUserFeign.getByUserIds(String.valueOf(caseRegistration.get("enforcers")));
        StringBuilder enforcersName = new StringBuilder();
        if (userList != null) {
            JSONObject obj;
            for (int i = 0; i < userList.size(); i++) {
                obj = userList.getJSONObject(i);
                if(obj == null){
                    continue;
                }
                enforcersName.append(obj.getString("name"));
                if(i < userList.size()-1){
                    enforcersName.append(",");
                }
            }
        }
        //执法者
        result.put("enforcersName", enforcersName.toString());
        return result;
    }

    /**
     * 在进行案件审批操作时，添加附件(文书模板上传)
     * @param jobjs
     */
    public void addAttachments(JSONObject jobjs) {
        if (BeanUtil.isNotEmpty(jobjs)) {
            this.addAttachments(jobjs, jobjs.getString("procBizId"));
        }
    }

    /**
     * 案件热力图分析
     * @param startDate
     * @param endDate
     * @return
     */
    public TableResultResponse<JSONObject> heatMap(String startDate, String endDate) {
        DateTime dateTime = new DateTime(DateUtil.dateFromStrToDate(endDate, "yyyy-MM").getTime());
        DateTime dateTime1 = dateTime.plusMonths(1);
        endDate =
            DateUtil.dateFromDateToStr(new Date(dateTime1.plusMonths(1).getMillis()), "yyyy-MM");

        List<JSONObject> rows=this.mapper.heatMap(startDate,endDate);
        if(BeanUtil.isNotEmpty(rows)){
            JSONObject mapInfoJObj=new JSONObject();
            for(JSONObject jobTmp:rows){
                //"mapInfo": "{\"lng\":120.514687,\"lat\":31.818712}"
                mapInfoJObj.put("lng", jobTmp.get("caseOngitude"));
                mapInfoJObj.put("lat", jobTmp.get("caseLatitude"));
                jobTmp.put("mapInfo", mapInfoJObj.toJSONString());
                mapInfoJObj.remove("caseOngitude");
                mapInfoJObj.remove("caseLatitude");
            }
            return new TableResultResponse<>(0, rows);
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

    /**
     * 指挥中心首页监管对象发生的案件量
     * @param queryJObj
     * @return
     */
    public TableResultResponse<JSONObject> regObjCount(JSONObject queryJObj) {
        /*
         * 在案件当事人表添加监管对象ID字段后，对该方法进行重构
         * 之前使用监管对象名称作关联
         */

        String lawTaskIds = queryJObj.getString("lawTaskIds");
        if(StringUtils.isNotBlank(lawTaskIds)){
            queryJObj.put("lawTaskIds", Arrays.asList(lawTaskIds.split(",")));
        }
        List<JSONObject> rows= this.mapper.regObjCount(queryJObj);

        //  regulaObjectId
        if(BeanUtil.isNotEmpty(rows)){
            List<Integer> objIdInRows = rows.stream().map(o -> o.getInteger("regulaObjectId")).distinct().collect(Collectors.toList());

            List<LawPatrolObject> lawPatrolObjectList =
                lawPatrolObjectBiz
                    .getByLawTaskIds(new HashSet<>(Arrays.asList(lawTaskIds.split(","))));
            // 按LawPatrolObject查询监管对象
            Map<Integer, RegulaObject> lawPatrolObjectNameIdMap = new HashMap<>();
            if (BeanUtil.isNotEmpty(lawPatrolObjectList)) {
                List<String> objIdList =
                    lawPatrolObjectList.stream().map(o -> String.valueOf(o.getRegulaObjectId()))
                        .distinct().collect(Collectors.toList());
                List<RegulaObject> regulaObjects =
                    regulaObjectBiz.selectByIds(String.join(",", objIdList));
                if (BeanUtil.isNotEmpty(regulaObjects)) {
                    for (RegulaObject regTmp : regulaObjects) {
                        lawPatrolObjectNameIdMap.put(regTmp.getId(), regTmp);
                    }
                }
            }

            // 整合尚未检查的监管对象,在lawPatrolObjectNameIdMap里保存的是全部监管对象信息
            for (Map.Entry<Integer, RegulaObject> e : lawPatrolObjectNameIdMap.entrySet()) {
                if (!objIdInRows.contains(e.getKey())) {
                    // 说明在结果集中尚未包含该监管对象
                    JSONObject tmpJObj = new JSONObject();
                    tmpJObj.put("mapInfo", e.getValue().getMapInfo());
                    tmpJObj.put("objName", e.getValue().getObjName());
                    tmpJObj.put("patrolCount", 0);
                    tmpJObj.put("pCountWithProblem", 0);
                    rows.add(tmpJObj);
                }
            }

            return new TableResultResponse<>(rows.size(), rows);
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

    /**
     * 检查是否需要自动生成案件名称
     * 如果没有案件名称，则自动生成
     * @param caseRegJObj
     * @return
     */
    private void checkCaseRegistrationName(JSONObject caseRegJObj) {
        /*
         * 如果案件名称为空，需要根据当事人类型自动生成相应的案件名称
         * 案件名称=【当事人姓名或单位名称】+【违法行为】
         */
        boolean isCaseNameBlank = StringUtils.isBlank(caseRegJObj.getString("caseName"));
        
        // 判断是否传入当事人信息
        JSONObject concernedJObj = caseRegJObj.getJSONObject("concerned");
        if (concernedJObj != null) {
            // 有当事人信息
            String concernedType = caseRegJObj.getString("concernedType");
            switch (concernedType) {
                case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG:
                    if (isCaseNameBlank) {
                        // 当事人以单位形式存在
                        CLEConcernedCompany concernedCompany =
                            JSON.parseObject(concernedJObj.toJSONString(),
                                CLEConcernedCompany.class);

                        // 查询权力事项
                        Integer rightsIssuesId = caseRegJObj.getInteger("rightId");
                        RightsIssues rightsIssues = null;
                        if (BeanUtil.isNotEmpty(rightsIssuesId)) {
                            rightsIssues = rightsIssuesBiz.selectById(rightsIssuesId);
                        }

                        List<String> caseNameList = new ArrayList<>();
                        String caseName = "";
                        if (BeanUtil.isNotEmpty(concernedCompany)
                            && StringUtils.isNotBlank(concernedCompany.getName())) {
                            caseNameList.add(concernedCompany.getName());
                        }
                        if (BeanUtil.isNotEmpty(rightsIssues)
                            && StringUtils.isNotBlank(rightsIssues.getUnlawfulAct())) {
                            caseNameList.add(rightsIssues.getUnlawfulAct());
                        }
                        if (BeanUtil.isNotEmpty(caseNameList)) {
                            caseName = String.join("_", caseNameList);
                        }

                        caseRegJObj.put("caseName", caseName);
                    }
                    break;
                case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON:
                    // 当事人以人个形式存在

                    if (isCaseNameBlank) {
                        CLEConcernedPerson concernedPerson =
                            JSON.parseObject(concernedJObj.toJSONString(),
                                CLEConcernedPerson.class);

                        // 查询权力事项
                        Integer rightsIssuesId = caseRegJObj.getInteger("rightId");
                        RightsIssues rightsIssues = null;
                        if (BeanUtil.isNotEmpty(rightsIssuesId)) {
                            rightsIssues = rightsIssuesBiz.selectById(rightsIssuesId);
                        }

                        List<String> caseNameList = new ArrayList<>();
                        String caseName = "";
                        if (BeanUtil.isNotEmpty(concernedPerson)
                            && StringUtils.isNotBlank(concernedPerson.getName())) {
                            caseNameList.add(concernedPerson.getName());
                        }
                        if (BeanUtil.isNotEmpty(rightsIssues)
                            && StringUtils.isNotBlank(rightsIssues.getUnlawfulAct())) {
                            caseNameList.add(rightsIssues.getUnlawfulAct());
                        }
                        if (BeanUtil.isNotEmpty(caseNameList)) {
                            caseName = String.join("_", caseNameList);
                        }

                        caseRegJObj.put("caseName", caseName);
                    }
                    break;
            }
        }
    }

    /**
     * 双向推送业务处理
     * @param objs
     * @return
     */
    public TableResultResponse<JSONObject> pushOfTwoWays(JSONObject objs) {
        JSONObject queryData = objs.getJSONObject("queryData");
        // 将请求数据转化到对象中
        CaseRegistration queryCaseRegistration = queryData.toJavaObject(CaseRegistration.class);

        // 默认分页为第1页，每页10条数据
        Integer page =
            BeanUtil.isEmpty(queryData.getInteger("page")) ? 1 : queryData.getInteger("page");
        Integer limit =
            BeanUtil.isEmpty(queryData.getInteger("limit")) ? 10 : queryData.getInteger("limit");

        TableResultResponse<CaseRegistration> list = this.getList(queryCaseRegistration,null, page, limit);

        List<JSONObject> result=new ArrayList<>();
        long total =0L;

        if(BeanUtil.isNotEmpty(list)){
            total=list.getData().getTotal();
            List<CaseRegistration> rows = list.getData().getRows();
            if(BeanUtil.isNotEmpty(rows)){

                // 整合业务条线
                List<String> dictKeys=new ArrayList<>();
                List<String> dealTypeKey =
                    rows.stream().map(o -> o.getDealType()).distinct().collect(Collectors.toList());// 处理类型
                List<String> bizTypeKey =
                    rows.stream().map(o -> o.getBizType()).distinct().collect(Collectors.toList());// 业务条线

                dictKeys.addAll(dealTypeKey);
                dictKeys.addAll(bizTypeKey);

                Map<String, String> dictValues=new HashMap<>();
                if(BeanUtil.isNotEmpty(dictKeys)){
                    dictValues = dictFeign.getByCodeIn(String.join(",", dictKeys));
                }

                // 转化查询结束，向前端传送部分字段
                for(CaseRegistration tmp:rows){
                    JSONObject tmpJObj=new JSONObject();
                    tmpJObj.put("id", tmp.getId());
                    tmpJObj.put("caseName", tmp.getCaseName());
                    tmpJObj.put("caseContent", tmp.getCaseContent());
                    tmpJObj.put("dealType", dictValues.get(tmp.getDealType()));
                    tmpJObj.put("bizType", dictValues.get(tmp.getBizType()));
                    tmpJObj.put("caseCode", tmp.getCaseCode());
                    result.add(tmpJObj);
                }
            }
        }

        return new TableResultResponse<>(total, result);
    }

    /**
     * 双向推送案件详情
     * @param objs
     * @return
     */
    public JSONObject pushOfTwoWaysDetail(JSONObject objs) {
        JSONObject bizData = objs.getJSONObject("bizData");

        CaseRegistration caseRegistration = this.selectById(bizData.getString("procBizId"));
        JSONObject result = new JSONObject();
        if (StringUtils.equals("1", caseRegistration.getIsDeleted())) {
            // 如果案件被删除，则直接返回
            return result;
        }

        // 案件名称
        result.put("caseName", caseRegistration.getCaseName());

        // org:单位，person:个人
        // 当事人：单位
        if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG
            .equals(caseRegistration.getConcernedType())) {
            CLEConcernedCompany concernedCompany =
                cLEConcernedCompanyBiz
                    .selectById(Integer.valueOf(caseRegistration.getConcernedId()));
            if (concernedCompany != null) {
                Map<String, Object> conditions = new HashMap<>();
                conditions.put("regulaObjId", concernedCompany.getName());
                List<EnterpriseInfo> enterpriseInfos = enterpriseInfoBiz.getByMap(conditions);

                result.put("concernedPerson", concernedCompany.getName());
                if (BeanUtil.isNotEmpty(enterpriseInfos)) {
                    result.put("creditCode", enterpriseInfos.get(0).getCreditCode());
                } else {
                    result.put("creditCode", "");
                }
            }
        }
        // 当事人：个人
        if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON
            .equals(caseRegistration.getConcernedType())) {
            CLEConcernedPerson concernedPerson =
                cLEConcernedPersonBiz
                    .selectById(Integer.valueOf(caseRegistration.getConcernedId()));
            if (concernedPerson != null) {
                result.put("concernedPerson", concernedPerson.getName());
                result.put("creditCode", concernedPerson.getCertCode());
            }
        }

        List<String> dictKeyList=new ArrayList<>();
        if (StringUtils.isNotBlank(caseRegistration.getCaseSourceType())) {
            dictKeyList.add(caseRegistration.getCaseSourceType());
        }

        // 查询字典
        Map<String, String> byCodeIn=new HashMap<>();
        if (BeanUtil.isNotEmpty(dictKeyList)) {
            byCodeIn = dictFeign.getByCodeIn(String.join(",", dictKeyList));
        }

        // 案件来源
        if(BeanUtil.isNotEmpty(byCodeIn)){
            result.put("caseSourceType", byCodeIn.get(caseRegistration.getCaseSourceType()));
        }

        // 来源时间
        result.put("caseSourceTime", caseRegistration.getCaseSourceTime());


        result.put("caseCode", caseRegistration.getCaseCode());// 案件编号
        result.put("caseContent", caseRegistration.getCaseContent());// 案件内容
        result.put("caseTime", caseRegistration.getCaseTime());// 案发时间
        result.put("caseAddress", caseRegistration.getCaseAddress());// 案发地址

        // 执法队员
        List<String> userNameList = new ArrayList<>();
        if (StringUtils.isNotBlank(caseRegistration.getEnforcers())) {
            JSONArray infoByUserIds = adminFeign.getInfoByUserIds(caseRegistration.getEnforcers());
            if (BeanUtil.isNotEmpty(infoByUserIds)) {
                for (int i = 0; i < infoByUserIds.size(); i++) {
                    JSONObject jsonObject = infoByUserIds.getJSONObject(i);
                    userNameList.add(jsonObject.getString("userName"));
                }
            }
        }
        result.put("enforcers", String.join(",", userNameList));

        // 案件状态
        String exeStatus = caseRegistration.getExeStatus();
        if (StringUtils.isNotBlank(exeStatus)) {
            switch (exeStatus) {
                case CaseRegistration.EXESTATUS_STATE_TODO:
                    result.put("exeStatus",
                        new String(
                            environment.getProperty("caseRegistration.exeStatus.zero").getBytes(StandardCharsets.ISO_8859_1),
                            StandardCharsets.UTF_8));
                    break;
                case CaseRegistration.EXESTATUS_STATE_FINISH:
                    result.put("exeStatus",
                            new String(
                                    environment.getProperty("caseRegistration.exeStatus.one").getBytes(StandardCharsets.ISO_8859_1),
                                    StandardCharsets.UTF_8));
                    break;
                case CaseRegistration.EXESTATUS_STATE_STOP:
                    result.put("exeStatus",
                            new String(
                                    environment.getProperty("caseRegistration.exeStatus.two").getBytes(StandardCharsets.ISO_8859_1),
                                    StandardCharsets.UTF_8));
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    /**
     * 通过监管对象ids获取案件量
     *
     * @param regulaObjIds
     * @return
     */
    public List<Map<String, Long>> selectByRegulaObjectId(Set<String> regulaObjIds) {
        if (BeanUtil.isEmpty(regulaObjIds)) {
            return new ArrayList<>();
        }
        List<Map<String, Long>> result =
                this.mapper.selectByRegulaObjectId(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG, regulaObjIds);
        return BeanUtil.isEmpty(regulaObjIds) ? new ArrayList<>() : result;
    }

    /**
     * 案件暂存
     * @param objs
     * @return
     */
    public ObjectRestResponse<Void> updateCache(JSONObject objs) {
        ObjectRestResponse<Void> objectResult=new ObjectRestResponse<>();

        JSONObject bizData = objs.getJSONObject(Constants.WfRequestDataTypeAttr.PROC_BIZDATA);

        // 去更新文书或添加附件
        writsInstancesBiz.addWritsInstances(bizData);
        this.addAttachments(bizData);
        String caseId = bizData.getString(Constants.WfProcessBizDataAttr.PROC_BIZID);

        CaseRegistration caseRegistrationToUpdate = bizData.toJavaObject(CaseRegistration.class);

        // 暂存当事人
        _cacheConcerned(bizData, caseId, caseRegistrationToUpdate);

        // 更新案件自身信息
        caseRegistrationToUpdate.setId(caseId);
        this.updateSelectiveById(caseRegistrationToUpdate);

        objectResult.setMessage("案件暂存成功");
        objectResult.setStatus(200);
        return objectResult;
    }

    private void _cacheConcerned(JSONObject bizData, String caseId, CaseRegistration caseRegistrationToUpdate) {
        String concernedType = bizData.getString("concernedType");
        if(StringUtils.isBlank(concernedType)){
            // 如果暂存的当事人类型不明确，直接结束方法
            return;
        }

        CaseRegistration caseRegistrationInDB = this.selectById(caseId);
        if(BeanUtil.isEmpty(caseRegistrationInDB)){
            caseRegistrationInDB=new CaseRegistration();
        }

        /*
         * 对于当事人类型
         * 1 可能切换了当事人类型，如之前类型为单位，现在切换为了个人。如果进行了切换，则在切换后的当事人表里添加一条记录，
         *   否则在原当事人上进行更新操作。这里认为未切换当事人前一定存在了当事人记录
         * 2 案件在登记时并未指定当事人，此时当事人类型字段为NULL，使用!StringUtils.equals(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG,
                        caseRegistrationInDB.getConcernedType())进行判断时，返回的为true,依然会执行插入操作
         */
        switch (concernedType) {
            case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG:
                // 当事人人单位
                CLEConcernedCompany cleConcernedCompany =
                    bizData.toJavaObject(CLEConcernedCompany.class);

                if (!StringUtils.equals(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG,
                        caseRegistrationInDB.getConcernedType())) {
                    // 说明切换了当事人类型
                    cLEConcernedCompanyBiz.insertSelective(cleConcernedCompany);
                    caseRegistrationToUpdate.setConcernedId(cleConcernedCompany.getId());
                } else {
                    cleConcernedCompany.setId(caseRegistrationInDB.getConcernedId());
                    cLEConcernedCompanyBiz.updateSelectiveById(cleConcernedCompany);
                }
                break;

            case Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON:
                // 当事人为个人
                CLEConcernedPerson cleConcernedPerson =
                    bizData.toJavaObject(CLEConcernedPerson.class);

                if (!StringUtils.equals(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON,
                        caseRegistrationInDB.getConcernedType())) {
                    // 说明切换了当事人类型
                    cLEConcernedPersonBiz.insertSelective(cleConcernedPerson);
                    caseRegistrationToUpdate.setConcernedId(cleConcernedPerson.getId());
                } else {
                    cleConcernedPerson.setId(caseRegistrationInDB.getConcernedId());
                    cLEConcernedPersonBiz.updateSelectiveById(cleConcernedPerson);
                }
                break;
            default:
        }


    }

    /**
     * 按来源查询案件
     * @param caseSourceType
     * @param caseSourceSet
     * @return
     */
    public List<CaseRegistration> getByCaseSource(String caseSourceType,Set<String> caseSourceSet){
        Example example=new Example(CaseRegistration.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("caseSourceType",caseSourceType);
        criteria.andIn("caseSource", caseSourceSet );

        List<CaseRegistration> caseRegistrations = this.selectByExample(example);

        if(BeanUtils.isEmpty(caseRegistrations)){
            return new ArrayList<>();
        }

        return caseRegistrations;
    }

    /**
     *
     * @param id
     * @return
     */
    public JSONObject getInfoById(String id) {
        /*
         * 1 查询与id关联的工作流实例ID
         * 2 生成工作流需要的请求参数结构
         */

        // 工作流业务
        JSONObject bizData = new JSONObject();
        // 事件流程编码
        bizData.put("procKey", "LawEnforcementProcess");
        // 业务ids
        bizData.put(Constants.WfProcessBizDataAttr.PROC_BIZID, "'"+String.valueOf(id)+"'");
        JSONObject objs = initWorkflowQuery(bizData);

        JSONObject queryData=new JSONObject();

        // 查询流程实例ID
        List<Map<String, Object>> procInstIdList = wfMonitorService.getProcInstIdByUserId(objs);

        queryData.put
                ("caseRegistrationId",id);
        objs.put("queryData", queryData);
        if (BeanUtil.isNotEmpty(procInstIdList)) {
            // 请求中，只通过一个id进行查询，所以返回结果如果不为空，则长度一定为1
            String procInstId = String.valueOf(procInstIdList.get(0).get("procInstId"));
            objs.getJSONObject("procData").put("procInstId", procInstId);

            return this.getInfoById(objs);
        }

        return new JSONObject();
    }

    /**
     * 按事件来源查询事件详情，也就意味着该处查询的是经过中心交办的案件详情
     * 
     * @param sourceType
     *            事件当时来源类型
     * @param sourceCode
     *            事件当时来源ID
     * @return
     */
    public ObjectRestResponse<JSONObject> detailForCaseInfoSource(String sourceType,
        String sourceCode) {

        ObjectRestResponse<JSONObject> result = new ObjectRestResponse<>();

        /*
         * 1-> 通过来源查询到对应的事件
         * 2-> 按事件查询具体对应中心交办后的案件详情
         */
        // 1->
        CaseInfo queryCaseInfo = new CaseInfo();
        queryCaseInfo.setIsDeleted("0");
        queryCaseInfo.setSourceCode(sourceCode);
        queryCaseInfo.setSourceType(sourceType);
        CaseInfo caseInfo = caseInfoMapper.selectOne(queryCaseInfo);

        if (BeanUtil.isEmpty(caseInfo)) {
            result.setStatus(200);
            result.setData(null);
            return result;
        }

        CaseRegistration queryCaerRegistration = new CaseRegistration();
        queryCaerRegistration.setIsDeleted("0");
        queryCaerRegistration.setCaseSource(String.valueOf(caseInfo.getId()));
        queryCaerRegistration.setCaseSourceType(CaseRegistration.CASE_SOURCE_TYPE_CENTER);
        CaseRegistration caseRegistration = this.selectOne(queryCaerRegistration);
        if (BeanUtil.isEmpty(caseRegistration)) {
            result.setStatus(200);
            result.setData(null);
            return result;
        }

        JSONObject infoById = this.getInfoById(caseRegistration.getId());
        result.setData(infoById);
        result.setStatus(200);
        return result;
    }

    /**
     * 案件删除
     * @param objs
     * @return
     */
    @Transactional
    public ObjectRestResponse<Void> suspentCaseInfo(JSONObject objs) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        /*
         * 1-> 更新事件记录
         * 2-> 挂起流程
         */
        // 1->
        JSONObject bizData = objs.getJSONObject("bizData");
        String procBizId = bizData.getString("procBizId");
        if (BeanUtil.isNotEmpty(procBizId)) {
            CaseRegistration caseReg = new CaseRegistration();
            caseReg.setId(procBizId);
            caseReg.setIsDeleted(BooleanUtil.BOOLEAN_TRUE);
            this.updateSelectiveById(caseReg);
        }

        // 2->
        try {
            wfProcTaskService.suspendProcess(objs);
        } catch (WorkflowException e) {
            restResult.setStatus(400);
            restResult.setMessage(e.getMessage());
            return restResult;
        }

        restResult.setMessage("案件删除成功");
        restResult.setStatus(200);
        return restResult;
    }

    /**
     * 案件恢复
     * @param objs
     * @return
     */
    @Transactional
    public ObjectRestResponse<Void> activeCaseInfo(JSONObject objs) {
        ObjectRestResponse<Void> restResult = new ObjectRestResponse<>();

        /*
         * 1-> 更新事件记录
         * 2-> 挂起流程
         */
        // 1->
        JSONObject bizData = objs.getJSONObject("bizData");
        String procBizId = bizData.getString("procBizId");
        if (BeanUtil.isNotEmpty(procBizId)) {
            CaseRegistration caseReg = new CaseRegistration();
            caseReg.setId(procBizId);
            caseReg.setIsDeleted(BooleanUtil.BOOLEAN_FALSE);
            this.updateSelectiveById(caseReg);
        }

        // 2->
        try {
            wfProcTaskService.activeProcess(objs);
        } catch (WorkflowException e) {
            restResult.setStatus(400);
            restResult.setMessage(e.getMessage());
            return restResult;
        }

        restResult.setMessage("案件恢复成功");
        restResult.setStatus(200);
        return restResult;
    }


    /**
     * 案件管理
     *
     * @param objs
     *            {"bizData":{}, "procData":{}, "authData":{"procAuthType":"2"},
     *            "variableData":{}, "queryData":{ } }<br/>
     * @return
     */
    public TableResultResponse<JSONObject> getCaseRegistrationManageList(JSONObject objs) {
        return allTasksAssist(objs,true);
    }

    private TableResultResponse<JSONObject> allTasksAssist(JSONObject objs,boolean isIntegratedQuery) {
        List<JSONObject> result = new ArrayList<>();

        // 业务查询条件
        JSONObject queryData = objs.getJSONObject("queryData");
        // 表征当前查询是否为案件管理页面
        queryData.put("isIntegratedQuery", isIntegratedQuery);
        CaseRegistration queryCaseRegistration = new CaseRegistration();
        queryCaseRegistration = JSONObject.parseObject(queryData.toJSONString(), CaseRegistration.class);

        // 工作流查询条件
        JSONObject bizData = objs.getJSONObject("bizData");
        // 事件工作流的定义代码
        bizData.put("prockey", "LawEnforcementProcess");
        if (StringUtils.isNotBlank(queryData.getString("procCtaskname"))) {
            /*
             * 是否按进度进行查找(即任务表中·PROC_CTASKNAME·字段)
             * 在需要中，进度除了流程图中节点进度外，还有自定义的【已结案】【已中止】进度
             * 参数都通过procCtaskname传入,需进行判断是否为自定义进度
             * 如果是自定义进度，则不进工作流中进行节点名称查询
             */
            if (!(CaseRegistration.EXESTATUS_STATE_FINISH.equals(queryData.getString("procCtaskname"))
                || CaseRegistration.EXESTATUS_STATE_STOP.equals(queryData.getString("procCtaskname")))) {
                bizData.put("procCtaskname", queryData.getString("procCtaskname"));
            }
        }
        objs.put("bizData", bizData);

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
}