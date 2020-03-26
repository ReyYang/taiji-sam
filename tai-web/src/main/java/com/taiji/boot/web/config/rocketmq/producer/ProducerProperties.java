package com.taiji.boot.web.config.rocketmq.producer;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Demo ProducerProperties
 *
 * @author ydy
 * @date 2020/3/26 10:49
 */
@Data
@ConfigurationProperties(prefix = "rocket.config.producer")
public class ProducerProperties {

    private String groupId;

    private Integer maxMessageSize;

    private Integer sendMsgTimeoutMillis;

    private Integer retryTimeWhenSendFailed;
}
