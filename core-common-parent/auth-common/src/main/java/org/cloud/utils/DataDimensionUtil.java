package org.cloud.utils;

import static org.cloud.constant.UserDataDimensionConstant.USER_DIMENSION_CACHE_KEY;

import java.util.Map;
import java.util.Set;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.UserDataDimensionConstant;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;

public class DataDimensionUtil {

    private DataDimensionUtil() {

    }

    private final static DataDimensionUtil dataDimensionUtil = new DataDimensionUtil();

    public static DataDimensionUtil single() {
        return dataDimensionUtil;
    }

    private RedisUtil redisUtil;


    public RedisUtil getRedisUtil() {

        if (redisUtil == null) {
            redisUtil = SpringContextUtil.getBean(RedisUtil.class);
        }
        return redisUtil;
    }

    public Map<String, Set<String>> getCurrentUserAllDataDimension() {
        final Long userId = RequestContextManager.single().getRequestContext().getUser().getId();
        return getRedisUtil().hashGet(CoreConstant.USER_LOGIN_SUCCESS_CACHE_KEY + userId, USER_DIMENSION_CACHE_KEY);
    }

    public Set<String> getCurrentUserDataDimensionByName(String DataDimensionName) {
        return getCurrentUserAllDataDimension().get(DataDimensionName);
    }




}
