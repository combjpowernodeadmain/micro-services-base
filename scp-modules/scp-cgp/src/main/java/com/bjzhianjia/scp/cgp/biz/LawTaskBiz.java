package com.bjzhianjia.scp.cgp.biz;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.EnforceCertificate;
import com.bjzhianjia.scp.cgp.entity.LawExecutePerson;
import com.bjzhianjia.scp.cgp.entity.LawPatrolObject;
import com.bjzhianjia.scp.cgp.entity.LawTask;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.LawTaskMapper;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.security.common.biz.BusinessBiz;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import com.bjzhianjia.scp.security.wf.base.utils.JSONUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import tk.mybatis.mapper.entity.Example;

/**
 * 执法任务管理
 *
 * @author chenshuai
 */
@Service
@Transactional
public class LawTaskBiz extends BusinessBiz<LawTaskMapper, LawTask> {

	@Autowired
	private LawPatrolObjectBiz lawPatrolObjectBiz;

	@Autowired
	private LawExecutePersonBiz lawExecutePersonBiz;

	@Autowired
	private LawTaskMapper lawTaskMapper;

	@Autowired
	private AdminFeign adminFeign;

	@Autowired
	private DictFeign dictFeign;

	@Autowired
	private EnforceCertificateBiz enforceCertificateBiz;

	@Autowired
	private RegulaObjectBiz regulaObjectBiz;

	public void createLawTask(LawTask lawTask) throws Exception {
		// 生成巡查记录编号
		LawTask maxOne = this.getMaxOne();
		Result<String> patrolResult = CommonUtil.generateCaseCode(maxOne != null ? maxOne.getLawTaskCode() : null);
		if (!patrolResult.getIsSuccess()) {
			throw new Exception(patrolResult.getMessage());
		}
		lawTask.setLawTaskCode("ZFRW" + patrolResult.getData());
		super.insertSelective(lawTask);
		// 添加关系表记录
		this.addExecutePerso(lawTask);
		this.addPatrolObjects(lawTask);

	}

	/**
	 * 更新执法任务
	 * 
	 * @param lawTask
	 */
	public void update(LawTask lawTask) {
		Integer lawTaskId = lawTask.getId();
		// 更新关系表
		LawTask _lawTask = super.selectById(lawTaskId);
		if (_lawTask != null) {
			// 判断巡查对象是否修改
			if (!_lawTask.getPatrolObject().equals(lawTask.getPatrolObject())) {
				lawPatrolObjectBiz.delByLawTaskId(lawTaskId);
				this.addPatrolObjects(lawTask);
			}
			// 判断执法者是否修改
			if (!_lawTask.getExecutePerson().equals(lawTask.getExecutePerson())) {
				lawExecutePersonBiz.delByLawTaskId(lawTask.getId());
				this.addExecutePerso(lawTask);
			}
			super.updateSelectiveById(lawTask);
		}
	}

	/**
	 * 添加巡查对象关系表记录
	 * 
	 * @param lawTask 执法任务
	 */
	private void addPatrolObjects(LawTask lawTask) {
		// 监管对象
		JSONObject patrolObjects = JSONUtil.strToJSON(lawTask.getPatrolObject());
		Set<String> patrolObjectsIds = patrolObjects.keySet();
		if (patrolObjectsIds != null && !patrolObjectsIds.isEmpty()) {
			LawPatrolObject lawPatrolObject = null;
			for (String id : patrolObjectsIds) {
				lawPatrolObject = new LawPatrolObject();
				lawPatrolObject.setLawTaskId(lawTask.getId());
				// 监管对象id
				lawPatrolObject.setRegulaObjectId(Integer.valueOf(id));
				// 监管对象名称
				lawPatrolObject.setRegulaObjectName(patrolObjects.get(id).toString());
				lawPatrolObjectBiz.insertSelective(lawPatrolObject);
			}
		}
	}

