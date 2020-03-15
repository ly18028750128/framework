package org.cloud.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.cloud.constant.CoreConstant;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.MD5Encoder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据接口定义，
 */
@Data
@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class DataInterFaceVO {
    private String _id;           // objectId
    private String interfaceName; //接口名称
    private String interfaceType; //接口类型   sql,存储过程,REST服务
    private String restMethod;    //rest请示方式，仅在rest请求时有效，rest仅调用一些不用授权登录的URL，降低复杂度
    private Collection<DataInterFaceParamVO> params = new TreeSet<>();    //参数列表
    private String urlOrSqlContent;// url或sql的内容
    private String microServiceName;  //微服务的名称
    private CoreConstant.AuthMethod authMethod;    //和CoreConstant.AuthMethod保持一致
    private Date createdOrUpdateTime;    //和CoreConstant.AuthMethod保持一致
    private Long createdOrUpdateBy;
    private String createdOrUpdateUserName;
    private String md5;
    private int status = 1;  //状态，0失效，1有效

    public void addParam(String fieldName, MongoEnumVO.DataType fieldType, String description) {
        params.add(new DataInterFaceParamVO(fieldName, fieldType, description, 1));
    }

    public void addParam(String fieldName, MongoEnumVO.DataType fieldType, String description, int status) {
        params.add(new DataInterFaceParamVO(fieldName, fieldType, description, status));
    }

    public String getMd5() {
        if (this.md5 == null) {
            this.md5 = MD5Encoder.encode(interfaceName);
        }
        return md5;
    }

    public void setMd5(String md5) {
        if (md5 == null) {
            this.md5 = MD5Encoder.encode(interfaceName);
        } else {
            this.md5 = md5;
        }
    }

    public void setParams(Collection<DataInterFaceParamVO> params) {
        if (CollectionUtil.single().isEmpty(params)) {
            this.params = params;
            return;
        }
        // 去重
        this.params = params.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(DataInterFaceParamVO::getFieldName)))
                        , ArrayList::new
                ));
    }

    public Collection<DataInterFaceParamVO> getParams() {
        if (CollectionUtil.single().isEmpty(this.params)) {
            return this.params;
        }
        return this.params.stream().sorted(Comparator.comparing(DataInterFaceParamVO::getSeq)).collect(Collectors.toList());
    }
}
