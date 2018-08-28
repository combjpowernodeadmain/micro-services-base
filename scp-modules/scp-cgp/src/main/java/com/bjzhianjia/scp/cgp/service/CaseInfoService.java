package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.ConcernedCompanyBiz;
import com.bjzhianjia.scp.cgp.biz.ConcernedPersonBiz;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.ExecuteInfoBiz;
import com.bjzhianjia.scp.cgp.biz.LeadershipAssignBiz;
import com.bjzhianjia.scp.cgp.biz.MayorHotlineBiz;
import com.bjzhianjia.scp.cgp.biz.PatrolTaskBiz;
import com.bjzhianjia.scp.cgp.biz.PublicOpinionBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.ExecuteInfo;
import com.bjzhianjia.scp.cgp.entity.LeadershipAssign;
import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.entity.PublicOpinion;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectMapper;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.service.impl.WfMonitorServiceImpl;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskHistoryBean;
import com.bjzhianjia.scp.security.wf.base.task.service.impl.WfProcTaskServiceImpl;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

/**
 * 立案管理
 * 
 * @author 尚
 */
@Service
@Slf4j
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
	public ObjectRestResponse<CaseInfo> get(Integer id) {
		CaseInfo caseInfo = this.caseInfoBiz.selectById(id);
		return new ObjectRestResponse<CaseInfo>().data(caseInfo);
	}

	/**
	 * 查询个人待办任务(工作流)
	 * 
	 * @author 尚
	 * @param objs {"bizData":{}, "procData":{}, "authData":{"procAuthType":"2"},
	 *             "variableData":{}, "queryData":{ "isQuery":"false" } }<br/>
	 *             如果存在查询条件，则将"isQuery"设为true,并在"queryData"大项内添加查询条件
	 * @return
	 */
	public TableResultResponse<JSONObject> getUserToDoTasks(JSONObject objs) {
		CaseInfo queryCaseInfo = new CaseInfo();
		JSONObject queryData = objs.getJSONObject("queryData");

		if ("true".equals(queryData.getString("isQuery"))) {
			queryCaseInfo = JSONObject.parseObject(queryData.toJSONString(), CaseInfo.class);
			if (StringUtils.isNotBlank(queryData.getString("procCtaskname"))) {
				// 是否按进度进行查找(即任务表中·PROC_CTASKNAME·字段)
				objs.getJSONObject("bizData").put("procCtaskname", queryData.getString("procCtaskname"));
			}
		}

		List<JSONObject> jObjList = new ArrayList<>();

		// 查询待办工作流任务
		PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getUserToDoTasks(objs);
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

		if ("true".equals(queryData.getString("isQuery"))) {
			queryCaseInfo = JSONObject.parseObject(queryData.toJSONString(), CaseInfo.class);
			if (StringUtils.isNotBlank(queryData.getString("procCtaskname"))) {
				// 是否按进度进行查找(即任务表中·PROC_CTASKNAME·字段)
				objs.getJSONObject("bizData").put("procCtaskname", queryData.getString("procCtaskname"));
			}
		}

		List<JSONObject> jObjList = new ArrayList<>();

		// 查询待办工作流任务
		PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getAllToDoTasks(objs);
		List<WfProcBackBean> list = pageInfo.getList();

		if (list != null && !list.isEmpty()) {
			// 有待办任务
			return queryAssist(queryCaseInfo, queryData, jObjList, pageInfo, objs);
		} else {
			// 无待办任务
			return new TableResultResponse<>(0, jObjList);
		}
	}

	private TableResultResponse<JSONObject> queryAssist(CaseInfo queryCaseInfo, JSONObject queryData,
			List<JSONObject> jObjList, PageInfo<WfProcBackBean> pageInfo, JSONObject objs) {
		List<WfProcBackBean> procBackBeanList = pageInfo.getList();

		List<Integer> bizIdStrList = new ArrayList<>();
		List<String> eventTypeIdStrList = new ArrayList<>();

		Set<String> rootBizIdSet = new HashSet<>();

		if (procBackBeanList != null && !procBackBeanList.isEmpty()) {
			for (int i = 0; i < procBackBeanList.size(); i++) {
				WfProcBackBean wfProcBackBean = procBackBeanList.get(i);
				try {
					bizIdStrList.add(Integer.valueOf(wfProcBackBean.getProcBizid()));
				} catch (NumberFormatException e) {
					continue;
				}
			}
		}

		// 查询与工作流任务对应的业务
		TableResultResponse<CaseInfo> tableResult = caseInfoBiz.getList(queryCaseInfo, bizIdStrList,queryData);

		List<CaseInfo> caseInfoList = tableResult.getData().getRows();

		Map<Integer, CaseInfo> caseInfo_ID_Entity_Map = new HashMap<>();
		for (CaseInfo caseInfo : caseInfoList) {
			caseInfo_ID_Entity_Map.put(caseInfo.getId(), caseInfo);

			rootBizIdSet.add(caseInfo.getBizList());
			rootBizIdSet.add(caseInfo.getSourceType());
			rootBizIdSet.add(caseInfo.getCaseLevel());

			eventTypeIdStrList.add(caseInfo.getEventTypeList());
		}

		/*
		 * 查询业务条线，查询事件来源，查询事件级别
		 */
		Map<String, String> rootBizList = new HashMap<>();
		if (rootBizIdSet != null && !rootBizIdSet.isEmpty()) {
			rootBizList = dictFeign.getByCodeIn(String.join(",", rootBizIdSet));
		}

		// 查询事件类别
		Map<String, String> eventTypeMap = new HashMap<>();
		String eventTypeName = "";
		if (eventTypeIdStrList != null && !eventTypeIdStrList.isEmpty()) {
			List<EventType> eventTypeList = new ArrayList<>();
			eventTypeList = eventTypeMapper.selectByIds(String.join(",", eventTypeIdStrList));
			List<String> eventTypeNameList = new ArrayList<>();
			for (EventType eventType : eventTypeList) {
				if (StringUtils.isNotBlank(eventType.getTypeName())) {
					eventTypeNameList.add(eventType.getTypeName());
				}
				eventTypeMap.put(String.valueOf(eventType.getId()), eventType.getTypeName());
			}
			eventTypeName = String.join(",", eventTypeNameList);
		}

		for (WfProcBackBean tmp : procBackBeanList) {
			JSONObject wfJObject = JSONObject.parseObject(JSON.toJSONString(tmp));

			CaseInfo caseInfo = caseInfo_ID_Entity_Map.get(Integer.valueOf(tmp.getProcBizid()));
			if (caseInfo != null) {
				JSONObject parse = JSONObject.parseObject(JSON.toJSONString(caseInfo));
				wfJObject.putAll(parse);

				wfJObject.put("bizListName", getRootBizTypeName(caseInfo.getBizList(), rootBizList));
				wfJObject.put("eventTypeListName", eventTypeName);
				wfJObject.put("sourceTypeName", getRootBizTypeName(caseInfo.getSourceType(), rootBizList));
				wfJObject.put("caseLevelName", getRootBizTypeName(caseInfo.getCaseLevel(), rootBizList));
				
				wfJObject.put("isUrge", "0".equals(caseInfo.getIsUrge())?false:true);
				wfJObject.put("isSupervise", "0".equals(caseInfo.getIsSupervise())? false:true);
				wfJObject.put("isOvertime",caseInfo.getDeadLine().compareTo(new Date())>0? true:false);
				wfJObject.put("caseInfoId",caseInfo.getId());
				jObjList.add(wfJObject);
			}
		}

		return new TableResultResponse<>(tableResult.getData().getTotal(), jObjList);
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
			reportPersonId = mayorHotline.getCrtUserId();
			resultJObjct = JSONObject.parseObject(JSON.toJSONString(mayorHotline));
			resultJObjct.put("eventSourceType", "市长热线12345");
			resultJObjct.put("sourceCode", mayorHotline.getHotlnCode());

			zhaiyaoList.add(mayorHotline.getHotlnType());
			zhaiyaoList.add(mayorHotline.getHotlnSubType());
			zhaiyaoList.add(mayorHotline.getAppealTel());
			if (mayorHotline.getReplyDatetime() != null) {
				zhaiyaoList.add(DateUtil.dateFromDateToStr(mayorHotline.getReplyDatetime(), "yyyy-MM-dd HH:mm:ss"));
			}
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CONSENSUS:
			// 舆情
			PublicOpinion poinion = publicOpinionBiz.selectById(Integer.valueOf(caseInfo.getSourceCode()));

			reportPersonId = poinion.getCrtUserId();

			// 查询字典里对应的值
			List<String> dictIdList = new ArrayList<>();
			dictIdList.add(poinion.getOpinType());
			dictIdList.add(poinion.getOpinLevel());
			resultJObjct = JSONObject.parseObject(JSON.toJSONString(poinion));
			resultJObjct.put("eventSourceType", "舆情");
			resultJObjct.put("sourceCode", poinion.getOpinCode());

			Map<String, String> dictValueMap = dictFeign.getByCodeIn(String.join(",", dictIdList));
			if (dictValueMap != null && !dictValueMap.isEmpty()) {
				zhaiyaoList.add(dictValueMap.get(poinion.getOpinType()));
				zhaiyaoList.add(dictValueMap.get(poinion.getOpinLevel()));
				zhaiyaoList.add(poinion.getOpinPort());
			}
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_LEADER:
			// 领导交办
			LeadershipAssign leadershipAssign = leadershipAssignBiz
					.selectById(Integer.valueOf(caseInfo.getSourceCode()));
			resultJObjct = JSONObject.parseObject(JSON.toJSONString(leadershipAssign));
			resultJObjct.put("eventSourceType", "领导交办");
			resultJObjct.put("sourceCode", leadershipAssign.getTaskCode());

			reportPersonId = leadershipAssign.getCrtUserId();

			// 查询数据库中交办领导,领导可能有多《数据库中直接保存的是领导的人名》
//			List<String> leaderNameList = new ArrayList<>();
//			Map<String, String> leaderMap = adminFeign.getUser(leadershipAssign.getTaskLeader());
//			Set<String> keySet2 = leaderMap.keySet();
//			for (String string : keySet2) {
//				JSONObject leaderJObj = JSONObject.parseObject(leaderMap.get(string));
//				leaderNameList.add(leaderJObj.getString("name"));
//			}
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
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK:
			// 巡查上报
			ObjectRestResponse<PatrolTask> patrolTaskResult = patreolTaskService
					.get(Integer.valueOf(caseInfo.getSourceCode()));
			PatrolTask patrolTask = patrolTaskResult.getData();

			resultJObjct = JSONObject.parseObject(JSON.toJSONString(patrolTask));
			resultJObjct.put("eventSourceType", "巡查上报");
			resultJObjct.put("sourceCode", patrolTask.getPatrolCode());

			reportPersonId = patrolTask.getCrtUserId();
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
		Map<String, String> userMap = adminFeign.getUser(reportPersonId);
		Set<String> userMapKeySet = userMap.keySet();
		for (String string : userMapKeySet) {
			JSONObject userJObj = JSONObject.parseObject(userMap.get(string));
			resultJObjct.put("crtUserTel", userJObj.getString("telPhone"));
		}

//		JSONObject sourceTypeJsonStr = JSONObject.parseObject(resultJObjct.toJSONString());
		wfJObject.put("sourceTypeHistory", resultJObjct);
	}

	private String getRootBizTypeName(String ids, Map<String, String> rootBizList) {
		if (StringUtils.isNotBlank(ids)) {
			String[] split = ids.split(",");
			List<String> nameList = new ArrayList<>();
			for (String string : split) {
//				JSONObject obj = JSONObject.parseObject(rootBizList.get(string));
				nameList.add(rootBizList.get(string));
//				nameList.add(obj.getString("labelDefault"));
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
	public void completeProcess(@RequestBody JSONObject objs) {
		// 完成已签收的任务，将工作流向下推进
		wfProcTaskService.completeProcessInstance(objs);

//		System.out.println(1/0);//模拟异常
		/*
		 * ===============更新业务数据===================开始=============
		 */
		JSONObject bizDataJObject = objs.getJSONObject("bizData");

		JSONObject caseInfoJObj = bizDataJObject.getJSONObject("caseInfo");// 立案单参数
		JSONObject variableDataJObject = objs.getJSONObject("variableData");// 流程参数
		JSONObject concernedPersonJObj = bizDataJObject.getJSONObject("concernedPerson");// 当事人(个人信息)
		JSONObject concernedCompanyJObj = bizDataJObject.getJSONObject("concernedCompany");// 当事人(单位)

		CaseInfo caseInfo = new CaseInfo();
		if (caseInfoJObj != null) {
			caseInfo = JSON.parseObject(caseInfoJObj.toJSONString(), CaseInfo.class);
		}
		caseInfo.setId(Integer.valueOf(bizDataJObject.getString("procBizId")));

		// 获取流程走向，以判断流程是否结束
		String flowDirection = variableDataJObject.getString("flowDirection");
		if (Constances.ProcFlowWork.TOFINISHWORKFLOW.equals(flowDirection)) {
			/*
			 * 任务已走向结束<br/> 去执行相应业务应该完成的操作<br/> 1 立案单isFinished：1<br/> 2 来源各变化
			 */
			log.info("该请求流向【结束】，流程即将结束。");
			// 查询数据库中的caseInfo,以确定与caseInfo相对应的登记表那条记录的ID
			CaseInfo caseInfoInDB = caseInfoBiz.selectById(Integer.valueOf(bizDataJObject.getString("procBizId")));

			caseInfo.setSourceCode(caseInfoInDB.getSourceCode());
			caseInfo.setIsFinished("1");
			caseInfo.setFinishTime(new Date());// 结案时间
			gotoFinishSource(caseInfo, false);// 去更新事件来源的状态
		} else if (Constances.ProcFlowWork.TOFINISHWORKFLOW_DUP.equals(flowDirection)) {
			// 因重覆而结束
			log.info("该请求流向【结束】（因事件重复），流程即将结束。");
			// 查询数据库中的caseInfo,以确定与caseInfo相对应的登记表那条记录的ID
			CaseInfo caseInfoInDB = caseInfoBiz.selectById(Integer.valueOf(bizDataJObject.getString("procBizId")));

			caseInfo.setSourceCode(caseInfoInDB.getSourceCode());
			caseInfo.setIsFinished("1");
			caseInfo.setIsDuplicate("1");
//			caseInfo.setDuplicateWith(Integer.valueOf(bizDataJObject.getString("duplicateWith")));
			gotoFinishSource(caseInfo, false);// 去更新事件来源的状态
		}

		// 判断是否有当事人信息concernedPerson
		if (concernedPersonJObj != null) {
			ConcernedPerson concernedPerson = JSON.parseObject(concernedPersonJObj.toJSONString(),
					ConcernedPerson.class);
			concernedPersonBiz.insertSelective(concernedPerson);
			caseInfo.setConcernedPerson(String.valueOf(concernedPerson.getId()));
			//当事人类型为"root_biz_concernedT_person"
			caseInfo.setConcernedType(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON);//
		}

		// 判断是否有当事人(单位)信息
		if (concernedCompanyJObj != null) {
			ConcernedCompany concernedCompany = JSONObject.parseObject(concernedCompanyJObj.toJSONString(),
					ConcernedCompany.class);
			concernedCompanyService.created(concernedCompany);
			caseInfo.setConcernedPerson(String.valueOf(concernedCompany.getId()));
			//当事人类型为"root_biz_concernedT_org"
			caseInfo.setConcernedType(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG);//
		}

		// 判断 是否有处理情况信息
		JSONObject executeInfoJObj = bizDataJObject.getJSONObject("executeInfoJObj");
		if (executeInfoJObj != null) {
			ExecuteInfo executeInfo = JSON.parseObject(executeInfoJObj.toJSONString(), ExecuteInfo.class);
			executeInfo.setCaseId(caseInfo.getId());
			executeInfoBiz.insertSelective(executeInfo);
		}
		// 更新业务数据(caseInfo)
		caseInfoBiz.updateSelectiveById(caseInfo);
	}

	/**
	 * 事件来源走向结束(终止或正常结束)的处理方法
	 * 
	 * @author 尚
	 * @param caseInfo      与事件来源对应的立案单
	 * @param isTermination true:终止操作|false:正常结束操作
	 */
	private void gotoFinishSource(CaseInfo caseInfo, boolean isTermination) {
		// 业务数据库中字典数据用字典表的code去表示，因为caseInfo内保存的就是code，无需再进行id换code操作
		String key = caseInfo.getSourceType();

		JSONObject assist = new JSONObject();
		switch (key) {
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_12345:
			// 市长热线
			log.info("更新市长热线事件为【已处理】");
			if (isTermination) {
				// 事件终止操作
				assist = assist(caseInfo.getSourceCode(), Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_STOP);
			} else {
				assist = assist(caseInfo.getSourceCode(), Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DONE);
			}
			MayorHotline mayorHotline = JSON.parseObject(JSON.toJSONString(assist), MayorHotline.class);
			mayorHotlineBiz.updateSelectiveById(mayorHotline);
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CONSENSUS:
			// 舆情
			log.info("更新舆情事件为【已完成】");
			if (isTermination) {
				// 事件终止操作
				assist = assist(caseInfo.getSourceCode(), Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_STOP);
			} else {
				assist = assist(caseInfo.getSourceCode(), Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_FINISH);
			}
			PublicOpinion publicOpinion = JSON.parseObject(assist.toJSONString(), PublicOpinion.class);
			publicOpinionBiz.updateSelectiveById(publicOpinion);
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_LEADER:
			// 领导交办
			log.info("更新领导交办事件为【已完成】");
			if (isTermination) {
				// 事件终止操作
				assist = assist(caseInfo.getSourceCode(), Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_STOP);
			} else {
				assist = assist(caseInfo.getSourceCode(), Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_FINISH);
			}
			LeadershipAssign leadershipAssign = JSON.parseObject(assist.toJSONString(), LeadershipAssign.class);
			leadershipAssignBiz.updateSelectiveById(leadershipAssign);
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK:
			// 巡查上报
			log.info("更新巡查上报事件为【已完成】");
			if (isTermination) {
				// 事件终止操作
				assist = assist(caseInfo.getSourceCode(), Constances.PartolTaskStatus.ROOT_BIZ_PARTOLTASKT_STOP);
			} else {
				assist = assist(caseInfo.getSourceCode(), Constances.PartolTaskStatus.ROOT_BIZ_PARTOLTASKT_FINISH);
			}
			PatrolTask patrolTask = JSON.parseObject(assist.toJSONString(), PatrolTask.class);
			patrolTaskBiz.updateSelectiveById(patrolTask);
			break;
		default:
			break;
		}
	}

	/**
	 * 根据事件来源ID及处理状态（code表示），返回一个包含ID及处理状态(ID表示)在内的JSONObject
	 * 
	 * @author 尚
	 * @param sourceCode 事件来源ID
	 * @param exeStatus  事件处理状态（code表示）
	 * @return sourceCode="source_abc",code="code_abc"(与之对应的字典中ID为"dict_id_abc"),则返回<br/>
	 *         {"source_abc":"dict_id_abc"}
	 */
	private JSONObject assist(String sourceCode, String exeStatus) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", sourceCode);
		jsonObject.put("exeStatus", exeStatus);

		// 查询已完成状态的ID
//		Map<String, String> dictIds = dictFeign.getDictIdByCod(exeStatus, false);
//		Map<String, String> dictIds = dictFeign.getDictIds(exeStatus);
//		Set<String> keySet = dictIds.keySet();
//		for (String string : keySet) {
		/*
		 * 如果string所对应的记录内code字段值与传入的exeStatus值相同，说明该记录的ID即为与exeStatus状态所对应的记录ID<br/>
		 * 如:dictIds={"abc1":{"id":"abc1","exeStatus":"def1"},"abc2":{"id":"abc2",
		 * "exeStatus":"def2"}},exeStatus="def1"<br/>
		 * 则向jsonObject内put一组数据"exeStatus":"abc1"
		 */
//			String exeStatusInDicts = CommonUtil.getValueFromJObjStr(dictIds.get(string), "code");
//			if (exeStatus.equals(exeStatusInDicts)) {
//				jsonObject.put("exeStatus", string);
//			}
//		}

		return jsonObject;
	}

	/**
	 * 查询详细待办任务
	 * 
	 * @author 尚
	 * @param objs 请求参数 {<br/>
	 *             "bizData":{"procBizId":"1"},<br/>
	 *             "procData":{"procInstId":"240067"},<br/>
	 *             "authData":{"procAuthType":"2"},<br/>
	 *             "variableData":{},<br/>
	 *             "queryData":{}<br/>
	 *             }
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

//		List<String> committerIdList = procHistoryList.stream().map(o -> o.getProcTaskCommitter()).distinct()
//				.collect(Collectors.toList());
		List<String> procTaskAssigneeIdList = procHistoryList.stream().map(o -> o.getProcTaskAssignee()).distinct()
				.collect(Collectors.toList());
		if (procTaskAssigneeIdList != null && !procTaskAssigneeIdList.isEmpty()) {
			Map<String, String> assignMap = adminFeign.getUser(String.join(",", procTaskAssigneeIdList));
			if (assignMap != null && !assignMap.isEmpty()) {
				for (int i = 0; i < procHistoryJArray.size(); i++) {
					JSONObject procHistoryJObj = procHistoryJArray.getJSONObject(i);

					/*
					 * IF ( task.PROC_TASK_STATUS = '1', task.PROC_TASK_GROUP,
					 * task.PROC_TASK_ASSIGNEE ) procTaskAssignee
					 * 以上为查询历史任务详情的SQL，当未签收时，会将procTaskGroup作为签收人查询出来
					 * 但用procTaskGroup去查base_user表时，查不出数据，即assignMap.get(procHistoryJObj.getString(
					 * "procTaskAssignee"))为空 需要进行非空判断
					 */
					JSONObject nameJObj = JSONObject
							.parseObject(assignMap.get(procHistoryJObj.getString("procTaskAssignee")));

					if (nameJObj != null) {
						procHistoryJObj.put("procTaskAssigneeName", nameJObj.getString("name"));
						procHistoryJObj.put("procTaskAssigneeTel", nameJObj.getString("telPhone"));
					}
//					procHistoryJObj.put("procTaskCommitterName",
//							JSONObject.parseObject(assignMap.get(procHistoryJObj.getString("procTaskCommitter")))
//									.getString("name"));
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
		sourceTypeHistoryAssist(resultJObj, caseInfo);// 来源历史查询帮助
		/*
		 * =================查询历史记录======附带来源信息=====结束==========
		 */
		// 通过root_biz进行模糊查询业务字典，这样查询数据量会稍大，但可以减少请求次数
		Map<String, String> manyDictValuesMap = dictFeign.getByCode("root_biz");

		List<String> adminIdList = new ArrayList<>();
		if (caseInfo.getCheckPerson() != null) {
			adminIdList.add(caseInfo.getCheckPerson());
		}
		for (WfProcTaskHistoryBean wfProcTaskHistoryBean : procHistoryList) {
			if (StringUtils.isNotBlank(wfProcTaskHistoryBean.getProcTaskCommitter())) {
				adminIdList.add(wfProcTaskHistoryBean.getProcTaskCommitter());
			}
		}
		if (StringUtils.isNotBlank(caseInfo.getFinishCheckPerson())) {
			adminIdList.add(caseInfo.getFinishCheckPerson());
		}
		if (StringUtils.isNotBlank(caseInfo.getFinishPerson())) {
			adminIdList.add(caseInfo.getFinishPerson());
		}

		// 将多次向adminFeign的请求集中到这里进行查询，在经之上的代码即对需要进行查询 ID的收集
		Map<String, String> manyUsersMap = adminFeign.getUser(String.join(",", adminIdList));

		/*
		 * =================查询基础信息===========开始==========
		 */
		JSONObject baseInfoJObj = new JSONObject();
		baseInfoJObj.put("caseCode", caseInfo.getCaseCode());
		baseInfoJObj.put("caesTitle", caseInfo.getCaseTitle());
		baseInfoJObj.put("caseLevel", caseInfo.getCaseLevel());
		if (caseInfo.getCaseLevel() != null) {
//			Map<String, String> dictValueMap = dictFeign.getDictValueByID(caseInfo.getCaseLevel());//>>>>>>>>>>>>>>>查询了字典>>>>>>>>>>>>>>>>>>>>>>>>>
			if (manyDictValuesMap != null && !manyDictValuesMap.isEmpty()) {
				baseInfoJObj.put("caseLevelName", manyDictValuesMap.get(caseInfo.getCaseLevel()));
			}
		}
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
			String concernedTypeId = "";

			// 从刚开始查出的许多业务字典Map集合中遍历当事人类别
			Set<String> concernedKeySet = manyDictValuesMap.keySet();
			for (String string : concernedKeySet) {
				// 找出字典中的code与常量类中的常量code对比
				String concernedCode = CommonUtil.getValueFromJObjStr(manyDictValuesMap.get(string), "code");
				if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG.equals(concernedCode)
						|| Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON.equals(concernedCode)) {
					// 当发现有匹配的code时，将其取出，并跳出
					concernedTypeId = string;
					break;
				}
			}
			if (concernedTypeId.equals(caseInfo.getConcernedType())) {
				concernedPerson = concernedPersonBiz.selectById(Integer.valueOf(caseInfo.getConcernedPerson()));
				if (concernedPerson != null) {
					concernedPersonJObj = JSONObject.parseObject(JSON.toJSONString(concernedPerson));
					// 证件类型
//			Map<String, String> credTypeMap = dictFeign.getDictValueByID(concernedPerson.getCredType());//>>>>>>>>>>>>>>>查询了字典>>>>>>>>>>>>>>>>>>>>>>>>>
					if (manyDictValuesMap != null && !manyDictValuesMap.isEmpty()) {
						if (StringUtils.isNotBlank(concernedPerson.getCredType())) {
							concernedPersonJObj.put("credTypeName",
									JSONObject.parseObject(manyDictValuesMap.get(concernedPerson.getCredType()))
											.getString("labelDefault"));
						}
					}
				}
			} else if (concernedTypeId.equals(caseInfo.getConcernedType())) {
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
//			Map<String, String> bizListMap = dictFeign.getDictValueByID(caseInfo.getBizList());//>>>>>>>>>>>>>>>查询了字典>>>>>>>>>>>>>>>>>>>>>>>>>
			if (manyDictValuesMap != null && !manyDictValuesMap.isEmpty()) {
				eventTypeJObj.put("bizListName",
						JSONObject.parseObject(manyDictValuesMap.get(caseInfo.getBizList())).getString("labelDefault"));
			}
		}
		// 事件类别（单选）
		eventTypeJObj.put("eventTypeList", caseInfo.getEventTypeList());
		EventType eventType = new EventType();
		if (StringUtils.isNotBlank(caseInfo.getEventTypeList())) {
			eventType = eventTypeBiz.selectById(Integer.valueOf(caseInfo.getEventTypeList()));
			if (eventType != null) {
				eventTypeJObj.put("eventTypeListName", eventType.getTypeName());
			}
		}
		// 监管对象,为多选
		List<String> regulaObjectNameList = new ArrayList<>();
		if (StringUtils.isNotBlank(caseInfo.getRegulaObjList())) {
			List<RegulaObject> regulaObjList = regulaObjectMapper.selectByIds(caseInfo.getRegulaObjList());
			for (RegulaObject regulaObject : regulaObjList) {
				regulaObjectNameList.add(regulaObject.getObjName());
			}
		}
//		RegulaObject regulaObject = regulaObjectBiz.selectById(Integer.valueOf(caseInfo.getRegulaObjList()));
		eventTypeJObj.put("regulaObjList", caseInfo.getRegulaObjList());
		eventTypeJObj.put("regulaObjListName", String.join(",", regulaObjectNameList));
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
			mapInfoJObj.put("gridName", grid.getGridName());
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
//			Map<String, String> checkPerson = adminFeign.getUser(caseInfo.getCheckPerson());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
			if (manyUsersMap != null && !manyUsersMap.isEmpty()) {
				JSONObject checkPersonJObj = JSONObject.parseObject(manyUsersMap.get(caseInfo.getCheckPerson()));
				checkJObj.put("checkPersonName", checkPersonJObj.getString("name"));
				checkJObj.put("checkPersonTel", checkPersonJObj.getString("telPhone"));
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
		requiredJObj.put("deadLine", caseInfo.getDeadLine());
		String executedDeptName = "";
		requiredJObj.put("executedDept", caseInfo.getExecuteDept());
		if (caseInfo.getExecuteDept() != null) {
			Map<String, String> executeDeptMap = adminFeign.getDepart(caseInfo.getExecuteDept());// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
			if (executeDeptMap != null && !executeDeptMap.isEmpty()) {
				executedDeptName = CommonUtil.getValueFromJObjStr(executeDeptMap.get(caseInfo.getExecuteDept()),
						"name");
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
			if (Constances.ProcCTaskCode.COMMANDERAPPROVE.equals(wfProcTaskHistoryBean.getProcCtaskcode())) {
				JSONObject commanderApproveJObj = JSONObject.parseObject(JSON.toJSONString(wfProcTaskHistoryBean));
				// 查询指挥长
				if (StringUtils.isNotBlank(wfProcTaskHistoryBean.getProcTaskCommitter())) {
//				Map<String, String> commanderApproveMap = adminFeign.getUser(wfProcTaskHistoryBean.getProcTaskCommitter());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
					if (manyUsersMap != null && !manyUsersMap.isEmpty()) {
						JSONObject jObjTmp = JSONObject
								.parseObject(manyUsersMap.get(wfProcTaskHistoryBean.getProcTaskCommitter()));
						commanderApproveJObj.put("procTaskCommitter", wfProcTaskHistoryBean.getProcTaskCommitter());// 审批人ID
						commanderApproveJObj.put("procTaskCommitterName", jObjTmp.getString("name"));// 审批人姓名
						commanderApproveJObj.put("commanderTel", jObjTmp.getString("telPhone"));// 审批人联系方法
						commanderApproveJObj.put("procTaskCommittime", wfProcTaskHistoryBean.getProcTaskCommittime());// 审批时间
						commanderApproveJObj.put("procTaskApprOpinion", wfProcTaskHistoryBean.getProcTaskApprOpinion());// 审批意见
						commanderApproveJArray.add(commanderApproveJObj);
					}
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
//			List<String> exePersonIdList = new ArrayList<>();
//			for (ExecuteInfo executeInfo : executeInfoList) {
//				if (StringUtils.isNotBlank(executeInfo.getExePerson())) {
//					exePersonIdList.add(executeInfo.getExePerson());
//				}
//			}

//			Map<String, String> exePersonMap = null;
//			if (exePersonIdList != null && !executeInfoList.isEmpty()) {
//				exePersonMap = adminFeign.getUser(String.join(",", exePersonIdList));// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
//			}

			for (ExecuteInfo executeInfo : executeInfoList) {
				JSONObject executeInfoJObj = new JSONObject();
				executeInfoJObj = JSONObject.parseObject(JSON.toJSONString(executeInfo));
				if (executeInfo != null) {
//					if (exePersonMap != null && !exePersonMap.isEmpty()) {
//						JSONObject exeInfoTmp = JSONObject.parseObject(exePersonMap.get(executeInfo.getExePerson()));
//						if (exeInfoTmp != null) {
//						}
//					}
					executeInfoJObj.put("exePersonName", executeInfo.getExePerson());// 办理人
//					executeInfoJObj.put("exePsersonTel", executeInfo.get);// 办理人联系方式
					executeInfoJObj.put("finishTime", executeInfo.getFinishTime());// 办结时间
					executeInfoJObj.put("exeDesc", executeInfo.getExeDesc());// 情况说明
					executeInfoJObj.put("picture", executeInfo.getPicture());

					JSONObject sourceType = resultJObj.getJSONObject("sourceTypeHistory");
					executeInfoJObj.put("recordPserson", sourceType.getString("crtUserId"));// 登记人ID
					executeInfoJObj.put("recordPsersonName", sourceType.getString("crtUserName"));// 登记人
					// 登记人电话
					String recordPersonTel = "";
					if (StringUtils.isNotBlank(sourceType.getString("crtUserId"))) {
						Map<String, String> recordUser = adminFeign.getUser(sourceType.getString("crtUserId"));// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
						recordPersonTel = CommonUtil
								.getValueFromJObjStr(recordUser.get(sourceType.getString("crtUserId")), "telPhone");
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
		finishCheckJObj.put("finishDesc", caseInfo.getFinishDesc());
		finishCheckJObj.put("finishCheckTime", caseInfo.getFinishCheckTime());
		finishCheckJObj.put("finishCheckPic", caseInfo.getFinishCheckPic());
		if (StringUtils.isNotBlank(caseInfo.getFinishCheckPerson())) {
//			Map<String, String> finishCheckPersonMap = adminFeign.getUser(caseInfo.getFinishCheckPerson());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
			if (manyUsersMap != null && !manyUsersMap.isEmpty()) {
				finishCheckJObj.put("finishCheckPerson", caseInfo.getFinishCheckPerson());

				JSONObject finishCheckPersonJObj = JSONObject
						.parseObject(manyUsersMap.get(caseInfo.getFinishCheckPerson()));
				finishCheckJObj.put("finishCheckPersonName", finishCheckPersonJObj.getString("name"));
				finishCheckJObj.put("finishCheckPersonTel", finishCheckPersonJObj.getString("telPhone"));
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
//			Map<String, String> finishPersonMap = adminFeign.getUser(caseInfo.getFinishPerson());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
			if (manyUsersMap != null && !manyUsersMap.isEmpty()) {
				finishJObj.put("finishPerson", caseInfo.getFinishPerson());

				JSONObject finishPersonJObj = JSONObject.parseObject(manyUsersMap.get(caseInfo.getFinishPerson()));
				finishJObj.put("finishPersonName", finishPersonJObj.getString("name"));
				finishJObj.put("finishPersonTel", finishPersonJObj.getString("telPhone"));
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

		return new ObjectRestResponse<JSONObject>().data(resultJObj);
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
		caseInfo.setIsFinished("1");
		caseInfo.setFinishTime(new Date());// 结案时间

		gotoFinishSource(caseInfo, true);// 去更新事件来源的状态

		// 更新业务数据(caseInfo)
		caseInfoBiz.updateSelectiveById(caseInfo);
	}
}