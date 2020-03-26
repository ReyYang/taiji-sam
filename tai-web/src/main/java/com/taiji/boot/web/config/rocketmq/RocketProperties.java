package com.taiji.boot.web.config.rocketmq;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Demo RocketProperties
 *
 * @author ydy
 * @date 2020/3/26 11:50
 */
@Data
@ConfigurationProperties(prefix = "rocket.config")
public class RocketProperties {

    private String nameSvrAddr;
}
