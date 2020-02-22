-- --------------------------------------------------------
-- 主机                            :127.0.0.1
-- 服务器版本                         :5.0.67-community-log - MySQL Community Edition (GPL)
-- 服务器操作系统                       :Win32
-- HeidiSQL 版本                   :7.0.0.4278
-- 创建                            :2019-07-02 21:33:06
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出 framework 的数据库结构
DROP DATABASE IF EXISTS `framework`;
CREATE DATABASE IF NOT EXISTS `framework` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `framework`;


-- 导出  过程 framework.sp_test 结构
DROP PROCEDURE IF EXISTS `sp_test`;
DELIMITER //
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_test`(in v_user_name varchar(2000),in v_page integer  )
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
    END//
DELIMITER ;


-- 导出  表 framework.t_framework_resource 结构
DROP TABLE IF EXISTS `t_framework_resource`;
CREATE TABLE IF NOT EXISTS `t_framework_resource` (
  `id` bigint(20) NOT NULL auto_increment,
  `resource_code` varchar(200) collate utf8_bin NOT NULL COMMENT '资源编码',
  `resource_name` varchar(200) collate utf8_bin NOT NULL COMMENT '资源名称',
  `uri` varchar(2000) collate utf8_bin default NULL COMMENT '资源地址',
  `method` varchar(200) collate utf8_bin default NULL COMMENT '访问方法：get,post',
  `create_by` varchar(50) collate utf8_bin default NULL COMMENT '创建人',
  `create_date` datetime default NULL COMMENT '创建日期',
  `update_by` varchar(50) collate utf8_bin default NULL COMMENT '更新人',
  `update_date` datetime default NULL COMMENT '更新日期',
  PRIMARY KEY  (`resource_code`),
  UNIQUE KEY `ui_framework_resorce_code` (`resource_code`),
  KEY `ak_kid` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统资源，用来记录所有的系统的URI服务资源\n可以定义参数类型并进行参数类型的检查';

-- 正在导出表  framework.t_framework_resource 的数据: ~0 rows ((大约))
DELETE FROM `t_framework_resource`;
/*!40000 ALTER TABLE `t_framework_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_framework_resource` ENABLE KEYS */;


-- 导出  表 framework.t_frame_auth 结构
DROP TABLE IF EXISTS `t_frame_auth`;
CREATE TABLE IF NOT EXISTS `t_frame_auth` (
  `id` bigint(20) NOT NULL auto_increment,
  `auth_code` varchar(200) collate utf8_bin NOT NULL,
  `auth_type` varchar(20) collate utf8_bin NOT NULL COMMENT '组织机构/角色/流程结点',
  PRIMARY KEY  (`auth_code`,`auth_type`),
  KEY `ak_kid` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  framework.t_frame_auth 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_auth`;
/*!40000 ALTER TABLE `t_frame_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_auth` ENABLE KEYS */;


