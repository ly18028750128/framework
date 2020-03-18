package com.longyou.comm.admin.service.impl;

import com.longyou.comm.admin.service.IRoleAdminService;
import com.longyou.comm.mapper.*;
import lombok.extern.slf4j.Slf4j;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.model.TFrameRole;
import org.cloud.model.TFrameRoleDataInterface;
import org.cloud.model.TFrameRoleResource;
import org.cloud.mongo.DataInterFaceParamVO;
import org.cloud.utils.CollectionUtil;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RoleAdminService implements IRoleAdminService {


    @Autowired
    TFrameRoleDao frameRoleDao;

    @Autowired
    TFrameRoleDataDao frameRoleDataDao;

    @Autowired
    TFrameRoleMenuDao frameRoleMenuDao;


    @Override
    @Transactional
    public int saveOrUpdate(final TFrameRole frameRole) throws Exception {

        int result = 0;

        LoginUserDetails user = RequestContextManager.single().getRequestContext().getUser();
        frameRole.setUpdateBy(user.getUsername());
        frameRole.setUpdateDate(new Date());

        if (frameRole.getRoleId() == null) {
            frameRole.setCreateBy(user.getUsername());
            frameRole.setCreateDate(new Date());
            result = frameRoleDao.insertSelective(frameRole);
        } else {
            result = frameRoleDao.updateByPrimaryKeySelective(frameRole);
        }

        if (result == 0) {
            throw new BusinessException("未更新到记录", frameRole.getRoleCode());
        }

        // 更新操作权限
        if (frameRole.getFrameRoleResourceUpdateFlag()) {
            saveOrUpdateFrameRoleResource(frameRole.getRoleId(), frameRole.getFrameRoleResourceList());
        }

        // 更新数据接口权限
        if (frameRole.getFrameRoleDataInterfaceUpdateFlag()) {
            saveOrUpdateFrameRoleDataInterfaceList(frameRole.getRoleId(), frameRole.getFrameRoleDataInterfaceList());
        }

        // 记录修改日志
        log.info(user.getUsername() + "正在修改权限配置{}", frameRole);
        return result;
    }


    @Autowired
    TFrameRoleDataInterfaceDao frameRoleDataInterfaceDao;

    private void saveOrUpdateFrameRoleDataInterfaceList(final Integer roleId, List<TFrameRoleDataInterface> frameRoleDataInterfaceList) throws Exception {
        // 增加和更新需要去重
        List<TFrameRoleDataInterface> updateList = new ArrayList<>();
        if (!CollectionUtil.single().isEmpty(frameRoleDataInterfaceList)) {
            updateList = frameRoleDataInterfaceList.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TFrameRoleDataInterface::getDataInterfaceId)))
                            , ArrayList::new
                    ));
        }
        frameRoleDataInterfaceDao.deleteByRoleId(roleId);
        for (TFrameRoleDataInterface frameRoleDataInterface : updateList) {
            frameRoleDataInterface.setRoleId(roleId);
            frameRoleDataInterfaceDao.insertSelective(frameRoleDataInterface);
        }
    }

    @Autowired
    TFrameRoleResourceDao frameRoleResourceDao;

    private void saveOrUpdateFrameRoleResource(final Integer roleId, List<TFrameRoleResource> frameRoleResourceList) throws Exception {
        Map<Boolean, List<TFrameRoleResource>> frameRoleResourceListMap = frameRoleResourceList.stream().collect(Collectors.groupingBy(TFrameRoleResource::getDeleted));
        // 增加和更新需要去重
        List<TFrameRoleResource> updateList = new ArrayList<>();
        if (!CollectionUtil.single().isEmpty(frameRoleResourceList)) {
            updateList = frameRoleResourceList.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TFrameRoleResource::getResourceId)))
                            , ArrayList::new
                    ));
        }
        frameRoleResourceDao.deleteByRoleId(roleId);
        for (TFrameRoleResource frameRoleResource : updateList) {
            frameRoleResource.setRoleId(roleId);
            frameRoleResourceDao.insertSelective(frameRoleResource);
        }
    }
}
