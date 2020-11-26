package org.cloud.config;

import lombok.Setter;
import lombok.SneakyThrows;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.LoginConstants;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.GoogleAuthenticatorUtil;
import org.cloud.utils.HttpServletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "system.mfa")
@Setter
@ConditionalOnProperty(prefix = "system.mfa", name = "enabled", havingValue = "true")
public class MfaFilterConfig {

    public final static String __MFA_TOKEN_USER_CACHE_KEY = "system:mfa:user:verify:result:";  // 校验结果
    public final static String __MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY = "system:mfa:user:secret:result:"; // 谷歌key

    private List<String> excludeUri;      // 默认为内部调用的url也可以自己添加
    private CoreConstant.MfaAuthType mfaAuthType = CoreConstant.MfaAuthType.GOOGLE;  //默认为google验证
    private Long expiredTime = 30 * 60L;  // 用户mfa过期时间，默认30分钟

    @Autowired
    RedisUtil redisUtil;

    @Bean
    public FilterRegistrationBean mfaWebFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new MfaWebFilter());
        registration.addUrlPatterns("/*"); //
        registration.setName("mfaWebFilter");
        registration.setOrder(100);
        return registration;
    }

    class MfaWebFilter extends OncePerRequestFilter {
        @SneakyThrows
        @Override
        protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
                throws ServletException, IOException {
            if (HttpServletUtil.signle().isExcludeUri(httpServletRequest, excludeUri)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            RequestContext currentRequestContext = RequestContextManager.single().getRequestContext();
            LoginUserDetails user = currentRequestContext.getUser();

            // 只有需要登录鉴权的接口并且来源为后台注册的用户才需要校验双因子
            if (user == null || (!LoginConstants.REGIST_SOURCE_BACKGROUND.equals(user.getUserRegistSource()))) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            Boolean isValidatePass = redisUtil.get(__MFA_TOKEN_USER_CACHE_KEY + user.getId());
            // 如果规定时间内校验过并且未过期，那么不校验
            if (isValidatePass != null && isValidatePass) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            try {
                if (CoreConstant.MfaAuthType.GOOGLE.equals(mfaAuthType)) {
                    String googleSecret = GoogleAuthenticatorUtil.single().getCurrentUserVerifyKey();
                    isValidatePass = GoogleAuthenticatorUtil.single().checkGoogleVerifyCode(googleSecret);
                    redisUtil.set(__MFA_TOKEN_USER_CACHE_KEY + user.getId(), isValidatePass, expiredTime);
                } else {
                    // TODO 处理其它方式的双因子验证
                }
            } catch (Exception e) {
                HttpServletUtil.signle().handlerBusinessException(e, httpServletResponse);
                return;
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

}