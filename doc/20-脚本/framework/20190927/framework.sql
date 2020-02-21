/*
 Navicat Premium Data Transfer

 Source Server         : mysql-localhost-root
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:13306
 Source Schema         : framework

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 27/09/2019 07:27:55
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_frame_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_auth`;
CREATE TABLE `t_frame_auth`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `auth_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `auth_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '组织机构/角色/流程结点',
  PRIMARY KEY (`auth_code`, `auth_type`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_data_interface
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_interface`;
CREATE TABLE `t_frame_data_interface`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据编号',
  `data_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据名称',
  `data_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据类型：SQL(SQL)/存储过程(SP)/REST/（OTHER）其它',
  `data_execute_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '必须继承IFrameDataInterfaceService，\n            并在应用中正确的注册，可以不填，\n            默认为util.[接口类型].FrameDataInterfaceMainService\n            如果需要指定那么该接口必须继承IFrameDataInterfaceService',
  `description` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '描述',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `create_date` datetime(0) NOT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '更新人',
  `update_date` datetime(0) NOT NULL COMMENT '更新日期',
  PRIMARY KEY (`data_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_interface
-- ----------------------------
INSERT INTO `t_frame_data_interface` VALUES (6, 'QUERY_USER', '查找用户', 'SQL', NULL, '查询用户服务', 'admin', '2016-01-07 18:59:33', 'admin', '2016-01-07 18:59:40');
INSERT INTO `t_frame_data_interface` VALUES (4, 'REST_001', '快递单查询', 'REST', NULL, '快递100快递单查询服务', 'admin', '2016-01-08 18:53:17', 'admin', '2016-01-08 18:53:22');
INSERT INTO `t_frame_data_interface` VALUES (5, 'SP_001', '存储过程测试', 'SP', NULL, '存储过程测试', 'admin', '2016-01-10 21:16:48', 'admin', '2016-01-10 21:16:55');
INSERT INTO `t_frame_data_interface` VALUES (2, 'SQL_001', 'SQL_001', 'SQL', '', NULL, 'admin', '2016-01-07 18:58:22', 'admin', '2016-01-07 18:58:28');
INSERT INTO `t_frame_data_interface` VALUES (3, 'SQL_002', 'SQL_002', 'SQL', 'util.SQL.FrameDataInterfaceService', NULL, 'admin', '2016-01-07 18:59:33', 'admin', '2016-01-07 18:59:40');

-- ----------------------------
-- Table structure for t_frame_data_interface_params
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_interface_params`;
CREATE TABLE `t_frame_data_interface_params`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据编号',
  `seq` int(11) NOT NULL,
  `param_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `param_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `param_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`data_code`, `seq`, `param_name`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  CONSTRAINT `fk_reference_32` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_interface_params
-- ----------------------------
INSERT INTO `t_frame_data_interface_params` VALUES (5, 'REST_001', 1, 'text', 'String', '快递单号');
INSERT INTO `t_frame_data_interface_params` VALUES (1, 'SQL_001', 1, 'param1', 'String', '参数1');
INSERT INTO `t_frame_data_interface_params` VALUES (2, 'SQL_001', 2, 'param2', 'String', '参数2');
INSERT INTO `t_frame_data_interface_params` VALUES (3, 'SQL_002', 1, 'param1', 'String', '参数2');
INSERT INTO `t_frame_data_interface_params` VALUES (4, 'SQL_002', 2, 'param2', 'String', '参数2');

-- ----------------------------
-- Table structure for t_frame_data_rest_config
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_rest_config`;
CREATE TABLE `t_frame_data_rest_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据编号',
  `uri` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT '数据访问地址',
  `method` char(10) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'GET/POST',
  `must_login` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `login_bean` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '请继承IFrameDataInterfaceLoginService，返回登录后java.net.HttpURLConnection',
  PRIMARY KEY (`data_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  CONSTRAINT `fk_reference_36` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'REST服务数据接口' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_rest_config
-- ----------------------------
INSERT INTO `t_frame_data_rest_config` VALUES (1, 'REST_001', 'http://www.kuaidi100.com/autonumber/autoComNum?', 'GET', 'N', NULL);

-- ----------------------------
-- Table structure for t_frame_data_sp_config
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_sp_config`;
CREATE TABLE `t_frame_data_sp_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据编号',
  `sql_content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'SQL内容',
  `data_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据源',
  `database_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'MYSQL/ORACLE',
  `data_dao_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据执行DAO，可以不用指定，如果不指定，默认取 util.[数据库类型].[数据源名称].FrameDataInterfaceExecuteMapper',
  PRIMARY KEY (`data_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  CONSTRAINT `fk_reference_33` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '存储过程数据接口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_sp_config
-- ----------------------------
INSERT INTO `t_frame_data_sp_config` VALUES (1, 'SP_001', ' {call sp_test(#{userName,jdbcType=VARCHAR,mode=IN},#{page,jdbcType=INTEGER,mode=IN})}', 'framework', 'Mysql', NULL);

-- ----------------------------
-- Table structure for t_frame_data_sql_config
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_sql_config`;
CREATE TABLE `t_frame_data_sql_config`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据编号',
  `sql_content` text CHARACTER SET utf8 COLLATE utf8_bin NULL COMMENT 'SQL内容',
  `data_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据源',
  `database_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'MYSQL/ORACLE',
  `data_dao_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '数据执行DAO，可以不用指定，如果不指定，默认取 util.[数据库类型].[数据源名称].FrameDataInterfaceExecuteMapper',
  PRIMARY KEY (`data_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  CONSTRAINT `fk_reference_31` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_sql_config
-- ----------------------------
INSERT INTO `t_frame_data_sql_config` VALUES (4, 'QUERY_USER', '	select * from t_frame_user\r     <where>\r      	<if test=\"userName!=null\">\r 			( user_Name like CONCAT(\'%\',#{userName,jdbcType=VARCHAR},\'%\' ))\r 		</if>\r     </where>', 'framework', 'Mysql', NULL);
INSERT INTO `t_frame_data_sql_config` VALUES (1, 'SQL_001', '	select * from t_frame_user where 0=0\r\n	<if test=\"userName!=null\">\r\n	and ( user_Name = #{userName})\r\n	</if>\r\n	<if test=\"list!=null\">\r\n	and user_Name in\r\n	<foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\">\r\n			#{id,jdbcType=VARCHAR}\r\n		</foreach>\r\n	</if>\r\n\r\nunion all\r\n	select * from t_frame_user where 0=0\r\n	<if test=\"userName!=null\">\r\n	and  ( user_Name = #{userName})\r\n	</if>\r\n	<if test=\"list!=null\">\r\n	and user_Name in\r\n	<foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\">\r\n			#{id,jdbcType=VARCHAR}\r\n		</foreach>\r\n	</if>\r\nunion all\r\n	select * from t_frame_user where 0=0\r\n	<if test=\"userName!=null\">\r\n	and   ( user_Name = #{userName})\r\n	</if>\r\n	<if test=\"list!=null\">\r\n	and user_Name in\r\n	<foreach collection=\"list\" item=\"id\" open=\"(\" separator=\",\" close=\")\">\r\n			#{id,jdbcType=VARCHAR}\r\n		</foreach>\r\n	</if>\r\norder by user_Name', 'framework', 'Mysql', NULL);
INSERT INTO `t_frame_data_sql_config` VALUES (2, 'SQL_002', 'select * from t_frame_data_interface where (#{dataCode} is null or data_code = #{dataCode} )', NULL, 'Mysql', 'util.Mysql.framework.FrameDataInterfaceExecuteMapper');

-- ----------------------------
-- Table structure for t_frame_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_menu`;
CREATE TABLE `t_frame_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源组code',
  `menu_code_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源组名称',
  `parent_menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '父菜单编码',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `create_date` datetime(0) NOT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '更新人',
  `update_date` datetime(0) NOT NULL COMMENT '更新日期',
  PRIMARY KEY (`menu_code`) USING BTREE,
  UNIQUE INDEX `ui_framework_menu_1`(`menu_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `ak_kid2`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_menu_data
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_menu_data`;
CREATE TABLE `t_frame_menu_data`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源组code',
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '数据编号',
  PRIMARY KEY (`menu_code`, `data_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `ak_kid2`(`id`) USING BTREE,
  INDEX `fk_reference_35`(`data_code`) USING BTREE,
  CONSTRAINT `fk_reference_34` FOREIGN KEY (`menu_code`) REFERENCES `t_frame_menu` (`menu_code`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reference_35` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_menu_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_menu_resource`;
CREATE TABLE `t_frame_menu_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源组code',
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源编码',
  PRIMARY KEY (`menu_code`, `resource_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `fk_reference_23`(`resource_code`) USING BTREE,
  CONSTRAINT `fk_reference_22` FOREIGN KEY (`menu_code`) REFERENCES `t_frame_menu` (`menu_code`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reference_23` FOREIGN KEY (`resource_code`) REFERENCES `t_framework_resource` (`resource_code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '菜单资源相关资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_org
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_org`;
CREATE TABLE `t_frame_org`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `org_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `auth_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '组织机构',
  `org_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `org_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `parent_org_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '父结点CODE',
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `validator_start` datetime(0) NOT NULL,
  `validator_end` datetime(0) NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `create_date` datetime(0) NOT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '更新人',
  `update_date` datetime(0) NOT NULL COMMENT '更新日期',
  PRIMARY KEY (`org_code`) USING BTREE,
  UNIQUE INDEX `index_1`(`org_code`, `status`, `validator_start`, `validator_end`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `fk_reference_30`(`org_code`, `auth_type`) USING BTREE,
  CONSTRAINT `fk_reference_30` FOREIGN KEY (`org_code`, `auth_type`) REFERENCES `t_frame_auth` (`auth_code`, `auth_type`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_role
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_role`;
CREATE TABLE `t_frame_role`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `auth_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '角色',
  `role_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '功能权限（10）/数据权限（20）/流程权限（30）',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`role_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `fk_reference_29`(`role_code`, `auth_type`) USING BTREE,
  CONSTRAINT `fk_reference_29` FOREIGN KEY (`role_code`, `auth_type`) REFERENCES `t_frame_auth` (`auth_code`, `auth_type`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_role_menu`;
CREATE TABLE `t_frame_role_menu`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源组code',
  PRIMARY KEY (`role_code`, `menu_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `fk_reference_27`(`menu_code`) USING BTREE,
  CONSTRAINT `fk_reference_24` FOREIGN KEY (`role_code`) REFERENCES `t_frame_role` (`role_code`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reference_27` FOREIGN KEY (`menu_code`) REFERENCES `t_frame_menu` (`menu_code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统角色菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_role_menu_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_role_menu_resource`;
CREATE TABLE `t_frame_role_menu_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源组code',
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源编码',
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `fk_reference_25`(`role_code`, `menu_code`) USING BTREE,
  INDEX `fk_reference_26`(`menu_code`, `resource_code`) USING BTREE,
  CONSTRAINT `fk_reference_25` FOREIGN KEY (`role_code`, `menu_code`) REFERENCES `t_frame_role_menu` (`role_code`, `menu_code`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reference_26` FOREIGN KEY (`menu_code`, `resource_code`) REFERENCES `t_frame_menu_resource` (`menu_code`, `resource_code`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统角色菜单资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_user
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_user`;
CREATE TABLE `t_frame_user`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `email` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `full_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `sex` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `status` int(2) NOT NULL DEFAULT 1 COMMENT '1:有效 0:无效 -1:过期',
  `default_role` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'User' COMMENT '默认角色',
  PRIMARY KEY (`user_name`) USING BTREE,
  UNIQUE INDEX `inx_user_user_id`(`user_name`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_user
-- ----------------------------
INSERT INTO `t_frame_user` VALUES (1, 'admin', '3e2345598780429e4758006e801f4b88', 'admin@qq.com', '管理员', 'F', 'admin', '2016-01-07 12:33:05', 'admin', '2016-01-07 12:33:05', 1, 'User');
INSERT INTO `t_frame_user` VALUES (2, 'admin1', 'E10ADC3949BA59ABBE56E057F20F883E', 'admin1@qq.com', 'admin1', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59', 1, 'User');
INSERT INTO `t_frame_user` VALUES (7, 'leader_老六', 'E10ADC3949BA59ABBE56E057F20F883E', 'lead_laoliu@html.com', 'leader_老六', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59', 1, 'User');
INSERT INTO `t_frame_user` VALUES (3, 'student_张三', 'E10ADC3949BA59ABBE56E057F20F883E', 'stu_zhangshan@html.com', 'student_张三', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59', 1, 'User');
INSERT INTO `t_frame_user` VALUES (5, 'student_李四', 'E10ADC3949BA59ABBE56E057F20F883E', 'stu_lisi@html.com', 'student_李四', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59', 1, 'User');
INSERT INTO `t_frame_user` VALUES (6, 'teacher_王五', 'E10ADC3949BA59ABBE56E057F20F883E', 'tech_wangwu@html.com', 'teacher_王五', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59', 1, 'User');

-- ----------------------------
-- Table structure for t_frame_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_user_auth`;
CREATE TABLE `t_frame_user_auth`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `auth_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `auth_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '组织机构/角色/流程结点/数据接口',
  `validator_start` datetime(0) NOT NULL,
  `validator_end` datetime(0) NOT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '创建人',
  `create_date` datetime(0) NOT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '更新人',
  `update_date` datetime(0) NOT NULL COMMENT '更新日期',
  PRIMARY KEY (`login_id`, `auth_code`, `auth_type`) USING BTREE,
  UNIQUE INDEX `uidx_user_blong_type_userid`(`login_id`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `fk_reference_28`(`auth_code`, `auth_type`) USING BTREE,
  CONSTRAINT `fk_reference_1` FOREIGN KEY (`login_id`) REFERENCES `t_frame_user` (`user_name`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `fk_reference_28` FOREIGN KEY (`auth_code`, `auth_type`) REFERENCES `t_frame_auth` (`auth_code`, `auth_type`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_user_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_user_ref`;
CREATE TABLE `t_frame_user_ref`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `attribute_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `attribute_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`login_id`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  CONSTRAINT `fk_reference_2` FOREIGN KEY (`login_id`) REFERENCES `t_frame_user` (`user_name`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_framework_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_framework_resource`;
CREATE TABLE `t_framework_resource`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源编码',
  `resource_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '资源名称',
  `uri` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '资源地址',
  `method` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '访问方法：get,post',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`resource_code`) USING BTREE,
  UNIQUE INDEX `ui_framework_resorce_code`(`resource_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统资源，用来记录所有的系统的URI服务资源\n可以定义参数类型并进行参数类型的检查' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Procedure structure for sp_test
-- ----------------------------
DROP PROCEDURE IF EXISTS `sp_test`;
delimiter ;;
CREATE PROCEDURE `sp_test`(in v_user_name varchar(2000),in v_page integer)
BEGIN
	IF v_page IS not NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	if v_user_name is null then
		select * from t_frame_user;
	else
		SELECT * FROM t_frame_user u where u.user_name = v_user_name;
	end if;
	IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;
	IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
	IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
IF v_page IS NOT NULL THEN
		IF v_user_name IS NULL THEN
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user;
		ELSE
			SELECT COUNT(*) AS total_count,CEIL(COUNT(*)/10) AS total_page,v_page as page,10 as page_Size FROM t_frame_user u WHERE u.user_name = v_user_name;
		END IF;
	END IF;
    
	IF v_user_name IS NULL THEN
		SELECT * FROM t_frame_user;
	ELSE
		SELECT * FROM t_frame_user u WHERE u.user_name = v_user_name;
	END IF;	
    END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
