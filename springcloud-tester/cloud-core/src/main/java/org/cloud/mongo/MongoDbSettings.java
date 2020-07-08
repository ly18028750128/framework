//package org.cloud.mongo;
//
//import com.mongodb.MongoClientSettings;
//import com.mongodb.client.MongoClient;
//import com.mongodb.connection.ClusterSettings;
//import lombok.Setter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.TimeUnit;
//
//@Configuration
//@ConfigurationProperties(prefix = "spring.data.mongodb.options")
//@Setter
//public class MongoDbSettings {
//
//    private Boolean alwaysUseMBeans = false;    //    always-use-m-beans: false
//    private Integer connectTimeout = 10000;    //    connect-timeout: 10000
//    private Integer heartbeatConnectTimeout = 20000;    //    heartbeat-connect-timeout: 20000
//    private Integer heartbeatFrequency = 10000;    //    heartbeat-frequency: 10000
//    private Integer heartbeatSocketTimeout = 20000;    //    heartbeat-socket-timeout: 20000
//    private long localThreshold = 15;    //    local-threshold: 15
//    private Integer maxConnectionIdleTime = 0;    //    max-connection-idel-time: 0
//    private Integer maxConnectionLifeTime = 0;    //    max-connection-life-time: 0
//    private Integer maxConnectionsPerHost = 20;    //    max-connections-per-host: 50
//    private Integer maxWaitTime = 120000;    //    max-wait-time: 120000
//    private Integer minConnectionsPerHost = 20;    //    min-connections-per-host: 20
//    private Integer minHeartbeatFrequency = 500;    //    min-heartbeat-frequency: 500
//    private Integer serverSelectionTimeout = 30000;    //    server-selection-timeout: 30000
//    private Boolean socketKeepAlive = false;    //    socket-keep-alive: false
//    private Integer socketTimeout = 0;    //    socket-timeout: 0
//    private Boolean sslEnabled = false;    //    ssl-enabled: false
//    private Boolean sslInvalidHostNameAllowed = false;    //    ssl-invalid-host-name-allowed: false
//    private Integer threadsAllowedToBlockForConnectionMultiplier = 5;    //    threads-allowed-to-block-for-connection-multiplier: 5
//
//
//    @Bean
//    public ClusterSettings.Builder clusterSettings() {
//        return ClusterSettings
//                .builder()
////                .alwaysUseMBeans(alwaysUseMBeans)
////                .connectTimeout(connectTimeout)
////                .heartbeatConnectTimeout(heartbeatConnectTimeout)
////                .heartbeatFrequency(heartbeatFrequency)
////                .heartbeatSocketTimeout(heartbeatSocketTimeout)
//                .localThreshold(localThreshold, TimeUnit.MILLISECONDS)
////                .maxConnectionIdleTime(maxConnectionIdleTime)
////                .maxConnectionLifeTime(maxConnectionLifeTime)
////                .connectionsPerHost(maxConnectionsPerHost)
////                .maxWaitTime(maxWaitTime)
////                .minConnectionsPerHost(minConnectionsPerHost)
////                .minHeartbeatFrequency(minHeartbeatFrequency)
//                .serverSelectionTimeout(serverSelectionTimeout, TimeUnit.MILLISECONDS);
////                .socketKeepAlive(socketKeepAlive)
////                .socketTimeout(socketTimeout)
////                .sslEnabled(sslEnabled)
////                .sslInvalidHostNameAllowed(sslInvalidHostNameAllowed)
////                .threadsAllowedToBlockForConnectionMultiplier(threadsAllowedToBlockForConnectionMultiplier)
//    }
//
//
//
////    @Bean
////    public MongoClientSettings mongoClientSettings() {
////        MongoClientSettings mongoClientSettings = MongoClientSettings.builder().build();
////
////
////        return mongoClientSettings;
////    }
//
//}