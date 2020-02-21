/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2016/1/8 19:41:59                            */
/*==============================================================*/


drop table if exists t_frame_auth;

drop table if exists t_frame_data_rest_config;

drop table if exists t_frame_data_sp_config;

drop table if exists t_frame_data_sql_config;

drop table if exists t_frame_data_interface;

drop table if exists t_frame_data_interface_params;

drop index ui_framework_menu_1 on t_frame_menu;

drop table if exists t_frame_menu;

drop table if exists t_frame_menu_data;

drop table if exists t_frame_menu_resource;

drop index index_1 on t_frame_org;

drop table if exists t_frame_org;

drop table if exists t_frame_role;

drop table if exists t_frame_role_menu;

drop table if exists t_frame_role_menu_resource;

drop index inx_user_user_id on t_frame_user;

drop table if exists t_frame_user;

drop index uidx_user_blong_type_userid on t_frame_user_auth;

drop table if exists t_frame_user_auth;

drop table if exists t_frame_user_ref;

drop index ui_framework_resorce_code on t_framework_resource;

drop table if exists t_framework_resource;

/*==============================================================*/
/* Table: t_frame_auth                                          */
/*==============================================================*/
create table `t_frame_auth`
(
   `id` bigint not null
   auto_increment,
   `auth_code` varchar(200) not null,
   `auth_type` varchar(20) not null comment '组织机构/角色/流程结点',
   primary key (auth_code, auth_type),
   key ak_kid (id) 
);

/*==============================================================*/
/* Table: t_frame_data_rest_config                              */
/*==============================================================*/
create table `t_frame_data_rest_config`
(
   `id` bigint not null
   auto_increment,
   `data_code` varchar(20) not null comment '数据编号',
   `uri` text comment '数据访问地址',
   `method` char(10) comment 'GET/POST',
   `must_login` varchar(20),
   `login_bean` varchar(200) comment '请继承IFrameDataInterfaceLoginService，返回登录后java.net.HttpURLConnection',
   primary key (data_code),
   key ak_kid (id) 
);

alter table t_frame_data_rest_config comment 'REST服务数据接口';

/*==============================================================*/
/* Table: t_frame_data_sp_config                                */
/*==============================================================*/
create table `t_frame_data_sp_config`
(
   `id` bigint not null
   auto_increment,
   `data_code` varchar(20) not null comment '数据编号',
   `sql_content` text comment 'SQL内容',
   `data_source` varchar(20) comment '数据源',
   `database_type` varchar(20) comment 'MYSQL/ORACLE',
   `data_dao_name` varchar(200) comment '数据执行DAO，可以不用指定，如果不指定，默认取 util.[数据库类型].[数据源名称].FrameDataInterfaceExecuteMapper',
   primary key (data_code),
   key ak_kid (id) 
);

alter table t_frame_data_sp_config comment '存储过程数据接口表';

/*==============================================================*/
/* Table: t_frame_data_sql_config                               */
/*==============================================================*/
create table `t_frame_data_sql_config`
(
   `id` bigint not null
   auto_increment,
   `data_code` varchar(20) not null comment '数据编号',
   `sql_content` text comment 'SQL内容',
   `data_source` varchar(20) comment '数据源',
   `database_type` varchar(20) comment 'MYSQL/ORACLE',
   `data_dao_name` varchar(200) comment '数据执行DAO，可以不用指定，如果不指定，默认取 util.[数据库类型].[数据源名称].FrameDataInterfaceExecuteMapper',
   primary key (data_code),
   key ak_kid (id) 
);

/*==============================================================*/
/* Table: t_frame_data_interface                                */
/*==============================================================*/
create table `t_frame_data_interface`
(
   `id` bigint not null
   auto_increment,
   `data_code` varchar(20) not null comment '数据编号',
   `data_name` varchar(200) comment '数据名称',
   `data_type` varchar(20) comment '数据类型：SQL(SQL)/存储过程(SP)/REST/（OTHER）其它',
   `data_execute_name` varchar(200) comment '必须继承IFrameDataInterfaceService，
            并在应用中正确的注册，可以不填，
            默认为util.[接口类型].FrameDataInterfaceMainService
            如果需要指定那么该接口必须继承IFrameDataInterfaceService',
   `description` text comment '描述',
   `create_by` varchar(50) not null comment '创建人',
   `create_date` datetime not null comment '创建日期',
   `update_by` varchar(50) not null comment '更新人',
   `update_date` datetime not null comment '更新日期',
   primary key (data_code),
   key ak_kid (id) 
);

/*==============================================================*/
/* Table: t_frame_data_interface_params                         */
/*==============================================================*/
create table `t_frame_data_interface_params`
(
   `id` bigint not null
   auto_increment,
   `data_code` varchar(20) not null comment '数据编号',
   `seq` int not null,
   `param_name` varchar(20) not null,
   `param_type` varchar(20),
   `param_desc` varchar(200),
   primary key (data_code, seq, param_name),
   key ak_kid (id) 
);

