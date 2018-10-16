package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.EventTypeBiz;
import com.bjzhianjia.scp.cgp.biz.InspectItemsBiz;
import com.bjzhianjia.scp.cgp.biz.PatrolResBiz;
import com.bjzhianjia.scp.cgp.biz.PatrolTaskBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectTypeBiz;
import com.bjzhianjia.scp.cgp.biz.SpecialEventBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.InspectItems;
import com.bjzhianjia.scp.cgp.entity.PatrolRes;
import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.RegulaObjectType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.entity.SpecialEvent;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.util.PropertiesProxy;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

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
		for (int i = 0; urls != null && i < urls.length; i++) {
			PatrolRes patrolRes = new PatrolRes();
			patrolRes.setPatrolTaskId(patrolTask.getId());
			patrolRes.setUrl(urls[i]);
			if (StringUtils.isNotBlank(urls[i])) {
				patrolResService.created(patrolRes);
			}
		}

		// 提交受理中心
		if ("submit".equals(handleStatus)) {
			// 预立案单
			Result<CaseInfo> caseInfoResult = this.createCaseInfo(patrolTask);
			if (!caseInfoResult.getIsSuccess()) {
				throw new Exception(caseInfoResult.getMessage());
			}
			result.setData(caseInfoResult.getData());
		}

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
		
		int nextId=maxOne==null?1:(maxOne.getId()+1);
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
			result.put("regulaObject", regulaObjectType.getObjectTypeName());
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
		
		//事件级别
		String dictPatrolLevel = CommonUtil.getByCode(dictFeign,patrolTask.getPatrolLevel());
		if(dictPatrolLevel != null) {
		   result.put("patrolLevel", dictPatrolLevel);
		}
		
		//获取巡查任务状态
		String dictStatus = CommonUtil.getByCode(dictFeign,patrolTask.getStatus());
		if(dictStatus != null) {
			result.put("status", dictStatus);
		}
		
		//业务条线
		String dictBizType = CommonUtil.getByCode(dictFeign,patrolTask.getBizTypeId());
		if(dictBizType != null) {
			result.put("bizType", dictBizType);
		}
		
		//事件类别
		EventType eventType = eventTypeBiz.selectById(patrolTask.getEventTypeId());
		if(eventType!=null) {
			result.put("eventType", eventType.getTypeName());
		}
		
		
		//巡查事项清单
		List<InspectItems> inspectItemsList =new ArrayList<>();
		if(BeanUtil.isNotEmpty(patrolTask.getEventTypeId())) {
		    inspectItemsList =inspectItemsBiz.getByEventType(patrolTask.getEventTypeId());
		}
		if(inspectItemsList!=null && inspectItemsList.size()>0) {
			List<String> names = new ArrayList<>();
			for(InspectItems inspectItems : inspectItemsList) {
				names.add(inspectItems.getName());
			}
			result.put("inspectItems",String.join(",", names));
		}
		
		result.put("content", patrolTask.getContent());
		result.put("address", patrolTask.getAddress());
		result.put("mapInfo", patrolTask.getMapInfo());
		
		//当事人
		String dictConcernedType = CommonUtil.getByCode(dictFeign,patrolTask.getConcernedType());
		if(dictConcernedType != null) {
			result.put("concernedType", dictConcernedType);
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
				//个人性别
				concernedPerson.setSex(CommonUtil.getByCode(dictFeign,concernedPerson.getSex()));
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
	 * @param entityName
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
	
		if (StringUtils.isBlank(patrolTask.getPatrolName()) && patrolTask.getPatrolName().length() > 127) {
			restResult.setIsSuccess(false);
			restResult.setMessage("事件名称不能为空！");
			return restResult;
		}
	
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
	
		if (StringUtils.isBlank(patrolTask.getContent()) && patrolTask.getContent().length() > 1024) {
			restResult.setIsSuccess(false);
			restResult.setMessage("巡查事项内容不能为空！");
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
