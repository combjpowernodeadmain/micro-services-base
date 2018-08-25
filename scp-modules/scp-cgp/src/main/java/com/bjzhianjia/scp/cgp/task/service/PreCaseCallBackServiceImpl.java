package com.bjzhianjia.scp.cgp.task.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
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
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;

/**
 * 处理预立案单
 * 
 * @author 尚
 *
 */
@Service
@Transactional
public class PreCaseCallBackServiceImpl implements IWfProcTaskCallBackService {
	@Autowired
	private MayorHotlineService mayorHotlineService;
	@Autowired
	private PublicOpinionService publicOpinionService;
	@Autowired
	private LeadershipAssignService leadershipAssignService;
	@Autowired
	private DictFeign dictFeign;
	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Override
	public void before(String dealType, Map<String, Object> procBizData) throws BizException {
		/*
		 * 在进行预立案单处理时，业务ID还没有生成，所以将业务逻辑放在before方法进行操作，以获取业务ID
		 */

		// 前端将来源的字典code传入
		String key = (String) procBizData.get("sourceType");

		if (StringUtils.isBlank(key)) {
			throw new BizException("请指定事件来源");
		}

		/*
		 * 查询字典里的事件来源，将字典里的code与前端传code对比，确定ID 将确定后的ID保存到数据库中
		 */
		String _key = "";
		Map<String, String> dictValueByID = dictFeign.getDictValueByID(key);
		if (dictValueByID != null && !dictValueByID.isEmpty()) {
			JSONObject jObject = JSONObject.parseObject(dictValueByID.get(key));
			_key = jObject.getString("code");
		} else {
			throw new BizException("未找到与事件来源类型对应的编号");
		}

		switch (_key) {
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

	@Override
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

		vo.setCaseSource((String) procBizData.get("sourceType"));

		if (vo.getId() == null && StringUtils.isNotBlank((String) procBizData.get("procBizId"))
				&& !"-1".equals((String) procBizData.get("procBizId"))) {
			vo.setId(Integer.valueOf((String) procBizData.get("procBizId")));
		}

		Result<Void> result = new Result<>();
		try {
			result = leadershipAssignService.createdLeadershipAssign(vo);
			if (!result.getIsSuccess()) {
				throw new BizException(result.getMessage());
			}
			procBizData.put("procBizId", String.valueOf(vo.getCaseId()));
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

		vo.setCaseSource((String) procBizData.get("sourceType"));

		if (vo.getId() == null && StringUtils.isNotBlank((String) procBizData.get("procBizId"))
				&& !"-1".equals((String) procBizData.get("procBizId"))) {
			vo.setId(Integer.valueOf((String) procBizData.get("procBizId")));
		}

		Result<Void> result = new Result<>();
		try {
			result = publicOpinionService.createdPublicOpinion(vo);
			if (!result.getIsSuccess()) {
				throw new BizException(result.getMessage());
			}
			procBizData.put("procBizId", String.valueOf(vo.getCaseId()));
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

		vo.setCaseSource((String) procBizData.get("sourceType"));

		if (vo.getId() == null && StringUtils.isNotBlank((String) procBizData.get("procBizId"))
				&& !"-1".equals((String) procBizData.get("procBizId"))) {
			vo.setId(Integer.valueOf((String) procBizData.get("procBizId")));
		}

		Result<Void> result = new Result<>();
		try {
			result = mayorHotlineService.createdMayorHotline(vo);
			if (!result.getIsSuccess()) {
				throw new BizException(result.getMessage());
			}
			procBizData.put("procBizId", String.valueOf(vo.getCaseId()));

			// 添加缓存到Redis
//			String redisField="caseInfo_"+vo.getId();
//			
//			stringRedisTemplate.opsForHash().put(redisKey, "caseCode", vo.getCaseCode());
//			stringRedisTemplate.opsForHash().put(redisKey, "caseTitle", vo.getCaseCode());
//			stringRedisTemplate.opsForHash().put(redisKey, "occurTime", vo.getAppealDatetime());
//			stringRedisTemplate.opsForHash().put(redisKey, "bizList", "");//热线不涉及业务条线
//			stringRedisTemplate.opsForHash().put(redisKey, "eventTypeList", "");//热线也不涉及事件类别
//			stringRedisTemplate.opsForHash().put(redisKey, "sourceCode", vo.getCaseSource());
//			stringRedisTemplate.opsForHash().put(redisKey, "caseLevel", "");

		} catch (Exception e) {
			e.printStackTrace();
			throw new BizException(StringUtils.isBlank(result.getMessage()) ? e.getMessage() : result.getMessage());
		}
	}

	private void addCacheDataToRedis(Map<String, Object> toCache) {

	}
}
