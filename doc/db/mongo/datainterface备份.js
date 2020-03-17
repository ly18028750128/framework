/*
 Navicat Premium Data Transfer

 Source Server         : 172.16.4.22
 Source Server Type    : MongoDB
 Source Server Version : 40013
 Source Host           : 172.16.4.22:27017
 Source Schema         : test

 Target Server Type    : MongoDB
 Target Server Version : 40000
 File Encoding         : 65001

 Date: 17/03/2020 17:08:44
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
    _id: ObjectId("5e69ec77543ee56a391f1fcb"),
    interfaceName: "动态SQL示例所有登录用户",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMONSERVICE",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-12T09:07:04.468Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "50d55445206d7a70022d932c98e2bd7d"
}, {
    _id: ObjectId("5e69ec93543ee56a391f1fe2"),
    interfaceName: "动态SQL仅查询当前用户",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\tand id= #{paramsCurrentUserId,jdbcType=BIGINT}\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMONSERVICE",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-12T09:05:42.503Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "23450f7c37ba735d498a0be8575baf2d"
}, {
    _id: ObjectId("5e69ecb0543ee56a391f1ff9"),
    interfaceName: "动态SQL无需授权示例",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMONSERVICE",
    authMethod: "NOAUTH",
    createdOrUpdateTime: ISODate("2020-03-12T09:09:58.275Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "da1664b5771bce00f4919c0b1942cdcf"
}, {
    _id: ObjectId("5e69ecc8543ee56a391f2010"),
    interfaceName: "动态SQL需要授权给用户的示例",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMONSERVICE",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-12T09:07:08.18Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "6191e5feb221dd9d0acb24ae2895fb80"
}, {
    _id: ObjectId("5e6f56df0c730000a10008d9"),
    interfaceName: "查询系统角色列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "<!--系统配置，切勿删除-->\nselect * from t_frame_role\n<where>\n<if test=\"roleId!=null\">\n\trole_id = #{roleId,jdbcType=INTEGER}\n</if>\n<if test=\"roleCode!=null\">\n\tand role_code = #{roleCode,jdbcType=INTEGER}\n</if>\n<if test=\"createBy!=null\">\n\tand create_by = #{createBy,jdbcType=VARCHAR}\n</if>\n<if test=\"createDate!=null\">\n\tand create_Date BETWEEN #{createDate.0,jdbcType=DATE} AND #{createDate.1,jdbcType=DATE}\n</if>\n<if test=\"updateBy!=null\">\n\tand update_by = #{updateBy,jdbcType=VARCHAR}\n</if>\n<if test=\"updateDate!=null\">\n\tand update_Date BETWEEN #{updateDate.0,jdbcType=DATE} AND #{updateDate.1,jdbcType=DATE}\n</if>\n<if test=\"status!=null\">\n\tand status= #{status,jdbcType=INTEGER}\n</if>\n<if test=\"roleName!=null\">\n\tand role_name like concat('%', #{roleName,jdbcType=VARCHAR},'%')\n</if>\n</where>",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-15T13:33:53.861Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "fca0c2236325f81017b12e4da4d32990",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
}, {
    _id: ObjectId("5e6f56df0c730000a10008da"),
    interfaceName: "查询需要授权的资源清单列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "SELECT\n\tresource_id,\n\tbelong_microservice,\n\tresource_path,\n\tresource_name \nFROM\n\tt_framework_resource \nWHERE\n\tmethod = 'BYUSERPERMISSION' \n<if test=\"keyWord!=null\">\nand ( belong_microservice like concat('%',#{keyWord,jdbcType=VARCHAR},\"%\") or resource_path like concat('%',#{keyWord,jdbcType=VARCHAR},\"%\") or resource_name like concat('%',#{keyWord,jdbcType=VARCHAR},\"%\") )\n</if>",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-15T12:35:29.41Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "ff4211d5f54026dc74e8de937a18b790",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
}, {
    _id: ObjectId("5e6f56df0c730000a10008db"),
    interfaceName: "查询当前角色操作权限列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "<!-- 请勿删除 -->\nSELECT\n\tfrr.role_resource_id,\n\tfrr.role_id,\n\tfrr.resource_id,\n\tfr.belong_microservice,\n\tfr.resource_path,\n\tfr.resource_name \nFROM\n\tt_framework_resource fr,t_frame_role_resource frr\nWHERE fr.resource_id = frr.resource_id \nand fr.method = 'BYUSERPERMISSION' \nand frr.role_id = #{roleId,jdbcType=INTEGER}\n\t",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-14T12:06:51.044Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "6a58842f2dab21009867e5d7782985ec",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
}, {
    _id: ObjectId("5e6f56df0c730000a10008dc"),
    interfaceName: "查询需要授权的接口清单",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "SELECT\n\tresource_id,\n\tbelong_microservice,\n\tresource_path,\n\tresource_name \nFROM\n\tt_framework_resource \nWHERE\n\tmethod = 'BYUSERPERMISSION' ",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-14T23:19:09.125Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "99deca6d1f7b31ceeeeaf99e5afa488d",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO"
}, {
    _id: ObjectId("5e6f56df0c730000a10008e2"),
    interfaceName: "查询当前角色的数据接口列表",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_role_data_interface where role_id = #{roleId,jdbcType=INTEGER}",
    microServiceName: "General",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-15T01:33:44.856Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "7d458ef7f373239fd213efa213a30091",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO"
}, {
    _id: ObjectId("5e703d0d1e209d1416226564"),
    interfaceName: "查询所有用户的接口",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user\n<where>\n<if test=\"userName!=null\">\nuser_name like concat('%',#{userName,jdbcType=VARCHAR},'%')\n</if>\n<if test=\"status!=null and status.size()>0\">\nand status in\n\t\t  <foreach item=\"statusItem\" collection=\"status\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{statusItem, jdbcType=INTEGER}\n\t\t </foreach>\n</if>\n</where>",
    microServiceName: "General",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-17T03:28:40.161Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "3efa00c2c4b06b73758fac9e95e9b6a9",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null
} ]);

// ----------------------------
// Function structure for LoggerLevelMapFun
// ----------------------------
db.system.js.deleteOne({
    _id: "LoggerLevelMapFun"
});
;
db.system.js.insert({
    _id: "LoggerLevelMapFun",
    value: Code("\r\nfunction() {\r\n    //    for (var idx = 0; idx < this.length; idx++) {\r\n    var key = {\r\n        level: this.level,\r\n        logger: this.logger\r\n    };\r\n\r\n    var value = {\r\n        count: 1,\r\n        createDate: this.createDate\r\n    }\r\n\r\n    emit(key, value);\r\n    //    }\r\n};\r\n")
})
;;


// ----------------------------
// Function structure for logerLevelRreduceFun
// ----------------------------
db.system.js.deleteOne({
    _id: "logerLevelRreduceFun"
});
;
db.system.js.insert({
    _id: "logerLevelRreduceFun",
    value: Code("function(keyLoggerAndLevel, countObjVals) {\r\n\r\n    var tempMaxDate = new Date(\"9999-01-01\");\r\n    var tempMinDate = new Date(\"1970-01-01\");\r\n\r\n    var reducedVal = {\r\n        count: 0,\r\n        maxCreateDate: null,\r\n        minCreateDate: null\r\n    };\r\n\r\n\r\n    for (var idx = 0; idx < countObjVals.length; idx++) {\r\n        reducedVal.count += countObjVals[idx].count;\r\n\r\n        if (countObjVals[idx].createDate > tempMinDate) {\r\n            tempMinDate = countObjVals[idx].createDate;\r\n        }\r\n\r\n        if (countObjVals[idx].createDate < tempMaxDate) {\r\n            tempMaxDate = countObjVals[idx].createDate;\r\n        }\r\n    }\r\n\r\n    reducedVal.minCreateDate = tempMinDate;\r\n    reducedVal.maxCreateDate = tempMinDate;\r\n\r\n    return reducedVal;\r\n}")
})
;;


// ----------------------------
// Function structure for loggerLevelFinalizeFun
// ----------------------------
db.system.js.deleteOne({
    _id: "loggerLevelFinalizeFun"
});
;
db.system.js.insert({
    _id: "loggerLevelFinalizeFun",
    value: Code("function(key, reducedVal) {\r\n\t if (reducedVal.createDate != null) {\r\n\t\t\t var newReduceValue={};\r\n\t\t\t newReduceValue.maxCreateDate = reducedVal.createDate;\r\n\t\t\t newReduceValue.minCreateDate = reducedVal.createDate;\r\n\t\t\t newReduceValue.count = reducedVal.count;\r\n\t\t\t return newReduceValue;\r\n\t }\r\n\t \r\n\t return reducedVal;\r\n\r\n};")
})
;;

