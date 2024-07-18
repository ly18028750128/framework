package com.longyou.comm.admin.service.impl;

import static org.cloud.constant.CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY;

import com.longyou.comm.CommonServiceConst;
import com.longyou.comm.admin.service.IUserAdminService;
import com.longyou.comm.mapper.TFrameUserDao;
import com.longyou.comm.mapper.TFrameUserRoleDao;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cloud.constant.CoreConstant.OperateLogType;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.logs.annotation.AuthLog;
import org.cloud.model.TFrameUser;
import org.cloud.model.TFrameUserRole;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.EnvUtil;
import org.cloud.utils.MD5Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserAdminService implements IUserAdminService {

    private final TFrameUserDao frameUserDao;
    private final TFrameUserRoleDao frameUserRoleDao;

    @Override
    @AuthLog(bizType = "user.admin.saveOrUpdate", desc = "修改用户信息", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdate(TFrameUser frameUser) throws Exception {
        LoginUserDetails userDetails = RequestContextManager.single().getRequestContext().getUser();
        long result = 0;
        frameUser.setUpdateBy(userDetails.getUsername());
        frameUser.setUpdateDate(new Date());
        if (ObjectUtils.isEmpty(frameUser.getId())) {
            frameUser.setCreateBy(userDetails.getUsername());
            frameUser.setCreateDate(new Date());
            final String salt = EnvUtil.single().getEnv("spring.security.salt-password", "");
            frameUser.setPassword(MD5Encoder.encode(frameUser.getPassword(), salt));
            if (frameUserDao.insertSelective(frameUser) > 0) {
                result = frameUser.getId();
            }
        } else {
            if (frameUserDao.updateByPrimaryKeySelective(frameUser) > 0) {
                result = frameUser.getId();
            }
        }
        // 保存角色信息
        saveOrUpdateFrameRoleDataInterfaceList(frameUser.getId(), frameUser.getFrameUserRoleList());
        return result;
    }

    private void saveOrUpdateFrameRoleDataInterfaceList(final Long userId, List<TFrameUserRole> frameUserRoleList) throws Exception {
        // 增加和更新需要去重
        List<TFrameUserRole> updateList = new ArrayList<>();
        if (!CollectionUtil.single().isEmpty(frameUserRoleList)) {
            updateList = frameUserRoleList.stream().collect(
                Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(TFrameUserRole::getRoleId))), ArrayList::new));
        }
        frameUserRoleDao.deleteByUserId(userId);  //如果有更新那么先删除再更新，暂时这样简单处理下
        for (TFrameUserRole frameUserRole : updateList) {
            frameUserRole.setUserId(userId);
            frameUserRoleDao.insertSelective(frameUserRole);
        }
    }

    @Override
    @AuthLog(bizType = "user.admin.resetPassword", desc = "重置密码", operateLogType = OperateLogType.LOG_TYPE_BACKEND)
    @Transactional(rollbackFor = Exception.class)
    public Long resetPassword(Long userId) throws Exception {
        final TFrameUser frameUser = new TFrameUser();
        final String restPass = EnvUtil.single().getEnv("system.setting.password.reset", "123456");  // 可以配置重置后的密码，如果没有配置那么重置后的密码为123456
        final String salt = EnvUtil.single().getEnv("spring.security.salt-password", "");
        frameUser.setId(userId);
        frameUser.setPassword(MD5Encoder.encode(restPass, salt));
        frameUser.setStatus(CommonServiceConst.userStatus.Reset.value());   // 状态设置为2，表示是密码重置
        LoginUserDetails userDetails = RequestContextManager.single().getRequestContext().getUser();
        frameUser.setUpdateBy(userDetails.getUsername());
        frameUser.setUpdateDate(new Date());
        if (frameUserDao.updateByPrimaryKeySelective(frameUser) > 0) {
            RedisUtil.single().remove(USER_LOGIN_SUCCESS_CACHE_KEY + frameUser.getId());
            return frameUser.getId();
        }

        return 0L;
    }
}
