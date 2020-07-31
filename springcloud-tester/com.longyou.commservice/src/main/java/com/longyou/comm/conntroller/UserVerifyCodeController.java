package com.longyou.comm.conntroller;

import com.longyou.comm.service.IValidateCodeGenerateService;
import org.cloud.exception.BusinessException;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.ResponseResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user/verify")
public class UserVerifyCodeController {


    /**
     * @param generateType 1:图形验证 2:手机验证
     * @return
     */
    @GetMapping("/generate/{generateType}")
    public ResponseResult generateVerifyCode(@PathVariable("generateType") Integer generateType,
                                             @RequestParam Map requestParams) throws BusinessException {
        IValidateCodeGenerateService validateCodeGenerateService = SpringContextUtil.getBean(IValidateCodeGenerateService.VALIDATE_CODE_BEAN_PREFIX + generateType);
        ResponseResult responseResult = ResponseResult.createSuccessResult();
        responseResult.setData(validateCodeGenerateService.generate(requestParams));
        return responseResult;
    }


}
