-- 增加授权的权限点，不然会产生授权不了的问题
-- 1:增加用户
INSERT INTO `framework`.`t_frame_user`(`id`, `user_name`, `password`, `email`, `full_name`, `sex`, `create_by`, `create_date`, `update_by`, `update_date`, `status`, `default_role`, `user_type`, `user_regist_source`, `session_key`) 
VALUES (1, 'admin', '3e2345598780429e4758006e801f4b88', 'admin@qq.com', '管理员', 'F', 'admin', '2016-01-07 12:33:05', 'admin', '2016-01-07 12:33:05', 1, 'Admin', 'admin', 'background', NULL);
-- 2:增加角色
INSERT INTO `framework`.`t_frame_role`(`role_id`, `role_code`, `role_name`, `role_type`, `create_by`, `create_date`, `update_by`, `update_date`, `status`) 
VALUES (1, 'Admin', '管理员', 'default', 'admin', '2020-01-15 16:48:11', 'admin', '2020-03-15 13:44:13', 1);
-- 3:增加授权的操作权限
INSERT INTO `framework`.`t_framework_resource`(`resource_id`, `resource_code`, `resource_name`, `resource_path`, `method`, `belong_microservice`, `create_by`, `create_date`, `update_by`, `update_date`) VALUES (-1000, 'saveOrUpdateRoles', '操作权限授权', '/admin/role/', 'BYUSERPERMISSION', 'common-service', 'admin', '2020-03-16 10:47:52', 'admin', '2020-03-16 10:47:52');
-- 4：增加用户角色
INSERT INTO `framework`.`t_frame_user_role`(`user_role_id`, `user_id`, `role_id`, `validator_start`, `validator_end`, `create_by`, `create_date`, `update_by`, `update_date`) 
VALUES (1, 1, 1, '2020-01-16 11:25:12', '2020-01-31 11:25:16', 'admin', '2020-01-16 11:25:27', 'admin', '2020-01-16 11:25:33');
-- 5：增加角色操作权限
INSERT INTO `framework`.`t_frame_role_resource`( `role_id`, `resource_id`) VALUES ( 1, -1000);
-- 6：增加数据接口权限
INSERT INTO `framework`.`t_frame_role_data_interface`( `role_id`, `data_interface_id`, `belong_microservice`, `data_interface_name`) VALUES ( 1, '5e6c43e07ad92a77cfad8ba6', 'COMMONSERVICE', '查询需要授权的资源清单列表');

INSERT INTO `framework`.`t_frame_role_data_interface`( `role_id`, `data_interface_id`, `belong_microservice`, `data_interface_name`) VALUES ( 1, '5e6c509e7ad92a77cfad8de2', 'COMMONSERVICE', '查询当前角色操作权限列表');

INSERT INTO `framework`.`t_frame_role_data_interface`( `role_id`, `data_interface_id`, `belong_microservice`, `data_interface_name`) VALUES ( 1, '5e6d666dc6af767df8096e37', 'COMMONSERVICE', '查询需要授权的接口清单');
INSERT INTO `framework`.`t_frame_role_data_interface`( `role_id`, `data_interface_id`, `belong_microservice`, `data_interface_name`) VALUES ( 1, 'fca0c2236325f81017b12e4da4d32990', 'COMMONSERVICE', '查询系统角色列表');
INSERT INTO `framework`.`t_frame_role_data_interface`( `role_id`, `data_interface_id`, `belong_microservice`, `data_interface_name`) VALUES ( 1, '3efa00c2c4b06b73758fac9e95e9b6a9', 'COMMONSERVICE', '查询所有用户的接口');

