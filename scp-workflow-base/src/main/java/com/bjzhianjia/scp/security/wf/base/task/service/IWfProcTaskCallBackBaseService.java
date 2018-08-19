package com.bjzhianjia.scp.security.wf.base.task.service;

import com.bjzhianjia.scp.security.wf.base.constant.Constants.WfProcDealType;
import com.bjzhianjia.scp.security.wf.base.constant.Constants.WfProcessBizDataAttr;

public interface IWfProcTaskCallBackBaseService {
	/** 流程处理类型 - 流程启动 */
    public static final String PROC_START = WfProcDealType.PROC_START;
    /** 流程处理类型 - 流程启动并提交 */
    public static final String PROC_COMMIT = WfProcDealType.PROC_COMMIT;
    /** 流程处理类型 - 流程任务审批 */
    public static final String PROC_APPROVE = WfProcDealType.PROC_APPROVE;
    /** 流程处理类型 - 流程任务签收 */
    public static final String PROC_CLAIM = WfProcDealType.PROC_CLAIM;
    /** 流程处理类型 - 流程任务取消签收 */
    public static final String PROC_UNCLAIM = WfProcDealType.PROC_UNCLAIM;
    /** 流程处理类型 - 流程任务删除 */
    public static final String PROC_DELETE = WfProcDealType.PROC_DELETE;
    /** 流程处理类型 - 流程任务取消 */
    public static final String PROC_CANCEL = WfProcDealType.PROC_CANCEL;
    /** 流程处理类型 - 流程任务撤回 */
    public static final String PROC_RETRIEVE = WfProcDealType.PROC_RETRIEVE;
    /** 流程处理类型 - 流程任务终止 */
    public static final String PROC_END = WfProcDealType.PROC_END;
    
    /** 流程处理数据 - 业务ID */
    public static final String PROC_BIZID = WfProcessBizDataAttr.PROC_BIZID;
    /** 流程处理数据 - 业务类型 */
    public static final String PROC_BIZTYPE = WfProcessBizDataAttr.PROC_BIZTYPE;
    /** 流程处理数据 - 所属机构编码 */
    public static final String PROC_ORGCODE = WfProcessBizDataAttr.PROC_ORGCODE;
    /** 流程处理数据 - 流程摘要 */
    public static final String PROC_BIZMEMO = WfProcessBizDataAttr.PROC_BIZMEMO;
    /** 流程处理数据 - 当前流程任务编码 */
    public static final String PROC_CURRTASK = WfProcessBizDataAttr.PROC_CURRTASK;
    /** 流程处理数据 - 流程下一任务列表 */
    public static final String PROC_NEXTTASKS = WfProcessBizDataAttr.PROC_NEXTTASKS;
    /** 流程处理数据 - 流程任务审批结论 */
    public static final String PROC_APPRSTATUS = WfProcessBizDataAttr.PROC_APPRSTATUS;
    /** 流程处理数据 - 当前流程实例ID */
    public static final String PROC_INSTANCEID = WfProcessBizDataAttr.PROC_INSTANCEID;
    /** 流程处理数据 - 流程审批是否结束 */
    public static final String PROC_ENDED = WfProcessBizDataAttr.PROC_ENDED;
}
