package com.longyou.comm.config;

import lombok.Data;

@Data
public class MicroAppConfig {
    private String appName; // 微服务应用名称
    private String type;  //登录类型,小程序类型 weixin,taobao
    private String appid; //小程序id
    private String appPassword; // 小程序密钥
}
