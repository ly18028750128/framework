package org.cloud.constant;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;

public interface LoginTypeConstant {

    String _LOGIN_BY_ETH_CHAIN = "LOGIN-BY-ETH-CHAIN";
    String _LOGIN_BY_THIRD_LOGIN = "LOGIN-BY-THIRD-LOGIN";
    String _LOGIN_BY_WEIXIN_MICROAPP = "LOGIN-BY-WEIXIN-MICROAPP";
    String _LOGIN_BY_VIRTUAL_USER = "LOGIN-BY-VIRTUAL-USER";
    String _LOGIN_BY_ADMIN_USER = "LOGIN-BY-ADMIN-USER";

    @AllArgsConstructor
    enum LoginTypeEnum {
        LOGIN_BY_ETH_CHAIN(_LOGIN_BY_ETH_CHAIN, _LOGIN_BY_ETH_CHAIN, "以太坊签名登录", false),
        LOGIN_BY_THIRD_LOGIN(_LOGIN_BY_THIRD_LOGIN, _LOGIN_BY_THIRD_LOGIN, "第三方登录，如其它应用程序登录，会生成一个地址+随机密码的用户", false),
        LOGIN_BY_WEIXIN_MICROAPP(_LOGIN_BY_WEIXIN_MICROAPP, _LOGIN_BY_WEIXIN_MICROAPP, "微信小程序登录，会生成一个openid+密码随机的用户", false),
        LOGIN_BY_VIRTUAL_USER(_LOGIN_BY_VIRTUAL_USER, "virtual", "虚拟用户登录，用于外部的租户的调用不校验密码，只校验用户名和密码", true),
        LOGIN_BY_ADMIN_USER(_LOGIN_BY_ADMIN_USER, "admin", "后台管理用户登录", true),

        ;

        public final String code;
        public final String userType;
        public final String description;
        public final Boolean canCreate; // 是否可以后台通过API创建

        public static LoginTypeEnum forCode(final String code) {
            return Arrays.stream(LoginTypeEnum.values()).filter(item -> item.code.equals(code)).findFirst().orElse(null);
        }

        public static List<DataShow> getShowList(Boolean canCreate) {

            if (canCreate) {
                return Arrays.stream(LoginTypeEnum.values()).filter(item -> item.canCreate).map(item -> new DataShow(item.userType, item.description))
                    .collect(Collectors.toList());
            }

            return Arrays.stream(LoginTypeEnum.values()).map(item -> new DataShow(item.userType, item.description)).collect(Collectors.toList());
        }

        public static List<DataShow> getShowList() {
            return getShowList(false);
        }

    }

    @AllArgsConstructor
    class DataShow {
        public final String userType;
        public final String description;
    }

}
