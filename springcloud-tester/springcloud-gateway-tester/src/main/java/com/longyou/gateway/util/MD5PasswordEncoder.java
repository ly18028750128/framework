package com.longyou.gateway.util;

import com.longyou.gateway.filter.CrosWebFilter;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.server.ServerWebExchange;

public class MD5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return MD5Encoder.encode((String) charSequence);
    }

    @Value("${spring.security.password_salt:}")
    String salt;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String sRawPassword = (String) rawPassword;
        final String basic64SplitStr = CommonUtil.single().getEnv("system.auth_basic64_split", CoreConstant._USER_BASIC64_SPLIT_STR);
        final String[] values = sRawPassword.split(basic64SplitStr);
        if (values.length == 1) {
            return encodedPassword.equals(MD5Encoder.encode(sRawPassword, salt));
        } else if (values.length == 2) {
            final String userBasic64RandomKey = values[1];
            String salt = (String)redisUtil.get(CoreConstant._REDIS_USER_SUCCESS_TOKEN_PREFIX+userBasic64RandomKey);
            final String password = values[0];
            return password.equals(MD5Encoder.encode(encodedPassword, salt));
        }
        return false;
    }
}
