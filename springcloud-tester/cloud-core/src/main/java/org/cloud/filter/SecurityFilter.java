package org.cloud.filter;

import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 安全过滤器，获取当前用户的信息，存储在上下文中。缓存到redis缓存中
 */
@WebFilter(urlPatterns = {"/*"}, filterName = "SecurityFilter", description = "权限安全过滤器，用于获取用户等")
public class SecurityFilter extends OncePerRequestFilter {

    //security的鉴权排除列表
    @Value("${spring.security.excludedAuthPages:}")
    private String[] excludedAuthPages;

    @Value("${spring.security.user_url:http://SPRING-GATEWAY/user/info/authentication}")
    private String gatewayUserUrl;//获取网关的获取用户的URL配置

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {

        boolean isExcludeUri = false;

        if (excludedAuthPages != null && excludedAuthPages.length != 0) {
            for (String exclude : excludedAuthPages) {
                final PathMatcher pathMathcer = new AntPathMatcher();
                if (pathMathcer.match(exclude, httpServletRequest.getRequestURI())) {
                    isExcludeUri = true;
                    break;
                }
            }
        }

        if (isExcludeUri) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        HttpHeaders headers = RestTemplateUtil.single().getHttpHeadersFromHttpRequest(httpServletRequest);
        LoginUserDetails user = RestTemplateUtil.single().execute(gatewayUserUrl, HttpMethod.GET, null, headers, LoginUserDetails.class);

        filterChain.doFilter(httpServletRequest, httpServletResponse);


    }
}
