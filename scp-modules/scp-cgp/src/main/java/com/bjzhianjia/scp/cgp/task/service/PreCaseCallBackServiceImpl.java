package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.exception.BizException;
import com.bjzhianjia.scp.cgp.feign.DictFeign;
import com.bjzhianjia.scp.cgp.service.LeadershipAssignService;
import com.bjzhianjia.scp.cgp.service.MayorHotlineService;
import com.bjzhianjia.scp.cgp.service.PublicOpinionService;
import com.bjzhianjia.scp.cgp.vo.LeadershipAssignVo;
import com.bjzhianjia.scp.cgp.vo.MayorHotlineVo;
import com.bjzhianjia.scp.cgp.vo.PublicOpinionVo;

/**
 * 处理预立案单
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class PreCaseCallBackServiceImpl {
	@Autowired
	private MayorHotlineService mayorHotlineService;
	@Autowired
	private PublicOpinionService publicOpinionService;
	@Autowired
	private LeadershipAssignService leadershipAssignService;
	@Autowired
	private DictFeign dictFeign;

	public void before(String dealType, Map<String, Object> procBizData) throws BizException {
		/*
		 * 在进行预立案单处理时，业务ID还没有生成，所以将业务逻辑放在before方法进行操作，以获取业务ID
		 */
		
		// 前端将来源的字典code传入
		String key = (String) procBizData.get("sourceType");
		switch (key) {
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_12345:
			// 市长热线
			addMayorLine(procBizData);
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CONSENSUS:
			// 舆情
			addPublicOpinion(procBizData);
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_LEADER:
			// 领导交办
			addLeaderAssign(procBizData);
			break;
		case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_CHECK:
			// 巡查上报
			addCheck(procBizData);
			break;
		default:
			break;
		}
	}

	public void after(String dealType, Map<String, Object> procBizData) throws BizException {

	}

	private void addCheck(Map<String, Object> procBizData) {
	}

	/**
	 * 添加领导交办预立案单
	 * 
	 * @author 尚
	 * @param procBizData
	 * @throws BizException
	 */
	private void addLeaderAssign(Map<String, Object> procBizData) throws BizException {
		LeadershipAssignVo vo = JSON.parseObject(JSON.toJSONString(procBizData), LeadershipAssignVo.class);

		vo.setCaseSource(getSourceTypeId(procBizData));

		Result<Void> result = new Result<>();
		try {
			result = leadershipAssignService.createdLeadershipAssign(vo);
			if (!result.getIsSuccess()) {
				throw new BizException(result.getMessage());
			}
			procBizData.put("procBizId", vo.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(StringUtils.isBlank(result.getMessage()) ? e.getMessage() : result.getMessage());
		}
	}

	/**
	 * 添加舆情预立案单
	 * 
	 * @author 尚
	 * @param procBizData
	 * @throws BizException
	 */
	private void addPublicOpinion(Map<String, Object> procBizData) throws BizException {
		PublicOpinionVo vo = JSON.parseObject(JSON.toJSONString(procBizData), PublicOpinionVo.class);

		vo.setCaseSource(getSourceTypeId(procBizData));

		Result<Void> result = new Result<>();
		try {
			result = publicOpinionService.createdPublicOpinion(vo);
			if (!result.getIsSuccess()) {
				throw new BizException(result.getMessage());
			}
			procBizData.put("procBizId", vo.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(StringUtils.isBlank(result.getMessage()) ? e.getMessage() : result.getMessage());
		}
	}

	/**
	 * 添加市长热线预立案单
	 * 
	 * @author 尚
	 * @param procBizData
	 * @throws BizException
	 */
	private void addMayorLine(Map<String, Object> procBizData) throws BizException {

		MayorHotlineVo vo = JSON.parseObject(JSON.toJSONString(procBizData), MayorHotlineVo.class);

		vo.setCaseSource(getSourceTypeId(procBizData));

		Result<Void> result = new Result<>();
		try {
			result = mayorHotlineService.createdMayorHotline(vo);
			if (!result.getIsSuccess()) {
				throw new BizException(result.getMessage());
			}
			procBizData.put("procBizId", vo.getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(StringUtils.isBlank(result.getMessage()) ? e.getMessage() : result.getMessage());
		}
	}

	/**
	 * 获取事件来源具体类型对应的ID
	 * 
	 * @author 尚
	 * @param procBizData
	 * @param eventTypeMap
	 * @return
	 */
	private String getSourceTypeId(Map<String, Object> procBizData) {
		/*
		 * 查询字典里的事件来源，将字典里的code与前端传code对比，确定ID 将确定后的ID保存到数据库中
		 */
		Map<String, String> eventTypeMap = dictFeign.getDictIdByCode(Constances.ROOT_BIZ_EVENTTYPE, true);

		String id = "";
		Set<String> keySet = eventTypeMap.keySet();
		for (String string : keySet) {
			JSONObject jObject = JSONObject.parseObject(eventTypeMap.get(string));
			if (jObject.getString("code").equals(procBizData.get("sourceType"))) {
				id = string;
				break;
			}
		}
		return id;
	}

}