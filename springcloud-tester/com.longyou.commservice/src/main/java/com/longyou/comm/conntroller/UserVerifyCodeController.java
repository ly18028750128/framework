package com.longyou.comm.conntroller;

import static com.longyou.comm.service.IValidateCodeGenerateService.VALIDATE_CODE_BEAN_PREFIX;

import com.longyou.comm.service.IValidateCodeGenerateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Map;
import org.cloud.exception.BusinessException;
import org.cloud.utils.SpringContextUtil;
import org.cloud.vo.CommonApiResult;
import org.cloud.vo.ValidateCodeVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/verify")
@Api(value = "验证码接口",tags = "验证码接口")
public class UserVerifyCodeController {


    /**
     * @param generateType 1:图形验证 2:手机验证
     * @return
     */
    @GetMapping("/generate/{generateType}")
    @ApiOperation("获取验证码")
    public CommonApiResult<ValidateCodeVO> generateVerifyCode(
        @ApiParam(value = "验证类型", defaultValue = "1") @PathVariable("generateType") Integer generateType,
        @ApiParam(value = "请求参数", required = false) @RequestParam Map<String, Object> requestParams) throws BusinessException {
        IValidateCodeGenerateService validateCodeGenerateService = SpringContextUtil.getBean(VALIDATE_CODE_BEAN_PREFIX + generateType);
        return CommonApiResult.createSuccessResult(validateCodeGenerateService.generate(requestParams));
    }


}
