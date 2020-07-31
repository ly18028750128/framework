package com.longyou.comm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sys.config.micro-app")
public class MicroAppConfigList {

    private List<MicroAppConfig> appList = new ArrayList<>();

    public List<MicroAppConfig> getAppList() {
        return appList;
    }

    public void setAppList(List<MicroAppConfig> appList) {
        this.appList = appList;
    }
}
