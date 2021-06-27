package org.cloud.filter;

import org.cloud.context.RequestContext;
import org.cloud.context.RequestContextManager;
import org.cloud.entity.LoginUserDetails;
import org.cloud.utils.CommonUtil;
import org.cloud.utils.HttpServletUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 安全过滤器，获取当前用户的信息，存储在上下文中。缓存到redis缓存中，通过网关登录后获取信息，这个过滤器只做基础的用户信息的上下文的过滤。 及Url是否需要登录的校验，其它的权限过滤将下发到各个应用中去做处理。因为不同的模块的权限管控是不同的。
 */
public class SecurityFilter extends OncePerRequestFilter {

  //security的鉴权排除列表
  private List<String> excludedAuthPages;

  public SecurityFilter(List<String> excludedAuthPages) {
    this.excludedAuthPages = excludedAuthPages;
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
    final LoginUserDetails user = CommonUtil.single().getLoginUser();
    if (user != null) {
      requestContext.setUser(user);
    }
    RequestContextManager.single().setRequestContext(requestContext);
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
