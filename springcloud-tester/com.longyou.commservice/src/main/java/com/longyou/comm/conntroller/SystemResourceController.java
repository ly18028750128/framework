package com.longyou.comm.conntroller;

import com.longyou.comm.service.ISystemResourceService;
import org.cloud.model.TFrameMenu;
import org.cloud.model.TFrameworkResource;
import org.cloud.model.TMicroserviceRegister;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/resource")
public class SystemResourceController {

    @Autowired
    ISystemResourceService systemResourceService;

    @RequestMapping(value = "/register/microservice", method = RequestMethod.POST)
    public ResponseResult saveOrUpdateMicroserviceRegister(@RequestBody TMicroserviceRegister microserviceRegister) throws Exception {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(systemResourceService.saveOrUpdateMicroserviceRegister(microserviceRegister));
        return responseResult;
    }

    @RequestMapping(value = "/register/menu", method = RequestMethod.POST)
    public TFrameMenu saveOrUpdateMenu(@RequestBody TFrameMenu frameMenu) throws Exception {
        systemResourceService.saveOrUpdateMenu(frameMenu);
        return frameMenu;
    }

    @RequestMapping(value = "/register/resource", method = RequestMethod.POST)
    public TFrameworkResource saveOrUpdateResource(@RequestBody TFrameworkResource frameworkResource) throws Exception {
        systemResourceService.saveOrUpdateResource(frameworkResource);
        return frameworkResource;
    }
}
