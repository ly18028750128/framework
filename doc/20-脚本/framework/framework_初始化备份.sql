/*
MySQL Backup
Database: template_framework
Backup Time: 2023-08-16 19:13:34
*/

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS `template_framework`.`t_article`;
DROP TABLE IF EXISTS `template_framework`.`t_email_sender_config`;
DROP TABLE IF EXISTS `template_framework`.`t_email_template`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_auth_type`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_data_dimension`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_data_interface`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_data_interface_params`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_data_rest_config`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_data_sp_config`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_data_sql_config`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_export_template`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_import_export_task`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_menu`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_menu_data`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_menu_old`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_menu_resource`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_org`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_role`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_role_data`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_role_data_interface`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_role_menu`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_role_menu_resource`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_role_resource`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_user`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_user_auth`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_user_ref`;
DROP TABLE IF EXISTS `template_framework`.`t_frame_user_role`;
DROP TABLE IF EXISTS `template_framework`.`t_framework_resource`;
DROP TABLE IF EXISTS `template_framework`.`t_framework_resource2`;
DROP TABLE IF EXISTS `template_framework`.`t_microservice_register`;
DROP TABLE IF EXISTS `template_framework`.`t_photo`;
DROP TABLE IF EXISTS `template_framework`.`t_system_dic_item`;
DROP TABLE IF EXISTS `template_framework`.`t_system_dic_master`;
DROP PROCEDURE IF EXISTS `template_framework`.`up_temp_dic_create`;
CREATE TABLE `t_article` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `node_type` int NOT NULL COMMENT '类型（1分类；2文章）',
  `language_type` int NOT NULL COMMENT '分类时语言类型（1中文，2英文,3泰文）',
  `parent_id` int NOT NULL DEFAULT '0' COMMENT '所属分类id',
  `class_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '分类编码（当前为分类时不能为空）',
  `article_sort` int NOT NULL DEFAULT '0' COMMENT '排序号（越大越靠前）',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题(分类名或文章标题)',
  `sub_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '副标题',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文章封面图',
  `article_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '文章内容',
  `article_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文章作者',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态（1正常；0禁用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `class_code_unique` (`class_code`,`language_type`) USING BTREE COMMENT '分类名称组合语言类型唯一',
  KEY `select_inx_1` (`node_type`,`status`,`parent_id`) USING BTREE,
  KEY `select_inx_2` (`node_type`,`class_code`,`status`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `t_email_sender_config` (
  `email_sender_id` int NOT NULL AUTO_INCREMENT,
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱密码：aes加密',
  `host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮件发送服务器',
  `port` int NOT NULL DEFAULT '465' COMMENT '邮件端口，默认为SSL的端口465',
  `protocol` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'smtp' COMMENT '协议，默认为SMTP',
  `default_encoding` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'UTF-8' COMMENT 'utf-8',
  `tls_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '开启SSL，默认开启',
  `starttls_enabled` tinyint(1) NOT NULL DEFAULT '0' COMMENT '开启STARTTLS',
  `CREATED_BY` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `CREATED_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDATED_BY` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `UPDATED_DATE` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1：有效 0：无效',
  PRIMARY KEY (`email_sender_id`) USING BTREE,
  UNIQUE KEY `udx_email_sender` (`user_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `t_frame_auth_type` (
  `auth_type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限类型ID',
  `auth_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限编码',
  `auth_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '权限类型：角色（role）/微服务权限（mircroservice）/组机机构（organization）/其它数据权限（可定义）',
  `remark` varchar(1000) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`auth_type_id`) USING BTREE,
  UNIQUE KEY `uidx_f_auth_type_code` (`auth_code`) USING BTREE,
  KEY `ak_kid` (`auth_type_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
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
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='数据权限列表\r\n角色类型为功能权限之外的权限统一为功能权限';
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='数据接口定义';
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='REST服务数据接口';
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='存储过程数据接口表';
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
CREATE TABLE `t_frame_export_template` (
  `template_id` int NOT NULL AUTO_INCREMENT,
  `template_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板编码',
  `template_type` tinyint NOT NULL DEFAULT '10' COMMENT '模板类型：10(excel),20(pdf),30(word)',
  `file_id` varchar(255) NOT NULL COMMENT '文件ID，存储在mongodb里的文件ObjectId',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态，1/有效 0/无效',
  `create_by` bigint NOT NULL COMMENT '创建人ID',
  `create_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_by` bigint NOT NULL COMMENT '更新人ID',
  `update_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`template_id`),
  UNIQUE KEY `udx_export_template` (`template_code`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='导出模板表';
CREATE TABLE `t_frame_import_export_task` (
  `task_id` bigint NOT NULL AUTO_INCREMENT,
  `task_type` tinyint NOT NULL COMMENT '任务类型：1(导入) 2(导出)',
  `task_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '任务名称',
  `file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '导入或者导出的文件名',
  `file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件id，用于存储导入或者导出的文件objectId',
  `error_file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '导入或者导出出错时保存错误的文件名',
  `error_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '文件id，用于存储导入或者导出出错时的文件objectId',
  `data_count` bigint NOT NULL DEFAULT '0' COMMENT '任务总行数',
  `data_error_count` bigint NOT NULL DEFAULT '0' COMMENT '错误行数',
  `data_correct_count` bigint NOT NULL DEFAULT '0' COMMENT '正确行数',
  `start_time` datetime DEFAULT NULL COMMENT '开始执行时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束执行时间',
  `execute_seconds` bigint GENERATED ALWAYS AS (timestampdiff(SECOND,`start_time`,`end_time`)) STORED,
  `message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '返回消息',
  `template_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '模板编码',
  `task_status` tinyint NOT NULL DEFAULT '1' COMMENT '任务状态：1(未执行) 2(执行中)3(执行成功)-1(执行失败)',
  `belong_microservice` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT 'common' COMMENT '所属微服务',
  `process_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '线程执行类的名称，导入继承ImportCallableService，导出继承ExportCallableService',
  `params` json DEFAULT NULL COMMENT '任务参数',
  `create_by` bigint NOT NULL COMMENT '创建人ID',
  `create_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_by` bigint NOT NULL COMMENT '更新人ID',
  `update_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `data_dimension` json DEFAULT NULL COMMENT '数据权限，记录当时登录用户的数据权限，方便代码中控制权限',
  PRIMARY KEY (`task_id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=10137 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='系统菜单';
CREATE TABLE `t_frame_menu_data` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `data_code` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`menu_code`,`data_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE,
  KEY `ak_kid2` (`id`) USING BTREE,
  KEY `fk_reference_35` (`data_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
CREATE TABLE `t_frame_menu_old` (
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
) ENGINE=InnoDB AUTO_INCREMENT=965 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='系统菜单';
CREATE TABLE `t_frame_menu_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `resource_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  PRIMARY KEY (`menu_code`,`resource_code`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE,
  KEY `fk_reference_23` (`resource_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='菜单资源相关资源';
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='系统角色';
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb3 COMMENT='数据权限列表\r\n角色类型为功能权限之外的权限统一为功能权限';
CREATE TABLE `t_frame_role_data_interface` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL COMMENT '角色id',
  `data_interface_id` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '接口名称的创建时的md5值存放在mongodb中，请参考DataInterFaceVO',
  `belong_microservice` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '所属微服务',
  `data_interface_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '接口名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `udx_frame_role_data_interface` (`role_id`,`data_interface_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=290 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='角色数据接口权限';
CREATE TABLE `t_frame_role_menu` (
  `role_menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '系统角色id',
  `role_id` int NOT NULL COMMENT '角色id',
  `menu_id` bigint DEFAULT NULL,
  `role_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`role_menu_id`) USING BTREE,
  KEY `ak_kid` (`role_menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='系统角色菜单';
CREATE TABLE `t_frame_role_menu_resource` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_menu` bigint DEFAULT NULL,
  `menu_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `resource_code` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE,
  KEY `fk_reference_25` (`menu_code`) USING BTREE,
  KEY `fk_reference_26` (`menu_code`,`resource_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='系统角色菜单资源';
CREATE TABLE `t_frame_role_resource` (
  `role_resource_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色菜单资源ID',
  `role_id` int DEFAULT NULL COMMENT '角色id',
  `resource_id` bigint DEFAULT NULL,
  PRIMARY KEY (`role_resource_id`) USING BTREE,
  KEY `ak_kid` (`role_resource_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15072 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='角色资源列表';
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
) ENGINE=InnoDB AUTO_INCREMENT=768 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='用户表';
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
CREATE TABLE `t_frame_user_ref` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `attribute_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL,
  `attribute_value` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `remark` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`,`attribute_name`) USING BTREE,
  KEY `ak_kid` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb3;
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
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin;
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
) ENGINE=InnoDB AUTO_INCREMENT=455 DEFAULT CHARSET=utf8mb3 COMMENT='系统资源，用来记录所有的系统的URI服务资源\r\n\r\n可以定义参数类型并进行参数类型的检查';
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_bin COMMENT='系统资源，用来记录所有的系统的URI服务资源\n可以定义参数类型并进行参数类型的检查';
CREATE TABLE `t_microservice_register` (
  `microservice_id` int NOT NULL AUTO_INCREMENT,
  `microservice_name` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '微服务名称',
  `create_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `update_date` datetime DEFAULT NULL,
  PRIMARY KEY (`microservice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb3 COMMENT='系统微服务注册表';
CREATE TABLE `t_photo` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `photo_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '启动页-LAUNCH_PAGE；首页-BANNER',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序号（越大越靠前）',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片资源地址',
  `jump_url` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '跳转链接',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态（1正常；0禁用）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `photo_desc` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '图片说明',
  `language_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '1中文，2英文',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
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
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统数据字典字典项表';
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
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='系统数据字典主表';
CREATE DEFINER=`root`@`%` PROCEDURE `up_temp_dic_create`()
BEGIN
  set @dicCode = 'accountWithdrawChargeType';
	delete from `t_system_dic_master`  where dic_code = @dicCode;

	INSERT INTO `t_system_dic_master` (`dic_code`, `dic_name`, `remark`, `belong_micro_service`, `parent_master_id`, `status`, `create_by`, `update_by`)
	 VALUES ( @dicCode, '提现手续费类型', NULL, 'General', NULL, 1, 1, 1);
	 
	 select dic_master_id into @master_id from `t_system_dic_master`  where dic_code = @dicCode;

	DELETE FROM  `t_system_dic_item` where `dic_master_id` = @master_id;
	
	INSERT INTO `t_system_dic_item` ( `dic_master_id`, `dic_item_code`, `dic_item_name`, `dic_item_value`, `ext_attribut1`, `ext_attribut3`, `ext_attribut2`, `ext_attribut4`, `ext_attribut5`, `status`, `create_by`, `update_by`, `language`, `seq`) 
	VALUES ( @master_id, '10', '按提现比例', '10', NULL, NULL, NULL, NULL, NULL, 1, 1, 1, 'zh_CN', 5),
				( @master_id, '20', '单笔固定', '20', NULL, NULL, NULL, NULL, NULL, 1, 1, 1, 'zh_CN', 10);
	
	

END;
BEGIN;
LOCK TABLES `template_framework`.`t_article` WRITE;
DELETE FROM `template_framework`.`t_article`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_email_sender_config` WRITE;
DELETE FROM `template_framework`.`t_email_sender_config`;
INSERT INTO `template_framework`.`t_email_sender_config` (`email_sender_id`,`user_name`,`password`,`host`,`port`,`protocol`,`default_encoding`,`tls_enabled`,`starttls_enabled`,`CREATED_BY`,`CREATED_DATE`,`UPDATED_BY`,`UPDATED_DATE`,`status`) VALUES (1, 'com_unknow_first@126.com', '2feb5e63e0f9fd03019220219fd1014a16bcab01629e6f3ecaeb83b6447d0026', 'smtp.126.com', 465, 'smtp', 'UTF-8', 1, 0, 'admin', '2022-12-10 16:36:29', 'admin', '2023-03-09 15:55:56', 1)
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_email_template` WRITE;
DELETE FROM `template_framework`.`t_email_template`;
INSERT INTO `template_framework`.`t_email_template` (`template_id`,`template_code`,`language`,`subject`,`from_address`,`to_address`,`template_text`,`CC`,`BCC`,`CREATED_BY`,`CREATED_DATE`,`UPDATED_BY`,`UPDATED_DATE`) VALUES (1, 'IP_LOGIN_LOCKER_EMAIL', 'zh_CN', 'ip: {{ip}} is locked!', 'com_unknow_first@126.com', '609925924@qq.com,longyou88888@126.com,com_unknow_first@126.com', '\n<div>\n    <div align=\"center\">\n        <div class=\"open_email\" style=\"margin-left: 8px; margin-top: 8px; margin-bottom: 8px; margin-right: 8px;\">\n            <div>\n                <br>\n                <span class=\"genEmailContent\">\n                    <div id=\"cTMail-Wrap\"\n                         style=\"word-break: break-all;box-sizing:border-box;text-align:center;min-width:320px; max-width:660px; border:1px solid #f6f6f6; background-color:#f7f8fa; margin:auto; padding:20px 0 30px; font-family:\'helvetica neue\',PingFangSC-Light,arial,\'hiragino sans gb\',\'microsoft yahei ui\',\'microsoft yahei\',simsun,sans-serif\">\n                        <div class=\"main-content\" style=\"\">\n                            <table style=\"width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse\">\n                                <tbody>\n                                <tr style=\"font-weight:300\">\n                                    <td style=\"width:3%;max-width:30px;\"></td>\n                                    <td style=\"max-width:600px;\">\n                                        <div id=\"cTMail-logo\" style=\"width:500px; height:25px;\">\n                                            <h1 th:text=\"${ip}+\',登录异常预警\'\" id=\"cTMail-title\" style=\"font-size: 20px; line-height: 36px; margin: 0px 0px 22px;\">\n                                                登录预警\n                                           </h1>\n                                        </div>\n                                        <p style=\"height:2px;background-color: #00a4ff;border: 0;font-size:0;padding:0;width:100%;margin-top:20px;\"></p>\n\n                                        <div id=\"cTMail-inner\" style=\"background-color:#fff; padding:23px 0 20px;box-shadow: 0px 1px 1px 0px rgba(122, 55, 55, 0.2);text-align:left;\">\n                                            <table style=\"width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse;text-align:left;\">\n                                                <tbody>\n                                                <tr style=\"font-weight:300\">\n                                                    <td style=\"width:3.2%;max-width:30px;\"></td>\n                                                    \n                                                    <td style=\"max-width:480px;text-align:left;\">\n\n                                                        <p th:text=\"\'最后登录IP：\' + ${ip}\" id=\"cTMail-userName\" style=\"font-size:14px;color:#333; line-height:24px; margin:0;\">\n                                                           ip\n                                                        </p>\n\n                                                        <p th:text=\"\'IP地址登录失败次数过多，IP地址将被禁止登录直到\'+${unlockedDate}\" class=\"cTMail-content\" style=\"line-height: 24px; margin: 6px 0px 0px; overflow-wrap: break-word; word-break: break-all;\">\n                                                            <span style=\"color: red; font-size: 16px; font-weight: 700\">\n                                                               错误提示\n                                                            </span>\n                                                        </p>\n                                                    </td>\n                                                    <td style=\"width:3.2%;max-width:30px;\"></td>\n                                                </tr>\n                                                </tbody>\n                                            </table>\n                                        </div>\n                                    </td>\n                                    <td style=\"width:3%;max-width:30px;\"></td>\n                                </tr>\n                                </tbody>\n                            </table>\n                        </div>\n                    </div>\n                </span>\n\n            </div>\n        </div>\n    </div>\n</div>', '', '', 'admin', '2022-06-23 09:54:27', 'admin', '2023-03-09 15:45:04'),(2, 'USER_LOGIN_LOCKER_EMAIL', 'zh_CN', 'User {{userName}} is locked!', 'com_unknow_first@126.com', '609925924@qq.com,longyou88888@126.com,com_unknow_first@126.com', '\r\n<div>\r\n    <div align=\"center\">\r\n        <div class=\"open_email\" style=\"margin-left: 8px; margin-top: 8px; margin-bottom: 8px; margin-right: 8px;\">\r\n            <div>\r\n                <br>\r\n                <span class=\"genEmailContent\">\r\n                    <div id=\"cTMail-Wrap\"\r\n                         style=\"word-break: break-all;box-sizing:border-box;text-align:center;min-width:320px; max-width:660px; border:1px solid #f6f6f6; background-color:#f7f8fa; margin:auto; padding:20px 0 30px; font-family:\'helvetica neue\',PingFangSC-Light,arial,\'hiragino sans gb\',\'microsoft yahei ui\',\'microsoft yahei\',simsun,sans-serif\">\r\n                        <div class=\"main-content\" style=\"\">\r\n                            <table style=\"width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse\">\r\n                                <tbody>\r\n                                <tr style=\"font-weight:300\">\r\n                                    <td style=\"width:3%;max-width:30px;\"></td>\r\n                                    <td style=\"max-width:600px;\">\r\n                                        <div id=\"cTMail-logo\" style=\"width:500px; height:25px;\">\r\n                                            <h1 th:text=\"${serviceName}+\'用户登录异常预警\'\" id=\"cTMail-title\" style=\"font-size: 20px; line-height: 36px; margin: 0px 0px 22px;\">\r\n                                                登录预警\r\n                                           </h1>\r\n                                        </div>\r\n                                        <p style=\"height:2px;background-color: #00a4ff;border: 0;font-size:0;padding:0;width:100%;margin-top:20px;\"></p>\r\n\r\n                                        <div id=\"cTMail-inner\" style=\"background-color:#fff; padding:23px 0 20px;box-shadow: 0px 1px 1px 0px rgba(122, 55, 55, 0.2);text-align:left;\">\r\n                                            <table style=\"width:100%;font-weight:300;margin-bottom:10px;border-collapse:collapse;text-align:left;\">\r\n                                                <tbody>\r\n                                                <tr style=\"font-weight:300\">\r\n                                                    <td style=\"width:3.2%;max-width:30px;\"></td>\r\n                                                    \r\n                                                    <td style=\"max-width:480px;text-align:left;\">\r\n\r\n\r\n                                                        <p th:text=\"\'用户名:\' + ${userName}\" id=\"cTMail-userName\" style=\"font-size:14px;color:#333; line-height:24px; margin:0;\">\r\n                                                           userName\r\n                                                        </p>\r\n\r\n\r\n                                                        <p th:text=\"\'最后登录IP：\' + ${ip}\" id=\"cTMail-userName\" style=\"font-size:14px;color:#333; line-height:24px; margin:0;\">\r\n                                                           ip\r\n                                                        </p>\r\n\r\n                                                        <p th:text=\"\'用户登录连续登录失败次数过多，用户将被锁定到\'+${unlockedDate}\" class=\"cTMail-content\" style=\"line-height: 24px; margin: 6px 0px 0px; overflow-wrap: break-word; word-break: break-all;\">\r\n                                                            <span style=\"color: red; font-size: 16px; font-weight: 700\">\r\n                                                               提示\r\n                                                            </span>\r\n                                                        </p>\r\n                                                    </td>\r\n                                                    <td style=\"width:3.2%;max-width:30px;\"></td>\r\n                                                </tr>\r\n                                                </tbody>\r\n                                            </table>\r\n                                        </div>\r\n                                    </td>\r\n                                    <td style=\"width:3%;max-width:30px;\"></td>\r\n                                </tr>\r\n                                </tbody>\r\n                            </table>\r\n                        </div>\r\n                    </div>\r\n                </span>\r\n\r\n            </div>\r\n        </div>\r\n    </div>\r\n</div>', NULL, NULL, 'admin', '2022-06-23 09:54:27', 'admin', '2022-12-11 13:30:14')
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_auth_type` WRITE;
DELETE FROM `template_framework`.`t_frame_auth_type`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_data_dimension` WRITE;
DELETE FROM `template_framework`.`t_frame_data_dimension`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_data_interface` WRITE;
DELETE FROM `template_framework`.`t_frame_data_interface`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_data_interface_params` WRITE;
DELETE FROM `template_framework`.`t_frame_data_interface_params`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_data_rest_config` WRITE;
DELETE FROM `template_framework`.`t_frame_data_rest_config`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_data_sp_config` WRITE;
DELETE FROM `template_framework`.`t_frame_data_sp_config`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_data_sql_config` WRITE;
DELETE FROM `template_framework`.`t_frame_data_sql_config`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_export_template` WRITE;
DELETE FROM `template_framework`.`t_frame_export_template`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_import_export_task` WRITE;
DELETE FROM `template_framework`.`t_frame_import_export_task`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_menu` WRITE;
DELETE FROM `template_framework`.`t_frame_menu`;
INSERT INTO `template_framework`.`t_frame_menu` (`menu_id`,`menu_code`,`menu_name`,`menu_url`,`component_path`,`type`,`show_type`,`icon`,`function_resource_code`,`status`,`seq_no`,`parent_menu_id`,`create_by`,`create_date`,`update_by`,`update_date`,`meta_data`) VALUES (1000, 'home', '首页', '/home', 'home/index', 0, 2, 'iconfont icon-shouye', '', 1, 0, NULL, NULL, NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":true,\"icon\":\"iconfont icon-shouye\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"首页\"}'),(1001, 'system', '系统设置', 'system/config', 'layout/routerView/parent', 0, 2, 'iconfont icon-xitongshezhi', '', 1, 300, NULL, NULL, NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-xitongshezhi\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"/system/menu\",\"roles\":[],\"title\":\"系统设置\"}'),(10000, 'systemMenu', '菜单管理', '/system/menu', 'system/menu/index', 0, 1, 'iconfont icon-diannao1', 'common-service::/admin/menu::listAllTreeMenuByParentId', 1, 10, 1001, NULL, NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-diannao1\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"菜单管理\"}'),(10001, 'systemUser', '用户管理', '/system/user', 'system/systemUserdmin/index', 0, 1, 'iconfont icon-gerenzhongxin', 'common-service::/admin/user::saveOrUpdateUser', 1, 20, 1001, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-gerenzhongxin\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"用户管理\"}'),(10020, 'microservice', '微服务管理', '/system/microservice', 'system/microservice/index', 0, 1, 'iconfont icon-putong', 'common-service::/discovery/client::/register/microservice', 1, 60, 1001, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-putong\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"微服务管理\"}'),(10021, 'dataDicManager', '数据字典管理', '/system/dataDicManager', 'system/dicManager/index', 0, 1, 'iconfont icon-shuxingtu', 'common-service::系统管理/数据字典::保存或者更新', 1, 40, 1001, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-shuxingtu\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"数据字典管理\"}'),(10022, 'quartzManager', '定时任务管理', '/system/quartzManager', 'system/quartzManager/index', 0, 1, 'ele-AlarmClock', 'common-service::/quartz/client::/getAll', 1, 1, 10034, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"ele-AlarmClock\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"定时任务管理\"}'),(10023, 'systemRoleAdmin', '角色管理', '/system/systemRoleAdmin', 'system/roleAdmin/index', 0, 1, 'iconfont icon-gongju', 'common-service::/admin/role/::saveOrUpdateRoles', 1, 30, 1001, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-gongju\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"角色管理\"}'),(10024, 'datainterface', '接口配置', '/system/datainterface', 'system/datainterface/index', 0, 1, 'iconfont icon-AIshiyanshi', 'common-service::/数据接口管理::查询数据接口', 1, 50, 1001, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-AIshiyanshi\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"接口配置\"}'),(10025, 'article', '文章管理', '/article', 'layout/routerView/parent', 0, 2, 'iconfont icon-bolangneng', '', 1, 500, NULL, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-bolangneng\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"/article/category_list\",\"roles\":[],\"title\":\"文章管理\"}'),(10029, 'fund_manage', '资金管理', '/fundmanage', 'layout/routerView/parent', 0, 0, 'iconfont icon-crew_feature', '', 1, 900, NULL, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-crew_feature\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"/fund/systemAccount\",\"roles\":[],\"title\":\"资金管理\"}'),(10034, 'menu_system_manager', '系统管理', 'system/manager', 'layout/routerView/parent', 0, 2, 'iconfont icon-wenducanshu-05', '', 1, 400, NULL, 'admin', '2023-02-15 11:17:01', 'admin', '2023-02-15 11:17:01', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-wenducanshu-05\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"系统管理\"}'),(10038, 'common_menu_message_log_manager', '消息日志管理', '/common_menu_message_log_manager', 'system/messageLog/index', 1, 1, 'iconfont icon-tongzhi3', 'common-service::/admin/message/logs::/config/list', 1, 10, 10034, 'admin', '2023-02-15 11:17:01', 'admin', '2023-02-15 11:17:01', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-tongzhi3\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"消息日志管理\"}'),(10039, 'menu_user_excel_parent', '导入导出管理', '/user/imexport/task', 'layout/routerView/parent', 0, 2, 'iconfont icon-caidan', '', 1, 600, NULL, 'admin', '2023-02-15 11:17:01', 'admin', '2023-02-15 11:17:01', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-caidan\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"导入导出管理\"}'),(10040, 'export_template_page', '导出模板管理', '/export_template_page', 'export/template/index', 1, 1, 'iconfont icon-gongju', 'common-service::/admin/export/template::/list', 1, 0, 10039, 'admin', '2023-02-15 11:17:01', 'admin', '2023-02-15 11:17:01', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-gongju\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"导出模板管理\"}'),(10041, 'menu_admin_imexport_task_page', '导入导出任务', '/menu_admin_imexport_task_page', 'imexport/admin/index', 1, 2, 'iconfont icon-ico_shuju', 'common-service::/admin/imexport/task::/list', 1, 0, 10039, 'admin', '2023-02-15 11:17:02', 'admin', '2023-02-15 11:17:02', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-ico_shuju\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"导入导出任务\"}'),(10042, 'menu_user_imexport_task_page', '我的导入导出', '/menu_user_imexport_task_page', 'imexport/myImexport/index', 1, 0, 'iconfont icon-gerenzhongxin', '', 1, 0, 10039, 'admin', '2023-02-15 11:17:02', 'admin', '2023-02-15 11:17:02', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-gerenzhongxin\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"我的导入导出\"}'),(10043, 'mongo_file_manage', '文件管理', '/mongo_file_manage', 'mongodb/gridfs/admin/index', 0, 1, 'ele-Folder', 'common-service::/admin/mongo::查看文件', 1, 3, 10034, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"ele-Folder\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"文件管理\"}'),(10044, 'personal_center', '个人中心', '/my', 'layout/routerView/parent', 0, 1, 'ele-UserFilled', 'three-body-service1::/flash/swap/pair/admin::/list', 1, 200, NULL, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"ele-UserFilled\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"个人中心\"}'),(10045, 'myfiles', '我的文件', '/myfiles', 'mongodb/gridfs/personal/index', 0, 2, 'iconfont icon--chaifenlie', 'common-service::/personal/mongo::查看本人文件', 1, 10, 10044, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon--chaifenlie\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"我的文件\"}'),(10046, 'publicFiles', '公共文件', '/publicFiles', 'mongodb/gridfs/public/index', 0, 2, 'iconfont icon-gongju', '', 1, 2, 10044, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-gongju\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"公共文件\"}'),(10047, 'common_menu_job_log_manager', '定时任务日志', '/common_menu_job_log_manager', 'system/jobLog/index', 1, 1, 'iconfont icon-bolangneng', 'common-service::/admin/job/logs::/list', 1, 2, 10034, 'admin', '2023-03-03 11:37:24', 'admin', '2023-03-03 11:37:24', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-bolangneng\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"定时任务日志\"}'),(10048, 'systemoperationlog', '操作日志', '/system/systemoperationlog', 'system/systemoperationlog/index', 0, 1, 'iconfont icon-shuaxin', 'common-service::/admin/mongo/OperateLog::获取操作日志', 1, 4, 10034, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-shuaxin\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"操作日志\"}'),(10049, 'block', '区块记录', '/block', 'layout/routerView/parent', 0, 2, 'iconfont icon-crew_feature', '', 1, 800, NULL, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-crew_feature\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"区块记录\"}'),(10091, 'menu_mail_manger', '邮件配置管理', '/email/admin', 'layout/routerView/parent', 0, 0, 'iconfont icon-crew_feature', 'common-service::/email/admin::/config/list', 1, 700, NULL, 'bjy', '2023-02-27 15:47:59', 'admin', '2023-02-27 15:47:59', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"ele-Suitcase\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"/menu_mail_template_config\",\"roles\":[],\"title\":\"邮件配置管理\"}'),(10092, 'menu_mail_template_config', '邮件模板配置', '/mail/template/index', 'mail/template/index', 1, 1, 'iconfont icon-wenducanshu-05', 'common-service::/email/admin::/config/template', 1, 0, 10091, 'bjy', '2023-02-27 15:47:59', 'admin', '2023-02-27 15:47:59', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-wenducanshu-05\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"邮件模板配置\"}'),(10093, 'common_menu_mail_sender_config', '邮件发送配置', '/mail/send/index', 'mail/send/index', 1, 1, 'iconfont icon-shouye_dongtaihui', 'common-service::/email/admin::/config/list', 1, 0, 10091, 'bjy', '2023-02-27 15:47:59', 'admin', '2023-02-27 15:47:59', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-shouye_dongtaihui\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"邮件发送配置\"}'),(10094, '/block/erc20/amount/index', 'ERC20持仓', '/block/erc20/amount/index', 'block/erc20/amount/index', 0, 1, 'iconfont icon-dongtai', 'chain-block-scan-service::/admin/tran::/admin-getErc20List', 1, 1, 10049, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-dongtai\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"ERC20持仓\"}'),(10095, '/block/erc20/tranrecord/index', 'ERC20转账记录', '/block/erc20/tranrecord/index', 'block/erc20/tranrecord/index', 0, 1, 'iconfont icon-dongtai', 'chain-block-scan-service::/admin/tran::/admin-getErc20TokenTransRecord', 1, 2, 10049, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-dongtai\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"ERC20转账记录\"}'),(10096, '/block/erc20/swaprecord/index', 'ERC20Swap记录', '/block/erc20/swaprecord/index', 'block/erc20/swaprecord/index', 0, 1, 'iconfont icon-shuaxin', 'chain-block-scan-service::/admin/tran::/admin-getSwapTokenRecord', 1, 3, 10049, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-shuaxin\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"ERC20Swap记录\"}'),(10097, '/block/erc20/tranlog/index', '合约交易记录', '/block/erc20/tranlog/index', 'block/erc20/tranlog/index', 0, 1, 'iconfont icon-dongtai', 'chain-block-scan-service::/admin/tran::/admin-getTranLogList', 1, 0, 10049, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-dongtai\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"合约交易记录\"}'),(10098, 'article_category_list', '文章分类', '/article/category_list', 'article/category/index', 0, 1, 'iconfont icon--chaifenlie', 'common-service::/admin/article::/admin-articleList', 1, 0, 10025, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon--chaifenlie\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"文章分类\"}'),(10099, 'photo_manage', '图片管理', '/article/photo', 'article/photo/index', 0, 1, 'iconfont icon-bolangnengshiyanchang', 'common-service::/admin/photo::/admin-getPhotoList', 1, 2, 10025, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-bolangnengshiyanchang\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"图片管理\"}'),(10100, 'article_list', '文章列表', '/article/list', 'article/articleList/index', 0, 1, 'ele-Calendar', 'common-service::/admin/article::/admin-articleList', 1, 1, 10025, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"ele-Calendar\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"文章列表\"}'),(10101, 'fund_systemAccount', '系统账户管理', '/fund/systemAccount', 'fund/systemAccount/index', 0, 1, 'iconfont icon-gerenzhongxin', 'three-body-service1::/admin/system/address::/list', 1, 1, 10029, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-gerenzhongxin\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"系统账户管理\"}'),(10102, 'fund_addressAccountCoinList', '币种列表', '/fund/addressAccountCoinList', 'fund/addressAccountCoinList/index', 0, 1, 'iconfont icon-wenducanshu-05', 'three-body-service1::/admin/coin/config::/coinList', 1, 1, 10029, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-wenducanshu-05\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"币种列表\"}'),(10103, 'fund_addressAccountList', '资金账户管理', '/fund/addressAccountList', 'fund/addressAccountList/index', 0, 1, 'iconfont icon--chaifenhang', 'three-body-service1::/admin/address/account::/accountList', 1, 1, 10029, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon--chaifenhang\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"资金账户管理\"}'),(10104, 'fund_addressAccountDetails', '资金记录', '/fund/addressAccountDetails', 'fund/addressAccountDetails/index', 0, 1, 'iconfont icon-shuju1', 'three-body-service1::/admin/address/account::/queryAccountDetail', 1, 1, 10029, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-shuju1\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"资金记录\"}'),(10105, 'fund_addressAccountAccountDetailsHistory', '资金历史记录', '/fund/addressAccountAccountDetailsHistory', 'fund/accountDetailsHistory/index', 0, 1, 'iconfont icon--chaifenlie', 'three-body-service1::/admin/address/account::/queryAccountDetailHistory', 1, 1, 10029, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon--chaifenlie\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"资金历史记录\"}'),(10114, '/block/nft/list/index', 'NFT列表', '/block/nft/list/index', 'block/nft/list/index', 0, 1, 'iconfont icon-dongtai', 'chain-block-scan-service::/admin/tran::/admin-getNFTList', 1, 5, 10049, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-dongtai\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"NFT列表\"}'),(10115, '/block/nft/tran/index', 'NFT交易', '/block/nft/tran/index', 'block/nft/tran/index', 0, 1, 'iconfont icon-shuaxin', 'chain-block-scan-service::/admin/tran::/admin-getNFTTranList', 1, 6, 10049, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-shuaxin\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"NFT交易\"}'),(10116, '/block/config', 'Chain&合约配置', '/block/config', 'layout/routerView/parent', 0, 2, 'iconfont icon-quanjushezhi_o', '', 1, 801, NULL, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-quanjushezhi_o\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"Chain&合约配置\"}'),(10117, '/block/config/chain/index', 'chain 配置', '/block/config/chain/index', 'block/config/chain/index', 0, 1, 'iconfont icon-shuaxin', 'chain-block-scan-service::/admin/chain::/admin-getChainList', 1, 1, 10116, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-shuaxin\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"chain 配置\"}'),(10118, '/block/config/contract/index', '合约配置', '/block/config/contract/index', '/block/config/contract/index', 0, 1, 'iconfont icon-shuaxin', 'chain-block-scan-service::/admin/contract::/admin-getContractList', 1, 2, 10116, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-shuaxin\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"合约配置\"}'),(10119, 'fund_withdrawalAudit', '提现审核', '/fund/withdrawalAudit', 'fund/withdrawalAudit/index', 0, 1, 'iconfont icon-caozuo-wailian', 'three-body-service1::/admin/address/account/detail::/authWithdraw', 1, 1, 10029, 'admin', NULL, 'admin', NULL, '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-caozuo-wailian\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"提现审核\"}'),(10134, 'flashSwapManager', '闪兑管理', '/flash/swap/record/admin', 'layout/routerView/parent', 0, 0, 'iconfont icon-shouye_dongtaihui', '', 1, 9000, NULL, 'admin', '2023-04-08 15:23:34', 'admin', '2023-04-08 15:23:34', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-shouye_dongtaihui\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"闪兑管理\"}'),(10135, 'flash/swap/pair', '交易队管理', '/flash/swap/pair', 'flash/swap/pair/index', 1, 1, 'iconfont icon-neiqianshujuchucun', 'three-body-service1::/flash/swap/pair/admin::/list', 1, 0, 10134, 'admin', '2023-04-08 15:23:34', 'admin', '2023-04-08 15:23:34', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-neiqianshujuchucun\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"交易队管理\"}'),(10136, 'flash/swap/record', '闪兑交易记录', '/flash/swap/record', 'flash/swap/record/index', 1, 1, 'iconfont icon-chazhaobiaodanliebiao', 'three-body-service1::/flash/swap/record/admin::/list', 1, 0, 10134, 'admin', '2023-04-08 15:23:34', 'admin', '2023-04-08 15:23:34', '{\"isKeepAlive\":true,\"isAffix\":false,\"icon\":\"iconfont icon-chazhaobiaodanliebiao\",\"isHide\":false,\"isIframe\":false,\"isLink\":\"\",\"redirect\":\"\",\"roles\":[],\"title\":\"闪兑交易记录\"}')
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_menu_data` WRITE;
DELETE FROM `template_framework`.`t_frame_menu_data`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_menu_old` WRITE;
DELETE FROM `template_framework`.`t_frame_menu_old`;
INSERT INTO `template_framework`.`t_frame_menu_old` (`menu_id`,`menu_code`,`menu_name`,`menu_url`,`component_path`,`type`,`show_type`,`icon`,`function_resource_code`,`status`,`seq_no`,`parent_menu_id`,`create_by`,`create_date`,`update_by`,`update_date`,`meta_data`) VALUES (-1000, 'menu_system_manager', '系统管理', '/admin', '/layout/Layout', 0, 2, 'sms', NULL, 1, 10, NULL, 'admin', '2020-05-07 19:08:52', 'admin', '2020-05-07 19:08:52', NULL),(-404, 'ERC20持仓', 'ERC20持仓', 'erc20List', '/log/erc20List', 1, 1, 'sms', 'chain-block-scan-service::/admin/tran::/admin-getErc20TokenTransRecord', 1, 20, -400, NULL, NULL, NULL, NULL, NULL),(-403, 'NFT交易', 'NFT交易', 'nftTranList', '/log/nftTranList', 1, 1, 'sms', 'chain-block-scan-service::/admin/tran::/admin-getErc20TokenTransRecord', 0, 50, -400, NULL, NULL, NULL, NULL, NULL),(-402, 'NFT记录', 'NFT记录', 'nftList', '/log/nftList', 1, 1, 'sms', 'chain-block-scan-service::/admin/tran::/admin-getErc20TokenTransRecord', 0, 40, -400, NULL, NULL, NULL, NULL, NULL),(-401, '事件', '事件', 'tranLog', '/log/tranLog', 1, 1, 'sms', 'chain-block-scan-service::/admin/tran::/admin-getErc20TokenTransRecord', 1, 90, -400, NULL, NULL, NULL, NULL, NULL),(-400, '区块记录', '区块记录', '/admin/log', '/layout/Layout', 0, 1, 'sms', '', 1, 60, NULL, NULL, NULL, NULL, NULL, NULL),(-302, '公链配置', '公链配置', 'chainConfig', '/chain/chainConfig', 1, 1, 'sms', 'chain-block-scan-service::/admin/chain::/admin-getChainList', 1, 60, -300, NULL, NULL, NULL, NULL, NULL),(-301, '合约配置', '合约配置', 'contractConfig', '/chain/contractConfig', 1, 1, 'sms', 'chain-block-scan-service::/admin/chain::/admin-getChainList', 1, 60, -300, NULL, NULL, NULL, NULL, NULL),(-300, '参数配置', '参数配置', '/admin/config', '/layout/Layout', 0, 1, 'sms', '', 1, 60, NULL, NULL, NULL, NULL, NULL, NULL),(2, '个人文件存储', '个人文件存储', 'personFileManager', '/user/personFileManager', 1, 2, 'folder', NULL, 1, 1, 938, NULL, NULL, NULL, NULL, '{\"pageType\": \"personal\"}'),(3, '公共文件查询', '公共文件查询', 'mongodbGridfsPublicFileQuery', '/user/publicFileQuery', 1, 2, 'folder', NULL, 1, 2, -1010, NULL, NULL, NULL, NULL, '{\"pageType\": \"public\"}'),(5, '定时任务管理', '定时任务管理', 'quartz/alljobs', '/admin/quartz/alljobs/index', 1, 1, 'sms-flash', 'common-service::定时任务/quartz::createOrUpdate', 1, 1, -1000, NULL, NULL, NULL, NULL, NULL),(6, '微服务管理', '微服务管理', 'microservice', '/admin/microservice/index', 1, 1, 'cloud', 'common-service::/admin/user::saveOrUpdateUser', 1, 2, -1000, NULL, NULL, NULL, NULL, NULL),(8, 'menu_datainterface_config', '接口配置', 'datainterface', '/admin/datainterface/index', 1, 1, 'controls-vertical-alt', 'common-service::/数据接口管理::查询数据接口', 1, 3, -1000, 'admin', '2020-05-05 11:08:52', 'admin', '2020-05-05 11:08:52', NULL),(11, 'systemRoleAdmin', '角色管理', 'systemRoleAdmin', '/admin/role/index', 1, 1, 'sms-flash', 'common-service::/admin/role/::saveOrUpdateRoles', 1, 4, -1000, NULL, NULL, NULL, NULL, NULL),(12, 'systemUserdmin', '用户管理', 'systemUserdmin', '/admin/user/index', 1, 1, 'user-1', 'common-service::/admin/user::saveOrUpdateUser', 1, 5, -1000, NULL, NULL, NULL, NULL, NULL),(15, '文件管理', '文件管理', 'fileManagerByAdmin', '/admin/mongodb/gridfs/index', 1, 1, 'folder', 'common-service::/admin/mongo::查看文件', 1, 6, -1000, NULL, NULL, NULL, NULL, '{\"pageType\": \"admin\"}'),(16, '数据字典管理', '数据字典管理', 'dataDicManager', '/admin/system/dic/index', 1, 1, 'folder', 'common-service::系统管理/数据字典::保存或者更新', 1, 2, -1000, NULL, NULL, NULL, NULL, NULL),(888, 'systemRouter', '菜单管理', 'systemRouter', '/admin/system/router', 1, 1, 'menu', 'common-service::/数据接口管理::查询数据接口', 1, 4, -1000, NULL, NULL, NULL, NULL, NULL),(920, 'ERC20转账记录', 'ERC20转账记录', 'admin-getErc20TokenTransRecord', '/log/TokenTranList', 1, 1, 'sms', 'chain-block-scan-service::/admin/tran::/admin-getErc20TokenTransRecord', 1, 30, -400, NULL, NULL, NULL, NULL, NULL),(921, '地址信息', '地址信息', 'AddressInfoQuery', '/dappAdmin/AddressInfoQuery', 1, 1, 'sms', 'land-admin-service::/addressInfo::/admin-queryAddressInfo', 0, 10, -400, NULL, NULL, NULL, NULL, NULL),(922, 'DAPP', 'DAPP', '/dapp', '/layout/Layout', 0, 0, 'sms', '', 0, 70, NULL, NULL, NULL, NULL, NULL, NULL),(923, '共管钱包', '共管钱包', 'CondominiumWallet', '/dapp/CondominiumWallet', 1, 1, 'sms', 'chain-block-scan-service::/admin/contract::/admin-updateContract', 1, 10, 922, NULL, NULL, NULL, NULL, NULL),(924, 'SWAP交易记录', 'SWAP交易记录', 'admin-getSwapTokenRecord', '/log/TokenSwapTranList', 1, 1, 'order', 'chain-block-scan-service::/admin/tran::/admin-getSwapTokenRecord', 1, 80, -400, NULL, NULL, NULL, NULL, NULL),(928, '用户管理', '用户管理', 'crowdfunding_queryCrowdfundingAddress', '/crowdfunding/queryCrowdfundingAddress', 1, 1, '', 'land-admin-service::/crowdfunding/admin::/queryCrowdfundingAddress', 1, 10, 933, NULL, NULL, NULL, NULL, NULL),(929, '资金明细', '资金明细', 'fund_details', '/crowdfunding/fundDetails', 1, 1, '', 'land-admin-service::/admin/address/account::/queryAccountDetail', 1, 30, 933, NULL, NULL, NULL, NULL, NULL),(930, '账户管理', '账户管理', 'crowdfunding_accountList', '/crowdfunding/accountList', 1, 1, '', 'land-admin-service::/admin/address/account::/accountList', 1, 10, 933, NULL, NULL, NULL, NULL, NULL),(932, '币种列表', '币种列表', 'coin_list', '/coin/manage/coin_list', 1, 1, 'sms', 'land-admin-service::/admin/coin/config::/coinList', 1, 10, 933, NULL, NULL, NULL, NULL, NULL),(933, '资金管理', '资金管理', '/land/tc', '/layout/Layout', 0, 2, 'sms', '', 0, 10, NULL, NULL, NULL, NULL, NULL, NULL),(938, '个人中心', '个人中心', '/personal/center', '/layout/Layout', 0, 0, 'sms', '', 1, 0, NULL, NULL, NULL, NULL, NULL, NULL),(941, '推荐图谱', '推荐图谱', 'getTeamTree', 'addressInfo/getTeamTree', 1, 1, 'sms-flash', 'land-admin-service::/crowdfunding/admin::/admin-getTeamTree', 1, 10, 933, NULL, NULL, NULL, NULL, NULL),(944, '转账记录', '转账记录', 'transferRecord', '/transfer/transferRecord', 1, 1, '', 'land-admin-service::/admin/address/transfer::/admin-list', 1, 10, 933, NULL, NULL, NULL, NULL, NULL),(946, '公告管理', '公告管理', '/article', '/layout/Layout', 0, 2, '', '', 1, 10, NULL, NULL, NULL, NULL, NULL, NULL),(947, '分类', '分类', 'category_list', '/article/manage/category_list', 1, 1, '', 'land-admin-service::/admin/article::/admin-articleList', 1, 10, 946, NULL, NULL, NULL, NULL, NULL),(948, '公告列表', '公告列表', 'article_list', '/article/manage/article_list', 1, 1, '', 'land-admin-service::/admin/article::/admin-articleList', 1, 10, 946, NULL, NULL, NULL, NULL, NULL),(949, '图片管理', '图片管理', 'photo', '/admin/user/photoList', 1, 1, '', 'land-admin-service::/admin/photo::/admin-getPhotoList', 1, 10, 946, NULL, NULL, NULL, NULL, NULL),(954, '资金账户管理', '资金账户管理', 'crowdfunding_accountList', '/crowdfunding/accountList', 1, 1, '', 'land-admin-service::/admin/address/account::/accountList', 1, 20, 933, NULL, NULL, NULL, NULL, NULL),(955, '提现审核', '提现审核', 'crowdfunding_withdrawalAudit', '/crowdfunding/withdrawalAudit', 1, 1, '', 'land-admin-service::/crowdfunding/admin::/queryCrowdfundingAddress', 1, 40, 933, NULL, NULL, NULL, NULL, NULL),(956, '历史记录', '历史记录', 'queryAccountDetailHistory', '/crowdfunding/history', 1, 1, '', 'land-admin-service::/admin/address/account::/queryAccountDetailHistory', 1, 50, 933, NULL, NULL, NULL, NULL, NULL),(957, 'menu_mail_manger', '邮件配置管理', '/email/admin', '/layout/Layout', 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2023-02-09 11:14:10', 'admin', '2023-02-09 11:14:10', NULL),(958, 'common_menu_mail_sender_config', '邮件发送配置', 'common_menu_mail_sender_config', 'common_menu_mail_sender_config', 1, 1, NULL, 'common-service::/email/admin::/config/list', 1, 0, 957, 'admin', '2023-02-09 11:14:10', 'admin', '2023-02-09 11:14:10', NULL),(959, 'menu_mail_template_config', '邮件模板配置', 'menu_mail_template_config', 'menu_mail_template_config', 1, 1, NULL, 'common-service::/email/admin::/config/template', 1, 0, 957, 'admin', '2023-02-09 11:14:10', 'admin', '2023-02-09 11:14:10', NULL),(960, 'common_menu_message_log_manager', '消息日志管理', 'common_menu_message_log_manager', 'common_menu_message_log_manager', 1, 1, NULL, 'common-service::/admin/message/logs::/config/list', 1, 0, -1000, 'admin', '2023-02-09 11:14:10', 'admin', '2023-02-09 11:14:10', NULL),(961, 'menu_user_excel_parent', '导入导出管理', '/user/excel/', '/layout/Layout', 0, 0, NULL, NULL, 1, 0, NULL, 'admin', '2023-02-09 11:14:10', 'admin', '2023-02-09 11:14:10', NULL),(962, 'export_template_page', '导出模板管理', 'export_template_page', 'export_template_page', 1, 1, NULL, 'common-service::/user/excel/::/list', 1, 0, 961, 'admin', '2023-02-09 11:14:10', 'admin', '2023-02-09 11:14:10', NULL),(963, 'menu_admin_imexport_task_page', '导入导出任务列表', 'menu_admin_imexport_task_page', 'menu_admin_imexport_task_page', 1, 1, NULL, 'common-service::/admin/imexport/task::/list', 1, 0, 961, 'admin', '2023-02-09 11:56:48', 'admin', '2023-02-09 11:56:48', NULL),(964, 'menu_user_imexport_task_page', '我的导入导出', 'menu_user_imexport_task_page', 'menu_user_imexport_task_page', 1, 2, NULL, NULL, 1, 0, 961, 'admin', '2023-02-09 17:00:29', 'admin', '2023-02-09 17:00:29', NULL)
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_menu_resource` WRITE;
DELETE FROM `template_framework`.`t_frame_menu_resource`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_org` WRITE;
DELETE FROM `template_framework`.`t_frame_org`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_role` WRITE;
DELETE FROM `template_framework`.`t_frame_role`;
INSERT INTO `template_framework`.`t_frame_role` (`role_id`,`role_code`,`role_name`,`role_type`,`create_by`,`create_date`,`update_by`,`update_date`,`status`) VALUES (1, 'Admin', '超级管理员', 'default', 'admin', '2020-01-29 16:48:11', 'admin', '2023-08-06 16:33:03', 1),(3, 'User', '默认用户权限', 'default', 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47', 1),(13, '虚拟用户角色【测试用】', '虚拟用户角色', NULL, 'admin', '2023-08-16 16:15:33', 'admin', '2023-08-16 16:15:33', 1)
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_role_data` WRITE;
DELETE FROM `template_framework`.`t_frame_role_data`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_role_data_interface` WRITE;
DELETE FROM `template_framework`.`t_frame_role_data_interface`;
INSERT INTO `template_framework`.`t_frame_role_data_interface` (`id`,`role_id`,`data_interface_id`,`belong_microservice`,`data_interface_name`) VALUES (281, 1, '3efa00c2c4b06b73758fac9e95e9b6a9', 'COMMONSERVICE', '查询所有用户的接口'),(282, 1, '5e6c43e07ad92a77cfad8ba6', 'COMMONSERVICE', '查询需要授权的资源清单列表'),(283, 1, '5e6c509e7ad92a77cfad8de2', 'COMMONSERVICE', '查询当前角色操作权限列表'),(284, 1, '5e6d666dc6af767df8096e37', 'COMMONSERVICE', '查询需要授权的接口清单'),(285, 1, '6191e5feb221dd9d0acb24ae2895fb80', 'COMMONSERVICE', '动态SQL需要授权给用户的示例'),(286, 1, 'dc14b9097ea7bf9eaee08b19a793ccf2', 'UNKNOW-ACCOUNT-SERVICE', '查询钱包用户信息'),(287, 1, 'f8c3d11952196739e55f80f472a52693', 'General', '查询操作权限列表'),(288, 1, 'fbf2e7c0ec6c91d9041ccd92509ce309', 'COMMONSERVICE', '按用户ID查询用户的角色列表'),(289, 1, 'fca0c2236325f81017b12e4da4d32990', 'COMMONSERVICE', '查询系统角色列表')
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_role_menu` WRITE;
DELETE FROM `template_framework`.`t_frame_role_menu`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_role_menu_resource` WRITE;
DELETE FROM `template_framework`.`t_frame_role_menu_resource`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_role_resource` WRITE;
DELETE FROM `template_framework`.`t_frame_role_resource`;
INSERT INTO `template_framework`.`t_frame_role_resource` (`role_resource_id`,`role_id`,`resource_id`) VALUES (14950, 1, -1001),(14951, 1, -1000),(14952, 1, 1),(14953, 1, 3),(14954, 1, 32),(14955, 1, 33),(14956, 1, 34),(14957, 1, 35),(14958, 1, 36),(14959, 1, 37),(14960, 1, 38),(14961, 1, 60),(14962, 1, 61),(14963, 1, 62),(14964, 1, 63),(14965, 1, 64),(14966, 1, 65),(14967, 1, 66),(14968, 1, 86),(14969, 1, 87),(14970, 1, 88),(14971, 1, 89),(14972, 1, 95),(14973, 1, 96),(14974, 1, 102),(14975, 1, 103),(14976, 1, 104),(14977, 1, 106),(14978, 1, 187),(14979, 1, 188),(14980, 1, 189),(14981, 1, 190),(14982, 1, 191),(14983, 1, 193),(14984, 1, 194),(14985, 1, 196),(14986, 1, 200),(14987, 1, 201),(14988, 1, 202),(14989, 1, 203),(14990, 1, 204),(14991, 1, 355),(14992, 1, 356),(14993, 1, 357),(14994, 1, 358),(14995, 1, 359),(14996, 1, 360),(14997, 1, 361),(14998, 1, 362),(14999, 1, 364),(15000, 1, 365),(15001, 1, 366),(15002, 1, 368),(15003, 1, 369),(15004, 1, 370),(15005, 1, 371),(15006, 1, 372),(15007, 1, 373),(15008, 1, 374),(15009, 1, 375),(15010, 1, 376),(15011, 1, 377),(15012, 1, 378),(15013, 1, 379),(15014, 1, 380),(15015, 1, 382),(15016, 1, 383),(15017, 1, 384),(15018, 1, 385),(15019, 1, 386),(15020, 1, 387),(15021, 1, 388),(15022, 1, 389),(15023, 1, 390),(15024, 1, 391),(15025, 1, 392),(15026, 1, 393),(15027, 1, 394),(15028, 1, 395),(15029, 1, 396),(15030, 1, 397),(15031, 1, 398),(15032, 1, 399),(15033, 1, 400),(15034, 1, 401),(15035, 1, 402),(15036, 1, 403),(15037, 1, 404),(15038, 1, 405),(15039, 1, 406),(15040, 1, 407),(15041, 1, 408),(15042, 1, 409),(15043, 1, 410),(15044, 1, 411),(15045, 1, 412),(15046, 1, 413),(15047, 1, 419),(15048, 1, 420),(15049, 1, 421),(15050, 1, 422),(15051, 1, 423),(15052, 1, 424),(15053, 1, 425),(15054, 1, 426),(15055, 1, 427),(15056, 1, 428),(15057, 1, 430),(15058, 1, 431),(15059, 1, 432),(15071, 13, 392)
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_user` WRITE;
DELETE FROM `template_framework`.`t_frame_user`;
INSERT INTO `template_framework`.`t_frame_user` (`id`,`user_name`,`password`,`email`,`full_name`,`sex`,`mobile_phone`,`session_key`,`default_role`,`user_type`,`user_regist_source`,`status`,`create_by`,`create_date`,`update_by`,`update_date`,`avatar`) VALUES (1, 'admin', '11863c7d74ddeb948949594d7240ad5b', 'admin@qq.com', '无敌超级管理员', 'F', '111', NULL, 'Admin', 'admin', 'background', 3, 'admin', '2016-01-11 12:33:05', 'admin', '2023-08-16 19:12:17', NULL),(767, 'virtual_user_1', 'ea5ea0bb4f0749a9abe712826fb2fdd2', '', '', NULL, '', NULL, 'User', 'virtual', 'background', 1, 'admin', '2023-08-16 15:07:34', 'admin', '2023-08-16 16:15:48', NULL)
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_user_auth` WRITE;
DELETE FROM `template_framework`.`t_frame_user_auth`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_user_ref` WRITE;
DELETE FROM `template_framework`.`t_frame_user_ref`;
INSERT INTO `template_framework`.`t_frame_user_ref` (`id`,`user_id`,`attribute_name`,`attribute_value`,`create_by`,`create_date`,`update_by`,`update_date`,`remark`) VALUES (32, '1', 'GOOGLE_MFA_USER_SECRET_BIND_FLAG', 'true', 'admin', '2023-08-16 17:28:51', 'admin', '2023-08-16 17:28:51', '谷歌验证绑定标识'),(31, '1', 'GOOGLE_MFA_USER_SECRET_KEY', '43989f4d38a515ad5a020a0f289961fbe04d8dc04eb2fe6e7c3e7eea3d043e6c', 'admin', '2023-08-16 17:28:04', 'admin', '2023-08-16 17:28:04', '谷歌验证码')
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_frame_user_role` WRITE;
DELETE FROM `template_framework`.`t_frame_user_role`;
INSERT INTO `template_framework`.`t_frame_user_role` (`user_role_id`,`user_id`,`role_id`,`validator_start`,`validator_end`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (48, 1, 1, '2023-08-16 19:12:15', '2024-08-16 19:12:15', 'admin', '2020-01-29 16:48:11', 'admin', '2023-08-06 16:33:03')
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_framework_resource` WRITE;
DELETE FROM `template_framework`.`t_framework_resource`;
INSERT INTO `template_framework`.`t_framework_resource` (`resource_id`,`resource_code`,`resource_name`,`resource_path`,`method`,`belong_microservice`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (-1001, 'saveOrUpdateUser', '增加或更新用户', '/admin/user', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(-1000, 'saveOrUpdateRoles', '增加或保存角色', '/admin/role/', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(1, 'datainterface_page_query', '查询接口列表分页', '系统管理/接口配置', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(3, 'datainterface_params_page_query', '查询接口参数列表分页', '系统管理/接口配置', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(32, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:10', 'admin', '2023-03-09 19:23:10'),(33, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:10', 'admin', '2023-03-09 19:23:10'),(34, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(35, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(36, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:10', 'admin', '2023-03-09 19:23:10'),(37, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:10', 'admin', '2023-03-09 19:23:10'),(38, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:10', 'admin', '2023-03-09 19:23:10'),(60, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2023-03-06 18:08:42', 'admin', '2023-03-06 18:08:42'),(61, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2023-03-06 18:08:42', 'admin', '2023-03-06 18:08:42'),(62, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2023-03-06 18:08:42', 'admin', '2023-03-06 18:08:42'),(63, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2023-03-06 18:08:42', 'admin', '2023-03-06 18:08:42'),(64, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2023-03-06 18:08:42', 'admin', '2023-03-06 18:08:42'),(65, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2023-03-06 18:08:42', 'admin', '2023-03-06 18:08:42'),(66, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2023-03-06 18:08:42', 'admin', '2023-03-06 18:08:42'),(86, '删除文件', '管理员后台删除文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(87, '查询上传文件', '管理员后台查询文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(88, '下载文件', '管理员后台下载文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(89, '查看文件', '管理员后台查看文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(90, '删除文件', '删除文件，仅登录用户删除自己的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(91, '查询个人上传文件', '查询个人上传文件，需登录', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(92, '下载本人文件', '下载文件，仅可下载本人的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(93, '查看本人文件', '查看本人文件，仅可查看本人的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(94, '上传文件', '上传文件，需要登录', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(95, '刷新缓存', '刷新缓存，需授权', '系统管理/数据字典', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(96, '保存或者更新', '保存或者更新数据字典', '系统管理/数据字典', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(102, '更新数据接口', '更新数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(103, '生效或者失效数据接口', '生效或者失效数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(104, '查询数据接口', '查询数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(106, 'resetPassword', '重置密码', '/admin/user', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(107, 'updatePassWordByUser', '用户更新密码', '/userinfo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(187, 'decrypt', 'AES解密', 'common/aes', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:10', 'admin', '2023-03-09 19:23:10'),(188, '获取操作日志', '管理员后台查询日志，需要授权', '/admin/mongo/OperateLog', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(189, 'queryDataDimensionListByTypeAndId', '按类型查询数据权限', '/admin/dataDimension', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(190, 'insertOrUpdateBatch', '批量更新数据权限', '/admin/dataDimension', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(191, 'selectDataDimensionByUserId', '查询用户有效的数据权限列表', '/admin/dataDimension', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(192, 'checkCurrentUserOperater', '校验当前用户的操作权限列表', '/userinfo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2022-08-17 14:38:33', 'admin', '2022-08-17 14:38:33'),(193, 'disabledUser', '禁用用户', '/userinfo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(194, 'enabledUser', '启用用户', '/userinfo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(195, 'bindUserGoogleSecret', '绑定谷歌验证', '/common/user/mfa', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(196, 'resetBindUserGoogleSecretFlag', '重置谷歌验证码状态', '/common/user/mfa', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(197, 'checkUserGoogleSecretBindStatus', '校验谷歌验证绑定状态', '/common/user/mfa', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(198, 'checkCurrentUserGoogleCode', '校验当前用户的谷歌验证码', '/common/user/mfa', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(199, '内部使用的获取当前用户扩展属性', '', '/commonServiceInner', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(200, 'decrypt', 'AES解密', 'common/aes', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2023-03-06 18:08:42', 'admin', '2023-03-06 18:08:42'),(201, 'updateMenuById', '根据ID更新菜单', '/admin/menu', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(202, 'listAllTreeMenuByParentId', '根据ID查询菜单及子菜单', '/admin/menu', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(203, 'refreshCache', '刷新菜单缓存', '/admin/menu', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(204, 'insertMenu', '插入菜单', '/admin/menu', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(355, '/admin-addArticle', '添加文章或分类', '/admin/article', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(356, '/admin-updateArticle', '修改文章或分类', '/admin/article', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(357, '/admin-articleList', '管理员获取文章或分类列表', '/admin/article', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(358, '/admin-get', '根据id获取文章或列表详细信息', '/admin/article', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(359, '/admin-addPhoto', '添加图片', '/admin/photo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(360, '/admin-updatePhoto', '修改图片', '/admin/photo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(361, '/admin-updatePhotoStatus', '修改图片状态', '/admin/photo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(362, '/admin-getPhotoList', '获取图片列表', '/admin/photo', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(363, '/getServices', '获取所有微服务', '/discovery/client', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(364, '/register/microservice', '获取所有微服务', '/discovery/client', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(365, '/getAll', '查询全部定时任务', '/quartz/client', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(366, '/getAllRunJob', '查询运行中的定时任务', '/quartz/client', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(367, 'checkCurrentUserOperator', '校验当前用户的操作权限列表', '/userinfo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(368, '/config/list', '管理员查询邮件配置', '/email/admin', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(369, '/template/update', '管理员更新邮件模板', '/email/admin', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(370, '/config/test', '管理员发送测试邮件', '/email/admin', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(371, '/config/template', '管理员查询邮件模板', '/email/admin', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(372, '/config/update', '管理员更新邮件配置', '/email/admin', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(373, '/config/create', '管理员增加邮件配置', '/email/admin', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(374, '/template/create', '管理员增加邮件模板', '/email/admin', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(375, '/config/list', '管理员查询消息日志', '/admin/message/logs', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(376, '/list', '查询导出模板列表', '/user/excel/', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-02-09 11:14:10', 'admin', '2023-02-09 11:14:10'),(377, '/create', '创建导出模板', '/admin/export/template', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-02-15 11:17:01', 'admin', '2023-02-15 11:17:01'),(378, '/list', '查询导出模板列表', '/admin/export/template', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(379, '/list', '管理员：查询导入导出任务列表', '/admin/imexport/task', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(380, '/create', '创建导入导出任务', '/user/imexport/task', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(381, '/list', '查询导入导出任务列表', '/user/imexport/task', 'ALLSYSTEMUSER', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(382, '/createOrUpdate', '创建或更新导出模板', '/admin/export/template', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(383, '/list', '管理员查询消息日志', '/admin/job/logs', 'BYUSERPERMISSION', 'common-service', 'admin', '2023-03-09 19:23:11', 'admin', '2023-03-09 19:23:11'),(384, 'decrypt', 'AES解密', 'common/aes', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(385, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(386, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(387, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(388, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(389, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(390, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(391, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(392, '/admin-updateStatus', '启用禁用', '/admin/chain', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(393, '/admin-getChainList', '获取链配置', '/admin/chain', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(394, '/admin-updateChain', '编辑链配置', '/admin/chain', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(395, '/admin-addChain', '添加链配置', '/admin/chain', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(396, '/admin-updateContract', '编辑合约配置', '/admin/contract', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(397, '/admin-addContract', '添加合约配置', '/admin/contract', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(398, '/admin-updateStatus', '启用禁用', '/admin/contract', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(399, '/admin-getContractList', '获取合约配置', '/admin/contract', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(400, '/admin-getTranLogList', '获取交易记录', '/admin/tran', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(401, '/admin-getNFTList', '获取NFT记录', '/admin/tran', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(402, '/admin-getNFTTranList', '获取NFT交易记录', '/admin/tran', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(403, '/admin-getErc20List', '获取erc20持仓列表', '/admin/tran', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(404, '/admin-getErc20TokenTransRecord', '获取erc20转账列表', '/admin/tran', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(405, '/admin-getSwapTokenRecord', '获取Swap交易列表', '/admin/tran', 'BYUSERPERMISSION', 'chain-block-scan-service', 'admin', '2023-03-07 14:38:46', 'admin', '2023-03-07 14:38:46'),(406, 'decrypt', 'AES解密', 'common/aes', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(407, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(408, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(409, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(410, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(411, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(412, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(413, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(414, '/queryAccountInfoByAddress', '查询地址信息', '/address/account', 'ALLSYSTEMUSER', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(415, '/queryAccountDetailByAddress', '查询地址交易信息', '/address/account', 'ALLSYSTEMUSER', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(416, '/queryAccountDetailCollectByAddress', '查询地址交易信息汇总金额', '/address/account', 'ALLSYSTEMUSER', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(417, '/createWithDraw', '用户创建提现订单', '/address/account', 'ALLSYSTEMUSER', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(418, '/listTradeType', '查询交易类型列表', '/address/account', 'ALLSYSTEMUSER', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(419, '/queryAccountDetail', '查询资金明细', '/admin/address/account', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(420, '/queryAccountDetailHistory', '查询历史资金明细', '/admin/address/account', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(421, '/queryAccountAuthWithDrawDetail', '查询资金审核记录', '/admin/address/account', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(422, '/accountList', '管理员后台查询账户列表', '/admin/address/account', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(423, '/adminRechargeWithdraw', '管理员后台充值提现', '/admin/address/account', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(424, '/adminUpdateRechargeWithdraw', '管理员后台更新用户提现限额', '/admin/address/account', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(425, '/authWithdraw', '管理员后台提现审核', '/admin/address/account/detail', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(426, '/admin-list', '管理员获取内部转账记录', '/admin/address/transfer', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(427, '/coinList', '管理员后台查询币种配置列表', '/admin/coin/config', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(428, '/updateCoin', '管理员后台更新币种信息', '/admin/coin/config', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(429, '/listSystemAddressType', '查询系统账户类型列表', '/admin/system/address', 'ALLSYSTEMUSER', 'three-body-service1', 'admin', '2023-04-08 18:52:44', 'admin', '2023-04-08 18:52:44'),(430, '/update', '更新系统用户', '/admin/system/address', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:44', 'admin', '2023-04-08 18:52:44'),(431, '/create', '创建系统用户', '/admin/system/address', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:44', 'admin', '2023-04-08 18:52:44'),(432, '/list', '管理员查询系统用户列表', '/admin/system/address', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:44', 'admin', '2023-04-08 18:52:44'),(450, '/createOrUpdate', '管理交易队', '/flash/swap/pair/admin', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(451, '/list', '查询闪兑交易队', '/flash/swap/pair/admin', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43'),(452, '/list', '查询闪兑记录', '/flash/swap/record/admin', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:44', 'admin', '2023-04-08 18:52:44'),(453, '/list', '查询闪兑交易队', '/flash/swap/pair/app', 'ALLSYSTEMUSER', 'three-body-service1', 'admin', '2023-04-08 18:52:44', 'admin', '2023-04-08 18:52:44'),(454, '/updateStatus', '更新交易队状态', '/flash/swap/pair/admin', 'BYUSERPERMISSION', 'three-body-service1', 'admin', '2023-04-08 18:52:43', 'admin', '2023-04-08 18:52:43')
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_framework_resource2` WRITE;
DELETE FROM `template_framework`.`t_framework_resource2`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_microservice_register` WRITE;
DELETE FROM `template_framework`.`t_microservice_register`;
INSERT INTO `template_framework`.`t_microservice_register` (`microservice_id`,`microservice_name`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (6, 'common-service', 'admin', '2023-03-09 19:23:10', 'admin', '2023-03-09 19:23:10'),(9, 'spring-gateway', 'admin', '2023-03-06 18:08:42', 'admin', '2023-03-06 18:08:42')
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_photo` WRITE;
DELETE FROM `template_framework`.`t_photo`;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_system_dic_item` WRITE;
DELETE FROM `template_framework`.`t_system_dic_item`;
INSERT INTO `template_framework`.`t_system_dic_item` (`dic_item_id`,`dic_master_id`,`dic_item_code`,`dic_item_name`,`dic_item_value`,`ext_attribut1`,`ext_attribut3`,`ext_attribut2`,`ext_attribut4`,`ext_attribut5`,`status`,`create_by`,`create_date`,`update_by`,`update_date`,`language`,`seq`) VALUES (1, 1, 'company_name', 'XXXX有限公司', 'XXXX有限公司', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-02-22 17:42:19', 1, '2020-08-10 19:55:20', 'zh_CN', 10),(13, 15, 'active', '有效', '1', '1', NULL, '12', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 15),(14, 15, 'inactive', '无效', '0', '2', NULL, '', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 20),(15, 15, 'expired', '过期', '-1', '3', NULL, '', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 25),(16, 16, 'commonService', '通用基础服务', 'COMMON-SERVICE', '', NULL, '', NULL, NULL, 1, 1, '2020-03-04 12:40:56', 1, '2023-08-08 11:44:53', 'zh_CN', 5),(20, 16, 'spring-gateway', 'SPRING网关服务', 'SPRING-GATEWAY', '', NULL, '', NULL, NULL, 1, 1, '2020-03-04 12:40:56', 1, '2023-08-08 11:44:53', 'zh_CN', 5),(25, 16, 'General', '所有微服务通用', 'General', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-05 10:32:09', 1, '2023-08-08 11:44:53', 'zh_CN', 0),(44, 28, 'admin', '后台管理用户', 'admin', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:51:50', 1, '2023-08-16 15:06:25', 'zh_CN', 5),(45, 28, 'LOGIN-BY-WEIXIN-MICROAPP', '微信小程序用户', 'LOGIN-BY-WEIXIN-MICROAPP', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:55:03', 1, '2023-08-16 15:06:25', 'zh_CN', 10),(46, 29, 'background', '后台添加', 'background', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:57:41', 1, '2020-03-18 01:57:41', 'zh_CN', 5),(47, 30, 'Reset', '重置', '3', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 20),(48, 30, 'New', '新建', '2', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 15),(49, 30, 'Inactive', '失效', '-1', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 10),(50, 30, 'Active', '生效', '1', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 5),(100, 16, 'UNKNOW-ACCOUNT-SERVICE', 'UNKNOW-ACCOUNT-SERVICE', 'UNKNOW-ACCOUNT-SERVICE', NULL, NULL, NULL, NULL, NULL, 0, 1, '2020-07-28 13:41:03', 1, '2023-08-08 11:44:53', 'zh_CN', 20),(101, 16, 'UNKNOW-WALLET-SERVICE', 'UNKNOW-WALLET-SERVICE', 'UNKNOW-WALLET-SERVICE', NULL, NULL, NULL, NULL, NULL, 0, 1, '2020-07-28 13:41:03', 1, '2023-08-08 11:44:53', 'zh_CN', 15),(106, 48, 'isMfaVerify', '是否开启双因子', 'true', NULL, NULL, NULL, NULL, NULL, 1, 1, '2021-06-03 16:21:54', 1, '2023-04-04 14:50:12', 'zh_CN', 0),(107, 49, '菜单', '菜单', '1', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:19:17', 1, '2022-05-16 17:19:17', 'zh_CN', 5),(108, 49, '目录', '目录', '0', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:19:17', 1, '2022-05-16 17:19:17', 'zh_CN', 5),(109, 50, '任何时候都显示', '任何时候都显示\n', '2', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:22:34', 1, '2022-05-16 17:22:34', 'zh_CN', 5),(110, 50, '有权限时显示', '有权限时显示\n', '1', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:22:34', 1, '2022-05-16 17:22:34', 'zh_CN', 5),(111, 50, '有子菜单时显示', '有子菜单时显示\n', '0', NULL, NULL, NULL, NULL, NULL, 1, 1, '2022-05-16 17:22:34', 1, '2022-05-16 17:22:34', 'zh_CN', 5),(112, 51, 'micro_service_name', '所属微服务', 'micro_service_name', NULL, NULL, NULL, NULL, NULL, 0, 1, '2021-06-30 21:27:01', 1, '2023-02-21 17:31:47', 'zh_CN', 20),(113, 51, 'code_10', 'xls', '10', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-21 15:52:03', 1, '2023-02-21 17:31:47', 'zh_CN', 5),(114, 51, 'code_20', 'pdf', '20', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-21 15:52:03', 1, '2023-02-21 17:31:47', 'zh_CN', 10),(115, 51, 'code_30', 'word', '30', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-21 15:52:03', 1, '2023-02-21 17:31:47', 'zh_CN', 15),(116, 52, 'taskStatus1', '未执行', '1', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38', 'zh_CN', 5),(117, 52, 'taskStatus2', '执行中', '2', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38', 'zh_CN', 10),(118, 52, 'taskStatus3', '执行成功', '3', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38', 'zh_CN', 15),(119, 52, 'taskStatus-1', '失败', '-1', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38', 'zh_CN', 20),(128, 55, '10', '按比例收取', '10', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-04-05 17:19:33', 1, '2023-04-05 17:19:33', 'zh_CN', 5),(129, 55, '20', '单笔固定金额', '20', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-04-05 17:19:33', 1, '2023-04-05 17:19:33', 'zh_CN', 10),(130, 56, '10', '按比例收取', '10', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-04-05 18:05:00', 1, '2023-04-05 18:05:00', 'zh_CN', 5),(131, 56, '20', '单笔固定', '20', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-04-05 18:05:00', 1, '2023-04-05 18:05:00', 'zh_CN', 10),(132, 57, '10', '按提现比例', '10', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-04-05 18:07:49', 1, '2023-04-05 18:07:49', 'zh_CN', 5),(133, 57, '20', '单笔固定', '20', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-04-05 18:07:49', 1, '2023-04-05 18:07:49', 'zh_CN', 10),(134, 58, 'zh_CN', '中文', '1', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-07 21:19:39', 1, '2023-08-07 21:25:19', 'zh_CN', 5),(135, 58, 'en_US', '英文', '2', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-07 21:25:19', 1, '2023-08-07 21:25:19', 'zh_CN', 10),(136, 58, 'zh_TW', '中文繁体', '3', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-07 21:25:19', 1, '2023-08-07 21:25:19', 'zh_CN', 15),(137, 58, 'vi', '越南语', '4', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-07 21:25:19', 1, '2023-08-07 21:25:19', 'zh_CN', 20),(138, 58, 'ja', '日语', '5', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-07 21:25:19', 1, '2023-08-07 21:25:19', 'zh_CN', 25),(139, 58, 'ko', '韩语', '6', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-07 21:25:19', 1, '2023-08-07 21:25:19', 'zh_CN', 30),(140, 28, 'LOGIN-BY-THIRD-LOGIN', '外部登录用户', 'LOGIN-BY-THIRD-LOGIN', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-16 11:46:05', 1, '2023-08-16 15:06:25', 'zh_CN', 15),(141, 28, 'LOGIN-BY-ETH-CHAIN', '以太链签名登录用户', 'LOGIN-BY-ETH-CHAIN', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-16 11:46:05', 1, '2023-08-16 15:06:25', 'zh_CN', 20),(142, 28, 'virtual', '虚拟用户', 'virtual', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-16 11:46:05', 1, '2023-08-16 15:06:25', 'zh_CN', 25),(143, 59, 'micro_service_name', '微服务服务名', 'micro_service_name', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-08-16 18:19:01', 1, '2023-08-16 18:19:01', 'zh_CN', 5)
;
UNLOCK TABLES;
COMMIT;
BEGIN;
LOCK TABLES `template_framework`.`t_system_dic_master` WRITE;
DELETE FROM `template_framework`.`t_system_dic_master`;
INSERT INTO `template_framework`.`t_system_dic_master` (`dic_master_id`,`dic_code`,`dic_name`,`remark`,`belong_micro_service`,`parent_master_id`,`status`,`create_by`,`create_date`,`update_by`,`update_date`) VALUES (1, 'systemBaseConfig', '系统基础配置', '系统基础配置', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-03-22 07:14:57'),(15, 'commStatus', '系统通用的状态值', '通用状态', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-05-05 19:17:31'),(16, 'microServiceList', '微服务列表', '微服务列表', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2023-08-08 11:44:53'),(28, 'userType', '用户类型', '用户的来源类型，各个应用，后台创建的就属于admin，其它的属于各个应用注册而来，可以定义，目前微信小程序是微信小程序的名称。', 'General', NULL, 1, 1, '2020-03-18 01:51:50', 1, '2023-08-16 15:06:25'),(29, 'userRegisterSource', '用户注册来源', '用户的注册来源，后台添加的为：background 微信小程序的为微信小程序的ID', 'General', NULL, 1, 1, '2020-03-18 01:57:41', 1, '2020-08-11 20:44:48'),(30, 'userStatus', '用户状态', '用户状态', 'COMMON-SERVICE', NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06'),(48, 'systemConfig', '系统配置', NULL, 'General', NULL, 1, 1, '2021-06-03 16:20:11', 1, '2023-04-04 14:50:12'),(49, 'menuType', '菜单类型', NULL, 'General', NULL, 1, 1, '2022-05-16 17:19:17', 1, '2022-05-16 17:19:17'),(50, 'menuShowType', '显示方式', '0：有子菜单时显示 1：有权限时显示 2：任何时候都显示\n', 'General', NULL, 1, 1, '2022-05-16 17:22:34', 1, '2022-05-16 17:22:34'),(51, '导出模板类型', '导出模板类型', '导出模板类型', 'General', NULL, 1, 1, '2023-02-21 15:52:03', 1, '2023-02-21 17:31:47'),(52, '导入导出任务状态', '导入导出任务状态', '导入导出任务状态', 'General', NULL, 1, 1, '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38'),(57, 'accountWithdrawChargeType', '提现手续费类型', NULL, 'General', NULL, 1, 1, '2023-04-05 18:07:49', 1, '2023-04-05 18:07:49'),(58, 'articleLanguage', '文章语言编码', '文章语言编码', 'General', NULL, 1, 1, '2023-08-07 21:19:39', 1, '2023-08-07 21:25:19'),(59, 'data_dimension', '数据权限配置', '数据权限配置，用于配置数据权限的编码，xml里的代码示例：（不能换行）\n <dataDimension> [{\"fieldName\":\"t_system_dic_master.belong_micro_service\",\"dimensionName\":\"micro_service_name\",\"operator\":\"=\"}] </dataDimension>', 'General', NULL, 1, 1, '2023-08-16 18:19:01', 1, '2023-08-16 18:19:01')
;
UNLOCK TABLES;
COMMIT;
