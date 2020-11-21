package org.cloud.config.redisson;

//@Configuration
//@EnableRedisHttpSession

import lombok.Data;
import org.cloud.utils.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(prefix = "system.redisson", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "system.redisson")
@Configuration
@Data
public class RedissonConfig extends Config {

    MasterSlaveServersConfig masterSlaveConfig;

    @Bean
    public RedissonClient RedissonClient() {
        System.setProperty("fastjson.parser.autoTypeSupport", "true");
        MasterSlaveServersConfig masterSlaveServersConfig = this.useMasterSlaveServers();
        BeanUtils.copyProperties(masterSlaveConfig, masterSlaveServersConfig);
        this.setCodec(new FastjsonCodec());
        return Redisson.create(this);
    }

    @Bean
    public RedissonUtil initRedissonUtil(RedissonClient redissonClient) {
        RedissonUtil.single().initRedissonClient(redissonClient);
        return RedissonUtil.single();
    }

}
