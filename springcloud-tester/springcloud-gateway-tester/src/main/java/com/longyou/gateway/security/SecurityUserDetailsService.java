package com.longyou.gateway.security;

import com.longyou.gateway.dao.IUserInfoDao;
import com.longyou.gateway.security.entity.AuthUserDetails;
import com.longyou.gateway.util.MD5Encoder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
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

    @Autowired
    IUserInfoDao userInfoDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Mono<UserDetails> findByUsername(String username) {

       UserDetails userFromDb = userInfoDao.getUserByName(username);

       //todo 预留调用数据库根据用户名获取用户
        if(userFromDb!=null && StringUtils.equals(userFromDb.getUsername(),username)){
//        if(StringUtils.equals(userName,username)){
            UserDetails user = User.withUserDetails(userFromDb).build();
            return Mono.just(user);
        }
        else{
            return Mono.error(new UsernameNotFoundException("User Not Found"));
        }
    }
}
