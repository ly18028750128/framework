/*
 Navicat Premium Data Transfer

 Source Server         : 测试环境mysql
 Source Server Type    : MySQL
 Source Server Version : 80022
 Source Host           : 192.168.30.99:10002
 Source Schema         : framework

 Target Server Type    : MySQL
 Target Server Version : 80022
 File Encoding         : 65001

 Date: 08/06/2021 16:22:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_frame_auth_type
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_auth_type`;
CREATE TABLE `t_frame_auth_type`  (
  `auth_type_id` bigint NOT NULL AUTO_INCREMENT COMMENT '权限类型ID',
  `auth_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限编码',
  `auth_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限类型：角色（role）/微服务权限（mircroservice）/组机机构（organization）/其它数据权限（可定义）',
  `remark` varchar(1000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`auth_type_id`) USING BTREE,
  UNIQUE INDEX `uidx_f_auth_type_code`(`auth_code`) USING BTREE,
  INDEX `ak_kid`(`auth_type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_auth_type
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_data_interface
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_interface`;
CREATE TABLE `t_frame_data_interface`  (
  `data_interface_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'data_interface_id',
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '数据编号',
  `data_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据名称',
  `data_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据类型',
  `data_execute_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '数据执行服务名',
  `description` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '描述',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`data_interface_id`) USING BTREE,
  UNIQUE INDEX `ak_kid`(`data_interface_id`) USING BTREE,
  UNIQUE INDEX `udx_data_interface_code`(`data_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '数据接口定义' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_interface
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_data_interface_params
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_interface_params`;
CREATE TABLE `t_frame_data_interface_params`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `seq` int NOT NULL,
  `param_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_interface_id` bigint NULL DEFAULT NULL,
  `param_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `param_desc` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_interface_params
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_data_rest_config
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_rest_config`;
CREATE TABLE `t_frame_data_rest_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_interface_id` bigint NULL DEFAULT NULL,
  `uri` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `method` char(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `must_login` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `login_bean` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = 'REST服务数据接口' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_rest_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_data_sp_config
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_sp_config`;
CREATE TABLE `t_frame_data_sp_config`  (
  `id` bigint NOT NULL,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_interface_id` bigint NULL DEFAULT NULL,
  `sql_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `data_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `database_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `data_dao_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `data_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '存储过程数据接口表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_sp_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_data_sql_config
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_data_sql_config`;
CREATE TABLE `t_frame_data_sql_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_interface_id` bigint NULL DEFAULT NULL,
  `sql_content` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL,
  `data_source` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `database_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `data_dao_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_data_sql_config
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_menu`;
CREATE TABLE `t_frame_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `menu_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `menu_url` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `component_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'vue组件位置',
  `type` int NOT NULL DEFAULT 1 COMMENT '0：目录 1：菜单',
  `show_type` int UNSIGNED NOT NULL DEFAULT 2 COMMENT '0：有子菜单时显示 1：有权限时显示 2：任何时候都显示',
  `icon` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '显示图标',
  `function_resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '权限编码：微服务application.name::resource_path::resource_name 例如：common-service::系统管理/数据字典::刷新缓存',
  `status` int NOT NULL DEFAULT 1 COMMENT '1:有效 0:无效 -1:过期',
  `seq_no` int NOT NULL DEFAULT 1,
  `parent_menu_id` bigint NULL DEFAULT NULL COMMENT '父级菜单ID',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `meta_data` varchar(2048) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '路由meta参数，必须为json字符串',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `ui_framework_menu_1`(`menu_code`) USING BTREE,
  UNIQUE INDEX `udx_framework_menu_1`(`menu_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_menu
-- ----------------------------
INSERT INTO `t_frame_menu` VALUES (-1010, '个人中心', '个人中心', '/personal/center', '/layout/Layout', 0, 2, 'sms', NULL, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_frame_menu` VALUES (-1000, 'menu_system_manager', '系统管理', '/admin', '/layout/Layout', 0, 2, NULL, NULL, 1, 10, NULL, 'admin', '2020-05-05 03:08:52', 'admin', '2020-05-05 03:08:52', NULL);
INSERT INTO `t_frame_menu` VALUES (-990, 'demo_test', '测试案例菜单', '/test/demo', '/layout/Layout', 0, 2, 'sms', NULL, 1, 10, NULL, 'admin', '2020-05-05 03:08:52', 'admin', '2020-05-05 03:08:52', NULL);
INSERT INTO `t_frame_menu` VALUES (2, '个人文件存储', '个人文件存储', 'personFileManager', '/user/personFileManager', 1, 2, 'folder', NULL, 1, 1, -1010, NULL, NULL, NULL, NULL, '{\"pageType\": \"personal\"}');
INSERT INTO `t_frame_menu` VALUES (3, '公共文件查询', '公共文件查询', 'mongodbGridfsPublicFileQuery', '/user/publicFileQuery', 1, 2, 'folder', NULL, 1, 2, -1010, NULL, NULL, NULL, NULL, '{\"pageType\": \"public\"}');
INSERT INTO `t_frame_menu` VALUES (5, '定时任务管理', '定时任务管理', 'quartz/alljobs', '/admin/quartz/alljobs/index', 1, 1, 'sms-flash', 'common-service::定时任务/quartz::createOrUpdate', 1, 1, -1000, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_frame_menu` VALUES (6, '微服务管理', '微服务管理', 'microservice', '/admin/microservice/index', 1, 1, 'cloud', 'common-service::/admin/user::saveOrUpdateUser', 1, 2, -1000, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_frame_menu` VALUES (8, 'menu_datainterface_config', '接口配置', 'datainterface', '/admin/datainterface/index', 1, 1, 'controls-vertical-alt', 'common-service::/数据接口管理::查询数据接口', 1, 3, -1000, 'admin', '2020-05-05 03:08:52', 'admin', '2020-05-05 03:08:52', NULL);
INSERT INTO `t_frame_menu` VALUES (11, 'systemRoleAdmin', '角色管理', 'systemRoleAdmin', '/admin/role/index', 1, 1, 'sms-flash', 'common-service::/admin/role/::saveOrUpdateRoles', 1, 4, -1000, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_frame_menu` VALUES (12, 'systemUserdmin', '用户管理', 'systemUserdmin', '/admin/user/index', 1, 1, 'people', 'common-service::/admin/user::saveOrUpdateUser', 1, 5, -1000, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_frame_menu` VALUES (13, 'testCarousel', 'demo示例', 'testCarousel', '/test/demo/index', 1, 1, 'window', 'common-service::/数据接口管理::查询数据接口', 1, 1, -990, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_frame_menu` VALUES (14, 'openlayer5', 'openlayer5', 'openlayer5', '/test/openlayers5/index', 1, 1, 'window', 'common-service::/数据接口管理::查询数据接口', 1, 1, -990, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `t_frame_menu` VALUES (15, '文件管理', '文件管理', 'fileManagerByAdmin', '/admin/mongodb/gridfs/index', 1, 2, 'folder', NULL, 1, 6, -1000, NULL, NULL, NULL, NULL, '{\"pageType\": \"admin\"}');
INSERT INTO `t_frame_menu` VALUES (16, '数据字典管理', '数据字典管理', 'dataDicManager', '/admin/system/dic/index', 1, 1, 'folder', 'common-service::系统管理/数据字典::保存或者更新', 1, 2, -1000, NULL, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_frame_menu_data
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_menu_data`;
CREATE TABLE `t_frame_menu_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `data_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`menu_code`, `data_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `ak_kid2`(`id`) USING BTREE,
  INDEX `fk_reference_35`(`data_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_menu_data
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_menu_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_menu_resource`;
CREATE TABLE `t_frame_menu_resource`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`menu_code`, `resource_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `fk_reference_23`(`resource_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '菜单资源相关资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_menu_resource
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_org
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_org`;
CREATE TABLE `t_frame_org`  (
  `org_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `org_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `org_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `parent_org_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `status` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `validator_start` datetime(0) NULL DEFAULT NULL,
  `validator_end` datetime(0) NULL DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`org_code`) USING BTREE,
  UNIQUE INDEX `index_1`(`org_code`, `status`, `validator_start`, `validator_end`) USING BTREE,
  INDEX `fk_reference_30`(`org_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_org
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_role
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_role`;
CREATE TABLE `t_frame_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色类型，  暂时不用！',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态 0：无效/1：有效',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `udx_t_frame_role_code`(`role_code`) USING BTREE,
  INDEX `fk_reference_29`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_role
-- ----------------------------
INSERT INTO `t_frame_role` VALUES (1, 'Admin', '管理员', 'default', 'admin', '2020-01-18 00:48:11', 'admin', '2020-08-04 13:31:43', 1);
INSERT INTO `t_frame_role` VALUES (3, 'User', '默认用户权限', 'default', 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47', 1);

-- ----------------------------
-- Table structure for t_frame_role_data
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_role_data`;
CREATE TABLE `t_frame_role_data`  (
  `data_auth_list_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `data_dimension` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限维度:： org（组织机构） /  micro 微服务',
  `data_dimension_value` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '权限值',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`data_auth_list_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据权限列表\r\n角色类型为功能权限之外的权限统一为功能权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_role_data
-- ----------------------------
INSERT INTO `t_frame_role_data` VALUES (1, 1, 'micro', 'common-service', 'admin', '2020-01-16 09:00:13', 'admin', '2020-01-16 09:00:21');
INSERT INTO `t_frame_role_data` VALUES (2, 1, 'micro', 'xgsixteen', 'admin', '2020-01-16 09:00:47', 'admin', '2020-01-16 09:00:54');
INSERT INTO `t_frame_role_data` VALUES (3, 5, 'micro', 'common-service', 'admin', '2020-01-16 09:13:57', 'admin', '2020-01-16 09:14:04');
INSERT INTO `t_frame_role_data` VALUES (4, 5, 'micro', 'xgsixteen', 'admin', '2020-01-16 09:13:57', 'admin', '2020-01-16 09:14:04');

-- ----------------------------
-- Table structure for t_frame_role_data_interface
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_role_data_interface`;
CREATE TABLE `t_frame_role_data_interface`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL COMMENT '角色id',
  `data_interface_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '接口名称的创建时的md5值存放在mongodb中，请参考DataInterFaceVO',
  `belong_microservice` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '所属微服务',
  `data_interface_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '接口名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `udx_frame_role_data_interface`(`role_id`, `data_interface_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '角色数据接口权限' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_role_data_interface
-- ----------------------------
INSERT INTO `t_frame_role_data_interface` VALUES (93, 1, '5e6c43e07ad92a77cfad8ba6', 'COMMONSERVICE', '查询需要授权的资源清单列表');
INSERT INTO `t_frame_role_data_interface` VALUES (94, 1, '5e6c509e7ad92a77cfad8de2', 'COMMONSERVICE', '查询当前角色操作权限列表');
INSERT INTO `t_frame_role_data_interface` VALUES (95, 1, '5e6d666dc6af767df8096e37', 'COMMONSERVICE', '查询需要授权的接口清单');
INSERT INTO `t_frame_role_data_interface` VALUES (96, 1, '6191e5feb221dd9d0acb24ae2895fb80', 'COMMONSERVICE', '动态SQL需要授权给用户的示例');
INSERT INTO `t_frame_role_data_interface` VALUES (97, 1, 'fca0c2236325f81017b12e4da4d32990', 'COMMONSERVICE', '查询系统角色列表');
INSERT INTO `t_frame_role_data_interface` VALUES (98, 1, '3efa00c2c4b06b73758fac9e95e9b6a9', 'COMMONSERVICE', '查询所有用户的接口');
INSERT INTO `t_frame_role_data_interface` VALUES (102, 1, 'fbf2e7c0ec6c91d9041ccd92509ce309', 'COMMONSERVICE', '按用户ID查询用户的角色列表');

-- ----------------------------
-- Table structure for t_frame_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_role_menu`;
CREATE TABLE `t_frame_role_menu`  (
  `role_menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '系统角色id',
  `role_id` int NOT NULL COMMENT '角色id',
  `menu_id` bigint NULL DEFAULT NULL,
  `role_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`role_menu_id`) USING BTREE,
  INDEX `ak_kid`(`role_menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统角色菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_role_menu_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_role_menu_resource`;
CREATE TABLE `t_frame_role_menu_resource`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `role_menu` bigint NULL DEFAULT NULL,
  `menu_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE,
  INDEX `fk_reference_25`(`menu_code`) USING BTREE,
  INDEX `fk_reference_26`(`menu_code`, `resource_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统角色菜单资源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_role_menu_resource
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_role_resource`;
CREATE TABLE `t_frame_role_resource`  (
  `role_resource_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色菜单资源ID',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `resource_id` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`role_resource_id`) USING BTREE,
  INDEX `ak_kid`(`role_resource_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1449 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '角色资源列表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_role_resource
-- ----------------------------
INSERT INTO `t_frame_role_resource` VALUES (1366, 1, -1001);
INSERT INTO `t_frame_role_resource` VALUES (1367, 1, -1000);
INSERT INTO `t_frame_role_resource` VALUES (1368, 1, 1);
INSERT INTO `t_frame_role_resource` VALUES (1369, 1, 3);
INSERT INTO `t_frame_role_resource` VALUES (1376, 1, 32);
INSERT INTO `t_frame_role_resource` VALUES (1377, 1, 33);
INSERT INTO `t_frame_role_resource` VALUES (1378, 1, 34);
INSERT INTO `t_frame_role_resource` VALUES (1379, 1, 35);
INSERT INTO `t_frame_role_resource` VALUES (1380, 1, 36);
INSERT INTO `t_frame_role_resource` VALUES (1381, 1, 37);
INSERT INTO `t_frame_role_resource` VALUES (1382, 1, 38);
INSERT INTO `t_frame_role_resource` VALUES (1404, 1, 60);
INSERT INTO `t_frame_role_resource` VALUES (1405, 1, 61);
INSERT INTO `t_frame_role_resource` VALUES (1406, 1, 62);
INSERT INTO `t_frame_role_resource` VALUES (1407, 1, 63);
INSERT INTO `t_frame_role_resource` VALUES (1408, 1, 64);
INSERT INTO `t_frame_role_resource` VALUES (1409, 1, 65);
INSERT INTO `t_frame_role_resource` VALUES (1410, 1, 66);
INSERT INTO `t_frame_role_resource` VALUES (1412, 1, 86);
INSERT INTO `t_frame_role_resource` VALUES (1413, 1, 87);
INSERT INTO `t_frame_role_resource` VALUES (1414, 1, 88);
INSERT INTO `t_frame_role_resource` VALUES (1415, 1, 89);
INSERT INTO `t_frame_role_resource` VALUES (1416, 1, 95);
INSERT INTO `t_frame_role_resource` VALUES (1417, 1, 96);
INSERT INTO `t_frame_role_resource` VALUES (1422, 1, 102);
INSERT INTO `t_frame_role_resource` VALUES (1423, 1, 103);
INSERT INTO `t_frame_role_resource` VALUES (1424, 1, 104);
INSERT INTO `t_frame_role_resource` VALUES (1425, 1, 106);
INSERT INTO `t_frame_role_resource` VALUES (1434, 1, 117);
INSERT INTO `t_frame_role_resource` VALUES (1435, 1, 118);
INSERT INTO `t_frame_role_resource` VALUES (1436, 1, 119);
INSERT INTO `t_frame_role_resource` VALUES (1437, 1, 120);
INSERT INTO `t_frame_role_resource` VALUES (1438, 1, 121);
INSERT INTO `t_frame_role_resource` VALUES (1439, 1, 122);
INSERT INTO `t_frame_role_resource` VALUES (1440, 1, 123);
INSERT INTO `t_frame_role_resource` VALUES (1441, 1, 124);
INSERT INTO `t_frame_role_resource` VALUES (1442, 1, 125);
INSERT INTO `t_frame_role_resource` VALUES (1443, 1, 126);
INSERT INTO `t_frame_role_resource` VALUES (1444, 1, 127);
INSERT INTO `t_frame_role_resource` VALUES (1445, 1, 128);
INSERT INTO `t_frame_role_resource` VALUES (1446, 1, 129);
INSERT INTO `t_frame_role_resource` VALUES (1447, 1, 130);

-- ----------------------------
-- Table structure for t_frame_user
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_user`;
CREATE TABLE `t_frame_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `password` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `email` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `full_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `sex` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `mobile_phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '手机号',
  `session_key` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'session_key',
  `default_role` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'User' COMMENT '默认角色',
  `user_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'admin' COMMENT '用户类型：后台管理用户(admin)/app的名称（用户注册时带必填上来源）',
  `user_regist_source` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT 'background' COMMENT '用户注册来源：后台(background)/其它appid',
  `status` int NOT NULL DEFAULT 1 COMMENT '1:有效 0:无效 -1:过期',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  `avatar` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '头像',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ak_kid`(`id`) USING BTREE,
  UNIQUE INDEX `inx_user_user_id`(`user_name`, `user_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 688 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_user
-- ----------------------------
INSERT INTO `t_frame_user` VALUES (1, 'admin', '3e2345598780429e4758006e801f4b88', 'admin@qq.com', '无敌超级管理员', 'F', '111', NULL, 'Admin', 'admin', 'background', 3, 'admin', '2016-01-11 12:33:05', 'admin', '2021-06-08 16:05:55', NULL);

-- ----------------------------
-- Table structure for t_frame_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_user_auth`;
CREATE TABLE `t_frame_user_auth`  (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `validator_start` datetime(0) NULL DEFAULT NULL COMMENT '有效开始时间',
  `validator_end` datetime(0) NULL DEFAULT NULL COMMENT '有效结束时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uidx_f_auth_user_auth_id`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_user_auth
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_user_ref
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_user_ref`;
CREATE TABLE `t_frame_user_ref`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attribute_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `attribute_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`, `attribute_name`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_user_ref
-- ----------------------------

-- ----------------------------
-- Table structure for t_frame_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_user_role`;
CREATE TABLE `t_frame_user_role`  (
  `user_role_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  `validator_start` datetime(0) NULL DEFAULT NULL COMMENT '有效开始时间',
  `validator_end` datetime(0) NULL DEFAULT NULL COMMENT '有效结束时间',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`user_role_id`) USING BTREE,
  UNIQUE INDEX `uidx_f_auth_user_auth_id`(`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 38 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_frame_user_role
-- ----------------------------
INSERT INTO `t_frame_user_role` VALUES (12, 173, 2, '2020-03-17 16:00:00', '2021-03-24 16:00:00', NULL, NULL, NULL, NULL);
INSERT INTO `t_frame_user_role` VALUES (13, 173, 3, '2020-03-17 16:00:00', '2021-03-16 16:00:00', 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47');
INSERT INTO `t_frame_user_role` VALUES (14, 173, 7, '2020-03-17 16:00:00', '2020-03-30 16:00:00', NULL, NULL, NULL, NULL);
INSERT INTO `t_frame_user_role` VALUES (25, 272, 1, NULL, NULL, 'admin', '2020-01-16 00:48:11', 'admin4', '2020-03-19 00:47:11');
INSERT INTO `t_frame_user_role` VALUES (26, 272, 2, NULL, NULL, 'admin', '2020-01-16 08:41:38', 'admin', '2020-03-15 13:35:53');
INSERT INTO `t_frame_user_role` VALUES (27, 272, 3, NULL, NULL, 'admin', '2020-01-16 08:46:23', 'admin', '2020-03-16 11:11:47');
INSERT INTO `t_frame_user_role` VALUES (28, 272, 4, NULL, NULL, 'admin', '2020-01-16 08:53:13', 'admin', '2020-01-16 08:53:26');
INSERT INTO `t_frame_user_role` VALUES (29, 272, 5, NULL, NULL, 'admin', '2020-01-16 09:07:39', 'admin', '2020-01-16 09:07:45');
INSERT INTO `t_frame_user_role` VALUES (30, 272, 6, NULL, NULL, 'admin', '2020-01-16 09:07:39', 'admin', '2020-01-16 09:07:45');
INSERT INTO `t_frame_user_role` VALUES (31, 272, 7, NULL, NULL, 'admin', '2020-03-15 11:37:12', 'admin', '2020-03-15 11:38:57');
INSERT INTO `t_frame_user_role` VALUES (32, 272, 8, NULL, NULL, 'admin', '2020-03-15 11:40:59', 'admin', '2020-03-15 11:40:59');
INSERT INTO `t_frame_user_role` VALUES (33, 272, 9, NULL, NULL, 'admin', '2020-03-15 11:43:06', 'admin', '2020-03-15 11:43:06');
INSERT INTO `t_frame_user_role` VALUES (34, 272, 10, NULL, NULL, 'admin', '2020-03-15 11:44:13', 'admin', '2020-03-15 13:49:51');
INSERT INTO `t_frame_user_role` VALUES (36, 275, 12, NULL, NULL, 'admin', '2020-05-05 19:16:12', 'admin', '2020-05-05 11:23:48');
INSERT INTO `t_frame_user_role` VALUES (37, 1, 1, '2020-01-16 19:25:12', '2020-01-31 19:25:16', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for t_framework_resource
-- ----------------------------
DROP TABLE IF EXISTS `t_framework_resource`;
CREATE TABLE `t_framework_resource`  (
  `resource_id` bigint NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源编码',
  `resource_name` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源名称',
  `resource_path` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '资源路径',
  `method` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源验证式',
  `belong_microservice` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属微服务',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_date` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_date` datetime(0) NULL DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`resource_id`) USING BTREE,
  INDEX `ak_kid`(`resource_id`) USING BTREE,
  INDEX `ui_framework_resorce_code`(`resource_path`, `resource_code`, `belong_microservice`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 143 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统资源，用来记录所有的系统的URI服务资源\r\n\r\n可以定义参数类型并进行参数类型的检查' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_framework_resource
-- ----------------------------
INSERT INTO `t_framework_resource` VALUES (-1001, 'saveOrUpdateUser', '用户角色授权', '/admin/user', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (-1000, 'saveOrUpdateRoles', '操作权限授权', '/admin/role/', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (1, 'datainterface_page_query', '查询接口列表分页', '系统管理/接口配置', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (3, 'datainterface_params_page_query', '查询接口参数列表分页', '系统管理/接口配置', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (32, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (33, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (34, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (35, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (36, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (37, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (38, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (60, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09');
INSERT INTO `t_framework_resource` VALUES (61, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09');
INSERT INTO `t_framework_resource` VALUES (62, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09');
INSERT INTO `t_framework_resource` VALUES (63, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09');
INSERT INTO `t_framework_resource` VALUES (64, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09');
INSERT INTO `t_framework_resource` VALUES (65, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09');
INSERT INTO `t_framework_resource` VALUES (66, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09');
INSERT INTO `t_framework_resource` VALUES (86, '删除文件', '管理员后台删除文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (87, '查询上传文件', '管理员后台查询文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (88, '下载文件', '管理员后台下载文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (89, '查看文件', '管理员后台查看文件，需要授', '/admin/mongo', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (90, '删除文件', '删除文件，仅登录用户删除自己的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (91, '查询个人上传文件', '查询个人上传文件，需登录', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (92, '下载本人文件', '下载文件，仅可下载本人的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (93, '查看本人文件', '查看本人文件，仅可查看本人的文件', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (94, '上传文件', '上传文件，需要登录', '/personal/mongo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (95, '刷新缓存', '刷新缓存，需授权', '系统管理/数据字典', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (96, '保存或者更新', '保存或者更新数据字典', '系统管理/数据字典', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (102, '更新数据接口', '更新数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (103, '生效或者失效数据接口', '生效或者失效数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (104, '查询数据接口', '查询数据接口', '/数据接口管理', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (106, 'resetPassword', '重置密码', '/admin/user', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (107, 'updatePassWordByUser', '用户更新密码', '/userinfo', 'ALLSYSTEMUSER', 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_framework_resource` VALUES (117, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (118, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (119, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (120, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (121, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (122, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (123, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (124, 'deleteJob', '删除定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13');
INSERT INTO `t_framework_resource` VALUES (125, 'resumeJob', '恢复定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13');
INSERT INTO `t_framework_resource` VALUES (126, 'pauseTrigger', '暂停定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13');
INSERT INTO `t_framework_resource` VALUES (127, 'pauseJob', '暂停定时任务的JOB', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13');
INSERT INTO `t_framework_resource` VALUES (128, 'resumeTrigger', '恢复定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13');
INSERT INTO `t_framework_resource` VALUES (129, 'createOrUpdate', '创建或更新定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13');
INSERT INTO `t_framework_resource` VALUES (130, 'runAJobNow', '手动运行定时任务', '定时任务/quartz', 'BYUSERPERMISSION', 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13');
INSERT INTO `t_framework_resource` VALUES (131, 'admin-accountList', '管理员获取用户账户列表', '/admin/account', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (132, 'admin-coinList', '管理员获取币种列表', '/admin/account', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (133, 'admin-walletInList', '管理员获取钱包充值记录列表', '/admin/account', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (134, 'admin-walletOutList', '管理员获取钱包提现记录列表', '/admin/account', 'BYUSERPERMISSION', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (135, '/exchange', '发起闪兑兑换', '/exchangeMarket', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (136, '/getExchangeRecord', '获取闪兑记录', '/exchangeMarket', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (137, '/insideTransfer', '用户站内转账', '/account', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (138, '/getAccountByCoinId', '根据币种id用户获取账户', '/account', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (139, '/getUserDetailList', '用户获取资金明细列表', '/account', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (140, '/accountList', '用户获取账户列表', '/account', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (141, 'getRecAddr', '用户获取币种充值地址', '/wallet', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');
INSERT INTO `t_framework_resource` VALUES (142, 'transferOut', '用户站外转出', '/wallet', 'ALLSYSTEMUSER', 'unknow-account-service', 'admin', '2020-08-11 15:37:12', 'admin', '2020-08-11 15:37:12');

-- ----------------------------
-- Table structure for t_framework_resource2
-- ----------------------------
DROP TABLE IF EXISTS `t_framework_resource2`;
CREATE TABLE `t_framework_resource2`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `resource_code` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `resource_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `uri` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `method` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`resource_code`) USING BTREE,
  UNIQUE INDEX `ui_framework_resorce_code`(`resource_code`) USING BTREE,
  INDEX `ak_kid`(`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '系统资源，用来记录所有的系统的URI服务资源\n可以定义参数类型并进行参数类型的检查' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_framework_resource2
-- ----------------------------

-- ----------------------------
-- Table structure for t_microservice_register
-- ----------------------------
DROP TABLE IF EXISTS `t_microservice_register`;
CREATE TABLE `t_microservice_register`  (
  `microservice_id` int NOT NULL AUTO_INCREMENT,
  `microservice_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '微服务名称',
  `create_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` datetime(0) NULL DEFAULT NULL,
  `update_by` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`microservice_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统微服务注册表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_microservice_register
-- ----------------------------
INSERT INTO `t_microservice_register` VALUES (6, 'common-service', 'admin', '2020-08-11 15:37:14', 'admin', '2020-08-11 15:37:14');
INSERT INTO `t_microservice_register` VALUES (9, 'spring-gateway', 'admin', '2020-08-11 15:37:09', 'admin', '2020-08-11 15:37:09');
INSERT INTO `t_microservice_register` VALUES (11, 'unknow-account-service', 'admin', '2020-08-11 15:37:11', 'admin', '2020-08-11 15:37:11');
INSERT INTO `t_microservice_register` VALUES (12, 'unknow-wallet-service', 'admin', '2020-08-11 15:37:13', 'admin', '2020-08-11 15:37:13');

-- ----------------------------
-- Table structure for t_system_dic_item
-- ----------------------------
DROP TABLE IF EXISTS `t_system_dic_item`;
CREATE TABLE `t_system_dic_item`  (
  `dic_item_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dic_master_id` bigint NOT NULL COMMENT '数据字典主表id',
  `dic_item_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典项编码',
  `dic_item_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典项名称',
  `dic_item_value` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典项值',
  `ext_attribut1` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut3` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut2` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut4` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展属性1',
  `ext_attribut5` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展属性1',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态，1/有效 0/无效 -1/过期',
  `create_by` bigint NOT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_by` bigint NOT NULL COMMENT '更新人',
  `update_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新日期',
  `language` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'zh_CN',
  `seq` int NOT NULL DEFAULT 0,
  PRIMARY KEY (`dic_item_id`) USING BTREE,
  UNIQUE INDEX `udx_dic_item_code`(`dic_master_id`, `dic_item_code`, `language`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 107 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统数据字典字典项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_system_dic_item
-- ----------------------------
INSERT INTO `t_system_dic_item` VALUES (1, 1, 'company_name', 'XXXX有限公司', 'XXXX有限公司', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-02-22 17:42:19', 1, '2020-08-10 19:55:20', 'zh_CN', 10);
INSERT INTO `t_system_dic_item` VALUES (13, 15, 'active', '有效', '1', '1', NULL, '12', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 15);
INSERT INTO `t_system_dic_item` VALUES (14, 15, 'inactive', '无效', '0', '2', NULL, '', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 20);
INSERT INTO `t_system_dic_item` VALUES (15, 15, 'expired', '过期', '-1', '3', NULL, '', NULL, NULL, 1, 1, '2020-03-04 10:19:32', 1, '2020-05-03 02:15:59', 'zh_CN', 25);
INSERT INTO `t_system_dic_item` VALUES (16, 16, 'commonService', '通用基础服务', 'COMMON-SERVICE', '', NULL, '', NULL, NULL, 1, 1, '2020-03-04 12:40:56', 1, '2020-07-28 13:41:03', 'zh_CN', 5);
INSERT INTO `t_system_dic_item` VALUES (20, 16, 'spring-gateway', 'SPRING网关服务', 'SPRING-GATEWAY', '', NULL, '', NULL, NULL, 1, 1, '2020-03-04 12:40:56', 1, '2020-07-28 13:41:03', 'zh_CN', 5);
INSERT INTO `t_system_dic_item` VALUES (25, 16, 'General', '所有微服务通用', 'General', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-05 10:32:09', 1, '2020-07-28 13:41:03', 'zh_CN', 0);
INSERT INTO `t_system_dic_item` VALUES (44, 28, 'admin', '后台管理用户', 'admin', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:51:50', 1, '2020-03-18 02:29:07', 'zh_CN', 5);
INSERT INTO `t_system_dic_item` VALUES (45, 28, 'wechatApplet', '微信小程序用户', 'wechatApplet', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:55:03', 1, '2020-03-18 02:29:07', 'zh_CN', 10);
INSERT INTO `t_system_dic_item` VALUES (46, 29, 'background', '后台添加', 'background', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-03-18 01:57:41', 1, '2020-03-18 01:57:41', 'zh_CN', 5);
INSERT INTO `t_system_dic_item` VALUES (47, 30, 'Reset', '重置', '3', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 20);
INSERT INTO `t_system_dic_item` VALUES (48, 30, 'New', '新建', '2', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 15);
INSERT INTO `t_system_dic_item` VALUES (49, 30, 'Inactive', '失效', '-1', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 10);
INSERT INTO `t_system_dic_item` VALUES (50, 30, 'Active', '生效', '1', NULL, NULL, NULL, NULL, NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06', 'zh_CN', 5);
INSERT INTO `t_system_dic_item` VALUES (100, 16, 'UNKNOW-ACCOUNT-SERVICE', 'UNKNOW-ACCOUNT-SERVICE', 'UNKNOW-ACCOUNT-SERVICE', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-07-28 13:41:03', 1, '2020-07-28 13:41:03', 'zh_CN', 20);
INSERT INTO `t_system_dic_item` VALUES (101, 16, 'UNKNOW-WALLET-SERVICE', 'UNKNOW-WALLET-SERVICE', 'UNKNOW-WALLET-SERVICE', NULL, NULL, NULL, NULL, NULL, 1, 1, '2020-07-28 13:41:03', 1, '2020-07-28 13:41:03', 'zh_CN', 15);
INSERT INTO `t_system_dic_item` VALUES (106, 48, 'isMfaVerify', NULL, 'false', NULL, NULL, NULL, NULL, NULL, 1, 1, '2021-06-03 16:21:54', 1, '2021-06-03 16:21:54', 'zh_CN', 0);

-- ----------------------------
-- Table structure for t_system_dic_master
-- ----------------------------
DROP TABLE IF EXISTS `t_system_dic_master`;
CREATE TABLE `t_system_dic_master`  (
  `dic_master_id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dic_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字典编码',
  `dic_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '字典名称',
  `remark` varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `belong_micro_service` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT 'General' COMMENT '所属微服务，默认为General(通用)',
  `parent_master_id` bigint NULL DEFAULT NULL COMMENT '父级id',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态，1/有效 0/无效 -1/过期',
  `create_by` bigint NOT NULL COMMENT '创建人',
  `create_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建日期',
  `update_by` bigint NOT NULL COMMENT '更新人',
  `update_date` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新日期',
  PRIMARY KEY (`dic_master_id`) USING BTREE,
  UNIQUE INDEX `udx_dic_master_code`(`dic_code`, `belong_micro_service`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统数据字典主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_system_dic_master
-- ----------------------------
INSERT INTO `t_system_dic_master` VALUES (1, 'systemBaseConfig', '系统基础配置', '系统基础配置', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-03-22 07:14:57');
INSERT INTO `t_system_dic_master` VALUES (15, 'commStatus', '系统通用的状态值', '通用状态', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-05-05 19:17:31');
INSERT INTO `t_system_dic_master` VALUES (16, 'microServiceList', '微服务列表', '微服务列表', 'General', NULL, 1, 1, '2020-02-22 16:32:24', 1, '2020-07-28 13:41:03');
INSERT INTO `t_system_dic_master` VALUES (28, 'userType', '用户类型', '用户的来源类型，各个应用，后台创建的就属于admin，其它的属于各个应用注册而来，可以定义，目前微信小程序是微信小程序的名称。', 'General', NULL, 1, 1, '2020-03-18 01:51:50', 1, '2020-03-18 02:29:07');
INSERT INTO `t_system_dic_master` VALUES (29, 'userRegisterSource', '用户注册来源', '用户的注册来源，后台添加的为：background 微信小程序的为微信小程序的ID', 'General', NULL, 1, 1, '2020-03-18 01:57:41', 1, '2020-08-11 20:44:48');
INSERT INTO `t_system_dic_master` VALUES (30, 'userStatus', '用户状态', '用户状态', 'COMMON-SERVICE', NULL, 1, 272, '2020-03-18 23:47:26', 272, '2020-03-18 23:58:06');
INSERT INTO `t_system_dic_master` VALUES (48, 'systemConfig', '系统配置', NULL, 'General', NULL, 1, 1, '2021-06-03 16:20:11', 2, '2021-06-03 16:20:16');

-- 增加菜单管理的相关脚本
INSERT INTO `t_system_dic_master` (`dic_master_id`, `dic_code`, `dic_name`, `remark`, `belong_micro_service`, `parent_master_id`, `status`, `create_by`, `create_date`, `update_by`, `update_date`) VALUES (49, 'menuShowType', '显示方式', '0：有子菜单时显示 1：有权限时显示 2：任何时候都显示', 'General', NULL, 1, 1, '2021-12-03 17:27:36', 1, '2021-12-03 17:27:36');
INSERT INTO `t_system_dic_master` (`dic_master_id`, `dic_code`, `dic_name`, `remark`, `belong_micro_service`, `parent_master_id`, `status`, `create_by`, `create_date`, `update_by`, `update_date`) VALUES (50, 'menuType', '菜单类型', '菜单类型，0：目录 1：菜单', 'General', NULL, 1, 1, '2021-12-03 17:28:44', 1, '2021-12-03 17:35:15');

INSERT INTO `t_system_dic_item` (`dic_item_id`, `dic_master_id`, `dic_item_code`, `dic_item_name`, `dic_item_value`, `ext_attribut1`, `ext_attribut3`, `ext_attribut2`, `ext_attribut4`, `ext_attribut5`, `status`, `create_by`, `create_date`, `update_by`, `update_date`, `language`, `seq`) VALUES (107, 49, '有子菜单时显示', '有子菜单时显示', '0', NULL, NULL, NULL, NULL, NULL, 1, 1, '2021-12-03 17:27:36', 1, '2021-12-03 17:27:36', 'zh_CN', 5);
INSERT INTO `t_system_dic_item` (`dic_item_id`, `dic_master_id`, `dic_item_code`, `dic_item_name`, `dic_item_value`, `ext_attribut1`, `ext_attribut3`, `ext_attribut2`, `ext_attribut4`, `ext_attribut5`, `status`, `create_by`, `create_date`, `update_by`, `update_date`, `language`, `seq`) VALUES (108, 49, '有权限时显示', '有权限时显示', '1', NULL, NULL, NULL, NULL, NULL, 1, 1, '2021-12-03 17:27:36', 1, '2021-12-03 17:27:36', 'zh_CN', 10);
INSERT INTO `t_system_dic_item` (`dic_item_id`, `dic_master_id`, `dic_item_code`, `dic_item_name`, `dic_item_value`, `ext_attribut1`, `ext_attribut3`, `ext_attribut2`, `ext_attribut4`, `ext_attribut5`, `status`, `create_by`, `create_date`, `update_by`, `update_date`, `language`, `seq`) VALUES (109, 49, '任何时候都显示', '任何时候都显示', '2', NULL, NULL, NULL, NULL, NULL, 1, 1, '2021-12-03 17:27:36', 1, '2021-12-03 17:27:36', 'zh_CN', 15);
INSERT INTO `t_system_dic_item` (`dic_item_id`, `dic_master_id`, `dic_item_code`, `dic_item_name`, `dic_item_value`, `ext_attribut1`, `ext_attribut3`, `ext_attribut2`, `ext_attribut4`, `ext_attribut5`, `status`, `create_by`, `create_date`, `update_by`, `update_date`, `language`, `seq`) VALUES (110, 50, '目录', '目录', '0', NULL, NULL, NULL, NULL, NULL, 1, 1, '2021-12-03 17:28:44', 1, '2021-12-03 17:35:15', 'zh_CN', 5);
INSERT INTO `t_system_dic_item` (`dic_item_id`, `dic_master_id`, `dic_item_code`, `dic_item_name`, `dic_item_value`, `ext_attribut1`, `ext_attribut3`, `ext_attribut2`, `ext_attribut4`, `ext_attribut5`, `status`, `create_by`, `create_date`, `update_by`, `update_date`, `language`, `seq`) VALUES (111, 50, '菜单', '菜单', '1', NULL, NULL, NULL, NULL, NULL, 1, 1, '2021-12-03 17:28:44', 1, '2021-12-03 17:35:15', 'zh_CN', 10);


SET FOREIGN_KEY_CHECKS = 1;
