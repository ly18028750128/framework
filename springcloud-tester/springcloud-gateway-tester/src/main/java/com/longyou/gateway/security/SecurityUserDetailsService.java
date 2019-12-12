package com.longyou.gateway.security;

import org.apache.commons.lang.StringUtils;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
public class SecurityUserDetailsService implements ReactiveUserDetailsService {

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Mono<UserDetails> findByUsername(String username) {
        UserDetails userFromDb = getUserByName(username);
        if (userFromDb != null && StringUtils.equals(userFromDb.getUsername(), username)) {
            UserDetails user = User.withUserDetails(userFromDb).build();
            user = userFromDb;
            return Mono.just(user);
        } else {
            return Mono.error(new UsernameNotFoundException("User Not Found"));
        }
    }

    @Value("${system.userinfo.query_user_url:http://COMMON-SERVICE/userinfo/getUserByName}")
    private String userinfoUrl;

    private LoginUserDetails getUserByName(String UserName) {
        ResponseEntity<LoginUserDetails> responseEntity = RestTemplateUtil.single().getResponse(userinfoUrl + "?userName=" + UserName, HttpMethod.GET, LoginUserDetails.class);
        return responseEntity.getBody();
    }

}
