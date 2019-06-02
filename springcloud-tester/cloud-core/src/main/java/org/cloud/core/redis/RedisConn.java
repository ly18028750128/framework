package org.cloud.core.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.redis") // prefix+参数名 对应于配置文件config.properties中的spring.redis.*信息
public class RedisConn
{
    private String host;
    
    private int port;
    
    private int timeout;
    
    private int database;
    
    private String password;
    
    private int maxActive;
    
    private int maxWait;
    
    private int maxIdle;
    
    private int minIdle;
    
    public String getHost()
    {
        return host;
    }
    
    public void setHost(String host)
    {
        this.host = host;
    }
    
    public int getPort()
    {
        return port;
    }
    
    public void setPort(int port)
    {
        this.port = port;
    }
    
    public int getTimeout()
    {
        return timeout;
    }
    
    public void setTimeout(int timeout)
    {
        this.timeout = timeout;
    }
    
    public int getDatabase()
    {
        return database;
    }
    
    public void setDatabase(int database)
    {
        this.database = database;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public int getMaxActive()
    {
        return maxActive;
    }
    
    public void setMaxActive(int maxActive)
    {
        this.maxActive = maxActive;
    }
    
    public int getMaxWait()
    {
        return maxWait;
    }
    
    public void setMaxWait(int maxWait)
    {
        this.maxWait = maxWait;
    }
    
    public int getMaxIdle()
    {
        return maxIdle;
    }
    
    public void setMaxIdle(int maxIdle)
    {
        this.maxIdle = maxIdle;
    }
    
    public int getMinIdle()
    {
        return minIdle;
    }
    
    public void setMinIdle(int minIdle)
    {
        this.minIdle = minIdle;
    }
    
}