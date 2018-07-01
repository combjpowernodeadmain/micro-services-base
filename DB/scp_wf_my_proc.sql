/*
Navicat MySQL Data Transfer

Source Server         : 中阁测试库
Source Server Version : 50537
Source Host           : 47.94.247.233:3306
Source Database       : consumer_instbiz

Target Server Type    : MYSQL
Target Server Version : 50537
File Encoding         : 65001

Date: 2018-06-11 12:06:53
*/

Use scp_workflow;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for scp_wf_my_proc
-- ----------------------------
DROP TABLE IF EXISTS `SCP_WF_MY_PROC`;
CREATE TABLE `SCP_WF_MY_PROC` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROC_INST_ID` varchar(64) DEFAULT NULL COMMENT '流程实例ID',
  `PROC_USER` varchar(32) DEFAULT NULL COMMENT '流程实例用户',
  `PROC_USER_TYPE` char(1) DEFAULT NULL COMMENT '1：流程创建者\r\n            2：流程参与者\r\n            3：流程订阅者\r\n            4：流程委托人\r\n            5：流程受托人',
  `PROC_TASKID` varchar(64) DEFAULT NULL COMMENT '流程任务ID',
  `PROC_TASKCODE` varchar(255) DEFAULT NULL COMMENT '流程任务代码',
  `PROC_TASKNAME` varchar(255) DEFAULT NULL COMMENT '流程任务名称',
  `PROC_DISPLAYURL` varchar(512) DEFAULT NULL COMMENT '查看页面',
  PRIMARY KEY (`ID`),
  KEY `IDX_MYPROC_PROC_ID` (`PROC_INST_ID`),
  KEY `IDX_MYPROC_USER` (`PROC_USER`),
  CONSTRAINT `FK_WF_MY_PROC` FOREIGN KEY (`PROC_INST_ID`) REFERENCES `scp_wf_proc` (`PROC_INST_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1723 DEFAULT CHARSET=utf8 COMMENT='记录每个用户可查看的流程列表';

