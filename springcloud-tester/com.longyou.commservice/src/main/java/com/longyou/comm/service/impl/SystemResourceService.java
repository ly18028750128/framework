package com.longyou.comm.service.impl;

import com.longyou.comm.mapper.TFrameMenuDao;
import com.longyou.comm.mapper.TFrameworkResourceDao;
import com.longyou.comm.mapper.TMicroserviceRegisterDao;
import com.longyou.comm.service.ISystemResourceService;
import org.cloud.model.TFrameMenu;
import org.cloud.model.TFrameworkResource;
import org.cloud.model.TMicroserviceRegister;
import org.cloud.utils.SystemStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SystemResourceService implements ISystemResourceService {

    Logger logger = LoggerFactory.getLogger(SystemResourceService.class);

    @Autowired
    TMicroserviceRegisterDao microserviceRegisterDao;

    @Autowired
    TFrameMenuDao frameMenuDao;

    @Autowired
    TFrameworkResourceDao frameworkResourceDao;


    @Override
    @Transactional
    public Integer saveOrUpdateMicroserviceRegister(TMicroserviceRegister microserviceRegister) throws Exception {

        TMicroserviceRegister selectData = microserviceRegisterDao.selectByMicroserviceName(microserviceRegister.getMicroserviceName());
        if (selectData == null) {
            return microserviceRegisterDao.insert(microserviceRegister);
        } else {
            microserviceRegister.setMicroserviceId(selectData.getMicroserviceId());
            return microserviceRegisterDao.updateByPrimaryKeySelective(microserviceRegister);
        }
    }

    @Override
    public Integer saveOrUpdateMenu(TFrameMenu frameMenu) throws Exception {
        TFrameMenu selectData = frameMenuDao.selectByMenuCode(frameMenu.getMenuCode());
        if (selectData == null) {
            return frameMenuDao.insert(frameMenu);
        } else {
            frameMenu.setMenuId(selectData.getMenuId());
            if (!SystemStringUtil.single().isEmpty(selectData.getMenuName())) {
                logger.info("id为" + frameMenu.getMenuId() + "的菜单名称已经被修改过，不再修改名称！");
                return 0;
            } else {
                return frameMenuDao.updateByPrimaryKeySelective(frameMenu);
            }
        }
    }

    @Override
    public Integer saveOrUpdateResource(TFrameworkResource frameworkResource) throws Exception {
        TFrameworkResource selectData = frameworkResourceDao.selectByResourceCodeAndPath(frameworkResource.getResourceCode(), frameworkResource.getResourcePath(), frameworkResource.getBelongMicroservice());
        if (selectData == null) {
            return frameworkResourceDao.insert(frameworkResource);
        } else {
            frameworkResource.setResourceId(selectData.getResourceId());
            return frameworkResourceDao.updateByPrimaryKeySelective(frameworkResource);
        }
    }
}
