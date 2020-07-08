package com.longyou.comm.conntroller;

import org.cloud.core.redis.RedisUtil;
import org.cloud.utils.image.ValidateCodeUtil;
import org.cloud.utils.math.RandomUtils;
import org.cloud.vo.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.cloud.constant.CoreConstant._VALIDATE_CODE_CACHE_KEY_PREFIX;

@RestController
@RequestMapping("/user/verify")
public class UserVerifyCodeController {

    @Autowired
    RedisUtil redisUtil;

    @GetMapping("/image/generate")
    public ResponseResult generateImageCode() {
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        ValidateCodeUtil.ValidateCodeVO validateCodeVO = ValidateCodeUtil.getInstance().getRandomCode();
        final String validateCodeRedisKey = _VALIDATE_CODE_CACHE_KEY_PREFIX + System.currentTimeMillis() + "." + RandomUtils.getInstance().getInt(0, 100000);
        redisUtil.set(validateCodeRedisKey, validateCodeVO.getValue(), 300L);
        validateCodeVO.setRedisKey(validateCodeRedisKey);
        validateCodeVO.setValue(null);
        responseResult.setData(validateCodeVO);
        return responseResult;
    }
}
