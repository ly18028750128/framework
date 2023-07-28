package com.longyou.comm.admin.service.impl;

import com.longyou.comm.admin.service.IUserAdminService;
import com.longyou.comm.mapper.TFrameUserDao;
import com.longyou.comm.mapper.TFrameUserRoleDao;
import lombok.extern.slf4j.Slf4j;
import org.cloud.logs.annotation.AuthLog;
import org.cloud.constant.CoreConstant.OperateLogType;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.model.TFrameUser;
import org.cloud.model.TFrameUserRole;
import org.cloud.utils.CollectionUtil;

import org.cloud.utils.EnvUtil;
import org.cloud.utils.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserAdminService implements IUserAdminService {

    @Autowired
    TFrameUserDao frameUserDao;

    @Override
    @AuthLog(bizType = "user.admin.saveOrUpdate", desc = "修改用户信息", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
    public long saveOrUpdate(TFrameUser frameUser) throws Exception {
        LoginUserDetails userDetails = RequestContextManager.single().getRequestContext().getUser();
        long result = 0;
        frameUser.setUpdateBy(userDetails.getUsername());
        frameUser.setUpdateDate(new Date());
        if (StringUtils.isEmpty(frameUser.getId())) {
            frameUser.setCreateBy(userDetails.getUsername());
            frameUser.setCreateDate(new Date());
            final String salt = EnvUtil.single().getEnv("spring.security.salt-password", "");
            frameUser.setPassword(MD5Encoder.encode(frameUser.getPassword(), salt));
            if (frameUserDao.insertSelective(frameUser)>0) {
                result = frameUser.getId();
            }
        } else {
            if (frameUserDao.updateByPrimaryKeySelective(frameUser) > 0) {
                result = frameUser.getId();
            }
        }
        if (frameUser.isUpdated()) {
            saveOrUpdateFrameRoleDataInterfaceList(frameUser.getId(), frameUser.getFrameUserRoleList());
        }
        log.info(userDetails.getUsername() + "正在执行更新用户权限的操作{}", frameUser);
        return result;
    }

    @Autowired
    TFrameUserRoleDao frameUserRoleDao;

    private void saveOrUpdateFrameRoleDataInterfaceList(final Long userId, List<TFrameUserRole> frameUserRoleList) throws Exception {
        // 增加和更新需要去重
        List<TFrameUserRole> updateList = new ArrayList<>();
        if (!CollectionUtil.single().isEmpty(frameUserRoleList)) {
            updateList = frameUserRoleList.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TFrameUserRole::getRoleId)))
                            , ArrayList::new
                    ));
        }
        frameUserRoleDao.deleteByUserId(userId);  //如果有更新那么先删除再更新，暂时这样简单处理下
        for (TFrameUserRole frameUserRole : updateList) {
            frameUserRole.setUserId(userId);
            frameUserRoleDao.insertSelective(frameUserRole);
        }

    }
}