-- ----------------------------
-- Table structure for scp_wf_proc
-- ----------------------------
DROP TABLE IF EXISTS `SCP_WF_PROC`;
CREATE TABLE `SCP_WF_PROC` (
  `PROC_INST_ID` varchar(64) NOT NULL COMMENT '流程实例ID',
  `PROC_ID` varchar(64) NOT NULL COMMENT '流程定义ID',
  `PROC_KEY` varchar(255) NOT NULL COMMENT '流程定义代码',
  `PROC_NAME` varchar(255) DEFAULT NULL COMMENT '流程定义名称',
  `PROC_VERSION` int(11) DEFAULT NULL COMMENT '流程版本号',
  `PROC_CREATOR` varchar(32) NOT NULL COMMENT '流程实例创建人代码',
  `PROC_CREATETIME` int(11) NOT NULL COMMENT '流程实例创建时间',
  `PROC_ENDTIME` int(11) DEFAULT NULL COMMENT '流程实例处理结束时间',
  `PROC_BIZID` varchar(64) NOT NULL COMMENT '流程实例关联的业务ID',
  `PROC_BIZTYPE` varchar(32) DEFAULT NULL COMMENT '流程实例关联的业务分类',
  `PROC_ORGCODE` varchar(32) NOT NULL COMMENT '流程关联业务所属机构',
  `PROC_MEMO` varchar(256) DEFAULT NULL COMMENT '流程实例关联的流程摘要信息',
  `PROC_STATUS` char(1) NOT NULL COMMENT '流程实例关联的流程状态\r\n            0：审批中\r\n            3：已暂停\r\n            4：已终止\r\n            5：已删除\r\n            6：已完成\r\n            7：已取消',
  `PROC_DISPLAYURL` varchar(512) DEFAULT NULL COMMENT '查看页面URL',
  PRIMARY KEY (`PROC_INST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务流程表';

-- ----------------------------
-- Table structure for scp_wf_proc_delegate
-- ----------------------------
DROP TABLE IF EXISTS `SCP_WF_PROC_DELEGATE`;
CREATE TABLE `SCP_WF_PROC_DELEGATE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROC_INST_ID` varchar(64) NOT NULL COMMENT '流程实例ID',
  `PROC_LICENSOR` varchar(32) NOT NULL COMMENT '流程实例委托授权用户',
  `PROC_LICENSOR_ROLE` varchar(255) NOT NULL COMMENT '授权人角色',
  `PROC_MANDATARY` varchar(32) NOT NULL COMMENT '委托授权人委托处理流程的用户',
  `PROC_LICENSE_TIME` int(11) DEFAULT NULL COMMENT '流程实例委托时间',
  `PROC_CANCEL_TIME` int(11) DEFAULT NULL COMMENT '流程实例委托取消时间',
  `PROC_LICENSE_ISVALID` char(1) DEFAULT NULL COMMENT '流程实例委托是否有效\r\n            1：是\r\n            0：否',
  PRIMARY KEY (`ID`),
  KEY `IDX_PROC_INST_ID` (`PROC_INST_ID`),
  CONSTRAINT `FK_WF_PROC_DELEGATE` FOREIGN KEY (`PROC_INST_ID`) REFERENCES `scp_wf_proc` (`PROC_INST_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务流程实例委托授权表';

-- ----------------------------
-- Table structure for scp_wf_proc_props
-- ----------------------------
DROP TABLE IF EXISTS `SCP_WF_PROC_PROPS`;
CREATE TABLE `SCP_WF_PROC_PROPS` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROC_ID` varchar(255) DEFAULT NULL COMMENT '流程代码',
  `PROC_TASK_CODE` varchar(255) DEFAULT NULL COMMENT '流程任务编号',
  `PROC_PROPS_TYPE` char(1) DEFAULT NULL COMMENT '流程属性类型\r\n            1：流程属性\r\n            2：流程任务属性',
  `PROC_PROPS_KEY` varchar(32) DEFAULT NULL COMMENT '流程属性key',
  `PROC_PROPS_VALUE` varchar(256) DEFAULT NULL COMMENT '流程属性value',
  `PROC_PROPS_CREATOR` varchar(32) DEFAULT NULL COMMENT '流程属性创建人',
  `PROC_PROPS_CREATETIME` int(11) DEFAULT NULL COMMENT '流程属性创建时间',
  `PROC_PROPS_UPDATOR` varchar(32) DEFAULT NULL COMMENT '流程属性最后更新人',
  `PROC_PROPS_UPDATETIME` int(11) DEFAULT NULL COMMENT '流程属性最后更新时间',
  `PROC_PROPS_VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `IDX_PROPS_PROC_ID` (`PROC_ID`),
  KEY `IDX_PROPS_TASK_CODE` (`PROC_ID`,`PROC_TASK_CODE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程属性定义表';

-- ----------------------------
-- Table structure for scp_wf_proc_task
-- ----------------------------
DROP TABLE IF EXISTS `SCP_WF_PROC_TASK`;
CREATE TABLE `SCP_WF_PROC_TASK` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PROC_INST_ID` varchar(64) NOT NULL COMMENT '流程实例ID',
  `PROC_ID` varchar(64) NOT NULL COMMENT '流程定义ID',
  `PROC_KEY` varchar(255) NOT NULL COMMENT '流程定义代码',
  `PROC_PTASKID` varchar(64) DEFAULT NULL COMMENT '流程实例上一任务ID',
  `PROC_PTASKCODE` varchar(255) DEFAULT NULL COMMENT '流程实例上一任务代码',
  `PROC_PTASKNAME` varchar(255) DEFAULT NULL COMMENT '流程实例上一任务名称',
  `PROC_TASK_COMMITTER` varchar(32) NOT NULL COMMENT '流程实例上一任务提交人',
  `PROC_TASK_COMMITTIME` int(11) NOT NULL COMMENT '流程实例上一任务提交时间',
  `PROC_CTASKID` varchar(64) NOT NULL COMMENT '流程实例当前任务ID',
  `PROC_CTASKCODE` varchar(255) NOT NULL COMMENT '流程实例当前任务代码',
  `PROC_CTASKNAME` varchar(255) NOT NULL COMMENT '流程实例当前任务名称',
  `PROC_EXECUTIONID` varchar(64) DEFAULT NULL COMMENT '流程任务执行ID',
  `PROC_TASK_GROUP` varchar(32) DEFAULT NULL COMMENT '流程实例当前任务处理的候选角色',
  `PROC_TASK_PROPERTIES` varchar(1024) DEFAULT NULL COMMENT '流程任务节点配置的表单数据',
  `PROC_DATAPERMISSION` char(1) NOT NULL COMMENT '流程任务数据权限类型\r\n0:无\r\n1:机构\r\n2:上级机构\r\n3: 机构及其上级',
  `PROC_APPOINT_USERS` varchar(512) DEFAULT NULL COMMENT '流程实例上一任务指定的处理人列表',
  `PROC_LICENSOR` varchar(32) DEFAULT NULL COMMENT '该流程实例委托授权用户',
  `PROC_MANDATARY` varchar(32) DEFAULT NULL COMMENT '委托授权人委托的该流程实例的处理用户',
  `PROC_TASK_ASSIGNEE` varchar(32) DEFAULT NULL COMMENT '流程实例当前任务签收用户',
  `PROC_TASK_ASSIGNTIME` int(11) DEFAULT NULL COMMENT '流程实例当前任务用户签收时间',
  `PROC_TASK_ENDTIME` int(11) DEFAULT NULL COMMENT '流程时间任务处理完成时间',
  `PROC_TASK_APPR_STATUS` char(1) DEFAULT NULL COMMENT '1：通过\r\n2：退回\r\n0：拒绝',
  `PROC_TASK_APPR_OPINION` varchar(512) DEFAULT NULL COMMENT '流程实例任务审批意见',
  `PROC_REFUSETASK` char(1) DEFAULT NULL COMMENT '是否回退任务\r\n0:否\r\n1:是',
  `PROC_TASK_STATUS` char(1) NOT NULL COMMENT '流程实例关联的流程状态\r\n            1：待签收\r\n            2：待处理\r\n            3：已处理\r\n            4：已终止',
  `PROC_APPROVEURL` varchar(512) DEFAULT NULL COMMENT '审批页面URL',
  `PROC_PARALLEL` char(1) DEFAULT NULL COMMENT '是否并发任务 1:是;0:否',
  `PROC_PARALLEL_STATUS` char(1) DEFAULT NULL COMMENT '并行审批状态 1:已审批;0:未审批',
  `PROC_VOTETASK` char(1) DEFAULT NULL COMMENT '是否投票决策 1:是;0:否',
  `PROC_VOTEPOWER` char(1) DEFAULT NULL COMMENT '特殊决策权 0:无;1:一票通过;2:一票否决',
  `PROC_VOTERULE` char(1) DEFAULT NULL COMMENT '投票规则 1:绝对票数;2:百分比',
  `PROC_VOTEWEIGHT` float(7,2) DEFAULT NULL COMMENT '投票权重，大于0且不超过100的数字',
  `PROC_VOTETHRESHOLD` float(7,2) DEFAULT NULL COMMENT '投票阈值，投票规则为绝对票数时为正整数，投票规则为百分比时为大于0且不超过100的数字',
  `PROC_VOTEQUICKLY` char(1) DEFAULT NULL COMMENT '是否速决，如产生决策结果时其他任务是否不再表决，1:是;0:否',
  PRIMARY KEY (`ID`),
  KEY `FK_WF_PROC_TASK` (`PROC_INST_ID`),
  CONSTRAINT `FK_WF_PROC_TASK` FOREIGN KEY (`PROC_INST_ID`) REFERENCES `scp_wf_proc` (`PROC_INST_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6212 DEFAULT CHARSET=utf8;
