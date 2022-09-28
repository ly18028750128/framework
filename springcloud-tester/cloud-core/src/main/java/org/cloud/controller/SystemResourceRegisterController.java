package org.cloud.controller;

import org.cloud.annotation.SystemResource;
import org.cloud.constant.CoreConstant;
import org.cloud.feign.service.ICommonServiceFeignClient;
import org.cloud.feign.service.ISystemResourceRegisterFeignClient;
import org.cloud.model.TFrameMenu;
import org.cloud.model.TFrameworkResource;
import org.cloud.model.TMicroserviceRegister;
import org.cloud.utils.SpringContextUtil;
import org.cloud.utils.SystemStringUtil;
import org.cloud.vo.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

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

    @RequestMapping("/register/all")
    @SystemResource(path = "/system/resource", value = "/register/all", authMethod = CoreConstant.AuthMethod.ALLSYSTEMUSER)
    public ResponseResult register() throws Exception {
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
                logger.error("{}", e);
            }
        }
        Map<String, Object> beans = SpringContextUtil.getApplicationContext().getBeansWithAnnotation(SystemResource.class);
        for (final String beanName : beans.keySet()) {
            logger.info("beanName:{}", beanName);
            SystemResource beanResourceAnnotation = AnnotationUtils.findAnnotation(beans.get(beanName).getClass(), SystemResource.class);
            logger.info("beanResourceAnnotation:{}", beanResourceAnnotation);
            if (!SystemStringUtil.single().isEmpty(beanResourceAnnotation.menuCode())) {
                TFrameMenu parentMenu = null;
                if (!SystemStringUtil.single().isEmpty(beanResourceAnnotation.parentMenuCode())) {
                    parentMenu = new TFrameMenu();
                    parentMenu.setMenuCode(beanResourceAnnotation.parentMenuCode());
                    parentMenu.setMenuName(beanResourceAnnotation.parentMenuName());
                    parentMenu.setSeqNo(beanResourceAnnotation.index());
                    parentMenu.setCreateBy("admin");
                    parentMenu.setCreateDate(new Date());
                    parentMenu.setUpdateBy("admin");
                    parentMenu.setUpdateDate(new Date());
                    parentMenu.setStatus(1);
                    try {
                        logger.info("parentMenu:{}", systemResourceRegisterFeignClient.saveOrUpdateMenu(parentMenu));
                    } catch (Exception e) {
                        logger.error("{}", e);
                    }

                }
                TFrameMenu menu = new TFrameMenu();
                if (parentMenu != null) {
                    menu.setParentMenuId(parentMenu.getMenuId());
                }
                menu.setMenuCode(beanResourceAnnotation.menuCode());
                menu.setMenuName(beanResourceAnnotation.menuName());
                menu.setSeqNo(beanResourceAnnotation.index());
                menu.setCreateBy("admin");
                menu.setCreateDate(new Date());
                menu.setUpdateBy("admin");
                menu.setUpdateDate(new Date());
                menu.setStatus(1);
                try {
                    logger.info("menu:{}", systemResourceRegisterFeignClient.saveOrUpdateMenu(menu));
                } catch (Exception e) {
                    logger.error("{}", e);
                }
            }

            Method[] methods = beans.get(beanName).getClass().getMethods();
            for (Method declaredMethod : methods) {
                SystemResource methodResourceAnnotation = AnnotationUtils.findAnnotation(declaredMethod, SystemResource.class);
                if (methodResourceAnnotation != null) {
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
                }
            }
        }
        return ResponseResult.createSuccessResult();
    }
}
