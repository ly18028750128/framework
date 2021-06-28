package com.longyou.comm.service.impl;

import static org.cloud.constant.UserDataDimensionConstant.USER_DIMENSION_CACHE_KEY;

import com.longyou.comm.mapper.TFrameRoleDao;
import com.longyou.comm.mapper.TFrameRoleResourceDao;
import com.longyou.comm.mapper.TFrameUserRoleDao;
import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.FrameDataDimensionService;
import com.longyou.comm.service.IUserInfoService;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.UserDataDimensionConstant;
import org.cloud.constant.UserDataDimensionConstant.UserDataDimension;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.model.*;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.MD5Encoder;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.cloud.vo.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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

  @Autowired
  FrameDataDimensionService frameDataDimensionService;

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
        if (frameUserRole.getFrameRole() == null) {
          continue;
        }
        loginUserDetails.getRoles().add(frameUserRole.getFrameRole());
      }
    }
    // 将菜单全部加载到对应的菜单列表中
    for (TFrameRole frameRole : loginUserDetails.getRoles()) {
      if (frameRole.getFrameRoleMenuList() == null) {
        continue;
      }
      for (TFrameRoleMenu frameRoleMenu : frameRole.getFrameRoleMenuList()) {
        loginUserDetails.getFrameMenuList().add(frameRoleMenu.getFrameMenu());
      }
    }

    Set<String> userFunctions = new HashSet<>();
    Map<String, Set<String>> userDatas = new HashMap<>();
    Set<String> dataInterfaces = new HashSet<>();
    Set<UserRole> userRoles = new HashSet<>();
    // 获取数据权限和操作权限
    for (TFrameRole frameRole : loginUserDetails.getRoles()) {
      for (TFrameRoleResource frameRoleResource : frameRole.getFrameRoleResourceList()) {
        if (frameRoleResource.getFrameworkResource() == null) {
          continue;
        }
        final String functionSetStr = frameRoleResource.getFrameworkResource().getBelongMicroservice() + CoreConstant._FUNCTION_SPLIT_STR +
            frameRoleResource.getFrameworkResource().getResourcePath() + CoreConstant._FUNCTION_SPLIT_STR +
            frameRoleResource.getFrameworkResource().getResourceCode();
        userFunctions.add(functionSetStr);
      }
      for (TFrameRoleData frameRoleResource : frameRole.getFrameRoleDataList()) {
        Set<String> dataDimensionset = userDatas.get(frameRoleResource.getDataDimension());
        if (dataDimensionset == null) {
          userDatas.put(frameRoleResource.getDataDimension(), new HashSet<String>());
        }
        userDatas.get(frameRoleResource.getDataDimension()).add(frameRoleResource.getDataDimensionValue());
      }

      for (TFrameRoleDataInterface frameRoleDataInterface : frameRole.getFrameRoleDataInterfaceList()) {
        dataInterfaces.add(frameRoleDataInterface.getDataInterfaceId());
      }

      UserRole userRole = new UserRole();
      userRole.setRoleId(frameRole.getRoleId());
      userRole.setRoleCode(frameRole.getRoleCode());
      userRole.setRoleName(frameRole.getRoleName());
      userRoles.add(userRole);
    }
    loginUserDetails.setUserRoles(userRoles);
    // 缓存用户的角色列表
    redisUtil.hashSet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(),
        CoreConstant.UserCacheKey.ROLE.value(),
        loginUserDetails.getRoles().stream().collect(Collectors.toMap(TFrameRole::getRoleCode, role -> role)),
        24 * 60 * 60L);

    // 缓存用户数据权限，按维度缓存
    redisUtil.hashSet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(),
        CoreConstant.UserCacheKey.DATA.value(), userDatas, 24 * 60 * 60L);

    // 缓存全部操作权限列表
    redisUtil.hashSet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(),
        CoreConstant.UserCacheKey.FUNCTION.value(), userFunctions, 24 * 60 * 60L);

    // 缓存接口权限
    redisUtil.hashSet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(),
        CoreConstant.UserCacheKey.DATA_INTERFACE.value(), dataInterfaces, 24 * 60 * 60L);

    final Map<String, Set<String>> userDataDimensionMap = getUserDataDimension(loginUserDetails.getId());

    // 缓存数据权限
    redisUtil
        .hashSet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(), USER_DIMENSION_CACHE_KEY, userDataDimensionMap, -1L);

    // 缓存角色名称
    redisUtil.hashSet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(),
        CoreConstant.UserCacheKey.ROLE_NAME.value(), userRoles, 24 * 60 * 60L);

    //缓存菜单
    redisUtil.hashSet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + loginUserDetails.getId(),
        CoreConstant.UserCacheKey.MENU.value(), loginUserDetails.getFrameMenuList(), 24 * 60 * 60L);

    // 缓存登录用户信息
    loginUserDetails.setRoles(new ArrayList<>());
    loginUserDetails.setFrameMenuList(new ArrayList<>());
    loginUserDetails.setUserDataDimensionMap(userDataDimensionMap);
    return loginUserDetails;
  }

  /**
   * 获取用户权限
   *
   * @param userId 用户id
   * @return
   */
  private Map<String, Set<String>> getUserDataDimension(Long userId) {
    List<FrameDataDimension> userDataDimensionList = frameDataDimensionService.selectDataDimensionByUserId(userId);

    Map<String, Set<String>> userScheduleListMap = Optional.ofNullable(userDataDimensionList).orElse(new ArrayList<>())
        .stream()
        .collect(Collectors.groupingBy(FrameDataDimension::getDataDimension,
            Collectors.mapping(FrameDataDimension::getDataDimensionValue, Collectors.toSet())));

    return userScheduleListMap;

  }

  @Override
  public int updatePassword(String oldPassword, String newPassword) throws Exception {
    LoginUserDetails loginUserDetails = RequestContextManager.single().getRequestContext().getUser();
    LoginUserGetParamsDTO getParamsDTO = new LoginUserGetParamsDTO();
    getParamsDTO.setUserId(loginUserDetails.getId());
    LoginUserDetails user = userInfoMapper.getUserByNameForAuth(getParamsDTO);
    final String salt = CommonUtil.single().getEnv("spring.security.salt-password", "");
    if (MD5Encoder.encode(oldPassword, salt).equals(user.getPassword())) {
      user.setPassword(MD5Encoder.encode(newPassword, salt));
      userInfoMapper.updateLoginUserById(user);
    } else {
      throw new BusinessException("原密码错误，请重新输入!");
    }
    return 1;
  }

}
