package com.longyou.comm;

import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.impl.UserInfoService;
import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import org.cloud.constant.CoreConstant;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.MD5Encoder;
import org.cloud.utils.SpringContextUtil;
import org.cloud.dimension.userinfo.LoginUserGetParamsDTO;

public final class LoginUtils {

    private LoginUtils() {

    }

    private static UserInfoMapper userInfoMapper;
    private static UserInfoService userInfoService;

    public static void setUserInfoMapperAndService() {

        if (userInfoMapper == null) {
            userInfoMapper = SpringContextUtil.getBean(UserInfoMapper.class);
        }

        if (userInfoService == null) {
            userInfoService = SpringContextUtil.getBean(UserInfoService.class);
        }


    }

    @SneakyThrows
    public static LoginUserDetails createOrUpdateUserByLoginUserGetParamsDTO(final LoginUserGetParamsDTO loginUserGetParamsDTO,
        final String salt) {

        setUserInfoMapperAndService();

        final String password = MD5Encoder.encode(loginUserGetParamsDTO.getPassword(), salt);  // 先用用户名做校验
        final String appName = loginUserGetParamsDTO.getMicroServiceName();
        loginUserGetParamsDTO.getParamMap().put(CoreConstant._USER_TYPE_KEY, appName);

        LoginUserDetails userDetails = userInfoMapper.getUserByNameForAuth(loginUserGetParamsDTO);
        if (userDetails == null) {
            // TODO 保存到t_frame_user表里
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("userName", loginUserGetParamsDTO.getUserName());
            userMap.put("password", password);
            userMap.put("createBy", loginUserGetParamsDTO.getUserName());
            userMap.put("updateBy", loginUserGetParamsDTO.getUserName());
            userMap.put("status", 1);
            userMap.put("defaultRole", "User");
            userMap.put("userType", loginUserGetParamsDTO.getLoginType());
            userMap.put("userRegistSource", appName);
            userMap.put("sessionKey", "");
            userInfoMapper.insertIntoUserInfo(userMap);
        } else {
            userDetails.setPassword(password);
            userDetails.setSessionKey(appName);
            userInfoMapper.updateLoginUserById(userDetails);
        }
        userDetails = userInfoService.getUserByNameForAuth(loginUserGetParamsDTO);
        return userDetails;
    }
}
