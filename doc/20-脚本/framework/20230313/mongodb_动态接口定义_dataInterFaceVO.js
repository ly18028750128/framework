/*
 Navicat Premium Data Transfer

 Source Server         : 309-192.168.30.239
 Source Server Type    : MongoDB
 Source Server Version : 50010 (5.0.10)
 Source Host           : 192.168.30.239:10003
 Source Schema         : YS-LONGYOU-framework

 Target Server Type    : MongoDB
 Target Server Version : 50010 (5.0.10)
 File Encoding         : 65001

 Date: 13/03/2023 14:38:41
*/


// ----------------------------
// Collection structure for dataInterFaceVO
// ----------------------------
db.getCollection("dataInterFaceVO").drop();
db.createCollection("dataInterFaceVO");
db.getCollection("dataInterFaceVO").createIndex({
    md5: NumberInt("1")
}, {
    name: "md5_1",
    unique: true
});
db.getCollection("dataInterFaceVO").createIndex({
    interfaceName: NumberInt("1")
}, {
    name: "interfaceName_1",
    unique: true
});

// ----------------------------
// Documents of dataInterFaceVO
// ----------------------------
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5f3e7732dd66d229d7fbe2e5"),
    interfaceName: "查询钱包用户信息",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_user_info \n<where>\npublic_Key = #{publicKey,jdbcType=VARCHAR}\n</where>",
    microServiceName: "UNKNOW-ACCOUNT-SERVICE",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-08-20T13:38:13.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    md5: "dc14b9097ea7bf9eaee08b19a793ccf2",
    restMethod: null
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e914a9f9af349432bc3486e"),
    interfaceName: "查询图层要素的属性",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select \"属性1\",\"属性2\",\"属性3\",\"属性4\",\"属性5\",\"属性6\",\"属性7\",\"属性8\",\"属性9\",\"属性10\",\"属性11\",\"属性12\",\"更新人\",\"更新日期\" from \"${tableName}\" where id=#{id,jdbcType=INTEGER} ",
    microServiceName: "HEYIGIS",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-04-23T13:39:19.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "4365a6e6cf1da3ab0215eb9fb15f7249",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e71b5ec561cd27d329b5d1f"),
    interfaceName: "按用户ID查询用户的角色列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "<!--查询当前用户的角色列表，请勿删除，系统用-->\nSELECT\n\tUR.role_id,\n\tR.role_code,\n\tR.role_name,\n\tUR.validator_start,\n\tUR.validator_end \nFROM\n\tt_frame_user_role ur,\n\tt_frame_role r \nWHERE\n\tur.role_id = r.role_id \n\tAND ur.user_id = #{userId,jdbcType=BIGINT}",
    microServiceName: "General",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-18T08:51:48.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "fbf2e7c0ec6c91d9041ccd92509ce309",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e8fe5c789118808da439e7c"),
    interfaceName: "查找当前用户的项目",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select id,create_date, area, \"style\",st_astext(geom) as wkt,st_astext(ST_Centroid(geom)) as center_pointer,project_name from t_user_project where create_by= #{paramsCurrentUserId,jdbcType=BIGINT}",
    microServiceName: "HEYIGIS",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-04-10T03:29:49.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "4c9d5f7c992dfe2a999d749476fa604f",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e6f56df0c730000a10008e2"),
    interfaceName: "查询当前角色的数据接口列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_role_data_interface where role_id = #{roleId,jdbcType=INTEGER}",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-15T01:33:44.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "7d458ef7f373239fd213efa213a30091",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO"
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e703d0d1e209d1416226564"),
    interfaceName: "查询所有用户的接口",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user  \n<where>\n1=1\n<if test=\"userName!=null and userName != '' \">\nand user_name like concat('%',#{userName,jdbcType=VARCHAR},'%')\n</if>\n<if test=\"roleId!=null and roleId != ''\">\nand EXISTS (SELECT user_id from t_frame_user_role where role_id = #{roleId, jdbcType=INTEGER} and t_frame_user.id = t_frame_user_role.user_id )\n</if>\n<if test=\"status!=null and status.size()>0\">\nand status in\n\t\t  <foreach item=\"statusItem\" collection=\"status\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{statusItem, jdbcType=INTEGER}\n\t\t </foreach>\n</if>\n</where>",
    microServiceName: "General",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2021-12-28T07:02:01.923Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "3efa00c2c4b06b73758fac9e95e9b6a9",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e6f56df0c730000a10008db"),
    interfaceName: "查询当前角色操作权限列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "<!-- 请勿删除 -->\nSELECT\n\tfrr.role_resource_id,\n\tfrr.role_id,\n\tfrr.resource_id,\n\tfr.belong_microservice,\n\tfr.resource_path,\n\tfr.resource_name \nFROM\n\tt_framework_resource fr,t_frame_role_resource frr\nWHERE fr.resource_id = frr.resource_id \nand fr.method = 'BYUSERPERMISSION' \nand frr.role_id = #{roleId,jdbcType=INTEGER}\n\t",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2021-12-28T07:25:21.452Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "6a58842f2dab21009867e5d7782985ec",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e6f56df0c730000a10008dc"),
    interfaceName: "查询需要授权的接口清单",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "SELECT\n\tresource_id,\n\tbelong_microservice,\n\tresource_path,\n\tresource_name \nFROM\n\tt_framework_resource \nWHERE\n\tmethod = 'BYUSERPERMISSION' ",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-14T23:19:09.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "99deca6d1f7b31ceeeeaf99e5afa488d",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO"
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e6f56df0c730000a10008d9"),
    interfaceName: "查询系统角色列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "<!--系统配置，切勿删除-->\nselect * from t_frame_role\n<where>\n<if test=\"roleId!=null\">\n\trole_id = #{roleId,jdbcType=INTEGER}\n</if>\n<if test=\"roleCode!=null\">\n\tand role_code like concat('%', #{roleCode,jdbcType=VARCHAR},'%')\n</if>\n<if test=\"createBy!=null\">\n\tand create_by = #{createBy,jdbcType=VARCHAR}\n</if>\n<if test=\"createDate!=null\">\n\tand create_Date BETWEEN #{createDate.0,jdbcType=DATE} AND #{createDate.1,jdbcType=DATE}\n</if>\n<if test=\"updateBy!=null\">\n\tand update_by = #{updateBy,jdbcType=VARCHAR}\n</if>\n<if test=\"updateDate!=null\">\n\tand update_Date BETWEEN #{updateDate.0,jdbcType=DATE} AND #{updateDate.1,jdbcType=DATE}\n</if>\n<if test=\"status!=null\">\n\tand status= #{status,jdbcType=INTEGER}\n</if>\n<if test=\"roleName!=null\">\n\tand role_name like concat('%', #{roleName,jdbcType=VARCHAR},'%')\n</if>\n<if test=\"keyWord!=null\">\n\tor role_name like concat('%', #{keyWord,jdbcType=VARCHAR},'%') or role_code like concat('%', #{keyWord,jdbcType=VARCHAR},'%')\n</if>\n</where>",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-18T05:12:56.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "fca0c2236325f81017b12e4da4d32990",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e6f56df0c730000a10008da"),
    interfaceName: "查询需要授权的资源清单列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "SELECT\n\tresource_id,\n\tbelong_microservice,\n\tresource_path,\n\tresource_name \nFROM\n\tt_framework_resource \nWHERE\n\tmethod = 'BYUSERPERMISSION' \nand not exists(select 1 from t_frame_role_resource frr where frr.resource_id=t_framework_resource.resource_id and frr.role_id=#{roleId,jdbcType=VARCHAR})\n<if test=\"keyWord!=null\">\nand ( belong_microservice like concat('%',#{keyWord,jdbcType=VARCHAR},\"%\") or resource_path like concat('%',#{keyWord,jdbcType=VARCHAR},\"%\") or resource_name like concat('%',#{keyWord,jdbcType=VARCHAR},\"%\") )\n</if>",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-08-25T13:58:35.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "ff4211d5f54026dc74e8de937a18b790",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e69ec77543ee56a391f1fcb"),
    interfaceName: "动态SQL示例所有登录用户",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMON-SERVICE",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-12T09:07:04.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "50d55445206d7a70022d932c98e2bd7d"
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e69ec93543ee56a391f1fe2"),
    interfaceName: "动态SQL仅查询当前用户",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\tand id= #{paramsCurrentUserId,jdbcType=BIGINT}\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMON-SERVICE",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-06-08T05:08:08.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "23450f7c37ba735d498a0be8575baf2d"
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e69ecb0543ee56a391f1ff9"),
    interfaceName: "动态SQL无需授权示例",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMON-SERVICE",
    authMethod: "NOAUTH",
    createdOrUpdateTime: ISODate("2020-03-12T09:09:58.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "da1664b5771bce00f4919c0b1942cdcf"
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("5e69ecc8543ee56a391f2010"),
    interfaceName: "动态SQL需要授权给用户的示例",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMON-SERVICE",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-12T09:07:08.000Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "6191e5feb221dd9d0acb24ae2895fb80"
} ]);
db.getCollection("dataInterFaceVO").insert([ {
    _id: ObjectId("63326f84e4b2246525477065"),
    interfaceName: "查询操作权限列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "SELECT t_framework_resource.*,CONCAT(belong_microservice,'::',resource_path,'::',resource_code) resource_full_path FROM `t_framework_resource`  \n <where>\n<if test=\"belongMicroservice!=null\">\n belong_microservice like  CONCAT('%',#{belongMicroservice,jdbcType=VARCHAR},'%') \n </if>\n <if test=\"resourceName!=null\">\nand resource_name like  CONCAT('%',#{resourceName,jdbcType=VARCHAR},'%') \n </if>\n <if test=\"resourcePath!=null\">\nand resource_path like  CONCAT('%',#{resourcePath,jdbcType=VARCHAR},'%') \n </if>\n <if test=\"resourceCode!=null\">\nand resource_code like  CONCAT('%',#{resourceCode,jdbcType=VARCHAR},'%') \n </if>\n <if test=\"method!=null\">\nand method=#{method,jdbcType=VARCHAR}\n </if>\n</where>",
    microServiceName: "General",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2022-09-27T06:45:20.113Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    md5: "f8c3d11952196739e55f80f472a52693",
    restMethod: null
} ]);