	/**
	 * 添加执法者关系表
	 * 
	 * @param lawTask 执法任务
	 */
	// TODO 优化
	private void addExecutePerso(LawTask lawTask) {
		// 格式：{id:{"name":"","deptId":""},id{}...}
		JSONArray executePerso = JSONArray.parseArray(lawTask.getExecutePerson());
		// 获取分队名称（部门名称）
		if (executePerso != null && !executePerso.isEmpty()) {
			JSONObject obj = null; // 执法者信息
			JSONObject _obj = null; // 执法者信息
			LawExecutePerson lawExecutePerson = null; // 执法者
			for (int i = 0; executePerso != null && i < executePerso.size(); i++) {
				obj = executePerso.getJSONObject(i);
				Set<String> key = obj.keySet();
				for (String userId : key) {
					_obj = JSONObject.parseObject(obj.getString(userId));
					// 添加任务执法者关系表
					lawExecutePerson = new LawExecutePerson();
					lawExecutePerson.setLawTaskId(lawTask.getId());
					// 执法者id
					lawExecutePerson.setUserId(userId);
					// 执法者姓名
					lawExecutePerson.setUserName(_obj.getString("userName"));
					// 执法者分队id
					lawExecutePerson.setDepartId(_obj.getString("deptId"));
					lawExecutePersonBiz.insertSelective(lawExecutePerson);
				}

			}
		}
	}

	/**
	 * 执法任务翻页查询
	 * 
	 * @param userName         执法者姓名
	 * @param regulaObjectName 巡查对象名称
	 * @param state            任务状态
	 * @param startTime        开始日期
	 * @param endTime          结束日期
	 * @param page             页码
	 * @param limit            页容量
	 * @return
	 */
	public TableResultResponse<Map<String, Object>> getLawTaskList(String userName, String regulaObjectName,
			String state, Date startTime, Date endTime, int page, int limit) {
		List<Map<String, Object>> list = lawTaskMapper.selectLawTaskList(userName, regulaObjectName, state, startTime,
				endTime);
		Page<Object> result = PageHelper.startPage(page, limit);
		if (list == null) {
			return new TableResultResponse<Map<String, Object>>(0, null);
		}

		Map<String, String> dictData = dictFeign.getByCode(LawTask.ROOT_BIZ_LAWTASKS);
		if (list.size() > 0) {
			// 执法者分队（部门名称）
			for (Map<String, Object> map : list) {

				String executePerson = map.get("executePerson").toString();
				String _executePerson = this.getDeptName(executePerson);
				map.put("executePerson", _executePerson);
			}
			// 任务状态（数据字典）
			if (dictData != null) {
				String _state = null;
				for (Map<String, Object> map : list) {
					_state = dictData.get(map.get("state"));
					map.put("state", _state);
				}
			}
		}

		return new TableResultResponse<Map<String, Object>>(result.getTotal(), list);
	}

	/**
	 * 通过json获取执法分队名称（部门名称）
	 * 
	 * @param executePerson 执法者信息
	 *                      [{'userid':{'userName':'xxxx','deptId':'xxxx'}},{'userid':{'userName':'xxx','deptId':'xxxx'}}]
	 * @return [{'userid':{'userName':'xxxx','deptId':'deptName','':'xxx'}},{'userid':{'userName':'xxx','deptId':'xxxx','deptName':'xxxx'}}]
	 */
	@SuppressWarnings("rawtypes")
	private String getDeptName(String executePerson) {
		// 执法者json信息
		JSONArray userObjs = JSONArray.parseArray(executePerson);
		JSONArray _userObjs = new JSONArray(); // 封装执法者信息
		JSONObject deptObj = null; // 执法者部门
		JSONObject obj = null; // 执法者
		if (userObjs != null) {
			for (int i = 0; userObjs != null && i < userObjs.size(); i++) {
				JSONObject object = userObjs.getJSONObject(i);
				Set<String> userIds = object.keySet();
				for (String userId : userIds) {
					String json = object.getString(userId);
					obj = JSONObject.parseObject(json);
					if (obj != null) {
						// 法者分队（部门名称）
						deptObj = adminFeign.getByDeptId(obj.getString("deptId"));
						if (deptObj != null) {
							obj.put("deptName", deptObj.getString("name"));
						}
						_userObjs.add(obj);
					}
				}

			}
		}
		return _userObjs.toJSONString();
	}

	/**
	 * 查询ID最大的那条记录
	 * 
	 * @author chenshuai
	 * @return
	 */
	public LawTask getMaxOne() {
		Example example = new Example(CaseInfo.class);
		example.setOrderByClause("id desc");
		PageHelper.startPage(1, 1);
		List<LawTask> lawTaskList = super.selectByExample(example);
		if (lawTaskList == null || lawTaskList.isEmpty()) {
			return null;
		}
		return lawTaskList.get(0);
	}

