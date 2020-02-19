package com.longyou.comm.conntroller;

import org.cloud.utils.RestTemplateUtil;
import org.cloud.utils.process.ProcessCallable;
import org.cloud.utils.process.ProcessUtil;
import org.cloud.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

@RestController
@RequestMapping("/quartz/client")
public class DiscoveryClientController {

    Logger logger = LoggerFactory.getLogger(DiscoveryClientController.class);

    @Autowired
    DiscoveryClient discoveryClient;

    @GetMapping("/getAll")
    public ResponseResult getQuartzJobs() throws Exception {
        ResponseResult successResult = ResponseResult.createSuccessResult();
        Map<String, List<Map>> quartzJobs = new LinkedHashMap<>();
        List<Callable<Boolean>> callables = new ArrayList<>();
        for (String serviceId : discoveryClient.getServices()) {
            callables.add(new ProcessCallable<Boolean>() {
                @Override
                public Boolean process() {
                    try {
                        final String url = "http://" + serviceId.toUpperCase() + "/quartz/job/all";
                        ResponseEntity<List> response = RestTemplateUtil.single().getResponse(url, HttpMethod.GET, List.class);
                        quartzJobs.put(serviceId.toUpperCase(), response.getBody());
                    } catch (Exception e) {
                        logger.info(serviceId + ",没有定时任务！");
                    }
                    return true;
                }
            });
        }
        ProcessUtil.single().runCablles(callables);
        successResult.setData(quartzJobs);
        return successResult;
    }

    @GetMapping("/getAllRunJob")
    public ResponseResult getQuartzRunJobs() throws Exception {
        ResponseResult successResult = ResponseResult.createSuccessResult();
        Map<String, List<Map>> quartzJobs = new LinkedHashMap<>();
        for (String serviceId : discoveryClient.getServices()) {
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
