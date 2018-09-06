package com.bjzhianjia.scp.cgp.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CLEConcernedCompanyBiz;
import com.bjzhianjia.scp.cgp.biz.CLEConcernedPersonBiz;
import com.bjzhianjia.scp.cgp.biz.CaseAttachmentsBiz;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.entity.CLEConcernedCompany;
import com.bjzhianjia.scp.cgp.entity.CLEConcernedPerson;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcTaskHistoryBean;
import com.bjzhianjia.scp.security.wf.base.task.service.impl.WfProcTaskServiceImpl;
import com.github.pagehelper.PageInfo;

/**
 * @author 尚
 */
@Service
public class CaseRegistrationService {
	@Autowired
	private WfProcTaskServiceImpl wfProcTaskService;
	@Autowired
	private AdminFeign adminFeign;
	@Autowired
	private CaseRegistrationBiz caseRegistrationBiz;
	@Autowired
	private CLEConcernedPersonBiz cleConcernedPersonBiz;
	@Autowired
	private CLEConcernedCompanyBiz cleConcernedCompanyBiz;
	@Autowired
	private DictFeign dictFeign;
	@Autowired
	private CaseAttachmentsBiz caseAttachmentsBiz;

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
		
		return null;
	}
	
	/**
	 * 查询待办详情
	 * @author 尚
	 * @param objs
	 * @return
	 */
	public ObjectRestResponse<JSONObject> getUserTaskDetail(JSONObject objs) {
		JSONObject resultJObj = new JSONObject();

		/*
		 * =================================查询历史========================================
		 * ================= TODO By尚--先按模板进行查询，后根据案件的需求进行调整
		 */
		// 查询流程历史记录
		JSONObject procDataJObj = objs.getJSONObject("procData");
		if (procDataJObj == null) {
			throw new BizException("请指定待查询流程组件");
		}
		PageInfo<WfProcTaskHistoryBean> procApprovedHistory = wfProcTaskService.getProcApprovedHistory(objs);
		List<WfProcTaskHistoryBean> procHistoryList = procApprovedHistory.getList();
		JSONArray procHistoryJArray = JSONArray.parseArray(JSON.toJSONString(procHistoryList));
		// 流程历史中的任务办理人ID列表
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
		String bizId = "";
		if (StringUtils.isNotBlank(bizIdStr)) {
			bizId = bizIdStr;
		} else {
			throw new BizException("请指定待查询业务ID");
		}

		CaseRegistration caseRegistration = caseRegistrationBiz.selectById(bizId);
		sourceTypeHistoryAssist(resultJObj, caseRegistration);// 来源历史查询帮助
		/*
		 * =================查询基础信息===========结束==========
		 */

		// 通过root_biz进行模糊查询业务字典，这样查询数据量会稍大，但可以减少请求次数
		Map<String, String> manyDictValuesMap = dictFeign.getByCode(Constances.ROOT_BIZ);

		/*
		 * =================查询当事人信息(当事人类型(1个人，2公司）)===========开始==========
		 */
		CLEConcernedPerson concernedPerson = null;
		CLEConcernedCompany concernedCompany = null;
		JSONObject concernedPersonJObj = new JSONObject();
		JSONObject concernedCompanyJObj = new JSONObject();
		if (caseRegistration.getConcernedId() != null) {
			if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_PERSON.equals(caseRegistration.getConcernedType())) {
				// 当事人以个人形式存在
				concernedPerson = cleConcernedPersonBiz.selectById(Integer.valueOf(caseRegistration.getConcernedId()));
				if (concernedPerson != null) {
					concernedPersonJObj = JSONObject.parseObject(JSON.toJSONString(concernedPerson));
					if (manyDictValuesMap != null && !manyDictValuesMap.isEmpty()) {
						// TODO By尚--案件中待联合当事人信息的地方
					}
				}
			} else if (Constances.ConcernedStatus.ROOT_BIZ_CONCERNEDT_ORG.equals(caseRegistration.getConcernedType())) {
				// 当事人以单位形式存在
				concernedCompany = cleConcernedCompanyBiz
						.selectById(Integer.valueOf(caseRegistration.getConcernedId()));
				if (concernedCompany != null) {
					concernedCompanyJObj = JSONObject.parseObject(JSON.toJSONString(concernedCompany));
					// TODO By尚--案件中待联合当事人信息的地方
				}
			}   
		}

		/*
		 * =================查询当事人信息(个人)===========结束 ==========
		 */

		/*
		 * ================关联附件===================开始==================
		 */

		/*
		 * 一个案件可以有多个附件，查询附件采用以下方式关联 前端拿到案件ID后再次向服务器发送查询附件的请求，请求采用分页查询
		 */

		/*
		 * ================关联附件===================结束==================
		 */

		return null;
	}

	private void sourceTypeHistoryAssist(JSONObject resultJObj, CaseRegistration caseRegistration) {
		String key = caseRegistration.getCaseSource();// 案件来源

//		switch (key) {
//		case value:
//			
//			break;
//
//		default:
//			break;
//		}
	}
}
