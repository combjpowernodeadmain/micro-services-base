package com.bjzhianjia.scp.security.wf.base.task.service.impl;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bjzhianjia.scp.security.wf.base.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.base.task.biz.WfProcTaskCallBackBiz;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcBean;
import com.bjzhianjia.scp.security.wf.base.task.service.IWfProcTaskCallBackService;
import com.bjzhianjia.scp.security.wf.base.utils.DateTools;
import com.bjzhianjia.scp.security.wf.base.utils.JSONUtil;
import com.bjzhianjia.scp.security.wf.base.utils.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 流程任务回调实现类
 * 
 * @author scp
 *
 */
@Service
@Slf4j
public class WfProcTaskCallBackDefaultService implements
		IWfProcTaskCallBackService {
	
	@Autowired
	WfProcTaskCallBackBiz wfProcTaskCallBackBiz;

	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.REPEATABLE_READ)
	public void before(String dealType, Map<String, Object> procBizData)
			throws WorkflowException {
		log.info("process task deal type:{}", dealType);
		WfProcBean bean1 = new WfProcBean();
		WfProcBean bean2 = new WfProcBean();

		try {
			String bizId = (String) procBizData.get(PROC_BIZID);
			String bizType = (String) procBizData.get(PROC_BIZTYPE);
			String bizMemo = (String) procBizData.get(PROC_BIZMEMO);
			String orgCode = (String) procBizData.get(PROC_ORGCODE);

			if (StringUtil.isNull(bizId)) {
				procBizData.put(PROC_BIZID, UUID.randomUUID().toString());
			}

			if (StringUtil.isNull(bizType)) {
				procBizData.put(PROC_BIZTYPE, "1101");
			}

			if (StringUtil.isNull(bizMemo)) {
				procBizData.put(PROC_BIZMEMO, "测试流程验证");
			}

			if (StringUtil.isNull(orgCode)) {
				procBizData.put(PROC_ORGCODE, "1001001");
			}

			if (procBizData != null) {
				log.info(JSONUtil.objToJson(procBizData));
				bean1.setProcInstId("068144cb-ea7f-11e6-8e2c-80a589ac6c46");
				bean1.setProcMemo("测试流程验证:"
						+ (String) procBizData.get(PROC_INSTANCEID));
				bean2.setProcInstId("db33e41f-3b76-11e7-9b54-80a589ac6c46");
				bean2.setProcMemo("测试流程验证:"
						+ (String) procBizData.get(PROC_INSTANCEID));
				bean2.setProcEndtime(DateTools.getCurrTime());
				wfProcTaskCallBackBiz.updateWfProcess(bean1);
				wfProcTaskCallBackBiz.updateWfProcess(bean2);

				if ("2".equals((String) procBizData.get("procCallback"))) {
					log.error("2是啥意思？");
					throw new Exception();
				}
			}
		} catch (WorkflowException wfe) {
			throw wfe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000001, e);
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS, isolation = Isolation.REPEATABLE_READ)
	public void after(String dealType, Map<String, Object> procBizData)
			throws WorkflowException {
		log.info("process task deal type: {}", dealType);
		WfProcBean bean1 = new WfProcBean();
		WfProcBean bean2 = new WfProcBean();

		try {
			String bizId = (String) procBizData.get(PROC_BIZID);
			String bizType = (String) procBizData.get(PROC_BIZTYPE);
			String bizMemo = (String) procBizData.get(PROC_BIZMEMO);
			String orgCode = (String) procBizData.get(PROC_ORGCODE);

			if (StringUtil.isNull(bizId)) {
				procBizData.put(PROC_BIZID, UUID.randomUUID().toString());
			}

			if (StringUtil.isNull(bizType)) {
				procBizData.put(PROC_BIZTYPE, "1101");
			}

			if (StringUtil.isNull(bizMemo)) {
				procBizData.put(PROC_BIZMEMO, "测试流程验证");
			}

			if (StringUtil.isNull(orgCode)) {
				procBizData.put(PROC_ORGCODE, "1001001");
			}

			if (procBizData != null) {
				log.info(JSONUtil.objToJson(procBizData));
				bean1.setProcInstId("000182b4-3b79-11e7-b453-80a589ac6c46");
				bean1.setProcMemo("测试流程验证:"
						+ (String) procBizData.get(PROC_INSTANCEID));
				bean2.setProcInstId("473b69ea-fe55-11e6-a5dc-80a589ac6c46");
				bean2.setProcMemo("测试流程验证:"
						+ (String) procBizData.get(PROC_INSTANCEID));
				bean2.setProcEndtime(DateTools.getCurrTime());
				wfProcTaskCallBackBiz.updateWfProcess(bean1);
				wfProcTaskCallBackBiz.updateWfProcess(bean2);

				if ("3".equals((String) procBizData.get("procCallback"))) {
					throw new Exception();
				}
			}
		} catch (WorkflowException wfe) {
			throw wfe;
		} catch (Exception e) {
			throw new WorkflowException(WorkflowEnumResults.WF_COMM_02000001, e);
		}
	}
}
