package com.taiji.boot.web.config.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Demo RedisCacheProperties
 *
 * @author ydy
 * @date 2020/3/11 17:38
 */
@ConfigurationProperties(prefix = "redis.cache")
@Data
public class RedisCacheProperties {

    private String prefix = "";

    private String host;

    private int port;

    private int timeout;

    private int maxTotal;

    private int maxIdle;

    private int maxWait;

    private String password;
}
