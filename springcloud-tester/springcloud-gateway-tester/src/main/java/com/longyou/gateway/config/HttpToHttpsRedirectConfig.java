package com.longyou.gateway.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.embedded.netty.NettyReactiveWebServerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@ConditionalOnProperty(name = "server.ssl.enabled", havingValue = "true")
public class HttpToHttpsRedirectConfig {

    @Value("${system_config_consul_instance_port:14321}")
    private int httpPort;
    @Value("${server.port}")
    private int serverPort;

    @PostConstruct
    public void startRedirectServer() {
        NettyReactiveWebServerFactory httpNettyReactiveWebServerFactory = new NettyReactiveWebServerFactory(httpPort);
        httpNettyReactiveWebServerFactory.getWebServer((request, response) -> {
            URI uri = request.getURI();

            if (uri.getPath().endsWith("/monitor/health")) {
                response.setStatusCode(HttpStatus.OK);
            } else {
                URI httpsUri;
                try {
                    httpsUri = new URI("https", uri.getUserInfo(), uri.getHost(), serverPort, uri.getPath(), uri.getQuery(), uri.getFragment());
                } catch (URISyntaxException e) {
                    return Mono.error(e);
                }
                response.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
                response.getHeaders().setLocation(httpsUri);

            }
            return response.setComplete();

        }).start();
    }


}
