package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.LeadershipAssignBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.LeadershipAssign;
import com.bjzhianjia.scp.cgp.entity.RegulaObject;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.AdminFeign;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.mapper.RegulaObjectMapper;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.vo.LeadershipAssignVo;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import tk.mybatis.mapper.entity.Example;

/**
 * 领导交办
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class LeadershipAssignService {
	@Autowired
	private LeadershipAssignBiz leadershipAssignBiz;
	@Autowired
	private CaseInfoBiz caseInfoBiz;
	@Autowired
	private RegulaObjectMapper regulaObjectMapper;
	@Autowired
	private DictFeign dictFeign;
	@Autowired
	private AdminFeign adminFeign;

	/**
	 * 添加单个对象
	 * 
	 * @author 尚
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Result<Void> createdLeadershipAssign(LeadershipAssignVo vo) throws Exception {
		/*
		 * 该方法需要对两个事务进行处理，所以在某一个事务发生异常时，需要抛出异常，以通知虚拟机回滚事务
		 */

		Result<Void> result = new Result<>();

		String doingExeStatus = CommonUtil.exeStatusUtil(dictFeign,
				Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_DOING);

		LeadershipAssign selectById = leadershipAssignBiz.selectById(vo.getId());
		if (selectById == null) {
			// 用户未进行暂存，直接点击了【预立案】按钮
			// 1.2->否：同时创建热线记录与预立案记录

			// 创建热线记录
			vo.setExeStatus(doingExeStatus);// 此时处理状态应为【处理中】
			Result<LeadershipAssign> cPublicOinionResult = this.createdLeaderAssignCache(vo);
			if (!cPublicOinionResult.getIsSuccess()) {
				result.setMessage(cPublicOinionResult.getMessage());
				throw new Exception(cPublicOinionResult.getMessage());
			}
		} else {
			// 1.1->是：将该暂存记录与当前要进行存储的预立案记录关联，并更新热线记录处理状态为【处理中】
			vo.setExeStatus(doingExeStatus);// 处理状态改为【处理中】
			Result<LeadershipAssign> update = updateCache(vo);
			if (!update.getIsSuccess()) {
				result.setMessage(update.getMessage());
				throw new Exception(update.getMessage());
			}
		}

		// 创建预立案记录
		Result<CaseInfo> resultCaseInfo = createCaseInfo(vo);
		if (!resultCaseInfo.getIsSuccess()) {
			result.setIsSuccess(false);
			result.setMessage(resultCaseInfo.getMessage());
			return result;
		}

		caseInfoBiz.insertSelective(resultCaseInfo.getData());

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	private Result<CaseInfo> createCaseInfo(LeadershipAssignVo vo) throws Exception {
		Result<CaseInfo> result = new Result<>();

		// 创建预立案单
		CaseInfo caseInfo = new CaseInfo();
		caseInfo.setSourceType(vo.getCaseSource());
		caseInfo.setSourceCode(vo.getId() + "");

		CaseInfo maxOne = caseInfoBiz.getMaxOne();

		int nextId = maxOne.getId() == null ? 1 : (maxOne.getId() + 1);

		// 立案单事件编号
		Result<String> caseCodeResult = CommonUtil.generateCaseCode(maxOne.getCaseCode());
		if (!caseCodeResult.getIsSuccess()) {
			result.setMessage(caseCodeResult.getMessage());
			throw new Exception(caseCodeResult.getMessage());// 向外抛出异常，使事务回滚
		}
		caseInfo.setCaseCode(caseCodeResult.getData());

		vo.setCaseId(nextId + "");// 将生成的立案单ID也放入到vo中一份，带出到工作流回调方法中

		caseInfo.setCaseTitle(vo.getTaskTitle());
		caseInfo.setCaseDesc(vo.getTaskDesc());
		caseInfo.setOccurAddr(vo.getTaskAddr());
		caseInfo.setDeadLine(vo.getDeadLine());
		caseInfo.setRegulaObjList(vo.getRegulaObjList());
		caseInfo.setOccurTime(vo.getTaskTime());

		result.setIsSuccess(true);
		result.setData(caseInfo);
		return result;
	}

	private Result<LeadershipAssign> check(LeadershipAssign vo, boolean isUpdate) {
		Result<LeadershipAssign> result = new Result<>();
		result.setIsSuccess(false);

		// ii. 验证当前领导交办事件编号的唯一性
		Example example = new Example(LeadershipAssign.class);
		Example.Criteria criteria = example.createCriteria();
		criteria.andEqualTo("taskCode", vo.getTaskCode());
		List<LeadershipAssign> inDBList = leadershipAssignBiz.selectByExample(example);
		if (inDBList != null && !inDBList.isEmpty()) {
			if (isUpdate) {
				for (LeadershipAssign tmp : inDBList) {
					if (!tmp.getId().equals(vo.getId()) && tmp.getIsDeleted().equals("0")) {
						result.setMessage("领导交办事项编号已存在");
						return result;
					}
				}
			} else {
				for (LeadershipAssign tmp : inDBList) {
					if (tmp.getIsDeleted().equals("0")) {
						result.setMessage("领导交办事项编号已存在");
						return result;
					}
				}
			}
		}

		result.setIsSuccess(true);
		return result;
	}

	/**
	 * 更新单个对象
	 * 
	 * @author 尚
	 * @param leadershipAssign
	 * @return
	 */
	public Result<LeadershipAssign> updateCache(LeadershipAssign leadershipAssign) {
		Result<LeadershipAssign> result = check(leadershipAssign, true);

		if (!result.getIsSuccess()) {
			return result;
		}

		leadershipAssignBiz.updateSelectiveById(leadershipAssign);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 同时修改登陆与预立案
	 * 
	 * @author 尚
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Result<LeadershipAssign> update(LeadershipAssignVo vo) throws Exception {
		Result<LeadershipAssign> result = check(vo, true);

		if (!result.getIsSuccess()) {
			return result;
		}

		// 判断舆情是否已启动
		LeadershipAssign leadershipAssign = leadershipAssignBiz.selectById(vo.getId());

		String toDoExeStatus = CommonUtil.exeStatusUtil(dictFeign,
				Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_TODO);

		if (!leadershipAssign.getExeStatus().equals(toDoExeStatus)) {
			result.setIsSuccess(false);
			result.setMessage("当前记录不能修改，只有【未发起】的热线记录可修改！");
			return result;
		}

		String doingExeStatus = CommonUtil.exeStatusUtil(dictFeign,
				Constances.LeaderAssignExeStatus.ROOT_BIZ_LDSTATE_DOING);

		vo.setExeStatus(doingExeStatus);
		leadershipAssignBiz.updateSelectiveById(vo);

		// 创建预立案单记录
		Result<CaseInfo> resultCaseInfo = createCaseInfo(vo);
		if (!resultCaseInfo.getIsSuccess()) {
			result.setIsSuccess(false);
			result.setMessage(resultCaseInfo.getMessage());
			return result;
		}

		caseInfoBiz.insertSelective(resultCaseInfo.getData());

		result.setMessage("成功");
		return result;
	}

	/**
	 * 分页获取对象
	 * 
	 * @author 尚
	 * @param leadershipAssign
	 * @param page
	 * @param limit
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public TableResultResponse<LeadershipAssignVo> getList(LeadershipAssign leadershipAssign, int page, int limit,
			String startTime, String endTime) {
		TableResultResponse<LeadershipAssign> tableResult = leadershipAssignBiz.getList(leadershipAssign, page, limit,
				startTime, endTime);

		List<LeadershipAssign> rows = tableResult.getData().getRows();

		List<LeadershipAssignVo> voList = queryAssist(rows);

		return new TableResultResponse<LeadershipAssignVo>(tableResult.getData().getTotal(), voList);
	}

	private List<LeadershipAssignVo> queryAssist(List<LeadershipAssign> rows) {
		List<LeadershipAssignVo> voList = BeanUtil.copyBeanList_New(rows, LeadershipAssignVo.class);

		// 聚和领导交办处理状态
		Map<String, String> dictIdMap = dictFeign.getDictIds(Constances.ROOT_BIZ_LDSTATE);
		if (dictIdMap != null && !dictIdMap.isEmpty()) {
			for (LeadershipAssignVo vo : voList) {
				vo.setExeStatusName(dictIdMap.get(vo.getExeStatus()));
			}
		}

		// 聚和交办领导《数据库中直接保存的是领导人的人名》
		// 交办领导为选填，所以交办领导项有可能为空

		/*
		 * 以下注释代码不要删
		 */
//		Set<String> leaderIdListStr = new HashSet<>();
//		for(LeadershipAssignVo vo:voList) {
//			if(vo.getTaskLeader()!=null) {
//				leaderIdListStr.add(vo.getTaskLeader());
//			}
//		}
//		Map<String, String> leaderMap = adminFeign.getUser(String.join(",", leaderIdListStr));
//		if(leaderMap!=null&&!leaderMap.isEmpty()) {
//			for(LeadershipAssignVo vo:voList) {
//				List<String> leaderNameList=new ArrayList<>();
//				String taskLeaders = vo.getTaskLeader();
//				
//				if(StringUtils.isNotBlank(taskLeaders)) {
//					String[] split = taskLeaders.split(",");
//					for(String taskLeaderId:split) {
//						String leaderName = leaderMap.get(taskLeaderId);
//						if(null!=leaderName) {
//							JSONObject leaderNameJObject = JSONObject.parseObject(leaderName);
//							leaderNameList.add(leaderNameJObject.getString("name"));
//						}
//					}
//				}
//				vo.setLeaderNames(String.join(",", leaderNameList));
//			}
//		}
		/*
		 * 以上注释代码不要删
		 */

		// 解析监管对象
		List<String> reguylaObjIdList = new ArrayList<>();
		for (LeadershipAssignVo tmp : voList) {
			if (StringUtils.isNotBlank(tmp.getRegulaObjList())) {
				reguylaObjIdList.add(tmp.getRegulaObjList());
			}
		}

		List<RegulaObject> regulaObjectList = new ArrayList<>();
		if (reguylaObjIdList != null && !reguylaObjIdList.isEmpty()) {
			regulaObjectList = regulaObjectMapper.selectByIds(String.join(",", reguylaObjIdList));
		}
		for (LeadershipAssignVo tmp : voList) {
//			String regulaObjListStr = tmp.getRegulaObjList();
//			List<RegulaObject> regulaObjectList = regulaObjectMapper.selectByIds(regulaObjListStr);
			if (regulaObjectList != null && !regulaObjectList.isEmpty()) {
				if (StringUtils.isNotBlank(tmp.getRegulaObjList())) {
					String regulaObjListStr = tmp.getRegulaObjList();
					String[] split = regulaObjListStr.split(",");
					List<String> regulaObjList = Arrays.asList(split); // 当前领导交办包含的监管对象集合
					
					List<String> collect = new ArrayList<>();
					for (RegulaObject regulaObjectTmp : regulaObjectList) {
						if (regulaObjList.contains(String.valueOf(regulaObjectTmp.getId()))
								&& regulaObjectTmp.getIsDeleted().equals("0")) {
							collect.add(regulaObjectTmp.getObjName());
						}
					}
					
					tmp.setRegulaObjName(String.join(",", collect));
				}
			}
		}

		// 聚和相关立案编号
		List<String> collect = voList.stream().map(o -> o.getId() + "").distinct().collect(Collectors.toList());

		if (collect != null && !collect.isEmpty()) {
			Example caseInfoExample = new Example(CaseInfo.class);
			Example.Criteria criteria = caseInfoExample.createCriteria();

			Map<String, String> dictValueMap = dictFeign
					.getDictIdByCode(Constances.BizEventType.ROOT_BIZ_EVENTTYPE_LEADER, false);

			String dictId = "";
			if (dictValueMap != null && !dictValueMap.isEmpty()) {
				List<String> aaList = new ArrayList<>(dictValueMap.keySet());
				// dictValueMap按code等值查询，得到的结果集为唯一
				dictId = aaList.get(0);
			}

			criteria.andEqualTo("sourceType", dictId);
			criteria.andIn("sourceCode", collect);
			List<CaseInfo> caseInfoListInDB = caseInfoBiz.selectByExample(caseInfoExample);

			Map<String, String> caseInfoMap = caseInfoListInDB.stream()
					.collect(Collectors.toMap(CaseInfo::getSourceCode, CaseInfo::getCaseCode));
			if (caseInfoMap != null && !caseInfoMap.isEmpty()) {
				for (LeadershipAssignVo vo : voList) {
					vo.setCaseCode(caseInfoMap.get(vo.getId() + ""));
				}
			}
		}

		return voList;
	}

	/**
	 * 获取单个对象
	 * 
	 * @author 尚
	 * @param id
	 * @return
	 */
	public ObjectRestResponse<LeadershipAssignVo> getOne(Integer id) {
		LeadershipAssign leadershipAssign = leadershipAssignBiz.selectById(id);
		List<LeadershipAssign> rows = new ArrayList<>();
		rows.add(leadershipAssign);
		List<LeadershipAssignVo> voList = queryAssist(rows);
		return new ObjectRestResponse<LeadershipAssignVo>().data(voList.get(0));
	}

	/**
	 * 添加领导交办暂存对象
	 * 
	 * @author 尚
	 * @param publicOninion
	 * @return
	 * @throws Exception
	 */
	public Result<LeadershipAssign> createdLeaderAssignCache(LeadershipAssignVo vo) throws Exception {
		Result<LeadershipAssign> result = new Result<>();

		LeadershipAssign maxOne = leadershipAssignBiz.getMaxOne();
		int maxId = -1;
		String preTaskCode = "";
		if (maxOne == null) {
			maxId = 1;
			preTaskCode = null;
		} else {
			maxId = maxOne.getId() + 1;
			preTaskCode = maxOne.getTaskCode().substring(4);
		}

		vo.setId(maxId);
		// 生成领导交办事项编号
		Result<String> taskCodeResult = CommonUtil.generateCaseCode(preTaskCode == null ? null : preTaskCode);
		if (!taskCodeResult.getIsSuccess()) {
			result.setMessage(taskCodeResult.getMessage());
			throw new Exception(taskCodeResult.getMessage());// 向外抛出异常，使事务回滚
		}

		vo.setTaskCode("LDJB" + taskCodeResult.getData());
		result = check(vo, false);
		if (!result.getIsSuccess()) {
			return result;
		}

		leadershipAssignBiz.insertSelective(vo);

		result.setIsSuccess(true);
		result.setData(vo);
		return result;
	}
}
