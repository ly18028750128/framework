package org.cloud.config.redisson;

import org.cloud.utils.RedissonUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author Administrator
 */
@Configuration
@Primary
public class RedissonConfig extends RedisProperties {

    @Bean
    public RedissonClient redissonClient() {
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
//        config.setCodec(new JsonJacksonCodec());
        RedissonClient redissonClient = Redisson.create(config);
        RedissonUtil.single().initRedissonClient(redissonClient);
        return redissonClient;
    }

}
