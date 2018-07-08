/*
 * @(#) WfAttribute.java  1.0  September 09, 2016
 *
 * Copyright 2016 by bjzhianjia
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * IQB("Confidential Information").  You shall not disclose such 
 * Confidential Information and shall use it only in accordance with
 * the terms of the license agreement
 * you entered into with IQB.
 */
package com.bjzhianjia.scp.security.wf.constant;

/**
 * Description: 工作流静态变量类
 * 
 * @author mayongming
 * @version 1.0
 * 
 * <pre>
 * Modification History: 
 * Date             Author      Version         Description 
 * ------------------------------------------------------------------
 * 2016.09.09    mayongming       1.0           1.0 Version
 * </pre>
 */
public class Constants{

    public class WfRequestDataTypeAttr {
        /** 工作流前端请求数据-业务数据 */
        public static final String PROC_BIZDATA = "bizData";
        /** 工作流前端请求数据-流程任务数据 */
        public static final String PROC_PROCDATA = "procData";
        /** 工作流前端请求数据-流程变量 */
        public static final String PROC_VARIABLEDATA = "variableData";
        /** 工作流前端请求数据-流程认证 */
        public static final String PROC_AUTHDATA = "authData";
    }
    
    /**
     * 工作流前端请求数据-流程任务数据类型
     * @author mayongming
     *
     */
    public class WfProcessDataAttr {
        /** 流程定义ID */
        public static final String PROC_DEFINITIONID = "procDefId";
        /** 流程定义代码 */
        public static final String PROC_DEFINITIONKEY = "procDefKey"; 
        /** 流程定义名称 */
        public static final String PROC_DEFINITIONNAME = "procDefName";
        /** 流程定义版本号 */
        public static final String PROC_DEFINITIONVERSION = "procDefVersion";
        /** 流程模型ID */
        public static final String PROC_MODELID = "modelId";
        /** 流程部署ID */
        public static final String PROC_DEPLOYMENTID = "deploymentId";
        /** 流程实例ID */
        public static final String PROC_INSTANCEID = "procInstId"; 
        /** 流程实例名称 */
        public static final String PROC_INSTANCENAME = "procInstName";
        /** 流程任务表ID */
        public static final String PROC_TASKID = "procTaskId";
        /** 流程任务代码 */
        public static final String PROC_TASKCODE = "procTaskCode"; 
        /** 流程任务名称 */
        public static final String PROC_TASKNAME = "procTaskName"; 
        /** 流程任务处理候选用户组 */
        public static final String PROC_TASKGROUP = "procTaskGroup"; 
    }
    
    /**
     * 工作流前端请求数据-流程变量数据类型
     * @author mayongming
     *
     */
    public class WfProcessVariableDataAttr {
        public static final String PROC_AUTHTYPE = "procAuthType";
        /** 流程任务token认证 -- 用户账号 */
        public static final String PROC_TOKENUSER = "procTokenUser";
        /** 流程任务token认证 -- 认证密码 */
        public static final String PROC_TOKENPASS = "procTokenPass";
        /** 流程任务的处理用户 */
        public static final String PROC_TASKUSER = "procTaskUser";
        /** 流程任务处理用户所属角色-Token方式 */
        public static final String PROC_TASKROLE = "procTaskRole";
        /** 流程任务处理用户所属角色列表 - Session方式 */
        public static final String PROC_TASKROLES = "procTaskRoles";
        /** 流程任务指定处理用户 */
        public static final String PROC_ASSIGNEE = "procAssignee";
        /** 流程任务指定提交节点 */
        public static final String PROC_APPOINTTASK = "procAppointTask";
        /** 流程任务审批结论 */
        public static final String PROC_APPRSTATUS = "procApprStatus";
        /** 流程任务审批意见 */
        public static final String PROC_APPROPINION = "procApprOpinion";
        /** 流程关联业务的业务类型 */
        public static final String PROC_BIZTYPE = "procBizType"; 
        /** 流程关联业务的所属机构代码 */
        public static final String PROC_ORGCODE = "procOrgCode"; 
        /** 流程特殊处理说明  */
        public static final String PROC_SPECIALDESC = "procSpecialDesc"; 
    }
    
