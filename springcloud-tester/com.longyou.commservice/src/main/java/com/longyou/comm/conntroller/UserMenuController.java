package com.longyou.comm.conntroller;

import com.longyou.comm.service.IUserMenuService;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/menu")
public class UserMenuController {

    @Autowired
    IUserMenuService userMenuService;

    @GetMapping("/getMenus")
    public ResponseResult getUserMenu() throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(userMenuService.listUserTreeMenu());
        return responseResult;
    }
}
