package org.cloud.dimension.userinfo;

import org.cloud.entity.LoginUserDetails;

public interface LoginUserGetInterface {

    String _LOGIN_USER_GET_PREFIX = "loginUserGet.bean.";


    public LoginUserDetails getUserInfo(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception;

}
