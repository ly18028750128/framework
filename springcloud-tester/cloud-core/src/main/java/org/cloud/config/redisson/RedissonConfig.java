package org.cloud.config.redisson;

//@Configuration
//@EnableRedisHttpSession

import lombok.Data;
import org.cloud.utils.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.MasterSlaveServersConfig;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@ConditionalOnProperty(prefix = "system.redisson", name = "enabled", havingValue = "true")
@ConfigurationProperties(prefix = "system.redisson")
@Configuration
@Data
public class RedissonConfig extends Config {

    // 支持如下配置，优先级为，1：主从配置 2：集群配置
    MasterSlaveServersConfig masterSlaveConfig; // 主从配置
    ClusterServersConfig clusterConfig; // 集群配置

    @Bean
    public RedissonClient RedissonClient() {
        Assert.isTrue(masterSlaveConfig != null || clusterConfig != null, "必须配置一个相关的redis配置，请检查");
        if (masterSlaveConfig != null) {
            MasterSlaveServersConfig masterSlaveServersConfig = this.useMasterSlaveServers();
            BeanUtils.copyProperties(masterSlaveConfig, masterSlaveServersConfig);
        } else if (clusterConfig != null) {
            ClusterServersConfig clusterServersConfig = this.useClusterServers();
            clusterServersConfig.getNodeAddresses().addAll(clusterConfig.getNodeAddresses());
            BeanUtils.copyProperties(clusterConfig, clusterServersConfig);
        } else {

        }
        System.setProperty("fastjson.parser.autoTypeSupport", "true");
        this.setCodec(new FastjsonCodec());
        return Redisson.create(this);
    }


    @Bean
    public RedissonUtil initRedissonUtil(RedissonClient redissonClient) {
        RedissonUtil.single().initRedissonClient(redissonClient);
        return RedissonUtil.single();
    }

}
