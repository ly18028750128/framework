package org.cloud.utils;


import org.cloud.core.redis.RedisUtil;
import org.cloud.model.TSystemDicItem;
import org.cloud.model.TSystemDicMaster;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.cloud.constant.CoreConstant.*;

/**
 * 从缓存中获取数据字典
 */
public final class SystemDicUtil {

    private SystemDicUtil() {
    }

    RedisUtil redisUtilService;

    private RedisUtil getRedisUtilService() {
        if (redisUtilService == null) {
            redisUtilService = SpringContextUtil.getBean(RedisUtil.class);
        }
        return redisUtilService;
    }


    private final static SystemDicUtil instance = new SystemDicUtil();


    public static SystemDicUtil single() {
        return instance;
    }

    /**
     * 根据dicCode获取数据字典列表
     *
     * @param microServiceName
     * @param dicCode
     * @param language
     * @return
     */
    public List<TSystemDicItem> getDicItemList(String microServiceName, String dicCode, String language) {

        List<TSystemDicItem> result = new ArrayList<>();
        Map<String, List<TSystemDicItem>> itemsMap = getRedisUtilService().hashGet(_SYSTEM_DIC_CACHE_KEY + microServiceName, dicCode + _SYSTEM_DIC_ITEMS_CACHE_KEY_WHIT_DOT);

        if (itemsMap == null) {
            return result;
        }

        return itemsMap.get(language);
    }

    public List<TSystemDicItem> getDicItemList(String microServiceName, String dicCode) {
        return getDicItemList(microServiceName, dicCode, SystemSupportLanguage.ZH_CN.value());
    }

    public List<TSystemDicItem> getDicItemList(String dicCode) {
        return getDicItemList(_GENERAL_SYSDIC_NAME, dicCode, SystemSupportLanguage.ZH_CN.value());
    }

    /**
     * 根据dicitemcode获取数据字典项
     *
     * @param microServiceName
     * @param dicCode
     * @param language
     * @param dicItemCode
     * @return
     */
    public TSystemDicItem getDicItem(String microServiceName, String dicCode, String language, @NotNull String dicItemCode) {
        List<TSystemDicItem> systemDicItems = this.getDicItemList(microServiceName, dicCode, language);
        for (TSystemDicItem item : systemDicItems) {
            if (dicItemCode.equals(item.getDicItemCode())) {
                return item;
            }
        }
        return null;
    }

    public TSystemDicItem getDicItem(String dicCode, String language, @NotNull String dicItemCode) {
        return getDicItem(_GENERAL_SYSDIC_NAME, dicCode, language, dicItemCode);
    }

    public TSystemDicItem getDicItem(String dicCode, @NotNull String dicItemCode) {
        return getDicItem(_GENERAL_SYSDIC_NAME, dicCode, SystemSupportLanguage.ZH_CN.value(), dicItemCode);
    }

    /**
     * 获取数据字典主表的详细信息
     *
     * @param microServiceName
     * @param dicCode
     * @return
     */
    public TSystemDicMaster getDic(String microServiceName, String dicCode) {
        return getRedisUtilService().hashGet(_SYSTEM_DIC_CACHE_KEY + microServiceName, dicCode);
    }

    public TSystemDicMaster getDic(String dicCode) {
        return getRedisUtilService().hashGet(_SYSTEM_DIC_CACHE_KEY + _GENERAL_SYSDIC_NAME, dicCode);
    }
}
