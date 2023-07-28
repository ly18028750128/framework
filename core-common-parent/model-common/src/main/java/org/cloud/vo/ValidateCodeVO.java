package org.cloud.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * 验证码VO
 */
@Data
public class ValidateCodeVO implements Serializable {
    private static final long serialVersionUID = -8847813271787345354L;
    private String redisKey;
    private String base64ImageUrl;        //Base64 值
    @JsonIgnore
    private String value;            //验证码值
    private Integer verifyType; // 校验类型
    private Long userId; // 用户id，当用户登录后进行相关的校验可用

}
