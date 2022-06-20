/*
 Navicat Premium Data Transfer

 Source Server         : 309-mysql-192.168.30.96
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 192.168.30.96:10002
 Source Schema         : dapp-admin_tc

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 16/06/2022 19:53:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `node_type` int NOT NULL COMMENT '类型（1分类；2文章）',
  `language_type` int NOT NULL COMMENT '分类时语言类型（1中文，2英文,3泰文）',
  `parent_id` int NOT NULL DEFAULT 0 COMMENT '所属分类id',
  `class_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分类编码（当前为分类时不能为空）',
  `article_sort` int NOT NULL DEFAULT 0 COMMENT '排序号（越大越靠前）',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标题(分类名或文章标题)',
  `sub_title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '副标题',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章封面图',
  `article_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '文章内容',
  `article_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文章作者',
  `status` int NOT NULL DEFAULT 1 COMMENT '状态（1正常；0禁用）',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建人',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '修改人',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `class_code_unique`(`class_code`, `language_type`) USING BTREE COMMENT '分类名称组合语言类型唯一',
  INDEX `select_inx_1`(`node_type`, `status`, `parent_id`) USING BTREE,
  INDEX `select_inx_2`(`node_type`, `class_code`, `status`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
