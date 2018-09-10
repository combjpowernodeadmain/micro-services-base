package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.biz.WritsInstancesBiz;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;

import lombok.extern.slf4j.Slf4j;

/**
 * 执法案件审批处理类
 * 
 * @author 尚
 */
@Service
@Slf4j
public class CleCaseRegistrationCallBackService implements IWfProcTaskCallBackService {

    @Autowired
    private WritsInstancesBiz writsInstanceBiz;

    @Autowired
    private CaseRegistrationBiz caseRegistrationBiz;

    @Override
    public void before(String dealType, Map<String, Object> procBizData) throws BizException {
        String bizType = String.valueOf(procBizData.get(PROC_BIZTYPE));
        log.debug("*********************************enter into call_back program*************************************");
        log.debug("*********************************bizType:" + bizType + "*************************************");

        switch (bizType) {
            case PROC_APPROVE:
                // 审批操作
                writsInstanceBiz.updateOrInsert(JSONObject.parseObject(JSON.toJSONString(procBizData)));
                break;
            case PROC_CLAIM:
                // 签收操作
                break;
            case PROC_END:
                // 流程走向结束
                endCase(procBizData);
            case "termination":
                terminationCase(procBizData);
                break;
            default:
                break;
        }
        log.debug("流程回调处理完毕");
    }

    private void endCase(Map<String, Object> procBizData) {
        String caseId = (String) procBizData.get("procBizId");
        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setId(caseId);
        caseRegistration.setExeStatus(CaseRegistration.EXESTATUS_STATE_STOP);
        caseRegistrationBiz.updateSelectiveById(caseRegistration);
    }
    private void terminationCase(Map<String, Object> procBizData) {
        String caseId = (String) procBizData.get("procBizId");
        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setId(caseId);
        caseRegistration.setExeStatus(CaseRegistration.EXESTATUS_STATE_STOP);
        caseRegistrationBiz.updateSelectiveById(caseRegistration);
    }

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws BizException {}

}
