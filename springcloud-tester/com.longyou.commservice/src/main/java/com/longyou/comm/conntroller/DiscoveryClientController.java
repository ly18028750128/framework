package com.longyou.comm.conntroller;

import org.cloud.utils.CommonUtil;
import org.cloud.utils.SystemStringUtil;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/discovery/client")
public class DiscoveryClientController {

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/getServices")
    public ResponseResult getServices() {
        ResponseResult successResult = ResponseResult.createSuccessResult();
        List<String> services = discoveryClient.getServices();
        String applicationGroup = CommonUtil.single().getEnv("spring.application.group", "");
        services.sort((v1, v2) -> {

            v1 = v1.toUpperCase();
            v2 = v2.toUpperCase();

            if (SystemStringUtil.single().isNotEmpty(applicationGroup)
                && (!v1.startsWith(applicationGroup))
                && v2.startsWith(applicationGroup)) {
                return 100;
            } else if (SystemStringUtil.single().isNotEmpty(applicationGroup)
                && v1.startsWith(applicationGroup)
                && (!v2.startsWith(applicationGroup))) {
                return -100;
            } else {
                return v1.compareTo(v2);
            }
        });
        successResult.setData(services);
        return successResult;
    }

    /**
     * 根据serverId获取服务实例列表
     *
     * @param serviceId serviceId
     * @return 服务实例
     */
    @GetMapping("/getServiceInstances")
    public ResponseResult getServiceInstances(@RequestParam String serviceId) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
        ResponseResult successResult = ResponseResult.createSuccessResult();
        successResult.setData(serviceInstances);
        return successResult;
    }
}
