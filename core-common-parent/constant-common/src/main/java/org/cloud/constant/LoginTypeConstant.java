package org.cloud.constant;

import lombok.AllArgsConstructor;

public interface LoginTypeConstant {

    String _LOGIN_BY_ETH_CHAIN = "LOGIN-BY-ETH-CHAIN";
    String _LOGIN_BY_THIRD_LOGIN = "LOGIN-BY-THIRD-LOGIN";
    String _LOGIN_BY_WEIXIN_MICROAPP = "LOGIN-BY-WEIXIN-MICROAPP";

    @AllArgsConstructor
    enum LoginType{
        LOGIN_BY_ETH_CHAIN(_LOGIN_BY_ETH_CHAIN,"以太坊签名登录"),
        LOGIN_BY_THIRD_LOGIN(_LOGIN_BY_THIRD_LOGIN,"第三方登录，如其它应用程序登录，会生成一个地址+随机密码的用户"),
        LOGIN_BY_WEIXIN_MICROAPP(_LOGIN_BY_WEIXIN_MICROAPP,"微信小程序登录，会生成一个openid+密码随机的用户"),

        ;

        public final String code;
        public final String description;
    }
}
