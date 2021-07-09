package com.longyou.gateway.security.handler;

import static org.cloud.constant.CoreConstant._BASIC64_TOKEN_USER_SUCCESS_TOKEN_KEY;

import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.HttpStatusReturningServerLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@Transactional(propagation = Propagation.NEVER)
@Slf4j
public class CustomServerLogoutSuccessHandler extends HttpStatusReturningServerLogoutSuccessHandler {

    //    private final Logger logger = LoggerFactory.getLogger(CustomServerLogoutSuccessHandler.class);
    private final HttpStatus httpStatusToReturn = HttpStatus.OK;
    @Autowired
    RedisUtil redisUtil;

    @Override
    public Mono<Void> onLogoutSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        try {
            if (authentication.getPrincipal() instanceof LoginUserDetails) {
                LoginUserDetails loginUserDetails = (LoginUserDetails) authentication.getPrincipal();
                final String logoutKey = MD5Encoder.encode("basic " + loginUserDetails.getToken());
                redisUtil.remove(CoreConstant._BASIC64_TOKEN_USER_CACHE_KEY + logoutKey);
                redisUtil.hashDel(_BASIC64_TOKEN_USER_SUCCESS_TOKEN_KEY + loginUserDetails.getId(), logoutKey);
            }
        } catch (Exception ex) {
            log.error("{}", ex);
        }
        return super.onLogoutSuccess(webFilterExchange, authentication);
    }
}
