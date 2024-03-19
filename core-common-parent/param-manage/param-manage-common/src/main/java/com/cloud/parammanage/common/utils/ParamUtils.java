package com.cloud.parammanage.common.utils;

import com.cloud.parammanage.common.constants.ParamConstants;
import com.cloud.parammanage.common.entity.ParamConfig;
import com.cloud.parammanage.common.service.ParamConfigService;
import lombok.SneakyThrows;
import org.cloud.utils.SpringContextUtil;
import org.springframework.util.ObjectUtils;

public class ParamUtils {


    private static ParamConfigService paramConfigService;

    private static ParamConfigService getParamConfigService() {
        if(paramConfigService==null){
            paramConfigService = SpringContextUtil.getBean(ParamConfigService.class);
        }
        return paramConfigService;
    }
    @SneakyThrows
    public static  <T> T getConfigValue(ParamConstants paramConstants) {
        ParamConfig paramConfig =getParamConfigService().get(paramConstants.name());
        String value ;
        if (paramConfig == null|| ObjectUtils.isEmpty(paramConfig.getConfigValue())) {
            value = paramConstants.getDefaultValue();
        }else {
            value = paramConfig.getConfigValue();
        }
        return (T) paramConstants.getCls().getConstructor(String.class).newInstance(value);
    }
}
