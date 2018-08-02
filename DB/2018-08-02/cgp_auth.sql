/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.22 : Database - scp_auth
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`scp_auth` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `scp_auth`;

/*Table structure for table `auth_client` */

DROP TABLE IF EXISTS `auth_client`;

CREATE TABLE `auth_client` (
  `id` varchar(36) NOT NULL,
  `code` varchar(255) DEFAULT NULL COMMENT '服务编码',
  `secret` varchar(255) DEFAULT NULL COMMENT '服务密钥',
  `name` varchar(255) DEFAULT NULL COMMENT '服务名',
  `locked` char(1) DEFAULT NULL COMMENT '是否锁定',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `crt_user` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_name` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `crt_host` varchar(255) DEFAULT NULL COMMENT '创建主机',
  `upd_time` datetime DEFAULT NULL COMMENT '更新时间',
  `upd_user` varchar(255) DEFAULT NULL COMMENT '更新人',
  `upd_name` varchar(255) DEFAULT NULL COMMENT '更新姓名',
  `upd_host` varchar(255) DEFAULT NULL COMMENT '更新主机',
  `attr1` varchar(255) DEFAULT NULL,
  `attr2` varchar(255) DEFAULT NULL,
  `attr3` varchar(255) DEFAULT NULL,
  `attr4` varchar(255) DEFAULT NULL,
  `attr5` varchar(255) DEFAULT NULL,
  `attr6` varchar(255) DEFAULT NULL,
  `attr7` varchar(255) DEFAULT NULL,
  `attr8` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_client` */

insert  into `auth_client`(`id`,`code`,`secret`,`name`,`locked`,`description`,`crt_time`,`crt_user`,`crt_name`,`crt_host`,`upd_time`,`upd_user`,`upd_name`,`upd_host`,`attr1`,`attr2`,`attr3`,`attr4`,`attr5`,`attr6`,`attr7`,`attr8`) values ('1','scp-gate','123456','scp-gate','0','服务网关',NULL,'','','','2017-07-07 21:51:32','1','管理员','0:0:0:0:0:0:0:1','','','','','','','',''),('18','scp-transaction','123456','scp-transaction','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('20','scp-dict','123566','scp-dict','0','数据字典服务',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('21','scp-demo-depart-data','123456','scp-demo-depart-data','0','测试服务',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('22','scp-workflow','123456','scp-workflow','0','工作流服务',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('3','scp-admin','123456','scp-admin','0','',NULL,NULL,NULL,NULL,'2017-07-06 21:42:17','1','管理员','0:0:0:0:0:0:0:1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('6','scp-auth','123456','scp-auth','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('7','scp-tool','123456','scp-tool','0',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('fa41642d02f84d8fbcf523b6aacb65c3','scp-cgp','cpg98siu','scp-cgp','0','综合执法微服务',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `auth_client_service` */

DROP TABLE IF EXISTS `auth_client_service`;

CREATE TABLE `auth_client_service` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(255) DEFAULT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `crt_user` varchar(255) DEFAULT NULL,
  `crt_name` varchar(255) DEFAULT NULL,
  `crt_host` varchar(255) DEFAULT NULL,
  `attr1` varchar(255) DEFAULT NULL,
  `attr2` varchar(255) DEFAULT NULL,
  `attr3` varchar(255) DEFAULT NULL,
  `attr4` varchar(255) DEFAULT NULL,
  `attr5` varchar(255) DEFAULT NULL,
  `attr6` varchar(255) DEFAULT NULL,
  `attr7` varchar(255) DEFAULT NULL,
  `attr8` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

/*Data for the table `auth_client_service` */

insert  into `auth_client_service`(`id`,`service_id`,`client_id`,`description`,`crt_time`,`crt_user`,`crt_name`,`crt_host`,`attr1`,`attr2`,`attr3`,`attr4`,`attr5`,`attr6`,`attr7`,`attr8`) values (21,'4','5',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(43,'3','16',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(45,'12','16',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(46,'18','18',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(53,'3','6',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(61,'3','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(62,'6','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(63,'20','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(65,'3','21',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(66,'3','22',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(67,'1','fa41642d02f84d8fbcf523b6aacb65c3',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(68,'fa41642d02f84d8fbcf523b6aacb65c3','1',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(69,'fa41642d02f84d8fbcf523b6aacb65c3','6',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(70,'fa41642d02f84d8fbcf523b6aacb65c3','3',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(71,'3','fa41642d02f84d8fbcf523b6aacb65c3',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `gateway_route` */

DROP TABLE IF EXISTS `gateway_route`;

CREATE TABLE `gateway_route` (
  `id` varchar(50) NOT NULL,
  `path` varchar(255) NOT NULL COMMENT '映射路劲',
  `service_id` varchar(50) DEFAULT NULL COMMENT '映射服务',
  `url` varchar(255) DEFAULT NULL COMMENT '映射外连接',
  `retryable` tinyint(1) DEFAULT NULL COMMENT '是否重试',
  `enabled` tinyint(1) NOT NULL COMMENT '是否启用',
  `strip_prefix` tinyint(1) DEFAULT NULL COMMENT '是否忽略前缀',
  `crt_user_name` varchar(255) DEFAULT NULL COMMENT '创建人',
  `crt_user_id` varchar(36) DEFAULT NULL COMMENT '创建人ID',
  `crt_time` datetime DEFAULT NULL COMMENT '创建时间',
  `upd_user_name` varchar(255) DEFAULT NULL COMMENT '最后更新人',
  `upd_user_id` varchar(36) DEFAULT NULL COMMENT '最后更新人ID',
  `upd_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `gateway_route` */

insert  into `gateway_route`(`id`,`path`,`service_id`,`url`,`retryable`,`enabled`,`strip_prefix`,`crt_user_name`,`crt_user_id`,`crt_time`,`upd_user_name`,`upd_user_id`,`upd_time`) values ('admin','/admin/**','scp-admin',NULL,0,1,1,'Mr.AG','1','2018-02-25 14:33:30','Mr.AG','1','2018-02-25 14:38:31'),('auth','/auth/**','scp-auth',NULL,0,1,1,NULL,NULL,NULL,'Mr.AG','1','2018-02-25 14:29:51'),('center','/center/**','scp-center',NULL,0,1,1,'Mr.AG','1','2018-02-26 12:50:51','Mr.AG','1','2018-02-26 12:50:51'),('cgp','/cgp/**','scp-cgp',NULL,0,1,1,NULL,NULL,'2018-07-05 13:58:08',NULL,'1','2018-07-05 13:58:08'),('dict','/dict/**','scp-dict',NULL,0,1,1,NULL,NULL,NULL,'Mr.AG','1','2018-02-25 14:41:07'),('tool','/tool/**','scp-tool',NULL,0,1,1,NULL,NULL,'2018-04-02 21:04:47',NULL,NULL,'2018-04-02 21:04:52'),('workflow','/workflow/**','scp-workflow','',0,1,1,NULL,NULL,'2018-04-05 13:58:08','Mr.AG','1','2018-06-14 17:47:23');

/*Table structure for table `oauth_client_details` */

DROP TABLE IF EXISTS `oauth_client_details`;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(256) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  `description` varchar(256) DEFAULT NULL,
  `crt_user_name` varchar(255) DEFAULT NULL,
  `crt_user_id` varchar(36) DEFAULT NULL,
  `crt_time` datetime DEFAULT NULL,
  `upd_user_name` varchar(255) DEFAULT NULL,
  `upd_user_id` varchar(36) DEFAULT NULL,
  `upd_time` datetime DEFAULT NULL,
  `is_deleted` char(1) DEFAULT NULL,
  `is_disabled` char(1) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

/*Data for the table `oauth_client_details` */

insert  into `oauth_client_details`(`client_id`,`resource_ids`,`client_secret`,`scope`,`authorized_grant_types`,`web_server_redirect_uri`,`authorities`,`access_token_validity`,`refresh_token_validity`,`additional_information`,`autoapprove`,`description`,`crt_user_name`,`crt_user_id`,`crt_time`,`upd_user_name`,`upd_user_id`,`upd_time`,`is_deleted`,`is_disabled`) values ('client','','client','read','password,refresh_token,authorization_code','http://localhost:4040/sso/login',NULL,3600,2592000,'{}','true',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),('mobile',NULL,'mobile123','read','password,refresh_token,authorization_code','http://bjzhianjia.com/#/',NULL,14400,2592000,NULL,'true',NULL,'管理员','1','2018-07-29 16:33:43','管理员','1','2018-07-29 17:25:55',NULL,NULL),('vue',NULL,'vue','read','password,refresh_token','http://localhost:9527/#/',NULL,14400,2592000,'{}','true','',NULL,NULL,NULL,'Mr.AG','1','2018-03-28 20:43:14',NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
