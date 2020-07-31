package com.longyou.comm.service;

import org.cloud.core.redis.RedisUtil;
import org.cloud.exception.BusinessException;
import org.cloud.utils.image.ValidateCodeUtil;
import org.cloud.utils.math.RandomUtils;
import org.cloud.vo.ValidateCodeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.cloud.constant.CoreConstant._VALIDATE_CODE_CACHE_KEY_PREFIX;

@Service(IValidateCodeGenerateService.VALIDATE_CODE_BEAN_PREFIX + "1")
public class ImageValidateCodeGenerateServiceImpl implements IValidateCodeGenerateService {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public ValidateCodeVO generate(Map requestParams) throws BusinessException {
        ValidateCodeVO validateCodeVO = ValidateCodeUtil.getInstance().getRandomCode();
        final String validateCodeRedisKey = _VALIDATE_CODE_CACHE_KEY_PREFIX + System.currentTimeMillis() + "." + RandomUtils.getInstance().getInt(0, 100000);
        redisUtil.set(validateCodeRedisKey, validateCodeVO.getValue(), 300L);
        validateCodeVO.setVerifyType(1);
        validateCodeVO.setRedisKey(validateCodeRedisKey);
        return validateCodeVO;
    }

    @Override
    public Boolean check(ValidateCodeVO vo) throws BusinessException {
        return null;
    }
}
