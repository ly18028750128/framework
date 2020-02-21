/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.21-log : Database - framework
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`framework` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `framework`;

/*Data for the table `t_frame_auth` */

/*Data for the table `t_frame_data_interface` */

insert  into `t_frame_data_interface`(`id`,`data_code`,`data_name`,`data_type`,`data_execute_name`,`description`,`create_by`,`create_date`,`update_by`,`update_date`) values (4,'REST_001','快递单查询','REST',NULL,'快递100快递单查询服务','admin','2016-01-08 18:53:17','admin','2016-01-08 18:53:22'),(5,'SP_001','存储过程测试','SP',NULL,'存储过程测试','admin','2016-01-10 21:16:48','admin','2016-01-10 21:16:55'),(2,'SQL_001','SQL_001','SQL','',NULL,'admin','2016-01-07 18:58:22','admin','2016-01-07 18:58:28'),(3,'SQL_002','SQL_002','SQL','util.SQL.FrameDataInterfaceService',NULL,'admin','2016-01-07 18:59:33','admin','2016-01-07 18:59:40');

/*Data for the table `t_frame_data_interface_params` */

insert  into `t_frame_data_interface_params`(`id`,`data_code`,`seq`,`param_name`,`param_type`,`param_desc`) values (5,'REST_001',1,'text','String','快递单号'),(1,'SQL_001',1,'param1','String','参数1'),(2,'SQL_001',2,'param2','String','参数2'),(3,'SQL_002',1,'param1','String','参数2'),(4,'SQL_002',2,'param2','String','参数2');

/*Data for the table `t_frame_data_rest_config` */

insert  into `t_frame_data_rest_config`(`id`,`data_code`,`uri`,`method`,`must_login`,`login_bean`) values (1,'REST_001','http://www.kuaidi100.com/autonumber/autoComNum?','GET','N',NULL);

/*Data for the table `t_frame_data_sp_config` */

insert  into `t_frame_data_sp_config`(`id`,`data_code`,`sql_content`,`data_source`,`database_type`,`data_dao_name`) values (1,'SP_001',' {call sp_test(#{userName,jdbcType=VARCHAR,mode=IN},#{page,jdbcType=INTEGER,mode=IN})}','framework','Mysql',NULL);

/*Data for the table `t_frame_data_sql_config` */

insert  into `t_frame_data_sql_config`(`id`,`data_code`,`sql_content`,`data_source`,`database_type`,`data_dao_name`) values (1,'SQL_001','select * from t_frame_user where ( #{userName} is null or user_Name = #{userName})','framework','Mysql',NULL),(2,'SQL_002','select \'sql002\' as test',NULL,NULL,'util.Mysql.framework.FrameDataInterfaceExecuteMapper');

/*Data for the table `t_frame_menu` */

/*Data for the table `t_frame_menu_data` */

/*Data for the table `t_frame_menu_resource` */

/*Data for the table `t_frame_org` */

/*Data for the table `t_frame_role` */

/*Data for the table `t_frame_role_menu` */

/*Data for the table `t_frame_role_menu_resource` */

/*Data for the table `t_frame_user` */

insert  into `t_frame_user`(`id`,`user_name`,`password`,`email`,`full_name`,`sex`,`create_by`,`create_date`,`update_by`,`update_date`) values (1,'admin','E10ADC3949BA59ABBE56E057F20F883E','admin@qq.com','管理员','F','admin','2016-01-07 12:33:05','admin','2016-01-07 12:33:05'),(2,'admin1','E10ADC3949BA59ABBE56E057F20F883E','admin1@qq.com','admin1','F','admin','2016-01-07 20:44:55','admin','2016-01-07 20:44:59');

/*Data for the table `t_frame_user_auth` */

/*Data for the table `t_frame_user_ref` */

/*Data for the table `t_framework_resource` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
