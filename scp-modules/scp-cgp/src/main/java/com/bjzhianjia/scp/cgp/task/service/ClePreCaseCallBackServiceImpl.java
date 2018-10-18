package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bjzhianjia.scp.cgp.biz.CaseRegistrationBiz;
import com.bjzhianjia.scp.cgp.entity.CaseRegistration;
import com.bjzhianjia.scp.security.wf.base.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;

import lombok.extern.slf4j.Slf4j;

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

    @Override
    public void before(String dealType, Map<String, Object> procBizData) throws BizException {
        String bizType = String.valueOf(procBizData.get(PROC_BIZTYPE));
        if ("null".equals(bizType)) {
            // 将bizTYpe置为空字符串，防止在switch时产生空指针，空字符串可进入default选项
            bizType = "";
        }
        log.debug("*********************************案件办理，进入回调类程序*************************************");
        log.debug("*********************************bizType:" + bizType + "*************************************");

        JSONObject bizData = JSONObject.parseObject(JSON.toJSONString(procBizData));

        switch (bizType) {
            case PROC_END:
                bizData.put("exeStatus", CaseRegistration.EXESTATUS_STATE_FINISH);
                break;
            default:
                bizData.put("exeStatus", CaseRegistration.EXESTATUS_STATE_TODO);
                break;
        }
        // 添加立案
        caseResistrationBiz.addCase(bizData);
        procBizData.put("procBizId", bizData.getString("procBizId"));
    }

    @Override
    public void after(String dealType, Map<String, Object> procBizData) throws BizException {}

}
