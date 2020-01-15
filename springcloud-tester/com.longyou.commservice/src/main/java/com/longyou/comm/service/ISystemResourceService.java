package com.longyou.comm.service;

import org.cloud.model.TFrameMenu;
import org.cloud.model.TFrameworkResource;
import org.cloud.model.TMicroserviceRegister;

public interface ISystemResourceService {

    Integer saveOrUpdateMicroserviceRegister(TMicroserviceRegister microserviceRegister) throws Exception;

    Integer saveOrUpdateMenu(TFrameMenu frameMenu) throws Exception;

    Integer saveOrUpdateResource(TFrameworkResource frameworkResource) throws Exception;
}
