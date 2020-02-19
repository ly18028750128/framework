package com.longyou.comm.conntroller;

import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/discovery/client")
public class QuartzClientController {

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/getServices")
    public ResponseResult getServices() throws Exception {
        ResponseResult successResult = ResponseResult.createSuccessResult();
        successResult.setData(discoveryClient.getServices());
        return successResult;
    }
}
