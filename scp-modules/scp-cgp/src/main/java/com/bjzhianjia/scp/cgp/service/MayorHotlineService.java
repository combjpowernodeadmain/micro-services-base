package com.bjzhianjia.scp.cgp.service;

import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.MayorHotlineBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.MayorHotline;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.vo.MayorHotlineVo;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 市长热线
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class MayorHotlineService {
	@Autowired
	private MayorHotlineBiz mayorHotlineBiz;
	@Autowired
	private CaseInfoBiz caseInfoBiz;
	@Autowired
	private DictFeign dictFeign;

	/**
	 * 添加市长热线暂存对象
	 * 
	 * @author 尚
	 * @param vo
	 * @return
	 */
	public Result<MayorHotline> createdMayorHotlineCache(MayorHotlineVo vo, String exeStatus) {
		Result<MayorHotline> result = checkCache(vo, false);

		if (!result.getIsSuccess()) {
			return result;
		}

		MayorHotline maxOne = mayorHotlineBiz.getMaxOne();
		int maxId = -1;
		if (maxOne == null) {
			maxId = 1;
		} else {
			maxId = maxOne.getId() + 1;
		}

		vo.setId(maxId);
		vo.setExeStatus(exeStatus);

		mayorHotlineBiz.insertSelective(vo);

		result.setIsSuccess(true);
		result.setData(vo);
		return result;
	}

	public Result<MayorHotline> checkCache(MayorHotline mayorHotline, boolean isUpdate) {
		Result<MayorHotline> result = new Result<>();
		result.setIsSuccess(false);

		// 验证当前热线事件编号的唯一性
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("hotlnCode", mayorHotline.getHotlnCode());
		List<MayorHotline> byMap = mayorHotlineBiz.getByMap(conditions);
		if (byMap != null) {
			if (isUpdate) {
				for (MayorHotline tmp : byMap) {
					if (!tmp.getId().equals(mayorHotline.getId()) && tmp.getIsDeleted().equals("0")) {
						result.setMessage("热线事件编号已存在");
						return result;
					}
				}
			} else {
				for (MayorHotline tmp : byMap) {
					if (tmp.getIsDeleted().equals("0")) {
						result.setMessage("热线事件编号已存在");
						return result;
					}
				}
			}
		}

		result.setIsSuccess(true);
		return result;
	}

	/**
	 * 添加预立案
	 * 
	 * @author 尚
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Result<Void> createdMayorHotline(MayorHotlineVo vo) throws Exception {
		Result<Void> result = new Result<>();

		/*
		 * 1->按热线事件ID进行查询，用户在使用过程中是否进行了暂存
		 * 1.1->是：将该暂存记录与当前要进行存储的预立案记录关联，并更新热线记录处理状态为【处理中】 1.2->否：同时创建热线记录与预立案记录
		 */
		MayorHotline selectById = mayorHotlineBiz.selectById(vo.getId());
		
		if (selectById == null) {
			// 用户未进行暂存，直接点击了【预立案】按钮
			// 1.2->否：同时创建热线记录与预立案记录

			// 创建热线记录
			Result<MayorHotline> cHotlineResult = this.createdMayorHotlineCache(vo,
					Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DOING);
			if (!cHotlineResult.getIsSuccess()) {
				throw new Exception(cHotlineResult.getMessage());
			}
		} else {
			// 1.1->是：将该暂存记录与当前要进行存储的预立案记录关联，并更新热线记录处理状态为【处理中】
			vo.setExeStatus(Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DOING);// 处理状态改为【处理中】
			Result<MayorHotline> update = updateCache(vo);
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

		CaseInfo caseInfo = resultCaseInfo.getData();
		caseInfoBiz.insertSelective(caseInfo);
		vo.setCaseId(String.valueOf(caseInfo.getId()));//将生成的立案单ID也放入到vo中一份，带出到工作流回调方法中

		return result;
	}

	public Result<CaseInfo> createCaseInfo(MayorHotlineVo vo) throws Exception {
		Result<CaseInfo> result = new Result<>();
		CaseInfo maxOne = caseInfoBiz.getMaxOne();
		
		// 验证该登记单是否创建了预立案

		// 创建预立案单
		CaseInfo caseInfo = new CaseInfo();
		caseInfo.setSourceType(vo.getCaseSource());// 来源事件类型
		caseInfo.setSourceCode(vo.getId() + "");// 来源事件编号

		// 立案单事件编号
		Result<String> caseCodeResult = CommonUtil.generateCaseCode(maxOne == null ? null : maxOne.getCaseCode());
		if (!caseCodeResult.getIsSuccess()) {
			result.setMessage(caseCodeResult.getMessage());
			throw new Exception(caseCodeResult.getMessage());// 向外抛出异常，使事务回滚
		}
		caseInfo.setCaseCode(caseCodeResult.getData());
		vo.setCaseCode(caseCodeResult.getData());//将生成的事件编号也放入到vo中一份，带出到工作流回调方法中
		

		caseInfo.setCaseTitle(vo.getHotlnTitle());//  立案单.事件标题
		caseInfo.setCaseDesc(vo.getAppealDesc());//  立案单.事件描述
		caseInfo.setOccurTime(vo.getAppealDatetime());

		result.setIsSuccess(true);
		result.setData(caseInfo);
		return result;
	}

	@Deprecated
	public Result<Void> check(MayorHotline mayorHotline, boolean isUpdate) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);

		// 验证热线事件编号唯一性
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("hotlnCode", mayorHotline.getHotlnCode());
		List<MayorHotline> byMap = mayorHotlineBiz.getByMap(conditions);
		if (isUpdate) {

		} else {
			// 添加操作
			if (!(byMap == null || byMap.isEmpty())) {
				result.setMessage("热线事件编号已存在");
				return result;
			}
		}

		result.setIsSuccess(true);
		return result;
	}

	/**
	 * 更新单个对象
	 * 
	 * @author 尚
	 * @param mayorHotline
	 * @return
	 */
	public Result<MayorHotline> updateCache(MayorHotline mayorHotline) {
		Result<MayorHotline> result = checkCache(mayorHotline, true);

		if (!result.getIsSuccess()) {
			return result;
		}

		mayorHotlineBiz.updateSelectiveById(mayorHotline);

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 同时修改热线与预立案
	 * 
	 * @author 尚
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Result<MayorHotline> update(MayorHotlineVo vo) throws Exception {
		Result<MayorHotline> result = checkCache(vo, true);

		if (!result.getIsSuccess()) {
			return result;
		}
		

		// 判断热线是否已启动
		MayorHotline mayorHotline = mayorHotlineBiz.selectById(vo.getId());
		if (!Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_TODO.equals(mayorHotline.getExeStatus())) {
			throw new Exception("当前记录不能修改，只有【未发起】的热线记录可修改！");
		}

		// 更新热线记录
		vo.setExeStatus(Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DOING);// 设置其状态为“处理中”
		mayorHotlineBiz.updateSelectiveById(vo);

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
	 * @param mayorHotline
	 * @return
	 */
	public TableResultResponse<MayorHotlineVo> getList(MayorHotline mayorHotline, int page, int limit, String startTime,
			String endTime) {
		TableResultResponse<MayorHotline> tableResult = mayorHotlineBiz.getList(mayorHotline, page, limit, startTime,
				endTime);
		List<MayorHotline> rows = tableResult.getData().getRows();

		List<MayorHotlineVo> voList = queryAssist(rows);

		return new TableResultResponse<MayorHotlineVo>(tableResult.getData().getTotal(), voList);
	}

	private List<MayorHotlineVo> queryAssist(List<MayorHotline> rows) {
		List<MayorHotlineVo> voList = BeanUtil.copyBeanList_New(rows, MayorHotlineVo.class);
		
		if(voList==null||voList.isEmpty()) {
			return voList;
		}

		// 收集字典中的code
		Set<String> dictCodes=new HashSet<>();
		for(MayorHotlineVo voTmp:voList){
			dictCodes.add(voTmp.getHotlnSource());
			dictCodes.add(voTmp.getExeStatus());
		}

		Map<String, String> dictValueMap = dictFeign.getByCodeIn(String.join(",", dictCodes));

		// 聚积字典名称
		if(BeanUtil.isNotEmpty(dictValueMap)){
			for (MayorHotlineVo vo : voList) {
				vo.setExeStatusName(dictValueMap.get(vo.getExeStatus()));
				vo.setHotlnSourceName(dictValueMap.get(vo.getHotlnSource()));
			}
		}

		// 相关立案单
		List<String> collect = voList.stream().map(o -> o.getId() + "").distinct().collect(Collectors.toList());
		Example caseInfoExample = new Example(CaseInfo.class);
		Example.Criteria criteria = caseInfoExample.createCriteria();

		criteria.andEqualTo("sourceType", Constances.BizEventType.ROOT_BIZ_EVENTTYPE_12345);
		criteria.andIn("sourceCode", collect);
		List<CaseInfo> caseInfoListInDB = caseInfoBiz.selectByExample(caseInfoExample);
		
		Map<String, String> caseInfoMap=new HashMap<>();
		for(CaseInfo caseInfo:caseInfoListInDB) {
			caseInfoMap.put(caseInfo.getSourceCode(), caseInfo.getCaseCode());
		}

//		Map<String, String> caseInfoMap = caseInfoListInDB.stream()
//				.collect(Collectors.toMap(CaseInfo::getSourceCode, CaseInfo::getCaseCode));
		if (caseInfoMap != null && !caseInfoMap.isEmpty()) {
			for (MayorHotlineVo vo : voList) {
				vo.setCaseCode(caseInfoMap.get(vo.getId() + ""));
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
	public ObjectRestResponse<MayorHotlineVo> getOne(Integer id) {
		ObjectRestResponse<MayorHotlineVo> result=new ObjectRestResponse<>();

		MayorHotline mayorHotline = mayorHotlineBiz.selectById(id);

		if(BeanUtil.isEmpty(mayorHotline)){
			result.setMessage("未找到相关信息");
			result.setStatus(400);
			return result;
		}

		List<MayorHotline> rows = new ArrayList<>();
		rows.add(mayorHotline);
		List<MayorHotlineVo> voList = queryAssist(rows);

		result.setData(voList.get(0));
		return result;
	}

	/**
	 * 记录反馈
	 * 
	 * @author 尚
	 * @param id
	 * @return
	 */
	public Result<Void> reply(Integer id) {
		Result<Void> result = new Result<>();
		result.setIsSuccess(false);
		
		String doneExeStatus = CommonUtil.exeStatusUtil(dictFeign, Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_DONE);
		String feedBackExeStatus = CommonUtil.exeStatusUtil(dictFeign, Constances.MayorHotlineExeStatus.ROOT_BIZ_12345STATE_FEEDBACK);

		MayorHotline mayorHotline = mayorHotlineBiz.selectById(id);
		if (mayorHotline.getExeStatus().equals(doneExeStatus)) {
			mayorHotline.setExeStatus(feedBackExeStatus);
			mayorHotline.setReplyDatetime(new Date());// 记录反馈的时间
			mayorHotlineBiz.updateSelectiveById(mayorHotline);
			result.setIsSuccess(true);
			result.setMessage("成功");
			return result;
		} else {
			result.setIsSuccess(false);
			result.setMessage("只有【已处理】的热线记录可执行反馈操作！");
			return result;
		}
	}
}
