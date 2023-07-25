package com.longyou.comm.conntroller.inner;

import com.longyou.comm.service.ISystemResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cloud.dimension.annotation.SystemResource;
import org.cloud.model.TFrameMenu;
import org.cloud.model.TFrameworkResource;
import org.cloud.model.TMicroserviceRegister;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "SystemResourceController", tags = "系统资源管理")
@SystemResource(path = "/system/resource")
@RestController
@RequestMapping("/inner/system/resource")
public class SystemResourceController {

    @Autowired
    ISystemResourceService systemResourceService;

    @ApiOperation(value = "更改或者保存系统微服务(仅内部调用)", notes = "更改或者保存系统微服务(仅内部调用)")
    @RequestMapping(value = "/register/microservice", method = RequestMethod.POST)
    public ResponseResult<Integer> saveOrUpdateMicroserviceRegister(@RequestBody TMicroserviceRegister microserviceRegister) throws Exception {
        return ResponseResult.createSuccessResult(systemResourceService.saveOrUpdateMicroserviceRegister(microserviceRegister));
    }

    @ApiOperation(value = "更改或者保存系统菜单(仅内部调用)", notes = "更改或者保存系统菜单(仅内部调用)")
    @RequestMapping(value = "/register/menu", method = RequestMethod.POST)
    public ResponseResult<TFrameMenu> saveOrUpdateMenu(@RequestBody TFrameMenu frameMenu) throws Exception {
        systemResourceService.saveOrUpdateMenu(frameMenu);
        return ResponseResult.createSuccessResult(frameMenu);
    }

    @ApiOperation(value = "更改或者保存系统resource(仅内部调用)", notes = "更改或者保存系统resource(仅内部调用)")
    @RequestMapping(value = "/register/resource", method = RequestMethod.POST)
    public ResponseResult<TFrameworkResource> saveOrUpdateResource(@RequestBody TFrameworkResource frameworkResource) throws Exception {
        systemResourceService.saveOrUpdateResource(frameworkResource);
        return ResponseResult.createSuccessResult(frameworkResource);
    }
}
