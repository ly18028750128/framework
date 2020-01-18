package com.longyou.comm.service.impl;


import com.alibaba.fastjson.JSON;
import com.longyou.comm.config.MicroAppConfig;
import com.longyou.comm.config.MicroAppConfigList;
import com.longyou.comm.mapper.UserInfoMapper;
import com.longyou.comm.service.IUserInfoService;
import com.netflix.ribbon.proxy.annotation.Http;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.cloud.constant.CoreConstant;
import org.cloud.entity.LoginUserDetails;
import org.cloud.userinfo.LoginUserGetInterface;
import org.cloud.utils.MD5Encoder;
import org.cloud.utils.http.HttpRequestParams;
import org.cloud.utils.http.OKHttpClientUtil;
import org.cloud.vo.LoginUserGetParamsDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service(LoginUserGetInterface._LOGIN_USER_GET_PREFIX + "weixin")
public class WeixinLoginUserGetService implements LoginUserGetInterface {

    @Value("${spring.security.password_salt:}")
    String salt;

    @Autowired
    UserInfoMapper userInfoMapper ;

    @Autowired
    MicroAppConfigList microAppConfigList;

    @Autowired
    IUserInfoService userInfoService;

    @Override
    @Transactional
    public LoginUserDetails getUserInfo(LoginUserGetParamsDTO loginUserGetParamsDTO) throws Exception {

        final MicroAppConfig microAppConfig = microAppConfigList.getAppList().get(loginUserGetParamsDTO.getMicroAppIndex());

        final String wenxinLoginUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

        LoginUserDetails userDetails = new LoginUserDetails();
        final String micrLoginCode = (String) loginUserGetParamsDTO.getParams().get(CoreConstant._MICRO_LOGIN_CODE_KEY);

        // TODO 此处编写通过jscode获取用户信息的操作
        HttpRequestParams httpRequestParams = new HttpRequestParams();
        final Call call = OKHttpClientUtil.single().createGetCall(httpRequestParams, String.format(wenxinLoginUrl, microAppConfig.getAppid(), microAppConfig.getAppPassword(), micrLoginCode));

        ResponseBody responeWexinLoginInfo = call.execute().body();
        Map weixinUserMap = JSON.parseObject(responeWexinLoginInfo.string(),Map.class);
        responeWexinLoginInfo.close();
        final String userName = (String)weixinUserMap.get("openid");

        if(userName == null){
            userDetails.setUsername("未授权的微信用户!");
            userDetails.setPassword("未授权的微信用户!");
            return userDetails;
        }
        final String password  = MD5Encoder.encode(micrLoginCode, salt);
        final String sessionKey = (String)weixinUserMap.get("session_key");
        // 设置成获取的用户名
        loginUserGetParamsDTO.setUserName(userName);
        loginUserGetParamsDTO.getParams().put(CoreConstant._USER_TYPE_KEY,microAppConfig.getAppName());
        userDetails = userInfoMapper.getUserByNameForAuth(loginUserGetParamsDTO);

        if(userDetails==null){
            // TODO 保存到t_frame_user表里
            Map<String,Object> userMap = new HashMap<>();
            userMap.put("userName",userName);
            userMap.put("password",password);
            userMap.put("createBy",userName);
            userMap.put("updateBy",userName);
            userMap.put("status",1);
            userMap.put("defaultRole","User");
            userMap.put("userType",microAppConfig.getAppName());
            userMap.put("userRegistSource",microAppConfig.getAppid());
            userMap.put("sessionKey",sessionKey);
            userInfoMapper.insertIntoUserInfo(userMap);
        }else{
            userDetails.setPassword(password);
            userDetails.setSessionKey(sessionKey);
            userInfoMapper.updateLoginUserById(userDetails);
        }
        userDetails = userInfoService.getUserByNameForAuth(loginUserGetParamsDTO);
        return userDetails;
    }
}
