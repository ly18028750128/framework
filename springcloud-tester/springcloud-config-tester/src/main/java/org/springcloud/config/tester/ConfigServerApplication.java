package org.springcloud.config.tester;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication(
//        exclude = {SecurityAutoConfiguration.class}
)
@EnableConfigServer
public class ConfigServerApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ConfigServerApplication.class, args);
    }

//    @Bean
//    RsaProperties rsaProperties(){
//        return new RsaProperties();
//    }
}
