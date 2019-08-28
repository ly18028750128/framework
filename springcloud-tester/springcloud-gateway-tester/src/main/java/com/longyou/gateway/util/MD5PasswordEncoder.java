package com.longyou.gateway.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5PasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        return MD5Encoder.encode((String) charSequence);
    }

    @Value("${spring.security.password_salt:}")
    String salt;

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(MD5Encoder.encode((String) rawPassword,salt));
    }
}