    /**
     * 工作流请求认证数据-流程请求合法性认证数据
     * @author mayongming
     *
     */
    public class WfProcessAuthData {
        public static final String PROC_AUTHTYPE = "procAuthType";
        /** 流程任务token认证 -- 用户账号 */
        public static final String PROC_TOKENUSER = "procTokenUser";
        /** 流程任务token认证 -- 认证密码 */
        public static final String PROC_TOKENPASS = "procTokenPass";
        /** 流程任务的处理用户 */
        public static final String PROC_TASKUSER = "procTaskUser";
        /** 流程任务处理用户所属角色-Token方式 */
        public static final String PROC_TASKROLE = "procTaskRole";
        /** 流程任务处理用户所属角色列表 - Session方式 */
        public static final String PROC_TASKROLES = "procTaskRoles";
        /** 流程任务处理用户所属机构代码 */
        public static final String PROC_ORGCODE = "procOrgCode"; 
    }
    
    /**
     * 工作流前端请求数据-业务数据类型
     * @author mayongming
     *
     */
    public class WfProcessBizDataAttr {
        /** 流程关联的业务ID */
        public static final String PROC_BIZID = "procBizId";
        /** 流程关联业务的业务类型 */
        public static final String PROC_BIZTYPE = "procBizType"; 
        /** 流程关联业务的所属机构代码 */
        public static final String PROC_ORGCODE = "procOrgCode"; 
        /** 流程关联业务的业务摘要 */
        public static final String PROC_BIZMEMO = "procBizMemo"; 
        /** 流程任务审批结论 */
        public static final String PROC_APPRSTATUS = "procApprStatus";
        /** 流程实例ID */
        public static final String PROC_INSTANCEID = "procInstId"; 
        /** 当前流程任务代码 */
        public static final String PROC_CURRTASK = "procCurrTask";
        /** 下一流程任务代码 */
        public static final String PROC_NEXTTASKS = "procNextTasks";
        /** 当前流程是否结束 1:是;0:否 */
        public static final String PROC_ENDED = "procEnded";
    }
    
    /**
     * 流程启动方式，分为通过流程定义key启动，以及通过流程定义id启动
     * @author mayongming
     *
     */
    public class WfProcessStartModeAttr {
        /** 通过流程定义key启动工作流 */
        public static final int PROC_DEFKEY = 1;
        /** 通过流程定义Id启动工作流 */
        public static final int PROC_DEFID = 0;
    }
    
    /**
     * 流程处理类型
     * @author mayongming
     *
     */
    public class WfProcDealType {
        /** 流程启动 */
        public static final String PROC_START = "start";
        /** 流程启动并提交 */
        public static final String PROC_COMMIT = "commit";
        /** 流程任务审批 */
        public static final String PROC_APPROVE = "approve";
        /** 流程任务签收 */
        public static final String PROC_CLAIM = "claim";
        /** 流程任务取消签收 */
        public static final String PROC_UNCLAIM = "unclaim";
        /** 流程任务删除 */
        public static final String PROC_DELETE = "delete";
        /** 流程任务取消 */
        public static final String PROC_CANCEL = "cancel";
        /** 流程任务撤回 */
        public static final String PROC_RETRIEVE = "retrieve";
        /** 流程任务终止 */
        public static final String PROC_END = "end";
    }
    
