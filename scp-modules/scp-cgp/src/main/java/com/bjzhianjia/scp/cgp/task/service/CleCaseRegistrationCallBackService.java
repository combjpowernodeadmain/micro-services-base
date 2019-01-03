package com.bjzhianjia.scp.cgp.task.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.biz.LawTaskBiz;
import com.bjzhianjia.scp.cgp.biz.MessageCenterBiz;
import com.bjzhianjia.scp.cgp.biz.WritsInstancesBiz;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.LawTask;
import com.bjzhianjia.scp.cgp.service.CaseInfoService;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
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
    
    @Autowired
    private LawTaskBiz lawTaskBiz;

    @Autowired
    private MessageCenterBiz messageCenterBiz;

    @Autowired
    private CaseInfoService caseInfoService;
    
    @Override
    public void before(String dealType, Map<String, Object> procBizData) throws BizException {
        String bizType = String.valueOf(procBizData.get(PROC_BIZTYPE));
        log.debug("*********************************enter into call_back program*************************************");
        log.debug("********************参数结构为:" + procBizData.toString() + "******************");

        switch (bizType) {
            case PROC_APPROVE:
                // 审批操作
                writsInstanceBiz.addWritsInstances(JSONObject.parseObject(JSON.toJSONString(procBizData)));
                caseRegistrationBiz.addAttachments(JSONObject.parseObject(JSON.toJSONString(procBizData)));
                approveCase(procBizData);
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
                caseRegistrationBiz.addAttachments(JSONObject.parseObject(JSON.toJSONString(procBizData)));
                // 流程走向结束
                endCase(procBizData);
                break;
            case "termination":
                writsInstanceBiz.addWritsInstances(JSONObject.parseObject(JSON.toJSONString(procBizData)));
                caseRegistrationBiz.addAttachments(JSONObject.parseObject(JSON.toJSONString(procBizData)));
                terminationCase(procBizData);
                break;
            default:
                break;
        }
        log.debug("流程回调处理完毕");
    }

    /**
     * 审批时修改案件信息
     * @param procBizData
     */
    private void approveCase(Map<String, Object> procBizData) {
        String caseId = (String) procBizData.get("procBizId");
        if (StringUtils.isBlank(caseId)) {
            log.info("进行案件审批操作，但并未指定案件ID");
            throw new BizException("进行流程结束操作，但并未指定案件ID");
        }

        CaseRegistration caseRegistration =
            JSONObject.parseObject(JSON.toJSONString(procBizData), CaseRegistration.class);
        if(BeanUtil.isEmpty(caseRegistration)){
            caseRegistration=new CaseRegistration();
        }
        caseRegistration.setId(caseId);

        caseRegistrationBiz.updateSelectiveById(caseRegistration);
    }

    /**
     * 结束案件
     * @param procBizData
     */
    private void endCase(Map<String, Object> procBizData) {
        String caseId = (String) procBizData.get("procBizId");
        
        if(StringUtils.isBlank(caseId)) {
            log.info("进行流程结束操作，但并未指定案件ID");
            throw new BizException("进行流程结束操作，但并未指定案件ID");
        }

        // 当案件结束时，如果提交了案件自身信息(案件表里存储的信息)，则需要去更新案件
        CaseRegistration caseRegistration =
                JSONObject.parseObject(JSON.toJSONString(procBizData), CaseRegistration.class);
        if(BeanUtil.isEmpty(caseRegistration)){
            caseRegistration=new CaseRegistration();
        }

        caseRegistration.setId(caseId);
        caseRegistration.setExeStatus(CaseRegistration.EXESTATUS_STATE_FINISH);
        caseRegistrationBiz.updateSelectiveById(caseRegistration);

        CaseRegistration caseRegistrationInDB = caseRegistrationBiz.selectById(caseId);
        // 判断案件来源类型是否为中心交办
        if (StringUtils.equals(CaseRegistration.CASE_SOURCE_TYPE_CENTER,
            caseRegistrationInDB.getCaseSourceType())) {
            // 去更新事件状态
            caseInfoService.centerCallback(caseRegistrationInDB,PROC_END);
        }

        // 当案件结束时，并不影响到执法任务的状态
        // updateCaseSource(caseId,PROC_END);
    }

    /**
     * 对于结案及中止的案件-->更新来源状态
     *  @param caseId
     * @param bizType
     */
    private void updateCaseSource(String caseId, String bizType) {
        try {
            CaseRegistration caseRegistrationInDB = caseRegistrationBiz.selectById(caseId);
            String caseSourceType = caseRegistrationInDB.getCaseSourceType();

            if(caseSourceType==null){
                return;
            }

            switch (caseSourceType) {
                case CaseRegistration.CASE_SOURCE_TYPE_TASK:
                    LawTask lawTaskToUpdate = new LawTask();
                    lawTaskToUpdate.setId(Integer.valueOf(caseRegistrationInDB.getCaseSource()));

                    // 执法任务
                    if (PROC_END.equals(bizType)) {
                        // 结案操作
                        lawTaskToUpdate.setState(LawTask.ROOT_BIZ_LAWTASKS_FINISH);
                    } else if ("termination".equals(bizType)) {
                        // 中止操作
                        lawTaskToUpdate.setState(LawTask.ROOT_BIZ_LAWTASKS_STOP);
                    }

                    lawTaskBiz.updateSelectiveById(lawTaskToUpdate);
                    break;

                case CaseRegistration.CASE_SOURCE_TYPE_CENTER:
                    // 中心交办
                    if (PROC_END.equals(bizType)) {
                        // 结案操作
                    } else if ("termination".equals(bizType)) {
                        // 中止操作
                    }
                    break;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * 中止案件
     * @param procBizData
     */
    private void terminationCase(Map<String, Object> procBizData) {
        String caseId = (String) procBizData.get("procBizId");
        
        if(StringUtils.isBlank(caseId)) {
            log.info("进行流程中止操作，但并未指定案件ID");
            throw new BizException("进行流程中止操作，但并未指定案件ID");
        }

        // 当案件中止时，如果提交了案件自身信息(案件表里存储的信息)，则需要去更新案件
        CaseRegistration caseRegistration =
                JSONObject.parseObject(JSON.toJSONString(procBizData), CaseRegistration.class);
        if(BeanUtil.isEmpty(caseRegistration)){
            caseRegistration=new CaseRegistration();
        }

        caseRegistration.setId(caseId);
        caseRegistration.setExeStatus(CaseRegistration.EXESTATUS_STATE_STOP);
        caseRegistrationBiz.updateSelectiveById(caseRegistration);

        CaseRegistration caseRegistrationInDB = caseRegistrationBiz.selectById(caseId);
        // 判断案件来源类型是否为中心交办
        if (StringUtils.equals(CaseRegistration.CASE_SOURCE_TYPE_CENTER,
                caseRegistrationInDB.getCaseSourceType())) {
            // 去更新事件状态
            caseInfoService.centerCallback(caseRegistrationInDB,"termination");
        }

        // 当案件中止时，并不影响到执法任务的状态
        // updateCaseSource(caseId,"termination");
    }

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws BizException {
        // 添加消息通知
        addMsgCenter(dealType, procBizData);
    }

    private void addMsgCenter(String dealType,Map<String, Object> procBizData){
        if(PROC_END.equals(dealType)||"termination".equals(dealType)){
            //如果流程结束，则不进行添加消息通知
            return;
        }
        try {
            messageCenterBiz.addMsgCenterRecord(procBizData);
        } catch (Exception e) {
            log.debug("添加消息通知失败，数组结构为："+procBizData);
        }
    }
}
