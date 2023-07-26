package com.unknow.first.config;

import com.unknow.first.util.GoogleAuthenticatorUtil;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.SneakyThrows;
import org.cloud.constant.CoreConstant;
import org.cloud.constant.LoginConstants;
import org.cloud.constant.MfaConstant;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.core.redis.RedisUtil;
import org.cloud.entity.LoginUserDetails;
import org.cloud.exception.BusinessException;
import org.cloud.model.TSystemDicItem;
import org.cloud.utils.HttpServletUtil;
import org.cloud.utils.IPUtil;
import org.cloud.utils.SystemDicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@ConfigurationProperties(prefix = "system.mfa")
@Setter
@ConditionalOnProperty(prefix = "system.mfa", name = "enabled", matchIfMissing = true)
public class MfaFilterConfig {

    public final static String __MFA_TOKEN_USER_CACHE_KEY = "system:mfa:user:verify:result:";  // 校验结果
    public final static String __MFA_TOKEN_USER_GOOGLE_SECRET_CACHE_KEY = "system:mfa:user:secret:result:"; // 谷歌key

    private List<String> excludeUri;      // 默认为内部调用的url也可以自己添加
    private CoreConstant.MfaAuthType mfaAuthType = CoreConstant.MfaAuthType.GOOGLE;  //默认为google验证


    @Autowired
    RedisUtil redisUtil;

    @Bean
    public FilterRegistrationBean mfaWebFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(
            new MfaWebFilter(excludeUri.stream().filter(StringUtils::hasLength).collect(Collectors.toList())));
        registration.addUrlPatterns("/*");
        registration.setName("mfaWebFilter");
        registration.setOrder(100);
        return registration;
    }

    class MfaWebFilter extends OncePerRequestFilter {

        private List<String> noMfaCheckUrl;      // 默认为内部调用的url也可以自己添加

        public MfaWebFilter(List<String> noMfaCheckUrl) {
            this.noMfaCheckUrl = noMfaCheckUrl;
        }

        @SneakyThrows
        @Override
        protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
            if (HttpServletUtil.signle().isExcludeUri(httpServletRequest, noMfaCheckUrl)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            // 如果数据字典中关闭了双因子校验，那么不进行双因子校验
            TSystemDicItem dicItem = SystemDicUtil.single().getDicItem("systemConfig", "isMfaVerify");
            if (dicItem != null && StringUtils.hasLength(dicItem.getDicItemValue()) && "false".equals(dicItem.getDicItemValue())) {
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
            try {
                GoogleAuthenticatorUtil.single().verifyCurrentUserBindGoogleKey();
            } catch (BusinessException businessException) {
                HttpServletUtil.signle().handlerBusinessException(businessException, httpServletResponse);
                return;
            }
            final String ipHash = redisUtil.getMd5Key(IPUtil.single().getIpAddress(httpServletRequest));
            Boolean isValidatePass = redisUtil.get(__MFA_TOKEN_USER_CACHE_KEY + user.getId() + ":" + ipHash);
            // 如果规定时间内校验过并且未过期，那么不校验
            if (isValidatePass != null && isValidatePass) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } else {
                BusinessException businessException = new BusinessException(MfaConstant.CORRELATION_GOOGLE_NOT_VERIFY_OR_EXPIRE.value(),
                    MfaConstant.CORRELATION_GOOGLE_NOT_VERIFY_OR_EXPIRE.description(), HttpStatus.BAD_REQUEST.value());
                HttpServletUtil.signle().handlerBusinessException(businessException, httpServletResponse);
            }
        }
    }

}
