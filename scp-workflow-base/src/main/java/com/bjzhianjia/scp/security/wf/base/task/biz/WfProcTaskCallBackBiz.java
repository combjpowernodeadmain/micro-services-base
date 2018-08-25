package com.bjzhianjia.scp.security.wf.base.task.biz;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bjzhianjia.scp.security.wf.base.biz.WfBaseBiz;
import com.bjzhianjia.scp.security.wf.base.constant.WorkflowEnumResults;
import com.bjzhianjia.scp.security.wf.base.exception.WorkflowException;
import com.bjzhianjia.scp.security.wf.base.task.entity.WfProcBean;
import com.bjzhianjia.scp.security.wf.base.task.mapper.WfProcBeanMapper;

@Service
public class WfProcTaskCallBackBiz extends WfBaseBiz {
	@Autowired
    WfProcBeanMapper wfProcBeanMapper;
	
	/**
     * 更新业务流程表
     * @param wfProcBean
     * @throws IqbException
     */
    public void updateWfProcess(WfProcBean wfProcBean) throws WorkflowException {
        if (null != wfProcBean) {
            // setDb(0, super.MASTER);
            wfProcBeanMapper.updateWfProcBeanByInstId(wfProcBean);
        } else {
            throw new WorkflowException(WorkflowEnumResults.WF_TASK_02020201);
        }
    }
}
