package com.longyou.gateway.util;

import com.longyou.gateway.filter.CrosWebFilter;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.MD5Encoder;
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

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        String sRawPassword = (String) rawPassword;
        if (!encodedPassword.equals(MD5Encoder.encode(sRawPassword, salt))) {   //如果用明文加盐密码处理不了，那么用basic 反向加密处理
            final long timeSaltChangeInterval = Long.parseLong(CommonUtil.single().getEnv("system.auth_basic_expire_time", Long.toString(5 * 60 * 1000)));
            final String[] values = sRawPassword.split("::");
            if(values.length<2){
                return false;
            }
            final long timeSalt = Long.parseLong(values[1]);
            if(System.currentTimeMillis() - timeSalt>timeSaltChangeInterval){   //如果超出时间，那么返回false;
                return false;
            }
            final String password = values[0];
            return password.equals(MD5Encoder.encode(encodedPassword, Long.toString(timeSalt)));
        }
        return true;
    }
}