	/**
	 * 双随机生成执法任务
	 * 
	 * @param objType         监管对象类型ids
	 * @param griIds          网格源ids
	 * @param peopleNumber    每组人数
	 * @param regulaObjNumber 每组巡查数
	 * @param startTime       开始日期
	 * @param endTime         结束日期
	 * @param info            任务要求
	 * @param bizTypeCode     业务条线
	 * @param eventTypeId     事件类别id
	 * @throws Exception
	 */
	public void randomLawTask(String objType, String griIds, int peopleNumber, int regulaObjNumber, Date startTime,
			Date endTime, String info,String bizTypeCode,int eventTypeId) throws Exception {

		// 执法者列表
		List<EnforceCertificate> userList = enforceCertificateBiz.getEnforceCertificateList();
		List<JSONArray> _userList = this.getUserList(peopleNumber, userList);

		// 监管对象列表
		List<RegulaObject> regulaObjects = regulaObjectBiz.selectByTypeAndGri(objType, griIds);
		List<JSONObject> _regulaObjects = this.getRegulaObject(regulaObjNumber, regulaObjects);

		if (_userList != null && _regulaObjects != null) {
			JSONArray executePerson = null;
			JSONObject patrolObject = null;
			for (int i = 0; i < _userList.size() && i < _regulaObjects.size(); i++) {

				executePerson = _userList.get(i);
				patrolObject = _regulaObjects.get(i);

				LawTask lawTask = new LawTask();
				lawTask.setExecutePerson(executePerson.toJSONString());
				lawTask.setPatrolObject(patrolObject.toJSONString());
				lawTask.setStartTime(startTime);
				lawTask.setEndTime(endTime);
				lawTask.setState(LawTask.ROOT_BIZ_LAWTASKS_TODO);
				lawTask.setInfo(info);
				lawTask.setBizTypeCode(bizTypeCode);
				lawTask.setEventTypeId(eventTypeId);
				this.createLawTask(lawTask);

				if (_userList.size() - 1 == i || _regulaObjects.size() - 1 == i) {
					break;
				}
			}
		}

	}

	/**
	 * 获取分组执法者
	 * 
	 * @param peopleNumber 每组人数
	 * @param userList     执法者列表
	 * @return
	 */
	private List<JSONArray> getUserList(int peopleNumber, List<EnforceCertificate> userList) {
		Collections.shuffle(userList);
		List<JSONArray> list = new ArrayList<>();
		int count = 1;
		JSONArray array = null;
		JSONObject json = null;
		EnforceCertificate enforceCertificate = null;
		for (int i = 0; i < userList.size(); i++) {
			enforceCertificate = userList.get(i);
			if (count == 1) {
				array = new JSONArray();
			}
			// 封装执法者json信息串
			StringBuilder userJson = new StringBuilder();
			userJson.append("{\"userName\":\"").append(enforceCertificate.getCrtUserName()).append("\",")
					.append("\"deptId\":\"").append(enforceCertificate.getDepartId()).append("\"}");

			json = new JSONObject();
			json.put(enforceCertificate.getUsrId(), JSONObject.parseObject(userJson.toString()));
			array.add(json);
			count++;
			if (count > peopleNumber) {
				list.add(array);
				count = 1;
			}
		}
		return list;
	}

	/**
	 * 获取分组监管对象
	 * 
	 * @param regulaObjNumber
	 * @param regulaObjects
	 * @return
	 */
	private List<JSONObject> getRegulaObject(int regulaObjNumber, List<RegulaObject> regulaObjects) {
		Collections.shuffle(regulaObjects);

		List<JSONObject> list = new ArrayList<>();
		int count = 1;
		JSONObject json = null;
		RegulaObject regulaObject = null;
		for (int i = 0; i < regulaObjects.size(); i++) {
			regulaObject = regulaObjects.get(i);
			if (count == 1) {
				json = new JSONObject();
			}
			json.put(regulaObject.getId().toString(), regulaObject.getObjName());
			count++;
			if (count > regulaObjNumber) {
				list.add(json);
				count = 1;
			}
		}
		return list;
	}

}