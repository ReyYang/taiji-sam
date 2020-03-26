package com.taiji.boot.web.config.rocketmq.topic;

import com.taiji.boot.common.rocketmq.constant.RocketBaseConstant;
import lombok.Data;

import java.util.List;

/**
 * Demo TopicProperties
 *
 * @author ydy
 * @date 2020/3/26 14:37
 */
@Data
public class TopicProperties {

    private String topic;

    private List<String> tags;

    public void init() {
        RocketBaseConstant.TOPIC_TAG_MAP.put(this.getTopic(), this.getTags());
    }

}
