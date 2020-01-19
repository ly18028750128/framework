package com.longyou.gateway.security.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.longyou.gateway.security.response.MessageCode;
import com.longyou.gateway.security.response.WsResponse;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.model.TFrameRole;
import org.cloud.model.TFrameRoleData;
import org.cloud.model.TFrameRoleResource;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;


@Component
public class AuthenticationSuccessHandler extends WebFilterChainServerAuthenticationSuccessHandler {

    final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandler.class);

    @Autowired
    RedisUtil redisUtil;

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        ServerHttpResponse response = exchange.getResponse();
        ServerHttpRequest request = exchange.getRequest();
        //设置headers
        HttpHeaders httpHeaders = response.getHeaders();
        httpHeaders.add("Content-Type", "application/json; charset=UTF-8");
        httpHeaders.add("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        //设置body
        WsResponse wsResponse = WsResponse.success();
        byte[] dataBytes = {};
        ObjectMapper mapper = new ObjectMapper();
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            final String userBasic64Random = Double.toString(Math.random() * 10000000);
            final String userBasic64RandomKey = userDetails.getUsername() + webFilterExchange.getExchange().getLogPrefix();
            final long timeSaltChangeInterval = Long.parseLong(CommonUtil.single().getEnv("system.auth_basic_expire_time", Long.toString(24 * 60 * 60L)));
            final String basic64SplitStr = CommonUtil.single().getEnv("system.auth_basic64_split", CoreConstant._USER_BASIC64_SPLIT_STR);
            redisUtil.set(CoreConstant._REDIS_USER_SUCCESS_TOKEN_PREFIX + userBasic64RandomKey, userBasic64Random, timeSaltChangeInterval);
            final StringBuffer authValue = new StringBuffer(userDetails.getUsername() + ":" + MD5Encoder.encode(userDetails.getPassword(), userBasic64Random));
            authValue.append(basic64SplitStr + userBasic64RandomKey);
            String token = Base64.getEncoder().encodeToString( authValue.toString().getBytes());
            if (userDetails instanceof LoginUserDetails) {
                LoginUserDetails loginUserDetails = ((LoginUserDetails) userDetails);
                loginUserDetails.setToken(token);
                // 缓存用户的角色列表
                redisUtil.set(CoreConstant.USER_ROLE_LIST_CACHE_KEY + loginUserDetails.getId(), loginUserDetails.getRoles(), 24 * 60 * 60L);
                Set<String> userFunctions = new HashSet<>();
                Map<String,Set<String>> userDatas=new HashMap<>();
                Set<String> roles=new HashSet<>();
                for (TFrameRole frameRole : loginUserDetails.getRoles()) {
                    for(TFrameRoleResource frameRoleResource:frameRole.getFrameRoleResourceList()) {
                        userFunctions.add(frameRoleResource.getFrameworkResource().getResourceCode());
                    }
                    for(TFrameRoleData frameRoleResource:frameRole.getFrameRoleDataList()) {
                        Set<String> dataDimensionset = userDatas.get(frameRoleResource.getDataDimension());
                        if(dataDimensionset==null) {
                            userDatas.put(frameRoleResource.getDataDimension(),new HashSet<String>());
                        }
                        userDatas.get(frameRoleResource.getDataDimension()).add(frameRoleResource.getDataDimensionValue());
                    }
                    roles.add(frameRole.getRoleCode());
                }

                // 缓存用户数据权限，按维度缓存
                redisUtil.set(CoreConstant.USER_DATA_LIST_CACHE_KEY+loginUserDetails.getId(),userFunctions, 24 * 60 * 60L);

                redisUtil.hashSet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY+loginUserDetails.getId(),"datas",userFunctions,24 * 60 * 60L);



                // 缓存用操作仅限信息
                redisUtil.set(CoreConstant.USER_FUNCTION_LIST_CACHE_KEY+loginUserDetails.getId(),userFunctions, 24 * 60 * 60L);

                // 缓存角色名称
                redisUtil.set(CoreConstant.USER_ROLE_STR_CACHE_KEY+loginUserDetails.getId(),roles, 24 * 60 * 60L);

                // 缓存登录用户信息
                loginUserDetails.setRoles(null);
                redisUtil.set(CoreConstant._BASIC64_TOKEN_USER_CACHE_KEY + "basic " + token, userDetails, 24 * 60 * 60L);
            }
            httpHeaders.add(HttpHeaders.AUTHORIZATION, token);
            wsResponse.setResult(userDetails);
            dataBytes = mapper.writeValueAsBytes(wsResponse);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            JsonObject result = new JsonObject();
            result.addProperty("status", MessageCode.COMMON_FAILURE.getCode());
            result.addProperty("message", "授权异常");
            dataBytes = result.toString().getBytes();
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }
}
