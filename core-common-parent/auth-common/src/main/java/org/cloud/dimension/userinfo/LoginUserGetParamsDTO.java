package org.cloud.dimension.userinfo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Map;
import lombok.ToString;

@Data
@ToString
public class LoginUserGetParamsDTO {

    private Long userId;
    private String userName;
    private String password;
    private String microServiceName;  // 微服务名称
    private String loginType; // 登录方式，默认（后台表单登录）,微信、支付宝、助记词
    private Integer microAppIndex;
    private String params = "{}";  //参数，如小程序的参数有,app-name,appid,app-password参数

    @JsonIgnore
    public Map<String, Object> getParamMap() {
        return JSON.parseObject(params);
    }
}
