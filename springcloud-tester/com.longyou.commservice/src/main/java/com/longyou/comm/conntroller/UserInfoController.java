package com.longyou.comm.conntroller;

import com.longyou.comm.service.IUserInfoService;
import org.cloud.entity.LoginUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/userinfo")
public class UserInfoController {

    @Autowired
    IUserInfoService userInfoService;

    @RequestMapping(value = "/getUserByName",method = RequestMethod.GET)
    public LoginUserDetails getUserByName(HttpServletRequest request, @RequestParam("userName") String userName) {
        return userInfoService.getUserByName(userName);
    }

}
