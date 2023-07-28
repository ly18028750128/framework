package com.longyou.comm.conntroller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;

import org.cloud.utils.EnvUtil;
import org.cloud.utils.SystemStringUtil;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "DiscoveryClientController", tags = "Discovery服务端管理")
@SystemResource(path = "/discovery/client")
@RestController
@RequestMapping("/discovery/client")
@Slf4j
public class DiscoveryClientController {

    @Autowired
    DiscoveryClient discoveryClient;

    @ApiOperation(value = "getServices", notes = "获取所有微服务")
    @SystemResource(value = "/getServices", description = "获取所有微服务", authMethod = AuthMethod.ALLSYSTEMUSER)
    @GetMapping("/getServices")
    public ResponseResult<String> getServices() {
        List<String> services = discoveryClient.getServices();
        String applicationGroup = EnvUtil.single().getEnv("spring.application.group", "");
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
        return ResponseResult.createSuccessResult(services);
    }

    /**
     * 根据serverId获取服务实例列表
     *
     * @param serviceId serviceId
     * @return 服务实例
     */
    @ApiOperation(value = "getServices", notes = "获取所有微服务")
    @SystemResource(value = "/register/microservice", description = "获取所有微服务", authMethod = AuthMethod.BYUSERPERMISSION)
    @ApiParam(name = "serviceId", value = "服务名称")
    @GetMapping("/getServiceInstances")
    public ResponseResult<ServiceInstance> getServiceInstances(@RequestParam("serviceId") String serviceId) {
        List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
        return ResponseResult.createSuccessResult(serviceInstances);
    }
}
