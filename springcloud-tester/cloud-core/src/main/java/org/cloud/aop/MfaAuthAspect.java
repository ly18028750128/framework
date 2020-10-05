package org.cloud.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.cloud.annotation.MfaAuth;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.MfaConstant;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.feign.service.ICommonServiceFeignClient;
import org.cloud.mybatis.dynamic.DynamicSqlUtil;
import org.cloud.utils.CollectionUtil;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.GoogleAuthenticatorUtil;
import org.cloud.utils.HttpServletUtil;
import org.cloud.vo.DynamicSqlQueryParamsVO;
import org.cloud.vo.FrameUserRefVO;
import org.cloud.vo.JavaBeanResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.List;

import static org.cloud.constant.CoreConstant._GOOGLE_MFA_USER_SECRET_REF_ATTR_NAME;

@Aspect
@Component
@Slf4j
@Order(10)
public class MfaAuthAspect {

    @Value("${spring.application.noGroupName:}")
    private String microName;
    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ICommonServiceFeignClient commonServiceFeignClient;

    @Pointcut("@annotation(org.cloud.annotation.MfaAuth)")
    public void mfaAuth() {
    }

    @Around("mfaAuth()")
    public Object mfaAuthCheck(ProceedingJoinPoint joinPoint) throws Throwable {

        //用的最多通知的签名
        Signature signature = joinPoint.getSignature();
        MethodSignature msg = (MethodSignature) signature;
        Object target = joinPoint.getTarget();
        Method method = target.getClass().getMethod(msg.getName(), msg.getParameterTypes());
        final MfaAuth mfaAuthAnnotation = method.getAnnotation(MfaAuth.class);

        if (CoreConstant.mfaAutoType.GOOGLE.equals(mfaAuthAnnotation.mfaAutoType())) {
            checkGoogleValidCode();
        } else {
            // todo 其它方式暂时不处理
        }


        return joinPoint.proceed();
    }


    /**
     * 校验当前用户的谷歌验证码是否正确
     */
    private void checkGoogleValidCode() throws Exception {
        RequestContext currentRequestContext = RequestContextManager.single().getRequestContext();
        LoginUserDetails user = currentRequestContext.getUser();
        DynamicSqlQueryParamsVO dynamicSqlQueryParamsVO = new DynamicSqlQueryParamsVO();

        dynamicSqlQueryParamsVO.getParams().put("userId", user.getId());
        dynamicSqlQueryParamsVO.getParams().put("attributeName", _GOOGLE_MFA_USER_SECRET_REF_ATTR_NAME);

        final String sqlForQueryUserGoogleKey = "select * from t_frame_user_ref where attribute_name = #{attributeName,jdbcType=VARCHAR} " +
                "and  user_id = #{userId,jdbcType=VARCHAR}";

        List<JavaBeanResultMap<Object>> resultMaps = DynamicSqlUtil.single().listDataBySqlContext(sqlForQueryUserGoogleKey, dynamicSqlQueryParamsVO);

        if (CollectionUtil.single().isEmpty(resultMaps)) {
            // todo 插入谷歌验证属性
            final String googleKey = GoogleAuthenticatorUtil.single().generateSecretKey();
            FrameUserRefVO frameUserRefVO = new FrameUserRefVO();
            frameUserRefVO.setAttributeName(_GOOGLE_MFA_USER_SECRET_REF_ATTR_NAME);
            frameUserRefVO.setUserId(user.getId());
            frameUserRefVO.setAttributeValue(googleKey);
            frameUserRefVO.setCreateBy("admin");
            frameUserRefVO.setUpdateBy("admin");
            frameUserRefVO.setRemark("谷歌验证码");
            commonServiceFeignClient.addUserRef(frameUserRefVO);
            throw new BusinessException(MfaConstant.CORRELATION_YOUR_GOOGLE_KEY.value(), googleKey, HttpStatus.UNAUTHORIZED.value()); //
            // 谷歌key
        }

        final String mfaValue = HttpServletUtil.signle().getHttpServlet().getHeader(MfaConstant.MFA_HEADER_NAME.value());

        throw new BusinessException(MfaConstant.CORRELATION_YOUR_GOOGLE_KEY.value(), HttpStatus.UNAUTHORIZED.value());


    }

}

