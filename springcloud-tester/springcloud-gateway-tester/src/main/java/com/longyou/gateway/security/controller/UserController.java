package com.longyou.gateway.security.controller;

import com.longyou.gateway.security.entity.AuthUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/info")
public class UserController {
    @RequestMapping("/authentication")
    public User getAuthentication(Authentication authentication){
      return (User)authentication.getPrincipal();
    }
}
