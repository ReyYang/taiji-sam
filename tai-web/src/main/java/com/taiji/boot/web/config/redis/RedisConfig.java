package com.taiji.boot.web.config.redis;

import com.taiji.boot.biz.middleware.redis.RedisClient;
import com.taiji.boot.common.redis.config.RedisCacheConfig;
import com.taiji.boot.common.redis.factory.CacheInterfaceFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * Demo RedisConfig
 *
 * @author ydy
 * @date 2020/3/11 16:35
 */
@Configuration
@EnableConfigurationProperties({RedisCacheProperties.class})
public class RedisConfig {

    @Resource
    private RedisCacheProperties redisCacheProperties;

    @Bean
    public RedisCacheConfig redisCacheConfig() {
        RedisCacheConfig config = new RedisCacheConfig();
        config.setHost(redisCacheProperties.getHost());
        config.setPort(redisCacheProperties.getPort());
        config.setMaxIdle(redisCacheProperties.getMaxIdle());
        config.setMaxTotal(redisCacheProperties.getMaxTotal());
        config.setMaxWait(redisCacheProperties.getMaxWait());
        config.setTimeout(redisCacheProperties.getTimeout());
        config.setPrefix(redisCacheProperties.getPrefix());
        config.setPassword(redisCacheProperties.getPassword());
        return config;
    }

    @Bean("redisClient")
    public CacheInterfaceFactory redisClient() {
        return new RedisClient(redisCacheConfig());
    }

}
