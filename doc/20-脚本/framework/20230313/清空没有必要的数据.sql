-- 清空邮件配置
TRUNCATE table t_email_sender_config;
DELETE from t_email_template where template_id not in(1,2);

-- 清空文章和图片
TRUNCATE table t_article;
TRUNCATE table t_photo;

-- 清空导入导出
TRUNCATE table t_frame_export_template;
TRUNCATE table t_frame_import_export_task;

-- 清空数据权限
TRUNCATE table t_frame_data_dimension;
TRUNCATE table t_frame_role_data;

-- SELECT * FROM `t_framework_resource` where belong_microservice in('spring-gateway','common-service','chain-block-scan-service');
-- SELECT DISTINCT belong_microservice FROM `t_framework_resource`
-- 清空没有必要的权限点
DELETE FROM `t_framework_resource` where belong_microservice not in('spring-gateway','common-service','chain-block-scan-service');
DELETE from t_frame_role_resource where role_id!=1;
DELETE from t_frame_role_resource where resource_id not in(select resource_id from `t_framework_resource`);

-- 清空接口权限
DELETE from t_frame_role_data_interface where role_id<>1;

-- 清空没必要的角色
DELETE from t_frame_user where id<>1;
DELETE from t_frame_user_role where user_id<>1;
TRUNCATE table t_frame_user_ref;

-- 清空没有必要的数据字典
-- select * from t_system_dic_master where t_system_dic_master.belong_micro_service = ''

SELECT DISTINCT t_system_dic_master.belong_micro_service FROM `t_system_dic_master`

-- 清空微服务表
DELETE from t_microservice_register where t_microservice_register.microservice_name not in('spring-gateway','common-service','chain-block-scan-service');
