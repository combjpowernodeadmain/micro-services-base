package com.bjzhianjia.scp.cgp.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.AreaGridBiz;
import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.PatrolTaskBiz;
import com.bjzhianjia.scp.cgp.biz.RegulaObjectBiz;
import com.bjzhianjia.scp.cgp.entity.AreaGrid;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.ConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.ConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.PatrolRes;
import com.bjzhianjia.scp.cgp.entity.PatrolTask;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.core.context.BaseContextHandler;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

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
	public Result<Void> createPatrolTask(JSONObject json) throws Exception {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		String concernedType = json.getString("concernedType"); // person 个人，org 单位)
		String handleStatus = json.getString("handleStatus"); // finish直接处理，submit提交受理中心
		String[] urls = json.getString("urls").split(",");
		String _concernedType = null; // 当事人类型（数据字典id）
		Integer concernedId = 0;// 当事人id
		// 巡查任务记录
	    PatrolTask patrolTask = this.parseData(json, "patrolTask", PatrolTask.class);
	    
		// 当事人（个人，单位）
		if ("person".equals(concernedType)) {
			ConcernedPerson concernedPerson = this.parseData(json, "concernedPerson", ConcernedPerson.class);
			result = concernedPersonService.created(concernedPerson);
			if (!result.getIsSuccess()) {
				throw new Exception(result.getMessage());
			}
			concernedId = concernedPerson.getId();
			_concernedType = CommonUtil.exeStatusUtil(dictFeign, Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON);
		} else if ("org".equals(concernedType)) {
			ConcernedCompany concernedCompany = this.parseData(json, "concernedCompany", ConcernedCompany.class);
			
			List<RegulaObject>  regulaObjects= regulaObjectBiz.selectByIds(String.valueOf(patrolTask.getRegulaObjectId()));
			//判断企业名称和监管对象id是否匹配，匹配则把关联id，负责作废关联id
			if(regulaObjects != null && regulaObjects.size() == 1)
			{
				RegulaObject regulaObject = regulaObjects.get(0);
				if(regulaObject.getObjName().equals(concernedCompany.getName())) {
					concernedCompany.setRegulaObjectId(patrolTask.getRegulaObjectId());
					
					//获取网格code和id
					 AreaGrid areaGrid = areaGridBiz.selectById(regulaObject.getGriId());
					 if(areaGrid != null) {
						 patrolTask.setAreaGridCode(areaGrid.getGridCode());
						 patrolTask.setAreaGridId(areaGrid.getId());
					 }
				}
			}
			
			result = concernedCompanyService.created(concernedCompany);
			if (!result.getIsSuccess()) {
				throw new Exception(result.getMessage());
			}
			concernedId = concernedCompany.getId();
			_concernedType = CommonUtil.exeStatusUtil(dictFeign, Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG);
		}

		// 获取数据字典id
		
		// 获取处理状态
		String toExeStatus = CommonUtil.exeStatusUtil(dictFeign,
				Constances.PartolTaskStatus.ROOT_BIZ_PARTOLTASKT_DOING);
		if ("finish".equals(handleStatus)) {
			toExeStatus = CommonUtil.exeStatusUtil(dictFeign, Constances.PartolTaskStatus.ROOT_BIZ_PARTOLTASKT_FINISH);
		}
		
		patrolTask.setStatus(toExeStatus);

		// 生成巡查记录编号
		PatrolTask maxOne = this.getMaxOne();
		Result<String> patrolResult = CommonUtil.generateCaseCode(maxOne != null ? maxOne.getPatrolCode() : null);
		if (!patrolResult.getIsSuccess()) {
			throw new Exception(patrolResult.getMessage());
		}
		patrolTask.setReleaseUserName(BaseContextHandler.getUserID());
		patrolTask.setPatrolCode("ZXXC" + patrolResult.getData());
		patrolTask.setConcernedId(concernedId);
		patrolTask.setConcernedType(_concernedType);
		patrolTask.setReleaseUserName(BaseContextHandler.getName());//上报人姓名
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
		caseInfo.setSourceType(
				CommonUtil.exeStatusUtil(dictFeign, Constances.PartolTaskStatus.ROOT_BIZ_PATROLTYPE_SPECIAL));
		caseInfo.setSourceCode(String.valueOf(patrolTask.getId()));

		CaseInfo maxOne = caseInfoBiz.getMaxOne();
		// 立案单事件编号
		Result<String> caseCodeResult = CommonUtil.generateCaseCode(maxOne.getCaseCode());
		if (!caseCodeResult.getIsSuccess()) {
			result.setMessage(caseCodeResult.getMessage());
			throw new Exception(caseCodeResult.getMessage());
		}
		caseInfo.setCaseCode(caseCodeResult.getData());
		
		caseInfo.setCaseTitle(patrolTask.getPatrolName());
		caseInfo.setCaseDesc(patrolTask.getContent());
		caseInfo.setRegulaObjList(String.valueOf(patrolTask.getRegulaObjectId()));
		caseInfo.setRegulaObjTypeId(String.valueOf(patrolTask.getRegulaObjectTypeId()));
		caseInfo.setBizList(patrolTask.getBizTypeId());
		caseInfo.setEventTypeList(patrolTask.getEventTypeId());
		caseInfo.setConcernedPerson(String.valueOf(patrolTask.getConcernedId()));
		caseInfo.setConcernedType(String.valueOf(patrolTask.getConcernedType()));
		caseInfo.setMapInfo(patrolTask.getMapInfo());

		caseInfoBiz.insertSelective(caseInfo);

		result.setIsSuccess(true);
		result.setData(caseInfo);
		return result;
	}

	/**
	 * 根据查询条件搜索
	 * 
	 * @param patrolTask 巡查任务记录
	 * @param speName    专项任务名称
	 * @param startTime  开始时间
	 * @param endTime    结束时间
	 * @param page       页码
	 * @param limit      页容量
	 * @return
	 */
	public TableResultResponse<Map<String, Object>> getList(PatrolTask patrolTask, String speName,
			Date startTime, Date endTime,int page,int limit) {

		TableResultResponse<Map<String, Object>> tableResult = patrolTaskBiz.selectPatrolTaskList(patrolTask, speName,
				startTime, endTime, page, limit);

		List<Map<String, Object>> list = tableResult.getData().getRows();
		if (list.size() == 0) {
			return tableResult;
		}
		return tableResult;
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
	 * 解析json 获取实体对象
	 * 
	 * @param objs
	 * @param entityName
	 * @param clazz
	 * @return
	 */
	private <T> T parseData(JSONObject objs, String jsonKey, Class<T> clazz) {
		if (objs.containsKey(jsonKey)) {
			return (T) JSON.toJavaObject(objs.getJSONObject(jsonKey), clazz);
		} else {
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
}
