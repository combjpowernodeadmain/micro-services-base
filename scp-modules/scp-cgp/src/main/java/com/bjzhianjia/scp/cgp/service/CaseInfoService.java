package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.EventType;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.EventTypeMapper;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.monitor.entity.WfProcBackBean;
import com.bjzhianjia.scp.security.wf.base.monitor.service.impl.WfMonitorServiceImpl;
import com.github.pagehelper.PageInfo;

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
	private EventTypeMapper eventTypeMapper;

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
		JSONObject queryData=objs.getJSONObject("queryData");

		if ("true".equals(queryData.getString("isQuery"))) {
			queryCaseInfo = JSONObject.parseObject(queryData.toJSONString(), CaseInfo.class);
		}

		List<JSONObject> jObjList = new ArrayList<>();

		// 查询待办工作流任务
		PageInfo<WfProcBackBean> pageInfo = wfMonitorService.getUserToDoTasks(objs);

		if (pageInfo != null) {
			//有待办任务
			return queryAssist(queryCaseInfo, queryData, jObjList, pageInfo);
		} else {
			//无待办任务
			return new TableResultResponse<>(0, jObjList);
		}
	}

	private TableResultResponse<JSONObject> queryAssist(CaseInfo queryCaseInfo, JSONObject queryData,
			List<JSONObject> jObjList, PageInfo<WfProcBackBean> pageInfo) {
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
		int page=StringUtils.isBlank(queryData.getString("page"))?1:Integer.valueOf(queryData.getString("page"));
		int limit=StringUtils.isBlank(queryData.getString("limit"))?10:Integer.valueOf(queryData.getString("limit"));
		TableResultResponse<CaseInfo> tableResult = caseInfoBiz.getList(queryCaseInfo, bizIdStrList, page,limit, queryData.getString("startQueryTime"),
				queryData.getString("endQueryTime"));
		
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

		for (WfProcBackBean tmp : procBackBeanList) {
			JSONObject wfJObject = JSONObject.parseObject(JSON.toJSONString(tmp));

			CaseInfo caseInfo = caseInfo_ID_Entity_Map.get(Integer.valueOf(tmp.getProcBizid()));
			if (caseInfo != null) {
				wfJObject.put("caseCode", caseInfo.getCaseCode());
				wfJObject.put("caseTitle", caseInfo.getCaseTitle());
				wfJObject.put("occurTime", caseInfo.getOccurTime());
				wfJObject.put("bizList", caseInfo.getBizList());
				wfJObject.put("bizListName", getRootBizTypeName(caseInfo.getBizList(), rootBizList));
				wfJObject.put("eventTypeList", caseInfo.getEventTypeList());
				wfJObject.put("eventTypeListName", getRootBizTypeName(caseInfo.getEventTypeList(), eventTypeMap));
				wfJObject.put("sourceType", caseInfo.getSourceType());
				wfJObject.put("sourceTypeName", getRootBizTypeName(caseInfo.getSourceType(), rootBizList));
				wfJObject.put("caseLevel", caseInfo.getCaseLevel());
				wfJObject.put("caseLevelName", getRootBizTypeName(caseInfo.getCaseLevel(), rootBizList));
			}
			jObjList.add(wfJObject);
		}

		return new TableResultResponse<>(tableResult.getData().getTotal(), jObjList);
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
}