    /**
     * 工作流流程任务属性定义
     * @author mayongming
     *
     */
    public class WfProcTaskProperty {
        /** 流程任务属性 -- 页面URL */
        public static final String PROC_URL = "taskUrl";
        /** 流程任务属性 -- 已办任务查看页面URL */
        public static final String PROC_DISPLAYURL = "displayUrl";
        /** 流程任务属性 -- 流程查询详情页面URL */
        public static final String PROC_DETAILURL = "detailUrl";
        /** 流程任务属性 -- 拒绝流程任务节点 */
        public static final String PROC_REFUSETASK = "refuseTask";
        /** 流程任务属性 -- 任务提交前和提交后的回调接口类 */
        public static final String PROC_CALLBACK = "callback";
        /** 流程任务属性 -- 数据权限类型 0:不控制;1:本机构;2:上级机构;3:本机构及其上级;9:本人 */
        public static final String PROC_DATAPERMISSION = "dataPermission";
        /** 流程任务属性 -- 流程任务节点是否可以撤回 */
        public static final String PROC_RETRIEVE = "retrieve";
        /** 流程任务属性 -- 是否参与决策 1:是;0:否 */
        public static final String PROC_VOTETASK = "voteTask";
        /** 流程任务属性 -- 特殊决策权 0:无;1:一票通过;2:一票否决 */
        public static final String PROC_VOTEPOWER = "votePower";
        /** 流程任务属性 -- 投票规则 1:绝对票数;2:百分比  */
        public static final String PROC_VOTERULE = "voteRule";
        /** 流程任务属性 -- 投票权重，大于0且不超过100的数字 */
        public static final String PROC_VOTEWEIGHT = "voteWeight";
        /** 流程任务属性 -- 投票阈值，投票规则为绝对票数时为正整数，投票规则为百分比时为大于0且不超过100的数字  */
        public static final String PROC_VOTETHRESHOLD = "voteThreshold";
        /** 流程任务属性 -- 是否速决，如产生决策结果时其他任务是否不再表决，1:是;0:否 */
        public static final String PROC_VOTEQUICKLY = "voteQuickly";
        /** 流程任务属性 -- 是否发送流程通知，1:是;0:否 */
        public static final String PROC_ISNOTIFY = "isNotify";
        /** 流程任务属性 -- 流程通知关键字 */
        public static final String PROC_NOTIFYKEY = "notifyKey";
        /** 流程任务属性 -- 流程任务通知指定用户-短信方式，多个手机号用','隔开 */
        public static final String PROC_NOTIFYUSERSBYSMS = "notifyUsersBySms";
        /** 流程任务属性 -- 流程任务通知指定用户-邮件方式，多个邮件地址用','隔开 */
        public static final String PROC_NOTIFYUSERSBYMAIL = "notifyUsersByMail";
        /** 流程任务属性 -- 流程任务通知指定用户-内部消息，多个内部用户用','隔开 */
        public static final String PROC_NOTIFYUSERSBYINNMSG = "notifyUsersByInnMsg";
        /** 流程任务属性 -- 流程任务通知指定用户-微信方式，多个微信号用','隔开 */
        public static final String PROC_NOTIFYUSERSBYWECHAT = "notifyUsersByWechat";
    }
    
    /**
     * 流程任务处理时的用户认证方式
     * @author mayongming
     *
     */
    public class WfUserAuthType {
        /** 流程任务处理用户认证方式 -- token */
        public static final String PROC_AUTH_TOKEN = "1";
        /** 流程任务处理用户认证方式 -- session */
        public static final String PROC_AUTH_SESSION = "2";
    }
    
    /**
     * 数据有效性标识
     * @author mayongming
     *
     */
    public class WfDataValid {
        /** 有效数据 */
        public static final String PROC_DATA_VALID = "1";
        /** 无效数据 */
        public static final String PROC_DATA_INVALID = "0";
    }
    
    /**
     * 工作流任务数据权限类型
     * @author mayongming
     *
     */
    public enum WfProcDataPermissionType {
        /** 不进行数据权限控制 */
        NONE("0","不控制"),
        /** 通过机构进行数据权限控制 */
        SELF("1","本机构"),
        /** 通过上级机构进行数据权限控制 */
        SUPER("2","上级机构"),
        /** 通过自身和上级机构进行数据权限控制 */
        INHERIT("3","本机构及其上级"),
        /** 通过本人进行数据权限控制 */
        ONESELF("9","本人");
        
        /** 响应代码 **/
        private String retCode = "";
        /** 提示信息 **/
        private String retInfo = "";

        private WfProcDataPermissionType() {

        }

        private WfProcDataPermissionType(String retCode, String retInfo) {
            this.retCode = retCode;
            this.retInfo = retInfo;
        }

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetInfo() {
            return retInfo;
        }

        public void setRetInfo(String retInfo) {
            this.retInfo = retInfo;
        }
    }

    /**
     * 工作流任务属性-投票特殊决策权
     * @author mayongming
     *
     */
    public enum WfProcVotePower {
        NONE("0","无"),
        PASS("1","一票通过"),
        VETO("2","一票否决");
        
        /** 响应代码 **/
        private String retCode = "";
        /** 提示信息 **/
        private String retInfo = "";

        private WfProcVotePower() {

        }

        private WfProcVotePower(String retCode, String retInfo) {
            this.retCode = retCode;
            this.retInfo = retInfo;
        }

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetInfo() {
            return retInfo;
        }

        public void setRetInfo(String retInfo) {
            this.retInfo = retInfo;
        }
    }
    
