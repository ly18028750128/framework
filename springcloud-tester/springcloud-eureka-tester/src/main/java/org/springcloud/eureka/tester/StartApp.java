package org.springcloud.eureka.tester;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class StartApp 
{
    public static void main( String[] args )
    {
        SpringApplicationBuilder builer=new SpringApplicationBuilder(StartApp.class);
        builer.web(true).run(args);
    }
}
