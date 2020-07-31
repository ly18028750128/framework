package com.longyou.comm.service;

import org.cloud.exception.BusinessException;
import org.cloud.vo.ValidateCodeVO;

import java.util.Map;

public interface IValidateCodeGenerateService {

    public final static String VALIDATE_CODE_BEAN_PREFIX = "ValidateCodeGenerateService.";

    /**
     * 生成验证码
     *
     * @param requestParams 请求参数，用于传入一些其它参数的地方
     * @return
     * @throws BusinessException
     */
    public ValidateCodeVO generate(Map requestParams) throws BusinessException;

    /**
     * @return
     * @throws BusinessException
     */
    public Boolean check(ValidateCodeVO vo) throws BusinessException;
}
