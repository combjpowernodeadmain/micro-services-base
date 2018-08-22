package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.ConcernedPersonBiz;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.ExecuteInfoBiz;
import com.bjzhianjia.scp.cgp.biz.LeadershipAssignBiz;
import com.bjzhianjia.scp.cgp.biz.MayorHotlineBiz;
import com.bjzhianjia.scp.cgp.biz.PublicOpinionBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.ExecuteInfo;
import com.bjzhianjia.scp.cgp.entity.LeadershipAssign;
import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import com.bjzhianjia.scp.cgp.entity.PublicOpinion;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.exception.BizException;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectMapper;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.service.impl.WfMonitorServiceImpl;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskHistoryBean;
import com.bjzhianjia.scp.security.wf.base.task.service.impl.WfProcTaskServiceImpl;
import com.github.pagehelper.PageInfo;

import tk.mybatis.mapper.entity.Example;

/**
 * 立案管理
 * 
 * @author 尚
 */
@Service
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
	public TableResultResponse<CaseInfo> getList(CaseInfo caseInfo, int page, int limit) {
		return caseInfoBiz.getList(caseInfo, page, limit);
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
	 * @param objs
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

		if (pageInfo != null) {
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

		List<String> rootBizIdList = new ArrayList<>();

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
		int page = StringUtils.isBlank(queryData.getString("page")) ? 1 : Integer.valueOf(queryData.getString("page"));
		int limit = StringUtils.isBlank(queryData.getString("limit")) ? 10
				: Integer.valueOf(queryData.getString("limit"));
		TableResultResponse<CaseInfo> tableResult = caseInfoBiz.getList(queryCaseInfo, bizIdStrList, page, limit,
				queryData.getString("startQueryTime"), queryData.getString("endQueryTime"));

		List<CaseInfo> caseInfoList = tableResult.getData().getRows();

		Map<Integer, CaseInfo> caseInfo_ID_Entity_Map = new HashMap<>();
		for (CaseInfo caseInfo : caseInfoList) {
			caseInfo_ID_Entity_Map.put(caseInfo.getId(), caseInfo);

			rootBizIdList.add(caseInfo.getBizList());
			rootBizIdList.add(caseInfo.getSourceType());
			rootBizIdList.add(caseInfo.getCaseLevel());

			eventTypeIdStrList.add(caseInfo.getEventTypeList());
		}

		/*
		 * 查询业务条线，查询事件来源，查询事件级别
		 */
		Map<String, String> rootBizList = new HashMap<>();
		if (rootBizIdList != null && !rootBizIdList.isEmpty()) {
			rootBizList = dictFeign.getDictValueByID(String.join(",", rootBizIdList));
		}

		// 查询事件类别
		Map<String, String> eventTypeMap = new HashMap<>();
		if (eventTypeIdStrList != null && !eventTypeIdStrList.isEmpty()) {
			List<EventType> eventTypeList = new ArrayList<>();
			eventTypeList = eventTypeMapper.selectByIds(String.join(",", eventTypeIdStrList));
			eventTypeMap = new HashMap<String, String>();
			for (EventType eventType : eventTypeList) {
				eventTypeMap.put(String.valueOf(eventType.getId()), eventType.getTypeName());
			}
		}

//		List<String> pricInstIdList = procBackBeanList.stream().map(o->o.getProcInstId()).distinct().collect(Collectors.toList());

		for (WfProcBackBean tmp : procBackBeanList) {
			JSONObject wfJObject = JSONObject.parseObject(JSON.toJSONString(tmp));

			CaseInfo caseInfo = caseInfo_ID_Entity_Map.get(Integer.valueOf(tmp.getProcBizid()));
			if (caseInfo != null) {
				caseInfo.setId(null);// 防止caseInfo的ID覆盖工作流任务的ID
				JSONObject parse = JSONObject.parseObject(JSON.toJSONString(caseInfo));
				wfJObject.putAll(parse);

//				wfJObject.put("caseCode", caseInfo.getCaseCode());
//				wfJObject.put("caseTitle", caseInfo.getCaseTitle());
//				wfJObject.put("occurTime", caseInfo.getOccurTime());
//				wfJObject.put("bizList", caseInfo.getBizList());
				wfJObject.put("bizListName", getRootBizTypeName(caseInfo.getBizList(), rootBizList));
//				wfJObject.put("eventTypeList", caseInfo.getEventTypeList());
				wfJObject.put("eventTypeListName", getRootBizTypeName(caseInfo.getEventTypeList(), eventTypeMap));
//				wfJObject.put("sourceType", caseInfo.getSourceType());
				wfJObject.put("sourceTypeName", getRootBizTypeName(caseInfo.getSourceType(), rootBizList));
//				wfJObject.put("caseLevel", caseInfo.getCaseLevel());
				wfJObject.put("caseLevelName", getRootBizTypeName(caseInfo.getCaseLevel(), rootBizList));
				jObjList.add(wfJObject);
			}
		}

		return new TableResultResponse<>(tableResult.getData().getTotal(), jObjList);
	}

	private void procHistoryAssist(JSONObject wfJObject, CaseInfo caseInfo) {

		// 查询登记历史
		String sourceType = caseInfo.getSourceType();
		Map<String, String> sourceTypeIdMap = dictFeign.getDictValueByID(sourceType);

		String sourceTypeJsonStr_Dict = "";
		Set<String> keySet = sourceTypeIdMap.keySet();
		for (String string : keySet) {
			sourceTypeJsonStr_Dict = sourceTypeIdMap.get(string);
		}

		JSONObject sourceTypeJObj = JSONObject.parseObject(sourceTypeJsonStr_Dict);
		String key = sourceTypeJObj.getString("code");

		JSONObject resultJObjct = new JSONObject();
		List<String> zhaiyaoList=new ArrayList<>();
//		StringBuffer buffer = new StringBuffer();
		String reportPersonId = "";
		switch (key) {
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_12345:
			// 市长热线
			MayorHotline mayorHotline = mayorHotlineBiz.selectById(Integer.valueOf(caseInfo.getSourceCode()));
			reportPersonId = mayorHotline.getCrtUserId();
			resultJObjct = JSONObject.parseObject(JSON.toJSONString(mayorHotline));
			resultJObjct.put("eventSourceType", "市长热线12345");

			zhaiyaoList.add(mayorHotline.getHotlnType());
			zhaiyaoList.add(mayorHotline.getHotlnSubType());
			zhaiyaoList.add(mayorHotline.getAppealTel());
			if(mayorHotline.getReplyDatetime()!=null) {
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

			Map<String, String> dictValueMap = dictFeign.getDictValueByID(String.join(",", dictIdList));
			if (dictValueMap != null && !dictValueMap.isEmpty()) {
				zhaiyaoList.add(dictValueMap.get(poinion.getOpinType()));
				zhaiyaoList.add(dictValueMap.get(dictValueMap.get(poinion.getOpinLevel())));
				zhaiyaoList.add(dictValueMap.get(poinion.getOpinPort()));
			}
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_LEADER:
			// 领导交办
			LeadershipAssign leadershipAssign = leadershipAssignBiz
					.selectById(Integer.valueOf(caseInfo.getSourceCode()));
			resultJObjct=JSONObject.parseObject(JSON.toJSONString(leadershipAssign));
			resultJObjct.put("eventSourceType", "领导交办");
			reportPersonId = leadershipAssign.getCrtUserId();

			// 查询数据库中交办领导,领导可能有多人
			List<String> leaderNameList = new ArrayList<>();
			Map<String, String> leaderMap = adminFeign.getUser(leadershipAssign.getTaskLeader());
			Set<String> keySet2 = leaderMap.keySet();
			for (String string : keySet2) {
				JSONObject leaderJObj = JSONObject.parseObject(leaderMap.get(string));
				leaderNameList.add(leaderJObj.getString("name"));
			}
			//涉及监管对象名称
			String regulaObjList = leadershipAssign.getRegulaObjList();
			List<RegulaObject> regulaObjectList = regulaObjectMapper.selectByIds(regulaObjList);
			List<String> regulaObjNameList=new ArrayList<>();
			if(regulaObjectList!=null&&!regulaObjectList.isEmpty()) {
				for(RegulaObject regulaObject:regulaObjectList) {
					regulaObjNameList.add(regulaObject.getObjName());
				}
			}
			
			zhaiyaoList.add(String.join(",", leaderNameList));
			zhaiyaoList.add(String.join(",", regulaObjNameList));
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK:
			// 巡查上报
			break;
		default:
			break;
		}

		for(int i=0;i<zhaiyaoList.size();i++) {
			String zhy = zhaiyaoList.get(i);
			if(StringUtils.isBlank(zhy)) {
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
				JSONObject obj = JSONObject.parseObject(rootBizList.get(string));
				nameList.add(obj.getString("labelDefault"));
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
		wfProcTaskService.startAndCompleteProcessInstance(objs);

		/*
		 * ===============更新业务数据===================开始=============
		 */
		JSONObject bizDataJObject = objs.getJSONObject("bizData");
		CaseInfo caseInfo = JSON.parseObject(bizDataJObject.toJSONString(), CaseInfo.class);

		// 获取流程走向，以判断流程是否结束
		JSONObject variableDataJObject = objs.getJSONObject("variableData");
		String flowDirection = variableDataJObject.getString("flowDirection");
		if (Constances.ProcFlowWork.TOFINISHWORKFLOW.equals(flowDirection)) {
			/*
			 * 任务已走向结束<br/> 去执行相应业务应该完成的操作<br/> 1 立案单isFinished：1<br/> 2 来源各变化
			 */
			caseInfo.setIsFinished("1");
			gotoFinishSource(caseInfo);// 去更新事件来源的状态
		}
		
		//判断是否有当事人信息concernedPerson
		JSONObject concernedPersonJObj = bizDataJObject.getJSONObject("concernedPerson");
		if(concernedPersonJObj!=null) {
			ConcernedPerson concernedPerson = JSON.parseObject(concernedPersonJObj.toJSONString(), ConcernedPerson.class);
			concernedPersonBiz.insertSelective(concernedPerson);
			caseInfo.setConcernedPerson(String.valueOf(concernedPerson.getId()));
		}
		
		//判断 是否有处理情况信息
		JSONObject executeInfoJObj = bizDataJObject.getJSONObject("executeInfoJObj");
		if(executeInfoJObj!=null) {
			ExecuteInfo executeInfo=JSON.parseObject(executeInfoJObj.toJSONString(), ExecuteInfo.class);
			executeInfo.setCaseId(caseInfo.getId());
			executeInfoBiz.insertSelective(executeInfo);
		}
		// 更新业务数据(caseInfo)
		caseInfoBiz.updateSelectiveById(caseInfo);
	}

	private void gotoFinishSource(CaseInfo caseInfo) {
		String sourceType = caseInfo.getSourceType();
		Map<String, String> sourceTypeIdMap = dictFeign.getDictValueByID(sourceType);

		String sourceTypeJsonStr = "";
		Set<String> keySet = sourceTypeIdMap.keySet();
		for (String string : keySet) {
			sourceTypeJsonStr = sourceTypeIdMap.get(string);
		}

		JSONObject sourceTypeJObj = JSONObject.parseObject(sourceTypeJsonStr);
		String key = sourceTypeJObj.getString("code");

		JSONObject assist = new JSONObject();
		switch (key) {
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_12345:
			// 市长热线
			assist = assist(caseInfo.getSourceCode(), Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DONE);
			MayorHotline mayorHotline = JSON.parseObject(JSON.toJSONString(assist), MayorHotline.class);
			mayorHotlineBiz.updateSelectiveById(mayorHotline);
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CONSENSUS:
			// 舆情
			assist = assist(caseInfo.getSourceCode(), Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_FINISH);
			PublicOpinion publicOpinion = JSON.parseObject(assist.toJSONString(), PublicOpinion.class);
			publicOpinionBiz.updateSelectiveById(publicOpinion);
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_LEADER:
			// 领导交办
			assist = assist(caseInfo.getSourceCode(), Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_FINISH);
			LeadershipAssign leadershipAssign = JSON.parseObject(assist.toJSONString(), LeadershipAssign.class);
			leadershipAssignBiz.updateSelectiveById(leadershipAssign);
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK:
			// 巡查上报
			break;
		default:
			break;
		}
	}

	private JSONObject assist(String sourceCode, String done) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("id", sourceCode);

		// 查询已完成状态的ID
		Map<String, String> dictIds = dictFeign.getDictIds(done);
		Set<String> keySet = dictIds.keySet();
		for (String string : keySet) {
			if (done.equals(dictIds.get(string))) {
				jsonObject.put("exeStatus", string);
			}
		}

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
		resultJObj.put("procHistory", procHistoryList);

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
		procHistoryAssist(resultJObj, caseInfo);
		/*
		 * =================查询历史记录======附带来源信息=====结束==========
		 */
		
		Map<String, String> manyDictValuesMap = dictFeign.getDictIdByCode("root_biz", true);
		
		List<String> adminIdList=new ArrayList<>();
		if(caseInfo.getCheckPerson()!=null) {
			adminIdList.add(caseInfo.getCheckPerson());
		}
		for (WfProcTaskHistoryBean wfProcTaskHistoryBean : procHistoryList) {
			if(StringUtils.isNotBlank(wfProcTaskHistoryBean.getProcTaskCommitter())) {
				adminIdList.add(wfProcTaskHistoryBean.getProcTaskCommitter());
			}
		}
		if(StringUtils.isNotBlank(caseInfo.getFinishCheckPerson())) {
			adminIdList.add(caseInfo.getFinishCheckPerson());
		}
		if(StringUtils.isNotBlank(caseInfo.getFinishPerson())) {
			adminIdList.add(caseInfo.getFinishPerson());
		}
		
		Map<String, String> manyUsersMap = adminFeign.getUser(String.join(",", adminIdList));
		
		/*
		 * =================查询基础信息===========开始==========
		 */
		JSONObject baseInfoJObj=new JSONObject();
		baseInfoJObj.put("caseCode", caseInfo.getCaseCode());
		baseInfoJObj.put("caesTitle", caseInfo.getCaseTitle());
		baseInfoJObj.put("caseLevel", caseInfo.getCaseLevel());
		if(caseInfo.getCaseLevel()!=null) {
//			Map<String, String> dictValueMap = dictFeign.getDictValueByID(caseInfo.getCaseLevel());//>>>>>>>>>>>>>>>查询了字典>>>>>>>>>>>>>>>>>>>>>>>>>
			if(manyDictValuesMap!=null&&!manyDictValuesMap.isEmpty()){
				baseInfoJObj.put("caseLevelName", JSONObject.parseObject(manyDictValuesMap.get(caseInfo.getCaseLevel())).getString("labelDefault"));
			}
		}
		baseInfoJObj.put("caseDesc", caseInfo.getCaseDesc());
		
		/*
		 * =================查询基础信息===========结束==========
		 */
		
		/*
		 * =================查询当事人信息(个人)===========开始==========
		 */
		ConcernedPerson concernedPerson = concernedPersonBiz.selectById(caseInfo.getConcernedPerson());
		JSONObject concernedPersonJObj =new JSONObject();
		if(concernedPerson!=null) {
			concernedPersonJObj= JSONObject.parseObject(JSON.toJSONString(concernedPerson));
			//证件类型
//			Map<String, String> credTypeMap = dictFeign.getDictValueByID(concernedPerson.getCredType());//>>>>>>>>>>>>>>>查询了字典>>>>>>>>>>>>>>>>>>>>>>>>>
			if(manyDictValuesMap!=null&&!manyDictValuesMap.isEmpty()) {
				concernedPersonJObj.put("credTypeName", JSONObject.parseObject(manyDictValuesMap.get(concernedPerson.getCredType())).getString("labelDefault"));
			}
		}
		
		/*
		 * =================查询当事人信息(个人)===========结束 ==========
		 */
		
		
		/*
		 * =================查询当事人信息(单位)===========开始==========
		 */
		
		/*
		 * =================查询当事人信息(单位)===========结束==========
		 */
		
		/*
		 * =================事件分类===========开始==========
		 */
		JSONObject eventTypeJObj=new JSONObject();
		//业务条线
		eventTypeJObj.put("bizList", caseInfo.getBizList());
		if(caseInfo.getBizList()!=null) {
//			Map<String, String> bizListMap = dictFeign.getDictValueByID(caseInfo.getBizList());//>>>>>>>>>>>>>>>查询了字典>>>>>>>>>>>>>>>>>>>>>>>>>
			if(manyDictValuesMap!=null&&!manyDictValuesMap.isEmpty()) {
				eventTypeJObj.put("bizListName", JSONObject.parseObject(manyDictValuesMap.get(caseInfo.getBizList())).getString("labelDefault"));
			}
		}
		//事件类别
		eventTypeJObj.put("eventTypeList", caseInfo.getEventTypeList());
		EventType eventType = eventTypeBiz.selectById(caseInfo.getEventTypeList());
		if(eventType!=null) {
			eventTypeJObj.put("eventTypeListName", eventType.getTypeName());
		}
		//监管对象
		RegulaObject regulaObject = regulaObjectBiz.selectById(caseInfo.getRegulaObjList());
		eventTypeJObj.put("regulaObjList", caseInfo.getRegulaObjList());
		if(regulaObject!=null) {
			eventTypeJObj.put("regulaObjListName", regulaObject.getObjName());
		}
		/*
		 * =================事件分类===========结束==========
		 */
		
		/*
		 * =================地理信息===========开始==========
		 */
		JSONObject mapInfoJObj=new JSONObject();
		//所属网格
		mapInfoJObj.put("grid", caseInfo.getGrid());
		AreaGrid grid = areaGridBiz.selectById(caseInfo.getGrid());
		if(grid!=null) {
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
		JSONObject checkJObj=new JSONObject();
		String checkIsExist="";
		if(StringUtils.isNotBlank(caseInfo.getCheckIsExist())) {
			checkIsExist="0".equals(caseInfo.getCheckIsExist())?"不存在":"存在";
		}
		checkJObj.put("checkIsExist", checkIsExist);
		checkJObj.put("checkOpinion", caseInfo.getCheckOpinion());
		if(caseInfo.getCheckPerson()!=null) {
			checkJObj.put("checkPerson", caseInfo.getCheckPerson());
//			Map<String, String> checkPerson = adminFeign.getUser(caseInfo.getCheckPerson());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
			if(manyUsersMap!=null&&!manyUsersMap.isEmpty()) {
				JSONObject checkPersonJObj=JSONObject.parseObject(manyUsersMap.get(caseInfo.getCheckPerson()));
				checkJObj.put("checkPersonName", checkPersonJObj.getString("name"));
				checkJObj.put("checkPersonTel", checkPersonJObj.getString("telPhone"));
			}
		}
		if(caseInfo.getCheckTime()!=null) {
			checkJObj.put("checkTime", DateUtil.dateFromDateToStr(caseInfo.getCheckTime(), "yyyy-MM-dd HH:mm:ss"));
		}
		checkJObj.put("checkPic", caseInfo.getCheckPic());
		/*
		 * =================立案检查情况===========结束==========
		 */
		
		/*
		 * =================事项要求===========开始=========
		 */
		JSONObject requiredJObj=new JSONObject();
		requiredJObj.put("deadLine", caseInfo.getDeadLine());
		if(caseInfo.getExecuteDept()!=null) {
			Map<String, String> executeDeptMap = adminFeign.getDepart(caseInfo.getExecuteDept());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
			requiredJObj.put("executedDept", caseInfo.getExecuteDept());
			if(executeDeptMap!=null&&executeDeptMap.isEmpty()) {
				requiredJObj.put("executedDeptName", executeDeptMap.get(caseInfo.getExecuteDept()));
			}
		}
		requiredJObj.put("requirements", caseInfo.getRequirements());
		/*
		 * =================事项要求===========结束==========
		 */
		
		/*
		 * =================指挥长审批===========开始==========
		 */
		JSONArray commanderApproveJArray=new JSONArray();
		for (WfProcTaskHistoryBean wfProcTaskHistoryBean : procHistoryList) {
			if(Constances.ProcCTaskCode.COMMANDERAPPROVE.equals(wfProcTaskHistoryBean.getProcCtaskcode())) {
				JSONObject commanderApproveJObj=JSONObject.parseObject(JSON.toJSONString(wfProcTaskHistoryBean));
				//查询指挥长
				if(StringUtils.isNotBlank(wfProcTaskHistoryBean.getProcTaskCommitter())) {
//				Map<String, String> commanderApproveMap = adminFeign.getUser(wfProcTaskHistoryBean.getProcTaskCommitter());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
					if(manyUsersMap!=null&&!manyUsersMap.isEmpty()) {
						JSONObject jObjTmp = JSONObject.parseObject(manyUsersMap.get(wfProcTaskHistoryBean.getProcTaskCommitter()));
						commanderApproveJObj.put("procTaskCommitter", wfProcTaskHistoryBean.getProcTaskCommitter());//审批人ID
						commanderApproveJObj.put("procTaskCommitterName", jObjTmp.getString("name"));//审批人姓名
						commanderApproveJObj.put("commanderTel", jObjTmp.getString("telPhone"));//审批人联系方法
						commanderApproveJObj.put("procTaskCommittime", wfProcTaskHistoryBean.getProcTaskCommittime());//审批时间
						commanderApproveJObj.put("procTaskApprOpinion", wfProcTaskHistoryBean.getProcTaskApprOpinion());//审批意见
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
		JSONArray executeInfoJArray=new JSONArray();
		List<ExecuteInfo> executeInfoList = executeInfoBiz.getListByCaseInfoId(caseInfo.getId());
		if(executeInfoList!=null&&!executeInfoList.isEmpty()) {
			for (ExecuteInfo executeInfo : executeInfoList) {
				JSONObject executeInfoJObj=new JSONObject();
				executeInfoJObj=JSONObject.parseObject(JSON.toJSONString(executeInfo));
				if(executeInfo!=null) {
					Map<String, String> exePersonMap = adminFeign.getUser(executeInfo.getExePerson());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
					if(exePersonMap!=null&&!exePersonMap.isEmpty()) {
						JSONObject exeInfoTmp = JSONObject.parseObject(exePersonMap.get(executeInfo.getExePerson()));
						executeInfoJObj.put("exePersonName", exeInfoTmp.getString("name"));//办理人
						executeInfoJObj.put("exePsersonTel",exeInfoTmp.getString("telPhone"));//办理人联系方式
						executeInfoJObj.put("finishTime", executeInfo.getFinishTime());//办结时间
						executeInfoJObj.put("exeDesc", executeInfo.getExeDesc());//情况说明
						executeInfoJObj.put("picture", executeInfo.getPicture());
					}
					JSONObject sourceType = resultJObj.getJSONObject("sourceTypeHistory");
					executeInfoJObj.put("recordPserson", sourceType.getString("crtUserId"));//登记人ID
					executeInfoJObj.put("recordPsersonName", sourceType.getString("crtUserName"));//登记人
					executeInfoJObj.put("recordTime",sourceType.getDate("crtTime"));//登记时间
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
		JSONObject finishCheckJObj=new JSONObject();
		finishCheckJObj.put("finishCheckIsExist", caseInfo.getFinishCheckIsExist());
		String finishCheckIsExistName="";
		if(StringUtils.isNotBlank(caseInfo.getFinishCheckIsExist())) {
			finishCheckIsExistName="0".equals(caseInfo.getFinishCheckIsExist())?"不存在":"存在";
		}
		finishCheckJObj.put("finishCheckIsExistName", finishCheckIsExistName);
		finishCheckJObj.put("finishDesc", caseInfo.getFinishDesc());
		finishCheckJObj.put("finishCheckTime", caseInfo.getFinishCheckTime());
		finishCheckJObj.put("finishCheckPic", caseInfo.getFinishCheckPic());
		if(StringUtils.isNotBlank(caseInfo.getFinishCheckPerson())) {
//			Map<String, String> finishCheckPersonMap = adminFeign.getUser(caseInfo.getFinishCheckPerson());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
			if(manyUsersMap!=null&&!manyUsersMap.isEmpty()) {
				finishCheckJObj.put("finishCheckPerson", caseInfo.getFinishCheckPerson());
				
				JSONObject finishCheckPersonJObj = JSONObject.parseObject(manyUsersMap.get(caseInfo.getFinishCheckPerson()));
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
		JSONObject finishJObj=new JSONObject();
		finishJObj.put("finishDesc", caseInfo.getFinishDesc());
		finishJObj.put("finishTime", caseInfo.getFinishTime());
		if(StringUtils.isNotBlank(caseInfo.getFinishPerson())) {
//			Map<String, String> finishPersonMap = adminFeign.getUser(caseInfo.getFinishPerson());//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>查询了admin》》》》》》》》》》》》》》》》》》》》》》》》》
			if(manyUsersMap!=null&&!manyUsersMap.isEmpty()) {
				finishJObj.put("finishPerson", caseInfo.getFinishPerson());
				
				JSONObject finishPersonJObj = JSONObject.parseObject(manyUsersMap.get(caseInfo.getFinishPerson()));
				finishJObj.put("finishPersonName", finishPersonJObj.getString("name"));
				finishJObj.put("finishPersonTel", finishPersonJObj.getString("telPhone"));
			}
		}
		
		/*
		 * =================结案说明==========结束==========
		 */
		
		resultJObj.put("baseInfo", baseInfoJObj);//基本信息
		resultJObj.put("concernedPerson", concernedPersonJObj);//当事人
		resultJObj.put("eventTypeJObj", eventTypeJObj);//事件分类
		resultJObj.put("mapInfoJObj", mapInfoJObj);//地理信息
		resultJObj.put("checkJObj", checkJObj);//立案检查情况
		resultJObj.put("requiredJObj", requiredJObj);//事项要求
		resultJObj.put("commanderApproveJArray", commanderApproveJArray);//指挥长审批
		resultJObj.put("executeInfoJObj", executeInfoList);//事件处理情况
		resultJObj.put("finishCheckJObj", finishCheckJObj);//结束检查
		resultJObj.put("finishJObj", finishJObj);//结束检查
		
		return new ObjectRestResponse<JSONObject>().data(resultJObj);
	}
	
	
	public void emdProcess(JSONObject objs) {
		wfProcTaskService.endProcessInstance(objs);
		
		//更新业务数据
		
	}
}
