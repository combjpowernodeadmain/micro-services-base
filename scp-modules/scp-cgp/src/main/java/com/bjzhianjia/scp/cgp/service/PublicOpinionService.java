package com.bjzhianjia.scp.cgp.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjzhianjia.scp.cgp.biz.CaseInfoBiz;
import com.bjzhianjia.scp.cgp.biz.PublicOpinionBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.PublicOpinion;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.cgp.util.CommonUtil;
import com.bjzhianjia.scp.cgp.vo.PublicOpinionVo;
import com.bjzhianjia.scp.security.common.msg.ObjectRestResponse;
import com.bjzhianjia.scp.security.common.msg.TableResultResponse;

import tk.mybatis.mapper.entity.Example;

/**
 * 舆情
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class PublicOpinionService {
	@Autowired
	private PublicOpinionBiz publicOpinionBiz;
	@Autowired
	private CaseInfoBiz caseInfoBiz;
	@Autowired
	private DictFeign dictFeign;

	/**
	 * 添加市长热线暂存对象
	 * 
	 * @author 尚
	 * @param publicOninion
	 * @return
	 */
	public Result<PublicOpinion> createdPublicOpinionCache(PublicOpinion publicOninion) {
		Result<PublicOpinion> result = checkCache(publicOninion, false);

		if (!result.getIsSuccess()) {
			return result;
		}

		PublicOpinion maxOne = publicOpinionBiz.getMaxOne();
		int maxId = -1;
		if (maxOne == null) {
			maxId = 1;
		} else {
			maxId = maxOne.getId() + 1;
		}

		publicOninion.setId(maxId);

		publicOpinionBiz.insertSelective(publicOninion);

		result.setIsSuccess(true);
		result.setData(publicOninion);
		return result;
	}

	public Result<PublicOpinion> checkCache(PublicOpinion publicOninion, boolean isUpdate) {
		Result<PublicOpinion> result = new Result<>();
		result.setIsSuccess(false);

		// 验证舆情事件编号唯一性
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("opinCode", publicOninion.getOpinCode());
		List<PublicOpinion> instanceInDB = publicOpinionBiz.getByMap(conditions);
		if (instanceInDB != null) {
			if (isUpdate) {
				for (PublicOpinion tmp : instanceInDB) {
					if (tmp.getIsDeleted().equals("0") && !tmp.getId().equals(publicOninion.getId())) {
						result.setMessage("舆情事件编号已存在");
						return result;
					}
				}
			} else {
				for (PublicOpinion tmp : instanceInDB) {
					if (tmp.getIsDeleted().equals("0")) {
						result.setMessage("舆情事件编号已存在");
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
	public Result<Void> createdPublicOpinion(PublicOpinionVo vo) throws Exception {
		/*
		 * 该方法需要对两个事务进行处理，所以在某一个事务发生异常时，需要抛出异常，以通知虚拟机回滚事务
		 */

		Result<Void> result = new Result<>();
		
		String doingExeStatus = CommonUtil.exeStatusUtil(dictFeign, Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_DOING);

		PublicOpinion selectById = publicOpinionBiz.selectById(vo.getId());
		if (selectById == null) {
			// 用户未进行暂存，直接点击了【预立案】按钮
			// 1.2->否：同时创建热线记录与预立案记录

			// 创建热线记录
			vo.setExeStatus(doingExeStatus);// 此时处理状态应为【处理中】
			Result<PublicOpinion> cPublicOinionResult = this.createdPublicOpinionCache(vo);
			if (!cPublicOinionResult.getIsSuccess()) {
				result.setMessage(cPublicOinionResult.getMessage());
				throw new Exception(cPublicOinionResult.getMessage());
			}
		} else {
			// 1.1->是：将该暂存记录与当前要进行存储的预立案记录关联，并更新热线记录处理状态为【处理中】
			vo.setExeStatus(doingExeStatus);// 处理状态改为【处理中】
			Result<PublicOpinion> update = updateCache(vo);
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

//		int i=1/0;//模拟异常
		caseInfoBiz.insertSelective(resultCaseInfo.getData());

		result.setIsSuccess(true);
		result.setMessage("成功");
		return result;
	}

	@Deprecated
	public Result<Void> check(PublicOpinion publicOpinion, boolean isUpdate) {
		Result<Void> result = new Result<>();

		result.setIsSuccess(true);
		return result;
	}

	public Result<CaseInfo> createCaseInfo(PublicOpinionVo vo) throws Exception {
		Result<CaseInfo> result = new Result<>();

		// 创建预立案单
		CaseInfo caseInfo = new CaseInfo();
		caseInfo.setSourceType(vo.getCaseSource());
		caseInfo.setSourceCode(vo.getId() + "");

		CaseInfo maxOne = caseInfoBiz.getMaxOne();
		
		int nextId=maxOne.getId()==null?1:(maxOne.getId()+1);
		
		// 立案单事件编号
		Result<String> caseCodeResult = CommonUtil.generateCaseCode(maxOne.getCaseCode());
		if (!caseCodeResult.getIsSuccess()) {
			result.setMessage(caseCodeResult.getMessage());
			throw new Exception(caseCodeResult.getMessage());// 向外抛出异常，使事务回滚
		}
		caseInfo.setCaseCode(caseCodeResult.getData());
		vo.setCaseId(nextId+"");//将生成的立案单ID也放入到vo中一份，带出到工作流回调方法中

		caseInfo.setCaseTitle(vo.getOpinTitle());//  立案单.事件标题
		caseInfo.setCaseDesc(vo.getOpinDesc());//  立案单.事件描述
		caseInfo.setOccurAddr(vo.getOpinAddr());//  立案单.发生地址
		caseInfo.setOccurTime(vo.getPublishTime());

		result.setIsSuccess(true);
		result.setData(caseInfo);
		return result;
	}

	/**
	 * 更新单个对象
	 * 
	 * @author 尚
	 * @param publicOpinion
	 * @return
	 */
	public Result<PublicOpinion> updateCache(PublicOpinion publicOpinion) {
		Result<PublicOpinion> result = checkCache(publicOpinion, true);

		if (!result.getIsSuccess()) {
			return result;
		}

		publicOpinionBiz.updateSelectiveById(publicOpinion);
		result.setMessage("成功");
		return result;
	}

	/**
	 * 同时修改舆情与预立案
	 * 
	 * @author 尚
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	public Result<PublicOpinion> update(PublicOpinionVo vo) throws Exception {
		Result<PublicOpinion> result = checkCache(vo, true);

		if (!result.getIsSuccess()) {
			return result;
		}
		

		// 判断舆情是否已启动
		PublicOpinion publicOpinion = publicOpinionBiz.selectById(vo.getId());
		
		String toExeStatus = CommonUtil.exeStatusUtil(dictFeign, Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_TODO);
		String doingExeStatus = CommonUtil.exeStatusUtil(dictFeign, Constances.PublicOpinionExeStatus.ROOT_BIZ_CONSTATE_DOING);
		
		if (!publicOpinion.getExeStatus().equals(toExeStatus)) {
			result.setIsSuccess(false);
			result.setMessage("当前记录不能修改，只有【未发起】的热线记录可修改！");
			return result;
		}

		// 更新舆情记录
		// 修改暂存设置其状态为【处理中】
		vo.setExeStatus(doingExeStatus);
		publicOpinionBiz.updateSelectiveById(vo);

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
	 * @param publicOpinion
	 * @param page
	 * @param limit
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public TableResultResponse<PublicOpinionVo> getList(PublicOpinion publicOpinion, int page, int limit,
			String startTime, String endTime) {
		TableResultResponse<PublicOpinion> tableResult = publicOpinionBiz.getList(publicOpinion, page, limit, startTime,
				endTime);
		List<PublicOpinion> rows = tableResult.getData().getRows();

		List<PublicOpinionVo> voList = queryAssist(rows);

		return new TableResultResponse<PublicOpinionVo>(tableResult.getData().getTotal(), voList);
	}

	private List<PublicOpinionVo> queryAssist(List<PublicOpinion> rows) {
		List<PublicOpinionVo> voList = BeanUtil.copyBeanList_New(rows, PublicOpinionVo.class);

		// 聚和舆情处理状态
		Map<String, String> dictStatusMap = dictFeign.getDictIds(Constances.ROOT_BIZ_CONSTATE);
		if (dictStatusMap != null && !dictStatusMap.isEmpty()) {
			for (PublicOpinionVo vo : voList) {
				vo.setExeStatusName(dictStatusMap.get(vo.getExeStatus()));
			}
		}

		// 聚和舆情来源类型
		Map<String, String> dictConSourceTMap = dictFeign.getDictIds(Constances.ROOT_BIZ_CONSOURCET);
		if (dictConSourceTMap != null && !dictConSourceTMap.isEmpty()) {
			for (PublicOpinionVo vo : voList) {
				vo.setOpinTypeName(dictConSourceTMap.get(vo.getOpinType()));
			}
		}

		// 聚和舆情舆情等级
		Map<String, String> dictConLevelMap = dictFeign.getDictIds(Constances.ROOT_BIZ_CONLEVEL);
		if (dictConLevelMap != null && !dictConLevelMap.isEmpty()) {
			for (PublicOpinionVo vo : voList) {
				vo.setOpinLevelName(dictConLevelMap.get(vo.getOpinLevel()));
			}
		}

		// 聚和相关立案编号
		List<String> collect = voList.stream().map(o -> o.getId() + "").distinct().collect(Collectors.toList());
		Example caseInfoExample = new Example(CaseInfo.class);
		Example.Criteria criteria = caseInfoExample.createCriteria();
		criteria.andEqualTo("sourceType", Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CONSENSUS);
		criteria.andIn("sourceCode", collect);
		List<CaseInfo> caseInfoListInDB = caseInfoBiz.selectByExample(caseInfoExample);

		Map<String, String> caseInfoMap = caseInfoListInDB.stream()
				.collect(Collectors.toMap(CaseInfo::getSourceCode, CaseInfo::getCaseCode));
		if (caseInfoMap != null && !caseInfoMap.isEmpty()) {
			for (PublicOpinionVo vo : voList) {
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
	public ObjectRestResponse<PublicOpinionVo> getOne(Integer id) {
		PublicOpinion publicOpinion = publicOpinionBiz.selectById(id);
		List<PublicOpinion> rows = new ArrayList<>();
		rows.add(publicOpinion);
		List<PublicOpinionVo> voList = queryAssist(rows);
		return new ObjectRestResponse<PublicOpinionVo>().data(voList.get(0));
	}
}
