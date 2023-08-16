-- 由于目前的备份是用three-body-service作为模板的，执行完备份后执行这个脚本，把新的服务名改成新的服务名
set @old_account_services = 'three-body-service';
set @new_account_services = '新的account服务名';

update t_framework_resource 
set t_framework_resource.belong_microservice = @new_account_services
where t_framework_resource.belong_microservice = @old_account_services
;

update t_frame_menu
set t_frame_menu.function_resource_code = REPLACE(function_resource_code,@old_account_services,@new_account_services) 
where  function_resource_code like CONCAT(@old_account_services,'::%')
;