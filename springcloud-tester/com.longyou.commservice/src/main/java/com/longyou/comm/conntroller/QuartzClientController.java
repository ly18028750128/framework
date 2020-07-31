package com.longyou.comm.conntroller;

import org.cloud.utils.CommonUtil;
import org.cloud.utils.RestTemplateUtil;
import org.cloud.utils.SystemStringUtil;
import org.cloud.utils.process.ProcessCallable;
import org.cloud.utils.process.ProcessUtil;
import org.cloud.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/quartz/client")
public class QuartzClientController {

    Logger logger = LoggerFactory.getLogger(QuartzClientController.class);

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult getQuartzJobs() throws Exception {
        String applicationGroup = CommonUtil.single().getEnv("spring.application.group", "");
        ResponseResult successResult = ResponseResult.createSuccessResult();
        Map<String, List<Map>> quartzJobs = new HashMap<>();
        List<Callable<Boolean>> callables = new ArrayList<>();
        List<String> services = discoveryClient.getServices();
        for (String serviceId : services) {

            if (!"".equals(applicationGroup) && !serviceId.toUpperCase().startsWith(applicationGroup)) {
                continue;
            }


            callables.add(new ProcessCallable<Boolean>() {
                @Override
                public Boolean process() {
                    try {
                        final String url = "http://" + serviceId.toUpperCase() + "/quartz/job/all";
                        ResponseEntity<List> response = RestTemplateUtil.single().getResponse(url, HttpMethod.GET, List.class);
                        if (response.getBody() != null && !response.getBody().isEmpty()) {
                            quartzJobs.put(serviceId.toUpperCase(), response.getBody());
                        }

                    } catch (Exception e) {
                        logger.info(serviceId + ",没有定时任务！");
                    }
                    return true;
                }
            });
        }
        ProcessUtil.single().runCablles(callables, 10, 180L);

        Map<String, List<Map>> sortResults = quartzJobs.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey((v1, v2) -> {
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
                }))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        successResult.setData(sortResults);
        return successResult;
    }

    @GetMapping(value = "/getAllRunJob", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult getQuartzRunJobs() throws Exception {
        ResponseResult successResult = ResponseResult.createSuccessResult();
        Map<String, List<Map>> quartzJobs = new LinkedHashMap<>();
        List<String> services = discoveryClient.getServices();
        for (String serviceId : services) {
            try {
                final String url = "http://" + serviceId.toUpperCase() + "/quartz/job/allRunJob";
                ResponseEntity<List> response = RestTemplateUtil.single().getResponse(url, HttpMethod.GET, List.class);
                quartzJobs.put(serviceId.toUpperCase(), response.getBody());
            } catch (Exception e) {
                logger.info(serviceId + ",没有运行中定时任务！");
            }
        }
        successResult.setData(quartzJobs);
        return successResult;
    }

}
