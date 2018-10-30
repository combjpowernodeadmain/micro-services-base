package com.bjzhianjia.scp.cgp.task.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.biz.WritsInstancesBiz;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        log.debug("********************参数结构为:" + procBizData.toString() + "******************");

        switch (bizType) {
            case PROC_APPROVE:
                // 审批操作
                writsInstanceBiz.addWritsInstances(JSONObject.parseObject(JSON.toJSONString(procBizData)));
                break;
            case PROC_CLAIM:
                // 签收操作
                break;

            case PROC_UNCLAIM:
                // 取消签收
                break;
            case PROC_END:
                //将文书提交后流程走向结束
                writsInstanceBiz.addWritsInstances(JSONObject.parseObject(JSON.toJSONString(procBizData)));
                // 流程走向结束
                endCase(procBizData);
                break;
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
        
        if(StringUtils.isBlank(caseId)) {
            log.info("进行流程结束操作，但并未指定案件ID");
            throw new BizException("进行流程结束操作，但并未指定案件ID");
        }
        
        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setId(caseId);
        caseRegistration.setExeStatus(CaseRegistration.EXESTATUS_STATE_FINISH);
        caseRegistrationBiz.updateSelectiveById(caseRegistration);
    }

    private void terminationCase(Map<String, Object> procBizData) {
        String caseId = (String) procBizData.get("procBizId");
        
        if(StringUtils.isBlank(caseId)) {
            log.info("进行流程中止操作，但并未指定案件ID");
            throw new BizException("进行流程中止操作，但并未指定案件ID");
        }
        
        CaseRegistration caseRegistration = new CaseRegistration();
        caseRegistration.setId(caseId);
        caseRegistration.setExeStatus(CaseRegistration.EXESTATUS_STATE_STOP);
        caseRegistrationBiz.updateSelectiveById(caseRegistration);
    }

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws BizException {}

}
