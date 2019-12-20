package com.longyou.gateway.security;

import com.alibaba.fastjson.JSON;
import com.longyou.gateway.filter.CrosWebFilter;
import org.apache.commons.lang.StringUtils;
import org.cloud.constant.CoreConstant;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.RestTemplateUtil;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

@Component
public class SecurityUserDetailsService implements ReactiveUserDetailsService {

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        final LoginUserGetParamsDTO loginUserGetParamsDTO=new LoginUserGetParamsDTO();
        UserDetails userDetails = null;
        ServerHttpRequest request = CrosWebFilter.serverWebExchangeThreadLocal.get().getRequest();
        final String microAppindex = request.getHeaders().getFirst(CoreConstant._MICRO_APPINDEX_KEY);
        final String userType =request.getHeaders().getFirst(CoreConstant._USER_TYPE_KEY);

        if(userType!=null){
            loginUserGetParamsDTO.getParams().put(CoreConstant._USER_TYPE_KEY,userType);
        }else{
            loginUserGetParamsDTO.getParams().remove(CoreConstant._USER_TYPE_KEY);
        }

        if (microAppindex != null) {
            Mono<MultiValueMap<String, String>> formdata = CrosWebFilter.serverWebExchangeThreadLocal.get().getFormData();
            final String weixinLoginCode =formdata.block().getFirst("password");
            loginUserGetParamsDTO.setUserName(weixinLoginCode);
            loginUserGetParamsDTO.getParams().put(CoreConstant._MICRO_LOGIN_CODE_KEY,weixinLoginCode);
            loginUserGetParamsDTO.setMicroAppIndex(Integer.parseInt(microAppindex));
            userDetails = getUserByName(loginUserGetParamsDTO);
            username = userDetails.getUsername();
        } else {
            loginUserGetParamsDTO.setUserName(username);
            userDetails = getUserByName(loginUserGetParamsDTO);
        }
        if (userDetails != null && StringUtils.equals(userDetails.getUsername(), username)) {
            UserDetails user = User.withUserDetails(userDetails).build();
            user = userDetails;
            return Mono.just(user);
        } else {
            return Mono.error(new UsernameNotFoundException("User Not Found"));
        }
    }

    @Value("${system.userinfo.query_user_url:http://COMMON-SERVICE/userinfo/getUserByName}")
    private String userinfoUrl;

    private LoginUserDetails getUserByName(LoginUserGetParamsDTO loginUserGetParamsDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ResponseEntity<LoginUserDetails> responseEntity = RestTemplateUtil.single().getResponse(userinfoUrl, HttpMethod.POST, JSON.toJSONString(loginUserGetParamsDTO),headers, LoginUserDetails.class);
        return responseEntity.getBody();
    }





}
