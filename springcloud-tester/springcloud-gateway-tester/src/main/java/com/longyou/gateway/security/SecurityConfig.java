package com.longyou.gateway.security;


import com.longyou.gateway.security.handler.AuthenticationFaillHandler;
import com.longyou.gateway.security.handler.AuthenticationSuccessHandler;
import com.longyou.gateway.security.handler.CustomHttpBasicServerAuthenticationEntryPoint;
import com.longyou.gateway.security.handler.CustomServerLogoutSuccessHandler;
import com.longyou.gateway.util.MD5PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.List;

@EnableWebFluxSecurity
public class SecurityConfig {


    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private AuthenticationFaillHandler authenticationFaillHandler;
    @Autowired
    private CustomHttpBasicServerAuthenticationEntryPoint customHttpBasicServerAuthenticationEntryPoint;
    @Autowired
    private CustomServerLogoutSuccessHandler customServerLogoutSuccessHandler;


    //security的鉴权排除列表
    @Value("${spring.security.excludedAuthPages:}")
    private List<String> excludedAuthPages;

    @Autowired
    private DiscoveryClient discoveryClient;


    @Bean
    SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) throws Exception {
//        List<String> services = discoveryClient.getServices();
//        services.forEach((value)->{
//            excludedAuthPages.add("/"+value.toUpperCase()+"/**");
//        });
        http
                .authorizeExchange()
                .pathMatchers(new String[]{"/**"}).permitAll()  //这里只做登录和生成token,最终的强制登录校验由core里的SecurityFilter进行校验
                .pathMatchers(HttpMethod.OPTIONS).permitAll() //option 请求默认放行
                .anyExchange().authenticated()
                .and()
                .httpBasic()
                .and()
                .formLogin().loginPage("/auth/login")
                .authenticationSuccessHandler(authenticationSuccessHandler) //认证成功
                .authenticationFailureHandler(authenticationFaillHandler) //登陆验证失败
                .and().exceptionHandling().authenticationEntryPoint(customHttpBasicServerAuthenticationEntryPoint)  //基于http的接口请求鉴权失败
                .and().csrf().disable()//必须支持跨域
                .logout().logoutSuccessHandler(customServerLogoutSuccessHandler).logoutUrl("/auth/logout")
        ;

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new MD5PasswordEncoder(); //默认
    }


}
