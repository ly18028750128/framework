delete
from t_frame_role_resource
where t_frame_role_resource.resource_id in (
    select t_framework_resource.resource_id
    from t_framework_resource
    where belong_microservice not in
          ('common-service', 'spring-gateway')
);


delete
from t_framework_resource
where belong_microservice not in ('common-service', 'spring-gateway')