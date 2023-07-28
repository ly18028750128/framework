package com.longyou.comm.conntroller;


import com.unknow.first.annotation.MfaAuth;
import org.cloud.vo.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
@Deprecated
public class DemoController {

    @GetMapping("/google/mfa/test")
    @MfaAuth
    public ResponseResult mfaTest() {
        return ResponseResult.createSuccessResult();
    }

}
