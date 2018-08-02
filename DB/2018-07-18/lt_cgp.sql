/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.22 : Database - lt_cgp
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`lt_cgp` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `lt_cgp`;

/*Table structure for table `area_grid` */

DROP TABLE IF EXISTS `area_grid`;

CREATE TABLE `area_grid` (
  `id` int(11) NOT NULL,
  `grid_code` varchar(32) DEFAULT NULL COMMENT '网格编号',
  `grid_name` varchar(100) DEFAULT NULL COMMENT '网格名称',
  `grid_level` tinyint(4) DEFAULT NULL COMMENT '网格等级',
  `grid_parent` int(32) DEFAULT NULL COMMENT '上级网格',
  `grid_team` int(11) DEFAULT NULL COMMENT '执法队伍',
  `grid_numbers` tinyint(1) DEFAULT NULL COMMENT '网格员数量',
  `grid_household` tinyint(4) DEFAULT NULL COMMENT '网格户数',
  `grid_persons` int(11) DEFAULT NULL COMMENT '网格人数',
  `grid_areas` float(18,2) DEFAULT NULL COMMENT '网格面积(平方米)',
  `grid_range` varchar(512) DEFAULT NULL COMMENT '网格范围',
  `mgr_dept` varchar(36) DEFAULT NULL COMMENT '管理部门',
  `map_info` text COMMENT '地图信息',
  `is_deleted` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除；1：是；0: 否',
  `is_disabled` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否禁用；1：是；0：否',
  `crt_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_user_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '最后更新人',
  `upd_user_id` varchar(36) CHARACTER SET utf8 DEFAULT NULL COMMENT '最后更新人ID',
  `upd_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格';

/*Data for the table `area_grid` */

/*Table structure for table `area_grid_member` */

DROP TABLE IF EXISTS `area_grid_member`;

CREATE TABLE `area_grid_member` (
  `id` int(11) NOT NULL,
  `grid_id` int(11) DEFAULT NULL COMMENT '网格ID',
  `grid_member` int(11) DEFAULT NULL COMMENT '网格成员',
  `grid_role` varchar(36) DEFAULT NULL COMMENT '成员所属岗位',
  `is_deleted` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除；1：是；0: 否',
  `is_disabled` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否禁用；1：是；0: 否',
  `crt_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_user_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '最后更新人',
  `upd_user_id` varchar(36) CHARACTER SET utf8 DEFAULT NULL COMMENT '最后更新人ID',
  `upd_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格成员';

/*Data for the table `area_grid_member` */

/*Table structure for table `dept_biztype` */

DROP TABLE IF EXISTS `dept_biztype`;

CREATE TABLE `dept_biztype` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `department` varchar(36) DEFAULT NULL COMMENT '部门',
  `biz_type` varchar(36) DEFAULT NULL COMMENT '业务条线',
  `is_deleted` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否删除；1：是；0: 否',
  `is_disabled` char(1) CHARACTER SET utf8 NOT NULL DEFAULT '0' COMMENT '是否禁用；1：是；0: 否',
  `crt_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_user_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '最后更新人',
  `upd_user_id` varchar(36) CHARACTER SET utf8 DEFAULT NULL COMMENT '最后更新人ID',
  `upd_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

/*Data for the table `dept_biztype` */

/*Table structure for table `enforce_certificate` */

DROP TABLE IF EXISTS `enforce_certificate`;

CREATE TABLE `enforce_certificate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `holder_name` varchar(50) DEFAULT NULL COMMENT '持证人姓名',
  `cert_type` varchar(36) DEFAULT NULL COMMENT '证件类型',
  `cert_code` varchar(255) DEFAULT NULL COMMENT '证件编号',
  `valid_start` date NOT NULL COMMENT '证件有效期起始',
  `valid_end` date NOT NULL COMMENT '证件有效期终止',
  `usr_id` varchar(36) DEFAULT NULL COMMENT '持证人ID',
  `is_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除；1：是；0: 否',
  `is_disabled` char(1) NOT NULL DEFAULT '0' COMMENT '是否禁用；1：是；0：否',
  `crt_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_user_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_name` varchar(255) DEFAULT NULL COMMENT '最后更新人',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '最后更新人ID',
  `upd_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户ID',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `biz_lists` varchar(255) NOT NULL COMMENT '涵盖业务线',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='执法证管理';

/*Data for the table `enforce_certificate` */

/*Table structure for table `enforce_terminal` */

DROP TABLE IF EXISTS `enforce_terminal`;

CREATE TABLE `enforce_terminal` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `terminal_code` varchar(255) DEFAULT NULL COMMENT '终端标识号',
  `terminal_phone` varchar(30) DEFAULT NULL COMMENT '终端手机号',
  `retrieval_department` varchar(36) DEFAULT NULL COMMENT '领用部门',
  `retrieval_user` varchar(36) DEFAULT NULL COMMENT '领用人',
  `terminal_type` varchar(36) DEFAULT NULL COMMENT '属配类型',
  `crt_user_id` varchar(36) NOT NULL COMMENT '创建用户Id',
  `crt_user_name` varchar(64) NOT NULL COMMENT '创建用户姓名',
  `crt_time` datetime NOT NULL COMMENT '创建时间',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '更新用户ID',
  `upd_user_name` varchar(64) DEFAULT NULL COMMENT '更新用户姓名',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `is_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除（1是/0否）',
  `is_enable` varchar(36) DEFAULT NULL COMMENT '是否可用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='执法终端';

/*Data for the table `enforce_terminal` */

insert  into `enforce_terminal`(`id`,`terminal_code`,`terminal_phone`,`retrieval_department`,`retrieval_user`,`terminal_type`,`crt_user_id`,`crt_user_name`,`crt_time`,`upd_time`,`upd_user_id`,`upd_user_name`,`tenant_id`,`depart_id`,`is_deleted`,`is_enable`) values (1,'bbb','12342342342','fe3f79bcf2c540559156275cd801843b','a4da7641babe4436830a83812b6febca','8d4e1c6fa40842e88cee773e3b5e3d57','5b29e88e363c41c394ad50cf830f6e0a','孙丽华','2018-07-10 16:37:14','2018-07-10 16:53:09','5b29e88e363c41c394ad50cf830f6e0a','孙丽华','ac88ceb386aa4231b09bf472cb937c24',NULL,'0','c0b20c762aba41ba9ebd3f2fad6a6256'),(2,'aaa','12342342341','fe3f79bcf2c540559156275cd801843b','a4da7641babe4436830a83812b6febca','8d4e1c6fa40842e88cee773e3b5e3d57','5b29e88e363c41c394ad50cf830f6e0a','孙丽华','2018-07-11 17:40:49','2018-07-11 17:40:49','5b29e88e363c41c394ad50cf830f6e0a','孙丽华','ac88ceb386aa4231b09bf472cb937c24',NULL,'0','648eba56aba04fdbb5be553c27234ab9');

/*Table structure for table `enterprise_info` */

DROP TABLE IF EXISTS `enterprise_info`;

CREATE TABLE `enterprise_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `trade_regist_code` varchar(60) DEFAULT NULL COMMENT '工商注册号',
  `type_code` varchar(50) DEFAULT NULL COMMENT '企业类型',
  `credit_code` varchar(60) DEFAULT NULL COMMENT '统一社会信用代码',
  `established_date` date DEFAULT NULL COMMENT '成立时间',
  `regist_capital` float(18,2) DEFAULT NULL COMMENT '注册资本',
  `business_scope` varchar(255) DEFAULT NULL COMMENT '经营范围',
  `operating_start_date` date DEFAULT NULL COMMENT '经营有效期-起始时间',
  `operating_end_date` date DEFAULT NULL COMMENT '经营有效期-截至时间',
  `legal_represent` varchar(60) DEFAULT NULL COMMENT '法定代表人',
  `represent_phone` varchar(20) DEFAULT NULL COMMENT '法人电话',
  `certificate_type` varchar(36) DEFAULT NULL COMMENT '证件类型',
  `cerificate_code` varchar(36) DEFAULT NULL COMMENT '证件号码',
  `firefight_cert_code` varchar(36) DEFAULT NULL COMMENT '消防证件号',
  `firefight_release_time` date DEFAULT NULL COMMENT '消防证发证时间',
  `economic_code` varchar(36) DEFAULT NULL COMMENT '企业类型/经济性质',
  `industry_code` varchar(36) DEFAULT NULL COMMENT '所属行业',
  `status` varchar(36) DEFAULT NULL COMMENT '企业状态',
  `credit_level` varchar(36) DEFAULT NULL COMMENT '企业信用等级',
  `industry_scope` varchar(36) DEFAULT NULL COMMENT '企业所属范围',
  `business_scale` varchar(36) DEFAULT NULL COMMENT '企业规模',
  `employees_count` int(11) DEFAULT NULL COMMENT '从业人员数量',
  `laid_off_count` int(11) DEFAULT NULL COMMENT '下岗失业人员数量',
  `real_wages` float DEFAULT NULL COMMENT '实发工资总额',
  `average_salary` float DEFAULT NULL COMMENT '月平均工资',
  `social_insurance_count` float DEFAULT NULL COMMENT '参保人数',
  `crt_user_id` varchar(36) NOT NULL COMMENT '创建用户ID',
  `crt_user_name` varchar(64) NOT NULL COMMENT '创建人姓名',
  `crt_time` datetime NOT NULL COMMENT '创建时间',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '更新用户ID',
  `upd_user_name` varchar(64) DEFAULT NULL COMMENT '更新用户姓名',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `id_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除（1是/0否）',
  `id_disabled` char(1) NOT NULL DEFAULT '0' COMMENT '是否禁用（1是/0否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业信息';

/*Data for the table `enterprise_info` */

/*Table structure for table `event_type` */

DROP TABLE IF EXISTS `event_type`;

CREATE TABLE `event_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `type_code` varchar(60) NOT NULL COMMENT '事件类别编码',
  `type_name` varchar(255) NOT NULL COMMENT '事件类别名称',
  `biz_type` varchar(36) DEFAULT NULL COMMENT '所属业务条线',
  `is_enable` char(1) NOT NULL DEFAULT '0' COMMENT '是否可用；1：是；0: 否',
  `order` int(11) DEFAULT NULL COMMENT '排序',
  `is_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除；1：是；0: 否',
  `crt_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_user_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_name` varchar(255) DEFAULT NULL COMMENT '最后更新人',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '最后更新人ID',
  `upd_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户ID',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='事件类型';

/*Data for the table `event_type` */

/*Table structure for table `factory_info` */

DROP TABLE IF EXISTS `factory_info`;

CREATE TABLE `factory_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `trade_regist_code` varchar(60) DEFAULT NULL COMMENT '工商注册号',
  `type_code` varchar(50) DEFAULT NULL COMMENT '企业类型',
  `credit_code` varchar(60) DEFAULT NULL COMMENT '统一社会信用代码',
  `established_date` date DEFAULT NULL COMMENT '成立时间',
  `regist_capital` float(18,2) DEFAULT NULL COMMENT '注册资本',
  `business_scope` varchar(255) DEFAULT NULL COMMENT '经营范围',
  `operating_start_date` date DEFAULT NULL COMMENT '经营有效期-起始时间',
  `operating_end_date` date DEFAULT NULL COMMENT '经营有效期-截至时间',
  `legal_represent` varchar(60) DEFAULT NULL COMMENT '法定代表人',
  `represent_phone` varchar(20) DEFAULT NULL COMMENT '法人电话',
  `certificate_type` varchar(36) DEFAULT NULL COMMENT '证件类型',
  `cerificate_code` varchar(36) DEFAULT NULL COMMENT '证件号码',
  `firefight_cert_code` varchar(36) DEFAULT NULL COMMENT '消防证件号',
  `firefight_release_time` date DEFAULT NULL COMMENT '消防证发证时间',
  `economic_code` varchar(36) DEFAULT NULL COMMENT '企业类型/经济性质',
  `industry_code` varchar(36) DEFAULT NULL COMMENT '所属行业',
  `status` varchar(36) DEFAULT NULL COMMENT '企业状态',
  `credit_level` varchar(36) DEFAULT NULL COMMENT '企业信用等级',
  `industry_scope` varchar(36) DEFAULT NULL COMMENT '企业所属范围',
  `business_scale` varchar(36) DEFAULT NULL COMMENT '企业规模',
  `employees_count` int(11) DEFAULT NULL COMMENT '从业人员数量',
  `laid_off_count` int(11) DEFAULT NULL COMMENT '下岗失业人员数量',
  `real_wages` float DEFAULT NULL COMMENT '实发工资总额',
  `average_salary` float DEFAULT NULL COMMENT '月平均工资',
  `social_insurance_count` float DEFAULT NULL COMMENT '参保人数',
  `crt_user_id` varchar(36) NOT NULL COMMENT '创建用户ID',
  `crt_user_name` varchar(64) NOT NULL COMMENT '创建人姓名',
  `crt_time` datetime NOT NULL COMMENT '创建时间',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '更新用户ID',
  `upd_user_name` varchar(64) DEFAULT NULL COMMENT '更新用户姓名',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `id_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除（1是/0否）',
  `id_disabled` char(1) NOT NULL DEFAULT '0' COMMENT '是否禁用（1是/0否）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='经营单位';

/*Data for the table `factory_info` */

/*Table structure for table `inspect_items` */

DROP TABLE IF EXISTS `inspect_items`;

CREATE TABLE `inspect_items` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) DEFAULT NULL COMMENT '巡查事项编号',
  `name` varchar(255) DEFAULT NULL COMMENT '巡查事项名称',
  `type` varchar(36) DEFAULT NULL COMMENT '事件类别',
  `biz_type` varchar(36) DEFAULT NULL COMMENT '业务条线',
  `inspect_way` varchar(255) DEFAULT NULL COMMENT '巡查方式',
  `inspect_frenquency` int(11) DEFAULT NULL COMMENT '巡查频次',
  `special_campaign` varchar(255) DEFAULT NULL COMMENT '专项活动',
  `item_num` varchar(50) DEFAULT NULL COMMENT '权力事项编号',
  `is_enable` varchar(36) DEFAULT NULL COMMENT '是否可用',
  `is_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除；1：是；0: 否',
  `crt_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_user_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_name` varchar(255) DEFAULT NULL COMMENT '最后更新人',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '最后更新人ID',
  `upd_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='巡查事项';

/*Data for the table `inspect_items` */

/*Table structure for table `regula_object` */

DROP TABLE IF EXISTS `regula_object`;

CREATE TABLE `regula_object` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `obj_code` varchar(60) NOT NULL COMMENT '监管对象编码',
  `obj_name` varchar(255) NOT NULL COMMENT '监管对象名称',
  `obj_type` varchar(50) NOT NULL COMMENT '监管对象类型',
  `obj_id` int(11) NOT NULL COMMENT '监管对象ID',
  `obj_address` varchar(512) NOT NULL COMMENT '监管对象地址',
  `linkman` varchar(20) NOT NULL COMMENT '联系人',
  `linkman_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `introduction` varchar(255) NOT NULL COMMENT '简介',
  `remark` varchar(255) NOT NULL COMMENT '备注',
  `pic_before` varchar(255) NOT NULL COMMENT '办理前图片',
  `longitude` float NOT NULL COMMENT '经度',
  `gri_id` int(11) NOT NULL COMMENT '所属网格',
  `latitude` float NOT NULL COMMENT '纬度',
  `gatherer` varchar(36) NOT NULL COMMENT '采集人',
  `gather_time` datetime NOT NULL COMMENT '采集时间',
  `biz_list` varchar(120) NOT NULL COMMENT '业务条线',
  `event_list` varchar(120) NOT NULL COMMENT '事件类别',
  `map_info` varchar(255) NOT NULL COMMENT '地理信息',
  `is_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除；1：是；0: 否',
  `is_disabled` char(1) NOT NULL DEFAULT '0' COMMENT '是否禁用；1：是；0：否',
  `crt_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_user_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_name` varchar(255) DEFAULT NULL COMMENT '最后更新人',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '最后更新人ID',
  `upd_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户ID',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='监管对象';

/*Data for the table `regula_object` */

/*Table structure for table `rights_issues` */

DROP TABLE IF EXISTS `rights_issues`;

CREATE TABLE `rights_issues` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `code` varchar(32) DEFAULT NULL COMMENT '权利事项编号',
  `type` varchar(36) DEFAULT NULL COMMENT '事件类别',
  `unlawful_act` varchar(255) DEFAULT NULL COMMENT '违法行为',
  `break_rule` varchar(255) DEFAULT NULL COMMENT '违则',
  `break_rule_detail` text COMMENT '违则详情',
  `penalty` varchar(255) DEFAULT NULL COMMENT '罚则',
  `penalty_detail` text COMMENT '罚则详情',
  `discretionary_range` text COMMENT '自由裁量范围',
  `is_enable` varchar(36) DEFAULT NULL COMMENT '是否可用',
  `is_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除；1：是；0: 否',
  `crt_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_user_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_name` varchar(255) DEFAULT NULL COMMENT '最后更新人',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '最后更新人ID',
  `upd_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权利事项';

/*Data for the table `rights_issues` */

/*Table structure for table `self_merchants` */

DROP TABLE IF EXISTS `self_merchants`;

CREATE TABLE `self_merchants` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `credit_code` varchar(60) DEFAULT NULL COMMENT '社会信用代码',
  `owner_credit_code` varchar(20) DEFAULT NULL COMMENT '业主身份证号码',
  `regist_address` varchar(255) DEFAULT NULL COMMENT '注册地址',
  `responser_user_name` varchar(64) DEFAULT NULL COMMENT '实际负责人姓名',
  `responser_user_phone` varchar(20) DEFAULT NULL COMMENT '实际负责人手机号码',
  `business_area` float(18,2) DEFAULT NULL COMMENT '经营面积',
  `storage_area` float(18,2) DEFAULT NULL COMMENT '仓储面积',
  `major_equipment_info` varchar(255) DEFAULT NULL COMMENT '主要设备名称及台（套）数',
  `auxiliary_equipment_info` varchar(255) DEFAULT NULL COMMENT '辅助设备名称及台（套）数',
  `annual_rent` float(18,2) DEFAULT NULL COMMENT '年房屋及设备租金',
  `is_rented` char(1) DEFAULT NULL COMMENT '是否租赁经营 1:是 0：否',
  `business_scope` varchar(255) DEFAULT NULL COMMENT '经营范围及方式',
  `employees_count` int(11) DEFAULT NULL COMMENT '从业人员数量',
  `affiliated_market` varchar(255) DEFAULT NULL COMMENT '所属集贸市场',
  `crt_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crt_user_id` varchar(36) NOT NULL COMMENT '创建用户ID',
  `crt_user_name` varchar(64) NOT NULL COMMENT '创建用户姓名',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '更新用户id',
  `upd_user_name` varchar(64) DEFAULT NULL COMMENT '更新用户姓名',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户',
  `is_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1：是 0：否',
  `is_disabled` char(1) NOT NULL DEFAULT '0' COMMENT '是否禁用 1：是 0：否',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个体商户';

/*Data for the table `self_merchants` */

/*Table structure for table `vhcl_management` */

DROP TABLE IF EXISTS `vhcl_management`;

CREATE TABLE `vhcl_management` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `ternimal_id` int(11) DEFAULT NULL COMMENT '终端标识',
  `vehicle_num` varchar(20) DEFAULT NULL COMMENT '车辆牌号',
  `vehicle_type` varchar(36) DEFAULT NULL COMMENT '车辆类型',
  `department` varchar(36) DEFAULT NULL COMMENT '所属部门',
  `vehicle_desc` varchar(255) DEFAULT NULL COMMENT '车辆说明',
  `crt_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `crt_user_id` varchar(36) NOT NULL COMMENT '创建人ID',
  `crt_user_name` varchar(64) NOT NULL COMMENT '创建人姓名',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '更新人id',
  `upd_user_name` varchar(60) DEFAULT NULL COMMENT '更新用户姓名',
  `is_deleted` char(1) NOT NULL DEFAULT '0' COMMENT '是否删除 1：是 0：否',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户',
  `depart_id` varchar(36) DEFAULT NULL COMMENT '部门ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='车辆管理';

/*Data for the table `vhcl_management` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
