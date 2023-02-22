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

 Date: 11/02/2023 16:58:08
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_frame_export_template
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_export_template`;
CREATE TABLE `t_frame_export_template`
(
    `template_id`    int                                                           NOT NULL AUTO_INCREMENT,
    `template_code`  varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL COMMENT '模板编码',
    `template_type`  tinyint                                                       NOT NULL DEFAULT 10 COMMENT '模板类型：10(excel),20(pdf),30(word)',
    `file_id`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文件ID，存储在mongodb里的文件ObjectId',
    `status`         tinyint                                                       NOT NULL DEFAULT 1 COMMENT '状态，1/有效 0/无效',
    `create_by`      bigint                                                        NOT NULL COMMENT '创建人ID',
    `create_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
    `create_date`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `update_by`      bigint                                                        NOT NULL COMMENT '更新人ID',
    `update_by_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人名称',
    `update_date`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
    PRIMARY KEY (`template_id`) USING BTREE,
    UNIQUE INDEX `udx_export_template` (`template_code` ASC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT = '导出模板表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_frame_import_export_task
-- ----------------------------
DROP TABLE IF EXISTS `t_frame_import_export_task`;
CREATE TABLE `t_frame_import_export_task`
(
    `task_id`             bigint                                                                              NOT NULL AUTO_INCREMENT,
    `task_type`           tinyint                                                                             NOT NULL COMMENT '任务类型：1(导入) 2(导出)',
    `task_name`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                       NOT NULL COMMENT '任务名称',
    `file_name`           varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                       NOT NULL COMMENT '导入或者导出的文件名',
    `file_id`             varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                       NULL     DEFAULT NULL COMMENT '文件id，用于存储导入或者导出的文件objectId',
    `error_file_name`     varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                       NULL     DEFAULT NULL COMMENT '导入或者导出出错时保存错误的文件名',
    `error_file_id`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                       NULL     DEFAULT NULL COMMENT '文件id，用于存储导入或者导出出错时的文件objectId',
    `data_count`          bigint                                                                              NOT NULL DEFAULT 0 COMMENT '任务总行数',
    `data_error_count`    bigint                                                                              NOT NULL DEFAULT 0 COMMENT '错误行数',
    `data_correct_count`  bigint                                                                              NOT NULL DEFAULT 0 COMMENT '正确行数',
    `start_time`          datetime                                                                            NULL     DEFAULT NULL COMMENT '开始执行时间',
    `end_time`            datetime                                                                            NULL     DEFAULT NULL COMMENT '结束执行时间',
    `execute_seconds`     bigint GENERATED ALWAYS AS (timestampdiff(SECOND, `start_time`, `end_time`)) STORED NULL,
    `message`             varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                      NULL     DEFAULT NULL COMMENT '返回消息',
    `template_code`       varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                        NULL     DEFAULT NULL COMMENT '模板编码',
    `task_status`         tinyint                                                                             NOT NULL DEFAULT 1 COMMENT '任务状态：1(未执行) 2(执行中)3(执行成功)-1(执行失败)',
    `belong_microservice` varchar(200) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci                       NULL     DEFAULT 'common' COMMENT '所属微服务',
    `process_class`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                       NULL     DEFAULT NULL COMMENT '线程执行类的名称，导入继承ImportCallableService，导出继承ExportCallableService',
    `params`              json                                                                                NULL COMMENT '任务参数',
    `create_by`           bigint                                                                              NOT NULL COMMENT '创建人ID',
    `create_by_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                       NOT NULL COMMENT '创建人名称',
    `create_date`         timestamp                                                                           NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    `update_by`           bigint                                                                              NOT NULL COMMENT '更新人ID',
    `update_by_name`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci                       NOT NULL COMMENT '创建人名称',
    `update_date`         timestamp                                                                           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
    `data_dimension`      json                                                                                NULL COMMENT '数据权限，记录当时登录用户的数据权限，方便代码中控制权限',
    PRIMARY KEY (`task_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 40
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- 数据字典
INSERT INTO `t_system_dic_master` (`dic_master_id`, `dic_code`, `dic_name`, `remark`,
                                               `belong_micro_service`, `parent_master_id`, `status`,
                                               `create_by`, `create_date`, `update_by`,
                                               `update_date`)
VALUES (-51, '导出模板类型', '导出模板类型', '导出模板类型', 'General', NULL, 1, 1,
        '2023-02-21 15:52:03', 1, '2023-02-21 17:31:47');
INSERT INTO `t_system_dic_master` (`dic_master_id`, `dic_code`, `dic_name`, `remark`,
                                               `belong_micro_service`, `parent_master_id`, `status`,
                                               `create_by`, `create_date`, `update_by`,
                                               `update_date`)
VALUES (-52, '导入导出任务状态', '导入导出任务状态', '导入导出任务状态', 'General', NULL, 1, 1,
        '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38');


INSERT INTO `t_system_dic_item` (`dic_master_id`, `dic_item_code`, `dic_item_name`,
                                             `dic_item_value`, `ext_attribut1`, `ext_attribut3`,
                                             `ext_attribut2`, `ext_attribut4`, `ext_attribut5`,
                                             `status`, `create_by`, `create_date`, `update_by`,
                                             `update_date`, `language`, `seq`)
VALUES (-51, 'code_10', 'xls', '10', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-21 15:52:03', 1,
        '2023-02-21 17:31:47', 'zh_CN', 5);
INSERT INTO `t_system_dic_item` (`dic_master_id`, `dic_item_code`, `dic_item_name`,
                                             `dic_item_value`, `ext_attribut1`, `ext_attribut3`,
                                             `ext_attribut2`, `ext_attribut4`, `ext_attribut5`,
                                             `status`, `create_by`, `create_date`, `update_by`,
                                             `update_date`, `language`, `seq`)
VALUES (-51, 'code_20', 'pdf', '20', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-21 15:52:03', 1,
        '2023-02-21 17:31:47', 'zh_CN', 10);
INSERT INTO `t_system_dic_item` (`dic_master_id`, `dic_item_code`, `dic_item_name`,
                                             `dic_item_value`, `ext_attribut1`, `ext_attribut3`,
                                             `ext_attribut2`, `ext_attribut4`, `ext_attribut5`,
                                             `status`, `create_by`, `create_date`, `update_by`,
                                             `update_date`, `language`, `seq`)
VALUES (-51, 'code_30', 'word', '30', NULL, NULL, NULL, NULL, NULL, 1, 1, '2023-02-21 15:52:03', 1,
        '2023-02-21 17:31:47', 'zh_CN', 15);
INSERT INTO `t_system_dic_item` (`dic_master_id`, `dic_item_code`, `dic_item_name`,
                                             `dic_item_value`, `ext_attribut1`, `ext_attribut3`,
                                             `ext_attribut2`, `ext_attribut4`, `ext_attribut5`,
                                             `status`, `create_by`, `create_date`, `update_by`,
                                             `update_date`, `language`, `seq`)
VALUES (-52, 'taskStatus1', '未执行', '1', NULL, NULL, NULL, NULL, NULL, 1, 1,
        '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38', 'zh_CN', 5);
INSERT INTO `t_system_dic_item` (`dic_master_id`, `dic_item_code`, `dic_item_name`,
                                             `dic_item_value`, `ext_attribut1`, `ext_attribut3`,
                                             `ext_attribut2`, `ext_attribut4`, `ext_attribut5`,
                                             `status`, `create_by`, `create_date`, `update_by`,
                                             `update_date`, `language`, `seq`)
VALUES (-52, 'taskStatus2', '执行中', '2', NULL, NULL, NULL, NULL, NULL, 1, 1,
        '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38', 'zh_CN', 10);
INSERT INTO `t_system_dic_item` (`dic_master_id`, `dic_item_code`, `dic_item_name`,
                                             `dic_item_value`, `ext_attribut1`, `ext_attribut3`,
                                             `ext_attribut2`, `ext_attribut4`, `ext_attribut5`,
                                             `status`, `create_by`, `create_date`, `update_by`,
                                             `update_date`, `language`, `seq`)
VALUES (-52, 'taskStatus3', '执行成功', '3', NULL, NULL, NULL, NULL, NULL, 1, 1,
        '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38', 'zh_CN', 15);
INSERT INTO `t_system_dic_item` (`dic_master_id`, `dic_item_code`, `dic_item_name`,
                                             `dic_item_value`, `ext_attribut1`, `ext_attribut3`,
                                             `ext_attribut2`, `ext_attribut4`, `ext_attribut5`,
                                             `status`, `create_by`, `create_date`, `update_by`,
                                             `update_date`, `language`, `seq`)
VALUES (-52, 'taskStatus-1', '失败', '-1', NULL, NULL, NULL, NULL, NULL, 1, 1,
        '2023-02-22 14:28:38', 1, '2023-02-22 14:28:38', 'zh_CN', 20);

