package org.cloud.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.cloud.constant.CoreConstant;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 数据接口定义，
 */
@Data
public class DataInterFaceVO {
    private String _id;           // objectId
    private String interfaceName; //接口名称
    private String interfaceType; //接口类型   sql,存储过程,REST服务
    private String restMethod;    //rest请示方式，仅在rest请求时有效，rest仅调用一些不用授权登录的URL，降低复杂度
    private Set<DataInterFaceParamVO> params = new LinkedHashSet<>();    //参数列表
    private String urlOrSqlContent;// url或sql的内容
    private String microServiceName;  //微服务的名称
    private CoreConstant.AuthMethod authMethod;    //和CoreConstant.AuthMethod保持一致
    private Date createdOrUpdateTime;    //和CoreConstant.AuthMethod保持一致
    private Long createdOrUpdateBy;
    private String createdOrUpdateUserName;
    private int status = 1;  //状态，0失效，1有效

    public void addParam(String fieldName, MongoEnumVO.DataType fieldType, String description) {
        params.add(new DataInterFaceParamVO(fieldName, fieldType, description, 1));
    }

    public void addParam(String fieldName, MongoEnumVO.DataType fieldType, String description, int status) {
        params.add(new DataInterFaceParamVO(fieldName, fieldType, description, status));
    }
}
