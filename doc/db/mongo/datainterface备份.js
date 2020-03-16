db.getCollection("dataInterFaceVO").insert( {
    interfaceName: "动态SQL示例所有登录用户",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMONSERVICE",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-13T04:44:51.336Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "50d55445206d7a70022d932c98e2bd7d"
} );

db.getCollection("dataInterFaceVO").insert( {
    interfaceName: "动态SQL仅查询当前用户",
    interfaceType: "sql",
    params: [
        {
            seq: NumberInt("0"),
            fieldName: "121",
            fieldType: "String",
            description: "1231312",
            status: NumberInt("1")
        },
        {
            seq: NumberInt("5"),
            fieldName: "12331",
            fieldType: "String",
            description: "12312",
            status: NumberInt("1")
        },
        {
            seq: NumberInt("15"),
            fieldName: "12321",
            fieldType: "String",
            description: "123",
            status: NumberInt("1")
        }
    ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\tand id= #{paramsCurrentUserId,jdbcType=BIGINT}\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMONSERVICE",
    authMethod: "ALLSYSTEMUSER",
    createdOrUpdateTime: ISODate("2020-03-13T07:31:39.017Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "23450f7c37ba735d498a0be8575baf2d"
} );

db.getCollection("dataInterFaceVO").insert( {
    interfaceName: "动态SQL无需授权示例",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMONSERVICE",
    authMethod: "NOAUTH",
    createdOrUpdateTime: ISODate("2020-03-14T02:25:50.794Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "da1664b5771bce00f4919c0b1942cdcf"
} );

db.getCollection("dataInterFaceVO").insert( {
    interfaceName: "动态SQL需要授权给用户的示例",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "select * from t_frame_user \n<where> \n\t<if test=\"userName!=null\">\n\t\tuser_name = #{userName,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"password!=null\">\n\t   and\tpassword = #{password,jdbcType=VARCHAR}\n\t</if>\n\t<if test=\"sexList!=null\">\n\t and sex in \n\t\t  <foreach item=\"item\" collection=\"sexList\" separator=\",\" open=\"(\" close=\")\" index=\"\">\n\t\t   #{item, jdbcType=VARCHAR}\n\t\t </foreach>\n\t</if>\n</where>",
    microServiceName: "COMMONSERVICE",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-13T04:41:30.731Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO",
    restMethod: null,
    md5: "6191e5feb221dd9d0acb24ae2895fb80"
} );

db.getCollection("dataInterFaceVO").insert( {
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
} );

db.getCollection("dataInterFaceVO").insert( {
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
} );

db.getCollection("dataInterFaceVO").insert( {
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
} );

db.getCollection("dataInterFaceVO").insert( {
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
} );

db.getCollection("dataInterFaceVO").insert( {
    interfaceName: "授权测试1",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "授权测试11213321123",
    microServiceName: "General",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-15T00:26:18.741Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "93714227ad780c1f8c5796943ce3a75c",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO"
} );

db.getCollection("dataInterFaceVO").insert( {
    interfaceName: "授权测试2",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "授权测试2授权测试2授权测试2授权测试2授权测试2授权测试2授权测试2授权测试2授权测试2授权测试2授权测试2授权测试2授权测试2",
    microServiceName: "General",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-15T00:31:00.139Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "5786b12d67c9c42e283d11ccbe0664d7",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO"
} );

db.getCollection("dataInterFaceVO").insert( {
    interfaceName: "授权测试3",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "授权测试3授权测试3授权测试3授权测试3授权测试3授权测试3授权测试3授权测试3授权测试3授权测试3授权测试3",
    microServiceName: "General",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-15T00:31:14.722Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "12c8b95e09760e76a3a81777417cce05",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO"
} );

db.getCollection("dataInterFaceVO").insert( {
    interfaceName: "interfaceName11213",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "interfaceName11111111111111",
    microServiceName: "General",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-15T00:46:17.724Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "fa11bc092628d58f8b2c3fdd91c4b7e6",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO"
} );

db.getCollection("dataInterFaceVO").insert( {
    interfaceName: "interfaceName12312",
    interfaceType: "sql",
    params: [ ],
    urlOrSqlContent: "interfaceNameinterfaceNameinterfaceNameinterfaceName",
    microServiceName: "General",
    authMethod: "BYUSERPERMISSION",
    createdOrUpdateTime: ISODate("2020-03-15T00:46:32.615Z"),
    createdOrUpdateBy: NumberLong("1"),
    createdOrUpdateUserName: "admin",
    md5: "9afe7bafb7416ccfc17071b1e4514359",
    status: NumberInt("1"),
    _class: "org.cloud.mongo.DataInterFaceVO"
} );

db.getCollection("dataInterFaceVO").insert( {
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
} );