-- 导出  表 framework.t_frame_data_interface 结构
DROP TABLE IF EXISTS `t_frame_data_interface`;
CREATE TABLE IF NOT EXISTS `t_frame_data_interface` (
  `id` bigint(20) NOT NULL auto_increment,
  `data_code` varchar(20) collate utf8_bin NOT NULL COMMENT '数据编号',
  `data_name` varchar(200) collate utf8_bin default NULL COMMENT '数据名称',
  `data_type` varchar(20) collate utf8_bin default NULL COMMENT '数据类型：SQL(SQL)/存储过程(SP)/REST/（OTHER）其它',
  `data_execute_name` varchar(200) collate utf8_bin default NULL COMMENT '必须继承IFrameDataInterfaceService，\n            并在应用中正确的注册，可以不填，\n            默认为util.[接口类型].FrameDataInterfaceMainService\n            如果需要指定那么该接口必须继承IFrameDataInterfaceService',
  `description` text collate utf8_bin COMMENT '描述',
  `create_by` varchar(50) collate utf8_bin NOT NULL COMMENT '创建人',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_by` varchar(50) collate utf8_bin NOT NULL COMMENT '更新人',
  `update_date` datetime NOT NULL COMMENT '更新日期',
  PRIMARY KEY  (`data_code`),
  KEY `ak_kid` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  framework.t_frame_data_interface 的数据: ~4 rows ((大约))
DELETE FROM `t_frame_data_interface`;
/*!40000 ALTER TABLE `t_frame_data_interface` DISABLE KEYS */;
INSERT INTO `t_frame_data_interface` (`id`, `data_code`, `data_name`, `data_type`, `data_execute_name`, `description`, `create_by`, `create_date`, `update_by`, `update_date`) VALUES
	(4, 'REST_001', '快递单查询', 'REST', NULL, '快递100快递单查询服务', 'admin', '2016-01-08 18:53:17', 'admin', '2016-01-08 18:53:22'),
	(5, 'SP_001', '存储过程测试', 'SP', NULL, '存储过程测试', 'admin', '2016-01-10 21:16:48', 'admin', '2016-01-10 21:16:55'),
	(2, 'SQL_001', 'SQL_001', 'SQL', '', NULL, 'admin', '2016-01-07 18:58:22', 'admin', '2016-01-07 18:58:28'),
	(3, 'SQL_002', 'SQL_002', 'SQL', 'util.SQL.FrameDataInterfaceService', NULL, 'admin', '2016-01-07 18:59:33', 'admin', '2016-01-07 18:59:40');
/*!40000 ALTER TABLE `t_frame_data_interface` ENABLE KEYS */;


-- 导出  表 framework.t_frame_data_interface_params 结构
DROP TABLE IF EXISTS `t_frame_data_interface_params`;
CREATE TABLE IF NOT EXISTS `t_frame_data_interface_params` (
  `id` bigint(20) NOT NULL auto_increment,
  `data_code` varchar(20) collate utf8_bin NOT NULL COMMENT '数据编号',
  `seq` int(11) NOT NULL,
  `param_name` varchar(20) collate utf8_bin NOT NULL,
  `param_type` varchar(20) collate utf8_bin default NULL,
  `param_desc` varchar(200) collate utf8_bin default NULL,
  PRIMARY KEY  (`data_code`,`seq`,`param_name`),
  KEY `ak_kid` (`id`),
  CONSTRAINT `fk_reference_32` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  framework.t_frame_data_interface_params 的数据: ~5 rows ((大约))
DELETE FROM `t_frame_data_interface_params`;
/*!40000 ALTER TABLE `t_frame_data_interface_params` DISABLE KEYS */;
INSERT INTO `t_frame_data_interface_params` (`id`, `data_code`, `seq`, `param_name`, `param_type`, `param_desc`) VALUES
	(5, 'REST_001', 1, 'text', 'String', '快递单号'),
	(1, 'SQL_001', 1, 'param1', 'String', '参数1'),
	(2, 'SQL_001', 2, 'param2', 'String', '参数2'),
	(3, 'SQL_002', 1, 'param1', 'String', '参数2'),
	(4, 'SQL_002', 2, 'param2', 'String', '参数2');
/*!40000 ALTER TABLE `t_frame_data_interface_params` ENABLE KEYS */;


-- 导出  表 framework.t_frame_data_rest_config 结构
DROP TABLE IF EXISTS `t_frame_data_rest_config`;
CREATE TABLE IF NOT EXISTS `t_frame_data_rest_config` (
  `id` bigint(20) NOT NULL auto_increment,
  `data_code` varchar(20) collate utf8_bin NOT NULL COMMENT '数据编号',
  `uri` text collate utf8_bin COMMENT '数据访问地址',
  `method` char(10) collate utf8_bin default NULL COMMENT 'GET/POST',
  `must_login` varchar(20) collate utf8_bin default NULL,
  `login_bean` varchar(200) collate utf8_bin default NULL COMMENT '请继承IFrameDataInterfaceLoginService，返回登录后java.net.HttpURLConnection',
  PRIMARY KEY  (`data_code`),
  KEY `ak_kid` (`id`),
  CONSTRAINT `fk_reference_36` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='REST服务数据接口';

-- 正在导出表  framework.t_frame_data_rest_config 的数据: ~1 rows ((大约))
DELETE FROM `t_frame_data_rest_config`;
/*!40000 ALTER TABLE `t_frame_data_rest_config` DISABLE KEYS */;
INSERT INTO `t_frame_data_rest_config` (`id`, `data_code`, `uri`, `method`, `must_login`, `login_bean`) VALUES
	(1, 'REST_001', 'http://www.kuaidi100.com/autonumber/autoComNum?', 'GET', 'N', NULL);
/*!40000 ALTER TABLE `t_frame_data_rest_config` ENABLE KEYS */;


-- 导出  表 framework.t_frame_data_sp_config 结构
DROP TABLE IF EXISTS `t_frame_data_sp_config`;
CREATE TABLE IF NOT EXISTS `t_frame_data_sp_config` (
  `id` bigint(20) NOT NULL auto_increment,
  `data_code` varchar(20) collate utf8_bin NOT NULL COMMENT '数据编号',
  `sql_content` text collate utf8_bin COMMENT 'SQL内容',
  `data_source` varchar(20) collate utf8_bin default NULL COMMENT '数据源',
  `database_type` varchar(20) collate utf8_bin default NULL COMMENT 'MYSQL/ORACLE',
  `data_dao_name` varchar(200) collate utf8_bin default NULL COMMENT '数据执行DAO，可以不用指定，如果不指定，默认取 util.[数据库类型].[数据源名称].FrameDataInterfaceExecuteMapper',
  PRIMARY KEY  (`data_code`),
  KEY `ak_kid` (`id`),
  CONSTRAINT `fk_reference_33` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='存储过程数据接口表';

-- 正在导出表  framework.t_frame_data_sp_config 的数据: ~1 rows ((大约))
DELETE FROM `t_frame_data_sp_config`;
/*!40000 ALTER TABLE `t_frame_data_sp_config` DISABLE KEYS */;
INSERT INTO `t_frame_data_sp_config` (`id`, `data_code`, `sql_content`, `data_source`, `database_type`, `data_dao_name`) VALUES
	(1, 'SP_001', ' {call sp_test(#{userName,jdbcType=VARCHAR,mode=IN},#{page,jdbcType=INTEGER,mode=IN})}', 'framework', 'Mysql', NULL);
/*!40000 ALTER TABLE `t_frame_data_sp_config` ENABLE KEYS */;


-- 导出  表 framework.t_frame_data_sql_config 结构
DROP TABLE IF EXISTS `t_frame_data_sql_config`;
CREATE TABLE IF NOT EXISTS `t_frame_data_sql_config` (
  `id` bigint(20) NOT NULL auto_increment,
  `data_code` varchar(20) collate utf8_bin NOT NULL COMMENT '数据编号',
  `sql_content` text collate utf8_bin COMMENT 'SQL内容',
  `data_source` varchar(20) collate utf8_bin default NULL COMMENT '数据源',
  `database_type` varchar(20) collate utf8_bin default NULL COMMENT 'MYSQL/ORACLE',
  `data_dao_name` varchar(200) collate utf8_bin default NULL COMMENT '数据执行DAO，可以不用指定，如果不指定，默认取 util.[数据库类型].[数据源名称].FrameDataInterfaceExecuteMapper',
  PRIMARY KEY  (`data_code`),
  KEY `ak_kid` (`id`),
  CONSTRAINT `fk_reference_31` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  framework.t_frame_data_sql_config 的数据: ~2 rows ((大约))
DELETE FROM `t_frame_data_sql_config`;
/*!40000 ALTER TABLE `t_frame_data_sql_config` DISABLE KEYS */;
INSERT INTO `t_frame_data_sql_config` (`id`, `data_code`, `sql_content`, `data_source`, `database_type`, `data_dao_name`) VALUES
	(1, 'SQL_001', '	select * from t_frame_user where 0=0\r\n	<if test="userName!=null">\r\n	and ( user_Name = #{userName})\r\n	</if>\r\n	<if test="list!=null">\r\n	and user_Name in\r\n	<foreach collection="list" item="id" open="(" separator="," close=")">\r\n			#{id,jdbcType=VARCHAR}\r\n		</foreach>\r\n	</if>\r\n\r\nunion all\r\n	select * from t_frame_user where 0=0\r\n	<if test="userName!=null">\r\n	and  ( user_Name = #{userName})\r\n	</if>\r\n	<if test="list!=null">\r\n	and user_Name in\r\n	<foreach collection="list" item="id" open="(" separator="," close=")">\r\n			#{id,jdbcType=VARCHAR}\r\n		</foreach>\r\n	</if>\r\nunion all\r\n	select * from t_frame_user where 0=0\r\n	<if test="userName!=null">\r\n	and   ( user_Name = #{userName})\r\n	</if>\r\n	<if test="list!=null">\r\n	and user_Name in\r\n	<foreach collection="list" item="id" open="(" separator="," close=")">\r\n			#{id,jdbcType=VARCHAR}\r\n		</foreach>\r\n	</if>\r\norder by user_Name', 'framework', 'Mysql', NULL),
	(2, 'SQL_002', 'select * from t_frame_data_interface where (#{dataCode} is null or data_code = #{dataCode} )', NULL, 'Mysql', 'util.Mysql.framework.FrameDataInterfaceExecuteMapper');
/*!40000 ALTER TABLE `t_frame_data_sql_config` ENABLE KEYS */;


-- 导出  表 framework.t_frame_menu 结构
DROP TABLE IF EXISTS `t_frame_menu`;
CREATE TABLE IF NOT EXISTS `t_frame_menu` (
  `id` bigint(20) NOT NULL auto_increment,
  `menu_code` varchar(200) collate utf8_bin NOT NULL COMMENT '资源组code',
  `menu_code_name` varchar(200) collate utf8_bin NOT NULL COMMENT '资源组名称',
  `parent_menu_code` varchar(200) collate utf8_bin NOT NULL COMMENT '父菜单编码',
  `create_by` varchar(50) collate utf8_bin NOT NULL COMMENT '创建人',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_by` varchar(50) collate utf8_bin NOT NULL COMMENT '更新人',
  `update_date` datetime NOT NULL COMMENT '更新日期',
  PRIMARY KEY  (`menu_code`),
  UNIQUE KEY `ui_framework_menu_1` (`menu_code`),
  KEY `ak_kid` (`id`),
  KEY `ak_kid2` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统菜单';

-- 正在导出表  framework.t_frame_menu 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_menu`;
/*!40000 ALTER TABLE `t_frame_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_menu` ENABLE KEYS */;


-- 导出  表 framework.t_frame_menu_data 结构
DROP TABLE IF EXISTS `t_frame_menu_data`;
CREATE TABLE IF NOT EXISTS `t_frame_menu_data` (
  `id` bigint(20) NOT NULL auto_increment,
  `menu_code` varchar(200) collate utf8_bin NOT NULL COMMENT '资源组code',
  `data_code` varchar(20) collate utf8_bin NOT NULL COMMENT '数据编号',
  PRIMARY KEY  (`menu_code`,`data_code`),
  KEY `ak_kid` (`id`),
  KEY `ak_kid2` (`id`),
  KEY `fk_reference_35` (`data_code`),
  CONSTRAINT `fk_reference_34` FOREIGN KEY (`menu_code`) REFERENCES `t_frame_menu` (`menu_code`),
  CONSTRAINT `fk_reference_35` FOREIGN KEY (`data_code`) REFERENCES `t_frame_data_interface` (`data_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  framework.t_frame_menu_data 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_menu_data`;
/*!40000 ALTER TABLE `t_frame_menu_data` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_menu_data` ENABLE KEYS */;


-- 导出  表 framework.t_frame_menu_resource 结构
DROP TABLE IF EXISTS `t_frame_menu_resource`;
CREATE TABLE IF NOT EXISTS `t_frame_menu_resource` (
  `id` bigint(20) NOT NULL auto_increment,
  `menu_code` varchar(200) collate utf8_bin NOT NULL COMMENT '资源组code',
  `resource_code` varchar(200) collate utf8_bin NOT NULL COMMENT '资源编码',
  PRIMARY KEY  (`menu_code`,`resource_code`),
  KEY `ak_kid` (`id`),
  KEY `fk_reference_23` (`resource_code`),
  CONSTRAINT `fk_reference_22` FOREIGN KEY (`menu_code`) REFERENCES `t_frame_menu` (`menu_code`),
  CONSTRAINT `fk_reference_23` FOREIGN KEY (`resource_code`) REFERENCES `t_framework_resource` (`resource_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='菜单资源相关资源';

-- 正在导出表  framework.t_frame_menu_resource 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_menu_resource`;
/*!40000 ALTER TABLE `t_frame_menu_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_menu_resource` ENABLE KEYS */;


-- 导出  表 framework.t_frame_org 结构
DROP TABLE IF EXISTS `t_frame_org`;
CREATE TABLE IF NOT EXISTS `t_frame_org` (
  `id` bigint(20) NOT NULL auto_increment,
  `org_code` varchar(200) collate utf8_bin NOT NULL,
  `auth_type` varchar(20) collate utf8_bin default NULL COMMENT '组织机构',
  `org_name` varchar(200) collate utf8_bin default NULL,
  `org_type` varchar(20) collate utf8_bin NOT NULL,
  `parent_org_code` varchar(50) collate utf8_bin default NULL COMMENT '父结点CODE',
  `status` varchar(20) collate utf8_bin NOT NULL,
  `validator_start` datetime NOT NULL,
  `validator_end` datetime NOT NULL,
  `create_by` varchar(50) collate utf8_bin NOT NULL COMMENT '创建人',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_by` varchar(50) collate utf8_bin NOT NULL COMMENT '更新人',
  `update_date` datetime NOT NULL COMMENT '更新日期',
  PRIMARY KEY  (`org_code`),
  UNIQUE KEY `index_1` (`org_code`,`status`,`validator_start`,`validator_end`),
  KEY `ak_kid` (`id`),
  KEY `fk_reference_30` (`org_code`,`auth_type`),
  CONSTRAINT `fk_reference_30` FOREIGN KEY (`org_code`, `auth_type`) REFERENCES `t_frame_auth` (`auth_code`, `auth_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  framework.t_frame_org 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_org`;
/*!40000 ALTER TABLE `t_frame_org` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_org` ENABLE KEYS */;


-- 导出  表 framework.t_frame_role 结构
DROP TABLE IF EXISTS `t_frame_role`;
CREATE TABLE IF NOT EXISTS `t_frame_role` (
  `id` bigint(20) NOT NULL auto_increment,
  `role_code` varchar(200) collate utf8_bin NOT NULL,
  `auth_type` varchar(20) collate utf8_bin default NULL COMMENT '角色',
  `role_type` varchar(20) collate utf8_bin NOT NULL COMMENT '功能权限（10）/数据权限（20）/流程权限（30）',
  `role_name` varchar(200) collate utf8_bin default NULL,
  `create_by` varchar(50) collate utf8_bin default NULL COMMENT '创建人',
  `create_date` datetime default NULL COMMENT '创建日期',
  `update_by` varchar(50) collate utf8_bin default NULL COMMENT '更新人',
  `update_date` datetime default NULL COMMENT '更新日期',
  PRIMARY KEY  (`role_code`),
  KEY `ak_kid` (`id`),
  KEY `fk_reference_29` (`role_code`,`auth_type`),
  CONSTRAINT `fk_reference_29` FOREIGN KEY (`role_code`, `auth_type`) REFERENCES `t_frame_auth` (`auth_code`, `auth_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色';

-- 正在导出表  framework.t_frame_role 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_role`;
/*!40000 ALTER TABLE `t_frame_role` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_role` ENABLE KEYS */;


-- 导出  表 framework.t_frame_role_menu 结构
DROP TABLE IF EXISTS `t_frame_role_menu`;
CREATE TABLE IF NOT EXISTS `t_frame_role_menu` (
  `id` bigint(20) NOT NULL auto_increment,
  `role_code` varchar(200) collate utf8_bin NOT NULL,
  `menu_code` varchar(200) collate utf8_bin NOT NULL COMMENT '资源组code',
  PRIMARY KEY  (`role_code`,`menu_code`),
  KEY `ak_kid` (`id`),
  KEY `fk_reference_27` (`menu_code`),
  CONSTRAINT `fk_reference_24` FOREIGN KEY (`role_code`) REFERENCES `t_frame_role` (`role_code`),
  CONSTRAINT `fk_reference_27` FOREIGN KEY (`menu_code`) REFERENCES `t_frame_menu` (`menu_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色菜单';

-- 正在导出表  framework.t_frame_role_menu 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_role_menu`;
/*!40000 ALTER TABLE `t_frame_role_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_role_menu` ENABLE KEYS */;


-- 导出  表 framework.t_frame_role_menu_resource 结构
DROP TABLE IF EXISTS `t_frame_role_menu_resource`;
CREATE TABLE IF NOT EXISTS `t_frame_role_menu_resource` (
  `id` bigint(20) NOT NULL auto_increment,
  `role_code` varchar(200) collate utf8_bin default NULL,
  `menu_code` varchar(200) collate utf8_bin default NULL COMMENT '资源组code',
  `resource_code` varchar(200) collate utf8_bin default NULL COMMENT '资源编码',
  KEY `ak_kid` (`id`),
  KEY `fk_reference_25` (`role_code`,`menu_code`),
  KEY `fk_reference_26` (`menu_code`,`resource_code`),
  CONSTRAINT `fk_reference_25` FOREIGN KEY (`role_code`, `menu_code`) REFERENCES `t_frame_role_menu` (`role_code`, `menu_code`),
  CONSTRAINT `fk_reference_26` FOREIGN KEY (`menu_code`, `resource_code`) REFERENCES `t_frame_menu_resource` (`menu_code`, `resource_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='系统角色菜单资源';

-- 正在导出表  framework.t_frame_role_menu_resource 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_role_menu_resource`;
/*!40000 ALTER TABLE `t_frame_role_menu_resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_role_menu_resource` ENABLE KEYS */;


-- 导出  表 framework.t_frame_user 结构
DROP TABLE IF EXISTS `t_frame_user`;
CREATE TABLE IF NOT EXISTS `t_frame_user` (
  `id` bigint(20) NOT NULL auto_increment,
  `user_name` varchar(200) collate utf8_bin NOT NULL,
  `password` varchar(200) collate utf8_bin NOT NULL,
  `email` varchar(200) collate utf8_bin NOT NULL,
  `full_name` varchar(200) collate utf8_bin NOT NULL,
  `sex` varchar(20) collate utf8_bin NOT NULL,
  `create_by` varchar(50) collate utf8_bin default NULL COMMENT '创建人',
  `create_date` datetime default NULL COMMENT '创建日期',
  `update_by` varchar(50) collate utf8_bin default NULL COMMENT '更新人',
  `update_date` datetime default NULL COMMENT '更新日期',
  PRIMARY KEY  (`user_name`),
  UNIQUE KEY `inx_user_user_id` (`user_name`),
  KEY `ak_kid` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户表';

-- 正在导出表  framework.t_frame_user 的数据: ~6 rows ((大约))
DELETE FROM `t_frame_user`;
/*!40000 ALTER TABLE `t_frame_user` DISABLE KEYS */;
INSERT INTO `t_frame_user` (`id`, `user_name`, `password`, `email`, `full_name`, `sex`, `create_by`, `create_date`, `update_by`, `update_date`) VALUES
	(1, 'admin', 'E10ADC3949BA59ABBE56E057F20F883E', 'admin@qq.com', '管理员', 'F', 'admin', '2016-01-07 12:33:05', 'admin', '2016-01-07 12:33:05'),
	(2, 'admin1', 'E10ADC3949BA59ABBE56E057F20F883E', 'admin1@qq.com', 'admin1', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59'),
	(7, 'leader_老六', 'E10ADC3949BA59ABBE56E057F20F883E', 'lead_laoliu@html.com', 'leader_老六', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59'),
	(3, 'student_张三', 'E10ADC3949BA59ABBE56E057F20F883E', 'stu_zhangshan@html.com', 'student_张三', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59'),
	(5, 'student_李四', 'E10ADC3949BA59ABBE56E057F20F883E', 'stu_lisi@html.com', 'student_李四', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59'),
	(6, 'teacher_王五', 'E10ADC3949BA59ABBE56E057F20F883E', 'tech_wangwu@html.com', 'teacher_王五', 'F', 'admin', '2016-01-07 20:44:55', 'admin', '2016-01-07 20:44:59');
/*!40000 ALTER TABLE `t_frame_user` ENABLE KEYS */;


-- 导出  表 framework.t_frame_user_auth 结构
DROP TABLE IF EXISTS `t_frame_user_auth`;
CREATE TABLE IF NOT EXISTS `t_frame_user_auth` (
  `id` bigint(20) NOT NULL auto_increment,
  `login_id` varchar(200) collate utf8_bin NOT NULL,
  `auth_code` varchar(200) collate utf8_bin NOT NULL,
  `auth_type` varchar(20) collate utf8_bin NOT NULL COMMENT '组织机构/角色/流程结点/数据接口',
  `validator_start` datetime NOT NULL,
  `validator_end` datetime NOT NULL,
  `create_by` varchar(50) collate utf8_bin NOT NULL COMMENT '创建人',
  `create_date` datetime NOT NULL COMMENT '创建日期',
  `update_by` varchar(50) collate utf8_bin NOT NULL COMMENT '更新人',
  `update_date` datetime NOT NULL COMMENT '更新日期',
  PRIMARY KEY  (`login_id`,`auth_code`,`auth_type`),
  UNIQUE KEY `uidx_user_blong_type_userid` (`login_id`),
  KEY `ak_kid` (`id`),
  KEY `fk_reference_28` (`auth_code`,`auth_type`),
  CONSTRAINT `fk_reference_1` FOREIGN KEY (`login_id`) REFERENCES `t_frame_user` (`user_name`),
  CONSTRAINT `fk_reference_28` FOREIGN KEY (`auth_code`, `auth_type`) REFERENCES `t_frame_auth` (`auth_code`, `auth_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  framework.t_frame_user_auth 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_user_auth`;
/*!40000 ALTER TABLE `t_frame_user_auth` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_user_auth` ENABLE KEYS */;


-- 导出  表 framework.t_frame_user_ref 结构
DROP TABLE IF EXISTS `t_frame_user_ref`;
CREATE TABLE IF NOT EXISTS `t_frame_user_ref` (
  `id` bigint(20) NOT NULL auto_increment,
  `login_id` varchar(200) collate utf8_bin NOT NULL,
  `attribute_name` varchar(50) collate utf8_bin NOT NULL,
  `attribute_value` varchar(200) collate utf8_bin default NULL,
  `create_by` varchar(50) collate utf8_bin default NULL COMMENT '创建人',
  `create_date` datetime default NULL COMMENT '创建日期',
  `update_by` varchar(50) collate utf8_bin default NULL COMMENT '更新人',
  `update_date` datetime default NULL COMMENT '更新日期',
  PRIMARY KEY  (`login_id`),
  KEY `ak_kid` (`id`),
  CONSTRAINT `fk_reference_2` FOREIGN KEY (`login_id`) REFERENCES `t_frame_user` (`user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- 正在导出表  framework.t_frame_user_ref 的数据: ~0 rows ((大约))
DELETE FROM `t_frame_user_ref`;
/*!40000 ALTER TABLE `t_frame_user_ref` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_frame_user_ref` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
