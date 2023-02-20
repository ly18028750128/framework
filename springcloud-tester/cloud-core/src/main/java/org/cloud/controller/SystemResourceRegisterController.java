package org.cloud.controller;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.CoreConstant.AuthMethod;
import org.cloud.feign.service.ISystemResourceRegisterFeignClient;
import org.cloud.model.TFrameMenu;
import org.cloud.model.TFrameworkResource;
import org.cloud.model.TMicroserviceRegister;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.SpringContextUtil;
import org.cloud.utils.SystemStringUtil;
import org.cloud.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

@Api(value = "SystemResourceRegisterController", tags = "系统资源注册接口")
@RestController
@RequestMapping("/system/resource")
public class SystemResourceRegisterController {

    final Logger logger = LoggerFactory.getLogger(SystemResourceRegisterController.class);
    @Value("${spring.application.noGroupName:}")
    private String microName;

    @Autowired
    RestTemplate restTemplate;

    @Value("${spring.application.group:}")
    String appGroup;

    @Autowired
    ISystemResourceRegisterFeignClient systemResourceRegisterFeignClient;

    @ApiOperation(value = "注册操作资源到数据库", notes = "注册资源到数据库，可生成菜单")
    @PostMapping("/register/all")
    @SystemResource(path = "/system/resource", value = "/register/all", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    public ResponseResult<Object> register() throws Exception {
        if (!SystemStringUtil.single().isEmpty(microName)) {
            TMicroserviceRegister microserviceRegister = new TMicroserviceRegister();
            microserviceRegister.setMicroserviceName(microName);
            microserviceRegister.setCreateBy("admin");
            microserviceRegister.setCreateDate(new Date());
            microserviceRegister.setUpdateBy("admin");
            microserviceRegister.setUpdateDate(new Date());
            try {
                logger.info("microserviceRegister:{}", systemResourceRegisterFeignClient.saveOrUpdateMicroserviceRegister(microserviceRegister));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        Map<String, Object> beans = SpringContextUtil.getApplicationContext().getBeansWithAnnotation(SystemResource.class);
        for (final String beanName : beans.keySet()) {
            logger.info("beanName:{}", beanName);
            SystemResource beanResourceAnnotation = AnnotationUtils.findAnnotation(beans.get(beanName).getClass(), SystemResource.class);
            logger.info("beanResourceAnnotation:{}", beanResourceAnnotation);
            TFrameMenu parentMenu = null;
            if (beanResourceAnnotation == null) {
                continue;
            }

            if (!SystemStringUtil.single().isEmpty(beanResourceAnnotation.parentMenuCode())) {
                parentMenu = new TFrameMenu();
                parentMenu.setMenuCode(beanResourceAnnotation.parentMenuCode());
                parentMenu.setMenuName(beanResourceAnnotation.parentMenuName());
                parentMenu.setSeqNo(beanResourceAnnotation.index());
                parentMenu.setCreateBy("admin");
                parentMenu.setCreateDate(new Date());
                parentMenu.setUpdateBy("admin");
                parentMenu.setUpdateDate(new Date());
                parentMenu.setType(0);  // 类上注解定义为目录
                parentMenu.setShowType(0);
//                parentMenu.setComponentPath("/layout/Layout");    //旧的后台管理
                parentMenu.setComponentPath("layout/routerView/parent");    //新的后台管理
                parentMenu.setMenuUrl(beanResourceAnnotation.path());
                parentMenu.setStatus(1);
                try {
                    String jsonStr = JSON.toJSONString(systemResourceRegisterFeignClient.saveOrUpdateMenu(parentMenu).getData());
                    parentMenu = JSON.parseObject(jsonStr, TFrameMenu.class);
                    logger.info("parentMenu:{}", parentMenu);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }

            Method[] methods = beans.get(beanName).getClass().getMethods();
            for (Method declaredMethod : methods) {
                SystemResource methodResourceAnnotation = AnnotationUtils.findAnnotation(declaredMethod, SystemResource.class);
                if (methodResourceAnnotation == null) {
                    continue;
                }
                TFrameworkResource frameworkResource = new TFrameworkResource();
                frameworkResource.setResourcePath(beanResourceAnnotation.path());
                frameworkResource.setResourceCode(methodResourceAnnotation.value());
                frameworkResource.setResourceName(methodResourceAnnotation.description());
                frameworkResource.setBelongMicroservice(microName);
                frameworkResource.setMethod(methodResourceAnnotation.authMethod().value());
                frameworkResource.setCreateBy("admin");
                frameworkResource.setCreateDate(new Date());
                frameworkResource.setUpdateBy("admin");
                frameworkResource.setUpdateDate(new Date());
                try {
                    logger.info("frameworkResource:{}", systemResourceRegisterFeignClient.saveOrUpdateResource(frameworkResource));
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                if (parentMenu != null && CollectionUtil.single().isNotEmpty(methodResourceAnnotation.menuCode())) {
                    TFrameMenu menu = new TFrameMenu();
                    menu.setMenuCode(methodResourceAnnotation.menuCode());
                    menu.setMenuName(methodResourceAnnotation.menuName());
                    menu.setSeqNo(methodResourceAnnotation.index());
                    menu.setCreateBy("admin");
                    menu.setParentMenuId(parentMenu.getMenuId());
                    menu.setType(1);  //
                    if (AuthMethod.BYUSERPERMISSION.equals(methodResourceAnnotation.authMethod())) {
                        menu.setShowType(1);
                        String functionResourceCode = String.format("%s::%s::%s", microName, beanResourceAnnotation.path(), methodResourceAnnotation.value());
                        menu.setFunctionResourceCode(functionResourceCode);
                    } else {
                        menu.setShowType(0);
                    }
//                    menu.setMenuUrl(methodResourceAnnotation.menuCode());
                    menu.setMenuUrl("/"+methodResourceAnnotation.menuCode());  // 新版本的后台管理，加上/
                    menu.setComponentPath(methodResourceAnnotation.menuCode());
                    menu.setCreateDate(new Date());
                    menu.setUpdateBy("admin");
                    menu.setUpdateDate(new Date());
                    menu.setStatus(1);
                    try {
                        logger.info("menu:{}", systemResourceRegisterFeignClient.saveOrUpdateMenu(menu));
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }


        }
        return ResponseResult.createSuccessResult();
    }
}
