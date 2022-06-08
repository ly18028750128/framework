package org.cloud.userinfo;

import org.cloud.entity.LoginUserDetails;
import org.cloud.vo.LoginUserGetParamsDTO;

public interface LoginUserGetInterface {

    public static final String _LOGIN_USER_GET_PREFIX = "loginUserGet.bean.";


    public LoginUserDetails getUserInfo(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception;

}
