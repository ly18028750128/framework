package org.cloud.config.redisson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.cloud.utils.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Administrator
 */
@Configuration
@Primary
@ConditionalOnProperty(prefix = "system.redisson", name = "enabled", matchIfMissing = true)
public class RedissonConfig extends RedisProperties {

    @Bean
    public RedissonClient redissonClient(@Qualifier("redissonObjectMapper") ObjectMapper objectMapper) {
//        Assert.isTrue(masterSlaveConfig != null || clusterConfig != null, "必须配置一个相关的redis配置，请检查");
        int timeOut = new Long(this.getTimeout().getSeconds() * 1000).intValue();
        Config config = new Config();
        if (this.getSentinel() != null) {
            String[] nodes = this.getSentinel().getNodes().toArray(new String[]{});
            config.useSentinelServers().addSentinelAddress(nodes).setPassword(this.getSentinel().getPassword()).setMasterName(this.getSentinel().getMaster())
                .setDatabase(this.getDatabase()).setTimeout(timeOut);
        } else if (this.getCluster() != null) {
            String[] nodes = this.getCluster().getNodes().toArray(new String[]{});
            config.useClusterServers().addNodeAddress(nodes).setPassword(this.getPassword()).setTimeout(timeOut);
        } else {
            config.useSingleServer().setAddress("redis://" + this.getHost() + ":" + this.getPort()).setPassword(this.getPassword()).setTimeout(timeOut)
                .setDatabase(this.getDatabase());
        }
//        增加了这个后topic侦听不了，请注意获取数据时同进同出
//        System.setProperty("fastjson.parser.autoTypeSupport", "true");
        config.setCodec(new JsonJacksonCodec(objectMapper));
        RedissonClient redissonClient = Redisson.create(config);
        RedissonUtil.single().initRedissonClient(redissonClient);
        return redissonClient;
    }

    @Bean("redissonObjectMapper")
    public ObjectMapper redissonObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        objectMapper.registerModule(javaTimeModule).registerModule(new ParameterNamesModule());
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(simpleModule);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper;
    }

}
