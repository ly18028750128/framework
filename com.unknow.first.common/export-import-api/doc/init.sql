/*
 Navicat Premium Data Transfer

 Source Server         : 309-mysql-192.168.30.239
 Source Server Type    : MySQL
 Source Server Version : 80030 (8.0.30)
 Source Host           : 192.168.30.239:10002
 Source Schema         : framework

 Target Server Type    : MySQL
 Target Server Version : 80030 (8.0.30)
 File Encoding         : 65001

 Date: 09/02/2023 19:18:58
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_frame_export_template
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_export_template`;
CREATE TABLE `t_frame_export_template`  (
  `template_id` int NOT NULL AUTO_INCREMENT,
  `template_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '模板编码',
  `template_type` tinyint NOT NULL DEFAULT 10 COMMENT '模板类型：10(excel),20(pdf),30(word)',
  `file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件ID，存储在mongodb里的文件ObjectId',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态，1/有效 0/无效',
  `create_by` bigint NOT NULL COMMENT '创建人ID',
  `create_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_by` bigint NOT NULL COMMENT '更新人ID',
  `update_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  PRIMARY KEY (`template_id`) USING BTREE,
  UNIQUE INDEX `udx_export_template`(`template_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '导出模板表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_import_export_task
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_import_export_task`;
CREATE TABLE `t_frame_import_export_task`  (
  `task_id` bigint NOT NULL AUTO_INCREMENT,
  `task_type` tinyint NOT NULL COMMENT '任务类型：1(导入) 2(导出)',
  `file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '导入或者导出的文件名',
  `file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件id，用于存储导入或者导出的文件objectId',
  `error_file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '导入或者导出出错时保存错误的文件名',
  `error_file_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件id，用于存储导入或者导出出错时的文件objectId',
  `data_count` bigint NOT NULL DEFAULT 0 COMMENT '任务总行数',
  `data_error_count` bigint NOT NULL DEFAULT 0 COMMENT '错误行数',
  `data_correct_count` bigint NOT NULL DEFAULT 0 COMMENT '正确行数',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始执行时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束执行时间',
  `message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '返回消息',
  `task_status` tinyint NOT NULL DEFAULT 1 COMMENT '任务状态：1(未执行) 2(执行中)3(执行成功)-1(执行失败)',
  `create_by` bigint NOT NULL COMMENT '创建人ID',
  `create_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `update_by` bigint NOT NULL COMMENT '更新人ID',
  `update_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  `execute_seconds` bigint GENERATED ALWAYS AS (timestampdiff(SECOND,`start_time`,`end_time`)) STORED NULL,
  `belong_microservice` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT 'common' COMMENT '所属微服务',
  `process_class` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '线程执行类的名称，继承ImexportRunnableService',
  `params` json NULL COMMENT '任务参数',
  PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