/*==============================================================*/
/* Table: t_frame_menu                                          */
/*==============================================================*/
create table `t_frame_menu`
(
   `id` bigint not null
   auto_increment,
   `menu_code` varchar(200) not null comment '资源组code',
   `menu_code_name` varchar(200) not null comment '资源组名称',
   `parent_menu_code` varchar(200) not null comment '父菜单编码',
   `create_by` varchar(50) not null comment '创建人',
   `create_date` datetime not null comment '创建日期',
   `update_by` varchar(50) not null comment '更新人',
   `update_date` datetime not null comment '更新日期',
   primary key (menu_code),
   key ak_kid (id),
   key ak_kid2 (id) 
);

alter table t_frame_menu comment '系统菜单';

/*==============================================================*/
/* Index: ui_framework_menu_1                                   */
/*==============================================================*/
create unique index ui_framework_menu_1 on t_frame_menu
(
   menu_code
);

/*==============================================================*/
/* Table: t_frame_menu_data                                     */
/*==============================================================*/
create table `t_frame_menu_data`
(
   `id` bigint not null
   auto_increment,
   `menu_code` varchar(200) not null comment '资源组code',
   `data_code` varchar(20) not null comment '数据编号',
   primary key (menu_code, data_code),
   key ak_kid (id),
   key ak_kid2 (id) 
);

/*==============================================================*/
/* Table: t_frame_menu_resource                                 */
/*==============================================================*/
create table `t_frame_menu_resource`
(
   `id` bigint not null
   auto_increment,
   `menu_code` varchar(200) not null comment '资源组code',
   `resource_code` varchar(200) not null comment '资源编码',
   primary key (menu_code, resource_code),
   key ak_kid (id) 
);

alter table t_frame_menu_resource comment '菜单资源相关资源';

/*==============================================================*/
/* Table: t_frame_org                                           */
/*==============================================================*/
create table `t_frame_org`
(
   `id` bigint not null
   auto_increment,
   `org_code` varchar(200) not null,
   `auth_type` varchar(20) comment '组织机构',
   `org_name` varchar(200),
   `org_type` varchar(20) not null,
   `parent_org_code` varchar(50) comment '父结点CODE',
   `status` varchar(20) not null,
   `validator_start` datetime not null,
   `validator_end` datetime not null,
   `create_by` varchar(50) not null comment '创建人',
   `create_date` datetime not null comment '创建日期',
   `update_by` varchar(50) not null comment '更新人',
   `update_date` datetime not null comment '更新日期',
   primary key (org_code),
   key ak_kid (id) 
);

/*==============================================================*/
/* Index: index_1                                               */
/*==============================================================*/
create unique index index_1 on t_frame_org
(
   org_code,
   status,
   validator_start,
   validator_end
);

/*==============================================================*/
/* Table: t_frame_role                                          */
/*==============================================================*/
create table `t_frame_role`
(
   `id` bigint not null
   auto_increment,
   `role_code` varchar(200) not null,
   `auth_type` varchar(20) comment '角色',
   `role_type` varchar(20) not null comment '功能权限（10）/数据权限（20）/流程权限（30）',
   `role_name` varchar(200),
   `create_by` varchar(50) comment '创建人',
   `create_date` datetime comment '创建日期',
   `update_by` varchar(50) comment '更新人',
   `update_date` datetime comment '更新日期',
   primary key (role_code),
   key ak_kid (id) 
);

alter table t_frame_role comment '系统角色';

/*==============================================================*/
/* Table: t_frame_role_menu                                     */
/*==============================================================*/
create table `t_frame_role_menu`
(
   `id` bigint not null
   auto_increment,
   `role_code` varchar(200) not null,
   `menu_code` varchar(200) not null comment '资源组code',
   primary key (role_code, menu_code),
   key ak_kid (id) 
);

alter table t_frame_role_menu comment '系统角色菜单';

/*==============================================================*/
/* Table: t_frame_role_menu_resource                            */
/*==============================================================*/
create table `t_frame_role_menu_resource`
(
   `id` bigint not null
   auto_increment,
   `role_code` varchar(200),
   `menu_code` varchar(200) comment '资源组code',
   `resource_code` varchar(200) comment '资源编码',
   key ak_kid (id) 
);

alter table t_frame_role_menu_resource comment '系统角色菜单资源';

/*==============================================================*/
/* Table: t_frame_user                                          */
/*==============================================================*/
create table `t_frame_user`
(
   `id` bigint not null
   auto_increment,
   `user_name` varchar(200) not null,
   `password` varchar(200) not null,
   `email` varchar(200) not null,
   `full_name` varchar(200) not null,
   `sex` varchar(20) not null,
   `create_by` varchar(50) comment '创建人',
   `create_date` datetime comment '创建日期',
   `update_by` varchar(50) comment '更新人',
   `update_date` datetime comment '更新日期',
   primary key (user_name),
   key ak_kid (id) 
);

alter table t_frame_user comment '用户表';

/*==============================================================*/
/* Index: inx_user_user_id                                      */
/*==============================================================*/
create unique index inx_user_user_id on t_frame_user
(
   user_name
);

