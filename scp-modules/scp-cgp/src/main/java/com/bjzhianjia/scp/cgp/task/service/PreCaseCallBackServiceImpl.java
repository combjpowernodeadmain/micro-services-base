package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CommandCenterHotlineBiz;
import com.bjzhianjia.scp.cgp.entity.CaseInfo;
import com.bjzhianjia.scp.cgp.entity.CommandCenterHotline;
import com.bjzhianjia.scp.cgp.entity.Constances;
import com.bjzhianjia.scp.cgp.entity.Result;
import com.bjzhianjia.scp.cgp.service.LeadershipAssignService;
import com.bjzhianjia.scp.cgp.service.MayorHotlineService;
import com.bjzhianjia.scp.cgp.service.PatrolTaskService;
import com.bjzhianjia.scp.cgp.service.PublicOpinionService;
import com.bjzhianjia.scp.cgp.vo.LeadershipAssignVo;
import com.bjzhianjia.scp.cgp.vo.MayorHotlineVo;
import com.bjzhianjia.scp.cgp.vo.PublicOpinionVo;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;

import lombok.extern.slf4j.Slf4j;

/**
 * 处理预立案单
 * 
 * @author 尚
 *
 */
@Service
@Transactional
@Slf4j
public class PreCaseCallBackServiceImpl implements IWfProcTaskCallBackService {

    @Autowired
    private MayorHotlineService mayorHotlineService;

    @Autowired
    private PublicOpinionService publicOpinionService;

    @Autowired
    private LeadershipAssignService leadershipAssignService;

    @Autowired
    private PatrolTaskService patrolTaskService;

    @Autowired
    private CommandCenterHotlineBiz commandCenterHotLineBiz;

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

        log.warn("添加事件逻辑--前回调逻辑--事件来源为：" + key);

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
                addPatrolTask(procBizData);
                break;
            case Constances.BizEventType.ROOT_BIZ_EVENTTYPE_COMMAND_LINE:
                // 指挥中心
                addCommandCenterLine(procBizData);
                break;
            default:
                break;
        }
    }

    private void addCommandCenterLine(Map<String, Object> procBizData) throws BizException {
        CommandCenterHotline instance = JSON.parseObject(JSON.toJSONString(procBizData), CommandCenterHotline.class);
        Result<CaseInfo> result = new Result<>();

        try {
            result = commandCenterHotLineBiz.createInstance(instance);
            procBizData.put("procBizId", String.valueOf(result.getData().getId()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws BizException {

    }

    /**
     * 添加巡查上报
     * 
     * @author 尚
     * @param procBizData
     */
    private void addPatrolTask(Map<String, Object> procBizData) {
        JSONObject patroTaskJObj = JSONObject.parseObject(JSON.toJSONString(procBizData));
        try {
            Result<CaseInfo> result = patrolTaskService.createPatrolTask(patroTaskJObj);

            CaseInfo caseInfo = result.getData();
            if (caseInfo != null) {
                procBizData.put("procBizId", String.valueOf(caseInfo.getId()));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
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

        if (vo.getId() == null && StringUtils.isNotBlank((String) procBizData.get("sourceCode"))) {
            vo.setId(Integer.valueOf((String) procBizData.get("sourceCode")));
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

        if (vo.getId() == null && StringUtils.isNotBlank((String) procBizData.get("sourceCode"))) {
            vo.setId(Integer.valueOf((String) procBizData.get("sourceCode")));
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

        if (vo.getId() == null && StringUtils.isNotBlank((String) procBizData.get("sourceCode"))) {
            vo.setId(Integer.valueOf((String) procBizData.get("sourceCode")));
        }

        try {
            mayorHotlineService.createdMayorHotline(vo);
            procBizData.put("procBizId", String.valueOf(vo.getCaseId()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new BizException(e.getMessage());
        }
    }
}
