package com.bjzhianjia.scp.cgp.task.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.biz.LawTaskBiz;
import com.bjzhianjia.scp.cgp.biz.MessageCenterBiz;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.cgp.entity.LawTask;
import com.bjzhianjia.scp.cgp.util.BeanUtil;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 处理执法立案逻辑的回调类
 * 
 * @author 尚
 */
@Service
@Slf4j
public class ClePreCaseCallBackServiceImpl implements IWfProcTaskCallBackService {

    @Autowired
    private CaseRegistrationBiz caseResistrationBiz;

    @Autowired
    private LawTaskBiz lawTaskBiz;

    @Autowired
    private MessageCenterBiz messageCenterBiz;

    @Override
    public void before(String dealType, Map<String, Object> procBizData) throws BizException {
        log.debug("添加案件登陆前回调方法执行，参数结构为：" + procBizData);

        String bizType = String.valueOf(procBizData.get(PROC_BIZTYPE));
        // 加入手机端案件登记，需要判断请求来自手机端还是WEB端
        String queryFrom = String.valueOf(procBizData.get("queryFrom"));

        if ("null".equals(bizType)) {
            // 将bizTYpe置为空字符串，防止在switch时产生空指针，空字符串可进入default选项
            bizType = "";
        }
        log.debug("*********************************案件办理，进入回调类程序*************************************");
        log.debug("*********************************bizType:" + bizType + "*************************************");
        log.debug("*********************************queryFrom:" + queryFrom + "*************************************");

        JSONObject bizData = JSONObject.parseObject(JSON.toJSONString(procBizData));

        String caseSourceType = bizData.getString("caseSourceType");
        if (caseSourceType == null) {
            caseSourceType = "";
        }
        switch (caseSourceType) {
            case CaseRegistration.CASE_SOURCE_TYPE_TASK:
                // 执法任务
                // 处理执法任务业务逻辑
                updateTask(bizData);
                break;
            case CaseRegistration.CASE_SOURCE_TYPE_CENTER:
                // 中心交办
                updateCaseInfo(bizData);
                checkCaseRegistration(bizData,CaseRegistration.CASE_SOURCE_TYPE_CENTER);
                break;
        }

        /*
         * PROC_END表示案件一发起就停止的情况，如现场处理……
         * 当不说明一发起就停止时，默认进入【一般程序】，案件状态为【处理中】
         */
        switch (bizType) {
            case "terminate":
                bizData.put("exeStatus", CaseRegistration.EXESTATUS_STATE_STOP);
                break;
            case PROC_END:
                bizData.put("exeStatus", CaseRegistration.EXESTATUS_STATE_FINISH);
                break;
            default:
                bizData.put("exeStatus", CaseRegistration.EXESTATUS_STATE_TODO);
                break;
        }
        // 添加立案
        /*
         * 加入手机端案件登记，需要判断请求来自手机商还是WEB端
         * 与APP商约定请求来源，手机端定义为“client”(借用Auth服务中对手机端的标识)
         */
        if("client".equals(queryFrom)){
            caseResistrationBiz.addCaseClient(bizData);
        }else{
            caseResistrationBiz.addCase(bizData);
        }

        procBizData.put("procBizId", bizData.getString("procBizId"));
    }

    /**
     * 如果案件登记来自于中心交办或是执法任务，检验案件是否存在
     * @param bizData
     * @param caseSourceType
     */
    private void checkCaseRegistration(JSONObject bizData,String caseSourceType) {
        CaseRegistration caseRegistration;
        if(BeanUtil.isNotEmpty(bizData.getString("caseSource"))){
            // 当存在caseSource时，说明该逻辑为中心交办或执法任务，需要判断是否是由同一事件发起
            CaseRegistration caseRegistrationToCheck=new CaseRegistration();
            caseRegistrationToCheck.setCaseSource(bizData.getString("caseSource"));

            switch (caseSourceType) {
                case CaseRegistration.CASE_SOURCE_TYPE_TASK:
                    caseRegistrationToCheck.setCaseSourceType(CaseRegistration.CASE_SOURCE_TYPE_TASK);
                    break;
                case CaseRegistration.CASE_SOURCE_TYPE_CENTER:
                    caseRegistrationToCheck.setCaseSourceType(CaseRegistration.CASE_SOURCE_TYPE_CENTER);
                    break;
            }

            caseRegistration = caseResistrationBiz.selectOne(caseRegistrationToCheck);
            if(BeanUtil.isNotEmpty(caseRegistration)){
                throw new BizException("该案件已存在，请进行确认。");
            }
        }
    }

    /**
     * 修改中心交办数据
     * @param bizData
     */
    private void updateCaseInfo(JSONObject bizData) {
    }

    /**
     * 修改执法任务数据
     * 
     * @param bizData
     */
    private void updateTask(JSONObject bizData) {
        Integer caseSource = bizData.getInteger("caseSource");
        LawTask lawTask = new LawTask();
        lawTask.setId(caseSource);
        lawTask.setState(LawTask.ROOT_BIZ_LAWTASKS_DOING);
        lawTaskBiz.updateSelectiveById(lawTask);
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
            e.printStackTrace();
            log.debug("添加消息通知失败，数组结构为："+procBizData);
        }
    }
}
