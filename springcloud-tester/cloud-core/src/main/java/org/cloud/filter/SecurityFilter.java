package org.cloud.filter;

import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
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
//        LoginUserDetails user = CommonUtil.single().getLoginUser(httpServletRequest);
        LoginUserDetails user = CommonUtil.single().getLoginUser(httpServletRequest);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
