package org.cloud.controller;

import org.cloud.annotation.SystemResource;
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

    @RequestMapping("/register/all")
    public ResponseResult register() throws Exception {

        if (!SystemStringUtil.single().isEmpty(microName)) {
            TMicroserviceRegister microserviceRegister = new TMicroserviceRegister();
            microserviceRegister.setMicroserviceName(microName);
            microserviceRegister.setCreateBy("admin");
            microserviceRegister.setCreateDate(new Date());
            microserviceRegister.setUpdateBy("admin");
            microserviceRegister.setUpdateDate(new Date());
//            system/resource/register/microservice
            try {
                ResponseEntity<ResponseResult> response = restTemplate.postForEntity("http://" + appGroup + "COMMON-SERVICE/system/resource/register/microservice", microserviceRegister, ResponseResult.class);
                ResponseResult value = response.getBody();
            } catch (Exception e) {
                logger.error("{}", e);
            }
        }
        Map<String, Object> beans = SpringContextUtil.getApplicationContext().getBeansWithAnnotation(SystemResource.class);
        for (final String beanName : beans.keySet()) {
            logger.info("beanName:{}", beanName);
            SystemResource beanResourceAnnotation = AnnotationUtils.findAnnotation(beans.get(beanName).getClass(), SystemResource.class);
            logger.info("beanResourceAnnotation:{}", beanResourceAnnotation);
            // todo 这里处理菜单创建的逻辑

            if(!SystemStringUtil.single().isEmpty(beanResourceAnnotation.menuCode())){
                TFrameMenu parentMenu = null;
                if(!SystemStringUtil.single().isEmpty(beanResourceAnnotation.parentMenuCode())){
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
                        ResponseEntity<TFrameMenu> response = restTemplate.postForEntity("http://" + appGroup + "COMMON-SERVICE/system/resource/register/menu", parentMenu, TFrameMenu.class);
                        parentMenu = response.getBody();
                    } catch (Exception e) {
                        logger.error("{}", e);
                    }

                }
                TFrameMenu menu = new TFrameMenu();
                if(parentMenu!=null){
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
                    ResponseEntity<TFrameMenu> response = restTemplate.postForEntity("http://" + appGroup + "COMMON-SERVICE/system/resource/register/menu", menu, TFrameMenu.class);
                    TFrameMenu value = response.getBody();
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
                        ResponseEntity<TFrameworkResource> response = restTemplate.postForEntity(
                                "http://" + appGroup + "COMMON-SERVICE/system/resource/register/resource",
                                frameworkResource, TFrameworkResource.class);
                        frameworkResource = response.getBody();
                    } catch (Exception e) {
                        logger.error("{}", e);
                    }

                    // todo 这里处理资源注册的相关逻辑
                    logger.info("frameworkResource:{}", frameworkResource);
                }
            }
        }
        return ResponseResult.createSuccessResult();
    }
}
