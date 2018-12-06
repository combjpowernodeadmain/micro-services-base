package com.bjzhianjia.scp.cgp.task.service;

import java.util.Map;

import com.bjzhianjia.scp.cgp.biz.MessageCenterBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bjzhianjia.scp.cgp.exception.BizException;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;

/**
 * 立案单处理
 * 
 * @author 尚
 */
@Service
@Transactional
@Slf4j
public class CaseCallBackServiceImpl implements IWfProcTaskCallBackService{
    @Autowired
    private MessageCenterBiz messageCenterBiz;

	@Override
	public void before(String dealType, Map<String, Object> procBizData) throws BizException {}

	@Override
    public void after(String dealType, Map<String, Object> procBizData) throws BizException {
        // 添加消息通知
        addMsgCenter(dealType, procBizData);
    }

    private void addMsgCenter(String dealType,Map<String, Object> procBizData){
        if (PROC_END.equals(dealType) || "termination".equals(dealType)) {
            // 如果流程结束，则不进行添加消息通知
            return;
        }
        try {
            messageCenterBiz.addMsgCenterRecord(procBizData);
        } catch (Exception e) {
            log.debug("添加消息通知失败，数组结构为：" + procBizData);
        }
    }
}