    /**
     * 流程任务属性 -- 投票规则
     * @author mayongming
     *
     */
    public enum WfProcVoteRole {
        VOTECOUNT("1","绝对票数"),
        PERCENTAGE("2","百分比");
        
        /** 响应代码 **/
        private String retCode = "";
        /** 提示信息 **/
        private String retInfo = "";

        private WfProcVoteRole() {

        }

        private WfProcVoteRole(String retCode, String retInfo) {
            this.retCode = retCode;
            this.retInfo = retInfo;
        }

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetInfo() {
            return retInfo;
        }

        public void setRetInfo(String retInfo) {
            this.retInfo = retInfo;
        }
    }
    
    /**
     * 流程任务属性 -- 投票规则
     * @author mayongming
     *
     */
    public enum WfProcParallStatus {
        APPROVED("1","已审批"),
        NOTAPPROVED("0","未审批");
        
        /** 响应代码 **/
        private String retCode = "";
        /** 提示信息 **/
        private String retInfo = "";

        private WfProcParallStatus() {

        }

        private WfProcParallStatus(String retCode, String retInfo) {
            this.retCode = retCode;
            this.retInfo = retInfo;
        }

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetInfo() {
            return retInfo;
        }

        public void setRetInfo(String retInfo) {
            this.retInfo = retInfo;
        }
    }
    
    public enum FlowStatus {
        //业务状态
        FLOW01("flowsuspend","流程挂起"),
        FLOW02("flowactive","流程激活"),
        FLOW03("stop","终止"),
        FLOW04("del","删除"),
        FLOW05("instsuspend","流程实例挂起"),
        FLOW06("instactive","流程实例激活"),
        
        //流程状态
        PROC10("0","审批中"),
        PROC13("3","已暂停"),
        PROC14("4","已终止"),
        PROC15("5","已删除"),
        PROC16("6","已完成"),
        PROC17("7","已取消"),
        //任务状态
        TASK01("1","待签收"),
        TASK02("2","待处理"),
        TASK03("3","已处理"),
        TASK04("4","已终止"),
        //审核状态
        CHECK01("1","通过"),
        CHECK02("2","退回"),
        CHECK03("3","删除"),
        CHECK04("4","取消"),
        CHECK05("5","终止"),
        CHECK00("0","拒绝");
        
        /** 响应代码 **/
        private String retCode = "";
        /** 提示信息 **/
        private String retInfo = "";

        private FlowStatus() {

        }

        private FlowStatus(String retCode, String retInfo) {
            this.retCode = retCode;
            this.retInfo = retInfo;
        }

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetInfo() {
            return retInfo;
        }

        public void setRetInfo(String retInfo) {
            this.retInfo = retInfo;
        }
    }
    
    public enum ProcUserType {
        //用户状态
        USERTYPE01("1","流程创建者"),
        USERTYPE02("2","流程参与者"),
        USERTYPE03("3","流程订阅者"),
        USERTYPE04("4","流程委托人"),
        USERTYPE05("5","流程受托人");
        
        /** 响应代码 **/
        private String retCode = "";
        /** 提示信息 **/
        private String retInfo = "";

        private ProcUserType() {

        }

        private ProcUserType(String retCode, String retInfo) {
            this.retCode = retCode;
            this.retInfo = retInfo;
        }

        public String getRetCode() {
            return retCode;
        }

        public void setRetCode(String retCode) {
            this.retCode = retCode;
        }

        public String getRetInfo() {
            return retInfo;
        }

        public void setRetInfo(String retInfo) {
            this.retInfo = retInfo;
        }
    }
    
    public enum ProcToken {
        PROC_TOKEN_100("f53674938794c432e1021584ffd963a6", "331493b0b9d8815135f6361bd1f83a7c", "以租代售");

        /** token user **/
        private String tokenUser = "";
        
        /** token pass **/
        private String tokenPass = "";
        
        /** token使用者 **/
        private String systemName = "";

        private ProcToken(String tokenUser, String tokenPass, String systemName) {
            this.tokenUser = tokenUser;
            this.tokenPass = tokenPass;
            this.systemName = systemName;
        }

        public String getTokenUser() {
            return tokenUser;
        }

        public String getTokenPass() {
            return tokenPass;
        }
        
        public String getSystemName() {
            return systemName;
        }
        
        public boolean exist(String tokenCode) {
            ProcToken.valueOf(tokenCode);
            return true;
        }
    }
}
