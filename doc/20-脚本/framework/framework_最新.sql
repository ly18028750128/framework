/*
MySQL Backup
Database: framework
Backup Time: 2020-08-13 08:14:59
*/

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_auth_type`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_data_interface`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_data_interface_params`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_data_rest_config`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_data_sp_config`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_data_sql_config`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_menu`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_menu_data`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_menu_resource`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_org`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_role`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_role_data`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_role_data_interface`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_role_menu`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_role_menu_resource`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_role_resource`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_user`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_user_auth`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_user_ref`;
DROP TABLE IF EXISTS `framework-jdy`.`t_frame_user_role`;
DROP TABLE IF EXISTS `framework-jdy`.`t_framework_resource2`;
DROP TABLE IF EXISTS `framework-jdy`.`t_framework_resource`;
DROP TABLE IF EXISTS `framework-jdy`.`t_microservice_register`;
DROP TABLE IF EXISTS `framework-jdy`.`t_system_dic_item`;
DROP TABLE IF EXISTS `framework-jdy`.`t_system_dic_master`;
CREATE TABLE `t_frame_auth_type` (
  `auth_type_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限类型ID',
  `auth_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限编码',
  `auth_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限类型：角色（role）/微服务权限（mircroservice）/组机机构（organization）/其它数据权限（可定义）',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`auth_type_id`) USING BTREE,
  UNIQUE KEY `uidx_f_auth_type_code` (`auth_code`) USING BTREE,
  KEY `ak_kid` (`auth_type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `t_frame_data_interface` (
  `data_interface_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'data_interface_id',
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据编号',
  `data_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据名称',
  `data_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据类型',
  `data_execute_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据执行服务名',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '描述',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`data_interface_id`) USING BTREE,
  UNIQUE KEY `ak_kid` (`data_interface_id`) USING BTREE,
  UNIQUE KEY `udx_data_interface_code` (`data_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='数据接口定义';
CREATE TABLE `t_frame_data_interface_params` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `seq` int(11) NOT NULL,
  `param_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_interface_id` bigint(20) DEFAULT NULL,
  `param_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `param_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `t_frame_data_rest_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_interface_id` bigint(20) DEFAULT NULL,
  `uri` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `method` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `must_login` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `login_bean` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='REST服务数据接口';
CREATE TABLE `t_frame_data_sp_config` (
  `id` bigint(20) NOT NULL,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_interface_id` bigint(20) DEFAULT NULL,
  `sql_content` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `data_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `database_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `data_dao_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`data_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='存储过程数据接口表';
CREATE TABLE `t_frame_data_sql_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_interface_id` bigint(20) DEFAULT NULL,
  `sql_content` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `data_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `database_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `data_dao_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `t_frame_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `menu_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `menu_url` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `component_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'vue组件位置',
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '0：目录 1：菜单',
  `show_type` int(10) unsigned NOT NULL DEFAULT '2' COMMENT '0：有子菜单时显示 1：有权限时显示 2：任何时候都显示',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '显示图标',
  `function_resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '权限编码：微服务application.name::resource_path::resource_name 例如：common-service::系统管理/数据字典::刷新缓存',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '1:有效 0:无效 -1:过期',
  `seq_no` int(11) NOT NULL DEFAULT '1',
  `parent_menu_id` bigint(20) DEFAULT NULL COMMENT '父级菜单ID',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `meta_data` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '路由meta参数，必须为json字符串',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE KEY `ui_framework_menu_1` (`menu_code`) USING BTREE,
  UNIQUE KEY `udx_framework_menu_1` (`menu_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统菜单';
CREATE TABLE `t_frame_menu_data` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`menu_code`,`data_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE,
  KEY `ak_kid2` (`id`) USING BTREE,
  KEY `fk_reference_35` (`data_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `t_frame_menu_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`menu_code`,`resource_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE,
  KEY `fk_reference_23` (`resource_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='菜单资源相关资源';
CREATE TABLE `t_frame_org` (
  `org_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `org_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `org_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `parent_org_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `validator_start` datetime DEFAULT NULL,
  `validator_end` datetime DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`org_code`) USING BTREE,
  UNIQUE KEY `index_1` (`org_code`,`status`,`validator_start`,`validator_end`) USING BTREE,
  KEY `fk_reference_30` (`org_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `t_frame_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  `role_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色类型，  暂时不用！',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 0：无效/1：有效',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `udx_t_frame_role_code` (`role_code`) USING BTREE,
  KEY `fk_reference_29` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色';
CREATE TABLE `t_frame_role_data` (
  `data_auth_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `data_dimension` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限维度:： org（组织机构） /  micro 微服务',
  `data_dimension_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限值',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`data_auth_list_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='数据权限列表\r\n角色类型为功能权限之外的权限统一为功能权限';
CREATE TABLE `t_frame_role_data_interface` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `data_interface_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '接口名称的创建时的md5值存放在mongodb中，请参考DataInterFaceVO',
  `belong_microservice` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '所属微服务',
  `data_interface_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '接口名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `udx_frame_role_data_interface` (`role_id`,`data_interface_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=103 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色数据接口权限';
CREATE TABLE `t_frame_role_menu` (
  `role_menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '系统角色id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` bigint(20) DEFAULT NULL,
  `role_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`role_menu_id`) USING BTREE,
  KEY `ak_kid` (`role_menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色菜单';
CREATE TABLE `t_frame_role_menu_resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_menu` bigint(20) DEFAULT NULL,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE,
  KEY `fk_reference_25` (`menu_code`) USING BTREE,
  KEY `fk_reference_26` (`menu_code`,`resource_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色菜单资源';
CREATE TABLE `t_frame_role_resource` (
  `role_resource_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色菜单资源ID',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `resource_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`role_resource_id`) USING BTREE,
  KEY `ak_kid` (`role_resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1449 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='角色资源列表';
CREATE TABLE `t_frame_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `email` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `full_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `sex` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `mobile_phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `session_key` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT 'session_key',
  `default_role` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'User' COMMENT '默认角色',
  `user_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'admin' COMMENT '用户类型：后台管理用户(admin)/app的名称（用户注册时带必填上来源）',
  `user_regist_source` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'background' COMMENT '用户注册来源：后台(background)/其它appid',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '1:有效 0:无效 -1:过期',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `avatar` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ak_kid` (`id`) USING BTREE,
  UNIQUE KEY `inx_user_user_id` (`user_name`,`user_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=688 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';
CREATE TABLE `t_frame_user_auth` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `validator_start` datetime DEFAULT NULL COMMENT '有效开始时间',
  `validator_end` datetime DEFAULT NULL COMMENT '有效结束时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_f_auth_user_auth_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `t_frame_user_ref` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attribute_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attribute_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`,`attribute_name`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
CREATE TABLE `t_frame_user_role` (
  `user_role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` int(11) DEFAULT NULL COMMENT '角色id',
  `validator_start` datetime DEFAULT NULL COMMENT '有效开始时间',
  `validator_end` datetime DEFAULT NULL COMMENT '有效结束时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`user_role_id`) USING BTREE,
  UNIQUE KEY `uidx_f_auth_user_auth_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
CREATE TABLE `t_framework_resource2` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `uri` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `method` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`resource_code`) USING BTREE,
  UNIQUE KEY `ui_framework_resorce_code` (`resource_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统资源，用来记录所有的系统的URI服务资源\n可以定义参数类型并进行参数类型的检查';
CREATE TABLE `t_framework_resource` (
  `resource_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源编码',
  `resource_name` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源名称',
  `resource_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源路径',
  `method` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源验证式',
  `belong_microservice` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '所属微服务',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`resource_id`) USING BTREE,
  KEY `ak_kid` (`resource_id`) USING BTREE,
  KEY `ui_framework_resorce_code` (`resource_path`,`resource_code`,`belong_microservice`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=143 DEFAULT CHARSET=utf8 COMMENT='系统资源，用来记录所有的系统的URI服务资源\r\n\r\n可以定义参数类型并进行参数类型的检查';
CREATE TABLE `t_microservice_register` (
  `microservice_id` int(11) NOT NULL AUTO_INCREMENT,
  `microservice_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '微服务名称',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`microservice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='系统微服务注册表';
CREATE TABLE `t_system_dic_item` (
  `dic_item_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dic_master_id` bigint(20) NOT NULL COMMENT '数据字典主表id',
  `dic_item_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典项编码',
  `dic_item_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '字典项名称',
  `dic_item_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '字典项值',
  `ext_attribut1` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut3` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut2` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut4` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut5` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1/有效 0/无效 -1/过期',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `language` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'zh_CN',
  `seq` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`dic_item_id`) USING BTREE,
  UNIQUE KEY `udx_dic_item_code` (`dic_master_id`,`dic_item_code`,`language`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COMMENT='系统数据字典字典项表';
CREATE TABLE `t_system_dic_master` (
  `dic_master_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dic_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典编码',
  `dic_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '字典名称',
  `remark` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `belong_micro_service` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'General' COMMENT '所属微服务，默认为General(通用)',
  `parent_master_id` bigint(20) DEFAULT NULL COMMENT '父级id',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态，1/有效 0/无效 -1/过期',
  `create_by` bigint(20) NOT NULL COMMENT '创建人',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_by` bigint(20) NOT NULL COMMENT '更新人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`dic_master_id`) USING BTREE,
  UNIQUE KEY `udx_dic_master_code` (`dic_code`,`belong_micro_service`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='系统数据字典主表';
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_auth_type` WRITE;
DELETE FROM `framework-jdy`.`t_frame_auth_type`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_data_interface` WRITE;
DELETE FROM `framework-jdy`.`t_frame_data_interface`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_data_interface_params` WRITE;
DELETE FROM `framework-jdy`.`t_frame_data_interface_params`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_data_rest_config` WRITE;
DELETE FROM `framework-jdy`.`t_frame_data_rest_config`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_data_sp_config` WRITE;
DELETE FROM `framework-jdy`.`t_frame_data_sp_config`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_data_sql_config` WRITE;
DELETE FROM `framework-jdy`.`t_frame_data_sql_config`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_menu` WRITE;
DELETE FROM `framework-jdy`.`t_frame_menu`;
INSERT INTO `framework-jdy`.`t_frame_menu` (`menu_id`,`menu_code`,`menu_name`,`menu_url`,`component_path`,`type`,`show_type`,`icon`,`function_resource_code`,`status`,`seq_no`,`parent_menu_id`,`create_by`,`create_date`,`update_by`,`update_date`,`meta_data`) VALUES (-1010, '个人中心', '个人中心', '/personal/center', '/layout/Layout', 0, 2, 'sms', NULL, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL),(-1000, 'menu_system_manager', '系统管理', '/admin', '/layout/Layout', 0, 2, NULL, NULL, 1, 10, NULL, 'admin', '2020-05-05 03:08:52', 'admin', '2020-05-05 03:08:52', NULL),(-990, 'demo_test', '测试案例菜单', '/test/demo', '/layout/Layout', 0, 2, 'sms', NULL, 1, 10, NULL, 'admin', '2020-05-05 03:08:52', 'admin', '2020-05-05 03:08:52', NULL),(2, '个人文件存储', '个人文件存储', 'personFileManager', '/user/personFileManager', 1, 2, 'folder', NULL, 1, 1, -1010, NULL, NULL, NULL, NULL, '{\"pageType\": \"personal\"}'),(3, '公共文件查询', '公共文件查询', 'mongodbGridfsPublicFileQuery', '/user/publicFileQuery', 1, 2, 'folder', NULL, 1, 2, -1010, NULL, NULL, NULL, NULL, '{\"pageType\": \"public\"}'),(5, '定时任务管理', '定时任务管理', 'quartz/alljobs', '/admin/quartz/alljobs/index', 1, 1, 'sms-flash', 'common-service::定时任务/quartz::createOrUpdate', 1, 1, -1000, NULL, NULL, NULL, NULL, NULL),(6, '微服务管理', '微服务管理', 'microservice', '/admin/microservice/index', 1, 1, 'cloud', 'common-service::/admin/user::saveOrUpdateUser', 1, 2, -1000, NULL, NULL, NULL, NULL, NULL),(8, 'menu_datainterface_config', '接口配置', 'datainterface', '/admin/datainterface/index', 1, 1, 'controls-vertical-alt', 'common-service::/数据接口管理::查询数据接口', 1, 3, -1000, 'admin', '2020-05-05 03:08:52', 'admin', '2020-05-05 03:08:52', NULL),(11, 'systemRoleAdmin', '角色管理', 'systemRoleAdmin', '/admin/role/index', 1, 1, 'sms-flash', 'common-service::/admin/role/::saveOrUpdateRoles', 1, 4, -1000, NULL, NULL, NULL, NULL, NULL),(12, 'systemUserdmin', '用户管理', 'systemUserdmin', '/admin/user/index', 1, 1, 'people', 'common-service::/admin/user::saveOrUpdateUser', 1, 5, -1000, NULL, NULL, NULL, NULL, NULL),(13, 'testCarousel', 'demo示例', 'testCarousel', '/test/demo/index', 1, 1, 'window', 'common-service::/数据接口管理::查询数据接口', 1, 1, -990, NULL, NULL, NULL, NULL, NULL),(14, 'openlayer5', 'openlayer5', 'openlayer5', '/test/openlayers5/index', 1, 1, 'window', 'common-service::/数据接口管理::查询数据接口', 1, 1, -990, NULL, NULL, NULL, NULL, NULL),(15, '文件管理', '文件管理', 'fileManagerByAdmin', '/admin/mongodb/gridfs/index', 1, 2, 'folder', NULL, 1, 6, -1000, NULL, NULL, NULL, NULL, '{\"pageType\": \"admin\"}'),(16, '数据字典管理', '数据字典管理', 'dataDicManager', '/admin/system/dic/index', 1, 1, 'folder', 'common-service::系统管理/数据字典::保存或者更新', 1, 2, -1000, NULL, NULL, NULL, NULL, NULL);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_menu_data` WRITE;
DELETE FROM `framework-jdy`.`t_frame_menu_data`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_menu_resource` WRITE;
DELETE FROM `framework-jdy`.`t_frame_menu_resource`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_org` WRITE;
DELETE FROM `framework-jdy`.`t_frame_org`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_role` WRITE;
DELETE FROM `framework-jdy`.`t_frame_role`;
INSERT INTO `framework-jdy`.`t_frame_role` (`role_id`,`role_code`,`role_name`,`role_type`,`create_by`,`create_date`,`update_by`,`update_date`,`status`) VALUES (1, 'Admin', '管理员', 'default', 'admin', '2020-01-18 00:48:11', 'admin', '2020-08-04 13:31:43', 1),(3, 'User', '默认用户权限', 'default', 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47', 1);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_role_data` WRITE;
DELETE FROM `framework-jdy`.`t_frame_role_data`;
INSERT INTO `framework-jdy`.`t_frame_role_data` (`data_auth_list_id`,`role_id`,`data_dimension`,`data_dimension_value`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (1, 1, 'micro', 'common-service', 'admin', '2020-01-16 09:00:13', 'admin', '2020-01-16 09:00:21'),(2, 1, 'micro', 'xgsixteen', 'admin', '2020-01-16 09:00:47', 'admin', '2020-01-16 09:00:54'),(3, 5, 'micro', 'common-service', 'admin', '2020-01-16 09:13:57', 'admin', '2020-01-16 09:14:04'),(4, 5, 'micro', 'xgsixteen', 'admin', '2020-01-16 09:13:57', 'admin', '2020-01-16 09:14:04');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_role_data_interface` WRITE;
DELETE FROM `framework-jdy`.`t_frame_role_data_interface`;
INSERT INTO `framework-jdy`.`t_frame_role_data_interface` (`id`,`role_id`,`data_interface_id`,`belong_microservice`,`data_interface_name`) VALUES (93, 1, '5e6c43e07ad92a77cfad8ba6', 'COMMONSERVICE', '查询需要授权的资源清单列表'),(94, 1, '5e6c509e7ad92a77cfad8de2', 'COMMONSERVICE', '查询当前角色操作权限列表'),(95, 1, '5e6d666dc6af767df8096e37', 'COMMONSERVICE', '查询需要授权的接口清单'),(96, 1, '6191e5feb221dd9d0acb24ae2895fb80', 'COMMONSERVICE', '动态SQL需要授权给用户的示例'),(97, 1, 'fca0c2236325f81017b12e4da4d32990', 'COMMONSERVICE', '查询系统角色列表'),(98, 1, '3efa00c2c4b06b73758fac9e95e9b6a9', 'COMMONSERVICE', '查询所有用户的接口'),(102, 1, 'fbf2e7c0ec6c91d9041ccd92509ce309', 'COMMONSERVICE', '按用户ID查询用户的角色列表');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_role_menu` WRITE;
DELETE FROM `framework-jdy`.`t_frame_role_menu`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_role_menu_resource` WRITE;
DELETE FROM `framework-jdy`.`t_frame_role_menu_resource`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_role_resource` WRITE;
DELETE FROM `framework-jdy`.`t_frame_role_resource`;
INSERT INTO `framework-jdy`.`t_frame_role_resource` (`role_resource_id`,`role_id`,`resource_id`) VALUES (1366, 1, -1001),(1367, 1, -1000),(1368, 1, 1),(1369, 1, 3),(1376, 1, 32),(1377, 1, 33),(1378, 1, 34),(1379, 1, 35),(1380, 1, 36),(1381, 1, 37),(1382, 1, 38),(1404, 1, 60),(1405, 1, 61),(1406, 1, 62),(1407, 1, 63),(1408, 1, 64),(1409, 1, 65),(1410, 1, 66),(1412, 1, 86),(1413, 1, 87),(1414, 1, 88),(1415, 1, 89),(1416, 1, 95),(1417, 1, 96),(1422, 1, 102),(1423, 1, 103),(1424, 1, 104),(1425, 1, 106),(1434, 1, 117),(1435, 1, 118),(1436, 1, 119),(1437, 1, 120),(1438, 1, 121),(1439, 1, 122),(1440, 1, 123),(1441, 1, 124),(1442, 1, 125),(1443, 1, 126),(1444, 1, 127),(1445, 1, 128),(1446, 1, 129),(1447, 1, 130);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_user` WRITE;
DELETE FROM `framework-jdy`.`t_frame_user`;
INSERT INTO `framework-jdy`.`t_frame_user` (`id`,`user_name`,`password`,`email`,`full_name`,`sex`,`mobile_phone`,`session_key`,`default_role`,`user_type`,`user_regist_source`,`status`,`create_by`,`create_date`,`update_by`,`update_date`,`avatar`) VALUES (1, 'admin', '4403a1992dcd5f31f73a7edfe3e84c0c', 'admin@qq.com', '无敌超级管理员', 'F', '111', NULL, 'Admin', 'admin', 'background', 3, 'admin', '2016-01-11 12:33:05', 'admin', '2020-08-11 15:28:47', NULL);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_user_auth` WRITE;
DELETE FROM `framework-jdy`.`t_frame_user_auth`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_user_ref` WRITE;
DELETE FROM `framework-jdy`.`t_frame_user_ref`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_frame_user_role` WRITE;
DELETE FROM `framework-jdy`.`t_frame_user_role`;
INSERT INTO `framework-jdy`.`t_frame_user_role` (`user_role_id`,`user_id`,`role_id`,`validator_start`,`validator_end`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (12, 173, 2, '2020-03-17 16:00:00', '2021-03-24 16:00:00', NULL, NULL, NULL, NULL),(13, 173, 3, '2020-03-17 16:00:00', '2021-03-16 16:00:00', 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47'),(14, 173, 7, '2020-03-17 16:00:00', '2020-03-30 16:00:00', NULL, NULL, NULL, NULL),(25, 272, 1, NULL, NULL, 'admin', '2020-01-16 00:48:11', 'admin4', '2020-03-19 00:47:11'),(26, 272, 2, NULL, NULL, 'admin', '2020-01-16 08:41:38', 'admin', '2020-03-15 13:35:53'),(27, 272, 3, NULL, NULL, 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47'),(28, 272, 4, NULL, NULL, 'admin', '2020-01-16 08:53:13', 'admin', '2020-01-16 08:53:26'),(29, 272, 5, NULL, NULL, 'admin', '2020-01-16 09:07:39', 'admin', '2020-01-16 09:07:45'),(30, 272, 6, NULL, NULL, 'admin', '2020-01-16 09:07:39', 'admin', '2020-01-16 09:07:45'),(31, 272, 7, NULL, NULL, 'admin', '2020-03-15 11:37:12', 'admin', '2020-03-15 11:38:57'),(32, 272, 8, NULL, NULL, 'admin', '2020-03-15 11:40:59', 'admin', '2020-03-15 11:40:59'),(33, 272, 9, NULL, NULL, 'admin', '2020-03-15 11:43:06', 'admin', '2020-03-15 11:43:06'),(34, 272, 10, NULL, NULL, 'admin', '2020-03-15 11:44:13', 'admin', '2020-03-15 13:49:51'),(36, 275, 12, NULL, NULL, 'admin', '2020-05-05 19:16:12', 'admin', '2020-05-05 11:23:48'),(37, 1, 1, '2020-01-16 19:25:12', '2020-01-31 19:25:16', NULL, NULL, NULL, NULL);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_framework_resource2` WRITE;
DELETE FROM `framework-jdy`.`t_framework_resource2`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_framework_resource` WRITE;
DELETE FROM `framework-jdy`.`t_framework_resource`;
INSERT INTO `framework-jdy`.`t_framework_resource` (`resource_id`,`resource_code`,`resource_name`,`resource_path`,`method`,`belong_microservice`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (-1001, 'saveOrUpdateUser', '用户角色授权', '/admin/user', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(-1000, 'saveOrUpdateRoles', '操作权限授权', '/admin/role/', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(1, 'datainterface_page_query', '查询接口列表分页', '系统管理/接口配置', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(3, 'datainterface_params_page_query', '查询接口参数列表分页', '系统管理/接口配置', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(32, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(33, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(34, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(35, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(36, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(37, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(38, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(60, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09'),(61, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09'),(62, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09'),(63, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09'),(64, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09'),(65, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09'),(66, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09'),(86, '删除文件', '管理员后台删除文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(87, '查询上传文件', '管理员后台查询文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(88, '下载文件', '管理员后台下载文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(89, '查看文件', '管理员后台查看文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(90, '删除文件', '删除文件，仅登录用户删除自己的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(91, '查询个人上传文件', '查询个人上传文件，需登录', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(92, '下载本人文件', '下载文件，仅可下载本人的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(93, '查看本人文件', '查看本人文件，仅可查看本人的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(94, '上传文件', '上传文件，需要登录', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(95, '刷新缓存', '刷新缓存，需授权', '系统管理/数据字典', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(96, '保存或者更新', '保存或者更新数据字典', '系统管理/数据字典', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(102, '更新数据接口', '更新数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(103, '生效或者失效数据接口', '生效或者失效数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(104, '查询数据接口', '查询数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(106, 'resetPassword', '重置密码', '/admin/user', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(107, 'updatePassWordByUser', '用户更新密码', '/userinfo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(117, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(118, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(119, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(120, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(121, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(122, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(123, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(124, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13'),(125, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13'),(126, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13'),(127, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13'),(128, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13'),(129, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13'),(130, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13'),(131, 'admin-accountList', '管理员获取用户账户列表', '/admin/account', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(132, 'admin-coinList', '管理员获取币种列表', '/admin/account', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(133, 'admin-walletInList', '管理员获取钱包充值记录列表', '/admin/account', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(134, 'admin-walletOutList', '管理员获取钱包提现记录列表', '/admin/account', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(135, '/exchange', '发起闪兑兑换', '/exchangeMarket', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(136, '/getExchangeRecord', '获取闪兑记录', '/exchangeMarket', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(137, '/insideTransfer', '用户站内转账', '/account', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(138, '/getAccountByCoinId', '根据币种id用户获取账户', '/account', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(139, '/getUserDetailList', '用户获取资金明细列表', '/account', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(140, '/accountList', '用户获取账户列表', '/account', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(141, 'getRecAddr', '用户获取币种充值地址', '/wallet', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12'),(142, 'transferOut', '用户站外转出', '/wallet', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_microservice_register` WRITE;
DELETE FROM `framework-jdy`.`t_microservice_register`;
INSERT INTO `framework-jdy`.`t_microservice_register` (`microservice_id`,`microservice_name`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (6, 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14'),(9, 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09'),(11, 'unknow-account-service', 'admin', '2020-08-11 15:37:11', 'admin', '2020-08-11 15:37:11'),(12, 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_system_dic_item` WRITE;
DELETE FROM `framework-jdy`.`t_system_dic_item`;
INSERT INTO `framework-jdy`.`t_system_dic_item` (`dic_item_id`,`dic_master_id`,`dic_item_code`,`dic_item_name`,`dic_item_value`,`ext_attribut1`,`ext_attribut3`,`ext_attribut2`,`ext_attribut4`,`ext_attribut5`,`status`,`create_by`,`create_date`,`update_by`,`update_date`,`language`,`seq`) VALUES (1, 1, 'company_name', 'XXXX有限公司', 'XXXX有限公司', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-02-22 17:42:19', 1, '2020-08-10 19:55:20', 'zh_CN', 10),(13, 15, 'active', '有效', '1', '1', NULL, '12', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 15),(14, 15, 'inactive', '无效', '0', '2', NULL, '', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 20),(15, 15, 'expired', '过期', '-1', '3', NULL, '', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 25),(16, 16, 'commonService', '通用基础服务', 'COMMON-SERVICE', '', NULL, '', NULL, NULL, 1, 1, '2020-03-04 12:40:56', 1, '2020-07-28 13:41:03', 'zh_CN', 5),(20, 16, 'spring-gateway', 'SPRING网关服务', 'SPRING-GATEWAY', '', NULL, '', NULL, NULL, 1, 1, '2020-03-04 12:40:56', 1, '2020-07-28 13:41:03', 'zh_CN', 5),(25, 16, 'General', '所有微服务通用', 'General', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-05 10:32:09', 1, '2020-07-28 13:41:03', 'zh_CN', 0),(44, 28, 'admin', '后台管理用户', 'admin', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:51:50', 1, '2020-03-18 02:29:07', 'zh_CN', 5),(45, 28, 'wechatApplet', '微信小程序用户', 'wechatApplet', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:55:03', 1, '2020-03-18 02:29:07', 'zh_CN', 10),(46, 29, 'background', '后台添加', 'background', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:57:41', 1, '2020-03-18 01:57:41', 'zh_CN', 5),(47, 30, 'Reset', '重置', '3', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 20),(48, 30, 'New', '新建', '2', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 15),(49, 30, 'Inactive', '失效', '-1', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 10),(50, 30, 'Active', '生效', '1', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 5),(100, 16, 'UNKNOW-ACCOUNT-SERVICE', 'UNKNOW-ACCOUNT-SERVICE', 'UNKNOW-ACCOUNT-SERVICE', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-07-28 13:41:03', 1, '2020-07-28 13:41:03', 'zh_CN', 20),(101, 16, 'UNKNOW-WALLET-SERVICE', 'UNKNOW-WALLET-SERVICE', 'UNKNOW-WALLET-SERVICE', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-07-28 13:41:03', 1, '2020-07-28 13:41:03', 'zh_CN', 15),(102, 46, 'platformCoinName', '平台币名称（不区分大小写，注意空格）', 'RC', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-08-12 12:43:25', 1, '2020-08-12 12:44:24', 'zh_CN', 0),(103, 46, 'stableCoinName', '稳定币名称（不区分大小写，注意空格）', 'RCT', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-08-12 12:44:24', 1, '2020-08-12 12:44:24', 'zh_CN', 0),(104, 47, 'lockPositionReleaseRate', '锁仓的平台币释放比例（实例3%填写为0.03）', '0.03', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-08-12 12:49:21', 1, '2020-08-12 12:49:21', 'zh_CN', 0),(105, 47, 'lockPositionExchangeRate', '锁仓时稳定币转平台币汇率（一稳定币等于多少平台币）', '1.5', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-08-12 12:49:21', 1, '2020-08-12 12:49:21', 'zh_CN', 0);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `framework-jdy`.`t_system_dic_master` WRITE;
DELETE FROM `framework-jdy`.`t_system_dic_master`;
INSERT INTO `framework-jdy`.`t_system_dic_master` (`dic_master_id`,`dic_code`,`dic_name`,`remark`,`belong_micro_service`,`parent_master_id`,`status`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (1, 'systemBaseConfig', '系统基础配置', '系统基础配置', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-03-22 07:14:57'),(15, 'commStatus', '系统通用的状态值', '通用状态', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-05-05 19:17:31'),(16, 'microServiceList', '微服务列表', '微服务列表', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-07-28 13:41:03'),(28, 'userType', '用户类型', '用户的来源类型，各个应用，后台创建的就属于admin，其它的属于各个应用注册而来，可以定义，目前微信小程序是微信小程序的名称。', 'General', NULL, 1, 1, '2020-03-18 01:51:50', 1, '2020-03-18 02:29:07'),(29, 'userRegisterSource', '用户注册来源', '用户的注册来源，后台添加的为：background 微信小程序的为微信小程序的ID', 'General', NULL, 1, 1, '2020-03-18 01:57:41', 1, '2020-08-11 20:44:48'),(30, 'userStatus', '用户状态', '用户状态', 'COMMON-SERVICE', NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06'),(46, 'coinConfig', '币种配置', '币种相关配置', 'UNKNOW-ACCOUNT-SERVICE', NULL, 1, 1, '2020-08-12 12:43:25', 1, '2020-08-12 12:44:24'),(47, 'lockPositionConfig', '平台币锁仓相关配置', '', 'UNKNOW-ACCOUNT-SERVICE', NULL, 1, 1, '2020-08-12 12:49:21', 1, '2020-08-12 12:49:21');
UNLOCK TABLES;
COMMIT;
