package org.cloud.dimension.filter;

import cn.hutool.core.collection.CollectionUtil;
import feign.FeignException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.feign.service.IGatewayFeignClient;
import org.cloud.utils.HttpServletUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 安全过滤器，获取当前用户的信息，存储在上下文中。缓存到redis缓存中，通过网关登录后获取信息，这个过滤器只做基础的用户信息的上下文的过滤。 及Url是否需要登录的校验，其它的权限过滤将下发到各个应用中去做处理。因为不同的模块的权限管控是不同的。
 */
public class SecurityFilter extends OncePerRequestFilter {

    //security的鉴权排除列表
    private List<String> excludedAuthPages;

    private IGatewayFeignClient gatewayFeignClient;

    public SecurityFilter(List<String> excludedAuthPages, IGatewayFeignClient gatewayFeignClient) {
        if(CollectionUtil.isEmpty(excludedAuthPages)){
            this.excludedAuthPages = new ArrayList<>();
        }else {
            this.excludedAuthPages = excludedAuthPages;
        }
        this.excludedAuthPages.add("/inner/**/*");
        this.gatewayFeignClient = gatewayFeignClient;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
        throws ServletException, IOException {
        // 这里只取上下文的信息授权通过 @SystemResource 注解来控制
        RequestContext requestContext = new RequestContext();
        requestContext.setHttpServletRequest(httpServletRequest);
        requestContext.setHttpServletResponse(httpServletResponse);
        boolean isExcludeUri = HttpServletUtil.signle().isExcludeUri(httpServletRequest, excludedAuthPages);
        if (isExcludeUri) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            RequestContextManager.single().setRequestContext(requestContext);
            return;
        }
        // 增加上下文的user的处理
        final LoginUserDetails user = getLoginUser();
        if (user != null) {
            requestContext.setUser(user);
        }
        RequestContextManager.single().setRequestContext(requestContext);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private LoginUserDetails getLoginUser() {
        try {
            return gatewayFeignClient.getAuthentication();
        } catch (HttpClientErrorException.Unauthorized e) {
            logger.info("rest请求无权限");
        } catch (FeignException.Unauthorized e) {
            logger.info("feign请求无权限");
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() != HttpStatus.UNAUTHORIZED.value()) {
                logger.error(e.getMessage(), e);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
