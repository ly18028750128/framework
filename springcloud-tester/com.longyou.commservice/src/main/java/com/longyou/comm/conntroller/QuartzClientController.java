package com.longyou.comm.conntroller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.RestTemplateUtil;
import org.cloud.utils.SystemStringUtil;
import org.cloud.utils.process.ProcessUtil;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "QuartzClientController", tags = "定时任务查询")
@SystemResource(path = "/quartz/client")
@RestController
@RequestMapping("/quartz/client")
@Slf4j
public class QuartzClientController {

    @Autowired
    DiscoveryClient discoveryClient;

    @ApiOperation(value = "查询全部定时任务", notes = "查询全部定时任务,查询所有的相关微服务的定时任务")
    @SystemResource(value = "/getAll", description = "查询全部定时任务", authMethod = AuthMethod.BYUSERPERMISSION)
    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResult<?> getQuartzJobs() throws Exception {
        String applicationGroup = CommonUtil.single().getEnv("spring.application.group", "");
        ResponseResult<?> successResult = ResponseResult.createSuccessResult();
        Map<String, List<Map>> quartzJobs = new HashMap<>();
        List<Callable<Boolean>> callables = new ArrayList<>();
        List<String> services = discoveryClient.getServices();
        for (String serviceId : services) {
            if (StringUtils.hasLength(applicationGroup) && !serviceId.toUpperCase().startsWith(applicationGroup)) {
                continue;
            }
            callables.add(() -> {
                try {
                    final String url = "http://" + serviceId.toUpperCase() + "/quartz/job/all";
                    ResponseEntity<List> response = RestTemplateUtil.single().getResponse(url, HttpMethod.GET, List.class);
                    if (response.getBody() != null && !response.getBody().isEmpty()) {
                        quartzJobs.put(serviceId.toUpperCase(), response.getBody());
                    }
                } catch (Exception e) {
                    log.info(serviceId + ",没有定时任务！");
                }
                return true;
            });
        }
        ProcessUtil.single().runCablles(callables, 20, 180L);

        Map<String, List<Map>> sortResults = quartzJobs.entrySet().stream().sorted(Entry.comparingByKey((v1, v2) -> {
            if (SystemStringUtil.single().isNotEmpty(applicationGroup) && (!v1.startsWith(applicationGroup)) && v2.startsWith(applicationGroup)) {
                return 100;
            } else if (SystemStringUtil.single().isNotEmpty(applicationGroup) && v1.startsWith(applicationGroup) && (!v2.startsWith(applicationGroup))) {
                return -100;
            } else {
                return v1.compareTo(v2);
            }
        })).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        successResult.setData(sortResults);
        return successResult;
    }

    @ApiOperation(value = "查询运行中的定时任务", notes = "查询运行中的定时任务,查询所有的相关微服务的定时任务")
    @SystemResource(value = "/getAllRunJob", description = "查询运行中的定时任务", authMethod = AuthMethod.BYUSERPERMISSION)
    @GetMapping(value = "/getAllRunJob", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseResult<?> getQuartzRunJobs() throws Exception {
        ResponseResult<?> successResult = ResponseResult.createSuccessResult();
        Map<String, List<Map>> quartzJobs = new LinkedHashMap<>();
        List<String> services = discoveryClient.getServices();
        for (String serviceId : services) {
            try {
                final String url = "http://" + serviceId.toUpperCase() + "/quartz/job/allRunJob";
                ResponseEntity<List> response = RestTemplateUtil.single().getResponse(url, HttpMethod.GET, List.class);
                quartzJobs.put(serviceId.toUpperCase(), response.getBody());
            } catch (Exception e) {
                log.info(serviceId + ",没有运行中定时任务！");
            }
        }
        successResult.setData(quartzJobs);
        return successResult;
    }

}
