package com.longyou.comm.service.impl;

import com.longyou.comm.mapper.TFrameRoleDao;
import com.longyou.comm.mapper.TFrameRoleResourceDao;
import com.longyou.comm.mapper.TFrameUserRoleDao;
import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.IUserInfoService;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.model.TFrameRole;
import org.cloud.model.TFrameRoleMenu;
import org.cloud.model.TFrameUserRole;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserInfoService implements IUserInfoService {

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    TFrameUserRoleDao frameUserRoleDao;

    @Autowired
    TFrameRoleResourceDao frameRoleResourceDao;

    @Autowired
    TFrameRoleDao frameRoleDao;

    @Autowired
    RedisUtil redisUtil;


    @Override
    @Transactional(readOnly = true)
    public LoginUserDetails getUserByNameForAuth(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {
        LoginUserDetails loginUserDetails = userInfoMapper.getUserByNameForAuth(loginUserGetParamsDTO);
        List<TFrameUserRole> frameUserRoleList = frameUserRoleDao.selectByUserId(loginUserDetails.getId());
        if (frameUserRoleList == null || frameUserRoleList.isEmpty()) {
            TFrameRole frameRole = frameRoleDao.selectByRoleCode(loginUserDetails.getDefaultRole());
            if (frameRole == null) {   // 如果找不到默认角色，那么默认角色为空！
                frameRole = new TFrameRole();
                frameRole.setRoleCode(loginUserDetails.getDefaultRole());
            }
            loginUserDetails.getRoles().add(frameRole);
        } else {
            //将所有菜单增加到用户属性中，用于不区分角色的情况下展示
            for (TFrameUserRole frameUserRole : frameUserRoleList) {
                if (frameUserRole.getFrameRole() == null)
                    continue;
                loginUserDetails.getRoles().add(frameUserRole.getFrameRole());
            }
        }
        // 将菜单全部加载到对应的菜单列表中
        for (TFrameRole frameRole : loginUserDetails.getRoles()) {
            if (frameRole.getFrameRoleMenuList() == null)
                continue;
            for (TFrameRoleMenu frameRoleMenu : frameRole.getFrameRoleMenuList()) {
                loginUserDetails.getFrameMenuList().add(frameRoleMenu.getFrameMenu());
            }
        }
        return loginUserDetails;
    }
}
