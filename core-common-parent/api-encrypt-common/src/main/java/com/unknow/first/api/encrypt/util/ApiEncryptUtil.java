package com.unknow.first.api.encrypt.util;

import static org.cloud.constant.ApiEncryptConstant.__USER_API_HASH_MAP_KEY;

import com.alibaba.fastjson.JSON;
import com.unknow.first.api.encrypt.ApiEncryptType;
import com.unknow.first.api.encrypt.annotation.ApiEncrypt;
import com.unknow.first.api.encrypt.config.ApiEncryptConfig;
import java.util.List;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.exception.BusinessException;
import org.cloud.utils.AES128Util;
import org.cloud.vo.CommonApiResult;

public final class ApiEncryptUtil {

    private ApiEncryptUtil() {

    }

    /**
     * 加解密方法，可以直接在代码中对数据进行加密操作
     *
     * @param result
     * @param apiEncryptType
     * @throws Exception
     */
    public static void encryptData(CommonApiResult<Object> result, ApiEncryptType apiEncryptType) throws Exception {
        if (ApiEncryptType.BY_SYSTEM.equals(apiEncryptType)) {
            String aesStr = AES128Util.single().encrypt(JSON.toJSONString(result.getData()), ApiEncryptConfig.getApiAesKey(), ApiEncryptConfig.getApiAesIv());
            result.setData(aesStr);
        } else if (ApiEncryptType.BY_USER.equals(apiEncryptType)) {
            Long userId = RequestContextManager.single().getRequestContext().getUser().getId();
            List<String> aesKeyAndIv = RedisUtil.single().hashGet(__USER_API_HASH_MAP_KEY, userId.toString());
            String aesStr = AES128Util.single().encrypt(JSON.toJSONString(result.getData()), aesKeyAndIv.get(0), aesKeyAndIv.get(1));
            result.setData(aesStr);
        } else {
            throw new BusinessException("不支持的api加密方式");
        }
        result.setEncryptType(apiEncryptType.name());
    }
}
