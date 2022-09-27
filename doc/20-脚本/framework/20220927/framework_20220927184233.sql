/*
MySQL Backup
Database: framework_init
Backup Time: 2022-09-27 18:42:33
*/

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `t_email_template`;
DROP TABLE IF EXISTS `t_frame_auth_type`;
DROP TABLE IF EXISTS `t_frame_data_dimension`;
DROP TABLE IF EXISTS `t_frame_data_interface`;
DROP TABLE IF EXISTS `t_frame_data_interface_params`;
DROP TABLE IF EXISTS `t_frame_data_rest_config`;
DROP TABLE IF EXISTS `t_frame_data_sp_config`;
DROP TABLE IF EXISTS `t_frame_data_sql_config`;
DROP TABLE IF EXISTS `t_frame_menu`;
DROP TABLE IF EXISTS `t_frame_menu_data`;
DROP TABLE IF EXISTS `t_frame_menu_resource`;
DROP TABLE IF EXISTS `t_frame_org`;
DROP TABLE IF EXISTS `t_frame_role`;
DROP TABLE IF EXISTS `t_frame_role_data`;
DROP TABLE IF EXISTS `t_frame_role_data_interface`;
DROP TABLE IF EXISTS `t_frame_role_menu`;
DROP TABLE IF EXISTS `t_frame_role_menu_resource`;
DROP TABLE IF EXISTS `t_frame_role_resource`;
DROP TABLE IF EXISTS `t_frame_user`;
DROP TABLE IF EXISTS `t_frame_user_auth`;
DROP TABLE IF EXISTS `t_frame_user_ref`;
DROP TABLE IF EXISTS `t_frame_user_role`;
DROP TABLE IF EXISTS `t_framework_resource`;
DROP TABLE IF EXISTS `t_framework_resource2`;
DROP TABLE IF EXISTS `t_microservice_register`;
DROP TABLE IF EXISTS `t_system_dic_item`;
DROP TABLE IF EXISTS `t_system_dic_master`;
CREATE TABLE `t_email_template` (
  `template_id` bigint NOT NULL AUTO_INCREMENT,
  `template_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板编码',
  `language` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'zh_CN' COMMENT '语言，默认zh_CN',
  `subject` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '主题（变量用{{变量名}}，方便替换）',
  `from_address` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '默认发送人(需要经过校验，暂时全局只有一个发送者，也就是配置的发送者)',
  `to_address` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '默认接收人（多个请用,号分割）',
  `template_text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '模板内容（thymeleaf模板）',
  `CC` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '抄送',
  `BCC` varchar(4000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '密送',
  `CREATED_BY` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `CREATED_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `UPDATED_BY` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新人',
  `UPDATED_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`template_id`) USING BTREE,
  UNIQUE KEY `udx_email_temlate` (`template_code`,`language`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `t_frame_auth_type` (
  `auth_type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限类型ID',
  `auth_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限编码',
  `auth_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限类型：角色（role）/微服务权限（mircroservice）/组机机构（organization）/其它数据权限（可定义）',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`auth_type_id`) USING BTREE,
  UNIQUE KEY `uidx_f_auth_type_code` (`auth_code`) USING BTREE,
  KEY `ak_kid` (`auth_type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC;
CREATE TABLE `t_frame_data_dimension` (
  `data_auth_list_id` bigint NOT NULL AUTO_INCREMENT,
  `data_dimension_type` int NOT NULL COMMENT '10:角色 20:用户数据权限',
  `refer_id` bigint NOT NULL COMMENT '根据权限类型来关系不用的ID',
  `data_dimension` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限维度:： org（组织机构） /  micro 微服务',
  `data_dimension_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '权限值',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态，1/有效 0/无效 -1/过期',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `create_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `update_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`data_auth_list_id`) USING BTREE,
  UNIQUE KEY `udx_t_frame_data_dimension` (`data_dimension_type`,`refer_id`,`data_dimension`,`data_dimension_value`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='数据权限列表\r\n角色类型为功能权限之外的权限统一为功能权限';
CREATE TABLE `t_frame_data_interface` (
  `data_interface_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'data_interface_id',
  `data_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '数据编号',
  `data_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '数据名称',
  `data_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '数据类型',
  `data_execute_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '数据执行服务名',
  `description` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci COMMENT '描述',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`data_interface_id`) USING BTREE,
  UNIQUE KEY `ak_kid` (`data_interface_id`) USING BTREE,
  UNIQUE KEY `udx_data_interface_code` (`data_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='数据接口定义';
CREATE TABLE `t_frame_data_interface_params` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `seq` int NOT NULL,
  `param_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `data_interface_id` bigint DEFAULT NULL,
  `param_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `param_desc` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC;
CREATE TABLE `t_frame_data_rest_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `data_interface_id` bigint DEFAULT NULL,
  `uri` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `method` char(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `must_login` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `login_bean` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='REST服务数据接口';
CREATE TABLE `t_frame_data_sp_config` (
  `id` bigint NOT NULL,
  `data_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `data_interface_id` bigint DEFAULT NULL,
  `sql_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `data_source` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `database_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `data_dao_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`,`data_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='存储过程数据接口表';
CREATE TABLE `t_frame_data_sql_config` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `data_interface_id` bigint DEFAULT NULL,
  `sql_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `data_source` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `database_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `data_dao_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC;
CREATE TABLE `t_frame_menu` (
  `menu_id` bigint NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `menu_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `menu_url` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `component_path` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'vue组件位置',
  `type` int NOT NULL DEFAULT '1' COMMENT '0：目录 1：菜单',
  `show_type` int unsigned NOT NULL DEFAULT '2' COMMENT '0：有子菜单时显示 1：有权限时显示 2：任何时候都显示',
  `icon` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '显示图标',
  `function_resource_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '权限编码：微服务application.name::resource_path::resource_name 例如：common-service::系统管理/数据字典::刷新缓存',
  `status` int NOT NULL DEFAULT '1' COMMENT '1:有效 0:无效 -1:过期',
  `seq_no` int NOT NULL DEFAULT '1',
  `parent_menu_id` bigint DEFAULT NULL COMMENT '父级菜单ID',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `meta_data` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '路由meta参数，必须为json字符串',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE KEY `ui_framework_menu_1` (`menu_code`) USING BTREE,
  UNIQUE KEY `udx_framework_menu_1` (`menu_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=934 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='系统菜单';
CREATE TABLE `t_frame_menu_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `data_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`menu_code`,`data_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE,
  KEY `ak_kid2` (`id`) USING BTREE,
  KEY `fk_reference_35` (`data_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
CREATE TABLE `t_frame_menu_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `resource_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`menu_code`,`resource_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE,
  KEY `fk_reference_23` (`resource_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='菜单资源相关资源';
CREATE TABLE `t_frame_org` (
  `org_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `org_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `org_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `parent_org_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `validator_start` datetime DEFAULT NULL,
  `validator_end` datetime DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`org_code`) USING BTREE,
  UNIQUE KEY `index_1` (`org_code`,`status`,`validator_start`,`validator_end`) USING BTREE,
  KEY `fk_reference_30` (`org_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC;
CREATE TABLE `t_frame_role` (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色名称',
  `role_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '角色类型，  暂时不用！',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态 0：无效/1：有效',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE KEY `udx_t_frame_role_code` (`role_code`) USING BTREE,
  KEY `fk_reference_29` (`role_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='系统角色';
CREATE TABLE `t_frame_role_data` (
  `data_auth_list_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int DEFAULT NULL COMMENT '角色id',
  `data_dimension` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限维度:： org（组织机构） /  micro 微服务',
  `data_dimension_value` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限值',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`data_auth_list_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='数据权限列表\r\n角色类型为功能权限之外的权限统一为功能权限';
CREATE TABLE `t_frame_role_data_interface` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL COMMENT '角色id',
  `data_interface_id` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '接口名称的创建时的md5值存放在mongodb中，请参考DataInterFaceVO',
  `belong_microservice` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '所属微服务',
  `data_interface_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '接口名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `udx_frame_role_data_interface` (`role_id`,`data_interface_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='角色数据接口权限';
CREATE TABLE `t_frame_role_menu` (
  `role_menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '系统角色id',
  `role_id` int NOT NULL COMMENT '角色id',
  `menu_id` bigint DEFAULT NULL,
  `role_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`role_menu_id`) USING BTREE,
  KEY `ak_kid` (`role_menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='系统角色菜单';
CREATE TABLE `t_frame_role_menu_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_menu` bigint DEFAULT NULL,
  `menu_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `resource_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE,
  KEY `fk_reference_25` (`menu_code`) USING BTREE,
  KEY `fk_reference_26` (`menu_code`,`resource_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='系统角色菜单资源';
CREATE TABLE `t_frame_role_resource` (
  `role_resource_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色菜单资源ID',
  `role_id` int DEFAULT NULL COMMENT '角色id',
  `resource_id` bigint DEFAULT NULL,
  PRIMARY KEY (`role_resource_id`) USING BTREE,
  KEY `ak_kid` (`role_resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4653 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='角色资源列表';
CREATE TABLE `t_frame_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `password` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL,
  `email` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `full_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `sex` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL,
  `mobile_phone` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '手机号',
  `session_key` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'session_key',
  `default_role` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT 'User' COMMENT '默认角色',
  `user_type` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT 'admin' COMMENT '用户类型：后台管理用户(admin)/app的名称（用户注册时带必填上来源）',
  `user_regist_source` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL DEFAULT 'background' COMMENT '用户注册来源：后台(background)/其它appid',
  `status` int NOT NULL DEFAULT '1' COMMENT '1:有效 0:无效 -1:过期',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  `avatar` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ak_kid` (`id`) USING BTREE,
  UNIQUE KEY `inx_user_user_id` (`user_name`,`user_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=736 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='用户表';
CREATE TABLE `t_frame_user_auth` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` int DEFAULT NULL COMMENT '角色id',
  `validator_start` datetime DEFAULT NULL COMMENT '有效开始时间',
  `validator_end` datetime DEFAULT NULL COMMENT '有效结束时间',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uidx_f_auth_user_auth_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC;
CREATE TABLE `t_frame_user_ref` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `attribute_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `attribute_value` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`,`attribute_name`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;
CREATE TABLE `t_frame_user_role` (
  `user_role_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` int DEFAULT NULL COMMENT '角色id',
  `validator_start` datetime DEFAULT NULL COMMENT '有效开始时间',
  `validator_end` datetime DEFAULT NULL COMMENT '有效结束时间',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`user_role_id`) USING BTREE,
  UNIQUE KEY `uidx_f_auth_user_auth_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC;
CREATE TABLE `t_framework_resource` (
  `resource_id` bigint NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '资源编码',
  `resource_name` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '资源名称',
  `resource_path` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '资源路径',
  `method` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '资源验证式',
  `belong_microservice` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '所属微服务',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '更新人',
  `update_date` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`resource_id`) USING BTREE,
  KEY `ak_kid` (`resource_id`) USING BTREE,
  KEY `ui_framework_resorce_code` (`resource_path`,`resource_code`,`belong_microservice`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=414 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统资源，用来记录所有的系统的URI服务资源\r\n\r\n可以定义参数类型并进行参数类型的检查';
CREATE TABLE `t_framework_resource2` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `resource_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `uri` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `method` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`resource_code`) USING BTREE,
  UNIQUE KEY `ui_framework_resorce_code` (`resource_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin ROW_FORMAT=DYNAMIC COMMENT='系统资源，用来记录所有的系统的URI服务资源\n可以定义参数类型并进行参数类型的检查';
CREATE TABLE `t_microservice_register` (
  `microservice_id` int NOT NULL AUTO_INCREMENT,
  `microservice_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '微服务名称',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`microservice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统微服务注册表';
CREATE TABLE `t_system_dic_item` (
  `dic_item_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dic_master_id` bigint NOT NULL COMMENT '数据字典主表id',
  `dic_item_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '字典项编码',
  `dic_item_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '字典项名称',
  `dic_item_value` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '字典项值',
  `ext_attribut1` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut3` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut2` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut4` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut5` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '扩展属性1',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态，1/有效 0/无效 -1/过期',
  `create_by` bigint NOT NULL COMMENT '创建人',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_by` bigint NOT NULL COMMENT '更新人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `language` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'zh_CN',
  `seq` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`dic_item_id`) USING BTREE,
  UNIQUE KEY `udx_dic_item_code` (`dic_master_id`,`dic_item_code`,`language`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=112 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统数据字典字典项表';
CREATE TABLE `t_system_dic_master` (
  `dic_master_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dic_code` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '字典编码',
  `dic_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '字典名称',
  `remark` varchar(2000) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `belong_micro_service` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT 'General' COMMENT '所属微服务，默认为General(通用)',
  `parent_master_id` bigint DEFAULT NULL COMMENT '父级id',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态，1/有效 0/无效 -1/过期',
  `create_by` bigint NOT NULL COMMENT '创建人',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_by` bigint NOT NULL COMMENT '更新人',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`dic_master_id`) USING BTREE,
  UNIQUE KEY `udx_dic_master_code` (`dic_code`,`belong_micro_service`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统数据字典主表';
BEGIN;
LOCK TABLES `t_email_template` WRITE;
DELETE FROM `t_email_template`;
INSERT INTO `t_email_template` (`template_id`,`template_code`,`language`,`subject`,`from_address`,`to_address`,`template_text`,`CC`,`BCC`,`CREATED_BY`,`CREATED_DATE`,`UPDATED_BY`,`UPDATED_DATE`) VALUES (1, 'IP_LOGIN_LOCKER_EMAIL', 'zh_CN', 'ip: {{ip}} is locked!', NULL, '609925924@qq.com,longyou88888@126.com,com_unknow_first@126.com', '\r\n<div>\r\n    <div align=\"center\">\r\n        <div class=\"open_email\" style=\"margin-left: 8px; margin-top: 8px; margin-bottom: 8px; margin-right: 8px;\">\r\n            <div>\r\n                <br>\r\n                <span class=\"genEmailContent\">\r\n                    <div id=\"cTMail-Wrap\"\r\n                         style=\"word-break: break-all;box-sizing:border-box;text-align:center;min-width:320px; max-width:660px; border:1px solid #f6f6f6; background-color:#f7f8fa; margin:auto; padding:20px 0 30px; font-family:\'helvetica neue\',PingFangSC-Light,arial,\'hiragino sans gb\',\'microsoft yahei ui\',\'microsoft yahei\',simsun,sans-serif\">\r\n                        <div class=\"main-content\" style=\"\">\r\n                            <table style=\"width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse\">\r\n                                <tbody>\r\n                                <tr style=\"font-weight:300\">\r\n                                    <td style=\"width:3%;max-width:30px;\"></td>\r\n                                    <td style=\"max-width:600px;\">\r\n                                        <div id=\"cTMail-logo\" style=\"width:500px; height:25px;\">\r\n                                            <h1 th:text=\"${ip}+\',登录异常预警\'\" id=\"cTMail-title\" style=\"font-size: 20px; line-height: 36px; margin: 0px 0px 22px;\">\r\n                                                登录预警\r\n                                           </h1>\r\n                                        </div>\r\n                                        <p style=\"height:2px;background-color: #00a4ff;border: 0;font-size:0;padding:0;width:100%;margin-top:20px;\"></p>\r\n\r\n                                        <div id=\"cTMail-inner\" style=\"background-color:#fff; padding:23px 0 20px;box-shadow: 0px 1px 1px 0px rgba(122, 55, 55, 0.2);text-align:left;\">\r\n                                            <table style=\"width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse;text-align:left;\">\r\n                                                <tbody>\r\n                                                <tr style=\"font-weight:300\">\r\n                                                    <td style=\"width:3.2%;max-width:30px;\"></td>\r\n                                                    \r\n                                                    <td style=\"max-width:480px;text-align:left;\">\r\n\r\n                                                        <p th:text=\"\'最后登录IP：\' + ${ip}\" id=\"cTMail-userName\" style=\"font-size:14px;color:#333; line-height:24px; margin:0;\">\r\n                                                           ip\r\n                                                        </p>\r\n\r\n                                                        <p th:text=\"\'IP地址登录失败次数过多，IP地址将被禁止登录直到\'+${unlockedDate}\" class=\"cTMail-content\" style=\"line-height: 24px; margin: 6px 0px 0px; overflow-wrap: break-word; word-break: break-all;\">\r\n                                                            <span style=\"color: red; font-size: 16px; font-weight: 700\">\r\n                                                               提示\r\n                                                            </span>\r\n                                                        </p>\r\n                                                    </td>\r\n                                                    <td style=\"width:3.2%;max-width:30px;\"></td>\r\n                                                </tr>\r\n                                                </tbody>\r\n                                            </table>\r\n                                        </div>\r\n                                    </td>\r\n                                    <td style=\"width:3%;max-width:30px;\"></td>\r\n                                </tr>\r\n                                </tbody>\r\n                            </table>\r\n                        </div>\r\n                    </div>\r\n                </span>\r\n\r\n            </div>\r\n        </div>\r\n    </div>\r\n</div>', NULL, NULL, 'admin', '2022-06-23 09:54:27', 'admin', '2022-06-24 12:07:36'),(2, 'USER_LOGIN_LOCKER_EMAIL', 'zh_CN', 'User {{userName}} is locked!', NULL, '609925924@qq.com,longyou88888@126.com,com_unknow_first@126.com', '\r\n<div>\r\n    <div align=\"center\">\r\n        <div class=\"open_email\" style=\"margin-left: 8px; margin-top: 8px; margin-bottom: 8px; margin-right: 8px;\">\r\n            <div>\r\n                <br>\r\n                <span class=\"genEmailContent\">\r\n                    <div id=\"cTMail-Wrap\"\r\n                         style=\"word-break: break-all;box-sizing:border-box;text-align:center;min-width:320px; max-width:660px; border:1px solid #f6f6f6; background-color:#f7f8fa; margin:auto; padding:20px 0 30px; font-family:\'helvetica neue\',PingFangSC-Light,arial,\'hiragino sans gb\',\'microsoft yahei ui\',\'microsoft yahei\',simsun,sans-serif\">\r\n                        <div class=\"main-content\" style=\"\">\r\n                            <table style=\"width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse\">\r\n                                <tbody>\r\n                                <tr style=\"font-weight:300\">\r\n                                    <td style=\"width:3%;max-width:30px;\"></td>\r\n                                    <td style=\"max-width:600px;\">\r\n                                        <div id=\"cTMail-logo\" style=\"width:500px; height:25px;\">\r\n                                            <h1 th:text=\"${serviceName}+\'用户登录异常预警\'\" id=\"cTMail-title\" style=\"font-size: 20px; line-height: 36px; margin: 0px 0px 22px;\">\r\n                                                登录预警\r\n                                           </h1>\r\n                                        </div>\r\n                                        <p style=\"height:2px;background-color: #00a4ff;border: 0;font-size:0;padding:0;width:100%;margin-top:20px;\"></p>\r\n\r\n                                        <div id=\"cTMail-inner\" style=\"background-color:#fff; padding:23px 0 20px;box-shadow: 0px 1px 1px 0px rgba(122, 55, 55, 0.2);text-align:left;\">\r\n                                            <table style=\"width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse;text-align:left;\">\r\n                                                <tbody>\r\n                                                <tr style=\"font-weight:300\">\r\n                                                    <td style=\"width:3.2%;max-width:30px;\"></td>\r\n                                                    \r\n                                                    <td style=\"max-width:480px;text-align:left;\">\r\n\r\n\r\n                                                        <p th:text=\"\'用户名:\' + ${userName}\" id=\"cTMail-userName\" style=\"font-size:14px;color:#333; line-height:24px; margin:0;\">\r\n                                                           userName\r\n                                                        </p>\r\n\r\n\r\n                                                        <p th:text=\"\'最后登录IP：\' + ${ip}\" id=\"cTMail-userName\" style=\"font-size:14px;color:#333; line-height:24px; margin:0;\">\r\n                                                           ip\r\n                                                        </p>\r\n\r\n                                                        <p th:text=\"\'用户登录连续登录失败次数过多，用户将被锁定到\'+${unlockedDate}\" class=\"cTMail-content\" style=\"line-height: 24px; margin: 6px 0px 0px; overflow-wrap: break-word; word-break: break-all;\">\r\n                                                            <span style=\"color: red; font-size: 16px; font-weight: 700\">\r\n                                                               提示\r\n                                                            </span>\r\n                                                        </p>\r\n                                                    </td>\r\n                                                    <td style=\"width:3.2%;max-width:30px;\"></td>\r\n                                                </tr>\r\n                                                </tbody>\r\n                                            </table>\r\n                                        </div>\r\n                                    </td>\r\n                                    <td style=\"width:3%;max-width:30px;\"></td>\r\n                                </tr>\r\n                                </tbody>\r\n                            </table>\r\n                        </div>\r\n                    </div>\r\n                </span>\r\n\r\n            </div>\r\n        </div>\r\n    </div>\r\n</div>', NULL, NULL, 'admin', '2022-06-23 09:54:27', 'admin', '2022-06-24 10:10:49');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_auth_type` WRITE;
DELETE FROM `t_frame_auth_type`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_data_dimension` WRITE;
DELETE FROM `t_frame_data_dimension`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_data_interface` WRITE;
DELETE FROM `t_frame_data_interface`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_data_interface_params` WRITE;
DELETE FROM `t_frame_data_interface_params`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_data_rest_config` WRITE;
DELETE FROM `t_frame_data_rest_config`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_data_sp_config` WRITE;
DELETE FROM `t_frame_data_sp_config`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_data_sql_config` WRITE;
DELETE FROM `t_frame_data_sql_config`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_menu` WRITE;
DELETE FROM `t_frame_menu`;
INSERT INTO `t_frame_menu` (`menu_id`,`menu_code`,`menu_name`,`menu_url`,`component_path`,`type`,`show_type`,`icon`,`function_resource_code`,`status`,`seq_no`,`parent_menu_id`,`create_by`,`create_date`,`update_by`,`update_date`,`meta_data`) VALUES (-1010, '个人中心', '个人中心', '/personal/center', '/layout/Layout', 0, 2, 'sms', NULL, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL),(-1000, 'menu_system_manager', '系统管理', '/admin', '/layout/Layout', 0, 2, NULL, NULL, 1, 10, NULL, 'admin', '2020-05-05 03:08:52', 'admin', '2020-05-05 03:08:52', NULL),(2, '个人文件存储', '个人文件存储', 'personFileManager', '/user/personFileManager', 1, 2, 'folder', NULL, 1, 1, -1010, NULL, NULL, NULL, NULL, '{\"pageType\": \"personal\"}'),(3, '公共文件查询', '公共文件查询', 'mongodbGridfsPublicFileQuery', '/user/publicFileQuery', 1, 2, 'folder', NULL, 1, 2, -1010, NULL, NULL, NULL, NULL, '{\"pageType\": \"public\"}'),(5, '定时任务管理', '定时任务管理', 'quartz/alljobs', '/admin/quartz/alljobs/index', 1, 1, 'sms-flash', 'common-service::定时任务/quartz::createOrUpdate', 1, 1, -1000, NULL, NULL, NULL, NULL, NULL),(6, '微服务管理', '微服务管理', 'microservice', '/admin/microservice/index', 1, 1, 'cloud', 'common-service::/admin/user::saveOrUpdateUser', 1, 2, -1000, NULL, NULL, NULL, NULL, NULL),(8, 'menu_datainterface_config', '接口配置', 'datainterface', '/admin/datainterface/index', 1, 1, 'controls-vertical-alt', 'common-service::/数据接口管理::查询数据接口', 1, 3, -1000, 'admin', '2020-05-05 03:08:52', 'admin', '2020-05-05 03:08:52', NULL),(11, 'systemRoleAdmin', '角色管理', 'systemRoleAdmin', '/admin/role/index', 1, 1, 'sms-flash', 'common-service::/admin/role/::saveOrUpdateRoles', 1, 4, -1000, NULL, NULL, NULL, NULL, NULL),(12, 'systemUserdmin', '用户管理', 'systemUserdmin', '/admin/user/index', 1, 1, 'people', 'common-service::/admin/user::saveOrUpdateUser', 1, 5, -1000, NULL, NULL, NULL, NULL, NULL),(15, '文件管理', '文件管理', 'fileManagerByAdmin', '/admin/mongodb/gridfs/index', 1, 1, 'folder', 'common-service::/admin/mongo::查询上传文件', 1, 6, -1000, NULL, NULL, NULL, NULL, '{\"pageType\": \"admin\"}'),(16, '数据字典管理', '数据字典管理', 'dataDicManager', '/admin/system/dic/index', 1, 1, 'folder', 'common-service::系统管理/数据字典::保存或者更新', 1, 2, -1000, NULL, NULL, NULL, NULL, NULL),(888, 'systemRouter', '菜单管理', 'systemRouter', '/admin/system/router', 1, 1, 'menu', 'common-service::/数据接口管理::查询数据接口', 1, 4, -1000, NULL, NULL, NULL, NULL, NULL),(933, '操作权限管理', '操作权限管理', 'operateLegalPower', '/admin/operateLegalPower', 1, 1, '', 'common-service::/admin/mongo::查询上传文件', 1, 10, -1000, NULL, NULL, NULL, NULL, NULL);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_menu_data` WRITE;
DELETE FROM `t_frame_menu_data`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_menu_resource` WRITE;
DELETE FROM `t_frame_menu_resource`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_org` WRITE;
DELETE FROM `t_frame_org`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_role` WRITE;
DELETE FROM `t_frame_role`;
INSERT INTO `t_frame_role` (`role_id`,`role_code`,`role_name`,`role_type`,`create_by`,`create_date`,`update_by`,`update_date`,`status`) VALUES (1, 'Admin', '管理员', 'default', 'admin', '2020-01-18 00:48:11', 'admin', '2022-09-27 11:39:29', 1),(3, 'User', '默认用户权限', 'default', 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47', 1);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_role_data` WRITE;
DELETE FROM `t_frame_role_data`;
INSERT INTO `t_frame_role_data` (`data_auth_list_id`,`role_id`,`data_dimension`,`data_dimension_value`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (1, 1, 'micro', 'common-service', 'admin', '2020-01-16 09:00:13', 'admin', '2020-01-16 09:00:21'),(2, 1, 'micro', 'xgsixteen', 'admin', '2020-01-16 09:00:47', 'admin', '2020-01-16 09:00:54'),(3, 5, 'micro', 'common-service', 'admin', '2020-01-16 09:13:57', 'admin', '2020-01-16 09:14:04'),(4, 5, 'micro', 'xgsixteen', 'admin', '2020-01-16 09:13:57', 'admin', '2020-01-16 09:14:04');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_role_data_interface` WRITE;
DELETE FROM `t_frame_role_data_interface`;
INSERT INTO `t_frame_role_data_interface` (`id`,`role_id`,`data_interface_id`,`belong_microservice`,`data_interface_name`) VALUES (128, 1, '3efa00c2c4b06b73758fac9e95e9b6a9', 'COMMONSERVICE', '查询所有用户的接口'),(129, 1, '5e6c43e07ad92a77cfad8ba6', 'COMMONSERVICE', '查询需要授权的资源清单列表'),(130, 1, '5e6c509e7ad92a77cfad8de2', 'COMMONSERVICE', '查询当前角色操作权限列表'),(131, 1, '5e6d666dc6af767df8096e37', 'COMMONSERVICE', '查询需要授权的接口清单'),(132, 1, '6191e5feb221dd9d0acb24ae2895fb80', 'COMMONSERVICE', '动态SQL需要授权给用户的示例'),(133, 1, 'dc14b9097ea7bf9eaee08b19a793ccf2', 'UNKNOW-ACCOUNT-SERVICE', '查询钱包用户信息'),(134, 1, 'f8c3d11952196739e55f80f472a52693', 'General', '查询操作权限列表'),(135, 1, 'fbf2e7c0ec6c91d9041ccd92509ce309', 'COMMONSERVICE', '按用户ID查询用户的角色列表'),(136, 1, 'fca0c2236325f81017b12e4da4d32990', 'COMMONSERVICE', '查询系统角色列表');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_role_menu` WRITE;
DELETE FROM `t_frame_role_menu`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_role_menu_resource` WRITE;
DELETE FROM `t_frame_role_menu_resource`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_role_resource` WRITE;
DELETE FROM `t_frame_role_resource`;
INSERT INTO `t_frame_role_resource` (`role_resource_id`,`role_id`,`resource_id`) VALUES (4319, 1, -1001),(4320, 1, -1000),(4321, 1, 1),(4322, 1, 3),(4323, 1, 32),(4324, 1, 33),(4325, 1, 34),(4326, 1, 35),(4327, 1, 36),(4328, 1, 37),(4329, 1, 38),(4330, 1, 60),(4331, 1, 61),(4332, 1, 62),(4333, 1, 63),(4334, 1, 64),(4335, 1, 65),(4336, 1, 66),(4337, 1, 86),(4338, 1, 87),(4339, 1, 88),(4340, 1, 89),(4341, 1, 95),(4342, 1, 96),(4343, 1, 102),(4344, 1, 103),(4345, 1, 104),(4346, 1, 106),(4347, 1, 165),(4348, 1, 166),(4349, 1, 167),(4350, 1, 168),(4351, 1, 169),(4352, 1, 170),(4353, 1, 176),(4354, 1, 178),(4355, 1, 181),(4356, 1, 185),(4357, 1, 187),(4358, 1, 197),(4359, 1, 205),(4360, 1, 214),(4361, 1, 215),(4362, 1, 216),(4363, 1, 217),(4364, 1, 218),(4365, 1, 219),(4366, 1, 220),(4367, 1, 221);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_user` WRITE;
DELETE FROM `t_frame_user`;
INSERT INTO `t_frame_user` (`id`,`user_name`,`password`,`email`,`full_name`,`sex`,`mobile_phone`,`session_key`,`default_role`,`user_type`,`user_regist_source`,`status`,`create_by`,`create_date`,`update_by`,`update_date`,`avatar`) VALUES (1, 'admin', '11863c7d74ddeb948949594d7240ad5b', 'admin@qq.com', '无敌超级管理员', 'F', '111', NULL, 'Admin', 'admin', 'background', 3, 'admin', '2016-01-11 12:33:05', 'admin', '2021-06-08 16:05:55', NULL),(721, 'D45771', 'b8112eb231f8c098a33ae4e35921c8d0', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, 'D45771', '2022-09-23 18:02:05', 'D45771', '2022-09-26 15:38:16', NULL),(722, 'A41A46', 'bc0a4efa8a0da3aff7d4192c5f2b76a8', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, 'A41A46', '2022-09-23 18:59:03', 'A41A46', '2022-09-23 19:02:51', NULL),(723, 'aleoAdmin', 'c71a2adea955532b379624fcc90baa37', NULL, NULL, NULL, NULL, NULL, 'User', 'admin', 'background', 2, 'admin', '2022-09-23 19:15:29', 'admin', '2022-09-23 19:15:29', NULL),(724, '839075', 'd17c0ffd036e6a6666928013203c8cd4', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, '839075', '2022-09-23 23:46:50', '839075', '2022-09-26 03:54:13', NULL),(725, '1B1C77', 'e0d841a05e271af7c7d06a9de7f089c1', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, '1B1C77', '2022-09-24 00:09:58', '1B1C77', '2022-09-26 19:31:11', NULL),(726, 'C2E282', 'c9ce7f574cfbfa635b1db477cd94a722', NULL, NULL, NULL, NULL, '', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, 'C2E282', '2022-09-24 15:33:31', 'C2E282', '2022-09-24 15:33:31', NULL),(727, 'FCF566', '97681e66ccc7ec9bcd45e8761795813b', NULL, NULL, NULL, NULL, '', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, 'FCF566', '2022-09-24 15:52:21', 'FCF566', '2022-09-24 15:52:21', NULL),(728, 'admin_test_1', '11863c7d74ddeb948949594d7240ad5b', NULL, NULL, NULL, NULL, NULL, 'User', 'admin', 'background', 2, 'admin', '2022-09-25 09:35:54', 'admin', '2022-09-25 09:35:54', NULL),(729, 'E73852', '49e9af06a882fd35428f985d5509989e', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, 'E73852', '2022-09-25 13:06:33', 'E73852', '2022-09-25 14:35:52', NULL),(730, '125984', '6eb30f7538def8c1058ca841c25b810f', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, '125984', '2022-09-25 14:06:32', '125984', '2022-09-26 22:30:27', NULL),(731, '4CA238', '0a8cc56274a4b0479d30894272c99318', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, '4CA238', '2022-09-25 14:09:43', '4CA238', '2022-09-25 14:22:31', NULL),(732, '307261', '6e7bf3be743e1fadaef983cce7d4b7e6', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, '307261', '2022-09-25 14:10:25', '307261', '2022-09-25 14:34:23', NULL),(733, 'C34552', 'fe7edca7ed2eeb1bec6986402cc9bb80', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, 'C34552', '2022-09-25 16:02:33', 'C34552', '2022-09-26 12:05:21', NULL),(734, '851D12', '7f9b767b19e9af0d326cc89b0fe4d60c', NULL, NULL, NULL, NULL, '', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, '851D12', '2022-09-26 09:04:55', '851D12', '2022-09-26 09:04:55', NULL),(735, '89C287', '769333cb986dd0658305044c125864af', NULL, NULL, NULL, NULL, 'UNKNOW-ACCOUNT-SERVICE', 'User', 'LOGIN-BY-THIRD-LOGIN', 'UNKNOW-ACCOUNT-SERVICE', 1, '89C287', '2022-09-26 12:00:17', '89C287', '2022-09-26 12:04:53', NULL);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_user_auth` WRITE;
DELETE FROM `t_frame_user_auth`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_user_ref` WRITE;
DELETE FROM `t_frame_user_ref`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_frame_user_role` WRITE;
DELETE FROM `t_frame_user_role`;
INSERT INTO `t_frame_user_role` (`user_role_id`,`user_id`,`role_id`,`validator_start`,`validator_end`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (12, 173, 2, '2020-03-17 16:00:00', '2021-03-24 16:00:00', NULL, NULL, NULL, NULL),(13, 173, 3, '2020-03-17 16:00:00', '2021-03-16 16:00:00', 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47'),(14, 173, 7, '2020-03-17 16:00:00', '2020-03-30 16:00:00', NULL, NULL, NULL, NULL),(25, 272, 1, NULL, NULL, 'admin', '2020-01-16 00:48:11', 'admin4', '2020-03-19 00:47:11'),(26, 272, 2, NULL, NULL, 'admin', '2020-01-16 08:41:38', 'admin', '2020-03-15 13:35:53'),(27, 272, 3, NULL, NULL, 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47'),(28, 272, 4, NULL, NULL, 'admin', '2020-01-16 08:53:13', 'admin', '2020-01-16 08:53:26'),(29, 272, 5, NULL, NULL, 'admin', '2020-01-16 09:07:39', 'admin', '2020-01-16 09:07:45'),(30, 272, 6, NULL, NULL, 'admin', '2020-01-16 09:07:39', 'admin', '2020-01-16 09:07:45'),(31, 272, 7, NULL, NULL, 'admin', '2020-03-15 11:37:12', 'admin', '2020-03-15 11:38:57'),(32, 272, 8, NULL, NULL, 'admin', '2020-03-15 11:40:59', 'admin', '2020-03-15 11:40:59'),(33, 272, 9, NULL, NULL, 'admin', '2020-03-15 11:43:06', 'admin', '2020-03-15 11:43:06'),(34, 272, 10, NULL, NULL, 'admin', '2020-03-15 11:44:13', 'admin', '2020-03-15 13:49:51'),(36, 275, 12, NULL, NULL, 'admin', '2020-05-05 19:16:12', 'admin', '2020-05-05 11:23:48'),(37, 1, 1, '2020-01-16 19:25:12', '2020-01-31 19:25:16', NULL, NULL, NULL, NULL),(38, 723, 3, NULL, NULL, 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47'),(39, 723, 13, NULL, NULL, 'admin', '2022-09-23 19:14:36', 'admin', '2022-09-23 19:14:36'),(40, 728, 13, NULL, NULL, 'admin', '2022-09-23 19:14:36', 'admin', '2022-09-25 09:33:53');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_framework_resource` WRITE;
DELETE FROM `t_framework_resource`;
INSERT INTO `t_framework_resource` (`resource_id`,`resource_code`,`resource_name`,`resource_path`,`method`,`belong_microservice`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (-1001, 'saveOrUpdateUser', '用户角色授权', '/admin/user', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(-1000, 'saveOrUpdateRoles', '操作权限授权', '/admin/role/', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(1, 'datainterface_page_query', '查询接口列表分页', '系统管理/接口配置', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(3, 'datainterface_params_page_query', '查询接口参数列表分页', '系统管理/接口配置', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(32, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(33, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(34, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(35, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(36, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(37, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(38, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(60, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2022-09-25 09:31:08', 'admin', '2022-09-25 09:31:08'),(61, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(62, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2022-09-25 09:31:08', 'admin', '2022-09-25 09:31:08'),(63, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2022-09-25 09:31:08', 'admin', '2022-09-25 09:31:08'),(64, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2022-09-25 09:31:08', 'admin', '2022-09-25 09:31:08'),(65, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2022-09-25 09:31:08', 'admin', '2022-09-25 09:31:08'),(66, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2022-09-25 09:31:08', 'admin', '2022-09-25 09:31:08'),(86, '删除文件', '管理员后台删除文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(87, '查询上传文件', '管理员后台查询文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(88, '下载文件', '管理员后台下载文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(89, '查看文件', '管理员后台查看文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(90, '删除文件', '删除文件，仅登录用户删除自己的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(91, '查询个人上传文件', '查询个人上传文件，需登录', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(92, '下载本人文件', '下载文件，仅可下载本人的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(93, '查看本人文件', '查看本人文件，仅可查看本人的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(94, '上传文件', '上传文件，需要登录', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(95, '刷新缓存', '刷新缓存，需授权', '系统管理/数据字典', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(96, '保存或者更新', '保存或者更新数据字典', '系统管理/数据字典', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(102, '更新数据接口', '更新数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(103, '生效或者失效数据接口', '生效或者失效数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(104, '查询数据接口', '查询数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(106, 'resetPassword', '重置密码', '/admin/user', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(107, 'updatePassWordByUser', '用户更新密码', '/userinfo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(165, 'decrypt', 'des加密', 'common/aes', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(166, 'insertMenu', '插入菜单', '/admin/menu', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(167, 'listAllTreeMenuByParentId', '根据ID查询菜单及子菜单', '/admin/menu', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(168, 'refreshCache', '刷新菜单缓存', '/admin/menu', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(169, 'updateMenuById', '根据ID更新菜单', '/admin/menu', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(170, '获取操作日志', '管理员后台查询日志，需要授权', '/admin/mongo/OperateLog', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(176, 'queryDataDimensionListByTypeAndId', '按类型查询数据权限', '/admin/dataDimension', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(178, 'insertOrUpdateBatch', '批量更新数据权限', '/admin/dataDimension', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(181, 'selectDataDimensionByUserId', '查询用户有效的数据权限列表', '/admin/dataDimension', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(183, '内部使用的获取当前用户扩展属性', '', '/commonServiceInner', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(185, 'disabledUser', '禁用用户', '/userinfo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(187, 'enabledUser', '启用用户', '/userinfo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(190, 'checkCurrentUserOperater', '校验当前用户的操作权限列表', '/userinfo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(193, 'checkUserGoogleSecretBindStatus', '校验谷歌验证绑定状态', '/common/user/mfa', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(194, 'bindUserGoogleSecret', '绑定谷歌验证', '/common/user/mfa', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(197, 'resetBindUserGoogleSecretFlag', '重置谷歌验证码状态', '/common/user/mfa', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(198, 'checkCurrentUserGoogleCode', '校验当前用户的谷歌验证码', '/common/user/mfa', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(205, 'decrypt', 'des加密', 'common/aes', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(214, '/admin-addArticle', '添加文章或分类', '/admin/article', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(215, '/admin-updateArticle', '修改文章或分类', '/admin/article', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(216, '/admin-articleList', '管理员获取文章或分类列表', '/admin/article', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(217, '/admin-get', '根据id获取文章或列表详细信息', '/admin/article', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(218, '/admin-addPhoto', '添加图片', '/admin/photo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(219, '/admin-updatePhoto', '修改图片', '/admin/photo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(220, '/admin-updatePhotoStatus', '修改图片状态', '/admin/photo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(221, '/admin-getPhotoList', '获取图片列表', '/admin/photo', 'BYUSERPERMISSION', 'common-service', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_framework_resource2` WRITE;
DELETE FROM `t_framework_resource2`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_microservice_register` WRITE;
DELETE FROM `t_microservice_register`;
INSERT INTO `t_microservice_register` (`microservice_id`,`microservice_name`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (15, 'common-service', 'admin', '2022-09-25 09:31:06', 'admin', '2022-09-25 09:31:06'),(16, 'spring-gateway', 'admin', '2022-09-25 09:31:07', 'admin', '2022-09-25 09:31:07'),(17, 'unknow-account-service', 'admin', '2022-09-25 09:31:08', 'admin', '2022-09-25 09:31:08'),(18, 'unknow-mining-service', 'admin', '2022-09-25 09:31:09', 'admin', '2022-09-25 09:31:09'),(19, 'unknow-wallet-service-v2', 'admin', '2022-09-26 08:52:12', 'admin', '2022-09-26 08:52:12');
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_system_dic_item` WRITE;
DELETE FROM `t_system_dic_item`;
INSERT INTO `t_system_dic_item` (`dic_item_id`,`dic_master_id`,`dic_item_code`,`dic_item_name`,`dic_item_value`,`ext_attribut1`,`ext_attribut3`,`ext_attribut2`,`ext_attribut4`,`ext_attribut5`,`status`,`create_by`,`create_date`,`update_by`,`update_date`,`language`,`seq`) VALUES (1, 1, 'company_name', 'XXXX有限公司', 'XXXX有限公司', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-02-22 17:42:19', 1, '2020-08-10 19:55:20', 'zh_CN', 10),(13, 15, 'active', '有效', '1', '1', NULL, '12', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 15),(14, 15, 'inactive', '无效', '0', '2', NULL, '', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 20),(15, 15, 'expired', '过期', '-1', '3', NULL, '', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 25),(16, 16, 'commonService', '通用基础服务', 'COMMON-SERVICE', '', NULL, '', NULL, NULL, 1, 1, '2020-03-04 12:40:56', 1, '2020-07-28 13:41:03', 'zh_CN', 5),(20, 16, 'spring-gateway', 'SPRING网关服务', 'SPRING-GATEWAY', '', NULL, '', NULL, NULL, 1, 1, '2020-03-04 12:40:56', 1, '2020-07-28 13:41:03', 'zh_CN', 5),(25, 16, 'General', '所有微服务通用', 'General', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-05 10:32:09', 1, '2020-07-28 13:41:03', 'zh_CN', 0),(44, 28, 'admin', '后台管理用户', 'admin', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:51:50', 1, '2020-03-18 02:29:07', 'zh_CN', 5),(45, 28, 'wechatApplet', '微信小程序用户', 'wechatApplet', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:55:03', 1, '2020-03-18 02:29:07', 'zh_CN', 10),(46, 29, 'background', '后台添加', 'background', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:57:41', 1, '2020-03-18 01:57:41', 'zh_CN', 5),(47, 30, 'Reset', '重置', '3', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 20),(48, 30, 'New', '新建', '2', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 15),(49, 30, 'Inactive', '失效', '-1', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 10),(50, 30, 'Active', '生效', '1', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 5),(100, 16, 'UNKNOW-ACCOUNT-SERVICE', 'UNKNOW-ACCOUNT-SERVICE', 'UNKNOW-ACCOUNT-SERVICE', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-07-28 13:41:03', 1, '2020-07-28 13:41:03', 'zh_CN', 20),(101, 16, 'UNKNOW-WALLET-SERVICE', 'UNKNOW-WALLET-SERVICE', 'UNKNOW-WALLET-SERVICE', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-07-28 13:41:03', 1, '2020-07-28 13:41:03', 'zh_CN', 15),(106, 48, 'isMfaVerify', NULL, 'false', NULL, NULL, NULL, NULL, NULL, 1, 1, '2021-06-03 16:21:54', 1, '2021-06-03 16:21:54', 'zh_CN', 0),(107, 49, '菜单', '菜单', '1', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:19:17', 1, '2022-05-16 17:19:17', 'zh_CN', 5),(108, 49, '目录', '目录', '0', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:19:17', 1, '2022-05-16 17:19:17', 'zh_CN', 5),(109, 50, '任何时候都显示', '任何时候都显示\n', '2', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:22:34', 1, '2022-05-16 17:22:34', 'zh_CN', 5),(110, 50, '有权限时显示', '有权限时显示\n', '1', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:22:34', 1, '2022-05-16 17:22:34', 'zh_CN', 5),(111, 50, '有子菜单时显示', '有子菜单时显示\n', '0', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:22:34', 1, '2022-05-16 17:22:34', 'zh_CN', 5);
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `t_system_dic_master` WRITE;
DELETE FROM `t_system_dic_master`;
INSERT INTO `t_system_dic_master` (`dic_master_id`,`dic_code`,`dic_name`,`remark`,`belong_micro_service`,`parent_master_id`,`status`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (1, 'systemBaseConfig', '系统基础配置', '系统基础配置', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-03-22 07:14:57'),(15, 'commStatus', '系统通用的状态值', '通用状态', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-05-05 19:17:31'),(16, 'microServiceList', '微服务列表', '微服务列表', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-07-28 13:41:03'),(28, 'userType', '用户类型', '用户的来源类型，各个应用，后台创建的就属于admin，其它的属于各个应用注册而来，可以定义，目前微信小程序是微信小程序的名称。', 'General', NULL, 1, 1, '2020-03-18 01:51:50', 1, '2020-03-18 02:29:07'),(29, 'userRegisterSource', '用户注册来源', '用户的注册来源，后台添加的为：background 微信小程序的为微信小程序的ID', 'General', NULL, 1, 1, '2020-03-18 01:57:41', 1, '2020-08-11 20:44:48'),(30, 'userStatus', '用户状态', '用户状态', 'COMMON-SERVICE', NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06'),(48, 'systemConfig', '系统配置', NULL, 'General', NULL, 1, 1, '2021-06-03 16:20:11', 2, '2021-06-03 16:20:16'),(49, 'menuType', '菜单类型', NULL, 'General', NULL, 1, 1, '2022-05-16 17:19:17', 1, '2022-05-16 17:19:17'),(50, 'menuShowType', '显示方式', '0：有子菜单时显示 1：有权限时显示 2：任何时候都显示\n', 'General', NULL, 1, 1, '2022-05-16 17:22:34', 1, '2022-05-16 17:22:34'),(51, 'data_dimension', '数据维度', '数据维度', 'General', NULL, 1, 1, '2021-06-24 15:31:02', 1, '2021-07-04 21:09:17');
UNLOCK TABLES;
COMMIT;
