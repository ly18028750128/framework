package com.longyou.gateway.security.handler;


import static com.longyou.gateway.security.response.MessageCode.COMMON_FAILURE;
import static com.longyou.gateway.security.response.MessageCode.COMMON_SUCCESS;
import static org.cloud.constant.CoreConstant._BASIC64_TOKEN_USER_SUCCESS_TOKEN_KEY;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonObject;
import com.longyou.gateway.security.response.WsResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.LoginConstants;
import org.cloud.constant.LoginConstants.LoginError;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.EnvUtil;
import org.cloud.utils.MD5Encoder;
import org.cloud.utils.process.ProcessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.WebFilterChainServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Transactional(propagation = Propagation.NEVER)
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

        byte[] dataBytes = {};
        try {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // 生成token加盐值和key
            final String userBasic64Random = MD5Encoder.encode(webFilterExchange.getExchange().getLogPrefix() + Math.random(), "天下无双");
            final String userBasic64RandomKey = MD5Encoder.encode(webFilterExchange.getExchange().getLogPrefix());
            // 获取token的超时时间设置
            long timeSaltChangeInterval = EnvUtil.single().getEnv("system.auth_basic_expire_time", 120 * 24 * 60 * 60L, Long.class);
            // 获取解密分隔符的处理
            final String basic64SplitStr = EnvUtil.single().getEnv("system.auth_basic64_split", CoreConstant._USER_BASIC64_SPLIT_STR);

            // 分隔userName, 备注用户名和密码不能包含 ：
            // 将token加盐的key增加到尾部
            String token = Base64.getEncoder().encodeToString(
                (userDetails.getUsername() + ":" + MD5Encoder.encode(userDetails.getPassword(), userBasic64Random) + basic64SplitStr
                    + userBasic64RandomKey).getBytes());
            if (userDetails instanceof LoginUserDetails) {
                LoginUserDetails loginUserDetails = ((LoginUserDetails) userDetails);
                // 登录成功后重置
                final String userNameKey = loginUserDetails.getUserType() + ":" + loginUserDetails.getUsername();
                final String userLoginCountKey = LoginError.USER_ERROR_COUNT_KEY.value + userNameKey;
                redisUtil.set(userLoginCountKey, 0);

                // 如果是后台管理用户，那么超时时间为60分钟
                if (LoginConstants.REGIST_SOURCE_BACKGROUND.equals(loginUserDetails.getUserRegistSource())) {
                    timeSaltChangeInterval = EnvUtil.single().getEnv("system.auth_basic_background_expire_time", 60 * 60L, Long.class);
                }
                // 缓存当前登录用户的登录信息
                final String successKey = MD5Encoder.encode("basic " + token);
                redisUtil.set(CoreConstant._BASIC64_TOKEN_USER_CACHE_KEY + successKey, userDetails, timeSaltChangeInterval);

                // 缓存已经登录的信息
                final Long expireTime = System.currentTimeMillis() + timeSaltChangeInterval * 1000L;
                // 先清理过期的数据再增加防止，更改过期时间的情况，会导致登录不成功的问题
                cleanUserLogin(loginUserDetails);
                redisUtil.hashSet(_BASIC64_TOKEN_USER_SUCCESS_TOKEN_KEY + loginUserDetails.getId(), successKey, expireTime, -1L);
                loginUserDetails.setPassword("***********");
                loginUserDetails.setToken(token);
            }
            // 将token加盐的值放到redis缓存中
            redisUtil.set(CoreConstant._REDIS_USER_SUCCESS_TOKEN_PREFIX + userBasic64RandomKey, userBasic64Random, timeSaltChangeInterval);
            httpHeaders.add(HttpHeaders.AUTHORIZATION, token);
            WsResponse<UserDetails> wsResponse = new WsResponse<>(COMMON_SUCCESS, userDetails);
            dataBytes = JSON.toJSONString(wsResponse).getBytes(StandardCharsets.UTF_8);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            JsonObject result = new JsonObject();
            result.addProperty("status", COMMON_FAILURE.code);
            result.addProperty("message", "授权异常");
            dataBytes = result.toString().getBytes();
        }
        DataBuffer bodyDataBuffer = response.bufferFactory().wrap(dataBytes);
        return response.writeWith(Mono.just(bodyDataBuffer));
    }

    private void cleanUserLogin(LoginUserDetails loginUserDetails) {
        // 每个用户最大的登录客户端，默认为5个
        final int maxSingleUserLoginCount = EnvUtil.single().getEnv("system.user.maxSingleUserLoginCount", 5, Integer.class) - 1;
        HashOperations<String, String, Long> opsForHash = redisUtil.getRedisTemplate().opsForHash();
        Map<String, Long> userLoginTokenTokeMap = opsForHash.entries(_BASIC64_TOKEN_USER_SUCCESS_TOKEN_KEY + loginUserDetails.getId());
        if (CollectionUtil.single().isNotEmpty(userLoginTokenTokeMap) && (userLoginTokenTokeMap.size() > maxSingleUserLoginCount)) {
            ProcessUtil.single().runCallable(() -> {
                try {
                    LinkedHashMap<String, Long> sortedMap = new LinkedHashMap<>();
                    userLoginTokenTokeMap.entrySet().stream().sorted(Entry.comparingByValue(Comparator.reverseOrder()))
                        .forEachOrdered(s -> sortedMap.put(s.getKey(), s.getValue()));
                    LinkedHashMap<String, Long> willRemoveMaps = CollectionUtil.single()
                        .subMap(sortedMap, maxSingleUserLoginCount, userLoginTokenTokeMap.size());
                    for (Entry<String, Long> entry : willRemoveMaps.entrySet()) {
                        redisUtil.hashDel(_BASIC64_TOKEN_USER_SUCCESS_TOKEN_KEY + loginUserDetails.getId(), entry.getKey());
                        redisUtil.remove(CoreConstant._BASIC64_TOKEN_USER_CACHE_KEY + entry.getKey());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            });
        }
    }
}