/*==============================================================*/
/* Table: t_frame_user_auth                                     */
/*==============================================================*/
create table `t_frame_user_auth`
(
   `id` bigint not null
   auto_increment,
   `login_id` varchar(200) not null,
   `auth_code` varchar(200) not null,
   `auth_type` varchar(20) not null comment '组织机构/角色/流程结点/数据接口',
   `validator_start` datetime not null,
   `validator_end` datetime not null,
   `create_by` varchar(50) not null comment '创建人',
   `create_date` datetime not null comment '创建日期',
   `update_by` varchar(50) not null comment '更新人',
   `update_date` datetime not null comment '更新日期',
   primary key (login_id, auth_code, auth_type),
   key ak_kid (id) 
);

/*==============================================================*/
/* Index: uidx_user_blong_type_userid                           */
/*==============================================================*/
create unique index uidx_user_blong_type_userid on t_frame_user_auth
(
   login_id
);

/*==============================================================*/
/* Table: t_frame_user_ref                                      */
/*==============================================================*/
create table `t_frame_user_ref`
(
   `id` bigint not null
   auto_increment,
   `login_id` varchar(200) not null,
   `attribute_name` varchar(50) not null,
   `attribute_value` varchar(200),
   `create_by` varchar(50) comment '创建人',
   `create_date` datetime comment '创建日期',
   `update_by` varchar(50) comment '更新人',
   `update_date` datetime comment '更新日期',
   primary key (login_id),
   key ak_kid (id) 
);

/*==============================================================*/
/* Table: t_framework_resource                                  */
/*==============================================================*/
create table `t_framework_resource`
(
   `id` bigint not null
   auto_increment,
   `resource_code` varchar(200) not null comment '资源编码',
   `resource_name` varchar(200) not null comment '资源名称',
   `uri` varchar(2000) comment '资源地址',
   `method` varchar(200) comment '访问方法：get,post',
   `create_by` varchar(50) comment '创建人',
   `create_date` datetime comment '创建日期',
   `update_by` varchar(50) comment '更新人',
   `update_date` datetime comment '更新日期',
   primary key (resource_code),
   key ak_kid (id) 
);

alter table t_framework_resource comment '系统资源，用来记录所有的系统的URI服务资源

可以定义参数类型并进行参数类型的检查';

/*==============================================================*/
/* Index: ui_framework_resorce_code                             */
/*==============================================================*/
create unique index ui_framework_resorce_code on t_framework_resource
(
   resource_code
);

alter table t_frame_data_rest_config add constraint fk_reference_36 foreign key (data_code)
      references t_frame_data_interface (data_code) on delete restrict on update restrict;

alter table t_frame_data_sp_config add constraint fk_reference_33 foreign key (data_code)
      references t_frame_data_interface (data_code) on delete restrict on update restrict;

alter table t_frame_data_sql_config add constraint fk_reference_31 foreign key (data_code)
      references t_frame_data_interface (data_code) on delete restrict on update restrict;

alter table t_frame_data_interface_params add constraint fk_reference_32 foreign key (data_code)
      references t_frame_data_interface (data_code) on delete restrict on update restrict;

alter table t_frame_menu_data add constraint fk_reference_34 foreign key (menu_code)
      references t_frame_menu (menu_code) on delete restrict on update restrict;

alter table t_frame_menu_data add constraint fk_reference_35 foreign key (data_code)
      references t_frame_data_interface (data_code) on delete restrict on update restrict;

alter table t_frame_menu_resource add constraint fk_reference_22 foreign key (menu_code)
      references t_frame_menu (menu_code) on delete restrict on update restrict;

alter table t_frame_menu_resource add constraint fk_reference_23 foreign key (resource_code)
      references t_framework_resource (resource_code) on delete restrict on update restrict;

alter table t_frame_org add constraint fk_reference_30 foreign key (org_code, auth_type)
      references t_frame_auth (auth_code, auth_type) on delete restrict on update restrict;

alter table t_frame_role add constraint fk_reference_29 foreign key (role_code, auth_type)
      references t_frame_auth (auth_code, auth_type) on delete restrict on update restrict;

alter table t_frame_role_menu add constraint fk_reference_24 foreign key (role_code)
      references t_frame_role (role_code) on delete restrict on update restrict;

alter table t_frame_role_menu add constraint fk_reference_27 foreign key (menu_code)
      references t_frame_menu (menu_code) on delete restrict on update restrict;

alter table t_frame_role_menu_resource add constraint fk_reference_25 foreign key (role_code, menu_code)
      references t_frame_role_menu (role_code, menu_code) on delete restrict on update restrict;

alter table t_frame_role_menu_resource add constraint fk_reference_26 foreign key (menu_code, resource_code)
      references t_frame_menu_resource (menu_code, resource_code) on delete restrict on update restrict;

alter table t_frame_user_auth add constraint fk_reference_1 foreign key (login_id)
      references t_frame_user (user_name) on delete restrict on update restrict;

alter table t_frame_user_auth add constraint fk_reference_28 foreign key (auth_code, auth_type)
      references t_frame_auth (auth_code, auth_type) on delete restrict on update restrict;

alter table t_frame_user_ref add constraint fk_reference_2 foreign key (login_id)
      references t_frame_user (user_name) on delete restrict on update restrict;

