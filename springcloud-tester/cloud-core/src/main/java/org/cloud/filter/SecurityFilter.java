package org.cloud.filter;

import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 安全过滤器，获取当前用户的信息，存储在上下文中。缓存到redis缓存中，通过网关登录后获取信息，这个过滤器只做基础的用户信息的上下文的过滤。
 * 及Url是否需要登录的校验，其它的权限过滤将下发到各个应用中去做处理。因为不同的模块的权限管控是不同的。
 */
@WebFilter(urlPatterns = {"/*"}, filterName = "SecurityFilter", description = "权限安全过滤器，用于获取用户等")
public class SecurityFilter extends OncePerRequestFilter {

    //security的鉴权排除列表
    @Value("${spring.security.excludedAuthPages:}")
    private List<String> excludedAuthPages;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String requestUri = httpServletRequest.getRequestURI();
        boolean isExcludeUri = CommonUtil.single().pathMatch(requestUri, excludedAuthPages);
        RequestContext requestContext = new RequestContext();
        requestContext.setHttpServletRequest(httpServletRequest);
        requestContext.setHttpServletResponse(httpServletResponse);

//        HttpHeaders headers = RestTemplateUtil.single().getHttpHeadersFromHttpRequest(httpServletRequest);
        LoginUserDetails user = CommonUtil.single().getLoginUser(httpServletRequest);

        // 设置上下文的用户信息
        if ( (!isExcludeUri) && (user == null) ) {
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }
        if (user != null) {
            requestContext.setUser(user);
        }

        RequestContextManager.single().setRequestContext(requestContext);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
