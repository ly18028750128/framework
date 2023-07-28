package org.cloud.dimension.config;

import org.cloud.dimension.mybatis.interceptor.DataDimensionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataDimensionConfig {

    @Bean
    public DataDimensionInterceptor dataDimensionInterceptor(){
        return new DataDimensionInterceptor();
    }
}
