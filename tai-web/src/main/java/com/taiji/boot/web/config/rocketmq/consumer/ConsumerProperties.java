package com.taiji.boot.web.config.rocketmq.consumer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Demo ConsumerProperties
 *
 * @author ydy
 * @date 2020/3/26 10:41
 */
@Data
@ConfigurationProperties(prefix = "rocket.config.consumer")
public class ConsumerProperties {

    private String groupId;

    private Integer consumeThreadMin;

    private Integer consumeThreadMax;

    private Integer consumeMessageBatchMaxSize;
}
