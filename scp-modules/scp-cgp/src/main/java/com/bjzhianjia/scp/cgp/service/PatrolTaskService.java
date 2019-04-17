package com.bjzhianjia.scp.cgp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.*;
import com.bjzhianjia.scp.cgp.entity.*;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.DateUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.utils.StringUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 巡查任务逻辑层
 * 
 * @author chenshuai
 *
 */
@Service
@Transactional
public class PatrolTaskService {

	@Autowired
	private PatrolTaskBiz patrolTaskBiz;


	@Autowired
	private DictFeign dictFeign;

	@Autowired
	private CaseInfoBiz caseInfoBiz;

	@Autowired
	private ConcernedPersonService concernedPersonService;

	@Autowired
	private ConcernedCompanyService concernedCompanyService;

	@Autowired
	private PatrolResService patrolResService;
	
	@Autowired
	private RegulaObjectBiz regulaObjectBiz;
	
	@Autowired
	private RegulaObjectTypeBiz regulaObjectTypeBiz;
	
	@Autowired
	private SpecialEventBiz specialEventBiz;
	
	@Autowired
	private EventTypeBiz eventTypeBiz;
	
	@Autowired
	private InspectItemsBiz inspectItemsBiz;
	
	@Autowired
	private PatrolResBiz patrolResBiz;

    @Autowired
    private Environment environment;
    
    @Autowired
    private PropertiesProxy propertiesProxy;

    @Autowired
    private AreaGridBiz areaGridBiz;

