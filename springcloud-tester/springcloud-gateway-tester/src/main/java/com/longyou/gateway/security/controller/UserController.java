package com.longyou.gateway.security.controller;

import org.cloud.utils.HttpServletUtil;
import org.cloud.utils.http.OKHttpClientUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/info")
public class UserController {
    @RequestMapping("/authentication")
    public UserDetails getAuthentication(Authentication authentication, ServerHttpResponse response) {
        if(authentication==null){
            return null;
        }

        return (UserDetails) authentication.getPrincipal();
    }

    @Value("${spring.security.password_salt:}")
    String salt;

    /**
     * 增加salt防止密码破解，由于md5加密是不可逆的
     *
     * @return
     */
    @RequestMapping("/salt")
    public String getSalt() {
        return salt;
    }

//    @RequestMapping("/logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response,Authentication authentication) {
//        if (authentication != null) {
//            new CookieClearingLogoutHandler("remember-me").logout(request, response, authentication);
//        }
//        return "welcome";
//    }
}