	/**
	 * 创建巡查任务
	 * 
	 * @param json 
	 * <p>
	 * {
	 *  "patrolTask":"", //巡查任务记录 
	 *  "concernedPerson":"", //当事人
	 *  "concernedCompany":"", //当事人企业信息
	 *	"concernedType":"org", // person 个人，org 企业
	 *	"handleStatus":"submit", //finish直接处理，submit提交受理中心
	 *  "urls":"url1,url2,url3" 
	 * }
	 * <p>
	 * @return
	 * @throws Exception
	 */
	public Result<CaseInfo> createPatrolTask(JSONObject json) throws Exception {
		Result<CaseInfo> result = new Result<>();
		result.setIsSuccess(false);

		String concernedType = json.getString("concernedType"); // person 个人，org 单位)
		String handleStatus = json.getString("handleStatus"); // finish直接处理，submit提交受理中心
		String[] urls = json.getString("urls")==null?null:json.getString("urls").split(",");
		String _concernedType = null; // 当事人类型（数据字典id）
		Integer concernedId = 0;// 当事人id
		// 巡查任务记录
	    PatrolTask patrolTask = this.parseData(json, "patrolTask", PatrolTask.class);
		
	    Result<Void> checkPatrolTask = this.checkPatrolTask(patrolTask);
		if(!checkPatrolTask.getIsSuccess()) {
			result.setMessage(checkPatrolTask.getMessage());
			result.setIsSuccess(false);
			return result;
		}

        // 自动生成巡查事件名称
        boolean isPatrolTaskNameBlank = StringUtils.isBlank(patrolTask.getPatrolName());
        List<String> patrolTaskNameList = new ArrayList<>();
        if (isPatrolTaskNameBlank) {
        	/*
        	 * 巡查事项清单关键字-事项类别-网格名称-日期
        	 */
			// 关键字
			String inspectIds = patrolTask.getInspectIds();
			List<InspectItems> itemList = inspectItemsBiz.getByInspectIds(inspectIds);
			if(BeanUtil.isNotEmpty(itemList)){
				List<String> itemNameList =
					itemList.stream().map(InspectItems::getKeyWord).distinct().collect(Collectors.toList());
				String keywords = String.join(",", itemNameList);
				patrolTaskNameList.add(keywords);
			}

			// 事件类别
			if (BeanUtil.isNotEmpty(patrolTask.getEventTypeId())) {
				EventType eventType = eventTypeBiz.getById(patrolTask.getEventTypeId());
				if (BeanUtil.isNotEmpty(eventType)) {
					patrolTaskNameList.add(eventType.getTypeName());
				}
			}

			// 网格名称
			AreaGrid areaGrid = areaGridBiz.selectById(patrolTask.getAreaGridId());
			if(BeanUtil.isNotEmpty(areaGrid)){
				patrolTaskNameList.add(areaGrid.getGridName());
			}

			// 日期
			patrolTaskNameList.add(DateUtil.dateFromDateToStr(new Date(), "yyyyMM"));

            patrolTask.setPatrolName(String.join("-", patrolTaskNameList));
        }

		// 当事人（个人，单位）
		if ("person".equals(concernedType)) {
			ConcernedPerson concernedPerson = this.parseData(json, "concernedPerson", ConcernedPerson.class);
			Result<Void> createdResult = concernedPersonService.created(concernedPerson);
			if (!createdResult.getIsSuccess()) {
				throw new Exception(createdResult.getMessage());
			}
			concernedId = concernedPerson.getId();
			_concernedType =Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON;
		} else if ("org".equals(concernedType)) {
			ConcernedCompany concernedCompany = this.parseData(json, "concernedCompany", ConcernedCompany.class);
			
			List<RegulaObject>  regulaObjects= regulaObjectBiz.selectByIds(String.valueOf(patrolTask.getRegulaObjectId()));
			//判断企业名称和监管对象id是否匹配，匹配则把关联id，负责作废关联id
			if(regulaObjects != null && regulaObjects.size() == 1)
			{
				RegulaObject regulaObject = regulaObjects.get(0);
				if(regulaObject.getObjName().equals(concernedCompany.getName())) {
					concernedCompany.setRegulaObjectId(patrolTask.getRegulaObjectId());
				}
			}
			
			Result<Void> concernedCompanyResult = concernedCompanyService.created(concernedCompany);
			if (!concernedCompanyResult.getIsSuccess()) {
				throw new Exception(result.getMessage());
			}
			concernedId = concernedCompany.getId();
			_concernedType = Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG;
		}

		// 获取数据字典id
		
		// 获取处理状态
		String toExeStatus = Constances.PartolTaskStatus.ROOT_BIZ_PARTOLTASKT_DOING;
		if ("finish".equals(handleStatus)) {
			toExeStatus =Constances.PartolTaskStatus.ROOT_BIZ_PARTOLTASKT_FINISH;
		}
		
		patrolTask.setStatus(toExeStatus);

		// 生成巡查记录编号
		PatrolTask maxOne = this.getMaxOne();
		Result<String> patrolResult = CommonUtil.generateCaseCode(maxOne != null ? maxOne.getPatrolCode() : null);
		if (!patrolResult.getIsSuccess()) {
			throw new Exception(patrolResult.getMessage());
		}
		patrolTask.setPatrolCode("ZXXC" + patrolResult.getData());
		patrolTask.setConcernedId(concernedId);
		patrolTask.setConcernedType(_concernedType);
		patrolTask.setCrtUserId(BaseContextHandler.getUserID());//上报人姓名
		patrolTaskBiz.insertSelective(patrolTask);
		// 巡查任务记录资源
        List<PatrolRes> patrolResList = new ArrayList<>();
		for (int i = 0; urls != null && i < urls.length; i++) {
			if (StringUtils.isNotBlank(urls[i])) {
				PatrolRes patrolRes = new PatrolRes();
				patrolRes.setPatrolTaskId(patrolTask.getId());
				patrolRes.setUrl(StringUtils.trim(urls[i]));
				patrolResList.add(patrolRes);
			}
		}
		// 将巡查资源批量添加到数据库
		if(BeanUtil.isNotEmpty(patrolResList)){
			patrolResBiz.insertList(patrolResList);
		}

		/*
		 * 最初直接处理不发起事件登记，用“submit”来标示，现在直接处理也进工作流
		 * 即前端传入的是“finish”的时候也要发起事件，故不进行判断
		 */
//		if ("submit".equals(handleStatus)) {
        // 预立案单
        Result<CaseInfo> caseInfoResult = this.createCaseInfo(patrolTask);
        if (!caseInfoResult.getIsSuccess()) {
            throw new Exception(caseInfoResult.getMessage());
        }
        result.setData(caseInfoResult.getData());
        // }

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 创建预立案单
	 * 
	 * @param patrolTask
	 * @return
	 * @throws Exception
	 */
	private Result<CaseInfo> createCaseInfo(PatrolTask patrolTask) throws Exception {
		Result<CaseInfo> result = new Result<>();

		// 创建预立案单
		CaseInfo caseInfo = new CaseInfo();
		caseInfo.setSourceType(Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK);
		caseInfo.setSourceCode(String.valueOf(patrolTask.getId()));

		CaseInfo maxOne = caseInfoBiz.getMaxOne();

        if (BeanUtil.isEmpty(maxOne)) {
            maxOne = new CaseInfo();
        }

        int nextId = maxOne.getId() == null ? 1 : +maxOne.getId() + 1;
		// 立案单事件编号
		Result<String> caseCodeResult = CommonUtil.generateCaseCode(maxOne.getCaseCode());
		if (!caseCodeResult.getIsSuccess()) {
			result.setMessage(caseCodeResult.getMessage());
			throw new Exception(caseCodeResult.getMessage());
		}
		caseInfo.setCaseCode(caseCodeResult.getData());
		
		caseInfo.setId(nextId);//By尚
		caseInfo.setCaseTitle(patrolTask.getPatrolName());
		caseInfo.setCaseDesc(patrolTask.getContent());
		caseInfo.setRegulaObjList(String.valueOf(patrolTask.getRegulaObjectId()));
		caseInfo.setRegulaObjTypeId(String.valueOf(patrolTask.getRegulaObjectTypeId()));
		caseInfo.setBizList(patrolTask.getBizTypeId());
		caseInfo.setEventTypeList(String.valueOf(patrolTask.getEventTypeId()));
		caseInfo.setConcernedPerson(String.valueOf(patrolTask.getConcernedId()));
		caseInfo.setConcernedType(String.valueOf(patrolTask.getConcernedType()));
		caseInfo.setMapInfo(patrolTask.getMapInfo());
		//添加巡查事件发生时间
        caseInfo
            .setOccurTime(patrolTask.getCrtTime() == null ? new Date() : patrolTask.getCrtTime());
        //将巡查的级别作为事件级别
        caseInfo.setCaseLevel(patrolTask.getPatrolLevel());

        // 事件发生地理位置信息
        caseInfo.setGrid(patrolTask.getAreaGridId());
        caseInfo.setOccurAddr(patrolTask.getAddress());

		if(Constances.PartolTaskStatus.ROOT_BIZ_PARTOLTASKT_FINISH.equals(patrolTask.getStatus())){
			//巡查上报为【直接处理】
            caseInfo.setIsFinished(CaseInfo.FINISHED_STATE_FINISH);
		}

		caseInfoBiz.insertSelective(caseInfo);
		caseInfo.getId();

		result.setIsSuccess(true);
		result.setData(caseInfo);
		return result;
	}

	/**
	 * 获取单个对象
	 * 
	 * @param id 待获取对象ID
	 * @return
	 */
	public ObjectRestResponse<PatrolTask> get(Integer id) {
		PatrolTask patrolTask = patrolTaskBiz.selectById(id);

		if (patrolTask == null) {
			return null;
		}
		return new ObjectRestResponse<PatrolTask>().data(patrolTask);
	}

	
	/**
	 *  通过巡查任务id详情
	 * @return
	 */
	public Map<String ,Object> getByIdInfo(Integer id){
		Map<String ,Object> result = new HashMap<>();
		
		PatrolTask patrolTask = patrolTaskBiz.selectById(id);
		if(patrolTask == null) {
			return null;
		}
		
		result.put("patrolCode", patrolTask.getPatrolCode());
		result.put("patrolName", patrolTask.getPatrolName());
		result.put("crtUserName", patrolTask.getCrtUserName());
		result.put("crtTime", patrolTask.getCrtTime());	//上报时间
		result.put("id",patrolTask.getId());
		result.put("sourceType", patrolTask.getSourceType());
		
		//判断任务来源类型
		if(Constances.PartolTaskStatus.ROOT_BIZ_PATROLTYPE_SPECIAL.equals(patrolTask.getSourceType())) {//专项任务
			SpecialEvent specialEvent = specialEventBiz.selectById(patrolTask.getSourceTaskId());
			if(specialEvent != null)
			{
				result.put("speName",specialEvent.getSpeName());//所属专项名称
			}
		}
		
		//监管对象类型
		RegulaObjectType regulaObjectType= regulaObjectTypeBiz.selectById(patrolTask.getRegulaObjectTypeId());
		if(regulaObjectType != null)
		{
			result.put("regulaObjectType", regulaObjectType.getObjectTypeName());
		}
		
		//监管对象
		List<RegulaObject> regulaObjects= regulaObjectBiz.selectByIds(String.valueOf(patrolTask.getRegulaObjectId()));
		if(regulaObjects != null && regulaObjects.size() == 1)
		{
			RegulaObject regulaObject = regulaObjects.get(0);
			
			if(regulaObjects!=null) {
				result.put("regulaObject", regulaObject.getObjName());
			}
		}

		//收集需进行字典查询的key
		Set<String> dictKey=new HashSet<>();
		if(StringUtils.isNotBlank(patrolTask.getPatrolLevel())){//巡查级别
			dictKey.add(patrolTask.getPatrolLevel());
		}
		if(StringUtils.isNotBlank(patrolTask.getStatus())){//巡查状态
			dictKey.add(patrolTask.getStatus());
		}
		if(StringUtil.isNotBlank(patrolTask.getBizTypeId())){//业务条线
			dictKey.add(patrolTask.getBizTypeId());
		}
		if(StringUtils.isNotBlank(patrolTask.getConcernedType())){
			dictKey.add(patrolTask.getConcernedType());
		}
		//查询字典
		Map<String, String> byCodeIn = dictFeign.getByCodeIn(String.join(",", dictKey));

		//事件级别
        String dictPatrolLevel =
            byCodeIn.get(patrolTask.getPatrolLevel()) == null ? ""
                : byCodeIn.get(patrolTask.getPatrolLevel());
		if(dictPatrolLevel != null) {
		   result.put("patrolLevel", dictPatrolLevel);
		}
		
		//获取巡查任务状态
        String dictStatus =
            byCodeIn.get(patrolTask.getStatus()) == null ? ""
                : byCodeIn.get(patrolTask.getStatus());
        if(dictStatus != null) {
			result.put("status", dictStatus);
		}

		//业务条线,可能是多选
		String dictBizType="";
		if(StringUtils.isNotBlank(patrolTask.getBizTypeId())){
			List<String> bizTypeNameList=new ArrayList<>();
			for(String key:patrolTask.getBizTypeId().split(",")){
				String s = byCodeIn.get(patrolTask.getBizTypeId()) == null ? "" : byCodeIn.get(patrolTask.getBizTypeId());
				bizTypeNameList.add(s);
			}
			dictBizType=String.join(",", bizTypeNameList);
		}
		if(dictBizType != null) {
			result.put("bizType", dictBizType);
		}
		
		//事件类别
		EventType eventType = eventTypeBiz.selectById(patrolTask.getEventTypeId());
		if(eventType!=null) {
			result.put("eventType", eventType.getTypeName());
            result.put("eventTypeName",
                eventType.getTypeName().endsWith(",")
                    ? eventType.getTypeName().substring(0, eventType.getTypeName().length() - 1)
                    : eventType.getTypeName());
        }
		//巡查事项清单
		List<InspectItems> inspectList = inspectItemsBiz.getByInspectIds(patrolTask.getInspectIds());
		StringBuilder inspectItemNames = new StringBuilder();
		InspectItems tempInspectItems = null;
		for(int i=0; i < inspectList.size(); i++){
			tempInspectItems = inspectList.get(i);
			if(i == inspectList.size()-1){
				inspectItemNames.append(tempInspectItems.getName());
			}else{
				inspectItemNames.append(tempInspectItems.getName()).append(";");
			}
		}
		//巡查事项清单名称
		result.put("inspectItems", inspectItemNames);

		result.put("content", patrolTask.getContent());
		result.put("address", patrolTask.getAddress());
		result.put("mapInfo", patrolTask.getMapInfo());
		
		//当事人
		String dictConcernedType =
				byCodeIn.get(patrolTask.getConcernedType()) == null ? ""
						: byCodeIn.get(patrolTask.getConcernedType());
		if(dictConcernedType != null) {
			result.put("concernedType", patrolTask.getConcernedType());
			result.put("concernedTypeName", dictConcernedType);
			//单位
			if(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG.equals(patrolTask.getConcernedType())) {
				ConcernedCompany concernedCompany = concernedCompanyService.selectById(patrolTask.getConcernedId());
				result.put("concernedData", concernedCompany);
				result.put("isCompany", true);//是否为单位
			}
			//个人
			if(Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON.equals(patrolTask.getConcernedType())){
				ConcernedPerson concernedPerson = concernedPersonService.selectById(patrolTask.getConcernedId());
				result.put("concernedData", concernedPerson);
				//个人性别+证件类型
                Set<String> concernedDictKeySist=new HashSet<>();
                if(StringUtils.isNotBlank(concernedPerson.getSex())){
                    concernedDictKeySist.add(concernedPerson.getSex());
                }
                if(StringUtils.isNotBlank(concernedPerson.getCredType())){
                    concernedDictKeySist.add(concernedPerson.getCredType());
                }
                Map<String, String> concernedPersonValues=null;
                if(BeanUtil.isNotEmpty(concernedDictKeySist)){
                    concernedPersonValues = dictFeign.getByCodeIn(String.join(",", concernedDictKeySist));
                }
                if(BeanUtil.isEmpty(concernedPersonValues)){
                    concernedPersonValues=new HashMap<>();
                }

                concernedPerson.setSex(concernedPersonValues.get(concernedPerson.getSex()));

                JSONObject resultConcernedJObj =
                    JSONObject.parseObject(JSONObject.toJSONString(concernedPerson));
                resultConcernedJObj.put("credTypeName",
                    concernedPersonValues.get(concernedPerson.getCredType()));

                result.put("concernedData", resultConcernedJObj);
                result.put("isCompany", false);
			}
		}
		//获取图片集
		List<PatrolRes> patrolResList = patrolResBiz.getByPatrolTaskId(patrolTask.getId());
		List<String> urls = new ArrayList<>();
		if(patrolResList != null && patrolResList.size()>0) {
			for(PatrolRes patrolRes:patrolResList) {
				urls.add(patrolRes.getUrl());
			}
		}
		result.put("urls", urls);

		return result;
	}
	
	/**
	 * 解析json 获取实体对象
	 * 
	 * @param objs
	 * @param jsonKey
	 * @param clazz
	 * @return
	 */
	private <T> T parseData(JSONObject objs, String jsonKey, Class<T> clazz) {
		try {
			if (objs.containsKey(jsonKey)) {
				return (T) JSON.toJavaObject(objs.getJSONObject(jsonKey), clazz);
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 查询ID最大的那条记录
	 * 
	 * @author 尚
	 * @return
	 */
	public PatrolTask getMaxOne() {
		Example example = new Example(CaseInfo.class);
		example.setOrderByClause("id desc");
		PageHelper.startPage(1, 1);
		List<PatrolTask> caseInfoList = patrolTaskBiz.selectByExample(example);
		if (caseInfoList == null || caseInfoList.isEmpty()) {
			return null;
		}
		return caseInfoList.get(0);
	}
	
	/**
	 * 检查巡查任务
	 * @param patrolTask
	 * @return
	 */
	public Result<Void> checkPatrolTask(PatrolTask patrolTask) {
		Result<Void> restResult = new Result<Void>();
		restResult.setIsSuccess(true);

		if (StringUtils.isBlank(patrolTask.getPatrolLevel())) {
			restResult.setIsSuccess(false);
			restResult.setMessage("事件级别不能为空！");
			return restResult;
		}
	
		if (StringUtils.isBlank(patrolTask.getAddress()) && StringUtils.isBlank(patrolTask.getMapInfo())) {
			restResult.setIsSuccess(false);
			restResult.setMessage("当前位置不能为空！");
			return restResult;
		}
	
		if (StringUtils.isBlank(patrolTask.getContent())) {
			restResult.setIsSuccess(false);
			restResult.setMessage("巡查事项内容不能为空！");
			return restResult;
		}

		if (patrolTask.getContent().length() > 1024) {
			restResult.setIsSuccess(false);
			restResult.setMessage("巡查事项内容应在1024字符以内！");
			return restResult;
		}

		return restResult;
	}
	
    /**
     * 查询类型为专项的任务
     * 
     * @param page
     * @param limit
     * @return
     */
    public TableResultResponse<JSONObject> listSpecial(int page, int limit) {
        Example example = new Example(PatrolTask.class);
        Criteria criteria = example.createCriteria();
        // criteria.andEqualTo("isDeleted", "0");
        criteria.andEqualTo("sourceType", environment.getProperty("patrolType.special"));

        example.setOrderByClause("id desc");

        Page<Object> pageInfo = PageHelper.startPage(page, limit);
        List<PatrolTask> patrolTaskList = this.patrolTaskBiz.selectByExample(example);
        List<JSONObject> resultJObjList = new ArrayList<>();
        if (BeanUtil.isNotEmpty(patrolTaskList)) {

            Set<String> dictCodeList = new HashSet<>();
            for (PatrolTask tmp : patrolTaskList) {
                if (org.apache.commons.lang3.StringUtils.isNotBlank(tmp.getSourceType())) {
                    dictCodeList.add(tmp.getSourceType());
                }
                if (org.apache.commons.lang3.StringUtils.isNotBlank(tmp.getPatrolLevel())) {
                    dictCodeList.add(tmp.getPatrolLevel());
                }
            }

            Map<String, String> dictValues = new HashMap<>();
            if (BeanUtil.isNotEmpty(dictCodeList)) {
                dictValues = dictFeign.getByCodeIn(String.join(",", dictCodeList));
            }

            for (PatrolTask tmp : patrolTaskList) {
                try {
                    JSONObject jObjTmp =
                        propertiesProxy.swapProperties(tmp, "id", "sourceType", "patrolName", "patrolLevel", "mapInfo",
                            "regulaObjectId");
                    jObjTmp.put("sourceTypeName", dictValues.get(tmp.getSourceType()));
                    jObjTmp.put("patrolLevelName", dictValues.get(tmp.getPatrolLevel()));
                    resultJObjList.add(jObjTmp);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
            return new TableResultResponse<>(pageInfo.getTotal(), resultJObjList);
        }

        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
